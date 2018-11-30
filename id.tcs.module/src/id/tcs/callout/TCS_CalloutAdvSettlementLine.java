package id.tcs.callout;

import id.tcs.model.HC_MBaseCity;
import id.tcs.model.HC_MPosition;
import id.tcs.model.TCS_MAdvRequestLine;
import id.tcs.model.TCS_MAdvSettlement;
import id.tcs.model.TCS_MDestSettlement;
import id.tcs.model.TCS_MFacilityLine;
import id.tcs.model.TCS_MTripFacility;

import java.math.BigDecimal;
import java.util.Properties;

import org.adempiere.base.IColumnCallout;
import org.compiere.model.GridField;
import org.compiere.model.GridTab;
import org.compiere.model.MBPartner;
import org.compiere.model.Query;
import org.compiere.util.Env;

public class TCS_CalloutAdvSettlementLine implements IColumnCallout{

	
	private String advRequestLine(Properties ctx, int WindowNo, GridTab mTab,
			GridField mField, Object value, Object oldValue) {
		
		TCS_MAdvRequestLine advRequestLine = new TCS_MAdvRequestLine(Env.getCtx(), (Integer)value, null);
		
		mTab.setValue("C_Charge_ID", advRequestLine.getC_Charge_ID());
		mTab.setValue("SeqNo", advRequestLine.getSeqNo());
		mTab.setValue("TCS_TripFacility_ID", advRequestLine.getTCS_TripFacility_ID());
		mTab.setValue("PriceEntered", advRequestLine.getPriceEntered());
		mTab.setValue("Qty", advRequestLine.getQty());
		mTab.setValue("Amt", advRequestLine.getAmt());
		mTab.setValue("TCS_FacilityLine_ID", advRequestLine.getTCS_FacilityLine_ID());
		return "";
	}
	
	private String tripFacility(Properties ctx, int WindowNo, GridTab mTab,
			GridField mField, Object value, Object oldValue) {

		return findFacilityLineID(ctx, WindowNo, mTab, mField);
	}
	
	private String qty(Properties ctx, int WindowNo, GridTab mTab,
			GridField mField, Object value, Object oldValue) {
		
		calc(ctx, WindowNo, mTab, mField);
		
		return "";	
	}
	
	private String priceEntered(Properties ctx, int WindowNo, GridTab mTab,
			GridField mField, Object value, Object oldValue) {
		
		calc(ctx, WindowNo, mTab, mField);
	
		return "";
	}
	
	private String charge(Properties ctx, int WindowNo, GridTab mTab,
			GridField mField, Object value, Object oldValue) {
			
		return findFacilityLineID(ctx, WindowNo, mTab, mField);
	}
	
	@Override
	public String start(Properties ctx, int WindowNo, GridTab mTab,
			GridField mField, Object value, Object oldValue) {

		if(mField.getColumnName().equals("TCS_AdvRequestLine_ID"))
			return advRequestLine(ctx, WindowNo, mTab, mField, value,oldValue);
		
		if(mField.getColumnName().equals("TCS_TripFacility_ID"))
			return tripFacility(ctx, WindowNo, mTab, mField, value,oldValue);

		if(mField.getColumnName().equals("Qty"))
			return qty(ctx, WindowNo, mTab, mField, value,oldValue);
		
		if(mField.getColumnName().equals("PriceEntered"))
			return priceEntered(ctx, WindowNo, mTab, mField, value,oldValue);

		if(mField.getColumnName().equals("C_Charge_ID"))
			return charge(ctx, WindowNo, mTab, mField, value,oldValue);
		
		return null;
	}

	private String findFacilityLineID(Properties ctx, int WindowNo, GridTab mTab,GridField mField)
	{
		boolean a=mTab.getValue("TCS_TripFacility_ID")!=null;
		boolean b=mTab.getValue("C_Charge_ID")!=null;
		
		if (a && b) {
		
		TCS_MTripFacility tripFacility = new TCS_MTripFacility(Env.getCtx(), (Integer)mTab.getValue("TCS_TripFacility_ID"), null);
		TCS_MDestSettlement destSettlement = new TCS_MDestSettlement(Env.getCtx(), (Integer)mTab.getValue("TCS_DestSettlement_ID"), null);
		HC_MBaseCity baseCity = new HC_MBaseCity(Env.getCtx(), destSettlement.getHC_BaseCityTo_ID(), null);
		TCS_MAdvSettlement settlement = (TCS_MAdvSettlement)destSettlement.getTCS_AdvSettlement();
		MBPartner bp = (MBPartner)settlement.getC_BPartner();		
	
		if (bp.get_ValueAsInt("HC_Position_ID")==0) {
			return "Business Partner Belum Memiliki Position";
		}
		
		HC_MPosition position = new HC_MPosition(Env.getCtx(), bp.get_ValueAsInt("HC_Position_ID"), null);
		
		String sql="TCS_TripFacility_ID= "+tripFacility.getTCS_TripFacility_ID()+
				" AND C_Charge_ID="+mTab.getValue("C_Charge_ID")+
				" AND HC_Base_ID="+baseCity.getHC_Base_ID()+
				" AND Level="+position.getLevelNo();

		int facilityLines[] = new Query(Env.getCtx(),"TCS_FacilityLine",sql,null).getIDs();
	
		if (facilityLines.length<1) {
			return "Belum Ada Trip Facility Line";
		}
		if (facilityLines.length>1) {
			return "Trip Facility Line Yang Ditemukan Lebih Dari 1";
		}
		TCS_MFacilityLine facilityLine = new TCS_MFacilityLine(Env.getCtx(), facilityLines[0], null);

		mTab.setValue("TCS_FacilityLine_ID", facilityLine.getTCS_FacilityLine_ID());
		mTab.setValue("PriceEntered", facilityLine.getPrice());
		}
		calc(ctx, WindowNo, mTab, mField);
		return "";
	}
	
	private void calc(Properties ctx, int WindowNo, GridTab mTab,GridField mField)
	{
		BigDecimal qty=(BigDecimal)mTab.getValue("Qty");
		BigDecimal price=(BigDecimal)mTab.getValue("PriceEntered");
		mTab.setValue("Amt", qty.multiply(price));

	}
}
