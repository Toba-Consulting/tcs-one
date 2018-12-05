package id.tcs.validator;

import id.tcs.model.TCS_MAdvRequest;
import id.tcs.model.TCS_MAdvSettlement;

import org.adempiere.base.event.IEventTopics;
import org.compiere.model.PO;
import org.compiere.util.Env;
import org.osgi.service.event.Event;

public class TCS_AdvSettlementValidator {

	public static String executeEvent(Event event, PO po){
		String msg = "";
		TCS_MAdvSettlement settlement = (TCS_MAdvSettlement) po;

		if(event.getTopic().equals(IEventTopics.DOC_AFTER_VOID))
			msg = afterVoid(settlement);
				
		return msg;
	}
	
	public  static String afterVoid(TCS_MAdvSettlement settlement){
		
		TCS_MAdvRequest request = new TCS_MAdvRequest(Env.getCtx(), settlement.getTCS_AdvRequest_ID(), null);
		request.setisSettled(false);
		request.saveEx();
		
		return "";
	}
}
