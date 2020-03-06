package id.tcs.callout;

import java.util.Properties;

import org.adempiere.base.IColumnCallout;
import org.compiere.model.GridField;
import org.compiere.model.GridTab;
import org.compiere.model.MProduct;

public class TCS_Callout_PP_Product_BOMLine implements IColumnCallout {

	@Override
	public String start(Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value, Object oldValue) {
		if (mField.getColumnName().equals("M_Product_ID"))
			return updateUOM(ctx, WindowNo, mTab, mField, value);

		return null;
	}

	private String updateUOM(Properties ctx, int windowNo, GridTab mTab, GridField mField, Object value) {
		if (value == null)
			return "";

		MProduct product = new MProduct(ctx, (int) value, null);
		mTab.setValue("C_UOM_ID", product.getC_UOM_ID());
		return "";
	}

}
