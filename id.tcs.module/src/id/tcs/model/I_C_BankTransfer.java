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

/** Generated Interface for C_BankTransfer
 *  @author iDempiere (generated) 
 *  @version Release 5.1
 */
@SuppressWarnings("all")
public interface I_C_BankTransfer 
{

    /** TableName=C_BankTransfer */
    public static final String Table_Name = "C_BankTransfer";

    /** AD_Table_ID=300461 */
    public static final int Table_ID = 300461;

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

    /** Column name AmountFrom */
    public static final String COLUMNNAME_AmountFrom = "AmountFrom";

	/** Set Amount From	  */
	public void setAmountFrom (BigDecimal AmountFrom);

	/** Get Amount From	  */
	public BigDecimal getAmountFrom();

    /** Column name AmountTo */
    public static final String COLUMNNAME_AmountTo = "AmountTo";

	/** Set Amount To	  */
	public void setAmountTo (BigDecimal AmountTo);

	/** Get Amount To	  */
	public BigDecimal getAmountTo();

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

    /** Column name C_BankAccount_From_ID */
    public static final String COLUMNNAME_C_BankAccount_From_ID = "C_BankAccount_From_ID";

	/** Set Bank Account From	  */
	public void setC_BankAccount_From_ID (int C_BankAccount_From_ID);

	/** Get Bank Account From	  */
	public int getC_BankAccount_From_ID();

	public org.compiere.model.I_C_BankAccount getC_BankAccount_From() throws RuntimeException;

    /** Column name C_BankAccount_To_ID */
    public static final String COLUMNNAME_C_BankAccount_To_ID = "C_BankAccount_To_ID";

	/** Set Bank Account To	  */
	public void setC_BankAccount_To_ID (int C_BankAccount_To_ID);

	/** Get Bank Account To	  */
	public int getC_BankAccount_To_ID();

	public org.compiere.model.I_C_BankAccount getC_BankAccount_To() throws RuntimeException;

    /** Column name C_BankTransfer_ID */
    public static final String COLUMNNAME_C_BankTransfer_ID = "C_BankTransfer_ID";

	/** Set Bank Transfer	  */
	public void setC_BankTransfer_ID (int C_BankTransfer_ID);

	/** Get Bank Transfer	  */
	public int getC_BankTransfer_ID();

    /** Column name C_BankTransfer_UU */
    public static final String COLUMNNAME_C_BankTransfer_UU = "C_BankTransfer_UU";

	/** Set C_BankTransfer_UU	  */
	public void setC_BankTransfer_UU (String C_BankTransfer_UU);

	/** Get C_BankTransfer_UU	  */
	public String getC_BankTransfer_UU();

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

    /** Column name C_ConversionType_ID */
    public static final String COLUMNNAME_C_ConversionType_ID = "C_ConversionType_ID";

	/** Set Currency Type.
	  * Currency Conversion Rate Type
	  */
	public void setC_ConversionType_ID (int C_ConversionType_ID);

	/** Get Currency Type.
	  * Currency Conversion Rate Type
	  */
	public int getC_ConversionType_ID();

	public org.compiere.model.I_C_ConversionType getC_ConversionType() throws RuntimeException;

    /** Column name C_Currency_From_ID */
    public static final String COLUMNNAME_C_Currency_From_ID = "C_Currency_From_ID";

	/** Set Currency From	  */
	public void setC_Currency_From_ID (int C_Currency_From_ID);

	/** Get Currency From	  */
	public int getC_Currency_From_ID();

	public org.compiere.model.I_C_Currency getC_Currency_From() throws RuntimeException;

    /** Column name C_Currency_To_ID */
    public static final String COLUMNNAME_C_Currency_To_ID = "C_Currency_To_ID";

	/** Set Currency To	  */
	public void setC_Currency_To_ID (int C_Currency_To_ID);

	/** Get Currency To	  */
	public int getC_Currency_To_ID();

	public org.compiere.model.I_C_Currency getC_Currency_To() throws RuntimeException;

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

    /** Column name C_Payment_From_ID */
    public static final String COLUMNNAME_C_Payment_From_ID = "C_Payment_From_ID";

	/** Set Payment From	  */
	public void setC_Payment_From_ID (int C_Payment_From_ID);

	/** Get Payment From	  */
	public int getC_Payment_From_ID();

	public org.compiere.model.I_C_Payment getC_Payment_From() throws RuntimeException;

