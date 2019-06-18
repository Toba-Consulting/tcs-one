package org.banktransfer.callout;

import java.util.Properties;

import org.adempiere.base.IColumnCallout;
import org.banktransfer.model.MAcctSchemaDefault;
import org.banktransfer.model.MAcctSchemaGL;
import org.banktransfer.model.MValidCombination;
import org.compiere.model.GridField;
import org.compiere.model.GridTab;
import org.compiere.model.MAccount;

public class CalloutAccountingSchemaDefault implements IColumnCallout{

	@Override
	public String start(Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value, Object oldValue) {
		// TODO Auto-generated method stub
		if (mField.getColumnName().equals(MAcctSchemaDefault.COLUMNNAME_C_ElementValue_UG_ID))
			return setUnrealizedGainAcct(ctx, WindowNo, mTab, mField, value, oldValue);
		if (mField.getColumnName().equals(MAcctSchemaDefault.COLUMNNAME_C_ElementValue_UL_ID))
			return setUnrealizedLossAcct(ctx, WindowNo, mTab, mField, value, oldValue);
		if (mField.getColumnName().equals(MAcctSchemaDefault.COLUMNNAME_C_ElementValue_RG_ID))
			return setRealizedGainAcct(ctx, WindowNo, mTab, mField, value, oldValue);
		if (mField.getColumnName().equals(MAcctSchemaDefault.COLUMNNAME_C_ElementValue_RL_ID))
			return setRealizedLossAcct(ctx, WindowNo, mTab, mField, value, oldValue);
		if (mField.getColumnName().equals(MAcctSchemaDefault.COLUMNNAME_C_ElementValue_ID))
			return setNotInvoicedReceiptsAcct(ctx, WindowNo, mTab, mField, value, oldValue);
		if (mField.getColumnName().equals(MAcctSchemaDefault.COLUMNNAME_C_ElementValue_PDE_ID))
			return setPaymentDiscountExpenseAcct(ctx, WindowNo, mTab, mField, value, oldValue);
		if (mField.getColumnName().equals(MAcctSchemaDefault.COLUMNNAME_C_ElementValue_PDR_ID))
			return setPaymentDiscountRevenueAcct(ctx, WindowNo, mTab, mField, value, oldValue);
		if (mField.getColumnName().equals(MAcctSchemaDefault.COLUMNNAME_C_ElementValue_WO_ID))
			return setWriteOffAcct(ctx, WindowNo, mTab, mField, value, oldValue);
		if (mField.getColumnName().equals(MAcctSchemaDefault.COLUMNNAME_C_ElementValue_CR_ID))
			return setCustomerReceivableAcct(ctx, WindowNo, mTab, mField, value, oldValue);
		if (mField.getColumnName().equals(MAcctSchemaDefault.COLUMNNAME_C_ElementValue_VL_ID))
			return setVendorLiabilityAcct(ctx, WindowNo, mTab, mField, value, oldValue);
		if (mField.getColumnName().equals(MAcctSchemaDefault.COLUMNNAME_C_ElementValue_CP_ID))
			return setCustomerPrepaymentAcct(ctx, WindowNo, mTab, mField, value, oldValue);
		if (mField.getColumnName().equals(MAcctSchemaDefault.COLUMNNAME_C_ElementValue_VP_ID))
			return setVendorPrepaymentAcct(ctx, WindowNo, mTab, mField, value, oldValue);
		if (mField.getColumnName().equals(MAcctSchemaDefault.COLUMNNAME_C_ElementValue_Asset_ID))
			return setProductAssetAcct(ctx, WindowNo, mTab, mField, value, oldValue);
		if (mField.getColumnName().equals(MAcctSchemaDefault.COLUMNNAME_C_ElementValue_PE_ID))
			return setProductExpenseAcct(ctx, WindowNo, mTab, mField, value, oldValue);
		if (mField.getColumnName().equals(MAcctSchemaDefault.COLUMNNAME_C_ElementValue_CA_ID))
			return setCostAdjustmentAcct(ctx, WindowNo, mTab, mField, value, oldValue);
		if (mField.getColumnName().equals(MAcctSchemaDefault.COLUMNNAME_C_ElementValue_IC_ID))
			return setInventoryClearingAcct(ctx, WindowNo, mTab, mField, value, oldValue);
		if (mField.getColumnName().equals(MAcctSchemaDefault.COLUMNNAME_C_ElementValue_Revenue_ID))
			return setProductRevenueAcct(ctx, WindowNo, mTab, mField, value, oldValue);
		if (mField.getColumnName().equals(MAcctSchemaDefault.COLUMNNAME_C_ElementValue_COGS_ID))
			return setProductCOGSAcct(ctx, WindowNo, mTab, mField, value, oldValue);
		if (mField.getColumnName().equals(MAcctSchemaDefault.COLUMNNAME_C_ElementValue_PPV_ID))
			return setPurchasePriceVarianceAcct(ctx, WindowNo, mTab, mField, value, oldValue);
		if (mField.getColumnName().equals(MAcctSchemaDefault.COLUMNNAME_C_ElementValue_IPV_ID))
			return setInvoicePriceVarianceAcct(ctx, WindowNo, mTab, mField, value, oldValue);
		if (mField.getColumnName().equals(MAcctSchemaDefault.COLUMNNAME_C_ElementValue_TDR_ID))
			return setTradeDiscountReceivedAcct(ctx, WindowNo, mTab, mField, value, oldValue);
		if (mField.getColumnName().equals(MAcctSchemaDefault.COLUMNNAME_C_ElementValue_TDG_ID))
			return setTradeDiscountGrantedAcct(ctx, WindowNo, mTab, mField, value, oldValue);
		if (mField.getColumnName().equals(MAcctSchemaDefault.COLUMNNAME_C_ElementValue_RV_ID))
			return setRateVarianceAcct(ctx, WindowNo, mTab, mField, value, oldValue);
		if (mField.getColumnName().equals(MAcctSchemaDefault.COLUMNNAME_C_ElementValue_ACV_ID))
			return setAverageCostVarianceAcct(ctx, WindowNo, mTab, mField, value, oldValue);
		if (mField.getColumnName().equals(MAcctSchemaDefault.COLUMNNAME_C_ElementValue_LCC_ID))
			return setLandedCostClearingAcct(ctx, WindowNo, mTab, mField, value, oldValue);
		if (mField.getColumnName().equals(MAcctSchemaDefault.COLUMNNAME_C_ElementValue_WD_ID))
			return setWarehouseDiffAcct(ctx, WindowNo, mTab, mField, value, oldValue);
		if (mField.getColumnName().equals(MAcctSchemaDefault.COLUMNNAME_C_ElementValue_BA_ID))
			return setBankAssetAcct(ctx, WindowNo, mTab, mField, value, oldValue);
		if (mField.getColumnName().equals(MAcctSchemaDefault.COLUMNNAME_C_ElementValue_BIT_ID))
			return setBankInTransitAcct(ctx, WindowNo, mTab, mField, value, oldValue);
		if (mField.getColumnName().equals(MAcctSchemaDefault.COLUMNNAME_C_ElementValue_PS_ID))
			return setPaymentSelectAcct(ctx, WindowNo, mTab, mField, value, oldValue);
		if (mField.getColumnName().equals(MAcctSchemaDefault.COLUMNNAME_C_ElementValue_UC_ID))
			return setUnallocatedCashAcct(ctx, WindowNo, mTab, mField, value, oldValue);
		if (mField.getColumnName().equals(MAcctSchemaDefault.COLUMNNAME_C_ElementValue_BIR_ID))
			return setBankInterestRevenueAcct(ctx, WindowNo, mTab, mField, value, oldValue);
		if (mField.getColumnName().equals(MAcctSchemaDefault.COLUMNNAME_C_ElementValue_BIE_ID))
			return setBankInterestExpenseAcct(ctx, WindowNo, mTab, mField, value, oldValue);
		if (mField.getColumnName().equals(MAcctSchemaDefault.COLUMNNAME_C_ElementValue_TD_ID))
			return setTaxDueAcct(ctx, WindowNo, mTab, mField, value, oldValue);
		if (mField.getColumnName().equals(MAcctSchemaDefault.COLUMNNAME_C_ElementValue_TC_ID))
			return setTaxCreditAcct(ctx, WindowNo, mTab, mField, value, oldValue);
		if (mField.getColumnName().equals(MAcctSchemaDefault.COLUMNNAME_C_ElementValue_TE_ID))
			return setTaxExpenseAcct(ctx, WindowNo, mTab, mField, value, oldValue);
		if (mField.getColumnName().equals(MAcctSchemaDefault.COLUMNNAME_C_ElementValue_Charge_ID))
			return setChargeAcct(ctx, WindowNo, mTab, mField, value, oldValue);
		if (mField.getColumnName().equals(MAcctSchemaDefault.COLUMNNAME_C_ElementValue_PA_ID))
			return setProjectAssetAcct(ctx, WindowNo, mTab, mField, value, oldValue);
		if (mField.getColumnName().equals(MAcctSchemaDefault.COLUMNNAME_C_ElementValue_WIP_ID))
			return setWorkInProgressAcct(ctx, WindowNo, mTab, mField, value, oldValue);
		if (mField.getColumnName().equals(MAcctSchemaDefault.COLUMNNAME_UnrealizedGain_Acct) 
				|| mField.getColumnName().equals(MAcctSchemaDefault.COLUMNNAME_UnrealizedLoss_Acct)
				|| mField.getColumnName().equals(MAcctSchemaDefault.COLUMNNAME_RealizedGain_Acct)
				|| mField.getColumnName().equals(MAcctSchemaDefault.COLUMNNAME_RealizedLoss_Acct)
				|| mField.getColumnName().equals(MAcctSchemaDefault.COLUMNNAME_NotInvoicedReceipts_Acct)
				|| mField.getColumnName().equals(MAcctSchemaDefault.COLUMNNAME_PayDiscount_Exp_Acct)
				|| mField.getColumnName().equals(MAcctSchemaDefault.COLUMNNAME_PayDiscount_Rev_Acct)
				|| mField.getColumnName().equals(MAcctSchemaDefault.COLUMNNAME_WriteOff_Acct)
				|| mField.getColumnName().equals(MAcctSchemaDefault.COLUMNNAME_C_Receivable_Acct)
				|| mField.getColumnName().equals(MAcctSchemaDefault.COLUMNNAME_V_Liability_Acct)
				|| mField.getColumnName().equals(MAcctSchemaDefault.COLUMNNAME_C_Prepayment_Acct)
				|| mField.getColumnName().equals(MAcctSchemaDefault.COLUMNNAME_V_Prepayment_Acct)
				|| mField.getColumnName().equals(MAcctSchemaDefault.COLUMNNAME_P_Asset_Acct)
				|| mField.getColumnName().equals(MAcctSchemaDefault.COLUMNNAME_P_Expense_Acct)
				|| mField.getColumnName().equals(MAcctSchemaDefault.COLUMNNAME_P_CostAdjustment_Acct)
				|| mField.getColumnName().equals(MAcctSchemaDefault.COLUMNNAME_P_InventoryClearing_Acct)
				|| mField.getColumnName().equals(MAcctSchemaDefault.COLUMNNAME_P_Revenue_Acct)
				|| mField.getColumnName().equals(MAcctSchemaDefault.COLUMNNAME_P_COGS_Acct)
				|| mField.getColumnName().equals(MAcctSchemaDefault.COLUMNNAME_P_PurchasePriceVariance_Acct)
				|| mField.getColumnName().equals(MAcctSchemaDefault.COLUMNNAME_P_InvoicePriceVariance_Acct)
				|| mField.getColumnName().equals(MAcctSchemaDefault.COLUMNNAME_P_TradeDiscountRec_Acct)
				|| mField.getColumnName().equals(MAcctSchemaDefault.COLUMNNAME_P_TradeDiscountGrant_Acct)
				|| mField.getColumnName().equals(MAcctSchemaDefault.COLUMNNAME_P_RateVariance_Acct)
				|| mField.getColumnName().equals(MAcctSchemaDefault.COLUMNNAME_P_AverageCostVariance_Acct)
				|| mField.getColumnName().equals(MAcctSchemaDefault.COLUMNNAME_P_LandedCostClearing_Acct)
				|| mField.getColumnName().equals(MAcctSchemaDefault.COLUMNNAME_W_Differences_Acct)
				|| mField.getColumnName().equals(MAcctSchemaDefault.COLUMNNAME_B_Asset_Acct)
				|| mField.getColumnName().equals(MAcctSchemaDefault.COLUMNNAME_B_InTransit_Acct)
				|| mField.getColumnName().equals(MAcctSchemaDefault.COLUMNNAME_B_PaymentSelect_Acct)
				|| mField.getColumnName().equals(MAcctSchemaDefault.COLUMNNAME_B_UnallocatedCash_Acct)
				|| mField.getColumnName().equals(MAcctSchemaDefault.COLUMNNAME_B_InterestRev_Acct)
				|| mField.getColumnName().equals(MAcctSchemaDefault.COLUMNNAME_B_InterestExp_Acct)
				|| mField.getColumnName().equals(MAcctSchemaDefault.COLUMNNAME_T_Due_Acct)
				|| mField.getColumnName().equals(MAcctSchemaDefault.COLUMNNAME_T_Credit_Acct)
				|| mField.getColumnName().equals(MAcctSchemaDefault.COLUMNNAME_T_Expense_Acct)
				|| mField.getColumnName().equals(MAcctSchemaDefault.COLUMNNAME_Ch_Expense_Acct)
				|| mField.getColumnName().equals(MAcctSchemaDefault.COLUMNNAME_PJ_Asset_Acct)
				|| mField.getColumnName().equals(MAcctSchemaDefault.COLUMNNAME_PJ_WIP_Acct))
			return setElementValue(ctx, WindowNo, mTab, mField, value, oldValue);
			
		return null;
	}

	
	// set Unrealized Gain Acct
	protected String setUnrealizedGainAcct(Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value, Object oldValue) {
		if(value == null)
			return "";
		
		MAccount validAccount = MAccount.get(ctx, (int)mTab.getValue("AD_Client_ID"), 0, (int)mTab.getValue("C_AcctSchema_ID"), (int)mTab.getValue("C_ElementValue_UG_ID"), 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, null);
		mTab.setValue("UnrealizedGain_Acct", validAccount.get_ID());
		return "";
	}
	
