package id.tcs.model;

import java.sql.ResultSet;
import java.util.Properties;

public class MTCSMatchAllocation extends X_TCS_Match_Allocation {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5847169908983074438L;

	public MTCSMatchAllocation(Properties ctx, int TCS_Match_Allocation_ID, String trxName) {
		super(ctx, TCS_Match_Allocation_ID, trxName);
	}

	public MTCSMatchAllocation(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	
}
