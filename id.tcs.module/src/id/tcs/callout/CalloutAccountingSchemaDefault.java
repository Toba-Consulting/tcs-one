package id.tcs.callout;

import java.util.Properties;

import org.adempiere.base.IColumnCallout;
import org.compiere.model.GridField;
import org.compiere.model.GridTab;
import org.compiere.model.MAccount;
import org.compiere.model.MAcctSchemaDefault;

public class CalloutAccountingSchemaDefault implements IColumnCallout { 

	@Override
	public String start(Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value, Object oldValue) {
		if (mField.getColumnName().equals("C_ElementValue_UG_ID"))
			return setAccount(ctx, WindowNo, mTab, mField, value, oldValue, "C_ElementValue_UG_ID");
		if (mField.getColumnName().equals("C_ElementValue_UL_ID"))
			return setAccount(ctx, WindowNo, mTab, mField, value, oldValue, "C_ElementValue_UL_ID");
		if (mField.getColumnName().equals("C_ElementValue_RG_ID"))
			return setAccount(ctx, WindowNo, mTab, mField, value, oldValue, "C_ElementValue_RG_ID");
		if (mField.getColumnName().equals("C_ElementValue_RL_ID"))
			return setAccount(ctx, WindowNo, mTab, mField, value, oldValue, "C_ElementValue_RL_ID");
		if (mField.getColumnName().equals("C_ElementValue_ID"))
			return setAccount(ctx, WindowNo, mTab, mField, value, oldValue, "C_ElementValue_ID");
		if (mField.getColumnName().equals("C_ElementValue_PDE_ID"))
			return setAccount(ctx, WindowNo, mTab, mField, value, oldValue, "C_ElementValue_PDE_ID");
		if (mField.getColumnName().equals("C_ElementValue_PDR_ID"))
			return setAccount(ctx, WindowNo, mTab, mField, value, oldValue, "C_ElementValue_PDR_ID");
		if (mField.getColumnName().equals("C_ElementValue_WO_ID"))
			return setAccount(ctx, WindowNo, mTab, mField, value, oldValue, "C_ElementValue_WO_ID");
		if (mField.getColumnName().equals("C_ElementValue_CR_ID"))
			return setAccount(ctx, WindowNo, mTab, mField, value, oldValue, "C_ElementValue_CR_ID");
		if (mField.getColumnName().equals("C_ElementValue_VL_ID"))
			return setAccount(ctx, WindowNo, mTab, mField, value, oldValue, "C_ElementValue_VL_ID");
		if (mField.getColumnName().equals("C_ElementValue_CP_ID"))
			return setAccount(ctx, WindowNo, mTab, mField, value, oldValue, "C_ElementValue_CP_ID");
		if (mField.getColumnName().equals("C_ElementValue_VP_ID"))
			return setAccount(ctx, WindowNo, mTab, mField, value, oldValue, "C_ElementValue_VP_ID");
		if (mField.getColumnName().equals("C_ElementValue_Asset_ID"))
			return setAccount(ctx, WindowNo, mTab, mField, value, oldValue, "C_ElementValue_Asset_ID");
		if (mField.getColumnName().equals("C_ElementValue_PE_ID"))
			return setAccount(ctx, WindowNo, mTab, mField, value, oldValue, "C_ElementValue_PE_ID");
		if (mField.getColumnName().equals("C_ElementValue_CA_ID"))
			return setAccount(ctx, WindowNo, mTab, mField, value, oldValue, "C_ElementValue_CA_ID");
		if (mField.getColumnName().equals("C_ElementValue_IC_ID"))
			return setAccount(ctx, WindowNo, mTab, mField, value, oldValue, "C_ElementValue_IC_ID");
		if (mField.getColumnName().equals("C_ElementValue_Revenue_ID"))
			return setAccount(ctx, WindowNo, mTab, mField, value, oldValue, "C_ElementValue_Revenue_ID");
		if (mField.getColumnName().equals("C_ElementValue_COGS_ID"))
			return setAccount(ctx, WindowNo, mTab, mField, value, oldValue, "C_ElementValue_COGS_ID");
		if (mField.getColumnName().equals("C_ElementValue_PPV_ID"))
			return setAccount(ctx, WindowNo, mTab, mField, value, oldValue, "C_ElementValue_PPV_ID");
		if (mField.getColumnName().equals("C_ElementValue_IPV_ID"))
			return setAccount(ctx, WindowNo, mTab, mField, value, oldValue, "C_ElementValue_IPV_ID");
		if (mField.getColumnName().equals("C_ElementValue_TDR_ID"))
			return setAccount(ctx, WindowNo, mTab, mField, value, oldValue, "C_ElementValue_TDR_ID");
		if (mField.getColumnName().equals("C_ElementValue_TDG_ID"))
			return setAccount(ctx, WindowNo, mTab, mField, value, oldValue, "C_ElementValue_TDG_ID");
		if (mField.getColumnName().equals("C_ElementValue_RV_ID"))
			return setAccount(ctx, WindowNo, mTab, mField, value, oldValue, "C_ElementValue_RV_ID");
		if (mField.getColumnName().equals("C_ElementValue_ACV_ID"))
			return setAccount(ctx, WindowNo, mTab, mField, value, oldValue, "C_ElementValue_ACV_ID");
		if (mField.getColumnName().equals("C_ElementValue_LCC_ID"))
			return setAccount(ctx, WindowNo, mTab, mField, value, oldValue, "C_ElementValue_LCC_ID");
		if (mField.getColumnName().equals("C_ElementValue_WD_ID"))
			return setAccount(ctx, WindowNo, mTab, mField, value, oldValue, "C_ElementValue_WD_ID");
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
		if (mField.getColumnName().equals("C_ElementValue_TD_ID"))
			return setAccount(ctx, WindowNo, mTab, mField, value, oldValue, "C_ElementValue_TD_ID");
		if (mField.getColumnName().equals("C_ElementValue_TC_ID"))
			return setAccount(ctx, WindowNo, mTab, mField, value, oldValue, "C_ElementValue_TC_ID");
		if (mField.getColumnName().equals("C_ElementValue_TE_ID"))
			return setAccount(ctx, WindowNo, mTab, mField, value, oldValue, "C_ElementValue_TE_ID");
		if (mField.getColumnName().equals("C_ElementValue_Charge_ID"))
			return setAccount(ctx, WindowNo, mTab, mField, value, oldValue, "C_ElementValue_Charge_ID");
		if (mField.getColumnName().equals("C_ElementValue_PA_ID"))
			return setAccount(ctx, WindowNo, mTab, mField, value, oldValue, "C_ElementValue_PA_ID");
		if (mField.getColumnName().equals("C_ElementValue_WIP_ID"))
			return setAccount(ctx, WindowNo, mTab, mField, value, oldValue, "C_ElementValue_WIP_ID");
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

	public String setAccount(Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value,
			Object oldValue, String columnName) {
		if(value == null)
			return "";
		
		MAccount validAccount = MAccount.get(ctx, (int)mTab.getValue("AD_Client_ID"), 0, (int)mTab.getValue("C_AcctSchema_ID"), (int)mTab.getValue(columnName), 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, null);
		if(columnName.equals("C_ElementValue_UG_ID"))
			mTab.setValue("UnrealizedGain_Acct", validAccount.get_ID());
		else if(columnName.equals("C_ElementValue_UL_ID"))
			mTab.setValue("UnrealizedLoss_Acct", validAccount.get_ID());
		else if(columnName.equals("C_ElementValue_RG_ID"))
			mTab.setValue("RealizedGain_Acct", validAccount.get_ID());
		else if(columnName.equals("C_ElementValue_RL_ID"))
			mTab.setValue("RealizedLoss_Acct", validAccount.get_ID());
		else if(columnName.equals("C_ElementValue_ID"))
			mTab.setValue("NotInvoicedReceipts_Acct", validAccount.get_ID());
		else if(columnName.equals("C_ElementValue_PDE_ID"))
			mTab.setValue("PayDiscount_Exp_Acct", validAccount.get_ID());
		else if(columnName.equals("C_ElementValue_PDR_ID"))
			mTab.setValue("PayDiscount_Rev_Acct", validAccount.get_ID());
		else if(columnName.equals("C_ElementValue_WO_ID"))
			mTab.setValue("WriteOff_Acct", validAccount.get_ID());
		else if(columnName.equals("C_ElementValue_CR_ID"))
			mTab.setValue("C_Receivable_Acct", validAccount.get_ID());
		else if(columnName.equals("C_ElementValue_VL_ID"))
			mTab.setValue("V_Liability_Acct", validAccount.get_ID());
		else if(columnName.equals("C_ElementValue_CP_ID"))
			mTab.setValue("C_Prepayment_Acct", validAccount.get_ID());
		else if(columnName.equals("C_ElementValue_VP_ID"))
			mTab.setValue("V_Prepayment_Acct", validAccount.get_ID());
		else if(columnName.equals("C_ElementValue_Asset_ID"))
			mTab.setValue("P_Asset_Acct", validAccount.get_ID());
		else if(columnName.equals("C_ElementValue_PE_ID"))	
			mTab.setValue("P_Expense_Acct", validAccount.get_ID());
		else if(columnName.equals("C_ElementValue_CA_ID"))
			mTab.setValue("P_CostAdjustment_Acct", validAccount.get_ID());
		else if(columnName.equals("C_ElementValue_IC_ID"))
			mTab.setValue("P_InventoryClearing_Acct", validAccount.get_ID());
		else if(columnName.equals("C_ElementValue_Revenue_ID"))
			mTab.setValue("P_Revenue_Acct", validAccount.get_ID());
		else if(columnName.equals("C_ElementValue_COGS_ID"))
			mTab.setValue("P_COGS_Acct", validAccount.get_ID());
		else if(columnName.equals("C_ElementValue_PPV_ID"))
			mTab.setValue("P_PurchasePriceVariance_Acct", validAccount.get_ID());
		else if(columnName.equals("C_ElementValue_IPV_ID"))
			mTab.setValue("P_InvoicePriceVariance_Acct", validAccount.get_ID());
		else if(columnName.equals("C_ElementValue_TDR_ID"))
			mTab.setValue("P_TradeDiscountRec_Acct", validAccount.get_ID());
		else if(columnName.equals("C_ElementValue_TDG_ID"))
			mTab.setValue("P_TradeDiscountGrant_Acct", validAccount.get_ID());
		else if(columnName.equals("C_ElementValue_RV_ID"))
			mTab.setValue("P_RateVariance_Acct", validAccount.get_ID());
		else if(columnName.equals("C_ElementValue_ACV_ID"))
			mTab.setValue("P_AverageCostVariance_Acct", validAccount.get_ID());
		else if(columnName.equals("C_ElementValue_LCC_ID"))
			mTab.setValue("P_LandedCostClearing_Acct", validAccount.get_ID());
		else if(columnName.equals("C_ElementValue_WD_ID"))
			mTab.setValue("W_Differences_Acct", validAccount.get_ID());
		else if(columnName.equals("C_ElementValue_BA_ID"))
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
		else if(columnName.equals("C_ElementValue_TD_ID"))
			mTab.setValue("T_Due_Acct", validAccount.get_ID());
		else if(columnName.equals("C_ElementValue_TC_ID"))
			mTab.setValue("T_Credit_Acct", validAccount.get_ID());
		else if(columnName.equals("C_ElementValue_TE_ID"))
			mTab.setValue("T_Expense_Acct", validAccount.get_ID());
		else if(columnName.equals("C_ElementValue_Charge_ID"))
			mTab.setValue("Ch_Expense_Acct", validAccount.get_ID());
		else if(columnName.equals("C_ElementValue_PA_ID"))
			mTab.setValue("PJ_Asset_Acct", validAccount.get_ID());
		else if(columnName.equals("C_ElementValue_WIP_ID"))
			mTab.setValue("PJ_WIP_Acct", validAccount.get_ID());
		return "";
	}
		
	//set Element Value
	protected String setElementValue(Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value, Object oldValue) {
		if(value == null) {
			return "";
		}
		
		int C_ValidCombination_ID = (int) value;
		MAccount validcombination = new MAccount(ctx, C_ValidCombination_ID, null);
		
		if(mField.getColumnName().equals("UnrealizedGain_Acct"))
			mTab.setValue("C_ElementValue_UG_ID", validcombination.getAccount_ID());
		else if(mField.getColumnName().equals("UnrealizedLoss_Acct"))
			mTab.setValue("C_ElementValue_UL_ID", validcombination.getAccount_ID());
		else if(mField.getColumnName().equals("RealizedGain_Acct"))
			mTab.setValue("C_ElementValue_RG_ID", validcombination.getAccount_ID());
		else if(mField.getColumnName().equals("RealizedLoss_Acct"))
			mTab.setValue("C_ElementValue_RL_ID", validcombination.getAccount_ID());
		else if(mField.getColumnName().equals("NotInvoicedReceipts_Acct"))
			mTab.setValue("C_ElementValue_ID", validcombination.getAccount_ID());
		else if(mField.getColumnName().equals("PayDiscount_Exp_Acct"))
			mTab.setValue("C_ElementValue_PDE_ID", validcombination.getAccount_ID());
		else if(mField.getColumnName().equals("PayDiscount_Rev_Acct"))
			mTab.setValue("C_ElementValue_PDR_ID", validcombination.getAccount_ID());
		else if(mField.getColumnName().equals("WriteOff_Acct"))
			mTab.setValue("C_ElementValue_WO_ID", validcombination.getAccount_ID());
		else if(mField.getColumnName().equals("C_Receivable_Acct"))
			mTab.setValue("C_ElementValue_CR_ID", validcombination.getAccount_ID());
		else if(mField.getColumnName().equals("V_Liability_Acct"))
			mTab.setValue("C_ElementValue_VL_ID", validcombination.getAccount_ID());
		else if(mField.getColumnName().equals("C_Prepayment_Acct"))
			mTab.setValue("C_ElementValue_CP_ID", validcombination.getAccount_ID());
		else if(mField.getColumnName().equals("V_Prepayment_Acct"))
			mTab.setValue("C_ElementValue_VP_ID", validcombination.getAccount_ID());
		else if(mField.getColumnName().equals("P_Asset_Acct"))
			mTab.setValue("C_ElementValue_Asset_ID", validcombination.getAccount_ID());
		else if(mField.getColumnName().equals("P_Expense_Acct"))
			mTab.setValue("C_ElementValue_PE_ID", validcombination.getAccount_ID());
		else if(mField.getColumnName().equals("P_CostAdjustment_Acct"))
			mTab.setValue("C_ElementValue_CA_ID", validcombination.getAccount_ID());
		else if(mField.getColumnName().equals("P_InventoryClearing_Acct"))
			mTab.setValue("C_ElementValue_IC_ID", validcombination.getAccount_ID());
		else if(mField.getColumnName().equals("P_Revenue_Acct"))
			mTab.setValue("C_ElementValue_Revenue_ID", validcombination.getAccount_ID());
		else if(mField.getColumnName().equals("P_COGS_Acct"))
			mTab.setValue("C_ElementValue_COGS_ID", validcombination.getAccount_ID());
		else if(mField.getColumnName().equals("P_PurchasePriceVariance_Acct"))
			mTab.setValue("C_ElementValue_PPV_ID", validcombination.getAccount_ID());
		else if(mField.getColumnName().equals("P_InvoicePriceVariance_Acct"))
			mTab.setValue("C_ElementValue_IPV_ID", validcombination.getAccount_ID());
		else if(mField.getColumnName().equals("P_TradeDiscountRec_Acct"))
			mTab.setValue("C_ElementValue_TDR_ID", validcombination.getAccount_ID());
		else if(mField.getColumnName().equals("P_TradeDiscountGrant_Acct"))
			mTab.setValue("C_ElementValue_TDG_ID", validcombination.getAccount_ID());
		else if(mField.getColumnName().equals("P_RateVariance_Acct"))
			mTab.setValue("C_ElementValue_RV_ID", validcombination.getAccount_ID());
		else if(mField.getColumnName().equals("P_AverageCostVariance_Acct"))
			mTab.setValue("C_ElementValue_ACV_ID", validcombination.getAccount_ID());
		else if(mField.getColumnName().equals("P_LandedCostClearing_Acct"))
			mTab.setValue("C_ElementValue_LCC_ID", validcombination.getAccount_ID());
		else if(mField.getColumnName().equals("W_Differences_Acct"))
			mTab.setValue("C_ElementValue_WD_ID", validcombination.getAccount_ID());
		else if(mField.getColumnName().equals("B_Asset_Acct"))
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
		else if(mField.getColumnName().equals("T_Due_Acct"))
			mTab.setValue("C_ElementValue_TD_ID", validcombination.getAccount_ID());
		else if(mField.getColumnName().equals("T_Credit_Acct"))
			mTab.setValue("C_ElementValue_TC_ID", validcombination.getAccount_ID());
		else if(mField.getColumnName().equals("T_Expense_Acct"))
			mTab.setValue("C_ElementValue_TE_ID", validcombination.getAccount_ID());
		else if(mField.getColumnName().equals("Ch_Expense_Acct"))
			mTab.setValue("C_ElementValue_Charge_ID", validcombination.getAccount_ID());
		else if(mField.getColumnName().equals("PJ_Asset_Acct"))
			mTab.setValue("C_ElementValue_PA_ID", validcombination.getAccount_ID());
		else if(mField.getColumnName().equals("PJ_WIP_Acct"))
			mTab.setValue("C_ElementValue_WIP_ID", validcombination.getAccount_ID());
		
		return "";
	}

}
