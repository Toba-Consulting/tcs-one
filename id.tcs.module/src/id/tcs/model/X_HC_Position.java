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

/** Generated Model for HC_Position
 *  @author iDempiere (generated) 
 *  @version Release 5.1 - $Id$ */
public class X_HC_Position extends PO implements I_HC_Position, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20181129L;

    /** Standard Constructor */
    public X_HC_Position (Properties ctx, int HC_Position_ID, String trxName)
    {
      super (ctx, HC_Position_ID, trxName);
      /** if (HC_Position_ID == 0)
        {
			setLevelNo (0);
        } */
    }

    /** Load Constructor */
    public X_HC_Position (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_HC_Position[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	/** Set Position.
		@param HC_Position_ID Position	  */
	public void setHC_Position_ID (int HC_Position_ID)
	{
		if (HC_Position_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_HC_Position_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_HC_Position_ID, Integer.valueOf(HC_Position_ID));
	}

	/** Get Position.
		@return Position	  */
	public int getHC_Position_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_HC_Position_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set HC_Position_UU.
		@param HC_Position_UU HC_Position_UU	  */
	public void setHC_Position_UU (String HC_Position_UU)
	{
		set_ValueNoCheck (COLUMNNAME_HC_Position_UU, HC_Position_UU);
	}

	/** Get HC_Position_UU.
		@return HC_Position_UU	  */
	public String getHC_Position_UU () 
	{
		return (String)get_Value(COLUMNNAME_HC_Position_UU);
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