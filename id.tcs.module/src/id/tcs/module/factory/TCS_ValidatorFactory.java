package id.tcs.module.factory;

import org.adempiere.base.event.AbstractEventHandler;
import org.adempiere.base.event.IEventTopics;
import org.adempiere.base.event.LoginEventData;
import org.adempiere.exceptions.AdempiereException;
import org.compiere.model.I_C_Order;
//import org.compiere.model.MAllocationHdr;
import org.compiere.model.MBankStatement;
import org.compiere.model.MInOut;
import org.compiere.model.MInvoice;
import org.compiere.model.MMovement;
import org.compiere.model.MOrder;
import org.compiere.model.MOrderLine;
import org.compiere.model.MPayment;
import org.compiere.model.MPaymentAllocate;
import org.compiere.model.MRequisition;
import org.compiere.model.MRfQ;
import org.compiere.model.X_AD_WF_Activity;
import org.compiere.util.CLogger;
import org.compiere.wf.MWFActivity;
import org.eevolution.model.MDDOrder;
import org.osgi.service.event.Event;

import id.tcs.model.MQuotation;
import id.tcs.model.MQuotationLine;
import id.tcs.validator.TCS_BankStatementDocValidator;
import id.tcs.validator.TCS_DDOrderValidator;
import id.tcs.validator.TCS_InOutValidator;
import id.tcs.validator.TCS_InvoiceValidator;
import id.tcs.validator.TCS_MovementValidator;
import id.tcs.validator.TCS_OrderValidator;
import id.tcs.validator.TCS_PaymentAllocateValidator;
import id.tcs.validator.TCS_PaymentValidator;
import id.tcs.validator.TCS_QuotationLineValidator;
import id.tcs.validator.TCS_QuotationValidator;
import id.tcs.validator.TCS_RequisitionValidator;
import id.tcs.validator.TCS_RfQValidator;
import id.tcs.validator.TCS_WFActivityValidator;
import id.tcs.validator.TCS_MAllocationHdrValidator;
import id.tcs.validator.TCS_OrderLineValidator;

public class TCS_ValidatorFactory extends AbstractEventHandler {
	private CLogger log = CLogger.getCLogger(TCS_ValidatorFactory.class);


	@Override
	protected void initialize() {
		registerEvent(IEventTopics.AFTER_LOGIN);
		registerTableEvent(IEventTopics.DOC_BEFORE_COMPLETE, I_C_Order.Table_Name);
		registerTableEvent(IEventTopics.DOC_BEFORE_VOID, I_C_Order.Table_Name);
		registerTableEvent(IEventTopics.DOC_BEFORE_REACTIVATE, I_C_Order.Table_Name);
		registerTableEvent(IEventTopics.DOC_AFTER_REACTIVATE, I_C_Order.Table_Name);
		registerTableEvent(IEventTopics.DOC_BEFORE_REVERSECORRECT, MInOut.Table_Name);		
		registerTableEvent(IEventTopics.DOC_BEFORE_REVERSEACCRUAL, MInOut.Table_Name);
		registerTableEvent(IEventTopics.PO_AFTER_NEW, X_AD_WF_Activity.Table_Name);
//		registerTableEvent(IEventTopics.DOC_AFTER_COMPLETE, MAllocationHdr.Table_Name);
//		registerTableEvent(IEventTopics.DOC_AFTER_REVERSEACCRUAL, MAllocationHdr.Table_Name);
//		registerTableEvent(IEventTopics.DOC_AFTER_REVERSECORRECT, MAllocationHdr.Table_Name);		
		registerTableEvent(IEventTopics.DOC_AFTER_REVERSECORRECT, MPayment.Table_Name);		
		registerTableEvent(IEventTopics.DOC_AFTER_REVERSEACCRUAL, MPayment.Table_Name);
		registerTableEvent(IEventTopics.DOC_BEFORE_REVERSECORRECT, MPayment.Table_Name);		
		registerTableEvent(IEventTopics.DOC_BEFORE_REVERSEACCRUAL, MPayment.Table_Name);
		registerTableEvent(IEventTopics.DOC_AFTER_REVERSECORRECT, MInvoice.Table_Name);		
		registerTableEvent(IEventTopics.DOC_AFTER_REVERSEACCRUAL, MInvoice.Table_Name);
		registerTableEvent(IEventTopics.DOC_BEFORE_REVERSECORRECT, MInvoice.Table_Name);		
		registerTableEvent(IEventTopics.DOC_BEFORE_REVERSEACCRUAL, MInvoice.Table_Name);
		registerTableEvent(IEventTopics.DOC_AFTER_REACTIVATE, MBankStatement.Table_Name);
		registerTableEvent(IEventTopics.PO_AFTER_NEW, MPaymentAllocate.Table_Name);
		registerTableEvent(IEventTopics.PO_AFTER_NEW, MQuotationLine.Table_Name);
		registerTableEvent(IEventTopics.PO_AFTER_CHANGE, MQuotationLine.Table_Name);
		registerTableEvent(IEventTopics.PO_AFTER_DELETE, MQuotationLine.Table_Name);
		registerTableEvent(IEventTopics.DOC_BEFORE_REACTIVATE, MQuotation.Table_Name);
		registerTableEvent(IEventTopics.DOC_BEFORE_VOID, MQuotation.Table_Name);
		registerTableEvent(IEventTopics.DOC_AFTER_VOID, MQuotation.Table_Name);
		registerTableEvent(IEventTopics.DOC_AFTER_VOID, MRfQ.Table_Name);
		registerTableEvent(IEventTopics.DOC_BEFORE_REVERSEACCRUAL, MRequisition.Table_Name);
		registerTableEvent(IEventTopics.DOC_BEFORE_REVERSECORRECT, MRequisition.Table_Name);
		registerTableEvent(IEventTopics.DOC_BEFORE_REACTIVATE, MRequisition.Table_Name);
		registerTableEvent(IEventTopics.DOC_BEFORE_VOID, MRequisition.Table_Name);
		registerTableEvent(IEventTopics.PO_BEFORE_DELETE, MOrderLine.Table_Name);
		registerTableEvent(IEventTopics.DOC_BEFORE_COMPLETE, MDDOrder.Table_Name);
		registerTableEvent(IEventTopics.DOC_BEFORE_VOID, MDDOrder.Table_Name);
		registerTableEvent(IEventTopics.DOC_BEFORE_COMPLETE, MMovement.Table_Name);
		log.info("PROJECT MANAGEMENT EVENT MANAGER // INITIALIZED");
	}

	
	
