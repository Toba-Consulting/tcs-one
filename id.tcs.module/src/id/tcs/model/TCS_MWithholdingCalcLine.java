package id.tcs.model;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.util.Properties;

import org.compiere.model.Query;

public class TCS_MWithholdingCalcLine extends X_TCS_WithholdingCalcLine{

    public TCS_MWithholdingCalcLine (Properties ctx, int TCS_WithholdingCalcLine_ID, String trxName)
    {
      super (ctx, TCS_WithholdingCalcLine_ID, trxName);
    }
	
    public TCS_MWithholdingCalcLine (Properties ctx, ResultSet rs, String trxName)
    {
      super (ctx, rs, trxName);
    }
    
}
