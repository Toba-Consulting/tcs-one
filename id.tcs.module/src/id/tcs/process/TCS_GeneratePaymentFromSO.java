package id.tcs.process;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;

import org.adempiere.exceptions.AdempiereException;
import org.compiere.model.MOrder;
import org.compiere.model.Query;
import org.compiere.model.TCS_MOrder;
import org.compiere.process.DocAction;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;

import id.tcs.model.X_C_OrderPayment;

import org.compiere.model.TCS_MPayment;

public class TCS_GeneratePaymentFromSO extends SvrProcess {

	int p_C_Order_ID = 0;
	
	@Override
	protected void prepare() {
		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++)
		{
			String name = para[i].getParameterName();
			if (para[i].getParameter() == null)
				;

//			else if (name.equals("isRecalculate"))
//				p_Recalculate = para[i].getParameterAsBoolean();

			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}

		p_C_Order_ID = getRecord_ID();
	}

	@Override
	protected String doIt() throws Exception {
		TCS_MOrder order = new TCS_MOrder(getCtx(), p_C_Order_ID, get_TrxName());
		
		// Mixed Payment
		if (order.getPaymentRule().equalsIgnoreCase("A")) {
			String whereClause = "C_Order_ID=?";
			List<X_C_OrderPayment> list = new Query(order.getCtx(), X_C_OrderPayment.Table_Name, whereClause , order.get_TrxName())
					.setParameters(order.get_ID())
					.list();

			X_C_OrderPayment[] orderPayments = list.toArray(new X_C_OrderPayment[list.size()]);

			String sql = "SELECT COALESCE(SUM(amt),0) FROM C_OrderPayment WHERE C_Order_ID = ?";
			BigDecimal sumAmt = DB.getSQLValueBD(get_TrxName(), sql, order.getC_Order_ID());
			
			if(sumAmt.compareTo(order.getGrandTotal()) != 0)
				throw new AdempiereException("Payment Amount Must Be The Same As Order Grand Total");

			
			for (X_C_OrderPayment orderPayment: orderPayments) {
				TCS_MPayment payment = new TCS_MPayment(order.getCtx(), 0, order.get_TrxName());
				payment.setAD_Org_ID(order.getAD_Org_ID());
				payment.setC_BPartner_ID(order.getC_BPartner_ID());
				payment.setC_BankAccount_ID(orderPayment.getC_BankAccount_ID());
				payment.setDocAction(DocAction.ACTION_Complete);
				payment.setDocStatus(DocAction.STATUS_Drafted);
				payment.setC_Currency_ID(order.getC_Currency_ID());
				payment.setTenderType(orderPayment.getTenderType());
				payment.setPayAmt(orderPayment.getAmt());
				payment.setC_Order_ID(order.get_ID());
				payment.setDateAcct(order.getDateAcct());
				payment.setDateTrx(order.getDateOrdered());
				payment.setIsPrepayment(false);
				payment.setIsReceipt(true);
				payment.setC_DocType_ID(true);
				payment.set_ValueOfColumn("C_OrderPayment_ID", orderPayment.getC_OrderPayment_ID());
				payment.saveEx();

				payment.processIt(DocAction.ACTION_Complete);
				payment.saveEx();

				orderPayment.set_ValueOfColumn("C_Payment_ID", payment.get_ID());
				orderPayment.set_ValueOfColumn("Processed", true);
				orderPayment.saveEx();
			}
			order.setC_Payment_ID(0);
			order.saveEx();

			//	getLines
			// P
		} else if (!order.getPaymentRule().equalsIgnoreCase(MOrder.PAYMENTRULE_OnCredit)) {
			TCS_MPayment payment = new TCS_MPayment(order.getCtx(), 0, order.get_TrxName());
			payment.setAD_Org_ID(order.getAD_Org_ID());
			payment.setC_BPartner_ID(order.getC_BPartner_ID());
			payment.setC_BankAccount_ID(order.get_ValueAsInt("C_BankAccount_ID"));
			payment.setDocAction(DocAction.ACTION_Complete);
			payment.setDocStatus(DocAction.STATUS_Drafted);
			payment.setC_Currency_ID(order.getC_Currency_ID());
			payment.setPayAmt(order.getGrandTotal());
			
			if(order.getPaymentRule().equalsIgnoreCase("C"))
				payment.setTenderType("X"); //Cash
			else if(order.getPaymentRule().equalsIgnoreCase("E"))
				payment.setTenderType("T"); //Account 
			else if(order.getPaymentRule().equalsIgnoreCase("K"))
				payment.setTenderType("E"); //Credit/Debit
			else if(order.getPaymentRule().equalsIgnoreCase("S"))
				payment.setTenderType("K"); //Check
			else
				payment.setTenderType(TCS_MPayment.TENDERTYPE_Cash);
			
			payment.setC_Order_ID(order.get_ID());
			payment.setDateAcct(order.getDateAcct());
			payment.setDateTrx(order.getDateOrdered());
			payment.setIsPrepayment(false);
			payment.setIsReceipt(true);
			payment.setC_DocType_ID(true);
			payment.saveEx();

			payment.processIt(DocAction.ACTION_Complete);
			payment.saveEx();

			order.setC_Payment_ID(payment.get_ID());
			order.saveEx();

		}
		
		return "";

	}

}