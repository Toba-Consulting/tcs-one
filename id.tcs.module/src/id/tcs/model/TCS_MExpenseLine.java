package id.tcs.model;

import java.sql.ResultSet;
import java.util.Properties;

public class TCS_MExpenseLine extends X_TCS_ExpenseLine{

	public TCS_MExpenseLine(Properties ctx, int TCS_ExpenseLine_ID,String trxName) {
		super(ctx, TCS_ExpenseLine_ID, trxName);
	}

    public TCS_MExpenseLine (Properties ctx, ResultSet rs, String trxName)
    {
      super (ctx, rs, trxName);
    }
 
    @Override
    protected boolean beforeSave(boolean newRecord) {
    
    	setAmt(getQty().multiply(getPriceEntered()));
    	
    	
    	return true;
    }
    
}
