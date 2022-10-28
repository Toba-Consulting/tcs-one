package id.tcs.validator;

import java.math.BigDecimal;
import java.util.List;

import org.adempiere.base.event.IEventTopics;
import org.compiere.model.MInOutLine;
import org.compiere.model.MMatchPO;
import org.compiere.model.MOrder;
import org.compiere.model.MOrderLine;
import org.compiere.model.MPayment;
import org.compiere.model.MRequisition;
import org.compiere.model.MRequisitionLine;
import org.compiere.model.PO;
import org.compiere.model.Query;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.osgi.service.event.Event;

import id.tcs.model.X_M_MatchPR;
import id.tcs.model.X_M_MatchQuotation;

public class TCS_OrderValidator {

	
	public static String executeEvent(Event event, PO po) {
		String msg = "";
		MOrder order = (MOrder) po;
		if (event.getTopic().equals(IEventTopics.DOC_AFTER_REACTIVATE)) {
			msg += unreserveQty(order);
		} 
		else if (event.getTopic().equals(IEventTopics.DOC_BEFORE_REACTIVATE)) {
			msg += checkMatchPO(order);
			msg += checkLinkedPayment(order);
			msg += checkActiveLinkedInOut(order);
		} 
		else if (event.getTopic().equals(IEventTopics.DOC_BEFORE_VOID)) {
			msg += checkMatchPO(order);
			msg += checkLinkedPayment(order);
			msg += checkActiveLinkedInOut(order);
			msg += removeMatchQuotation(order);
//		} else  if (event.getTopic().equals(IEventTopics.DOC_BEFORE_COMPLETE)){
//			msg += generateRequisition(order);
		} else  if (event.getTopic().equals(IEventTopics.DOC_BEFORE_REVERSEACCRUAL) || 
				event.getTopic().equals(IEventTopics.DOC_BEFORE_REVERSECORRECT)){
			msg += removeRequisition(order);
		}
		return msg;
	}
	
	private static String generateRequisition(MOrder order) {
		if(!order.isSOTrx())
			updateRequisition(order);
		
		return "";
	}

	private static String updateRequisition(MOrder order){
		String whereClause = "DocStatus!='CO' AND M_Requisition_ID IN (SELECT DISTINCT M_Requisition_ID "
				+ "FROM M_RequisitionLine mrl "
				+ "JOIN C_OrderLine col ON col.M_RequisitionLine_ID=mrl.M_RequisitionLine_ID "
				+ "WHERE col.C_Order_ID=?)";
		
		List<MRequisition> requisitions = new Query(Env.getCtx(), MRequisition.Table_Name, whereClause, null)
								.setParameters(order.getC_Order_ID())
								.setOnlyActiveRecords(true)
								.list();
		
		if (!requisitions.isEmpty()) {
			StringBuilder sb = new StringBuilder();
			for (MRequisition req : requisitions) {
				sb.append(req.getDocumentNo()).append(",");
			}
			sb.deleteCharAt(sb.length()-1);
			return "Abort.. Please check document status for Requisition: ";
			
		}
		
		for (MOrderLine orderLine : order.getLines()) {
			int M_RequisitionLine_ID = orderLine.get_ValueAsInt("M_RequisitionLine_ID");
			if(M_RequisitionLine_ID <= 0)
				continue;
			MRequisitionLine requisitionLine = new MRequisitionLine(Env.getCtx(), M_RequisitionLine_ID, null);	
			System.out.println(M_RequisitionLine_ID);
			System.out.println(requisitionLine.get_Value("QtyRequired"));
			System.out.println(orderLine.getQtyOrdered());
			requisitionLine.set_ValueOfColumn("QtyRequired", ((BigDecimal) requisitionLine.get_Value("QtyRequired")).subtract(orderLine.getQtyOrdered()));
			requisitionLine.set_ValueOfColumn("QtyOrdered", requisitionLine.getQtyOrdered().add(orderLine.getQtyOrdered()));
			requisitionLine.saveEx();
			
			X_M_MatchPR matchPR = new X_M_MatchPR(Env.getCtx(),0,null);
	        matchPR.setC_OrderLine_ID(orderLine.getC_OrderLine_ID());
	        matchPR.setM_Requisition_ID(requisitionLine.getM_Requisition_ID());
	        matchPR.setM_RequisitionLine_ID(requisitionLine.get_ID());
	        matchPR.setC_Order_ID(orderLine.getC_Order_ID());
	        matchPR.setDateTrx(orderLine.getC_Order().getDateOrdered());
	        matchPR.setQtyOrdered(orderLine.getQtyOrdered());
	        matchPR.saveEx();
		}
		
		return "";
	}
	
