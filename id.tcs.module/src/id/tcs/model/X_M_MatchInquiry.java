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
import org.compiere.model.*;
import org.compiere.util.Env;
import org.compiere.util.KeyNamePair;

/** Generated Model for M_MatchInquiry
 *  @author iDempiere (generated) 
 *  @version Release 2.1 - $Id$ */
public class X_M_MatchInquiry extends PO implements I_M_MatchInquiry, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20150814L;

    /** Standard Constructor */
    public X_M_MatchInquiry (Properties ctx, int M_MatchInquiry_ID, String trxName)
    {
      super (ctx, M_MatchInquiry_ID, trxName);
      /** if (M_MatchInquiry_ID == 0)
        {
			setM_MatchInquiry_ID (0);
        } */
    }

    /** Load Constructor */
    public X_M_MatchInquiry (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_M_MatchInquiry[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	public I_C_Inquiry getC_Inquiry() throws RuntimeException
    {
		return (I_C_Inquiry)MTable.get(getCtx(), I_C_Inquiry.Table_Name)
			.getPO(getC_Inquiry_ID(), get_TrxName());	}

	/** Set Inquiry.
		@param C_Inquiry_ID Inquiry	  */
	public void setC_Inquiry_ID (int C_Inquiry_ID)
	{
		if (C_Inquiry_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_C_Inquiry_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_C_Inquiry_ID, Integer.valueOf(C_Inquiry_ID));
	}

	/** Get Inquiry.
		@return Inquiry	  */
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

	/** Set M_MatchInquiry.
		@param M_MatchInquiry_ID M_MatchInquiry	  */
	public void setM_MatchInquiry_ID (int M_MatchInquiry_ID)
	{
		if (M_MatchInquiry_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_M_MatchInquiry_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_M_MatchInquiry_ID, Integer.valueOf(M_MatchInquiry_ID));
	}

	/** Get M_MatchInquiry.
		@return M_MatchInquiry	  */
	public int getM_MatchInquiry_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_M_MatchInquiry_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

    /** Get Record ID/ColumnName
        @return ID/ColumnName pair
      */
    public KeyNamePair getKeyNamePair() 
    {
        return new KeyNamePair(get_ID(), String.valueOf(getM_MatchInquiry_ID()));
    }

	/** Set M_MatchInquiry_UU.
		@param M_MatchInquiry_UU M_MatchInquiry_UU	  */
	public void setM_MatchInquiry_UU (String M_MatchInquiry_UU)
	{
		set_Value (COLUMNNAME_M_MatchInquiry_UU, M_MatchInquiry_UU);
	}

	/** Get M_MatchInquiry_UU.
		@return M_MatchInquiry_UU	  */
	public String getM_MatchInquiry_UU () 
	{
		return (String)get_Value(COLUMNNAME_M_MatchInquiry_UU);
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

	/** Set Quantity.
		@param Qty 
		Quantity
	  */
	public void setQty (BigDecimal Qty)
	{
		set_Value (COLUMNNAME_Qty, Qty);
	}

	/** Get Quantity.
		@return Quantity
	  */
	public BigDecimal getQty () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_Qty);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}
}