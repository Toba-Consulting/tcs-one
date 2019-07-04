package id.tcs.callout;

import java.util.Properties;

import org.adempiere.base.IColumnCallout;
import org.compiere.model.GridField;
import org.compiere.model.GridTab;
import org.compiere.model.MAccount;

import id.tcs.model.IAccountGen;
import id.tcs.model.MAcctSchemaGL;
import id.tcs.model.MValidCombination;
import id.tcs.model.MFADefaultAccount;

public class CalloutFADefaultAccount implements IColumnCallout, IAccountGen{

	@Override
	public String start(Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value, Object oldValue) {
		if (mField.getColumnName().equals(MFADefaultAccount.COLUMNNAME_C_ElementValue_ADepre_ID))
			return setAccount(ctx, WindowNo, mTab, mField, value, oldValue, "C_ElementValue_ADepre_ID");
		if (mField.getColumnName().equals(MFADefaultAccount.COLUMNNAME_C_ElementValue_AssetAcct_ID))
			return setAccount(ctx, WindowNo, mTab, mField, value, oldValue, "C_ElementValue_AssetAcct_ID");
		if (mField.getColumnName().equals(MFADefaultAccount.COLUMNNAME_C_ElementValue_AClearing_ID))
			return setAccount(ctx, WindowNo, mTab, mField, value, oldValue, "C_ElementValue_AClearing_ID");
		if (mField.getColumnName().equals(MFADefaultAccount.COLUMNNAME_C_ElementValue_Depre_ID))
			return setAccount(ctx, WindowNo, mTab, mField, value, oldValue, "C_ElementValue_Depre_ID");
		if (mField.getColumnName().equals(MFADefaultAccount.COLUMNNAME_C_ElementValue_DGain_ID))
			return setAccount(ctx, WindowNo, mTab, mField, value, oldValue, "C_ElementValue_DGain_ID");
		if (mField.getColumnName().equals(MFADefaultAccount.COLUMNNAME_C_ElementValue_DLoss_ID))
			return setAccount(ctx, WindowNo, mTab, mField, value, oldValue, "C_ElementValue_DLoss_ID");
		if (mField.getColumnName().equals(MFADefaultAccount.COLUMNNAME_C_ElementValue_DRevenue_ID))
			return setAccount(ctx, WindowNo, mTab, mField, value, oldValue, "C_ElementValue_DRevenue_ID");
		if (mField.getColumnName().equals(MFADefaultAccount.COLUMNNAME_A_Accumdepreciation_Acct) 
				|| mField.getColumnName().equals(MFADefaultAccount.COLUMNNAME_A_Asset_Acct)
				|| mField.getColumnName().equals(MFADefaultAccount.COLUMNNAME_A_Asset_Clearing_Acct)
				|| mField.getColumnName().equals(MFADefaultAccount.COLUMNNAME_A_Depreciation_Acct)
				|| mField.getColumnName().equals(MFADefaultAccount.COLUMNNAME_A_Disposal_Gain_Acct)
				|| mField.getColumnName().equals(MFADefaultAccount.COLUMNNAME_A_Disposal_Loss_Acct)
				|| mField.getColumnName().equals(MFADefaultAccount.COLUMNNAME_A_Disposal_Revenue_Acct) )
			return setElementValue(ctx, WindowNo, mTab, mField, value, oldValue);
		
		return null;
	}
	
	@Override
	public String setAccount(Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value,
			Object oldValue, String columnName) {
		if (value == null)
			return "";
		
		MAccount validAccount = MAccount.get(ctx, (int)mTab.getValue("AD_Client_ID"), 0, (int)mTab.getValue("C_AcctSchema_ID"), (int)mTab.getValue(columnName), 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, null);
		if (columnName.equals("C_ElementValue_ADepre_ID"))
			mTab.setValue("A_Accumdepreciation_Acct", validAccount.get_ID());
		else if (columnName.equals("C_ElementValue_AssetAcct_ID"))
			mTab.setValue("A_Asset_Acct", validAccount.get_ID());
		else if (columnName.equals("C_ElementValue_AClearing_ID"))
			mTab.setValue("A_Asset_Clearing_Acct", validAccount.get_ID());
		else if (columnName.equals("C_ElementValue_Depre_ID"))
			mTab.setValue("A_Depreciation_Acct", validAccount.get_ID());
		else if (columnName.equals("C_ElementValue_DGain_ID"))
			mTab.setValue("A_Disposal_Gain_Acct", validAccount.get_ID());
		else if (columnName.equals("C_ElementValue_DLoss_ID"))
			mTab.setValue("A_Disposal_Loss_Acct", validAccount.get_ID());
		else if (columnName.equals("C_ElementValue_DRevenue_ID"))
			mTab.setValue("A_Disposal_Revenue_Acct", validAccount.get_ID());
		
		return "";
	}

	//Set Element Value
		protected String setElementValue(Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value, Object oldValue) {
			if(value == null)
				return "";
			
			int C_ValidCombination_ID = (int)value;
			MValidCombination validCombination = new MValidCombination(ctx, C_ValidCombination_ID, null);
			
			if (mField.getColumnName().equals("A_Accumdepreciation_Acct")) {
				mTab.setValue(MFADefaultAccount.COLUMNNAME_C_ElementValue_ADepre_ID, validCombination.getAccount_ID());
			}
			else if (mField.getColumnName().equals("A_Asset_Acct")) {
				mTab.setValue(MFADefaultAccount.COLUMNNAME_C_ElementValue_AssetAcct_ID, validCombination.getAccount_ID());
			}
			else if (mField.getColumnName().equals("A_Asset_Clearing_Acct")) {
				mTab.setValue(MFADefaultAccount.COLUMNNAME_C_ElementValue_AClearing_ID, validCombination.getAccount_ID());
			}
			else if (mField.getColumnName().equals("A_Depreciation_Acct")) {
				mTab.setValue(MFADefaultAccount.COLUMNNAME_C_ElementValue_Depre_ID, validCombination.getAccount_ID());
			}
			else if (mField.getColumnName().equals("A_Disposal_Gain_Acct")) {
				mTab.setValue(MFADefaultAccount.COLUMNNAME_C_ElementValue_DGain_ID, validCombination.getAccount_ID());
			}
			else if (mField.getColumnName().equals("A_Disposal_Loss_Acct")) {
				mTab.setValue(MFADefaultAccount.COLUMNNAME_C_ElementValue_DLoss_ID, validCombination.getAccount_ID());
			}
			else if (mField.getColumnName().equals("A_Disposal_Revenue_Acct")) {
				mTab.setValue(MFADefaultAccount.COLUMNNAME_C_ElementValue_DRevenue_ID, validCombination.getAccount_ID());
			}
			
			return "";
		}

}
