package id.tcs.model;

import java.sql.ResultSet;
import java.util.List;
import java.util.Properties;

import org.adempiere.exceptions.AdempiereException;
import org.compiere.model.Query;

public class MTCSAmortizationPlan extends X_TCS_AmortizationPlan{

	/**
	 * 
	 */
	private static final long serialVersionUID = 608033154362461412L;

	public MTCSAmortizationPlan(Properties ctx, int TCS_AmortizationPlan_ID,
			String trxName) {
		super(ctx, TCS_AmortizationPlan_ID, trxName);
	}
	
	public MTCSAmortizationPlan(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/**
	 * 	Before Delete
	 *	@return true if it can be deleted
	 */
	protected boolean beforeDelete ()
	{
		if(getLines().length > 0){
			throw new AdempiereException("Cant Delete - Lines not empty");
		}
		
		return true;
	}
	
	/**
	 * get amortization line
	 * @return array of amortization line
	 */
	public MTCSAmortizationLine[] getLines(){
		
		final String whereClause = MTCSAmortizationLine.COLUMNNAME_TCS_AmortizationPlan_ID
				+ "=?";
		List<MTCSAmortizationLine> list = new Query(getCtx(), MTCSAmortizationLine.Table_Name, 
				whereClause, get_TrxName())
				.setParameters(get_ID())
				.setOnlyActiveRecords(true)
				.list();
		//
		MTCSAmortizationLine[] lines = new MTCSAmortizationLine[list.size()];
		list.toArray(lines);
		return lines;
	}
	
}
