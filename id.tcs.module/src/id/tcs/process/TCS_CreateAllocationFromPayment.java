package id.tcs.process;
import java.math.BigDecimal;
import java.util.List;
import java.util.logging.Level;

import org.adempiere.exceptions.AdempiereException;
import org.compiere.model.MAllocationHdr;
import org.compiere.model.MAllocationLine;
import org.compiere.model.MBPartner;
import org.compiere.model.MCash;
import org.compiere.model.MCashLine;
import org.compiere.model.MClient;
import org.compiere.model.MConversionRate;
import org.compiere.model.MConversionRateUtil;
import org.compiere.model.MDocTypeCounter;
import org.compiere.model.MInvoice;
import org.compiere.model.MOrder;
import org.compiere.model.MOrg;
import org.compiere.model.MPaymentAllocate;
import org.compiere.model.Query;
import org.compiere.model.TCS_MPayment;
import org.compiere.model.X_C_CashLine;
import org.compiere.process.DocAction;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;

public class TCS_CreateAllocationFromPayment extends SvrProcess{

	private String		m_processMsg = null;
	
	@Override
	protected void prepare() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected String doIt() throws Exception {
		// TODO Auto-generated method stub
		String where = "IsAllocated = 'N' and DocStatus = 'CO'";
		List<TCS_MPayment> lists = new Query(getCtx(), TCS_MPayment.Table_Name, where, get_TrxName())
				.addJoinClause("JOIN TCS_AllocateCharge tac on C_Payment.C_Payment_ID = tac.C_Payment_ID")
				.list();
		
		for(TCS_MPayment payment : lists) {
			boolean createdAllocationRecords = false;
			if (payment.getC_Charge_ID() != 0)
			{

				MAllocationHdr alloc = new MAllocationHdr(getCtx(), false, 
						payment.getDateTrx(), payment.getC_Currency_ID(), 
						Msg.translate(getCtx(), "C_Payment_ID")	+ ": " + payment.getDocumentNo(), 
						get_TrxName());
				alloc.setAD_Org_ID(payment.getAD_Org_ID());
				alloc.setDateAcct(payment.getDateAcct()); // in case date acct is different from datetrx in payment; IDEMPIERE-1532 tbayen
				if (!alloc.save())
				{
					log.severe("P.Allocations not created");
					return DocAction.STATUS_Invalid;
				}
				
				BigDecimal allocateAmount = payment.isReceipt() 
						? payment.getPayAmt() 
						: payment.getPayAmt().negate();
				
				MAllocationLine alloclineCr = new MAllocationLine(alloc);
				alloclineCr.setAD_Org_ID(payment.getAD_Org_ID());
				alloclineCr.setC_BPartner_ID(payment.getC_BPartner_ID());
				alloclineCr.setC_Payment_ID(payment.getC_Payment_ID());
				alloclineCr.setDateTrx(alloc.getDateTrx());
				alloclineCr.setAmount(allocateAmount);
				alloclineCr.saveEx();
				
				MAllocationLine alloclineDr = new MAllocationLine(alloc);
				alloclineDr.setAD_Org_ID(payment.getAD_Org_ID());
				alloclineDr.setC_BPartner_ID(payment.getC_BPartner_ID());
				alloclineDr.setDateTrx(alloc.getDateTrx());
				alloclineDr.setAmount(allocateAmount.negate());
				alloclineDr.setC_Charge_ID(payment.getC_Charge_ID());
				alloclineDr.saveEx();
				
				// added AdempiereException by zuhri
				if (!alloc.processIt(DocAction.ACTION_Complete))
					throw new AdempiereException("Failed when processing document - " + alloc.getProcessMsg());
				
				alloc.save(get_TrxName());
				payment.setIsAllocated(true);
			}
			else
			{
				createdAllocationRecords = payment.allocateIt();	//	Create Allocation Records
				payment.testAllocation();
			}

			//	Project update
			if (payment.getC_Project_ID() != 0)
			{
			//	MProject project = new MProject(getCtx(), getC_Project_ID());
			}
			//	Update BP for Prepayments
			if (payment.getC_BPartner_ID() != 0 && payment.getC_Invoice_ID() == 0 && payment.getC_Charge_ID() == 0 && MPaymentAllocate.get(payment).length == 0 && !createdAllocationRecords)
			{
				MBPartner bp = new MBPartner (getCtx(), payment.getC_BPartner_ID(), get_TrxName());
				DB.getDatabase().forUpdate(bp, 0);
				//	Update total balance to include this payment 
				BigDecimal payAmt = MConversionRate.convertBase(getCtx(), payment.getPayAmt(), 
						payment.getC_Currency_ID(), payment.getDateAcct(), payment.getC_ConversionType_ID(), payment.getAD_Client_ID(), payment.getAD_Org_ID());
				if (payAmt == null)
				{
					m_processMsg = MConversionRateUtil.getErrorMessage(getCtx(), "ErrorConvertingCurrencyToBaseCurrency",
							payment.getC_Currency_ID(), MClient.get(getCtx()).getC_Currency_ID(), payment.getC_ConversionType_ID(), payment.getDateAcct(), get_TrxName());
					return DocAction.STATUS_Invalid;
				}
				//	Total Balance
				BigDecimal newBalance = bp.getTotalOpenBalance();
				if (newBalance == null)
					newBalance = Env.ZERO;
				if (payment.isReceipt())
					newBalance = newBalance.subtract(payAmt);
				else
					newBalance = newBalance.add(payAmt);
					
				bp.setTotalOpenBalance(newBalance);
				bp.setSOCreditStatus();
				bp.saveEx();
			}		

			// @Trifon - CashPayments
			//if ( getTenderType().equals("X") ) {
			if ( payment.isCashbookTrx()) {
				// Create Cash Book entry
				if ( payment.getC_CashBook_ID() <= 0 ) {
					log.saveError("Error", Msg.parseTranslation(getCtx(), "@Mandatory@: @C_CashBook_ID@"));
					m_processMsg = "@NoCashBook@";
					return DocAction.STATUS_Invalid;
				}
				MCash cash = MCash.get (getCtx(), payment.getAD_Org_ID(), payment.getDateAcct(), payment.getC_Currency_ID(), get_TrxName());
				if (cash == null || cash.get_ID() == 0)
				{
					m_processMsg = "@NoCashBook@";
					return DocAction.STATUS_Invalid;
				}
				MCashLine cl = new MCashLine( cash );
				cl.setCashType( X_C_CashLine.CASHTYPE_GeneralReceipts );
				cl.setDescription("Generated From Payment #" + payment.getDocumentNo());
				cl.setC_Currency_ID( payment.getC_Currency_ID() );
				cl.setC_Payment_ID( payment.getC_Payment_ID() ); // Set Reference to payment.
				StringBuilder info=new StringBuilder();
				info.append("Cash journal ( ")
					.append(cash.getDocumentNo()).append(" )");				
				m_processMsg = info.toString();
				//	Amount
				BigDecimal amt = payment.getPayAmt();
				
				cl.setAmount( amt );
				//
				cl.setDiscountAmt( Env.ZERO );
				cl.setWriteOffAmt( Env.ZERO );
				cl.setIsGenerated( true );
				
				if (!cl.save(get_TrxName()))
				{
					m_processMsg = "Could not save Cash Journal Line";
					return DocAction.STATUS_Invalid;
				}
			}
			
			//	update C_Invoice.C_Payment_ID and C_Order.C_Payment_ID reference
			if (payment.getC_Invoice_ID() != 0)
			{
				MInvoice inv = new MInvoice(getCtx(), payment.getC_Invoice_ID(), get_TrxName());
				if (inv.getC_Payment_ID() != payment.getC_Payment_ID())
				{
					inv.setC_Payment_ID(payment.getC_Payment_ID());
					inv.saveEx();
				}
			}		
			if (payment.getC_Order_ID() != 0)
			{
				MOrder ord = new MOrder(getCtx(), payment.getC_Order_ID(), get_TrxName());
				if (ord.getC_Payment_ID() != payment.getC_Payment_ID())
				{
					ord.setC_Payment_ID(payment.getC_Payment_ID());
					ord.saveEx();
				}
			}

		}
		return "";
	}
}
