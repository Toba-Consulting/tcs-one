package id.tcs.process;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;

import org.adempiere.exceptions.AdempiereException;
import org.compiere.model.MBankAccount;
import org.compiere.model.MDocType;
import org.compiere.model.MInOut;
import org.compiere.model.MInvoice;
import org.compiere.model.MOrder;
import org.compiere.model.Query;
import org.compiere.model.TCS_MOrder;
import org.compiere.process.DocAction;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;

import id.tcs.model.X_C_OrderPayment;

import org.compiere.model.TCS_MPayment;
import org.compiere.model.TCS_MRMA;

public class TCS_GeneratePaymentFromRMA extends SvrProcess {

	int p_M_RMA_ID = 0;
	
	@Override
	protected void prepare() {
		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++)
		{
			String name = para[i].getParameterName();
			if (para[i].getParameter() == null)
				;

			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}

		p_M_RMA_ID = getRecord_ID();
	}

	@Override
	protected String doIt() throws Exception {
		TCS_MRMA rma = new TCS_MRMA(getCtx(), p_M_RMA_ID, get_TrxName());
		MDocType dt = new MDocType(getCtx(), rma.getC_DocType_ID(), get_TrxName());
		
		if(!dt.get_ValueAsString("DocSubTypeSOReturn").equals("RC")) {
			throw new AdempiereException("Only Available for Customer Return Cash");
		}
		
		String sqlAmount = "Select sum(linenetamt) from m_rmaline where m_rma_id = ?";
		BigDecimal amt = DB.getSQLValueBD(get_TrxName(), sqlAmount, rma.getM_RMA_ID());
		
		String sqlInout = "select m_inout_id from m_inout where docstatus ='CO' and m_rma_id = " + rma.getM_RMA_ID(); 
		int M_InOut_ID = DB.getSQLValue(get_TrxName(), sqlInout);
		MInOut inout = new MInOut(getCtx(), M_InOut_ID, get_TrxName());
		
		MInvoice invoice = new MInvoice(getCtx(), inout.getC_Invoice_ID(), get_TrxName());
			
		TCS_MPayment payment = new TCS_MPayment(rma.getCtx(), 0, get_TrxName());
		MBankAccount bankAcct = new MBankAccount(getCtx(), rma.get_ValueAsInt("C_BankAccount_ID"), get_TrxName());
		
		String sql = "SELECT C_DocType_ID FROM C_DocType WHERE IsActive='Y' and docbasetype ='APP' AND AD_Client_ID=? AND issotrx = ? AND isPrepayment = ? ORDER BY IsDefault DESC";
		int C_DocType_ID = DB.getSQLValue(get_TrxName(), sql, new Object[] {rma.getAD_Client_ID(), false, false});
		
		
		payment.setAD_Org_ID(rma.getAD_Org_ID());
		payment.setC_BPartner_ID(rma.getC_BPartner_ID());
		payment.setC_BankAccount_ID(rma.get_ValueAsInt("C_BankAccount_ID"));
		payment.setDocAction(DocAction.ACTION_Complete);
		payment.setDocStatus(DocAction.STATUS_Drafted);
		payment.setC_Currency_ID(bankAcct.getC_Currency_ID());
		payment.setPayAmt(amt);
		
		if(rma.get_ValueAsString("PaymentRule").equalsIgnoreCase("C"))
			payment.setTenderType("X"); //Cash
		else if(rma.get_ValueAsString("PaymentRule").equalsIgnoreCase("E"))
			payment.setTenderType("T"); //Account 
		else if(rma.get_ValueAsString("PaymentRule").equalsIgnoreCase("K"))
			payment.setTenderType("E"); //Credit
		else if(rma.get_ValueAsString("PaymentRule").equalsIgnoreCase("L"))
			payment.setTenderType("L"); //Debit
		else if(rma.get_ValueAsString("PaymentRule").equalsIgnoreCase("S"))
			payment.setTenderType("K"); //Check
		else
			payment.setTenderType(TCS_MPayment.TENDERTYPE_Cash);
		
		payment.setC_Invoice_ID(invoice.getC_Invoice_ID()); 
		payment.setDateAcct((Timestamp) rma.get_Value("DateDoc"));
		payment.setDateTrx((Timestamp) rma.get_Value("DateDoc"));
		payment.setC_DocType_ID(C_DocType_ID);
		payment.set_ValueOfColumn("M_RMA_ID", rma.getM_RMA_ID());
		payment.setIsPrepayment(false);
		payment.setIsReceipt(true);
		payment.saveEx();
		if(!payment.processIt(DocAction.ACTION_Complete)) 
			throw new AdempiereException("Could not complte Payment");
		else
			rma.set_ValueOfColumn("IsPaid", true);

		payment.saveEx();

		invoice.setC_Payment_ID(payment.get_ID());
		invoice.saveEx();
		
		rma.set_ValueOfColumn("C_Payment_ID", payment.getC_Payment_ID());
		rma.saveEx();
		
		return "Payment with Document No: " + payment.getDocumentNo();
	}

}