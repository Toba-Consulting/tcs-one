package id.tcs.model;

import java.sql.ResultSet;
import java.util.Properties;

public class TCS_MDestSettlement extends X_TCS_DestSettlement{
	
	public TCS_MDestSettlement(Properties ctx, int TCS_AdvSettlement_ID,String trxName) {
		super(ctx, TCS_AdvSettlement_ID, trxName);
	}

	public TCS_MDestSettlement (Properties ctx, ResultSet rs, String trxName)
	{
		super (ctx, rs, trxName);
	}
}
