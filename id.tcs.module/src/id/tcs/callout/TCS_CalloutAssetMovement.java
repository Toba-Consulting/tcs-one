package id.tcs.callout;

import java.util.Properties;

import org.adempiere.base.IColumnCallout;
import org.compiere.model.CalloutEngine;
import org.compiere.model.GridField;
import org.compiere.model.GridTab;
import org.compiere.model.MAsset;
import org.compiere.util.Env;

public class TCS_CalloutAssetMovement extends CalloutEngine implements IColumnCallout{

	@Override
	public String start(Properties ctx, int WindowNo, GridTab mTab,
			GridField mField, Object value, Object oldValue) {
		
		if (mField.getColumnName().equals("A_Asset_ID")){
			return asset(ctx, WindowNo, mTab, mField, value);
		}
		
		return "";
	}

	/**
	 * @param ctx, windowNo, mTab, mField, value
	 * @return empty string
	 */
	private String asset(Properties ctx, int windowNo, GridTab mTab,
			GridField mField, Object value) {
		
		if(value == null)
		{
			mTab.setValue("C_BPartner_ID", null);
			mTab.setValue("LocationComment", null);
			return "";
		}
		
		int A_Asset_ID = (int) value;
		MAsset asset = new MAsset(Env.getCtx(), A_Asset_ID, null);
		mTab.setValue("C_BPartner_ID", asset.getC_BPartner_ID());
		mTab.setValue("LocationComment", asset.getLocationComment());
		mTab.setValue("AD_Org_ID", asset.getAD_Org_ID());
		
		return "";
	}


}
