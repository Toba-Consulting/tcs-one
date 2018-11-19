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

/** Generated Interface for T_MatchAllocation
 *  @author iDempiere (generated) 
 *  @version Release 5.1
 */
@SuppressWarnings("all")
public interface I_T_MatchAllocation 
{

    /** TableName=T_MatchAllocation */
    public static final String Table_Name = "T_MatchAllocation";

    /** AD_Table_ID=300266 */
    public static final int Table_ID = 300266;

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

    /** Column name AllocationAmt */
    public static final String COLUMNNAME_AllocationAmt = "AllocationAmt";

	/** Set AllocationAmt	  */
	public void setAllocationAmt (BigDecimal AllocationAmt);

	/** Get AllocationAmt	  */
	public BigDecimal getAllocationAmt();

    /** Column name C_AllocationHdr_ID */
    public static final String COLUMNNAME_C_AllocationHdr_ID = "C_AllocationHdr_ID";

	/** Set Allocation.
	  * Payment allocation
	  */
	public void setC_AllocationHdr_ID (int C_AllocationHdr_ID);

	/** Get Allocation.
	  * Payment allocation
	  */
	public int getC_AllocationHdr_ID();

	public org.compiere.model.I_C_AllocationHdr getC_AllocationHdr() throws RuntimeException;

    /** Column name C_Charge_ID */
    public static final String COLUMNNAME_C_Charge_ID = "C_Charge_ID";

	/** Set Charge.
	  * Additional document charges
	  */
	public void setC_Charge_ID (int C_Charge_ID);

	/** Get Charge.
	  * Additional document charges
	  */
	public int getC_Charge_ID();

	public org.compiere.model.I_C_Charge getC_Charge() throws RuntimeException;

    /** Column name C_DocType_ID */
    public static final String COLUMNNAME_C_DocType_ID = "C_DocType_ID";

	/** Set Document Type.
	  * Document type or rules
	  */
	public void setC_DocType_ID (int C_DocType_ID);

	/** Get Document Type.
	  * Document type or rules
	  */
	public int getC_DocType_ID();

	public org.compiere.model.I_C_DocType getC_DocType() throws RuntimeException;

    /** Column name C_Invoice_ID */
    public static final String COLUMNNAME_C_Invoice_ID = "C_Invoice_ID";

	/** Set Invoice.
	  * Invoice Identifier
	  */
	public void setC_Invoice_ID (int C_Invoice_ID);

	/** Get Invoice.
	  * Invoice Identifier
	  */
	public int getC_Invoice_ID();

	public org.compiere.model.I_C_Invoice getC_Invoice() throws RuntimeException;

    /** Column name C_Payment_ID */
    public static final String COLUMNNAME_C_Payment_ID = "C_Payment_ID";

	/** Set Payment.
	  * Payment identifier
	  */
	public void setC_Payment_ID (int C_Payment_ID);

	/** Get Payment.
	  * Payment identifier
	  */
	public int getC_Payment_ID();

	public org.compiere.model.I_C_Payment getC_Payment() throws RuntimeException;

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

    /** Column name DateAllocated */
    public static final String COLUMNNAME_DateAllocated = "DateAllocated";

	/** Set DateAllocated	  */
	public void setDateAllocated (Timestamp DateAllocated);

	/** Get DateAllocated	  */
	public Timestamp getDateAllocated();

    /** Column name DiscountAmt */
    public static final String COLUMNNAME_DiscountAmt = "DiscountAmt";

	/** Set Discount Amount.
	  * Calculated amount of discount
	  */
	public void setDiscountAmt (BigDecimal DiscountAmt);

	/** Get Discount Amount.
	  * Calculated amount of discount
	  */
	public BigDecimal getDiscountAmt();

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

    /** Column name Match_DiscountAmt */
    public static final String COLUMNNAME_Match_DiscountAmt = "Match_DiscountAmt";

	/** Set Match_DiscountAmt	  */
	public void setMatch_DiscountAmt (BigDecimal Match_DiscountAmt);

	/** Get Match_DiscountAmt	  */
	public BigDecimal getMatch_DiscountAmt();

    /** Column name Match_DocType_ID */
    public static final String COLUMNNAME_Match_DocType_ID = "Match_DocType_ID";

	/** Set Match_DocType_ID	  */
	public void setMatch_DocType_ID (int Match_DocType_ID);

	/** Get Match_DocType_ID	  */
	public int getMatch_DocType_ID();

	public org.compiere.model.I_C_DocType getMatch_DocType() throws RuntimeException;

    /** Column name Match_Invoice_ID */
    public static final String COLUMNNAME_Match_Invoice_ID = "Match_Invoice_ID";

	/** Set Match_Invoice_ID	  */
	public void setMatch_Invoice_ID (int Match_Invoice_ID);

	/** Get Match_Invoice_ID	  */
	public int getMatch_Invoice_ID();

	public org.compiere.model.I_C_Invoice getMatch_Invoice() throws RuntimeException;

    /** Column name Match_Payment_ID */
    public static final String COLUMNNAME_Match_Payment_ID = "Match_Payment_ID";

	/** Set Match_Payment_ID	  */
	public void setMatch_Payment_ID (int Match_Payment_ID);

	/** Get Match_Payment_ID	  */
	public int getMatch_Payment_ID();

	public org.compiere.model.I_C_Payment getMatch_Payment() throws RuntimeException;

    /** Column name Match_WriteOffAmt */
    public static final String COLUMNNAME_Match_WriteOffAmt = "Match_WriteOffAmt";

	/** Set Match_WriteOffAmt	  */
	public void setMatch_WriteOffAmt (BigDecimal Match_WriteOffAmt);

