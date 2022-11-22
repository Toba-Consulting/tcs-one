package id.tcs.callout;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Properties;
import java.util.logging.Level;

import org.adempiere.base.IColumnCallout;
import org.compiere.model.CalloutEngine;
import org.compiere.model.GridField;
import org.compiere.model.GridTab;
import org.compiere.model.I_C_Invoice;
import org.compiere.model.MBPartnerLocation;
import org.compiere.model.MOrder;
import org.compiere.model.MOrgInfo;
import org.compiere.model.MPaymentTerm;
import org.compiere.model.MPriceList;
import org.compiere.model.MProduct;
import org.compiere.model.MProductPricing;
import org.compiere.model.MRole;
import org.compiere.model.MSequence;
import org.compiere.model.MUOM;
import org.compiere.model.MUOMConversion;
import org.compiere.model.MWarehouse;
import org.compiere.model.Tax;
import org.compiere.model.X_C_Order;
import org.compiere.model.X_C_POSTenderType;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.DisplayType;
import org.compiere.util.Env;

import id.tcs.model.I_C_Quotation;
import id.tcs.model.MQuotationLine;
import id.tcs.model.X_C_Quotation;

public class TCS_CalloutQuotationLine extends CalloutEngine implements IColumnCallout{

	boolean steps = false;
	boolean isNewProductDes = false;

	public String total(Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value){

		BigDecimal baseprice = mTab.getValue("BasePrice") != null ? (BigDecimal)mTab.getValue("BasePrice") : BigDecimal.ZERO;
		BigDecimal discount = mTab.getValue("Discount") != null ? (BigDecimal)mTab.getValue("Discount") : BigDecimal.ZERO;
		BigDecimal qtyEntered = mTab.getValue("QtyEntered") != null ? (BigDecimal)mTab.getValue("QtyEntered") : BigDecimal.ZERO;
		
		BigDecimal beforeDisc = baseprice.multiply(discount).divide(Env.ONEHUNDRED);
		BigDecimal afterDisc = baseprice.subtract(beforeDisc).setScale(0, RoundingMode.UP);

		/* @win HWH custom code
		BigDecimal faktorkondisi = (BigDecimal)mTab.getValue("FaktorKondisi");
		BigDecimal beforeDisc = baseprice.multiply(faktorkondisi).multiply(discount).divide(Env.ONEHUNDRED);
		BigDecimal afterDisc = baseprice.multiply(faktorkondisi).subtract(beforeDisc).setScale(0, RoundingMode.UP);
		 */

		int price = 0;

		if(afterDisc.intValueExact()>100){
			String stringPrice = Integer.toString(afterDisc.intValueExact());
			int len = stringPrice.length();
			StringBuilder sbPrice = new StringBuilder();
			sbPrice.append(stringPrice.charAt(len-2));
			sbPrice.append(stringPrice.charAt(len-1));
			stringPrice = sbPrice.toString();
			price = 100-Integer.parseInt(stringPrice);
			if(price==100){
				mTab.setValue("PriceEntered", afterDisc);
				mTab.setValue("PriceActual", afterDisc);
				//mTab.setValue("PriceKondisi", afterDisc);
			}else{
				mTab.setValue("PriceEntered", afterDisc.add(new BigDecimal(price)));
				mTab.setValue("PriceActual", afterDisc.add(new BigDecimal(price)));
			}
		}else{
			mTab.setValue("PriceEntered", afterDisc);
			mTab.setValue("PriceActual", afterDisc);
			//mTab.setValue("PriceKondisi", afterDisc);
		}
		BigDecimal priceEntered = (BigDecimal)mTab.getValue("PriceEntered");
		mTab.setValue("LineNetAmt", priceEntered.multiply(qtyEntered));
		return "";
	}

