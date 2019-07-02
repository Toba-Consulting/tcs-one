package id.tcs.module.factory;

import org.adempiere.base.event.AbstractEventHandler;
import org.adempiere.base.event.IEventTopics;
import org.adempiere.base.event.LoginEventData;
import org.adempiere.exceptions.AdempiereException;
import org.compiere.model.I_C_Order;
import org.compiere.model.MAllocationHdr;
import org.compiere.model.MBankStatement;
import org.compiere.model.MInvoice;
import org.compiere.model.MPayment;
import org.compiere.model.X_AD_WF_Activity;
import org.compiere.util.CLogger;
import org.compiere.wf.MWFActivity;
import org.osgi.service.event.Event;

import id.tcs.validator.TCS_BankStatementDocValidator;
import id.tcs.validator.TCS_InvoiceValidator;
import id.tcs.validator.TCS_OrderValidator;
import id.tcs.validator.TCS_PaymentValidator;
import id.tcs.validator.TCS_WFActivityValidator;
import id.tcs.validator.TCS_MAllocationHdrValidator;

public class TCS_ValidatorFactory extends AbstractEventHandler {
	private CLogger log = CLogger.getCLogger(TCS_ValidatorFactory.class);


	@Override
	protected void initialize() {
		registerEvent(IEventTopics.AFTER_LOGIN);
		registerTableEvent(IEventTopics.DOC_BEFORE_COMPLETE, I_C_Order.Table_Name);
		registerTableEvent(IEventTopics.DOC_BEFORE_VOID, I_C_Order.Table_Name);
		registerTableEvent(IEventTopics.DOC_BEFORE_REACTIVATE, I_C_Order.Table_Name);
		registerTableEvent(IEventTopics.DOC_AFTER_REACTIVATE, I_C_Order.Table_Name);
		registerTableEvent(IEventTopics.PO_AFTER_NEW, X_AD_WF_Activity.Table_Name);
		registerTableEvent(IEventTopics.DOC_AFTER_COMPLETE, MAllocationHdr.Table_Name);
		registerTableEvent(IEventTopics.DOC_AFTER_REVERSEACCRUAL, MAllocationHdr.Table_Name);
		registerTableEvent(IEventTopics.DOC_AFTER_REVERSECORRECT, MAllocationHdr.Table_Name);		
		registerTableEvent(IEventTopics.DOC_AFTER_REVERSECORRECT, MPayment.Table_Name);		
		registerTableEvent(IEventTopics.DOC_AFTER_REVERSEACCRUAL, MPayment.Table_Name);
		registerTableEvent(IEventTopics.DOC_BEFORE_REVERSECORRECT, MPayment.Table_Name);		
		registerTableEvent(IEventTopics.DOC_BEFORE_REVERSEACCRUAL, MPayment.Table_Name);
		registerTableEvent(IEventTopics.DOC_AFTER_REVERSECORRECT, MInvoice.Table_Name);		
		registerTableEvent(IEventTopics.DOC_AFTER_REVERSEACCRUAL, MInvoice.Table_Name);
		registerTableEvent(IEventTopics.DOC_BEFORE_REVERSECORRECT, MInvoice.Table_Name);		
		registerTableEvent(IEventTopics.DOC_BEFORE_REVERSEACCRUAL, MInvoice.Table_Name);
		registerTableEvent(IEventTopics.DOC_AFTER_REACTIVATE, MBankStatement.Table_Name);
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
		else if(getPO(event).get_TableName().equals(MAllocationHdr.Table_Name)) {
			msg = TCS_MAllocationHdrValidator.executeEvent(event, getPO(event));
		}
		else if(getPO(event).get_TableName().equals(MBankStatement.Table_Name)) {
			msg = TCS_BankStatementDocValidator.executeEvent(event, getPO(event));
		}
		else if(getPO(event).get_TableName().equals(MBankStatement.Table_Name)) {
			msg = TCS_OrderValidator.executeEvent(event, getPO(event));
		}
		else if(getPO(event).get_TableName().equals(MPayment.Table_Name)) {
			msg = TCS_PaymentValidator.executeEvent(event, getPO(event));
		}
		else if(getPO(event).get_TableName().equals(MInvoice.Table_Name)) {
			msg = TCS_InvoiceValidator.executeEvent(event, getPO(event));
		}

		if (msg.length() > 0)
			throw new AdempiereException(msg);

	}


}