	/** Get Match_WriteOffAmt	  */
	public BigDecimal getMatch_WriteOffAmt();

    /** Column name N_Amount */
    public static final String COLUMNNAME_N_Amount = "N_Amount";

	/** Set N_Amount	  */
	public void setN_Amount (BigDecimal N_Amount);

	/** Get N_Amount	  */
	public BigDecimal getN_Amount();

    /** Column name N_DiscountAmt */
    public static final String COLUMNNAME_N_DiscountAmt = "N_DiscountAmt";

	/** Set N_DiscountAmt	  */
	public void setN_DiscountAmt (BigDecimal N_DiscountAmt);

	/** Get N_DiscountAmt	  */
	public BigDecimal getN_DiscountAmt();

    /** Column name N_DocType_ID */
    public static final String COLUMNNAME_N_DocType_ID = "N_DocType_ID";

	/** Set N_DocType_ID	  */
	public void setN_DocType_ID (int N_DocType_ID);

	/** Get N_DocType_ID	  */
	public int getN_DocType_ID();

	public org.compiere.model.I_C_DocType getN_DocType() throws RuntimeException;

    /** Column name N_Invoice_ID */
    public static final String COLUMNNAME_N_Invoice_ID = "N_Invoice_ID";

	/** Set N_Invoice_ID	  */
	public void setN_Invoice_ID (int N_Invoice_ID);

	/** Get N_Invoice_ID	  */
	public int getN_Invoice_ID();

	public org.compiere.model.I_C_Invoice getN_Invoice() throws RuntimeException;

    /** Column name N_Payment_ID */
    public static final String COLUMNNAME_N_Payment_ID = "N_Payment_ID";

	/** Set N_Payment_ID	  */
	public void setN_Payment_ID (int N_Payment_ID);

	/** Get N_Payment_ID	  */
	public int getN_Payment_ID();

	public org.compiere.model.I_C_Payment getN_Payment() throws RuntimeException;

    /** Column name N_WriteOffAmt */
    public static final String COLUMNNAME_N_WriteOffAmt = "N_WriteOffAmt";

	/** Set N_WriteOffAmt	  */
	public void setN_WriteOffAmt (BigDecimal N_WriteOffAmt);

	/** Get N_WriteOffAmt	  */
	public BigDecimal getN_WriteOffAmt();

    /** Column name P_Amount */
    public static final String COLUMNNAME_P_Amount = "P_Amount";

	/** Set P_Amount	  */
	public void setP_Amount (BigDecimal P_Amount);

	/** Get P_Amount	  */
	public BigDecimal getP_Amount();

    /** Column name P_DiscountAmt */
    public static final String COLUMNNAME_P_DiscountAmt = "P_DiscountAmt";

	/** Set P_DiscountAmt	  */
	public void setP_DiscountAmt (BigDecimal P_DiscountAmt);

	/** Get P_DiscountAmt	  */
	public BigDecimal getP_DiscountAmt();

    /** Column name P_DocType_ID */
    public static final String COLUMNNAME_P_DocType_ID = "P_DocType_ID";

	/** Set P_DocType_ID	  */
	public void setP_DocType_ID (int P_DocType_ID);

	/** Get P_DocType_ID	  */
	public int getP_DocType_ID();

	public org.compiere.model.I_C_DocType getP_DocType() throws RuntimeException;

    /** Column name P_Invoice_ID */
    public static final String COLUMNNAME_P_Invoice_ID = "P_Invoice_ID";

	/** Set P_Invoice_ID	  */
	public void setP_Invoice_ID (int P_Invoice_ID);

	/** Get P_Invoice_ID	  */
	public int getP_Invoice_ID();

	public org.compiere.model.I_C_Invoice getP_Invoice() throws RuntimeException;

    /** Column name P_Payment_ID */
    public static final String COLUMNNAME_P_Payment_ID = "P_Payment_ID";

	/** Set P_Payment_ID	  */
	public void setP_Payment_ID (int P_Payment_ID);

	/** Get P_Payment_ID	  */
	public int getP_Payment_ID();

	public org.compiere.model.I_C_Payment getP_Payment() throws RuntimeException;

    /** Column name P_WriteOffAmt */
    public static final String COLUMNNAME_P_WriteOffAmt = "P_WriteOffAmt";

	/** Set P_WriteOffAmt	  */
	public void setP_WriteOffAmt (BigDecimal P_WriteOffAmt);

	/** Get P_WriteOffAmt	  */
	public BigDecimal getP_WriteOffAmt();

    /** Column name T_MatchAllocation_ID */
    public static final String COLUMNNAME_T_MatchAllocation_ID = "T_MatchAllocation_ID";

	/** Set T_MatchAllocation_ID	  */
	public void setT_MatchAllocation_ID (int T_MatchAllocation_ID);

	/** Get T_MatchAllocation_ID	  */
	public int getT_MatchAllocation_ID();

    /** Column name T_MatchAllocation_UU */
    public static final String COLUMNNAME_T_MatchAllocation_UU = "T_MatchAllocation_UU";

	/** Set T_MatchAllocation_UU	  */
	public void setT_MatchAllocation_UU (String T_MatchAllocation_UU);

	/** Get T_MatchAllocation_UU	  */
	public String getT_MatchAllocation_UU();

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

    /** Column name WriteOffAmt */
    public static final String COLUMNNAME_WriteOffAmt = "WriteOffAmt";

	/** Set Write-off Amount.
	  * Amount to write-off
	  */
	public void setWriteOffAmt (BigDecimal WriteOffAmt);

	/** Get Write-off Amount.
	  * Amount to write-off
	  */
	public BigDecimal getWriteOffAmt();
}
