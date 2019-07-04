package id.tcs.validator;

import java.math.BigDecimal;

import org.adempiere.base.event.IEventTopics;
import org.compiere.model.MPayment;
import org.compiere.model.MPaymentAllocate;
import org.compiere.model.PO;
import org.compiere.util.DB;
import org.osgi.service.event.Event;

public class TCS_PaymentAllocateValidator {
	public static String executeEvent(Event event, PO po){
		String msg = "";
		MPaymentAllocate payAlloc = (MPaymentAllocate) po;

		if ((event.getTopic().equals(IEventTopics.PO_AFTER_NEW)) ||
				(event.getTopic().equals(IEventTopics.PO_AFTER_CHANGE))) {
			msg = setPaymentAmount(payAlloc);
		}
		return msg;
	}
	
	public static String setPaymentAmount(MPaymentAllocate payAlloc){
		
		String sqlSumPayAlloc = "SELECT COALESCE(SUM(Amount),0) FROM C_PaymentAllocate WHERE C_Payment_ID="+payAlloc.getC_Payment_ID();
		String sqlSumAllocCharge = "SELECT COALESCE(SUM(Amount),0) FROM TCS_AllocateCharge WHERE C_Payment_ID="+payAlloc.getC_Payment_ID();
		BigDecimal PayAmt = DB.getSQLValueBD(payAlloc.get_TrxName(), sqlSumPayAlloc);
		PayAmt=PayAmt.add(DB.getSQLValueBD(payAlloc.get_TrxName(), sqlSumAllocCharge));
		
		MPayment payment = new MPayment(payAlloc.getCtx(), payAlloc.getC_Payment_ID(), payAlloc.get_TrxName());
		payment.setPayAmt(PayAmt);
		payment.saveEx();
		
		return "";
	}
}
