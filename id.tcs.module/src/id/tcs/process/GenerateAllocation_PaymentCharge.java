package id.tcs.process;


import java.math.BigDecimal;

import org.compiere.model.MAllocationLine;
import org.compiere.model.Query;
import org.compiere.process.DocAction;
import org.compiere.process.SvrProcess;
import org.compiere.util.Msg;

import org.compiere.model.TCS_MAllocationHdr;
import org.compiere.model.TCS_MPayment;

public class GenerateAllocation_PaymentCharge extends SvrProcess{

	@Override
	protected void prepare() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected String doIt() throws Exception {
		int id[] = null;
		
		String whereClause = " C_Payment.C_Charge_Id is not null "
				+ "AND docstatus = 'CO' "
				+ "AND c_PAyment.dateacct > '2018-01-31' "
				+ "AND c_payment.c_payment_ID not in (select c_payment_id from c_allocationline where c_payment_id is not null)";
		id = new Query(getCtx(), TCS_MPayment.Table_Name, whereClause, get_TrxName()).getIDs();
		
		for (int i = 0; i < id.length; i++) {
			TCS_MPayment payment = new TCS_MPayment(getCtx(), id[i], get_TrxName());
			TCS_MAllocationHdr alloc = new TCS_MAllocationHdr(getCtx(), false, 
					payment.getDateTrx(), payment.getC_Currency_ID(), 
					Msg.translate(getCtx(), "C_Payment_ID")	+ ": " + payment.getDocumentNo(), 
					get_TrxName());
			alloc.setAD_Org_ID(payment.getAD_Org_ID());
			alloc.setDateAcct(payment.getDateAcct());
			alloc.save();
			
			BigDecimal allocateAmount = payment.isReceipt() 
					? payment.getPayAmt() 
					: payment.getPayAmt().negate();
			
			MAllocationLine alloclineCr = new MAllocationLine(alloc);
			alloclineCr.setAD_Org_ID(payment.getAD_Org_ID());
			alloclineCr.setC_BPartner_ID(payment.getC_BPartner_ID());
			alloclineCr.setC_Payment_ID(payment.getC_Payment_ID());
			alloclineCr.setDateTrx(alloc.getDateTrx());
			alloclineCr.setAmount(allocateAmount);
			alloclineCr.saveEx();
			
			MAllocationLine alloclineDr = new MAllocationLine(alloc);
			alloclineDr.setAD_Org_ID(payment.getAD_Org_ID());
			alloclineDr.setC_BPartner_ID(payment.getC_BPartner_ID());
			alloclineDr.setDateTrx(alloc.getDateTrx());
			alloclineDr.setAmount(allocateAmount.negate());
			alloclineDr.setC_Charge_ID(payment.getC_Charge_ID());
			alloclineDr.saveEx();
			
			alloc.processIt(DocAction.ACTION_Complete);
		}
		
		return Integer.toString(id.length);
	}

}
