package org.banktransfer.callout;

import java.util.Properties;

import org.adempiere.base.IColumnCallout;
import org.banktransfer.model.MAcctSchemaGL;
import org.banktransfer.model.MValidCombination;
import org.compiere.model.GridField;
import org.compiere.model.GridTab;
import org.compiere.model.MAccount;
import org.compiere.model.MAcctSchema;
import org.compiere.model.MBankAccount;
import org.compiere.model.MField;
import org.compiere.util.Env;

public class CalloutAccountingSchemaGL implements IColumnCallout{

	@Override
	public String start(Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value, Object oldValue) {
		// TODO Auto-generated method stub
		if (mField.getColumnName().equals(MAcctSchemaGL.COLUMNNAME_C_ElementValue_SB_ID))
			return setSuspenseBalancingAcct(ctx, WindowNo, mTab, mField, value, oldValue);
		if (mField.getColumnName().equals(MAcctSchemaGL.COLUMNNAME_C_ElementValue_CB_ID))
			return setCurrencyBalancingAcct(ctx, WindowNo, mTab, mField, value, oldValue);
		if (mField.getColumnName().equals(MAcctSchemaGL.COLUMNNAME_C_ElementValue_IDT_ID))
			return setIntercompanyDueToAcct(ctx, WindowNo, mTab, mField, value, oldValue);
		if (mField.getColumnName().equals(MAcctSchemaGL.COLUMNNAME_C_ElementValue_IDF_ID))
			return setIntercompanyDueFromAcct(ctx, WindowNo, mTab, mField, value, oldValue);
		if (mField.getColumnName().equals(MAcctSchemaGL.COLUMNNAME_C_ElementValue_PPVO_ID))
			return setPPVOffsetAcct(ctx, WindowNo, mTab, mField, value, oldValue);
		if (mField.getColumnName().equals(MAcctSchemaGL.COLUMNNAME_C_ElementValue_CO_ID))
			return setCommitmentOffsetAcct(ctx, WindowNo, mTab, mField, value, oldValue);
		if (mField.getColumnName().equals(MAcctSchemaGL.COLUMNNAME_C_ElementValue_COS_ID))
			return setCommitmentOffsetSalesAcct(ctx, WindowNo, mTab, mField, value, oldValue);
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
	
	// set Suspense Balancing Acct
	protected String setSuspenseBalancingAcct(Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value, Object oldValue) {
		if(value == null)
			return "";
		
		MAccount validAccount = MAccount.get(ctx, (int)mTab.getValue("AD_Client_ID"), 0, (int)mTab.getValue("C_AcctSchema_ID"), (int)mTab.getValue("C_ElementValue_SB_ID"), 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, null);
		mTab.setValue("SuspenseBalancing_Acct", validAccount.get_ID());
		return "";
	}
	
	// set Currency Balancing Acct
	protected String setCurrencyBalancingAcct(Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value, Object oldValue) {
		if(value == null)
			return "";
			
		MAccount validAccount = MAccount.get(ctx, (int)mTab.getValue("AD_Client_ID"), 0, (int)mTab.getValue("C_AcctSchema_ID"), (int)mTab.getValue("C_ElementValue_CB_ID"), 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, null);
		mTab.setValue("CurrencyBalancing_Acct", validAccount.get_ID());
		return "";
	}

	// set Intercompany Due To Acct
	protected String setIntercompanyDueToAcct(Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value, Object oldValue) {
		if(value == null)
			return "";
			
		MAccount validAccount = MAccount.get(ctx, (int)mTab.getValue("AD_Client_ID"), 0, (int)mTab.getValue("C_AcctSchema_ID"), (int)mTab.getValue("C_ElementValue_IDT_ID"), 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, null);
		mTab.setValue("IntercompanyDueTo_Acct", validAccount.get_ID());
		return "";
	}
	
	// set Intercompany Due From Acct
	protected String setIntercompanyDueFromAcct(Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value, Object oldValue) {
		if(value == null)
			return "";
			
		MAccount validAccount = MAccount.get(ctx, (int)mTab.getValue("AD_Client_ID"), 0, (int)mTab.getValue("C_AcctSchema_ID"), (int)mTab.getValue("C_ElementValue_IDF_ID"), 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, null);
		mTab.setValue("IntercompanyDueFrom_Acct", validAccount.get_ID());
		return "";
	}
	
	// set PPV Offset Acct 
	protected String setPPVOffsetAcct(Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value, Object oldValue) {
		if(value == null)
			return "";
				
		MAccount validAccount = MAccount.get(ctx, (int)mTab.getValue("AD_Client_ID"), 0, (int)mTab.getValue("C_AcctSchema_ID"), (int)mTab.getValue("C_ElementValue_PPVO_ID"), 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, null);
		mTab.setValue("PPVOffset_Acct", validAccount.get_ID());
		return "";
	}	
	
	// set Commitment Offset Acct 
	protected String setCommitmentOffsetAcct(Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value, Object oldValue) {
		if(value == null)
			return "";
				
		MAccount validAccount = MAccount.get(ctx, (int)mTab.getValue("AD_Client_ID"), 0, (int)mTab.getValue("C_AcctSchema_ID"), (int)mTab.getValue("C_ElementValue_CO_ID"), 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, null);
		mTab.setValue("CommitmentOffset_Acct", validAccount.get_ID());
		return "";
	}		
	
	// set Commitment Offset Sales Acct 
	protected String setCommitmentOffsetSalesAcct(Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value, Object oldValue) {
		if(value == null)
			return "";
				
		MAccount validAccount = MAccount.get(ctx, (int)mTab.getValue("AD_Client_ID"), 0, (int)mTab.getValue("C_AcctSchema_ID"), (int)mTab.getValue("C_ElementValue_COS_ID"), 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, null);
		mTab.setValue("CommitmentOffsetSales_Acct", validAccount.get_ID());
		return "";
	}	
	
	//set Element Value
	protected String setElementValue(Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value, Object oldValue) {
		if(value == null) {
			return "";
		}
		
		int C_ValidCombination_ID = (int) value;
		MValidCombination validcombination = new MValidCombination(ctx, C_ValidCombination_ID, null);
		
		if(mField.getColumnName().equals("SuspenseBalancing_Acct"))
			mTab.setValue(MAcctSchemaGL.COLUMNNAME_C_ElementValue_SB_ID, validcombination.getAccount_ID());
		else if(mField.getColumnName().equals("CurrencyBalancing_Acct"))
			mTab.setValue(MAcctSchemaGL.COLUMNNAME_C_ElementValue_CB_ID, validcombination.getAccount_ID());
		else if(mField.getColumnName().equals("IntercompanyDueTo_Acct"))
			mTab.setValue(MAcctSchemaGL.COLUMNNAME_C_ElementValue_IDT_ID, validcombination.getAccount_ID());
		else if(mField.getColumnName().equals("IntercompanyDueFrom_Acct"))
			mTab.setValue(MAcctSchemaGL.COLUMNNAME_C_ElementValue_IDF_ID, validcombination.getAccount_ID());
		else if(mField.getColumnName().equals("PPVOffset_Acct"))
			mTab.setValue(MAcctSchemaGL.COLUMNNAME_C_ElementValue_PPVO_ID, validcombination.getAccount_ID());
		else if(mField.getColumnName().equals("CommitmentOffset_Acct"))
			mTab.setValue(MAcctSchemaGL.COLUMNNAME_C_ElementValue_CO_ID, validcombination.getAccount_ID());
		else if(mField.getColumnName().equals("CommitmentOffsetSales_Acct"))
			mTab.setValue(MAcctSchemaGL.COLUMNNAME_C_ElementValue_COS_ID, validcombination.getAccount_ID());
		return "";
	}

}
