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

/** Generated Model for C_BankTransfer
 *  @author iDempiere (generated) 
 *  @version Release 5.1 - $Id$ */
public class X_C_BankTransfer extends PO implements I_C_BankTransfer, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20190704L;

    /** Standard Constructor */
    public X_C_BankTransfer (Properties ctx, int C_BankTransfer_ID, String trxName)
    {
      super (ctx, C_BankTransfer_ID, trxName);
      /** if (C_BankTransfer_ID == 0)
        {
			setC_BankTransfer_ID (0);
			setC_BPartner_ID (0);
			setDateAcct (new Timestamp( System.currentTimeMillis() ));
        } */
    }

    /** Load Constructor */
    public X_C_BankTransfer (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_C_BankTransfer[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	/** Set Amount From.
		@param AmountFrom Amount From	  */
	public void setAmountFrom (BigDecimal AmountFrom)
	{
		set_Value (COLUMNNAME_AmountFrom, AmountFrom);
	}

	/** Get Amount From.
		@return Amount From	  */
	public BigDecimal getAmountFrom () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_AmountFrom);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Amount To.
		@param AmountTo Amount To	  */
	public void setAmountTo (BigDecimal AmountTo)
	{
		set_Value (COLUMNNAME_AmountTo, AmountTo);
	}

	/** Get Amount To.
		@return Amount To	  */
	public BigDecimal getAmountTo () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_AmountTo);
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

	public org.compiere.model.I_C_BankAccount getC_BankAccount_From() throws RuntimeException
    {
		return (org.compiere.model.I_C_BankAccount)MTable.get(getCtx(), org.compiere.model.I_C_BankAccount.Table_Name)
			.getPO(getC_BankAccount_From_ID(), get_TrxName());	}

	/** Set Bank Account From.
		@param C_BankAccount_From_ID Bank Account From	  */
	public void setC_BankAccount_From_ID (int C_BankAccount_From_ID)
	{
		if (C_BankAccount_From_ID < 1) 
			set_Value (COLUMNNAME_C_BankAccount_From_ID, null);
		else 
			set_Value (COLUMNNAME_C_BankAccount_From_ID, Integer.valueOf(C_BankAccount_From_ID));
	}

	/** Get Bank Account From.
		@return Bank Account From	  */
	public int getC_BankAccount_From_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_BankAccount_From_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public org.compiere.model.I_C_BankAccount getC_BankAccount_To() throws RuntimeException
    {
		return (org.compiere.model.I_C_BankAccount)MTable.get(getCtx(), org.compiere.model.I_C_BankAccount.Table_Name)
			.getPO(getC_BankAccount_To_ID(), get_TrxName());	}

	/** Set Bank Account To.
		@param C_BankAccount_To_ID Bank Account To	  */
	public void setC_BankAccount_To_ID (int C_BankAccount_To_ID)
	{
		if (C_BankAccount_To_ID < 1) 
			set_Value (COLUMNNAME_C_BankAccount_To_ID, null);
		else 
			set_Value (COLUMNNAME_C_BankAccount_To_ID, Integer.valueOf(C_BankAccount_To_ID));
	}

	/** Get Bank Account To.
		@return Bank Account To	  */
	public int getC_BankAccount_To_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_BankAccount_To_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Bank Transfer.
		@param C_BankTransfer_ID Bank Transfer	  */
	public void setC_BankTransfer_ID (int C_BankTransfer_ID)
	{
		if (C_BankTransfer_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_C_BankTransfer_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_C_BankTransfer_ID, Integer.valueOf(C_BankTransfer_ID));
	}

	/** Get Bank Transfer.
		@return Bank Transfer	  */
	public int getC_BankTransfer_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_BankTransfer_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set C_BankTransfer_UU.
		@param C_BankTransfer_UU C_BankTransfer_UU	  */
	public void setC_BankTransfer_UU (String C_BankTransfer_UU)
	{
		set_Value (COLUMNNAME_C_BankTransfer_UU, C_BankTransfer_UU);
	}

	/** Get C_BankTransfer_UU.
		@return C_BankTransfer_UU	  */
	public String getC_BankTransfer_UU () 
	{
		return (String)get_Value(COLUMNNAME_C_BankTransfer_UU);
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

	public org.compiere.model.I_C_Charge getC_Charge() throws RuntimeException
    {
		return (org.compiere.model.I_C_Charge)MTable.get(getCtx(), org.compiere.model.I_C_Charge.Table_Name)
			.getPO(getC_Charge_ID(), get_TrxName());	}

	/** Set Charge.
		@param C_Charge_ID 
		Additional document charges
	  */
	public void setC_Charge_ID (int C_Charge_ID)
	{
		if (C_Charge_ID < 1) 
			set_Value (COLUMNNAME_C_Charge_ID, null);
		else 
			set_Value (COLUMNNAME_C_Charge_ID, Integer.valueOf(C_Charge_ID));
	}

	/** Get Charge.
		@return Additional document charges
	  */
	public int getC_Charge_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_Charge_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public org.compiere.model.I_C_ConversionType getC_ConversionType() throws RuntimeException
    {
		return (org.compiere.model.I_C_ConversionType)MTable.get(getCtx(), org.compiere.model.I_C_ConversionType.Table_Name)
			.getPO(getC_ConversionType_ID(), get_TrxName());	}

	/** Set Currency Type.
		@param C_ConversionType_ID 
		Currency Conversion Rate Type
	  */
	public void setC_ConversionType_ID (int C_ConversionType_ID)
	{
		if (C_ConversionType_ID < 1) 
			set_Value (COLUMNNAME_C_ConversionType_ID, null);
		else 
			set_Value (COLUMNNAME_C_ConversionType_ID, Integer.valueOf(C_ConversionType_ID));
	}

	/** Get Currency Type.
		@return Currency Conversion Rate Type
	  */
	public int getC_ConversionType_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_ConversionType_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public org.compiere.model.I_C_Currency getC_Currency_From() throws RuntimeException
    {
		return (org.compiere.model.I_C_Currency)MTable.get(getCtx(), org.compiere.model.I_C_Currency.Table_Name)
			.getPO(getC_Currency_From_ID(), get_TrxName());	}

	/** Set Currency From.
		@param C_Currency_From_ID Currency From	  */
	public void setC_Currency_From_ID (int C_Currency_From_ID)
	{
		if (C_Currency_From_ID < 1) 
			set_Value (COLUMNNAME_C_Currency_From_ID, null);
		else 
			set_Value (COLUMNNAME_C_Currency_From_ID, Integer.valueOf(C_Currency_From_ID));
	}

	/** Get Currency From.
		@return Currency From	  */
	public int getC_Currency_From_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_Currency_From_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public org.compiere.model.I_C_Currency getC_Currency_To() throws RuntimeException
    {
		return (org.compiere.model.I_C_Currency)MTable.get(getCtx(), org.compiere.model.I_C_Currency.Table_Name)
			.getPO(getC_Currency_To_ID(), get_TrxName());	}

	/** Set Currency To.
		@param C_Currency_To_ID Currency To	  */
	public void setC_Currency_To_ID (int C_Currency_To_ID)
	{
		if (C_Currency_To_ID < 1) 
			set_Value (COLUMNNAME_C_Currency_To_ID, null);
		else 
			set_Value (COLUMNNAME_C_Currency_To_ID, Integer.valueOf(C_Currency_To_ID));
	}

	/** Get Currency To.
		@return Currency To	  */
	public int getC_Currency_To_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_Currency_To_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public org.compiere.model.I_C_DocType getC_DocType() throws RuntimeException
    {
		return (org.compiere.model.I_C_DocType)MTable.get(getCtx(), org.compiere.model.I_C_DocType.Table_Name)
			.getPO(getC_DocType_ID(), get_TrxName());	}

	/** Set Document Type.
		@param C_DocType_ID 
		Document type or rules
	  */
	public void setC_DocType_ID (int C_DocType_ID)
	{
		if (C_DocType_ID < 0) 
			set_ValueNoCheck (COLUMNNAME_C_DocType_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_C_DocType_ID, Integer.valueOf(C_DocType_ID));
	}

	/** Get Document Type.
		@return Document type or rules
	  */
	public int getC_DocType_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_DocType_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public org.compiere.model.I_C_Payment getC_Payment_From() throws RuntimeException
    {
		return (org.compiere.model.I_C_Payment)MTable.get(getCtx(), org.compiere.model.I_C_Payment.Table_Name)
			.getPO(getC_Payment_From_ID(), get_TrxName());	}

	/** Set Payment From.
		@param C_Payment_From_ID Payment From	  */
	public void setC_Payment_From_ID (int C_Payment_From_ID)
	{
		if (C_Payment_From_ID < 1) 
			set_Value (COLUMNNAME_C_Payment_From_ID, null);
		else 
			set_Value (COLUMNNAME_C_Payment_From_ID, Integer.valueOf(C_Payment_From_ID));
	}

	/** Get Payment From.
		@return Payment From	  */
	public int getC_Payment_From_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_Payment_From_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public org.compiere.model.I_C_Payment getC_Payment_To() throws RuntimeException
    {
		return (org.compiere.model.I_C_Payment)MTable.get(getCtx(), org.compiere.model.I_C_Payment.Table_Name)
			.getPO(getC_Payment_To_ID(), get_TrxName());	}

	/** Set Payment To.
		@param C_Payment_To_ID Payment To	  */
	public void setC_Payment_To_ID (int C_Payment_To_ID)
	{
		if (C_Payment_To_ID < 1) 
			set_Value (COLUMNNAME_C_Payment_To_ID, null);
		else 
			set_Value (COLUMNNAME_C_Payment_To_ID, Integer.valueOf(C_Payment_To_ID));
	}

	/** Get Payment To.
		@return Payment To	  */
	public int getC_Payment_To_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_Payment_To_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public org.compiere.model.I_C_Payment getC_Payment_Transfer() throws RuntimeException
    {
		return (org.compiere.model.I_C_Payment)MTable.get(getCtx(), org.compiere.model.I_C_Payment.Table_Name)
			.getPO(getC_Payment_Transfer_ID(), get_TrxName());	}

	/** Set Payment Transfer.
		@param C_Payment_Transfer_ID Payment Transfer	  */
	public void setC_Payment_Transfer_ID (int C_Payment_Transfer_ID)
	{
		if (C_Payment_Transfer_ID < 1) 
			set_Value (COLUMNNAME_C_Payment_Transfer_ID, null);
		else 
			set_Value (COLUMNNAME_C_Payment_Transfer_ID, Integer.valueOf(C_Payment_Transfer_ID));
	}

	/** Get Payment Transfer.
		@return Payment Transfer	  */
	public int getC_Payment_Transfer_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_Payment_Transfer_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Charge amount.
		@param ChargeAmt 
		Charge Amount
	  */
	public void setChargeAmt (BigDecimal ChargeAmt)
	{
		set_Value (COLUMNNAME_ChargeAmt, ChargeAmt);
	}

	/** Get Charge amount.
		@return Charge Amount
	  */
	public BigDecimal getChargeAmt () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_ChargeAmt);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Account Date.
		@param DateAcct 
		Accounting Date
	  */
	public void setDateAcct (Timestamp DateAcct)
	{
		set_ValueNoCheck (COLUMNNAME_DateAcct, DateAcct);
	}

	/** Get Account Date.
		@return Accounting Date
	  */
	public Timestamp getDateAcct () 
	{
		return (Timestamp)get_Value(COLUMNNAME_DateAcct);
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

	/** DocAction AD_Reference_ID=135 */
	public static final int DOCACTION_AD_Reference_ID=135;
	/** Complete = CO */
	public static final String DOCACTION_Complete = "CO";
	/** Approve = AP */
	public static final String DOCACTION_Approve = "AP";
	/** Reject = RJ */
	public static final String DOCACTION_Reject = "RJ";
	/** Post = PO */
	public static final String DOCACTION_Post = "PO";
	/** Void = VO */
	public static final String DOCACTION_Void = "VO";
	/** Close = CL */
	public static final String DOCACTION_Close = "CL";
	/** Reverse - Correct = RC */
	public static final String DOCACTION_Reverse_Correct = "RC";
	/** Reverse - Accrual = RA */
	public static final String DOCACTION_Reverse_Accrual = "RA";
	/** Invalidate = IN */
	public static final String DOCACTION_Invalidate = "IN";
	/** Re-activate = RE */
	public static final String DOCACTION_Re_Activate = "RE";
	/** <None> = -- */
	public static final String DOCACTION_None = "--";
	/** Prepare = PR */
	public static final String DOCACTION_Prepare = "PR";
	/** Unlock = XL */
	public static final String DOCACTION_Unlock = "XL";
	/** Wait Complete = WC */
	public static final String DOCACTION_WaitComplete = "WC";
	/** Set Document Action.
		@param DocAction 
		The targeted status of the document
	  */
	public void setDocAction (String DocAction)
	{

		set_Value (COLUMNNAME_DocAction, DocAction);
	}

	/** Get Document Action.
		@return The targeted status of the document
	  */
	public String getDocAction () 
	{
		return (String)get_Value(COLUMNNAME_DocAction);
	}

	/** DocStatus AD_Reference_ID=131 */
	public static final int DOCSTATUS_AD_Reference_ID=131;
	/** Drafted = DR */
	public static final String DOCSTATUS_Drafted = "DR";
	/** Completed = CO */
	public static final String DOCSTATUS_Completed = "CO";
	/** Approved = AP */
	public static final String DOCSTATUS_Approved = "AP";
	/** Not Approved = NA */
	public static final String DOCSTATUS_NotApproved = "NA";
	/** Voided = VO */
	public static final String DOCSTATUS_Voided = "VO";
	/** Invalid = IN */
	public static final String DOCSTATUS_Invalid = "IN";
	/** Reversed = RE */
	public static final String DOCSTATUS_Reversed = "RE";
	/** Closed = CL */
	public static final String DOCSTATUS_Closed = "CL";
	/** Unknown = ?? */
	public static final String DOCSTATUS_Unknown = "??";
	/** In Progress = IP */
	public static final String DOCSTATUS_InProgress = "IP";
	/** Waiting Payment = WP */
	public static final String DOCSTATUS_WaitingPayment = "WP";
	/** Waiting Confirmation = WC */
	public static final String DOCSTATUS_WaitingConfirmation = "WC";
	/** Set Document Status.
		@param DocStatus 
		The current status of the document
	  */
	public void setDocStatus (String DocStatus)
	{

		set_Value (COLUMNNAME_DocStatus, DocStatus);
	}

	/** Get Document Status.
		@return The current status of the document
	  */
	public String getDocStatus () 
	{
		return (String)get_Value(COLUMNNAME_DocStatus);
	}

	/** Set Document No.
		@param DocumentNo 
		Document sequence number of the document
	  */
	public void setDocumentNo (String DocumentNo)
	{
		set_ValueNoCheck (COLUMNNAME_DocumentNo, DocumentNo);
	}

	/** Get Document No.
		@return Document sequence number of the document
	  */
	public String getDocumentNo () 
	{
		return (String)get_Value(COLUMNNAME_DocumentNo);
	}

	/** Set Canceled.
		@param IsCanceled Canceled	  */
	public void setIsCanceled (boolean IsCanceled)
	{
		set_Value (COLUMNNAME_IsCanceled, Boolean.valueOf(IsCanceled));
	}

	/** Get Canceled.
		@return Canceled	  */
	public boolean isCanceled () 
	{
		Object oo = get_Value(COLUMNNAME_IsCanceled);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set Transfer Fee.
		@param isHasTransferFee Transfer Fee	  */
	public void setisHasTransferFee (boolean isHasTransferFee)
	{
		set_Value (COLUMNNAME_isHasTransferFee, Boolean.valueOf(isHasTransferFee));
	}

	/** Get Transfer Fee.
		@return Transfer Fee	  */
	public boolean isHasTransferFee () 
	{
		Object oo = get_Value(COLUMNNAME_isHasTransferFee);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set Pay Amount From.
		@param PayAmtFrom Pay Amount From	  */
	public void setPayAmtFrom (BigDecimal PayAmtFrom)
	{
		set_Value (COLUMNNAME_PayAmtFrom, PayAmtFrom);
	}

	/** Get Pay Amount From.
		@return Pay Amount From	  */
	public BigDecimal getPayAmtFrom () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_PayAmtFrom);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Pay Amount To.
		@param PayAmtTo Pay Amount To	  */
	public void setPayAmtTo (BigDecimal PayAmtTo)
	{
		set_Value (COLUMNNAME_PayAmtTo, PayAmtTo);
	}

	/** Get Pay Amount To.
		@return Pay Amount To	  */
	public BigDecimal getPayAmtTo () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_PayAmtTo);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Processed.
		@param Processed 
		The document has been processed
	  */
	public void setProcessed (boolean Processed)
	{
		set_Value (COLUMNNAME_Processed, Boolean.valueOf(Processed));
	}

	/** Get Processed.
		@return The document has been processed
	  */
	public boolean isProcessed () 
	{
		Object oo = get_Value(COLUMNNAME_Processed);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
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

	/** TransferFeeType AD_Reference_ID=300148 */
	public static final int TRANSFERFEETYPE_AD_Reference_ID=300148;
	/** Charge On Bank From = F */
	public static final String TRANSFERFEETYPE_ChargeOnBankFrom = "F";
	/** Charge On Bank To = T */
	public static final String TRANSFERFEETYPE_ChargeOnBankTo = "T";
	/** Set Transfer Fee Type.
		@param TransferFeeType Transfer Fee Type	  */
	public void setTransferFeeType (String TransferFeeType)
	{

		set_Value (COLUMNNAME_TransferFeeType, TransferFeeType);
	}

	/** Get Transfer Fee Type.
		@return Transfer Fee Type	  */
	public String getTransferFeeType () 
	{
		return (String)get_Value(COLUMNNAME_TransferFeeType);
	}
}