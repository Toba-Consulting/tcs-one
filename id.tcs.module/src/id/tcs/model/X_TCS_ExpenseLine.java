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
import org.compiere.util.KeyNamePair;

/** Generated Model for TCS_ExpenseLine
 *  @author iDempiere (generated) 
 *  @version Release 5.1 - $Id$ */
public class X_TCS_ExpenseLine extends PO implements I_TCS_ExpenseLine, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20181130L;

    /** Standard Constructor */
    public X_TCS_ExpenseLine (Properties ctx, int TCS_ExpenseLine_ID, String trxName)
    {
      super (ctx, TCS_ExpenseLine_ID, trxName);
      /** if (TCS_ExpenseLine_ID == 0)
        {
			setC_BPartner_ID (0);
			setC_Charge_ID (0);
			setTCS_TravelExpense_ID (0);
        } */
    }

    /** Load Constructor */
    public X_TCS_ExpenseLine (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_TCS_ExpenseLine[")
        .append(get_ID()).append("]");
      return sb.toString();
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
			set_Value (COLUMNNAME_C_BPartner_ID, null);
		else 
			set_Value (COLUMNNAME_C_BPartner_ID, Integer.valueOf(C_BPartner_ID));
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

    /** Get Record ID/ColumnName
        @return ID/ColumnName pair
      */
    public KeyNamePair getKeyNamePair() 
    {
        return new KeyNamePair(get_ID(), String.valueOf(getC_BPartner_ID()));
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

	/** Set PriceEntered.
		@param PriceEntered 
		Price Entered - the price based on the selected/base UoM
	  */
	public void setPriceEntered (BigDecimal PriceEntered)
	{
		set_ValueNoCheck (COLUMNNAME_PriceEntered, PriceEntered);
	}

	/** Get PriceEntered.
		@return Price Entered - the price based on the selected/base UoM
	  */
	public BigDecimal getPriceEntered () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_PriceEntered);
		if (bd == null)
			 return Env.ZERO;
		return bd;
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

	/** Set TCS_ExpenseLine.
		@param TCS_ExpenseLine_ID TCS_ExpenseLine	  */
	public void setTCS_ExpenseLine_ID (int TCS_ExpenseLine_ID)
	{
		if (TCS_ExpenseLine_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_TCS_ExpenseLine_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_TCS_ExpenseLine_ID, Integer.valueOf(TCS_ExpenseLine_ID));
	}

	/** Get TCS_ExpenseLine.
		@return TCS_ExpenseLine	  */
	public int getTCS_ExpenseLine_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_TCS_ExpenseLine_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set TCS_ExpenseLine_UU.
		@param TCS_ExpenseLine_UU TCS_ExpenseLine_UU	  */
	public void setTCS_ExpenseLine_UU (String TCS_ExpenseLine_UU)
	{
		set_ValueNoCheck (COLUMNNAME_TCS_ExpenseLine_UU, TCS_ExpenseLine_UU);
	}

	/** Get TCS_ExpenseLine_UU.
		@return TCS_ExpenseLine_UU	  */
	public String getTCS_ExpenseLine_UU () 
	{
		return (String)get_Value(COLUMNNAME_TCS_ExpenseLine_UU);
	}

	public I_TCS_TravelExpense getTCS_TravelExpense() throws RuntimeException
    {
		return (I_TCS_TravelExpense)MTable.get(getCtx(), I_TCS_TravelExpense.Table_Name)
			.getPO(getTCS_TravelExpense_ID(), get_TrxName());	}

	/** Set TCS_TravelExpense.
		@param TCS_TravelExpense_ID TCS_TravelExpense	  */
	public void setTCS_TravelExpense_ID (int TCS_TravelExpense_ID)
	{
		if (TCS_TravelExpense_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_TCS_TravelExpense_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_TCS_TravelExpense_ID, Integer.valueOf(TCS_TravelExpense_ID));
	}

	/** Get TCS_TravelExpense.
		@return TCS_TravelExpense	  */
	public int getTCS_TravelExpense_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_TCS_TravelExpense_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}
}