	// set Unrealized Loss Acct
	protected String setUnrealizedLossAcct(Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value, Object oldValue) {
		if(value == null)
			return "";
		
		MAccount validAccount = MAccount.get(ctx, (int)mTab.getValue("AD_Client_ID"), 0, (int)mTab.getValue("C_AcctSchema_ID"), (int)mTab.getValue("C_ElementValue_UL_ID"), 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, null);
		mTab.setValue("UnrealizedLost_Acct", validAccount.get_ID());
		return "";
	}
	
	// set Realized Gain Acct
	protected String setRealizedGainAcct(Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value, Object oldValue) {
		if(value == null)
			return "";
			
		MAccount validAccount = MAccount.get(ctx, (int)mTab.getValue("AD_Client_ID"), 0, (int)mTab.getValue("C_AcctSchema_ID"), (int)mTab.getValue("C_ElementValue_RG_ID"), 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, null);
		mTab.setValue("RealizedGain_Acct", validAccount.get_ID());
		return "";
	}
	
	// set Realized Loss Acct
	protected String setRealizedLossAcct(Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value, Object oldValue) {
		if(value == null)
			return "";
			
		MAccount validAccount = MAccount.get(ctx, (int)mTab.getValue("AD_Client_ID"), 0, (int)mTab.getValue("C_AcctSchema_ID"), (int)mTab.getValue("C_ElementValue_RL_ID"), 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, null);
		mTab.setValue("RealizedLoss_Acct", validAccount.get_ID());
		return "";
	}	
	
