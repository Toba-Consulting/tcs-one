package org.compiere.model;

import id.tcs.model.MTCS_AllocateCharge;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;

import org.adempiere.exceptions.AdempiereException;
import org.adempiere.util.PaymentUtil;
import org.compiere.model.MAllocationHdr;
import org.compiere.model.MAllocationLine;
import org.compiere.model.MBPartner;
import org.compiere.model.MBankAccount;
import org.compiere.model.MCash;
import org.compiere.model.MCashLine;
import org.compiere.model.MClient;
import org.compiere.model.MConversionRate;
import org.compiere.model.MConversionRateUtil;
import org.compiere.model.MDocType;
import org.compiere.model.MDocTypeCounter;
import org.compiere.model.MInvoice;
import org.compiere.model.MOrder;
import org.compiere.model.MOrg;
import org.compiere.model.MPayment;
import org.compiere.model.MPaymentAllocate;
import org.compiere.model.MPeriod;
import org.compiere.model.MSysConfig;
import org.compiere.model.ModelValidationEngine;
import org.compiere.model.ModelValidator;
import org.compiere.model.Obscure;
import org.compiere.model.Query;
import org.compiere.model.X_C_CashLine;
import org.compiere.process.DocAction;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.IBAN;
import org.compiere.util.Msg;
import org.compiere.util.TimeUtil;
import org.compiere.util.Util;

public class TCS_MPayment extends MPayment {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4010769181573799098L;

	public TCS_MPayment(Properties ctx, int C_Payment_ID, String trxName) {
		super(ctx, C_Payment_ID, trxName);
	}

	public TCS_MPayment(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}
	
	/**	Process Message 			*/
	private String		m_processMsg = null;
	/**	Just Prepared Flag			*/
	private boolean		m_justPrepared = false;
	
	/**************************************************************************
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

		// Set the definite document number after completed (if needed)
		setDefiniteDocumentNo();

		m_processMsg = ModelValidationEngine.get().fireDocValidate(this, ModelValidator.TIMING_BEFORE_COMPLETE);
		if (m_processMsg != null)
			return DocAction.STATUS_Invalid;

		//	Implicit Approval
		if (!isApproved())
			approveIt();
		if (log.isLoggable(Level.INFO)) log.info(toString());

		//	Charge Handling
		boolean createdAllocationRecords = false;
		if (getC_Charge_ID() != 0)
		{
			//@PhieAlbert
			MAllocationHdr alloc = new MAllocationHdr(getCtx(), false, 
					getDateTrx(), getC_Currency_ID(), 
					Msg.translate(getCtx(), "C_Payment_ID")	+ ": " + getDocumentNo(), 
					get_TrxName());
			alloc.setAD_Org_ID(getAD_Org_ID());
			alloc.setDateAcct(getDateAcct()); // in case date acct is different from datetrx in payment; IDEMPIERE-1532 tbayen
			if (!alloc.save())
			{
				log.severe("P.Allocations not created");
				return DocAction.STATUS_Invalid;
			}
			
			BigDecimal allocateAmount = isReceipt() 
					? getPayAmt() 
					: getPayAmt().negate();
			
			MAllocationLine alloclineCr = new MAllocationLine(alloc);
			alloclineCr.setAD_Org_ID(getAD_Org_ID());
			alloclineCr.setC_BPartner_ID(getC_BPartner_ID());
			alloclineCr.setC_Payment_ID(getC_Payment_ID());
			alloclineCr.setDateTrx(alloc.getDateTrx());
			alloclineCr.setAmount(allocateAmount);
			alloclineCr.saveEx();
			
			MAllocationLine alloclineDr = new MAllocationLine(alloc);
			alloclineDr.setAD_Org_ID(getAD_Org_ID());
			alloclineDr.setC_BPartner_ID(getC_BPartner_ID());
			alloclineDr.setDateTrx(alloc.getDateTrx());
			alloclineDr.setAmount(allocateAmount.negate());
			alloclineDr.setC_Charge_ID(getC_Charge_ID());
			alloclineDr.saveEx();
			//end @PhieAlbert
			
			// added AdempiereException by zuhri
			if (!alloc.processIt(DocAction.ACTION_Complete))
				throw new AdempiereException("Failed when processing document - " + alloc.getProcessMsg());
			
			setIsAllocated(true);
		}
		else
		{
			createdAllocationRecords = allocateIt();	//	Create Allocation Records
			testAllocation();
		}

		//	Project update
		if (getC_Project_ID() != 0)
		{
		//	MProject project = new MProject(getCtx(), getC_Project_ID());
		}
		//	Update BP for Prepayments
		if (getC_BPartner_ID() != 0 && getC_Invoice_ID() == 0 && getC_Charge_ID() == 0 && MPaymentAllocate.get(this).length == 0 && !createdAllocationRecords)
		{
			MBPartner bp = new MBPartner (getCtx(), getC_BPartner_ID(), get_TrxName());
			DB.getDatabase().forUpdate(bp, 0);
			//	Update total balance to include this payment 
			BigDecimal payAmt = MConversionRate.convertBase(getCtx(), getPayAmt(), 
				getC_Currency_ID(), getDateAcct(), getC_ConversionType_ID(), getAD_Client_ID(), getAD_Org_ID());
			if (payAmt == null)
			{
				m_processMsg = MConversionRateUtil.getErrorMessage(getCtx(), "ErrorConvertingCurrencyToBaseCurrency",
						getC_Currency_ID(), MClient.get(getCtx()).getC_Currency_ID(), getC_ConversionType_ID(), getDateAcct(), get_TrxName());
				return DocAction.STATUS_Invalid;
			}
			//	Total Balance
			BigDecimal newBalance = bp.getTotalOpenBalance();
			if (newBalance == null)
				newBalance = Env.ZERO;
			if (isReceipt())
				newBalance = newBalance.subtract(payAmt);
			else
				newBalance = newBalance.add(payAmt);
				
			bp.setTotalOpenBalance(newBalance);
			bp.setSOCreditStatus();
			bp.saveEx();
		}		

		//	Counter Doc
		MPayment counter = createCounterDoc();
		if (counter != null)
			m_processMsg += " @CounterDoc@: @C_Payment_ID@=" + counter.getDocumentNo();

		// @Trifon - CashPayments
		//if ( getTenderType().equals("X") ) {
		if ( isCashbookTrx()) {
			// Create Cash Book entry
			if ( getC_CashBook_ID() <= 0 ) {
				log.saveError("Error", Msg.parseTranslation(getCtx(), "@Mandatory@: @C_CashBook_ID@"));
				m_processMsg = "@NoCashBook@";
				return DocAction.STATUS_Invalid;
			}
			MCash cash = MCash.get (getCtx(), getAD_Org_ID(), getDateAcct(), getC_Currency_ID(), get_TrxName());
			if (cash == null || cash.get_ID() == 0)
			{
				m_processMsg = "@NoCashBook@";
				return DocAction.STATUS_Invalid;
			}
			MCashLine cl = new MCashLine( cash );
			cl.setCashType( X_C_CashLine.CASHTYPE_GeneralReceipts );
			cl.setDescription("Generated From Payment #" + getDocumentNo());
			cl.setC_Currency_ID( this.getC_Currency_ID() );
			cl.setC_Payment_ID( getC_Payment_ID() ); // Set Reference to payment.
			StringBuilder info=new StringBuilder();
			info.append("Cash journal ( ")
				.append(cash.getDocumentNo()).append(" )");				
			m_processMsg = info.toString();
			//	Amount
			BigDecimal amt = this.getPayAmt();
			/*
			MDocType dt = MDocType.get(getCtx(), invoice.getC_DocType_ID());			
			if (MDocType.DOCBASETYPE_APInvoice.equals( dt.getDocBaseType() )
				|| MDocType.DOCBASETYPE_ARCreditMemo.equals( dt.getDocBaseType() ) 
			) {
				amt = amt.negate();
			}
			 */
			cl.setAmount( amt );
			//
			cl.setDiscountAmt( Env.ZERO );
			cl.setWriteOffAmt( Env.ZERO );
			cl.setIsGenerated( true );
			
