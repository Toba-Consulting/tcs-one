package id.tcs.validator;

import java.math.BigDecimal;

import org.adempiere.base.event.IEventTopics;
import org.compiere.model.MBankStatement;
import org.compiere.model.MBankStatementLine;
import org.compiere.model.MInvoice;
import org.compiere.model.MInvoiceLine;
import org.compiere.model.MPayment;
import org.compiere.model.PO;
import org.compiere.util.Env;
import org.osgi.service.event.Event;

public class TCS_BankStatementDocValidator {

	public static String executeEvent(Event event, PO po) {
		String msg = "";
		MBankStatement bankStatement = (MBankStatement) po;
		if (event.getTopic().equals(IEventTopics.DOC_AFTER_REACTIVATE)) {
			msg = unreconcilePayment(bankStatement);
		} 
		return msg;
	}

	private static String unreconcilePayment(MBankStatement bankStatement) {
		
		MBankStatementLine[] lines = bankStatement.getLines(false);
		
		for (MBankStatementLine line: lines) {
			if (line.getC_Payment_ID() == 0)
				continue;
			
			MPayment payment = (MPayment) line.getC_Payment();
			payment.setIsReconciled(false);
			payment.saveEx();
		}
		
		return "";
	}

	
}
