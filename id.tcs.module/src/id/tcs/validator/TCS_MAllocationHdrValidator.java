package id.tcs.validator;


import org.compiere.model.PO;
import org.osgi.service.event.Event;

public class TCS_MAllocationHdrValidator {

	public static String executeEvent(Event event, PO po){
		String msg = "";
/*		MAllocationHdr allocationHdr = (MAllocationHdr) po;

		if(event.getTopic().equals(IEventTopics.DOC_AFTER_COMPLETE))
			msg = afterComplete(allocationHdr);

		if(event.getTopic().equals(IEventTopics.DOC_AFTER_REVERSEACCRUAL))
			msg = afterReverse(allocationHdr);

		if(event.getTopic().equals(IEventTopics.DOC_AFTER_REVERSECORRECT))
			msg = afterReverse(allocationHdr);
*/
		return msg;
	}



}
