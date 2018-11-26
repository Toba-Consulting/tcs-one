package id.tcs.validator;


import org.adempiere.base.event.IEventTopics;
import org.compiere.model.MAllocationHdr;
import org.compiere.model.PO;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.osgi.service.event.Event;
import org.compiere.model.TCS_MAllocationHdr;

public class TCS_MAllocationHdrValidator {

	public static String executeEvent(Event event, PO po){
		String msg = "";
		MAllocationHdr allocationHdr = (MAllocationHdr) po;
		
		if(event.getTopic().equals(IEventTopics.DOC_AFTER_COMPLETE))
			msg = afterComplete(allocationHdr);
		
		if(event.getTopic().equals(IEventTopics.DOC_AFTER_REVERSEACCRUAL))
			msg = afterReverse(allocationHdr);
		
		if(event.getTopic().equals(IEventTopics.DOC_AFTER_REVERSECORRECT))
			msg = afterReverse(allocationHdr);
		
		return msg;
	}
	
	public static String afterComplete(MAllocationHdr allocationHdr){
		
		TCS_MAllocationHdr docAllocationHdr = new TCS_MAllocationHdr(Env.getCtx(), allocationHdr.getC_AllocationHdr_ID(), allocationHdr.get_TrxName());
		if (docAllocationHdr.isReversal()) {
			docAllocationHdr.createMatchAllocation();			
		}
		

		return "";
	}
	public  static String afterReverse(MAllocationHdr allocationHdr){
		
		String query="DELETE FROM T_MatchAllocation WHERE C_AllocationHdr_ID=?";
		DB.executeUpdate(query, allocationHdr.getC_AllocationHdr_ID(), null);
		
		return "";
	}
}
