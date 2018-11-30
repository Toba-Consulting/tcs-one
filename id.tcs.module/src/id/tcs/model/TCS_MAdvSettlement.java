package id.tcs.model;

import java.io.File;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.util.Properties;
import java.util.logging.Level;

import org.compiere.model.MDocType;
import org.compiere.model.MPeriod;
import org.compiere.model.ModelValidationEngine;
import org.compiere.model.ModelValidator;
import org.compiere.model.Query;
import org.compiere.process.DocAction;
import org.compiere.process.DocumentEngine;
import org.compiere.util.Env;

public class TCS_MAdvSettlement extends X_TCS_AdvSettlement implements DocAction{

	/**	Process Message 			*/
	private String		m_processMsg = null;
	/**	Just Prepared Flag			*/
	private boolean		m_justPrepared = false;

	
	public TCS_MAdvSettlement(Properties ctx, int TCS_AdvSettlement_ID,String trxName) {
		super(ctx, TCS_AdvSettlement_ID, trxName);
	}

	public TCS_MAdvSettlement (Properties ctx, ResultSet rs, String trxName)
	{
		super (ctx, rs, trxName);
	}

	@Override
	public boolean processIt(String action) throws Exception {
		
		m_processMsg = null;
		DocumentEngine engine = new DocumentEngine (this, getDocStatus());
		return engine.processIt (action, getDocAction());//return false;
		
	}

	@Override
	public boolean unlockIt() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean invalidateIt() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String prepareIt() {


		if (log.isLoggable(Level.INFO)) log.info(toString());
		
		// Call model validators
		m_processMsg = ModelValidationEngine.get().fireDocValidate(this, ModelValidator.TIMING_BEFORE_PREPARE);
		if (m_processMsg != null)
		{
			return DocAction.STATUS_Invalid;
		}
		
		MPeriod.testPeriodOpen(getCtx(), getDateDoc(), MDocType.DOCBASETYPE_APPayment, getAD_Org_ID());
						
		// Call model validators
		m_processMsg = ModelValidationEngine.get().fireDocValidate(this, ModelValidator.TIMING_AFTER_PREPARE);
		if (m_processMsg != null)
		{
			return DocAction.STATUS_Invalid;
		}

		
		//	Done
		m_justPrepared = true;
		if (!DOCACTION_Complete.equals(getDocAction()))
			setDocAction(DOCACTION_Complete);
		return DocAction.STATUS_InProgress;
	
		}

	@Override
	public boolean approveIt() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean rejectIt() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String completeIt() {


		if (!m_justPrepared)
		{
			String status = prepareIt();
			m_justPrepared = false;
			if (!DocAction.STATUS_InProgress.equals(status))
				return status;
		}
		
		m_processMsg = ModelValidationEngine.get().fireDocValidate(this, ModelValidator.TIMING_BEFORE_COMPLETE);
		if (m_processMsg != null) {
			return DocAction.STATUS_Invalid;
		}

//Set Amount Reimburse/Returned = request.GrandTotal-settlement.GrandTotal 		
		
		BigDecimal requestAmt = Env.ZERO;
		BigDecimal settlementAmt = getGrandTotal();
		
		if (getTCS_AdvRequest_ID()!=0) {
			TCS_MAdvRequest request = new TCS_MAdvRequest(getCtx(), getTCS_AdvRequest_ID(), get_TrxName());
			requestAmt=request.getGrandTotal();
			request.setisSettled(true);
			request.saveEx();
		}
		
		BigDecimal difference = requestAmt.subtract(settlementAmt);
		
		if (difference.compareTo(Env.ZERO)>0) {
			setAmountReturned(difference);
		}
		else if (difference.compareTo(Env.ZERO)<0)
			setAmountReimbursed(difference.abs());
/*		
		//	Implicit Approval
		if (!isApproved())
			approveIt();
*/		
		if (log.isLoggable(Level.INFO)) log.info(toString());
		
//		User Validation
		m_processMsg = ModelValidationEngine.get().fireDocValidate(this, ModelValidator.TIMING_AFTER_COMPLETE);
		if (m_processMsg != null) {
			return DocAction.STATUS_Invalid;
		}
			//
		
		setProcessed(true);
		setDocAction(DOCACTION_Close);
		//
		//	User Validation
		m_processMsg = ModelValidationEngine.get().fireDocValidate(this, ModelValidator.TIMING_AFTER_COMPLETE);
		if (m_processMsg != null) {
			return DocAction.STATUS_Invalid;
		}
		
		//Set Sub Tab Processed='Y'
		String sqlDestSettlement="TCS_AdvSettlement_ID="+getTCS_AdvSettlement_ID();
		int [] destSettlementIDs = new Query(getCtx(), TCS_MDestSettlement.Table_Name, sqlDestSettlement, get_TrxName()).getIDs();
		TCS_MDestSettlement destSettlement;
		for (int destSettlementID : destSettlementIDs) {
			
			destSettlement = new TCS_MDestSettlement(getCtx(), destSettlementID, get_TrxName());
			String sqlSettlementLine="TCS_DestSettlement_ID="+destSettlementID;
			int [] settlementLineIDs = new Query(getCtx(), TCS_MAdvSettlementLine.Table_Name, sqlSettlementLine, get_TrxName()).getIDs();
			TCS_MAdvSettlementLine settlementLine;
			for (int settlementLineID : settlementLineIDs) {
				
				settlementLine = new TCS_MAdvSettlementLine(getCtx(), settlementLineID, get_TrxName());
				settlementLine.setProcessed(true);
				settlementLine.saveEx();
			}
			destSettlement.setProcessed(true);
			destSettlement.saveEx();
		}
		
		return DocAction.STATUS_Completed;
		
		
	}

	@Override
	public boolean voidIt() {
		// TODO Auto-generated method stub
		// Before Void
		m_processMsg = ModelValidationEngine.get().fireDocValidate(this,ModelValidator.TIMING_BEFORE_VOID);
		if (m_processMsg != null)
			return false;		
		
		String errmsg = ModelValidationEngine.get().fireDocValidate(this, ModelValidator.TIMING_AFTER_VOID);
		if (errmsg != null)
		{
			m_processMsg = errmsg;
			return false;
		}

		// finish
		setProcessed(true);
		setDocAction(DOCACTION_None);
		return true;
	}

	@Override
	public boolean closeIt() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean reverseCorrectIt() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean reverseAccrualIt() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean reActivateIt() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getSummary() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDocumentInfo() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public File createPDF() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getProcessMsg() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getDoc_User_ID() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public BigDecimal getApprovalAmt() {
		// TODO Auto-generated method stub
		return null;
	}
}
