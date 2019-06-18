package org.banktransfer.callout;

import java.util.Properties;

import org.adempiere.base.IColumnCallout;
import org.banktransfer.model.MAcctSchemaDefault;
import org.banktransfer.model.MBankAccountAcct;
import org.banktransfer.model.MValidCombination;
import org.compiere.model.GridField;
import org.compiere.model.GridTab;
import org.compiere.model.MAccount;

public class CalloutBankCash implements IColumnCallout{

	@Override
	public String start(Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value, Object oldValue) {
		// TODO Auto-generated method stub
		if (mField.getColumnName().equals(MBankAccountAcct.COLUMNNAME_C_ElementValue_BA_ID))
			return setBankAssetAcct(ctx, WindowNo, mTab, mField, value, oldValue);
		if (mField.getColumnName().equals(MBankAccountAcct.COLUMNNAME_C_ElementValue_BIT_ID))
			return setBankInTransitAcct(ctx, WindowNo, mTab, mField, value, oldValue);
		if (mField.getColumnName().equals(MBankAccountAcct.COLUMNNAME_C_ElementValue_PS_ID))
			return setPaymentSelectAcct(ctx, WindowNo, mTab, mField, value, oldValue);
		if (mField.getColumnName().equals(MBankAccountAcct.COLUMNNAME_C_ElementValue_UC_ID))
			return setUnallocatedCashAcct(ctx, WindowNo, mTab, mField, value, oldValue);
		if (mField.getColumnName().equals(MBankAccountAcct.COLUMNNAME_C_ElementValue_BIR_ID))
			return setBankInterestRevenueAcct(ctx, WindowNo, mTab, mField, value, oldValue);
		if (mField.getColumnName().equals(MBankAccountAcct.COLUMNNAME_C_ElementValue_BIE_ID))
			return setBankInterestExpenseAcct(ctx, WindowNo, mTab, mField, value, oldValue);
		if (mField.getColumnName().equals(MBankAccountAcct.COLUMNNAME_B_Asset_Acct) 
				|| mField.getColumnName().equals(MBankAccountAcct.COLUMNNAME_B_InTransit_Acct) 
				|| mField.getColumnName().equals(MBankAccountAcct.COLUMNNAME_B_PaymentSelect_Acct) 
				|| mField.getColumnName().equals(MBankAccountAcct.COLUMNNAME_B_UnallocatedCash_Acct) 
				|| mField.getColumnName().equals(MBankAccountAcct.COLUMNNAME_B_InterestRev_Acct) 
				|| mField.getColumnName().equals(MBankAccountAcct.COLUMNNAME_B_InterestExp_Acct))
			return setElementValue(ctx, WindowNo, mTab, mField, value, oldValue);
		return null;
	}

	// set Bank Asset Acct 
	protected String setBankAssetAcct(Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value, Object oldValue) {
		if(value == null)
			return "";
			
		MAccount validAccount = MAccount.get(ctx, (int)mTab.getValue("AD_Client_ID"), 0, (int)mTab.getValue("C_AcctSchema_ID"), (int)mTab.getValue("C_ElementValue_BA_ID"), 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, null);
		mTab.setValue("B_Asset_Acct", validAccount.get_ID());
		return "";
	}	 

	// set Bank InTransit Acct 
	protected String setBankInTransitAcct(Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value, Object oldValue) {
		if(value == null)
			return "";
			
		MAccount validAccount = MAccount.get(ctx, (int)mTab.getValue("AD_Client_ID"), 0, (int)mTab.getValue("C_AcctSchema_ID"), (int)mTab.getValue("C_ElementValue_BIT_ID"), 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, null);
		mTab.setValue("B_InTransit_Acct", validAccount.get_ID());
		return "";
	}	
	
	// set Payment Select Acct
	protected String setPaymentSelectAcct(Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value, Object oldValue) {
		if(value == null)
			return "";
			
		MAccount validAccount = MAccount.get(ctx, (int)mTab.getValue("AD_Client_ID"), 0, (int)mTab.getValue("C_AcctSchema_ID"), (int)mTab.getValue("C_ElementValue_PS_ID"), 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, null);
		mTab.setValue("B_PaymentSelect_Acct", validAccount.get_ID());
		return "";
	}	
	
	// set Unallocated Cash Acct
	protected String setUnallocatedCashAcct(Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value, Object oldValue) {
		if(value == null)
			return "";
			
		MAccount validAccount = MAccount.get(ctx, (int)mTab.getValue("AD_Client_ID"), 0, (int)mTab.getValue("C_AcctSchema_ID"), (int)mTab.getValue("C_ElementValue_UC_ID"), 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, null);
		mTab.setValue("B_UnallocatedCash_Acct", validAccount.get_ID());
		return "";
	}		
	
	// set Bank Interest Revenue Acct
	protected String setBankInterestRevenueAcct(Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value, Object oldValue) {
		if(value == null)
			return "";
			
		MAccount validAccount = MAccount.get(ctx, (int)mTab.getValue("AD_Client_ID"), 0, (int)mTab.getValue("C_AcctSchema_ID"), (int)mTab.getValue("C_ElementValue_BIR_ID"), 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, null);
		mTab.setValue("B_InterestRev_Acct", validAccount.get_ID());
		return "";
	}		

	// set Bank Interest Expense Acct
	protected String setBankInterestExpenseAcct(Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value, Object oldValue) {
		if(value == null)
			return "";
			
		MAccount validAccount = MAccount.get(ctx, (int)mTab.getValue("AD_Client_ID"), 0, (int)mTab.getValue("C_AcctSchema_ID"), (int)mTab.getValue("C_ElementValue_BIE_ID"), 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, null);
		mTab.setValue("B_InterestExp_Acct", validAccount.get_ID());
		return "";
	}
	
	//set Element Value
	protected String setElementValue(Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value, Object oldValue) {
		if(value == null) {
			return "";
		}
		
		int C_ValidCombination_ID = (int) value;
		MValidCombination validcombination = new MValidCombination(ctx, C_ValidCombination_ID, null);
		
		if(mField.getColumnName().equals("B_Asset_Acct"))
			mTab.setValue(MBankAccountAcct.COLUMNNAME_C_ElementValue_BA_ID, validcombination.getAccount_ID());
		else if(mField.getColumnName().equals("B_InTransit_Acct"))
			mTab.setValue(MBankAccountAcct.COLUMNNAME_C_ElementValue_BIT_ID, validcombination.getAccount_ID());
		else if(mField.getColumnName().equals("B_PaymentSelect_Acct"))
			mTab.setValue(MBankAccountAcct.COLUMNNAME_C_ElementValue_PS_ID, validcombination.getAccount_ID());
		else if(mField.getColumnName().equals("B_UnallocatedCash_Acct"))
			mTab.setValue(MBankAccountAcct.COLUMNNAME_C_ElementValue_UC_ID, validcombination.getAccount_ID());
		else if(mField.getColumnName().equals("B_InterestRev_Acct"))
			mTab.setValue(MBankAccountAcct.COLUMNNAME_C_ElementValue_BIR_ID, validcombination.getAccount_ID());
		else if(mField.getColumnName().equals("B_InterestExp_Acct"))
			mTab.setValue(MBankAccountAcct.COLUMNNAME_C_ElementValue_BIE_ID, validcombination.getAccount_ID());
		
		return "";
	}
}
