package id.tcs.process;

import java.sql.Timestamp;
import java.util.Iterator;
import java.util.logging.Level;

import org.adempiere.exceptions.AdempiereException;
import org.compiere.model.I_C_Order;
import org.compiere.model.MDocType;
import org.compiere.model.MInOutLine;
import org.compiere.model.MOrder;
import org.compiere.model.MOrderLine;
import org.compiere.model.MProduct;
import org.compiere.model.Query;

import id.tcs.model.I_M_MatchQuotation;
import id.tcs.model.MQuotation;
import id.tcs.model.MQuotationLine;
import id.tcs.model.X_M_MatchInquiry;
import id.tcs.model.X_M_MatchQuotation;
import org.compiere.model.X_TCS_AllocateCharge;

import org.compiere.process.DocAction;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.Env;
import org.compiere.util.Msg;

public class TCSQuotationToOrder extends SvrProcess{

	int C_Quotation_ID = 0;
	int C_QuotationLine_ID = 0;
	int p_C_DocType_ID = 0;
	int p_M_Warehouse_ID = 0;
	
	@Override
	protected void prepare() {
		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++) {
			String name = para[i].getParameterName();
			if (para[i].getParameter() == null) {
				;
			} else if (name.equals("C_DocType_ID")) {
					p_C_DocType_ID = para[i].getParameterAsInt();
			}else {
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
			}
		}
	}
	@Override
	protected String doIt() throws Exception {

		C_Quotation_ID = getRecord_ID();

		if (p_C_DocType_ID == 0) {
			return "Error: DocType is blank";
			
		} else {
			MDocType docType = MDocType.get(getCtx(), p_C_DocType_ID);
			if (!docType.getDocBaseType().equals(MDocType.DOCBASETYPE_SalesOrder) && (!docType.getDocSubTypeSO().equals(MDocType.DOCSUBTYPESO_StandardOrder)))
				return "Error: DocType must be Standard Order ";
			
		}

		MQuotation quotation = new MQuotation(getCtx(), C_Quotation_ID, get_TrxName());

		//Validation date valid
		/*//@win temporary comment
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date = sdf.parse(quotation.getUpdated().toString());
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		if (quotation.getDaysDue() > 0) {
			c.add(Calendar.DATE, quotation.getDaysDue());
		}
		Date validDate = sdf.parse(c.getTime().toString());
		
		if(date.after(validDate)){
			return "Valid Until Date has been expired !";
		}
		*/
		//Validate: Check if quotation has match quotation records
		
		//quotation can be convert to SO many times(sisi)
		/*
		if (quotation.hasMatchQuotationSO()) {
			return "Quotation has been converted";
		}
		*/
		
		MQuotationLine[] quoteLines = quotation.getLines();		
		
		//Validate: Quotation has lines
		if (quoteLines.length == 0) {
			return "No Quotation Line ";
		} 

		//Validate: Check Quotation status must be completed
		if (!(quotation.getDocStatus().equals(DocAction.STATUS_Completed))) 
			return "Quotation Status is not Completed";
		
		//Validate: Quotation must be winner
//		if (!quotation.isQuotationAccepted())
//			return "Quotation has not been declared as winner";
		
		
		
		//Validate: All quotation lines must have M_Product_ID
		boolean checkProductID = true;
		
		for (MQuotationLine quoteLine : quoteLines) {
			
			if (quoteLine.getM_Product_ID() == 0 && quoteLine.getC_Charge_ID() == 0)
				checkProductID = false;
		}
		
//		if (!checkProductID)
//		return "Only Completed and Winning Quotation with Products/Charge Can be Converted to Sales Order";
		
		
		//Validate : Check all quotationline has matchSO
		boolean checkMatch = true;
		
		for (MQuotationLine quoteLine : quoteLines) {
			String whereClause = " C_QuotationLine.C_QuotationLine_ID=? AND mm.C_OrderLine_ID IS NULL";
			boolean match = new Query(getCtx(), MQuotationLine.Table_Name, whereClause, get_TrxName())
					.addJoinClause("LEFT JOIN M_MatchQuotation mm ON C_QuotationLine.C_QuotationLine_ID = mm.C_QuotationLine_ID")
					.setParameters(quoteLine.get_ID()).match();
			
			if (match)
				checkMatch = false;
		}
		
		if (checkMatch)
			throw new AdempiereException("All QuotationLine has MatchSO");
		
		MOrder order = new MOrder(getCtx(), 0, get_TrxName());

		order.setClientOrg(Env.getContextAsInt(getCtx(), Env.AD_CLIENT_ID), quotation.getAD_Org_ID());
		order.setC_DocTypeTarget_ID(p_C_DocType_ID);
		order.setC_DocType_ID(p_C_DocType_ID);
		order.setIsSOTrx(true);
		//order.setC_Tax_ID(quotation.getC_Tax_ID());
		order.set_ValueOfColumn("C_Tax_ID", quotation.getC_Tax_ID());
		order.setC_BPartner_ID(quotation.getC_BPartner_ID());
		order.setC_BPartner_Location_ID(quotation.getC_BPartner_Location_ID());
		order.setSalesRep_ID(quotation.getSalesRep_ID());
		order.setM_Warehouse_ID(quotation.getM_Warehouse_ID());
		order.setDateOrdered(new Timestamp(System.currentTimeMillis()));
		order.setDateAcct(new Timestamp(System.currentTimeMillis()));
		order.setFreightAmt(Env.ZERO);
		order.setDeliveryRule(quotation.getDeliveryRule());
		order.setDeliveryViaRule(quotation.getDeliveryViaRule());
		order.setIsDelivered(false);
		order.set_ValueOfColumn("PHDNO", quotation.get_Value("PHDNO"));
		order.setIsInvoiced(false);
		order.setM_PriceList_ID(quotation.getM_PriceList_ID());
		order.setC_PaymentTerm_ID(quotation.getC_PaymentTerm_ID());
		order.setDocStatus(DocAction.STATUS_Drafted);
		order.setDocAction(DocAction.ACTION_Complete);
		order.setPaymentRule(quotation.getPaymentRule());
		order.setTotalLines(Env.ZERO);
		order.setGrandTotal(Env.ZERO);
		order.set_ValueOfColumn("Certificate", false);
		order.set_ValueOfColumn("MDRCertificate", false);
		order.set_ValueOfColumn("WarrantyCertificate", false);
		order.set_ValueOfColumn("TDSCertificate", false);
		order.set_ValueOfColumn("TestReport", false);
		order.set_ValueOfColumn("CertManufacture", false);
		order.set_ValueOfColumn("InsReport", false);
		order.set_ValueOfColumn("MillCertificate", false);
		order.set_ValueOfColumn("CertCompliance", false);
		order.set_ValueOfColumn("MSDSCertificate", false);
		/*
		order.setCertificate(false);
		order.setMDRCertificate(false);
		order.setWarrantyCertificate(false);
		order.setTDSCertificate(false);
		order.setTestReport(false);
		order.setCertManufacture(false);
		order.setInsReport(false);
		order.setMillCertificate(false);
		order.setCertCompliance(false);
		order.setMSDSCertificate(false);
		*/
		order.set_ValueOfColumn("DeliveryDays", quotation.get_Value("DeliveryDays"));
		if (quotation.getC_Project_ID() > 0)
			order.setC_Project_ID(quotation.getC_Project_ID());
		if (quotation.getC_Activity_ID() > 0)
			order.setC_Activity_ID(quotation.getC_Activity_ID());
		if (quotation.getC_Campaign_ID() > 0)
			order.setC_Campaign_ID(quotation.getC_Campaign_ID());
		if (quotation.getC_Currency_ID() > 0)
			order.setC_Currency_ID(quotation.getC_Currency_ID());
		if (quotation.getC_ConversionType_ID() > 0)
			order.setC_ConversionType_ID(quotation.getC_ConversionType_ID());
		if (quotation.getAD_OrgTrx_ID() > 0)
			order.setAD_OrgTrx_ID(quotation.getAD_OrgTrx_ID());
		
		order.saveEx();
			
		
		for (MQuotationLine quoteLine : quoteLines) {
			String whereClause = " C_QuotationLine_ID=? AND C_OrderLine_ID IS NOT NULL";
			boolean match = new Query(getCtx(), X_M_MatchQuotation.Table_Name, whereClause, get_TrxName())
					.setParameters(quoteLine.get_ID()).match();
			
			if(!match) {
			MOrderLine orderLine = new MOrderLine(order);
	//		if(quoteLine.get_ValueAsBoolean("IsAccepted")==true){
				if (quoteLine.getM_Product_ID() > 0)
					orderLine.setM_Product_ID(quoteLine.getM_Product_ID());
				if (quoteLine.getC_Charge_ID() > 0)
					orderLine.setC_Charge_ID(quoteLine.getC_Charge_ID());
				if (quoteLine.getAD_OrgTrx_ID() > 0)
					orderLine.setAD_OrgTrx_ID(quoteLine.getAD_OrgTrx_ID());
				if (quoteLine.getC_Project_ID() > 0)
					orderLine.setC_Project_ID(quoteLine.getC_Project_ID());
				if (quoteLine.getProduct() != null)
					orderLine.set_ValueOfColumn("Product", quoteLine.getProduct());
				if (quoteLine.getM_Product_Category_ID() > 0)
					orderLine.set_ValueOfColumn("M_Product_Category_ID", quoteLine.getM_Product_Category_ID());
				if (quoteLine.getSize() != null)
					orderLine.set_ValueOfColumn("Size", quoteLine.getSize());
				
				orderLine.setQtyEntered(quoteLine.getQtyEntered());
				orderLine.setQtyOrdered(quoteLine.getQtyOrdered());
				orderLine.setC_UOM_ID(quoteLine.getC_UOM_ID());
				orderLine.setPriceEntered(quoteLine.getPriceEntered());
				orderLine.setPriceActual(quoteLine.getPriceActual());
				orderLine.setDescription(quoteLine.getDescription());
				orderLine.setPriceList(quoteLine.getPriceList());
				orderLine.setLine(quoteLine.getLine());
				orderLine.set_ValueOfColumn("commissioncustomer", quoteLine.get_Value("commissioncustomer"));
				
				// set isBOM value for BOM drop
				MProduct prod = new MProduct(getCtx(), quoteLine.getM_Product_ID(), quoteLine.get_TrxName());
				orderLine.set_ValueNoCheck("IsBOM", prod.get_Value("IsBOM"));
				orderLine.saveEx();
				
				
				//Create Match Quotation Records
				X_M_MatchQuotation matchQuote = new X_M_MatchQuotation(getCtx(), 0, get_TrxName());
				matchQuote.setAD_Org_ID(quotation.getAD_Org_ID());
				matchQuote.setC_Quotation_ID(quotation.getC_Quotation_ID());
				matchQuote.setC_QuotationLine_ID(quoteLine.getC_QuotationLine_ID());
				matchQuote.setC_Order_ID(order.getC_Order_ID());
				matchQuote.setC_OrderLine_ID(orderLine.get_ID());
				matchQuote.setDateTrx(new Timestamp(System.currentTimeMillis()));
				matchQuote.setQtyOrdered(quoteLine.getQtyOrdered());
				matchQuote.saveEx();
		//	}
			}
		}
		
		String message = Msg.parseTranslation(getCtx(), "@GeneratedOrder@ " +order.getDocumentNo());
		addBufferLog(0, null, null, message, order.get_Table_ID(), order.getC_Order_ID());
		return "";
	}
}
