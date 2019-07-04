package id.tcs.model;

import java.sql.ResultSet;
import java.util.Properties;

public class MTaxAcct extends X_C_Tax_Acct{

	public MTaxAcct(Properties ctx, int C_Tax_Acct_ID, String trxName) {
		super(ctx, C_Tax_Acct_ID, trxName);
		// TODO Auto-generated constructor stub
	}

	public MTaxAcct(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}
	
	protected boolean beforeSave(boolean newRecord) {
		return true;
	}
}
