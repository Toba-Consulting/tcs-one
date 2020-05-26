package id.tcs.callout;

import java.util.Properties;

import org.adempiere.base.IColumnCallout;
import org.compiere.model.CalloutEngine;
import org.compiere.model.GridField;
import org.compiere.model.GridTab;

public class TCS_CalloutAsset extends CalloutEngine implements IColumnCallout {

	@Override
	public String start(Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value, Object oldValue) {
		if(mField.getColumnName().equals("Value")){
			return inventoryNo(ctx, WindowNo, mTab, mField, value);
		}
		return null;
	}

	private String inventoryNo(Properties ctx, int windowNo, GridTab mTab,
			GridField mField, Object value){
		
		if (value==null){
			return "";
		}
		
		String InventoryNo = (String) value;
		
		mTab.setValue("inventoryno", InventoryNo);
		return "";
	}

}
