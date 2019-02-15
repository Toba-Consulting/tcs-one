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

/** Generated Model for TCS_WithholdingRate
 *  @author iDempiere (generated) 
 *  @version Release 5.1 - $Id$ */
public class X_TCS_WithholdingRate extends PO implements I_TCS_WithholdingRate, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20190114L;

    /** Standard Constructor */
    public X_TCS_WithholdingRate (Properties ctx, int TCS_WithholdingRate_ID, String trxName)
    {
      super (ctx, TCS_WithholdingRate_ID, trxName);
      /** if (TCS_WithholdingRate_ID == 0)
        {
			setC_Charge_ID (0);
			setMaxValue (Env.ZERO);
			setMinValue (Env.ZERO);
			setRate (Env.ZERO);
			setTCS_WithholdingRate_ID (0);
			setTCS_WithholdingType_ID (0);
        } */
    }

    /** Load Constructor */
    public X_TCS_WithholdingRate (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_TCS_WithholdingRate[")
        .append(get_ID()).append("]");
      return sb.toString();
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
			set_Value (COLUMNNAME_C_Charge_ID, null);
		else 
			set_Value (COLUMNNAME_C_Charge_ID, Integer.valueOf(C_Charge_ID));
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

	/** Set Max Value.
		@param MaxValue Max Value	  */
	public void setMaxValue (BigDecimal MaxValue)
	{
		set_Value (COLUMNNAME_MaxValue, MaxValue);
	}

	/** Get Max Value.
		@return Max Value	  */
	public BigDecimal getMaxValue () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_MaxValue);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Min Value.
		@param MinValue Min Value	  */
	public void setMinValue (BigDecimal MinValue)
	{
		set_Value (COLUMNNAME_MinValue, MinValue);
	}

	/** Get Min Value.
		@return Min Value	  */
	public BigDecimal getMinValue () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_MinValue);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Rate.
		@param Rate 
		Rate or Tax or Exchange
	  */
	public void setRate (BigDecimal Rate)
	{
		set_Value (COLUMNNAME_Rate, Rate);
	}

	/** Get Rate.
		@return Rate or Tax or Exchange
	  */
	public BigDecimal getRate () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_Rate);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Withholding Rate.
		@param TCS_WithholdingRate_ID Withholding Rate	  */
	public void setTCS_WithholdingRate_ID (int TCS_WithholdingRate_ID)
	{
		if (TCS_WithholdingRate_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_TCS_WithholdingRate_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_TCS_WithholdingRate_ID, Integer.valueOf(TCS_WithholdingRate_ID));
	}

	/** Get Withholding Rate.
		@return Withholding Rate	  */
	public int getTCS_WithholdingRate_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_TCS_WithholdingRate_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set TCS_WithholdingRate_UU.
		@param TCS_WithholdingRate_UU TCS_WithholdingRate_UU	  */
	public void setTCS_WithholdingRate_UU (String TCS_WithholdingRate_UU)
	{
		set_ValueNoCheck (COLUMNNAME_TCS_WithholdingRate_UU, TCS_WithholdingRate_UU);
	}

	/** Get TCS_WithholdingRate_UU.
		@return TCS_WithholdingRate_UU	  */
	public String getTCS_WithholdingRate_UU () 
	{
		return (String)get_Value(COLUMNNAME_TCS_WithholdingRate_UU);
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