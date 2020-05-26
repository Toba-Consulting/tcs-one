package id.tcs.callout;

import java.util.Properties;

import org.adempiere.base.IColumnCallout;
import org.compiere.model.CalloutEngine;
import org.compiere.model.GridField;
import org.compiere.model.GridTab;
import org.compiere.model.MBankAccount;
import org.compiere.model.MDocType;

/**
 * @author TommyAng
 * Callout Payment
 */

public class TCS_CalloutPayment extends CalloutEngine implements IColumnCallout{

	@Override
	public String start(Properties ctx, int WindowNo, GridTab mTab,
			GridField mField, Object value, Object oldValue) {
		
		if(mField.getColumnName().equals("C_DocType_ID")){
			return docType(ctx, WindowNo, mTab, mField, value);
		}else if(mField.getColumnName().equals("C_BankAccount_ID")){
			return bankAccount(ctx, WindowNo, mTab, mField, value);
		}
		return "";
	}
	
	private String bankAccount(Properties ctx, int windowNo, GridTab mTab,
			GridField mField, Object value){
		
		if (value==null){
			return "";
		}
		
		int C_BankAccount_ID = (int) value;
		
		MBankAccount bank = new MBankAccount(ctx, C_BankAccount_ID, null);
		mTab.setValue("C_Currency_ID", bank.getC_Currency_ID());
		return "";
	}
	
	private String docType(Properties ctx, int windowNo, GridTab mTab,
			GridField mField, Object value){
		
		if (value==null){
			return "";
		}
		
		int C_DocType_ID = (int) value;
		
		MDocType docType = new MDocType(ctx, C_DocType_ID, null);
		mTab.setValue("IsPrepayment", docType.get_ValueAsBoolean("IsPrepayment"));

		return "";
	}
}
