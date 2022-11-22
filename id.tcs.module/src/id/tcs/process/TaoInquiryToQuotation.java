package id.tcs.process;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import org.adempiere.exceptions.AdempiereException;
import org.compiere.model.MPriceList;
import org.compiere.model.MProductPrice;
import org.compiere.model.MProductPricing;
import org.compiere.model.MRfQ;
import org.compiere.model.MRfQResponse;
import org.compiere.model.MRfQResponseLine;
import org.compiere.model.Query;
import org.compiere.process.DocAction;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;

import id.tcs.model.MInquiry;
import id.tcs.model.MInquiryLine;
import id.tcs.model.MQuotation;
import id.tcs.model.MQuotationLine;
import id.tcs.model.X_M_MatchInquiry;
import id.tcs.model.X_M_MatchQuotation;

public class TaoInquiryToQuotation extends SvrProcess{

	int C_Inquiry_ID = 0;
	int M_Warehouse_ID = 0;
	int C_DocTypeTarget_ID = 0;
	int C_PaymentTerm_ID = 0;
	int C_Tax_ID = 0;
	int p_M_PriceList_ID = 0;
	
	@Override
	protected void prepare() {
		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++) {
			String name = para[i].getParameterName();
			if (para[i].getParameter() == null) {
				;
			} else if (para[i].getParameterName().equalsIgnoreCase("M_Warehouse_ID")) {
				M_Warehouse_ID = para[i].getParameterAsInt();
			} else if (para[i].getParameterName().equalsIgnoreCase("C_DocTypeTarget_ID")) {
				C_DocTypeTarget_ID = para[i].getParameterAsInt();
			} else if (para[i].getParameterName().equalsIgnoreCase("C_PaymentTerm_ID")) {
				C_PaymentTerm_ID = para[i].getParameterAsInt();
			} else if (para[i].getParameterName().equalsIgnoreCase("C_Tax_ID")) {
				C_Tax_ID = para[i].getParameterAsInt();
			} else if (para[i].getParameterName().equalsIgnoreCase("M_PriceList_ID")) {
				p_M_PriceList_ID = para[i].getParameterAsInt();
			
			}
			
			else {
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
			}
		}
	}

	@Override
	protected String doIt() throws Exception {
		
		C_Inquiry_ID = getRecord_ID();
		
		if(C_Inquiry_ID == 0){
			return "Fill the Inquiry";
		}
		
		if (p_M_PriceList_ID <= 0) {
			return "Price List is Mandatory";
		}
		
		MPriceList priceList = new MPriceList(getCtx(), p_M_PriceList_ID, get_TrxName());
		
		String sql = "SELECT plv.M_PriceList_Version_ID "
				+ "FROM M_PriceList_Version plv "
				+ "WHERE plv.M_PriceList_ID=? "						//	1
				+ " AND plv.ValidFrom <= ? "
				+ "ORDER BY plv.ValidFrom DESC";
			//	Use newest price list - may not be future

		Timestamp today = new Timestamp(System.currentTimeMillis());
		int	M_PriceList_Version_ID = DB.getSQLValueEx(null, sql, p_M_PriceList_ID, today);
					
		MInquiry inquiry = new MInquiry(getCtx(), C_Inquiry_ID, get_TrxName());
		MInquiryLine inqLines[] = inquiry.getLines();
//		List<String> listDoc = new ArrayList<String>();
		
		if (inqLines.length == 0) 
			return "Inquiry has no lines";
		
		//FBI-2274 add validation create quotation must have rfq with responses @Febrian
		int[] matchInquiry_IDs = new Query(getCtx(), X_M_MatchInquiry.Table_Name, " C_Inquiry_ID = ? ", get_TrxName())
			.setParameters(C_Inquiry_ID).getIDs();
		for (int matchInquiry_ID : matchInquiry_IDs) {
			X_M_MatchInquiry matchInquiry = new X_M_MatchInquiry(getCtx(), matchInquiry_ID, get_TrxName());
			int RfQ_ID = matchInquiry.getC_RfQ_ID();
			if(RfQ_ID>0)
			{
				MRfQ rfq = new MRfQ(getCtx(), RfQ_ID, get_TrxName());
				
				if(rfq.getResponses(true, true).length <= 0)
					throw new AdempiereException("Cannot create quotation due to RfQ have no responses.");
			}
		}
		//FBI-2274
		
		boolean isNewProduct = false;
		MRfQ rfq = null;
		MRfQResponse rfqResponse = null;
		Map<Integer, MRfQResponseLine> rfqResponseMap = new HashMap<Integer, MRfQResponseLine>();
		
		for (MInquiryLine line : inqLines) {
			if (line.isIncludeRfQ()) {
				isNewProduct = true;
				break;
			}
		}
		if (isNewProduct) {
			/*
			if (inquiry.getC_RfQ_ID() <= 0)
				return "Inquiry has no related RfQ";
			
			rfq = MRfQ.get(getCtx(), inquiry.getC_RfQ_ID(), get_TrxName());
			
			if (!rfq.getDocStatus().equals(DocAction.STATUS_Completed))
				return "Error: RfQ status is not Completed";
			*/
			//@KevinY FBI - 2530
			String whereClause = "IsInternal='Y' AND isSelectedWinner='Y' AND C_RfQ_ID IN (SELECT DISTINCT C_RfQ_ID FROM M_MatchInquiry WHERE C_Inquiry_ID=?)";
			//@KevinY end
			List<MRfQResponse> rfqResponses = new Query(getCtx(), MRfQResponse.Table_Name, whereClause, get_TrxName())
							.setParameters(C_Inquiry_ID)
							.list();
			
			if (rfqResponses.isEmpty())
				return "Related RfQ has no selected internal response";
			
			for(MRfQResponse response : rfqResponses) {
				MRfQResponseLine[] respLines = response.getLines();
				
				for (MRfQResponseLine respLine : respLines) {
					rfqResponseMap.put(respLine.getC_RfQLine_ID(), respLine);
					
				}
			}
			
		}
		MQuotation quotation = new MQuotation(getCtx(), 0, get_TrxName());

		quotation.setDateAcct(today);
		quotation.setDateOrdered(today);
		quotation.setDatePromised(today);
		quotation.setDeliveryRule(MQuotation.DELIVERYRULE_Manual);
		quotation.setDeliveryViaRule(MQuotation.DELIVERYVIARULE_Pickup);
		quotation.setDocStatus(DocAction.STATUS_Drafted);
		quotation.setDocAction(DocAction.ACTION_Complete);
		quotation.setFreightCostRule(MQuotation.FREIGHTCOSTRULE_FreightIncluded);
		quotation.setInvoiceRule(MQuotation.INVOICERULE_Immediate);
		quotation.setIsActive(true);
		quotation.setIsApproved(true);
		quotation.setIsSOTrx(true);
		quotation.setPriorityRule(MQuotation.PRIORITYRULE_Medium);
		quotation.setPaymentRule(MQuotation.PAYMENTRULE_Cash);
		quotation.setM_Warehouse_ID(M_Warehouse_ID);
		quotation.setAD_Org_ID(inquiry.getAD_Org_ID());
		if (inquiry.get_ValueAsInt("AD_OrgTrx_ID") > 0) {
			quotation.setAD_OrgTrx_ID(inquiry.get_ValueAsInt("AD_OrgTrx_ID"));
		}
		if (inquiry.getC_Project_ID() > 0) {
		quotation.setC_Project_ID(inquiry.getC_Project_ID());
		}
		
		//@KevinY FBI - 2530
		if(( inquiry.getC_BPartner_ID() > 0 && inquiry.getC_BPartner_Location_ID() > 0 ) 
				&& inquiry.get_ValueAsInt("TCS_Lead_ID") <= 0){
			quotation.setC_BPartner_ID(inquiry.getC_BPartner_ID());
			quotation.setC_BPartner_Location_ID(inquiry.getC_BPartner_Location_ID());
			quotation.set_ValueOfColumn("TCS_Lead_ID", null);
		}else if((inquiry.getC_BPartner_ID() <= 0  && inquiry.getC_BPartner_Location_ID() <= 0) 
				&& inquiry.get_ValueAsInt("TCS_Lead_ID") > 0){
			quotation.set_ValueOfColumn("TCS_Lead_ID",inquiry.get_Value("TCS_Lead_ID"));
			quotation.set_ValueOfColumn("C_BPartner_ID", null);
			quotation.set_ValueOfColumn("C_BPartner_Location_ID", null);
		}
		//@KevinY end
		quotation.setC_DocType_ID(C_DocTypeTarget_ID);
		quotation.setC_DocTypeTarget_ID(C_DocTypeTarget_ID);
		quotation.setC_PaymentTerm_ID(C_PaymentTerm_ID);
		quotation.setM_PriceList_ID(p_M_PriceList_ID);
		quotation.setC_Tax_ID(C_Tax_ID);
		if (inquiry.getSalesRep_ID() > 0) {
		quotation.setSalesRep_ID(inquiry.getSalesRep_ID());
		}
		quotation.setGrandTotal(Env.ZERO);
		quotation.setTotalLines(Env.ZERO);
		quotation.saveEx();
		
		for (MInquiryLine inqLine : inqLines) {
			MQuotationLine quoLine = new MQuotationLine(quotation);
			
			//if inquiry line not new item
			if(!inqLine.isNewProduct()){
				quoLine.setDateOrdered(today);
				quoLine.setIsActive(true);
				quoLine.setAD_Org_ID(inqLine.getAD_Org_ID());
				
				MProductPricing pp = new MProductPricing (inqLine.getM_Product_ID(), 
						inquiry.getC_BPartner_ID(), inqLine.getQty(), true, null);
				pp.setM_PriceList_ID(p_M_PriceList_ID);
				pp.setM_PriceList_Version_ID(M_PriceList_Version_ID);
				pp.setPriceDate(today);

				//get currency_id
				
				quoLine.setC_Currency_ID(pp.getC_Currency_ID());
				quoLine.setC_Tax_ID(C_Tax_ID);
				//@KevinY FBI - 2530
				if (inquiry.getC_BPartner_Location_ID() != 0){
					quoLine.setC_BPartner_Location_ID(inquiry.getC_BPartner_Location_ID());
				}
				//@KevinY end
				quoLine.setC_UOM_ID(inqLine.getC_UOM_ID());
				
				
				//if includeRfQ, then getprice from responseLine, else from pricelist
				if(inqLine.isIncludeRfQ()){
					//int rfqLineID = inqLine.getC_RfQLine_ID();
					String sqlGet = "SELECT C_RfQLine_ID FROM M_MatchInquiry WHERE C_InquiryLine_ID=? AND C_RfQLine_ID IS NOT NULL";
					int rfqLineID = DB.getSQLValue(get_TrxName(), sqlGet, inqLine.getC_InquiryLine_ID());
				
					MRfQResponseLine respLine = rfqResponseMap.get(rfqLineID);
					quotation.set_ValueOfColumn("PHDNO",inquiry.get_Value("PHDNO"));
					quoLine.setPriceEntered((BigDecimal)respLine.get_Value("Price"));
					quoLine.setPriceActual((BigDecimal)respLine.get_Value("Price"));
					quoLine.setPriceList((BigDecimal)respLine.get_Value("Price"));
					quoLine.set_ValueOfColumn("BasePrice", (BigDecimal)respLine.get_Value("Price"));
					quoLine.setDeliveryDays(respLine.getDeliveryDays());
					quoLine.set_ValueOfColumn("Help", respLine.getHelp());
					MRfQResponse resp = new MRfQResponse(getCtx(), respLine.getC_RfQResponse_ID(), get_TrxName());
					quotation.set_ValueOfColumn("DeliveryDays", resp.getDeliveryDays());
					quotation.setDaysDue((Integer)resp.get_Value("DaysDue"));
					quotation.saveEx();
				}else{
					BigDecimal priceStd = Env.ZERO;
					BigDecimal priceLst = Env.ZERO;
					
					if (pp.getPriceStd()!= null) {
						priceStd = pp.getPriceStd();
					}
					if (pp.getPriceList()!= null) {
						priceLst = pp.getPriceList();
					}
					
					quoLine.setPriceEntered(priceStd);
					quoLine.setPriceActual(priceStd);
					quoLine.set_CustomColumn("BasePrice", priceStd);
					quoLine.setPriceList(priceLst);
				}
				
				quoLine.setProduct(inqLine.getProduct());
				quoLine.setM_Product_ID(inqLine.getM_Product_ID());
				quoLine.setQtyEntered(inqLine.getQty());
				quoLine.setQtyOrdered(inqLine.getQty());
			}
			
			//if inquiry line is new item
			else if(inqLine.isNewProduct()){
				//get price
				String sqlGet = "SELECT C_RfQLine_ID FROM M_MatchInquiry WHERE C_InquiryLine_ID=? AND C_RfQLine_ID IS NOT NULL";
				int rfqLineID = DB.getSQLValue(get_TrxName(), sqlGet, inqLine.getC_InquiryLine_ID());
				//@albert add try catch
				try{
				MRfQResponseLine respLine = rfqResponseMap.get(rfqLineID);
 
				quoLine.setAD_Org_ID(inqLine.getAD_Org_ID());
				quoLine.setDateOrdered(today);

				//get currency_id
				quoLine.setC_Currency_ID(priceList.getC_Currency_ID());
				quoLine.setC_Tax_ID(C_Tax_ID);
				//@KevinY FBI - 2530
				if(inquiry.getC_BPartner_Location_ID() > 0)
					quoLine.setC_BPartner_Location_ID(inquiry.getC_BPartner_Location_ID());
				//@KevinY end
				quoLine.setC_UOM_ID(inqLine.getC_UOM_ID());
				quoLine.setPriceEntered((BigDecimal)respLine.get_Value("Price"));
				quoLine.set_ValueOfColumn("BasePrice", (BigDecimal)respLine.get_Value("Price"));
				
				
				StringBuilder sqls = new StringBuilder();	
				sqls.append("select M_ProductPrice_ID From M_ProductPrice where M_PriceList_Version_ID="+M_PriceList_Version_ID+" AND M_Product_ID="+quoLine.getM_Product_ID());
				Integer M_ProductPrice_ID = DB.getSQLValue(null, sql.toString());
				if(M_ProductPrice_ID!=-1){
					MProductPrice productprice= new MProductPrice(Env.getCtx(),M_ProductPrice_ID,get_TrxName());
					quoLine.set_ValueOfColumn("PriceKondisi", productprice.getPriceList());
				}else{
					quoLine.set_ValueOfColumn("PriceKondisi",(BigDecimal)respLine.get_Value("PriceKondisi"));
				}
				quoLine.setPriceActual((BigDecimal)respLine.get_Value("Price"));
				quoLine.setPriceList((BigDecimal)respLine.get_Value("Price"));
				quoLine.setDeliveryDays(respLine.getDeliveryDays());
				MRfQResponse resp = new MRfQResponse(getCtx(), respLine.getC_RfQResponse_ID(), get_TrxName());
				quotation.set_ValueOfColumn("DeliveryDays", resp.getDeliveryDays());
				quotation.saveEx();
				quoLine.setDescription(inqLine.getDescription());
				quoLine.setQtyEntered(inqLine.getQty());
				quoLine.setQtyOrdered(inqLine.getQty());
				}catch(Exception e){
					e.printStackTrace();
				}
				//
			}
			
			if (inquiry.get_ValueAsInt("AD_OrgTrx_ID") > 0) {
				quoLine.setAD_OrgTrx_ID(inquiry.get_ValueAsInt("AD_OrgTrx_ID"));
			}
			if (inquiry.getC_Project_ID() > 0) {
				quoLine.setC_Project_ID(inquiry.getC_Project_ID());
			}

			if(inqLine.getC_Charge_ID() > 0){
				quoLine.setC_Charge_ID(inqLine.getC_Charge_ID());
			}
			quoLine.setM_Product_Category_ID(inqLine.getM_Product_Category_ID());
			quoLine.setSize(inqLine.getSize());
			quoLine.setDescription(inqLine.getDescription());
			quoLine.setProduct(inqLine.getProduct());
			quoLine.setLine(inqLine.getLineNo());
			quoLine.set_ValueOfColumn("FaktorKondisi", Env.ONE);
			quoLine.set_ValueOfColumn("C_InquiryLine_ID", inqLine.getC_InquiryLine_ID());			
			quoLine.saveEx();
			
			if(quoLine.get_Value("BasePrice")==null){
				quoLine.set_ValueOfColumn("BasePrice", quoLine.getPrice());
				quoLine.saveEx();
			}
			
			X_M_MatchQuotation match = new X_M_MatchQuotation(getCtx(), 0, get_TrxName());
			match.setAD_Org_ID(quoLine.getAD_Org_ID());
			match.setC_Quotation_ID(quoLine.getC_Quotation_ID());
			match.setC_QuotationLine_ID(quoLine.getC_QuotationLine_ID());
			match.setC_Inquiry_ID(C_Inquiry_ID);
			match.setC_InquiryLine_ID(inqLine.getC_InquiryLine_ID());
			match.setDateTrx(today);
			match.setQtyOrdered(quoLine.getQtyOrdered());
			match.saveEx();
		}
		
		String message = Msg.parseTranslation(getCtx(), "@GeneratedQuotation@ " + quotation.getDocumentNo());
		addBufferLog(0, null, null, message, quotation.get_Table_ID(), quotation.getC_Quotation_ID());
		
		return "";
	}

}