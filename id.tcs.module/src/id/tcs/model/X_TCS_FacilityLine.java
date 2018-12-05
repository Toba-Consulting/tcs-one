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

/** Generated Model for TCS_FacilityLine
 *  @author iDempiere (generated) 
 *  @version Release 5.1 - $Id$ */
public class X_TCS_FacilityLine extends PO implements I_TCS_FacilityLine, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20181204L;

    /** Standard Constructor */
    public X_TCS_FacilityLine (Properties ctx, int TCS_FacilityLine_ID, String trxName)
    {
      super (ctx, TCS_FacilityLine_ID, trxName);
      /** if (TCS_FacilityLine_ID == 0)
        {
			setTCS_TripFacility_ID (0);
        } */
    }

    /** Load Constructor */
    public X_TCS_FacilityLine (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_TCS_FacilityLine[")
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

	public I_HC_Base getHC_Base() throws RuntimeException
    {
		return (I_HC_Base)MTable.get(getCtx(), I_HC_Base.Table_Name)
			.getPO(getHC_Base_ID(), get_TrxName());	}

	/** Set HC_Base.
		@param HC_Base_ID HC_Base	  */
	public void setHC_Base_ID (int HC_Base_ID)
	{
		if (HC_Base_ID < 1) 
			set_Value (COLUMNNAME_HC_Base_ID, null);
		else 
			set_Value (COLUMNNAME_HC_Base_ID, Integer.valueOf(HC_Base_ID));
	}

	/** Get HC_Base.
		@return HC_Base	  */
	public int getHC_Base_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_HC_Base_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Level no.
		@param LevelNo Level no	  */
	public void setLevelNo (int LevelNo)
	{
		set_Value (COLUMNNAME_LevelNo, Integer.valueOf(LevelNo));
	}

	/** Get Level no.
		@return Level no	  */
	public int getLevelNo () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_LevelNo);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Price.
		@param Price 
		Price
	  */
	public void setPrice (BigDecimal Price)
	{
		set_Value (COLUMNNAME_Price, Price);
	}

	/** Get Price.
		@return Price
	  */
	public BigDecimal getPrice () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_Price);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

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

	/** Set TCS_FacilityLine_UU.
		@param TCS_FacilityLine_UU TCS_FacilityLine_UU	  */
	public void setTCS_FacilityLine_UU (String TCS_FacilityLine_UU)
	{
		set_ValueNoCheck (COLUMNNAME_TCS_FacilityLine_UU, TCS_FacilityLine_UU);
	}

	/** Get TCS_FacilityLine_UU.
		@return TCS_FacilityLine_UU	  */
	public String getTCS_FacilityLine_UU () 
	{
		return (String)get_Value(COLUMNNAME_TCS_FacilityLine_UU);
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
			set_ValueNoCheck (COLUMNNAME_TCS_TripFacility_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_TCS_TripFacility_ID, Integer.valueOf(TCS_TripFacility_ID));
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