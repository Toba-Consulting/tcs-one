package id.tcs.callout;

import java.util.Properties;

import org.adempiere.base.IColumnCallout;
import org.compiere.model.CalloutEngine;
import org.compiere.model.GridField;
import org.compiere.model.GridTab;
import org.compiere.model.MBPartner;
import org.compiere.util.Env;

import id.tcs.model.MQuotation;

public class TCS_CalloutQuotation extends CalloutEngine implements IColumnCallout{

	
	public String setPriceList(Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value, Object oldValue) {
		if (value == null)
			return "";
		
		int C_BPartner_ID = (int) value;
		MBPartner partner = new MBPartner(Env.getCtx(), C_BPartner_ID, null);
		mTab.setValue(MQuotation.COLUMNNAME_M_PriceList_ID, partner.getM_PriceList_ID());
		return "";
	}
	
	@Override
	public String start(Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value, Object oldValue) {
		// TODO Auto-generated method stub
		if (mField.getColumnName().equals(MQuotation.COLUMNNAME_C_BPartner_ID)) {
			return setPriceList(ctx, WindowNo, mTab, mField, value, oldValue);
		}
		return null;
	}

}
