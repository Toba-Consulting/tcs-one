/******************************************************************************
 * Product: iDempiere ERP & CRM Smart Business Solution                       *
 * Copyright (C) 1999-2012 ComPiere, Inc. All Rights Reserved.                *
 * This program is free software, you can redistribute it and/or modify it    *
 * under the terms version 2 of the GNU General Public License as published   *
 * by the Free Software Foundation. This program is distributed in the hope   *
 * that it will be useful, but WITHOUT ANY WARRANTY, without even the implied *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
 * See the GNU General Public License for more details.                       *
 * You should have received a copy of the GNU General Public License along    *
 * with this program, if not, write to the Free Software Foundation, Inc.,    *
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     *
 * For the text or an alternative of this public license, you may reach us    *
 * ComPiere, Inc., 2620 Augustine Dr. #245, Santa Clara, CA 95054, USA        *
 * or via info@compiere.org or http://www.compiere.org/license.html           *
 *****************************************************************************/
/** Generated Model - DO NOT CHANGE */
package id.tcs.model;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Properties;

import id.tcs.model.I_C_Quotation;
import id.tcs.model.I_C_QuotationLine;
import org.compiere.model.I_Persistent;
import id.tcs.model.I_TCS_R_RequestLine;
import org.compiere.model.MTable;
import org.compiere.model.PO;
import org.compiere.model.POInfo;
import org.compiere.util.Env;
import org.compiere.util.KeyNamePair;

/** Generated Model for M_MatchRequest
 *  @author iDempiere (generated) 
 *  @version Release 2.1 - $Id$ */
