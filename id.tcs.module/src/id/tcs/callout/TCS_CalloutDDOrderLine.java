package id.tcs.callout;

import java.util.Properties;

import org.adempiere.base.IColumnCallout;
import org.compiere.model.CalloutEngine;
import org.compiere.model.GridField;
import org.compiere.model.GridTab;
import org.compiere.model.MProduct;
import org.eevolution.model.MDDOrderLine;

public class TCS_CalloutDDOrderLine extends CalloutEngine implements IColumnCallout {

	
	@Override
	public String start(Properties ctx, int WindowNo, GridTab mTab,
			GridField mField, Object value, Object oldValue) {
		
		if(mField.getColumnName().equals(MDDOrderLine.COLUMNNAME_M_Product_ID)){
			return Product(ctx, WindowNo, mTab, mField, value);
		}
		
		return null;
	}

	@SuppressWarnings("static-access")
	public String Product(Properties ctx, int WindowNo, GridTab mTab,
			GridField mField, Object value) {
		
		MDDOrderLine InterWHLine = new MDDOrderLine(ctx, 0, null);
		
		Integer M_Product_ID = (Integer) value;
		if (M_Product_ID == null || M_Product_ID.intValue() == 0)
			return "";
		
		MProduct product = new MProduct(ctx, M_Product_ID, null);

		int prod = (int) product.get_Value("M_Product_ID");
		if (prod >0){
			mTab.setValue(InterWHLine.COLUMNNAME_C_UOM_ID, product.getC_UOM_ID());
		}

		return "";
	}
	
	

}
