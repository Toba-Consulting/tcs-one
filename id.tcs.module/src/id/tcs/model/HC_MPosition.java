package id.tcs.model;

import java.sql.ResultSet;
import java.util.Properties;

public class HC_MPosition extends X_HC_Position{

	public HC_MPosition(Properties ctx, int HC_Position_ID, String trxName) {
		super(ctx, HC_Position_ID, trxName);
	}

	public HC_MPosition (Properties ctx, ResultSet rs, String trxName)
    {
      super (ctx, rs, trxName);
    }
}
