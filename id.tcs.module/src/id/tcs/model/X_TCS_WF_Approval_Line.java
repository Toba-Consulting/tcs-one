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
import java.util.Properties;
import org.compiere.model.*;
import org.compiere.util.Env;

/** Generated Model for TCS_WF_Approval_Line
 *  @author iDempiere (generated) 
 *  @version Release 5.1 - $Id$ */
public class X_TCS_WF_Approval_Line extends PO implements I_TCS_WF_Approval_Line, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20181031L;

    /** Standard Constructor */
    public X_TCS_WF_Approval_Line (Properties ctx, int TCS_WF_Approval_Line_ID, String trxName)
    {
      super (ctx, TCS_WF_Approval_Line_ID, trxName);
      /** if (TCS_WF_Approval_Line_ID == 0)
        {
			setApprovalAmt (Env.ZERO);
			setC_Currency_ID (0);
			setDelegateUser_ID (0);
			setSequence (Env.ZERO);
        } */
    }

    /** Load Constructor */
    public X_TCS_WF_Approval_Line (Properties ctx, ResultSet rs, String trxName)
    {
      super (ctx, rs, trxName);
    }

    /** AccessLevel
      * @return 4 - System 
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
      StringBuffer sb = new StringBuffer ("X_TCS_WF_Approval_Line[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	public org.compiere.model.I_AD_User getAD_User() throws RuntimeException
    {
		return (org.compiere.model.I_AD_User)MTable.get(getCtx(), org.compiere.model.I_AD_User.Table_Name)
			.getPO(getAD_User_ID(), get_TrxName());	}

	/** Set User/Contact.
		@param AD_User_ID 
		User within the system - Internal or Business Partner Contact
	  */
	public void setAD_User_ID (int AD_User_ID)
	{
		if (AD_User_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_AD_User_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_AD_User_ID, Integer.valueOf(AD_User_ID));
	}

	/** Get User/Contact.
		@return User within the system - Internal or Business Partner Contact
	  */
	public int getAD_User_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AD_User_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Approval Amount.
		@param ApprovalAmt 
		Document Approval Amount
	  */
	public void setApprovalAmt (BigDecimal ApprovalAmt)
	{
		set_Value (COLUMNNAME_ApprovalAmt, ApprovalAmt);
	}

	/** Get Approval Amount.
		@return Document Approval Amount
	  */
	public BigDecimal getApprovalAmt () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_ApprovalAmt);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	public org.compiere.model.I_C_Currency getC_Currency() throws RuntimeException
    {
		return (org.compiere.model.I_C_Currency)MTable.get(getCtx(), org.compiere.model.I_C_Currency.Table_Name)
			.getPO(getC_Currency_ID(), get_TrxName());	}

	/** Set Currency.
		@param C_Currency_ID 
		The Currency for this record
	  */
	public void setC_Currency_ID (int C_Currency_ID)
	{
		if (C_Currency_ID < 1) 
			set_Value (COLUMNNAME_C_Currency_ID, null);
		else 
			set_Value (COLUMNNAME_C_Currency_ID, Integer.valueOf(C_Currency_ID));
	}

	/** Get Currency.
		@return The Currency for this record
	  */
	public int getC_Currency_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_Currency_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public org.compiere.model.I_AD_User getDelegateUser() throws RuntimeException
    {
		return (org.compiere.model.I_AD_User)MTable.get(getCtx(), org.compiere.model.I_AD_User.Table_Name)
			.getPO(getDelegateUser_ID(), get_TrxName());	}

	/** Set Delegate User.
		@param DelegateUser_ID Delegate User	  */
	public void setDelegateUser_ID (int DelegateUser_ID)
	{
		if (DelegateUser_ID < 1) 
			set_Value (COLUMNNAME_DelegateUser_ID, null);
		else 
			set_Value (COLUMNNAME_DelegateUser_ID, Integer.valueOf(DelegateUser_ID));
	}

	/** Get Delegate User.
		@return Delegate User	  */
	public int getDelegateUser_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_DelegateUser_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Description.
		@param Description 
		Optional short description of the record
	  */
	public void setDescription (String Description)
	{
		set_Value (COLUMNNAME_Description, Description);
	}

	/** Get Description.
		@return Optional short description of the record
	  */
	public String getDescription () 
	{
		return (String)get_Value(COLUMNNAME_Description);
	}

	/** Set Sequence.
		@param Sequence Sequence	  */
	public void setSequence (BigDecimal Sequence)
	{
		set_Value (COLUMNNAME_Sequence, Sequence);
	}

	/** Get Sequence.
		@return Sequence	  */
	public BigDecimal getSequence () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_Sequence);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	public I_TCS_WF_Approval_Header getTCS_WF_Approval_Header() throws RuntimeException
    {
		return (I_TCS_WF_Approval_Header)MTable.get(getCtx(), I_TCS_WF_Approval_Header.Table_Name)
			.getPO(getTCS_WF_Approval_Header_ID(), get_TrxName());	}

	/** Set TCS_WF_Approval_Header.
		@param TCS_WF_Approval_Header_ID TCS_WF_Approval_Header	  */
	public void setTCS_WF_Approval_Header_ID (int TCS_WF_Approval_Header_ID)
	{
		if (TCS_WF_Approval_Header_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_TCS_WF_Approval_Header_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_TCS_WF_Approval_Header_ID, Integer.valueOf(TCS_WF_Approval_Header_ID));
	}

	/** Get TCS_WF_Approval_Header.
		@return TCS_WF_Approval_Header	  */
	public int getTCS_WF_Approval_Header_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_TCS_WF_Approval_Header_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set TCS_WF_Approval_Line.
		@param TCS_WF_Approval_Line_ID TCS_WF_Approval_Line	  */
	public void setTCS_WF_Approval_Line_ID (int TCS_WF_Approval_Line_ID)
	{
		if (TCS_WF_Approval_Line_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_TCS_WF_Approval_Line_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_TCS_WF_Approval_Line_ID, Integer.valueOf(TCS_WF_Approval_Line_ID));
	}

	/** Get TCS_WF_Approval_Line.
		@return TCS_WF_Approval_Line	  */
	public int getTCS_WF_Approval_Line_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_TCS_WF_Approval_Line_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set TCS_WF_Approval_Line_UU.
		@param TCS_WF_Approval_Line_UU TCS_WF_Approval_Line_UU	  */
	public void setTCS_WF_Approval_Line_UU (String TCS_WF_Approval_Line_UU)
	{
		set_ValueNoCheck (COLUMNNAME_TCS_WF_Approval_Line_UU, TCS_WF_Approval_Line_UU);
	}

	/** Get TCS_WF_Approval_Line_UU.
		@return TCS_WF_Approval_Line_UU	  */
	public String getTCS_WF_Approval_Line_UU () 
	{
		return (String)get_Value(COLUMNNAME_TCS_WF_Approval_Line_UU);
	}
}