package id.tcs.callout;

import java.util.Properties;

import org.adempiere.base.IColumnCallout;
import org.compiere.model.GridField;
import org.compiere.model.GridTab;
import org.compiere.model.MAsset;
import org.compiere.util.Env;

public class TCS_CalloutAssetReval implements IColumnCallout{

	public String asset(Properties ctx, int WindowNo, GridTab mTab,
			GridField mField, Object value, Object oldValue){
		
		MAsset asset = new MAsset(Env.getCtx(), (Integer)value, null);
		mTab.setValue(asset.COLUMNNAME_AD_Org_ID, asset.getAD_Org_ID());
		
		return null;
	}
	
	@Override
	public String start(Properties ctx, int WindowNo, GridTab mTab,
			GridField mField, Object value, Object oldValue) {
		
		if(mField.getColumnName().equals(MAsset.COLUMNNAME_A_Asset_ID))
			return asset(ctx, WindowNo, mTab, mField, value,oldValue);

		return null;
	}

}
