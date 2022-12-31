package id.tcs.callout;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Properties;
import java.util.logging.Level;

import org.adempiere.base.IColumnCallout;
import org.compiere.model.CalloutEngine;
import org.compiere.model.GridField;
import org.compiere.model.GridTab;
import org.compiere.model.MDocType;
import org.compiere.model.MProduct;
import org.compiere.model.MUOMConversion;
import org.compiere.util.Env;


public class TCS_CalloutInventoryLine extends CalloutEngine implements IColumnCallout {

	private String DOCSUBTYPEINV_MiscReceipt = "MR";
	
	@Override
	public String start(Properties ctx, int WindowNo, GridTab mTab,
			GridField mField, Object value, Object oldValue) {
		if (mField.getColumnName().equals("QtyEntered")){
			return setQtyUOMConversion(ctx, WindowNo, mTab, mField, value) ;
		}
		if (mField.getColumnName().equals("C_UOM_ID")){
			return QtyUOM(ctx, WindowNo, mTab, mField, value) ;
		}
		if (mField.getColumnName().equals("M_Product_ID")){
			return setUOM(ctx, WindowNo, mTab, mField, value) ;
		}
		return "";
	}
	
	
	private String setUOM(Properties ctx, int windowNo, GridTab mTab, GridField mField, Object value) {
		String msg="";
		if (value==null){
			return msg;
		}
		
		int M_Product_ID = (int)value;
		MProduct prod = new MProduct(ctx, M_Product_ID, null);
		
		mTab.setValue("C_UOM_ID", prod.getC_UOM_ID());
		
		return "";
	}


	private String setQtyUOMConversion(Properties ctx, int windowNo, GridTab mTab,
			GridField mField, Object value) {
		
		String msg="";
		if (value==null){
			return msg;
		}
		
		BigDecimal newQty = Env.ZERO;
		BigDecimal QtyEntered = Env.ZERO;
		int M_Product_ID = Env.getContextAsInt(ctx, windowNo, mTab.getTabNo(), "M_Product_ID");
		
		int C_UOM_To_ID = Env.getContextAsInt(ctx, windowNo, mTab.getTabNo(), "C_UOM_ID");
		
		QtyEntered = (BigDecimal)value;
		
		int C_DocType_ID = Env.getContextAsInt(ctx, windowNo, "C_DocType_ID");
		boolean isInternalUse = false;
		boolean isMiscReceipt = false;
		MDocType docType = MDocType.get(ctx, C_DocType_ID);
		
		if (docType.getDocSubTypeInv().equals(MDocType.DOCSUBTYPEINV_InternalUseInventory)) {
			isInternalUse = true;
		}
		else if (docType.getDocSubTypeInv().equals(DOCSUBTYPEINV_MiscReceipt)) {
			isMiscReceipt = true;
		}
		
		MProduct product = MProduct.get(ctx, M_Product_ID);
		if (product==null){
			return msg;
		}
		
		
		int precision = product.getUOMPrecision();
		BigDecimal QtyEntered1 = QtyEntered.setScale(precision, RoundingMode.HALF_UP);
		if (QtyEntered.compareTo(QtyEntered1) != 0)
		{
			if (log.isLoggable(Level.FINE)) log.fine("Corrected MovementQty "
				+ QtyEntered + "->" + QtyEntered1);
			QtyEntered = QtyEntered1;
			mTab.setValue("QtyEntered", QtyEntered);
		}
		newQty = MUOMConversion.convertProductFrom (ctx, M_Product_ID,
			C_UOM_To_ID, QtyEntered);
		
		if (newQty == null)
			newQty = QtyEntered;
		boolean conversion = QtyEntered.compareTo(newQty) != 0;
		Env.setContext(ctx, windowNo, "UOMConversion", conversion ? "Y" : "N");
		
		if(isInternalUse) {
			mTab.setValue("QtyInternalUse", newQty);
			mTab.setValue("QtyMiscReceipt", newQty.negate());
		}
		else if(isMiscReceipt) {
			mTab.setValue("QtyMiscReceipt", newQty);
			mTab.setValue("QtyInternalUse", newQty.negate());
		}
		
		return "";
		
	}
	
