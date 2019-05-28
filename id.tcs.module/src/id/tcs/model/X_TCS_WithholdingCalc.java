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

/** Generated Model for TCS_WithholdingCalc
 *  @author iDempiere (generated) 
 *  @version Release 5.1 - $Id$ */
public class X_TCS_WithholdingCalc extends PO implements I_TCS_WithholdingCalc, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20190527L;

    /** Standard Constructor */
    public X_TCS_WithholdingCalc (Properties ctx, int TCS_WithholdingCalc_ID, String trxName)
    {
      super (ctx, TCS_WithholdingCalc_ID, trxName);
      /** if (TCS_WithholdingCalc_ID == 0)
        {
			setTCS_WithholdingCalc_ID (0);
        } */
    }

    /** Load Constructor */
    public X_TCS_WithholdingCalc (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_TCS_WithholdingCalc[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	/** Set Accumulated Amount.
		@param AccumulatedAmt Accumulated Amount	  */
	public void setAccumulatedAmt (BigDecimal AccumulatedAmt)
	{
		set_Value (COLUMNNAME_AccumulatedAmt, AccumulatedAmt);
	}

	/** Get Accumulated Amount.
		@return Accumulated Amount	  */
	public BigDecimal getAccumulatedAmt () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_AccumulatedAmt);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Amount.
		@param Amt 
		Amount
	  */
	public void setAmt (BigDecimal Amt)
	{
		set_Value (COLUMNNAME_Amt, Amt);
	}

	/** Get Amount.
		@return Amount
	  */
	public BigDecimal getAmt () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_Amt);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	public org.compiere.model.I_C_BPartner getC_BPartner() throws RuntimeException
    {
		return (org.compiere.model.I_C_BPartner)MTable.get(getCtx(), org.compiere.model.I_C_BPartner.Table_Name)
			.getPO(getC_BPartner_ID(), get_TrxName());	}

	/** Set Business Partner .
		@param C_BPartner_ID 
		Identifies a Business Partner
	  */
	public void setC_BPartner_ID (int C_BPartner_ID)
	{
		if (C_BPartner_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_C_BPartner_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_C_BPartner_ID, Integer.valueOf(C_BPartner_ID));
	}

	/** Get Business Partner .
		@return Identifies a Business Partner
	  */
	public int getC_BPartner_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_BPartner_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public org.compiere.model.I_C_Period getC_Period() throws RuntimeException
    {
		return (org.compiere.model.I_C_Period)MTable.get(getCtx(), org.compiere.model.I_C_Period.Table_Name)
			.getPO(getC_Period_ID(), get_TrxName());	}

	/** Set Period.
		@param C_Period_ID 
		Period of the Calendar
	  */
	public void setC_Period_ID (int C_Period_ID)
	{
		if (C_Period_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_C_Period_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_C_Period_ID, Integer.valueOf(C_Period_ID));
	}

	/** Get Period.
		@return Period of the Calendar
	  */
	public int getC_Period_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_Period_ID);
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

	/** Set DPP.
		@param DPP DPP	  */
	public void setDPP (BigDecimal DPP)
	{
		set_Value (COLUMNNAME_DPP, DPP);
	}

	/** Get DPP.
		@return DPP	  */
	public BigDecimal getDPP () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_DPP);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Halved Amt.
		@param HalvedAmt 
		Untuk menampung 50% dari nilai Invoice yang akan dihitung sebagai penagihan PPh 21
	  */
	public void setHalvedAmt (BigDecimal HalvedAmt)
	{
		set_Value (COLUMNNAME_HalvedAmt, HalvedAmt);
	}

	/** Get Halved Amt.
		@return Untuk menampung 50% dari nilai Invoice yang akan dihitung sebagai penagihan PPh 21
	  */
	public BigDecimal getHalvedAmt () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_HalvedAmt);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set PPh.
		@param PPh PPh	  */
	public void setPPh (BigDecimal PPh)
	{
		set_Value (COLUMNNAME_PPh, PPh);
	}

	/** Get PPh.
		@return PPh	  */
	public BigDecimal getPPh () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_PPh);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Sequence.
		@param Sequence Sequence	  */
	public void setSequence (int Sequence)
	{
		set_Value (COLUMNNAME_Sequence, Integer.valueOf(Sequence));
	}

	/** Get Sequence.
		@return Sequence	  */
	public int getSequence () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_Sequence);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Withholding Calculation.
		@param TCS_WithholdingCalc_ID Withholding Calculation	  */
	public void setTCS_WithholdingCalc_ID (int TCS_WithholdingCalc_ID)
	{
		if (TCS_WithholdingCalc_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_TCS_WithholdingCalc_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_TCS_WithholdingCalc_ID, Integer.valueOf(TCS_WithholdingCalc_ID));
	}

	/** Get Withholding Calculation.
		@return Withholding Calculation	  */
	public int getTCS_WithholdingCalc_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_TCS_WithholdingCalc_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set TCS_WithholdingCalc_UU.
		@param TCS_WithholdingCalc_UU TCS_WithholdingCalc_UU	  */
	public void setTCS_WithholdingCalc_UU (String TCS_WithholdingCalc_UU)
	{
		set_ValueNoCheck (COLUMNNAME_TCS_WithholdingCalc_UU, TCS_WithholdingCalc_UU);
	}

	/** Get TCS_WithholdingCalc_UU.
		@return TCS_WithholdingCalc_UU	  */
	public String getTCS_WithholdingCalc_UU () 
	{
		return (String)get_Value(COLUMNNAME_TCS_WithholdingCalc_UU);
	}

	public I_TCS_WithholdingType getTCS_WithholdingType() throws RuntimeException
    {
		return (I_TCS_WithholdingType)MTable.get(getCtx(), I_TCS_WithholdingType.Table_Name)
			.getPO(getTCS_WithholdingType_ID(), get_TrxName());	}

	/** Set Withholding Type.
		@param TCS_WithholdingType_ID Withholding Type	  */
	public void setTCS_WithholdingType_ID (int TCS_WithholdingType_ID)
	{
		if (TCS_WithholdingType_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_TCS_WithholdingType_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_TCS_WithholdingType_ID, Integer.valueOf(TCS_WithholdingType_ID));
	}

	/** Get Withholding Type.
		@return Withholding Type	  */
	public int getTCS_WithholdingType_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_TCS_WithholdingType_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}
}