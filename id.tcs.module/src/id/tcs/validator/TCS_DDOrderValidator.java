package id.tcs.validator;

import java.math.BigDecimal;

import org.adempiere.base.event.IEventTopics;
import org.compiere.model.MBankStatement;
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
			msg = checkUsedOrderLineQty(ddOrder);
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
}
