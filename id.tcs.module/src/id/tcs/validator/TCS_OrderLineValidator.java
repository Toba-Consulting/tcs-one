package id.tcs.validator;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;

import org.adempiere.base.event.IEventTopics;
import org.compiere.model.MOrderLine;
import org.compiere.model.MPriceList;
import org.compiere.model.MRequisitionLine;
import org.compiere.model.PO;
import org.compiere.model.Query;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Trx;
import org.osgi.service.event.Event;

import id.tcs.model.MMatchPR;
import id.tcs.model.X_M_MatchPR;
import id.tcs.model.X_M_MatchQuotation;

public class TCS_OrderLineValidator {
	public static String executeEvent(Event event, PO po) {
		String msg = "";
		MOrderLine orderLine = (MOrderLine) po;

		if (event.getTopic().equals(IEventTopics.PO_BEFORE_DELETE)) {
			if (orderLine.getC_Order().isSOTrx()) {
				msg += removeMatchQuotation(orderLine);
			} else {
				// Update Qty Ordered on Requisition Line first before delete match pr
				msg += updateQtyOrderedRequisition(orderLine);
				//Delete related Match PR before delete PO Line
				msg += removeMatchPR(orderLine);
			}
			msg += updateLineReferences(orderLine);
			
		}
		else if (event.getTopic().equals(IEventTopics.PO_BEFORE_CHANGE)) {
			if (!orderLine.getC_Order().isSOTrx()) {
				msg += ValidateMatchPR(orderLine);
			}
		}

//		else if ((event.getTopic().equals(IEventTopics.PO_AFTER_NEW))) {
//			if (!orderLine.getC_Order().isSOTrx()) {
//				msg += createMatchPR(orderLine);
//			}
//			else {
//				if (orderLine.getM_Product_ID() > 0 || orderLine.getC_Charge_ID() > 0)
//					msg += setDiscount(orderLine);
//			}
//		}

		else if (event.getTopic().equals(IEventTopics.PO_AFTER_CHANGE)) {
			if(!orderLine.get_ValueAsBoolean("IsBOMDrop"))
				msg += setDiscount(orderLine);
			
			if (!orderLine.getC_Order().isSOTrx()) {
				if (orderLine.is_ValueChanged("QtyEntered") || orderLine.is_ValueChanged("QtyOrdered"))
					msg += updateMatchPR(orderLine);
			}
			if (orderLine.getC_Order().isSOTrx() && orderLine.get_ValueAsBoolean("IsBOMDrop")) {
				msg += updatePrices(orderLine);
			}
		}
		else if (event.getTopic().equals(IEventTopics.PO_AFTER_NEW)) {
			if (orderLine.getC_Order().isSOTrx() && orderLine.get_ValueAsBoolean("IsBOMDrop")) {
				msg += updatePrices(orderLine);
			}
			if (!orderLine.getC_Order().isSOTrx())
				msg += updateMatchPR(orderLine);
		}

		return msg;
	}

	private static String updateQtyOrderedRequisition(MOrderLine orderLine) {

		String whereClause = " C_OrderLine_ID = " + orderLine.getC_OrderLine_ID();
		MMatchPR matchpr = new Query(Env.getCtx(), MMatchPR.Table_Name, whereClause, orderLine.get_TrxName()).first();
		if(matchpr.getM_RequisitionLine_ID() > 0 ) {
			MRequisitionLine rline = new MRequisitionLine(Env.getCtx(), matchpr.getM_RequisitionLine_ID(), orderLine.get_TrxName());
			BigDecimal qtyOrdered = (BigDecimal) rline.get_Value("QtyOrdered");
			BigDecimal newQtyOrdered = qtyOrdered.subtract(orderLine.getQtyOrdered());
			rline.set_ValueNoCheck("QtyOrdered", newQtyOrdered);
			rline.saveEx();
			
		}
		
		return "";
	}
	
	private static String updateLineReferences(MOrderLine orderLine) {
		if(orderLine.get_ValueAsBoolean("IsBOMDrop")) {
			MOrderLine ol = new MOrderLine(Env.getCtx(), orderLine.get_ValueAsInt("BOMDrop_Line_ID"), orderLine.get_TrxName());
			ol.set_ValueNoCheck("IsGeneratedBOMDrop", false);
			ol.saveEx();
		}
		return "";
	}