	@Override
	protected void doHandleEvent(Event event) {
		String msg = "";
		
		if (event.getTopic().equals(IEventTopics.AFTER_LOGIN)) {
			LoginEventData eventData = getEventData(event);
			log.info(" topic="+event.getTopic()+" AD_Client_ID="+eventData.getAD_Client_ID()
					+" AD_Org_ID="+eventData.getAD_Org_ID()+" AD_Role_ID="+eventData.getAD_Role_ID()
					+" AD_User_ID="+eventData.getAD_User_ID());

		}		
		
		else if(getPO(event).get_TableName().equals(MWFActivity.Table_Name)){
			msg = TCS_WFActivityValidator.executeEvent(event, getPO(event));
		}
//		else if(getPO(event).get_TableName().equals(MAllocationHdr.Table_Name)) {
//			msg = TCS_MAllocationHdrValidator.executeEvent(event, getPO(event));
//		}
		else if(getPO(event).get_TableName().equals(MBankStatement.Table_Name)) {
			msg = TCS_BankStatementDocValidator.executeEvent(event, getPO(event));
		}
		else if(getPO(event).get_TableName().equals(MOrder.Table_Name)) {
			msg = TCS_OrderValidator.executeEvent(event, getPO(event));
		}
		else if (getPO(event).get_TableName().equals(MOrderLine.Table_Name)) {
			msg = TCS_OrderLineValidator.executeEvent(event, getPO(event));
		}
		else if(getPO(event).get_TableName().equals(MPayment.Table_Name)) {
			msg = TCS_PaymentValidator.executeEvent(event, getPO(event));
		}
		else if(getPO(event).get_TableName().equals(MInvoice.Table_Name)) {
			msg = TCS_InvoiceValidator.executeEvent(event, getPO(event));
		}
		else if(getPO(event).get_TableName().equals(MInOut.Table_Name)) {
			msg = TCS_InOutValidator.executeEvent(event, getPO(event));
		}
		else if(getPO(event).get_TableName().equals(MPaymentAllocate.Table_Name)) {
			msg = TCS_PaymentAllocateValidator.executeEvent(event, getPO(event));
		}
		else if(getPO(event).get_TableName().equals(MQuotation.Table_Name)) {
			msg = TCS_QuotationValidator.executeEvent(event, getPO(event));
		}
		else if(getPO(event).get_TableName().equals(MQuotationLine.Table_Name)) {
			msg = TCS_QuotationLineValidator.executeEvent(event, getPO(event));
		}
		else if(getPO(event).get_TableName().equals(MRfQ.Table_Name)) {
			msg = TCS_RfQValidator.executeEvent(event, getPO(event));
		}
		else if(getPO(event).get_TableName().equals(MRequisition.Table_Name)) {
			msg = TCS_RequisitionValidator.executeEvent(event, getPO(event));
		}
		
		else if(getPO(event).get_TableName().equals(MDDOrder.Table_Name)) {
			msg = TCS_DDOrderValidator.executeEvent(event, getPO(event));
		}
		else if(getPO(event).get_TableName().equals(MMovement.Table_Name)) {
			msg = TCS_MovementValidator.executeEvent(event, getPO(event));
		}

		if (msg.length() > 0)
			throw new AdempiereException(msg);

	}


}
