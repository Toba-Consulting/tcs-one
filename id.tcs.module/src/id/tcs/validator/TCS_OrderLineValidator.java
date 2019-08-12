package id.tcs.validator;

import org.adempiere.base.event.IEventTopics;
import org.compiere.model.MOrderLine;
import org.compiere.model.PO;
import org.compiere.util.DB;
import org.osgi.service.event.Event;

import id.tcs.model.X_M_MatchQuotation;

public class TCS_OrderLineValidator {
	public static String executeEvent(Event event, PO po) {
		String msg = "";
		MOrderLine orderLine = (MOrderLine) po;
		
		if (event.getTopic().equals(IEventTopics.PO_BEFORE_DELETE)) {
			msg += removeMatchQuotation(orderLine);
		}
		return msg;
	}
	
	private static String removeMatchQuotation(MOrderLine orderLine) {
		StringBuilder sql = new StringBuilder("DELETE FROM ").append(X_M_MatchQuotation.Table_Name)
				.append(" WHERE C_OrderLine_ID=?");
		DB.executeUpdateEx(sql.toString(), new Object[] {orderLine.get_ID()}, orderLine.get_TrxName());

		return "";
	}
}
