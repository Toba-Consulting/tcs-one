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

/** Generated Model for TCS_BOM_Explosion_List
 *  @author iDempiere (generated) 
 *  @version Release 5.1 - $Id$ */
public class X_TCS_BOM_Explosion_List extends PO implements I_TCS_BOM_Explosion_List, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20200626L;

    /** Standard Constructor */
    public X_TCS_BOM_Explosion_List (Properties ctx, int TCS_BOM_Explosion_List_ID, String trxName)
    {
      super (ctx, TCS_BOM_Explosion_List_ID, trxName);
      /** if (TCS_BOM_Explosion_List_ID == 0)
        {
        } */
    }

    /** Load Constructor */
    public X_TCS_BOM_Explosion_List (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_TCS_BOM_Explosion_List[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	/** Set bomdistance.
		@param bomdistance bomdistance	  */
	public void setbomdistance (int bomdistance)
	{
		set_Value (COLUMNNAME_bomdistance, Integer.valueOf(bomdistance));
	}

	/** Get bomdistance.
		@return bomdistance	  */
	public int getbomdistance () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_bomdistance);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set M_Product_Child_ID.
		@param M_Product_Child_ID M_Product_Child_ID	  */
	public void setM_Product_Child_ID (int M_Product_Child_ID)
	{
		if (M_Product_Child_ID < 1) 
			set_Value (COLUMNNAME_M_Product_Child_ID, null);
		else 
			set_Value (COLUMNNAME_M_Product_Child_ID, Integer.valueOf(M_Product_Child_ID));
	}

	/** Get M_Product_Child_ID.
		@return M_Product_Child_ID	  */
	public int getM_Product_Child_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_M_Product_Child_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public org.compiere.model.I_M_Product getM_Product() throws RuntimeException
    {
		return (org.compiere.model.I_M_Product)MTable.get(getCtx(), org.compiere.model.I_M_Product.Table_Name)
			.getPO(getM_Product_ID(), get_TrxName());	}

	/** Set Product.
		@param M_Product_ID 
		Product, Service, Item
	  */
	public void setM_Product_ID (int M_Product_ID)
	{
		if (M_Product_ID < 1) 
			set_Value (COLUMNNAME_M_Product_ID, null);
		else 
			set_Value (COLUMNNAME_M_Product_ID, Integer.valueOf(M_Product_ID));
	}

	/** Get Product.
		@return Product, Service, Item
	  */
	public int getM_Product_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_M_Product_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set M_Product_Parent_ID.
		@param M_Product_Parent_ID M_Product_Parent_ID	  */
	public void setM_Product_Parent_ID (int M_Product_Parent_ID)
	{
		if (M_Product_Parent_ID < 1) 
			set_Value (COLUMNNAME_M_Product_Parent_ID, null);
		else 
			set_Value (COLUMNNAME_M_Product_Parent_ID, Integer.valueOf(M_Product_Parent_ID));
	}

	/** Get M_Product_Parent_ID.
		@return M_Product_Parent_ID	  */
	public int getM_Product_Parent_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_M_Product_Parent_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set TCS_BOM_Explosion_List.
		@param TCS_BOM_Explosion_List_ID TCS_BOM_Explosion_List	  */
	public void setTCS_BOM_Explosion_List_ID (int TCS_BOM_Explosion_List_ID)
	{
		if (TCS_BOM_Explosion_List_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_TCS_BOM_Explosion_List_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_TCS_BOM_Explosion_List_ID, Integer.valueOf(TCS_BOM_Explosion_List_ID));
	}

	/** Get TCS_BOM_Explosion_List.
		@return TCS_BOM_Explosion_List	  */
	public int getTCS_BOM_Explosion_List_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_TCS_BOM_Explosion_List_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}
}