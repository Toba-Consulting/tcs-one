package id.tcs.process;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.logging.Level;

import org.adempiere.exceptions.AdempiereException;
import org.compiere.model.MBPartnerLocation;
import org.compiere.model.MBankAccount;
import org.compiere.model.MDocType;
import org.compiere.model.MInOut;
import org.compiere.model.MInOutLine;
import org.compiere.model.MInvoice;
import org.compiere.model.MInvoiceLine;
import org.compiere.model.MInvoiceTax;
import org.compiere.model.MProduct;
import org.compiere.model.MRMA;
import org.compiere.model.MRMALine;
import org.compiere.model.MRMATax;
import org.compiere.model.MUOMConversion;
import org.compiere.model.MWarehouse;
import org.compiere.model.Query;
import org.compiere.model.TCS_MInventory;
import org.compiere.model.TCS_MInventoryLine;
import org.compiere.model.TCS_MPayment;
import org.compiere.model.TCS_MRMA;
import org.compiere.model.X_M_RMALine;
import org.compiere.process.DocAction;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;
import org.compiere.util.Trx;

public class TCS_GenerateInvoiceFromInout extends SvrProcess {
	
	int p_M_InOut_ID = 0;
	
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

		p_M_InOut_ID = getRecord_ID();
	}

	@Override
	protected String doIt() throws Exception {
		MInOut inout = new MInOut(Env.getCtx(), p_M_InOut_ID, get_TrxName());
		TCS_MRMA rma = new TCS_MRMA(getCtx(), inout.getM_RMA_ID(), get_TrxName());
		MInvoice invoice = new MInvoice (Env.getCtx(), 0, get_TrxName());
		
		String sqlpricelist = "select m_pricelist_id  from m_pricelist mp where issopricelist  = 'Y' and ad_client_id = 1000000";
		int m_pricelist_id = DB.getSQLValue(get_TrxName(), sqlpricelist);
		
		
		invoice.setC_DocTypeTarget_ID(1000004);
		invoice.setDateInvoiced(inout.getMovementDate());
		invoice.setDateAcct(inout.getMovementDate());
		invoice.setC_Currency_ID(303);
		invoice.setC_BPartner_ID(inout.getC_BPartner_ID());
		invoice.setM_PriceList_ID(m_pricelist_id);
		invoice.setC_BPartner_Location_ID(inout.getC_BPartner_Location_ID());
		invoice.setIsSOTrx(true);
		invoice.setAD_Org_ID(inout.getAD_Org_ID());
		
		if (!invoice.save(get_TrxName()))
		{
			throw new AdempiereException("Could not create Invoice");
		}
		
		//	If we have a Shipment - use that as a base
		if (inout != null)
		{
			inout.setC_Invoice_ID(invoice.getC_Invoice_ID());
			inout.saveEx();

			MInOutLine[] sLines = inout.getLines(false);
			for (int i = 0; i < sLines.length; i++)
			{
				MInOutLine sLine = sLines[i];
				//
				MInvoiceLine iLine = new MInvoiceLine(invoice);
				iLine.setShipLine(sLine);
				//	Qty = Delivered	
				if (sLine.sameOrderLineUOM())
					iLine.setQtyEntered(sLine.getQtyEntered());
				else
					iLine.setQtyEntered(sLine.getMovementQty());
				iLine.setQtyInvoiced(sLine.getMovementQty());
				iLine.setM_InOutLine_ID(sLine.getM_InOutLine_ID());
				iLine.setM_Product_ID(sLine.getM_Product_ID());
				if (!iLine.save(get_TrxName()))
				{
					throw new AdempiereException("Could not create Invoice Line from Shipment Line");
				}
				//
				sLine.setIsInvoiced(true);
				if (!sLine.save(get_TrxName()))
				{
					throw new AdempiereException("Could not update Shipment line: " + sLine);
				}
							}
		}

		
		// added AdempiereException by zuhri
		if (!invoice.processIt(DocAction.ACTION_Complete))
			throw new AdempiereException(Msg.getMsg(Env.getCtx(), "FailedProcessingDocument") + " - " + invoice.getProcessMsg());
		// end added
		invoice.saveEx(get_TrxName());
		if (!invoice.getDocStatus().equals("CO"))
		{
			throw new AdempiereException("@C_Invoice_ID@: " + invoice.getProcessMsg());
		}
		
//		Trx trx = Trx.get(get_TrxName(), false);
//
//		try {
//			trx.commit(true);
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		updateInvoiceTax(rma);

//		String message = Msg.parseTranslation(getCtx(), "@GeneratedInvoice@"+ invoice.getDocumentNo());
//		addBufferLog(0, null, null, message, invoice.get_Table_ID(),invoice.getC_Invoice_ID());
//
		MDocType dt = new MDocType(getCtx(), rma.getC_DocType_ID(), get_TrxName());
		
		String paymentNo="";
		if(dt.get_ValueAsString("DocSubTypeSOReturn").equals("RC")) {
			paymentNo = generatePayment(invoice, rma);
		}
		
		String msg = "";
		if(paymentNo.length() > 0)
			msg += "Generated Invoice with Document No: " + invoice.getDocumentNo() + " And " + paymentNo;
		else
			msg += "Generated Invoice with Document No: " + invoice;
		return msg;

	}
	
	private void updateInvoiceTax(TCS_MRMA rma) {
		String sqlIO = "Select m_inout_id from m_inout where m_rma_id = " + rma.getM_RMA_ID();
		int m_inout_id = DB.getSQLValue(rma.get_TrxName(), sqlIO);
		MInOut io = new MInOut(Env.getCtx(), m_inout_id, rma.get_TrxName());
		
		String whereClauseRma = "m_rma_id = " + rma.getM_RMA_ID();
		List<MRMATax> rmaTaxs = new Query(Env.getCtx(), MRMATax.Table_Name, whereClauseRma, rma.get_TrxName()).list();
		
		for(MRMATax rmatax : rmaTaxs) {
			BigDecimal rmaTaxAmt = rmatax.getTaxAmt();
			BigDecimal rmaTaxBaseAmt = rmatax.getTaxBaseAmt();
			int c_tax_id = rmatax.getC_Tax_ID();
			
			String updateInvoiceTax = "update c_invoicetax set istaxincluded = 'Y', taxamt=" +rmaTaxAmt + ", taxbaseamt=" +rmaTaxBaseAmt + 
					" where c_invoice_id = " + io.getC_Invoice_ID() + " and c_tax_id = " + c_tax_id;
			DB.executeUpdate(updateInvoiceTax, rma.get_TrxName());

		}
		
				
		String whereClause = "c_invoice_id = " + io.getC_Invoice_ID();
		List<MInvoiceTax> taxs = new Query(Env.getCtx(), MInvoiceTax.Table_Name, whereClause, rma.get_TrxName()).list();
		
		for(MInvoiceTax tax : taxs) {
			String sqlGetTotalLine = "Select sum(linenetamt) from c_invoiceline where c_invoice_id = ?";
			BigDecimal totalLines = DB.getSQLValueBD(rma.get_TrxName(), sqlGetTotalLine, io.getC_Invoice_ID());
			
			String sqlGetTax = "select sum(taxamt) from C_InvoiceTax where C_Invoice_ID = ?";
			BigDecimal TaxAmt = DB.getSQLValueBD(rma.get_TrxName(), sqlGetTax, io.getC_Invoice_ID());
			
			BigDecimal grandTotal = totalLines.add(TaxAmt);
			
			String sqlUpdateTotalLines = "";
			if(tax.get_ValueAsBoolean("istaxincluded")) 
				sqlUpdateTotalLines = "Update c_invoice set totallines = " + totalLines + " , grandtotal = " + totalLines + " where C_Invoice_ID = " + io.getC_Invoice_ID();
			else
				sqlUpdateTotalLines = "Update c_invoice set totallines = " + totalLines + " , grandtotal = " + grandTotal + " where C_Invoice_ID = " + io.getC_Invoice_ID();
			
			DB.executeUpdate(sqlUpdateTotalLines, rma.get_TrxName());

		}
		
	}
	
	private String generatePayment(MInvoice invoice, TCS_MRMA rma) {
		String sqlAmount = "Select sum(linenetamt) from m_rmaline where m_rma_id = ?";
		BigDecimal amt = DB.getSQLValueBD(get_TrxName(), sqlAmount, rma.getM_RMA_ID());
			
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
		payment.setIsPrepayment(false);
		payment.setIsReceipt(true);
		payment.saveEx();
		payment.processIt(DocAction.ACTION_Complete);
		payment.saveEx();

		invoice.setC_Payment_ID(payment.get_ID());
		invoice.saveEx();
		
		rma.set_ValueOfColumn("C_Payment_ID", payment.getC_Payment_ID());
		rma.saveEx();
		
		return "Payment with Document No: " + payment.getDocumentNo();
	}
	

}