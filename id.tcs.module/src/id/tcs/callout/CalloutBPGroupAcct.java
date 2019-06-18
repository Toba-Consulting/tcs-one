package org.bpgroupacct.callout;

import java.util.Properties;

import org.adempiere.base.IColumnCallout;
import org.bpgroupacct.model.MBPGroupAcct;
import org.bpgroupacct.model.MValidCombination;
import org.compiere.model.GridField;
import org.compiere.model.GridTab;
import org.compiere.model.MAccount;

public class CalloutBPGroupAcct implements IColumnCallout{

	@Override
	public String start(Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value, Object oldValue) {
		// TODO Auto-generated method stub
		if (mField.getColumnName().equals(MBPGroupAcct.COLUMNNAME_C_ElementValue_ID)) 
			return setNotInvoicedReceiptsAcct(ctx, WindowNo, mTab, mField, value, oldValue);
		if (mField.getColumnName().equals(MBPGroupAcct.COLUMNNAME_C_ElementValue_PDE_ID))
			return setPayDiscountExpAcct(ctx, WindowNo, mTab, mField, value, oldValue);
		if (mField.getColumnName().equals(MBPGroupAcct.COLUMNNAME_C_ElementValue_PDR_ID))
			return setPayDiscountRevAcct(ctx, WindowNo, mTab, mField, value, oldValue);
		if (mField.getColumnName().equals(MBPGroupAcct.COLUMNNAME_C_ElementValue_WO_ID))
			return setWriteOffAcct(ctx, WindowNo, mTab, mField, value, oldValue);
		if (mField.getColumnName().equals(MBPGroupAcct.COLUMNNAME_C_ElementValue_CP_ID))
			return setCustomerPrepaymentAcct(ctx, WindowNo, mTab, mField, value, oldValue);
		if (mField.getColumnName().equals(MBPGroupAcct.COLUMNNAME_C_ElementValue_CR_ID))
			return setCustomerReceivableAcct(ctx, WindowNo, mTab, mField, value, oldValue);
		if (mField.getColumnName().equals(MBPGroupAcct.COLUMNNAME_C_ElementValue_VL_ID))
			return setVendorLiabilityAcct(ctx, WindowNo, mTab, mField, value, oldValue);
		if (mField.getColumnName().equals(MBPGroupAcct.COLUMNNAME_C_ElementValue_VP_ID))
			return setVendorPrepaymentAcct(ctx, WindowNo, mTab, mField, value, oldValue);
		if (mField.getColumnName().equals(MBPGroupAcct.COLUMNNAME_NotInvoicedReceipts_Acct)
				|| mField.getColumnName().equals(MBPGroupAcct.COLUMNNAME_PayDiscount_Exp_Acct)
				|| mField.getColumnName().equals(MBPGroupAcct.COLUMNNAME_PayDiscount_Rev_Acct)
				|| mField.getColumnName().equals(MBPGroupAcct.COLUMNNAME_WriteOff_Acct)
				|| mField.getColumnName().equals(MBPGroupAcct.COLUMNNAME_C_Prepayment_Acct)
				|| mField.getColumnName().equals(MBPGroupAcct.COLUMNNAME_C_Receivable_Acct)
				|| mField.getColumnName().equals(MBPGroupAcct.COLUMNNAME_V_Liability_Acct)
				|| mField.getColumnName().equals(MBPGroupAcct.COLUMNNAME_V_Prepayment_Acct))
			return setElementValue(ctx, WindowNo, mTab, mField, value, oldValue);
		return null;
	}
	
	//Business Partner Group
	//Set Not-Invoiced Receipts Acct 
	protected String setNotInvoicedReceiptsAcct(Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value, Object oldValue) {
		if (value == null)
			return "";
		
		MAccount validAccount = MAccount.get(ctx, (int)mTab.getValue("AD_Client_ID"), 0, (int)mTab.getValue("C_AcctSchema_ID"), (int)mTab.getValue("C_ElementValue_ID"), 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, null);
		mTab.setValue("NotInvoicedReceipts_Acct", validAccount.get_ID());
		return "";
	}
	
	//Set Payment Discount Expense
	protected String setPayDiscountExpAcct(Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value, Object oldValue) {
		if (value == null)
			return "";
		
		MAccount validAccount = MAccount.get(ctx, (int)mTab.getValue("AD_Client_ID"), 0, (int)mTab.getValue("C_AcctSchema_ID"), (int)mTab.getValue("C_ElementValue_PDE_ID"), 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, null);
		mTab.setValue("PayDiscount_Exp_Acct", validAccount.get_ID());
		return "";
	}
	
	//Set Payment Discount Revenue
	protected String setPayDiscountRevAcct(Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value, Object oldValue) {
		if (value == null)
			return "";
			
		MAccount validAccount = MAccount.get(ctx, (int)mTab.getValue("AD_Client_ID"), 0, (int)mTab.getValue("C_AcctSchema_ID"), (int)mTab.getValue("C_ElementValue_PDR_ID"), 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, null);
		mTab.setValue("PayDiscount_Rev_Acct", validAccount.get_ID());
		return "";
	}
	
