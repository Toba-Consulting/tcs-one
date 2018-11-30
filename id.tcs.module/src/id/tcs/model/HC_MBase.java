package id.tcs.model;

import java.sql.ResultSet;
import java.util.Properties;

public class HC_MBase extends X_HC_Base{

	public HC_MBase(Properties ctx, int HC_Base_ID, String trxName) {
		super(ctx, HC_Base_ID, trxName);
	}
	
    public HC_MBase (Properties ctx, ResultSet rs, String trxName)
    {
      super (ctx, rs, trxName);
    }
}
