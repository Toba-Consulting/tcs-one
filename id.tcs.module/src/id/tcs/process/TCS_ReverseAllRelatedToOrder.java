package id.tcs.process;

import java.math.BigDecimal;
import java.util.List;
import java.util.logging.Level;

import org.compiere.model.I_M_Product_BOM;
import org.compiere.model.MAllocationHdr;
import org.compiere.model.MInOut;
import org.compiere.model.MInvoice;
import org.compiere.model.MOrder;
import org.compiere.model.MOrderLine;
import org.compiere.model.MPayment;
import org.compiere.model.MProduct;
import org.compiere.model.MProductBOM;
import org.compiere.model.PO;
import org.compiere.model.Query;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;


public class TCS_ReverseAllRelatedToOrder extends SvrProcess {

	int p_C_Order_ID = 0;
	
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

		p_C_Order_ID = getRecord_ID();
	}

	@Override
	protected String doIt() throws Exception {
		MOrder order = new MOrder(getCtx(), p_C_Order_ID, get_TrxName());
		
		String sqlInout = "Select m_inout_id from m_inout where docstatus ='CO' and c_order_id = " + p_C_Order_ID;
		int M_Inout_ID = DB.getSQLValue(get_TrxName(), sqlInout);
		MInOut inout = new MInOut(getCtx(), M_Inout_ID, get_TrxName());
		
		String sqlInvoice = "select ci.c_invoice_id from c_invoice ci join c_invoiceline cil on ci.c_invoice_id = cil.c_invoice_id "
				+ "where m_inoutline_id in(select m_inoutline_id from m_inoutline where m_inout_id=?) and ci.docstatus= 'CO'";
		int C_Invoice_ID = DB.getSQLValue(get_TrxName(), sqlInvoice, M_Inout_ID);
		MInvoice invoice = new MInvoice(getCtx(), C_Invoice_ID, get_TrxName());
		
		String whereClause = "docstatus='CO' and cal.c_invoice_id = " + C_Invoice_ID;
		
		List<MAllocationHdr> allocs = new Query(getCtx(), MAllocationHdr.Table_Name, whereClause, get_TrxName())
				.addJoinClause(" join c_allocationline cal on cal.c_allocationhdr_id = C_AllocationHDR.c_allocationhdr_id")
				.list();
		
		for(MAllocationHdr alloc : allocs) {
			int C_AllocationHdr_ID = 0;
			if(alloc != null)
				C_AllocationHdr_ID = alloc.getC_AllocationHdr_ID();
			
			String sqlPayment = "select c_payment_id from c_allocationline cal "
					+ "join c_allocationhdr cah on cal.c_allocationhdr_id = cah.c_allocationhdr_id where cah.docstatus='CO' and cah.c_allocationhdr_id = " + alloc.getC_AllocationHdr_ID() ;
			int C_Payment_ID = DB.getSQLValue(get_TrxName(), sqlPayment);
			
			// allocation
			if(C_AllocationHdr_ID > 0) {
				alloc.processIt("RC");
				alloc.saveEx(get_TrxName());		
			}
			//Payment
			if(C_Payment_ID > 0) {
				MPayment payment = new MPayment(getCtx(), C_Payment_ID, get_TrxName());
				payment.processIt("RC");
				payment.saveEx(get_TrxName());	
			}	
		}
		
		// Reverse all
		//Invoice
		invoice.processIt("RC");
		invoice.saveEx(get_TrxName());
		//Inout
		inout.processIt("RC");
		inout.saveEx(get_TrxName());
				
		return "";

	}

}