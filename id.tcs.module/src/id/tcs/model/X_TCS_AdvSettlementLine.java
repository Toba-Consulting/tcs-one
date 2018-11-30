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

/** Generated Model for TCS_AdvSettlementLine
 *  @author iDempiere (generated) 
 *  @version Release 5.1 - $Id$ */
public class X_TCS_AdvSettlementLine extends PO implements I_TCS_AdvSettlementLine, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20181129L;

    /** Standard Constructor */
    public X_TCS_AdvSettlementLine (Properties ctx, int TCS_AdvSettlementLine_ID, String trxName)
    {
      super (ctx, TCS_AdvSettlementLine_ID, trxName);
      /** if (TCS_AdvSettlementLine_ID == 0)
        {
			setC_Charge_ID (0);
			setTCS_AdvSettlementLine_ID (0);
			setTCS_DestSettlement_ID (0);
        } */
    }

    /** Load Constructor */
    public X_TCS_AdvSettlementLine (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_TCS_AdvSettlementLine[")
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
		set_Value (COLUMNNAME_PriceEntered, PriceEntered);
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

	/** Set Sequence.
		@param SeqNo 
		Method of ordering records; lowest number comes first
	  */
	public void setSeqNo (int SeqNo)
	{
		set_Value (COLUMNNAME_SeqNo, Integer.valueOf(SeqNo));
	}

	/** Get Sequence.
		@return Method of ordering records; lowest number comes first
	  */
	public int getSeqNo () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_SeqNo);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_TCS_AdvRequestLine getTCS_AdvRequestLine() throws RuntimeException
    {
		return (I_TCS_AdvRequestLine)MTable.get(getCtx(), I_TCS_AdvRequestLine.Table_Name)
			.getPO(getTCS_AdvRequestLine_ID(), get_TrxName());	}

	/** Set TCS_AdvRequestLine.
		@param TCS_AdvRequestLine_ID TCS_AdvRequestLine	  */
	public void setTCS_AdvRequestLine_ID (int TCS_AdvRequestLine_ID)
	{
		if (TCS_AdvRequestLine_ID < 1) 
			set_Value (COLUMNNAME_TCS_AdvRequestLine_ID, null);
		else 
			set_Value (COLUMNNAME_TCS_AdvRequestLine_ID, Integer.valueOf(TCS_AdvRequestLine_ID));
	}

	/** Get TCS_AdvRequestLine.
		@return TCS_AdvRequestLine	  */
	public int getTCS_AdvRequestLine_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_TCS_AdvRequestLine_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set TCS_AdvSettlementLine.
		@param TCS_AdvSettlementLine_ID TCS_AdvSettlementLine	  */
	public void setTCS_AdvSettlementLine_ID (int TCS_AdvSettlementLine_ID)
	{
		if (TCS_AdvSettlementLine_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_TCS_AdvSettlementLine_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_TCS_AdvSettlementLine_ID, Integer.valueOf(TCS_AdvSettlementLine_ID));
	}

	/** Get TCS_AdvSettlementLine.
		@return TCS_AdvSettlementLine	  */
	public int getTCS_AdvSettlementLine_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_TCS_AdvSettlementLine_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set TCS_AdvSettlementLine_UU.
		@param TCS_AdvSettlementLine_UU TCS_AdvSettlementLine_UU	  */
	public void setTCS_AdvSettlementLine_UU (String TCS_AdvSettlementLine_UU)
	{
		set_ValueNoCheck (COLUMNNAME_TCS_AdvSettlementLine_UU, TCS_AdvSettlementLine_UU);
	}

	/** Get TCS_AdvSettlementLine_UU.
		@return TCS_AdvSettlementLine_UU	  */
	public String getTCS_AdvSettlementLine_UU () 
	{
		return (String)get_Value(COLUMNNAME_TCS_AdvSettlementLine_UU);
	}

	public I_TCS_DestSettlement getTCS_DestSettlement() throws RuntimeException
    {
		return (I_TCS_DestSettlement)MTable.get(getCtx(), I_TCS_DestSettlement.Table_Name)
			.getPO(getTCS_DestSettlement_ID(), get_TrxName());	}

	/** Set TCS_DestSettlement.
		@param TCS_DestSettlement_ID TCS_DestSettlement	  */
	public void setTCS_DestSettlement_ID (int TCS_DestSettlement_ID)
	{
		if (TCS_DestSettlement_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_TCS_DestSettlement_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_TCS_DestSettlement_ID, Integer.valueOf(TCS_DestSettlement_ID));
	}

	/** Get TCS_DestSettlement.
		@return TCS_DestSettlement	  */
	public int getTCS_DestSettlement_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_TCS_DestSettlement_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_TCS_FacilityLine getTCS_FacilityLine() throws RuntimeException
    {
		return (I_TCS_FacilityLine)MTable.get(getCtx(), I_TCS_FacilityLine.Table_Name)
			.getPO(getTCS_FacilityLine_ID(), get_TrxName());	}

	/** Set TCS_FacilityLine.
		@param TCS_FacilityLine_ID TCS_FacilityLine	  */
	public void setTCS_FacilityLine_ID (int TCS_FacilityLine_ID)
	{
		if (TCS_FacilityLine_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_TCS_FacilityLine_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_TCS_FacilityLine_ID, Integer.valueOf(TCS_FacilityLine_ID));
	}

	/** Get TCS_FacilityLine.
		@return TCS_FacilityLine	  */
	public int getTCS_FacilityLine_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_TCS_FacilityLine_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_TCS_TripFacility getTCS_TripFacility() throws RuntimeException
    {
		return (I_TCS_TripFacility)MTable.get(getCtx(), I_TCS_TripFacility.Table_Name)
			.getPO(getTCS_TripFacility_ID(), get_TrxName());	}

	/** Set TCS_TripFacility.
		@param TCS_TripFacility_ID TCS_TripFacility	  */
	public void setTCS_TripFacility_ID (int TCS_TripFacility_ID)
	{
		if (TCS_TripFacility_ID < 1) 
			set_Value (COLUMNNAME_TCS_TripFacility_ID, null);
		else 
			set_Value (COLUMNNAME_TCS_TripFacility_ID, Integer.valueOf(TCS_TripFacility_ID));
	}

	/** Get TCS_TripFacility.
		@return TCS_TripFacility	  */
	public int getTCS_TripFacility_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_TCS_TripFacility_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}
}