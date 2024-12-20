package org.compiere.model;

import java.sql.ResultSet;
import java.util.Properties;

import org.compiere.process.DocAction;
import org.compiere.process.DocOptions;

public class TCS_MInOut extends MInOut implements DocOptions {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4470831310287534076L;

	public TCS_MInOut(Properties ctx, int M_InOut_ID, String trxName) {
		super(ctx, M_InOut_ID, trxName);
	}

	public TCS_MInOut(Properties ctx, ResultSet rs, String trxName) {
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
			options[index++] = DocAction.ACTION_Void;
		} else if (docStatus.equals(DocAction.STATUS_InProgress)) {
			options[index++] = DocAction.ACTION_Complete;
			options[index++] = DocAction.ACTION_Void;
		} else if (docStatus.equals(DocAction.STATUS_Completed)) {
			options[index++] = DocAction.ACTION_Reverse_Accrual;
			options[index++] = DocAction.ACTION_Reverse_Correct;
		} else if (docStatus.equals(DocAction.STATUS_Invalid)) {
			options[index++] = DocAction.ACTION_Complete;
			options[index++] = DocAction.ACTION_Void;
		} else if (docStatus.equals(DocAction.STATUS_NotApproved)) {
			options[index++] = DocAction.ACTION_Complete;
			options[index++] = DocAction.ACTION_Void;
		}

		return index;
	
	}

}