	private static String updatePrices(MOrderLine orderLine) {

		String sql = "UPDATE c_orderline set priceentered = 0, priceactual = 0, pricelist = 0, linenetamt = 0, discount = 0 where c_orderline_id ="+orderLine.getC_OrderLine_ID();
		DB.executeUpdate(sql, orderLine.get_TrxName());
		
		String sqlGetTotalLine = "Select sum(linenetamt) from c_orderline where c_order_id = ?";
		BigDecimal totalLines = DB.getSQLValueBD(orderLine.get_TrxName(), sqlGetTotalLine, orderLine.getC_Order_ID());
		
		String sqlGetTax = "select coalesce(sum(taxamt),0) from C_OrderTax where c_order_id = ?";
		BigDecimal TaxAmt = DB.getSQLValueBD(orderLine.get_TrxName(), sqlGetTax, orderLine.getC_Order_ID());
		
		BigDecimal grandTotal = totalLines.add(TaxAmt);
		
		MPriceList pl = new MPriceList(Env.getCtx(), orderLine.getC_Order().getM_PriceList_ID(), orderLine.get_TrxName());
		String sqlUpdateTotalLines = "";
		
		if(pl.get_ValueAsBoolean("IsTaxIncluded"))
			sqlUpdateTotalLines = "Update c_order set totallines = " + totalLines + " , grandtotal = " + totalLines + " where c_order_id = " + orderLine.getC_Order_ID();
		else
			sqlUpdateTotalLines = "Update c_order set totallines = " + totalLines + " , grandtotal = " + grandTotal + " where c_order_id = " + orderLine.getC_Order_ID();
		
		DB.executeUpdate(sqlUpdateTotalLines, orderLine.get_TrxName());
		return "";
	}


	private static String setDiscount(MOrderLine orderLine) {
		BigDecimal list = orderLine.getPriceList();

		if (list.compareTo(BigDecimal.ZERO) == 0)
			list = orderLine.getPriceActual();

		BigDecimal discount = list.subtract(orderLine.getPriceActual())
				.multiply(Env.ONEHUNDRED)
				.divide(list, 2, RoundingMode.HALF_UP);

		String sql = "UPDATE C_OrderLine set discount = " + discount + "where c_orderline_id = " + orderLine.get_ID();
		DB.executeUpdate(sql, orderLine.get_TrxName());
		return "";
	}

	private static String ValidateMatchPR(MOrderLine orderLine) {

		if (orderLine.get_ValueAsInt("M_RequisitionLine_ID") == 0)
			return "";

		if (orderLine.is_ValueChanged(MOrderLine.COLUMNNAME_QtyEntered)) {

			//Update MatchPR according 
			BigDecimal sumMatchedQty = new Query(Env.getCtx(), MMatchPR.Table_Name, "C_OrderLine_ID=?", null)
					.setParameters(orderLine.getC_OrderLine_ID())
					.sum(MMatchPR.COLUMNNAME_QtyOrdered);

			MRequisitionLine reqLine = new MRequisitionLine(orderLine.getCtx(), orderLine.get_ValueAsInt("M_RequisitionLine_ID"), orderLine.get_TrxName());
			BigDecimal oldQty = (BigDecimal) reqLine.get_Value("QtyEntered");
			BigDecimal finalQtyOrdered = orderLine.getQtyOrdered().add(reqLine.getQtyOrdered());

			if (finalQtyOrdered.compareTo(oldQty) > 0)
				return "Line " +orderLine.getLine() + ", QtyOrdered = "+orderLine.getQtyOrdered() + ", Previously Matched Qty = " + sumMatchedQty 
						+ ", Total Requisition Qty = " + reqLine.getQty() + " - Total Qty Ordered > Qty on Requisition";
		}
		return "";
	}

