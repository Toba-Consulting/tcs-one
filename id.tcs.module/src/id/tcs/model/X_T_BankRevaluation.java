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

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Properties;
import org.compiere.model.*;
import org.compiere.util.Env;

/** Generated Model for T_BankRevaluation
 *  @author iDempiere (generated) 
 *  @version Release 5.1 - $Id$ */
public class X_T_BankRevaluation extends PO implements I_T_BankRevaluation, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20181226L;

    /** Standard Constructor */
    public X_T_BankRevaluation (Properties ctx, int T_BankRevaluation_ID, String trxName)
    {
      super (ctx, T_BankRevaluation_ID, trxName);
      /** if (T_BankRevaluation_ID == 0)
        {
        } */
    }

    /** Load Constructor */
    public X_T_BankRevaluation (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_T_BankRevaluation[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	public org.compiere.model.I_AD_PInstance getAD_PInstance() throws RuntimeException
    {
		return (org.compiere.model.I_AD_PInstance)MTable.get(getCtx(), org.compiere.model.I_AD_PInstance.Table_Name)
			.getPO(getAD_PInstance_ID(), get_TrxName());	}

	/** Set Process Instance.
		@param AD_PInstance_ID 
		Instance of the process
	  */
	public void setAD_PInstance_ID (int AD_PInstance_ID)
	{
		if (AD_PInstance_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_AD_PInstance_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_AD_PInstance_ID, Integer.valueOf(AD_PInstance_ID));
	}

	/** Get Process Instance.
		@return Instance of the process
	  */
	public int getAD_PInstance_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AD_PInstance_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Accounted Balance.
		@param AmtAcctBalance 
		Accounted Balance Amount
	  */
	public void setAmtAcctBalance (BigDecimal AmtAcctBalance)
	{
		set_ValueNoCheck (COLUMNNAME_AmtAcctBalance, AmtAcctBalance);
	}

	/** Get Accounted Balance.
		@return Accounted Balance Amount
	  */
	public BigDecimal getAmtAcctBalance () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_AmtAcctBalance);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set AmtRevalDiff.
		@param AmtRevalDiff AmtRevalDiff	  */
	public void setAmtRevalDiff (BigDecimal AmtRevalDiff)
	{
		set_Value (COLUMNNAME_AmtRevalDiff, AmtRevalDiff);
	}

	/** Get AmtRevalDiff.
		@return AmtRevalDiff	  */
	public BigDecimal getAmtRevalDiff () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_AmtRevalDiff);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Source Balance.
		@param AmtSourceBalance 
		Source Balance Amount
	  */
	public void setAmtSourceBalance (BigDecimal AmtSourceBalance)
	{
		set_ValueNoCheck (COLUMNNAME_AmtSourceBalance, AmtSourceBalance);
	}

	/** Get Source Balance.
		@return Source Balance Amount
	  */
	public BigDecimal getAmtSourceBalance () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_AmtSourceBalance);
		if (bd == null)
			 return Env.ZERO;
		return bd;
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
			set_Value (COLUMNNAME_C_BankAccount_ID, null);
		else 
			set_Value (COLUMNNAME_C_BankAccount_ID, Integer.valueOf(C_BankAccount_ID));
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

	public org.compiere.model.I_C_ConversionType getC_ConversionTypeReval() throws RuntimeException
    {
		return (org.compiere.model.I_C_ConversionType)MTable.get(getCtx(), org.compiere.model.I_C_ConversionType.Table_Name)
			.getPO(getC_ConversionTypeReval_ID(), get_TrxName());	}

	/** Set Revaluation Conversion Type.
		@param C_ConversionTypeReval_ID 
		Revaluation Currency Conversion Type
	  */
	public void setC_ConversionTypeReval_ID (int C_ConversionTypeReval_ID)
	{
		if (C_ConversionTypeReval_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_C_ConversionTypeReval_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_C_ConversionTypeReval_ID, Integer.valueOf(C_ConversionTypeReval_ID));
	}

	/** Get Revaluation Conversion Type.
		@return Revaluation Currency Conversion Type
	  */
	public int getC_ConversionTypeReval_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_ConversionTypeReval_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public org.compiere.model.I_C_DocType getC_DocTypeReval() throws RuntimeException
    {
		return (org.compiere.model.I_C_DocType)MTable.get(getCtx(), org.compiere.model.I_C_DocType.Table_Name)
			.getPO(getC_DocTypeReval_ID(), get_TrxName());	}

	/** Set Revaluation Document Type.
		@param C_DocTypeReval_ID 
		Document Type for Revaluation Journal
	  */
	public void setC_DocTypeReval_ID (int C_DocTypeReval_ID)
	{
		if (C_DocTypeReval_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_C_DocTypeReval_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_C_DocTypeReval_ID, Integer.valueOf(C_DocTypeReval_ID));
	}

	/** Get Revaluation Document Type.
		@return Document Type for Revaluation Journal
	  */
	public int getC_DocTypeReval_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_DocTypeReval_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
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

	/** Set Revaluation Date.
		@param DateReval 
		Date of Revaluation
	  */
	public void setDateReval (Timestamp DateReval)
	{
		set_ValueNoCheck (COLUMNNAME_DateReval, DateReval);
	}

	/** Get Revaluation Date.
		@return Date of Revaluation
	  */
	public Timestamp getDateReval () 
	{
		return (Timestamp)get_Value(COLUMNNAME_DateReval);
	}

	/** Set Ending balance.
		@param EndingBalance 
		Ending  or closing balance
	  */
	public void setEndingBalance (BigDecimal EndingBalance)
	{
		set_Value (COLUMNNAME_EndingBalance, EndingBalance);
	}

	/** Get Ending balance.
		@return Ending  or closing balance
	  */
	public BigDecimal getEndingBalance () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_EndingBalance);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Rate.
		@param Rate 
		Rate or Tax or Exchange
	  */
	public void setRate (BigDecimal Rate)
	{
		set_ValueNoCheck (COLUMNNAME_Rate, Rate);
	}

	/** Get Rate.
		@return Rate or Tax or Exchange
	  */
	public BigDecimal getRate () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_Rate);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set T_BankRevaluation.
		@param T_BankRevaluation_ID T_BankRevaluation	  */
	public void setT_BankRevaluation_ID (int T_BankRevaluation_ID)
	{
		if (T_BankRevaluation_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_T_BankRevaluation_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_T_BankRevaluation_ID, Integer.valueOf(T_BankRevaluation_ID));
	}

	/** Get T_BankRevaluation.
		@return T_BankRevaluation	  */
	public int getT_BankRevaluation_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_T_BankRevaluation_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set T_BankRevaluation_UU.
		@param T_BankRevaluation_UU T_BankRevaluation_UU	  */
	public void setT_BankRevaluation_UU (String T_BankRevaluation_UU)
	{
		set_ValueNoCheck (COLUMNNAME_T_BankRevaluation_UU, T_BankRevaluation_UU);
	}

	/** Get T_BankRevaluation_UU.
		@return T_BankRevaluation_UU	  */
	public String getT_BankRevaluation_UU () 
	{
		return (String)get_Value(COLUMNNAME_T_BankRevaluation_UU);
	}
}