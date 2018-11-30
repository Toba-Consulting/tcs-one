package id.tcs.model;

import java.sql.ResultSet;
import java.util.Properties;

public class TCS_MTravelExpense extends X_TCS_TravelExpense{

	public TCS_MTravelExpense(Properties ctx, int TCS_TravelExpense_ID,String trxName) 
	{
		super(ctx, TCS_TravelExpense_ID, trxName);
	}

    public TCS_MTravelExpense (Properties ctx, ResultSet rs, String trxName)    
    {
      super (ctx, rs, trxName);
    }
    
    
}
