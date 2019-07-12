package id.tcs.validator;

import org.adempiere.base.event.IEventTopics;
import org.adempiere.exceptions.DBException;
import org.compiere.model.MRfQ;
import org.compiere.model.PO;
import org.compiere.util.DB;
import org.osgi.service.event.Event;
import id.tcs.model.X_M_MatchInquiry;

public class TCS_RfQValidator {

	public static String executeEvent(Event event, PO po) {
		String msgRfQ = "";
		MRfQ rfq = (MRfQ) po;
		 
		if (event.getTopic().equals(IEventTopics.DOC_AFTER_VOID)) {
			//msgRfQ = removeMatchRequestRfQ(rfq);
			msgRfQ = removeMatchInquiry(rfq);
		} 
		return msgRfQ;
	}

	/**
	 * @author stephan
	 * @param rfq
	 * @return
	 */
	private static String removeMatchInquiry(MRfQ rfq){
		try {
			StringBuilder sql = new StringBuilder("DELETE FROM ").append(X_M_MatchInquiry.Table_Name).append(" WHERE C_RfQ_ID=?");
			DB.executeUpdateEx(sql.toString(), new Object[] {rfq.get_ID()}, rfq.get_TrxName());

		} catch (DBException e) {
			e.printStackTrace();
			return 	e.toString();
		}

		return "";
	}
}
