package org.compiere.model;

import java.sql.ResultSet;
import java.util.Properties;
import java.util.logging.Level;

import org.compiere.process.DocAction;
import org.compiere.process.DocOptions;

public class TCS_MRMA extends MRMA implements DocOptions {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4470831310287534076L;

	public TCS_MRMA(Properties ctx, int M_RMA_ID, String trxName) {
		super(ctx, M_RMA_ID, trxName);
	}

	public TCS_MRMA(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	@Override
	public int customizeValidActions(String docStatus, Object processing, String orderType, String isSOTrx,
			int AD_Table_ID, String[] docAction, String[] options, int index) {

		for (int i = 0; i < options.length; i++) {
			options[i] = null;
		}

		index = 0;

		if (docStatus.equals(DocAction.STATUS_Drafted)) {
			options[index++] = DocAction.ACTION_Complete;
			options[index++] = DocAction.ACTION_Prepare;
			options[index++] = DocAction.ACTION_Void;
		} else if (docStatus.equals(DocAction.STATUS_InProgress)) {
			options[index++] = DocAction.ACTION_Complete;
			options[index++] = DocAction.ACTION_Void;
		} else if (docStatus.equals(DocAction.STATUS_Completed)) {
			options[index++] = DocAction.ACTION_Close;
			options[index++] = DocAction.ACTION_Void;
			options[index++] = DocAction.ACTION_ReActivate;
		} else if (docStatus.equals(DocAction.STATUS_Invalid)) {
			options[index++] = DocAction.ACTION_Complete;
			options[index++] = DocAction.ACTION_Void;
		}

		return index;

	}

	/**
	 * 	Re-activate
	 * 	@return true if success
	 */

	public boolean reActivateIt()
	{
		if (log.isLoggable(Level.INFO)) log.info("reActivateIt - " + toString());
		// Before reActivate
		m_processMsg = ModelValidationEngine.get().fireDocValidate(this,ModelValidator.TIMING_BEFORE_REACTIVATE);
		if (m_processMsg != null)
			return false;

		//@win Check Counter Document Begin
		//		Is this a counter doc ?
		if (getRef_RMA_ID() > 0) {
			m_processMsg = "Counter Document Existed, Cannot Reactivate";
			return false;
		}
		
		//@win Check Counter Document End
		setIsApproved(false);
		setProcessed(false);
		setDocAction(DocAction.ACTION_Complete);
		setDocStatus(DocAction.STATUS_InProgress);

		// After reActivate
		m_processMsg = ModelValidationEngine.get().fireDocValidate(this,ModelValidator.TIMING_AFTER_REACTIVATE);
		if (m_processMsg != null)
			return false;

		return true;
	}	//	reActivateIt
	
//	public String completeIt()
//	{
//		//	Re-Check
//		if (!m_justPrepared)
//		{
//			String status = prepareIt();
//			m_justPrepared = false;
//			if (!DocAction.STATUS_InProgress.equals(status))
//				return status;
//		}
//
//		// Set the definite document number after completed (if needed)
//		setDefiniteDocumentNo();
//
//		m_processMsg = ModelValidationEngine.get().fireDocValidate(this, ModelValidator.TIMING_BEFORE_COMPLETE);
//		if (m_processMsg != null)
//			return DocAction.STATUS_Invalid;
//
//		//	Implicit Approval
//		if (!isApproved())
//			approveIt();
//		if (log.isLoggable(Level.INFO)) log.info("completeIt - " + toString());
//		//
//		/*
//		Flow for the creation of the credit memo document changed
//        if (true)
//		{
//			m_processMsg = "Need to code creating the credit memo";
//			return DocAction.STATUS_InProgress;
//		}
//        */
//		
//
//		//		Counter Documents
//		MRMA counter = createCounterDoc();
//		if (counter != null)
//			m_processMsg = "@CounterDoc@: RMA=" + counter.getDocumentNo();
//
//		//	User Validation
//		String valid = ModelValidationEngine.get().fireDocValidate(this, ModelValidator.TIMING_AFTER_COMPLETE);
//		if (valid != null)
//		{
//			m_processMsg = valid;
//			return DocAction.STATUS_Invalid;
//		}
//
//		//		Waiting Payment - until we have a payment
//		MDocType dt = new MDocType(getCtx(), getC_DocType_ID(), get_TrxName());
//		if (dt.get_Value("DocSubTypeSOReturn").equals("RC") && get_ValueAsInt("C_Payment_ID") <= 0)
//		{
//			setProcessed(true);
//			return DocAction.STATUS_WaitingPayment;
//		}
//		
//
//		
//		//
//		setProcessed(true);
//		setDocAction(DOCACTION_Close);
//		return DocAction.STATUS_Completed;
//	}	//	completeIt

}
