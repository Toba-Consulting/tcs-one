package id.tcs.callout;

import java.util.Properties;

import org.adempiere.base.IColumnCallout;
import org.compiere.model.GridField;
import org.compiere.model.GridTab;
import org.compiere.model.MAccount;

import id.tcs.model.IAccountGen;
import id.tcs.model.MProductAcct;
import id.tcs.model.MValidCombination;

public class CalloutProductAcct implements IColumnCallout, IAccountGen{

	@Override
	public String start(Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value, Object oldValue) {
		// TODO Auto-generated method stub
		if (mField.getColumnName().equals(MProductAcct.COLUMNNAME_C_ElementValue_Asset_ID))
			return setAccount(ctx, WindowNo, mTab, mField, value, oldValue, "C_ElementValue_Asset_ID");
		if (mField.getColumnName().equals(MProductAcct.COLUMNNAME_C_ElementValue_CA_ID))
			return setAccount(ctx, WindowNo, mTab, mField, value, oldValue, "C_ElementValue_CA_ID");
		if (mField.getColumnName().equals(MProductAcct.COLUMNNAME_C_ElementValue_COGS_ID))
			return setAccount(ctx, WindowNo, mTab, mField, value, oldValue, "C_ElementValue_COGS_ID");
		if (mField.getColumnName().equals(MProductAcct.COLUMNNAME_C_ElementValue_PPV_ID))
			return setAccount(ctx, WindowNo, mTab, mField, value, oldValue, "C_ElementValue_PPV_ID");
		if (mField.getColumnName().equals(MProductAcct.COLUMNNAME_C_ElementValue_TDR_ID))
			return setAccount(ctx, WindowNo, mTab, mField, value, oldValue, "C_ElementValue_TDR_ID");
		if (mField.getColumnName().equals(MProductAcct.COLUMNNAME_C_ElementValue_RV_ID))
			return setAccount(ctx, WindowNo, mTab, mField, value, oldValue, "C_ElementValue_RV_ID");
		if (mField.getColumnName().equals(MProductAcct.COLUMNNAME_C_ElementValue_LCC_ID))
			return setAccount(ctx, WindowNo, mTab, mField, value, oldValue, "C_ElementValue_LCC_ID");
		if (mField.getColumnName().equals(MProductAcct.COLUMNNAME_C_ElementValue_PE_ID))
			return setAccount(ctx, WindowNo, mTab, mField, value, oldValue, "C_ElementValue_PE_ID");
		if (mField.getColumnName().equals(MProductAcct.COLUMNNAME_C_ElementValue_IC_ID))
			return setAccount(ctx, WindowNo, mTab, mField, value, oldValue, "C_ElementValue_IC_ID");
		if (mField.getColumnName().equals(MProductAcct.COLUMNNAME_C_ElementValue_Revenue_ID))
			return setAccount(ctx, WindowNo, mTab, mField, value, oldValue, "C_ElementValue_Revenue_ID");
		if (mField.getColumnName().equals(MProductAcct.COLUMNNAME_C_ElementValue_IPV_ID))
			return setAccount(ctx, WindowNo, mTab, mField, value, oldValue, "C_ElementValue_IPV_ID");
		if (mField.getColumnName().equals(MProductAcct.COLUMNNAME_C_ElementValue_TDG_ID))
			return setAccount(ctx, WindowNo, mTab, mField, value, oldValue, "C_ElementValue_TDG_ID");
		if (mField.getColumnName().equals(MProductAcct.COLUMNNAME_C_ElementValue_ACV_ID))
			return setAccount(ctx, WindowNo, mTab, mField, value, oldValue, "C_ElementValue_ACV_ID");
		if (mField.getColumnName().equals(MProductAcct.COLUMNNAME_P_Asset_Acct)
				|| mField.getColumnName().equals(MProductAcct.COLUMNNAME_P_CostAdjustment_Acct)
				|| mField.getColumnName().equals(MProductAcct.COLUMNNAME_P_COGS_Acct)
				|| mField.getColumnName().equals(MProductAcct.COLUMNNAME_P_PurchasePriceVariance_Acct)
				|| mField.getColumnName().equals(MProductAcct.COLUMNNAME_P_TradeDiscountRec_Acct)
				|| mField.getColumnName().equals(MProductAcct.COLUMNNAME_P_RateVariance_Acct)
				|| mField.getColumnName().equals(MProductAcct.COLUMNNAME_P_LandedCostClearing_Acct)
				|| mField.getColumnName().equals(MProductAcct.COLUMNNAME_P_Expense_Acct)
				|| mField.getColumnName().equals(MProductAcct.COLUMNNAME_P_InventoryClearing_Acct)
				|| mField.getColumnName().equals(MProductAcct.COLUMNNAME_P_Revenue_Acct)
				|| mField.getColumnName().equals(MProductAcct.COLUMNNAME_P_InvoicePriceVariance_Acct)
				|| mField.getColumnName().equals(MProductAcct.COLUMNNAME_P_TradeDiscountGrant_Acct)
				|| mField.getColumnName().equals(MProductAcct.COLUMNNAME_P_AverageCostVariance_Acct)
				)
			return setElementValue(ctx, WindowNo, mTab, mField, value, oldValue);
		return null;
	}
	
