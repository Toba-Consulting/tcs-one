package org.compiere.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.ResultSet;
import java.util.Properties;
import java.util.logging.Level;

import org.adempiere.exceptions.AdempiereException;
import org.compiere.process.DocAction;
import org.compiere.process.DocOptions;
import org.compiere.util.Env;

import id.tcs.model.I_M_MatchPR;
import id.tcs.model.MMatchPR;
import id.tcs.model.X_M_MatchPR;

public class TCS_MRequisition extends MRequisition implements DocOptions {

	private String			m_processMsg = null;
	/**
	 * 
	 */
	private static final long serialVersionUID = 4470831310287534076L;

	public TCS_MRequisition(Properties ctx, int M_Requisition_ID, String trxName) {
		super(ctx, M_Requisition_ID, trxName);
	}

	public TCS_MRequisition(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	
	@Override
	public boolean reActivateIt() {
		if (log.isLoggable(Level.INFO)) log.info("reActivateIt - " + toString());
		// Before reActivate
		m_processMsg = ModelValidationEngine.get().fireDocValidate(this,ModelValidator.TIMING_BEFORE_REACTIVATE);
		if (m_processMsg != null)
			return false;

		setProcessed(false);
//		if (! reverseCorrectIt())
		//	return false;
		boolean match = false;
		String sqlWhere = "M_Requisition.M_Requisition_ID="+getM_Requisition_ID()+" AND co.DocStatus IN ('CO','CL') ";
		match = new Query(getCtx(), MRequisition.Table_Name, sqlWhere, get_TrxName())
				.addJoinClause("JOIN M_RequisitionLine rl on rl.M_Requisition_ID=M_Requisition.M_Requisition_ID ")
				.addJoinClause("JOIN C_OrderLine col on col.M_RequisitionLine_ID=rl.M_RequisitionLine_ID ")
				.addJoinClause("JOIN C_Order co on co.C_Order_ID=col.C_Order_ID AND co.IsSOTrx='N' ")
				.match();
		
		if (match) 
			throw new AdempiereException("Active Purchase Order Referencing This Requisition Exist");
		

		// After reActivate
		m_processMsg = ModelValidationEngine.get().fireDocValidate(this,ModelValidator.TIMING_AFTER_REACTIVATE);
		if (m_processMsg != null)
			return false;

		return true;
	}
	
	@Override
	public boolean voidIt() {
		// Added code from core class MRequisition before void
		if (log.isLoggable(Level.INFO)) log.info("voidIt - " + toString());
		// Before Void
		m_processMsg = ModelValidationEngine.get().fireDocValidate(this,ModelValidator.TIMING_BEFORE_VOID);
		if (m_processMsg != null)
			return false;
		
		if (!closeIt())
			return false;
		//
		
		boolean match = false;
		String sqlWhere = "M_Requisition.M_Requisition_ID="+getM_Requisition_ID()+" AND co.DocStatus IN ('CO','CL') ";
		match = new Query(getCtx(), MRequisition.Table_Name, sqlWhere, get_TrxName())
				.addJoinClause("JOIN M_RequisitionLine rl on rl.M_Requisition_ID=M_Requisition.M_Requisition_ID ")
				.addJoinClause("JOIN C_OrderLine col on col.M_RequisitionLine_ID=rl.M_RequisitionLine_ID ")
				.addJoinClause("JOIN C_Order co on co.C_Order_ID=col.C_Order_ID AND co.IsSOTrx='N' ")
				.match();
		
		if (match) 
			throw new AdempiereException("Active Purchase Order Referencing This Requisition Exist");
		
		// After reActivate
//		return super.voidIt();
		
		// Added code from core class MRequisition after void
		m_processMsg = ModelValidationEngine.get().fireDocValidate(this,ModelValidator.TIMING_AFTER_VOID);
		if (m_processMsg != null)
			return false;

		return true;
		//
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
	 * @param M_RequisitionLine_ID
	 * @return temporary table for calculation
	 */
	protected int[] getMatchToReqLines(int M_RequisitionLine_ID){
		String where = "M_RequisitionLine_ID="+M_RequisitionLine_ID;
		int[] matchPRIDs = new Query(getCtx(), MMatchPR.Table_Name, where, get_TrxName())
			.setOnlyActiveRecords(true)
			.getIDs();
		
		return matchPRIDs;
	}
	
	public boolean hasMatchPR() {

		final String whereClause = I_M_MatchPR.COLUMNNAME_M_Requisition_ID + "=?";
		boolean match = new Query(getCtx(),X_M_MatchPR.Table_Name, whereClause, get_TrxName())
				.setParameters(get_ID())
				.match();

		return match;
	}
	
	@Override
	public String getProcessMsg()
	{
		//Returning m_processMsg from this child class avoiding null value
		return m_processMsg;
	}
}
