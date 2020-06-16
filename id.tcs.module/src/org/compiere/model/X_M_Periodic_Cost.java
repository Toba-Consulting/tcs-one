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
import org.compiere.model.*;
import org.compiere.util.Env;
import org.compiere.util.KeyNamePair;

/** Generated Model for M_Periodic_Cost
 *  @author iDempiere (generated) 
 *  @version Release 3.1 - $Id$ */
public class X_M_Periodic_Cost extends PO implements I_M_Periodic_Cost, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20180612L;

    /** Standard Constructor */
    public X_M_Periodic_Cost (Properties ctx, int M_Periodic_Cost_ID, String trxName)
    {
      super (ctx, M_Periodic_Cost_ID, trxName);
      /** if (M_Periodic_Cost_ID == 0)
        {
			setM_Periodic_Cost_ID (0);
        } */
    }

    /** Load Constructor */
    public X_M_Periodic_Cost (Properties ctx, ResultSet rs, String trxName)
    {
      super (ctx, rs, trxName);
    }

    /** AccessLevel
      * @return 4 - System 
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

	/** Set BeginningQty.
		@param BeginningQty BeginningQty	  */
	public void setBeginningQty (BigDecimal BeginningQty)
	{
		set_Value (COLUMNNAME_BeginningQty, BeginningQty);
	}

	/** Get BeginningQty.
		@return BeginningQty	  */
	public BigDecimal getBeginningQty () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_BeginningQty);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set BeginnningAmount.
		@param BeginnningAmount BeginnningAmount	  */
	public void setBeginningAmount (BigDecimal BeginnningAmount)
	{
		set_Value (COLUMNNAME_BeginningAmount, BeginnningAmount);
	}

	/** Get BeginnningAmount.
		@return BeginnningAmount	  */
	public BigDecimal getBeginningAmount () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_BeginningAmount);
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

	/** Set Cost Price.
		@param CostPrice Cost Price	  */
	public void setCostPrice (BigDecimal CostPrice)
	{
		set_Value (COLUMNNAME_CostPrice, CostPrice);
	}

	/** Get Cost Price.
		@return Cost Price	  */
	public BigDecimal getCostPrice () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_CostPrice);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set EndingAmount.
		@param EndingAmount EndingAmount	  */
	public void setEndingAmount (BigDecimal EndingAmount)
	{
		set_Value (COLUMNNAME_EndingAmount, EndingAmount);
	}

	/** Get EndingAmount.
		@return EndingAmount	  */
	public BigDecimal getEndingAmount () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_EndingAmount);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set EndingQty.
		@param EndingQty EndingQty	  */
	public void setEndingQty (BigDecimal EndingQty)
	{
		set_Value (COLUMNNAME_EndingQty, EndingQty);
	}

	/** Get EndingQty.
		@return EndingQty	  */
	public BigDecimal getEndingQty () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_EndingQty);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set IPV_Amount.
		@param IPV_Amount IPV_Amount	  */
	public void setIPV_Amount (BigDecimal IPV_Amount)
	{
		set_Value (COLUMNNAME_IPV_Amount, IPV_Amount);
	}

	/** Get IPV_Amount.
		@return IPV_Amount	  */
	public BigDecimal getIPV_Amount () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_IPV_Amount);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set IssueAmount.
		@param IssueAmount IssueAmount	  */
	public void setIssueAmount (BigDecimal IssueAmount)
	{
		set_Value (COLUMNNAME_IssueAmount, IssueAmount);
	}

	/** Get IssueAmount.
		@return IssueAmount	  */
	public BigDecimal getIssueAmount () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_IssueAmount);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set IssueQty.
		@param IssueQty IssueQty	  */
	public void setIssueQty (BigDecimal IssueQty)
	{
		set_Value (COLUMNNAME_IssueQty, IssueQty);
	}

	/** Get IssueQty.
		@return IssueQty	  */
	public BigDecimal getIssueQty () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_IssueQty);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set LandedCostAmount.
		@param LandedCostAmount LandedCostAmount	  */
	public void setLandedCostAmount (BigDecimal LandedCostAmount)
	{
		set_Value (COLUMNNAME_LandedCostAmount, LandedCostAmount);
	}

	/** Get LandedCostAmount.
		@return LandedCostAmount	  */
	public BigDecimal getLandedCostAmount () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_LandedCostAmount);
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

	/** Set M_Periodic_Cost.
		@param M_Periodic_Cost_ID M_Periodic_Cost	  */
	public void setM_Periodic_Cost_ID (int M_Periodic_Cost_ID)
	{
		if (M_Periodic_Cost_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_M_Periodic_Cost_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_M_Periodic_Cost_ID, Integer.valueOf(M_Periodic_Cost_ID));
	}

	/** Get M_Periodic_Cost.
		@return M_Periodic_Cost	  */
	public int getM_Periodic_Cost_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_M_Periodic_Cost_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

    /** Get Record ID/ColumnName
        @return ID/ColumnName pair
      */
    public KeyNamePair getKeyNamePair() 
    {
        return new KeyNamePair(get_ID(), String.valueOf(getM_Periodic_Cost_ID()));
    }

	/** Set M_Periodic_Cost_UU.
		@param M_Periodic_Cost_UU M_Periodic_Cost_UU	  */
	public void setM_Periodic_Cost_UU (String M_Periodic_Cost_UU)
	{
		set_Value (COLUMNNAME_M_Periodic_Cost_UU, M_Periodic_Cost_UU);
	}

	/** Get M_Periodic_Cost_UU.
		@return M_Periodic_Cost_UU	  */
	public String getM_Periodic_Cost_UU () 
	{
		return (String)get_Value(COLUMNNAME_M_Periodic_Cost_UU);
	}

	public org.compiere.model.I_M_Product_Category getM_Product_Category() throws RuntimeException
    {
		return (org.compiere.model.I_M_Product_Category)MTable.get(getCtx(), org.compiere.model.I_M_Product_Category.Table_Name)
			.getPO(getM_Product_Category_ID(), get_TrxName());	}

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

	/** Set ReceiptAmount.
		@param ReceiptAmount ReceiptAmount	  */
	public void setReceiptAmount (BigDecimal ReceiptAmount)
	{
		set_Value (COLUMNNAME_ReceiptAmount, ReceiptAmount);
	}

	/** Get ReceiptAmount.
		@return ReceiptAmount	  */
	public BigDecimal getReceiptAmount () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_ReceiptAmount);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set ReceiptQty.
		@param ReceiptQty ReceiptQty	  */
	public void setReceiptQty (BigDecimal ReceiptQty)
	{
		set_Value (COLUMNNAME_ReceiptQty, ReceiptQty);
	}

	/** Get ReceiptQty.
		@return ReceiptQty	  */
	public BigDecimal getReceiptQty () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_ReceiptQty);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}
}