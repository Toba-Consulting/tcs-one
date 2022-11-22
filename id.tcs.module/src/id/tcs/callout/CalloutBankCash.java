package id.tcs.callout;

import java.util.Properties;

import org.adempiere.base.IColumnCallout;
import org.compiere.model.GridField;
import org.compiere.model.GridTab;
import org.compiere.model.MAccount;

public class CalloutBankCash implements IColumnCallout {

	@Override
	public String start(Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value, Object oldValue) {
		if (mField.getColumnName().equals("C_ElementValue_BA_ID"))
			return setAccount(ctx, WindowNo, mTab, mField, value, oldValue, "C_ElementValue_BA_ID");
		if (mField.getColumnName().equals("C_ElementValue_BIT_ID"))
			return setAccount(ctx, WindowNo, mTab, mField, value, oldValue, "C_ElementValue_BIT_ID");
		if (mField.getColumnName().equals("C_ElementValue_PS_ID"))
			return setAccount(ctx, WindowNo, mTab, mField, value, oldValue, "C_ElementValue_PS_ID");
		if (mField.getColumnName().equals("C_ElementValue_UC_ID"))
			return setAccount(ctx, WindowNo, mTab, mField, value, oldValue, "C_ElementValue_UC_ID");
		if (mField.getColumnName().equals("C_ElementValue_BIR_ID"))
			return setAccount(ctx, WindowNo, mTab, mField, value, oldValue, "C_ElementValue_BIR_ID");
		if (mField.getColumnName().equals("C_ElementValue_BIE_ID"))
			return setAccount(ctx, WindowNo, mTab, mField, value, oldValue, "C_ElementValue_BIE_ID");
		if (mField.getColumnName().equals("B_Asset_Acct") 
				|| mField.getColumnName().equals("B_InTransit_Acct") 
				|| mField.getColumnName().equals("B_PaymentSelect_Acct") 
				|| mField.getColumnName().equals("B_UnallocatedCash_Acct") 
				|| mField.getColumnName().equals("B_InterestRev_Acct") 
				|| mField.getColumnName().equals("B_InterestExp_Acct"))
			return setElementValue(ctx, WindowNo, mTab, mField, value, oldValue);
		return null;
	}

	public String setAccount(Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value,
			Object oldValue, String columnName) {
		
		if(value == null)
			return "";
			
		MAccount validAccount = MAccount.get(ctx, (int)mTab.getValue("AD_Client_ID"), 0, (int)mTab.getValue("C_AcctSchema_ID"), (int)mTab.getValue(columnName), 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, null);
		
		if(columnName.equals("C_ElementValue_BA_ID"))
			mTab.setValue("B_Asset_Acct", validAccount.get_ID());
		else if(columnName.equals("C_ElementValue_BIT_ID"))
			mTab.setValue("B_InTransit_Acct", validAccount.get_ID());
		else if(columnName.equals("C_ElementValue_PS_ID"))
			mTab.setValue("B_PaymentSelect_Acct", validAccount.get_ID());
		else if(columnName.equals("C_ElementValue_UC_ID"))
			mTab.setValue("B_UnallocatedCash_Acct", validAccount.get_ID());
		else if(columnName.equals("C_ElementValue_BIR_ID"))
			mTab.setValue("B_InterestRev_Acct", validAccount.get_ID());
		else if(columnName.equals("C_ElementValue_BIE_ID"))
			mTab.setValue("B_InterestExp_Acct", validAccount.get_ID());
		return "";

	}

	
	//set Element Value
	protected String setElementValue(Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value, Object oldValue) {
		
		if(value == null) {
			return "";
		}
		
		int C_ValidCombination_ID = (int) value;
		MAccount validcombination = new MAccount(ctx, C_ValidCombination_ID, null);
		
		if(mField.getColumnName().equals("B_Asset_Acct"))
			mTab.setValue("C_ElementValue_BA_ID", validcombination.getAccount_ID());
		else if(mField.getColumnName().equals("B_InTransit_Acct"))
			mTab.setValue("C_ElementValue_BIT_ID", validcombination.getAccount_ID());
		else if(mField.getColumnName().equals("B_PaymentSelect_Acct"))
			mTab.setValue("C_ElementValue_PS_ID", validcombination.getAccount_ID());
		else if(mField.getColumnName().equals("B_UnallocatedCash_Acct"))
			mTab.setValue("C_ElementValue_UC_ID", validcombination.getAccount_ID());
		else if(mField.getColumnName().equals("B_InterestRev_Acct"))
			mTab.setValue("C_ElementValue_BIR_ID", validcombination.getAccount_ID());
		else if(mField.getColumnName().equals("B_InterestExp_Acct"))
			mTab.setValue("C_ElementValue_BIE_ID", validcombination.getAccount_ID());
		
		return "";
	}

}
