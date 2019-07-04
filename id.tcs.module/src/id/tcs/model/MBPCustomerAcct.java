package id.tcs.model;

import java.sql.ResultSet;
import java.util.Properties;

public class MBPCustomerAcct extends X_C_BP_Customer_Acct{

	public MBPCustomerAcct(Properties ctx, int C_BP_Customer_Acct_ID, String trxName) {
		super(ctx, C_BP_Customer_Acct_ID, trxName);
		// TODO Auto-generated constructor stub
	}
	
	public MBPCustomerAcct(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}
	
	protected boolean beforeSave(boolean newRecord) {
		return true;
	}

}
