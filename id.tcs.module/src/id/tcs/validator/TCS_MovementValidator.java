package id.tcs.validator;

import java.math.BigDecimal;

import org.adempiere.base.event.IEventTopics;
import org.compiere.model.MMovement;
import org.compiere.model.MMovementLine;
import org.compiere.model.MOrderLine;
import org.compiere.model.PO;
import org.compiere.model.Query;
import org.eevolution.model.MDDOrder;
import org.eevolution.model.MDDOrderLine;
import org.osgi.service.event.Event;

public class TCS_MovementValidator {

	public static String executeEvent(Event event, PO po) {
		String msg = "";
		MMovement ddOrder = (MMovement) po;
		if (event.getTopic().equals(IEventTopics.DOC_BEFORE_COMPLETE)) {
			msg = checkUsedDDOrderLineQty(ddOrder);
		} 
		return msg;
	}
	
	//If M_MovementLine.DD_OrderLine_ID is not null
	//check new (M_MovementLine + SUM QtyEntered FROM M_MovementLine BY DD_OrderLine_ID) <= DD_OrderLine.QtyEntered
	private static String checkUsedDDOrderLineQty(MMovement move){
		
		MMovementLine [] moveLines = move.getLines(true);
		for (MMovementLine moveLine : moveLines) {

			int C_OrderLine_ID = moveLine.get_ValueAsInt("C_OrderLine_ID");
			if (C_OrderLine_ID != 0) {

				MDDOrderLine ddLine = new MDDOrderLine(move.getCtx(), C_OrderLine_ID, move.get_TrxName());
				BigDecimal lineQty = moveLine.getMovementQty();

				String sqlSumUsedQty = "DD_OrderLine_ID="+moveLine.getDD_OrderLine_ID()+" AND mm.DocStatus IN ('CO','CL') ";
				if (move.get_ValueAsBoolean("IsOutbound")) {
					sqlSumUsedQty += " AND IsOutbound='Y'";
				}
				if (move.get_ValueAsBoolean("IsInbound")) {
					sqlSumUsedQty += " AND IsInbound='Y'";
				}
				
				BigDecimal UsedQty = new Query(move.getCtx(), MMovementLine.Table_Name, sqlSumUsedQty, move.get_TrxName())
										.addJoinClause("JOIN M_Movement mm on mm.M_Movement_ID=M_MovementLine.M_Movement_ID ")
										.sum("MovementQty");

				BigDecimal remainingQty = ddLine.getQtyEntered().subtract(UsedQty);
				if (remainingQty.compareTo(lineQty)<0) {
					return "Line "+moveLine.getLine()+" : Qty after complete is more than order qty, reamining qty = "+remainingQty;
				}
			}

		}
		return "";
	}
}
