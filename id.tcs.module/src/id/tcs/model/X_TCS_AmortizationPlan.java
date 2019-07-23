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

import org.compiere.model.I_C_ValidCombination;
import org.compiere.model.I_Persistent;
import org.compiere.model.MTable;
import org.compiere.model.PO;
import org.compiere.model.POInfo;
import org.compiere.util.Env;
import org.compiere.util.KeyNamePair;

/** Generated Model for TCS_AmortizationPlan
 *  @author iDempiere (generated) 
 *  @version Release 3.1 - $Id$ */
public class X_TCS_AmortizationPlan extends PO implements I_TCS_AmortizationPlan, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20170223L;

    /** Standard Constructor */
    public X_TCS_AmortizationPlan (Properties ctx, int TCS_AmortizationPlan_ID, String trxName)
    {
      super (ctx, TCS_AmortizationPlan_ID, trxName);
      /** if (TCS_AmortizationPlan_ID == 0)
        {
			setAmortizationPlanNo (null);
			setTCS_AmortizationPlan_ID (0);
        } */
    }

    /** Load Constructor */
    public X_TCS_AmortizationPlan (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_TCS_AmortizationPlan[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	/** Set Amortization Period.
		@param AmortizationPeriod Amortization Period	  */
	public void setAmortizationPeriod (int AmortizationPeriod)
	{
		set_Value (COLUMNNAME_AmortizationPeriod, Integer.valueOf(AmortizationPeriod));
	}

	/** Get Amortization Period.
		@return Amortization Period	  */
	public int getAmortizationPeriod () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AmortizationPeriod);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Amortization Plan No.
		@param AmortizationPlanNo Amortization Plan No	  */
	public void setAmortizationPlanNo (String AmortizationPlanNo)
	{
		set_ValueNoCheck (COLUMNNAME_AmortizationPlanNo, AmortizationPlanNo);
	}

	/** Get Amortization Plan No.
		@return Amortization Plan No	  */
	public String getAmortizationPlanNo () 
	{
		return (String)get_Value(COLUMNNAME_AmortizationPlanNo);
	}

	/** Set Amortization Start Date.
		@param AmortizationStartDate Amortization Start Date	  */
	public void setAmortizationStartDate (Timestamp AmortizationStartDate)
	{
		set_Value (COLUMNNAME_AmortizationStartDate, AmortizationStartDate);
	}

	/** Get Amortization Start Date.
		@return Amortization Start Date	  */
	public Timestamp getAmortizationStartDate () 
	{
		return (Timestamp)get_Value(COLUMNNAME_AmortizationStartDate);
	}

	public org.compiere.model.I_C_Activity getC_Activity() throws RuntimeException
    {
		return (org.compiere.model.I_C_Activity)MTable.get(getCtx(), org.compiere.model.I_C_Activity.Table_Name)
			.getPO(getC_Activity_ID(), get_TrxName());	}

	/** Set Activity.
		@param C_Activity_ID 
		Business Activity
	  */
	public void setC_Activity_ID (int C_Activity_ID)
	{
		if (C_Activity_ID < 1) 
			set_Value (COLUMNNAME_C_Activity_ID, null);
		else 
			set_Value (COLUMNNAME_C_Activity_ID, Integer.valueOf(C_Activity_ID));
	}

	/** Get Activity.
		@return Business Activity
	  */
	public int getC_Activity_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_Activity_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public org.compiere.model.I_C_BPartner getC_BPartner() throws RuntimeException
    {
		return (org.compiere.model.I_C_BPartner)MTable.get(getCtx(), org.compiere.model.I_C_BPartner.Table_Name)
			.getPO(getC_BPartner_ID(), get_TrxName());	}

	/** Set Business Partner .
		@param C_BPartner_ID 
		Identifies a Business Partner
	  */
	public void setC_BPartner_ID (int C_BPartner_ID)
	{
		if (C_BPartner_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_C_BPartner_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_C_BPartner_ID, Integer.valueOf(C_BPartner_ID));
	}

	/** Get Business Partner .
		@return Identifies a Business Partner
	  */
	public int getC_BPartner_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_BPartner_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public org.compiere.model.I_C_Campaign getC_Campaign() throws RuntimeException
    {
		return (org.compiere.model.I_C_Campaign)MTable.get(getCtx(), org.compiere.model.I_C_Campaign.Table_Name)
			.getPO(getC_Campaign_ID(), get_TrxName());	}

	/** Set Campaign.
		@param C_Campaign_ID 
		Marketing Campaign
	  */
	public void setC_Campaign_ID (int C_Campaign_ID)
	{
		if (C_Campaign_ID < 1) 
			set_Value (COLUMNNAME_C_Campaign_ID, null);
		else 
			set_Value (COLUMNNAME_C_Campaign_ID, Integer.valueOf(C_Campaign_ID));
	}

	/** Get Campaign.
		@return Marketing Campaign
	  */
	public int getC_Campaign_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_Campaign_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public org.compiere.model.I_C_Currency getC_Currency() throws RuntimeException
    {
		return (org.compiere.model.I_C_Currency)MTable.get(getCtx(), org.compiere.model.I_C_Currency.Table_Name)
			.getPO(getC_Currency_ID(), get_TrxName());	}

	/** Set Currency.
		@param C_Currency_ID 
		The Currency for this record
	  */
	public void setC_Currency_ID (int C_Currency_ID)
	{
		if (C_Currency_ID < 1) 
			set_Value (COLUMNNAME_C_Currency_ID, null);
		else 
			set_Value (COLUMNNAME_C_Currency_ID, Integer.valueOf(C_Currency_ID));
	}

	/** Get Currency.
		@return The Currency for this record
	  */
	public int getC_Currency_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_Currency_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public org.compiere.model.I_C_Location getC_LocFrom() throws RuntimeException
    {
		return (org.compiere.model.I_C_Location)MTable.get(getCtx(), org.compiere.model.I_C_Location.Table_Name)
			.getPO(getC_LocFrom_ID(), get_TrxName());	}

	/** Set Location From.
		@param C_LocFrom_ID 
		Location that inventory was moved from
	  */
	public void setC_LocFrom_ID (int C_LocFrom_ID)
	{
		if (C_LocFrom_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_C_LocFrom_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_C_LocFrom_ID, Integer.valueOf(C_LocFrom_ID));
	}

	/** Get Location From.
		@return Location that inventory was moved from
	  */
	public int getC_LocFrom_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_LocFrom_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public org.compiere.model.I_C_Location getC_LocTo() throws RuntimeException
    {
		return (org.compiere.model.I_C_Location)MTable.get(getCtx(), org.compiere.model.I_C_Location.Table_Name)
			.getPO(getC_LocTo_ID(), get_TrxName());	}

	/** Set Location To.
		@param C_LocTo_ID 
		Location that inventory was moved to
	  */
	public void setC_LocTo_ID (int C_LocTo_ID)
	{
		if (C_LocTo_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_C_LocTo_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_C_LocTo_ID, Integer.valueOf(C_LocTo_ID));
	}

	/** Get Location To.
		@return Location that inventory was moved to
	  */
	public int getC_LocTo_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_LocTo_ID);
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

	public org.compiere.model.I_C_Project getC_Project() throws RuntimeException
    {
		return (org.compiere.model.I_C_Project)MTable.get(getCtx(), org.compiere.model.I_C_Project.Table_Name)
			.getPO(getC_Project_ID(), get_TrxName());	}

	/** Set Project.
		@param C_Project_ID 
		Financial Project
	  */
	public void setC_Project_ID (int C_Project_ID)
	{
		if (C_Project_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_C_Project_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_C_Project_ID, Integer.valueOf(C_Project_ID));
	}

	/** Get Project.
		@return Financial Project
	  */
	public int getC_Project_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_Project_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_C_ValidCombination getCredit_Account() throws RuntimeException
    {
		return (I_C_ValidCombination)MTable.get(getCtx(), I_C_ValidCombination.Table_Name)
			.getPO(getCredit_Account_ID(), get_TrxName());	}

	/** Set Credit Account.
		@param Credit_Account_ID Credit Account	  */
	public void setCredit_Account_ID (int Credit_Account_ID)
	{
		if (Credit_Account_ID < 1) 
			set_Value (COLUMNNAME_Credit_Account_ID, null);
		else 
			set_Value (COLUMNNAME_Credit_Account_ID, Integer.valueOf(Credit_Account_ID));
	}

	/** Get Credit Account.
		@return Credit Account	  */
	public int getCredit_Account_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_Credit_Account_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public org.compiere.model.I_C_SalesRegion getC_SalesRegion() throws RuntimeException
    {
		return (org.compiere.model.I_C_SalesRegion)MTable.get(getCtx(), org.compiere.model.I_C_SalesRegion.Table_Name)
			.getPO(getC_SalesRegion_ID(), get_TrxName());	}

	/** Set Sales Region.
		@param C_SalesRegion_ID 
		Sales coverage region
	  */
	public void setC_SalesRegion_ID (int C_SalesRegion_ID)
	{
		if (C_SalesRegion_ID < 1) 
			set_Value (COLUMNNAME_C_SalesRegion_ID, null);
		else 
			set_Value (COLUMNNAME_C_SalesRegion_ID, Integer.valueOf(C_SalesRegion_ID));
	}

	/** Get Sales Region.
		@return Sales coverage region
	  */
	public int getC_SalesRegion_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_SalesRegion_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public org.compiere.model.I_C_SubAcct getC_SubAcct() throws RuntimeException
    {
		return (org.compiere.model.I_C_SubAcct)MTable.get(getCtx(), org.compiere.model.I_C_SubAcct.Table_Name)
			.getPO(getC_SubAcct_ID(), get_TrxName());	}

	/** Set Sub Account.
		@param C_SubAcct_ID 
		Sub account for Element Value
	  */
	public void setC_SubAcct_ID (int C_SubAcct_ID)
	{
		if (C_SubAcct_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_C_SubAcct_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_C_SubAcct_ID, Integer.valueOf(C_SubAcct_ID));
	}

	/** Get Sub Account.
		@return Sub account for Element Value
	  */
	public int getC_SubAcct_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_SubAcct_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Document Date.
		@param DateDoc 
		Date of the Document
	  */
	public void setDateDoc (Timestamp DateDoc)
	{
		set_Value (COLUMNNAME_DateDoc, DateDoc);
	}

	/** Get Document Date.
		@return Date of the Document
	  */
	public Timestamp getDateDoc () 
	{
		return (Timestamp)get_Value(COLUMNNAME_DateDoc);
	}

	public I_C_ValidCombination getDebit_Account() throws RuntimeException
    {
		return (I_C_ValidCombination)MTable.get(getCtx(), I_C_ValidCombination.Table_Name)
			.getPO(getDebit_Account_ID(), get_TrxName());	}

	/** Set Debit Account.
		@param Debit_Account_ID Debit Account	  */
	public void setDebit_Account_ID (int Debit_Account_ID)
	{
		if (Debit_Account_ID < 1) 
			set_Value (COLUMNNAME_Debit_Account_ID, null);
		else 
			set_Value (COLUMNNAME_Debit_Account_ID, Integer.valueOf(Debit_Account_ID));
	}

	/** Get Debit Account.
		@return Debit Account	  */
	public int getDebit_Account_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_Debit_Account_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
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

	/** Set Is Fully Amortized.
		@param IsFullyAmortized Is Fully Amortized	  */
	public void setIsFullyAmortized (boolean IsFullyAmortized)
	{
		set_Value (COLUMNNAME_IsFullyAmortized, Boolean.valueOf(IsFullyAmortized));
	}

	/** Get Is Fully Amortized.
		@return Is Fully Amortized	  */
	public boolean isFullyAmortized () 
	{
		Object oo = get_Value(COLUMNNAME_IsFullyAmortized);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
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

	/** Set Standard Precision.
		@param StdPrecision 
		Rule for rounding  calculated amounts
	  */
	public void setStdPrecision (BigDecimal StdPrecision)
	{
		set_ValueNoCheck (COLUMNNAME_StdPrecision, StdPrecision);
	}

	/** Get Standard Precision.
		@return Rule for rounding  calculated amounts
	  */
	public BigDecimal getStdPrecision () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_StdPrecision);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set TCS_AmortizationPlan.
		@param TCS_AmortizationPlan_ID TCS_AmortizationPlan	  */
	public void setTCS_AmortizationPlan_ID (int TCS_AmortizationPlan_ID)
	{
		if (TCS_AmortizationPlan_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_TCS_AmortizationPlan_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_TCS_AmortizationPlan_ID, Integer.valueOf(TCS_AmortizationPlan_ID));
	}

	/** Get TCS_AmortizationPlan.
		@return TCS_AmortizationPlan	  */
	public int getTCS_AmortizationPlan_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_TCS_AmortizationPlan_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

    /** Get Record ID/ColumnName
        @return ID/ColumnName pair
      */
    public KeyNamePair getKeyNamePair() 
    {
        return new KeyNamePair(get_ID(), String.valueOf(getTCS_AmortizationPlan_ID()));
    }

	/** Set TCS_AmortizationPlan_UU.
		@param TCS_AmortizationPlan_UU TCS_AmortizationPlan_UU	  */
	public void setTCS_AmortizationPlan_UU (String TCS_AmortizationPlan_UU)
	{
		set_Value (COLUMNNAME_TCS_AmortizationPlan_UU, TCS_AmortizationPlan_UU);
	}

	/** Get TCS_AmortizationPlan_UU.
		@return TCS_AmortizationPlan_UU	  */
	public String getTCS_AmortizationPlan_UU () 
	{
		return (String)get_Value(COLUMNNAME_TCS_AmortizationPlan_UU);
	}

	/** Set Total Amount.
		@param TotalAmt 
		Total Amount
	  */
	public void setTotalAmt (BigDecimal TotalAmt)
	{
		set_ValueNoCheck (COLUMNNAME_TotalAmt, TotalAmt);
	}

	/** Get Total Amount.
		@return Total Amount
	  */
	public BigDecimal getTotalAmt () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_TotalAmt);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}
}