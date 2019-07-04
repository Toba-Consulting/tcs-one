package id.tcs.model;

import java.sql.ResultSet;
import java.util.Properties;

public class MBPVendorAcct extends X_C_BP_Vendor_Acct{

	public MBPVendorAcct(Properties ctx, int C_BP_Vendor_Acct_ID, String trxName) {
		super(ctx, C_BP_Vendor_Acct_ID, trxName);
		// TODO Auto-generated constructor stub
	}
	
	public MBPVendorAcct(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}
	
	protected boolean beforeSave(boolean newRecord) {
		return true;
	}
	
	
}
