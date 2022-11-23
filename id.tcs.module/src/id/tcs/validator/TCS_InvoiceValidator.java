package id.tcs.validator;

import org.adempiere.base.event.IEventTopics;
import org.adempiere.exceptions.AdempiereException;
import org.compiere.model.MAllocationLine;
import org.compiere.model.MInOut;
import org.compiere.model.MInvoice;
import org.compiere.model.MOrder;
import org.compiere.model.PO;
import org.compiere.model.Query;
import org.osgi.service.event.Event;

public class TCS_InvoiceValidator {


	public static String executeEvent(Event event, PO po){
		String msg = "";
		MInvoice invoice = (MInvoice) po;

		if ((event.getTopic().equals(IEventTopics.DOC_BEFORE_REVERSEACCRUAL)) ||
				(event.getTopic().equals(IEventTopics.DOC_BEFORE_REVERSECORRECT))) {
			msg = checkAllocation(invoice);
		}
		else if(event.getTopic().equals(IEventTopics.DOC_BEFORE_COMPLETE)){
			if (!invoice.isReversal())
				msg += validateRelatedInOut(invoice);
				msg += validateRelatedOrder(invoice);
				
		}

		return msg;
	}

	private static String checkAllocation(MInvoice invoice) {
		String whereClause = "C_Invoice_ID = ? AND cdr.docStatus='CO'";
		boolean match = new Query(invoice.getCtx(), MAllocationLine.Table_Name, whereClause, invoice.get_TrxName())
				.addJoinClause("JOIN C_AllocationHdr cdr ON cdr.c_AllocationHdr_ID = C_AllocationLine.C_AllocationHdr_ID")
				.setParameters(new Object[]{invoice.getC_Invoice_ID()})
				.match();

		if(match)
			throw new AdempiereException("Cannot reverse invoice.. Related allocation exists..");
		return "";
	}

	private static String validateRelatedInOut(MInvoice invoice) {		
		StringBuilder whereClause = new StringBuilder().append("EXISTS (SELECT 1 FROM M_InoutLine mil ")
				.append("JOIN C_InvoiceLine cil ON cil.M_InOutLine_ID=mil.M_InOutLine_ID ")
				.append("WHERE mil.M_InOut_ID=M_InOut.M_InOut_ID AND cil.C_Invoice_ID=?) ")
				.append("AND DocStatus!='CO'");

		boolean match = new Query(invoice.getCtx(), MInOut.Table_Name, whereClause.toString(), invoice.get_TrxName())
				.setParameters(invoice.get_ID())
				.setOnlyActiveRecords(true)
				.match();

		if (match)
			throw new IllegalArgumentException("Cannot complete Invoice.. Related Material Receipt / Shipment is not complete");
		
		return "";
	}
	
	private static String validateRelatedOrder(MInvoice invoice) {		
		StringBuilder whereClause = new StringBuilder().append("EXISTS (SELECT 1 FROM C_OrderLine col ")
				.append("JOIN C_InvoiceLine cil ON cil.C_OrderLine_ID=col.C_OrderLine_ID ")
				.append("WHERE col.C_Order_ID=C_Order.C_Order_ID AND cil.C_Invoice_ID=?) ")
				.append("AND DocStatus!='CO'");

		boolean match = new Query(invoice.getCtx(), MOrder.Table_Name, whereClause.toString(), invoice.get_TrxName())
				.setParameters(invoice.get_ID())
				.setOnlyActiveRecords(true)
				.match();

		if (match)
			throw new IllegalArgumentException("Cannot complete Invoice.. Related order is not complete");
		
		return "";
	}
}
