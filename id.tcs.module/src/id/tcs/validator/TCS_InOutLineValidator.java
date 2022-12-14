package id.tcs.validator;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.adempiere.base.event.IEventTopics;
import org.compiere.model.MInOutLine;
import org.compiere.model.MInvoiceLine;
import org.compiere.model.MOrderLine;
import org.compiere.model.MRequisitionLine;
import org.compiere.model.PO;
import org.compiere.model.Query;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.osgi.service.event.Event;

import id.tcs.model.MMatchPR;
import id.tcs.model.X_M_MatchPR;
import id.tcs.model.X_M_MatchQuotation;

public class TCS_InOutLineValidator {
	public static String executeEvent(Event event, PO po) {
		String msg = "";
		MInOutLine inoutLine = (MInOutLine) po;
		MOrderLine orderLine = new MOrderLine(Env.getCtx(), inoutLine.getC_OrderLine_ID(), null);
		
		if (event.getTopic().equals(IEventTopics.PO_AFTER_CHANGE)) {
			
			if (inoutLine.getM_InOut().isSOTrx() && orderLine.get_ValueAsBoolean("IsBomDrop")) {
				msg += updatePrices(inoutLine);
			}
		}
		else if (event.getTopic().equals(IEventTopics.PO_AFTER_NEW)) {
			if (inoutLine.getM_InOut().isSOTrx() && orderLine.get_ValueAsBoolean("IsBomDrop")) {
				msg += updatePrices(inoutLine);
			}
		}

		return msg;
	}


	private static String updatePrices(MInOutLine inoutLine) {

		String sql = "UPDATE M_inoutline set isbomdrop = 'Y', isprinted ='N' where m_inoutline_id ="+inoutLine.getM_InOutLine_ID();
		DB.executeUpdate(sql, inoutLine.get_TrxName());
		return "";
	}
}
