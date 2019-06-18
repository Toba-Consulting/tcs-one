package org.bpacct.callout;

import java.util.Properties;

import org.adempiere.base.IColumnCallout;
import org.bpacct.model.MBPCustomerAcct;
import org.bpacct.model.MBPVendorAcct;
import org.bpacct.model.MValidCombination;
import org.compiere.model.GridField;
import org.compiere.model.GridTab;
import org.compiere.model.MAccount;

public class CalloutBPAcct implements IColumnCallout{

	@Override
	public String start(Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value, Object oldValue) {
		// TODO Auto-generated method stub
		if (mField.getColumnName().equals(MBPCustomerAcct.COLUMNNAME_C_ElementValue_CP_ID))
			return setCustomerPrepaymentAcct(ctx, WindowNo, mTab, mField, value, oldValue);
		if (mField.getColumnName().equals(MBPCustomerAcct.COLUMNNAME_C_ElementValue_CR_ID))
			return setCustomerReceivablesAcct(ctx, WindowNo, mTab, mField, value, oldValue);
		if (mField.getColumnName().equals(MBPVendorAcct.COLUMNNAME_C_ElementValue_VL_ID))
			return setVendorLiabilityAcct(ctx, WindowNo, mTab, mField, value, oldValue);
		if (mField.getColumnName().equals(MBPVendorAcct.COLUMNNAME_C_ElementValue_VP_ID))
			return setVendorPrepaymentAcct(ctx, WindowNo, mTab, mField, value, oldValue);
		if (mField.getColumnName().equals(MBPCustomerAcct.COLUMNNAME_C_Receivable_Acct)
				|| mField.getColumnName().equals(MBPCustomerAcct.COLUMNNAME_C_Prepayment_Acct)
				|| mField.getColumnName().equals(MBPVendorAcct.COLUMNNAME_V_Liability_Acct)
				|| mField.getColumnName().equals(MBPVendorAcct.COLUMNNAME_V_Prepayment_Acct)
				)
			return setElementValue(ctx, WindowNo, mTab, mField, value, oldValue);
		return null;
	}

	//Business Partner
	//Set Customer Receivables
	protected String setCustomerReceivablesAcct(Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value, Object oldValue) {
		if (value == null) 
			return "";
		
		MAccount validAccount = MAccount.get(ctx, (int)mTab.getValue("AD_Client_ID"), 0, (int)mTab.getValue("C_AcctSchema_ID"), (int)mTab.getValue("C_ElementValue_CR_ID"), 0, 0, (int)mTab.getValue("C_BPartner_ID"), 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, null);
		mTab.setValue("C_Receivable_Acct", validAccount.get_ID());
		return "";
	}
	
	//Set Customer Prepayment
	protected String setCustomerPrepaymentAcct(Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value, Object oldValue) {
		if (value == null)
			return "";
		
		MAccount validAccount = MAccount.get(ctx, (int)mTab.getValue("AD_Client_ID"), 0, (int)mTab.getValue("C_AcctSchema_ID"), (int)mTab.getValue("C_ElementValue_CP_ID"), 0, 0, (int)mTab.getValue("C_BPartner_ID"), 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, null);
		mTab.setValue("C_Prepayment_Acct", validAccount.get_ID());
		return "";
	}
	
	//Set Vendor Liability
	protected String setVendorLiabilityAcct(Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value, Object oldValue) {
		if (value == null)
			return "";
		
		MAccount validAccount = MAccount.get(ctx, (int)mTab.getValue("AD_Client_ID"), 0, (int)mTab.getValue("C_AcctSchema_ID"), (int)mTab.getValue("C_ElementValue_VL_ID"), 0, 0, (int)mTab.getValue("C_BPartner_ID"), 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, null);
		mTab.setValue("V_Liability_Acct", validAccount.get_ID());
		return "";
	}
	
	//Set Vendor Prepayment
	protected String setVendorPrepaymentAcct(Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value, Object oldValue) {
		if (value == null)
			return "";
			
		MAccount validAccount = MAccount.get(ctx, (int)mTab.getValue("AD_Client_ID"), 0, (int)mTab.getValue("C_AcctSchema_ID"), (int)mTab.getValue("C_ElementValue_VP_ID"), 0, 0, (int)mTab.getValue("C_BPartner_ID"), 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, null);
		mTab.setValue("V_Prepayment_Acct", validAccount.get_ID());
		return "";
	}
	
	//Set Element Value
	protected String setElementValue(Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value, Object oldValue) {
		if (value == null)
			return "";
		
		int C_ValidCombination_ID = (int) value;
		MValidCombination validCombination = new MValidCombination(ctx, C_ValidCombination_ID, null);
		
		if (mField.getColumnName().equals(MBPCustomerAcct.COLUMNNAME_C_Prepayment_Acct)) {
			mTab.setValue(MBPCustomerAcct.COLUMNNAME_C_ElementValue_CP_ID, validCombination.getAccount_ID());
		}else if (mField.getColumnName().equals(MBPCustomerAcct.COLUMNNAME_C_Receivable_Acct)) {
			mTab.setValue(MBPCustomerAcct.COLUMNNAME_C_ElementValue_CR_ID, validCombination.getAccount_ID());
		}else if (mField.getColumnName().equals(MBPVendorAcct.COLUMNNAME_V_Liability_Acct)) {
			mTab.setValue(MBPVendorAcct.COLUMNNAME_C_ElementValue_VL_ID, validCombination.getAccount_ID());
		}else if (mField.getColumnName().equals(MBPVendorAcct.COLUMNNAME_V_Prepayment_Acct)) {
			mTab.setValue(MBPVendorAcct.COLUMNNAME_C_ElementValue_VP_ID, validCombination.getAccount_ID());
		}
		
		return "";
	}
	
}
