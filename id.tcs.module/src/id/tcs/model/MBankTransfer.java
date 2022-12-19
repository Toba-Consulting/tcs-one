package id.tcs.model;

import java.io.File;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;

import org.compiere.model.MAllocationHdr;
import org.compiere.model.MAllocationLine;
import org.compiere.model.MBankAccount;
import org.compiere.model.MPayment;
import org.compiere.model.MPeriod;
import org.compiere.model.MSysConfig;
import org.compiere.model.ModelValidationEngine;
import org.compiere.model.ModelValidator;
import org.compiere.model.PO;
import org.compiere.model.Query;
import org.compiere.model.TCS_MPayment;
import org.compiere.process.DocAction;
import org.compiere.process.DocOptions;
import org.compiere.process.DocumentEngine;
import org.compiere.util.DB;
import org.compiere.util.Env;

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
		}
		else if (docStatus.equals(DocAction.STATUS_Completed)) {
			options[index++] = DocAction.ACTION_Void;
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
		if (log.isLoggable(Level.INFO)) log.info(toString());
		setProcessing(false);
		return true;
	}// unlockit

	@Override
	public boolean invalidateIt() {
		if (log.isLoggable(Level.INFO)) log.info(toString());
		setDocAction(DOCACTION_Prepare);
		return true;
	}

	@Override
	public String prepareIt() {
		if (log.isLoggable(Level.INFO)) log.info(toString());
		m_processMsg = ModelValidationEngine.get().fireDocValidate(this, ModelValidator.TIMING_BEFORE_PREPARE);
		if(m_processMsg != null)
			return DocAction.STATUS_Invalid;
		
		//@win note
		//all validation code here
		//01 - currency from = currency bankaccountfrom
		//02 - currency to = currency bankaccountto
		//03 - ad org tidak boleh *, ad org harus sama dengan org bank account from
		//04 - if ishastransferfee=N, set c_charge_id, chargeamt, charge_currency_id, charge_payment_ID to null
		
		if (getC_BankAccount_From()==null)
			return "Error.. Bank Account From is mandatory";
		
		if (getC_BankAccount_To()==null)
			return "Error.. Bank Account To is mandatory";
		
		if(getC_Currency_From() == null)
			return "Error.. Currency From is mandatory";
		
		if(getC_Currency_To() == null)
			return "Error.. Currency To is mandatory";
		
		if (getAmountFrom()==null)
			return "Error.. Amount From is mandatory";
		
		if (getAmountTo()==null)
			return "Error.. Amount To is mandatory";
		
		MBankAccount bankAccountFrom = new MBankAccount(getCtx(), getC_BankAccount_From_ID(), get_TrxName());
		MBankAccount bankAccountTo = new MBankAccount(getCtx(), getC_BankAccount_To_ID(), get_TrxName());
		
		if(getC_Currency_From_ID() != bankAccountFrom.getC_Currency_ID())
		{
			m_processMsg = "Currency From does not match with Currency from Bank Account From";
			return DocAction.STATUS_Invalid;
		}
		
		if(getC_Currency_To_ID() != bankAccountTo.getC_Currency_ID())
		{
			m_processMsg = "Currency To does not match with Currency from Bank Account To";
			return DocAction.STATUS_Invalid;
		}
		
		if(getAD_Org_ID() == 0)
		{
			m_processMsg = "Organization cannot be *";
			return DocAction.STATUS_Invalid;
		}
			 
		if(!isHasTransferFee())	
		{
			setC_Charge_ID(0);
			setChargeAmt(null);
			setTransferFeeType(null);
		} else {
			if (getC_Charge()==null)
				return "Error, transfer charge is mandatory";
			
			if (getChargeAmt()==null)
				return "Error, transfer amount is mandatory";
			
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
		if (log.isLoggable(Level.INFO)) log.info(toString());
		setProcessed(true);
		return true;
	}

	@Override
	public boolean rejectIt() {
		if (log.isLoggable(Level.INFO)) log.info(toString());
		setProcessed(false);
		return true;
	}

	@Override
	public String completeIt() {
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
			int funcCurrencyID = Env.getContextAsInt(getCtx(), "$C_Currency_ID");
			
			TCS_MPayment paymentFrom = new TCS_MPayment(getCtx(), 0, get_TrxName());			
			paymentFrom.setAD_Org_ID(getC_BankAccount_From().getAD_Org_ID());
			paymentFrom.setTenderType(MPayment.TENDERTYPE_Account);
			paymentFrom.setC_BankAccount_ID(getC_BankAccount_From_ID());
			paymentFrom.setC_BPartner_ID(getC_BPartner_ID());
			paymentFrom.setC_DocType_ID(false, false); 
			paymentFrom.setC_Currency_ID(getC_Currency_From_ID());
			paymentFrom.set_ValueOfColumn("C_BankTransfer_ID", getC_BankTransfer_ID());
			if(getDescription() == null) {
				paymentFrom.setDescription("Generated From Bank Transfer " + getDocumentNo());								
			}
			else {
				paymentFrom.setDescription(getDescription() + " | Generated From Bank Transfer " + getDocumentNo());
			}
			paymentFrom.setDateAcct(getDateAcct());
			paymentFrom.setDateTrx(getDateAcct()); 

			if(!get_ValueAsBoolean("isHasTransferFee"))
				paymentFrom.setPayAmt(getPayAmtFrom());
			else if(getTransferFeeType().equals("F") && get_ValueAsBoolean("isHasTransferFee"))
				paymentFrom.setPayAmt(getPayAmtFrom().add(getChargeAmt()));
			else
				paymentFrom.setPayAmt(getPayAmtFrom());
			
			
			if (funcCurrencyID!=getC_Currency_From_ID()) {
				paymentFrom.setC_ConversionType_ID(getC_ConversionType_ID());
			}
			paymentFrom.saveEx();
			
			if(!paymentFrom.processIt(MPayment.DOCACTION_Complete)) {
				log.warning("Payment Process Failed: " + paymentFrom + " - " + paymentFrom.getProcessMsg());
				throw new IllegalStateException("Payment Process Failed: " + paymentFrom + " - " + paymentFrom.getProcessMsg());
			}
			paymentFrom.saveEx();
			//payment from
			
			TCS_MPayment paymentTo = new TCS_MPayment(getCtx(), 0, get_TrxName());
			paymentTo.setAD_Org_ID(getC_BankAccount_To().getAD_Org_ID());
			paymentTo.setTenderType(MPayment.TENDERTYPE_Account);
			paymentTo.setC_BankAccount_ID(getC_BankAccount_To_ID());
			paymentTo.setC_BPartner_ID(getC_BPartner_ID());
			paymentTo.setC_DocType_ID(true, false);
			paymentTo.setC_Currency_ID(getC_Currency_To_ID());
			paymentTo.set_ValueOfColumn("C_BankTransfer_ID", getC_BankTransfer_ID());
			if(getDescription() == null) {
				paymentTo.setDescription("Generated From Bank Transfer " + getDocumentNo());								
			}
			else {
				paymentTo.setDescription(getDescription() + " | Generated From Bank Transfer " + getDocumentNo());
			}
			paymentTo.setDateAcct(getDateAcct());
			paymentTo.setDateTrx(getDateAcct());
			if(!get_ValueAsBoolean("isHasTransferFee"))
				paymentTo.setPayAmt(getPayAmtTo());
			else
				paymentTo.setPayAmt(getPayAmtTo());
			
			if (funcCurrencyID!=getC_Currency_From_ID()) {
				paymentTo.setC_ConversionType_ID(getC_ConversionType_ID());
			}
			paymentTo.saveEx();
			
			if(!paymentTo.processIt(MPayment.DOCACTION_Complete)) {
				log.warning("Payment Process Failed: " + paymentTo+ " - " + paymentTo.getProcessMsg());
				throw new IllegalStateException("Payment Process Failed: " + paymentTo + " - " + paymentTo.getProcessMsg());
			}
			paymentTo.saveEx();
			//paymentTo
			
			int C_PaymentCharge_ID = 0;
			if(getTransferFeeType().equals("T") && get_ValueAsBoolean("isHasTransferFee")) {
				TCS_MPayment paytoCharge = new TCS_MPayment(getCtx(), 0, get_TrxName());
				
				paytoCharge.setPayAmt(getPayAmtTo().add(getChargeAmt()));
				paytoCharge.setAD_Org_ID(getC_BankAccount_From().getAD_Org_ID());
				paytoCharge.setTenderType(MPayment.TENDERTYPE_Account);
				paytoCharge.setC_BankAccount_ID(getC_BankAccount_To_ID());
				paytoCharge.setC_BPartner_ID(getC_BPartner_ID());
				paytoCharge.setC_DocType_ID(false, false); 
				paytoCharge.setC_Currency_ID(getC_Currency_From_ID());
				paytoCharge.set_ValueOfColumn("C_BankTransfer_ID", getC_BankTransfer_ID());
				if(getDescription() == null) {
					paytoCharge.setDescription("Generated From Bank Transfer " + getDocumentNo());								
				}
				else {
					paytoCharge.setDescription(getDescription() + " | Generated From Bank Transfer " + getDocumentNo());
				}
				paytoCharge.setDateAcct(getDateAcct());
				paytoCharge.setDateTrx(getDateAcct()); 
				
				paytoCharge.setPayAmt(getChargeAmt());
				
				
				if (funcCurrencyID!=getC_Currency_From_ID()) {
					paytoCharge.setC_ConversionType_ID(getC_ConversionType_ID());
				}
				
				if(!paytoCharge.processIt(TCS_MPayment.DOCACTION_Complete)) {
					log.warning("Payment Process Failed: " + paytoCharge+ " - " + paytoCharge.getProcessMsg());
					throw new IllegalStateException("Payment Process Failed: " + paytoCharge + " - " + paytoCharge.getProcessMsg());
				}
				
				paytoCharge.saveEx();
				
				set_Value("C_Payment_Charge_ID",paytoCharge.getC_Payment_ID());
				saveEx();
				C_PaymentCharge_ID = paytoCharge.getC_Payment_ID();
			}
				
			
			/*
			if(isHasTransferFee()) {
				MPayment paymentTransfer = new MPayment(getCtx(), 0, get_TrxName());
				
				paymentTransfer.setTenderType(MPayment.TENDERTYPE_DirectDeposit);
				if(getTransferFeeType().equals(MBankTransfer.TRANSFERFEETYPE_ChargeOnBankFrom)) {
					paymentTransfer.setC_BankAccount_ID(getC_BankAccount_From_ID());
					paymentTransfer.setC_Currency_ID(getChar);
					paymentTransfer.setPayAmt(getChargeAmt());
				}
				else {
					paymentTransfer.setC_BankAccount_ID(getC_BankAccount_To_ID());
					paymentTransfer.setC_Currency_ID(getC_Currency_To_ID());
					paymentTransfer.setPayAmt(getPayAmtTo());
				}
				paymentTransfer.setC_BPartner_ID(getC_BPartner_ID());
				paymentTransfer.setC_DocType_ID(true);
				
				if(getDescription() == null) {
					paymentTransfer.setDescription("Generated From Bank Transfer " + getDocumentNo());								
				}
				else {
					paymentTransfer.setDescription(getDescription() + " | Generated From Bank Transfer " + getDocumentNo());
				}
				paymentTransfer.setDateAcct(getDateAcct());
				paymentTransfer.setDateTrx(getDateAcct());
				paymentTransfer.setChargeAmt(getChargeAmt());
				paymentTransfer.setC_ConversionType_ID(getC_ConversionType_ID());
				paymentTransfer.saveEx();
				
				if(!paymentTransfer.processIt(MPayment.DOCACTION_Complete)){
					log.warning("Payment Process Failed: " + paymentTransfer + " - " + paymentTransfer.getProcessMsg());
					throw new IllegalStateException("Payment Process Failed: " + paymentTransfer + " - " + paymentTransfer.getProcessMsg());
				}
				setC_Payment_Transfer_ID(paymentTransfer.getC_Payment_ID());
				paymentTransfer.saveEx();
			
			}
			*/
			
			//allocation

			String sqlDivideRate =
					"SELECT dividerate FROM C_Conversion_Rate"
				  + " WHERE C_Currency_ID = " + getC_Currency_From_ID() 
				  + " AND C_Currency_ID_To = " + getC_Currency_To_ID()
				  + " AND ValidFrom <= '" + getDateAcct()
				  + "' AND ValidTo >= '" + getDateAcct() + "'"
				  + " AND C_ConversionType_ID = " + getC_ConversionType_ID();

			String sqlMultiplyRate =
					"SELECT multiplyrate FROM C_Conversion_Rate"
				  + " WHERE C_Currency_ID = " + getC_Currency_From_ID() 
				  + " AND C_Currency_ID_To = " + getC_Currency_To_ID()
				  + " AND ValidFrom <= '" + getDateAcct()
				  + "' AND ValidTo >= '" + getDateAcct() + "'"
				  + " AND C_ConversionType_ID = " + getC_ConversionType_ID();
			
			String sqlMultiplyRateForCurrencynotIDRFrom =
					"SELECT multiplyrate FROM C_Conversion_Rate"
				  + " WHERE C_Currency_ID = " + getC_Currency_From_ID() 
				  + " AND C_Currency_ID_To = " + getC_Currency_ID()
				  + " AND ValidFrom <= '" + getDateAcct()
				  + "' AND ValidTo >= '" + getDateAcct() + "'"
				  + " AND C_ConversionType_ID = " + getC_ConversionType_ID();
			
			String sqlMultiplyRateForCurrencynotIDRTo =
					"SELECT multiplyrate FROM C_Conversion_Rate"
				  + " WHERE C_Currency_ID = " + getC_Currency_To_ID()
				  + " AND C_Currency_ID_To = " + getC_Currency_ID()
				  + " AND ValidFrom <= '" + getDateAcct()
				  + "' AND ValidTo >= '" + getDateAcct() + "'"
				  + " AND C_ConversionType_ID = " + getC_ConversionType_ID();
						
			BigDecimal multiplyRate = DB.getSQLValueBD(get_TrxName(), sqlMultiplyRate);
			BigDecimal multiplyRateCurrencynotIDRFrom = DB.getSQLValueBD(get_TrxName(), sqlMultiplyRateForCurrencynotIDRFrom);
			BigDecimal multiplyRateCurrencynotIDRTo = DB.getSQLValueBD(get_TrxName(), sqlMultiplyRateForCurrencynotIDRTo);			
			BigDecimal divideRate = DB.getSQLValueBD(get_TrxName(), sqlDivideRate);
			
			MAllocationHdr allocHdr = new MAllocationHdr(getCtx(), false, getDateAcct(), getC_Currency_ID(),"", get_TrxName());
			allocHdr.setAD_Org_ID(getAD_Org_ID());
			allocHdr.setDateAcct(getDateAcct());
			allocHdr.setDateTrx(getDateAcct());
			allocHdr.setC_Currency_ID(getC_Currency_ID());
			allocHdr.set_ValueOfColumn("C_BankTransfer_ID", getC_BankTransfer_ID());

			if(getDescription() == null) {
				allocHdr.setDescription("Generated From Bank Transfer " + getDocumentNo());								
			}
			else {
				allocHdr.setDescription(getDescription() + " | Generated From Bank Transfer " + getDocumentNo());
			}
			allocHdr.saveEx();
			
			MAllocationLine alloclineAP = new MAllocationLine(allocHdr);
			
			alloclineAP.setAD_Org_ID(getC_BankAccount_From().getAD_Org_ID());
			alloclineAP.setC_BPartner_ID(paymentFrom.getC_BPartner_ID());
			alloclineAP.setC_Payment_ID(paymentFrom.getC_Payment_ID());
			if(getC_Currency_From_ID() != getC_Currency_To_ID() && getC_Currency_From_ID() != funcCurrencyID && getC_Currency_To_ID() != funcCurrencyID)
				alloclineAP.setAmount(paymentFrom.getPayAmt().multiply(multiplyRateCurrencynotIDRFrom).negate());
			else if(getC_Currency_From_ID() != getC_Currency_To_ID() && getC_Currency_From_ID() != funcCurrencyID)
				alloclineAP.setAmount(paymentFrom.getPayAmt().multiply(multiplyRate).negate());
			else if(getC_Currency_From_ID() != getC_Currency_ID() && getC_Currency_To_ID() != getC_Currency_ID())
				alloclineAP.setAmount(paymentFrom.getPayAmt().multiply(multiplyRateCurrencynotIDRFrom).negate());
			else if(!get_ValueAsBoolean("isHasTransferFee"))
				alloclineAP.setAmount(getAmountFrom().negate());
			else if(getTransferFeeType().equals("F") && get_ValueAsBoolean("isHasTransferFee"))
				alloclineAP.setAmount(getPayAmtFrom().negate());
			else 
				alloclineAP.setAmount(getAmountFrom().negate());
			
			alloclineAP.saveEx();
			
			MAllocationLine alloclineAR = new MAllocationLine(allocHdr);
			
			alloclineAR.setAD_Org_ID(getC_BankAccount_To().getAD_Org_ID());
			alloclineAR.setC_BPartner_ID(paymentTo.getC_BPartner_ID());
			alloclineAR.setC_Payment_ID(paymentTo.getC_Payment_ID());
			if(getC_Currency_From_ID() != getC_Currency_To_ID() && getC_Currency_From_ID() != funcCurrencyID && getC_Currency_To_ID() != funcCurrencyID)
				alloclineAR.setAmount(paymentTo.getPayAmt().multiply(multiplyRateCurrencynotIDRTo));
			else if(getC_Currency_To_ID() != getC_Currency_From_ID() && getC_Currency_To_ID() != funcCurrencyID)
				alloclineAR.setAmount(paymentTo.getPayAmt().multiply(divideRate));
			else if(getC_Currency_From_ID() != getC_Currency_ID() && getC_Currency_To_ID() != getC_Currency_ID())
				alloclineAR.setAmount(paymentTo.getPayAmt().multiply(multiplyRateCurrencynotIDRTo));
			else if(!get_ValueAsBoolean("isHasTransferFee"))
				alloclineAR.setAmount(getAmountTo());
//			else if(getTransferFeeType().equals("T") && get_ValueAsBoolean("isHasTransferFee"))
//				alloclineAR.setAmount(getPayAmtTo());
			else
				alloclineAR.setAmount(getAmountTo());
		
			alloclineAR.saveEx();
			
			if(isHasTransferFee() && getTransferFeeType().equals("F")){
				MAllocationLine alloclineCharge= new MAllocationLine(allocHdr);
				
				alloclineCharge.setAD_Org_ID(allocHdr.getAD_Org_ID());
				alloclineCharge.setC_BPartner_ID(getC_BPartner_ID());
				alloclineCharge.setAmount(getChargeAmt());
				alloclineCharge.setC_Charge_ID(getC_Charge_ID());
				
				alloclineCharge.saveEx();
				
				
				if(getTransferFeeType().equals("F") && get_ValueAsBoolean("isHasTransferFee")) {
					MAllocationLine alloclineChargePay= new MAllocationLine(allocHdr);
					alloclineChargePay.setAD_Org_ID(allocHdr.getAD_Org_ID());
					alloclineChargePay.setC_BPartner_ID(getC_BPartner_ID());
					alloclineChargePay.setAmount(getChargeAmt().negate());
					alloclineChargePay.setC_Payment_ID(paymentFrom.getC_Payment_ID());
				
//				else if(getTransferFeeType().equals("T") && get_ValueAsBoolean("isHasTransferFee")) {
//					alloclineChargePay.setAmount(getChargeAmt());
//					alloclineChargePay.setC_Payment_ID(paymentTo.getC_Payment_ID());
//				}
					alloclineChargePay.saveEx();
				}
			}				
		
			if(getC_Currency_From_ID() != getC_Currency_To_ID()) {
				
				MAllocationLine alloclineLossvsGain = new MAllocationLine(allocHdr);
				
				alloclineLossvsGain.setAD_Org_ID(allocHdr.getAD_Org_ID());
				alloclineLossvsGain.setC_BPartner_ID(getC_BPartner_ID());
				alloclineLossvsGain.setAmount(alloclineAP.getAmount().negate().subtract(alloclineAR.getAmount()));
				
				if(alloclineAP.getAmount().negate().compareTo(alloclineAR.getAmount())==1) {
					alloclineLossvsGain.setC_Charge_ID(MSysConfig.getIntValue("currency_loss_charge", 0, paymentFrom.getAD_Client_ID()));
				}
				else if(alloclineAR.getAmount().compareTo(alloclineAP.getAmount().negate())==1)
				{
					alloclineLossvsGain.setC_Charge_ID(MSysConfig.getIntValue("currency_gain_charge", 0, paymentFrom.getAD_Client_ID()));
				}
				alloclineLossvsGain.saveEx();
			}				
			
			allocHdr.processIt(DocAction.ACTION_Complete);

			if(getTransferFeeType().equals("T") && get_ValueAsBoolean("isHasTransferFee")) {
				MAllocationHdr allocHdrCharge = new MAllocationHdr(getCtx(), false, getDateAcct(), getC_Currency_ID(),"", get_TrxName());
				allocHdrCharge.setAD_Org_ID(getAD_Org_ID());
				allocHdrCharge.setDateAcct(getDateAcct());
				allocHdrCharge.setDateTrx(getDateAcct());
				allocHdrCharge.setC_Currency_ID(getC_Currency_ID());
				allocHdrCharge.set_ValueOfColumn("C_BankTransfer_ID", getC_BankTransfer_ID());

				if(getDescription() == null) {
					allocHdrCharge.setDescription("Generated From Bank Transfer " + getDocumentNo());								
				}
				else {
					allocHdrCharge.setDescription(getDescription() + " | Generated From Bank Transfer " + getDocumentNo());
				}
				allocHdrCharge.saveEx();
				
				MAllocationLine allocPaytoCharge= new MAllocationLine(allocHdrCharge);
				
				allocPaytoCharge.setAD_Org_ID(allocHdrCharge.getAD_Org_ID());
				allocPaytoCharge.setC_BPartner_ID(getC_BPartner_ID());
				allocPaytoCharge.setAmount(getChargeAmt());
				allocPaytoCharge.setC_Charge_ID(getC_Charge_ID());
				
				allocPaytoCharge.saveEx();

				MAllocationLine allocChargePay= new MAllocationLine(allocHdrCharge);
				
				allocChargePay.setAD_Org_ID(allocHdrCharge.getAD_Org_ID());
				allocChargePay.setC_BPartner_ID(getC_BPartner_ID());
				allocChargePay.setAmount(getChargeAmt().negate());
				allocChargePay.setC_Payment_ID(C_PaymentCharge_ID);
				
				allocChargePay.saveEx();
				
				System.out.println(allocPaytoCharge.getC_AllocationLine_ID());
				System.out.println(allocPaytoCharge.getC_AllocationHdr_ID());
				
				allocHdrCharge.processIt(DocAction.ACTION_Complete);
				
			}
						
			
			
			
			String valid = ModelValidationEngine.get().fireDocValidate(this, ModelValidator.TIMING_AFTER_COMPLETE);
			if (valid != null)
			{
				m_processMsg = valid;
				return DocAction.STATUS_Invalid;
			}
			
			setProcessed(true);
			setC_Payment_From_ID(paymentFrom.getC_Payment_ID());
			setC_Payment_To_ID(paymentTo.getC_Payment_ID());			
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
		
		if(getDocStatus().equals(DOCSTATUS_Completed)) {
			m_processMsg = ModelValidationEngine.get().fireDocValidate(this,ModelValidator.TIMING_BEFORE_VOID);
			if (m_processMsg != null)
				return false;	
			
			String whereClause = "c_banktransfer_id = " + getC_BankTransfer_ID();
			List<TCS_MPayment> payments =  new Query(getCtx(), TCS_MPayment.Table_Name, whereClause, get_TrxName()).list();

			List<MAllocationHdr> allocs=  new Query(getCtx(), MAllocationHdr.Table_Name, whereClause, get_TrxName()).list();

			
			for(MAllocationHdr alloc : allocs) {
				alloc.set_ValueOfColumn("C_BankTransfer_ID", null);
				alloc.saveEx();
				
			}
			
			for(TCS_MPayment payment : payments) {
				payment.set_ValueOfColumn("C_BankTransfer_ID", null);
				payment.saveEx();
			}
			
				
			for(MAllocationHdr alloc : allocs) {
				alloc.processIt(DOCACTION_Reverse_Correct);
				alloc.saveEx(get_TrxName());

			}
			
			for(TCS_MPayment payment : payments) {
				payment.processIt(DOCACTION_Reverse_Correct);
				payment.saveEx();
			}
			

			for(MAllocationHdr alloc : allocs) {
				alloc.set_ValueOfColumn("C_BankTransfer_ID", getC_BankTransfer_ID());
				alloc.saveEx();
				
			}
			
			for(TCS_MPayment payment : payments) {
				payment.set_ValueOfColumn("C_BankTransfer_ID", getC_BankTransfer_ID());
				payment.saveEx();
			}

			
		}
	
//		if(getDocStatus().equals(DOCSTATUS_Drafted)
//				|| getDocStatus().equals(DOCSTATUS_Invalid)
//				|| getDocStatus().equals(DOCSTATUS_InProgress))
//		{
//			m_processMsg = ModelValidationEngine.get().fireDocValidate(this,ModelValidator.TIMING_BEFORE_VOID);
//			if (m_processMsg != null)
//				return false;
//			
//			setDescription(getDescription() + "[VOIDED], Pay Amount From = " + getPayAmtFrom() 
//				+ ", Pay Amount To = " + getPayAmtTo() + ", Charge Amount = " + getChargeAmt());
//			setPayAmtFrom(Env.ZERO);
//			setPayAmtTo(Env.ZERO);
//			setChargeAmt(Env.ZERO);
//			setDocStatus(DOCSTATUS_Voided);
//		}
//		else
//		{
//			m_processMsg = "Cannot Void Document.. Only Document with status Draft / Invalid / In Progress can be voided";
//			return false;
//		}
		
		m_processMsg = ModelValidationEngine.get().fireDocValidate(this,ModelValidator.TIMING_AFTER_VOID);
		if (m_processMsg != null)
			return false;
		
		setDocStatus(DOCSTATUS_Voided);
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
		
		//@win.. when reversing, we are only creating a bank transfer which reverse the flow of original bank transfer and then process complete it
		
		// create reversal
		MBankTransfer reversal = new MBankTransfer(getCtx(), 0, get_TrxName());
		copyValues(this, reversal);
		reversal.setClientOrg(this);
		reversal.setDateAcct(dateAcct);
		//
		reversal.setDocumentNo(getDocumentNo() + REVERSE_INDICATOR);
		reversal.setDocStatus(DOCSTATUS_Drafted);
		reversal.setDocAction(DOCACTION_Complete);
		reversal.setC_BankAccount_From_ID(getC_BankAccount_To_ID());
		reversal.setC_BankAccount_To_ID(getC_BankAccount_From_ID());
		reversal.setC_Currency_From_ID(getC_Currency_To_ID());
		reversal.setC_Currency_To_ID(getC_Currency_From_ID());
		//
		reversal.setPayAmtFrom(getPayAmtTo());
		reversal.setPayAmtTo(getPayAmtFrom());
		reversal.setChargeAmt(getChargeAmt().negate());
		//
		reversal.setProcessing(false);
		reversal.setProcessed(false);
		reversal.setDescription(getDescription());
		reversal.addDescription("{->" + getDocumentNo() + ")");
		//
		reversal.saveEx(get_TrxName());
		// post reversal
		try {
			if (!reversal.processIt(DOCACTION_Complete))
			{
				m_processMsg = "Reversal Error: " + reversal.getProcessMsg();
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		reversal.closeIt();
		reversal.setDocStatus(DOCSTATUS_Reversed);
		reversal.setDocAction(DOCACTION_None);
		reversal.saveEx(get_TrxName());
		
		//Add description on original document to link to reversal document
		addDescription("{->" + reversal.getDocumentNo() + ")");
		saveEx();
		
		StringBuilder info = new StringBuilder(reversal.getDocumentNo());

		
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
		
//		StringBuilder info = reverse(false);
//		if (info == null)
//			return false;
//		
		//after reverseCorrect
		m_processMsg = ModelValidationEngine.get().fireDocValidate(this, ModelValidator.TIMING_AFTER_REVERSECORRECT);
		if(m_processMsg != null)
			return false;
		
		//m_processMsg = info.toString();
		return false;
	}

	@Override
	public boolean reverseAccrualIt() {
		return false;
	}

	@Override
	public boolean reActivateIt() {
		return false;
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
		return null;
	}

	@Override
	public int getDoc_User_ID() {
		return 0;
	}

	@Override
	public int getC_Currency_ID() {
		//@win note.. get from context variable.. from default acct schema
		return Env.getContextAsInt(getCtx(), "$C_Currency_ID");
	}

	@Override
	public BigDecimal getApprovalAmt() {
		return this.getPayAmtFrom();
	}

}