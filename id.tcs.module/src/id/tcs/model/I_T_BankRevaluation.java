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
package id.tcs.model;

import java.math.BigDecimal;
import java.sql.Timestamp;
import org.compiere.model.*;
import org.compiere.util.KeyNamePair;

/** Generated Interface for T_BankRevaluation
 *  @author iDempiere (generated) 
 *  @version Release 5.1
 */
@SuppressWarnings("all")
public interface I_T_BankRevaluation 
{

    /** TableName=T_BankRevaluation */
    public static final String Table_Name = "T_BankRevaluation";

    /** AD_Table_ID=300291 */
    public static final int Table_ID = 300291;

    KeyNamePair Model = new KeyNamePair(Table_ID, Table_Name);

    /** AccessLevel = 3 - Client - Org 
     */
    BigDecimal accessLevel = BigDecimal.valueOf(3);

    /** Load Meta Data */

    /** Column name AD_Client_ID */
    public static final String COLUMNNAME_AD_Client_ID = "AD_Client_ID";

	/** Get Client.
	  * Client/Tenant for this installation.
	  */
	public int getAD_Client_ID();

    /** Column name AD_Org_ID */
    public static final String COLUMNNAME_AD_Org_ID = "AD_Org_ID";

	/** Set Organization.
	  * Organizational entity within client
	  */
	public void setAD_Org_ID (int AD_Org_ID);

	/** Get Organization.
	  * Organizational entity within client
	  */
	public int getAD_Org_ID();

    /** Column name AD_PInstance_ID */
    public static final String COLUMNNAME_AD_PInstance_ID = "AD_PInstance_ID";

	/** Set Process Instance.
	  * Instance of the process
	  */
	public void setAD_PInstance_ID (int AD_PInstance_ID);

	/** Get Process Instance.
	  * Instance of the process
	  */
	public int getAD_PInstance_ID();

	public org.compiere.model.I_AD_PInstance getAD_PInstance() throws RuntimeException;

    /** Column name AmtAcctBalance */
    public static final String COLUMNNAME_AmtAcctBalance = "AmtAcctBalance";

	/** Set Accounted Balance.
	  * Accounted Balance Amount
	  */
	public void setAmtAcctBalance (BigDecimal AmtAcctBalance);

	/** Get Accounted Balance.
	  * Accounted Balance Amount
	  */
	public BigDecimal getAmtAcctBalance();

    /** Column name AmtRevalDiff */
    public static final String COLUMNNAME_AmtRevalDiff = "AmtRevalDiff";

	/** Set AmtRevalDiff	  */
	public void setAmtRevalDiff (BigDecimal AmtRevalDiff);

	/** Get AmtRevalDiff	  */
	public BigDecimal getAmtRevalDiff();

    /** Column name AmtSourceBalance */
    public static final String COLUMNNAME_AmtSourceBalance = "AmtSourceBalance";

	/** Set Source Balance.
	  * Source Balance Amount
	  */
	public void setAmtSourceBalance (BigDecimal AmtSourceBalance);

	/** Get Source Balance.
	  * Source Balance Amount
	  */
	public BigDecimal getAmtSourceBalance();

    /** Column name C_AcctSchema_ID */
    public static final String COLUMNNAME_C_AcctSchema_ID = "C_AcctSchema_ID";

	/** Set Accounting Schema.
	  * Rules for accounting
	  */
	public void setC_AcctSchema_ID (int C_AcctSchema_ID);

	/** Get Accounting Schema.
	  * Rules for accounting
	  */
	public int getC_AcctSchema_ID();

	public org.compiere.model.I_C_AcctSchema getC_AcctSchema() throws RuntimeException;

    /** Column name C_BankAccount_ID */
    public static final String COLUMNNAME_C_BankAccount_ID = "C_BankAccount_ID";

	/** Set Bank Account.
	  * Account at the Bank
	  */
	public void setC_BankAccount_ID (int C_BankAccount_ID);

	/** Get Bank Account.
	  * Account at the Bank
	  */
	public int getC_BankAccount_ID();

	public org.compiere.model.I_C_BankAccount getC_BankAccount() throws RuntimeException;

    /** Column name C_ConversionTypeReval_ID */
    public static final String COLUMNNAME_C_ConversionTypeReval_ID = "C_ConversionTypeReval_ID";

