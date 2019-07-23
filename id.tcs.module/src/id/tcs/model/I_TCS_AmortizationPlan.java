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

import org.compiere.model.I_C_ValidCombination;
import org.compiere.util.KeyNamePair;

/** Generated Interface for TCS_AmortizationPlan
 *  @author iDempiere (generated) 
 *  @version Release 3.1
 */
public interface I_TCS_AmortizationPlan 
{

    /** TableName=TCS_AmortizationPlan */
    public static final String Table_Name = "TCS_AmortizationPlan";

    /** AD_Table_ID=300189 */
    public static final int Table_ID = 300310;

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

    /** Column name AmortizationPeriod */
    public static final String COLUMNNAME_AmortizationPeriod = "AmortizationPeriod";

	/** Set Amortization Period	  */
	public void setAmortizationPeriod (int AmortizationPeriod);

	/** Get Amortization Period	  */
	public int getAmortizationPeriod();

    /** Column name AmortizationPlanNo */
    public static final String COLUMNNAME_AmortizationPlanNo = "AmortizationPlanNo";

	/** Set Amortization Plan No	  */
	public void setAmortizationPlanNo (String AmortizationPlanNo);

	/** Get Amortization Plan No	  */
	public String getAmortizationPlanNo();

    /** Column name AmortizationStartDate */
    public static final String COLUMNNAME_AmortizationStartDate = "AmortizationStartDate";

	/** Set Amortization Start Date	  */
	public void setAmortizationStartDate (Timestamp AmortizationStartDate);

	/** Get Amortization Start Date	  */
	public Timestamp getAmortizationStartDate();

    /** Column name C_Activity_ID */
    public static final String COLUMNNAME_C_Activity_ID = "C_Activity_ID";

	/** Set Activity.
	  * Business Activity
	  */
	public void setC_Activity_ID (int C_Activity_ID);

	/** Get Activity.
	  * Business Activity
	  */
	public int getC_Activity_ID();

	public org.compiere.model.I_C_Activity getC_Activity() throws RuntimeException;

    /** Column name C_BPartner_ID */
    public static final String COLUMNNAME_C_BPartner_ID = "C_BPartner_ID";

	/** Set Business Partner .
	  * Identifies a Business Partner
	  */
	public void setC_BPartner_ID (int C_BPartner_ID);

	/** Get Business Partner .
	  * Identifies a Business Partner
	  */
	public int getC_BPartner_ID();

	public org.compiere.model.I_C_BPartner getC_BPartner() throws RuntimeException;

    /** Column name C_Campaign_ID */
    public static final String COLUMNNAME_C_Campaign_ID = "C_Campaign_ID";

	/** Set Campaign.
	  * Marketing Campaign
	  */
	public void setC_Campaign_ID (int C_Campaign_ID);

	/** Get Campaign.
	  * Marketing Campaign
	  */
	public int getC_Campaign_ID();

	public org.compiere.model.I_C_Campaign getC_Campaign() throws RuntimeException;

    /** Column name C_Currency_ID */
    public static final String COLUMNNAME_C_Currency_ID = "C_Currency_ID";

	/** Set Currency.
	  * The Currency for this record
	  */
	public void setC_Currency_ID (int C_Currency_ID);

	/** Get Currency.
	  * The Currency for this record
	  */
	public int getC_Currency_ID();

	public org.compiere.model.I_C_Currency getC_Currency() throws RuntimeException;

    /** Column name C_LocFrom_ID */
    public static final String COLUMNNAME_C_LocFrom_ID = "C_LocFrom_ID";

	/** Set Location From.
	  * Location that inventory was moved from
	  */
	public void setC_LocFrom_ID (int C_LocFrom_ID);

	/** Get Location From.
	  * Location that inventory was moved from
	  */
	public int getC_LocFrom_ID();

	public org.compiere.model.I_C_Location getC_LocFrom() throws RuntimeException;

    /** Column name C_LocTo_ID */
    public static final String COLUMNNAME_C_LocTo_ID = "C_LocTo_ID";

	/** Set Location To.
	  * Location that inventory was moved to
	  */
	public void setC_LocTo_ID (int C_LocTo_ID);

	/** Get Location To.
	  * Location that inventory was moved to
	  */
	public int getC_LocTo_ID();

	public org.compiere.model.I_C_Location getC_LocTo() throws RuntimeException;

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

    /** Column name C_Project_ID */
    public static final String COLUMNNAME_C_Project_ID = "C_Project_ID";

	/** Set Project.
	  * Financial Project
	  */
	public void setC_Project_ID (int C_Project_ID);

	/** Get Project.
	  * Financial Project
	  */
	public int getC_Project_ID();

	public org.compiere.model.I_C_Project getC_Project() throws RuntimeException;

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

