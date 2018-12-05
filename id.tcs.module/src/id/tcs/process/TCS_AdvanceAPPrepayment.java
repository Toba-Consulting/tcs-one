package id.tcs.process;

import java.math.BigDecimal;
import java.sql.Timestamp;

import org.compiere.model.MPayment;
import id.tcs.model.TCS_MAdvRequest;
import org.compiere.process.DocAction;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.Env;
import org.compiere.util.Msg;

public class TCS_AdvanceAPPrepayment extends SvrProcess {
	
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
		
		TCS_MAdvRequest request = new TCS_MAdvRequest(getCtx(), getRecord_ID(), get_TrxName());
		
		if (request.getC_Payment_ID()!=0)
			return "Process gagal.. Advance Request sudah diproses";
		if (!request.getDocStatus().equals("CO") && !request.getDocStatus().equals("CL")) {
			return "Request Belum Dicomplete";
		}
		
		MPayment payment = new MPayment(getCtx(), 0, get_TrxName());
		payment.setAD_Org_ID(request.getAD_Org_ID());
		payment.setC_DocType_ID(p_C_DocType_ID);
		payment.setC_BankAccount_ID(p_C_BankAccount_ID);
		payment.setTenderType(p_TenderType);
		
		if (p_TenderType.equals(MPayment.TENDERTYPE_Check))
			payment.setCheckNo(p_CheckNo);
		payment.setDateTrx(p_DateTrx);
		payment.setDateAcct(p_DateAcct);
		payment.setC_BPartner_ID(request.getC_BPartner_ID());
		payment.setDescription(request.getDescription() == null ? "" : request.getDescription());
		payment.setAmount(request.getC_Currency_ID(), request.getGrandTotal());
		payment.setC_ConversionType_ID(request.getC_ConversionType_ID());
		payment.setIsPrepayment(true);
		payment.saveEx();
		payment.processIt(DocAction.ACTION_Complete);
		payment.saveEx();
		
		String message = Msg.parseTranslation(getCtx(), "@GeneratedPayment@ - " + payment.getDocumentNo());
		addBufferLog(0, null, null, message, payment.get_Table_ID(),
				payment.get_ID());
		request.setC_Payment_ID(payment.getC_Payment_ID());
		request.saveEx();
		
		return "Success";
	}

}
