package org.taxrateacct.callout;

import java.util.Properties;

import org.adempiere.base.IColumnCallout;
import org.compiere.model.GridField;
import org.compiere.model.GridTab;
import org.compiere.model.MAccount;
import org.taxrateacct.model.MTaxAcct;
import org.taxrateacct.model.MValidCombination;

public class CalloutTaxRateAcct implements IColumnCallout{

	@Override
	public String start(Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value, Object oldValue) {
		// TODO Auto-generated method stub
		if (mField.getColumnName().equals(MTaxAcct.COLUMNNAME_C_ElementValue_TD_ID))
			return setTaxDue(ctx, WindowNo, mTab, mField, value, oldValue);
		if (mField.getColumnName().equals(MTaxAcct.COLUMNNAME_C_ElementValue_TE_ID))
			return setTaxExpense(ctx, WindowNo, mTab, mField, value, oldValue);
		if (mField.getColumnName().equals(MTaxAcct.COLUMNNAME_C_ElementValue_TC_ID))
			return setTaxCredit(ctx, WindowNo, mTab, mField, value, oldValue);
		if (mField.getColumnName().equals(MTaxAcct.COLUMNNAME_T_Due_Acct)
				|| mField.getColumnName().equals(MTaxAcct.COLUMNNAME_T_Expense_Acct)
				|| mField.getColumnName().equals(MTaxAcct.COLUMNNAME_T_Credit_Acct)
				)
			setElementValue(ctx, WindowNo, mTab, mField, value, oldValue);
		return null;
	}

	//Tax Rate
	//Set Tax Due
	protected String setTaxDue(Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value, Object oldValue) {
		if (value == null)
			return "";
		
		MAccount validAccount = MAccount.get(ctx, (int)mTab.getValue("AD_Client_ID"), 0, (int)mTab.getValue("C_AcctSchema_ID"), (int)mTab.getValue("C_ElementValue_TD_ID"), 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, null);
		mTab.setValue("T_Due_Acct", validAccount.get_ID());
		return "";
	}
	
	//Set Tax Credit
	protected String setTaxCredit(Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value, Object oldValue) {
		if (value == null)
			return "";
			
		MAccount validAccount = MAccount.get(ctx, (int)mTab.getValue("AD_Client_ID"), 0, (int)mTab.getValue("C_AcctSchema_ID"), (int)mTab.getValue("C_ElementValue_TC_ID"), 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, null);
		mTab.setValue("T_Credit_Acct", validAccount.get_ID());
		return "";
	}
	
	//Set Tax Expense
	protected String setTaxExpense(Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value, Object oldValue) {
		if (value == null)
			return "";
			
		MAccount validAccount = MAccount.get(ctx, (int)mTab.getValue("AD_Client_ID"), 0, (int)mTab.getValue("C_AcctSchema_ID"), (int)mTab.getValue("C_ElementValue_TE_ID"), 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, null);
		mTab.setValue("T_Expense_Acct", validAccount.get_ID());
		return "";
	}
	
	//Set Element Value
	protected String setElementValue(Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value, Object oldValue) {
		if(value == null)
			return "";
		
		int C_ValidCombination_ID = (int)value;
		MValidCombination validCombination = new MValidCombination(ctx, C_ValidCombination_ID, null);
		
		if (mField.getColumnName().equals("T_Due_Acct")) {
			mTab.setValue(MTaxAcct.COLUMNNAME_C_ElementValue_TD_ID, validCombination.getAccount_ID());
		}else if (mField.getColumnName().equals("T_Expense_Acct")) {
			mTab.setValue(MTaxAcct.COLUMNNAME_C_ElementValue_TE_ID, validCombination.getAccount_ID());
		}else if (mField.getColumnName().equals("T_Credit_Acct")) {
			mTab.setValue(MTaxAcct.COLUMNNAME_C_ElementValue_TC_ID, validCombination.getAccount_ID());
		}
		return "";
	}
}
