package id.tcs.validator;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.adempiere.base.event.IEventTopics;
import org.compiere.model.MOrder;
import org.compiere.model.MRequisition;
import org.compiere.model.MRequisitionLine;
import org.compiere.model.MUOM;
import org.compiere.model.MUOMConversion;
import org.compiere.model.PO;
import org.compiere.model.Query;
import org.compiere.util.Env;
import org.osgi.service.event.Event;

import id.tcs.model.X_M_MatchPR;

public class TCS_RequisitionLineValidator {

	public static String executeEvent(Event event, PO po){
		String msg = "";
		MRequisitionLine reqLine = (MRequisitionLine) po;

//		if ((event.getTopic().equals(IEventTopics.PO_AFTER_NEW))) {
//			//Fix Qty in Requisition Line - it's err after close code in core
//			msg += createMatchPR(req);
//		}
		return msg;
	}


}
