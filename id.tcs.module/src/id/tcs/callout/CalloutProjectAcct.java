package id.tcs.callout;

import java.util.Properties;

import org.adempiere.base.IColumnCallout;
import org.compiere.model.MProjectAcct;
import org.compiere.model.GridField;
import org.compiere.model.GridTab;
import org.compiere.model.MAccount;

public class CalloutProjectAcct implements IColumnCallout{

	@Override
	public String start(Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value, Object oldValue) {
		// TODO Auto-generated method stub
		if (mField.getColumnName().equals("C_ElementValue_PA_ID"))
			return setAccount(ctx, WindowNo, mTab, mField, value, oldValue, "C_ElementValue_PA_ID");
		if (mField.getColumnName().equals("C_ElementValue_WIP_ID"))
			return setAccount(ctx, WindowNo, mTab, mField, value, oldValue, "C_ElementValue_WIP_ID");
		if (mField.getColumnName().equals(MProjectAcct.COLUMNNAME_PJ_Asset_Acct)
				|| mField.getColumnName().equals(MProjectAcct.COLUMNNAME_PJ_WIP_Acct))
			return setElementValue(ctx, WindowNo, mTab, mField, value, oldValue);
		
		return null;
	}
	
	public String setAccount(Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value,
			Object oldValue, String columnName) {
		// TODO Auto-generated method stub
		if(value == null) {
			return "";
		}
		
		MAccount validAccount = MAccount.get(ctx, (int)mTab.getValue("AD_Client_ID"), 0, (int)mTab.getValue("C_AcctSchema_ID"), (int)mTab.getValue(columnName), 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, null);
		
		if(columnName.equals("C_ElementValue_WIP_ID"))
			mTab.setValue("PJ_WIP_Acct", validAccount.get_ID());
		else if(columnName.equals("C_ElementValue_PA_ID"))
			mTab.setValue("PJ_Asset_Acct", validAccount.get_ID());
		
		return "";
	}
	
	//set Element Value
	protected String setElementValue(Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value, Object oldValue) {
		if(value == null) {
			return "";
		}
		
		int C_ValidCombination_ID = (int)value;
		MAccount validcombination = new MAccount(ctx, C_ValidCombination_ID, null);
		
		if(mField.getColumnName().equals("PJ_Asset_Acct"))
			mTab.setValue("C_ElementValue_PA_ID", validcombination.getAccount_ID());
		else if(mField.getColumnName().equals("PJ_WIP_Acct"))
			mTab.setValue("C_ElementValue_WIP_ID", validcombination.getAccount_ID());
		return "";
	}

	
	
}
