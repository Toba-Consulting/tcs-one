package id.tcs.validator;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.adempiere.base.event.IEventTopics;
import org.adempiere.exceptions.AdempiereException;
import org.compiere.model.MInOutLine;
import org.compiere.model.MMatchPO;
import org.compiere.model.MOrderLine;
import org.compiere.model.MRequisition;
import org.compiere.model.MRequisitionLine;
import org.compiere.model.MUOM;
import org.compiere.model.MUOMConversion;
import org.compiere.model.PO;
import org.compiere.model.Query;
import org.compiere.model.TCS_MOrder;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.osgi.service.event.Event;

import id.tcs.model.MMatchPR;

public class TCS_RequisitionValidator {

	public static String executeEvent(Event event, PO po){
		String msg = "";
		MRequisition req = (MRequisition) po;

		if ((event.getTopic().equals(IEventTopics.DOC_BEFORE_REVERSEACCRUAL)) ||
				(event.getTopic().equals(IEventTopics.DOC_BEFORE_REVERSECORRECT))) {
			msg = checkPO(req);
		}
		else if (event.getTopic().equals(IEventTopics.DOC_BEFORE_REACTIVATE)) {
			//Check MatchPR table for any link to M_RequisitionLine that have the Docstatus of 'CO','CL','IP','DR'
			msg += checkMatchPR(req);
			//			msg += checkLinkedPayment(order);
			//Check C_Order table for any link to M_RequisitionLine that have the Docstatus of 'CO','CL','IP','DR'
			msg += checkActiveLinkedOrder(req);
		} 
		else if (event.getTopic().equals(IEventTopics.DOC_BEFORE_VOID)) {
			//Check MatchPR table for any link to M_RequisitionLine that have the Docstatus of 'CO','CL','IP','DR'
			msg += checkMatchPR(req);
			//			msg += checkLinkedPayment(order);
			//Check C_Order table for any link to M_RequisitionLine that have the Docstatus of 'CO','CL','IP','DR'
			msg += checkActiveLinkedOrder(req);
		} 

		else if ((event.getTopic().equals(IEventTopics.DOC_AFTER_PREPARE))) {
			//check and adjust qtyrequired for all requisition line if required
			msg += setQtyRequiredRequisitionLine(req);
		}

		else if ((event.getTopic().equals(IEventTopics.DOC_AFTER_CLOSE))) {
			//Fix Qty in Requisition Line - it's err after close code in core
			msg += fixQtyRequisitionLine(req);
		}
		

		else if ((event.getTopic().equals(IEventTopics.DOC_AFTER_VOID))) {
			msg += updateReferences(req);
		}
		return msg;
	}

	public static String checkActiveLinkedOrder(MRequisition req) {
		int [] temp = new Query(req.getCtx(), MRequisitionLine.Table_Name, "M_Requisition_ID="+req.getM_Requisition_ID(), req.get_TrxName())
				.getIDs();
		if (temp==null || temp.length==0) {
			return "";
		}
		String IDs = "";
		for (int i : temp) {
			IDs+=i;
			IDs+=", ";
		}
		IDs=IDs.substring(0, IDs.length()-2);
		String sqlWhere = "M_RequisitionLine_ID IN ("+IDs+") AND co.DocStatus IN ('CO','CL','IP','DR')";
		boolean match = new Query(req.getCtx(), MOrderLine.Table_Name, sqlWhere, req.get_TrxName())
				.addJoinClause("JOIN C_Order co on co.C_Order_ID=C_OrderLine.C_Order_ID")
				.match();
		if (match) {
			throw new AdempiereException("Cannot Reactivate Requisition : Active Order Exists For Requisition Line");
		}
		return "";
	}

	public static String checkMatchPR(MRequisition req) {
		int [] temp = new Query(req.getCtx(), MRequisitionLine.Table_Name, "M_Requisition_ID="+req.getM_Requisition_ID(), req.get_TrxName())
				.getIDs();
		if (temp==null || temp.length==0) {
			return "";
		}
		String IDs = "";
		for (int i : temp) {
			IDs+=i;
			IDs+=", ";
		}
		IDs=IDs.substring(0, IDs.length()-2);
		String sqlWhere = "M_MatchPR.M_RequisitionLine_ID IN ("+IDs+") AND co.DocStatus IN ('CO','CL','IP','DR')";
		boolean match = new Query(req.getCtx(), MMatchPR.Table_Name, sqlWhere, req.get_TrxName())
				.addJoinClause("JOIN C_OrderLine col on col.C_OrderLine_ID=M_MatchPR.C_OrderLine_ID")
				.addJoinClause("JOIN C_Order co on co.C_Order_ID=col.C_Order_ID")
				.match();
		if (match) {
			throw new AdempiereException("Cannot Reverse Requisition: Existing Match PR Exists For Requisition Line");
		}
		return "";
	}

