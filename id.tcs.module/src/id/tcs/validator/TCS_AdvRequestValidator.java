package id.tcs.validator;

import id.tcs.model.TCS_MAdvRequest;

import org.adempiere.base.event.IEventTopics;
import org.compiere.model.PO;
import org.osgi.service.event.Event;

public class TCS_AdvRequestValidator {

	public static String executeEvent(Event event, PO po){
		String msg = "";
		TCS_MAdvRequest request = (TCS_MAdvRequest) po;

		if(event.getTopic().equals(IEventTopics.DOC_BEFORE_VOID))
			msg = beforeVoid(request);
				
		return msg;
	}
	
	public  static String beforeVoid(TCS_MAdvRequest request){
		

		return "";
	}
	
}
