package id.tcs.validator;

import id.tcs.model.TCS_MAdvRequest;
import id.tcs.model.TCS_MAdvSettlement;

import org.adempiere.base.event.IEventTopics;
import org.compiere.model.MAllocationHdr;
import org.compiere.model.MPayment;
import org.compiere.model.PO;
import org.compiere.model.Query;
import org.compiere.util.DB;
import org.osgi.service.event.Event;

public class TCS_PaymentValidator {

	public static String executeEvent(Event event, PO po){
		String msg = "";
		MPayment payment = (MPayment) po;

		if(event.getTopic().equals(IEventTopics.DOC_AFTER_REVERSEACCRUAL))
			msg = afterReverse(payment);
		
		if(event.getTopic().equals(IEventTopics.DOC_AFTER_REVERSECORRECT))
			msg = afterReverse(payment);
		
		return msg;
	}
	
	public  static String afterReverse(MPayment payment){
		
		//Set Tcs_AdvRequest.C_Payment_ID = NULL When Reverse when reverse C_Payment
		String sqlRequest="C_Payment_ID="+payment.getC_Payment_ID();
		int [] requestIDs = new Query(payment.getCtx(), TCS_MAdvRequest.Table_Name, sqlRequest, payment.get_TrxName()).getIDs();
		if (requestIDs.length>0) {
			
			TCS_MAdvRequest request;
			
			for (int i : requestIDs) {
				request= new TCS_MAdvRequest(payment.getCtx(), i, payment.get_TrxName());
				request.setC_Payment_ID(0);
				request.saveEx();
			}
	/*		
			String sqlRequestUpdate="UPDATE TCS_AdvRequest SET C_Payment_ID = NULL WHERE TCS_AdvRequest_ID=?";
			for (int i : requestIDs) {
				DB.executeUpdate(sqlRequestUpdate, i, payment.get_TrxName());
			}
	*/	
		}
		
		//Set Tcs_AdvSettlement.C_Payment_ID = NULL When Reverse when reverse C_Payment
		String sqlSettlement="C_Payment_ID="+payment.getC_Payment_ID();
		int [] settlementIDs = new Query(payment.getCtx(), TCS_MAdvSettlement.Table_Name, sqlSettlement, payment.get_TrxName()).getIDs();
		if (settlementIDs.length>0) {
		
			TCS_MAdvSettlement settlement;
			
			for (int i : settlementIDs) {
				settlement= new TCS_MAdvSettlement(payment.getCtx(), i, payment.get_TrxName());
				settlement.setC_Payment_ID(0);
				settlement.setisReimbursed(false);
				settlement.setisReturned(false);
				settlement.saveEx();
			}
			/*			
			String sqlSettlementUpdate="UPDATE TCS_AdvSettlement SET C_Payment_ID = NULL WHERE TCS_AdvRequest_ID=?";
			for (int i : settlementIDs) {
				DB.executeUpdate(sqlSettlementUpdate, i, payment.get_TrxName());
			}
			 */			
		}
		
		return "";
	}
}
