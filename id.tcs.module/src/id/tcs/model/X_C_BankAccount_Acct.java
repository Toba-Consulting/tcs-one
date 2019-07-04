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

/** Generated Model for C_BankAccount_Acct
 *  @author iDempiere (generated) 
 *  @version Release 5.1 - $Id$ */
public class X_C_BankAccount_Acct extends PO implements I_C_BankAccount_Acct, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20190619L;

    /** Standard Constructor */
    public X_C_BankAccount_Acct (Properties ctx, int C_BankAccount_Acct_ID, String trxName)
    {
      super (ctx, C_BankAccount_Acct_ID, trxName);
      /** if (C_BankAccount_Acct_ID == 0)
        {
			setB_Asset_Acct (0);
			setB_InterestExp_Acct (0);
			setB_InterestRev_Acct (0);
			setB_InTransit_Acct (0);
			setB_PaymentSelect_Acct (0);
			setB_UnallocatedCash_Acct (0);
			setC_AcctSchema_ID (0);
			setC_BankAccount_ID (0);
        } */
    }

    /** Load Constructor */
    public X_C_BankAccount_Acct (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_C_BankAccount_Acct[")
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

	/** Set C_BankAccount_Acct_UU.
		@param C_BankAccount_Acct_UU C_BankAccount_Acct_UU	  */
	public void setC_BankAccount_Acct_UU (String C_BankAccount_Acct_UU)
	{
		set_Value (COLUMNNAME_C_BankAccount_Acct_UU, C_BankAccount_Acct_UU);
	}

	/** Get C_BankAccount_Acct_UU.
		@return C_BankAccount_Acct_UU	  */
	public String getC_BankAccount_Acct_UU () 
	{
		return (String)get_Value(COLUMNNAME_C_BankAccount_Acct_UU);
	}

	public org.compiere.model.I_C_BankAccount getC_BankAccount() throws RuntimeException
    {
		return (org.compiere.model.I_C_BankAccount)MTable.get(getCtx(), org.compiere.model.I_C_BankAccount.Table_Name)
			.getPO(getC_BankAccount_ID(), get_TrxName());	}

	/** Set Bank Account.
		@param C_BankAccount_ID 
		Account at the Bank
	  */
	public void setC_BankAccount_ID (int C_BankAccount_ID)
	{
		if (C_BankAccount_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_C_BankAccount_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_C_BankAccount_ID, Integer.valueOf(C_BankAccount_ID));
	}

	/** Get Bank Account.
		@return Account at the Bank
	  */
	public int getC_BankAccount_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_BankAccount_ID);
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
}