package id.tcs.validator;

import org.adempiere.base.event.IEventTopics;
import org.compiere.model.MInOutLine;
import org.compiere.model.MMatchPO;
import org.compiere.model.MOrder;
import org.compiere.model.MOrderLine;
import org.compiere.model.MPayment;
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
}
