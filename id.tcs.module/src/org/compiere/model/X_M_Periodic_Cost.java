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
package org.compiere.model;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.util.Properties;
import org.compiere.util.Env;

/** Generated Model for M_Periodic_Cost
 *  @author iDempiere (generated) 
 *  @version Release 5.1 - $Id$ */
public class X_M_Periodic_Cost extends PO implements I_M_Periodic_Cost, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20200620L;

    /** Standard Constructor */
    public X_M_Periodic_Cost (Properties ctx, int M_Periodic_Cost_ID, String trxName)
    {
      super (ctx, M_Periodic_Cost_ID, trxName);
      /** if (M_Periodic_Cost_ID == 0)
        {
			setm_periodic_cost_id (0);
        } */
    }

    /** Load Constructor */
    public X_M_Periodic_Cost (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_M_Periodic_Cost[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	/** Set beginningamount.
		@param beginningamount beginningamount	  */
	public void setbeginningamount (BigDecimal beginningamount)
	{
		set_Value (COLUMNNAME_beginningamount, beginningamount);
	}

	/** Get beginningamount.
		@return beginningamount	  */
	public BigDecimal getbeginningamount () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_beginningamount);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set beginningqty.
		@param beginningqty beginningqty	  */
	public void setbeginningqty (BigDecimal beginningqty)
	{
		set_Value (COLUMNNAME_beginningqty, beginningqty);
	}

	/** Get beginningqty.
		@return beginningqty	  */
	public BigDecimal getbeginningqty () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_beginningqty);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	public org.compiere.model.I_C_Period getC_Period() throws RuntimeException
    {
		return (org.compiere.model.I_C_Period)MTable.get(getCtx(), org.compiere.model.I_C_Period.Table_Name)
			.getPO(getC_Period_ID(), get_TrxName());	}

	/** Set Period.
		@param C_Period_ID 
		Period of the Calendar
	  */
	public void setC_Period_ID (int C_Period_ID)
	{
		if (C_Period_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_C_Period_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_C_Period_ID, Integer.valueOf(C_Period_ID));
	}

	/** Get Period.
		@return Period of the Calendar
	  */
	public int getC_Period_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_Period_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set costprice.
		@param costprice costprice	  */
	public void setcostprice (BigDecimal costprice)
	{
		set_Value (COLUMNNAME_costprice, costprice);
	}

	/** Get costprice.
		@return costprice	  */
	public BigDecimal getcostprice () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_costprice);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set endingamount.
		@param endingamount endingamount	  */
	public void setendingamount (BigDecimal endingamount)
	{
		set_Value (COLUMNNAME_endingamount, endingamount);
	}

	/** Get endingamount.
		@return endingamount	  */
	public BigDecimal getendingamount () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_endingamount);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set endingqty.
		@param endingqty endingqty	  */
	public void setendingqty (BigDecimal endingqty)
	{
		set_Value (COLUMNNAME_endingqty, endingqty);
	}

	/** Get endingqty.
		@return endingqty	  */
	public BigDecimal getendingqty () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_endingqty);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set ipv_amount.
		@param ipv_amount ipv_amount	  */
	public void setipv_amount (BigDecimal ipv_amount)
	{
		set_Value (COLUMNNAME_ipv_amount, ipv_amount);
	}

	/** Get ipv_amount.
		@return ipv_amount	  */
	public BigDecimal getipv_amount () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_ipv_amount);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set issueamount.
		@param issueamount issueamount	  */
	public void setissueamount (BigDecimal issueamount)
	{
		set_Value (COLUMNNAME_issueamount, issueamount);
	}

	/** Get issueamount.
		@return issueamount	  */
	public BigDecimal getissueamount () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_issueamount);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set issueqty.
		@param issueqty issueqty	  */
	public void setissueqty (BigDecimal issueqty)
	{
		set_Value (COLUMNNAME_issueqty, issueqty);
	}

	/** Get issueqty.
		@return issueqty	  */
	public BigDecimal getissueqty () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_issueqty);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set landedcostamount.
		@param landedcostamount landedcostamount	  */
	public void setlandedcostamount (BigDecimal landedcostamount)
	{
		set_Value (COLUMNNAME_landedcostamount, landedcostamount);
	}

	/** Get landedcostamount.
		@return landedcostamount	  */
	public BigDecimal getlandedcostamount () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_landedcostamount);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	public I_M_AttributeSetInstance getM_AttributeSetInstance() throws RuntimeException
    {
		return (I_M_AttributeSetInstance)MTable.get(getCtx(), I_M_AttributeSetInstance.Table_Name)
			.getPO(getM_AttributeSetInstance_ID(), get_TrxName());	}

	/** Set Attribute Set Instance.
		@param M_AttributeSetInstance_ID 
		Product Attribute Set Instance
	  */
	public void setM_AttributeSetInstance_ID (int M_AttributeSetInstance_ID)
	{
		if (M_AttributeSetInstance_ID < 0) 
			set_Value (COLUMNNAME_M_AttributeSetInstance_ID, null);
		else 
			set_Value (COLUMNNAME_M_AttributeSetInstance_ID, Integer.valueOf(M_AttributeSetInstance_ID));
	}

	/** Get Attribute Set Instance.
		@return Product Attribute Set Instance
	  */
	public int getM_AttributeSetInstance_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_M_AttributeSetInstance_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set m_periodic_cost_id.
		@param m_periodic_cost_id m_periodic_cost_id	  */
	public void setm_periodic_cost_id (int m_periodic_cost_id)
	{
		set_ValueNoCheck (COLUMNNAME_m_periodic_cost_id, Integer.valueOf(m_periodic_cost_id));
	}

	/** Get m_periodic_cost_id.
		@return m_periodic_cost_id	  */
	public int getm_periodic_cost_id () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_m_periodic_cost_id);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set m_periodic_cost_uu.
		@param m_periodic_cost_uu m_periodic_cost_uu	  */
	public void setm_periodic_cost_uu (String m_periodic_cost_uu)
	{
		set_ValueNoCheck (COLUMNNAME_m_periodic_cost_uu, m_periodic_cost_uu);
	}

	/** Get m_periodic_cost_uu.
		@return m_periodic_cost_uu	  */
	public String getm_periodic_cost_uu () 
	{
		return (String)get_Value(COLUMNNAME_m_periodic_cost_uu);
	}

	/** Set Product Category.
		@param M_Product_Category_ID 
		Category of a Product
	  */
	public void setM_Product_Category_ID (int M_Product_Category_ID)
	{
		if (M_Product_Category_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_M_Product_Category_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_M_Product_Category_ID, Integer.valueOf(M_Product_Category_ID));
	}

	/** Get Product Category.
		@return Category of a Product
	  */
	public int getM_Product_Category_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_M_Product_Category_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

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

	/** Set manufacturedamt.
		@param manufacturedamt manufacturedamt	  */
	public void setmanufacturedamt (BigDecimal manufacturedamt)
	{
		set_Value (COLUMNNAME_manufacturedamt, manufacturedamt);
	}

	/** Get manufacturedamt.
		@return manufacturedamt	  */
	public BigDecimal getmanufacturedamt () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_manufacturedamt);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set manufacturedqty.
		@param manufacturedqty manufacturedqty	  */
	public void setmanufacturedqty (BigDecimal manufacturedqty)
	{
		set_Value (COLUMNNAME_manufacturedqty, manufacturedqty);
	}

	/** Get manufacturedqty.
		@return manufacturedqty	  */
	public BigDecimal getmanufacturedqty () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_manufacturedqty);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set receiptamount.
		@param receiptamount receiptamount	  */
	public void setreceiptamount (BigDecimal receiptamount)
	{
		set_Value (COLUMNNAME_receiptamount, receiptamount);
	}

	/** Get receiptamount.
		@return receiptamount	  */
	public BigDecimal getreceiptamount () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_receiptamount);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set receiptqty.
		@param receiptqty receiptqty	  */
	public void setreceiptqty (BigDecimal receiptqty)
	{
		set_Value (COLUMNNAME_receiptqty, receiptqty);
	}

	/** Get receiptqty.
		@return receiptqty	  */
	public BigDecimal getreceiptqty () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_receiptqty);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set shippedamt.
		@param shippedamt shippedamt	  */
	public void setshippedamt (BigDecimal shippedamt)
	{
		set_Value (COLUMNNAME_shippedamt, shippedamt);
	}

	/** Get shippedamt.
		@return shippedamt	  */
	public BigDecimal getshippedamt () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_shippedamt);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set shippedqty.
		@param shippedqty shippedqty	  */
	public void setshippedqty (BigDecimal shippedqty)
	{
		set_Value (COLUMNNAME_shippedqty, shippedqty);
	}

	/** Get shippedqty.
		@return shippedqty	  */
	public BigDecimal getshippedqty () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_shippedqty);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}
}