	private String QtyUOM(Properties ctx, int windowNo, GridTab mTab,
			GridField mField, Object value) {
		
		String msg="";
		if (value==null){
			return msg;
		}
		
		BigDecimal newQty = Env.ZERO;
		BigDecimal QtyEntered = Env.ZERO;
		int M_Product_ID = Env.getContextAsInt(ctx, windowNo, mTab.getTabNo(), "M_Product_ID");
		
		if (M_Product_ID == 0)
			return msg;
		
		int C_UOM_To_ID = Env.getContextAsInt(ctx, windowNo, mTab.getTabNo(), "C_UOM_ID");
		
		if (C_UOM_To_ID == 0)
			return msg;
		
		QtyEntered = (BigDecimal) mTab.getValue("QtyEntered");
		
		int C_DocType_ID = Env.getContextAsInt(ctx, windowNo, "C_DocType_ID");
		boolean isInternalUse = false;
		boolean isMiscReceipt = false;
		MDocType docType = MDocType.get(ctx, C_DocType_ID);
		
		if (docType.getDocSubTypeInv().equals(MDocType.DOCSUBTYPEINV_InternalUseInventory)) {
			isInternalUse = true;
		}
		else if (docType.getDocSubTypeInv().equals("MR")) {
			isMiscReceipt = true;
		}
		
		MProduct product = MProduct.get(ctx, M_Product_ID);
		
		if (product == null)
			return msg;
		
		int precision = product.getUOMPrecision();
		BigDecimal QtyEntered1 = QtyEntered.setScale(precision, RoundingMode.HALF_UP);
		if (QtyEntered.compareTo(QtyEntered1) != 0)
		{
			if (log.isLoggable(Level.FINE)) log.fine("Corrected MovementQty "
				+ QtyEntered + "->" + QtyEntered1);
			QtyEntered = QtyEntered1;
			mTab.setValue("QtyEntered", QtyEntered);
		}
		newQty = MUOMConversion.convertProductFrom (ctx, M_Product_ID,
			C_UOM_To_ID, QtyEntered);
		if (newQty == null)
			newQty = QtyEntered;
		boolean conversion = QtyEntered.compareTo(newQty) != 0;

		Env.setContext(ctx, windowNo, "UOMConversion", conversion ? "Y" : "N");

		if(isInternalUse) {
			mTab.setValue("QtyInternalUse", newQty);
			mTab.setValue("QtyMiscReceipt", newQty.negate());
		}
		else if(isMiscReceipt) {
			mTab.setValue("QtyMiscReceipt", newQty);
			mTab.setValue("QtyInternalUse", newQty.negate());
		}
		
		return "";
		
	}
	
//	public String qtyMiscReceipt(Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value) {
//		if (isCalloutActive() || value == null)
//			return "";
//
//		int M_Product_ID = Env.getContextAsInt(ctx, WindowNo, mTab.getTabNo(), "M_Product_ID");
//		
//		//	@win add support multiUOM
//		BigDecimal MovementQty = Env.ZERO;
//		BigDecimal QtyMiscReceipt = Env.ZERO;
//		
//		int C_DocType_ID = Env.getContextAsInt(ctx, WindowNo, "C_DocType_ID");
//		boolean isMiscReceipt = false;
//		MDocType docType = MDocType.get(ctx, C_DocType_ID);
//		
//		if (docType.getDocSubTypeInv().equals(MDocType.DOCSUBTYPEINV_MiscReceipt)) {
//			isMiscReceipt = true;
//		}
//		
//		//	No Product
//		if (M_Product_ID == 0)
//		{
//			QtyMiscReceipt = (BigDecimal)mTab.getValue("QtyMiscReceipt");
//			mTab.setValue(MInventoryLine.COLUMNNAME_QtyInternalUse, QtyMiscReceipt.negate());
//		}
//		
//		//	UOM Changed - convert from Entered -> Product
//		else if (mField.getColumnName().equals("C_UOM_ID") && isMiscReceipt)
//		{
//			int C_UOM_To_ID = ((Integer)value).intValue();
//			QtyMiscReceipt = (BigDecimal)mTab.getValue("QtyMiscReceipt");
//			BigDecimal QtyMiscReceipt1 = QtyMiscReceipt.setScale(MUOM.getPrecision(ctx, C_UOM_To_ID), BigDecimal.ROUND_HALF_UP);
//			if (QtyMiscReceipt.compareTo(QtyMiscReceipt1) != 0)
//			{
//				if (log.isLoggable(Level.FINE)) log.fine("Corrected QtyEntered Scale UOM=" + C_UOM_To_ID
//					+ "; QtyEntered=" + QtyMiscReceipt + "->" + QtyMiscReceipt1);
//				QtyMiscReceipt = QtyMiscReceipt1;
//				mTab.setValue("QtyMiscReceipt", QtyMiscReceipt);
//			}
//			
//			MovementQty = MUOMConversion.convertProductFrom (ctx, M_Product_ID,
//				C_UOM_To_ID, QtyMiscReceipt);
//			if (MovementQty == null)
//				MovementQty = QtyMiscReceipt;
//			
//			boolean conversion = QtyMiscReceipt.compareTo(MovementQty) != 0;
//			if (log.isLoggable(Level.FINE)) log.fine("UOM=" + C_UOM_To_ID
//				+ ", QtyEntered=" + QtyMiscReceipt
//				+ " -> " + conversion
//				+ " QtyInternalUse=" + MovementQty);
//			Env.setContext(ctx, WindowNo, "UOMConversion", conversion ? "Y" : "N");
//			mTab.setValue(MInventoryLine.COLUMNNAME_QtyInternalUse, MovementQty.negate());
//		}
//		//	No UOM defined
//		else if (Env.getContextAsInt(ctx, WindowNo, mTab.getTabNo(), "C_UOM_ID") == 0)
//		{
//			QtyMiscReceipt = (BigDecimal)mTab.getValue("QtyMiscReceipt");
//			mTab.setValue(MInventoryLine.COLUMNNAME_QtyInternalUse, QtyMiscReceipt.negate());
//		}
//		//	QtyEntered changed - calculate MovementQty
//		else if (mField.getColumnName().equals("QtyMiscReceipt"))
//		{
//			int C_UOM_To_ID = Env.getContextAsInt(ctx, WindowNo, mTab.getTabNo(), "C_UOM_ID");
//			QtyMiscReceipt = (BigDecimal)value;
//			BigDecimal QtyMiscReceipt1 = QtyMiscReceipt.setScale(MUOM.getPrecision(ctx, C_UOM_To_ID), BigDecimal.ROUND_HALF_UP);
//			if (QtyMiscReceipt.compareTo(QtyMiscReceipt1) != 0)
//			{
//				if (log.isLoggable(Level.FINE)) log.fine("Corrected QtyEntered Scale UOM=" + C_UOM_To_ID
//					+ "; QtyEntered=" + QtyMiscReceipt + "->" + QtyMiscReceipt1);
//				QtyMiscReceipt = QtyMiscReceipt1;
//				mTab.setValue("QtyMiscReceipt", QtyMiscReceipt);
//			}
//			MovementQty = MUOMConversion.convertProductFrom (ctx, M_Product_ID,
//				C_UOM_To_ID, QtyMiscReceipt);
//			if (MovementQty == null)
//				MovementQty = QtyMiscReceipt;
//			boolean conversion = QtyMiscReceipt.compareTo(MovementQty) != 0;
//			if (log.isLoggable(Level.FINE)) log.fine("UOM=" + C_UOM_To_ID
//				+ ", QtyEntered=" + QtyMiscReceipt
//				+ " -> " + conversion
//				+ " QtyInternalUse=" + MovementQty);
//			Env.setContext(ctx, WindowNo, "UOMConversion", conversion ? "Y" : "N");
//			mTab.setValue(MInventoryLine.COLUMNNAME_QtyInternalUse, MovementQty.negate());
//		}
//		//	end	@win add support multiUOM
//	//	mTab.setValue(MInventoryLine.COLUMNNAME_QtyEntered, QtyMiscReceipt.negate());
//		return "";
//	}

}
