package id.tcs.model;

import java.sql.ResultSet;
import java.util.Properties;

public class MChargeAcct extends X_C_Charge_Acct{

	public MChargeAcct(Properties ctx, int C_Charge_Acct_ID, String trxName) {
		super(ctx, C_Charge_Acct_ID, trxName);
		// TODO Auto-generated constructor stub
	}
	
	public MChargeAcct(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}
	
	protected boolean beforeSave(boolean newRecord) {
		return true;
	}
}
