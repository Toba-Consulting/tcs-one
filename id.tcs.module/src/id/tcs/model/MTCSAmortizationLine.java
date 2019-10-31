package id.tcs.model;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.util.Properties;

import org.adempiere.exceptions.AdempiereException;
import org.compiere.model.Query;

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

			String sql = "TCS_AmortizationRun_ID="+getTCS_AmortizationRun_ID();
			BigDecimal gTotal = new Query(getCtx(), Table_Name, sql, get_TrxName())
								.sum("AmtAcct");
			aRun.setGrandTotal(gTotal);
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