	// set Not Invoiced Receipts Acct 
	protected String setNotInvoicedReceiptsAcct(Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value, Object oldValue) {
		if(value == null)
			return "";
			
		MAccount validAccount = MAccount.get(ctx, (int)mTab.getValue("AD_Client_ID"), 0, (int)mTab.getValue("C_AcctSchema_ID"), (int)mTab.getValue("C_ElementValue_ID"), 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, null);
		mTab.setValue("NotInvoicedReceipts_Acct", validAccount.get_ID());
		return "";
	}	

	// set Payment Discount Expense Acct 
	protected String setPaymentDiscountExpenseAcct(Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value, Object oldValue) {
		if(value == null)
			return "";
			
		MAccount validAccount = MAccount.get(ctx, (int)mTab.getValue("AD_Client_ID"), 0, (int)mTab.getValue("C_AcctSchema_ID"), (int)mTab.getValue("C_ElementValue_PDE_ID"), 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, null);
		mTab.setValue("PayDiscount_Exp_Acct", validAccount.get_ID());
		return "";
	}		

	// set Payment Discount Revenue Acct 
	protected String setPaymentDiscountRevenueAcct(Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value, Object oldValue) {
		if(value == null)
			return "";
			
		MAccount validAccount = MAccount.get(ctx, (int)mTab.getValue("AD_Client_ID"), 0, (int)mTab.getValue("C_AcctSchema_ID"), (int)mTab.getValue("C_ElementValue_PDR_ID"), 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, null);
		mTab.setValue("PayDiscount_Rev_Acct", validAccount.get_ID());
		return "";
	}		
	