	private static String updateMatchPR(MOrderLine orderLine) {

		int reqLineID = orderLine.get_ValueAsInt("M_RequisitionLine_ID");
		if (reqLineID == 0)
			return "";

		//Update MatchPR according 
		MMatchPR matchPR = new Query(Env.getCtx(), MMatchPR.Table_Name, "C_OrderLine_ID=?", null)
				.setParameters(orderLine.getC_OrderLine_ID())
				.first();

		if (matchPR == null) {
			return createMatchPR(orderLine);
		}
		
		BigDecimal diffQty = matchPR.getQtyOrdered().subtract(orderLine.getQtyDelivered());
		matchPR.setQtyOrdered(orderLine.getQtyOrdered());
		matchPR.saveEx();

		String whereSumQtyOrdered = "M_requisitionline_id = ? and c_orderline_id <> ?";
		BigDecimal sumQtyOrdered = new Query(Env.getCtx(), MMatchPR.Table_Name, whereSumQtyOrdered, orderLine.get_TrxName())
				.setParameters(new Object[] {matchPR.getM_RequisitionLine_ID() , orderLine.getC_OrderLine_ID()})
				.sum("QtyOrdered");

		MRequisitionLine reqLine = new MRequisitionLine(orderLine.getCtx(), matchPR.getM_RequisitionLine_ID(), orderLine.get_TrxName());
//		BigDecimal QtyOrdered = reqLine.get_Value("QtyOrdered") != null ? (BigDecimal)reqLine.get_Value("QtyOrdered") : BigDecimal.ZERO;			
		BigDecimal newQtyOrdered = sumQtyOrdered.add(orderLine.getQtyEntered());

		reqLine.set_CustomColumn("QtyOrdered", newQtyOrdered);
		reqLine.saveEx(reqLine.get_TrxName());



//		MRequisitionLine rLine = new MRequisitionLine(Env.getCtx(), matchPR.getM_RequisitionLine_ID(), null);
//		BigDecimal reqLineQtyOrdered = (BigDecimal) rLine.get_Value("QtyOrdered");
//		rLine.set_CustomColumn("QtyOrdered", reqLineQtyOrdered.subtract(diffQty));
//		rLine.save();


		return "";
	}

	private static String removeMatchQuotation(MOrderLine orderLine) {
		StringBuilder sql = new StringBuilder("DELETE FROM ").append(X_M_MatchQuotation.Table_Name)
				.append(" WHERE C_OrderLine_ID=?");
		DB.executeUpdateEx(sql.toString(), new Object[] {orderLine.get_ID()}, orderLine.get_TrxName());

		return "";
	}

	private static String removeMatchPR(MOrderLine orderLine) {
		StringBuilder sql = new StringBuilder("DELETE FROM ").append(X_M_MatchPR.Table_Name)
				.append(" WHERE C_OrderLine_ID=?");
		DB.executeUpdateEx(sql.toString(), new Object[] {orderLine.get_ID()}, orderLine.get_TrxName());
		
		return "";
	}


	private static String createMatchPR(MOrderLine orderLine) {

		int reqLineID = orderLine.get_ValueAsInt("M_RequisitionLine_ID");
		if (reqLineID == 0)
			return "";

		MRequisitionLine reqLine = new MRequisitionLine(orderLine.getCtx(), reqLineID, orderLine.get_TrxName());
		BigDecimal QtyOrdered = reqLine.get_Value("QtyOrdered") != null ? (BigDecimal)reqLine.get_Value("QtyOrdered") : BigDecimal.ZERO;			
		BigDecimal newQtyOrdered = QtyOrdered.add(orderLine.getQtyOrdered());

		X_M_MatchPR match = new X_M_MatchPR(Env.getCtx(), 0, orderLine.get_TrxName());
		match.setAD_Org_ID(reqLine.getAD_Org_ID());
		match.setDateTrx(orderLine.getDateOrdered());
		match.setC_Order_ID(orderLine.getC_Order_ID());
		match.setC_OrderLine_ID(orderLine.getC_OrderLine_ID());
		match.setM_Requisition_ID(reqLine.getM_Requisition_ID());
		match.setM_RequisitionLine_ID(reqLine.getM_RequisitionLine_ID());
		match.setQtyOrdered(orderLine.getQtyOrdered());
		match.saveEx();

		reqLine.set_CustomColumn("QtyOrdered", newQtyOrdered);
		reqLine.saveEx();

		return "";

	}
}
