package id.tcs.model;

import java.sql.ResultSet;
import java.util.Properties;

public class MMatchPR extends X_M_MatchPR{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3913936929908338253L;
	
	public MMatchPR(Properties ctx, int M_MatchPR_ID, String trxName) {
		super(ctx, M_MatchPR_ID, trxName);		
	}

	public MMatchPR(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}
	
}
