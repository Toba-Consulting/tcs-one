package id.tcs.callout;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Properties;
import java.util.logging.Level;

import org.adempiere.base.IColumnCallout;
import org.compiere.model.CalloutEngine;
import org.compiere.model.GridField;
import org.compiere.model.GridTab;
import org.compiere.model.MProduct;
import org.compiere.model.MUOMConversion;
import org.compiere.util.Env;
import org.eevolution.model.MDDOrderLine;

public class TCS_CalloutDDOrderLine extends CalloutEngine implements IColumnCallout {

	@Override
	public String start(Properties ctx, int WindowNo, GridTab mTab,
			GridField mField, Object value, Object oldValue) {
		if (mField.getColumnName().equals("M_Product_ID")){
			return product(ctx, WindowNo, mTab, mField, value) ;
		}
		if(mField.getColumnName().equals(MDDOrderLine.COLUMNNAME_QtyEntered)){
			return qtyOrdered(ctx, WindowNo, mTab, mField, value);
		}
		if(mField.getColumnName().equals(MDDOrderLine.COLUMNNAME_C_UOM_ID)){
			return qtyOrderedUOM(ctx, WindowNo, mTab, mField, value);
		}
		return "";
	}

	@SuppressWarnings("static-access")
	public String product(Properties ctx, int WindowNo, GridTab mTab,
			GridField mField, Object value) {
		
		String msg="";
		if (value==null){
			return msg;
		}
		
		Integer M_Product_ID = (Integer) value;
		if (M_Product_ID == null || M_Product_ID.intValue() <= 0)
			return "";
		
		MProduct product = MProduct.get(M_Product_ID);
		
		mTab.setValue("C_UOM_ID", product.getC_UOM_ID());
		
		return "";
		
		
	}
	
private String qtyOrdered(Properties ctx, int windowNo, GridTab mTab, GridField mField, Object value) {
		
		String msg="";
		if (value==null){
			return msg;
		}
		
		int M_Product_ID = Env.getContextAsInt(ctx, windowNo, mTab.getTabNo(), "M_Product_ID");
		int C_UOM_To_ID = Env.getContextAsInt(ctx, windowNo, mTab.getTabNo(), "C_UOM_ID");
		BigDecimal enteredQty = (BigDecimal) value;
		
		MProduct product = MProduct.get(ctx, M_Product_ID);
		if (product == null)
			return msg;
		
		int precision = product.getUOMPrecision();
		BigDecimal QtyEntered1 = enteredQty.setScale(precision, RoundingMode.HALF_UP);
		if (enteredQty.compareTo(QtyEntered1) != 0)
		{
			if (log.isLoggable(Level.FINE)) log.fine("Corrected MovementQty "
				+ enteredQty + "->" + QtyEntered1);
			enteredQty = QtyEntered1;
			mTab.setValue("QtyEntered", enteredQty);
		}
		
		
		BigDecimal orderedQty = MUOMConversion.convertProductFrom (ctx, M_Product_ID,
				C_UOM_To_ID, enteredQty);
		
		if (orderedQty == null)
			orderedQty = enteredQty;
		boolean conversion = enteredQty.compareTo(orderedQty) != 0;
		if (log.isLoggable(Level.FINE)) log.fine("UOM=" + C_UOM_To_ID
			+ ", QtyEntered=" + enteredQty
			+ " -> " + conversion
			+ " QtyOrdered=" + orderedQty);
		
		Env.setContext(ctx, windowNo, "UOMConversion", conversion ? "Y" : "N");
		mTab.setValue("QtyOrdered", orderedQty);

		return "";
	}

	private String qtyOrderedUOM(Properties ctx, int windowNo, GridTab mTab, GridField mField, Object value) {
		
		String msg="";
		if (value==null){
			return msg;
		}
		
		int M_Product_ID = Env.getContextAsInt(ctx, windowNo, mTab.getTabNo(), "M_Product_ID");
		int C_UOM_To_ID = Env.getContextAsInt(ctx, windowNo, mTab.getTabNo(), "C_UOM_ID");
		BigDecimal enteredQty = (BigDecimal) mTab.getValue("QtyEntered");
		
		int precision = MProduct.get(ctx, M_Product_ID).getUOMPrecision();
		BigDecimal QtyEntered1 = enteredQty.setScale(precision, BigDecimal.ROUND_HALF_UP);
		if (enteredQty.compareTo(QtyEntered1) != 0)
		{
			if (log.isLoggable(Level.FINE)) log.fine("Corrected MovementQty "
				+ enteredQty + "->" + QtyEntered1);
			enteredQty = QtyEntered1;
			mTab.setValue("QtyEntered", enteredQty);
		}
		
		
		BigDecimal orderedQty = MUOMConversion.convertProductFrom (ctx, M_Product_ID,
				C_UOM_To_ID, enteredQty);
		
		if (orderedQty == null)
			orderedQty = enteredQty;
		boolean conversion = enteredQty.compareTo(orderedQty) != 0;
		if (log.isLoggable(Level.FINE)) log.fine("UOM=" + C_UOM_To_ID
			+ ", QtyEntered=" + enteredQty
			+ " -> " + conversion
			+ " QtyOrdered=" + orderedQty);
		
		Env.setContext(ctx, windowNo, "UOMConversion", conversion ? "Y" : "N");
		mTab.setValue("QtyOrdered", orderedQty);

		return "";
	}
	

}
