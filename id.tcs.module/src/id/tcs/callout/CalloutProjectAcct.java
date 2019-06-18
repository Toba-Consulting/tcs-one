package org.banktransfer.callout;

import java.util.Properties;

import org.adempiere.base.IColumnCallout;
import org.banktransfer.model.MAcctSchemaGL;
import org.banktransfer.model.MProjectAcct;
import org.banktransfer.model.MValidCombination;
import org.compiere.model.GridField;
import org.compiere.model.GridTab;
import org.compiere.model.MAccount;

public class CalloutProjectAcct implements IColumnCallout{

	@Override
	public String start(Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value, Object oldValue) {
		// TODO Auto-generated method stub
		if (mField.getColumnName().equals(MProjectAcct.COLUMNNAME_C_ElementValue_PA_ID))
			return setProjectAssetAcct(ctx, WindowNo, mTab, mField, value, oldValue);
		if (mField.getColumnName().equals(MProjectAcct.COLUMNNAME_C_ElementValue_WIP_ID))
			return setWorkInProgressAcct(ctx, WindowNo, mTab, mField, value, oldValue);
		if (mField.getColumnName().equals(MProjectAcct.COLUMNNAME_PJ_Asset_Acct)
				|| mField.getColumnName().equals(MProjectAcct.COLUMNNAME_PJ_WIP_Acct))
			return setElementValue(ctx, WindowNo, mTab, mField, value, oldValue);
		
		return null;
	}
	
	//set Project Asset Acct
	protected String setProjectAssetAcct(Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value, Object oldValue) {
		if(value == null) {
			return "";
		}
		
		MAccount validAccount = MAccount.get(ctx, (int)mTab.getValue("AD_Client_ID"), 0, (int)mTab.getValue("C_AcctSchema_ID"), (int)mTab.getValue("C_ElementValue_PA_ID"), 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, null);
		mTab.setValue("PJ_Asset_Acct", validAccount.get_ID());
		return "";
	}
	
	//set Work In Progress Acct
	protected String setWorkInProgressAcct(Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value, Object oldValue) {
		if(value == null) {
			return "";
		}
		
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
		
		if(mField.getColumnName().equals("PJ_Asset_Acct"))
			mTab.setValue(MProjectAcct.COLUMNNAME_C_ElementValue_PA_ID, validcombination.getAccount_ID());
		else if(mField.getColumnName().equals("PJ_WIP_Acct"))
			mTab.setValue(MProjectAcct.COLUMNNAME_C_ElementValue_WIP_ID, validcombination.getAccount_ID());
		return "";
	}
	
}
