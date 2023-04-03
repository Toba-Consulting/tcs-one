package org.compiere.model;

import java.sql.ResultSet;
import java.util.Properties;
import java.util.logging.Level;

import org.adempiere.exceptions.AdempiereException;
import org.compiere.model.MBankAccount;
import org.compiere.model.MBankStatement;
import org.compiere.model.MBankStatementLine;
import org.compiere.model.MFactAcct;
import org.compiere.model.MPayment;
import org.compiere.model.ModelValidationEngine;
import org.compiere.model.ModelValidator;
import org.compiere.model.Query;
import org.compiere.process.DocAction;
import org.compiere.process.DocOptions;

public class TCS_MBankStatement extends MBankStatement implements DocOptions{

	public TCS_MBankStatement(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	public TCS_MBankStatement(Properties ctx, int C_BankStatement_ID, String trxName) {
		super(ctx, C_BankStatement_ID, trxName);
	}

	public TCS_MBankStatement(MBankAccount account, boolean isManual) {
		super(account, isManual);
	}

	public TCS_MBankStatement(MBankAccount account) {
		super(account);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 2858066902877761708L;
	
	/**	Process Message 			*/
	private String		m_processMsg = null;
	
	/** 
	 * 	Re-activate
	 * 	@return false 
	 */
	public boolean reActivateIt()
	{
		if (log.isLoggable(Level.INFO)) log.info("reActivateIt - " + toString());
		// Before reActivate
		m_processMsg = ModelValidationEngine.get().fireDocValidate(this,ModelValidator.TIMING_BEFORE_REACTIVATE);
		if (m_processMsg != null)
			return false;		
		
		//	Std Period open?
		MPeriod.testPeriodOpen(getCtx(), getStatementDate(), MDocType.DOCBASETYPE_BankStatement, getAD_Org_ID());

		//@win TODO: add check if there are bank statement record with date after current one that is completed.
		StringBuilder whereClause = new StringBuilder("C_BankAccount_ID=")
				.append(getC_BankAccount_ID())
				.append(" AND DocStatus='CO' AND DateAcct > '")
				.append(getDateAcct()+"' ");
		
		boolean match = new Query(getCtx(), MBankStatement.Table_Name, whereClause.toString() , get_TrxName())
		.match();
		
		if (match) {
			//m_processMsg = "Error: Cannot Reactivate. Bank Statement Newer Than Current Record Exists";
			//return false;
			throw new AdempiereException("Error: Cannot Reactivate. Bank Statement Newer Than Current Record Exists");
		}
		//	Set Payment reconciled
		MBankStatementLine[] lines = getLines(false);
		for (int i = 0; i < lines.length; i++)
		{
			MBankStatementLine line = lines[i];
			if (line.getC_Payment_ID() != 0)
			{
				TCS_MPayment payment = new TCS_MPayment (getCtx(), line.getC_Payment_ID(), get_TrxName());
				payment.setIsReconciled(false);
				payment.saveEx(get_TrxName());

				//Temporary Comment Out
				//String sql = "DELETE FROM M_MatchBankCashStatement WHERE C_BankStatementLine_ID=" + line.getC_BankStatementLine_ID();
				//int no = DB.executeUpdate(sql, get_TrxName());
				//log.info("DELETED MatchBankCashStatement#"+no);
			}
		}
		//	Update Bank Account
		MBankAccount ba = getBankAccount();
		ba.load(get_TrxName());
		//BF 1933645
		ba.setCurrentBalance(ba.getCurrentBalance().subtract(getStatementDifference()));
		ba.saveEx(get_TrxName());
		
		//
		
		// After reActivate
		m_processMsg = ModelValidationEngine.get().fireDocValidate(this,ModelValidator.TIMING_AFTER_REACTIVATE);
		if (m_processMsg != null)
			return false;		

		//@PhieAlbert
		MFactAcct.deleteEx(MBankStatement.Table_ID, get_ID(), get_TrxName());
		//end @PhieAlbert
		setProcessed(false);
		setDocStatus(DocAction.STATUS_InProgress);
		//iqbal
		setPosted(false);
		//end
		setDocAction(DOCACTION_Complete);

		return true;
	}	//	reActivateIt
	
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
		if (log.isLoggable(Level.INFO)) log.info("completeIt - " + toString());
		
		//	Set Payment reconciled
		MBankStatementLine[] lines = getLines(false);
		for (int i = 0; i < lines.length; i++)
		{
			MBankStatementLine line = lines[i];
			if (line.getC_Payment_ID() != 0)
			{
				TCS_MPayment payment = new TCS_MPayment (getCtx(), line.getC_Payment_ID(), get_TrxName());
				payment.setIsReconciled(true);
				payment.saveEx(get_TrxName());
			}
		}
		//	Update Bank Account
		MBankAccount ba = getBankAccount();
		ba.load(get_TrxName());
		//BF 1933645
		ba.setCurrentBalance(ba.getCurrentBalance().add(getStatementDifference()));
		ba.saveEx(get_TrxName());
		
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
	
	@Override
	public int customizeValidActions(String docStatus, Object processing,
			String orderType, String isSOTrx, int AD_Table_ID,
			String[] docAction, String[] options, int index) {

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
			options[index++] = DocAction.ACTION_ReActivate;
		} else if (docStatus.equals(DocAction.STATUS_Invalid)) {
			options[index++] = DocAction.ACTION_Complete;
			options[index++] = DocAction.ACTION_Void;
		}

		return index;
	}
}