	private static String removeRequisition(MOrder order){
		if (order.getDocStatus().equals("CO")) {
			for (MOrderLine orderLine : order.getLines()) {
				int M_RequisitionLine_ID = orderLine.get_ValueAsInt("M_RequisitionLine_ID");
				if(M_RequisitionLine_ID <= 0)
					continue;
				MRequisitionLine requisitionLine = new MRequisitionLine(Env.getCtx(), M_RequisitionLine_ID, null);
				requisitionLine.set_ValueOfColumn("QtyRequired", ((BigDecimal) requisitionLine.get_Value("QtyRequired")).add(orderLine.getQtyOrdered()));
				requisitionLine.set_ValueOfColumn("QtyOrdered",requisitionLine.getQtyOrdered().subtract(orderLine.getQtyOrdered()));
				requisitionLine.saveEx();	
			}
			String sqlDelete = "DELETE FROM M_MatchPR WHERE C_Order_ID=?";
			DB.executeUpdate(sqlDelete, order.get_ID(), null);
			
		}
		return "";
		/*//@win: for now we prefer not removing the reference to Requisition Line to preserve audit reference
		if (DocAction.equalsIgnoreCase(DOCACTION_Void)) {
	        String sql = "UPDATE C_OrderLine SET M_RequisitionLine_ID = NULL WHERE C_Order_ID=?";
	        int no = DB.executeUpdate(sql, get_ID(), get_TrxName());
	        log.info("UPDATED Order Line "+no);
		}
		*/	
	}

	public static String unreserveQty(MOrder order){
		MOrderLine [] lines = order.getLines();
		for (MOrderLine line : lines) {
			line.setQtyReserved(Env.ZERO);
			line.saveEx();
		}
		
		return "";
	}
	
	public static String checkMatchPO(MOrder order){
		int [] temp = new Query(order.getCtx(), MOrderLine.Table_Name, "C_Order_ID="+order.getC_Order_ID(), order.get_TrxName())
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
		//match PO tetap ada setelah MR direverse
		String sqlWhere = "M_MatchPO.C_OrderLine_ID IN ("+IDs+") AND mi.DocStatus IN ('CO','CL','IP')";
		boolean match = new Query(order.getCtx(), MMatchPO.Table_Name, sqlWhere, order.get_TrxName())
							.addJoinClause("JOIN M_InOutLine mil on mil.M_InOutline_ID=M_MatchPO.M_InOutLine_ID")
							.addJoinClause("JOIN M_InOut mi on mi.M_InOut_ID=mil.M_InOut_ID")
							.match();
		if (match) {
			return "Cannot Reverse Order : Existing Match PO Exist For Order Line";
		}
		return "";
	}
	
	public static String checkLinkedPayment(MOrder order){
		String sqlWhere="C_Order_ID="+order.getC_Order_ID()+" AND DocStatus IN ('CO','CL','IP')";
		boolean match = new Query(order.getCtx(), MPayment.Table_Name, sqlWhere, order.get_TrxName())
						.match();
		
		if (match) return "Cannot Reactivate / Void : Linked Payment Exist";
		return "";
	}
	
	public static String checkActiveLinkedInOut(MOrder order){
		int [] temp = new Query(order.getCtx(), MOrderLine.Table_Name, "C_Order_ID="+order.getC_Order_ID(), order.get_TrxName())
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
		String sqlWhere = "C_OrderLine_ID IN ("+IDs+") AND mi.DocStatus IN ('CO','CL','IP')";
		boolean match = new Query(order.getCtx(), MInOutLine.Table_Name, sqlWhere, order.get_TrxName())
				.addJoinClause("JOIN M_InOut mi on mi.M_InOut_ID=M_InOutLine.M_InOut_ID")
				.match();
		if (match) {
			return "Cannot Reverse Order : Active InOut Exists For Order Line";
		}
		return "";
}
	private static String removeMatchQuotation(MOrder order) {
		StringBuilder sql = new StringBuilder("DELETE FROM ").append(X_M_MatchQuotation.Table_Name)
				.append(" WHERE C_Order_ID=?");
		DB.executeUpdateEx(sql.toString(), new Object[] {order.get_ID()}, order.get_TrxName());

		return "";
	}

}