	public String docType (Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{
		Integer C_DocType_ID = (Integer) value;		//	Actually C_DocTypeTarget_ID
		if (C_DocType_ID == null || C_DocType_ID.intValue() == 0)
			return "";

		//	Re-Create new DocNo, if there is a doc number already
		//	and the existing source used a different Sequence number
		String oldDocNo = (String)mTab.getValue("DocumentNo");
		boolean newDocNo = (oldDocNo == null);

		if (!newDocNo && oldDocNo.startsWith("<") && oldDocNo.endsWith(">"))
			newDocNo = true;

		Integer oldC_DocType_ID = (Integer)mTab.getValue("C_DocType_ID");

		String sql = "SELECT d.DocSubTypeSO,d.HasCharges,"			//	1..2
				+ "d.IsDocNoControlled,"     //  3
				+ "s.AD_Sequence_ID,d.IsSOTrx "                             //	4..5
				+ "FROM C_DocType d "
				+ "LEFT OUTER JOIN AD_Sequence s ON (d.DocNoSequence_ID=s.AD_Sequence_ID) "
				+ "WHERE C_DocType_ID=?";	//	#1

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			int oldAD_Sequence_ID = 0;

			//	Get old AD_SeqNo for comparison
			if (!newDocNo && oldC_DocType_ID.intValue() != 0)
			{
				pstmt = DB.prepareStatement(sql, null);
				pstmt.setInt(1, oldC_DocType_ID.intValue());
				rs = pstmt.executeQuery();
				if (rs.next())
					oldAD_Sequence_ID = rs.getInt("AD_Sequence_ID");
				DB.close(rs, pstmt);
				rs = null;
				pstmt = null;
			}

			pstmt = DB.prepareStatement(sql, null);
			pstmt.setInt(1, C_DocType_ID.intValue());
			rs = pstmt.executeQuery();
			String DocSubTypeSO = "";
			boolean IsSOTrx = true;
			if (rs.next())		//	we found document type
			{
				//	Set Context:	Document Sub Type for Sales Orders
				DocSubTypeSO = rs.getString("DocSubTypeSO");
				if (DocSubTypeSO == null)
					DocSubTypeSO = "--";
				Env.setContext(ctx, WindowNo, "OrderType", DocSubTypeSO);


				//	IsSOTrx
				if ("N".equals(rs.getString("IsSOTrx")))
					IsSOTrx = false;

				//	Set Context:
				Env.setContext(ctx, WindowNo, "HasCharges", rs.getString("HasCharges"));

				//	DocumentNo
				if (rs.getString("IsDocNoControlled").equals("Y"))			//	IsDocNoControlled
				{
					if (!newDocNo && oldAD_Sequence_ID != rs.getInt("AD_Sequence_ID"))
						newDocNo = true;
					if (newDocNo) {
						int AD_Sequence_ID = rs.getInt("AD_Sequence_ID");
						mTab.setValue("DocumentNo", MSequence.getPreliminaryNo(mTab, AD_Sequence_ID));
					}
				}
			}


			DB.close(rs, pstmt);
			rs = null;
			pstmt = null;

			//  When BPartner is changed, the Rules are not set if
			//  it is a POS or Credit Order (i.e. defaults from Standard BPartner)
			//  This re-reads the Rules and applies them.
			if (DocSubTypeSO.equals(MOrder.DocSubTypeSO_POS) 
					|| DocSubTypeSO.equals(MOrder.DocSubTypeSO_Prepay))    //  not for POS/PrePay
				;
			else
			{
				sql = "SELECT PaymentRule,C_PaymentTerm_ID,"            //  1..2
						+ "InvoiceRule,DeliveryRule,"                       //  3..4
						+ "FreightCostRule,DeliveryViaRule, "               //  5..6
						+ "PaymentRulePO,PO_PaymentTerm_ID "
						+ "FROM C_BPartner "
						+ "WHERE C_BPartner_ID=?";		//	#1
				pstmt = DB.prepareStatement(sql, null);
				int C_BPartner_ID = Env.getContextAsInt(ctx, WindowNo, "C_BPartner_ID");
				pstmt.setInt(1, C_BPartner_ID);
				//
				rs = pstmt.executeQuery();
				if (rs.next())
				{
					//	PaymentRule
					String s = rs.getString(IsSOTrx ? "PaymentRule" : "PaymentRulePO");
					if (s != null && s.length() != 0)
					{
						if (IsSOTrx && (s.equals("B") || s.equals("S") || s.equals("U")))	//	No Cash/Check/Transfer for SO_Trx
							s = "P";										//  Payment Term
						if (!IsSOTrx && (s.equals("B")))					//	No Cash for PO_Trx
							s = "P";										//  Payment Term
						mTab.setValue("PaymentRule", s);
					}
					//	Payment Term
					Integer ii = Integer.valueOf(rs.getInt(IsSOTrx ? "C_PaymentTerm_ID" : "PO_PaymentTerm_ID"));
					if (!rs.wasNull())
						mTab.setValue("C_PaymentTerm_ID", ii);
					//	InvoiceRule
					s = rs.getString(3);
					if (s != null && s.length() != 0)
						mTab.setValue("InvoiceRule", s);
					//	DeliveryRule
					s = rs.getString(4);
					if (s != null && s.length() != 0)
						mTab.setValue("DeliveryRule", s);
					//	FreightCostRule
					s = rs.getString(5);
					if (s != null && s.length() != 0)
						mTab.setValue("FreightCostRule", s);
					//	DeliveryViaRule
					s = rs.getString(6);
					if (s != null && s.length() != 0)
						mTab.setValue("DeliveryViaRule", s);
				}
			} 
			//  re-read customer rules
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, sql, e);
			return e.getLocalizedMessage();
		}
		finally
		{
			DB.close(rs, pstmt);
			rs = null; pstmt = null;
		}
		return "";
	}

	public String bPartner (Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{
		Integer C_BPartner_ID = (Integer)value;
		if (C_BPartner_ID == null || C_BPartner_ID.intValue() == 0)
			return "";
		
		String sql = "SELECT p.AD_Language,p.C_PaymentTerm_ID,"
				+ " COALESCE(p.M_PriceList_ID,g.M_PriceList_ID) AS M_PriceList_ID, p.PaymentRule,p.POReference,"
				+ " p.SO_Description,p.IsDiscountPrinted,"
				+ " p.InvoiceRule,p.DeliveryRule,p.FreightCostRule,DeliveryViaRule,"
				+ " p.SO_CreditLimit, p.SO_CreditLimit-p.SO_CreditUsed AS CreditAvailable,"
				+ " lship.C_BPartner_Location_ID,c.AD_User_ID,"
				+ " COALESCE(p.PO_PriceList_ID,g.PO_PriceList_ID) AS PO_PriceList_ID, p.PaymentRulePO,p.PO_PaymentTerm_ID,"
				+ " lbill.C_BPartner_Location_ID AS Bill_Location_ID, p.SOCreditStatus, "
				+ " p.SalesRep_ID "
				+ "FROM C_BPartner p"
				+ " INNER JOIN C_BP_Group g ON (p.C_BP_Group_ID=g.C_BP_Group_ID)"
				+ " LEFT OUTER JOIN C_BPartner_Location lbill ON (p.C_BPartner_ID=lbill.C_BPartner_ID AND lbill.IsBillTo='Y' AND lbill.IsActive='Y')"
				+ " LEFT OUTER JOIN C_BPartner_Location lship ON (p.C_BPartner_ID=lship.C_BPartner_ID AND lship.IsShipTo='Y' AND lship.IsActive='Y')"
				+ " LEFT OUTER JOIN AD_User c ON (p.C_BPartner_ID=c.C_BPartner_ID AND c.IsActive='Y') "
				+ "WHERE p.C_BPartner_ID=? AND p.IsActive='Y'";		//	#1

		boolean IsSOTrx = "Y".equals(Env.getContext(ctx, WindowNo, "IsSOTrx"));
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, null);
			pstmt.setInt(1, C_BPartner_ID.intValue());
			rs = pstmt.executeQuery();
			if (rs.next())
			{
				// Sales Rep - If BP has a default SalesRep then default it
				Integer salesRep = rs.getInt("SalesRep_ID");
				if (IsSOTrx && salesRep != 0 )
				{
					mTab.setValue("SalesRep_ID", salesRep);
				}

				//	PriceList (indirect: IsTaxIncluded & Currency)
				Integer priceListID = Integer.valueOf(rs.getInt(IsSOTrx ? "M_PriceList_ID" : "PO_PriceList_ID"));
				
				if (!rs.wasNull())
					mTab.setValue("M_PriceList_ID", priceListID);
				else
				{	//	get default PriceList
					int i = Env.getContextAsInt(ctx, "#M_PriceList_ID");
					if (i != 0)
					{
						MPriceList pl = new MPriceList(ctx, i, null);
						if (IsSOTrx == pl.isSOPriceList())
							mTab.setValue("M_PriceList_ID", Integer.valueOf(i));
						else
						{
							String sql2 = "SELECT M_PriceList_ID FROM M_PriceList WHERE AD_Client_ID=? AND IsSOPriceList=? AND IsActive='Y' ORDER BY IsDefault DESC";
							priceListID = DB.getSQLValue (null, sql2, Env.getAD_Client_ID(ctx), IsSOTrx);
							if (priceListID != 0)
								mTab.setValue("M_PriceList_ID", priceListID);
						}
					}
				}

				//	Bill-To
				mTab.setValue("Bill_BPartner_ID", C_BPartner_ID);

				int shipTo_ID = 0;
				int bill_Location_ID =0;
				//	overwritten by InfoBP selection - works only if InfoWindow
				//	was used otherwise creates error (uses last value, may belong to different BP)
				if (C_BPartner_ID.toString().equals(Env.getContext(ctx, WindowNo, Env.TAB_INFO, "C_BPartner_ID")))
				{
					String loc = Env.getContext(ctx, WindowNo, Env.TAB_INFO, "C_BPartner_Location_ID");
					int locationId = 0;
					if (loc.length() > 0)
						locationId = Integer.parseInt(loc);
					if (locationId > 0) {
						MBPartnerLocation bpLocation = new MBPartnerLocation(ctx, locationId, null);
						if (bpLocation.isBillTo())
							bill_Location_ID = locationId;
						if (bpLocation.isShipTo())
							shipTo_ID = locationId;
					}
				}
				if (bill_Location_ID == 0)
					bill_Location_ID = rs.getInt("Bill_Location_ID");
				if (bill_Location_ID == 0)
					mTab.setValue("Bill_Location_ID", null);
				else
					mTab.setValue("Bill_Location_ID", Integer.valueOf(bill_Location_ID));
				// Ship-To Location
				if (shipTo_ID == 0)
					shipTo_ID = rs.getInt("C_BPartner_Location_ID");

				if (shipTo_ID == 0)
					mTab.setValue("C_BPartner_Location_ID", null);
				else
					mTab.setValue("C_BPartner_Location_ID", Integer.valueOf(shipTo_ID));

				//	Contact - overwritten by InfoBP selection
				int contactID = rs.getInt("AD_User_ID");
				if (C_BPartner_ID.toString().equals(Env.getContext(ctx, WindowNo, Env.TAB_INFO, "C_BPartner_ID")))
				{
					String cont = Env.getContext(ctx, WindowNo, Env.TAB_INFO, "AD_User_ID");
					if (cont.length() > 0)
						contactID = Integer.parseInt(cont);
				}
				if (contactID == 0)
					mTab.setValue("AD_User_ID", null);
				else
				{
					mTab.setValue("AD_User_ID", Integer.valueOf(contactID));
					mTab.setValue("Bill_User_ID", Integer.valueOf(contactID));
				}

				//	CreditAvailable
				if (IsSOTrx)
				{
					double CreditLimit = rs.getDouble("SO_CreditLimit");
					if (CreditLimit != 0)
					{
						double CreditAvailable = rs.getDouble("CreditAvailable");
						if (!rs.wasNull() && CreditAvailable < 0)
							mTab.fireDataStatusEEvent("CreditLimitOver",
									DisplayType.getNumberFormat(DisplayType.Amount).format(CreditAvailable),
									false);
					}
				}

				//	PO Reference
				String s = rs.getString("POReference");
				if (s != null && s.length() != 0)
					mTab.setValue("POReference", s);
				// should not be reset to null if we entered already value! VHARCQ, accepted YS makes sense that way
				// TODO: should get checked and removed if no longer needed!
				/*else
					mTab.setValue("POReference", null);*/

				//	SO Description
				s = rs.getString("SO_Description");
				if (s != null && s.trim().length() != 0)
					mTab.setValue("Description", s);
				//	IsDiscountPrinted
				s = rs.getString("IsDiscountPrinted");
				if (s != null && s.length() != 0)
					mTab.setValue("IsDiscountPrinted", s);
				else
					mTab.setValue("IsDiscountPrinted", "N");

				//	Defaults, if not Walkin Receipt or Walkin Invoice
				String OrderType = Env.getContext(ctx, WindowNo, "OrderType");
				mTab.setValue("InvoiceRule", X_C_Quotation.INVOICERULE_AfterDelivery);
				mTab.setValue("DeliveryRule", X_C_Quotation.DELIVERYRULE_Availability);
				mTab.setValue("PaymentRule", X_C_Quotation.PAYMENTRULE_OnCredit);
				if (OrderType.equals(MOrder.DocSubTypeSO_Prepay))
				{
					mTab.setValue("InvoiceRule", X_C_Quotation.INVOICERULE_Immediate);
					mTab.setValue("DeliveryRule", X_C_Quotation.DELIVERYRULE_AfterReceipt);
				}
				else if (OrderType.equals(MOrder.DocSubTypeSO_POS))	//  for POS
					mTab.setValue("PaymentRule", X_C_Quotation.PAYMENTRULE_Cash);
				else
				{
					//	PaymentRule
					s = rs.getString(IsSOTrx ? "PaymentRule" : "PaymentRulePO");
					if (s != null && s.length() != 0)
					{
						if (s.equals("B"))				//	No Cache in Non POS
							s = "P";
						if (IsSOTrx && (s.equals("S") || s.equals("U")))	//	No Check/Transfer for SO_Trx
							s = "P";										//  Payment Term
						mTab.setValue("PaymentRule", s);
					}
					//	Payment Term
					priceListID = Integer.valueOf(rs.getInt(IsSOTrx ? "C_PaymentTerm_ID" : "PO_PaymentTerm_ID"));
					if (!rs.wasNull())
						mTab.setValue("C_PaymentTerm_ID", priceListID);
					//	InvoiceRule
					s = rs.getString("InvoiceRule");
					if (s != null && s.length() != 0)
						mTab.setValue("InvoiceRule", s);
					//	DeliveryRule
					s = rs.getString("DeliveryRule");
					if (s != null && s.length() != 0)
						mTab.setValue("DeliveryRule", s);
					//	FreightCostRule
					s = rs.getString("FreightCostRule");
					if (s != null && s.length() != 0)
						mTab.setValue("FreightCostRule", s);
					//	DeliveryViaRule
					s = rs.getString("DeliveryViaRule");
					if (s != null && s.length() != 0)
						mTab.setValue("DeliveryViaRule", s);
				}
			}
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, sql, e);
			return e.getLocalizedMessage();
		}
		finally
		{
			DB.close(rs, pstmt);
			rs = null; pstmt = null;
		}
		return "";
	}	//	bPartner

	public String bPartnerBill (Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{
		if (isCalloutActive())
			return "";
		Integer bill_BPartner_ID = (Integer)value;
		if (bill_BPartner_ID == null || bill_BPartner_ID.intValue() == 0)
			return "";

		String sql = "SELECT p.AD_Language,p.C_PaymentTerm_ID,"
				+ "p.M_PriceList_ID,p.PaymentRule,p.POReference,"
				+ "p.SO_Description,p.IsDiscountPrinted,"
				+ "p.InvoiceRule,p.DeliveryRule,p.FreightCostRule,DeliveryViaRule,"
				+ "p.SO_CreditLimit, p.SO_CreditLimit-p.SO_CreditUsed AS CreditAvailable,"
				+ "c.AD_User_ID,"
				+ "p.PO_PriceList_ID, p.PaymentRulePO, p.PO_PaymentTerm_ID,"
				+ "lbill.C_BPartner_Location_ID AS Bill_Location_ID "
				+ "FROM C_BPartner p"
				+ " LEFT OUTER JOIN C_BPartner_Location lbill ON (p.C_BPartner_ID=lbill.C_BPartner_ID AND lbill.IsBillTo='Y' AND lbill.IsActive='Y')"
				+ " LEFT OUTER JOIN AD_User c ON (p.C_BPartner_ID=c.C_BPartner_ID AND c.IsActive='Y') "
				+ "WHERE p.C_BPartner_ID=? AND p.IsActive='Y'";		//	#1

		boolean IsSOTrx = "Y".equals(Env.getContext(ctx, WindowNo, "IsSOTrx"));
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, null);
			pstmt.setInt(1, bill_BPartner_ID.intValue());
			rs = pstmt.executeQuery();
			if (rs.next())
			{
				//	PriceList (indirect: IsTaxIncluded & Currency)
				Integer ii = Integer.valueOf(rs.getInt(IsSOTrx ? "M_PriceList_ID" : "PO_PriceList_ID"));
				if (!rs.wasNull())
					mTab.setValue("M_PriceList_ID", ii);
				else
				{	//	get default PriceList
					int i = Env.getContextAsInt(ctx, "#M_PriceList_ID");
					if (i != 0)
					{
						MPriceList pl = new MPriceList(ctx, i, null);
						if (IsSOTrx == pl.isSOPriceList())
							mTab.setValue("M_PriceList_ID", Integer.valueOf(i));
						else
						{
							String sql2 = "SELECT M_PriceList_ID FROM M_PriceList WHERE AD_Client_ID=? AND IsSOPriceList=? AND IsActive='Y' ORDER BY IsDefault DESC";
							ii = DB.getSQLValue (null, sql2, Env.getAD_Client_ID(ctx), IsSOTrx);
							if (ii != 0)
								mTab.setValue("M_PriceList_ID", Integer.valueOf(ii));
						}
					}
				}

				int bill_Location_ID = rs.getInt("Bill_Location_ID");
				//	overwritten by InfoBP selection - works only if InfoWindow
				//	was used otherwise creates error (uses last value, may belong to different BP)
				if (bill_BPartner_ID.toString().equals(Env.getContext(ctx, WindowNo, Env.TAB_INFO, "C_BPartner_ID")))
				{
					int locationId = 0;
					String loc = Env.getContext(ctx, WindowNo, Env.TAB_INFO, "C_BPartner_Location_ID");
					if (loc.length() > 0)
						locationId = Integer.parseInt(loc);
					if (locationId > 0) {
						MBPartnerLocation bpLocation = new MBPartnerLocation(ctx, locationId, null);
						if (bpLocation.isBillTo())
							bill_Location_ID = locationId;
					}
				}
				if (bill_Location_ID == 0)
					mTab.setValue("Bill_Location_ID", null);
				else
					mTab.setValue("Bill_Location_ID", Integer.valueOf(bill_Location_ID));

				//	Contact - overwritten by InfoBP selection
				int contID = rs.getInt("AD_User_ID");
				if (bill_BPartner_ID.toString().equals(Env.getContext(ctx, WindowNo, Env.TAB_INFO, "C_BPartner_ID")))
				{
					String cont = Env.getContext(ctx, WindowNo, Env.TAB_INFO, "AD_User_ID");
					if (cont.length() > 0)
						contID = Integer.parseInt(cont);
				}
				if (contID == 0)
					mTab.setValue("Bill_User_ID", null);
				else
					mTab.setValue("Bill_User_ID", Integer.valueOf(contID));

				//	CreditAvailable
				if (IsSOTrx)
				{
					double CreditLimit = rs.getDouble("SO_CreditLimit");
					if (CreditLimit != 0)
					{
						double CreditAvailable = rs.getDouble("CreditAvailable");
						if (!rs.wasNull() && CreditAvailable < 0)
							mTab.fireDataStatusEEvent("CreditLimitOver",
									DisplayType.getNumberFormat(DisplayType.Amount).format(CreditAvailable),
									false);
					}
				}

				//	PO Reference
				String s = rs.getString("POReference");
				if (s != null && s.length() != 0)
					mTab.setValue("POReference", s);
				else
					mTab.setValue("POReference", null);
				//	SO Description
				s = rs.getString("SO_Description");
				if (s != null && s.trim().length() != 0)
					mTab.setValue("Description", s);
				//	IsDiscountPrinted
				s = rs.getString("IsDiscountPrinted");
				if (s != null && s.length() != 0)
					mTab.setValue("IsDiscountPrinted", s);
				else
					mTab.setValue("IsDiscountPrinted", "N");

				//	Defaults, if not Walkin Receipt or Walkin Invoice
				String OrderType = Env.getContext(ctx, WindowNo, "OrderType");
				mTab.setValue("InvoiceRule", X_C_Order.INVOICERULE_AfterDelivery);
				mTab.setValue("PaymentRule", X_C_Order.PAYMENTRULE_OnCredit);
				if (OrderType.equals(MOrder.DocSubTypeSO_Prepay))
					mTab.setValue("InvoiceRule", X_C_Order.INVOICERULE_Immediate);
				else if (OrderType.equals(MOrder.DocSubTypeSO_POS))	//  for POS
					mTab.setValue("PaymentRule", X_C_Order.PAYMENTRULE_Cash);
				else
				{
					//	PaymentRule
					s = rs.getString(IsSOTrx ? "PaymentRule" : "PaymentRulePO");
					if (s != null && s.length() != 0)
					{
						if (s.equals("B"))				//	No Cache in Non POS
							s = "P";
						if (IsSOTrx && (s.equals("S") || s.equals("U")))	//	No Check/Transfer for SO_Trx
							s = "P";										//  Payment Term
						mTab.setValue("PaymentRule", s);
					}
					//	Payment Term
					ii = Integer.valueOf(rs.getInt(IsSOTrx ? "C_PaymentTerm_ID" : "PO_PaymentTerm_ID"));
					if (!rs.wasNull())
						mTab.setValue("C_PaymentTerm_ID", ii);
					//	InvoiceRule
					s = rs.getString("InvoiceRule");
					if (s != null && s.length() != 0)
						mTab.setValue("InvoiceRule", s);
				}
			}
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, "bPartnerBill", e);
			return e.getLocalizedMessage();
		}
		finally
		{
			DB.close(rs, pstmt);
			rs = null; pstmt = null;
		}
		return "";
	}	//	bPartnerBill

	public String warehouse (Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{
		if (isCalloutActive())		//	assuming it is resetting value
			return "";

		Integer M_Warehouse_ID = (Integer)value;
		if (M_Warehouse_ID == null || M_Warehouse_ID.intValue() == 0)
			return "";

		MWarehouse wh = MWarehouse.get(ctx, M_Warehouse_ID);
		String DeliveryRule = mTab.get_ValueAsString("DeliveryRule");
		if((wh.isDisallowNegativeInv() && DeliveryRule.equals(X_C_Quotation.DELIVERYRULE_Force)) ||
				(DeliveryRule == null || DeliveryRule.length()==0))
			mTab.setValue("DeliveryRule",X_C_Quotation.DELIVERYRULE_Availability);

		return "";
	}	//	warehouse

	public String priceList (Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{
		Integer M_PriceList_ID = (Integer) mTab.getValue("M_PriceList_ID");
		if (M_PriceList_ID == null || M_PriceList_ID.intValue()== 0)
			return "";
		if (steps) log.warning("init");
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "SELECT pl.IsTaxIncluded,pl.EnforcePriceLimit,pl.C_Currency_ID,c.StdPrecision,"
				+ "plv.M_PriceList_Version_ID,plv.ValidFrom "
				+ "FROM M_PriceList pl,C_Currency c,M_PriceList_Version plv "
				+ "WHERE pl.C_Currency_ID=c.C_Currency_ID"
				+ " AND pl.M_PriceList_ID=plv.M_PriceList_ID"
				+ " AND pl.M_PriceList_ID=? "						//	1
				+ " AND plv.ValidFrom <= ? "
				+ "ORDER BY plv.ValidFrom DESC";
		//	Use newest price list - may not be future
		try
		{
			pstmt = DB.prepareStatement(sql, null);
			pstmt.setInt(1, M_PriceList_ID.intValue());
			Timestamp date = new Timestamp(System.currentTimeMillis());
			if (mTab.getAD_Table_ID() == I_C_Quotation.Table_ID)
				date = Env.getContextAsDate(ctx, WindowNo, "DateOrdered");
			else if (mTab.getAD_Table_ID() == I_C_Invoice.Table_ID)
				date = Env.getContextAsDate(ctx, WindowNo, "DateInvoiced");
			pstmt.setTimestamp(2, date);

			rs = pstmt.executeQuery();
			if (rs.next())
			{
				//	Tax Included
				mTab.setValue("IsTaxIncluded", Boolean.valueOf("Y".equals(rs.getString(1))));
				//	Price Limit Enforce
				Env.setContext(ctx, WindowNo, "EnforcePriceLimit", rs.getString(2));
				//	Currency
				Integer ii = Integer.valueOf(rs.getInt(3));
				mTab.setValue("C_Currency_ID", ii);
				//	PriceList Version
				Env.setContext(ctx, WindowNo, "M_PriceList_Version_ID", rs.getInt(5));
			}
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, sql, e);
			return e.getLocalizedMessage();
		}
		finally
		{
			DB.close(rs, pstmt);
			rs = null; pstmt = null;
		}
		if (steps) log.warning("fini");

		return "";
	}	//	priceList

	public String paymentTerm (Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{
		Integer C_PaymentTerm_ID = (Integer)value;
		int C_Quotation_ID = Env.getContextAsInt(ctx, WindowNo, "C_Order_ID");
		if (C_PaymentTerm_ID == null || C_PaymentTerm_ID.intValue() == 0
				|| C_Quotation_ID == 0)	//	not saved yet
			return "";
		//
		MPaymentTerm pt = new MPaymentTerm (ctx, C_PaymentTerm_ID.intValue(), null);
		if (pt.get_ID() == 0)
			return "PaymentTerm not found";

		boolean valid = pt.applyOrder (C_Quotation_ID);
		mTab.setValue("IsPayScheduleValid", valid ? "Y" : "N");

		return "";
	}	//	paymentTerm

	public String product (Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{
		try{
			Integer M_Product_ID = (Integer)value;
			MProduct product = new MProduct(ctx, M_Product_ID, null);
			mTab.setValue("M_Product_Category_ID", product.getM_Product_Category_ID());
			//mTab.setValue("Size", product.get_Value("Size"));


			//Integer M_Product_ID = (Integer)value;
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
			BigDecimal Qty = (BigDecimal)mTab.getValue("QtyOrdered");
			boolean IsSOTrx = Env.getContext(ctx, WindowNo, "IsSOTrx").equals("Y");
			MProductPricing pp = new MProductPricing (M_Product_ID.intValue(), C_BPartner_ID, Qty, IsSOTrx, null);
			//
			int M_PriceList_ID = Env.getContextAsInt(ctx, WindowNo, "M_PriceList_ID");
			pp.setM_PriceList_ID(M_PriceList_ID);
			Timestamp orderDate = (Timestamp)mTab.getValue("DateOrdered");
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

			BigDecimal priceList = (BigDecimal)mTab.getValue("PriceList");
			BigDecimal price = (BigDecimal)mTab.getValue("PriceEntered");
			String newProduct = (String)mTab.getValue("Product");
			if (newProduct!=null){
				isNewProductDes = true;
			}
			pp.setM_PriceList_Version_ID(M_PriceList_Version_ID);
			pp.setPriceDate(orderDate);
			//

			//@tegar : set new product price on quotation line 
			if (priceList.compareTo(Env.ZERO) > 0 && isNewProductDes){
				mTab.setValue("PriceList", priceList);
				mTab.setValue("PriceEntered", price);
				mTab.setValue("PriceActual", price);
				mTab.setValue("PriceLimit", price);

			}	else{

				mTab.setValue("PriceList", pp.getPriceList());
				mTab.setValue("PriceEntered", pp.getPriceStd());
				mTab.setValue("PriceActual", pp.getPriceStd());
				mTab.setValue("PriceLimit", pp.getPriceLimit());

			}

			mTab.setValue("C_Currency_ID", Integer.valueOf(pp.getC_Currency_ID()));
			mTab.setValue("Discount", pp.getDiscount());
			mTab.setValue("C_UOM_ID", Integer.valueOf(pp.getC_UOM_ID()));
			mTab.setValue("QtyOrdered", mTab.getValue("QtyEntered"));
			Env.setContext(ctx, WindowNo, "EnforcePriceLimit", pp.isEnforcePriceLimit() ? "Y" : "N");
			Env.setContext(ctx, WindowNo, "DiscountSchema", pp.isDiscountSchema() ? "Y" : "N");

			//	Check/Update Warehouse Setting
			//	int M_Warehouse_ID = Env.getContextAsInt(ctx, WindowNo, "M_Warehouse_ID");
			//	Integer wh = (Integer)mTab.getValue("M_Warehouse_ID");
			//	if (wh.intValue() != M_Warehouse_ID)
			//	{
			//		mTab.setValue("M_Warehouse_ID", new Integer(M_Warehouse_ID));
			//		ADialog.warn(,WindowNo, "WarehouseChanged");
			//	}

			//
			if (steps) log.warning("fini");
			return tax (ctx, WindowNo, mTab, mField, value);
		}catch(Exception e){
			return"";
		}
	}	//	product

	public String tax (Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{
		String column = mField.getColumnName();
		if (value == null)
			return "";
		if (steps) log.warning("init");

		//	Check Product
		int M_Product_ID = 0;
		if (column.equals("M_Product_ID"))
			M_Product_ID = ((Integer)value).intValue();
		else
			M_Product_ID = Env.getContextAsInt(ctx, WindowNo, mTab.getTabNo(), "M_Product_ID");
		int C_Charge_ID = 0;
		if (column.equals("C_Charge_ID"))
			C_Charge_ID = ((Integer)value).intValue();
		else
			C_Charge_ID = Env.getContextAsInt(ctx, WindowNo, mTab.getTabNo(), "C_Charge_ID");
		if (log.isLoggable(Level.FINE)) log.fine("Product=" + M_Product_ID + ", C_Charge_ID=" + C_Charge_ID);
		if (M_Product_ID == 0 && C_Charge_ID == 0)
			return amt(ctx, WindowNo, mTab, mField, value);		//

		//	Check Partner Location
		int shipC_BPartner_Location_ID = 0;
		if (column.equals("C_BPartner_Location_ID"))
			shipC_BPartner_Location_ID = ((Integer)value).intValue();
		else
			shipC_BPartner_Location_ID = Env.getContextAsInt(ctx, WindowNo, "C_BPartner_Location_ID");
		if (shipC_BPartner_Location_ID == 0)
			return amt(ctx, WindowNo, mTab, mField, value);		//
		if (log.isLoggable(Level.FINE)) log.fine("Ship BP_Location=" + shipC_BPartner_Location_ID);

		//
		Timestamp billDate = Env.getContextAsDate(ctx, WindowNo, "DateOrdered");
		if (log.isLoggable(Level.FINE)) log.fine("Bill Date=" + billDate);

		Timestamp shipDate = Env.getContextAsDate(ctx, WindowNo, "DatePromised");
		if (log.isLoggable(Level.FINE)) log.fine("Ship Date=" + shipDate);

		int AD_Org_ID = Env.getContextAsInt(ctx, WindowNo, "AD_Org_ID");
		if (log.isLoggable(Level.FINE)) log.fine("Org=" + AD_Org_ID);

		int M_Warehouse_ID = Env.getContextAsInt(ctx, WindowNo, "M_Warehouse_ID");
		if (log.isLoggable(Level.FINE)) log.fine("Warehouse=" + M_Warehouse_ID);

		int billC_BPartner_Location_ID = Env.getContextAsInt(ctx, WindowNo, "Bill_Location_ID");
		if (billC_BPartner_Location_ID == 0)
			billC_BPartner_Location_ID = shipC_BPartner_Location_ID;
		if (log.isLoggable(Level.FINE)) log.fine("Bill BP_Location=" + billC_BPartner_Location_ID);

		//
		int C_Tax_ID = Tax.get (ctx, M_Product_ID, C_Charge_ID, billDate, shipDate,
				AD_Org_ID, M_Warehouse_ID, billC_BPartner_Location_ID, shipC_BPartner_Location_ID,
				"Y".equals(Env.getContext(ctx, WindowNo, "IsSOTrx")), null);
		if (log.isLoggable(Level.INFO)) log.info("Tax ID=" + C_Tax_ID);
		//
		if (C_Tax_ID == 0)
			mTab.fireDataStatusEEvent(CLogger.retrieveError());
		else
			mTab.setValue("C_Tax_ID", Integer.valueOf(C_Tax_ID));
		//
		if (steps) log.warning("fini");
		return amt(ctx, WindowNo, mTab, mField, value);
	}	//	tax

	public String amt (Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{
		if (isCalloutActive() || value == null)
			return "";

		if (steps) log.warning("init");
		int C_UOM_To_ID = Env.getContextAsInt(ctx, WindowNo, mTab.getTabNo(), "C_UOM_ID");
		int M_Product_ID = Env.getContextAsInt(ctx, WindowNo, mTab.getTabNo(), "M_Product_ID");
		int M_PriceList_ID = Env.getContextAsInt(ctx, WindowNo, mTab.getTabNo(), "M_PriceList_ID");
		int StdPrecision = MPriceList.getStandardPrecision(ctx, M_PriceList_ID);
		MPriceList pl = new MPriceList(ctx, M_PriceList_ID, null);
		boolean isEnforcePriceLimit = pl.isEnforcePriceLimit();
		BigDecimal QtyEntered, QtyOrdered, PriceEntered, PriceActual, PriceLimit, Discount, PriceList;
		//	get values
		QtyEntered = mTab.getValue("QtyEntered") != null ? (BigDecimal)mTab.getValue("QtyEntered") : BigDecimal.ZERO;
		QtyOrdered = mTab.getValue("QtyOrdered") != null ? (BigDecimal)mTab.getValue("QtyOrdered") : BigDecimal.ZERO;
		if (log.isLoggable(Level.FINE)) log.fine("QtyEntered=" + QtyEntered + ", Ordered=" + QtyOrdered + ", UOM=" + C_UOM_To_ID);
		//
		PriceEntered = mTab.getValue("PriceEntered") != null ? (BigDecimal)mTab.getValue("PriceEntered") : BigDecimal.ZERO;
		PriceActual = mTab.getValue("PriceActual") != null ? (BigDecimal)mTab.getValue("PriceActual") : BigDecimal.ZERO;
		Discount = mTab.getValue("Discount") != null ? (BigDecimal)mTab.getValue("Discount") : BigDecimal.ZERO;
		PriceLimit = mTab.getValue("PriceLimit") != null ? (BigDecimal)mTab.getValue("PriceLimit") : BigDecimal.ZERO;
		PriceList = mTab.getValue("PriceList") != null ? (BigDecimal)mTab.getValue("PriceList") : BigDecimal.ZERO;
		
		if (log.isLoggable(Level.FINE)){
			log.fine("PriceList=" + PriceList + ", Limit=" + PriceLimit + ", Precision=" + StdPrecision);
			log.fine("PriceEntered=" + PriceEntered + ", Actual=" + PriceActual + ", Discount=" + Discount);
		}
		//FBI-1574 validate null PriceList @Albert
		if(PriceList==null)
			PriceList = Env.ZERO;
		//End of FBI-1574

		//		No Product
		if (M_Product_ID == 0)
		{
			// if price change sync price actual and entered
			// else ignore
			if (mField.getColumnName().equals("PriceActual"))
			{
				PriceEntered = (BigDecimal) value;
				mTab.setValue("PriceEntered", value);
			}
			else if (mField.getColumnName().equals("PriceEntered"))
			{
				PriceActual = (BigDecimal) value;
				mTab.setValue("PriceActual", value);
			}
		}
		//	Product Qty changed - recalc price
		else if ((mField.getColumnName().equals("QtyOrdered")
				|| mField.getColumnName().equals("QtyEntered")
				|| mField.getColumnName().equals("C_UOM_ID")
				|| mField.getColumnName().equals("M_Product_ID"))
				&& !"N".equals(Env.getContext(ctx, WindowNo, "DiscountSchema")))
		{
			int C_BPartner_ID = Env.getContextAsInt(ctx, WindowNo, "C_BPartner_ID");
			if (mField.getColumnName().equals("QtyEntered"))
				QtyOrdered = MUOMConversion.convertProductFrom (ctx, M_Product_ID,
						C_UOM_To_ID, QtyEntered);
			if (QtyOrdered == null)
				QtyOrdered = QtyEntered;
			boolean IsSOTrx = Env.getContext(ctx, WindowNo, "IsSOTrx").equals("Y");
			MProductPricing pp = new MProductPricing (M_Product_ID, C_BPartner_ID, QtyOrdered, IsSOTrx, null);
			pp.setM_PriceList_ID(M_PriceList_ID);
			int M_PriceList_Version_ID = Env.getContextAsInt(ctx, WindowNo, "M_PriceList_Version_ID");
			pp.setM_PriceList_Version_ID(M_PriceList_Version_ID);
			Timestamp date = (Timestamp)mTab.getValue("DateOrdered");
			pp.setPriceDate(date);
			//
			PriceEntered = MUOMConversion.convertProductFrom (ctx, M_Product_ID,
					C_UOM_To_ID, pp.getPriceStd());
			if (PriceEntered == null)
				PriceEntered = pp.getPriceStd();
			//
			if (log.isLoggable(Level.FINE)) log.fine("QtyChanged -> PriceActual=" + pp.getPriceStd()
			+ ", PriceEntered=" + PriceEntered + ", Discount=" + pp.getDiscount());
			PriceActual = pp.getPriceStd();
			mTab.setValue("PriceActual", pp.getPriceStd());
			mTab.setValue("Discount", pp.getDiscount());
			mTab.setValue("PriceEntered", PriceEntered);
			Env.setContext(ctx, WindowNo, "DiscountSchema", pp.isDiscountSchema() ? "Y" : "N");
		}
		else if (mField.getColumnName().equals("PriceActual"))
		{
			PriceActual = (BigDecimal)value;
			PriceEntered = MUOMConversion.convertProductFrom (ctx, M_Product_ID,
					C_UOM_To_ID, PriceActual);
			if (PriceEntered == null)
				PriceEntered = PriceActual;
			//
			if (log.isLoggable(Level.FINE)) log.fine("PriceActual=" + PriceActual
					+ " -> PriceEntered=" + PriceEntered);
			mTab.setValue("PriceEntered", PriceEntered);
		}
		else if (mField.getColumnName().equals("PriceEntered"))
		{
			PriceEntered = (BigDecimal)value;
			PriceActual = MUOMConversion.convertProductTo (ctx, M_Product_ID,
					C_UOM_To_ID, PriceEntered);
			if (PriceActual == null)
				PriceActual = PriceEntered;
			//
			if (log.isLoggable(Level.FINE)) log.fine("PriceEntered=" + PriceEntered
					+ " -> PriceActual=" + PriceActual);
			mTab.setValue("PriceActual", PriceActual);
		}

		//  Discount entered - Calculate Actual/Entered
		if (mField.getColumnName().equals("Discount"))
		{
			if ( PriceList.doubleValue() != 0 )
				PriceActual = BigDecimal.valueOf((100.0 - Discount.doubleValue()) / 100.0 * PriceList.doubleValue());
			if (PriceActual.scale() > StdPrecision)
				PriceActual = PriceActual.setScale(StdPrecision, RoundingMode.HALF_UP);
			PriceEntered = MUOMConversion.convertProductFrom (ctx, M_Product_ID,
					C_UOM_To_ID, PriceActual);
			if (PriceEntered == null)
				PriceEntered = PriceActual;
			mTab.setValue("PriceActual", PriceActual);
			mTab.setValue("PriceEntered", PriceEntered);
		}
		//	calculate Discount
		else
		{
			if (PriceList.intValue() == 0)
				Discount = Env.ZERO;
			else
				Discount = BigDecimal.valueOf((PriceList.doubleValue() - PriceActual.doubleValue()) / PriceList.doubleValue() * 100.0);
			
			if (Discount.scale() > 2)
				Discount = Discount.setScale(2, RoundingMode.HALF_UP);
			
			mTab.setValue("Discount", Discount);
		}
		if (log.isLoggable(Level.FINE)) log.fine("PriceEntered=" + PriceEntered + ", Actual=" + PriceActual + ", Discount=" + Discount);

		//	Check PriceLimit
		String epl = Env.getContext(ctx, WindowNo, "EnforcePriceLimit");
		boolean enforce = Env.isSOTrx(ctx, WindowNo) && epl != null && !epl.equals("") ? epl.equals("Y") : isEnforcePriceLimit;
		if (enforce && MRole.getDefault().isOverwritePriceLimit())
			enforce = false;

		//	Check Price Limit?
		//@tegar validate null pricelimit
		if(PriceLimit==null){
			PriceLimit = Env.ZERO;
			mTab.setValue ("PriceLimit", PriceLimit);
		}//end
		if (enforce && PriceLimit.doubleValue() != 0.0
				&& PriceActual.compareTo(PriceLimit) < 0)
		{
			//@tegar
			if(isNewProductDes==true){
				mTab.setValue ("PriceLimit", PriceEntered);
			}//end
			PriceActual = PriceLimit;
			PriceEntered = MUOMConversion.convertProductFrom (ctx, M_Product_ID,
					C_UOM_To_ID, PriceLimit);
			if (PriceEntered == null)
				PriceEntered = PriceLimit;
			if (log.isLoggable(Level.FINE)) log.fine("(under) PriceEntered=" + PriceEntered + ", Actual" + PriceLimit);
			mTab.setValue ("PriceEntered", PriceEntered);
			mTab.fireDataStatusEEvent ("UnderLimitPrice", "", false);
			//	Repeat Discount calc
			if (PriceList.intValue() != 0)
			{
				Discount = BigDecimal.valueOf((PriceList.doubleValue () - PriceActual.doubleValue ()) / PriceList.doubleValue () * 100.0);
				if (Discount.scale () > 2)
					Discount = Discount.setScale (2, RoundingMode.HALF_UP);
				mTab.setValue ("Discount", Discount);
			}
		}

		//@tegar
		//BigDecimal Diskon = (BigDecimal)mTab.getValue("Discount");
		if (Discount.compareTo(Env.ZERO) < 0){
			mTab.setValue("Discount", Env.ZERO);
		}
		//	Line Net Amt

		//@tegar
		BigDecimal price = (BigDecimal)mTab.getValue("PriceEntered");
		if (price.compareTo(Env.ZERO)>0) {
			BigDecimal LineNetAmt = QtyOrdered.multiply(price);
			if (LineNetAmt.scale() > StdPrecision)
				LineNetAmt = LineNetAmt.setScale(StdPrecision, RoundingMode.HALF_UP);
			if (log.isLoggable(Level.INFO)) log.info("LineNetAmt=" + LineNetAmt);
			mTab.setValue("LineNetAmt", LineNetAmt);//end tegar
		} else {
			BigDecimal LineNetAmt = QtyOrdered.multiply(PriceActual);
			if (LineNetAmt.scale() > StdPrecision)
				LineNetAmt = LineNetAmt.setScale(StdPrecision, RoundingMode.HALF_UP);
			if (log.isLoggable(Level.INFO)) log.info("LineNetAmt=" + LineNetAmt);
			mTab.setValue("LineNetAmt", LineNetAmt);
			//
		}

		return "";

	}	//	amt

	public String qty (Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{
		if (isCalloutActive() || value == null)
			return "";
		int M_Product_ID = Env.getContextAsInt(ctx, WindowNo, mTab.getTabNo(), "M_Product_ID");
		if (steps) log.warning("init - M_Product_ID=" + M_Product_ID + " - " );
		BigDecimal QtyOrdered = Env.ZERO;
		BigDecimal QtyEntered, PriceActual, PriceEntered;

		//	No Product
		if (M_Product_ID == 0)
		{
			QtyEntered = (BigDecimal)mTab.getValue("QtyEntered");
			QtyOrdered = QtyEntered;
			mTab.setValue("QtyOrdered", QtyOrdered);
		}
		//	UOM Changed - convert from Entered -> Product
		else if (mField.getColumnName().equals("C_UOM_ID"))
		{
			int C_UOM_To_ID = ((Integer)value).intValue();
			QtyEntered = (BigDecimal)mTab.getValue("QtyEntered");
			BigDecimal QtyEntered1 = QtyEntered.setScale(MUOM.getPrecision(ctx, C_UOM_To_ID), RoundingMode.HALF_UP);
			if (QtyEntered.compareTo(QtyEntered1) != 0)
			{
				if (log.isLoggable(Level.FINE)) log.fine("Corrected QtyEntered Scale UOM=" + C_UOM_To_ID
						+ "; QtyEntered=" + QtyEntered + "->" + QtyEntered1);
				QtyEntered = QtyEntered1;
				mTab.setValue("QtyEntered", QtyEntered);
			}
			QtyOrdered = MUOMConversion.convertProductFrom (ctx, M_Product_ID,
					C_UOM_To_ID, QtyEntered);
			if (QtyOrdered == null)
				QtyOrdered = QtyEntered;
			boolean conversion = QtyEntered.compareTo(QtyOrdered) != 0;
			PriceActual = (BigDecimal)mTab.getValue("PriceActual");
			PriceEntered = MUOMConversion.convertProductFrom (ctx, M_Product_ID,
					C_UOM_To_ID, PriceActual);
			if (PriceEntered == null)
				PriceEntered = PriceActual;
			if (log.isLoggable(Level.FINE)) log.fine("UOM=" + C_UOM_To_ID
					+ ", QtyEntered/PriceActual=" + QtyEntered + "/" + PriceActual
					+ " -> " + conversion
					+ " QtyOrdered/PriceEntered=" + QtyOrdered + "/" + PriceEntered);
			Env.setContext(ctx, WindowNo, "UOMConversion", conversion ? "Y" : "N");
			mTab.setValue("QtyOrdered", QtyOrdered);
			mTab.setValue("PriceEntered", PriceEntered);
		}
		//	QtyEntered changed - calculate QtyOrdered
		else if (mField.getColumnName().equals("QtyEntered"))
		{
			int C_UOM_To_ID = Env.getContextAsInt(ctx, WindowNo, mTab.getTabNo(), "C_UOM_ID");
			QtyEntered = (BigDecimal)value;
			BigDecimal QtyEntered1 = QtyEntered.setScale(MUOM.getPrecision(ctx, C_UOM_To_ID), RoundingMode.HALF_UP);
			if (QtyEntered.compareTo(QtyEntered1) != 0)
			{
				if (log.isLoggable(Level.FINE)) log.fine("Corrected QtyEntered Scale UOM=" + C_UOM_To_ID
						+ "; QtyEntered=" + QtyEntered + "->" + QtyEntered1);
				QtyEntered = QtyEntered1;
				mTab.setValue("QtyEntered", QtyEntered);
			}
			QtyOrdered = MUOMConversion.convertProductFrom (ctx, M_Product_ID,
					C_UOM_To_ID, QtyEntered);
			if (QtyOrdered == null)
				QtyOrdered = QtyEntered;
			boolean conversion = QtyEntered.compareTo(QtyOrdered) != 0;
			if (log.isLoggable(Level.FINE)) log.fine("UOM=" + C_UOM_To_ID
					+ ", QtyEntered=" + QtyEntered
					+ " -> " + conversion
					+ " QtyOrdered=" + QtyOrdered);
			Env.setContext(ctx, WindowNo, "UOMConversion", conversion ? "Y" : "N");
			mTab.setValue("QtyOrdered", QtyOrdered);
		}
		//	QtyOrdered changed - calculate QtyEntered (should not happen)
		else if (mField.getColumnName().equals("QtyOrdered"))
		{
			int C_UOM_To_ID = Env.getContextAsInt(ctx, WindowNo, mTab.getTabNo(), "C_UOM_ID");
			QtyOrdered = (BigDecimal)value;
			int precision = MProduct.get(ctx, M_Product_ID).getUOMPrecision();
			BigDecimal QtyOrdered1 = QtyOrdered.setScale(precision, RoundingMode.HALF_UP);
			if (QtyOrdered.compareTo(QtyOrdered1) != 0)
			{
				if (log.isLoggable(Level.FINE)) log.fine("Corrected QtyOrdered Scale "
						+ QtyOrdered + "->" + QtyOrdered1);
				QtyOrdered = QtyOrdered1;
				mTab.setValue("QtyOrdered", QtyOrdered);
			}
			QtyEntered = MUOMConversion.convertProductTo (ctx, M_Product_ID,
					C_UOM_To_ID, QtyOrdered);
			if (QtyEntered == null)
				QtyEntered = QtyOrdered;
			boolean conversion = QtyOrdered.compareTo(QtyEntered) != 0;
			if (log.isLoggable(Level.FINE)) log.fine("UOM=" + C_UOM_To_ID
					+ ", QtyOrdered=" + QtyOrdered
					+ " -> " + conversion
					+ " QtyEntered=" + QtyEntered);
			Env.setContext(ctx, WindowNo, "UOMConversion", conversion ? "Y" : "N");
			mTab.setValue("QtyEntered", QtyEntered);
		}
		else
		{
			QtyOrdered = (BigDecimal)mTab.getValue("QtyOrdered");
		}

		//
		return "";
	}	//	qty

	public String SalesOrderTenderType (Properties ctx, int WindowNo,
			GridTab mTab, GridField mField, Object value, Object oldValue)
	{
		log.info("");
		// Called from tender type in Sales Order - POS Payments
		// to fill IsPostDated and TenderType

		if (value == null)
			return "";

		int tendertype_id = ((Integer) value).intValue();

		X_C_POSTenderType tendertype = new X_C_POSTenderType(ctx, tendertype_id, null);
		mTab.setValue("IsPostDated", tendertype.isPostDated());
		mTab.setValue("TenderType", tendertype.getTenderType());

		return "";
	}	//	SalesOrderTenderType

	public String organization(Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value){

		//Return if Organization field is empty
		if(value == null || (Integer)value == 0)
			return "";

		log.info("Set default Warehouse for Organization " + value + " on Window " + WindowNo);

		//Get the current Warehouse
		Integer m_warehouse_id = (Integer) mTab.getValue("M_Warehouse_ID");

		//Only set Warehouse if the field is empty
		if(m_warehouse_id == null || m_warehouse_id == 0){
			Integer ad_org_id = (Integer) value;
			MOrgInfo orginfo = MOrgInfo.get(ctx, ad_org_id.intValue(), null);

			//only set Warehouse if there is a default Warehouse on OrgInfo
			if(orginfo!=null && orginfo.getM_Warehouse_ID() != 0)
				mTab.setValue("M_Warehouse_ID", orginfo.getM_Warehouse_ID());
		}
		return "";
	}

	public String charge (Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{
		Integer C_Charge_ID = (Integer)value;
		if (C_Charge_ID == null || C_Charge_ID.intValue() == 0)
			return "";
		//	No Product defined
		if (mTab.getValue("M_Product_ID") != null)
		{
			mTab.setValue("C_Charge_ID", null);
			return "ChargeExclusively";
		}
		mTab.setValue("M_AttributeSetInstance_ID", null);
		mTab.setValue("S_ResourceAssignment_ID", null);
		mTab.setValue("C_UOM_ID", null);	//	EA

		Env.setContext(ctx, WindowNo, "DiscountSchema", "N");
		String sql = "SELECT ChargeAmt FROM C_Charge WHERE C_Charge_ID=?";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, null);
			pstmt.setInt(1, C_Charge_ID.intValue());
			rs = pstmt.executeQuery();
			if (rs.next())
			{
				mTab.setValue ("PriceEntered", rs.getBigDecimal (1));
				mTab.setValue ("PriceActual", rs.getBigDecimal (1));
				mTab.setValue ("PriceLimit", Env.ZERO);
				mTab.setValue ("PriceList", Env.ZERO);
				mTab.setValue ("Discount", Env.ZERO);
			}
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, sql, e);
			return e.getLocalizedMessage();
		}
		finally
		{
			DB.close(rs, pstmt);
			rs = null; pstmt = null;
		}

		//
		return tax (ctx, WindowNo, mTab, mField, value);
	}	//	charge

	@Override
	public String start(Properties ctx, int WindowNo, GridTab mTab,
			GridField mField, Object value, Object oldValue) {

		String msg = "";

		if(mField.getColumnName().equals(MQuotationLine.COLUMNNAME_Discount)){
			msg += total(ctx, WindowNo, mTab, mField, value);
			msg += amt(ctx, WindowNo, mTab, mField, oldValue);
			return msg;
		}
		else if(mField.getColumnName().equals(MQuotationLine.COLUMNNAME_M_Product_ID)){
			msg += product(ctx, WindowNo, mTab, mField, value);
			msg += amt(ctx, WindowNo, mTab, mField, oldValue);
			return msg;
		}
		else if(mField.getColumnName().equals(MQuotationLine.COLUMNNAME_C_UOM_ID)){
			msg += qty(ctx, WindowNo, mTab, mField, value);				
			msg += amt(ctx, WindowNo, mTab, mField, value);				
			return msg;
		}
		else if(mField.getColumnName().equals(MQuotationLine.COLUMNNAME_PriceEntered)){
			msg += amt(ctx, WindowNo, mTab, mField, value);
			return msg;
		}
		else if(mField.getColumnName().equals(MQuotationLine.COLUMNNAME_PriceActual)){
			msg += amt(ctx, WindowNo, mTab, mField, value);
			return msg;
		}
		else if(mField.getColumnName().equals(MQuotationLine.COLUMNNAME_PriceList)){
			msg += amt(ctx, WindowNo, mTab, mField, value);
			return msg;
		}
		else if(mField.getColumnName().equals(MQuotationLine.COLUMNNAME_QtyEntered)){
			msg += qty(ctx, WindowNo, mTab, mField, value);
			msg += amt(ctx, WindowNo, mTab, mField, value);
			return msg;
		}
		else if(mField.getColumnName().equals(MQuotationLine.COLUMNNAME_C_BPartner_Location_ID)){
			msg += tax(ctx, WindowNo, mTab, mField, value);
			return msg;
		}
		else if(mField.getColumnName().equals(MQuotationLine.COLUMNNAME_QtyOrdered)){
			msg += qty(ctx, WindowNo, mTab, mField, value);
			msg += amt(ctx, WindowNo, mTab, mField, value);
			return msg;
		}

		return null;
	}
}