public class X_M_MatchRequest extends PO implements I_M_MatchRequest, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20150623L;

    /** Standard Constructor */
    public X_M_MatchRequest (Properties ctx, int M_MatchRequest_ID, String trxName)
    {
      super (ctx, M_MatchRequest_ID, trxName);
      /** if (M_MatchRequest_ID == 0)
        {
			setM_MatchRequest_ID (0);
        } */
    }

    /** Load Constructor */
    public X_M_MatchRequest (Properties ctx, ResultSet rs, String trxName)
    {
      super (ctx, rs, trxName);
    }

    /** AccessLevel
      * @return 3 - Client - Org 
      */
    protected int get_AccessLevel()
    {
      return accessLevel.intValue();
    }

    /** Load Meta Data */
    protected POInfo initPO (Properties ctx)
    {
      POInfo poi = POInfo.getPOInfo (ctx, Table_ID, get_TrxName());
      return poi;
    }

    public String toString()
    {
      StringBuffer sb = new StringBuffer ("X_M_MatchRequest[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	public I_C_Inquiry getC_Inquiry() throws RuntimeException
    {
		return (I_C_Inquiry)MTable.get(getCtx(), I_C_Inquiry.Table_Name)
			.getPO(getC_Inquiry_ID(), get_TrxName());	}

	/** Set C_Inquiry_ID.
		@param C_Inquiry_ID C_Inquiry_ID	  */
	public void setC_Inquiry_ID (int C_Inquiry_ID)
	{
		if (C_Inquiry_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_C_Inquiry_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_C_Inquiry_ID, Integer.valueOf(C_Inquiry_ID));
	}

	/** Get C_Inquiry_ID.
		@return C_Inquiry_ID	  */
	public int getC_Inquiry_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_Inquiry_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_C_InquiryLine getC_InquiryLine() throws RuntimeException
    {
		return (I_C_InquiryLine)MTable.get(getCtx(), I_C_InquiryLine.Table_Name)
			.getPO(getC_InquiryLine_ID(), get_TrxName());	}

	/** Set Inquiry Line.
		@param C_InquiryLine_ID Inquiry Line	  */
	public void setC_InquiryLine_ID (int C_InquiryLine_ID)
	{
		if (C_InquiryLine_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_C_InquiryLine_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_C_InquiryLine_ID, Integer.valueOf(C_InquiryLine_ID));
	}

	/** Get Inquiry Line.
		@return Inquiry Line	  */
	public int getC_InquiryLine_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_InquiryLine_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set ConvertStatus.
		@param ConvertStatus 
		Status of Inquiry Conversion (to RfP or to RfI)
	  */
	public void setConvertStatus (String ConvertStatus)
	{
		set_Value (COLUMNNAME_ConvertStatus, ConvertStatus);
	}

	/** Get ConvertStatus.
		@return Status of Inquiry Conversion (to RfP or to RfI)
	  */
	public String getConvertStatus () 
	{
		return (String)get_Value(COLUMNNAME_ConvertStatus);
	}

	public org.compiere.model.I_C_Order getC_Order() throws RuntimeException
    {
		return (org.compiere.model.I_C_Order)MTable.get(getCtx(), org.compiere.model.I_C_Order.Table_Name)
			.getPO(getC_Order_ID(), get_TrxName());	}

	/** Set Order.
		@param C_Order_ID 
		Order
	  */
	public void setC_Order_ID (int C_Order_ID)
	{
		if (C_Order_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_C_Order_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_C_Order_ID, Integer.valueOf(C_Order_ID));
	}

	/** Get Order.
		@return Order
	  */
	public int getC_Order_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_Order_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public org.compiere.model.I_C_OrderLine getC_OrderLine() throws RuntimeException
    {
		return (org.compiere.model.I_C_OrderLine)MTable.get(getCtx(), org.compiere.model.I_C_OrderLine.Table_Name)
			.getPO(getC_OrderLine_ID(), get_TrxName());	}

	/** Set Sales Order Line.
		@param C_OrderLine_ID 
		Sales Order Line
	  */
	public void setC_OrderLine_ID (int C_OrderLine_ID)
	{
		if (C_OrderLine_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_C_OrderLine_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_C_OrderLine_ID, Integer.valueOf(C_OrderLine_ID));
	}

	/** Get Sales Order Line.
		@return Sales Order Line
	  */
	public int getC_OrderLine_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_OrderLine_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public org.compiere.model.I_C_Payment getC_Payment() throws RuntimeException
    {
		return (org.compiere.model.I_C_Payment)MTable.get(getCtx(), org.compiere.model.I_C_Payment.Table_Name)
			.getPO(getC_Payment_ID(), get_TrxName());	}

	/** Set Payment.
		@param C_Payment_ID 
		Payment identifier
	  */
	public void setC_Payment_ID (int C_Payment_ID)
	{
		if (C_Payment_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_C_Payment_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_C_Payment_ID, Integer.valueOf(C_Payment_ID));
	}

	/** Get Payment.
		@return Payment identifier
	  */
	public int getC_Payment_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_Payment_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_C_Quotation getC_Quotation() throws RuntimeException
    {
		return (I_C_Quotation)MTable.get(getCtx(), I_C_Quotation.Table_Name)
			.getPO(getC_Quotation_ID(), get_TrxName());	}

	/** Set Quotation.
		@param C_Quotation_ID Quotation	  */
	public void setC_Quotation_ID (int C_Quotation_ID)
	{
		if (C_Quotation_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_C_Quotation_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_C_Quotation_ID, Integer.valueOf(C_Quotation_ID));
	}

	/** Get Quotation.
		@return Quotation	  */
	public int getC_Quotation_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_Quotation_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_C_QuotationLine getC_QuotationLine() throws RuntimeException
    {
		return (I_C_QuotationLine)MTable.get(getCtx(), I_C_QuotationLine.Table_Name)
			.getPO(getC_QuotationLine_ID(), get_TrxName());	}

	/** Set Quotation Line.
		@param C_QuotationLine_ID Quotation Line	  */
	public void setC_QuotationLine_ID (int C_QuotationLine_ID)
	{
		if (C_QuotationLine_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_C_QuotationLine_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_C_QuotationLine_ID, Integer.valueOf(C_QuotationLine_ID));
	}

	/** Get Quotation Line.
		@return Quotation Line	  */
	public int getC_QuotationLine_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_QuotationLine_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public org.compiere.model.I_C_RfQ getC_RfQ() throws RuntimeException
    {
		return (org.compiere.model.I_C_RfQ)MTable.get(getCtx(), org.compiere.model.I_C_RfQ.Table_Name)
			.getPO(getC_RfQ_ID(), get_TrxName());	}

	/** Set RfQ.
		@param C_RfQ_ID 
		Request for Quotation
	  */
	public void setC_RfQ_ID (int C_RfQ_ID)
	{
		if (C_RfQ_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_C_RfQ_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_C_RfQ_ID, Integer.valueOf(C_RfQ_ID));
	}

	/** Get RfQ.
		@return Request for Quotation
	  */
	public int getC_RfQ_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_RfQ_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public org.compiere.model.I_C_RfQLine getC_RfQLine() throws RuntimeException
    {
		return (org.compiere.model.I_C_RfQLine)MTable.get(getCtx(), org.compiere.model.I_C_RfQLine.Table_Name)
			.getPO(getC_RfQLine_ID(), get_TrxName());	}

	/** Set RfQ Line.
		@param C_RfQLine_ID 
		Request for Quotation Line
	  */
	public void setC_RfQLine_ID (int C_RfQLine_ID)
	{
		if (C_RfQLine_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_C_RfQLine_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_C_RfQLine_ID, Integer.valueOf(C_RfQLine_ID));
	}

	/** Get RfQ Line.
		@return Request for Quotation Line
	  */
	public int getC_RfQLine_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_RfQLine_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Transaction Date.
		@param DateTrx 
		Transaction Date
	  */
	public void setDateTrx (Timestamp DateTrx)
	{
		set_ValueNoCheck (COLUMNNAME_DateTrx, DateTrx);
	}

	/** Get Transaction Date.
		@return Transaction Date
	  */
	public Timestamp getDateTrx () 
	{
		return (Timestamp)get_Value(COLUMNNAME_DateTrx);
	}

	public org.eevolution.model.I_DD_Order getDD_Order() throws RuntimeException
    {
		return (org.eevolution.model.I_DD_Order)MTable.get(getCtx(), org.eevolution.model.I_DD_Order.Table_Name)
			.getPO(getDD_Order_ID(), get_TrxName());	}

	/** Set Distribution Order.
		@param DD_Order_ID Distribution Order	  */
	public void setDD_Order_ID (int DD_Order_ID)
	{
		if (DD_Order_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_DD_Order_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_DD_Order_ID, Integer.valueOf(DD_Order_ID));
	}

	/** Get Distribution Order.
		@return Distribution Order	  */
	public int getDD_Order_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_DD_Order_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public org.eevolution.model.I_DD_OrderLine getDD_OrderLine() throws RuntimeException
    {
		return (org.eevolution.model.I_DD_OrderLine)MTable.get(getCtx(), org.eevolution.model.I_DD_OrderLine.Table_Name)
			.getPO(getDD_OrderLine_ID(), get_TrxName());	}

	/** Set Distribution Order Line.
		@param DD_OrderLine_ID Distribution Order Line	  */
	public void setDD_OrderLine_ID (int DD_OrderLine_ID)
	{
		if (DD_OrderLine_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_DD_OrderLine_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_DD_OrderLine_ID, Integer.valueOf(DD_OrderLine_ID));
	}

	/** Get Distribution Order Line.
		@return Distribution Order Line	  */
	public int getDD_OrderLine_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_DD_OrderLine_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Match Request.
		@param M_MatchRequest_ID Match Request	  */
	public void setM_MatchRequest_ID (int M_MatchRequest_ID)
	{
		if (M_MatchRequest_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_M_MatchRequest_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_M_MatchRequest_ID, Integer.valueOf(M_MatchRequest_ID));
	}

	/** Get Match Request.
		@return Match Request	  */
	public int getM_MatchRequest_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_M_MatchRequest_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

    /** Get Record ID/ColumnName
        @return ID/ColumnName pair
      */
    public KeyNamePair getKeyNamePair() 
    {
        return new KeyNamePair(get_ID(), String.valueOf(getM_MatchRequest_ID()));
    }

	/** Set M_MatchRequest_UU.
		@param M_MatchRequest_UU M_MatchRequest_UU	  */
	public void setM_MatchRequest_UU (String M_MatchRequest_UU)
	{
		set_Value (COLUMNNAME_M_MatchRequest_UU, M_MatchRequest_UU);
	}

	/** Get M_MatchRequest_UU.
		@return M_MatchRequest_UU	  */
	public String getM_MatchRequest_UU () 
	{
		return (String)get_Value(COLUMNNAME_M_MatchRequest_UU);
	}

	public org.compiere.model.I_M_Movement getM_Movement() throws RuntimeException
    {
		return (org.compiere.model.I_M_Movement)MTable.get(getCtx(), org.compiere.model.I_M_Movement.Table_Name)
			.getPO(getM_Movement_ID(), get_TrxName());	}

	/** Set Inventory Move.
		@param M_Movement_ID 
		Movement of Inventory
	  */
	public void setM_Movement_ID (int M_Movement_ID)
	{
		if (M_Movement_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_M_Movement_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_M_Movement_ID, Integer.valueOf(M_Movement_ID));
	}

	/** Get Inventory Move.
		@return Movement of Inventory
	  */
	public int getM_Movement_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_M_Movement_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public org.compiere.model.I_M_MovementLine getM_MovementLine() throws RuntimeException
    {
		return (org.compiere.model.I_M_MovementLine)MTable.get(getCtx(), org.compiere.model.I_M_MovementLine.Table_Name)
			.getPO(getM_MovementLine_ID(), get_TrxName());	}

	/** Set Move Line.
		@param M_MovementLine_ID 
		Inventory Move document Line
	  */
	public void setM_MovementLine_ID (int M_MovementLine_ID)
	{
		if (M_MovementLine_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_M_MovementLine_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_M_MovementLine_ID, Integer.valueOf(M_MovementLine_ID));
	}

	/** Get Move Line.
		@return Inventory Move document Line
	  */
	public int getM_MovementLine_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_M_MovementLine_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public org.compiere.model.I_M_Requisition getM_Requisition() throws RuntimeException
    {
		return (org.compiere.model.I_M_Requisition)MTable.get(getCtx(), org.compiere.model.I_M_Requisition.Table_Name)
			.getPO(getM_Requisition_ID(), get_TrxName());	}

	/** Set Requisition.
		@param M_Requisition_ID 
		Material Requisition
	  */
	public void setM_Requisition_ID (int M_Requisition_ID)
	{
		if (M_Requisition_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_M_Requisition_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_M_Requisition_ID, Integer.valueOf(M_Requisition_ID));
	}

	/** Get Requisition.
		@return Material Requisition
	  */
	public int getM_Requisition_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_M_Requisition_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public org.compiere.model.I_M_RequisitionLine getM_RequisitionLine() throws RuntimeException
    {
		return (org.compiere.model.I_M_RequisitionLine)MTable.get(getCtx(), org.compiere.model.I_M_RequisitionLine.Table_Name)
			.getPO(getM_RequisitionLine_ID(), get_TrxName());	}

	/** Set Requisition Line.
		@param M_RequisitionLine_ID 
		Material Requisition Line
	  */
	public void setM_RequisitionLine_ID (int M_RequisitionLine_ID)
	{
		if (M_RequisitionLine_ID < 1) 
			set_Value (COLUMNNAME_M_RequisitionLine_ID, null);
		else 
			set_Value (COLUMNNAME_M_RequisitionLine_ID, Integer.valueOf(M_RequisitionLine_ID));
	}

	/** Get Requisition Line.
		@return Material Requisition Line
	  */
	public int getM_RequisitionLine_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_M_RequisitionLine_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Processed.
		@param Processed 
		The document has been processed
	  */
	public void setProcessed (boolean Processed)
	{
		set_Value (COLUMNNAME_Processed, Boolean.valueOf(Processed));
	}

	/** Get Processed.
		@return The document has been processed
	  */
	public boolean isProcessed () 
	{
		Object oo = get_Value(COLUMNNAME_Processed);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set Ordered Quantity.
		@param QtyOrdered 
		Ordered Quantity
	  */
	public void setQtyOrdered (BigDecimal QtyOrdered)
	{
		set_ValueNoCheck (COLUMNNAME_QtyOrdered, QtyOrdered);
	}

	/** Get Ordered Quantity.
		@return Ordered Quantity
	  */
	public BigDecimal getQtyOrdered () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_QtyOrdered);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Request Amount.
		@param RequestAmt 
		Amount associated with this request
	  */
	public void setRequestAmt (BigDecimal RequestAmt)
	{
		set_Value (COLUMNNAME_RequestAmt, RequestAmt);
	}

	/** Get Request Amount.
		@return Amount associated with this request
	  */
	public BigDecimal getRequestAmt () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_RequestAmt);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	public org.compiere.model.I_R_Request getR_Request() throws RuntimeException
    {
		return (org.compiere.model.I_R_Request)MTable.get(getCtx(), org.compiere.model.I_R_Request.Table_Name)
			.getPO(getR_Request_ID(), get_TrxName());	}

	/** Set Request.
		@param R_Request_ID 
		Request from a Business Partner or Prospect
	  */
	public void setR_Request_ID (int R_Request_ID)
	{
		if (R_Request_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_R_Request_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_R_Request_ID, Integer.valueOf(R_Request_ID));
	}

	/** Get Request.
		@return Request from a Business Partner or Prospect
	  */
	public int getR_Request_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_R_Request_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_TCS_R_RequestLine getR_RequestLine() throws RuntimeException
    {
		return (I_TCS_R_RequestLine)MTable.get(getCtx(), I_TCS_R_RequestLine.Table_Name)
			.getPO(getR_RequestLine_ID(), get_TrxName());	}

	/** Set Request Line.
		@param R_RequestLine_ID Request Line	  */
	public void setR_RequestLine_ID (int R_RequestLine_ID)
	{
		if (R_RequestLine_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_R_RequestLine_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_R_RequestLine_ID, Integer.valueOf(R_RequestLine_ID));
	}

	/** Get Request Line.
		@return Request Line	  */
	public int getR_RequestLine_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_R_RequestLine_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}
}