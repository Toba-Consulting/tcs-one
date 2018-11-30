package id.tcs.model;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.util.Properties;

import org.adempiere.exceptions.AdempiereException;
import org.compiere.util.DB;

public class TCS_MAdvSettlementLine extends X_TCS_AdvSettlementLine{
	
	public TCS_MAdvSettlementLine(Properties ctx, int TCS_AdvSettlement_ID,String trxName) {
		super(ctx, TCS_AdvSettlement_ID, trxName);
	}

	public TCS_MAdvSettlementLine (Properties ctx, ResultSet rs, String trxName)
	{
		super (ctx, rs, trxName);
	}
	
	
	@Override
	protected boolean beforeSave(boolean newRecord) {

		TCS_MTripFacility tripFacility = new TCS_MTripFacility(getCtx(), getTCS_TripFacility_ID(), get_TrxName());
		TCS_MFacilityLine facilityLine = new TCS_MFacilityLine(getCtx(), getTCS_FacilityLine_ID(), get_TrxName());
		
		if (tripFacility.isFixed() && facilityLine.getPrice().compareTo(getPriceEntered())!=0)
			throw new AdempiereException("Price Of Facility is Fixed At "+facilityLine.getPrice());		
		else if (tripFacility.isLimited() && facilityLine.getPrice().compareTo(getPriceEntered())<0)
			throw new AdempiereException("Price Of Facility Is Limited To "+facilityLine.getPrice());		

		
		return true;
	}
	
	@Override
	protected boolean afterSave(boolean newRecord, boolean success) {

		TCS_MDestSettlement destSettlement = new TCS_MDestSettlement(getCtx(), getTCS_DestSettlement_ID(), get_TrxName());
		TCS_MAdvSettlement advSettlement = new TCS_MAdvSettlement(getCtx(), destSettlement.getTCS_AdvSettlement_ID(), get_TrxName());
		
		String sql="SELECT SUM(Amt) FROM TCS_AdvSettlementLine asl "+
				"JOIN TCS_DestSettlement ds ON ds.TCS_DestSettlement_ID=asl.TCS_DestSettlement_ID "+
				"WHERE ds.TCS_AdvSettlement_ID=? ;";
		BigDecimal grandTotal=DB.getSQLValueBD(get_TrxName(), sql, advSettlement.getTCS_AdvSettlement_ID());
		advSettlement.setGrandTotal(grandTotal);
		advSettlement.saveEx();
		
		return true;
	}
	
	@Override
	protected boolean afterDelete(boolean success) {
		
		TCS_MDestSettlement destSettlement = new TCS_MDestSettlement(getCtx(), getTCS_DestSettlement_ID(), get_TrxName());
		TCS_MAdvSettlement advSettlement = new TCS_MAdvSettlement(getCtx(), destSettlement.getTCS_AdvSettlement_ID(), get_TrxName());
		
		String sql="SELECT SUM(Amt) FROM TCS_AdvSettlementLine asl "+
				"JOIN TCS_DestSettlement ds ON ds.TCS_DestSettlement_ID=asl.TCS_DestSettlement_ID "+
				"WHERE ds.TCS_AdvSettlement_ID=? ;";
		BigDecimal grandTotal=DB.getSQLValueBD(get_TrxName(), sql, advSettlement.getTCS_AdvSettlement_ID());
		advSettlement.setGrandTotal(grandTotal);
		advSettlement.saveEx();
		
		return true;
	}
}