	// set Write-Off Acct 
	protected String setWriteOffAcct(Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value, Object oldValue) {
		if(value == null)
			return "";
			
		MAccount validAccount = MAccount.get(ctx, (int)mTab.getValue("AD_Client_ID"), 0, (int)mTab.getValue("C_AcctSchema_ID"), (int)mTab.getValue("C_ElementValue_WO_ID"), 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, null);
		mTab.setValue("WriteOff_Acct", validAccount.get_ID());
		return "";
	}	
	
	// set Customer Receivable Acct 
	protected String setCustomerReceivableAcct(Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value, Object oldValue) {
		if(value == null)
			return "";
			
		MAccount validAccount = MAccount.get(ctx, (int)mTab.getValue("AD_Client_ID"), 0, (int)mTab.getValue("C_AcctSchema_ID"), (int)mTab.getValue("C_ElementValue_CR_ID"), 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, null);
		mTab.setValue("C_Receivable_Acct", validAccount.get_ID());
		return "";
	}	

	// set Vendor Liability Acct 
	protected String setVendorLiabilityAcct(Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value, Object oldValue) {
		if(value == null)
			return "";
			
		MAccount validAccount = MAccount.get(ctx, (int)mTab.getValue("AD_Client_ID"), 0, (int)mTab.getValue("C_AcctSchema_ID"), (int)mTab.getValue("C_ElementValue_VL_ID"), 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, null);
		mTab.setValue("V_Liability_Acct", validAccount.get_ID());
		return "";
	}		
	
	// set Customer Prepayment Acct 
	protected String setCustomerPrepaymentAcct(Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value, Object oldValue) {
		if(value == null)
			return "";
			
		MAccount validAccount = MAccount.get(ctx, (int)mTab.getValue("AD_Client_ID"), 0, (int)mTab.getValue("C_AcctSchema_ID"), (int)mTab.getValue("C_ElementValue_CP_ID"), 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, null);
		mTab.setValue("C_Prepayment_Acct", validAccount.get_ID());
		return "";
	}			