    /** Column name Credit_Account_ID */
    public static final String COLUMNNAME_Credit_Account_ID = "Credit_Account_ID";

	/** Set Credit Account	  */
	public void setCredit_Account_ID (int Credit_Account_ID);

	/** Get Credit Account	  */
	public int getCredit_Account_ID();

	public I_C_ValidCombination getCredit_Account() throws RuntimeException;

    /** Column name C_SalesRegion_ID */
    public static final String COLUMNNAME_C_SalesRegion_ID = "C_SalesRegion_ID";

	/** Set Sales Region.
	  * Sales coverage region
	  */
	public void setC_SalesRegion_ID (int C_SalesRegion_ID);

	/** Get Sales Region.
	  * Sales coverage region
	  */
	public int getC_SalesRegion_ID();

	public org.compiere.model.I_C_SalesRegion getC_SalesRegion() throws RuntimeException;

    /** Column name C_SubAcct_ID */
    public static final String COLUMNNAME_C_SubAcct_ID = "C_SubAcct_ID";

	/** Set Sub Account.
	  * Sub account for Element Value
	  */
	public void setC_SubAcct_ID (int C_SubAcct_ID);

	/** Get Sub Account.
	  * Sub account for Element Value
	  */
	public int getC_SubAcct_ID();

	public org.compiere.model.I_C_SubAcct getC_SubAcct() throws RuntimeException;

    /** Column name DateDoc */
    public static final String COLUMNNAME_DateDoc = "DateDoc";

	/** Set Document Date.
	  * Date of the Document
	  */
	public void setDateDoc (Timestamp DateDoc);

	/** Get Document Date.
	  * Date of the Document
	  */
	public Timestamp getDateDoc();

    /** Column name Debit_Account_ID */
    public static final String COLUMNNAME_Debit_Account_ID = "Debit_Account_ID";

	/** Set Debit Account	  */
	public void setDebit_Account_ID (int Debit_Account_ID);

	/** Get Debit Account	  */
	public int getDebit_Account_ID();

	public I_C_ValidCombination getDebit_Account() throws RuntimeException;

    /** Column name Description */
    public static final String COLUMNNAME_Description = "Description";

	/** Set Description.
	  * Optional short description of the record
	  */
	public void setDescription (String Description);

	/** Get Description.
	  * Optional short description of the record
	  */
	public String getDescription();

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

    /** Column name IsFullyAmortized */
    public static final String COLUMNNAME_IsFullyAmortized = "IsFullyAmortized";

	/** Set Is Fully Amortized	  */
	public void setIsFullyAmortized (boolean IsFullyAmortized);

	/** Get Is Fully Amortized	  */
	public boolean isFullyAmortized();

    /** Column name M_Product_ID */
    public static final String COLUMNNAME_M_Product_ID = "M_Product_ID";

	/** Set Product.
	  * Product, Service, Item
	  */
	public void setM_Product_ID (int M_Product_ID);

	/** Get Product.
	  * Product, Service, Item
	  */
	public int getM_Product_ID();

	public org.compiere.model.I_M_Product getM_Product() throws RuntimeException;

    /** Column name StdPrecision */
    public static final String COLUMNNAME_StdPrecision = "StdPrecision";

	/** Set Standard Precision.
	  * Rule for rounding  calculated amounts
	  */
	public void setStdPrecision (BigDecimal StdPrecision);

	/** Get Standard Precision.
	  * Rule for rounding  calculated amounts
	  */
	public BigDecimal getStdPrecision();

    /** Column name TCS_AmortizationPlan_ID */
    public static final String COLUMNNAME_TCS_AmortizationPlan_ID = "TCS_AmortizationPlan_ID";

	/** Set TCS_AmortizationPlan	  */
	public void setTCS_AmortizationPlan_ID (int TCS_AmortizationPlan_ID);

	/** Get TCS_AmortizationPlan	  */
	public int getTCS_AmortizationPlan_ID();

    /** Column name TCS_AmortizationPlan_UU */
    public static final String COLUMNNAME_TCS_AmortizationPlan_UU = "TCS_AmortizationPlan_UU";

	/** Set TCS_AmortizationPlan_UU	  */
	public void setTCS_AmortizationPlan_UU (String TCS_AmortizationPlan_UU);

	/** Get TCS_AmortizationPlan_UU	  */
	public String getTCS_AmortizationPlan_UU();

    /** Column name TotalAmt */
    public static final String COLUMNNAME_TotalAmt = "TotalAmt";

	/** Set Total Amount.
	  * Total Amount
	  */
	public void setTotalAmt (BigDecimal TotalAmt);

	/** Get Total Amount.
	  * Total Amount
	  */
	public BigDecimal getTotalAmt();

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
