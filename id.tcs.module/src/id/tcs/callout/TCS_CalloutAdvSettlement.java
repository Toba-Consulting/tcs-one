package id.tcs.callout;

import id.tcs.model.TCS_MAdvRequest;
import id.tcs.model.TCS_MAdvSettlement;

import java.math.BigDecimal;
import java.util.Properties;

import org.adempiere.base.IColumnCallout;
import org.compiere.model.GridField;
import org.compiere.model.GridTab;
import org.compiere.util.Env;

public class TCS_CalloutAdvSettlement implements IColumnCallout{

	private String advanceRequest(Properties ctx, int WindowNo, GridTab mTab,
			GridField mField, Object value, Object oldValue) {

		TCS_MAdvRequest request = new TCS_MAdvRequest(Env.getCtx(), (Integer)value, null);
		mTab.setValue("AD_Org_ID", request.getAD_Org_ID());
		mTab.setValue("C_Bpartner_ID", request.getC_BPartner_ID());
		mTab.setValue("C_Bpartner_ID", request.getC_BPartner_ID());
		mTab.setValue("DateFrom", request.getDateFrom());
		mTab.setValue("DateTo", request.getDateTo());
		mTab.setValue("Days", request.getDays());
		
//		calc(ctx, WindowNo, mTab, mField);
		
		return "";
	}
	
	private String grandTotal(Properties ctx, int WindowNo, GridTab mTab,
			GridField mField, Object value, Object oldValue) {
		
//		calc(ctx, WindowNo, mTab, mField);
		
		return "";
	}
	
	public String start(Properties ctx, int WindowNo, GridTab mTab,
			GridField mField, Object value, Object oldValue) {
		
		if(mField.getColumnName().equals("TCS_AdvRequest_ID"))
			return advanceRequest(ctx, WindowNo, mTab, mField, value,
			oldValue);
		
		if(mField.getColumnName().equals("GrandTotal"))
			return grandTotal(ctx, WindowNo, mTab, mField, value,
			oldValue);
		
		return null;
	}

/*
	private void calc(Properties ctx, int WindowNo, GridTab mTab,
			GridField mField) {

		TCS_MAdvRequest request = new TCS_MAdvRequest(Env.getCtx(), (Integer)mTab.getValue(TCS_MAdvSettlement.COLUMNNAME_TCS_AdvRequest_ID), null);
		
		BigDecimal difference=request.getGrandTotal().subtract((BigDecimal)mTab.getValue("GrandTotal"));
		
		if (difference.compareTo(Env.ZERO)>0) {
			mTab.setValue(TCS_MAdvSettlement.COLUMNNAME_AmountReturned, difference);
			mTab.setValue(TCS_MAdvSettlement.COLUMNNAME_AmountReimbursed, 0);			
		}
		else if (difference.compareTo(Env.ZERO)<0) {
			mTab.setValue(TCS_MAdvSettlement.COLUMNNAME_AmountReimbursed, difference.abs());
			mTab.setValue(TCS_MAdvSettlement.COLUMNNAME_AmountReturned, 0);

		}
		
	}
*/
	
}
