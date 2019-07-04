package id.tcs.model;

import java.sql.ResultSet;
import java.util.Properties;

public class MBPGroupAcct extends X_C_BP_Group_Acct{

	public MBPGroupAcct(Properties ctx, int C_BP_Group_Acct_ID, String trxName) {
		super(ctx, C_BP_Group_Acct_ID, trxName);
		// TODO Auto-generated constructor stub
	}
	
	public MBPGroupAcct(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	protected boolean beforeSave(boolean newRecord) {
		return true;
	}
}
