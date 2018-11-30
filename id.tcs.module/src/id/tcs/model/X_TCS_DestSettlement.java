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

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Properties;
import org.compiere.model.*;

/** Generated Model for TCS_DestSettlement
 *  @author iDempiere (generated) 
 *  @version Release 5.1 - $Id$ */
public class X_TCS_DestSettlement extends PO implements I_TCS_DestSettlement, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20181129L;

    /** Standard Constructor */
    public X_TCS_DestSettlement (Properties ctx, int TCS_DestSettlement_ID, String trxName)
    {
      super (ctx, TCS_DestSettlement_ID, trxName);
      /** if (TCS_DestSettlement_ID == 0)
        {
			setTCS_AdvSettlement_ID (0);
			setTCS_DestSettlement_ID (0);
        } */
    }

    /** Load Constructor */
    public X_TCS_DestSettlement (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_TCS_DestSettlement[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	/** Set Date From.
		@param DateFrom 
		Starting date for a range
	  */
	public void setDateFrom (Timestamp DateFrom)
	{
		set_Value (COLUMNNAME_DateFrom, DateFrom);
	}

	/** Get Date From.
		@return Starting date for a range
	  */
	public Timestamp getDateFrom () 
	{
		return (Timestamp)get_Value(COLUMNNAME_DateFrom);
	}

	/** Set Date To.
		@param DateTo 
		End date of a date range
	  */
	public void setDateTo (Timestamp DateTo)
	{
		set_Value (COLUMNNAME_DateTo, DateTo);
	}

	/** Get Date To.
		@return End date of a date range
	  */
	public Timestamp getDateTo () 
	{
		return (Timestamp)get_Value(COLUMNNAME_DateTo);
	}

	public I_HC_BaseCity getHC_BaseCityFrom() throws RuntimeException
    {
		return (I_HC_BaseCity)MTable.get(getCtx(), I_HC_BaseCity.Table_Name)
			.getPO(getHC_BaseCityFrom_ID(), get_TrxName());	}

	/** Set HC_BaseCityFrom.
		@param HC_BaseCityFrom_ID HC_BaseCityFrom	  */
	public void setHC_BaseCityFrom_ID (int HC_BaseCityFrom_ID)
	{
		if (HC_BaseCityFrom_ID < 1) 
			set_Value (COLUMNNAME_HC_BaseCityFrom_ID, null);
		else 
			set_Value (COLUMNNAME_HC_BaseCityFrom_ID, Integer.valueOf(HC_BaseCityFrom_ID));
	}

	/** Get HC_BaseCityFrom.
		@return HC_BaseCityFrom	  */
	public int getHC_BaseCityFrom_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_HC_BaseCityFrom_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_HC_BaseCity getHC_BaseCityTo() throws RuntimeException
    {
		return (I_HC_BaseCity)MTable.get(getCtx(), I_HC_BaseCity.Table_Name)
			.getPO(getHC_BaseCityTo_ID(), get_TrxName());	}

	/** Set HC_BaseCityTo.
		@param HC_BaseCityTo_ID HC_BaseCityTo	  */
	public void setHC_BaseCityTo_ID (int HC_BaseCityTo_ID)
	{
		if (HC_BaseCityTo_ID < 1) 
			set_Value (COLUMNNAME_HC_BaseCityTo_ID, null);
		else 
			set_Value (COLUMNNAME_HC_BaseCityTo_ID, Integer.valueOf(HC_BaseCityTo_ID));
	}

	/** Get HC_BaseCityTo.
		@return HC_BaseCityTo	  */
	public int getHC_BaseCityTo_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_HC_BaseCityTo_ID);
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

	public I_TCS_AdvSettlement getTCS_AdvSettlement() throws RuntimeException
    {
		return (I_TCS_AdvSettlement)MTable.get(getCtx(), I_TCS_AdvSettlement.Table_Name)
			.getPO(getTCS_AdvSettlement_ID(), get_TrxName());	}

	/** Set TCS_AdvSettlement.
		@param TCS_AdvSettlement_ID TCS_AdvSettlement	  */
	public void setTCS_AdvSettlement_ID (int TCS_AdvSettlement_ID)
	{
		if (TCS_AdvSettlement_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_TCS_AdvSettlement_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_TCS_AdvSettlement_ID, Integer.valueOf(TCS_AdvSettlement_ID));
	}

	/** Get TCS_AdvSettlement.
		@return TCS_AdvSettlement	  */
	public int getTCS_AdvSettlement_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_TCS_AdvSettlement_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_TCS_DestRequest getTCS_DestRequest() throws RuntimeException
    {
		return (I_TCS_DestRequest)MTable.get(getCtx(), I_TCS_DestRequest.Table_Name)
			.getPO(getTCS_DestRequest_ID(), get_TrxName());	}

	/** Set TCS_DestRequest.
		@param TCS_DestRequest_ID TCS_DestRequest	  */
	public void setTCS_DestRequest_ID (int TCS_DestRequest_ID)
	{
		if (TCS_DestRequest_ID < 1) 
			set_Value (COLUMNNAME_TCS_DestRequest_ID, null);
		else 
			set_Value (COLUMNNAME_TCS_DestRequest_ID, Integer.valueOf(TCS_DestRequest_ID));
	}

	/** Get TCS_DestRequest.
		@return TCS_DestRequest	  */
	public int getTCS_DestRequest_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_TCS_DestRequest_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

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

	/** Set TCS_DestSettlement_UU.
		@param TCS_DestSettlement_UU TCS_DestSettlement_UU	  */
	public void setTCS_DestSettlement_UU (String TCS_DestSettlement_UU)
	{
		set_ValueNoCheck (COLUMNNAME_TCS_DestSettlement_UU, TCS_DestSettlement_UU);
	}

	/** Get TCS_DestSettlement_UU.
		@return TCS_DestSettlement_UU	  */
	public String getTCS_DestSettlement_UU () 
	{
		return (String)get_Value(COLUMNNAME_TCS_DestSettlement_UU);
	}
}