	private static String updateReferences(MRequisition req) {
		String sqlUpdate = "update m_replenishment set m_requisition_id = null where m_requisition_id =" +req.getM_Requisition_ID();
		DB.executeUpdate(sqlUpdate, req.get_TrxName());
		
		String sqlUpdateReqLine = "update m_requisitionline set qtyentered = 0 where m_requisition_id =" +req.getM_Requisition_ID();
		DB.executeUpdate(sqlUpdateReqLine, req.get_TrxName());
		
		
		req.setProcessed(true);
		req.saveEx();
		
		return "";
	}

	private static String fixQtyRequisitionLine(MRequisition req) {
		//		Close Not delivered Qty
		MRequisitionLine[] lines = req.getLines();
		BigDecimal totalLines = Env.ZERO;
		for (MRequisitionLine line : lines)
		{
			BigDecimal QtyOrdered = line.get_Value("QtyOrdered") != null ? (BigDecimal)line.get_Value("QtyOrdered") : BigDecimal.ZERO;				
			BigDecimal QtyEntered = line.get_Value("QtyEntered") != null ? (BigDecimal)line.get_Value("QtyEntered") : BigDecimal.ZERO;
			BigDecimal Qty = MUOMConversion.convertProductFrom (req.getCtx(), line.getM_Product_ID(),
					line.getC_UOM_ID(), QtyEntered);

			BigDecimal finalQty = QtyOrdered.compareTo(BigDecimal.ZERO) > 0 ? QtyOrdered : BigDecimal.ZERO;

			//	final qty is not line qty
			if (finalQty.compareTo(line.getQty()) != 0)
			{
				String description = line.getDescription();
				if (description == null)
					description = "";
				description += " [" + Qty + "]"; 
				line.setDescription(description);
				line.setQty(finalQty);
				line.setLineNetAmt();
				line.saveEx();
			}
			totalLines = totalLines.add (line.getLineNetAmt());
		}
		if (totalLines.compareTo(req.getTotalLines()) != 0)
		{
			req.setTotalLines(totalLines);
			req.saveEx();
		}
		return "";
	}

	private static String setQtyRequiredRequisitionLine(MRequisition req) {

		MRequisitionLine[] lines = req.getLines();

		for (MRequisitionLine line : lines) {

			BigDecimal QtyEntered = line.get_Value("QtyEntered") != null ? (BigDecimal)line.get_Value("QtyEntered") : BigDecimal.ZERO;
			BigDecimal Qty = line.getQty();
			int C_UOM_ID = line.getC_UOM_ID();
			boolean needSave = false;

			BigDecimal QtyEntered1 = QtyEntered.setScale(MUOM.getPrecision(req.getCtx(), C_UOM_ID), RoundingMode.HALF_UP);
			if (QtyEntered.compareTo(QtyEntered1) != 0) {
				QtyEntered = QtyEntered1;
				line.set_CustomColumn("QtyEntered", QtyEntered1);

				needSave = true;
			}

			BigDecimal Qty1 = MUOMConversion.convertProductFrom (req.getCtx(), line.getM_Product_ID(),
					C_UOM_ID, QtyEntered);
			if (Qty.compareTo(Qty1) != 0) {
				Qty = Qty1;
				line.setQty(Qty);
				needSave = true;
			}

			if (needSave)
				line.saveEx();
		}

		return "";

	}

	public static String checkPO(MRequisition req){

		boolean match = false;
		String sqlWhere = "M_Requisition.M_Requisition_ID="+req.getM_Requisition_ID()+" AND co.DocStatus IN ('CO','CL','DR') ";
		match = new Query(req.getCtx(), MRequisition.Table_Name, sqlWhere, req.get_TrxName())
				.addJoinClause("JOIN M_RequisitionLine rl on rl.M_Requisition_ID=M_Requisition.M_Requisition_ID ")
				.addJoinClause("JOIN C_OrderLine col on col.M_RequisitionLine_ID=rl.M_RequisitionLine_ID ")
				.addJoinClause("JOIN C_Order co on co.C_Order_ID=col.C_Order_ID AND co.IsSOTrx='N' ")
				.match();

		if (match) 
			return "Active Purchase Order Referencing This Requisition Exist";

		return "";
	}

}
