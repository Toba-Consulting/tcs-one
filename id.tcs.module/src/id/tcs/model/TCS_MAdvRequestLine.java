package id.tcs.model;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.util.Properties;

import org.adempiere.exceptions.AdempiereException;
import org.compiere.model.Query;
import org.compiere.util.DB;
import org.compiere.util.Env;

public class TCS_MAdvRequestLine extends X_TCS_AdvRequestLine{

	public TCS_MAdvRequestLine(Properties ctx, int TCS_AdvRequestLine_ID,String trxName) {
		super(ctx, TCS_AdvRequestLine_ID, trxName);
	}

	public TCS_MAdvRequestLine (Properties ctx, ResultSet rs, String trxName)
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

		//Update Header GrandTotal
		TCS_MDestRequest destRequest = new TCS_MDestRequest(Env.getCtx(), getTCS_DestRequest_ID(), null);
		TCS_MAdvRequest advRequest = new TCS_MAdvRequest(getCtx(), destRequest.getTCS_AdvRequest_ID(), get_TrxName());
		
		String sql="SELECT SUM(Amt) FROM TCS_AdvRequestLine arl "+
				"JOIN TCS_DestRequest dr ON dr.TCS_DestRequest_ID=arl.TCS_DestRequest_ID "+
				"WHERE dr.TCS_AdvRequest_ID=? ;";
		BigDecimal grandTotal=DB.getSQLValueBD(get_TrxName(), sql, advRequest.getTCS_AdvRequest_ID());
		advRequest.setGrandTotal(grandTotal);
		advRequest.saveEx();
		
		return true;
	}
	
	@Override
	protected boolean afterDelete(boolean success) {
		
		TCS_MDestRequest destRequest = new TCS_MDestRequest(getCtx(), getTCS_DestRequest_ID(), get_TrxName());
		TCS_MAdvRequest advRequest = new TCS_MAdvRequest(getCtx(), destRequest.getTCS_AdvRequest_ID(), get_TrxName());
		
		String sql="SELECT SUM(Amt) FROM TCS_AdvRequestLine arl "+
				"JOIN TCS_DestRequest dr ON dr.TCS_DestRequest_ID=arl.TCS_DestRequest_ID "+
				"WHERE dr.TCS_AdvRequest_ID=? ;";
		BigDecimal grandTotal=DB.getSQLValueBD(get_TrxName(), sql, advRequest.getTCS_AdvRequest_ID());
		advRequest.setGrandTotal(grandTotal);
		advRequest.saveEx();
		
		return true;
	}
}
