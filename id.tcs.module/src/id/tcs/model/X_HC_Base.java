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

/** Generated Model for HC_Base
 *  @author iDempiere (generated) 
 *  @version Release 5.1 - $Id$ */
public class X_HC_Base extends PO implements I_HC_Base, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20181127L;

    /** Standard Constructor */
    public X_HC_Base (Properties ctx, int HC_Base_ID, String trxName)
    {
      super (ctx, HC_Base_ID, trxName);
      /** if (HC_Base_ID == 0)
        {
        } */
    }

    /** Load Constructor */
    public X_HC_Base (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_HC_Base[")
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

	/** Set HC_Base_UU.
		@param HC_Base_UU HC_Base_UU	  */
	public void setHC_Base_UU (String HC_Base_UU)
	{
		set_ValueNoCheck (COLUMNNAME_HC_Base_UU, HC_Base_UU);
	}

	/** Get HC_Base_UU.
		@return HC_Base_UU	  */
	public String getHC_Base_UU () 
	{
		return (String)get_Value(COLUMNNAME_HC_Base_UU);
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