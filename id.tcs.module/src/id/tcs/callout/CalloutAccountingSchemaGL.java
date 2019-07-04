package id.tcs.callout;

import java.util.Properties;

import org.adempiere.base.IColumnCallout;
import org.compiere.model.GridField;
import org.compiere.model.GridTab;
import org.compiere.model.MAccount;
import org.compiere.model.MAcctSchemaGL;

public class CalloutAccountingSchemaGL implements IColumnCallout {

	@Override
	public String start(Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value, Object oldValue) {
		if (mField.getColumnName().equals("C_ElementValue_SB_ID"))
			return setAccount(ctx, WindowNo, mTab, mField, value, oldValue, "C_ElementValue_SB_ID");
		if (mField.getColumnName().equals("C_ElementValue_CB_ID"))
			return setAccount(ctx, WindowNo, mTab, mField, value, oldValue, "C_ElementValue_CB_ID");
		if (mField.getColumnName().equals("C_ElementValue_IDT_ID"))
			return setAccount(ctx, WindowNo, mTab, mField, value, oldValue, "C_ElementValue_IDT_ID");
		if (mField.getColumnName().equals("C_ElementValue_IDF_ID"))
			return setAccount(ctx, WindowNo, mTab, mField, value, oldValue, "C_ElementValue_IDF_ID");
		if (mField.getColumnName().equals("C_ElementValue_PPVO_ID"))
			return setAccount(ctx, WindowNo, mTab, mField, value, oldValue, "C_ElementValue_PPVO_ID");
		if (mField.getColumnName().equals("C_ElementValue_CO_ID"))
			return setAccount(ctx, WindowNo, mTab, mField, value, oldValue, "C_ElementValue_CO_ID");
		if (mField.getColumnName().equals("C_ElementValue_COS_ID"))
			return setAccount(ctx, WindowNo, mTab, mField, value, oldValue, "C_ElementValue_COS_ID");
		if (mField.getColumnName().equals(MAcctSchemaGL.COLUMNNAME_SuspenseBalancing_Acct) 
				|| mField.getColumnName().equals(MAcctSchemaGL.COLUMNNAME_CurrencyBalancing_Acct)
				|| mField.getColumnName().equals(MAcctSchemaGL.COLUMNNAME_IntercompanyDueTo_Acct)
				|| mField.getColumnName().equals(MAcctSchemaGL.COLUMNNAME_IntercompanyDueFrom_Acct)
				|| mField.getColumnName().equals(MAcctSchemaGL.COLUMNNAME_PPVOffset_Acct)
				|| mField.getColumnName().equals(MAcctSchemaGL.COLUMNNAME_CommitmentOffset_Acct)
				|| mField.getColumnName().equals(MAcctSchemaGL.COLUMNNAME_CommitmentOffsetSales_Acct) )
			return setElementValue(ctx, WindowNo, mTab, mField, value, oldValue);
			
		return null;
	}
	
	public String setAccount(Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value,
			Object oldValue, String columnName) {
		if(value == null)
			return "";
		
		MAccount validAccount = MAccount.get(ctx, (int)mTab.getValue("AD_Client_ID"), 0, (int)mTab.getValue("C_AcctSchema_ID"), (int)mTab.getValue(columnName), 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, null);
		if(columnName.equals("C_ElementValue_SB_ID"))
			mTab.setValue("SuspenseBalancing_Acct", validAccount.get_ID());
		else if(columnName.equals("C_ElementValue_CB_ID"))
			mTab.setValue("CurrencyBalancing_Acct", validAccount.get_ID());
		else if(columnName.equals("C_ElementValue_IDT_ID"))
			mTab.setValue("IntercompanyDueTo_Acct", validAccount.get_ID());
		else if(columnName.equals("C_ElementValue_IDF_ID"))
			mTab.setValue("IntercompanyDueFrom_Acct", validAccount.get_ID());
		else if(columnName.equals("C_ElementValue_PPVO_ID"))
			mTab.setValue("PPVOffset_Acct", validAccount.get_ID());
		else if(columnName.equals("C_ElementValue_CO_ID"))
			mTab.setValue("CommitmentOffset_Acct", validAccount.get_ID());
		else if(columnName.equals("C_ElementValue_COS_ID"))
			mTab.setValue("CommitmentOffsetSales_Acct", validAccount.get_ID());
			
		return "";
	}
		
	//set Element Value
	protected String setElementValue(Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value, Object oldValue) {
		if(value == null) {
			return "";
		}
		
		int C_ValidCombination_ID = (int) value;
		MAccount validcombination = new MAccount(ctx, C_ValidCombination_ID, null);
		
		if(mField.getColumnName().equals("SuspenseBalancing_Acct"))
			mTab.setValue("C_ElementValue_SB_ID", validcombination.getAccount_ID());
		else if(mField.getColumnName().equals("CurrencyBalancing_Acct"))
			mTab.setValue("C_ElementValue_CB_ID", validcombination.getAccount_ID());
		else if(mField.getColumnName().equals("IntercompanyDueTo_Acct"))
			mTab.setValue("C_ElementValue_IDT_ID", validcombination.getAccount_ID());
		else if(mField.getColumnName().equals("IntercompanyDueFrom_Acct"))
			mTab.setValue("C_ElementValue_IDF_ID", validcombination.getAccount_ID());
		else if(mField.getColumnName().equals("PPVOffset_Acct"))
			mTab.setValue("C_ElementValue_PPVO_ID", validcombination.getAccount_ID());
		else if(mField.getColumnName().equals("CommitmentOffset_Acct"))
			mTab.setValue("C_ElementValue_CO_ID", validcombination.getAccount_ID());
		else if(mField.getColumnName().equals("CommitmentOffsetSales_Acct"))
			mTab.setValue("C_ElementValue_COS_ID", validcombination.getAccount_ID());
		return "";
	}




}
