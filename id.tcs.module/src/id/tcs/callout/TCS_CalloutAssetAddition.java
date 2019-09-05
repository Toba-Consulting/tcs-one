package id.tcs.callout;

import java.util.Properties;

import org.adempiere.base.IColumnCallout;
import org.compiere.model.GridField;
import org.compiere.model.GridTab;
import org.compiere.model.MAsset;
import org.compiere.model.MAssetAddition;
import org.compiere.util.DB;

public class TCS_CalloutAssetAddition implements IColumnCallout{

	@Override
	public String start(Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value, Object oldValue) {
		System.out.println();
		
		if(mField.getColumnName().equals("A_CapvsExp"))
			return setUseLifeYears(ctx, WindowNo, mTab, mField, value, oldValue);
		
		if(mField.getColumnName().equals("A_CreateAsset"))
			return setUseLifeYears(ctx, WindowNo, mTab, mField, value, oldValue);
		
		if(mField.getColumnName().equals("A_Asset_ID"))
			return setAssetGroup(ctx, WindowNo, mTab, mField, value, oldValue);
		
		return null;
	}

//	private String setDeltaToZero(Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value, Object oldValue) {
//		if(value == null)
//			return "";
//		
//		mTab.setValue("UseLifeYears", 0);
//		mTab.setValue("UseLifeYears_F", 0);
//		
//		return "";
//	}

	private String setUseLifeYears(Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value, Object oldValue) {
		//validate: check value is not null
		if(value == null)
			return "";
		
		//to set use life years based on asset group acct
		if(value.equals(MAssetAddition.A_CAPVSEXP_Capital) && mTab.getValueAsBoolean("A_CreateAsset")) {
			String sqlUseLifeYears =
					"SELECT UseLifeYears FROM "
				  + "A_Asset_Group_Acct "
				  + "WHERE A_Asset_Group_ID = " + mTab.getValue("A_Asset_Group_ID");
			
			String sqlUseLifeYears_F =
					"SELECT UseLifeYears_F FROM "
				  + "A_Asset_Group_Acct "
				  + "WHERE A_Asset_Group_ID = " + mTab.getValue("A_Asset_Group_ID");
			
			int useLifeYears = DB.getSQLValue(null, sqlUseLifeYears);
			int useLifeYears_F = DB.getSQLValue(null, sqlUseLifeYears_F);
			
			mTab.setValue("UseLifeYears", useLifeYears);
			mTab.setValue("UseLifeYears_F", useLifeYears_F);	
		}	
		//set use life years to 0 if capital/expense = expense or create asset = 'N'
		else if(value.equals(MAssetAddition.A_CAPVSEXP_Expense) || !mTab.getValueAsBoolean("A_CreateAsset")){
			mTab.setValue("UseLifeYears", 0);
			mTab.setValue("UseLifeYears_F", 0);		
		}
		
		return "";
	}
	
	private String setAssetGroup(Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value, Object oldValue) {
		if(value == null)
			return "";
		
		MAsset asset = new MAsset(ctx, (int) value, null);
		
		mTab.setValue("A_Asset_Group_ID", asset.getA_Asset_Group_ID());
		
		return "";
	}
}