    /** Column name C_Payment_To_ID */
    public static final String COLUMNNAME_C_Payment_To_ID = "C_Payment_To_ID";

	/** Set Payment To	  */
	public void setC_Payment_To_ID (int C_Payment_To_ID);

	/** Get Payment To	  */
	public int getC_Payment_To_ID();

	public org.compiere.model.I_C_Payment getC_Payment_To() throws RuntimeException;

    /** Column name C_Payment_Transfer_ID */
    public static final String COLUMNNAME_C_Payment_Transfer_ID = "C_Payment_Transfer_ID";

	/** Set Payment Transfer	  */
	public void setC_Payment_Transfer_ID (int C_Payment_Transfer_ID);

	/** Get Payment Transfer	  */
	public int getC_Payment_Transfer_ID();

	public org.compiere.model.I_C_Payment getC_Payment_Transfer() throws RuntimeException;

    /** Column name ChargeAmt */
    public static final String COLUMNNAME_ChargeAmt = "ChargeAmt";

	/** Set Charge amount.
	  * Charge Amount
	  */
	public void setChargeAmt (BigDecimal ChargeAmt);

	/** Get Charge amount.
	  * Charge Amount
	  */
	public BigDecimal getChargeAmt();

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

    /** Column name DateAcct */
    public static final String COLUMNNAME_DateAcct = "DateAcct";

	/** Set Account Date.
	  * Accounting Date
	  */
	public void setDateAcct (Timestamp DateAcct);

	/** Get Account Date.
	  * Accounting Date
	  */
	public Timestamp getDateAcct();

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

    /** Column name DocAction */
    public static final String COLUMNNAME_DocAction = "DocAction";

	/** Set Document Action.
	  * The targeted status of the document
	  */
	public void setDocAction (String DocAction);

	/** Get Document Action.
	  * The targeted status of the document
	  */
	public String getDocAction();

    /** Column name DocStatus */
    public static final String COLUMNNAME_DocStatus = "DocStatus";

	/** Set Document Status.
	  * The current status of the document
	  */
	public void setDocStatus (String DocStatus);

	/** Get Document Status.
	  * The current status of the document
	  */
	public String getDocStatus();

    /** Column name DocumentNo */
    public static final String COLUMNNAME_DocumentNo = "DocumentNo";

	/** Set Document No.
	  * Document sequence number of the document
	  */
	public void setDocumentNo (String DocumentNo);

	/** Get Document No.
	  * Document sequence number of the document
	  */
	public String getDocumentNo();

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

    /** Column name IsCanceled */
    public static final String COLUMNNAME_IsCanceled = "IsCanceled";

	/** Set Canceled	  */
	public void setIsCanceled (boolean IsCanceled);

	/** Get Canceled	  */
	public boolean isCanceled();

    /** Column name isHasTransferFee */
    public static final String COLUMNNAME_isHasTransferFee = "isHasTransferFee";

	/** Set Transfer Fee	  */
	public void setisHasTransferFee (boolean isHasTransferFee);

	/** Get Transfer Fee	  */
	public boolean isHasTransferFee();

    /** Column name PayAmtFrom */
    public static final String COLUMNNAME_PayAmtFrom = "PayAmtFrom";

	/** Set Pay Amount From	  */
	public void setPayAmtFrom (BigDecimal PayAmtFrom);

	/** Get Pay Amount From	  */
	public BigDecimal getPayAmtFrom();

    /** Column name PayAmtTo */
    public static final String COLUMNNAME_PayAmtTo = "PayAmtTo";

	/** Set Pay Amount To	  */
	public void setPayAmtTo (BigDecimal PayAmtTo);

	/** Get Pay Amount To	  */
	public BigDecimal getPayAmtTo();

    /** Column name Processed */
    public static final String COLUMNNAME_Processed = "Processed";

	/** Set Processed.
	  * The document has been processed
	  */
	public void setProcessed (boolean Processed);

	/** Get Processed.
	  * The document has been processed
	  */
	public boolean isProcessed();

    /** Column name Processing */
    public static final String COLUMNNAME_Processing = "Processing";

	/** Set Process Now	  */
	public void setProcessing (boolean Processing);

	/** Get Process Now	  */
	public boolean isProcessing();

    /** Column name TransferFeeType */
    public static final String COLUMNNAME_TransferFeeType = "TransferFeeType";

	/** Set Transfer Fee Type	  */
	public void setTransferFeeType (String TransferFeeType);

	/** Get Transfer Fee Type	  */
	public String getTransferFeeType();

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
