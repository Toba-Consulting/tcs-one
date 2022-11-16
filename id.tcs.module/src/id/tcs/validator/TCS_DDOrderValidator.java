package id.tcs.validator;

import java.math.BigDecimal;

import org.adempiere.base.event.IEventTopics;
import org.compiere.model.MBankStatement;
import org.compiere.model.MMovement;
import org.compiere.model.MOrderLine;
import org.compiere.model.PO;
import org.compiere.model.Query;
import org.eevolution.model.MDDOrder;
import org.eevolution.model.MDDOrderLine;
import org.osgi.service.event.Event;

public class TCS_DDOrderValidator {

	public static String executeEvent(Event event, PO po) {
		String msg = "";
		MDDOrder ddOrder = (MDDOrder) po;
		if (event.getTopic().equals(IEventTopics.DOC_BEFORE_COMPLETE)) {
			msg += checkUsedOrderLineQty(ddOrder);
		} 
		else if (event.getTopic().equals(IEventTopics.DOC_BEFORE_VOID)) {
			msg += checkActiveInOutBound(ddOrder);
			msg += unReferenceToCOrder(ddOrder);
		} 
		else if (event.getTopic().equals(IEventTopics.DOC_BEFORE_REACTIVATE)) {
			msg += checkActiveInOutBound(ddOrder);
		} 
		return msg;
	}
	
	//If DD_OrderLine.C_OrderLine_ID is not null
	//check new (DD_OrderLine + SUM QtyEntered FROM DD_OrderLine BY C_OrderLine_ID) <= C_OrderLine.QtyEntered
	private static String checkUsedOrderLineQty(MDDOrder ddOrder){
		
		MDDOrderLine [] ddLines = ddOrder.getLines();
		for (MDDOrderLine ddLine : ddLines) {

			int C_OrderLine_ID = ddLine.get_ValueAsInt("C_OrderLine_ID");
			if (C_OrderLine_ID != 0) {

				MOrderLine oLine = new MOrderLine(ddOrder.getCtx(), C_OrderLine_ID, ddOrder.get_TrxName());
				BigDecimal lineQty = ddLine.getQtyEntered();

				String sqlSumUsedQty = "C_OrderLine_ID="+ddLine.get_ValueAsInt("C_OrderLine_ID")+" AND dd.DocStatus IN ('CO','CL')";
				BigDecimal UsedQty = new Query(ddOrder.getCtx(), MDDOrderLine.Table_Name, sqlSumUsedQty, ddOrder.get_TrxName())
										.addJoinClause("JOIN DD_Order dd on dd.DD_Order_ID=DD_OrderLine.DD_Order_ID ")
										.sum("QtyEntered");

				BigDecimal remainingQty = oLine.getQtyEntered().subtract(UsedQty);
				if (remainingQty.compareTo(lineQty)<0) {
					return "Line "+ddLine.getLine()+" : Qty after complete is more than order qty, reamining qty = "+remainingQty;
				}
			}

		}
		return "";
	}
	
	private static String checkActiveInOutBound(MDDOrder ddOrder){
		String sqlWhere = "DD_Order_ID="+ddOrder.getDD_Order_ID()+" AND DocStatus IN ('CO','CL') AND AD_Client_ID="+ddOrder.getAD_Client_ID();
		boolean match = new Query(ddOrder.getCtx(), MMovement.Table_Name, sqlWhere, ddOrder.get_TrxName())
						.match();
		if (match)
			return "Active Movement Exist";
		
		return "";
	}

	private static String unReferenceToCOrder(MDDOrder ddOrder){
		
		ddOrder.set_ValueOfColumn("C_Order_ID", null);
		ddOrder.saveEx();
		MDDOrderLine [] lines = ddOrder.getLines();
		for (MDDOrderLine line : lines) {
			line.set_ValueOfColumn("C_OrderLine_ID", null);
			line.saveEx();
		}
		return "";
	}
}
