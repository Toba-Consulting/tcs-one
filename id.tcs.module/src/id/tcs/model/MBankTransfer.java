package id.tcs.model;

import java.io.File;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Properties;
import java.util.logging.Level;

import org.adempiere.exceptions.AdempiereException;
import org.adempiere.exceptions.PeriodClosedException;
import org.compiere.model.MAllocationHdr;
import org.compiere.model.MAllocationLine;
import org.compiere.model.MBPartner;
import org.compiere.model.MBankAccount;
import org.compiere.model.MPayment;
import org.compiere.model.MPeriod;
import org.compiere.model.ModelValidationEngine;
import org.compiere.model.ModelValidator;
import org.compiere.model.X_C_DocType;
import org.compiere.process.DocAction;
import org.compiere.process.DocOptions;
import org.compiere.process.DocumentEngine;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;

public class MBankTransfer extends X_C_BankTransfer implements DocAction, DocOptions {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1321756785631494309L;

	public static String REVERSE_INDICATOR = "^";
	
	public MBankTransfer(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	public MBankTransfer(Properties ctx, int C_BankTransfer_ID, String trxName) {
		super(ctx, C_BankTransfer_ID, trxName);
	}
	/**************************************************************************
	 * 	Before Save
	 *	@param newRecord new
	 *	@return save
	 */
	protected boolean beforeSave (boolean newRecord)
	{
//		BigDecimal amtTo = getAmountTo() ;
//		BigDecimal amtFrom = getAmountFrom() ;
//		
//		BigDecimal divideRate = amtTo.divide(amtFrom, 2, RoundingMode.HALF_UP);
//		BigDecimal multiplyRate = amtFrom.divide(amtTo, 2, RoundingMode.HALF_UP);
//
//		setDivideRate(divideRate);
//		setMultiplyRate(multiplyRate);

		return true;
	}

	@Override
	public int customizeValidActions(String docStatus, Object processing, String orderType, String isSOTrx,
			int AD_Table_ID, String[] docAction, String[] options, int index) {
		// TODO Auto-generated method stub
		
		//if docstatus = DR,IN, IP allowed docaction CO, VO, PR
		//if docstatus = CO allowed docaction RE
		//if docstatus = VO, RE, allowed docaction none
		for (int i = 0; i < options.length; i++) {
			options[i] = null;
		}

		index = 0;

		if (docStatus.equals(DocAction.STATUS_Drafted) 
				|| docStatus.equals(DocAction.STATUS_InProgress) 
				|| docStatus.equals(DocAction.STATUS_Invalid)) {
			options[index++] = DocAction.ACTION_Complete;
			options[index++] = DocAction.ACTION_Void;
			options[index++] = DocAction.ACTION_Prepare;
		} else if (docStatus.equals(DocAction.STATUS_Completed)) {
			options[index++] = DocAction.ACTION_Reverse_Accrual;
			options[index++] = DocAction.ACTION_Reverse_Correct;
		} else if (docStatus.equals(DocAction.STATUS_Voided) 
				|| docStatus.equals(DocAction.STATUS_Reversed)) {
			options[index++] = DocAction.ACTION_None;
		}

		return index;
	}

	@Override
	public boolean processIt(String processAction) throws Exception {
		m_processMsg = null;
		DocumentEngine engine = new DocumentEngine(this, getDocStatus());
		return engine.processIt(processAction, getDocAction());
	}// process
	
	private String m_processMsg = null;
	
	private boolean m_justPrepared = false;

	@Override
	public boolean unlockIt() {
		// TODO Auto-generated method stub
		if (log.isLoggable(Level.INFO)) log.info(toString());
		setProcessing(false);
		return true;
	}// unlockit

	@Override
	public boolean invalidateIt() {
		// TODO Auto-generated method stub
		if (log.isLoggable(Level.INFO)) log.info(toString());
		setDocAction(DOCACTION_Prepare);
		return true;
	}

	@Override
	public String prepareIt() {
		// TODO Auto-generated method stub
		if (log.isLoggable(Level.INFO)) log.info(toString());
		m_processMsg = ModelValidationEngine.get().fireDocValidate(this, ModelValidator.TIMING_BEFORE_PREPARE);
		if(m_processMsg != null)
			return DocAction.STATUS_Invalid;
		
		int C_BankTransfer_ID = getC_BankTransfer_ID();
		//@win note
		//all validation code here
		//01 - currency from = currency bankaccountfrom
		//02 - currency to = currency bankaccountto
		//03 - ad org tidak boleh *, ad org harus sama dengan org bank account from
		//04 - if ishastransferfee=N, set c_charge_id, chargeamt, charge_currency_id, charge_payment_ID to null
		MBankTransfer bankTransfer = new MBankTransfer(getCtx(), C_BankTransfer_ID, get_TrxName());
		MBankAccount bankAccountFrom = new MBankAccount(getCtx(), bankTransfer.getC_BankAccount_From_ID(), get_TrxName());
		MBankAccount bankAccountTo = new MBankAccount(getCtx(), bankTransfer.getC_BankAccount_To_ID(), get_TrxName());
		
		if(bankTransfer.getC_Currency_From_ID() != bankAccountFrom.getC_Currency_ID())
		{
			m_processMsg = "Currency From does not match with Currency from Bank Account From";
			return DocAction.STATUS_Invalid;
		}
		
		if(bankTransfer.getC_Currency_To_ID() != bankAccountTo.getC_Currency_ID())
		{
			m_processMsg = "Currency To does not match with Currency from Bank Account To";
			return DocAction.STATUS_Invalid;
		}
		
		if(bankTransfer.getAD_Org_ID() == 0)
		{
			m_processMsg = "Organization cannot be *";
			return DocAction.STATUS_Invalid;
		}
			 
		if(!isHasTransferFee())	
		{
//			setC_Charge_ID(null);
			
			setChargeAmt(null);
			
		}
			
		m_processMsg = ModelValidationEngine.get().fireDocValidate(this, ModelValidator.TIMING_AFTER_PREPARE);
		if (m_processMsg != null)
		{
			m_processMsg = "@PeriodClosed@";
			return DocAction.STATUS_Invalid;
		}

		
		m_justPrepared = true;

		if (!DOCACTION_Complete.equals(getDocAction()))
			setDocAction(DOCACTION_Complete);

		return DocAction.STATUS_InProgress;
	}

	@Override
	public boolean approveIt() {
		// TODO Auto-generated method stub
		if (log.isLoggable(Level.INFO)) log.info(toString());
		setProcessed(true);
		return true;
	}

	@Override
	public boolean rejectIt() {
		// TODO Auto-generated method stub
		if (log.isLoggable(Level.INFO)) log.info(toString());
		setProcessed(false);
		return true;
	}

	@Override
	public String completeIt() {
		// TODO Auto-generated method stub
		//		Re-Check
			if (!m_justPrepared)
			{
				String status = prepareIt();
				m_justPrepared = false;
				if (!DocAction.STATUS_InProgress.equals(status))
					return status;
			}
			
			m_processMsg = ModelValidationEngine.get().fireDocValidate(this, ModelValidator.TIMING_BEFORE_COMPLETE);
			if(m_processMsg != null)
				return DocAction.STATUS_Invalid;
	
			//code complete
			//create payment from and complete
			//create payment to and complete
			//allocate payment from to payment to and complete
			//if converted amt payment from > converted amt payment to, allocate payment from to charge realized currency loss
			//else allocate payment-to to charge realized currency gain
			
			//transfer fee
			//Charge_Currency_ID
			//create payment for transfer fee and complete
			
			//setdocstatus and docaction
			
			MBankTransfer bankTransfer = new MBankTransfer(getCtx(), getC_BankTransfer_ID(), get_TrxName());
			MPayment paymentFrom = new MPayment(getCtx(), 0, get_TrxName());
			
			paymentFrom.setAD_Org_ID(getAD_Org_ID());
			paymentFrom.setTenderType(MPayment.TENDERTYPE_DirectDeposit);
			paymentFrom.setC_BankAccount_ID(getC_BankAccount_From_ID());
			paymentFrom.setC_BPartner_ID(getC_BPartner_ID());
			paymentFrom.setC_DocType_ID(false); 
			paymentFrom.setC_Currency_ID(getC_Currency_From_ID());
			if(bankTransfer.getDescription() == null) {
				paymentFrom.setDescription("Generated From Bank Transfer " + getDocumentNo());								
			}
			else {
				paymentFrom.setDescription(getDescription() + " | Generated From Bank Transfer " + getDocumentNo());
			}
			paymentFrom.setDateAcct(getDateAcct());
			paymentFrom.setDateTrx(getDateAcct()); 
			paymentFrom.setPayAmt(getPayAmtFrom());
			paymentFrom.setChargeAmt(getChargeAmt());
			paymentFrom.setC_ConversionType_ID(getC_ConversionType_ID());
			paymentFrom.saveEx();
			
			if(!paymentFrom.processIt(MPayment.DOCACTION_Complete)) {
				log.warning("Payment Process Failed: " + paymentFrom + " - " + paymentFrom.getProcessMsg());
				throw new IllegalStateException("Payment Process Failed: " + paymentFrom + " - " + paymentFrom.getProcessMsg());
			}
			paymentFrom.saveEx();
			//payment from
			
			MPayment paymentTo = new MPayment(getCtx(), 0, get_TrxName());
			
			paymentTo.setAD_Org_ID(getAD_Org_ID());
			paymentTo.setTenderType(MPayment.TENDERTYPE_DirectDeposit);
			paymentTo.setC_BankAccount_ID(getC_BankAccount_To_ID());
			paymentTo.setC_BPartner_ID(getC_BPartner_ID());
			paymentTo.setC_DocType_ID(true);
			paymentTo.setC_Currency_ID(getC_Currency_To_ID());
			if(bankTransfer.getDescription() == null) {
				paymentTo.setDescription("Generated From Bank Transfer " + getDocumentNo());								
			}
			else {
				paymentTo.setDescription(getDescription() + " | Generated From Bank Transfer " + getDocumentNo());
			}
			paymentTo.setDateAcct(getDateAcct());
			paymentTo.setDateTrx(getDateAcct());
			paymentTo.setPayAmt(getPayAmtTo());
			paymentTo.setChargeAmt(getChargeAmt());
			paymentTo.setC_ConversionType_ID(getC_ConversionType_ID());
			paymentTo.saveEx();
			
			if(!paymentTo.processIt(MPayment.DOCACTION_Complete)) {
				log.warning("Payment Process Failed: " + paymentTo+ " - " + paymentTo.getProcessMsg());
				throw new IllegalStateException("Payment Process Failed: " + paymentTo + " - " + paymentTo.getProcessMsg());
			}
			paymentTo.saveEx();
			//paymentTo
			
			//allocation
			MAllocationHdr allocHdr = new MAllocationHdr(getCtx(), 0, get_TrxName());
			//MAcctSchema schema = new MAcctSchema(getCtx(), bankTransfer.get_ValueAsInt("C_AcctSchema_ID"), get_TrxName());
			
			String sqlDocBaseTypeAlloc = 
						"SELECT C_DocType_ID FROM C_DOCTYPE" 
					  + " WHERE DocBaseType = 'CMA'";
			
			String sqlDivideRate =
					"SELECT dividerate FROM C_Conversion_Rate"
				  + " WHERE C_Currency_ID = " + bankTransfer.getC_Currency_From_ID() 
				  + " AND C_Currency_ID_To = " + bankTransfer.getC_Currency_To_ID()
				  + " AND ValidFrom < '" + bankTransfer.getDateAcct()
				  + "' AND ValidTo >= '" + bankTransfer.getDateAcct() + "'"
				  + " AND C_ConversionType_ID = " + bankTransfer.getC_ConversionType_ID();
			
			String sqlMultiplyRate =
					"SELECT multiplyrate FROM C_Conversion_Rate"
				  + " WHERE C_Currency_ID = " + bankTransfer.getC_Currency_From_ID() 
				  + " AND C_Currency_ID_To = " + bankTransfer.getC_Currency_To_ID()
				  + " AND ValidFrom < '" + bankTransfer.getDateAcct()
				  + "' AND ValidTo >= '" + bankTransfer.getDateAcct() + "'"
				  + " AND C_ConversionType_ID = " + bankTransfer.getC_ConversionType_ID();			

			int DocBaseTypeAllocID = DB.getSQLValue(get_TrxName(), sqlDocBaseTypeAlloc);
			BigDecimal divideRate = DB.getSQLValueBD(get_TrxName(), sqlDivideRate);
			BigDecimal multiplyRate = DB.getSQLValueBD(get_TrxName(), sqlMultiplyRate);
			
			System.out.println(multiplyRate);
			
			allocHdr.setAD_Org_ID(bankTransfer.getAD_Org_ID());
			allocHdr.setC_DocType_ID(DocBaseTypeAllocID);
			allocHdr.setDateAcct(bankTransfer.getDateAcct());
			allocHdr.setDateTrx(bankTransfer.getDateAcct());
			allocHdr.setC_Currency_ID(getC_Currency_ID());
			if(bankTransfer.getDescription() == null) {
				allocHdr.setDescription("Generated From Bank Transfer " + bankTransfer.getDocumentNo());								
			}
			else {
				allocHdr.setDescription(bankTransfer.getDescription() + " | Generated From Bank Transfer " + bankTransfer.getDocumentNo());
			}
			allocHdr.saveEx();
			
			MAllocationLine alloclineAP = new MAllocationLine(allocHdr);
			
			alloclineAP.setAD_Org_ID(allocHdr.getAD_Org_ID());
			alloclineAP.setC_BPartner_ID(paymentFrom.getC_BPartner_ID());
			alloclineAP.setC_Payment_ID(paymentFrom.getC_Payment_ID());
			if(!paymentFrom.get_Value("C_Currency_ID").equals(allocHdr.getC_Currency_ID()))
				alloclineAP.setAmount(getAmountFrom().negate());
			else
				alloclineAP.setAmount(getAmountFrom().negate());
			alloclineAP.saveEx();
			
			MAllocationLine alloclineAR = new MAllocationLine(allocHdr);
			
			alloclineAR.setAD_Org_ID(allocHdr.getAD_Org_ID());
			alloclineAR.setC_BPartner_ID(paymentTo.getC_BPartner_ID());
			alloclineAR.setC_Payment_ID(paymentTo.getC_Payment_ID());
			if(!paymentTo.get_Value("C_Currency_ID").equals(getC_Currency_ID()))
				alloclineAR.setAmount(getPayAmtFrom().subtract(bankTransfer.getChargeAmt()));
			else
				alloclineAR.setAmount(getPayAmtTo());
			bankTransfer.setC_Payment_To_ID(paymentTo.getC_Payment_ID());
			alloclineAR.saveEx();
			
			if(bankTransfer.get_Value("C_Charge_ID") != null){
				MAllocationLine alloclineChr = new MAllocationLine(allocHdr);
				
				alloclineChr.setAD_Org_ID(allocHdr.getAD_Org_ID());
				alloclineChr.setC_BPartner_ID(getC_BPartner_ID());
				alloclineChr.setC_Charge_ID(getC_Charge_ID());
				if(bankTransfer.getTransferFeeType().equals(MBankTransfer.TRANSFERFEETYPE_ChargeOnBankFrom)) {
					alloclineChr.setAmount(getChargeAmt().negate());
					alloclineChr.setC_Payment_ID(getC_Payment_From_ID());
				}
				else {
					alloclineChr.setAmount(getChargeAmt());
					alloclineChr.setC_Payment_ID(getC_Payment_To_ID());
				}
				alloclineChr.saveEx();
			
				MAllocationLine alloclineChrGap = new MAllocationLine(allocHdr);
//				MCharge charge2 = new MCharge(getCtx(), 1000761, get_TrxName());
				alloclineChrGap.setAD_Org_ID(allocHdr.getAD_Org_ID());
				alloclineChrGap.setC_BPartner_ID(getC_BPartner_ID());
//				alloclineChrGap.setC_Charge_ID(charge2.getC_Charge_ID());
				if(bankTransfer.getTransferFeeType().equals(MBankTransfer.TRANSFERFEETYPE_ChargeOnBankFrom)) {
//					charge2.setChargeAmt(alloclineAP.getAmount().add(bankTransfer.getPayAmtTo().multiply(divideRate)));
//					alloclineChrGap.setAmount(charge2.getChargeAmt());
					alloclineChrGap.setC_Payment_ID(getC_Payment_To_ID());
				}
				else {
//					charge2.setChargeAmt(bankTransfer.getPayAmtTo().multiply(divideRate).add(alloclineAP.getAmount()));
//					alloclineChrGap.setAmount(charge2.getChargeAmt());
					alloclineChrGap.setC_Payment_ID(getC_Payment_To_ID());
				}
				alloclineChrGap.saveEx();
				
		}	
			
			allocHdr.processIt(DocAction.ACTION_Complete);

			
			
			String valid = ModelValidationEngine.get().fireDocValidate(this, ModelValidator.TIMING_AFTER_COMPLETE);
			if (valid != null)
			{
				m_processMsg = valid;
				return DocAction.STATUS_Invalid;
			}
			
			setProcessed(true);
			setDocStatus(DOCSTATUS_Completed);
			setDocAction(DOCACTION_Close);
			return DocAction.STATUS_Completed;
	}

	@Override
	public boolean voidIt() {
		//@win note
		//validate docstatus harus DR, IN, IP
		//set payamtfrom, payamtto, chargeamt=0
		//set description = original description + [VOIDED], payamt from = xxx, payamtto = yyy, chargeamt= zzz
		//set docstatus and docaction
	
		if(getDocStatus().equals(DOCSTATUS_Drafted)
				|| getDocStatus().equals(DOCSTATUS_Invalid)
				|| getDocStatus().equals(DOCSTATUS_InProgress))
		{
			m_processMsg = ModelValidationEngine.get().fireDocValidate(this,ModelValidator.TIMING_BEFORE_VOID);
			if (m_processMsg != null)
				return false;
			
			setDescription(getDescription() + "[VOIDED], Pay Amount From = " + getPayAmtFrom() 
				+ ", Pay Amount To = " + getPayAmtTo() + ", Charge Amount = " + getChargeAmt());
			setPayAmtFrom(Env.ZERO);
			setPayAmtTo(Env.ZERO);
			setChargeAmt(Env.ZERO);
			setDocStatus(DOCSTATUS_Voided);
		}
		else
		{
			boolean accrual = false;
			try 
			{
				MPeriod.testPeriodOpen(getCtx(), getDateAcct(), getC_DocType_ID(), getAD_Org_ID());
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
		
		
		m_processMsg = ModelValidationEngine.get().fireDocValidate(this,ModelValidator.TIMING_AFTER_VOID);
		if (m_processMsg != null)
			return false;
		
		setDocAction(DOCACTION_None);
		return true;
	}

	@Override
	public boolean closeIt() {
		return false;
	}

	private StringBuilder reverse(boolean accrual) {
//		if() - voidonlinepayment
		
		// Std Period open?
		Timestamp dateAcct = accrual ? Env.getContextAsDate(getCtx(), "#Date") : getDateAcct();
		if (dateAcct == null) {
			dateAcct = new Timestamp(System.currentTimeMillis());
		}
		MPeriod.testPeriodOpen(getCtx(), dateAcct, getC_DocType_ID(), getAD_Org_ID());
		
		// create reversal
		MBankTransfer reversal = new MBankTransfer(getCtx(), 0, get_TrxName());
		copyValues(this, reversal);
		reversal.setClientOrg(this);
//		reversal.setC_Invoice_ID(0);
		reversal.setDateAcct(dateAcct);
		//
		reversal.setDocumentNo(getDocumentNo() + REVERSE_INDICATOR);
		reversal.setDocStatus(DOCSTATUS_Drafted);
		reversal.setDocAction(DOCACTION_Complete);
		//
		reversal.setPayAmtFrom(getPayAmtFrom().negate());
		reversal.setPayAmtTo(getPayAmtTo().negate());
		reversal.setAmountFrom(getAmountFrom().negate());
		reversal.setAmountTo(getAmountTo().negate());
		reversal.setChargeAmt(getChargeAmt().negate());
		//
		reversal.setIsCanceled(true);
		reversal.setProcessing(false);
		reversal.setProcessed(false);
		reversal.setPosted(false);
		reversal.setDescription(getDescription());
		reversal.addDescription("{->" + getDocumentNo() + ")");
		//
//		reversal.setReversal_ID(getC_BankTransfer_ID()); reversal id nya gaada
		reversal.saveEx(get_TrxName());
		// post reversal
		try {
			if (!reversal.processIt(DOCACTION_Complete))
			{
				m_processMsg = "Reversal Error: " + reversal.getProcessMsg();
				return null;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		reversal.closeIt();
		reversal.setDocStatus(DOCSTATUS_Reversed);
		reversal.setDocAction(DOCACTION_None);
		reversal.saveEx(get_TrxName());
	

		
		StringBuilder info = new StringBuilder(reversal.getDocumentNo());

		//	Create automatic Allocation
		MAllocationHdr alloc = new MAllocationHdr (getCtx(), false, 
			getDateAcct(), 
			getC_Currency_ID(),
			Msg.translate(getCtx(), "C_BankTransfer_ID")	+ ": " + reversal.getDocumentNo(), get_TrxName());
		alloc.setAD_Org_ID(getAD_Org_ID());
		alloc.setDateAcct(dateAcct); // dateAcct variable already take into account the accrual parameter
		alloc.saveEx(get_TrxName());

		//	Original Allocation
		MAllocationLine aLine = new MAllocationLine (alloc);
		aLine.setDocInfo(getC_BPartner_ID(), 0, 0);
		if (!aLine.save(get_TrxName()))
			log.warning("Automatic allocation - line not saved");
		//	Reversal Allocation
		aLine = new MAllocationLine (alloc);
		aLine.setDocInfo(reversal.getC_BPartner_ID(), 0, 0);
//		aLine.setPaymentInfo(reversal.getC_Payment_ID(), 0);
		if (!aLine.save(get_TrxName()))
			log.warning("Automatic allocation - reversal line not saved");
		
		// added AdempiereException by zuhri
		if (!alloc.processIt(DocAction.ACTION_Complete))
			throw new AdempiereException("Failed when processing document - " + alloc.getProcessMsg());
		// end added
		alloc.saveEx(get_TrxName());
		//			
		info.append(" - @C_AllocationHdr_ID@: ").append(alloc.getDocumentNo());
		
		//	Update BPartner
		if (getC_BPartner_ID() != 0)
		{
			MBPartner bp = new MBPartner (getCtx(), getC_BPartner_ID(), get_TrxName());
			bp.setTotalOpenBalance();
			bp.saveEx(get_TrxName());
		}
		
		return info;
	}
	
	public void addDescription (String description)
	{
		String desc = getDescription();
		if(desc == null)
			setDescription(description);
		else
			setDescription(desc + " | " + description);
	}
	
	@Override
	public boolean reverseCorrectIt() {
		if(log.isLoggable(Level.INFO)) log.info(toString());
		//before reverseCorrect
		m_processMsg = ModelValidationEngine.get().fireDocValidate(this, ModelValidator.TIMING_BEFORE_REVERSECORRECT);
		if(m_processMsg != null)
			return false;
		
		StringBuilder info = reverse(false);
		if (info == null)
			return false;
		
		//after reverseCorrect
		m_processMsg = ModelValidationEngine.get().fireDocValidate(this, ModelValidator.TIMING_AFTER_REVERSECORRECT);
		if(m_processMsg != null)
			return false;
		
		m_processMsg = info.toString();
		return true;
	}

	@Override
	public boolean reverseAccrualIt() {
		if (log.isLoggable(Level.INFO)) log.info(toString());
		//before reverseAccrual
		m_processMsg = ModelValidationEngine.get().fireDocValidate(this, ModelValidator.TIMING_BEFORE_REVERSEACCRUAL);
		if(m_processMsg != null)
			return false;

		StringBuilder info = reverse(true);
		if (info == null)
			return false;

		//After reverseAccrual
		m_processMsg = ModelValidationEngine.get().fireDocValidate(this, ModelValidator.TIMING_AFTER_REVERSEACCRUAL);
		if (m_processMsg != null)
			return false;
		
		m_processMsg = info.toString();
		return true;
	}

	@Override
	public boolean reActivateIt() {
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
	public int getC_Currency_ID() {
		// TODO Auto-generated method stub
		//@win note.. get from context variable.. from default acct schema
		int C_Currency_ID = Env.getContextAsInt(getCtx(), "$C_Currency_ID");
		return C_Currency_ID;
	}

	@Override
	public BigDecimal getApprovalAmt() {
		return this.getPayAmtFrom();
	}

}