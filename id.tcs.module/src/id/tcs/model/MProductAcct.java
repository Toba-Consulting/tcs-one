package id.tcs.model;

import java.sql.ResultSet;
import java.util.Properties;

public class MProductAcct extends X_M_Product_Acct{

	public MProductAcct(Properties ctx, int M_Product_Acct_ID, String trxName) {
		super(ctx, M_Product_Acct_ID, trxName);
		// TODO Auto-generated constructor stub
	}
	
	public MProductAcct(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}
	
	protected boolean beforeSave(boolean newRecord) {
		return true;
	}

}
