/******************************************************************************
 * Product: Adempiere ERP & CRM Smart Business Solution                       *
 * Copyright (C) 1999-2006 ComPiere, Inc. All Rights Reserved.                *
 * This program is free software; you can redistribute it and/or modify it    *
 * under the terms version 2 of the GNU General Public License as published   *
 * by the Free Software Foundation. This program is distributed in the hope   *
 * that it will be useful, but WITHOUT ANY WARRANTY; without even the implied *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
 * See the GNU General Public License for more details.                       *
 * You should have received a copy of the GNU General Public License along    *
 * with this program; if not, write to the Free Software Foundation, Inc.,    *
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     *
 * For the text or an alternative of this public license, you may reach us    *
 * ComPiere, Inc., 2620 Augustine Dr. #245, Santa Clara, CA 95054, USA        *
 * or via info@compiere.org or http://www.compiere.org/license.html           *
 *****************************************************************************/
package org.compiere.model;

import id.tcs.model.MTCS_AllocateCharge;
import id.tcs.model.X_TCS_AllocateCharge;
import id.tcs.model.X_T_MatchAllocation;

import java.io.File;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;

import org.adempiere.exceptions.AdempiereException;
import org.adempiere.exceptions.PeriodClosedException;
import org.compiere.process.DocAction;
import org.compiere.process.DocumentEngine;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;

