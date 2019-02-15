package id.tcs.model;

import java.sql.ResultSet;
import java.util.Properties;

public class TCS_MWithholdingRate extends X_TCS_WithholdingRate{

	public TCS_MWithholdingRate(Properties ctx, int TCS_WithholdingRate_ID, String trxName) {
		super(ctx, TCS_WithholdingRate_ID, trxName);
	}

    public TCS_MWithholdingRate (Properties ctx, ResultSet rs, String trxName)
    {
      super (ctx, rs, trxName);
    }
    
}
