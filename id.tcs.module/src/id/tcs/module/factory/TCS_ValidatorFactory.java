package id.tcs.module.factory;

import org.adempiere.base.event.AbstractEventHandler;
import org.adempiere.base.event.IEventTopics;
import org.adempiere.base.event.LoginEventData;
import org.adempiere.exceptions.AdempiereException;
import org.compiere.model.I_AD_WF_Activity;
import org.compiere.model.I_C_AllocationHdr;
import org.compiere.model.I_C_BankStatement;
import org.compiere.model.I_C_Invoice;
import org.compiere.model.I_C_Order;
import org.compiere.model.I_C_OrderLine;
import org.compiere.model.I_C_Payment;
import org.compiere.model.I_C_PaymentAllocate;
import org.compiere.model.I_C_RfQ;
import org.compiere.model.I_C_RfQLine;
import org.compiere.model.I_M_InOut;
import org.compiere.model.I_M_Movement;
import org.compiere.model.I_M_RMA;
import org.compiere.model.I_M_Requisition;
import id.tcs.model.I_C_Quotation;
import id.tcs.model.I_C_QuotationLine;
import org.eevolution.model.I_DD_Order;
import org.eevolution.model.I_PP_Product_BOMLine;

import id.tcs.validator.TCS_BankStatementDocValidator;
import id.tcs.validator.TCS_DDOrderValidator;
import id.tcs.validator.TCS_InOutValidator;
import id.tcs.validator.TCS_InvoiceValidator;
import id.tcs.validator.TCS_MAllocationHdrValidator;
import id.tcs.validator.TCS_MovementValidator;
import id.tcs.validator.TCS_OrderLineValidator;
import id.tcs.validator.TCS_OrderValidator;
import id.tcs.validator.TCS_PPProductBomLineValidator;
import id.tcs.validator.TCS_PaymentAllocateValidator;
import id.tcs.validator.TCS_PaymentValidator;
import id.tcs.validator.TCS_QuotationLineValidator;
import id.tcs.validator.TCS_QuotationValidator;
import id.tcs.validator.TCS_RFQLineValidator;
import id.tcs.validator.TCS_RMAValidator;
import id.tcs.validator.TCS_RequisitionValidator;
import id.tcs.validator.TCS_RfQValidator;
import id.tcs.validator.TCS_WFActivityValidator;

import org.compiere.util.CLogger;
import org.osgi.service.event.Event;

public class TCS_ValidatorFactory extends AbstractEventHandler {
	private CLogger log = CLogger.getCLogger(TCS_ValidatorFactory.class);