/**
 *  Payment Allocation Model.
 * 	Allocation Trigger update C_BPartner
 *
 *  @author 	Jorg Janke
 *  @version 	$Id: MAllocationHdr.java,v 1.3 2006/07/30 00:51:03 jjanke Exp $
 *  @author victor.perez@e-evolution.com, e-Evolution http://www.e-evolution.com
 *  			<li>FR [ 1866214 ]  
 *				<li> http://sourceforge.net/tracker/index.php?func=detail&aid=1866214&group_id=176962&atid=879335
 * 				<li>FR [ 2520591 ] Support multiples calendar for Org 
 *				<li> http://sourceforge.net/tracker2/?func=detail&atid=879335&aid=2520591&group_id=176962 
 *				<li>BF [ 2880182 ] Error you can allocate a payment to invoice that was paid
 *				<li> https://sourceforge.net/tracker/index.php?func=detail&aid=2880182&group_id=176962&atid=879332
*/
public class TCS_MAllocationHdr extends X_C_AllocationHdr implements DocAction
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7787519874581251920L;
	/**	Tolerance Gain and Loss */
	private static final BigDecimal	TOLERANCE = BigDecimal.valueOf(0.02);
	
	/**
	 * 	Get Allocations of Payment
	 *	@param ctx context
	 *	@param C_Payment_ID payment
	 *	@return allocations of payment
	 *	@param trxName transaction
	 */
	public static TCS_MAllocationHdr[] getOfPayment (Properties ctx, int C_Payment_ID, String trxName)
	{
		String sql = "SELECT * FROM C_AllocationHdr h "
			+ "WHERE IsActive='Y'"
			+ " AND EXISTS (SELECT * FROM C_AllocationLine l "
				+ "WHERE h.C_AllocationHdr_ID=l.C_AllocationHdr_ID AND l.C_Payment_ID=?)";
		ArrayList<TCS_MAllocationHdr> list = new ArrayList<TCS_MAllocationHdr>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, trxName);
			pstmt.setInt(1, C_Payment_ID);
			rs = pstmt.executeQuery();
			while (rs.next())
				list.add (new TCS_MAllocationHdr(ctx, rs, trxName));
		}
		catch (Exception e)
		{
			s_log.log(Level.SEVERE, sql, e);
		}
		finally
		{
			DB.close(rs, pstmt);
			rs = null; pstmt = null;
		}
		TCS_MAllocationHdr[] retValue = new TCS_MAllocationHdr[list.size()];
		list.toArray(retValue);
		return retValue;
	}	//	getOfPayment

	/**
	 * 	Get Allocations of Invoice
	 *	@param ctx context
	 *	@param C_Invoice_ID payment
	 *	@return allocations of payment
	 *	@param trxName transaction
	 */
	public static TCS_MAllocationHdr[] getOfInvoice (Properties ctx, int C_Invoice_ID, String trxName)
	{
		String sql = "SELECT * FROM C_AllocationHdr h "
			+ "WHERE IsActive='Y'"
			+ " AND EXISTS (SELECT * FROM C_AllocationLine l "
				+ "WHERE h.C_AllocationHdr_ID=l.C_AllocationHdr_ID AND l.C_Invoice_ID=?)";
		ArrayList<TCS_MAllocationHdr> list = new ArrayList<TCS_MAllocationHdr>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, trxName);
			pstmt.setInt(1, C_Invoice_ID);
			rs = pstmt.executeQuery();
			while (rs.next())
				list.add (new TCS_MAllocationHdr(ctx, rs, trxName));
		}
		catch (Exception e)
		{
			s_log.log(Level.SEVERE, sql, e);
		}
		finally
		{
			DB.close(rs, pstmt);
			rs = null; pstmt = null;
		}
		TCS_MAllocationHdr[] retValue = new TCS_MAllocationHdr[list.size()];
		list.toArray(retValue);
		return retValue;
	}	//	getOfInvoice
	
	//FR [ 1866214 ]
	/**
	 * 	Get Allocations of Cash
	 *	@param ctx context
	 *	@param C_Cash_ID Cash ID
	 *	@return allocations of payment
	 *	@param trxName transaction
	 */
	public static TCS_MAllocationHdr[] getOfCash (Properties ctx, int C_Cash_ID, String trxName)
	{
		final String whereClause = "IsActive='Y'"
			+ " AND EXISTS (SELECT 1 FROM C_CashLine cl, C_AllocationLine al "
				+ "where cl.C_Cash_ID=? and al.C_CashLine_ID=cl.C_CashLine_ID "
						+ "and C_AllocationHdr.C_AllocationHdr_ID=al.C_AllocationHdr_ID)";
		Query query = MTable.get(ctx, I_C_AllocationHdr.Table_ID)
							.createQuery(whereClause, trxName);
		query.setParameters(C_Cash_ID);
		List<TCS_MAllocationHdr> list = query.list();
		TCS_MAllocationHdr[] retValue = new TCS_MAllocationHdr[list.size()];
		list.toArray(retValue);
		return retValue;
	}	//	getOfCash
	
	/**	Logger						*/
	private static CLogger s_log = CLogger.getCLogger(MAllocationHdr.class);
	
	
	/**************************************************************************
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param C_AllocationHdr_ID id
	 *	@param trxName transaction
	 */
	public TCS_MAllocationHdr (Properties ctx, int C_AllocationHdr_ID, String trxName)
	{
		super (ctx, C_AllocationHdr_ID, trxName);
		if (C_AllocationHdr_ID == 0)
		{
		//	setDocumentNo (null);
			setDateTrx (new Timestamp(System.currentTimeMillis()));
			setDateAcct (getDateTrx());
			setDocAction (DOCACTION_Complete);	// CO
			setDocStatus (DOCSTATUS_Drafted);	// DR
		//	setC_Currency_ID (0);
			setApprovalAmt (Env.ZERO);
			setIsApproved (false);
			setIsManual (false);
			//
			setPosted (false);
			setProcessed (false);
			setProcessing(false);
			setC_DocType_ID(MDocType.getDocType("CMA"));
		}
	}	//	MAllocation

	/**
	 * 	Mandatory New Constructor
	 *	@param ctx context
	 *	@param IsManual manual trx
	 *	@param DateTrx date (if null today)
	 *	@param C_Currency_ID currency
	 *	@param description description
	 *	@param trxName transaction
	 */
	public TCS_MAllocationHdr (Properties ctx, boolean IsManual, Timestamp DateTrx, 
		int C_Currency_ID, String description, String trxName)
	{
		this (ctx, 0, trxName);
		setIsManual(IsManual);
		if (DateTrx != null)
		{
			setDateTrx (DateTrx);
			setDateAcct (DateTrx);
		}
		setC_Currency_ID (C_Currency_ID);
		if (description != null)
			setDescription(description);
	}	//  create Allocation


	/** 
	 * 	Load Constructor
	 * 	@param ctx context
	 *	@param rs result set
	 *	@param trxName transaction
	 */
	public TCS_MAllocationHdr (Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	//	MAllocation

	/**	Lines						*/
	private TCS_MAllocationLine[]	m_lines = null;
	
	/**
	 * 	Get Lines
	 *	@param requery if true requery
	 *	@return lines
	 */
	public TCS_MAllocationLine[] getLines (boolean requery)
	{
		if (m_lines != null && m_lines.length != 0 && !requery) {
			set_TrxName(m_lines, get_TrxName());
			return m_lines;
		}
		//
		String sql = "SELECT * FROM C_AllocationLine WHERE C_AllocationHdr_ID=?";
		ArrayList<TCS_MAllocationLine> list = new ArrayList<TCS_MAllocationLine>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement (sql, get_TrxName());
			pstmt.setInt (1, getC_AllocationHdr_ID());
			rs = pstmt.executeQuery ();
			while (rs.next ())
			{
				TCS_MAllocationLine line = new TCS_MAllocationLine(getCtx(), rs, get_TrxName());
				line.setParent(this);
				list.add (line);
			}
		} 
		catch (Exception e)
		{
			log.log(Level.SEVERE, sql, e);
		}
		finally
		{
			DB.close(rs, pstmt);
			rs = null; pstmt = null;
		}
		//
		m_lines = new TCS_MAllocationLine[list.size ()];
		list.toArray (m_lines);
		return m_lines;
	}	//	getLines

	/**
	 * 	Set Processed
	 *	@param processed Processed
	 */
	public void setProcessed (boolean processed)
	{
		super.setProcessed (processed);
		if (get_ID() == 0)
			return;
		StringBuilder sql = new StringBuilder("UPDATE C_AllocationHdr SET Processed='")
			.append((processed ? "Y" : "N"))
			.append("' WHERE C_AllocationHdr_ID=").append(getC_AllocationHdr_ID());
		int no = DB.executeUpdate(sql.toString(), get_TrxName());
		m_lines = null;
		if (log.isLoggable(Level.FINE)) log.fine(processed + " - #" + no);
	}	//	setProcessed
	
	
	/**************************************************************************
	 * 	Before Save
	 *	@param newRecord
	 *	@return save
	 */
	protected boolean beforeSave (boolean newRecord)
	{
		//	Changed from Not to Active
		if (!newRecord && is_ValueChanged("IsActive") && isActive())
		{
			log.severe ("Cannot Re-Activate deactivated Allocations");
			return false;
		}
		return true;
	}	//	beforeSave

	/**
	 * 	Before Delete.
	 *	@return true if acct was deleted
	 */
	protected boolean beforeDelete ()
	{
		String trxName = get_TrxName();
		if (trxName == null || trxName.length() == 0)
			log.warning ("No transaction");
		if (isPosted())
		{
			MPeriod.testPeriodOpen(getCtx(), getDateTrx(), MDocType.DOCBASETYPE_PaymentAllocation, getAD_Org_ID());
			setPosted(false);
			MFactAcct.deleteEx (Table_ID, get_ID(), trxName);
		}
		//	Mark as Inactive
		setIsActive(false);		//	updated DB for line delete/process
		this.saveEx();

		//	Unlink
		getLines(true);
		if (!updateBP(true)) 
			return false;
		
		for (int i = 0; i < m_lines.length; i++)
		{
			TCS_MAllocationLine line = m_lines[i];
			line.deleteEx(true, trxName);
		}
		return true;
	}	//	beforeDelete

	/**
	 * 	After Save
	 *	@param newRecord
	 *	@param success
	 *	@return success
	 */
	protected boolean afterSave (boolean newRecord, boolean success)
	{
		return success;
	}	//	afterSave
	
	/**************************************************************************
	 * 	Process document
	 *	@param processAction document action
	 *	@return true if performed
	 */
	public boolean processIt (String processAction)
	{
		m_processMsg = null;
		DocumentEngine engine = new DocumentEngine (this, getDocStatus());
		return engine.processIt (processAction, getDocAction());
	}	//	processIt
	
	/**	Process Message 			*/
	private String		m_processMsg = null;
	/**	Just Prepared Flag			*/
	private boolean		m_justPrepared = false;

	/**
	 * 	Unlock Document.
	 * 	@return true if success 
	 */
	public boolean unlockIt()
	{
		if (log.isLoggable(Level.INFO)) log.info(toString());
		setProcessing(false);
		return true;
	}	//	unlockIt
	
	/**
	 * 	Invalidate Document
	 * 	@return true if success 
	 */
	public boolean invalidateIt()
	{
		if (log.isLoggable(Level.INFO)) log.info(toString());
		setDocAction(DOCACTION_Prepare);
		return true;
	}	//	invalidateIt
	
	/**
	 *	Prepare Document
	 * 	@return new status (In Progress or Invalid) 
	 */
	public String prepareIt()
	{
		if (log.isLoggable(Level.INFO)) log.info(toString());
		m_processMsg = ModelValidationEngine.get().fireDocValidate(this, ModelValidator.TIMING_BEFORE_PREPARE);
		if (m_processMsg != null)
			return DocAction.STATUS_Invalid;

		//	Std Period open?
		MPeriod.testPeriodOpen(getCtx(), getDateAcct(), MDocType.DOCBASETYPE_PaymentAllocation, getAD_Org_ID());
		getLines(true);
		if (m_lines.length == 0)
		{
			m_processMsg = "@NoLines@";
			return DocAction.STATUS_Invalid;
		}
		
		// Stop the Document Workflow if invoice to allocate is as paid
		if (!isReversal()) {
			for (TCS_MAllocationLine line :m_lines)
			{	
				if (line.getC_Invoice_ID() != 0)
				{
					StringBuilder whereClause = new StringBuilder(I_C_Invoice.COLUMNNAME_C_Invoice_ID).append("=? AND ") 
									   .append(I_C_Invoice.COLUMNNAME_IsPaid).append("=? AND ")
									   .append(I_C_Invoice.COLUMNNAME_DocStatus).append(" NOT IN (?,?)");
					boolean InvoiceIsPaid = new Query(getCtx(), I_C_Invoice.Table_Name, whereClause.toString(), get_TrxName())
					.setClient_ID()
					.setParameters(line.getC_Invoice_ID(), "Y", X_C_Invoice.DOCSTATUS_Voided, X_C_Invoice.DOCSTATUS_Reversed)
					.match();
					if (InvoiceIsPaid && line.getAmount().signum() > 0)
						throw new  AdempiereException("@ValidationError@ @C_Invoice_ID@ @IsPaid@");
				}
			}	
		}
		
		//	Add up Amounts & validate
		BigDecimal approval = Env.ZERO;
		for (int i = 0; i < m_lines.length; i++)
		{
			TCS_MAllocationLine line = m_lines[i];
			approval = approval.add(line.getWriteOffAmt()).add(line.getDiscountAmt());
			//	Make sure there is BP
			if (line.getC_BPartner_ID() == 0)
			{
				m_processMsg = "No Business Partner";
				return DocAction.STATUS_Invalid;
			}

			// IDEMPIERE-1850 - validate date against related docs
			if (line.getC_Invoice_ID() > 0) {
				if (line.getC_Invoice().getDateAcct().after(getDateAcct())) {
					m_processMsg = "Wrong allocation date";
					return DocAction.STATUS_Invalid;
				}
			}
			if (line.getC_Payment_ID() > 0) {
				if (line.getC_Payment().getDateAcct().after(getDateAcct())) {
					m_processMsg = "Wrong allocation date";
					return DocAction.STATUS_Invalid;
				}
			}
		}
		setApprovalAmt(approval);
		//
		m_processMsg = ModelValidationEngine.get().fireDocValidate(this, ModelValidator.TIMING_AFTER_PREPARE);
		if (m_processMsg != null)
			return DocAction.STATUS_Invalid;
		
		m_justPrepared = true;
		if (!DOCACTION_Complete.equals(getDocAction()))
			setDocAction(DOCACTION_Complete);
		
		return DocAction.STATUS_InProgress;
	}	//	prepareIt
	
	/**
	 * 	Approve Document
	 * 	@return true if success 
	 */
	public boolean  approveIt()
	{
		if (log.isLoggable(Level.INFO)) log.info(toString());
		setIsApproved(true);
		return true;
	}	//	approveIt
	
	/**
	 * 	Reject Approval
	 * 	@return true if success 
	 */
	public boolean rejectIt()
	{
		if (log.isLoggable(Level.INFO)) log.info(toString());
		setIsApproved(false);
		return true;
	}	//	rejectIt
	
	/**
	 * 	Complete Document
	 * 	@return new status (Complete, In Progress, Invalid, Waiting ..)
	 */
	public String completeIt()
	{
		//	Re-Check
		if (!m_justPrepared)
		{
			String status = prepareIt();
			m_justPrepared = false;
			if (!DocAction.STATUS_InProgress.equals(status))
				return status;
		}

		m_processMsg = ModelValidationEngine.get().fireDocValidate(this, ModelValidator.TIMING_BEFORE_COMPLETE);
		if (m_processMsg != null)
			return DocAction.STATUS_Invalid;
		
		//	Implicit Approval
		if (!isApproved())
			approveIt();
		if (log.isLoggable(Level.INFO)) log.info(toString());

		//	Link
		getLines(false);
		if(!updateBP(isReversal()))
			return DocAction.STATUS_Invalid;
		
		for (int i = 0; i < m_lines.length; i++)
		{
			TCS_MAllocationLine line = m_lines[i];
			line.processIt(isReversal());
		}		

		//	User Validation
		String valid = ModelValidationEngine.get().fireDocValidate(this, ModelValidator.TIMING_AFTER_COMPLETE);
		if (valid != null)
		{
			m_processMsg = valid;
			return DocAction.STATUS_Invalid;
		}

		setProcessed(true);
		setDocAction(DOCACTION_Close);
		return DocAction.STATUS_Completed;
	}	//	completeIt
	
	/**
	 * 	Void Document.
	 * 	Same as Close.
	 * 	@return true if success 
	 */
	public boolean voidIt()
	{
		if (log.isLoggable(Level.INFO)) log.info(toString());
		
		boolean retValue = false;
		if (DOCSTATUS_Closed.equals(getDocStatus())
			|| DOCSTATUS_Reversed.equals(getDocStatus())
			|| DOCSTATUS_Voided.equals(getDocStatus()))
		{
			m_processMsg = "Document Closed: " + getDocStatus();
			setDocAction(DOCACTION_None);
			return false;
		}

		//	Not Processed
		if (DOCSTATUS_Drafted.equals(getDocStatus())
			|| DOCSTATUS_Invalid.equals(getDocStatus())
			|| DOCSTATUS_InProgress.equals(getDocStatus())
			|| DOCSTATUS_Approved.equals(getDocStatus())
			|| DOCSTATUS_NotApproved.equals(getDocStatus()) )
		{
			// Before Void
			m_processMsg = ModelValidationEngine.get().fireDocValidate(this,ModelValidator.TIMING_BEFORE_VOID);
			if (m_processMsg != null)
				return false;

			//	Set lines to 0
			TCS_MAllocationLine[] lines = getLines(true);
			if(!updateBP(true))
				return false;
			
			for (int i = 0; i < lines.length; i++)
			{
				TCS_MAllocationLine line = lines[i];
				line.setAmount(Env.ZERO);
				line.setDiscountAmt(Env.ZERO);
				line.setWriteOffAmt(Env.ZERO);
				line.setOverUnderAmt(Env.ZERO);
				line.saveEx();
				// Unlink invoices
				line.processIt(true);
			}			
			
			addDescription(Msg.getMsg(getCtx(), "Voided"));
			retValue = true;
		}
		else
		{
			boolean accrual = false;
			try 
			{
				MPeriod.testPeriodOpen(getCtx(), getDateTrx(), MPeriodControl.DOCBASETYPE_PaymentAllocation, getAD_Org_ID());
			}
			catch (PeriodClosedException e) 
			{
				accrual = true;
			}
			
			if (accrual)
				return reverseAccrualIt();
			else
				return reverseCorrectIt();
		}

		// After Void
		m_processMsg = ModelValidationEngine.get().fireDocValidate(this,ModelValidator.TIMING_AFTER_VOID);
		if (m_processMsg != null)
			return false;
		
		setProcessed(true);
		setDocAction(DOCACTION_None);

		return retValue;
	}	//	voidIt
	
	/**
	 * 	Close Document.
	 * 	Cancel not delivered Qunatities
	 * 	@return true if success 
	 */
	public boolean closeIt()
	{
		if (log.isLoggable(Level.INFO)) log.info(toString());
		// Before Close
		m_processMsg = ModelValidationEngine.get().fireDocValidate(this,ModelValidator.TIMING_BEFORE_CLOSE);
		if (m_processMsg != null)
			return false;

		setDocAction(DOCACTION_None);

		// After Close
		m_processMsg = ModelValidationEngine.get().fireDocValidate(this,ModelValidator.TIMING_AFTER_CLOSE);
		if (m_processMsg != null)
			return false;

		return true;
	}	//	closeIt
	
	/**
	 * 	Reverse Correction
	 * 	@return true if success 
	 */
	public boolean reverseCorrectIt()
	{
		if (log.isLoggable(Level.INFO)) log.info(toString());
		// Before reverseCorrect
		m_processMsg = ModelValidationEngine.get().fireDocValidate(this,ModelValidator.TIMING_BEFORE_REVERSECORRECT);
		if (m_processMsg != null)
			return false;
		
		boolean retValue = reverseIt(false);

		// After reverseCorrect
		m_processMsg = ModelValidationEngine.get().fireDocValidate(this,ModelValidator.TIMING_AFTER_REVERSECORRECT);
		if (m_processMsg != null)
			return false;
		
		setDocAction(DOCACTION_None);
		return retValue;
	}	//	reverseCorrectionIt
	
	/**
	 * 	Reverse Accrual - none
	 * 	@return false 
	 */
	public boolean reverseAccrualIt()
	{
		if (log.isLoggable(Level.INFO)) log.info(toString());
		// Before reverseAccrual
		m_processMsg = ModelValidationEngine.get().fireDocValidate(this,ModelValidator.TIMING_BEFORE_REVERSEACCRUAL);
		if (m_processMsg != null)
			return false;
		
		boolean retValue = reverseIt(true);

		// After reverseAccrual
		m_processMsg = ModelValidationEngine.get().fireDocValidate(this,ModelValidator.TIMING_AFTER_REVERSEACCRUAL);
		if (m_processMsg != null)
			return false;
		
		setDocAction(DOCACTION_None);
		return retValue;
	}	//	reverseAccrualIt
	
	/** 
	 * 	Re-activate
	 * 	@return false 
	 */
	public boolean reActivateIt()
	{
		if (log.isLoggable(Level.INFO)) log.info(toString());
		// Before reActivate
		m_processMsg = ModelValidationEngine.get().fireDocValidate(this,ModelValidator.TIMING_BEFORE_REACTIVATE);
		if (m_processMsg != null)
			return false;	
		
		// After reActivate
		m_processMsg = ModelValidationEngine.get().fireDocValidate(this,ModelValidator.TIMING_AFTER_REACTIVATE);
		if (m_processMsg != null)
			return false;
		
		return false;
	}	//	reActivateIt

	/**
	 * 	String Representation
	 *	@return info
	 */
	public String toString ()
	{
		StringBuilder sb = new StringBuilder ("MAllocationHdr[");
		sb.append(get_ID()).append("-").append(getSummary()).append ("]");
		return sb.toString ();
	}	//	toString

	/**
	 * 	Get Document Info
	 *	@return document info (untranslated)
	 */
	public String getDocumentInfo()
	{
		StringBuilder msgreturn = new StringBuilder().append(Msg.getElement(getCtx(), "C_AllocationHdr_ID")).append(" ").append(getDocumentNo());
		return msgreturn.toString();
	}	//	getDocumentInfo

	/**
	 * 	Create PDF
	 *	@return File or null
	 */
	public File createPDF ()
	{
		try
		{
			StringBuilder msgctf = new StringBuilder().append(get_TableName()).append(get_ID()).append("_");
			File temp = File.createTempFile(msgctf.toString(), ".pdf");
			return createPDF (temp);
		}
		catch (Exception e)
		{
			log.severe("Could not create PDF - " + e.getMessage());
		}
		return null;
	}	//	getPDF

	/**
	 * 	Create PDF file
	 *	@param file output file
	 *	@return file if success
	 */
	public File createPDF (File file)
	{
	//	ReportEngine re = ReportEngine.get (getCtx(), ReportEngine.INVOICE, getC_Invoice_ID());
	//	if (re == null)
			return null;
	//	return re.getPDF(file);
	}	//	createPDF

	/*************************************************************************
	 * 	Get Summary
	 *	@return Summary of Document
	 */
	public String getSummary()
	{
		StringBuilder sb = new StringBuilder();
		sb.append(getDocumentNo());
		//	: Total Lines = 123.00 (#1)
		sb.append(": ")
			.append(Msg.translate(getCtx(),"ApprovalAmt")).append("=").append(getApprovalAmt())
			.append(" (#").append(getLines(false).length).append(")");
		//	 - Description
		if (getDescription() != null && getDescription().length() > 0)
			sb.append(" - ").append(getDescription());
		return sb.toString();
	}	//	getSummary
	
	/**
	 * 	Get Process Message
	 *	@return clear text error message
	 */
	public String getProcessMsg()
	{
		return m_processMsg;
	}	//	getProcessMsg
	
	/**
	 * 	Get Document Owner (Responsible)
	 *	@return AD_User_ID
	 */
	public int getDoc_User_ID()
	{
		return getCreatedBy();
	}	//	getDoc_User_ID

	/**
	 * 	Add to Description
	 *	@param description text
	 */
	public void addDescription (String description)
	{
		String desc = getDescription();
		if (desc == null)
			setDescription(description);
		else
			setDescription(desc + " | " + description);
	}	//	addDescription
	
	/**************************************************************************
	 * 	Reverse Allocation.
	 * 	Period needs to be open
	 *	@return true if reversed
	 */
	private boolean reverseIt(boolean accrual) 
	{
		if (!isActive()
			|| getDocStatus().equals(DOCSTATUS_Voided)	// Goodwill.co.id
			|| getDocStatus().equals(DOCSTATUS_Reversed))
		{
			// Goodwill: don't throw exception here
			//	BF: Reverse is not allowed at Payment void when Allocation is already reversed at Invoice void
			//throw new IllegalStateException("Allocation already reversed (not active)");
			log.warning("Allocation already reversed (not active)");
			return true;
		}
	

		Timestamp reversalDate = accrual ? Env.getContextAsDate(getCtx(), "#Date") : getDateAcct();
		if (reversalDate == null) {
			reversalDate = new Timestamp(System.currentTimeMillis());
		}
		
		//	Can we delete posting
		MPeriod.testPeriodOpen(getCtx(), reversalDate, MPeriodControl.DOCBASETYPE_PaymentAllocation, getAD_Org_ID());

		if (accrual) 
		{
			//	Deep Copy
			TCS_MAllocationHdr reversal = copyFrom (this, reversalDate, reversalDate, get_TrxName());
			if (reversal == null)
			{
				m_processMsg = "Could not create Payment Allocation Reversal";
				return false;
			}
			reversal.setReversal_ID(getC_AllocationHdr_ID());

			//	Reverse Line Amt
			TCS_MAllocationLine[] rLines = reversal.getLines(false);
			for (TCS_MAllocationLine rLine : rLines) {
				rLine.setAmount(rLine.getAmount().negate());
				rLine.setDiscountAmt(rLine.getDiscountAmt().negate());
				rLine.setWriteOffAmt(rLine.getWriteOffAmt().negate());
				rLine.setOverUnderAmt(rLine.getOverUnderAmt().negate());
				if (!rLine.save(get_TrxName()))
				{
					m_processMsg = "Could not correct Payment Allocation Reversal Line";
					return false;
				}
			}
			reversal.setReversal(true);
			reversal.setDocumentNo(getDocumentNo()+"^");		
			reversal.addDescription("{->" + getDocumentNo() + ")");
			//
			if (!DocumentEngine.processIt(reversal, DocAction.ACTION_Complete))
			{
				m_processMsg = "Reversal ERROR: " + reversal.getProcessMsg();
				return false;
			}

			DocumentEngine.processIt(reversal, DocAction.ACTION_Close);
			reversal.setProcessing (false);
			reversal.setDocStatus(DOCSTATUS_Reversed);
			reversal.setDocAction(DOCACTION_None);
			reversal.saveEx();
			m_processMsg = reversal.getDocumentNo();
			addDescription("(" + reversal.getDocumentNo() + "<-)");
			
		}
		else
		{
			//	Set Inactive
			setIsActive (false);
			if ( !isPosted() )
				setPosted(true);
			setDocumentNo(getDocumentNo()+"^");
			setDocStatus(DOCSTATUS_Reversed);	//	for direct calls
			if (!save() || isActive())
				throw new IllegalStateException("Cannot de-activate allocation");
				
			//	Delete Posting
			MFactAcct.deleteEx(MAllocationHdr.Table_ID, getC_AllocationHdr_ID(), get_TrxName());
			
			//	Unlink Invoices
			getLines(true);
			if(!updateBP(true))
				return false;
			
			for (int i = 0; i < m_lines.length; i++)
			{
				TCS_MAllocationLine line = m_lines[i];
				line.setIsActive(false);
				line.setAmount(Env.ZERO);
				line.setDiscountAmt(Env.ZERO);
				line.setWriteOffAmt(Env.ZERO);
				line.setOverUnderAmt(Env.ZERO);
				line.saveEx();
				line.processIt(true);	//	reverse
			}			
			
			addDescription(Msg.getMsg(getCtx(), "Voided"));
		}
		
		setProcessed(true);
		setDocStatus(DOCSTATUS_Reversed);	//	may come from void
		setDocAction(DOCACTION_None);
		return true;
	}	//	reverse

	private boolean updateBP(boolean reverse)
	{
		
		getLines(false);
		for (TCS_MAllocationLine line : m_lines) {
			int C_Payment_ID = line.getC_Payment_ID();
			int C_BPartner_ID = line.getC_BPartner_ID();
			int M_Invoice_ID = line.getC_Invoice_ID();

			if ((C_BPartner_ID == 0) || ((M_Invoice_ID == 0) && (C_Payment_ID == 0)))
				continue;

			boolean isSOTrxInvoice = false;
			MInvoice invoice = M_Invoice_ID > 0 ? new MInvoice (getCtx(), M_Invoice_ID, get_TrxName()) : null;
			if (M_Invoice_ID > 0)
				isSOTrxInvoice = invoice.isSOTrx();
			
			MBPartner bpartner = new MBPartner (getCtx(), line.getC_BPartner_ID(), get_TrxName());
			DB.getDatabase().forUpdate(bpartner, 0);

			BigDecimal allocAmt = line.getAmount().add(line.getDiscountAmt()).add(line.getWriteOffAmt());
			BigDecimal openBalanceDiff = Env.ZERO;
			MClient client = MClient.get(getCtx(), getAD_Client_ID());
			
			boolean paymentProcessed = false;
			boolean paymentIsReceipt = false;
			
			// Retrieve payment information
			if (C_Payment_ID > 0)
			{
				MPayment payment = null;
				int convTypeID = 0;
				Timestamp paymentDate = null;
				
				payment = new MPayment (getCtx(), C_Payment_ID, get_TrxName());
				convTypeID = payment.getC_ConversionType_ID();
				paymentDate = payment.getDateAcct();
				paymentProcessed = payment.isProcessed();
				paymentIsReceipt = payment.isReceipt();
						
				// Adjust open amount with allocated amount. 
				if (paymentProcessed)
				{
					if (invoice != null)
					{
						// If payment is already processed, only adjust open balance by discount and write off amounts.
						BigDecimal amt = MConversionRate.convertBase(getCtx(), line.getWriteOffAmt().add(line.getDiscountAmt()),
								getC_Currency_ID(), paymentDate, convTypeID, getAD_Client_ID(), getAD_Org_ID());
						if (amt == null)
						{
							m_processMsg = MConversionRateUtil.getErrorMessage(getCtx(), "ErrorConvertingAllocationCurrencyToBaseCurrency",
									getC_Currency_ID(), MClient.get(getCtx()).getC_Currency_ID(), convTypeID, paymentDate, get_TrxName());
							return false;
						}
						openBalanceDiff = openBalanceDiff.add(amt);
					}
					else
					{
						// Allocating payment to payment.
						BigDecimal amt = MConversionRate.convertBase(getCtx(), allocAmt,
								getC_Currency_ID(), paymentDate, convTypeID, getAD_Client_ID(), getAD_Org_ID());
						if (amt == null)
						{
							m_processMsg = MConversionRateUtil.getErrorMessage(getCtx(), "ErrorConvertingAllocationCurrencyToBaseCurrency",
									getC_Currency_ID(), MClient.get(getCtx()).getC_Currency_ID(), convTypeID, paymentDate, get_TrxName());
							return false;
						}
						openBalanceDiff = openBalanceDiff.add(amt);
					}
				} else {
					// If payment has not been processed, adjust open balance by entire allocated amount.
					BigDecimal allocAmtBase = MConversionRate.convertBase(getCtx(), allocAmt,	
							getC_Currency_ID(), getDateAcct(), convTypeID, getAD_Client_ID(), getAD_Org_ID());
					if (allocAmtBase == null)
					{
						m_processMsg = MConversionRateUtil.getErrorMessage(getCtx(), "ErrorConvertingAllocationCurrencyToBaseCurrency",
								getC_Currency_ID(), MClient.get(getCtx()).getC_Currency_ID(), convTypeID, getDateAcct(), get_TrxName());
						return false;
					}
	
					openBalanceDiff = openBalanceDiff.add(allocAmtBase);
				}
			}
			else if (invoice != null)
			{
				// adjust open balance by discount and write off amounts.
				BigDecimal amt = MConversionRate.convertBase(getCtx(), line.getWriteOffAmt().add(line.getDiscountAmt()),
						getC_Currency_ID(), invoice.getDateAcct(), invoice.getC_ConversionType_ID(), getAD_Client_ID(), getAD_Org_ID());
				if (amt == null)
				{
					m_processMsg = MConversionRateUtil.getErrorMessage(getCtx(), "ErrorConvertingAllocationCurrencyToBaseCurrency",
							getC_Currency_ID(), MClient.get(getCtx()).getC_Currency_ID(), invoice.getC_ConversionType_ID(), invoice.getDateAcct(), get_TrxName());
					return false;
				}
				openBalanceDiff = openBalanceDiff.add(amt);
			}
			
			// Adjust open amount for currency gain/loss
			if ((invoice != null) && 
					((getC_Currency_ID() != client.getC_Currency_ID()) ||
					 (getC_Currency_ID() != invoice.getC_Currency_ID())))
			{
				if (getC_Currency_ID() != invoice.getC_Currency_ID())
				{
					allocAmt = MConversionRate.convert(getCtx(), allocAmt,	
							getC_Currency_ID(), invoice.getC_Currency_ID(), getDateAcct(), invoice.getC_ConversionType_ID(), getAD_Client_ID(), getAD_Org_ID());
					if (allocAmt == null)
					{
						m_processMsg = MConversionRateUtil.getErrorMessage(getCtx(), "ErrorConvertingAllocationCurrencyToInvoiceCurrency",
								getC_Currency_ID(), invoice.getC_Currency_ID(), invoice.getC_ConversionType_ID(), getDateAcct(), get_TrxName());
						return false;
					}
				}
				BigDecimal invAmtAccted = MConversionRate.convertBase(getCtx(), invoice.getGrandTotal(),	
						invoice.getC_Currency_ID(), invoice.getDateAcct(), invoice.getC_ConversionType_ID(), getAD_Client_ID(), getAD_Org_ID());
				if (invAmtAccted == null)
				{
					m_processMsg = MConversionRateUtil.getErrorMessage(getCtx(), "ErrorConvertingInvoiceCurrencyToBaseCurrency",
							invoice.getC_Currency_ID(), MClient.get(getCtx()).getC_Currency_ID(), invoice.getC_ConversionType_ID(), invoice.getDateAcct(), get_TrxName());
					return false;
				}
				
				BigDecimal allocAmtAccted = MConversionRate.convertBase(getCtx(), allocAmt,	
						invoice.getC_Currency_ID(), getDateAcct(), invoice.getC_ConversionType_ID(), getAD_Client_ID(), getAD_Org_ID());
				if (allocAmtAccted == null)
				{
					m_processMsg = MConversionRateUtil.getErrorMessage(getCtx(), "ErrorConvertingInvoiceCurrencyToBaseCurrency",
							invoice.getC_Currency_ID(), MClient.get(getCtx()).getC_Currency_ID(), invoice.getC_ConversionType_ID(), getDateAcct(), get_TrxName());
					return false;
				}

				if (allocAmt.compareTo(invoice.getGrandTotal()) == 0)
				{
					openBalanceDiff = openBalanceDiff.add(invAmtAccted).subtract(allocAmtAccted);
				}
				else
				{
					//	allocation as a percentage of the invoice
					double multiplier = allocAmt.doubleValue() / invoice.getGrandTotal().doubleValue();
					//	Reduce Orig Invoice Accounted
					invAmtAccted = invAmtAccted.multiply(BigDecimal.valueOf(multiplier));
					//	Difference based on percentage of Orig Invoice
					openBalanceDiff = openBalanceDiff.add(invAmtAccted).subtract(allocAmtAccted);	//	gain is negative
					//	ignore Tolerance
					if (openBalanceDiff.abs().compareTo(TOLERANCE) < 0)
						openBalanceDiff = Env.ZERO;
					//	Round
					int precision = MCurrency.getStdPrecision(getCtx(), client.getC_Currency_ID());
					if (openBalanceDiff.scale() > precision)
						openBalanceDiff = openBalanceDiff.setScale(precision, BigDecimal.ROUND_HALF_UP);
				}
			}			
			
			//	Total Balance
			BigDecimal newBalance = bpartner.getTotalOpenBalance();
			if (newBalance == null)
				newBalance = Env.ZERO;
			
			BigDecimal originalBalance = new BigDecimal(newBalance.toString());

			if (openBalanceDiff.signum() != 0)
			{
				if (reverse)
					newBalance = newBalance.add(openBalanceDiff);
				else
					newBalance = newBalance.subtract(openBalanceDiff);
			}

			// Update BP Credit Used only for Customer Invoices and for payment-to-payment allocations.
			BigDecimal newCreditAmt = Env.ZERO;
			if (isSOTrxInvoice || (invoice == null && paymentIsReceipt && paymentProcessed))
			{
				if (invoice == null)
					openBalanceDiff = openBalanceDiff.negate();

				newCreditAmt = bpartner.getSO_CreditUsed();

				if(reverse)
				{
					if (newCreditAmt == null)
						newCreditAmt = openBalanceDiff;
					else
						newCreditAmt = newCreditAmt.add(openBalanceDiff);
				}
				else
				{
					if (newCreditAmt == null)
						newCreditAmt = openBalanceDiff.negate();
					else
						newCreditAmt = newCreditAmt.subtract(openBalanceDiff);
				}

				if (log.isLoggable(Level.FINE))
				{
					log.fine("TotalOpenBalance=" + bpartner.getTotalOpenBalance() + "(" + openBalanceDiff
							+ ", Credit=" + bpartner.getSO_CreditUsed() + "->" + newCreditAmt
							+ ", Balance=" + bpartner.getTotalOpenBalance() + " -> " + newBalance);
				}
				bpartner.setSO_CreditUsed(newCreditAmt);
			}
			else
			{
				if (log.isLoggable(Level.FINE))
				{
					log.fine("TotalOpenBalance=" + bpartner.getTotalOpenBalance() + "(" + openBalanceDiff
							+ ", Balance=" + bpartner.getTotalOpenBalance() + " -> " + newBalance);				
				}
			}

			if (newBalance.compareTo(originalBalance) != 0)
				bpartner.setTotalOpenBalance(newBalance);
			
			bpartner.setSOCreditStatus();
			if (!bpartner.save(get_TrxName()))
			{
				m_processMsg = "Could not update Business Partner";
				return false;
			}

		} // for all lines

		return true;
	}	//	updateBP
	
	/**
	 * 	Document Status is Complete or Closed
	 *	@return true if CO, CL or RE
	 */
	public boolean isComplete()
	{
		String ds = getDocStatus();
		return DOCSTATUS_Completed.equals(ds) 
			|| DOCSTATUS_Closed.equals(ds)
			|| DOCSTATUS_Reversed.equals(ds);
	}	//	isComplete

	/**
	 * 	Create new Allocation by copying
	 * 	@param from allocation
	 * 	@param dateAcct date of the document accounting date
	 *  @param dateTrx date of the document transaction.
	 * 	@param trxName
	 *	@return Allocation
	 */
	public static TCS_MAllocationHdr copyFrom (TCS_MAllocationHdr from, Timestamp dateAcct, Timestamp dateTrx,
		String trxName)
	{
		TCS_MAllocationHdr to = new TCS_MAllocationHdr (from.getCtx(), 0, trxName);
		PO.copyValues (from, to, from.getAD_Client_ID(), from.getAD_Org_ID());
		to.set_ValueNoCheck ("DocumentNo", null);
		//
		to.setDocStatus (DOCSTATUS_Drafted);		//	Draft
		to.setDocAction(DOCACTION_Complete);
		//
		to.setDateTrx (dateAcct);
		to.setDateAcct (dateTrx);
		to.setIsManual(false);
		//
		to.setIsApproved (false);
		//
		to.setPosted (false);
		to.setProcessed (false);

		to.saveEx();

		//	Lines
		if (to.copyLinesFrom(from) == 0)
			throw new AdempiereException("Could not create Allocation Lines");

		return to;
	}	//	copyFrom
	
	/**
	 * 	Copy Lines From other Allocation.
	 *	@param otherAllocation allocation
	 *	@return number of lines copied
	 */
	public int copyLinesFrom (TCS_MAllocationHdr otherAllocation)
	{
		if (isProcessed() || isPosted() || (otherAllocation == null))
			return 0;
		TCS_MAllocationLine[] fromLines = otherAllocation.getLines(false);
		int count = 0;
		for (TCS_MAllocationLine fromLine : fromLines) {
			TCS_MAllocationLine line = new TCS_MAllocationLine (getCtx(), 0, get_TrxName());
			PO.copyValues (fromLine, line, fromLine.getAD_Client_ID(), fromLine.getAD_Org_ID());
			line.setC_AllocationHdr_ID(getC_AllocationHdr_ID());
			line.setParent(this);
			line.set_ValueNoCheck ("C_AllocationLine_ID", I_ZERO);	// new

			if (line.getC_Payment_ID() != 0)
			{
				MPayment payment = new MPayment(getCtx(), line.getC_Payment_ID(), get_TrxName());
				if (DOCSTATUS_Reversed.equals(payment.getDocStatus()))
				{
					MPayment reversal = (MPayment) payment.getReversal();
					if (reversal != null)
					{
						line.setPaymentInfo(reversal.getC_Payment_ID(), 0);
					}
				}				
			}

			line.saveEx();
			count++;
		}
		if (fromLines.length != count)
			log.log(Level.WARNING, "Line difference - From=" + fromLines.length + " <> Saved=" + count);
		return count;
	}	//	copyLinesFrom
	
	// Goodwill.co.id
	/** Reversal Flag		*/
	private boolean m_reversal = false;
	
	/**
	 * 	Set Reversal
	 *	@param reversal reversal
	 */
	private void setReversal(boolean reversal)
	{
		m_reversal = reversal;
	}	//	setReversal
	
	/**
	 * 	Is Reversal
	 *	@return reversal
	 */
	public boolean isReversal()
	{
		return m_reversal;
	}	//	isReversal

	/** Returns a description parsing the bpartner defined in the Allocation form and then the allocation itself */
	public String getDescriptionForManualAllocation(int bpartnerID, String trxName)
	{
		String sysconfig_desc = MSysConfig.getValue(MSysConfig.ALLOCATION_DESCRIPTION, "@#AD_User_Name@", getAD_Client_ID());
		String description = "";
		if (sysconfig_desc.contains("@")) {
			description = Env.parseVariable(sysconfig_desc, new MBPartner(getCtx(), bpartnerID, null), trxName, true);
			description = Env.parseVariable(description, this, trxName, true);
			description = Msg.parseTranslation(getCtx(), description);
		} else
			description = Env.getContext(getCtx(), "#AD_User_Name"); // just to be sure

		return description;
	}
	
