package org.compiere.model;

import java.sql.ResultSet;
import java.util.Properties;

import org.compiere.model.MRfQ;
import org.compiere.process.DocAction;

public class TCS_MRfQ extends MRfQ{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2142309603719606732L;
	
	public TCS_MRfQ(Properties ctx, int C_RfQ_ID, String trxName) {
		super(ctx, C_RfQ_ID, trxName);
	}
	public TCS_MRfQ(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	public int customizeValidActions(String docStatus, Object processing,
			String orderType, String isSOTrx, int AD_Table_ID,
			String[] docAction, String[] options, int index) {
		index = 0;
		if (docStatus.equals(DocAction.STATUS_Drafted)) {
			options[index++] = DocAction.ACTION_Prepare;
			options[index++] = DocAction.ACTION_Void;

		} else if (docStatus.equals(DocAction.STATUS_InProgress)) {
			options[index++] = DocAction.ACTION_Complete;
			options[index++] = DocAction.ACTION_Void;

		} else if (docStatus.equals(DocAction.STATUS_Invalid)) {
			options[index++] = DocAction.ACTION_Prepare;
			options[index++] = DocAction.ACTION_Void;

		} else if (docStatus.equals(DocAction.STATUS_Completed)) {
			options[index++] = DocAction.ACTION_Void;
			options[index++] = DocAction.ACTION_Close;
		}
		return index;
	}
	
}
