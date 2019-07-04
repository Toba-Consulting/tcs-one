package id.tcs.validator;

import org.adempiere.base.event.IEventTopics;
import org.adempiere.exceptions.AdempiereException;
import org.compiere.model.MInOut;
import org.compiere.model.MInOutLine;
import org.compiere.model.MMatchInv;
import org.compiere.model.PO;
import org.compiere.model.Query;
import org.osgi.service.event.Event;

public class TCS_InOutValidator {
	
	public static String executeEvent(Event event, PO po) {
		String msg = "";
		MInOut inout = (MInOut) po;
		if (event.getTopic().equals(IEventTopics.DOC_BEFORE_REVERSEACCRUAL) ||
				event.getTopic().equals(IEventTopics.DOC_BEFORE_REVERSECORRECT)) {
			msg = checkMatchInvoice(inout);
		} else if(event.getTopic().equals(IEventTopics.DOC_BEFORE_COMPLETE)){
			msg = checkExistsNullOrderLine(inout);
		}		
		return msg;
	}
	
	public static String checkMatchInvoice(MInOut inout){
		
		String sqlWhereIDs = "M_InOut_ID="+inout.getM_InOut_ID();
		int [] IDs = new Query(inout.getCtx(), MInOutLine.Table_Name, sqlWhereIDs, inout.get_TrxName())
							.getIDs();
		
		if (IDs.length==0 || IDs == null) {
			return "";
		}
		
		String inoutLineIDs = "";
		for (int i : IDs) {
			inoutLineIDs += i;
			inoutLineIDs += ", ";
		}
		inoutLineIDs = inoutLineIDs.substring(0, inoutLineIDs.length()-2);
		
		String sqlWhere = "M_MatchInv.M_InOutLine_ID IN ("+inoutLineIDs+") AND ci.DocStatus IN ('CO','CL','IP')";
		boolean match = new Query(inout.getCtx(), MMatchInv.Table_Name, sqlWhere, inout.get_TrxName())
						.addJoinClause("JOIN C_InvoiceLine cil on cil.C_InvoiceLine_ID=M_MatchInv.C_InvoiceLine_ID")
						.addJoinClause("JOIN C_Invoice ci on ci.C_Invoice_ID=cil.C_Invoice_ID")
						.match();
		if (match) return "Cannot Reverse InOut : Match Invoice With Active Invoice Exists";
		return "";
	}
	
	private static String checkExistsNullOrderLine(MInOut inout)
	{
		String whereClause = "M_InOut_ID = ? AND C_OrderLine_ID is null";
		boolean match = new Query(inout.getCtx(), MInOutLine.Table_Name, whereClause, inout.get_TrxName())
		.setParameters(inout.get_ID())
		.setOnlyActiveRecords(true)
		.match();
		
		if(match)
			return "Cannot complete.. All shipment / receipt line(s) must be linked to order line";
		
		else return "";
	}
}
