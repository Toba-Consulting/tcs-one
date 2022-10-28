package id.tcs.validator;

import java.math.BigDecimal;

import org.adempiere.base.event.IEventTopics;
import org.adempiere.exceptions.AdempiereException;
import org.compiere.model.MMovement;
import org.compiere.model.MMovementLine;
import org.compiere.model.MOrderLine;
import org.compiere.model.PO;
import org.compiere.model.Query;
import org.compiere.util.Env;
import org.eevolution.model.MDDOrder;
import org.eevolution.model.MDDOrderLine;
import org.osgi.service.event.Event;

public class TCS_MovementValidator {

	public static String executeEvent(Event event, PO po) {
		String msg = "";
		MMovement move = (MMovement) po;
		if (event.getTopic().equals(IEventTopics.DOC_BEFORE_COMPLETE)) {
			msg += checkUsedDDOrderLineQty(move);
			msg += updateQtyOutBoundInBound(move);
		} 
		else if (event.getTopic().equals(IEventTopics.DOC_BEFORE_REVERSEACCRUAL) ||
				event.getTopic().equals(IEventTopics.DOC_BEFORE_REVERSECORRECT)) {
			msg += checkOutboundHasNoActiveInbound(move);
		}
		return msg;
	}
	
	private static String updateQtyOutBoundInBound(MMovement move) {
		MMovementLine [] moveLines = move.getLines(true);
		for(MMovementLine line : moveLines) {
			MDDOrderLine DDOLine = new MDDOrderLine(move.getCtx(), line.getDD_OrderLine_ID(), move.get_TrxName());
			if(move.get_ValueAsBoolean("isOutBound") == true) {
				if(DDOLine.getM_Product().equals(line.getM_Product_ID())) {
					BigDecimal currQtyOutBound = (BigDecimal) DDOLine.get_Value("qtyoutbound");

					DDOLine.set_ValueOfColumn("qtyoutbound", currQtyOutBound.add(line.getMovementQty()));				
				}
			}
			else if(move.get_ValueAsBoolean("isInBound") == true) {
				if(DDOLine.getM_Product().equals(line.getM_Product_ID())) {
					BigDecimal currQtyInBound = (BigDecimal) DDOLine.get_Value("qtyinbound");
					
					DDOLine.set_ValueOfColumn("qtyinbound", currQtyInBound.add(line.getMovementQty()));									
				}
			}
		}
		return "";
	}

	//If M_MovementLine.DD_OrderLine_ID is not null
	//check new (M_MovementLine + SUM QtyEntered FROM M_MovementLine BY DD_OrderLine_ID) <= DD_OrderLine.QtyEntered
	private static String checkUsedDDOrderLineQty(MMovement move){
		
		MMovementLine [] moveLines = move.getLines(true);
		for (MMovementLine moveLine : moveLines) {

			int DD_OrderLine_ID = moveLine.get_ValueAsInt("DD_OrderLine_ID");
			if (DD_OrderLine_ID != 0) {

				MDDOrderLine ddLine = new MDDOrderLine(move.getCtx(), DD_OrderLine_ID, move.get_TrxName());
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
					return "Line "+moveLine.getLine()+" : Qty after complete is more than order qty, reamining qty = "+remainingQty+". ";
				}
			}

		}
		return "";
	}
	
	private static String checkOutboundHasNoActiveInbound(MMovement move){

		if (move.get_ValueAsBoolean("IsOutbound")) {

			//Get Line IDs for next query
			String sqlWhereIDs = "M_Movement_ID="+move.getM_Movement_ID();
			int [] outBoundLineIDs = new Query(move.getCtx(), MMovementLine.Table_Name, sqlWhereIDs, move.get_TrxName())
			.getIDs();

			String lineIDs = "";
			for (int i : outBoundLineIDs) {
				lineIDs += i;
				lineIDs += ", ";
			}
			lineIDs = lineIDs.substring(0, lineIDs.length()-2);

			String sqlWhereMatch = "M_OutBoundLine_ID IN ("+lineIDs+") AND IsInBound='Y' AND DocStatus IN ('CO','CL')";
			boolean match = new Query(move.getCtx(), MMovementLine.Table_Name, sqlWhereMatch, move.get_TrxName())
			.addJoinClause("JOIN M_Movement ON M_Movement.M_Movement_ID = M_MovementLine.M_Movement_ID")
			.match();
			if (match) {
				return "Active Inbound Referencing This Outbound Exist. ";
			}
		}

		return "";
	}
}