	@Override
	protected void initialize() {
		registerEvent(IEventTopics.AFTER_LOGIN);
		
		//Quotation
		registerTableEvent(IEventTopics.DOC_BEFORE_REACTIVATE, I_C_Quotation.Table_Name);
		registerTableEvent(IEventTopics.DOC_BEFORE_VOID, I_C_Quotation.Table_Name);
		registerTableEvent(IEventTopics.DOC_AFTER_VOID, I_C_Quotation.Table_Name);

		//Quotation Line
		registerTableEvent(IEventTopics.PO_AFTER_NEW, I_C_QuotationLine.Table_Name);
		registerTableEvent(IEventTopics.PO_AFTER_CHANGE, I_C_QuotationLine.Table_Name);
		registerTableEvent(IEventTopics.PO_AFTER_DELETE, I_C_QuotationLine.Table_Name);
		
		//RfQ
		registerTableEvent(IEventTopics.DOC_AFTER_VOID, I_C_RfQ.Table_Name);
		
		//RfQ Line
		registerTableEvent(IEventTopics.PO_BEFORE_DELETE, I_C_RfQLine.Table_Name);
		registerTableEvent(IEventTopics.PO_BEFORE_DELETE, I_C_RfQLine.Table_Name);
		
		//Requisition
		registerTableEvent(IEventTopics.DOC_BEFORE_REVERSEACCRUAL, I_M_Requisition.Table_Name);
		registerTableEvent(IEventTopics.DOC_BEFORE_REVERSECORRECT, I_M_Requisition.Table_Name);
		registerTableEvent(IEventTopics.DOC_BEFORE_REACTIVATE, I_M_Requisition.Table_Name);
		registerTableEvent(IEventTopics.DOC_BEFORE_VOID, I_M_Requisition.Table_Name);
		
		//Order
		registerTableEvent(IEventTopics.DOC_BEFORE_COMPLETE, I_C_Order.Table_Name);
		registerTableEvent(IEventTopics.DOC_BEFORE_VOID, I_C_Order.Table_Name);
		registerTableEvent(IEventTopics.DOC_BEFORE_REACTIVATE, I_C_Order.Table_Name);
		registerTableEvent(IEventTopics.DOC_BEFORE_REVERSEACCRUAL, I_C_Order.Table_Name);
		registerTableEvent(IEventTopics.DOC_BEFORE_REVERSECORRECT, I_C_Order.Table_Name);
		registerTableEvent(IEventTopics.DOC_AFTER_REACTIVATE, I_C_Order.Table_Name);
		
		//Order Line
		registerTableEvent(IEventTopics.PO_BEFORE_DELETE, I_C_OrderLine.Table_Name);
		
		//RMA
		registerTableEvent(IEventTopics.DOC_BEFORE_REACTIVATE, I_M_RMA.Table_Name);
		registerTableEvent(IEventTopics.DOC_AFTER_COMPLETE, I_M_RMA.Table_Name);
		registerTableEvent(IEventTopics.DOC_AFTER_REACTIVATE, I_M_RMA.Table_Name);
		
		//InOut
		registerTableEvent(IEventTopics.DOC_BEFORE_REVERSECORRECT, I_M_InOut.Table_Name);		
		registerTableEvent(IEventTopics.DOC_BEFORE_REVERSEACCRUAL, I_M_InOut.Table_Name);
		
		//Invoice
		registerTableEvent(IEventTopics.DOC_BEFORE_REVERSECORRECT, I_C_Invoice.Table_Name);		
		registerTableEvent(IEventTopics.DOC_BEFORE_REVERSEACCRUAL, I_C_Invoice.Table_Name);
		registerTableEvent(IEventTopics.DOC_AFTER_REVERSECORRECT, I_C_Invoice.Table_Name);		
		registerTableEvent(IEventTopics.DOC_AFTER_REVERSEACCRUAL, I_C_Invoice.Table_Name);
		//registerTableEvent(IEventTopics.DOC_BEFORE_COMPLETE, I_C_Invoice.Table_Name);		
		
		//Payment
		registerTableEvent(IEventTopics.DOC_BEFORE_REVERSECORRECT, I_C_Payment.Table_Name);		
		registerTableEvent(IEventTopics.DOC_BEFORE_REVERSEACCRUAL, I_C_Payment.Table_Name);
		registerTableEvent(IEventTopics.DOC_AFTER_REVERSECORRECT, I_C_Payment.Table_Name);		
		registerTableEvent(IEventTopics.DOC_AFTER_REVERSEACCRUAL, I_C_Payment.Table_Name);
		
		//Payment Allocate
		registerTableEvent(IEventTopics.PO_AFTER_NEW, I_C_PaymentAllocate.Table_Name);
		
		//Allocation
		registerTableEvent(IEventTopics.DOC_AFTER_COMPLETE, I_C_AllocationHdr.Table_Name);
		registerTableEvent(IEventTopics.DOC_AFTER_REVERSEACCRUAL, I_C_AllocationHdr.Table_Name);
		registerTableEvent(IEventTopics.DOC_AFTER_REVERSECORRECT, I_C_AllocationHdr.Table_Name);	
		
		//Bank Statement
		registerTableEvent(IEventTopics.DOC_AFTER_REACTIVATE, I_C_BankStatement.Table_Name);

		//Movement
		registerTableEvent(IEventTopics.DOC_BEFORE_COMPLETE, I_M_Movement.Table_Name);
		registerTableEvent(IEventTopics.DOC_BEFORE_REVERSEACCRUAL, I_M_Movement.Table_Name);
		registerTableEvent(IEventTopics.DOC_BEFORE_REVERSECORRECT, I_M_Movement.Table_Name);
		
		//DD_Order
		registerTableEvent(IEventTopics.DOC_BEFORE_COMPLETE, I_DD_Order.Table_Name);
		//registerTableEvent(IEventTopics.DOC_BEFORE_VOID, I_DD_Order.Table_Name);
		registerTableEvent(IEventTopics.DOC_BEFORE_REACTIVATE, I_DD_Order.Table_Name);
		
		//WF Activity
		registerTableEvent(IEventTopics.PO_AFTER_NEW, I_AD_WF_Activity.Table_Name);
		
		//Product BOM Line
		registerTableEvent(IEventTopics.PO_AFTER_CHANGE, I_PP_Product_BOMLine.Table_Name);
		registerTableEvent(IEventTopics.PO_AFTER_NEW, I_PP_Product_BOMLine.Table_Name);
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
		
		//Quotation
		else if(getPO(event).get_TableName().equals(I_C_Quotation.Table_Name)) {
			msg = TCS_QuotationValidator.executeEvent(event, getPO(event));
		}
		
		//Quotation Line
		else if(getPO(event).get_TableName().equals(I_C_QuotationLine.Table_Name)) {
			msg = TCS_QuotationLineValidator.executeEvent(event, getPO(event));
		}
		
		//RfQ
		else if(getPO(event).get_TableName().equals(I_C_RfQ.Table_Name)) {
			msg = TCS_RfQValidator.executeEvent(event, getPO(event));
		}

		//RfQLine
		else if(getPO(event).get_TableName().equals(I_C_RfQLine.Table_Name)) {
			msg = TCS_RFQLineValidator.executeEvent(event, getPO(event));
		}

		//Requisition
		else if(getPO(event).get_TableName().equals(I_M_Requisition.Table_Name)) {
			msg = TCS_RequisitionValidator.executeEvent(event, getPO(event));
		}
		
		//RMA 
		else if(getPO(event).get_TableName().equals(I_M_RMA.Table_Name)) {
			msg = TCS_RMAValidator.executeEvent(event, getPO(event));
		}		
		
		//Order
		else if(getPO(event).get_TableName().equals(I_C_Order.Table_Name)) {
			msg = TCS_OrderValidator.executeEvent(event, getPO(event));
		}
		
		//Order Line
		else if (getPO(event).get_TableName().equals(I_C_OrderLine.Table_Name)) {
			msg = TCS_OrderLineValidator.executeEvent(event, getPO(event));
		}

		//InOut
		else if(getPO(event).get_TableName().equals(I_M_InOut.Table_Name)) {
			msg = TCS_InOutValidator.executeEvent(event, getPO(event));
		}

		//Invoice
		else if(getPO(event).get_TableName().equals(I_C_Invoice.Table_Name)) {
			msg = TCS_InvoiceValidator.executeEvent(event, getPO(event));
		}
		
		//Payment
		else if(getPO(event).get_TableName().equals(I_C_Payment.Table_Name)) {
			msg = TCS_PaymentValidator.executeEvent(event, getPO(event));
		}
				
		//Payment Allocate
		else if(getPO(event).get_TableName().equals(I_C_PaymentAllocate.Table_Name)) {
			msg = TCS_PaymentAllocateValidator.executeEvent(event, getPO(event));
		}

		//Allocation
		else if(getPO(event).get_TableName().equals(I_C_AllocationHdr.Table_Name)) {
			msg = TCS_MAllocationHdrValidator.executeEvent(event, getPO(event));
		}
		
		//Bank Statement
		else if(getPO(event).get_TableName().equals(I_C_BankStatement.Table_Name)) {
			msg = TCS_BankStatementDocValidator.executeEvent(event, getPO(event));
		}
		
		//DD Order
		else if(getPO(event).get_TableName().equals(I_DD_Order.Table_Name)) {
			msg = TCS_DDOrderValidator.executeEvent(event, getPO(event));
		}
		
		//Movement
		else if(getPO(event).get_TableName().equals(I_M_Movement.Table_Name)) {
			msg = TCS_MovementValidator.executeEvent(event, getPO(event));
		}
		
		//WF Activator
		else if(getPO(event).get_TableName().equals(I_AD_WF_Activity.Table_Name)){
			msg = TCS_WFActivityValidator.executeEvent(event, getPO(event));
		}
		
		//Product BOM Line
		else if(getPO(event).get_TableName().equals(I_PP_Product_BOMLine.Table_Name)) {
			msg = TCS_PPProductBomLineValidator.executeEvent(event, getPO(event));
		}

		if (msg.length() > 0)
			throw new AdempiereException(msg);

	}
}