	// set Vendor Prepayment Acct 
	protected String setVendorPrepaymentAcct(Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value, Object oldValue) {
		if(value == null)
			return "";
			
		MAccount validAccount = MAccount.get(ctx, (int)mTab.getValue("AD_Client_ID"), 0, (int)mTab.getValue("C_AcctSchema_ID"), (int)mTab.getValue("C_ElementValue_VP_ID"), 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, null);
		mTab.setValue("V_Prepayment_Acct", validAccount.get_ID());
		return "";
	}	

	// set Product Asset Acct 
	protected String setProductAssetAcct(Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value, Object oldValue) {
		if(value == null)
			return "";
			
		MAccount validAccount = MAccount.get(ctx, (int)mTab.getValue("AD_Client_ID"), 0, (int)mTab.getValue("C_AcctSchema_ID"), (int)mTab.getValue("C_ElementValue_Asset_ID"), 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, null);
		mTab.setValue("P_Asset_Acct", validAccount.get_ID());
		return "";
	}		 
	
	// set Product Asset Acct 
	protected String setProductExpenseAcct(Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value, Object oldValue) {
		if(value == null)
			return "";
			
		MAccount validAccount = MAccount.get(ctx, (int)mTab.getValue("AD_Client_ID"), 0, (int)mTab.getValue("C_AcctSchema_ID"), (int)mTab.getValue("C_ElementValue_PE_ID"), 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, null);
		mTab.setValue("P_Expense_Acct", validAccount.get_ID());
		return "";
	}		
	
	// set Cost Adjusment Acct 
	protected String setCostAdjustmentAcct(Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value, Object oldValue) {
		if(value == null)
			return "";
			
		MAccount validAccount = MAccount.get(ctx, (int)mTab.getValue("AD_Client_ID"), 0, (int)mTab.getValue("C_AcctSchema_ID"), (int)mTab.getValue("C_ElementValue_CA_ID"), 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, null);
		mTab.setValue("P_CostAdjustment_Acct", validAccount.get_ID());
		return "";
	}	 	
	
	// set Inventory Clearing Acct 
	protected String setInventoryClearingAcct(Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value, Object oldValue) {
		if(value == null)
			return "";
			
		MAccount validAccount = MAccount.get(ctx, (int)mTab.getValue("AD_Client_ID"), 0, (int)mTab.getValue("C_AcctSchema_ID"), (int)mTab.getValue("C_ElementValue_IC_ID"), 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, null);
		mTab.setValue("P_InventoryClearing_Acct", validAccount.get_ID());
		return "";
	}	
	
	// set Product Revenue Acct 
	protected String setProductRevenueAcct(Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value, Object oldValue) {
		if(value == null)
			return "";
			
		MAccount validAccount = MAccount.get(ctx, (int)mTab.getValue("AD_Client_ID"), 0, (int)mTab.getValue("C_AcctSchema_ID"), (int)mTab.getValue("C_ElementValue_Revenue_ID"), 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, null);
		mTab.setValue("P_Revenue_Acct", validAccount.get_ID());
		return "";
	}	
	
	// set Product COGS Acct 
	protected String setProductCOGSAcct(Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value, Object oldValue) {
		if(value == null)
			return "";
			
		MAccount validAccount = MAccount.get(ctx, (int)mTab.getValue("AD_Client_ID"), 0, (int)mTab.getValue("C_AcctSchema_ID"), (int)mTab.getValue("C_ElementValue_COGS_ID"), 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, null);
		mTab.setValue("P_COGS_Acct", validAccount.get_ID());
		return "";
	}	 

	// set Purchase Price Variance Acct 
	protected String setPurchasePriceVarianceAcct(Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value, Object oldValue) {
		if(value == null)
			return "";
			
		MAccount validAccount = MAccount.get(ctx, (int)mTab.getValue("AD_Client_ID"), 0, (int)mTab.getValue("C_AcctSchema_ID"), (int)mTab.getValue("C_ElementValue_PPV_ID"), 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, null);
		mTab.setValue("P_PurchasePriceVariance_Acct", validAccount.get_ID());
		return "";
	}		

	// set Invioce Price Variance Acct 
	protected String setInvoicePriceVarianceAcct(Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value, Object oldValue) {
		if(value == null)
			return "";
			
		MAccount validAccount = MAccount.get(ctx, (int)mTab.getValue("AD_Client_ID"), 0, (int)mTab.getValue("C_AcctSchema_ID"), (int)mTab.getValue("C_ElementValue_IPV_ID"), 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, null);
		mTab.setValue("P_InvoicePriceVariance_Acct", validAccount.get_ID());
		return "";
	}	
	
