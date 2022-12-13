package id.tcs.validator;

import java.math.BigDecimal;

import org.adempiere.base.event.IEventTopics;
import org.compiere.model.I_M_RMA;
import org.compiere.model.MRMALine;
import org.compiere.model.PO;
import org.compiere.model.Query;
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

		if (event.getTopic().equals(IEventTopics.DOC_BEFORE_REACTIVATE)) {

			msgRMA = ValidateBeforeReactivate(rma);
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

	private static String ValidateBeforeReactivate(TCS_MRMA rma) {
		
		String whereClause = "rline.M_RMA_ID=? AND rline.QtyDelivered > 0";
		boolean match = new Query(rma.getCtx(), I_M_RMA.Table_Name, whereClause, rma.get_TrxName())
							.addJoinClause("JOIN M_RMALine rline on rline.M_RMA_ID = M_RMA.M_RMA_ID")
							.setOnlyActiveRecords(true)
							.setParameters(new Object[] {rma.get_ID()})
							.match();
		
		if (match)
			return "Cannot Reactivate, RMA has link to completed vendor/customer return";
		
		return "";
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
