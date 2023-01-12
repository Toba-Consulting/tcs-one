package id.tcs.callout;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Properties;

import org.adempiere.base.IColumnCallout;
import org.compiere.model.CalloutEngine;
import org.compiere.model.GridField;
import org.compiere.model.GridTab;
import org.compiere.model.MProduct;
import org.compiere.model.MRMALine;
import org.compiere.util.DB;
import org.compiere.util.Env;

public class TCS_CalloutOrderLine extends CalloutEngine implements IColumnCallout{
	
	@Override
	public String start(Properties ctx, int WindowNo, GridTab mTab,
			GridField mField, Object value, Object oldValue) {
		if (mField.getColumnName().equals("PriceList")){
			return setDiscount(ctx, WindowNo, mTab, mField, value) ;
		}
		else if (mField.getColumnName().equals("M_Product_ID")){
			return setBOM(ctx, WindowNo, mTab, mField, value) ;
		}
		return "";
	}
	
	
	private String setBOM(Properties ctx, int windowNo, GridTab mTab, GridField mField, Object value) {
		int M_Product_ID =  (int) value;
		MProduct prod = new MProduct(ctx, M_Product_ID, null);
		mTab.setValue("IsBOM", prod.get_Value("IsBOM"));
		
		return "";
	}


	private String setDiscount(Properties ctx, int windowNo, GridTab mTab, GridField mField, Object value) {
		BigDecimal pricelist =  (BigDecimal) value;
		if(pricelist.compareTo(Env.ZERO) == 0)
			pricelist = Env.ONE;
		
		BigDecimal discount = pricelist.subtract((BigDecimal) mTab.getValue("PriceActual"))
				.multiply(Env.ONEHUNDRED)
				.divide(pricelist, 2, RoundingMode.HALF_UP);
		Boolean BOMDrop = mTab.getValueAsBoolean("IsBOMDrop");

		if(BOMDrop)
			mTab.setValue("Discount", Env.ZERO);
		else
			mTab.setValue("Discount", discount);
		
		return "";
	}

	
}