public void createMatchAllocation(){
	
	//Split Up ID and Amount Based On Amount, as ArrayList
	
	//Invoice
	ArrayList<Integer> plusInvoiceID = new ArrayList<Integer>();
	ArrayList<Integer> minInvoiceID = new ArrayList<Integer>();
	
	
	ArrayList<BigDecimal> plusAmount = new ArrayList<BigDecimal>();
	ArrayList<BigDecimal> minAmount = new ArrayList<BigDecimal>();
	
	ArrayList<BigDecimal> pDiscountAmt = new ArrayList<BigDecimal>();
	ArrayList<BigDecimal> nDiscountAmt = new ArrayList<BigDecimal>();
	
	ArrayList<BigDecimal> pWriteOffAmt = new ArrayList<BigDecimal>();
	ArrayList<BigDecimal> nWriteOffAmt = new ArrayList<BigDecimal>();
	//
	
	//Payment
	ArrayList<Integer> paymentID = new ArrayList<Integer>();
	ArrayList<Integer> receiptID = new ArrayList<Integer>();
	
	ArrayList<BigDecimal> paymentAmount = new ArrayList<BigDecimal>();
	ArrayList<BigDecimal> receiptAmount = new ArrayList<BigDecimal>();
	//
	
	//Charge
//	int chargeID = 0;
	ArrayList<Integer> chargeID = new ArrayList<Integer>();
	ArrayList<Integer> allocateChargeID = new ArrayList<Integer>();
	BigDecimal chargeAmount = Env.ZERO;
	//
	
	//ChargeDescription
	//Commented By David
	//ArrayList<String> chargeDescription = new ArrayList<String>();
	//
	
	int plusI = 0, minI = 0, pay = 0, rec = 0, charge = 0, count = 0, matched = 0;
			
	//Line loop to get record
	//1. If Line only have Invoice ID without Payment ID and Amount is +
	//2. If Line only have Invoice ID without Payment ID and Amount is -
	//3. If Line only have Payment ID without Invoice ID and Payment is AP Payment
	//4. If Line only have Payment ID without Invoice ID and Payment is AR Receipt
	//5. If Line is Charge
	//6. If Line have both Invoice ID and Payment ID and Payment is AP Payment
	//7. If Line have both Invoice ID and Payment ID and Payment is AR Receipt
	for(TCS_MAllocationLine line : getLines(true)){
		
		if(line.getC_Payment_ID()==0 && line.getC_Invoice_ID()>0 && line.getAmount().compareTo(Env.ZERO)>0){					//1
			plusInvoiceID.add(line.getC_Invoice_ID());
			plusAmount.add(line.getAmount());
			pDiscountAmt.add(line.getDiscountAmt());
			pWriteOffAmt.add(line.getWriteOffAmt());
			plusI++;
		}
		else if(line.getC_Payment_ID()==0 && line.getC_Invoice_ID()>0 && line.getAmount().compareTo(Env.ZERO)<0){				//2
			minInvoiceID.add(line.getC_Invoice_ID());
			minAmount.add(line.getAmount());
			nDiscountAmt.add(line.getDiscountAmt());
			nWriteOffAmt.add(line.getWriteOffAmt());
			minI++;
		}			
		else if(line.getC_Invoice_ID()==0 && line.getC_Payment_ID()>0 && line.getC_Payment().getC_DocType().getDocBaseType().equals("APP")){				//3
			paymentID.add(line.getC_Payment_ID());
			//negate because in allocationline, the payment amount is negated, so its negated here as well
			paymentAmount.add(line.getAmount().negate());
			pay++;
		}
		else if(line.getC_Invoice_ID()==0 && line.getC_Payment_ID()>0 && line.getC_Payment().getC_DocType().getDocBaseType().equals("ARR")){				//4
			receiptID.add(line.getC_Payment_ID());
			receiptAmount.add(line.getAmount());
			rec++;
		}
		else if(line.getC_Charge_ID()>0){																						//5
			//chargeID = line.getC_Charge_ID();
			chargeID.add(line.getC_Charge_ID());
			allocateChargeID.add(line.get_ValueAsInt("TCS_AllocateCharge_ID"));
			//Commented By David
			//matchDescription.add(line.get_ValueAsString(COLUMNNAME_Description));
			//chargeAmount = line.getAmount();
			chargeAmount = chargeAmount.add(line.getAmount());
			charge++;
		}else if(line.getAmount().compareTo(Env.ZERO)!=0){
			X_T_MatchAllocation match = new X_T_MatchAllocation(getCtx(), 0, get_TrxName());
			match.set_CustomColumn("C_AllocationHdr_ID", get_ID());
			
			//invoice
			match.set_CustomColumn("C_Invoice_ID", line.getC_Invoice_ID());
			match.set_CustomColumn("DiscountAmt", line.getDiscountAmt());
			match.set_CustomColumn("WriteOffAmt", line.getWriteOffAmt());
			
			MInvoice invoice = new MInvoice(getCtx(), line.getC_Invoice_ID(), get_TrxName());
			match.set_CustomColumn("C_DocType_ID", invoice.getC_DocType_ID());
			
			//payment
			match.set_CustomColumn("C_Payment_ID", line.getC_Payment_ID());
			
			MPayment payment = new MPayment(getCtx(), line.getC_Payment_ID(), get_TrxName());
			match.set_CustomColumn("Match_DocType_ID", payment.getC_DocType_ID());

			match.set_ValueOfColumn("DateAllocated", getCreated());
			match.set_CustomColumn("AllocationAmt", line.getAmount().abs());

//Commented By David
//Change Way To Set Description			
//			match.set_CustomColumn("Description", invoice.getDescription());
			match.set_ValueOfColumn("Description", getMatchAllocationDescription(match));
			match.saveEx();
			matched++;
			
			/*
			if(line.getC_Payment().getC_DocType().getDocBaseType().equals("APP")){												//6
				
				//match.set_CustomColumn("P_Payment_ID", line.getC_Payment_ID());
				//match.set_CustomColumn("P_Amount", line.getAmount().abs());
				//match.set_CustomColumn("N_Invoice_ID", line.getC_Invoice_ID());
				//match.set_CustomColumn("N_Amount", line.getAmount().abs().negate());
				match.set_CustomColumn("C_DiscountAmt", line.getDiscountAmt());
				match.set_CustomColumn("C_WriteOffAmt", line.getWriteOffAmt());
				
				//MPayment pPayment = new MPayment(getCtx(), line.getC_Payment_ID(), get_TrxName());
				//match.set_CustomColumn("P_DocType_ID", pPayment.getC_DocType_ID());
				
				//MInvoice nInvoice = new MInvoice(getCtx(), line.getC_Invoice_ID(), get_TrxName());
				//match.set_CustomColumn("N_DocType_ID", nInvoice.getC_DocType_ID());
				
			}else if(line.getC_Payment().getC_DocType().getDocBaseType().equals("ARR")){										//7
				
				//match.set_CustomColumn("N_Payment_ID", line.getC_Payment_ID());
				//match.set_CustomColumn("N_Amount", line.getAmount().abs().negate());
				//match.set_CustomColumn("P_Invoice_ID", line.getC_Invoice_ID());
				//match.set_CustomColumn("P_Amount", line.getAmount().abs());
				match.set_CustomColumn("P_DiscountAmt", line.getDiscountAmt());
				match.set_CustomColumn("P_WriteOffAmt", line.getWriteOffAmt());
				
				//MPayment nPayment = new MPayment(getCtx(), line.getC_Payment_ID(), get_TrxName());
				//match.set_CustomColumn("N_DocType_ID", nPayment.getC_DocType_ID());
									
				//MInvoice pInvoice = new MInvoice(getCtx(), line.getC_Invoice_ID(), get_TrxName());
				//match.set_CustomColumn("P_DocType_ID", pInvoice.getC_DocType_ID());
				
			}
			*/
		}
		count++;
	}
	
	int total = plusI+minI+pay+rec+charge;
	count -= matched;
	
	BigDecimal tempPaymentAmt = Env.ZERO;
	BigDecimal tempInvoiceAmt = Env.ZERO;
	
	//Loop for pInvoice
	for(int i = 0;i<plusI;i++){
		tempInvoiceAmt = plusAmount.get(i);
		
		if(!plusInvoiceID.isEmpty() && !minInvoiceID.isEmpty()){
							
			for(int j = 0;j<minInvoiceID.size();j++){
				if(j==0 && minAmount.get(j).compareTo(Env.ZERO)<0){
					//match
					X_T_MatchAllocation match = new X_T_MatchAllocation(getCtx(), 0, get_TrxName());
					match.set_CustomColumn("C_AllocationHdr_ID", get_ID());
					
					//invoice
					match.set_CustomColumn("C_Invoice_ID", plusInvoiceID.get(i));
					match.set_CustomColumn("DiscountAmt", pDiscountAmt.get(i));
					match.set_CustomColumn("WriteOffAmt", pWriteOffAmt.get(i));
					
					MInvoice pInvoice = new MInvoice(getCtx(), plusInvoiceID.get(i), get_TrxName());
					match.set_CustomColumn("C_DocType_ID", pInvoice.getC_DocType_ID());
					
					//match.set_CustomColumn("P_Invoice_ID", plusInvoiceID.get(i));
					//match.set_CustomColumn("P_Amount", plusAmount.get(i));
					//match.set_CustomColumn("N_Invoice_ID", minInvoiceID.get(j));
					//match.set_CustomColumn("N_Amount", minAmount.get(j));
					
					//invoice1
					match.set_CustomColumn("Match_Invoice_ID", minInvoiceID.get(j));
					match.set_CustomColumn("Match_DiscountAmt", nDiscountAmt.get(j));
					match.set_CustomColumn("Match_WriteOffAmt", nWriteOffAmt.get(j));
					
					MInvoice nInvoice = new MInvoice(getCtx(), minInvoiceID.get(j), get_TrxName());
					match.set_CustomColumn("Match_DocType_ID", nInvoice.getC_DocType_ID());
					
					if(tempInvoiceAmt.compareTo(minAmount.get(j).abs())>0)
						match.set_CustomColumn("AllocationAmt", minAmount.get(j).abs());	
					else
						match.set_CustomColumn("AllocationAmt", tempInvoiceAmt);
					
					match.set_ValueOfColumn("DateAllocated", getCreated());
					match.set_ValueOfColumn("Description", getMatchAllocationDescription(match));
					match.saveEx();
					
					//match1
					X_T_MatchAllocation match1 = new X_T_MatchAllocation(getCtx(), 0, get_TrxName());
					match1.set_CustomColumn("C_AllocationHdr_ID", get_ID());
					
					//invoice
					match1.set_CustomColumn("C_Invoice_ID", minInvoiceID.get(j));
					match1.set_CustomColumn("C_DocType_ID", nInvoice.getC_DocType_ID());
					match1.set_CustomColumn("DiscountAmt", nDiscountAmt.get(j));
					match1.set_CustomColumn("WriteOffAmt", nWriteOffAmt.get(j));
					
					//invoice1
					match1.set_CustomColumn("Match_Invoice_ID", plusInvoiceID.get(i));
					match1.set_CustomColumn("Match_DocType_ID", pInvoice.getC_DocType_ID());
					match1.set_CustomColumn("Match_DiscountAmt", pDiscountAmt.get(i));
					match1.set_CustomColumn("Match_WriteOffAmt", pWriteOffAmt.get(i));
					
					//match1.set_CustomColumn("P_Invoice_ID", plusInvoiceID.get(i));
					//match1.set_CustomColumn("P_Amount", plusAmount.get(i));
					//match1.set_CustomColumn("N_Invoice_ID", minInvoiceID.get(j));
					//match1.set_CustomColumn("N_Amount", minAmount.get(j));
					
					if(tempInvoiceAmt.compareTo(minAmount.get(j).abs())>0)
						match1.set_CustomColumn("AllocationAmt", minAmount.get(j).abs());	
					else
						match1.set_CustomColumn("AllocationAmt", tempInvoiceAmt);
					
					
					
					match1.set_ValueOfColumn("DateAllocated", getCreated());
					match1.set_ValueOfColumn("Description", getMatchAllocationDescription(match1));
					match1.saveEx();
					//
					pDiscountAmt.set(i, Env.ZERO);
					pWriteOffAmt.set(i, Env.ZERO);
					nDiscountAmt.set(j, Env.ZERO);
					nWriteOffAmt.set(j, Env.ZERO);
					
				}
				
				
				if(j!=0 && tempInvoiceAmt.compareTo(Env.ZERO)>0 
						&& minAmount.get(j).compareTo(Env.ZERO)<0){
					//match
					X_T_MatchAllocation match = new X_T_MatchAllocation(getCtx(), 0, get_TrxName());
					match.set_CustomColumn("C_AllocationHdr_ID", get_ID());
					
					//invoice
					match.set_CustomColumn("C_Invoice_ID", plusInvoiceID.get(i));
					match.set_CustomColumn("DiscountAmt", pDiscountAmt.get(i));
					match.set_CustomColumn("WriteOffAmt", pWriteOffAmt.get(i));
					
					MInvoice pInvoice = new MInvoice(getCtx(), plusInvoiceID.get(i), get_TrxName());
					match.set_CustomColumn("C_DocType_ID", pInvoice.getC_DocType_ID());
					
					//invoice1
					match.set_CustomColumn("Match_Invoice_ID", minInvoiceID.get(j));
					match.set_CustomColumn("Match_DiscountAmt", nDiscountAmt.get(j));
					match.set_CustomColumn("Match_WriteOffAmt", nWriteOffAmt.get(j));
					
					MInvoice nInvoice = new MInvoice(getCtx(), minInvoiceID.get(j), get_TrxName());
					match.set_CustomColumn("Match_DocType_ID", nInvoice.getC_DocType_ID());
					
					//match.set_CustomColumn("P_Invoice_ID", plusInvoiceID.get(i));
					//match.set_CustomColumn("P_Amount", plusAmount.get(i));
					//match.set_CustomColumn("N_Invoice_ID", minInvoiceID.get(j));
					//match.set_CustomColumn("N_Amount", minAmount.get(j));
					
					if(tempInvoiceAmt.compareTo(minAmount.get(j).abs())>0)
						match.set_CustomColumn("AllocationAmt", minAmount.get(j).abs());	
					else
						match.set_CustomColumn("AllocationAmt", tempInvoiceAmt);
					
					match.set_ValueOfColumn("DateAllocated", getCreated());
					match.set_ValueOfColumn("Description", getMatchAllocationDescription(match));
					match.saveEx();
					
					//match1				
					X_T_MatchAllocation match1 = new X_T_MatchAllocation(getCtx(), 0, get_TrxName());
					match1.set_CustomColumn("C_AllocationHdr_ID", get_ID());
					
					
					//invoice
					match1.set_CustomColumn("C_Invoice_ID", minInvoiceID.get(j));
					match1.set_CustomColumn("DiscountAmt", nDiscountAmt.get(j));
					match1.set_CustomColumn("WriteOffAmt", nWriteOffAmt.get(j));
					match1.set_CustomColumn("C_DocType_ID", nInvoice.getC_DocType_ID());
					
					//invoice1
					match1.set_CustomColumn("Match_Invoice_ID", plusInvoiceID.get(i));
					match1.set_CustomColumn("Match_DocType_ID", pInvoice.getC_DocType_ID());
					match1.set_CustomColumn("Match_DiscountAmt", pDiscountAmt.get(i));
					match1.set_CustomColumn("Match_WriteOffAmt", pWriteOffAmt.get(i));
					
					//match1.set_CustomColumn("P_Invoice_ID", plusInvoiceID.get(i));
					//match1.set_CustomColumn("P_Amount", plusAmount.get(i));
					//match1.set_CustomColumn("N_Invoice_ID", minInvoiceID.get(j));
					//match1.set_CustomColumn("N_Amount", minAmount.get(j));
					
					//MInvoice pInvoice = new MInvoice(getCtx(), plusInvoiceID.get(i), get_TrxName());
					//match1.set_CustomColumn("N_DocType_ID", pInvoice.getC_DocType_ID());
					
					//MInvoice nInvoice = new MInvoice(getCtx(), minInvoiceID.get(j), get_TrxName());
					//match1.set_CustomColumn("P_DocType_ID", nInvoice.getC_DocType_ID());
					
					if(tempInvoiceAmt.compareTo(minAmount.get(j).abs())>0)
						match1.set_CustomColumn("AllocationAmt", minAmount.get(j).abs());	
					else
						match1.set_CustomColumn("AllocationAmt", tempInvoiceAmt);
					
					match1.set_ValueOfColumn("DateAllocated", getCreated());
					match1.set_ValueOfColumn("Description", getMatchAllocationDescription(match1));
					match1.saveEx();
					//
					pDiscountAmt.set(i, Env.ZERO);
					pWriteOffAmt.set(i, Env.ZERO);
					nDiscountAmt.set(j, Env.ZERO);
					nWriteOffAmt.set(j, Env.ZERO);
												
				}
				
				tempInvoiceAmt = tempInvoiceAmt.add(minAmount.get(j));
				
				if(tempInvoiceAmt.compareTo(Env.ZERO)>0){
					minAmount.set(j, Env.ZERO);
				}else if(tempInvoiceAmt.compareTo(Env.ZERO)<0){
					plusAmount.set(i, Env.ZERO);
					minAmount.set(j, tempInvoiceAmt);
					break;
				}else if(tempInvoiceAmt.compareTo(Env.ZERO)==0){
					plusAmount.set(i, Env.ZERO);
					minAmount.set(j, Env.ZERO);
					break;
				}
			}
			
		}
		
		BigDecimal totalMinAmount = Env.ZERO;
		for(int k=0; k<minInvoiceID.size(); k++)
		{
			totalMinAmount = totalMinAmount.add(minAmount.get(k));
		}
		if(!chargeID.isEmpty()){
			if(totalMinAmount.compareTo(Env.ZERO)==0 && chargeID.get(0)>0 && tempInvoiceAmt.compareTo(Env.ZERO)>0 && chargeAmount.compareTo(Env.ZERO)>0){					
				X_T_MatchAllocation match = new X_T_MatchAllocation(getCtx(), 0, get_TrxName());
				match.set_CustomColumn("C_AllocationHdr_ID", get_ID());
				
				match.set_CustomColumn("C_Invoice_ID", plusInvoiceID.get(i));
				match.set_CustomColumn("DiscountAmt", pDiscountAmt.get(i));
				match.set_CustomColumn("WriteOffAmt", pWriteOffAmt.get(i));
				
				MInvoice pInvoice = new MInvoice(getCtx(), plusInvoiceID.get(i), get_TrxName());
				match.set_CustomColumn("C_DocType_ID", pInvoice.getC_DocType_ID());
				
				//match.set_CustomColumn("P_Invoice_ID", plusInvoiceID.get(i));
				//match.set_CustomColumn("P_Amount", tempInvoiceAmt);
				match.set_CustomColumn("C_Charge_ID", chargeID.get(0));
				//match.set_CustomColumn("N_Amount", tempInvoiceAmt.abs().negate());
				
				match.set_CustomColumn("AllocationAmt", tempInvoiceAmt);
				match.set_ValueOfColumn("DateAllocated", getCreated());
				match.set_ValueOfColumn("Description", getMatchAllocationDescription(match));
				match.saveEx();
				
				chargeAmount = chargeAmount.add(tempInvoiceAmt);
				
				pDiscountAmt.set(i, Env.ZERO);
				pWriteOffAmt.set(i, Env.ZERO);
				
			} else if(!plusInvoiceID.isEmpty() && minInvoiceID.isEmpty() && chargeID.get(i)>0){
				X_T_MatchAllocation match = new X_T_MatchAllocation(getCtx(), 0, get_TrxName());
				match.set_CustomColumn("C_AllocationHdr_ID", get_ID());
				match.set_CustomColumn("C_Invoice_ID", plusInvoiceID.get(i));
				match.set_CustomColumn("DiscountAmt", pDiscountAmt.get(i));
				match.set_CustomColumn("WriteOffAmt", pWriteOffAmt.get(i));
				
				MInvoice pInvoice = new MInvoice(getCtx(), plusInvoiceID.get(i), get_TrxName());
				match.set_CustomColumn("C_DocType_ID", pInvoice.getC_DocType_ID());
				
				//match.set_CustomColumn("P_Invoice_ID", plusInvoiceID.get(i));
				//match.set_CustomColumn("P_Amount", tempInvoiceAmt);
				match.set_CustomColumn("C_Charge_ID", chargeID.get(0));
				//match.set_CustomColumn("N_Amount", tempInvoiceAmt.abs().negate());
				
				//MInvoice pInvoice = new MInvoice(getCtx(), plusInvoiceID.get(i), get_TrxName());
				//match.set_CustomColumn("P_DocType_ID", pInvoice.getC_DocType_ID());
				
				match.set_CustomColumn("AllocationAmt", chargeAmount);
				match.set_ValueOfColumn("DateAllocated", getCreated());
				match.set_ValueOfColumn("Description", getMatchAllocationDescription(match));
				match.saveEx();
				
				chargeAmount = chargeAmount.add(tempInvoiceAmt);
				
				pDiscountAmt.set(i, Env.ZERO);
				pWriteOffAmt.set(i, Env.ZERO);
			}	
		} /*else if(plusInvoiceID.isEmpty() && !minInvoiceID.isEmpty() && chargeID>0){
			X_T_MatchAllocation match = new X_T_MatchAllocation(getCtx(), 0, get_TrxName());
			match.set_CustomColumn("C_AllocationHdr_ID", get_ID());
			match.set_CustomColumn("N_Invoice_ID", minInvoiceID.get(0));
			match.set_CustomColumn("N_Amount", minAmount.get(0));
			match.set_CustomColumn("C_Charge_ID", chargeID);
			match.set_CustomColumn("P_Amount", chargeAmount);
			
			MInvoice nInvoice = new MInvoice(getCtx(), minInvoiceID.get(0), get_TrxName());
			match.set_CustomColumn("N_DocType_ID", nInvoice.getC_DocType_ID());
			
			match.set_CustomColumn("AllocationAmt", chargeAmount.abs());
			
			match.set_CustomColumn("N_DiscountAmt", nDiscountAmt.get(0));
			match.set_CustomColumn("N_WriteOffAmt", nWriteOffAmt.get(0));
			
			nDiscountAmt.set(0, Env.ZERO);
			nWriteOffAmt.set(0, Env.ZERO);
			
			match.saveEx();
		}*/
	}
	//End pInvoice Loop
	
	//Loop for Payment
	for(int i = 0;i<pay;i++){
		tempPaymentAmt = paymentAmount.get(i);
		
		if(!paymentID.isEmpty() && !receiptID.isEmpty()){
			
			for(int j = 0;j<receiptID.size();j++){
				
				//if(j==0 && receiptAmount.get(j).compareTo(Env.ZERO)<0){
				if(j==0 && receiptAmount.get(j).compareTo(Env.ZERO)>0){
					//match
					X_T_MatchAllocation match = new X_T_MatchAllocation(getCtx(), 0, get_TrxName());
					match.set_CustomColumn("C_AllocationHdr_ID", get_ID());
					
					//payment
					match.set_CustomColumn("C_Payment_ID", paymentID.get(i));
					
					MPayment pPayment = new MPayment(getCtx(), paymentID.get(i), get_TrxName());
					match.set_CustomColumn("C_DocType_ID", pPayment.getC_DocType_ID());
					
					//receipt
					match.set_CustomColumn("Match_Payment_ID", receiptID.get(j));
					
					MPayment nPayment = new MPayment(getCtx(), receiptID.get(j), get_TrxName());
					match.set_CustomColumn("Match_DocType_ID", nPayment.getC_DocType_ID());
					//match.set_CustomColumn("P_Payment_ID", paymentID.get(i));
					//match.set_CustomColumn("P_Amount", paymentAmount.get(i));
					//match.set_CustomColumn("N_Payment_ID", receiptID.get(j));
					//match.set_CustomColumn("N_Amount", receiptAmount.get(j));
					
					if(tempPaymentAmt.compareTo(receiptAmount.get(j).abs())>0)
						match.set_CustomColumn("AllocationAmt", receiptAmount.get(j).abs());	
					else
						match.set_CustomColumn("AllocationAmt", tempPaymentAmt);
												
					match.set_ValueOfColumn("DateAllocated", getCreated());	
					match.set_ValueOfColumn("Description", getMatchAllocationDescription(match));
					match.saveEx();
					
					//match1
					X_T_MatchAllocation match1 = new X_T_MatchAllocation(getCtx(), 0, get_TrxName());
					match1.set_CustomColumn("C_AllocationHdr_ID", get_ID());
					
					//receipt
					match1.set_CustomColumn("C_Payment_ID", receiptID.get(j));
					match1.set_CustomColumn("C_DocType_ID", nPayment.getC_DocType_ID());
					
					//payment
					match1.set_CustomColumn("Match_Payment_ID", paymentID.get(i));
					match1.set_CustomColumn("Match_DocType_ID", pPayment.getC_DocType_ID());
					
					//match.set_CustomColumn("P_Payment_ID", paymentID.get(i));
					//match.set_CustomColumn("P_Amount", paymentAmount.get(i));
					//match.set_CustomColumn("N_Payment_ID", receiptID.get(j));
					//match.set_CustomColumn("N_Amount", receiptAmount.get(j));
					
					if(tempPaymentAmt.compareTo(receiptAmount.get(j).abs())>0)
						match1.set_CustomColumn("AllocationAmt", receiptAmount.get(j).abs());	
					else
						match1.set_CustomColumn("AllocationAmt", tempPaymentAmt);
												
					match1.set_ValueOfColumn("DateAllocated", getCreated());	
					match1.set_ValueOfColumn("Description", getMatchAllocationDescription(match1));
					match1.saveEx();
				}
				
				
				//if(j!=0 && tempPaymentAmt.compareTo(Env.ZERO)>0 
				//		&& receiptAmount.get(j).compareTo(Env.ZERO)<0){
				if(j!=0 && tempPaymentAmt.compareTo(Env.ZERO)<0 
						&& receiptAmount.get(j).compareTo(Env.ZERO)>0){
										//match
					X_T_MatchAllocation match = new X_T_MatchAllocation(getCtx(), 0, get_TrxName());
					match.set_CustomColumn("C_AllocationHdr_ID", get_ID());
					
					//payment
					match.set_CustomColumn("C_Payment_ID", paymentID.get(i));
					
					MPayment pPayment = new MPayment(getCtx(), paymentID.get(i), get_TrxName());
					match.set_CustomColumn("C_DocType_ID", pPayment.getC_DocType_ID());
					
					//receipt
					match.set_CustomColumn("Match_Payment_ID", receiptID.get(j));
					
					MPayment nPayment = new MPayment(getCtx(), receiptID.get(j), get_TrxName());
					match.set_CustomColumn("Match_DocType_ID", nPayment.getC_DocType_ID());
					//match.set_CustomColumn("P_Payment_ID", paymentID.get(i));
					//match.set_CustomColumn("P_Amount", tempPaymentAmt);
					//match.set_CustomColumn("N_Payment_ID", receiptID.get(j));
					//match.set_CustomColumn("N_Amount", receiptAmount.get(j));
					
					if(tempPaymentAmt.compareTo(receiptAmount.get(j).abs())>0)
						match.set_CustomColumn("AllocationAmt", receiptAmount.get(j).abs());	
					else
						match.set_CustomColumn("AllocationAmt", tempPaymentAmt);
												
					match.set_ValueOfColumn("DateAllocated", getCreated());							
					match.saveEx();
					
					//match1
					X_T_MatchAllocation match1 = new X_T_MatchAllocation(getCtx(), 0, get_TrxName());
					match1.set_CustomColumn("C_AllocationHdr_ID", get_ID());
					
					//receipt
					match1.set_CustomColumn("C_Payment_ID", receiptID.get(j));
					match1.set_CustomColumn("C_DocType_ID", nPayment.getC_DocType_ID());
					
					//payment
					match1.set_CustomColumn("Match_Payment_ID", paymentID.get(i));
					match1.set_CustomColumn("Match_DocType_ID", pPayment.getC_DocType_ID());
					
					//match.set_CustomColumn("P_Payment_ID", paymentID.get(i));
					//match.set_CustomColumn("P_Amount", tempPaymentAmt);
					//match.set_CustomColumn("N_Payment_ID", receiptID.get(j));
					//match.set_CustomColumn("N_Amount", receiptAmount.get(j));
						
					if(tempPaymentAmt.compareTo(receiptAmount.get(j).abs())>0)
						match1.set_CustomColumn("AllocationAmt", receiptAmount.get(j).abs());	
					else
						match1.set_CustomColumn("AllocationAmt", tempPaymentAmt);
												
					match1.set_ValueOfColumn("DateAllocated", getCreated());
					match1.set_ValueOfColumn("Description", getMatchAllocationDescription(match1));
					match1.saveEx();
				}
				
				tempPaymentAmt = tempPaymentAmt.add(receiptAmount.get(j));
				
				if(tempPaymentAmt.compareTo(Env.ZERO)>0){
					receiptAmount.set(j, Env.ZERO);
				}else if(tempPaymentAmt.compareTo(Env.ZERO)<0){
					paymentAmount.set(i, Env.ZERO);
					receiptAmount.set(j, tempPaymentAmt);
					break;
				}else if(tempPaymentAmt.compareTo(Env.ZERO)==0){
					paymentAmount.set(i, Env.ZERO);
					receiptAmount.set(j, Env.ZERO);
					break;
				}
			}
		}
		
		BigDecimal totalReceiptAmount = Env.ZERO;
		for(int k=0; k<minInvoiceID.size(); k++)
		{
			totalReceiptAmount = totalReceiptAmount.add(minAmount.get(k));
		}
		if(!chargeID.isEmpty()){
			if(totalReceiptAmount.compareTo(Env.ZERO)==0 && chargeID.get(i)>0 && tempPaymentAmt.compareTo(Env.ZERO)>0 && chargeAmount.compareTo(Env.ZERO)>0){					
				X_T_MatchAllocation match = new X_T_MatchAllocation(getCtx(), 0, get_TrxName());
				match.set_CustomColumn("C_AllocationHdr_ID", get_ID());
				
				//payment
				match.set_CustomColumn("C_Payment_ID", paymentID.get(i));
				
				MPayment pPayment = new MPayment(getCtx(), paymentID.get(i), get_TrxName());
				match.set_CustomColumn("C_DocType_ID", pPayment.getC_DocType_ID());
				//match.set_CustomColumn("P_Payment_ID", paymentID.get(i));
				//match.set_CustomColumn("P_Amount", tempPaymentAmt);
				
				//charge
				match.set_CustomColumn("C_Charge_ID", chargeID.get(i));
				//Commented By David
				//match.set_CustomColumn("Description", matchDescription.get(i));
				//match.set_CustomColumn("N_Amount", tempPaymentAmt.abs().negate());
				
				match.set_CustomColumn("AllocationAmt", tempPaymentAmt);
				match.set_ValueOfColumn("DateAllocated", getCreated());	
				
				if (allocateChargeID.get(i)>0) {
					MTCS_AllocateCharge allocateCharge = new MTCS_AllocateCharge(getCtx(), allocateChargeID.get(i), get_TrxName());
					match.set_ValueOfColumn("Description", allocateCharge.getDescription());								
				}
				else
					match.set_ValueOfColumn("Description", getMatchAllocationDescription(match));
				
				match.saveEx();
				
				chargeAmount = chargeAmount.abs().subtract(tempPaymentAmt);
									
			} else if(!paymentID.isEmpty() && receiptID.isEmpty() && chargeID.get(i)>0){
				X_T_MatchAllocation match = new X_T_MatchAllocation(getCtx(), 0, get_TrxName());
				match.set_CustomColumn("C_AllocationHdr_ID", get_ID());
				
				//payment
				match.set_CustomColumn("C_Payment_ID", paymentID.get(i));
				
				MPayment pPayment = new MPayment(getCtx(), paymentID.get(i), get_TrxName());
				match.set_CustomColumn("C_DocType_ID", pPayment.getC_DocType_ID());
				//match.set_CustomColumn("P_Payment_ID", paymentID.get(i));
				//match.set_CustomColumn("P_Amount", tempPaymentAmt);
				
				//charge
				match.set_CustomColumn("C_Charge_ID", chargeID.get(i));
				//match.set_CustomColumn("N_Amount", tempPaymentAmt.abs().negate());
				
				match.set_CustomColumn("AllocationAmt", tempPaymentAmt);
				match.set_ValueOfColumn("DateAllocated", getCreated());
				
				if (allocateChargeID.get(i)>0) {
					MTCS_AllocateCharge allocateCharge = new MTCS_AllocateCharge(getCtx(), allocateChargeID.get(i), get_TrxName());
					match.set_ValueOfColumn("Description", allocateCharge.getDescription());								
				}
				else
					match.set_ValueOfColumn("Description", getMatchAllocationDescription(match));
				
				match.saveEx();
				
				chargeAmount = chargeAmount.abs().subtract(tempPaymentAmt);
			}	
		}/* else if(paymentID.isEmpty() && !receiptID.isEmpty() && chargeID>0){
			X_T_MatchAllocation match = new X_T_MatchAllocation(getCtx(), 0, get_TrxName());
			match.set_CustomColumn("C_AllocationHdr_ID", get_ID());
			match.set_CustomColumn("N_Payment_ID", receiptID.get(0));
			match.set_CustomColumn("N_Amount", receiptAmount.get(0));
			match.set_CustomColumn("C_Charge_ID", chargeID);
			match.set_CustomColumn("P_Amount", chargeAmount);
			
			MPayment nPayment = new MPayment(getCtx(), receiptID.get(0), get_TrxName());
			match.set_CustomColumn("N_DocType_ID", nPayment.getC_DocType_ID());
			
			match.set_CustomColumn("AllocationAmt", chargeAmount.abs());
			
			match.saveEx();	
		}*/
		
	}
	//End Payment Loop
		
	//Loop for nInvoice
	for(int i=0; i<minI; i++)
	{
		BigDecimal tempnInvoiceAmt = minAmount.get(i);
		if(!chargeID.isEmpty()){
			if(tempnInvoiceAmt.compareTo(Env.ZERO)<0 && chargeID.get(0)>0 && chargeAmount.compareTo(Env.ZERO)<0){
				X_T_MatchAllocation match = new X_T_MatchAllocation(getCtx(), 0, get_TrxName());
				match.set_CustomColumn("C_AllocationHdr_ID", get_ID());
				
				//invoice
				match.set_CustomColumn("C_Invoice_ID", minInvoiceID.get(i));
				match.set_CustomColumn("DiscountAmt", nDiscountAmt.get(i));
				match.set_CustomColumn("WriteOffAmt", nWriteOffAmt.get(i));
				
				MInvoice nInvoice = new MInvoice(getCtx(), minInvoiceID.get(i), get_TrxName());
				match.set_CustomColumn("C_DocType_ID", nInvoice.getC_DocType_ID());
				
				//match.set_CustomColumn("N_Invoice_ID", minInvoiceID.get(i));
				//match.set_CustomColumn("N_Amount", tempnInvoiceAmt.abs().negate());
				
				//charge
				match.set_CustomColumn("C_Charge_ID", chargeID.get(0));
				//match.set_CustomColumn("P_Amount", tempnInvoiceAmt.abs());
				
				match.set_CustomColumn("AllocationAmt", tempnInvoiceAmt);
				match.set_ValueOfColumn("DateAllocated", getCreated());
				match.set_ValueOfColumn("Description", getMatchAllocationDescription(match));
				match.saveEx();
				
				chargeAmount = chargeAmount.add(tempnInvoiceAmt);
				
				nDiscountAmt.set(0, Env.ZERO);
				nWriteOffAmt.set(0, Env.ZERO);
			}
		}
	}
	//End nInvoice Loop	
	
	//Loop for Receipt
	for(int i=0; i<rec; i++)
	{
		BigDecimal tempReceiptAmt = receiptAmount.get(i);
		
		if(!chargeID.isEmpty()){
			if(tempReceiptAmt.compareTo(Env.ZERO)!=0 && chargeID.get(i)>0 && chargeAmount.compareTo(Env.ZERO)<0){
				X_T_MatchAllocation match = new X_T_MatchAllocation(getCtx(), 0, get_TrxName());
				match.set_CustomColumn("C_AllocationHdr_ID", get_ID());
				
				//receipt
				match.set_CustomColumn("C_Payment_ID", receiptID.get(i));
				
				MPayment nPayment = new MPayment(getCtx(), receiptID.get(i), get_TrxName());
				match.set_CustomColumn("C_DocType_ID", nPayment.getC_DocType_ID());
				//match.set_CustomColumn("N_Payment_ID", receiptID.get(i));
				//match.set_CustomColumn("N_Amount", tempReceiptAmt.abs().negate());
				
				//charge
				match.set_CustomColumn("C_Charge_ID", chargeID.get(i));
				//match.set_CustomColumn("P_Amount", tempReceiptAmt.abs());
				
				
				match.set_CustomColumn("AllocationAmt", tempReceiptAmt);
				match.set_ValueOfColumn("DateAllocated", getCreated());
				if (allocateChargeID.get(i)>0) {					
					MTCS_AllocateCharge allocateCharge = new MTCS_AllocateCharge(getCtx(), allocateChargeID.get(i), get_TrxName());
					match.set_ValueOfColumn("Description", allocateCharge.getDescription());
				}
				else
					match.set_ValueOfColumn("Description", getMatchAllocationDescription(match));
				
				match.saveEx();	
				
				chargeAmount = chargeAmount.abs().negate().add(tempPaymentAmt.abs());
			}
		}
	}
	//End Receipt Loop
	
	plusInvoiceID.clear();
	minInvoiceID.clear();
	paymentID.clear();
	receiptID.clear();
	plusAmount.clear();
	minAmount.clear();
	paymentAmount.clear();
	receiptAmount.clear();
	pDiscountAmt.clear();
	nDiscountAmt.clear();
	pWriteOffAmt.clear();
	nWriteOffAmt.clear();
}

	private String getMatchAllocationDescription(X_T_MatchAllocation match){
		
		/* Case:
		 * 1. Payment Allocate Charge, Get Description From TCS_AllocateCharge
		 * 2. Invoice Allocate Charge, Get Description From C_AllocationHdr
		 * 3. Payment Allocate Invoice, Get Description From C_Invoice
		 * 4. Invoice Allocate Invoice, Get Description From Match_Invoice
		 * 5. Payment Allocate Payment, Get Description From C_Payment
		 */
		
		int C_Invoice_ID=match.getC_Invoice_ID();
		int C_Payment_ID=match.getC_Payment_ID();
		int Match_Invoice_ID=match.getMatch_Invoice_ID();
		int Match_Payment_ID=match.getMatch_Payment_ID();
		int C_Charge_ID=match.getC_Charge_ID();
		
		//Case 1
		if (C_Payment_ID>0 && C_Charge_ID>0) {
			String sql="C_Payment_ID="+C_Payment_ID+" AND C_Charge_ID="+C_Charge_ID+" AND AD_Client_ID="+Env.getAD_Client_ID(getCtx());
			int allocateCharge_ID = new Query(getCtx(), X_TCS_AllocateCharge.Table_Name, sql, get_TrxName()).firstId();
			if (allocateCharge_ID>0) {
				X_TCS_AllocateCharge allocateCharge = new X_TCS_AllocateCharge(getCtx(), allocateCharge_ID, get_TrxName());
				return allocateCharge.getDescription();
			}
			else{
				MPayment pay = new MPayment(getCtx(), C_Payment_ID, get_TrxName());
				return pay.getDescription();
			}
		}

		//Case 2
		else if (C_Invoice_ID>0 && C_Charge_ID>0) {
			
			int C_AllocationHdr_ID=match.getC_AllocationHdr_ID();
			if (C_AllocationHdr_ID>0) {
				MAllocationHdr allocHdr = new MAllocationHdr(getCtx(), C_AllocationHdr_ID, get_TrxName());
				return allocHdr.getDescription();
			}
		}

		//Case 3
		else if (C_Payment_ID>0 && C_Invoice_ID>0) {
			
			MInvoice inv = new MInvoice(getCtx(), C_Invoice_ID, get_TrxName());
			return inv.getDescription();
		}

		//Case 4
		else if (C_Invoice_ID>0 && Match_Invoice_ID>0) {
			
			MInvoice inv = new MInvoice(getCtx(), Match_Invoice_ID, get_TrxName());
			return inv.getDescription();
		}

		//Case 5
		else if (C_Payment_ID>0 && Match_Payment_ID>0) {
			
			MPayment pay = new MPayment(getCtx(), C_Payment_ID, get_TrxName());
			return pay.getDescription();
		}
		
		return "";
			
	}
}   //  MAllocation