	/** Set Revaluation Conversion Type.
	  * Revaluation Currency Conversion Type
	  */
	public void setC_ConversionTypeReval_ID (int C_ConversionTypeReval_ID);

	/** Get Revaluation Conversion Type.
	  * Revaluation Currency Conversion Type
	  */
	public int getC_ConversionTypeReval_ID();

	public org.compiere.model.I_C_ConversionType getC_ConversionTypeReval() throws RuntimeException;

    /** Column name C_DocTypeReval_ID */
    public static final String COLUMNNAME_C_DocTypeReval_ID = "C_DocTypeReval_ID";

	/** Set Revaluation Document Type.
	  * Document Type for Revaluation Journal
	  */
	public void setC_DocTypeReval_ID (int C_DocTypeReval_ID);

	/** Get Revaluation Document Type.
	  * Document Type for Revaluation Journal
	  */
	public int getC_DocTypeReval_ID();

	public org.compiere.model.I_C_DocType getC_DocTypeReval() throws RuntimeException;

    /** Column name C_Period_ID */
    public static final String COLUMNNAME_C_Period_ID = "C_Period_ID";

	/** Set Period.
	  * Period of the Calendar
	  */
	public void setC_Period_ID (int C_Period_ID);

	/** Get Period.
	  * Period of the Calendar
	  */
	public int getC_Period_ID();

	public org.compiere.model.I_C_Period getC_Period() throws RuntimeException;

    /** Column name Created */
    public static final String COLUMNNAME_Created = "Created";

	/** Get Created.
	  * Date this record was created
	  */
	public Timestamp getCreated();

    /** Column name CreatedBy */
    public static final String COLUMNNAME_CreatedBy = "CreatedBy";

	/** Get Created By.
	  * User who created this records
	  */
	public int getCreatedBy();

    /** Column name DateReval */
    public static final String COLUMNNAME_DateReval = "DateReval";

	/** Set Revaluation Date.
	  * Date of Revaluation
	  */
	public void setDateReval (Timestamp DateReval);

	/** Get Revaluation Date.
	  * Date of Revaluation
	  */
	public Timestamp getDateReval();

    /** Column name EndingBalance */
    public static final String COLUMNNAME_EndingBalance = "EndingBalance";

	/** Set Ending balance.
	  * Ending  or closing balance
	  */
	public void setEndingBalance (BigDecimal EndingBalance);

	/** Get Ending balance.
	  * Ending  or closing balance
	  */
	public BigDecimal getEndingBalance();

    /** Column name IsActive */
    public static final String COLUMNNAME_IsActive = "IsActive";

	/** Set Active.
	  * The record is active in the system
	  */
	public void setIsActive (boolean IsActive);

	/** Get Active.
	  * The record is active in the system
	  */
	public boolean isActive();

    /** Column name Rate */
    public static final String COLUMNNAME_Rate = "Rate";

	/** Set Rate.
	  * Rate or Tax or Exchange
	  */
	public void setRate (BigDecimal Rate);

	/** Get Rate.
	  * Rate or Tax or Exchange
	  */
	public BigDecimal getRate();

    /** Column name T_BankRevaluation_ID */
    public static final String COLUMNNAME_T_BankRevaluation_ID = "T_BankRevaluation_ID";

	/** Set T_BankRevaluation	  */
	public void setT_BankRevaluation_ID (int T_BankRevaluation_ID);

	/** Get T_BankRevaluation	  */
	public int getT_BankRevaluation_ID();

    /** Column name T_BankRevaluation_UU */
    public static final String COLUMNNAME_T_BankRevaluation_UU = "T_BankRevaluation_UU";

	/** Set T_BankRevaluation_UU	  */
	public void setT_BankRevaluation_UU (String T_BankRevaluation_UU);

	/** Get T_BankRevaluation_UU	  */
	public String getT_BankRevaluation_UU();

    /** Column name Updated */
    public static final String COLUMNNAME_Updated = "Updated";

	/** Get Updated.
	  * Date this record was updated
	  */
	public Timestamp getUpdated();

    /** Column name UpdatedBy */
    public static final String COLUMNNAME_UpdatedBy = "UpdatedBy";

	/** Get Updated By.
	  * User who updated this records
	  */
	public int getUpdatedBy();
}
