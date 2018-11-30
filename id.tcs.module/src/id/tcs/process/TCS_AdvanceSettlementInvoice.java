package id.tcs.process;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

import org.adempiere.exceptions.AdempiereException;
import org.compiere.model.MAllocationHdr;
import org.compiere.model.MAllocationLine;
import org.compiere.model.MBPartnerLocation;
import org.compiere.model.MDocType;
import org.compiere.model.MInvoice;
import org.compiere.model.MInvoiceLine;
import org.compiere.model.MPayment;
import org.compiere.model.Query;

import id.tcs.model.TCS_MAdvRequest;
import id.tcs.model.TCS_MAdvSettlement;
import id.tcs.model.TCS_MAdvSettlementLine;
import id.tcs.model.TCS_MDestSettlement;

import org.compiere.model.X_C_DocType;
import org.compiere.process.DocAction;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.Env;

public class TCS_AdvanceSettlementInvoice extends SvrProcess {
	int p_C_DocType_ID = 0;
	String p_TenderType = "";
	Timestamp p_DateTrx = null;
	Timestamp p_DateAcct = null;

	@Override
	protected void prepare() {
		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++) {
			if (para[i].getParameter() == null)
				;
			else if (para[i].getParameterName().equals(MPayment.COLUMNNAME_C_DocType_ID))
				p_C_DocType_ID = para[i].getParameterAsInt();
			else if (para[i].getParameterName().equals(MPayment.COLUMNNAME_TenderType))
				p_TenderType = para[i].getParameterAsString();
			else if (para[i].getParameterName().equals(MPayment.COLUMNNAME_DateTrx))
				p_DateTrx = para[i].getParameterAsTimestamp();
			else if (para[i].getParameterName().equals(MPayment.COLUMNNAME_DateAcct))
				p_DateAcct = para[i].getParameterAsTimestamp();
		}

	}

	@Override
	protected String doIt() throws Exception {
		TCS_MAdvSettlement settlement = new TCS_MAdvSettlement(getCtx(), getRecord_ID(), get_TrxName());
		if(settlement.getC_Invoice_ID()>0)
			throw new AdempiereException("Invoice sudah terbuat sebelumnya, silahkan cek kembali !");
		
		TCS_MAdvRequest request = (TCS_MAdvRequest) settlement.getTCS_AdvRequest();

		MInvoice inv = new MInvoice(getCtx(), 0, get_TrxName());
		int C_BPartner_Location_ID = new Query(getCtx(), MBPartnerLocation.Table_Name, "C_BPartner_ID =?",
				get_TrxName()).setOnlyActiveRecords(true).setParameters(new Object[] { request.getC_BPartner_ID() })
						.firstId();
		inv.setAD_Org_ID(settlement.getAD_Org_ID());
		inv.setC_DocTypeTarget_ID(p_C_DocType_ID);
		inv.setDateInvoiced(p_DateTrx);
		inv.setDateAcct(p_DateAcct);
		inv.setIsSOTrx(false);
		inv.setC_BPartner_Location_ID(C_BPartner_Location_ID);
		inv.setC_BPartner_ID(request.getC_BPartner_ID());
		inv.setC_Currency_ID(settlement.getC_Currency_ID());
		inv.setC_ConversionType_ID(settlement.getC_ConversionType_ID());
		inv.saveEx();
		
		settlement.setC_Invoice_ID(inv.getC_Invoice_ID());
		settlement.saveEx();
		
		List<TCS_MDestSettlement> settleDest = new Query(getCtx(), TCS_MDestSettlement.Table_Name,
				TCS_MDestSettlement.COLUMNNAME_TCS_AdvSettlement_ID + " =?", get_TrxName())
						.setParameters(new Object[] { settlement.getTCS_AdvSettlement_ID() })
						.list();		
		TCS_MDestSettlement[] SettleDestArray = settleDest.toArray(new TCS_MDestSettlement[settleDest.size()]);
		
		int defultTax = new Query(getCtx(), "C_Tax", "IsDefault='Y'", null).firstIdOnly();
		
		for (TCS_MDestSettlement settleDestLoop : SettleDestArray) {
			
			List<TCS_MAdvSettlementLine> settleLine = new Query(getCtx(), TCS_MAdvSettlementLine.Table_Name,
				TCS_MAdvSettlementLine.COLUMNNAME_TCS_DestSettlement_ID + " =?", get_TrxName())
						.setParameters(new Object[] { settleDestLoop.getTCS_DestSettlement_ID()})
						.list();
			TCS_MAdvSettlementLine[] lines = settleLine.toArray(new TCS_MAdvSettlementLine[settleLine.size()]);

			for (TCS_MAdvSettlementLine line : lines) {
				MInvoiceLine invL = new MInvoiceLine(inv);
				invL.setQty(line.getQty());
				invL.setPrice(line.getPriceEntered());
				invL.setC_Tax_ID(defultTax);
				invL.setC_Charge_ID(line.getC_Charge_ID());
				invL.setLineNetAmt(invL.getPriceEntered().multiply(invL.getQtyInvoiced()));
				invL.setLineNetAmt();
				invL.saveEx();	
			}
		}
			inv.processIt(DocAction.ACTION_Complete);
			inv.saveEx();
		
			MAllocationHdr alloc = new MAllocationHdr(getCtx(), true, // manual
				p_DateTrx, settlement.getC_Currency_ID(), Env.getContext(Env.getCtx(), "#AD_User_Name"), get_TrxName());
			alloc.setAD_Org_ID(settlement.getAD_Org_ID());
			alloc.setC_DocType_ID(MDocType.getDocType(X_C_DocType.DOCBASETYPE_PaymentAllocation));
			alloc.setDateTrx(p_DateTrx);
			alloc.setDateAcct(p_DateAcct);
			alloc.setDescription("Pelunasan PUM:" +request.getDocumentNo() + ", " +
				alloc.getDescriptionForManualAllocation(settlement.get_ValueAsInt("Requestor_ID"), get_TrxName()));
		
			alloc.saveEx();
		
			int paymentID = request.getC_Payment_ID();
			MPayment payment = new MPayment(getCtx(), paymentID, get_TrxName());
			
			BigDecimal allocAmt;
			if (payment.getPayAmt().compareTo(settlement.getGrandTotal()) > 0)
				allocAmt = settlement.getGrandTotal();
			else
				 allocAmt = payment.getPayAmt();
			MAllocationLine aLine = new MAllocationLine (alloc, allocAmt.negate(), 
				Env.ZERO, Env.ZERO, Env.ZERO);

			aLine.setDocInfo(request.getC_BPartner_ID(), 0, inv.get_ID());
			aLine.setPaymentInfo(paymentID, 0);
			aLine.saveEx();	
			alloc.processIt(DocAction.ACTION_Complete);
			alloc.saveEx();

			request.saveEx();
			return null;
		}	
}
