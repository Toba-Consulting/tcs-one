package id.tcs.validator;


import org.adempiere.base.event.IEventTopics;
import org.compiere.model.MAllocationHdr;
import org.compiere.model.PO;
import org.compiere.util.Env;
import org.osgi.service.event.Event;

import id.tcs.model.TCS_MAllocationHdr;

public class TCS_MAllocationHdrValidator {

	public static String executeEvent(Event event, PO po){
		String msg = "";
		MAllocationHdr allocationHdr = (MAllocationHdr) po;
		
		if(event.getTopic().equals(IEventTopics.DOC_AFTER_COMPLETE))
			msg = afterComplete(allocationHdr);
		
		return msg;
	}
	
	public static String afterComplete(MAllocationHdr allocationHdr){
		
		TCS_MAllocationHdr docAllocationHdr = new TCS_MAllocationHdr(Env.getCtx(), allocationHdr.getC_AllocationHdr_ID(), allocationHdr.get_TrxName());
		docAllocationHdr.createMatchAllocation();

		return "";
	}
	
}
