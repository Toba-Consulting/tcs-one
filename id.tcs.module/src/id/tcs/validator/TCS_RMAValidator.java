package id.tcs.validator;

import java.math.BigDecimal;

import org.adempiere.base.event.IEventTopics;
import org.compiere.model.MRMALine;
import org.compiere.model.PO;
import org.compiere.model.TCS_MRMA;
import org.osgi.service.event.Event;

public class TCS_RMAValidator {

	public static String executeEvent(Event event, PO po) {
		String msgRMA = "";
		TCS_MRMA rma = (TCS_MRMA) po;
		
		if (event.getTopic().equals(IEventTopics.DOC_AFTER_COMPLETE)) {
			boolean isReplacement = rma.get_ValueAsBoolean("IsReplacement");
			
			if (!isReplacement)
				return msgRMA;
			else 
				msgRMA = setQtyInvoicedReplacementRMA(rma);
		} 
		
		if (event.getTopic().equals(IEventTopics.DOC_AFTER_REACTIVATE)) {
			boolean isReplacement = rma.get_ValueAsBoolean("IsReplacement");
			
			if (!isReplacement)
				return msgRMA;
			else 
				msgRMA = resetQtyInvoicedReplacementRMA(rma);
		} 
		return msgRMA;
	}

	/**
	 * @author edwinang
	 * @param rma
	 * @return
	 */
	private static String setQtyInvoicedReplacementRMA(TCS_MRMA rma){
		
		MRMALine[] rmaLines = rma.getLines(false);
		
		for (MRMALine rmaLine:rmaLines) {
			rmaLine.setQtyInvoiced(rmaLine.getQty());
			rmaLine.saveEx();
		}
		
		return "";
	}
	
	/**
	 * @author edwinang
	 * @param rma
	 * @return
	 */
	private static String resetQtyInvoicedReplacementRMA(TCS_MRMA rma){
		
		MRMALine[] rmaLines = rma.getLines(false);
		
		for (MRMALine rmaLine:rmaLines) {
			rmaLine.setQtyInvoiced(BigDecimal.ZERO);
			rmaLine.saveEx();
		}
		
		return "";
	}
}
