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
		BigDecimal QtyRequired = Env.ZERO;
		BigDecimal Qty;

		//	No Product
		if (M_Product_ID == 0)
		{
			Qty = mTab.getValue("Qty") != null ? (BigDecimal)mTab.getValue("Qty") : BigDecimal.ZERO;
			QtyRequired = Qty;
			mTab.setValue("QtyRequired", QtyRequired);
		}
		//	UOM Changed - convert from Entered -> Product
		else if (mField.getColumnName().equals("C_UOM_ID"))
		{
			int C_UOM_To_ID = ((Integer)value).intValue();
			if (MUOM.getPrecision(ctx, C_UOM_To_ID) == 0)
					return "";
			
			Qty = mTab.getValue("Qty") != null ? (BigDecimal)mTab.getValue("Qty") : BigDecimal.ZERO;
			BigDecimal Qty1 = Qty.setScale(MUOM.getPrecision(ctx, C_UOM_To_ID), RoundingMode.HALF_UP);
			if (Qty.compareTo(Qty1) != 0)
			{
				if (log.isLoggable(Level.FINE)) log.fine("Corrected Qty Scale UOM=" + C_UOM_To_ID
					+ "; Qty=" + Qty + "->" + Qty1);
				Qty = Qty1;
				mTab.setValue("Qty", Qty);
			}
			QtyRequired = MUOMConversion.convertProductFrom (ctx, M_Product_ID,
				C_UOM_To_ID, Qty);
			if (QtyRequired == null)
				QtyRequired = Qty;
			boolean conversion = Qty.compareTo(QtyRequired) != 0;
			Env.setContext(ctx, WindowNo, "UOMConversion", conversion ? "Y" : "N");
			mTab.setValue("QtyRequired", QtyRequired);
		}
		//	Qty changed - calculate QtyRequired
		else if (mField.getColumnName().equals("Qty"))
		{
			int C_UOM_To_ID = Env.getContextAsInt(ctx, WindowNo, mTab.getTabNo(), "C_UOM_ID");
			if (MUOM.getPrecision(ctx, C_UOM_To_ID) == 0)
				return "";
			
			Qty = (BigDecimal)value;
			BigDecimal Qty1 = Qty.setScale(MUOM.getPrecision(ctx, C_UOM_To_ID), RoundingMode.HALF_UP);
			if (Qty.compareTo(Qty1) != 0)
			{
				if (log.isLoggable(Level.FINE)) log.fine("Corrected Qty Scale UOM=" + C_UOM_To_ID
					+ "; Qty=" + Qty + "->" + Qty1);
				Qty = Qty1;
				mTab.setValue("Qty", Qty);
			}
			QtyRequired = MUOMConversion.convertProductFrom (ctx, M_Product_ID,
				C_UOM_To_ID, Qty);
			if (QtyRequired == null)
				QtyRequired = Qty;
			boolean conversion = Qty.compareTo(QtyRequired) != 0;
			if (log.isLoggable(Level.FINE)) log.fine("UOM=" + C_UOM_To_ID
				+ ", Qty=" + Qty
				+ " -> " + conversion
				+ " QtyRequired=" + QtyRequired);
			Env.setContext(ctx, WindowNo, "UOMConversion", conversion ? "Y" : "N");
			mTab.setValue("QtyRequired", QtyRequired);
		}
		//	QtyOrdered changed - calculate QtyEntered (should not happen)
		else if (mField.getColumnName().equals("QtyRequired"))
		{
			int C_UOM_To_ID = Env.getContextAsInt(ctx, WindowNo, mTab.getTabNo(), "C_UOM_ID");
			if (MUOM.getPrecision(ctx, C_UOM_To_ID) == 0)
				return "";
			
			QtyRequired = (BigDecimal)value;
			int precision = MProduct.get(ctx, M_Product_ID).getUOMPrecision();
			BigDecimal QtyRequired1 = QtyRequired.setScale(precision, RoundingMode.HALF_UP);
			if (QtyRequired.compareTo(QtyRequired1) != 0)
			{
				if (log.isLoggable(Level.FINE)) log.fine("Corrected QtyRequired Scale "
					+ QtyRequired + "->" + QtyRequired1);
				QtyRequired = QtyRequired1;
				mTab.setValue("QtyRequired", QtyRequired);
			}
			Qty = MUOMConversion.convertProductTo (ctx, M_Product_ID,
				C_UOM_To_ID, QtyRequired);
			if (Qty == null)
				Qty = QtyRequired;
			boolean conversion = QtyRequired.compareTo(Qty) != 0;
			if (log.isLoggable(Level.FINE)) log.fine("UOM=" + C_UOM_To_ID
				+ ", QtyRequired=" + QtyRequired
				+ " -> " + conversion
				+ " Qty=" + Qty);
			Env.setContext(ctx, WindowNo, "UOMConversion", conversion ? "Y" : "N");
			mTab.setValue("Qty", Qty);
		}
		else
		{
			QtyRequired = (BigDecimal)mTab.getValue("Qty");
		}
		//
		return "";
	}	//	qty
	
	

	
}
