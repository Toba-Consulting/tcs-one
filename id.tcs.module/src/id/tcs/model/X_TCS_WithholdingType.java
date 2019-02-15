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

/** Generated Model for TCS_WithholdingType
 *  @author iDempiere (generated) 
 *  @version Release 5.1 - $Id$ */
public class X_TCS_WithholdingType extends PO implements I_TCS_WithholdingType, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20190114L;

    /** Standard Constructor */
    public X_TCS_WithholdingType (Properties ctx, int TCS_WithholdingType_ID, String trxName)
    {
      super (ctx, TCS_WithholdingType_ID, trxName);
      /** if (TCS_WithholdingType_ID == 0)
        {
			setC_Charge_ID (0);
			setTCS_WithholdingType_ID (0);
        } */
    }

    /** Load Constructor */
    public X_TCS_WithholdingType (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_TCS_WithholdingType[")
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

	/** Set TCS_WithholdingType_UU.
		@param TCS_WithholdingType_UU TCS_WithholdingType_UU	  */
	public void setTCS_WithholdingType_UU (String TCS_WithholdingType_UU)
	{
		set_ValueNoCheck (COLUMNNAME_TCS_WithholdingType_UU, TCS_WithholdingType_UU);
	}

	/** Get TCS_WithholdingType_UU.
		@return TCS_WithholdingType_UU	  */
	public String getTCS_WithholdingType_UU () 
	{
		return (String)get_Value(COLUMNNAME_TCS_WithholdingType_UU);
	}
}