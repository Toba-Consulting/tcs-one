package id.tcs.validator;

import org.adempiere.base.event.IEventTopics;
import org.adempiere.exceptions.AdempiereException;
import org.compiere.model.MAllocationLine;
import org.compiere.model.MPayment;
import org.compiere.model.PO;
import org.compiere.model.Query;
import org.osgi.service.event.Event;

public class TCS_PaymentValidator {

	public static String executeEvent(Event event, PO po){
		String msg = "";
		MPayment payment = (MPayment) po;

		if ((event.getTopic().equals(IEventTopics.DOC_BEFORE_REVERSEACCRUAL)) ||
				(event.getTopic().equals(IEventTopics.DOC_BEFORE_REVERSECORRECT))) {
			msg = checkAllocation(payment);
		}
		return msg;
	}
	
	public static String checkAllocation(MPayment payment){
		String whereClause = "C_Payment_ID = ? AND cdr.docStatus='CO'";
		boolean match = new Query(payment.getCtx(), MAllocationLine.Table_Name, whereClause, payment.get_TrxName())
				.addJoinClause("JOIN C_AllocationHdr cdr ON cdr.c_AllocationHdr_ID = C_AllocationLine.C_AllocationHdr_ID")
				.setParameters(new Object[]{payment.getC_Payment_ID()})
				.match();

		if(match)
			throw new AdempiereException("Cannot reverse payment.. Related allocation exists..");
		return "";
	}
}