			if (!cl.save(get_TrxName()))
			{
				m_processMsg = "Could not save Cash Journal Line";
				return DocAction.STATUS_Invalid;
			}
		}
		// End Trifon - CashPayments
		
		//	update C_Invoice.C_Payment_ID and C_Order.C_Payment_ID reference
		if (getC_Invoice_ID() != 0)
		{
			MInvoice inv = new MInvoice(getCtx(), getC_Invoice_ID(), get_TrxName());
			if (inv.getC_Payment_ID() != getC_Payment_ID())
			{
				inv.setC_Payment_ID(getC_Payment_ID());
				inv.saveEx();
			}
		}		
		if (getC_Order_ID() != 0)
		{
			MOrder ord = new MOrder(getCtx(), getC_Order_ID(), get_TrxName());
			if (ord.getC_Payment_ID() != getC_Payment_ID())
			{
				ord.setC_Payment_ID(getC_Payment_ID());
				ord.saveEx();
			}
		}
		
		//	User Validation
		String valid = ModelValidationEngine.get().fireDocValidate(this, ModelValidator.TIMING_AFTER_COMPLETE);
		if (valid != null)
		{
			m_processMsg = valid;
			return DocAction.STATUS_Invalid;
		}

		//
		setProcessed(true);
		setDocAction(DOCACTION_Close);
		return DocAction.STATUS_Completed;
	}	//	completeIt
	
	// IDEMPIERE-2588
	private MAllocationHdr m_justCreatedAllocInv = null;
	public MAllocationHdr getJustCreatedAllocInv() {
		return m_justCreatedAllocInv;
	}
	
	/**
	 * 	Allocate single AP/AR Invoice
	 * 	@return true if allocated
	 */
	private boolean allocateInvoice()
	{
		//	calculate actual allocation
		BigDecimal allocationAmt = getPayAmt();			//	underpayment
		if (getOverUnderAmt().signum() < 0 && getPayAmt().signum() > 0)
			allocationAmt = allocationAmt.add(getOverUnderAmt());	//	overpayment (negative)

		MAllocationHdr alloc = new MAllocationHdr(getCtx(), false, 
			getDateTrx(), getC_Currency_ID(),
			Msg.translate(getCtx(), "C_Payment_ID") + ": " + getDocumentNo() + " [1]", get_TrxName());
		alloc.setAD_Org_ID(getAD_Org_ID());
		alloc.setDateAcct(getDateAcct()); // in case date acct is different from datetrx in payment
		alloc.saveEx();
		MAllocationLine aLine = null;
		if (isReceipt())
			aLine = new MAllocationLine (alloc, allocationAmt, 
				getDiscountAmt(), getWriteOffAmt(), getOverUnderAmt());
		else
			aLine = new MAllocationLine (alloc, allocationAmt.negate(), 
				getDiscountAmt().negate(), getWriteOffAmt().negate(), getOverUnderAmt().negate());
		aLine.setDocInfo(getC_BPartner_ID(), 0, getC_Invoice_ID());
		aLine.setC_Payment_ID(getC_Payment_ID());
		aLine.saveEx(get_TrxName());
		// added AdempiereException by zuhri
		if (!alloc.processIt(DocAction.ACTION_Complete))
			throw new AdempiereException("Failed when processing document - " + alloc.getProcessMsg());
		// end added
		alloc.saveEx(get_TrxName());
		m_justCreatedAllocInv = alloc;
		m_processMsg = "@C_AllocationHdr_ID@: " + alloc.getDocumentNo();
			
		//	Get Project from Invoice
		int C_Project_ID = DB.getSQLValue(get_TrxName(), 
			"SELECT MAX(C_Project_ID) FROM C_Invoice WHERE C_Invoice_ID=?", getC_Invoice_ID());
		if (C_Project_ID > 0 && getC_Project_ID() == 0)
			setC_Project_ID(C_Project_ID);
		else if (C_Project_ID > 0 && getC_Project_ID() > 0 && C_Project_ID != getC_Project_ID())
			log.warning("Invoice C_Project_ID=" + C_Project_ID 
				+ " <> Payment C_Project_ID=" + getC_Project_ID());
		return true;
	}	//	allocateInvoice
	
	/**
	 * 	Allocate Payment Selection
	 * 	@return true if allocated
	 */
	private boolean allocatePaySelection()
	{
		MAllocationHdr alloc = new MAllocationHdr(getCtx(), false, 
			getDateTrx(), getC_Currency_ID(),
			Msg.translate(getCtx(), "C_Payment_ID")	+ ": " + getDocumentNo() + " [n]", get_TrxName());
		alloc.setAD_Org_ID(getAD_Org_ID());
		alloc.setDateAcct(getDateAcct()); // in case date acct is different from datetrx in payment
		
		String sql = "SELECT psc.C_BPartner_ID, psl.C_Invoice_ID, psl.IsSOTrx, "	//	1..3
			+ " psl.PayAmt, psl.DiscountAmt, psl.DifferenceAmt, psl.OpenAmt, psl.WriteOffAmt "  // 4..8
			+ "FROM C_PaySelectionLine psl"
			+ " INNER JOIN C_PaySelectionCheck psc ON (psl.C_PaySelectionCheck_ID=psc.C_PaySelectionCheck_ID) "
			+ "WHERE psc.C_Payment_ID=?";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, get_TrxName());
			pstmt.setInt(1, getC_Payment_ID());
			rs = pstmt.executeQuery();
			while (rs.next())
			{
				int C_BPartner_ID = rs.getInt(1);
				int C_Invoice_ID = rs.getInt(2);
				if (C_BPartner_ID == 0 && C_Invoice_ID == 0)
					continue;
				boolean isSOTrx = "Y".equals(rs.getString(3));
				BigDecimal PayAmt = rs.getBigDecimal(4);
				BigDecimal DiscountAmt = rs.getBigDecimal(5);
				BigDecimal WriteOffAmt = rs.getBigDecimal(8);
				BigDecimal OpenAmt = rs.getBigDecimal(7);
				BigDecimal OverUnderAmt = OpenAmt.subtract(PayAmt)
					.subtract(DiscountAmt).subtract(WriteOffAmt);
				//
				if (alloc.get_ID() == 0 && !alloc.save(get_TrxName()))
				{
					log.log(Level.SEVERE, "Could not create Allocation Hdr");
					return false;
				}
				MAllocationLine aLine = null;
				if (isSOTrx)
					aLine = new MAllocationLine (alloc, PayAmt, 
						DiscountAmt, WriteOffAmt, OverUnderAmt);
				else
					aLine = new MAllocationLine (alloc, PayAmt.negate(), 
						DiscountAmt.negate(), WriteOffAmt.negate(), OverUnderAmt.negate());
				aLine.setDocInfo(C_BPartner_ID, 0, C_Invoice_ID);
				aLine.setC_Payment_ID(getC_Payment_ID());
				if (!aLine.save(get_TrxName()))
					log.log(Level.SEVERE, "Could not create Allocation Line");
			}
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, "allocatePaySelection", e);
		}
		finally
		{
			DB.close(rs, pstmt);
			rs = null;
			pstmt = null;
		}
		
		//	Should start WF
		boolean ok = true;
		if (alloc.get_ID() == 0)
		{
			if (log.isLoggable(Level.FINE)) log.fine("No Allocation created - C_Payment_ID=" 
				+ getC_Payment_ID());
			ok = false;
		}
		else
		{
			// added Adempiere Exception by zuhri
			if(alloc.processIt(DocAction.ACTION_Complete))
				ok = alloc.save(get_TrxName());
			else
				throw new AdempiereException("Failed when processing document - " + alloc.getProcessMsg());
			// end added by zuhri
			m_processMsg = "@C_AllocationHdr_ID@: " + alloc.getDocumentNo();
		}
		return ok;
	}	//	allocatePaySelection
	
	public MTCS_AllocateCharge[] getAllocLines() {

		List<MTCS_AllocateCharge> listAllocateCharge = new ArrayList<MTCS_AllocateCharge>();
		listAllocateCharge = new Query(getCtx(), MTCS_AllocateCharge.Table_Name, 
				MTCS_AllocateCharge.COLUMNNAME_C_Payment_ID + "=?", get_TrxName())
		.setParameters(getC_Payment_ID())
		.setOnlyActiveRecords(true)
		.list();

		return listAllocateCharge.toArray(new MTCS_AllocateCharge[listAllocateCharge.size()]);
	}
	
	/**
	 * 	Allocate It.
	 * 	Only call when there is NO allocation as it will create duplicates.
	 * 	If an invoice exists, it allocates that 
	 * 	otherwise it allocates Payment Selection.
	 *	@return true if allocated
	 */
	public boolean allocateIt()
	{
		//	Create invoice Allocation -	See also MCash.completeIt
		if (getC_Invoice_ID() != 0)
		{	
				return allocateInvoice();
		}	
		//	Invoices of a AP Payment Selection
		if (allocatePaySelection())
			return true;
		
		if (getC_Order_ID() != 0)
			return false;
			
		//	Allocate to multiple Payments based on entry
		MPaymentAllocate[] pAllocs = MPaymentAllocate.get(this);
		//if (pAllocs.length == 0)
			//return false;
		
//		Allocate to multiple Payments based on entry
		MAllocationHdr alloc = null;
		
		if (pAllocs.length > 0) {
			alloc = new MAllocationHdr(getCtx(), false, 
					getDateTrx(), getC_Currency_ID(), 
						Msg.translate(getCtx(), "C_Payment_ID")	+ ": " + getDocumentNo(), 
						get_TrxName());
			alloc.setAD_Org_ID(getAD_Org_ID());
			alloc.setDateAcct(getDateAcct()); // in case date acct is different from datetrx in payment; IDEMPIERE-1532 tbayen
			if (!alloc.save())
			{
				log.severe("P.Allocations not created");
				return false;
			}
			//	Lines
			for (int i = 0; i < pAllocs.length; i++)
			{
				MPaymentAllocate pa = pAllocs[i];
				MAllocationLine aLine = null;
				if (isReceipt())
					aLine = new MAllocationLine (alloc, pa.getAmount(), 
						pa.getDiscountAmt(), pa.getWriteOffAmt(), pa.getOverUnderAmt());
				else
					aLine = new MAllocationLine (alloc, pa.getAmount().negate(), 
						pa.getDiscountAmt().negate(), pa.getWriteOffAmt().negate(), pa.getOverUnderAmt().negate());
				aLine.setDocInfo(pa.getC_BPartner_ID(), 0, pa.getC_Invoice_ID());
				aLine.setPaymentInfo(getC_Payment_ID(), 0);
				aLine.set_ValueOfColumn("Description", pa.get_ValueAsString("Description"));
				if (!aLine.save(get_TrxName()))
					log.warning("P.Allocations - line not saved");
				else
				{
					pa.setC_AllocationLine_ID(aLine.getC_AllocationLine_ID());
					pa.saveEx();
				}
			}
		}
		
		MTCS_AllocateCharge[] pAllocMultiCharge = getAllocLines();
		if (pAllocMultiCharge.length > 0) {
			if(alloc==null){
				alloc = new MAllocationHdr(getCtx(), false, 
						getDateTrx(), getC_Currency_ID(), 
						Msg.translate(getCtx(), "C_Payment_ID")	+ ": " + getDocumentNo(), 
						get_TrxName());
				alloc.setAD_Org_ID(getAD_Org_ID());
				alloc.setDateAcct(getDateAcct()); // in case date acct is different from datetrx in payment; IDEMPIERE-1532 tbayen
				if (!alloc.save())
				{
					log.severe("P.Allocations not created");
					return false;
				}
			}
			for (MTCS_AllocateCharge allocharge : pAllocMultiCharge)
			{
				BigDecimal allocateAmount = isReceipt() 
						? allocharge.getAmount() 
						: allocharge.getAmount().negate();
				
				MAllocationLine alloclineCr = new MAllocationLine(alloc);
				alloclineCr.setAD_Org_ID(allocharge.getAD_Org_ID());
				alloclineCr.setC_BPartner_ID(getC_BPartner_ID());
				alloclineCr.setC_Payment_ID(getC_Payment_ID());
				alloclineCr.setDateTrx(alloc.getDateTrx());
				alloclineCr.setAmount(allocateAmount);
				//allocline.setC_Charge_ID(allocharge.getC_Charge_ID());
				alloclineCr.set_ValueOfColumn("Description", allocharge.get_ValueAsString("Description"));
				alloclineCr.saveEx();
				
				MAllocationLine alloclineDr = new MAllocationLine(alloc);
				alloclineDr.setAD_Org_ID(allocharge.getAD_Org_ID());
				alloclineDr.setC_BPartner_ID(getC_BPartner_ID());
				//alloclineDr.setC_Payment_ID(getC_Payment_ID());
				alloclineDr.setDateTrx(alloc.getDateTrx());
				alloclineDr.setAmount(allocateAmount.negate());
				alloclineDr.setC_Charge_ID(allocharge.getC_Charge_ID());
				//@PhieAlbert
				alloclineDr.set_ValueOfColumn("TCS_AllocateCharge_ID", allocharge.getTCS_AllocateCharge_ID());
				alloclineDr.set_ValueOfColumn("Description", allocharge.get_ValueAsString("Description"));
				//end @PhieAlbert
				alloclineDr.saveEx();
			}
		}
		
		if (alloc==null)
			return false;
		
		// added AdempiereException by zuhri
		if (!alloc.processIt(DocAction.ACTION_Complete))
			throw new AdempiereException("Failed when processing document - " + alloc.getProcessMsg());
		// end added
		m_processMsg = "@C_AllocationHdr_ID@: " + alloc.getDocumentNo();
		return alloc.save(get_TrxName());
	}	//	allocateIt
	
	/**
	 * 	Set the definite document number after completed
	 */
	private void setDefiniteDocumentNo() {
		MDocType dt = MDocType.get(getCtx(), getC_DocType_ID());
		if (dt.isOverwriteDateOnComplete()) {
			setDateTrx(TimeUtil.getDay(0));
			if (getDateAcct().before(getDateTrx())) {
				setDateAcct(getDateTrx());
				MPeriod.testPeriodOpen(getCtx(), getDateAcct(), getC_DocType_ID(), getAD_Org_ID());
			}
		}
		if (dt.isOverwriteSeqOnComplete()) {
			String value = DB.getDocumentNo(getC_DocType_ID(), get_TrxName(), true, this);
			if (value != null)
				setDocumentNo(value);
		}
	}
	
	/**
	 * 	Create Counter Document
	 * 	@return payment
	 */
	private MPayment createCounterDoc()
	{
		//	Is this a counter doc ?
		if (getRef_Payment_ID() != 0)
			return null;

		//	Org Must be linked to BPartner
		MOrg org = MOrg.get(getCtx(), getAD_Org_ID());
		int counterC_BPartner_ID = org.getLinkedC_BPartner_ID(get_TrxName()); 
		if (counterC_BPartner_ID == 0)
			return null;
		//	Business Partner needs to be linked to Org
		MBPartner bp = new MBPartner (getCtx(), getC_BPartner_ID(), get_TrxName());
		int counterAD_Org_ID = bp.getAD_OrgBP_ID_Int(); 
		if (counterAD_Org_ID == 0)
			return null;
		
		MBPartner counterBP = new MBPartner (getCtx(), counterC_BPartner_ID, get_TrxName());
	//	MOrgInfo counterOrgInfo = MOrgInfo.get(getCtx(), counterAD_Org_ID);
		if (log.isLoggable(Level.INFO)) log.info("Counter BP=" + counterBP.getName());

		//	Document Type
		int C_DocTypeTarget_ID = 0;
		MDocTypeCounter counterDT = MDocTypeCounter.getCounterDocType(getCtx(), getC_DocType_ID());
		if (counterDT != null)
		{
			if (log.isLoggable(Level.FINE)) log.fine(counterDT.toString());
			if (!counterDT.isCreateCounter() || !counterDT.isValid())
				return null;
			C_DocTypeTarget_ID = counterDT.getCounter_C_DocType_ID();
		}
		else	//	indirect
		{
			C_DocTypeTarget_ID = MDocTypeCounter.getCounterDocType_ID(getCtx(), getC_DocType_ID());
			if (log.isLoggable(Level.FINE)) log.fine("Indirect C_DocTypeTarget_ID=" + C_DocTypeTarget_ID);
			if (C_DocTypeTarget_ID <= 0)
				return null;
		}

		//	Deep Copy
		MPayment counter = new MPayment (getCtx(), 0, get_TrxName());
		counter.setAD_Org_ID(counterAD_Org_ID);
		counter.setC_BPartner_ID(counterBP.getC_BPartner_ID());
		counter.setIsReceipt(!isReceipt());
		counter.setC_DocType_ID(C_DocTypeTarget_ID);
		counter.setTrxType(getTrxType());
		counter.setTenderType(getTenderType());
		//
		counter.setPayAmt(getPayAmt());
		counter.setDiscountAmt(getDiscountAmt());
		counter.setTaxAmt(getTaxAmt());
		counter.setWriteOffAmt(getWriteOffAmt());
		counter.setIsOverUnderPayment (isOverUnderPayment());
		counter.setOverUnderAmt(getOverUnderAmt());
		counter.setC_Currency_ID(getC_Currency_ID());
		counter.setC_ConversionType_ID(getC_ConversionType_ID());
		//
		counter.setDateTrx (getDateTrx());
		counter.setDateAcct (getDateAcct());
		counter.setRef_Payment_ID(getC_Payment_ID());
		//
		String sql = "SELECT C_BankAccount_ID FROM C_BankAccount "
			+ "WHERE C_Currency_ID=? AND AD_Org_ID IN (0,?) AND IsActive='Y' "
			+ "ORDER BY IsDefault DESC";
		int C_BankAccount_ID = DB.getSQLValue(get_TrxName(), sql, getC_Currency_ID(), counterAD_Org_ID);
		counter.setC_BankAccount_ID(C_BankAccount_ID);

		//	References
		counter.setC_Activity_ID(getC_Activity_ID());
		counter.setC_Campaign_ID(getC_Campaign_ID());
		counter.setC_Project_ID(getC_Project_ID());
		counter.setUser1_ID(getUser1_ID());
		counter.setUser2_ID(getUser2_ID());
		counter.saveEx(get_TrxName());
		if (log.isLoggable(Level.FINE)) log.fine(counter.toString());
		setRef_Payment_ID(counter.getC_Payment_ID());
		
		//	Document Action
		if (counterDT != null)
		{
			if (counterDT.getDocAction() != null)
			{
				counter.setDocAction(counterDT.getDocAction());
				// added AdempiereException by zuhri
				if (!counter.processIt(counterDT.getDocAction()))
					throw new AdempiereException("Failed when rocessing document - " + counter.getProcessMsg());
				// end added
				counter.saveEx(get_TrxName());
			}
		}
		return counter;
	}	//	createCounterDoc
	
	@Override
	protected boolean beforeSave(boolean newRecord) {
		if (isComplete() && 
				! is_ValueChanged(COLUMNNAME_Processed) &&
	            (   is_ValueChanged(COLUMNNAME_C_BankAccount_ID)
	             || is_ValueChanged(COLUMNNAME_C_BPartner_ID)
	             || is_ValueChanged(COLUMNNAME_C_Charge_ID)
	             || is_ValueChanged(COLUMNNAME_C_Currency_ID)
	             || is_ValueChanged(COLUMNNAME_C_DocType_ID)
	             || is_ValueChanged(COLUMNNAME_DateAcct)
	             || is_ValueChanged(COLUMNNAME_DateTrx)
	             || is_ValueChanged(COLUMNNAME_DiscountAmt)
	             || is_ValueChanged(COLUMNNAME_PayAmt)
	             || is_ValueChanged(COLUMNNAME_WriteOffAmt))) {
				log.saveError("PaymentAlreadyProcessed", Msg.translate(getCtx(), "C_Payment_ID"));
				return false;
			}
			// @Trifon - CashPayments
			//if ( getTenderType().equals("X") ) {
			if ( isCashbookTrx()) {
				// Cash Book Is mandatory
				if ( getC_CashBook_ID() <= 0 ) {
					log.saveError("Error", Msg.parseTranslation(getCtx(), "@Mandatory@: @C_CashBook_ID@"));
					return false;
				}
			} else {
				// Bank Account Is mandatory
				if ( getC_BankAccount_ID() <= 0 ) {
					log.saveError("Error", Msg.parseTranslation(getCtx(), "@Mandatory@: @C_BankAccount_ID@"));
					return false;
				}
			}
			// end @Trifon - CashPayments
			
			//	We have a charge
			if (getC_Charge_ID() != 0) 
			{
				if (newRecord || is_ValueChanged("C_Charge_ID"))
				{
					setC_Order_ID(0);
					setC_Invoice_ID(0);
					setWriteOffAmt(Env.ZERO);
					setDiscountAmt(Env.ZERO);
					setIsOverUnderPayment(false);
					setOverUnderAmt(Env.ZERO);
					setIsPrepayment(false);
				}
			}
			//	We need a BPartner
			else if (getC_BPartner_ID() == 0 && !isCashTrx())
			{
				if (getC_Invoice_ID() != 0)
					;
				else if (getC_Order_ID() != 0)
					;
				else
				{
					log.saveError("Error", Msg.parseTranslation(getCtx(), "@NotFound@: @C_BPartner_ID@"));
					return false;
				}
			}
			//	Prepayment: No charge and order or project (not as acct dimension)
/*
			if (newRecord 
				|| is_ValueChanged("C_Charge_ID") || is_ValueChanged("C_Invoice_ID")
				|| is_ValueChanged("C_Order_ID") || is_ValueChanged("C_Project_ID"))
				setIsPrepayment (getC_Charge_ID() == 0 
					&& getC_BPartner_ID() != 0
					&& (getC_Order_ID() != 0 
						|| (getC_Project_ID() != 0 && getC_Invoice_ID() == 0)));
*/
			if (isPrepayment())
			{
				if (newRecord 
					|| is_ValueChanged("C_Order_ID") || is_ValueChanged("C_Project_ID"))
				{
					setWriteOffAmt(Env.ZERO);
					setDiscountAmt(Env.ZERO);
					setIsOverUnderPayment(false);
					setOverUnderAmt(Env.ZERO);
				}
			}
			
			//	Document Type/Receipt
			if (getC_DocType_ID() == 0)
				setC_DocType_ID();
			else
			{
				MDocType dt = MDocType.get(getCtx(), getC_DocType_ID());
				setIsReceipt(dt.isSOTrx());
			}
			setDocumentNo();
			//
			if (getDateAcct() == null)
				setDateAcct(getDateTrx());
			//
			if (!isOverUnderPayment())
				setOverUnderAmt(Env.ZERO);
			
			//	Organization
			if ((newRecord || is_ValueChanged("C_BankAccount_ID"))
				&& getC_Charge_ID() == 0)	//	allow different org for charge
			{
				MBankAccount ba = MBankAccount.get(getCtx(), getC_BankAccount_ID());
				if (ba.getAD_Org_ID() != 0)
					setAD_Org_ID(ba.getAD_Org_ID());
			}
			
			// [ adempiere-Bugs-1885417 ] Validate BP on Payment Prepare or BeforeSave
			// there is bp and (invoice or order)
			if (getC_BPartner_ID() != 0 && (getC_Invoice_ID() != 0 || getC_Order_ID() != 0)) {
				if (getC_Invoice_ID() != 0) {
					MInvoice inv = new MInvoice(getCtx(), getC_Invoice_ID(), get_TrxName());
					if (inv.getC_BPartner_ID() != getC_BPartner_ID()) {
						log.saveError("Error", Msg.parseTranslation(getCtx(), "BP different from BP Invoice"));
						return false;
					}
				}
				if (getC_Order_ID() != 0) {
					MOrder ord = new MOrder(getCtx(), getC_Order_ID(), get_TrxName());
					if (ord.getC_BPartner_ID() != getC_BPartner_ID()) {
						log.saveError("Error", Msg.parseTranslation(getCtx(), "BP different from BP Order"));
						return false;
					}
				}
			}
			
			if (isProcessed())
			{
				if (getCreditCardNumber() != null)
				{
					String encrpytedCCNo = PaymentUtil.encrpytCreditCard(getCreditCardNumber());
					if (!encrpytedCCNo.equals(getCreditCardNumber()))
						setCreditCardNumber(encrpytedCCNo);
				}
				
				if (getCreditCardVV() != null)
				{
					String encrpytedCvv = PaymentUtil.encrpytCvv(getCreditCardVV());
					if (!encrpytedCvv.equals(getCreditCardVV()))
						setCreditCardVV(encrpytedCvv);
				}
			}

			if (MSysConfig.getBooleanValue(MSysConfig.IBAN_VALIDATION, true, Env.getAD_Client_ID(Env.getCtx()))) {
				if (!Util.isEmpty(getIBAN())) {
					setIBAN(IBAN.normalizeIBAN(getIBAN()));
					if (!IBAN.isValid(getIBAN())) {
						log.saveError("Error", Msg.getMsg(getCtx(), "InvalidIBAN"));
						return false;
					}
				}
			}
			
			return true;
		}
	/**
	 * 	Set Doc Type bases on IsReceipt
	 */
	private void setC_DocType_ID ()
	{
		setC_DocType_ID(isReceipt());
	}	//	setC_DocType_ID
	
	/**
	 *  Set DocumentNo to Payment info.
	 * 	If there is a R_PnRef that is set automatically 
	 */
	private void setDocumentNo()
	{
		//	Cash Transfer
		if ("X".equals(getTenderType()))
			return;
		//	Current Document No
		String documentNo = getDocumentNo();
		//	Existing reversal
		if (documentNo != null 
			&& documentNo.indexOf(REVERSE_INDICATOR) >= 0)
			return;
		
		//	If external number exists - enforce it 
		if (getR_PnRef() != null && getR_PnRef().length() > 0)
		{
			if (!getR_PnRef().equals(documentNo))
				setDocumentNo(getR_PnRef());
			return;
		}
		
		documentNo = "";
		// globalqss - read configuration to assign credit card or check number number for Payments
		//	Credit Card
		if (TENDERTYPE_CreditCard.equals(getTenderType()))
		{
			if (MSysConfig.getBooleanValue(MSysConfig.PAYMENT_OVERWRITE_DOCUMENTNO_WITH_CREDIT_CARD, true, getAD_Client_ID())) {
				documentNo = getCreditCardType()
					+ " " + Obscure.obscure(getCreditCardNumber())
					+ " " + getCreditCardExpMM() 
					+ "/" + getCreditCardExpYY();
			}
		}
		//	Own Check No
		else if (TENDERTYPE_Check.equals(getTenderType())
			&& !isReceipt()
			&& getCheckNo() != null && getCheckNo().length() > 0)
		{
			if (MSysConfig.getBooleanValue(MSysConfig.PAYMENT_OVERWRITE_DOCUMENTNO_WITH_CHECK_ON_PAYMENT, true, getAD_Client_ID())) {
				documentNo = getCheckNo();
			}
		}
		//	Customer Check: Routing: Account #Check 
		else if (TENDERTYPE_Check.equals(getTenderType())
			&& isReceipt())
		{
			if (MSysConfig.getBooleanValue(MSysConfig.PAYMENT_OVERWRITE_DOCUMENTNO_WITH_CHECK_ON_RECEIPT, true, getAD_Client_ID())) {
				if (getRoutingNo() != null)
					documentNo = getRoutingNo() + ": ";
				if (getAccountNo() != null)
					documentNo += getAccountNo();
				if (getCheckNo() != null)
				{
					if (documentNo.length() > 0)
						documentNo += " ";
					documentNo += "#" + getCheckNo();
				}
			}
		}

		//	Set Document No
		documentNo = documentNo.trim();
		if (documentNo.length() > 0)
			setDocumentNo(documentNo);
	}	//	setDocumentNo
	
	@Override
	public String prepareIt() {
		if (log.isLoggable(Level.INFO)) log.info(toString());
		m_processMsg = ModelValidationEngine.get().fireDocValidate(this, ModelValidator.TIMING_BEFORE_PREPARE);
		if (m_processMsg != null)
			return DocAction.STATUS_Invalid;

		if (! MPaySelectionCheck.deleteGeneratedDraft(getCtx(), getC_Payment_ID(), get_TrxName())) {
			m_processMsg = "Could not delete draft generated payment selection lines";
			return DocAction.STATUS_Invalid;
		}

		//	Std Period open?
		if (!MPeriod.isOpen(getCtx(), getDateAcct(), 
			isReceipt() ? X_C_DocType.DOCBASETYPE_ARReceipt : X_C_DocType.DOCBASETYPE_APPayment, getAD_Org_ID()))
		{
			m_processMsg = "@PeriodClosed@";
			return DocAction.STATUS_Invalid;
		}
		
		//	Unsuccessful Online Payment
		if (isOnline() && !isApproved())
		{
			if (getR_Result() != null)
				m_processMsg = "@OnlinePaymentFailed@";
			else
				m_processMsg = "@PaymentNotProcessed@";
			return DocAction.STATUS_Invalid;
		}
		
		//	Waiting Payment - Need to create Invoice & Shipment
		if (getC_Order_ID() != 0 && getC_Invoice_ID() == 0)
		{	//	see WebOrder.process
			MOrder order = new MOrder (getCtx(), getC_Order_ID(), get_TrxName());
			if (DOCSTATUS_WaitingPayment.equals(order.getDocStatus()))
			{
				order.setC_Payment_ID(getC_Payment_ID());
				order.setDocAction(X_C_Order.DOCACTION_WaitComplete);
				order.set_TrxName(get_TrxName());
				// added AdempiereException by zuhri 
				if (!order.processIt (X_C_Order.DOCACTION_WaitComplete))
					throw new AdempiereException("Failed when processing document - " + order.getProcessMsg());
				// end added
				m_processMsg = order.getProcessMsg();
				order.saveEx(get_TrxName());
				//	Set Invoice
				MInvoice[] invoices = order.getInvoices();
				int length = invoices.length;
				if (length > 0)		//	get last invoice
					setC_Invoice_ID (invoices[length-1].getC_Invoice_ID());
				//
				if (getC_Invoice_ID() == 0)
				{
					m_processMsg = "@NotFound@ @C_Invoice_ID@";
					return DocAction.STATUS_Invalid;
				}
			}	//	WaitingPayment
		}
		
		MPaymentAllocate[] pAllocs = MPaymentAllocate.get(this);
		
		//	Consistency of Invoice / Document Type and IsReceipt
		if (!verifyDocType(pAllocs))
		{
			m_processMsg = "@PaymentDocTypeInvoiceInconsistent@";
			return DocAction.STATUS_Invalid;
		}

		//	Payment Allocate is ignored if charge/invoice/order exists in header
		if (!verifyPaymentAllocateVsHeader(pAllocs))
		{
			m_processMsg = "@PaymentAllocateIgnored@";
			return DocAction.STATUS_Invalid;
		}

		//	Payment Amount must be equal to sum of Allocate amounts
		if (!verifyPaymentAllocateSum(pAllocs))
		{
			m_processMsg = "@PaymentAllocateSumInconsistent@";
			return DocAction.STATUS_Invalid;
		}

		//	Do not pay when Credit Stop/Hold
		if (!isReceipt())
		{
			MBPartner bp = new MBPartner (getCtx(), getC_BPartner_ID(), get_TrxName());
			if (X_C_BPartner.SOCREDITSTATUS_CreditStop.equals(bp.getSOCreditStatus()))
			{
				m_processMsg = "@BPartnerCreditStop@ - @TotalOpenBalance@=" 
					+ bp.getTotalOpenBalance()
					+ ", @SO_CreditLimit@=" + bp.getSO_CreditLimit();
				return DocAction.STATUS_Invalid;
			}
			if (X_C_BPartner.SOCREDITSTATUS_CreditHold.equals(bp.getSOCreditStatus()))
			{
				m_processMsg = "@BPartnerCreditHold@ - @TotalOpenBalance@=" 
					+ bp.getTotalOpenBalance()
					+ ", @SO_CreditLimit@=" + bp.getSO_CreditLimit();
				return DocAction.STATUS_Invalid;
			}
		}
		
		m_processMsg = ModelValidationEngine.get().fireDocValidate(this, ModelValidator.TIMING_AFTER_PREPARE);
		if (m_processMsg != null)
			return DocAction.STATUS_Invalid;

		m_justPrepared = true;
		if (!DOCACTION_Complete.equals(getDocAction()))
			setDocAction(DOCACTION_Complete);
		return DocAction.STATUS_InProgress;
	}
	private boolean verifyPaymentAllocateSum(MPaymentAllocate[] pAllocs) {
		BigDecimal sumPaymentAllocates = Env.ZERO;
		//TCS calculation include TCS_AllocateCharge
		String sqlCount = "SELECT COUNT(1) FROM TCS_AllocateCharge WHERE C_Payment_ID="+getC_Payment_ID();
		int allocChargeCount = DB.getSQLValue(get_TrxName(), sqlCount);
		
		if (pAllocs.length > 0 || allocChargeCount > 0) {
			for (MPaymentAllocate pAlloc : pAllocs)
				sumPaymentAllocates = sumPaymentAllocates.add(pAlloc.getAmount());
			
			String sql = "SELECT COALESCE(SUM(Amount),0) FROM TCS_AllocateCharge WHERE C_Payment_ID="+getC_Payment_ID();
			sumPaymentAllocates=sumPaymentAllocates.add((BigDecimal)DB.getSQLValueBD(get_TrxName(), sql));
			if (getPayAmt().compareTo(sumPaymentAllocates) != 0) {
				if (isReceipt() && getPayAmt().compareTo(sumPaymentAllocates) < 0) {
					if (MSysConfig.getBooleanValue(MSysConfig.ALLOW_OVER_APPLIED_PAYMENT, false, Env.getAD_Client_ID(Env.getCtx()))) {
						return true;
					}
				}
				return false;
			}
		}
		return true;
	}
	
	private boolean verifyDocType(MPaymentAllocate[] pAllocs)
	{
		if (getC_DocType_ID() == 0)
			return false;
		//
		Boolean documentSO = null;
		//	Check Invoice First
		if (getC_Invoice_ID() > 0)
		{
			String sql = "SELECT idt.IsSOTrx "
				+ "FROM C_Invoice i"
				+ " INNER JOIN C_DocType idt ON (CASE WHEN i.C_DocType_ID=0 THEN i.C_DocTypeTarget_ID ELSE i.C_DocType_ID END=idt.C_DocType_ID) "
				+ "WHERE i.C_Invoice_ID=?";
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			try
			{
				pstmt = DB.prepareStatement(sql, get_TrxName());
				pstmt.setInt(1, getC_Invoice_ID());
				rs = pstmt.executeQuery();
				if (rs.next())
					documentSO = new Boolean ("Y".equals(rs.getString(1)));
			}
			catch (Exception e)
			{
				log.log(Level.SEVERE, sql, e);
			}
			finally
			{
				DB.close(rs, pstmt);
				rs = null;
				pstmt = null;
			}
		}	//	now Order - in Adempiere is allowed to pay PO or receive SO
		else if (getC_Order_ID() > 0)
		{
			String sql = "SELECT odt.IsSOTrx "
				+ "FROM C_Order o"
				+ " INNER JOIN C_DocType odt ON (o.C_DocType_ID=odt.C_DocType_ID) "
				+ "WHERE o.C_Order_ID=?";
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			try
			{
				pstmt = DB.prepareStatement(sql, get_TrxName());
				pstmt.setInt(1, getC_Order_ID());
				rs = pstmt.executeQuery();
				if (rs.next())
					documentSO = new Boolean ("Y".equals(rs.getString(1)));
			}
			catch (Exception e)
			{
				log.log(Level.SEVERE, sql, e);
			}
			finally
			{
				DB.close(rs, pstmt);
				rs = null;
				pstmt = null;
			}
		}	//	now Charge
		else if (getC_Charge_ID() > 0) 
		{
			// do nothing about charge
		} // now payment allocate
		else
		{
			if (pAllocs.length > 0) {
				for (MPaymentAllocate pAlloc : pAllocs) {
					String sql = "SELECT idt.IsSOTrx "
						+ "FROM C_Invoice i"
						+ " INNER JOIN C_DocType idt ON (i.C_DocType_ID=idt.C_DocType_ID) "
						+ "WHERE i.C_Invoice_ID=?";
					PreparedStatement pstmt = null;
					ResultSet rs = null;
					try
					{
						pstmt = DB.prepareStatement(sql, get_TrxName());
						pstmt.setInt(1, pAlloc.getC_Invoice_ID());
						rs = pstmt.executeQuery();
						if (rs.next()) {
							if (documentSO != null) { // already set, compare with current
								if (documentSO.booleanValue() != ("Y".equals(rs.getString(1)))) {
									return false;
								}
							} else {
								documentSO = new Boolean ("Y".equals(rs.getString(1)));
							}
						}
					}
					catch (Exception e)
					{
						log.log(Level.SEVERE, sql, e);
					}
					finally
					{
						DB.close(rs, pstmt);
						rs = null;
						pstmt = null;
					}
				}
			}
		}
		
		//	DocumentType
		Boolean paymentSO = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "SELECT IsSOTrx "
			+ "FROM C_DocType "
			+ "WHERE C_DocType_ID=?";
		try
		{
			pstmt = DB.prepareStatement(sql, get_TrxName());
			pstmt.setInt(1, getC_DocType_ID());
			rs = pstmt.executeQuery();
			if (rs.next())
				paymentSO = new Boolean ("Y".equals(rs.getString(1)));
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, sql, e);
		}
		finally
		{
			DB.close(rs, pstmt);
			rs = null;
			pstmt = null;
		}
		//	No Payment info
		if (paymentSO == null)
			return false;
		setIsReceipt(paymentSO.booleanValue());
			
		//	We have an Invoice .. and it does not match
		if (documentSO != null 
				&& documentSO.booleanValue() != paymentSO.booleanValue())
			return false;
		//	OK
		return true;
	}	//	verifyDocType
	
	/**
	 * 	Verify Payment Allocate is ignored (must not exists) if the payment header has charge/invoice/order
	 * @param pAllocs 
	 *	@return true if ok
	 */
	private boolean verifyPaymentAllocateVsHeader(MPaymentAllocate[] pAllocs) {
		if (pAllocs.length > 0) {
			if (getC_Charge_ID() > 0 || getC_Invoice_ID() > 0 || getC_Order_ID() > 0)
				return false;
		}
		return true;
	}
}
