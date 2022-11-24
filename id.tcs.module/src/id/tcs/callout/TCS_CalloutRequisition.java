package id.tcs.callout;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Properties;
import java.util.logging.Level;

import org.adempiere.base.IColumnCallout;
import org.compiere.model.CalloutEngine;
import org.compiere.model.GridField;
import org.compiere.model.GridTab;
import org.compiere.model.I_M_RequisitionLine;
import org.compiere.model.MProduct;
import org.compiere.model.MUOM;
import org.compiere.model.MUOMConversion;
import org.compiere.util.Env;

public class TCS_CalloutRequisition extends CalloutEngine implements IColumnCallout{

	@Override
	public String start(Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value, Object oldValue) {
		if (mField.getColumnName().equals(I_M_RequisitionLine.COLUMNNAME_C_UOM_ID) ||
			mField.getColumnName().equals(I_M_RequisitionLine.COLUMNNAME_Qty) ||
			mField.getColumnName().equals("QtyRequired")
					
				) {
			return qty(ctx, WindowNo, mTab, mField, value, oldValue);
		}
		return null;
	}

	
	/**
	 *	Requisition Line - Quantity.
	 *		- called from C_UOM_ID, Qty, QtyRequired
	 *		- enforces qty UOM relationship
	 *  @param ctx context
	 *  @param WindowNo current Window No
	 *  @param mTab Grid Tab
	 *  @param mField Grid Field
	 *  @param value New Value
	 *  @return null or error message
	 */
	public String qty (Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value, Object oldValue)
	{
		if (isCalloutActive() || value == null)
			return "";
		int M_Product_ID = Env.getContextAsInt(ctx, WindowNo, mTab.getTabNo(), "M_Product_ID");
		BigDecimal Qty = Env.ZERO;
		BigDecimal QtyEntered;

		//	No Product
		if (M_Product_ID == 0) {
			QtyEntered = mTab.getValue("QtyEntered") != null ? (BigDecimal)mTab.getValue("QtyEntered") : BigDecimal.ZERO;
			Qty = QtyEntered;
			mTab.setValue("Qty", Qty);
		}
		//	UOM Changed - convert from Entered -> Product
		else if (mField.getColumnName().equals("C_UOM_ID"))
		{
			int C_UOM_To_ID = ((Integer)value).intValue();
			if (MUOM.getPrecision(ctx, C_UOM_To_ID) == 0)
					return "";
			
			QtyEntered = mTab.getValue("QtyEntered") != null ? (BigDecimal)mTab.getValue("QtyEntered") : BigDecimal.ZERO;
			BigDecimal QtyEntered1 = QtyEntered.setScale(MUOM.getPrecision(ctx, C_UOM_To_ID), RoundingMode.HALF_UP);
			if (QtyEntered.compareTo(QtyEntered1) != 0)
			{
				if (log.isLoggable(Level.FINE)) log.fine("Corrected Qty Scale UOM=" + C_UOM_To_ID
					+ "; QtyEntered=" + QtyEntered + "->" + QtyEntered1);
				QtyEntered = QtyEntered1;
				mTab.setValue("QtyEntered", QtyEntered);
			}
			Qty = MUOMConversion.convertProductFrom (ctx, M_Product_ID,
				C_UOM_To_ID, QtyEntered);
			if (Qty == null)
				Qty = QtyEntered;
			boolean conversion = QtyEntered.compareTo(Qty) != 0;
			Env.setContext(ctx, WindowNo, "UOMConversion", conversion ? "Y" : "N");
			mTab.setValue("Qty", Qty);
		}
		//	QtyEntered changed - calculate Qty
		else if (mField.getColumnName().equals("QtyEntered"))
		{
			int C_UOM_To_ID = Env.getContextAsInt(ctx, WindowNo, mTab.getTabNo(), "C_UOM_ID");
			if (MUOM.getPrecision(ctx, C_UOM_To_ID) == 0)
				return "";
			
			QtyEntered = (BigDecimal)value;
			BigDecimal QtyEntered1 = QtyEntered.setScale(MUOM.getPrecision(ctx, C_UOM_To_ID), RoundingMode.HALF_UP);
			if (QtyEntered.compareTo(QtyEntered1) != 0)
			{
				if (log.isLoggable(Level.FINE)) log.fine("Corrected Qty Scale UOM=" + C_UOM_To_ID
					+ "; QtyEntered=" + QtyEntered + "->" + QtyEntered1);
				QtyEntered = QtyEntered1;
				mTab.setValue("QtyEntered", QtyEntered);
			}
			Qty = MUOMConversion.convertProductFrom (ctx, M_Product_ID,
				C_UOM_To_ID, QtyEntered);
			if (Qty == null)
				Qty = QtyEntered;
			boolean conversion = QtyEntered.compareTo(Qty) != 0;
			if (log.isLoggable(Level.FINE)) log.fine("UOM=" + C_UOM_To_ID
				+ ", QtyEntered=" + QtyEntered
				+ " -> " + conversion
				+ " Qty=" + Qty);
			Env.setContext(ctx, WindowNo, "UOMConversion", conversion ? "Y" : "N");
			mTab.setValue("Qty", Qty);
		}
		//	Qty changed - calculate QtyEntered (should not happen)
		else if (mField.getColumnName().equals("Qty"))
		{
			int C_UOM_To_ID = Env.getContextAsInt(ctx, WindowNo, mTab.getTabNo(), "C_UOM_ID");
			if (MUOM.getPrecision(ctx, C_UOM_To_ID) == 0)
				return "";
			
			Qty = (BigDecimal)value;
			int precision = MProduct.get(ctx, M_Product_ID).getUOMPrecision();
			BigDecimal Qty1 = Qty.setScale(precision, RoundingMode.HALF_UP);
			if (Qty.compareTo(Qty1) != 0)
			{
				if (log.isLoggable(Level.FINE)) log.fine("Corrected QtyRequired Scale "
					+ Qty + "->" + Qty1);
				Qty = Qty1;
				mTab.setValue("Qty", Qty);
			}
			QtyEntered = MUOMConversion.convertProductTo (ctx, M_Product_ID,
				C_UOM_To_ID, Qty);
			if (QtyEntered == null)
				QtyEntered = Qty;
			boolean conversion = Qty.compareTo(QtyEntered) != 0;
			if (log.isLoggable(Level.FINE)) log.fine("UOM=" + C_UOM_To_ID
				+ ", Qty=" + Qty
				+ " -> " + conversion
				+ " QtyEntered=" + QtyEntered);
			Env.setContext(ctx, WindowNo, "UOMConversion", conversion ? "Y" : "N");
			mTab.setValue("QtyEntered", QtyEntered);
		}

		//
		return "";
	}	//	qty
	
	

	
}