	// set Trade Discount Received Acct 
	protected String setTradeDiscountReceivedAcct(Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value, Object oldValue) {
		if(value == null)
			return "";
			
		MAccount validAccount = MAccount.get(ctx, (int)mTab.getValue("AD_Client_ID"), 0, (int)mTab.getValue("C_AcctSchema_ID"), (int)mTab.getValue("C_ElementValue_TDR_ID"), 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, null);
		mTab.setValue("P_TradeDiscountRec_Acct", validAccount.get_ID());
		return "";
	}		

	// set Trade Discount Granted Acct 
	protected String setTradeDiscountGrantedAcct(Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value, Object oldValue) {
		if(value == null)
			return "";
			
		MAccount validAccount = MAccount.get(ctx, (int)mTab.getValue("AD_Client_ID"), 0, (int)mTab.getValue("C_AcctSchema_ID"), (int)mTab.getValue("C_ElementValue_TDG_ID"), 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, null);
		mTab.setValue("P_TradeDiscountGrant_Acct", validAccount.get_ID());
		return "";
	}	
	
	// set Rate Variance Acct 
	protected String setRateVarianceAcct(Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value, Object oldValue) {
		if(value == null)
			return "";
			
		MAccount validAccount = MAccount.get(ctx, (int)mTab.getValue("AD_Client_ID"), 0, (int)mTab.getValue("C_AcctSchema_ID"), (int)mTab.getValue("C_ElementValue_RV_ID"), 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, null);
		mTab.setValue("P_RateVariance_Acct", validAccount.get_ID());
		return "";
	}	 
	
	// set Average Cost Variance Acct 
	protected String setAverageCostVarianceAcct(Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value, Object oldValue) {
		if(value == null)
			return "";
			
		MAccount validAccount = MAccount.get(ctx, (int)mTab.getValue("AD_Client_ID"), 0, (int)mTab.getValue("C_AcctSchema_ID"), (int)mTab.getValue("C_ElementValue_ACV_ID"), 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, null);
		mTab.setValue("P_AverageCostVariance_Acct", validAccount.get_ID());
		return "";
	}	

	// set Landed Cost Clearing Acct 
	protected String setLandedCostClearingAcct(Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value, Object oldValue) {
		if(value == null)
			return "";
			
		MAccount validAccount = MAccount.get(ctx, (int)mTab.getValue("AD_Client_ID"), 0, (int)mTab.getValue("C_AcctSchema_ID"), (int)mTab.getValue("C_ElementValue_LCC_ID"), 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, null);
		mTab.setValue("P_LandedCostClearing_Acct", validAccount.get_ID());
		return "";
	}		
	
