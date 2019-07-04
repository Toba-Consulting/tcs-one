package id.tcs.model;

import java.sql.ResultSet;
import java.util.Properties;

public class MProductCategoryAcct extends X_M_Product_Category_Acct{

	public MProductCategoryAcct(Properties ctx, int M_Product_Category_Acct_ID, String trxName) {
		super(ctx, M_Product_Category_Acct_ID, trxName);
		// TODO Auto-generated constructor stub
	}
	
	public MProductCategoryAcct(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}
	
	protected boolean beforeSave(boolean newRecord) {
		return true;
	}

}
