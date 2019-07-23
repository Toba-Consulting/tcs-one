package id.tcs.model;

import java.sql.ResultSet;
import java.util.Properties;

import org.adempiere.exceptions.AdempiereException;

public class MTCSAmortizationLine extends X_TCS_AmortizationLine{

	/**
	 * 
	 */
	private static final long serialVersionUID = 316016081079947769L;

	public MTCSAmortizationLine(Properties ctx, int TCS_AmortizationLine_ID,
			String trxName) {
		super(ctx, TCS_AmortizationLine_ID, trxName);
	}
	
	public MTCSAmortizationLine(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}
	
	/**
	 * 	After Save
	 *	@param newRecord new
	 *	@param success success
	 *	@return saved
	 */
	protected boolean afterSave (boolean newRecord, boolean success)
	{
		if(getTCS_AmortizationRun_ID() > 0){
			MTCSAmortizationRun aRun = (MTCSAmortizationRun) getTCS_AmortizationRun();
			aRun.setGrandTotal(aRun.getGrandTotal().add(getAmtAcct()));
			aRun.saveEx();
		}
		
		return true;
	}
	
	/**
	 * 	Before Delete
	 *	@return true if it can be deleted
	 */
	protected boolean beforeDelete ()
	{
		if(isProcessed() || getTCS_AmortizationRun_ID() > 0)
			throw new AdempiereException("Cant Delete Amortized Line");
		
		return true;
	}
	
}
