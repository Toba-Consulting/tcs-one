package id.tcs.model;

import java.sql.ResultSet;
import java.util.Properties;

public class MValidCombination extends X_C_ValidCombination{
	public MValidCombination(Properties ctx, int C_ValidCombination_ID, String trxName) {
		super(ctx, C_ValidCombination_ID, trxName);
		// TODO Auto-generated constructor stub
	}
	
	public MValidCombination(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}
	
	protected boolean beforeSave(boolean newRecord) {
		return true;
	}
}
