package id.tcs.validator;

import id.tcs.model.TCS_MRfQ;
import id.tcs.model.TCS_MRfQLine;
import id.tcs.model.X_M_MatchInquiry;

import org.adempiere.base.event.IEventTopics;
import org.adempiere.exceptions.DBException;
import org.compiere.model.PO;
import org.compiere.util.DB;
import org.osgi.service.event.Event;

public class TCS_RFQLineValidator {

	public static String executeEvent(Event event, PO po) {
		String msgRfQL = "";
		TCS_MRfQLine rfqL = (TCS_MRfQLine) po;
		 
		if (event.getTopic().equals(IEventTopics.PO_BEFORE_DELETE)) {
			//msgRfQ = removeMatchRequestRfQ(rfq);
			msgRfQL = deleteMatch(rfqL);
		} 
		return msgRfQL;
	}

	/**
	 * @author stephan
	 * @param rfq
	 * @return
	 */
	private static String deleteMatch(TCS_MRfQLine rfqL){
		try {
			StringBuilder sql = new StringBuilder("DELETE FROM ").append(X_M_MatchInquiry.Table_Name).append(" WHERE C_RfQLine_ID=?");
			DB.executeUpdateEx(sql.toString(), new Object[] {rfqL.get_ID()}, rfqL.get_TrxName());

		} catch (DBException e) {
			e.printStackTrace();
			return 	e.toString();
		}

		return "";
	}
}
