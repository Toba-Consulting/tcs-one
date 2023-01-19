package id.tcs.validator;

import org.adempiere.base.event.IEventTopics;
import org.adempiere.exceptions.AdempiereException;
import org.compiere.model.MAllocationLine;
import org.compiere.model.MPayment;
import org.compiere.model.PO;
import org.compiere.model.Query;
import org.compiere.model.TCS_MOrder;
import org.compiere.model.TCS_MRMA;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.osgi.service.event.Event;

import id.tcs.model.TCS_MOrderPayment;

public class TCS_PaymentValidator {

	public static String executeEvent(Event event, PO po){
		String msg = "";
		MPayment payment = (MPayment) po;

		if ((event.getTopic().equals(IEventTopics.DOC_BEFORE_REVERSEACCRUAL)) ||
				(event.getTopic().equals(IEventTopics.DOC_BEFORE_REVERSECORRECT))) {
			msg += checkBankTransfer(payment);
			msg += checkAllocation(payment);
			msg += checkReconcile(payment);
		}
		else if ((event.getTopic().equals(IEventTopics.DOC_AFTER_REVERSEACCRUAL)) ||
				(event.getTopic().equals(IEventTopics.DOC_AFTER_REVERSECORRECT))) {
			msg += removeOrder(payment);
			msg += removeRMARef(payment);
		}
		return msg;
	}
	
	private static String removeRMARef(MPayment payment) {
		if(payment.get_ValueAsInt("M_RMA_ID") > 0) {
			TCS_MRMA rma = new TCS_MRMA(Env.getCtx(), payment.get_ValueAsInt("M_RMA_ID"), payment.get_TrxName());
			rma.set_ValueOfColumn("IsPaid", false);
			rma.set_ValueOfColumn("C_Payment_ID", null);
			rma.saveEx();
		}
		return "";
	}

	private static String removeOrder(MPayment payment) {
		if(payment.get_ValueAsInt("C_OrderPayment_ID") > 0) {
			int orderPay_ID = payment.get_ValueAsInt("C_OrderPayment_ID");
			TCS_MOrderPayment orderPay = new TCS_MOrderPayment(Env.getCtx(), orderPay_ID, null);
			TCS_MOrder order = new TCS_MOrder(Env.getCtx(), payment.getC_Order_ID(), null);
			
//			String sqlOrder = "UPDATE C_ORDER SET C_Payment_ID = null WHERE C_Order_ID = " + orderPay.getC_Order_ID();
//			DB.executeUpdate(sqlOrder, null);
//			
//			String sqlPayment = "UPDATE C_Payment SET C_OrderPayment_ID = null WHERE C_Payment_ID = " + payment.getC_Order_ID();
//			DB.executeUpdate(sqlPayment, null);
			
			order.set_ValueOfColumn("C_Payment_ID", null);
			order.saveEx(payment.get_TrxName());
			
			orderPay.set_ValueOfColumn("C_Payment_ID", null);
			orderPay.saveEx();
		}
		else if(payment.getC_Order_ID() > 0) {
			TCS_MOrder order = new TCS_MOrder(Env.getCtx(), payment.getC_Order_ID(), payment.get_TrxName());
			
			order.set_ValueOfColumn("C_Payment_ID", null);
			order.saveEx(payment.get_TrxName());
		}
		
		return "";
	}

	private static String checkBankTransfer(MPayment payment) {
		String msg = "";
		if (payment.get_ValueAsInt("C_BankTransfer_ID") > 0)
			msg = "Error: Payment is generated from Bank Transfer, cannot reverse payment manually.";
		return msg;
	}

	public static String checkAllocation(MPayment payment){
		String whereClause =  " C_AllocationLine.C_Payment_ID = ? AND (cp.C_Charge_ID IS NULL OR cp.C_Charge_ID=0) AND cdr.docStatus='CO' AND"
							+ " NOT EXISTS ("
							+ " SELECT 1 FROM TCS_AllocateCharge tac WHERE tac.C_Payment_ID = C_AllocationLine.C_Payment_ID)"
							+ " AND NOT EXISTS ("
							+ " SELECT 1 FROM C_PaymentAllocate cpa WHERE C_AllocationLine.C_Payment_ID=cpa.C_Payment_ID)";
		boolean match = new Query(payment.getCtx(), MAllocationLine.Table_Name, whereClause, payment.get_TrxName())
				.addJoinClause("JOIN C_AllocationHdr cdr ON cdr.c_AllocationHdr_ID = C_AllocationLine.C_AllocationHdr_ID "
							+  "JOIN C_Payment cp on cp.C_Payment_ID=C_AllocationLine.C_Payment_ID")
				.setParameters(new Object[]{payment.getC_Payment_ID()})
				.match();

		if(match)
			throw new AdempiereException("Cannot reverse payment.. Related allocation exists..");
		return "";
	}
	
	/**
	 * Validation before reverse correct / accrual
	 * Cannot reverse reconciled payment
	 * @param payment
	 * @return
	 */
	public static String checkReconcile(MPayment payment) {
		String msg = "";
		if (payment.isReconciled())
			msg = "Error: Payment is reconciled. Please reversed related bank statement first";
		return msg;
				
	}
}
