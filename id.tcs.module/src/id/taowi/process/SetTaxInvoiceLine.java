package id.taowi.process;

import java.math.BigDecimal;
import java.util.logging.Level;

import org.compiere.model.MInvoice;
import org.compiere.model.MInvoiceLine;
import org.compiere.model.MPriceList;
import org.compiere.model.MTax;
import org.compiere.model.Query;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.Env;

public class SetTaxInvoiceLine extends SvrProcess{

	int p_C_Tax_ID = 0;
	
	@Override
	protected void prepare() {

		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++) {
			String name = para[i].getParameterName();
			if (para[i].getParameter() == null) {
				;
			}else if (para[i].getParameterName().equalsIgnoreCase("C_Tax_ID")) {
				p_C_Tax_ID = para[i].getParameterAsInt();
			}else {
				log.log(Level.SEVERE, "Unknown Parameter: "+name);
			}
		}
	}

	@Override
	protected String doIt() throws Exception {
		
		int C_Invoice_ID = getRecord_ID();
		
		MInvoice invoice = new MInvoice(getCtx(), C_Invoice_ID , get_TrxName());
		MTax tax = new MTax(getCtx(), p_C_Tax_ID, get_TrxName());
		MInvoiceLine[] invoiceLines = invoice.getLines();
		
		BigDecimal grandTotal = invoice.getGrandTotal();
		BigDecimal totalLine = invoice.getTotalLines();
		BigDecimal taxRate = tax.getRate();
		
		// Validate : Check Parameter Value
		if (p_C_Tax_ID == 0)
			return "Error: Tax is Empty";
		
		// Change Tax for All InvoiceLine
		for (MInvoiceLine invoiceLine : invoiceLines) {
			int M_PriceList_ID = Env.getContextAsInt(getCtx(), C_Invoice_ID, "M_PriceList_ID");
			int StdPrecission = MPriceList.getStandardPrecision(getCtx(), M_PriceList_ID);
			BigDecimal TaxAmt = Env.ZERO;
			
			invoiceLine.setC_Tax_ID(p_C_Tax_ID);

			// Calculate TaxAmt after Tax change
			TaxAmt = tax.calculateTax(invoiceLine.getLineNetAmt(), false, StdPrecission);
			
			// Set TaxAmt and LineTotalAmt 
			invoiceLine.setTaxAmt(TaxAmt);
			invoiceLine.setLineTotalAmt(invoiceLine.getLineNetAmt().add(invoiceLine.getTaxAmt()));
			invoiceLine.saveEx();
		}
		
		// Validate : Check InvoiceLine 
		boolean check = true;
		for (MInvoiceLine invoiceLine : invoiceLines) {
			String whereClause = "C_InvoiceLine_ID =? AND TaxAmt = 0";
			boolean match = new Query(getCtx(), MInvoiceLine.Table_Name, whereClause, get_TrxName())
					.setParameters(invoiceLine.get_ID()).match();
			
			if (match)
				check = false;
		}
		
		if (!check) {
			if (invoice.getTotalLines().compareTo(invoice.getGrandTotal()) != 0) {
				invoice.setGrandTotal(grandTotal.subtract(totalLine.multiply(taxRate.divide(Env.ONEHUNDRED))));
				invoice.saveEx();
			}
		}else {
			if (invoice.getTotalLines().compareTo(invoice.getGrandTotal()) == 0) {
				invoice.setGrandTotal(totalLine.add(totalLine.multiply(taxRate.divide(Env.ONEHUNDRED))));
				invoice.saveEx();
			}
		}
		return "";
	}

}
