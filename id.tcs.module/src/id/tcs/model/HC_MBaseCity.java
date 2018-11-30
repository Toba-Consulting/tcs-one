package id.tcs.model;

import java.sql.ResultSet;
import java.util.Properties;

public class HC_MBaseCity extends X_HC_BaseCity{

	public HC_MBaseCity(Properties ctx, int HC_BaseCity_ID, String trxName) 
	{
		super(ctx, HC_BaseCity_ID, trxName);

	}
	
	public HC_MBaseCity (Properties ctx, ResultSet rs, String trxName)
    {
      super (ctx, rs, trxName);
    }
}
