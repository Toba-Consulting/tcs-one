package id.tcs.validator;

import java.math.BigDecimal;

import org.adempiere.base.event.IEventTopics;
import org.compiere.model.I_M_RMA;
import org.compiere.model.MInOutLine;
import org.compiere.model.MOrderLine;
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

		int [] temp = new Query(rma.getCtx(), MRMALine.Table_Name, "M_RMA_ID="+rma.getM_RMA_ID(), rma.get_TrxName())
				.getIDs();
		if (temp==null || temp.length==0) {
			return "";
		}
		String IDs = "";
		for (int i : temp) {
			IDs+=i;
			IDs+=", ";
		}
		IDs=IDs.substring(0, IDs.length()-2);
		String sqlWhere = "M_RMALine_ID IN ("+IDs+") AND mi.DocStatus IN ('CO','CL','IP')";
		boolean match = new Query(rma.getCtx(), MInOutLine.Table_Name, sqlWhere, rma.get_TrxName())
				.addJoinClause("JOIN M_InOut mi on mi.M_InOut_ID=M_InOutLine.M_InOut_ID")
				.match();
		if (match) {
			return "Cannot Reactivate RMA : Active InOut Exists For RMA Line";
		}
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
