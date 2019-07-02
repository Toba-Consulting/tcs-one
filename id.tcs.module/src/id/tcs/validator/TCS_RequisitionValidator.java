package id.tcs.validator;

import org.adempiere.base.event.IEventTopics;
import org.compiere.model.MRequisition;
import org.compiere.model.PO;
import org.compiere.model.Query;
import org.osgi.service.event.Event;

public class TCS_RequisitionValidator {

	public static String executeEvent(Event event, PO po){
		String msg = "";
		MRequisition req = (MRequisition) po;

		if ((event.getTopic().equals(IEventTopics.DOC_BEFORE_REVERSEACCRUAL)) ||
				(event.getTopic().equals(IEventTopics.DOC_BEFORE_REVERSECORRECT))) {
			msg = checkPO(req);
		}

		return msg;
	}
	
	public static String checkPO(MRequisition req){
		
		boolean match = false;
		String sqlWhere = "M_Requisition.M_Requisition_ID="+req.getM_Requisition_ID()+" AND co.DocStatus IN ('CO','CL') ";
		match = new Query(req.getCtx(), MRequisition.Table_Name, sqlWhere, req.get_TrxName())
				.addJoinClause("JOIN M_RequisitionLine rl on rl.M_Requisition_ID=M_Requisition.M_Requsition_ID ")
				.addJoinClause("JOIN C_OrderLine col on col.M_RequisitionLine_ID=rl.M_RequisitionLine_ID ")
				.addJoinClause("JOIN C_Order co on co.C_Order_ID=col.C_Order_ID AND co.IsSOTrx='N' ")
				.match();
		
		if (match)return "Active Purchase Order Referencing This Requisition Exist";
		return "";
	}
}
