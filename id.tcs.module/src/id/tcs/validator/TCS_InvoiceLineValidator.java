package id.tcs.validator;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.adempiere.base.event.IEventTopics;
import org.compiere.model.MInOutLine;
import org.compiere.model.MInvoiceLine;
import org.compiere.model.MOrderLine;
import org.compiere.model.MPriceList;
import org.compiere.model.MRequisitionLine;
import org.compiere.model.PO;
import org.compiere.model.Query;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.osgi.service.event.Event;

import id.tcs.model.MMatchPR;
import id.tcs.model.X_M_MatchPR;
import id.tcs.model.X_M_MatchQuotation;

public class TCS_InvoiceLineValidator {
	public static String executeEvent(Event event, PO po) {
		String msg = "";
		MInvoiceLine invoiceLine = (MInvoiceLine) po;
		MInOutLine inoutLine = new MInOutLine(Env.getCtx(), invoiceLine.getM_InOutLine_ID(), null);

		if (event.getTopic().equals(IEventTopics.PO_AFTER_CHANGE)) {
			
			if (invoiceLine.getC_Invoice().isSOTrx() && inoutLine.get_ValueAsBoolean("IsBomDrop")) {
				msg += updatePrices(invoiceLine);
			}
		}
		else if (event.getTopic().equals(IEventTopics.PO_AFTER_NEW)) {
			if (invoiceLine.getC_Invoice().isSOTrx() && inoutLine.get_ValueAsBoolean("IsBomDrop")) {
				msg += updatePrices(invoiceLine);
			}
		}

		return msg;
	}


	private static String updatePrices(MInvoiceLine invoiceLine) {

		String sql = "UPDATE c_invoiceline set priceentered = 0, priceactual = 0, pricelist = 0, linenetamt = 0, isbomdrop = 'Y', isprinted ='N' where c_invoiceline_id ="+invoiceLine.getC_InvoiceLine_ID();
		DB.executeUpdate(sql, invoiceLine.get_TrxName());
		
		String sqlGetTotalLine = "Select sum(linenetamt) from c_invoiceline where c_invoice_id = ?";
		BigDecimal totalLines = DB.getSQLValueBD(invoiceLine.get_TrxName(), sqlGetTotalLine, invoiceLine.getC_Invoice_ID());
		
		String sqlGetTax = "select sum(taxamt) from C_InvoiceTax where C_Invoice_ID = ?";
		BigDecimal TaxAmt = DB.getSQLValueBD(invoiceLine.get_TrxName(), sqlGetTax, invoiceLine.getC_Invoice_ID());
		
		BigDecimal grandTotal = totalLines.add(TaxAmt);
		
		MPriceList pl = new MPriceList(Env.getCtx(), invoiceLine.getC_Invoice().getM_PriceList_ID(), invoiceLine.get_TrxName());
		String sqlUpdateTotalLines = "";
		
		if(pl.get_ValueAsBoolean("IsTaxIncluded"))
			sqlUpdateTotalLines = "Update c_invoice set totallines = " + totalLines + " , grandtotal = " + totalLines + " where C_Invoice_ID = " + invoiceLine.getC_Invoice_ID();
		else
			sqlUpdateTotalLines = "Update c_invoice set totallines = " + totalLines + " , grandtotal = " + grandTotal + " where C_Invoice_ID = " + invoiceLine.getC_Invoice_ID();
		
		DB.executeUpdate(sqlUpdateTotalLines, invoiceLine.get_TrxName());
		return "";
	}
}
