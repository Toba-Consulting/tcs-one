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

/** Generated Model for HC_BaseCity
 *  @author iDempiere (generated) 
 *  @version Release 5.1 - $Id$ */
public class X_HC_BaseCity extends PO implements I_HC_BaseCity, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20181127L;

    /** Standard Constructor */
    public X_HC_BaseCity (Properties ctx, int HC_BaseCity_ID, String trxName)
    {
      super (ctx, HC_BaseCity_ID, trxName);
      /** if (HC_BaseCity_ID == 0)
        {
			setHC_Base_ID (0);
			setHC_BaseCity_ID (0);
        } */
    }

    /** Load Constructor */
    public X_HC_BaseCity (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_HC_BaseCity[")
        .append(get_ID()).append("]");
      return sb.toString();
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

	public I_HC_Base getHC_Base() throws RuntimeException
    {
		return (I_HC_Base)MTable.get(getCtx(), I_HC_Base.Table_Name)
			.getPO(getHC_Base_ID(), get_TrxName());	}

	/** Set HC_Base.
		@param HC_Base_ID HC_Base	  */
	public void setHC_Base_ID (int HC_Base_ID)
	{
		if (HC_Base_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_HC_Base_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_HC_Base_ID, Integer.valueOf(HC_Base_ID));
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

	/** Set HC_BaseCity.
		@param HC_BaseCity_ID HC_BaseCity	  */
	public void setHC_BaseCity_ID (int HC_BaseCity_ID)
	{
		if (HC_BaseCity_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_HC_BaseCity_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_HC_BaseCity_ID, Integer.valueOf(HC_BaseCity_ID));
	}

	/** Get HC_BaseCity.
		@return HC_BaseCity	  */
	public int getHC_BaseCity_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_HC_BaseCity_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set HC_BaseCity_UU.
		@param HC_BaseCity_UU HC_BaseCity_UU	  */
	public void setHC_BaseCity_UU (String HC_BaseCity_UU)
	{
		set_ValueNoCheck (COLUMNNAME_HC_BaseCity_UU, HC_BaseCity_UU);
	}

	/** Get HC_BaseCity_UU.
		@return HC_BaseCity_UU	  */
	public String getHC_BaseCity_UU () 
	{
		return (String)get_Value(COLUMNNAME_HC_BaseCity_UU);
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
}