package id.tcs.validator;

import java.math.BigDecimal;
import java.sql.Timestamp;

import org.adempiere.base.event.IEventTopics;
import org.adempiere.exceptions.AdempiereException;
import org.adempiere.exceptions.NegativeInventoryDisallowedException;
import org.compiere.model.MInOutLine;
import org.compiere.model.MInvoice;
import org.compiere.model.MLocator;
import org.compiere.model.MMovement;
import org.compiere.model.MMovementLine;
import org.compiere.model.MMovementLineMA;
import org.compiere.model.MStorageOnHand;
import org.compiere.model.PO;
import org.compiere.model.Query;
import org.compiere.model.TCS_MDDOrder;
import org.compiere.model.TCS_MOrder;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Util;
import org.eevolution.model.MDDOrderLine;
import org.osgi.service.event.Event;

public class TCS_MovementValidator {

	public static String executeEvent(Event event, PO po) {
		String msg = "";
		MMovement move = (MMovement) po;
		
		if (event.getTopic().equals(IEventTopics.DOC_BEFORE_COMPLETE)) {
			msg += checkUsedDDOrderLineQty(move);
			msg += updateQtyOutBoundInBound(move);
//			msg += checkQtyOnhand(move);
		} 
		else if (event.getTopic().equals(IEventTopics.DOC_BEFORE_REVERSEACCRUAL) ||
				event.getTopic().equals(IEventTopics.DOC_BEFORE_REVERSECORRECT)) {
			msg += checkOutboundHasNoActiveInbound(move);
		}
		else if (event.getTopic().equals(IEventTopics.DOC_AFTER_REVERSEACCRUAL) ||
				event.getTopic().equals(IEventTopics.DOC_AFTER_REVERSECORRECT)) {
			msg += updateReferences(move);
		}
		if (event.getTopic().equals(IEventTopics.DOC_BEFORE_REVERSEACCRUAL) 
				|| event.getTopic().equals(IEventTopics.DOC_BEFORE_REVERSECORRECT)) {
			msg += beforeReverse(move);
		}
		
		return msg;
	}
	
	private static String updateReferences(MMovement move) {	
		TCS_MDDOrder ddorder = new TCS_MDDOrder(move.getCtx(), move.getDD_Order_ID(), move.get_TrxName());
	
		String sqlUpdateMovement = "UPDATE M_Movement set dd_order_id = null where m_movement_id = ?";
		String sqlUpdateMovementLine = "UPDATE M_MovementLine set M_OutboundLine_ID=null, M_InternalPOLine_ID = null, dd_orderline_id = null where m_movement_id = ?";
		
		// Update Movement
		DB.executeUpdate(sqlUpdateMovement, move.getM_Movement_ID(), move.get_TrxName());
		// Update Movement Line
		DB.executeUpdate(sqlUpdateMovementLine, move.getM_Movement_ID(), move.get_TrxName());
		
		ddorder.setDescription(ddorder.getDocumentNo());
		ddorder.saveEx();
		
		return "";
	}
	
	private static String checkQtyOnhand(MMovement move) {
		MMovementLine lines[] = move.getLines(true);
		for(MMovementLine line : lines) {
//			MMovementLineMA mas[] = MMovementLineMA.get(line.getCtx(),
//					line.getM_MovementLine_ID(), line.get_TrxName());
//			for (int j = 0; j < mas.length; j++)
//			{
//				MMovementLineMA ma = mas[j];
				//
				MLocator locator = new MLocator (line.getCtx(), line.getM_Locator_ID(), line.get_TrxName());
				Timestamp dateMPolicy = move.getMovementDate();
				if (dateMPolicy != null)
					dateMPolicy = Util.removeTime(dateMPolicy);

				//	Get Storage
				MStorageOnHand storage = MStorageOnHand.getCreate (line.getCtx(), locator.getM_Locator_ID(), line.getM_Product_ID(), 0, dateMPolicy, line.get_TrxName(), true, 120);
				BigDecimal newOnhandQty = storage.getQtyOnHand().subtract(line.getMovementQty()); 
				if(newOnhandQty.compareTo(Env.ZERO) <= 0) {
					throw new AdempiereException("Negative Inventory for Product: " + storage.getM_Product().getName());
				}
//			}
		}
			
		return "";
	}

	private static String beforeReverse(MMovement movement) {
		if(movement.get_ValueAsBoolean("IsInbound"))
		{
			MMovementLine [] lines = movement.getLines(true);
			
			for(MMovementLine line : lines) {
				String sqlQtyDeliv = "Select qtydelivered from dd_orderline where dd_orderline_id = " + line.get_Value("dd_orderline_id"); //internal po
				String sqlQtyDeliv2 = "Select qtydelivered from M_MovementLine where m_movementline_id = " + line.get_Value("m_outboundline_id"); // inbound
				BigDecimal qtyDeliv = DB.getSQLValueBD(null, sqlQtyDeliv);
				BigDecimal qtyDeliv2 = DB.getSQLValueBD(null, sqlQtyDeliv2);

				BigDecimal newQtyDelivered =  qtyDeliv.subtract((BigDecimal) line.get_Value("QtyEntered"));
				BigDecimal newQtyDelivered2 =  qtyDeliv2.subtract((BigDecimal) line.get_Value("QtyEntered"));
				String sql = "UPDATE DD_OrderLine SET QtyDelivered = " + newQtyDelivered + " WHERE dd_orderline_id = " + line.get_Value("dd_orderline_id");
				String sqlMovement = "UPDATE M_MovementLine SET QtyDelivered = " + newQtyDelivered2 + " WHERE m_movementline_id = " + line.get_ValueAsInt("m_outboundline_id");
				DB.executeUpdateEx(sql, null);
				DB.executeUpdateEx(sqlMovement, null);
			}
		}
		if(movement.get_ValueAsBoolean("IsOutbound"))
		{
			MMovementLine [] lines = movement.getLines(true);
			
			for(MMovementLine line : lines) {
				String sqlQtyDeliv2 = "Select qtydelivered from M_MovementLine where m_movementline_id = " + line.getM_MovementLine_ID(); // outbound
				BigDecimal qtyDeliv2 = DB.getSQLValueBD(null, sqlQtyDeliv2);
				
				BigDecimal newQtyDelivered =  line.getDD_OrderLine().getQtyDelivered().subtract((BigDecimal) line.get_Value("QtyEntered"));
				BigDecimal newQtyDelivered2 =  qtyDeliv2.subtract((BigDecimal) line.get_Value("QtyEntered"));
				String sql = "UPDATE DD_OrderLine SET QtyDelivered = " + newQtyDelivered + " WHERE DD_OrderLine_ID = " + line.getDD_OrderLine_ID();
				String sqlMovement = "UPDATE M_MovementLine SET QtyDelivered = " + newQtyDelivered2 + " WHERE m_movementline_id = " + line.getM_MovementLine_ID();
				DB.executeUpdateEx(sql, null);
				DB.executeUpdateEx(sqlMovement, null);
			}
		}
		
		return "";
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
										.sum("QtyDelivered");

				BigDecimal remainingQty = ddLine.getQtyEntered().subtract(UsedQty);

				if (remainingQty.compareTo(ddLine.getQtyEntered())<0) {
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