	// set Warehouse Differences Acct 
	protected String setWarehouseDiffAcct(Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value, Object oldValue) {
		if(value == null)
			return "";
			
		MAccount validAccount = MAccount.get(ctx, (int)mTab.getValue("AD_Client_ID"), 0, (int)mTab.getValue("C_AcctSchema_ID"), (int)mTab.getValue("C_ElementValue_WD_ID"), 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, null);
		mTab.setValue("W_Differences_Acct", validAccount.get_ID());
		return "";
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
	
	// set Tax Due Acct
	protected String setTaxDueAcct(Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value, Object oldValue) {
		if(value == null)
			return "";
			
		MAccount validAccount = MAccount.get(ctx, (int)mTab.getValue("AD_Client_ID"), 0, (int)mTab.getValue("C_AcctSchema_ID"), (int)mTab.getValue("C_ElementValue_TD_ID"), 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, null);
		mTab.setValue("T_Due_Acct", validAccount.get_ID());
		return "";
	}	
	
	// set Tax Credit Acct
	protected String setTaxCreditAcct(Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value, Object oldValue) {
		if(value == null)
			return "";
			
		MAccount validAccount = MAccount.get(ctx, (int)mTab.getValue("AD_Client_ID"), 0, (int)mTab.getValue("C_AcctSchema_ID"), (int)mTab.getValue("C_ElementValue_TC_ID"), 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, null);
		mTab.setValue("T_Credit_Acct", validAccount.get_ID());
		return "";
	}	

	// set Tax Expense Acct
	protected String setTaxExpenseAcct(Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value, Object oldValue) {
		if(value == null)
			return "";
			
		MAccount validAccount = MAccount.get(ctx, (int)mTab.getValue("AD_Client_ID"), 0, (int)mTab.getValue("C_AcctSchema_ID"), (int)mTab.getValue("C_ElementValue_TE_ID"), 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, null);
		mTab.setValue("T_Expense_Acct", validAccount.get_ID());
		return "";
	}		 
	
	// set Charge Acct
	protected String setChargeAcct(Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value, Object oldValue) {
		if(value == null)
			return "";
			
		MAccount validAccount = MAccount.get(ctx, (int)mTab.getValue("AD_Client_ID"), 0, (int)mTab.getValue("C_AcctSchema_ID"), (int)mTab.getValue("C_ElementValue_Charge_ID"), 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, null);
		mTab.setValue("Ch_Expense_Acct", validAccount.get_ID());
		return "";
	}		
	
	// set Project Asset Acct
	protected String setProjectAssetAcct(Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value, Object oldValue) {
		if(value == null)
			return "";
			
		MAccount validAccount = MAccount.get(ctx, (int)mTab.getValue("AD_Client_ID"), 0, (int)mTab.getValue("C_AcctSchema_ID"), (int)mTab.getValue("C_ElementValue_PA_ID"), 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, null);
		mTab.setValue("PJ_Asset_Acct", validAccount.get_ID());
		return "";
	}	
	
	// set Work In Progress Acct
	protected String setWorkInProgressAcct(Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value, Object oldValue) {
		if(value == null)
			return "";
			
		MAccount validAccount = MAccount.get(ctx, (int)mTab.getValue("AD_Client_ID"), 0, (int)mTab.getValue("C_AcctSchema_ID"), (int)mTab.getValue("C_ElementValue_WIP_ID"), 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, null);
		mTab.setValue("PJ_WIP_Acct", validAccount.get_ID());
		return "";
	}	
	
	//set Element Value
	protected String setElementValue(Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value, Object oldValue) {
		if(value == null) {
			return "";
		}
		
		int C_ValidCombination_ID = (int) value;
		MValidCombination validcombination = new MValidCombination(ctx, C_ValidCombination_ID, null);
		
		if(mField.getColumnName().equals("UnrealizedGain_Acct"))
			mTab.setValue(MAcctSchemaDefault.COLUMNNAME_C_ElementValue_UG_ID, validcombination.getAccount_ID());
		else if(mField.getColumnName().equals("UnrealizedLoss_Acct"))
			mTab.setValue(MAcctSchemaDefault.COLUMNNAME_C_ElementValue_UL_ID, validcombination.getAccount_ID());
		else if(mField.getColumnName().equals("RealizedGain_Acct"))
			mTab.setValue(MAcctSchemaDefault.COLUMNNAME_C_ElementValue_RG_ID, validcombination.getAccount_ID());
		else if(mField.getColumnName().equals("RealizedLoss_Acct"))
			mTab.setValue(MAcctSchemaDefault.COLUMNNAME_C_ElementValue_RL_ID, validcombination.getAccount_ID());
		else if(mField.getColumnName().equals("NotInvoicedReceipts_Acct"))
			mTab.setValue(MAcctSchemaDefault.COLUMNNAME_C_ElementValue_ID, validcombination.getAccount_ID());
		else if(mField.getColumnName().equals("PayDiscount_Exp_Acct"))
			mTab.setValue(MAcctSchemaDefault.COLUMNNAME_C_ElementValue_PDE_ID, validcombination.getAccount_ID());
		else if(mField.getColumnName().equals("PayDiscount_Rev_Acct"))
			mTab.setValue(MAcctSchemaDefault.COLUMNNAME_C_ElementValue_PDR_ID, validcombination.getAccount_ID());
		else if(mField.getColumnName().equals("WriteOff_Acct"))
			mTab.setValue(MAcctSchemaDefault.COLUMNNAME_C_ElementValue_WO_ID, validcombination.getAccount_ID());
		else if(mField.getColumnName().equals("C_Receivable_Acct"))
			mTab.setValue(MAcctSchemaDefault.COLUMNNAME_C_ElementValue_CR_ID, validcombination.getAccount_ID());
		else if(mField.getColumnName().equals("V_Liability_Acct"))
			mTab.setValue(MAcctSchemaDefault.COLUMNNAME_C_ElementValue_VL_ID, validcombination.getAccount_ID());
		else if(mField.getColumnName().equals("C_Prepayment_Acct"))
			mTab.setValue(MAcctSchemaDefault.COLUMNNAME_C_ElementValue_CP_ID, validcombination.getAccount_ID());
		else if(mField.getColumnName().equals("V_Prepayment_Acct"))
			mTab.setValue(MAcctSchemaDefault.COLUMNNAME_C_ElementValue_VP_ID, validcombination.getAccount_ID());
		else if(mField.getColumnName().equals("P_Asset_Acct"))
			mTab.setValue(MAcctSchemaDefault.COLUMNNAME_C_ElementValue_Asset_ID, validcombination.getAccount_ID());
		else if(mField.getColumnName().equals("P_Expense_Acct"))
			mTab.setValue(MAcctSchemaDefault.COLUMNNAME_C_ElementValue_PE_ID, validcombination.getAccount_ID());
		else if(mField.getColumnName().equals("P_CostAdjustment_Acct"))
			mTab.setValue(MAcctSchemaDefault.COLUMNNAME_C_ElementValue_CA_ID, validcombination.getAccount_ID());
		else if(mField.getColumnName().equals("P_InventoryClearing_Acct"))
			mTab.setValue(MAcctSchemaDefault.COLUMNNAME_C_ElementValue_IC_ID, validcombination.getAccount_ID());
		else if(mField.getColumnName().equals("P_Revenue_Acct"))
			mTab.setValue(MAcctSchemaDefault.COLUMNNAME_C_ElementValue_Revenue_ID, validcombination.getAccount_ID());
		else if(mField.getColumnName().equals("P_COGS_Acct"))
			mTab.setValue(MAcctSchemaDefault.COLUMNNAME_C_ElementValue_COGS_ID, validcombination.getAccount_ID());
		else if(mField.getColumnName().equals("P_PurchasePriceVariance_Acct"))
			mTab.setValue(MAcctSchemaDefault.COLUMNNAME_C_ElementValue_PPV_ID, validcombination.getAccount_ID());
		else if(mField.getColumnName().equals("P_InvoicePriceVariance_Acct"))
			mTab.setValue(MAcctSchemaDefault.COLUMNNAME_C_ElementValue_IPV_ID, validcombination.getAccount_ID());
		else if(mField.getColumnName().equals("P_TradeDiscountRec_Acct"))
			mTab.setValue(MAcctSchemaDefault.COLUMNNAME_C_ElementValue_TDR_ID, validcombination.getAccount_ID());
		else if(mField.getColumnName().equals("P_TradeDiscountGrant_Acct"))
			mTab.setValue(MAcctSchemaDefault.COLUMNNAME_C_ElementValue_TDG_ID, validcombination.getAccount_ID());
		else if(mField.getColumnName().equals("P_RateVariance_Acct"))
			mTab.setValue(MAcctSchemaDefault.COLUMNNAME_C_ElementValue_RV_ID, validcombination.getAccount_ID());
		else if(mField.getColumnName().equals("P_AverageCostVariance_Acct"))
			mTab.setValue(MAcctSchemaDefault.COLUMNNAME_C_ElementValue_ACV_ID, validcombination.getAccount_ID());
		else if(mField.getColumnName().equals("P_LandedCostClearing_Acct"))
			mTab.setValue(MAcctSchemaDefault.COLUMNNAME_C_ElementValue_LCC_ID, validcombination.getAccount_ID());
		else if(mField.getColumnName().equals("W_Differences_Acct"))
			mTab.setValue(MAcctSchemaDefault.COLUMNNAME_C_ElementValue_WD_ID, validcombination.getAccount_ID());
		else if(mField.getColumnName().equals("B_Asset_Acct"))
			mTab.setValue(MAcctSchemaDefault.COLUMNNAME_C_ElementValue_BA_ID, validcombination.getAccount_ID());
		else if(mField.getColumnName().equals("B_InTransit_Acct"))
			mTab.setValue(MAcctSchemaDefault.COLUMNNAME_C_ElementValue_BIT_ID, validcombination.getAccount_ID());
		else if(mField.getColumnName().equals("B_PaymentSelect_Acct"))
			mTab.setValue(MAcctSchemaDefault.COLUMNNAME_C_ElementValue_PS_ID, validcombination.getAccount_ID());
		else if(mField.getColumnName().equals("B_UnallocatedCash_Acct"))
			mTab.setValue(MAcctSchemaDefault.COLUMNNAME_C_ElementValue_UC_ID, validcombination.getAccount_ID());
		else if(mField.getColumnName().equals("B_InterestRev_Acct"))
			mTab.setValue(MAcctSchemaDefault.COLUMNNAME_C_ElementValue_BIR_ID, validcombination.getAccount_ID());
		else if(mField.getColumnName().equals("B_InterestExp_Acct"))
			mTab.setValue(MAcctSchemaDefault.COLUMNNAME_C_ElementValue_BIE_ID, validcombination.getAccount_ID());
		else if(mField.getColumnName().equals("T_Due_Acct"))
			mTab.setValue(MAcctSchemaDefault.COLUMNNAME_C_ElementValue_TD_ID, validcombination.getAccount_ID());
		else if(mField.getColumnName().equals("T_Credit_Acct"))
			mTab.setValue(MAcctSchemaDefault.COLUMNNAME_C_ElementValue_TC_ID, validcombination.getAccount_ID());
		else if(mField.getColumnName().equals("T_Expense_Acct"))
			mTab.setValue(MAcctSchemaDefault.COLUMNNAME_C_ElementValue_TE_ID, validcombination.getAccount_ID());
		else if(mField.getColumnName().equals("Ch_Expense_Acct"))
			mTab.setValue(MAcctSchemaDefault.COLUMNNAME_C_ElementValue_Charge_ID, validcombination.getAccount_ID());
		else if(mField.getColumnName().equals("PJ_Asset_Acct"))
			mTab.setValue(MAcctSchemaDefault.COLUMNNAME_C_ElementValue_PA_ID, validcombination.getAccount_ID());
		else if(mField.getColumnName().equals("PJ_WIP_Acct"))
			mTab.setValue(MAcctSchemaDefault.COLUMNNAME_C_ElementValue_WIP_ID, validcombination.getAccount_ID());
		
		return "";
	}
}
