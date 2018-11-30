package id.tcs.validator;

import id.tcs.model.TCS_MAdvSettlement;

import org.adempiere.base.event.IEventTopics;
import org.compiere.model.MInvoice;
import org.compiere.model.PO;
import org.compiere.model.Query;
import org.osgi.service.event.Event;

public class TCS_InvoiceValidator {

	
	public static String executeEvent(Event event, PO po){
		String msg = "";
		MInvoice invoice = (MInvoice) po;

		if(event.getTopic().equals(IEventTopics.DOC_AFTER_REVERSEACCRUAL))
			msg = afterReverse(invoice);
		
		if(event.getTopic().equals(IEventTopics.DOC_AFTER_REVERSECORRECT))
			msg = afterReverse(invoice);
		
		return msg;
	}
	
	public  static String afterReverse(MInvoice invoice){
		
		String sqlSettlement="C_Invoice_ID="+invoice.getC_Invoice_ID();
		int [] settlementIDs = new Query(invoice.getCtx(), TCS_MAdvSettlement.Table_Name, sqlSettlement, invoice.get_TrxName()).getIDs();
		if (settlementIDs.length>0) {
		
			TCS_MAdvSettlement settlement;
			
			for (int i : settlementIDs) {
				settlement= new TCS_MAdvSettlement(invoice.getCtx(), i, invoice.get_TrxName());
				settlement.setC_Invoice_ID(0);
				settlement.saveEx();
			}
		}
		return "";
	}
}
