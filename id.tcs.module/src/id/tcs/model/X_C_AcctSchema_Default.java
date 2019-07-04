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
import org.compiere.util.KeyNamePair;

/** Generated Model for C_AcctSchema_Default
 *  @author iDempiere (generated) 
 *  @version Release 5.1 - $Id$ */
public class X_C_AcctSchema_Default extends PO implements I_C_AcctSchema_Default, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20190619L;

    /** Standard Constructor */
    public X_C_AcctSchema_Default (Properties ctx, int C_AcctSchema_Default_ID, String trxName)
    {
      super (ctx, C_AcctSchema_Default_ID, trxName);
      /** if (C_AcctSchema_Default_ID == 0)
        {
			setB_Asset_Acct (0);
			setB_InterestExp_Acct (0);
			setB_InterestRev_Acct (0);
			setB_InTransit_Acct (0);
			setB_PaymentSelect_Acct (0);
			setB_UnallocatedCash_Acct (0);
			setC_AcctSchema_ID (0);
			setC_Prepayment_Acct (0);
			setC_Receivable_Acct (0);
			setCh_Expense_Acct (0);
			setNotInvoicedReceipts_Acct (0);
			setP_Asset_Acct (0);
			setP_COGS_Acct (0);
			setP_CostAdjustment_Acct (0);
			setP_Expense_Acct (0);
			setP_InventoryClearing_Acct (0);
			setP_InvoicePriceVariance_Acct (0);
			setP_PurchasePriceVariance_Acct (0);
			setP_RateVariance_Acct (0);
			setP_Revenue_Acct (0);
			setP_TradeDiscountGrant_Acct (0);
			setP_TradeDiscountRec_Acct (0);
			setPayDiscount_Exp_Acct (0);
			setPayDiscount_Rev_Acct (0);
			setPJ_Asset_Acct (0);
			setPJ_WIP_Acct (0);
			setRealizedGain_Acct (0);
			setRealizedLoss_Acct (0);
			setT_Credit_Acct (0);
			setT_Due_Acct (0);
			setT_Expense_Acct (0);
			setUnrealizedGain_Acct (0);
			setUnrealizedLoss_Acct (0);
			setV_Liability_Acct (0);
			setV_Prepayment_Acct (0);
			setW_Differences_Acct (0);
			setWriteOff_Acct (0);
        } */
    }

    /** Load Constructor */
    public X_C_AcctSchema_Default (Properties ctx, ResultSet rs, String trxName)
    {
      super (ctx, rs, trxName);
    }

    /** AccessLevel
      * @return 2 - Client 
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
      StringBuffer sb = new StringBuffer ("X_C_AcctSchema_Default[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	public I_C_ValidCombination getB_Asset_A() throws RuntimeException
    {
		return (I_C_ValidCombination)MTable.get(getCtx(), I_C_ValidCombination.Table_Name)
			.getPO(getB_Asset_Acct(), get_TrxName());	}

	/** Set Bank Asset.
		@param B_Asset_Acct 
		Bank Asset Account
	  */
	public void setB_Asset_Acct (int B_Asset_Acct)
	{
		set_Value (COLUMNNAME_B_Asset_Acct, Integer.valueOf(B_Asset_Acct));
	}

	/** Get Bank Asset.
		@return Bank Asset Account
	  */
	public int getB_Asset_Acct () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_B_Asset_Acct);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_C_ValidCombination getB_InterestExp_A() throws RuntimeException
    {
		return (I_C_ValidCombination)MTable.get(getCtx(), I_C_ValidCombination.Table_Name)
			.getPO(getB_InterestExp_Acct(), get_TrxName());	}

	/** Set Bank Interest Expense.
		@param B_InterestExp_Acct 
		Bank Interest Expense Account
	  */
	public void setB_InterestExp_Acct (int B_InterestExp_Acct)
	{
		set_Value (COLUMNNAME_B_InterestExp_Acct, Integer.valueOf(B_InterestExp_Acct));
	}

	/** Get Bank Interest Expense.
		@return Bank Interest Expense Account
	  */
	public int getB_InterestExp_Acct () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_B_InterestExp_Acct);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_C_ValidCombination getB_InterestRev_A() throws RuntimeException
    {
		return (I_C_ValidCombination)MTable.get(getCtx(), I_C_ValidCombination.Table_Name)
			.getPO(getB_InterestRev_Acct(), get_TrxName());	}

	/** Set Bank Interest Revenue.
		@param B_InterestRev_Acct 
		Bank Interest Revenue Account
	  */
	public void setB_InterestRev_Acct (int B_InterestRev_Acct)
	{
		set_Value (COLUMNNAME_B_InterestRev_Acct, Integer.valueOf(B_InterestRev_Acct));
	}

	/** Get Bank Interest Revenue.
		@return Bank Interest Revenue Account
	  */
	public int getB_InterestRev_Acct () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_B_InterestRev_Acct);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_C_ValidCombination getB_InTransit_A() throws RuntimeException
    {
		return (I_C_ValidCombination)MTable.get(getCtx(), I_C_ValidCombination.Table_Name)
			.getPO(getB_InTransit_Acct(), get_TrxName());	}

	/** Set Bank In Transit.
		@param B_InTransit_Acct 
		Bank In Transit Account
	  */
	public void setB_InTransit_Acct (int B_InTransit_Acct)
	{
		set_Value (COLUMNNAME_B_InTransit_Acct, Integer.valueOf(B_InTransit_Acct));
	}

	/** Get Bank In Transit.
		@return Bank In Transit Account
	  */
	public int getB_InTransit_Acct () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_B_InTransit_Acct);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_C_ValidCombination getB_PaymentSelect_A() throws RuntimeException
    {
		return (I_C_ValidCombination)MTable.get(getCtx(), I_C_ValidCombination.Table_Name)
			.getPO(getB_PaymentSelect_Acct(), get_TrxName());	}

	/** Set Payment Selection.
		@param B_PaymentSelect_Acct 
		AP Payment Selection Clearing Account
	  */
	public void setB_PaymentSelect_Acct (int B_PaymentSelect_Acct)
	{
		set_Value (COLUMNNAME_B_PaymentSelect_Acct, Integer.valueOf(B_PaymentSelect_Acct));
	}

	/** Get Payment Selection.
		@return AP Payment Selection Clearing Account
	  */
	public int getB_PaymentSelect_Acct () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_B_PaymentSelect_Acct);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_C_ValidCombination getB_UnallocatedCash_A() throws RuntimeException
    {
		return (I_C_ValidCombination)MTable.get(getCtx(), I_C_ValidCombination.Table_Name)
			.getPO(getB_UnallocatedCash_Acct(), get_TrxName());	}

	/** Set Unallocated Cash.
		@param B_UnallocatedCash_Acct 
		Unallocated Cash Clearing Account
	  */
	public void setB_UnallocatedCash_Acct (int B_UnallocatedCash_Acct)
	{
		set_Value (COLUMNNAME_B_UnallocatedCash_Acct, Integer.valueOf(B_UnallocatedCash_Acct));
	}

	/** Get Unallocated Cash.
		@return Unallocated Cash Clearing Account
	  */
	public int getB_UnallocatedCash_Acct () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_B_UnallocatedCash_Acct);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set C_AcctSchema_Default_UU.
		@param C_AcctSchema_Default_UU C_AcctSchema_Default_UU	  */
	public void setC_AcctSchema_Default_UU (String C_AcctSchema_Default_UU)
	{
		set_Value (COLUMNNAME_C_AcctSchema_Default_UU, C_AcctSchema_Default_UU);
	}

	/** Get C_AcctSchema_Default_UU.
		@return C_AcctSchema_Default_UU	  */
	public String getC_AcctSchema_Default_UU () 
	{
		return (String)get_Value(COLUMNNAME_C_AcctSchema_Default_UU);
	}

	public org.compiere.model.I_C_AcctSchema getC_AcctSchema() throws RuntimeException
    {
		return (org.compiere.model.I_C_AcctSchema)MTable.get(getCtx(), org.compiere.model.I_C_AcctSchema.Table_Name)
			.getPO(getC_AcctSchema_ID(), get_TrxName());	}

	/** Set Accounting Schema.
		@param C_AcctSchema_ID 
		Rules for accounting
	  */
	public void setC_AcctSchema_ID (int C_AcctSchema_ID)
	{
		if (C_AcctSchema_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_C_AcctSchema_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_C_AcctSchema_ID, Integer.valueOf(C_AcctSchema_ID));
	}

	/** Get Accounting Schema.
		@return Rules for accounting
	  */
	public int getC_AcctSchema_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_AcctSchema_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

    /** Get Record ID/ColumnName
        @return ID/ColumnName pair
      */
    public KeyNamePair getKeyNamePair() 
    {
        return new KeyNamePair(get_ID(), String.valueOf(getC_AcctSchema_ID()));
    }

	public org.compiere.model.I_C_ElementValue getC_ElementValue_ACV() throws RuntimeException
    {
		return (org.compiere.model.I_C_ElementValue)MTable.get(getCtx(), org.compiere.model.I_C_ElementValue.Table_Name)
			.getPO(getC_ElementValue_ACV_ID(), get_TrxName());	}

	/** Set Account Average Cost Variance.
		@param C_ElementValue_ACV_ID Account Average Cost Variance	  */
	public void setC_ElementValue_ACV_ID (int C_ElementValue_ACV_ID)
	{
		if (C_ElementValue_ACV_ID < 1) 
			set_Value (COLUMNNAME_C_ElementValue_ACV_ID, null);
		else 
			set_Value (COLUMNNAME_C_ElementValue_ACV_ID, Integer.valueOf(C_ElementValue_ACV_ID));
	}

	/** Get Account Average Cost Variance.
		@return Account Average Cost Variance	  */
	public int getC_ElementValue_ACV_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_ElementValue_ACV_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public org.compiere.model.I_C_ElementValue getC_ElementValue_Asset() throws RuntimeException
    {
		return (org.compiere.model.I_C_ElementValue)MTable.get(getCtx(), org.compiere.model.I_C_ElementValue.Table_Name)
			.getPO(getC_ElementValue_Asset_ID(), get_TrxName());	}

	/** Set Account Product Asset.
		@param C_ElementValue_Asset_ID Account Product Asset	  */
	public void setC_ElementValue_Asset_ID (int C_ElementValue_Asset_ID)
	{
		if (C_ElementValue_Asset_ID < 1) 
			set_Value (COLUMNNAME_C_ElementValue_Asset_ID, null);
		else 
			set_Value (COLUMNNAME_C_ElementValue_Asset_ID, Integer.valueOf(C_ElementValue_Asset_ID));
	}

	/** Get Account Product Asset.
		@return Account Product Asset	  */
	public int getC_ElementValue_Asset_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_ElementValue_Asset_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public org.compiere.model.I_C_ElementValue getC_ElementValue_BA() throws RuntimeException
    {
		return (org.compiere.model.I_C_ElementValue)MTable.get(getCtx(), org.compiere.model.I_C_ElementValue.Table_Name)
			.getPO(getC_ElementValue_BA_ID(), get_TrxName());	}

	/** Set Account Bank Asset.
		@param C_ElementValue_BA_ID Account Bank Asset	  */
	public void setC_ElementValue_BA_ID (int C_ElementValue_BA_ID)
	{
		if (C_ElementValue_BA_ID < 1) 
			set_Value (COLUMNNAME_C_ElementValue_BA_ID, null);
		else 
			set_Value (COLUMNNAME_C_ElementValue_BA_ID, Integer.valueOf(C_ElementValue_BA_ID));
	}

	/** Get Account Bank Asset.
		@return Account Bank Asset	  */
	public int getC_ElementValue_BA_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_ElementValue_BA_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public org.compiere.model.I_C_ElementValue getC_ElementValue_BIE() throws RuntimeException
    {
		return (org.compiere.model.I_C_ElementValue)MTable.get(getCtx(), org.compiere.model.I_C_ElementValue.Table_Name)
			.getPO(getC_ElementValue_BIE_ID(), get_TrxName());	}

	/** Set Account Bank Interest Expense.
		@param C_ElementValue_BIE_ID Account Bank Interest Expense	  */
	public void setC_ElementValue_BIE_ID (int C_ElementValue_BIE_ID)
	{
		if (C_ElementValue_BIE_ID < 1) 
			set_Value (COLUMNNAME_C_ElementValue_BIE_ID, null);
		else 
			set_Value (COLUMNNAME_C_ElementValue_BIE_ID, Integer.valueOf(C_ElementValue_BIE_ID));
	}

	/** Get Account Bank Interest Expense.
		@return Account Bank Interest Expense	  */
	public int getC_ElementValue_BIE_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_ElementValue_BIE_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public org.compiere.model.I_C_ElementValue getC_ElementValue_BIR() throws RuntimeException
    {
		return (org.compiere.model.I_C_ElementValue)MTable.get(getCtx(), org.compiere.model.I_C_ElementValue.Table_Name)
			.getPO(getC_ElementValue_BIR_ID(), get_TrxName());	}

	/** Set Account Bank Interest Revenue.
		@param C_ElementValue_BIR_ID Account Bank Interest Revenue	  */
	public void setC_ElementValue_BIR_ID (int C_ElementValue_BIR_ID)
	{
		if (C_ElementValue_BIR_ID < 1) 
			set_Value (COLUMNNAME_C_ElementValue_BIR_ID, null);
		else 
			set_Value (COLUMNNAME_C_ElementValue_BIR_ID, Integer.valueOf(C_ElementValue_BIR_ID));
	}

	/** Get Account Bank Interest Revenue.
		@return Account Bank Interest Revenue	  */
	public int getC_ElementValue_BIR_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_ElementValue_BIR_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public org.compiere.model.I_C_ElementValue getC_ElementValue_BIT() throws RuntimeException
    {
		return (org.compiere.model.I_C_ElementValue)MTable.get(getCtx(), org.compiere.model.I_C_ElementValue.Table_Name)
			.getPO(getC_ElementValue_BIT_ID(), get_TrxName());	}

	/** Set Account Bank In Transit.
		@param C_ElementValue_BIT_ID Account Bank In Transit	  */
	public void setC_ElementValue_BIT_ID (int C_ElementValue_BIT_ID)
	{
		if (C_ElementValue_BIT_ID < 1) 
			set_Value (COLUMNNAME_C_ElementValue_BIT_ID, null);
		else 
			set_Value (COLUMNNAME_C_ElementValue_BIT_ID, Integer.valueOf(C_ElementValue_BIT_ID));
	}

	/** Get Account Bank In Transit.
		@return Account Bank In Transit	  */
	public int getC_ElementValue_BIT_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_ElementValue_BIT_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public org.compiere.model.I_C_ElementValue getC_ElementValue_CA() throws RuntimeException
    {
		return (org.compiere.model.I_C_ElementValue)MTable.get(getCtx(), org.compiere.model.I_C_ElementValue.Table_Name)
			.getPO(getC_ElementValue_CA_ID(), get_TrxName());	}

	/** Set Account Cost Adjustment.
		@param C_ElementValue_CA_ID Account Cost Adjustment	  */
	public void setC_ElementValue_CA_ID (int C_ElementValue_CA_ID)
	{
		if (C_ElementValue_CA_ID < 1) 
			set_Value (COLUMNNAME_C_ElementValue_CA_ID, null);
		else 
			set_Value (COLUMNNAME_C_ElementValue_CA_ID, Integer.valueOf(C_ElementValue_CA_ID));
	}

	/** Get Account Cost Adjustment.
		@return Account Cost Adjustment	  */
	public int getC_ElementValue_CA_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_ElementValue_CA_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public org.compiere.model.I_C_ElementValue getC_ElementValue_Charge() throws RuntimeException
    {
		return (org.compiere.model.I_C_ElementValue)MTable.get(getCtx(), org.compiere.model.I_C_ElementValue.Table_Name)
			.getPO(getC_ElementValue_Charge_ID(), get_TrxName());	}

	/** Set Account Charge.
		@param C_ElementValue_Charge_ID Account Charge	  */
	public void setC_ElementValue_Charge_ID (int C_ElementValue_Charge_ID)
	{
		if (C_ElementValue_Charge_ID < 1) 
			set_Value (COLUMNNAME_C_ElementValue_Charge_ID, null);
		else 
			set_Value (COLUMNNAME_C_ElementValue_Charge_ID, Integer.valueOf(C_ElementValue_Charge_ID));
	}

	/** Get Account Charge.
		@return Account Charge	  */
	public int getC_ElementValue_Charge_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_ElementValue_Charge_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public org.compiere.model.I_C_ElementValue getC_ElementValue_COGS() throws RuntimeException
    {
		return (org.compiere.model.I_C_ElementValue)MTable.get(getCtx(), org.compiere.model.I_C_ElementValue.Table_Name)
			.getPO(getC_ElementValue_COGS_ID(), get_TrxName());	}

	/** Set Account COGS.
		@param C_ElementValue_COGS_ID Account COGS	  */
	public void setC_ElementValue_COGS_ID (int C_ElementValue_COGS_ID)
	{
		if (C_ElementValue_COGS_ID < 1) 
			set_Value (COLUMNNAME_C_ElementValue_COGS_ID, null);
		else 
			set_Value (COLUMNNAME_C_ElementValue_COGS_ID, Integer.valueOf(C_ElementValue_COGS_ID));
	}

	/** Get Account COGS.
		@return Account COGS	  */
	public int getC_ElementValue_COGS_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_ElementValue_COGS_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public org.compiere.model.I_C_ElementValue getC_ElementValue_CP() throws RuntimeException
    {
		return (org.compiere.model.I_C_ElementValue)MTable.get(getCtx(), org.compiere.model.I_C_ElementValue.Table_Name)
			.getPO(getC_ElementValue_CP_ID(), get_TrxName());	}

	/** Set Account Customer Prepayment.
		@param C_ElementValue_CP_ID Account Customer Prepayment	  */
	public void setC_ElementValue_CP_ID (int C_ElementValue_CP_ID)
	{
		if (C_ElementValue_CP_ID < 1) 
			set_Value (COLUMNNAME_C_ElementValue_CP_ID, null);
		else 
			set_Value (COLUMNNAME_C_ElementValue_CP_ID, Integer.valueOf(C_ElementValue_CP_ID));
	}

	/** Get Account Customer Prepayment.
		@return Account Customer Prepayment	  */
	public int getC_ElementValue_CP_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_ElementValue_CP_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public org.compiere.model.I_C_ElementValue getC_ElementValue_CR() throws RuntimeException
    {
		return (org.compiere.model.I_C_ElementValue)MTable.get(getCtx(), org.compiere.model.I_C_ElementValue.Table_Name)
			.getPO(getC_ElementValue_CR_ID(), get_TrxName());	}

	/** Set Account Customer Receivables.
		@param C_ElementValue_CR_ID Account Customer Receivables	  */
	public void setC_ElementValue_CR_ID (int C_ElementValue_CR_ID)
	{
		if (C_ElementValue_CR_ID < 1) 
			set_Value (COLUMNNAME_C_ElementValue_CR_ID, null);
		else 
			set_Value (COLUMNNAME_C_ElementValue_CR_ID, Integer.valueOf(C_ElementValue_CR_ID));
	}

	/** Get Account Customer Receivables.
		@return Account Customer Receivables	  */
	public int getC_ElementValue_CR_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_ElementValue_CR_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public org.compiere.model.I_C_ElementValue getC_ElementValue_IC() throws RuntimeException
    {
		return (org.compiere.model.I_C_ElementValue)MTable.get(getCtx(), org.compiere.model.I_C_ElementValue.Table_Name)
			.getPO(getC_ElementValue_IC_ID(), get_TrxName());	}

	/** Set Account Inventory Clearing.
		@param C_ElementValue_IC_ID Account Inventory Clearing	  */
	public void setC_ElementValue_IC_ID (int C_ElementValue_IC_ID)
	{
		if (C_ElementValue_IC_ID < 1) 
			set_Value (COLUMNNAME_C_ElementValue_IC_ID, null);
		else 
			set_Value (COLUMNNAME_C_ElementValue_IC_ID, Integer.valueOf(C_ElementValue_IC_ID));
	}

	/** Get Account Inventory Clearing.
		@return Account Inventory Clearing	  */
	public int getC_ElementValue_IC_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_ElementValue_IC_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public org.compiere.model.I_C_ElementValue getC_ElementValue() throws RuntimeException
    {
		return (org.compiere.model.I_C_ElementValue)MTable.get(getCtx(), org.compiere.model.I_C_ElementValue.Table_Name)
			.getPO(getC_ElementValue_ID(), get_TrxName());	}

	/** Set Account Element.
		@param C_ElementValue_ID 
		Account Element
	  */
	public void setC_ElementValue_ID (int C_ElementValue_ID)
	{
		if (C_ElementValue_ID < 1) 
			set_Value (COLUMNNAME_C_ElementValue_ID, null);
		else 
			set_Value (COLUMNNAME_C_ElementValue_ID, Integer.valueOf(C_ElementValue_ID));
	}

	/** Get Account Element.
		@return Account Element
	  */
	public int getC_ElementValue_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_ElementValue_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public org.compiere.model.I_C_ElementValue getC_ElementValue_IPV() throws RuntimeException
    {
		return (org.compiere.model.I_C_ElementValue)MTable.get(getCtx(), org.compiere.model.I_C_ElementValue.Table_Name)
			.getPO(getC_ElementValue_IPV_ID(), get_TrxName());	}

	/** Set Account Invoice Price Variance.
		@param C_ElementValue_IPV_ID Account Invoice Price Variance	  */
	public void setC_ElementValue_IPV_ID (int C_ElementValue_IPV_ID)
	{
		if (C_ElementValue_IPV_ID < 1) 
			set_Value (COLUMNNAME_C_ElementValue_IPV_ID, null);
		else 
			set_Value (COLUMNNAME_C_ElementValue_IPV_ID, Integer.valueOf(C_ElementValue_IPV_ID));
	}

	/** Get Account Invoice Price Variance.
		@return Account Invoice Price Variance	  */
	public int getC_ElementValue_IPV_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_ElementValue_IPV_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public org.compiere.model.I_C_ElementValue getC_ElementValue_LCC() throws RuntimeException
    {
		return (org.compiere.model.I_C_ElementValue)MTable.get(getCtx(), org.compiere.model.I_C_ElementValue.Table_Name)
			.getPO(getC_ElementValue_LCC_ID(), get_TrxName());	}

	/** Set Account Landed Cost Clearing.
		@param C_ElementValue_LCC_ID Account Landed Cost Clearing	  */
	public void setC_ElementValue_LCC_ID (int C_ElementValue_LCC_ID)
	{
		if (C_ElementValue_LCC_ID < 1) 
			set_Value (COLUMNNAME_C_ElementValue_LCC_ID, null);
		else 
			set_Value (COLUMNNAME_C_ElementValue_LCC_ID, Integer.valueOf(C_ElementValue_LCC_ID));
	}

	/** Get Account Landed Cost Clearing.
		@return Account Landed Cost Clearing	  */
	public int getC_ElementValue_LCC_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_ElementValue_LCC_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public org.compiere.model.I_C_ElementValue getC_ElementValue_PA() throws RuntimeException
    {
		return (org.compiere.model.I_C_ElementValue)MTable.get(getCtx(), org.compiere.model.I_C_ElementValue.Table_Name)
			.getPO(getC_ElementValue_PA_ID(), get_TrxName());	}

	/** Set Account Project Asset.
		@param C_ElementValue_PA_ID Account Project Asset	  */
	public void setC_ElementValue_PA_ID (int C_ElementValue_PA_ID)
	{
		if (C_ElementValue_PA_ID < 1) 
			set_Value (COLUMNNAME_C_ElementValue_PA_ID, null);
		else 
			set_Value (COLUMNNAME_C_ElementValue_PA_ID, Integer.valueOf(C_ElementValue_PA_ID));
	}

	/** Get Account Project Asset.
		@return Account Project Asset	  */
	public int getC_ElementValue_PA_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_ElementValue_PA_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public org.compiere.model.I_C_ElementValue getC_ElementValue_PDE() throws RuntimeException
    {
		return (org.compiere.model.I_C_ElementValue)MTable.get(getCtx(), org.compiere.model.I_C_ElementValue.Table_Name)
			.getPO(getC_ElementValue_PDE_ID(), get_TrxName());	}

	/** Set Account Payment Discount Expense.
		@param C_ElementValue_PDE_ID Account Payment Discount Expense	  */
	public void setC_ElementValue_PDE_ID (int C_ElementValue_PDE_ID)
	{
		if (C_ElementValue_PDE_ID < 1) 
			set_Value (COLUMNNAME_C_ElementValue_PDE_ID, null);
		else 
			set_Value (COLUMNNAME_C_ElementValue_PDE_ID, Integer.valueOf(C_ElementValue_PDE_ID));
	}

	/** Get Account Payment Discount Expense.
		@return Account Payment Discount Expense	  */
	public int getC_ElementValue_PDE_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_ElementValue_PDE_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public org.compiere.model.I_C_ElementValue getC_ElementValue_PDR() throws RuntimeException
    {
		return (org.compiere.model.I_C_ElementValue)MTable.get(getCtx(), org.compiere.model.I_C_ElementValue.Table_Name)
			.getPO(getC_ElementValue_PDR_ID(), get_TrxName());	}

	/** Set Account Payment Discount Revenue.
		@param C_ElementValue_PDR_ID Account Payment Discount Revenue	  */
	public void setC_ElementValue_PDR_ID (int C_ElementValue_PDR_ID)
	{
		if (C_ElementValue_PDR_ID < 1) 
			set_Value (COLUMNNAME_C_ElementValue_PDR_ID, null);
		else 
			set_Value (COLUMNNAME_C_ElementValue_PDR_ID, Integer.valueOf(C_ElementValue_PDR_ID));
	}

	/** Get Account Payment Discount Revenue.
		@return Account Payment Discount Revenue	  */
	public int getC_ElementValue_PDR_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_ElementValue_PDR_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public org.compiere.model.I_C_ElementValue getC_ElementValue_PE() throws RuntimeException
    {
		return (org.compiere.model.I_C_ElementValue)MTable.get(getCtx(), org.compiere.model.I_C_ElementValue.Table_Name)
			.getPO(getC_ElementValue_PE_ID(), get_TrxName());	}

	/** Set Account Product Expense.
		@param C_ElementValue_PE_ID Account Product Expense	  */
	public void setC_ElementValue_PE_ID (int C_ElementValue_PE_ID)
	{
		if (C_ElementValue_PE_ID < 1) 
			set_Value (COLUMNNAME_C_ElementValue_PE_ID, null);
		else 
			set_Value (COLUMNNAME_C_ElementValue_PE_ID, Integer.valueOf(C_ElementValue_PE_ID));
	}

	/** Get Account Product Expense.
		@return Account Product Expense	  */
	public int getC_ElementValue_PE_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_ElementValue_PE_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public org.compiere.model.I_C_ElementValue getC_ElementValue_PPV() throws RuntimeException
    {
		return (org.compiere.model.I_C_ElementValue)MTable.get(getCtx(), org.compiere.model.I_C_ElementValue.Table_Name)
			.getPO(getC_ElementValue_PPV_ID(), get_TrxName());	}

	/** Set Account Purchase Price Variance.
		@param C_ElementValue_PPV_ID Account Purchase Price Variance	  */
	public void setC_ElementValue_PPV_ID (int C_ElementValue_PPV_ID)
	{
		if (C_ElementValue_PPV_ID < 1) 
			set_Value (COLUMNNAME_C_ElementValue_PPV_ID, null);
		else 
			set_Value (COLUMNNAME_C_ElementValue_PPV_ID, Integer.valueOf(C_ElementValue_PPV_ID));
	}

	/** Get Account Purchase Price Variance.
		@return Account Purchase Price Variance	  */
	public int getC_ElementValue_PPV_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_ElementValue_PPV_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public org.compiere.model.I_C_ElementValue getC_ElementValue_PS() throws RuntimeException
    {
		return (org.compiere.model.I_C_ElementValue)MTable.get(getCtx(), org.compiere.model.I_C_ElementValue.Table_Name)
			.getPO(getC_ElementValue_PS_ID(), get_TrxName());	}

	/** Set Account Payment Selection.
		@param C_ElementValue_PS_ID Account Payment Selection	  */
	public void setC_ElementValue_PS_ID (int C_ElementValue_PS_ID)
	{
		if (C_ElementValue_PS_ID < 1) 
			set_Value (COLUMNNAME_C_ElementValue_PS_ID, null);
		else 
			set_Value (COLUMNNAME_C_ElementValue_PS_ID, Integer.valueOf(C_ElementValue_PS_ID));
	}

	/** Get Account Payment Selection.
		@return Account Payment Selection	  */
	public int getC_ElementValue_PS_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_ElementValue_PS_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public org.compiere.model.I_C_ElementValue getC_ElementValue_Revenue() throws RuntimeException
    {
		return (org.compiere.model.I_C_ElementValue)MTable.get(getCtx(), org.compiere.model.I_C_ElementValue.Table_Name)
			.getPO(getC_ElementValue_Revenue_ID(), get_TrxName());	}

	/** Set Account Product Revenue.
		@param C_ElementValue_Revenue_ID Account Product Revenue	  */
	public void setC_ElementValue_Revenue_ID (int C_ElementValue_Revenue_ID)
	{
		if (C_ElementValue_Revenue_ID < 1) 
			set_Value (COLUMNNAME_C_ElementValue_Revenue_ID, null);
		else 
			set_Value (COLUMNNAME_C_ElementValue_Revenue_ID, Integer.valueOf(C_ElementValue_Revenue_ID));
	}

	/** Get Account Product Revenue.
		@return Account Product Revenue	  */
	public int getC_ElementValue_Revenue_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_ElementValue_Revenue_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public org.compiere.model.I_C_ElementValue getC_ElementValue_RG() throws RuntimeException
    {
		return (org.compiere.model.I_C_ElementValue)MTable.get(getCtx(), org.compiere.model.I_C_ElementValue.Table_Name)
			.getPO(getC_ElementValue_RG_ID(), get_TrxName());	}

	/** Set Account Realized Gain.
		@param C_ElementValue_RG_ID Account Realized Gain	  */
	public void setC_ElementValue_RG_ID (int C_ElementValue_RG_ID)
	{
		if (C_ElementValue_RG_ID < 1) 
			set_Value (COLUMNNAME_C_ElementValue_RG_ID, null);
		else 
			set_Value (COLUMNNAME_C_ElementValue_RG_ID, Integer.valueOf(C_ElementValue_RG_ID));
	}

	/** Get Account Realized Gain.
		@return Account Realized Gain	  */
	public int getC_ElementValue_RG_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_ElementValue_RG_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public org.compiere.model.I_C_ElementValue getC_ElementValue_RL() throws RuntimeException
    {
		return (org.compiere.model.I_C_ElementValue)MTable.get(getCtx(), org.compiere.model.I_C_ElementValue.Table_Name)
			.getPO(getC_ElementValue_RL_ID(), get_TrxName());	}

	/** Set Account Realized Loss.
		@param C_ElementValue_RL_ID Account Realized Loss	  */
	public void setC_ElementValue_RL_ID (int C_ElementValue_RL_ID)
	{
		if (C_ElementValue_RL_ID < 1) 
			set_Value (COLUMNNAME_C_ElementValue_RL_ID, null);
		else 
			set_Value (COLUMNNAME_C_ElementValue_RL_ID, Integer.valueOf(C_ElementValue_RL_ID));
	}

	/** Get Account Realized Loss.
		@return Account Realized Loss	  */
	public int getC_ElementValue_RL_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_ElementValue_RL_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public org.compiere.model.I_C_ElementValue getC_ElementValue_RV() throws RuntimeException
    {
		return (org.compiere.model.I_C_ElementValue)MTable.get(getCtx(), org.compiere.model.I_C_ElementValue.Table_Name)
			.getPO(getC_ElementValue_RV_ID(), get_TrxName());	}

	/** Set Account Rate Variance.
		@param C_ElementValue_RV_ID Account Rate Variance	  */
	public void setC_ElementValue_RV_ID (int C_ElementValue_RV_ID)
	{
		if (C_ElementValue_RV_ID < 1) 
			set_Value (COLUMNNAME_C_ElementValue_RV_ID, null);
		else 
			set_Value (COLUMNNAME_C_ElementValue_RV_ID, Integer.valueOf(C_ElementValue_RV_ID));
	}

	/** Get Account Rate Variance.
		@return Account Rate Variance	  */
	public int getC_ElementValue_RV_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_ElementValue_RV_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public org.compiere.model.I_C_ElementValue getC_ElementValue_TC() throws RuntimeException
    {
		return (org.compiere.model.I_C_ElementValue)MTable.get(getCtx(), org.compiere.model.I_C_ElementValue.Table_Name)
			.getPO(getC_ElementValue_TC_ID(), get_TrxName());	}

	/** Set Account Tax Credit.
		@param C_ElementValue_TC_ID Account Tax Credit	  */
	public void setC_ElementValue_TC_ID (int C_ElementValue_TC_ID)
	{
		if (C_ElementValue_TC_ID < 1) 
			set_Value (COLUMNNAME_C_ElementValue_TC_ID, null);
		else 
			set_Value (COLUMNNAME_C_ElementValue_TC_ID, Integer.valueOf(C_ElementValue_TC_ID));
	}

	/** Get Account Tax Credit.
		@return Account Tax Credit	  */
	public int getC_ElementValue_TC_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_ElementValue_TC_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public org.compiere.model.I_C_ElementValue getC_ElementValue_TD() throws RuntimeException
    {
		return (org.compiere.model.I_C_ElementValue)MTable.get(getCtx(), org.compiere.model.I_C_ElementValue.Table_Name)
			.getPO(getC_ElementValue_TD_ID(), get_TrxName());	}

	/** Set Account Tax Due.
		@param C_ElementValue_TD_ID Account Tax Due	  */
	public void setC_ElementValue_TD_ID (int C_ElementValue_TD_ID)
	{
		if (C_ElementValue_TD_ID < 1) 
			set_Value (COLUMNNAME_C_ElementValue_TD_ID, null);
		else 
			set_Value (COLUMNNAME_C_ElementValue_TD_ID, Integer.valueOf(C_ElementValue_TD_ID));
	}

	/** Get Account Tax Due.
		@return Account Tax Due	  */
	public int getC_ElementValue_TD_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_ElementValue_TD_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public org.compiere.model.I_C_ElementValue getC_ElementValue_TDG() throws RuntimeException
    {
		return (org.compiere.model.I_C_ElementValue)MTable.get(getCtx(), org.compiere.model.I_C_ElementValue.Table_Name)
			.getPO(getC_ElementValue_TDG_ID(), get_TrxName());	}

	/** Set Account Trade Discount Granted.
		@param C_ElementValue_TDG_ID Account Trade Discount Granted	  */
	public void setC_ElementValue_TDG_ID (int C_ElementValue_TDG_ID)
	{
		if (C_ElementValue_TDG_ID < 1) 
			set_Value (COLUMNNAME_C_ElementValue_TDG_ID, null);
		else 
			set_Value (COLUMNNAME_C_ElementValue_TDG_ID, Integer.valueOf(C_ElementValue_TDG_ID));
	}

	/** Get Account Trade Discount Granted.
		@return Account Trade Discount Granted	  */
	public int getC_ElementValue_TDG_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_ElementValue_TDG_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public org.compiere.model.I_C_ElementValue getC_ElementValue_TDR() throws RuntimeException
    {
		return (org.compiere.model.I_C_ElementValue)MTable.get(getCtx(), org.compiere.model.I_C_ElementValue.Table_Name)
			.getPO(getC_ElementValue_TDR_ID(), get_TrxName());	}

	/** Set Account Trade Discount Received.
		@param C_ElementValue_TDR_ID Account Trade Discount Received	  */
	public void setC_ElementValue_TDR_ID (int C_ElementValue_TDR_ID)
	{
		if (C_ElementValue_TDR_ID < 1) 
			set_Value (COLUMNNAME_C_ElementValue_TDR_ID, null);
		else 
			set_Value (COLUMNNAME_C_ElementValue_TDR_ID, Integer.valueOf(C_ElementValue_TDR_ID));
	}

	/** Get Account Trade Discount Received.
		@return Account Trade Discount Received	  */
	public int getC_ElementValue_TDR_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_ElementValue_TDR_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public org.compiere.model.I_C_ElementValue getC_ElementValue_TE() throws RuntimeException
    {
		return (org.compiere.model.I_C_ElementValue)MTable.get(getCtx(), org.compiere.model.I_C_ElementValue.Table_Name)
			.getPO(getC_ElementValue_TE_ID(), get_TrxName());	}

	/** Set Account Tax Expense.
		@param C_ElementValue_TE_ID Account Tax Expense	  */
	public void setC_ElementValue_TE_ID (int C_ElementValue_TE_ID)
	{
		if (C_ElementValue_TE_ID < 1) 
			set_Value (COLUMNNAME_C_ElementValue_TE_ID, null);
		else 
			set_Value (COLUMNNAME_C_ElementValue_TE_ID, Integer.valueOf(C_ElementValue_TE_ID));
	}

	/** Get Account Tax Expense.
		@return Account Tax Expense	  */
	public int getC_ElementValue_TE_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_ElementValue_TE_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public org.compiere.model.I_C_ElementValue getC_ElementValue_UC() throws RuntimeException
    {
		return (org.compiere.model.I_C_ElementValue)MTable.get(getCtx(), org.compiere.model.I_C_ElementValue.Table_Name)
			.getPO(getC_ElementValue_UC_ID(), get_TrxName());	}

	/** Set Account Unallocated Cash.
		@param C_ElementValue_UC_ID Account Unallocated Cash	  */
	public void setC_ElementValue_UC_ID (int C_ElementValue_UC_ID)
	{
		if (C_ElementValue_UC_ID < 1) 
			set_Value (COLUMNNAME_C_ElementValue_UC_ID, null);
		else 
			set_Value (COLUMNNAME_C_ElementValue_UC_ID, Integer.valueOf(C_ElementValue_UC_ID));
	}

	/** Get Account Unallocated Cash.
		@return Account Unallocated Cash	  */
	public int getC_ElementValue_UC_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_ElementValue_UC_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public org.compiere.model.I_C_ElementValue getC_ElementValue_UG() throws RuntimeException
    {
		return (org.compiere.model.I_C_ElementValue)MTable.get(getCtx(), org.compiere.model.I_C_ElementValue.Table_Name)
			.getPO(getC_ElementValue_UG_ID(), get_TrxName());	}

	/** Set Account Unrealized Gain.
		@param C_ElementValue_UG_ID Account Unrealized Gain	  */
	public void setC_ElementValue_UG_ID (int C_ElementValue_UG_ID)
	{
		if (C_ElementValue_UG_ID < 1) 
			set_Value (COLUMNNAME_C_ElementValue_UG_ID, null);
		else 
			set_Value (COLUMNNAME_C_ElementValue_UG_ID, Integer.valueOf(C_ElementValue_UG_ID));
	}

	/** Get Account Unrealized Gain.
		@return Account Unrealized Gain	  */
	public int getC_ElementValue_UG_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_ElementValue_UG_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public org.compiere.model.I_C_ElementValue getC_ElementValue_UL() throws RuntimeException
    {
		return (org.compiere.model.I_C_ElementValue)MTable.get(getCtx(), org.compiere.model.I_C_ElementValue.Table_Name)
			.getPO(getC_ElementValue_UL_ID(), get_TrxName());	}

	/** Set Account Unrealized Loss.
		@param C_ElementValue_UL_ID Account Unrealized Loss	  */
	public void setC_ElementValue_UL_ID (int C_ElementValue_UL_ID)
	{
		if (C_ElementValue_UL_ID < 1) 
			set_Value (COLUMNNAME_C_ElementValue_UL_ID, null);
		else 
			set_Value (COLUMNNAME_C_ElementValue_UL_ID, Integer.valueOf(C_ElementValue_UL_ID));
	}

	/** Get Account Unrealized Loss.
		@return Account Unrealized Loss	  */
	public int getC_ElementValue_UL_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_ElementValue_UL_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public org.compiere.model.I_C_ElementValue getC_ElementValue_VL() throws RuntimeException
    {
		return (org.compiere.model.I_C_ElementValue)MTable.get(getCtx(), org.compiere.model.I_C_ElementValue.Table_Name)
			.getPO(getC_ElementValue_VL_ID(), get_TrxName());	}

	/** Set Account Vendor Liablity.
		@param C_ElementValue_VL_ID Account Vendor Liablity	  */
	public void setC_ElementValue_VL_ID (int C_ElementValue_VL_ID)
	{
		if (C_ElementValue_VL_ID < 1) 
			set_Value (COLUMNNAME_C_ElementValue_VL_ID, null);
		else 
			set_Value (COLUMNNAME_C_ElementValue_VL_ID, Integer.valueOf(C_ElementValue_VL_ID));
	}

	/** Get Account Vendor Liablity.
		@return Account Vendor Liablity	  */
	public int getC_ElementValue_VL_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_ElementValue_VL_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public org.compiere.model.I_C_ElementValue getC_ElementValue_VP() throws RuntimeException
    {
		return (org.compiere.model.I_C_ElementValue)MTable.get(getCtx(), org.compiere.model.I_C_ElementValue.Table_Name)
			.getPO(getC_ElementValue_VP_ID(), get_TrxName());	}

	/** Set Account Vendor Prepayment.
		@param C_ElementValue_VP_ID Account Vendor Prepayment	  */
	public void setC_ElementValue_VP_ID (int C_ElementValue_VP_ID)
	{
		if (C_ElementValue_VP_ID < 1) 
			set_Value (COLUMNNAME_C_ElementValue_VP_ID, null);
		else 
			set_Value (COLUMNNAME_C_ElementValue_VP_ID, Integer.valueOf(C_ElementValue_VP_ID));
	}

	/** Get Account Vendor Prepayment.
		@return Account Vendor Prepayment	  */
	public int getC_ElementValue_VP_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_ElementValue_VP_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public org.compiere.model.I_C_ElementValue getC_ElementValue_WD() throws RuntimeException
    {
		return (org.compiere.model.I_C_ElementValue)MTable.get(getCtx(), org.compiere.model.I_C_ElementValue.Table_Name)
			.getPO(getC_ElementValue_WD_ID(), get_TrxName());	}

	/** Set Account Warehouse Differences.
		@param C_ElementValue_WD_ID Account Warehouse Differences	  */
	public void setC_ElementValue_WD_ID (int C_ElementValue_WD_ID)
	{
		if (C_ElementValue_WD_ID < 1) 
			set_Value (COLUMNNAME_C_ElementValue_WD_ID, null);
		else 
			set_Value (COLUMNNAME_C_ElementValue_WD_ID, Integer.valueOf(C_ElementValue_WD_ID));
	}

	/** Get Account Warehouse Differences.
		@return Account Warehouse Differences	  */
	public int getC_ElementValue_WD_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_ElementValue_WD_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public org.compiere.model.I_C_ElementValue getC_ElementValue_WIP() throws RuntimeException
    {
		return (org.compiere.model.I_C_ElementValue)MTable.get(getCtx(), org.compiere.model.I_C_ElementValue.Table_Name)
			.getPO(getC_ElementValue_WIP_ID(), get_TrxName());	}

	/** Set Account Work In Progress.
		@param C_ElementValue_WIP_ID Account Work In Progress	  */
	public void setC_ElementValue_WIP_ID (int C_ElementValue_WIP_ID)
	{
		if (C_ElementValue_WIP_ID < 1) 
			set_Value (COLUMNNAME_C_ElementValue_WIP_ID, null);
		else 
			set_Value (COLUMNNAME_C_ElementValue_WIP_ID, Integer.valueOf(C_ElementValue_WIP_ID));
	}

	/** Get Account Work In Progress.
		@return Account Work In Progress	  */
	public int getC_ElementValue_WIP_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_ElementValue_WIP_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public org.compiere.model.I_C_ElementValue getC_ElementValue_WO() throws RuntimeException
    {
		return (org.compiere.model.I_C_ElementValue)MTable.get(getCtx(), org.compiere.model.I_C_ElementValue.Table_Name)
			.getPO(getC_ElementValue_WO_ID(), get_TrxName());	}

	/** Set Account Write-Off.
		@param C_ElementValue_WO_ID Account Write-Off	  */
	public void setC_ElementValue_WO_ID (int C_ElementValue_WO_ID)
	{
		if (C_ElementValue_WO_ID < 1) 
			set_Value (COLUMNNAME_C_ElementValue_WO_ID, null);
		else 
			set_Value (COLUMNNAME_C_ElementValue_WO_ID, Integer.valueOf(C_ElementValue_WO_ID));
	}

	/** Get Account Write-Off.
		@return Account Write-Off	  */
	public int getC_ElementValue_WO_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_ElementValue_WO_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_C_ValidCombination getC_Prepayment_A() throws RuntimeException
    {
		return (I_C_ValidCombination)MTable.get(getCtx(), I_C_ValidCombination.Table_Name)
			.getPO(getC_Prepayment_Acct(), get_TrxName());	}

	/** Set Customer Prepayment.
		@param C_Prepayment_Acct 
		Account for customer prepayments
	  */
	public void setC_Prepayment_Acct (int C_Prepayment_Acct)
	{
		set_Value (COLUMNNAME_C_Prepayment_Acct, Integer.valueOf(C_Prepayment_Acct));
	}

	/** Get Customer Prepayment.
		@return Account for customer prepayments
	  */
	public int getC_Prepayment_Acct () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_Prepayment_Acct);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_C_ValidCombination getC_Receivable_A() throws RuntimeException
    {
		return (I_C_ValidCombination)MTable.get(getCtx(), I_C_ValidCombination.Table_Name)
			.getPO(getC_Receivable_Acct(), get_TrxName());	}

	/** Set Customer Receivables.
		@param C_Receivable_Acct 
		Account for Customer Receivables
	  */
	public void setC_Receivable_Acct (int C_Receivable_Acct)
	{
		set_Value (COLUMNNAME_C_Receivable_Acct, Integer.valueOf(C_Receivable_Acct));
	}

	/** Get Customer Receivables.
		@return Account for Customer Receivables
	  */
	public int getC_Receivable_Acct () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_Receivable_Acct);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_C_ValidCombination getC_Receivable_Services_A() throws RuntimeException
    {
		return (I_C_ValidCombination)MTable.get(getCtx(), I_C_ValidCombination.Table_Name)
			.getPO(getC_Receivable_Services_Acct(), get_TrxName());	}

	/** Set Receivable Services.
		@param C_Receivable_Services_Acct 
		Customer Accounts Receivables Services Account
	  */
	public void setC_Receivable_Services_Acct (int C_Receivable_Services_Acct)
	{
		set_Value (COLUMNNAME_C_Receivable_Services_Acct, Integer.valueOf(C_Receivable_Services_Acct));
	}

	/** Get Receivable Services.
		@return Customer Accounts Receivables Services Account
	  */
	public int getC_Receivable_Services_Acct () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_Receivable_Services_Acct);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_C_ValidCombination getCB_Asset_A() throws RuntimeException
    {
		return (I_C_ValidCombination)MTable.get(getCtx(), I_C_ValidCombination.Table_Name)
			.getPO(getCB_Asset_Acct(), get_TrxName());	}

	/** Set Cash Book Asset.
		@param CB_Asset_Acct 
		Cash Book Asset Account
	  */
	public void setCB_Asset_Acct (int CB_Asset_Acct)
	{
		set_Value (COLUMNNAME_CB_Asset_Acct, Integer.valueOf(CB_Asset_Acct));
	}

	/** Get Cash Book Asset.
		@return Cash Book Asset Account
	  */
	public int getCB_Asset_Acct () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_CB_Asset_Acct);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_C_ValidCombination getCB_CashTransfer_A() throws RuntimeException
    {
		return (I_C_ValidCombination)MTable.get(getCtx(), I_C_ValidCombination.Table_Name)
			.getPO(getCB_CashTransfer_Acct(), get_TrxName());	}

	/** Set Cash Transfer.
		@param CB_CashTransfer_Acct 
		Cash Transfer Clearing Account
	  */
	public void setCB_CashTransfer_Acct (int CB_CashTransfer_Acct)
	{
		set_Value (COLUMNNAME_CB_CashTransfer_Acct, Integer.valueOf(CB_CashTransfer_Acct));
	}

	/** Get Cash Transfer.
		@return Cash Transfer Clearing Account
	  */
	public int getCB_CashTransfer_Acct () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_CB_CashTransfer_Acct);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_C_ValidCombination getCB_Differences_A() throws RuntimeException
    {
		return (I_C_ValidCombination)MTable.get(getCtx(), I_C_ValidCombination.Table_Name)
			.getPO(getCB_Differences_Acct(), get_TrxName());	}

	/** Set Cash Book Differences.
		@param CB_Differences_Acct 
		Cash Book Differences Account
	  */
	public void setCB_Differences_Acct (int CB_Differences_Acct)
	{
		set_Value (COLUMNNAME_CB_Differences_Acct, Integer.valueOf(CB_Differences_Acct));
	}

	/** Get Cash Book Differences.
		@return Cash Book Differences Account
	  */
	public int getCB_Differences_Acct () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_CB_Differences_Acct);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_C_ValidCombination getCB_Expense_A() throws RuntimeException
    {
		return (I_C_ValidCombination)MTable.get(getCtx(), I_C_ValidCombination.Table_Name)
			.getPO(getCB_Expense_Acct(), get_TrxName());	}

	/** Set Cash Book Expense.
		@param CB_Expense_Acct 
		Cash Book Expense Account
	  */
	public void setCB_Expense_Acct (int CB_Expense_Acct)
	{
		set_Value (COLUMNNAME_CB_Expense_Acct, Integer.valueOf(CB_Expense_Acct));
	}

	/** Get Cash Book Expense.
		@return Cash Book Expense Account
	  */
	public int getCB_Expense_Acct () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_CB_Expense_Acct);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_C_ValidCombination getCB_Receipt_A() throws RuntimeException
    {
		return (I_C_ValidCombination)MTable.get(getCtx(), I_C_ValidCombination.Table_Name)
			.getPO(getCB_Receipt_Acct(), get_TrxName());	}

	/** Set Cash Book Receipt.
		@param CB_Receipt_Acct 
		Cash Book Receipts Account
	  */
	public void setCB_Receipt_Acct (int CB_Receipt_Acct)
	{
		set_Value (COLUMNNAME_CB_Receipt_Acct, Integer.valueOf(CB_Receipt_Acct));
	}

	/** Get Cash Book Receipt.
		@return Cash Book Receipts Account
	  */
	public int getCB_Receipt_Acct () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_CB_Receipt_Acct);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_C_ValidCombination getCh_Expense_A() throws RuntimeException
    {
		return (I_C_ValidCombination)MTable.get(getCtx(), I_C_ValidCombination.Table_Name)
			.getPO(getCh_Expense_Acct(), get_TrxName());	}

	/** Set Charge Account.
		@param Ch_Expense_Acct 
		Charge Account
	  */
	public void setCh_Expense_Acct (int Ch_Expense_Acct)
	{
		set_Value (COLUMNNAME_Ch_Expense_Acct, Integer.valueOf(Ch_Expense_Acct));
	}

	/** Get Charge Account.
		@return Charge Account
	  */
	public int getCh_Expense_Acct () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_Ch_Expense_Acct);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_C_ValidCombination getNotInvoicedReceipts_A() throws RuntimeException
    {
		return (I_C_ValidCombination)MTable.get(getCtx(), I_C_ValidCombination.Table_Name)
			.getPO(getNotInvoicedReceipts_Acct(), get_TrxName());	}

	/** Set Not-invoiced Receipts.
		@param NotInvoicedReceipts_Acct 
		Account for not-invoiced Material Receipts
	  */
	public void setNotInvoicedReceipts_Acct (int NotInvoicedReceipts_Acct)
	{
		set_Value (COLUMNNAME_NotInvoicedReceipts_Acct, Integer.valueOf(NotInvoicedReceipts_Acct));
	}

	/** Get Not-invoiced Receipts.
		@return Account for not-invoiced Material Receipts
	  */
	public int getNotInvoicedReceipts_Acct () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_NotInvoicedReceipts_Acct);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_C_ValidCombination getP_Asset_A() throws RuntimeException
    {
		return (I_C_ValidCombination)MTable.get(getCtx(), I_C_ValidCombination.Table_Name)
			.getPO(getP_Asset_Acct(), get_TrxName());	}

	/** Set Product Asset.
		@param P_Asset_Acct 
		Account for Product Asset (Inventory)
	  */
	public void setP_Asset_Acct (int P_Asset_Acct)
	{
		set_Value (COLUMNNAME_P_Asset_Acct, Integer.valueOf(P_Asset_Acct));
	}

	/** Get Product Asset.
		@return Account for Product Asset (Inventory)
	  */
	public int getP_Asset_Acct () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_P_Asset_Acct);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_C_ValidCombination getP_AverageCostVariance_A() throws RuntimeException
    {
		return (I_C_ValidCombination)MTable.get(getCtx(), I_C_ValidCombination.Table_Name)
			.getPO(getP_AverageCostVariance_Acct(), get_TrxName());	}

	/** Set Average Cost Variance.
		@param P_AverageCostVariance_Acct 
		Average Cost Variance
	  */
	public void setP_AverageCostVariance_Acct (int P_AverageCostVariance_Acct)
	{
		set_Value (COLUMNNAME_P_AverageCostVariance_Acct, Integer.valueOf(P_AverageCostVariance_Acct));
	}

	/** Get Average Cost Variance.
		@return Average Cost Variance
	  */
	public int getP_AverageCostVariance_Acct () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_P_AverageCostVariance_Acct);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_C_ValidCombination getP_COGS_A() throws RuntimeException
    {
		return (I_C_ValidCombination)MTable.get(getCtx(), I_C_ValidCombination.Table_Name)
			.getPO(getP_COGS_Acct(), get_TrxName());	}

	/** Set Product COGS.
		@param P_COGS_Acct 
		Account for Cost of Goods Sold
	  */
	public void setP_COGS_Acct (int P_COGS_Acct)
	{
		set_Value (COLUMNNAME_P_COGS_Acct, Integer.valueOf(P_COGS_Acct));
	}

	/** Get Product COGS.
		@return Account for Cost of Goods Sold
	  */
	public int getP_COGS_Acct () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_P_COGS_Acct);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_C_ValidCombination getP_CostAdjustment_A() throws RuntimeException
    {
		return (I_C_ValidCombination)MTable.get(getCtx(), I_C_ValidCombination.Table_Name)
			.getPO(getP_CostAdjustment_Acct(), get_TrxName());	}

	/** Set Cost Adjustment.
		@param P_CostAdjustment_Acct 
		Product Cost Adjustment Account
	  */
	public void setP_CostAdjustment_Acct (int P_CostAdjustment_Acct)
	{
		set_Value (COLUMNNAME_P_CostAdjustment_Acct, Integer.valueOf(P_CostAdjustment_Acct));
	}

	/** Get Cost Adjustment.
		@return Product Cost Adjustment Account
	  */
	public int getP_CostAdjustment_Acct () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_P_CostAdjustment_Acct);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_C_ValidCombination getP_Expense_A() throws RuntimeException
    {
		return (I_C_ValidCombination)MTable.get(getCtx(), I_C_ValidCombination.Table_Name)
			.getPO(getP_Expense_Acct(), get_TrxName());	}

	/** Set Product Expense.
		@param P_Expense_Acct 
		Account for Product Expense
	  */
	public void setP_Expense_Acct (int P_Expense_Acct)
	{
		set_Value (COLUMNNAME_P_Expense_Acct, Integer.valueOf(P_Expense_Acct));
	}

	/** Get Product Expense.
		@return Account for Product Expense
	  */
	public int getP_Expense_Acct () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_P_Expense_Acct);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_C_ValidCombination getP_InventoryClearing_A() throws RuntimeException
    {
		return (I_C_ValidCombination)MTable.get(getCtx(), I_C_ValidCombination.Table_Name)
			.getPO(getP_InventoryClearing_Acct(), get_TrxName());	}

	/** Set Inventory Clearing.
		@param P_InventoryClearing_Acct 
		Product Inventory Clearing Account
	  */
	public void setP_InventoryClearing_Acct (int P_InventoryClearing_Acct)
	{
		set_Value (COLUMNNAME_P_InventoryClearing_Acct, Integer.valueOf(P_InventoryClearing_Acct));
	}

	/** Get Inventory Clearing.
		@return Product Inventory Clearing Account
	  */
	public int getP_InventoryClearing_Acct () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_P_InventoryClearing_Acct);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_C_ValidCombination getP_InvoicePriceVariance_A() throws RuntimeException
    {
		return (I_C_ValidCombination)MTable.get(getCtx(), I_C_ValidCombination.Table_Name)
			.getPO(getP_InvoicePriceVariance_Acct(), get_TrxName());	}

	/** Set Invoice Price Variance.
		@param P_InvoicePriceVariance_Acct 
		Difference between Costs and Invoice Price (IPV)
	  */
	public void setP_InvoicePriceVariance_Acct (int P_InvoicePriceVariance_Acct)
	{
		set_Value (COLUMNNAME_P_InvoicePriceVariance_Acct, Integer.valueOf(P_InvoicePriceVariance_Acct));
	}

	/** Get Invoice Price Variance.
		@return Difference between Costs and Invoice Price (IPV)
	  */
	public int getP_InvoicePriceVariance_Acct () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_P_InvoicePriceVariance_Acct);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_C_ValidCombination getP_LandedCostClearing_A() throws RuntimeException
    {
		return (I_C_ValidCombination)MTable.get(getCtx(), I_C_ValidCombination.Table_Name)
			.getPO(getP_LandedCostClearing_Acct(), get_TrxName());	}

	/** Set Landed Cost Clearing.
		@param P_LandedCostClearing_Acct 
		Product Landed Cost Clearing Account
	  */
	public void setP_LandedCostClearing_Acct (int P_LandedCostClearing_Acct)
	{
		set_Value (COLUMNNAME_P_LandedCostClearing_Acct, Integer.valueOf(P_LandedCostClearing_Acct));
	}

	/** Get Landed Cost Clearing.
		@return Product Landed Cost Clearing Account
	  */
	public int getP_LandedCostClearing_Acct () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_P_LandedCostClearing_Acct);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_C_ValidCombination getP_PurchasePriceVariance_A() throws RuntimeException
    {
		return (I_C_ValidCombination)MTable.get(getCtx(), I_C_ValidCombination.Table_Name)
			.getPO(getP_PurchasePriceVariance_Acct(), get_TrxName());	}

	/** Set Purchase Price Variance.
		@param P_PurchasePriceVariance_Acct 
		Difference between Standard Cost and Purchase Price (PPV)
	  */
	public void setP_PurchasePriceVariance_Acct (int P_PurchasePriceVariance_Acct)
	{
		set_Value (COLUMNNAME_P_PurchasePriceVariance_Acct, Integer.valueOf(P_PurchasePriceVariance_Acct));
	}

	/** Get Purchase Price Variance.
		@return Difference between Standard Cost and Purchase Price (PPV)
	  */
	public int getP_PurchasePriceVariance_Acct () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_P_PurchasePriceVariance_Acct);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_C_ValidCombination getP_RateVariance_A() throws RuntimeException
    {
		return (I_C_ValidCombination)MTable.get(getCtx(), I_C_ValidCombination.Table_Name)
			.getPO(getP_RateVariance_Acct(), get_TrxName());	}

	/** Set Rate Variance.
		@param P_RateVariance_Acct 
		The Rate Variance account is the account used Manufacturing Order
	  */
	public void setP_RateVariance_Acct (int P_RateVariance_Acct)
	{
		set_Value (COLUMNNAME_P_RateVariance_Acct, Integer.valueOf(P_RateVariance_Acct));
	}

	/** Get Rate Variance.
		@return The Rate Variance account is the account used Manufacturing Order
	  */
	public int getP_RateVariance_Acct () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_P_RateVariance_Acct);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_C_ValidCombination getP_Revenue_A() throws RuntimeException
    {
		return (I_C_ValidCombination)MTable.get(getCtx(), I_C_ValidCombination.Table_Name)
			.getPO(getP_Revenue_Acct(), get_TrxName());	}

	/** Set Product Revenue.
		@param P_Revenue_Acct 
		Account for Product Revenue (Sales Account)
	  */
	public void setP_Revenue_Acct (int P_Revenue_Acct)
	{
		set_Value (COLUMNNAME_P_Revenue_Acct, Integer.valueOf(P_Revenue_Acct));
	}

	/** Get Product Revenue.
		@return Account for Product Revenue (Sales Account)
	  */
	public int getP_Revenue_Acct () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_P_Revenue_Acct);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_C_ValidCombination getP_TradeDiscountGrant_A() throws RuntimeException
    {
		return (I_C_ValidCombination)MTable.get(getCtx(), I_C_ValidCombination.Table_Name)
			.getPO(getP_TradeDiscountGrant_Acct(), get_TrxName());	}

	/** Set Trade Discount Granted.
		@param P_TradeDiscountGrant_Acct 
		Trade Discount Granted Account
	  */
	public void setP_TradeDiscountGrant_Acct (int P_TradeDiscountGrant_Acct)
	{
		set_Value (COLUMNNAME_P_TradeDiscountGrant_Acct, Integer.valueOf(P_TradeDiscountGrant_Acct));
	}

	/** Get Trade Discount Granted.
		@return Trade Discount Granted Account
	  */
	public int getP_TradeDiscountGrant_Acct () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_P_TradeDiscountGrant_Acct);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_C_ValidCombination getP_TradeDiscountRec_A() throws RuntimeException
    {
		return (I_C_ValidCombination)MTable.get(getCtx(), I_C_ValidCombination.Table_Name)
			.getPO(getP_TradeDiscountRec_Acct(), get_TrxName());	}

	/** Set Trade Discount Received.
		@param P_TradeDiscountRec_Acct 
		Trade Discount Receivable Account
	  */
	public void setP_TradeDiscountRec_Acct (int P_TradeDiscountRec_Acct)
	{
		set_Value (COLUMNNAME_P_TradeDiscountRec_Acct, Integer.valueOf(P_TradeDiscountRec_Acct));
	}

	/** Get Trade Discount Received.
		@return Trade Discount Receivable Account
	  */
	public int getP_TradeDiscountRec_Acct () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_P_TradeDiscountRec_Acct);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_C_ValidCombination getPayDiscount_Exp_A() throws RuntimeException
    {
		return (I_C_ValidCombination)MTable.get(getCtx(), I_C_ValidCombination.Table_Name)
			.getPO(getPayDiscount_Exp_Acct(), get_TrxName());	}

	/** Set Payment Discount Expense.
		@param PayDiscount_Exp_Acct 
		Payment Discount Expense Account
	  */
	public void setPayDiscount_Exp_Acct (int PayDiscount_Exp_Acct)
	{
		set_Value (COLUMNNAME_PayDiscount_Exp_Acct, Integer.valueOf(PayDiscount_Exp_Acct));
	}

	/** Get Payment Discount Expense.
		@return Payment Discount Expense Account
	  */
	public int getPayDiscount_Exp_Acct () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_PayDiscount_Exp_Acct);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_C_ValidCombination getPayDiscount_Rev_A() throws RuntimeException
    {
		return (I_C_ValidCombination)MTable.get(getCtx(), I_C_ValidCombination.Table_Name)
			.getPO(getPayDiscount_Rev_Acct(), get_TrxName());	}

	/** Set Payment Discount Revenue.
		@param PayDiscount_Rev_Acct 
		Payment Discount Revenue Account
	  */
	public void setPayDiscount_Rev_Acct (int PayDiscount_Rev_Acct)
	{
		set_Value (COLUMNNAME_PayDiscount_Rev_Acct, Integer.valueOf(PayDiscount_Rev_Acct));
	}

	/** Get Payment Discount Revenue.
		@return Payment Discount Revenue Account
	  */
	public int getPayDiscount_Rev_Acct () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_PayDiscount_Rev_Acct);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_C_ValidCombination getPJ_Asset_A() throws RuntimeException
    {
		return (I_C_ValidCombination)MTable.get(getCtx(), I_C_ValidCombination.Table_Name)
			.getPO(getPJ_Asset_Acct(), get_TrxName());	}

	/** Set Project Asset.
		@param PJ_Asset_Acct 
		Project Asset Account
	  */
	public void setPJ_Asset_Acct (int PJ_Asset_Acct)
	{
		set_Value (COLUMNNAME_PJ_Asset_Acct, Integer.valueOf(PJ_Asset_Acct));
	}

	/** Get Project Asset.
		@return Project Asset Account
	  */
	public int getPJ_Asset_Acct () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_PJ_Asset_Acct);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_C_ValidCombination getPJ_WIP_A() throws RuntimeException
    {
		return (I_C_ValidCombination)MTable.get(getCtx(), I_C_ValidCombination.Table_Name)
			.getPO(getPJ_WIP_Acct(), get_TrxName());	}

	/** Set Work In Progress.
		@param PJ_WIP_Acct 
		Account for Work in Progress
	  */
	public void setPJ_WIP_Acct (int PJ_WIP_Acct)
	{
		set_Value (COLUMNNAME_PJ_WIP_Acct, Integer.valueOf(PJ_WIP_Acct));
	}

	/** Get Work In Progress.
		@return Account for Work in Progress
	  */
	public int getPJ_WIP_Acct () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_PJ_WIP_Acct);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Process Now.
		@param Processing Process Now	  */
	public void setProcessing (boolean Processing)
	{
		set_Value (COLUMNNAME_Processing, Boolean.valueOf(Processing));
	}

	/** Get Process Now.
		@return Process Now	  */
	public boolean isProcessing () 
	{
		Object oo = get_Value(COLUMNNAME_Processing);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	public I_C_ValidCombination getRealizedGain_A() throws RuntimeException
    {
		return (I_C_ValidCombination)MTable.get(getCtx(), I_C_ValidCombination.Table_Name)
			.getPO(getRealizedGain_Acct(), get_TrxName());	}

	/** Set Realized Gain Acct.
		@param RealizedGain_Acct 
		Realized Gain Account
	  */
	public void setRealizedGain_Acct (int RealizedGain_Acct)
	{
		set_Value (COLUMNNAME_RealizedGain_Acct, Integer.valueOf(RealizedGain_Acct));
	}

	/** Get Realized Gain Acct.
		@return Realized Gain Account
	  */
	public int getRealizedGain_Acct () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_RealizedGain_Acct);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_C_ValidCombination getRealizedLoss_A() throws RuntimeException
    {
		return (I_C_ValidCombination)MTable.get(getCtx(), I_C_ValidCombination.Table_Name)
			.getPO(getRealizedLoss_Acct(), get_TrxName());	}

	/** Set Realized Loss Acct.
		@param RealizedLoss_Acct 
		Realized Loss Account
	  */
	public void setRealizedLoss_Acct (int RealizedLoss_Acct)
	{
		set_Value (COLUMNNAME_RealizedLoss_Acct, Integer.valueOf(RealizedLoss_Acct));
	}

	/** Get Realized Loss Acct.
		@return Realized Loss Account
	  */
	public int getRealizedLoss_Acct () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_RealizedLoss_Acct);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_C_ValidCombination getT_Credit_A() throws RuntimeException
    {
		return (I_C_ValidCombination)MTable.get(getCtx(), I_C_ValidCombination.Table_Name)
			.getPO(getT_Credit_Acct(), get_TrxName());	}

	/** Set Tax Credit.
		@param T_Credit_Acct 
		Account for Tax you can reclaim
	  */
	public void setT_Credit_Acct (int T_Credit_Acct)
	{
		set_Value (COLUMNNAME_T_Credit_Acct, Integer.valueOf(T_Credit_Acct));
	}

	/** Get Tax Credit.
		@return Account for Tax you can reclaim
	  */
	public int getT_Credit_Acct () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_T_Credit_Acct);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_C_ValidCombination getT_Due_A() throws RuntimeException
    {
		return (I_C_ValidCombination)MTable.get(getCtx(), I_C_ValidCombination.Table_Name)
			.getPO(getT_Due_Acct(), get_TrxName());	}

	/** Set Tax Due.
		@param T_Due_Acct 
		Account for Tax you have to pay
	  */
	public void setT_Due_Acct (int T_Due_Acct)
	{
		set_Value (COLUMNNAME_T_Due_Acct, Integer.valueOf(T_Due_Acct));
	}

	/** Get Tax Due.
		@return Account for Tax you have to pay
	  */
	public int getT_Due_Acct () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_T_Due_Acct);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_C_ValidCombination getT_Expense_A() throws RuntimeException
    {
		return (I_C_ValidCombination)MTable.get(getCtx(), I_C_ValidCombination.Table_Name)
			.getPO(getT_Expense_Acct(), get_TrxName());	}

	/** Set Tax Expense.
		@param T_Expense_Acct 
		Account for paid tax you cannot reclaim
	  */
	public void setT_Expense_Acct (int T_Expense_Acct)
	{
		set_Value (COLUMNNAME_T_Expense_Acct, Integer.valueOf(T_Expense_Acct));
	}

	/** Get Tax Expense.
		@return Account for paid tax you cannot reclaim
	  */
	public int getT_Expense_Acct () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_T_Expense_Acct);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_C_ValidCombination getUnEarnedRevenue_A() throws RuntimeException
    {
		return (I_C_ValidCombination)MTable.get(getCtx(), I_C_ValidCombination.Table_Name)
			.getPO(getUnEarnedRevenue_Acct(), get_TrxName());	}

	/** Set Unearned Revenue.
		@param UnEarnedRevenue_Acct 
		Account for unearned revenue
	  */
	public void setUnEarnedRevenue_Acct (int UnEarnedRevenue_Acct)
	{
		set_Value (COLUMNNAME_UnEarnedRevenue_Acct, Integer.valueOf(UnEarnedRevenue_Acct));
	}

	/** Get Unearned Revenue.
		@return Account for unearned revenue
	  */
	public int getUnEarnedRevenue_Acct () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_UnEarnedRevenue_Acct);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_C_ValidCombination getUnrealizedGain_A() throws RuntimeException
    {
		return (I_C_ValidCombination)MTable.get(getCtx(), I_C_ValidCombination.Table_Name)
			.getPO(getUnrealizedGain_Acct(), get_TrxName());	}

	/** Set Unrealized Gain Acct.
		@param UnrealizedGain_Acct 
		Unrealized Gain Account for currency revaluation
	  */
	public void setUnrealizedGain_Acct (int UnrealizedGain_Acct)
	{
		set_Value (COLUMNNAME_UnrealizedGain_Acct, Integer.valueOf(UnrealizedGain_Acct));
	}

	/** Get Unrealized Gain Acct.
		@return Unrealized Gain Account for currency revaluation
	  */
	public int getUnrealizedGain_Acct () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_UnrealizedGain_Acct);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_C_ValidCombination getUnrealizedLoss_A() throws RuntimeException
    {
		return (I_C_ValidCombination)MTable.get(getCtx(), I_C_ValidCombination.Table_Name)
			.getPO(getUnrealizedLoss_Acct(), get_TrxName());	}

	/** Set Unrealized Loss Acct.
		@param UnrealizedLoss_Acct 
		Unrealized Loss Account for currency revaluation
	  */
	public void setUnrealizedLoss_Acct (int UnrealizedLoss_Acct)
	{
		set_Value (COLUMNNAME_UnrealizedLoss_Acct, Integer.valueOf(UnrealizedLoss_Acct));
	}

	/** Get Unrealized Loss Acct.
		@return Unrealized Loss Account for currency revaluation
	  */
	public int getUnrealizedLoss_Acct () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_UnrealizedLoss_Acct);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_C_ValidCombination getV_Liability_A() throws RuntimeException
    {
		return (I_C_ValidCombination)MTable.get(getCtx(), I_C_ValidCombination.Table_Name)
			.getPO(getV_Liability_Acct(), get_TrxName());	}

	/** Set Vendor Liability.
		@param V_Liability_Acct 
		Account for Vendor Liability
	  */
	public void setV_Liability_Acct (int V_Liability_Acct)
	{
		set_Value (COLUMNNAME_V_Liability_Acct, Integer.valueOf(V_Liability_Acct));
	}

	/** Get Vendor Liability.
		@return Account for Vendor Liability
	  */
	public int getV_Liability_Acct () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_V_Liability_Acct);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_C_ValidCombination getV_Liability_Services_A() throws RuntimeException
    {
		return (I_C_ValidCombination)MTable.get(getCtx(), I_C_ValidCombination.Table_Name)
			.getPO(getV_Liability_Services_Acct(), get_TrxName());	}

	/** Set Vendor Service Liability.
		@param V_Liability_Services_Acct 
		Account for Vendor Service Liability
	  */
	public void setV_Liability_Services_Acct (int V_Liability_Services_Acct)
	{
		set_Value (COLUMNNAME_V_Liability_Services_Acct, Integer.valueOf(V_Liability_Services_Acct));
	}

	/** Get Vendor Service Liability.
		@return Account for Vendor Service Liability
	  */
	public int getV_Liability_Services_Acct () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_V_Liability_Services_Acct);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_C_ValidCombination getV_Prepayment_A() throws RuntimeException
    {
		return (I_C_ValidCombination)MTable.get(getCtx(), I_C_ValidCombination.Table_Name)
			.getPO(getV_Prepayment_Acct(), get_TrxName());	}

	/** Set Vendor Prepayment.
		@param V_Prepayment_Acct 
		Account for Vendor Prepayments
	  */
	public void setV_Prepayment_Acct (int V_Prepayment_Acct)
	{
		set_Value (COLUMNNAME_V_Prepayment_Acct, Integer.valueOf(V_Prepayment_Acct));
	}

	/** Get Vendor Prepayment.
		@return Account for Vendor Prepayments
	  */
	public int getV_Prepayment_Acct () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_V_Prepayment_Acct);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_C_ValidCombination getW_Differences_A() throws RuntimeException
    {
		return (I_C_ValidCombination)MTable.get(getCtx(), I_C_ValidCombination.Table_Name)
			.getPO(getW_Differences_Acct(), get_TrxName());	}

	/** Set Warehouse Differences.
		@param W_Differences_Acct 
		Warehouse Differences Account
	  */
	public void setW_Differences_Acct (int W_Differences_Acct)
	{
		set_Value (COLUMNNAME_W_Differences_Acct, Integer.valueOf(W_Differences_Acct));
	}

	/** Get Warehouse Differences.
		@return Warehouse Differences Account
	  */
	public int getW_Differences_Acct () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_W_Differences_Acct);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_C_ValidCombination getWriteOff_A() throws RuntimeException
    {
		return (I_C_ValidCombination)MTable.get(getCtx(), I_C_ValidCombination.Table_Name)
			.getPO(getWriteOff_Acct(), get_TrxName());	}

	/** Set Write-off.
		@param WriteOff_Acct 
		Account for Receivables write-off
	  */
	public void setWriteOff_Acct (int WriteOff_Acct)
	{
		set_Value (COLUMNNAME_WriteOff_Acct, Integer.valueOf(WriteOff_Acct));
	}

	/** Get Write-off.
		@return Account for Receivables write-off
	  */
	public int getWriteOff_Acct () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_WriteOff_Acct);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}
}