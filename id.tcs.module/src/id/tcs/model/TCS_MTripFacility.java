package id.tcs.model;

import java.sql.ResultSet;
import java.util.Properties;

public class TCS_MTripFacility extends X_TCS_TripFacility{
	

    public TCS_MTripFacility (Properties ctx, int TCS_TripFacility_ID, String trxName)
    {
      super (ctx, TCS_TripFacility_ID, trxName);
    }
    
    public TCS_MTripFacility (Properties ctx, ResultSet rs, String trxName)
    {
      super (ctx, rs, trxName);
    }
    
}