	//Set Write-Off
	protected String setWriteOffAcct(Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value, Object oldValue) {
		if (value == null)
			return "";
				
		MAccount validAccount = MAccount.get(ctx, (int)mTab.getValue("AD_Client_ID"), 0, (int)mTab.getValue("C_AcctSchema_ID"), (int)mTab.getValue("C_ElementValue_WO_ID"), 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, null);
		mTab.setValue("WriteOff_Acct", validAccount.get_ID());
		return "";
	}
	
	//Set Customer Prepayment Acct
	protected String setCustomerPrepaymentAcct(Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value, Object oldValue) {
		if (value == null)
			return "";
					
		MAccount validAccount = MAccount.get(ctx, (int)mTab.getValue("AD_Client_ID"), 0, (int)mTab.getValue("C_AcctSchema_ID"), (int)mTab.getValue("C_ElementValue_CP_ID"), 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, null);
		mTab.setValue("C_Prepayment_Acct", validAccount.get_ID());
		return "";
	}
	
	//Set Customer Receivable Acct
	protected String setCustomerReceivableAcct(Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value, Object oldValue) {
		if (value == null)
			return "";
						
		MAccount validAccount = MAccount.get(ctx, (int)mTab.getValue("AD_Client_ID"), 0, (int)mTab.getValue("C_AcctSchema_ID"), (int)mTab.getValue("C_ElementValue_CR_ID"), 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, null);
		mTab.setValue("C_Receivable_Acct", validAccount.get_ID());
		return "";
	}
	
	//Set Vendor Liability Acct
	protected String setVendorLiabilityAcct(Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value, Object oldValue) {
		if (value == null)
			return "";
						
		MAccount validAccount = MAccount.get(ctx, (int)mTab.getValue("AD_Client_ID"), 0, (int)mTab.getValue("C_AcctSchema_ID"), (int)mTab.getValue("C_ElementValue_VL_ID"), 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, null);
		mTab.setValue("V_Liability_Acct", validAccount.get_ID());
		return "";
	}
	
	//Set Vendor Prepayment Acct
	protected String setVendorPrepaymentAcct(Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value, Object oldValue) {
		if (value == null)
			return "";
						
		MAccount validAccount = MAccount.get(ctx, (int)mTab.getValue("AD_Client_ID"), 0, (int)mTab.getValue("C_AcctSchema_ID"), (int)mTab.getValue("C_ElementValue_VP_ID"), 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, null);
		mTab.setValue("V_Prepayment_Acct", validAccount.get_ID());
		return "";
	}
	
	//Set Element Value
	protected String setElementValue(Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value, Object oldValue) {
		
		if(value == null) {
			return "";
		}
		
		int C_ValidCombination_ID = (int) value;
		MValidCombination validCombination = new MValidCombination(ctx, C_ValidCombination_ID, null);
		
		if (mField.getColumnName().equals("NotInvoicedReceipts_Acct")) {
			mTab.setValue(MBPGroupAcct.COLUMNNAME_C_ElementValue_ID, validCombination.getAccount_ID());
		}else if(mField.getColumnName().equals("PayDiscount_Exp_Acct")) {
			mTab.setValue(MBPGroupAcct.COLUMNNAME_C_ElementValue_PDE_ID, validCombination.getAccount_ID());
		}else if(mField.getColumnName().equals("PayDiscount_Rev_Acct")) {
			mTab.setValue(MBPGroupAcct.COLUMNNAME_C_ElementValue_PDR_ID, validCombination.getAccount_ID());
		}else if(mField.getColumnName().equals("WriteOff_Acct")) {
			mTab.setValue(MBPGroupAcct.COLUMNNAME_C_ElementValue_WO_ID, validCombination.getAccount_ID());
		}else if(mField.getColumnName().equals("C_Prepayment_Acct")) {
			mTab.setValue(MBPGroupAcct.COLUMNNAME_C_ElementValue_CP_ID, validCombination.getAccount_ID());
		}else if(mField.getColumnName().equals("C_Receivable_Acct")) {
			mTab.setValue(MBPGroupAcct.COLUMNNAME_C_ElementValue_CR_ID, validCombination.getAccount_ID());
		}else if(mField.getColumnName().equals("V_Liability_Acct")) {
			mTab.setValue(MBPGroupAcct.COLUMNNAME_C_ElementValue_VL_ID, validCombination.getAccount_ID());
		}else if(mField.getColumnName().equals("V_Prepayment_Acct")) {
			mTab.setValue(MBPGroupAcct.COLUMNNAME_C_ElementValue_VP_ID, validCombination.getAccount_ID());
		}
		return "";
	}

}
