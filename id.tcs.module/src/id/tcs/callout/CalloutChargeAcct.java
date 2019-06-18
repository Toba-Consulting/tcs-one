package org.chargeacct.callout;

import java.util.Properties;

import org.adempiere.base.IColumnCallout;
import org.chargeacct.model.MChargeAcct;
import org.chargeacct.model.MValidCombination;
import org.compiere.model.GridField;
import org.compiere.model.GridTab;
import org.compiere.model.MAccount;
import org.compiere.util.Env;

public class CalloutChargeAcct implements IColumnCallout{

	@Override
	public String start(Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value, Object oldValue) {
		// TODO Auto-generated method stub
		if(mField.getColumnName().equals(MChargeAcct.COLUMNNAME_C_ElementValue_ID))
			return setChargeAcct(ctx, WindowNo, mTab, mField, value, oldValue);
		if(mField.getColumnName().equals(MChargeAcct.COLUMNNAME_Ch_Expense_Acct))
			return setElementValue(ctx, WindowNo, mTab, mField, value, oldValue);
		return null;
	}
	
	//Charge
	//Set Ch_Expense_Acct
	protected String setChargeAcct(Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value, Object oldValue) {
		if(value == null)
			return "";
	
		MAccount validAccount = MAccount.get(ctx, (int)mTab.getValue("AD_Client_ID"), 0, (int)mTab.getValue("C_AcctSchema_ID"), (int)mTab.getValue("C_ElementValue_ID"), 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, null);		
		mTab.setValue("Ch_Expense_Acct", validAccount.get_ID());
		
		return "";
	}
	
	//Charge
	//Set C_ElementValue_ID
	protected String setElementValue(Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value, Object oldValue) {
		if(value == null)
			return "";
		
		int C_ValidCombination_ID = (int) value;
		MValidCombination validCombination = new MValidCombination(Env.getCtx(), C_ValidCombination_ID, null);
		mTab.setValue(MChargeAcct.COLUMNNAME_C_ElementValue_ID, validCombination.getAccount_ID());
		return "";
	}
}
