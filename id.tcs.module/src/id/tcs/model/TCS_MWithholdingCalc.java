package id.tcs.model;

import java.sql.ResultSet;
import java.util.Properties;

public class TCS_MWithholdingCalc extends X_TCS_WithholdingCalc{

    public TCS_MWithholdingCalc (Properties ctx, int TCS_WithholdingCalc_ID, String trxName)
    {
      super (ctx, TCS_WithholdingCalc_ID, trxName);
    }
    public TCS_MWithholdingCalc (Properties ctx, ResultSet rs, String trxName)
    {
      super (ctx, rs, trxName);
    }
}
