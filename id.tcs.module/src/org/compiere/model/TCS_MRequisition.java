package org.compiere.model;

import java.util.Properties;
import java.util.logging.Level;

import org.compiere.process.DocAction;
import org.compiere.process.DocOptions;

public class TCS_MRequisition extends MRequisition implements DocOptions {

	private String			m_processMsg = null;
	/**
	 * 
	 */
	private static final long serialVersionUID = 4470831310287534076L;

	public TCS_MRequisition(Properties ctx, int M_Requisition_ID, String trxName) {
		super(ctx, M_Requisition_ID, trxName);
	}

	@Override
	public boolean reActivateIt() {
		if (log.isLoggable(Level.INFO)) log.info("reActivateIt - " + toString());
		// Before reActivate
		m_processMsg = ModelValidationEngine.get().fireDocValidate(this,ModelValidator.TIMING_BEFORE_REACTIVATE);
		if (m_processMsg != null)
			return false;

	//	setProcessed(false);
//		if (! reverseCorrectIt())
		//	return false;

		// After reActivate
		m_processMsg = ModelValidationEngine.get().fireDocValidate(this,ModelValidator.TIMING_AFTER_REACTIVATE);
		if (m_processMsg != null)
			return false;

		return true;
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

}
