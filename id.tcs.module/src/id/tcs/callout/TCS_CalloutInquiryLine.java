package id.tcs.callout;

import id.tcs.model.I_C_InquiryLine;
import id.tcs.model.MInquiry;
import id.tcs.model.MInquiryLine;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Properties;

import org.adempiere.base.IColumnCallout;
import org.adempiere.model.GridTabWrapper;
import org.compiere.model.CalloutEngine;
import org.compiere.model.GridField;
import org.compiere.model.GridTab;
import org.compiere.model.MProduct;
import org.compiere.model.MProductPricing;
import org.compiere.util.DB;
import org.compiere.util.Env;

public class TCS_CalloutInquiryLine extends CalloutEngine implements IColumnCallout{
	
	private boolean steps = false;
	
	public String product (Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{
		Integer M_Product_ID = (Integer)value;
		if (M_Product_ID == null || M_Product_ID.intValue() == 0)
			return "";
		if (steps) log.warning("init");
		//
		mTab.setValue("C_Charge_ID", null);
		//	Set Attribute
		if (Env.getContextAsInt(ctx, WindowNo, Env.TAB_INFO, "M_Product_ID") == M_Product_ID.intValue()
			&& Env.getContextAsInt(ctx, WindowNo, Env.TAB_INFO, "M_AttributeSetInstance_ID") != 0)
			mTab.setValue("M_AttributeSetInstance_ID", Env.getContextAsInt(ctx, WindowNo, Env.TAB_INFO, "M_AttributeSetInstance_ID"));
		else
			mTab.setValue("M_AttributeSetInstance_ID", null);

		/*****	Price Calculation see also qty	****/
		int C_BPartner_ID = Env.getContextAsInt(ctx, WindowNo, "C_BPartner_ID");
		BigDecimal Qty = (BigDecimal)mTab.getValue("Qty");
		//boolean IsSOTrx = Env.getContext(ctx, WindowNo, "IsSOTrx").equals("Y");
		MProductPricing pp = new MProductPricing (M_Product_ID.intValue(), C_BPartner_ID, Qty, true);
		//
		int M_PriceList_ID = Env.getContextAsInt(ctx, WindowNo, "M_PriceList_ID");
		pp.setM_PriceList_ID(M_PriceList_ID);
		
		//Timestamp orderDate = (Timestamp)mTab.getValue("DateOrdered");
		
		int C_Inquiry_ID = Env.getContextAsInt(ctx, WindowNo, "C_Inquiry_ID");
		MInquiry inquiry = new MInquiry(ctx, C_Inquiry_ID, null);
		Timestamp orderDate = inquiry.getDateOrdered();
		/** PLV is only accurate if PL selected in header */
		int M_PriceList_Version_ID = Env.getContextAsInt(ctx, WindowNo, "M_PriceList_Version_ID");
		if ( M_PriceList_Version_ID == 0 && M_PriceList_ID > 0)
		{
			String sql = "SELECT plv.M_PriceList_Version_ID "
				+ "FROM M_PriceList_Version plv "
				+ "WHERE plv.M_PriceList_ID=? "						//	1
				+ " AND plv.ValidFrom <= ? "
				+ "ORDER BY plv.ValidFrom DESC";
			//	Use newest price list - may not be future

			M_PriceList_Version_ID = DB.getSQLValueEx(null, sql, M_PriceList_ID, orderDate);
			if ( M_PriceList_Version_ID > 0 )
				Env.setContext(ctx, WindowNo, "M_PriceList_Version_ID", M_PriceList_Version_ID );
		}
		pp.setM_PriceList_Version_ID(M_PriceList_Version_ID);
		pp.setPriceDate(orderDate);
		//
		mTab.setValue("PriceList", pp.getPriceList());
		mTab.setValue("PriceLimit", pp.getPriceLimit());
		mTab.setValue("PriceActual", pp.getPriceStd());
		mTab.setValue("PriceEntered", pp.getPriceStd());
		mTab.setValue("C_Currency_ID", new Integer(pp.getC_Currency_ID()));
		mTab.setValue("Discount", pp.getDiscount());
		mTab.setValue("C_UOM_ID", new Integer(pp.getC_UOM_ID()));
		mTab.setValue("QtyOrdered", mTab.getValue("QtyEntered"));
		Env.setContext(ctx, WindowNo, "EnforcePriceLimit", pp.isEnforcePriceLimit() ? "Y" : "N");
		Env.setContext(ctx, WindowNo, "DiscountSchema", pp.isDiscountSchema() ? "Y" : "N");
		
		MProduct product = new MProduct(ctx, M_Product_ID, null);
		mTab.setValue("M_Product_Category_ID", product.getM_Product_Category_ID());
		//mTab.setValue("Size", product.get_Value("Size"));
		
		if (steps) log.warning("fini");
		return "";
	}	//	product
	
	public String isNewItem(Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value, Object oldValue){
		I_C_InquiryLine inqLine = GridTabWrapper.create(mTab, I_C_InquiryLine.class);
		
		boolean newItem = (boolean)value;
		if(newItem){
			inqLine.setQty(Env.ZERO);
			inqLine.setPriceActual(null);
			inqLine.setPriceEntered(null);
			inqLine.setPriceList(null);
			inqLine.setIsIncludeRfQ(true);
			mTab.setValue("M_Product_ID", null);
			mTab.setValue("C_Charge_ID", null);

		} else {
			mTab.setValue("M_Product_ID", null);
			mTab.setValue("Product", null);
		}
		return null;
	}

	@Override
	public String start(Properties ctx, int WindowNo, GridTab mTab,
			GridField mField, Object value, Object oldValue) {
		if(mField.getColumnName().equals(MInquiryLine.COLUMNNAME_M_Product_ID)){
			return product(ctx, WindowNo, mTab, mField, value);
		}
		else if(mField.getColumnName().equals(MInquiryLine.COLUMNNAME_IsNewProduct)){
			return isNewItem(ctx, WindowNo, mTab, mField, value, oldValue);
		}
		return null;
	}

}
