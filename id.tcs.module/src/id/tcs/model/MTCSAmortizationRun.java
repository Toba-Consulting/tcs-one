package id.tcs.model;

import java.io.File;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;

import org.adempiere.exceptions.AdempiereException;
import org.compiere.model.MFactAcct;
import org.compiere.model.MPeriod;
import org.compiere.model.Query;
import org.compiere.process.DocAction;
import org.compiere.process.DocOptions;
import org.compiere.process.DocumentEngine;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;
import org.compiere.util.TimeUtil;

/**
 * @author Stephan
 * Amortization Run Model
 */
public class MTCSAmortizationRun extends X_TCS_AmortizationRun implements DocAction, DocOptions{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3003805287315371786L;

	public MTCSAmortizationRun(Properties ctx, int TCS_AmortizationRun_ID,
			String trxName) {
		super(ctx, TCS_AmortizationRun_ID, trxName);
	}
	
	public MTCSAmortizationRun(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/**	Process Message 			*/
	protected String		m_processMsg = null;
	
	@Override
	public boolean processIt(String action) {
		log.warning("Processing Action=" + action + " - DocStatus=" + getDocStatus() + " - DocAction=" + getDocAction());
		DocumentEngine engine = new DocumentEngine(this, getDocStatus());
		return engine.processIt(action, getDocAction());
	}

	@Override
	public boolean unlockIt() {
		return true;
	}

	@Override
	public boolean invalidateIt() {
		return true;
	}

	@Override
	public String prepareIt() {
		
		setC_DocType_ID(getC_DocTypeTarget_ID());
		
		return DocAction.STATUS_InProgress;
	}

	@Override
	public boolean approveIt() {
		return true;
	}

	@Override
	public boolean rejectIt() {
		return true;
	}

	@Override
	public String completeIt() {
		
		//	Just prepare
		if (DOCACTION_Prepare.equals(getDocAction()))
		{
			//setProcessed(false);
			return DocAction.STATUS_InProgress;
		}
		
		if(getGrandTotal().compareTo(Env.ZERO) == 0)
			m_processMsg = "Grand total can't be zero";
		
		m_processMsg = validateAmortization();
		
		if(m_processMsg != null)
			return DocAction.STATUS_Invalid;
		
		for (MTCSAmortizationLine line : getLines()) {
			line.setProcessed(true);
			line.saveEx();
		}
		
		setProcessed(true);	
		
		//  check if fully amortization
		checkAmortization();
		
		//
		setDocAction(DOCACTION_Close);
		return DocAction.STATUS_Completed;
	}

	@Override
	public boolean voidIt() {
		for (MTCSAmortizationLine line : getLines()) {
			//  make amortization line not processed
			line.setProcessed(true);
			removeMatch(line);
			line.saveEx();
		}

		/* globalqss - 2317928 - Reactivating/Voiding order must reset posted */
		MFactAcct.deleteEx(MTCSAmortizationRun.Table_ID, getTCS_AmortizationRun_ID(), get_TrxName());
		
		setDocAction(DOCACTION_None);
		setPosted(false);
		setProcessed(true);
		return true;
	}

	@Override
	public boolean closeIt() {
		return true;
	}

	@Override
	public boolean reverseCorrectIt() {
		/*
		if (log.isLoggable(Level.INFO)) log.info(toString());
		
		MTCSAmortizationRun reversal = reverse(false);
		if (reversal == null){
			m_processMsg = "Could not create reverse Amortization Run";
			return false;
		}
		
		if (m_processMsg != null)
			return false;
		*/
		return false;
	}

	@Override
	public boolean reverseAccrualIt() {
		/*
		if (log.isLoggable(Level.INFO)) log.info(toString());
		
		MTCSAmortizationRun reversal = reverse(true);
		if (reversal == null){
			m_processMsg = "Could not create reverse Amortization Run";
			return false;
		}
		
		if (m_processMsg != null)
			return false;
		
		*/
		return false;
	}

	@Override
	public boolean reActivateIt() {
		if (log.isLoggable(Level.INFO)) log.info(toString());
		
		//  delete fact acct record
		MFactAcct.deleteEx(MTCSAmortizationRun.Table_ID, getTCS_AmortizationRun_ID(), get_TrxName());
		
		for (MTCSAmortizationLine line : getLines()) {
			line.setProcessed(false);
			line.saveEx();
		}
		
		setPosted(false);
		setDocAction(DOCACTION_Complete);
		setProcessed(false);
		//  rollback line
		
		return true;
	}

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
		} else if (docStatus.equals(DocAction.STATUS_InProgress)) {
			options[index++] = DocAction.ACTION_Complete;
		} else if (docStatus.equals(DocAction.STATUS_Completed)) {
			options[index++] = DocAction.ACTION_ReActivate;
		} else if (docStatus.equals(DocAction.STATUS_Invalid)) {
			options[index++] = DocAction.ACTION_Complete;
		}