	@Override
	public String setAccount(Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value,
			Object oldValue, String columnName) {
		// TODO Auto-generated method stub
		if (value == null)
			return "";
		
		MAccount validAccount = MAccount.get(ctx, (int)mTab.getValue("AD_Client_ID"), 0, (int)mTab.getValue("C_AcctSchema_ID"), (int)mTab.getValue(columnName), 0, (int)mTab.getValue("M_Product_ID"), 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, null);
		if(columnName.equals("C_ElementValue_Asset_ID"))	
			mTab.setValue("P_Asset_Acct", validAccount.get_ID());
		else if(columnName.equals("C_ElementValue_CA_ID"))
			mTab.setValue("P_CostAdjustment_Acct", validAccount.get_ID());
		else if(columnName.equals("C_ElementValue_COGS_ID"))
			mTab.setValue("P_COGS_Acct", validAccount.get_ID());
		else if(columnName.equals("C_ElementValue_PPV_ID"))
			mTab.setValue("P_PurchasePriceVariance_Acct", validAccount.get_ID());
		else if(columnName.equals("C_ElementValue_TDR_ID"))
			mTab.setValue("P_TradeDiscountRec_Acct", validAccount.get_ID());
		else if(columnName.equals("C_ElementValue_RV_ID"))
			mTab.setValue("P_RateVariance_Acct", validAccount.get_ID());
		else if(columnName.equals("C_ElementValue_LCC_ID"))
			mTab.setValue("P_LandedCostClearing_Acct", validAccount.get_ID());
		else if(columnName.equals("C_ElementValue_PE_ID"))
			mTab.setValue("P_Expense_Acct", validAccount.get_ID());
		else if(columnName.equals("C_ElementValue_IC_ID"))
			mTab.setValue("P_InventoryClearing_Acct", validAccount.get_ID());
		else if(columnName.equals("C_ElementValue_Revenue_ID"))
			mTab.setValue("P_Revenue_Acct", validAccount.get_ID());
		else if(columnName.equals("C_ElementValue_IPV_ID"))
			mTab.setValue("P_InvoicePriceVariance_Acct", validAccount.get_ID());
		else if(columnName.equals("C_ElementValue_TDG_ID"))
			mTab.setValue("P_TradeDiscountGrant_Acct", validAccount.get_ID());
		else if(columnName.equals("C_ElementValue_ACV_ID"))
			mTab.setValue("P_AverageCostVariance_Acct", validAccount.get_ID());
		
		return "";
	}		
	
	//Set Element Value
	protected String setElementValue(Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value, Object oldValue) {
		if(value == null)
			return "";
		
		int C_ValidCombination_ID = (int)value;
		MValidCombination validCombination = new MValidCombination(ctx, C_ValidCombination_ID, null);
		
		if (mField.getColumnName().equals("P_Asset_Acct")) {
			mTab.setValue(MProductAcct.COLUMNNAME_C_ElementValue_Asset_ID, validCombination.getAccount_ID());
		}else if (mField.getColumnName().equals("P_CostAdjustment_Acct")) {
			mTab.setValue(MProductAcct.COLUMNNAME_C_ElementValue_CA_ID, validCombination.getAccount_ID());
		}else if (mField.getColumnName().equals("P_COGS_Acct")) {
			mTab.setValue(MProductAcct.COLUMNNAME_C_ElementValue_COGS_ID, validCombination.getAccount_ID());
		}else if (mField.getColumnName().equals("P_PurchasePriceVariance_Acct")) {
			mTab.setValue(MProductAcct.COLUMNNAME_C_ElementValue_PPV_ID, validCombination.getAccount_ID());
		}else if (mField.getColumnName().equals("P_TradeDiscountRec_Acct")) {
			mTab.setValue(MProductAcct.COLUMNNAME_C_ElementValue_TDR_ID, validCombination.getAccount_ID());
		}else if (mField.getColumnName().equals("P_RateVariance_Acct")) {
			mTab.setValue(MProductAcct.COLUMNNAME_C_ElementValue_RV_ID, validCombination.getAccount_ID());
		}else if (mField.getColumnName().equals("P_LandedCostClearing_Acct")) {
			mTab.setValue(MProductAcct.COLUMNNAME_C_ElementValue_LCC_ID, validCombination.getAccount_ID());
		}else if (mField.getColumnName().equals("P_Expense_Acct")) {
			mTab.setValue(MProductAcct.COLUMNNAME_C_ElementValue_PE_ID, validCombination.getAccount_ID());
		}else if (mField.getColumnName().equals("P_InventoryClearing_Acct")) {
			mTab.setValue(MProductAcct.COLUMNNAME_C_ElementValue_IC_ID, validCombination.getAccount_ID());
		}else if (mField.getColumnName().equals("P_Revenue_Acct")) {
			mTab.setValue(MProductAcct.COLUMNNAME_C_ElementValue_Revenue_ID, validCombination.getAccount_ID());
		}else if (mField.getColumnName().equals("P_InvoicePriceVariance_Acct")) {
			mTab.setValue(MProductAcct.COLUMNNAME_C_ElementValue_IPV_ID, validCombination.getAccount_ID());
		}else if (mField.getColumnName().equals("P_TradeDiscountGrant_Acct")) {
			mTab.setValue(MProductAcct.COLUMNNAME_C_ElementValue_TDG_ID, validCombination.getAccount_ID());
		}else if (mField.getColumnName().equals("P_AverageCostVariance_Acct")) {
			mTab.setValue(MProductAcct.COLUMNNAME_C_ElementValue_ACV_ID, validCombination.getAccount_ID());
		}
		
		return "";
	}
}
