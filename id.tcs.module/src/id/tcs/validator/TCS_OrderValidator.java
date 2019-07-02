package id.tcs.validator;

import org.adempiere.base.event.IEventTopics;
import org.compiere.model.MMatchPO;
import org.compiere.model.MOrder;
import org.compiere.model.MOrderLine;
import org.compiere.model.PO;
import org.compiere.model.Query;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.osgi.service.event.Event;

public class TCS_OrderValidator {

	
	public static String executeEvent(Event event, PO po) {
		String msg = "";
		MOrder order = (MOrder) po;
		if (event.getTopic().equals(IEventTopics.DOC_AFTER_REACTIVATE)) {
			msg = unreserveQty(order);
		} 
		else if (event.getTopic().equals(IEventTopics.DOC_BEFORE_REACTIVATE)) {
			msg = checkMatchPO(order);
		} 
		else if (event.getTopic().equals(IEventTopics.DOC_BEFORE_VOID)) {
			msg = checkMatchPO(order);
		} 
		return msg;
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
		
		String sqlWhere = "C_OrderLine_ID IN ("+IDs+")";
		boolean match = new Query(order.getCtx(), MMatchPO.Table_Name, sqlWhere, order.get_TrxName())
							.match();
		if (match) {
			return "Cannot Reverse Order : Existing Match PO Exist For Order Line";
		}
		return "";
	}
}