		return index;
	}
	
	@Override
	public String getSummary() {
		return null;
	}

	@Override
	public String getDocumentInfo() {
		return null;
	}

	@Override
	public File createPDF() {
		return null;
	}

	@Override
	public String getProcessMsg() {
		return m_processMsg;
	}

	@Override
	public int getDoc_User_ID() {
		return 0;
	}

	@Override
	public int getC_Currency_ID() {
		return super.getC_Currency_ID();
	}

	@Override
	public BigDecimal getApprovalAmt() {
		return null;
	}

	/**
	 * check if fully amortization or not
	 */
	public void checkAmortization(){
		
		for (MTCSAmortizationLine aline : getLines()) {
			MTCSAmortizationPlan aPlan = (MTCSAmortizationPlan) aline.getTCS_AmortizationPlan();
			int numberOfPeriod = aPlan.getAmortizationPeriod();
			int countOfPeriod = 0;
			for (MTCSAmortizationLine line : aPlan.getLines()) {
				if(line.getTCS_AmortizationRun_ID() > 0)
					countOfPeriod += 1;
			}
			
			if(numberOfPeriod == countOfPeriod){
				aPlan.setIsFullyAmortized(true);
				aPlan.saveEx();
			}
			
		}
		
	}

	/**
	 * get amortization line
	 * @return array of amortization line
	 */
	public MTCSAmortizationLine[] getLines(){
		
		final String whereClause = MTCSAmortizationRun.COLUMNNAME_TCS_AmortizationRun_ID + "=?";
		List<MTCSAmortizationLine> list = new Query(getCtx(), MTCSAmortizationLine.Table_Name, 
				whereClause, get_TrxName())
				.setParameters(get_ID())
				.setOnlyActiveRecords(true)
				.list();
		//
		MTCSAmortizationLine[] lines = new MTCSAmortizationLine[list.size()];
		list.toArray(lines);
		return lines;
	}
	
	/**
	 * reverse correct and reverse accrual
	 * @param isAccrual
	 * @return MTCSAmortizationRun
	 */
	/*
	private MTCSAmortizationRun reverse (boolean accrual){
		
		Timestamp reversalDateAcct = accrual ? Env.getContextAsDate(getCtx(), "#Date") : getDateAcct();
		if (reversalDateAcct == null) {
			reversalDateAcct = new Timestamp(System.currentTimeMillis());
		}
		Timestamp reversalDateDoc = accrual ? reversalDateAcct : getDateDoc();
		
		MTCSAmortizationRun reversal = copyFrom(this, reversalDateDoc, reversalDateAcct, getDocumentNo()+"^");
		reversal.setTotalLines(reversal.getTotalLines().negate());
		reversal.setGrandTotal(reversal.getGrandTotal().negate());
		reversal.setProcessed(true);
		reversal.saveEx();
		
		for (MTCSAmortizationLine line : this.getLines()) {
			//reversalLine.setAmtAcct(reversalLine.getAmtAcct().negate());
			//reversalLine.saveEx();
			
			//  make amortization line not processed
			line.setProcessed(false);
			line.saveEx();
			
			//removeMatch(line);
		}
		
		reversal.setReversal_ID(this.getTCS_AmortizationRun_ID());
		reversal.setProcessing (false);
		reversal.setDocStatus(DOCSTATUS_Reversed);
		reversal.setDocAction(DOCACTION_None);
		reversal.saveEx();
		
		if (!reversal.processIt(DocAction.ACTION_Complete))
		{
			m_processMsg = "Reversal ERROR: " + reversal.getProcessMsg();
			return null;
		}
		
		return reversal;
	}
	*/
	
	/**
	 * check previous period for unamortization
	 * @return error message if previous period not completed
	 */
	public String validateAmortization(){
		
		for (MTCSAmortizationLine line : getLines()) {
			
			Timestamp prevDate = line.getC_Period().getStartDate();
			prevDate = TimeUtil.addDays(prevDate, -1);
			MPeriod prevPeriod = MPeriod.get(getCtx(), prevDate, 0, get_TrxName());
			
			StringBuilder sb = new StringBuilder();
			sb.append("TCS_AmortizationRun_ID IS NOT NULL AND Processed = 'N' "
					+ "AND TCS_AmortizationPlan_ID = ? AND C_Period_ID = ?");
			
			boolean match = new Query(getCtx(), MTCSAmortizationLine.Table_Name, sb.toString(), get_TrxName())
				.setClient_ID()
				.setOnlyActiveRecords(true)
				.setParameters(new Object[]{
						line.getTCS_AmortizationPlan_ID(), prevPeriod.get_ID()})
				.match();
			
			if(match)
				m_processMsg = "Previous Period Not Completed";
		}
		
		return m_processMsg;
	}
	
	/**
	 * remove match amortization line
	 */
	public void removeMatch(MTCSAmortizationLine line){
		
		StringBuilder sb = new StringBuilder();
		
		sb.append("UPDATE TCS_AmortizationLine SET TCS_AmortizationRun_ID=NULL WHERE TCS_AmortizationLine_ID=?");
		int no = DB.executeUpdate(sb.toString(), line.get_ID(), get_TrxName());
		log.info("UPDATED AmortizationLine#"+no);
	}
	
}
