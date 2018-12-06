package id.tcs.process;

import java.math.BigDecimal;
import java.sql.Timestamp;

import org.adempiere.exceptions.AdempiereException;
import org.compiere.model.MAllocationHdr;
import org.compiere.model.MAllocationLine;
import org.compiere.model.MDocType;
import org.compiere.model.MPayment;

import id.tcs.model.TCS_MAdvRequest;
import id.tcs.model.TCS_MAdvSettlement;

import org.compiere.process.DocAction;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.Env;
import org.compiere.util.Msg;

public class TCS_AdvanceReimbursePayment extends SvrProcess {

	int p_C_DocType_ID = 0;
	int p_C_BankAccount_ID = 0;
	String p_TenderType = "";
	String p_CheckNo = "";
	Timestamp p_DateTrx = null;
	Timestamp p_DateAcct = null;

	@Override
	protected void prepare() {
		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++) {
			if (para[i].getParameter() == null)
				;
			else if (para[i].getParameterName().equals(MPayment.COLUMNNAME_C_DocType_ID))
				p_C_DocType_ID = para[i].getParameterAsInt();
			else if (para[i].getParameterName().equals(MPayment.COLUMNNAME_C_BankAccount_ID))
				p_C_BankAccount_ID = para[i].getParameterAsInt();
			else if (para[i].getParameterName().equals(MPayment.COLUMNNAME_TenderType))
				p_TenderType = para[i].getParameterAsString();
			else if (para[i].getParameterName().equals(MPayment.COLUMNNAME_CheckNo))
				p_CheckNo = para[i].getParameterAsString();
			else if (para[i].getParameterName().equals(MPayment.COLUMNNAME_DateTrx))
				p_DateTrx = para[i].getParameterAsTimestamp();
			else if (para[i].getParameterName().equals(MPayment.COLUMNNAME_DateAcct))
				p_DateAcct = para[i].getParameterAsTimestamp();
		}
	}

	@Override
	protected String doIt() throws Exception {
		TCS_MAdvSettlement settlement = new TCS_MAdvSettlement(getCtx(), getRecord_ID(), get_TrxName());
		TCS_MAdvRequest request = (TCS_MAdvRequest) settlement.getTCS_AdvRequest();

		if (!settlement.getDocStatus().equals("CO") && !settlement.getDocStatus().equals("CL")) {
			return "Settlement Belum Dicomplete";
		}
		
		if (settlement.getC_Invoice_ID()<=0) {
			throw new AdempiereException("Invoice Atas Settlement Ini Belum Terbuat");
		}
		
		if (settlement.getAmountReimbursed().compareTo(Env.ZERO)<=0) {
			throw new AdempiereException("Amount Reimbursed <= 0");			
		}
		BigDecimal requestAmt = request.getRequestGrandTotal();
		if (settlement.getGrandTotal().compareTo(requestAmt) <= 0)
			throw new AdempiereException("Nilai Settlement lebih kecil dari nilai Request");

		MPayment payment = new MPayment(getCtx(), 0, get_TrxName());
		payment.setAD_Org_ID(settlement.getAD_Org_ID());
		payment.setC_DocType_ID(p_C_DocType_ID);
		payment.setIsReceipt(false);
		payment.setC_BankAccount_ID(p_C_BankAccount_ID);
		payment.setTenderType(p_TenderType);
		if (p_TenderType.equals(MPayment.TENDERTYPE_Check))
			payment.setCheckNo(p_CheckNo);
		payment.setDateTrx(p_DateTrx);
		payment.setDateAcct(p_DateAcct);
		payment.setC_BPartner_ID(request.getC_BPartner_ID());
		payment.setDescription(request.getDescription() == null ? "" : request.getDescription());
		payment.setAmount(request.getC_Currency_ID(), settlement.getAmountReimbursed());
		payment.setC_ConversionType_ID(request.getC_ConversionType_ID());
		
		payment.saveEx();
		payment.processIt(DocAction.ACTION_Complete);
		payment.saveEx();
		settlement.setC_Payment_ID(payment.get_ID());
		settlement.setisReimbursed(true);
		settlement.saveEx();
		
		MAllocationHdr alloc = new MAllocationHdr(getCtx(), true, // manual
				p_DateTrx, settlement.getC_Currency_ID(), Env.getContext(Env.getCtx(), "#AD_User_Name"), get_TrxName());
		alloc.setAD_Org_ID(settlement.getAD_Org_ID());
		alloc.setC_DocType_ID(MDocType.getDocType("CMA"));
		alloc.setDescription("Reimburse PUM:" +request.getDocumentNo() + ", " +
		alloc.getDescriptionForManualAllocation(settlement.getC_BPartner_ID(), get_TrxName()));
		alloc.saveEx();

//		MAllocationLine aLine = new MAllocationLine(alloc, request.getGrandTotal().subtract(settlement.getGrandTotal()),
//				Env.ZERO, Env.ZERO, Env.ZERO);
		MAllocationLine aLine = new MAllocationLine(alloc, settlement.getAmountReimbursed().negate(),
				Env.ZERO, Env.ZERO, Env.ZERO);
		aLine.setDocInfo(request.getC_BPartner_ID(), 0, settlement.getC_Invoice_ID());
		aLine.setPaymentInfo(payment.get_ID(), 0);
		aLine.saveEx();
			
		
		alloc.processIt(DocAction.ACTION_Complete);
		alloc.saveEx();
		
		String message = Msg.parseTranslation(getCtx(), "@GeneratedPayment@ - " + payment.getDocumentNo());
		addBufferLog(0, null, null, message, payment.get_Table_ID(),
				payment.get_ID());

		return "Success";
	}

}
