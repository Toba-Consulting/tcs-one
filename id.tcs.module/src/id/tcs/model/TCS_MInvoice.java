package id.tcs.model;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Properties;

import org.adempiere.exceptions.AdempiereException;
import org.compiere.model.MInOut;
import org.compiere.model.MInvoice;
import org.compiere.model.MInvoiceLine;
import org.compiere.model.MOrder;

public class TCS_MInvoice extends MInvoice{

	public TCS_MInvoice (Properties ctx, int C_Invoice_ID, String trxName)
	{
		super (ctx, C_Invoice_ID, trxName);
	}
	
	public TCS_MInvoice (Properties ctx, ResultSet rs, String trxName)
	{
		super (ctx, rs, trxName);
	}
	
	public TCS_MInvoice (MOrder order, int C_DocTypeTarget_ID, Timestamp invoiceDate)
	{
		super (order, C_DocTypeTarget_ID, invoiceDate);
	}
	
	public TCS_MInvoice(MInOut ship, Timestamp invoiceDate) {
		super(ship, invoiceDate);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String completeIt() {
		
		//Process Travel Expense
		int travelExpenseID=get_ValueAsInt("TCS_TravelExpense_ID");
		if (travelExpenseID>0) {
			MInvoiceLine [] invoiceLines = getLines(true);
			for (MInvoiceLine invoiceLine : invoiceLines) {
				int expenseLineID=invoiceLine.get_ValueAsInt("TCS_ExpenseLine_ID");
				TCS_MExpenseLine expenseLine = new TCS_MExpenseLine(getCtx(), expenseLineID, get_TrxName());
				if (expenseLine.isProcessed()) {
					throw new AdempiereException("Travel Expense On Invoice Line "+invoiceLine.getLine()+" Already Processed");
				}
				expenseLine.setProcessed(true);
				expenseLine.saveEx();

			}		
			
			TCS_MTravelExpense travelExpense = new TCS_MTravelExpense(getCtx(), travelExpenseID, get_TrxName());
			if (travelExpense.getC_Invoice_ID()>0) 
				throw new AdempiereException("Travel Expense No "+travelExpense.getDocumentNo()+" Already Processed");
			else
				travelExpense.setC_Invoice_ID(getC_Invoice_ID());
				travelExpense.setProcessed(true);

			travelExpense.saveEx();
		}
		return super.completeIt();		
	}
	
	@Override
	public boolean reverseAccrualIt() {
		
		unprocessTravelExpense();
		return super.reverseAccrualIt();
	}
	
	@Override
	public boolean reverseCorrectIt() {
		
		unprocessTravelExpense();
		return super.reverseCorrectIt();
	}
	
	private String unprocessTravelExpense(){
		if (get_ValueAsInt("TCS_TravelExpense_ID")>0) {
			MInvoiceLine [] invoiceLines = getLines(true);
			for (MInvoiceLine invoiceLine : invoiceLines) {
				int expenseLineID=invoiceLine.get_ValueAsInt("TCS_ExpenseLine_ID");
				TCS_MExpenseLine expenseLine = new TCS_MExpenseLine(getCtx(), expenseLineID, get_TrxName());
				expenseLine.setProcessed(false);
				
				TCS_MTravelExpense travelExpense = new TCS_MTravelExpense(getCtx(), expenseLine.getTCS_TravelExpense_ID(), get_TrxName());
				travelExpense.set_ValueOfColumn("C_Invoice_ID", null);
				travelExpense.setProcessed(false);
				expenseLine.saveEx();
				travelExpense.saveEx();
			}	
		}
		return "";
	}
}
