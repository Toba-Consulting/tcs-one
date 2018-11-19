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

/** Generated Model for T_MatchAllocation
 *  @author iDempiere (generated) 
 *  @version Release 5.1 - $Id$ */
public class X_T_MatchAllocation extends PO implements I_T_MatchAllocation, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20181116L;

    /** Standard Constructor */
    public X_T_MatchAllocation (Properties ctx, int T_MatchAllocation_ID, String trxName)
    {
      super (ctx, T_MatchAllocation_ID, trxName);
      /** if (T_MatchAllocation_ID == 0)
        {
			setDateAllocated (new Timestamp( System.currentTimeMillis() ));
			setT_MatchAllocation_ID (0);
        } */
    }

    /** Load Constructor */
    public X_T_MatchAllocation (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_T_MatchAllocation[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	/** Set AllocationAmt.
		@param AllocationAmt AllocationAmt	  */
	public void setAllocationAmt (BigDecimal AllocationAmt)
	{
		set_Value (COLUMNNAME_AllocationAmt, AllocationAmt);
	}

	/** Get AllocationAmt.
		@return AllocationAmt	  */
	public BigDecimal getAllocationAmt () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_AllocationAmt);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	public org.compiere.model.I_C_AllocationHdr getC_AllocationHdr() throws RuntimeException
    {
		return (org.compiere.model.I_C_AllocationHdr)MTable.get(getCtx(), org.compiere.model.I_C_AllocationHdr.Table_Name)
			.getPO(getC_AllocationHdr_ID(), get_TrxName());	}

	/** Set Allocation.
		@param C_AllocationHdr_ID 
		Payment allocation
	  */
	public void setC_AllocationHdr_ID (int C_AllocationHdr_ID)
	{
		if (C_AllocationHdr_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_C_AllocationHdr_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_C_AllocationHdr_ID, Integer.valueOf(C_AllocationHdr_ID));
	}

	/** Get Allocation.
		@return Payment allocation
	  */
	public int getC_AllocationHdr_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_AllocationHdr_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public org.compiere.model.I_C_Charge getC_Charge() throws RuntimeException
    {
		return (org.compiere.model.I_C_Charge)MTable.get(getCtx(), org.compiere.model.I_C_Charge.Table_Name)
			.getPO(getC_Charge_ID(), get_TrxName());	}

	/** Set Charge.
		@param C_Charge_ID 
		Additional document charges
	  */
	public void setC_Charge_ID (int C_Charge_ID)
	{
		if (C_Charge_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_C_Charge_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_C_Charge_ID, Integer.valueOf(C_Charge_ID));
	}

	/** Get Charge.
		@return Additional document charges
	  */
	public int getC_Charge_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_Charge_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public org.compiere.model.I_C_DocType getC_DocType() throws RuntimeException
    {
		return (org.compiere.model.I_C_DocType)MTable.get(getCtx(), org.compiere.model.I_C_DocType.Table_Name)
			.getPO(getC_DocType_ID(), get_TrxName());	}

	/** Set Document Type.
		@param C_DocType_ID 
		Document type or rules
	  */
	public void setC_DocType_ID (int C_DocType_ID)
	{
		if (C_DocType_ID < 0) 
			set_ValueNoCheck (COLUMNNAME_C_DocType_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_C_DocType_ID, Integer.valueOf(C_DocType_ID));
	}

	/** Get Document Type.
		@return Document type or rules
	  */
	public int getC_DocType_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_DocType_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public org.compiere.model.I_C_Invoice getC_Invoice() throws RuntimeException
    {
		return (org.compiere.model.I_C_Invoice)MTable.get(getCtx(), org.compiere.model.I_C_Invoice.Table_Name)
			.getPO(getC_Invoice_ID(), get_TrxName());	}

	/** Set Invoice.
		@param C_Invoice_ID 
		Invoice Identifier
	  */
	public void setC_Invoice_ID (int C_Invoice_ID)
	{
		if (C_Invoice_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_C_Invoice_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_C_Invoice_ID, Integer.valueOf(C_Invoice_ID));
	}

	/** Get Invoice.
		@return Invoice Identifier
	  */
	public int getC_Invoice_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_Invoice_ID);
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

	/** Set DateAllocated.
		@param DateAllocated DateAllocated	  */
	public void setDateAllocated (Timestamp DateAllocated)
	{
		set_Value (COLUMNNAME_DateAllocated, DateAllocated);
	}

	/** Get DateAllocated.
		@return DateAllocated	  */
	public Timestamp getDateAllocated () 
	{
		return (Timestamp)get_Value(COLUMNNAME_DateAllocated);
	}

	/** Set Discount Amount.
		@param DiscountAmt 
		Calculated amount of discount
	  */
	public void setDiscountAmt (BigDecimal DiscountAmt)
	{
		set_ValueNoCheck (COLUMNNAME_DiscountAmt, DiscountAmt);
	}

	/** Get Discount Amount.
		@return Calculated amount of discount
	  */
	public BigDecimal getDiscountAmt () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_DiscountAmt);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Match_DiscountAmt.
		@param Match_DiscountAmt Match_DiscountAmt	  */
	public void setMatch_DiscountAmt (BigDecimal Match_DiscountAmt)
	{
		set_Value (COLUMNNAME_Match_DiscountAmt, Match_DiscountAmt);
	}

	/** Get Match_DiscountAmt.
		@return Match_DiscountAmt	  */
	public BigDecimal getMatch_DiscountAmt () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_Match_DiscountAmt);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	public org.compiere.model.I_C_DocType getMatch_DocType() throws RuntimeException
    {
		return (org.compiere.model.I_C_DocType)MTable.get(getCtx(), org.compiere.model.I_C_DocType.Table_Name)
			.getPO(getMatch_DocType_ID(), get_TrxName());	}

	/** Set Match_DocType_ID.
		@param Match_DocType_ID Match_DocType_ID	  */
	public void setMatch_DocType_ID (int Match_DocType_ID)
	{
		if (Match_DocType_ID < 1) 
			set_Value (COLUMNNAME_Match_DocType_ID, null);
		else 
			set_Value (COLUMNNAME_Match_DocType_ID, Integer.valueOf(Match_DocType_ID));
	}

	/** Get Match_DocType_ID.
		@return Match_DocType_ID	  */
	public int getMatch_DocType_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_Match_DocType_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public org.compiere.model.I_C_Invoice getMatch_Invoice() throws RuntimeException
    {
		return (org.compiere.model.I_C_Invoice)MTable.get(getCtx(), org.compiere.model.I_C_Invoice.Table_Name)
			.getPO(getMatch_Invoice_ID(), get_TrxName());	}

	/** Set Match_Invoice_ID.
		@param Match_Invoice_ID Match_Invoice_ID	  */
	public void setMatch_Invoice_ID (int Match_Invoice_ID)
	{
		if (Match_Invoice_ID < 1) 
			set_Value (COLUMNNAME_Match_Invoice_ID, null);
		else 
			set_Value (COLUMNNAME_Match_Invoice_ID, Integer.valueOf(Match_Invoice_ID));
	}

	/** Get Match_Invoice_ID.
		@return Match_Invoice_ID	  */
	public int getMatch_Invoice_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_Match_Invoice_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public org.compiere.model.I_C_Payment getMatch_Payment() throws RuntimeException
    {
		return (org.compiere.model.I_C_Payment)MTable.get(getCtx(), org.compiere.model.I_C_Payment.Table_Name)
			.getPO(getMatch_Payment_ID(), get_TrxName());	}

	/** Set Match_Payment_ID.
		@param Match_Payment_ID Match_Payment_ID	  */
	public void setMatch_Payment_ID (int Match_Payment_ID)
	{
		if (Match_Payment_ID < 1) 
			set_Value (COLUMNNAME_Match_Payment_ID, null);
		else 
			set_Value (COLUMNNAME_Match_Payment_ID, Integer.valueOf(Match_Payment_ID));
	}

	/** Get Match_Payment_ID.
		@return Match_Payment_ID	  */
	public int getMatch_Payment_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_Match_Payment_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Match_WriteOffAmt.
		@param Match_WriteOffAmt Match_WriteOffAmt	  */
	public void setMatch_WriteOffAmt (BigDecimal Match_WriteOffAmt)
	{
		set_Value (COLUMNNAME_Match_WriteOffAmt, Match_WriteOffAmt);
	}

	/** Get Match_WriteOffAmt.
		@return Match_WriteOffAmt	  */
	public BigDecimal getMatch_WriteOffAmt () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_Match_WriteOffAmt);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set N_Amount.
		@param N_Amount N_Amount	  */
	public void setN_Amount (BigDecimal N_Amount)
	{
		set_Value (COLUMNNAME_N_Amount, N_Amount);
	}

	/** Get N_Amount.
		@return N_Amount	  */
	public BigDecimal getN_Amount () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_N_Amount);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set N_DiscountAmt.
		@param N_DiscountAmt N_DiscountAmt	  */
	public void setN_DiscountAmt (BigDecimal N_DiscountAmt)
	{
		set_Value (COLUMNNAME_N_DiscountAmt, N_DiscountAmt);
	}

	/** Get N_DiscountAmt.
		@return N_DiscountAmt	  */
	public BigDecimal getN_DiscountAmt () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_N_DiscountAmt);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	public org.compiere.model.I_C_DocType getN_DocType() throws RuntimeException
    {
		return (org.compiere.model.I_C_DocType)MTable.get(getCtx(), org.compiere.model.I_C_DocType.Table_Name)
			.getPO(getN_DocType_ID(), get_TrxName());	}

	/** Set N_DocType_ID.
		@param N_DocType_ID N_DocType_ID	  */
	public void setN_DocType_ID (int N_DocType_ID)
	{
		if (N_DocType_ID < 1) 
			set_Value (COLUMNNAME_N_DocType_ID, null);
		else 
			set_Value (COLUMNNAME_N_DocType_ID, Integer.valueOf(N_DocType_ID));
	}

	/** Get N_DocType_ID.
		@return N_DocType_ID	  */
	public int getN_DocType_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_N_DocType_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public org.compiere.model.I_C_Invoice getN_Invoice() throws RuntimeException
    {
		return (org.compiere.model.I_C_Invoice)MTable.get(getCtx(), org.compiere.model.I_C_Invoice.Table_Name)
			.getPO(getN_Invoice_ID(), get_TrxName());	}

	/** Set N_Invoice_ID.
		@param N_Invoice_ID N_Invoice_ID	  */
	public void setN_Invoice_ID (int N_Invoice_ID)
	{
		if (N_Invoice_ID < 1) 
			set_Value (COLUMNNAME_N_Invoice_ID, null);
		else 
			set_Value (COLUMNNAME_N_Invoice_ID, Integer.valueOf(N_Invoice_ID));
	}

	/** Get N_Invoice_ID.
		@return N_Invoice_ID	  */
	public int getN_Invoice_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_N_Invoice_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public org.compiere.model.I_C_Payment getN_Payment() throws RuntimeException
    {
		return (org.compiere.model.I_C_Payment)MTable.get(getCtx(), org.compiere.model.I_C_Payment.Table_Name)
			.getPO(getN_Payment_ID(), get_TrxName());	}

	/** Set N_Payment_ID.
		@param N_Payment_ID N_Payment_ID	  */
	public void setN_Payment_ID (int N_Payment_ID)
	{
		if (N_Payment_ID < 1) 
			set_Value (COLUMNNAME_N_Payment_ID, null);
		else 
			set_Value (COLUMNNAME_N_Payment_ID, Integer.valueOf(N_Payment_ID));
	}

	/** Get N_Payment_ID.
		@return N_Payment_ID	  */
	public int getN_Payment_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_N_Payment_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set N_WriteOffAmt.
		@param N_WriteOffAmt N_WriteOffAmt	  */
	public void setN_WriteOffAmt (BigDecimal N_WriteOffAmt)
	{
		set_Value (COLUMNNAME_N_WriteOffAmt, N_WriteOffAmt);
	}

	/** Get N_WriteOffAmt.
		@return N_WriteOffAmt	  */
	public BigDecimal getN_WriteOffAmt () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_N_WriteOffAmt);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set P_Amount.
		@param P_Amount P_Amount	  */
	public void setP_Amount (BigDecimal P_Amount)
	{
		set_Value (COLUMNNAME_P_Amount, P_Amount);
	}

	/** Get P_Amount.
		@return P_Amount	  */
	public BigDecimal getP_Amount () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_P_Amount);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set P_DiscountAmt.
		@param P_DiscountAmt P_DiscountAmt	  */
	public void setP_DiscountAmt (BigDecimal P_DiscountAmt)
	{
		set_Value (COLUMNNAME_P_DiscountAmt, P_DiscountAmt);
	}

	/** Get P_DiscountAmt.
		@return P_DiscountAmt	  */
	public BigDecimal getP_DiscountAmt () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_P_DiscountAmt);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	public org.compiere.model.I_C_DocType getP_DocType() throws RuntimeException
    {
		return (org.compiere.model.I_C_DocType)MTable.get(getCtx(), org.compiere.model.I_C_DocType.Table_Name)
			.getPO(getP_DocType_ID(), get_TrxName());	}

	/** Set P_DocType_ID.
		@param P_DocType_ID P_DocType_ID	  */
	public void setP_DocType_ID (int P_DocType_ID)
	{
		if (P_DocType_ID < 1) 
			set_Value (COLUMNNAME_P_DocType_ID, null);
		else 
			set_Value (COLUMNNAME_P_DocType_ID, Integer.valueOf(P_DocType_ID));
	}

	/** Get P_DocType_ID.
		@return P_DocType_ID	  */
	public int getP_DocType_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_P_DocType_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public org.compiere.model.I_C_Invoice getP_Invoice() throws RuntimeException
    {
		return (org.compiere.model.I_C_Invoice)MTable.get(getCtx(), org.compiere.model.I_C_Invoice.Table_Name)
			.getPO(getP_Invoice_ID(), get_TrxName());	}

	/** Set P_Invoice_ID.
		@param P_Invoice_ID P_Invoice_ID	  */
	public void setP_Invoice_ID (int P_Invoice_ID)
	{
		if (P_Invoice_ID < 1) 
			set_Value (COLUMNNAME_P_Invoice_ID, null);
		else 
			set_Value (COLUMNNAME_P_Invoice_ID, Integer.valueOf(P_Invoice_ID));
	}

	/** Get P_Invoice_ID.
		@return P_Invoice_ID	  */
	public int getP_Invoice_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_P_Invoice_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public org.compiere.model.I_C_Payment getP_Payment() throws RuntimeException
    {
		return (org.compiere.model.I_C_Payment)MTable.get(getCtx(), org.compiere.model.I_C_Payment.Table_Name)
			.getPO(getP_Payment_ID(), get_TrxName());	}

	/** Set P_Payment_ID.
		@param P_Payment_ID P_Payment_ID	  */
	public void setP_Payment_ID (int P_Payment_ID)
	{
		if (P_Payment_ID < 1) 
			set_Value (COLUMNNAME_P_Payment_ID, null);
		else 
			set_Value (COLUMNNAME_P_Payment_ID, Integer.valueOf(P_Payment_ID));
	}

	/** Get P_Payment_ID.
		@return P_Payment_ID	  */
	public int getP_Payment_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_P_Payment_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set P_WriteOffAmt.
		@param P_WriteOffAmt P_WriteOffAmt	  */
	public void setP_WriteOffAmt (BigDecimal P_WriteOffAmt)
	{
		set_Value (COLUMNNAME_P_WriteOffAmt, P_WriteOffAmt);
	}

	/** Get P_WriteOffAmt.
		@return P_WriteOffAmt	  */
	public BigDecimal getP_WriteOffAmt () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_P_WriteOffAmt);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set T_MatchAllocation_ID.
		@param T_MatchAllocation_ID T_MatchAllocation_ID	  */
	public void setT_MatchAllocation_ID (int T_MatchAllocation_ID)
	{
		if (T_MatchAllocation_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_T_MatchAllocation_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_T_MatchAllocation_ID, Integer.valueOf(T_MatchAllocation_ID));
	}

	/** Get T_MatchAllocation_ID.
		@return T_MatchAllocation_ID	  */
	public int getT_MatchAllocation_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_T_MatchAllocation_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

    /** Get Record ID/ColumnName
        @return ID/ColumnName pair
      */
    public KeyNamePair getKeyNamePair() 
    {
        return new KeyNamePair(get_ID(), String.valueOf(getT_MatchAllocation_ID()));
    }

	/** Set T_MatchAllocation_UU.
		@param T_MatchAllocation_UU T_MatchAllocation_UU	  */
	public void setT_MatchAllocation_UU (String T_MatchAllocation_UU)
	{
		set_ValueNoCheck (COLUMNNAME_T_MatchAllocation_UU, T_MatchAllocation_UU);
	}

	/** Get T_MatchAllocation_UU.
		@return T_MatchAllocation_UU	  */
	public String getT_MatchAllocation_UU () 
	{
		return (String)get_Value(COLUMNNAME_T_MatchAllocation_UU);
	}

	/** Set Write-off Amount.
		@param WriteOffAmt 
		Amount to write-off
	  */
	public void setWriteOffAmt (BigDecimal WriteOffAmt)
	{
		set_ValueNoCheck (COLUMNNAME_WriteOffAmt, WriteOffAmt);
	}

	/** Get Write-off Amount.
		@return Amount to write-off
	  */
	public BigDecimal getWriteOffAmt () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_WriteOffAmt);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}
}