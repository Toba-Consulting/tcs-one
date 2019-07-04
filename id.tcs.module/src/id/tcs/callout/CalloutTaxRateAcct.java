package id.tcs.callout;

import java.util.Properties;

import org.adempiere.base.IColumnCallout;
import org.compiere.model.MTaxAcct;
import org.compiere.model.GridField;
import org.compiere.model.GridTab;
import org.compiere.model.MAccount;

public class CalloutTaxRateAcct implements IColumnCallout{

	@Override
	public String start(Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value, Object oldValue) {
		// TODO Auto-generated method stub
		if (mField.getColumnName().equals("C_ElementValue_TD_ID"))
			return setAccount(ctx, WindowNo, mTab, mField, value, oldValue, "C_ElementValue_TD_ID");
		if (mField.getColumnName().equals("C_ElementValue_TE_ID"))
			return setAccount(ctx, WindowNo, mTab, mField, value, oldValue, "C_ElementValue_TE_ID");
		if (mField.getColumnName().equals("C_ElementValue_TC_ID"))
			return setAccount(ctx, WindowNo, mTab, mField, value, oldValue, "C_ElementValue_TC_ID");
		if (mField.getColumnName().equals(MTaxAcct.COLUMNNAME_T_Due_Acct)
				|| mField.getColumnName().equals(MTaxAcct.COLUMNNAME_T_Expense_Acct)
				|| mField.getColumnName().equals(MTaxAcct.COLUMNNAME_T_Credit_Acct)
				)
			setElementValue(ctx, WindowNo, mTab, mField, value, oldValue);
		return null;
	}

	public String setAccount(Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value,
			Object oldValue, String columnName) {
		// TODO Auto-generated method stub
		if (value == null)
			return "";
		
		MAccount validAccount = MAccount.get(ctx, (int)mTab.getValue("AD_Client_ID"), 0, (int)mTab.getValue("C_AcctSchema_ID"), (int)mTab.getValue(columnName), 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, null);
		if(columnName.equals("C_ElementValue_TD_ID"))
			mTab.setValue("T_Due_Acct", validAccount.get_ID());
		else if(columnName.equals("C_ElementValue_TC_ID"))
			mTab.setValue("T_Credit_Acct", validAccount.get_ID());
		else if(columnName.equals("C_ElementValue_TE_ID"))
			mTab.setValue("T_Expense_Acct", validAccount.get_ID());
		
		return "";
	}
	
	//Set Element Value
	protected String setElementValue(Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value, Object oldValue) {
		if(value == null)
			return "";
		
		int C_ValidCombination_ID = (int)value;
		MAccount validCombination = new MAccount(ctx, C_ValidCombination_ID, null);
		
		if (mField.getColumnName().equals("T_Due_Acct")) {
			mTab.setValue("C_ElementValue_TD_ID", validCombination.getAccount_ID());
		}else if (mField.getColumnName().equals("T_Expense_Acct")) {
			mTab.setValue("C_ElementValue_TE_ID", validCombination.getAccount_ID());
		}else if (mField.getColumnName().equals("T_Credit_Acct")) {
			mTab.setValue("C_ElementValue_TC_ID", validCombination.getAccount_ID());
		}
		return "";
	}
}
