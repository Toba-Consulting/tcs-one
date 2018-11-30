package id.tcs.model;

import java.sql.ResultSet;
import java.util.Properties;

public class TCS_MDestRequest extends X_TCS_DestRequest{

	public TCS_MDestRequest(Properties ctx, int TCS_DestRequest_ID,String trxName) {
		super(ctx, TCS_DestRequest_ID, trxName);
	}

	public TCS_MDestRequest (Properties ctx, ResultSet rs, String trxName)
	{
		super (ctx, rs, trxName);
	}
	
}
