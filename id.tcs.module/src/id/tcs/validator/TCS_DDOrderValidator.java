package id.tcs.validator;

import java.math.BigDecimal;

import org.adempiere.base.event.IEventTopics;
import org.compiere.model.MMovement;
import org.compiere.model.MOrderLine;
import org.compiere.model.PO;
import org.compiere.model.Query;
import org.compiere.model.TCS_MDDOrder;
import org.compiere.process.DocAction;
import org.eevolution.model.MDDOrder;
import org.eevolution.model.MDDOrderLine;
import org.osgi.service.event.Event;

public class TCS_DDOrderValidator {

	public static String executeEvent(Event event, PO po) {
		String msg = "";
		TCS_MDDOrder ddOrder = (TCS_MDDOrder) po;

		if (event.getTopic().equals(IEventTopics.DOC_BEFORE_COMPLETE)) {
			msg += checkUsedOrderLineQty(ddOrder);
		} 
		else if (event.getTopic().equals(IEventTopics.DOC_BEFORE_VOID)) {
			msg += checkActiveInOutBound(ddOrder);
			//msg += unReferenceToCOrder(ddOrder);
		} 

		if (event.getTopic().equals(IEventTopics.DOC_BEFORE_REACTIVATE)) {
			msg += validateReactivate(ddOrder);
		} 

		if (!ddOrder.isSOTrx() && event.getTopic().equals(IEventTopics.DOC_BEFORE_COMPLETE)) {
			msg += createInternalSO(ddOrder);

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
					return "Line "+ddLine.getLine()+" : Qty after complete is more than order qty, remaining qty = "+remainingQty;
				}
			}

		}
		return "";
	}
	
	private static String checkActiveInOutBound(TCS_MDDOrder ddOrder){
		String sqlWhere = "DD_Order_ID="+ddOrder.getDD_Order_ID()+" AND DocStatus IN ('CO','CL') AND AD_Client_ID="+ddOrder.getAD_Client_ID();
		boolean match = new Query(ddOrder.getCtx(), MMovement.Table_Name, sqlWhere, ddOrder.get_TrxName())
				.match();
		if (match)
			return "Active Movement Exist";

		return "";
	}
	
	/*
	private static String unReferenceToCOrder(TCS_MDDOrder ddOrder){

		ddOrder.set_ValueOfColumn("C_Order_ID", null);
		ddOrder.saveEx();
		MDDOrderLine [] lines = ddOrder.getLines();
		for (MDDOrderLine line : lines) {
			line.set_ValueOfColumn("C_OrderLine_ID", null);
			line.saveEx();
		}
		return "";
	}
	*/
	
	private static String createInternalSO(TCS_MDDOrder order) {

		TCS_MDDOrder counter = order.createCounterDoc();
		return "";
	}

	private static String validateReactivate(TCS_MDDOrder order) {
		if (order.isSOTrx()) {
			//allow reactivate only if outbound is reversed
			String whereClause = "IsOutbound='Y' AND DD_Order_ID=? AND DocStatus IN ('CO','CL')";
			boolean match = new Query(order.getCtx(), MMovement.Table_Name, whereClause, order.get_TrxName())
					.setParameters(new Object[] {order.get_ID()})
					.match();

			if (match)
				return "Error, please void related outbound first";

		} else {
			//allow reactivate only if internal SO is voided
			if (order.get_ValueAsInt("Ref_InternalOrder_ID")!= 0) {
				TCS_MDDOrder internalSO = new TCS_MDDOrder(order.getCtx(), order.get_ValueAsInt("Ref_InternalOrder_ID"), order.get_TrxName());
				if (!internalSO.getDocStatus().equalsIgnoreCase(DocAction.STATUS_Voided))
					return "Cannot Reactivate Internal PO, Void Related Internal SO First";
			}	
		}
		return "";
	}
}
