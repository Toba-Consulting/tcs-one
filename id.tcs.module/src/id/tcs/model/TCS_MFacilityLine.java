package id.tcs.model;

import java.sql.ResultSet;
import java.util.Properties;

public class TCS_MFacilityLine extends X_TCS_FacilityLine{

	public TCS_MFacilityLine(Properties ctx, int TCS_FacilityLine_ID,String trxName) 
	{
		super(ctx, TCS_FacilityLine_ID, trxName);
	}

	public TCS_MFacilityLine (Properties ctx, ResultSet rs, String trxName)
    {
      super (ctx, rs, trxName);
    }
}
