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

/** Generated Interface for TCS_WithholdingCalc
 *  @author iDempiere (generated) 
 *  @version Release 5.1
 */
@SuppressWarnings("all")
public interface I_TCS_WithholdingCalc 
{

    /** TableName=TCS_WithholdingCalc */
    public static final String Table_Name = "TCS_WithholdingCalc";

    /** AD_Table_ID=1000045 */
    public static final int Table_ID = MTable.getTable_ID(Table_Name);

    KeyNamePair Model = new KeyNamePair(Table_ID, Table_Name);

    /** AccessLevel = 3 - Client - Org 
     */
    BigDecimal accessLevel = BigDecimal.valueOf(3);

    /** Load Meta Data */

    /** Column name AccumulatedAmt */
    public static final String COLUMNNAME_AccumulatedAmt = "AccumulatedAmt";

	/** Set Accumulated Amount	  */
	public void setAccumulatedAmt (BigDecimal AccumulatedAmt);

	/** Get Accumulated Amount	  */
	public BigDecimal getAccumulatedAmt();

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

    /** Column name Amt */
    public static final String COLUMNNAME_Amt = "Amt";

	/** Set Amount.
	  * Amount
	  */
	public void setAmt (BigDecimal Amt);

	/** Get Amount.
	  * Amount
	  */
	public BigDecimal getAmt();

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

    /** Column name DPP */
    public static final String COLUMNNAME_DPP = "DPP";

	/** Set DPP	  */
	public void setDPP (BigDecimal DPP);

	/** Get DPP	  */
	public BigDecimal getDPP();

    /** Column name HalvedAmt */
    public static final String COLUMNNAME_HalvedAmt = "HalvedAmt";

	/** Set Amt (50%).
	  * Menampilkan 50% dari Amt Invoice yang di gunakan untuk perhitungan PPh
	  */
	public void setHalvedAmt (BigDecimal HalvedAmt);

	/** Get Amt (50%).
	  * Menampilkan 50% dari Amt Invoice yang di gunakan untuk perhitungan PPh
	  */
	public BigDecimal getHalvedAmt();

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

    /** Column name PPh */
    public static final String COLUMNNAME_PPh = "PPh";

	/** Set PPh	  */
	public void setPPh (BigDecimal PPh);

	/** Get PPh	  */
	public BigDecimal getPPh();

    /** Column name TCS_WithholdingCalc_ID */
    public static final String COLUMNNAME_TCS_WithholdingCalc_ID = "TCS_WithholdingCalc_ID";

	/** Set Withholding Calc	  */
	public void setTCS_WithholdingCalc_ID (int TCS_WithholdingCalc_ID);

	/** Get Withholding Calc	  */
	public int getTCS_WithholdingCalc_ID();

    /** Column name TCS_WithholdingCalc_UU */
    public static final String COLUMNNAME_TCS_WithholdingCalc_UU = "TCS_WithholdingCalc_UU";

	/** Set TCS_WithholdingCalc_UU	  */
	public void setTCS_WithholdingCalc_UU (String TCS_WithholdingCalc_UU);

	/** Get TCS_WithholdingCalc_UU	  */
	public String getTCS_WithholdingCalc_UU();

    /** Column name TCS_WithholdingType_ID */
    public static final String COLUMNNAME_TCS_WithholdingType_ID = "TCS_WithholdingType_ID";

	/** Set Withholding Type	  */
	public void setTCS_WithholdingType_ID (int TCS_WithholdingType_ID);

	/** Get Withholding Type	  */
	public int getTCS_WithholdingType_ID();

	public I_TCS_WithholdingType getTCS_WithholdingType() throws RuntimeException;

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
