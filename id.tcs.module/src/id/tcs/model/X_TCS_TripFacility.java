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
import java.util.Properties;
import org.compiere.model.*;

/** Generated Model for TCS_TripFacility
 *  @author iDempiere (generated) 
 *  @version Release 5.1 - $Id$ */
public class X_TCS_TripFacility extends PO implements I_TCS_TripFacility, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20181129L;

    /** Standard Constructor */
    public X_TCS_TripFacility (Properties ctx, int TCS_TripFacility_ID, String trxName)
    {
      super (ctx, TCS_TripFacility_ID, trxName);
      /** if (TCS_TripFacility_ID == 0)
        {
        } */
    }

    /** Load Constructor */
    public X_TCS_TripFacility (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_TCS_TripFacility[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	/** Set Is Fixed.
		@param isFixed Is Fixed	  */
	public void setisFixed (boolean isFixed)
	{
		set_Value (COLUMNNAME_isFixed, Boolean.valueOf(isFixed));
	}

	/** Get Is Fixed.
		@return Is Fixed	  */
	public boolean isFixed () 
	{
		Object oo = get_Value(COLUMNNAME_isFixed);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set Is Limited.
		@param isLimited Is Limited	  */
	public void setisLimited (boolean isLimited)
	{
		set_Value (COLUMNNAME_isLimited, Boolean.valueOf(isLimited));
	}

	/** Get Is Limited.
		@return Is Limited	  */
	public boolean isLimited () 
	{
		Object oo = get_Value(COLUMNNAME_isLimited);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set Name.
		@param Name 
		Alphanumeric identifier of the entity
	  */
	public void setName (String Name)
	{
		set_Value (COLUMNNAME_Name, Name);
	}

	/** Get Name.
		@return Alphanumeric identifier of the entity
	  */
	public String getName () 
	{
		return (String)get_Value(COLUMNNAME_Name);
	}

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

	/** Set TCS_TripFacility_UU.
		@param TCS_TripFacility_UU TCS_TripFacility_UU	  */
	public void setTCS_TripFacility_UU (String TCS_TripFacility_UU)
	{
		set_ValueNoCheck (COLUMNNAME_TCS_TripFacility_UU, TCS_TripFacility_UU);
	}

	/** Get TCS_TripFacility_UU.
		@return TCS_TripFacility_UU	  */
	public String getTCS_TripFacility_UU () 
	{
		return (String)get_Value(COLUMNNAME_TCS_TripFacility_UU);
	}
}