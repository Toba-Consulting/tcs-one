package id.tcs.process;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;

import org.compiere.model.MDocType;
import org.compiere.model.MRfQ;
import org.compiere.model.MRfQLine;
import org.compiere.model.Query;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import id.tcs.model.I_C_InquiryLine;
import id.tcs.model.MInquiry;
import id.tcs.model.MInquiryLine;
import id.tcs.model.X_M_MatchInquiry;

public class TCSInquiryToRfQ extends SvrProcess{

	int p_C_Inquiry_ID = 0;
	int C_RfQ_Topic_ID = 0;
	int C_Currency_ID = 0;
	Timestamp DateResponse = null;
	
	@Override
	protected void prepare() {
		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++) {
			String name = para[i].getParameterName();
			if (para[i].getParameter() == null) {
				;
			} else if (para[i].getParameterName().equalsIgnoreCase("C_RfQ_Topic_ID")) {
				C_RfQ_Topic_ID = para[i].getParameterAsInt();
			}else if (para[i].getParameterName().equalsIgnoreCase("C_Currency_ID")) {
				C_Currency_ID = para[i].getParameterAsInt();
			}else if (para[i].getParameterName().equalsIgnoreCase("DateResponse")) {
				DateResponse = para[i].getParameterAsTimestamp();
			}else {
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
			}
		}
		p_C_Inquiry_ID = getRecord_ID();
	}

	@Override
	protected String doIt() throws Exception {
		if(p_C_Inquiry_ID <= 0){
			return "Inquiry is mandatory";
		}
		
		MInquiry inquiry = new MInquiry(getCtx(), p_C_Inquiry_ID, get_TrxName());
		ResultSet rs = null;
		StringBuilder sql = new StringBuilder();
		PreparedStatement pstmt = null;
		
		List<Integer> listCategory = new ArrayList<Integer>();
		List<String> listDocument = new ArrayList<String>();
		StringBuilder msg = new StringBuilder();
		
		try{
			sql.append("SELECT DISTINCT M_Product_Category_ID FROM C_InquiryLine il ")
				.append("WHERE C_Inquiry_ID = ")
				.append(p_C_Inquiry_ID)
				.append(" AND IsIncludeRfQ = 'Y' AND NOT EXISTS (SELECT 1 FROM M_MatchInquiry mi ")
				.append("WHERE mi.C_InquiryLine_ID=il.C_InquiryLine_ID AND mi.C_Inquiry_ID= ")
				.append(p_C_Inquiry_ID).append(" AND mi.C_RfQLine_ID IS NOT NULL) ");
			pstmt = DB.prepareStatement(sql.toString(), get_TrxName());
			rs = pstmt.executeQuery();
			while(rs.next()){
				listCategory.add(rs.getInt(1));
			}
		}catch(Exception e){
			return "Error : "+e;
		}finally{
			DB.close(rs, pstmt);
			rs = null;
			pstmt = null;
		}
		
		Collections.sort(listCategory);
		
		for(int i=0;i<listCategory.size();i++){
			MInquiryLine inqLines[] = getLines(p_C_Inquiry_ID, listCategory.get(i));
			if (inqLines.length == 0)
				continue;
					
			MRfQ rfq = new MRfQ(getCtx(), 0, get_TrxName());
			rfq.setAD_Org_ID(inquiry.getAD_Org_ID());
			rfq.setC_RfQ_Topic_ID(C_RfQ_Topic_ID);
			rfq.setIsSelfService(false);
			rfq.setIsQuoteAllQty(false);
			rfq.setIsInvitedVendorsOnly(true);
			rfq.set_ValueOfColumn("DateDoc", (new Timestamp(System.currentTimeMillis())));
			rfq.setIsQuoteTotalAmt(false);
			rfq.setIsRfQResponseAccepted(false);
			//@KevinY FBI - 2530 
			//@win
			//rfq.setC_BPartner_ID(inquiry.getC_BPartner_ID());
			//rfq.setC_BPartner_Location_ID(inquiry.getC_BPartner_Location_ID());
			if(inquiry.getC_BPartner_ID() > 0)
				rfq.setC_BPartner_ID(inquiry.getC_BPartner_ID());
			if(inquiry.getC_BPartner_Location_ID() > 0)
				rfq.setC_BPartner_Location_ID(inquiry.getC_BPartner_Location_ID());
			//@win
			//@KevinY end
			rfq.setName(inquiry.getName());
			rfq.setSalesRep_ID(inquiry.getSalesRep_ID());
			if (inquiry.getC_Project_ID() > 0) {
				rfq.set_ValueOfColumn("C_Project_ID",(inquiry.getC_Project_ID()));
			}
			if (inquiry.get_ValueAsInt("AD_OrgTrx_ID") > 0) {
				rfq.set_ValueOfColumn("AD_OrgTrx_ID", inquiry.get_ValueAsInt("AD_OrgTrx_ID"));
			}
			if (inquiry.get_ValueAsString("Help").length()>0){
				rfq.setHelp(inquiry.get_ValueAsString("Help"));
			}
			rfq.setC_Currency_ID(C_Currency_ID);
			rfq.setDateResponse(DateResponse);
			rfq.setDescription(inquiry.getDescription());
			rfq.set_ValueOfColumn("M_Product_Category_ID",listCategory.get(i));
			rfq.set_ValueOfColumn("C_DocType_ID",MDocType.getDocType("RFQ"));
			rfq.setDateWorkStart(new Timestamp(System.currentTimeMillis()));
			rfq.setDateWorkComplete(new Timestamp(System.currentTimeMillis()));
			rfq.saveEx();
			listDocument.add(rfq.getDocumentNo());
			int line = 0;
			for (MInquiryLine inqLine : inqLines) {
				if(listCategory.get(i)==inqLine.getM_Product_Category_ID() && inqLine.isIncludeRfQ()){
					MRfQLine rfqline = new MRfQLine(rfq);
					rfqline.setLine(inqLine.getLineNo());
					rfqline.setAD_Org_ID(inqLine.getAD_Org_ID());
					if (inqLine.get_ValueAsInt("AD_OrgTrx_ID") > 0) {
						rfqline.set_ValueOfColumn("AD_OrgTrx_ID", inquiry.get_ValueAsInt("AD_OrgTrx_ID"));
					}
					if(inqLine.getM_Product_ID() > 0){
						rfqline.setM_Product_ID(inqLine.getM_Product_ID());
					}
					if(inqLine.getC_Charge_ID() > 0){
						rfqline.set_ValueOfColumn("C_Charge_ID",inqLine.getC_Charge_ID());
					}
					rfqline.set_ValueOfColumn("Qty",inqLine.getQty());
					rfqline.setDescription(inqLine.getDescription());
					rfqline.set_ValueOfColumn("C_UOM_ID",inqLine.getC_UOM_ID());
					rfqline.set_ValueOfColumn("M_Product_Category_ID",inqLine.getM_Product_Category_ID());
					rfqline.set_ValueOfColumn("Size",inqLine.getSize());
					rfqline.setHelp(inqLine.get_ValueAsString("Help"));
					rfqline.setIsActive(true);
					/*
					if(inqLine.isIncludeRfQ()){
						rfqline.setIsDescription(true);
					}*/
					if(inqLine.isNewProduct()){
						rfqline.set_ValueOfColumn("Product",inqLine.getProduct());
						rfqline.set_ValueOfColumn("IsDescription",true);
					}
					rfqline.saveEx();
					
					X_M_MatchInquiry match = new X_M_MatchInquiry(getCtx(), 0, get_TrxName());
					match.setAD_Org_ID(inqLine.getAD_Org_ID());
					match.setDateTrx(new Timestamp(System.currentTimeMillis()));
					match.setQty(inqLine.getQty());
					match.setC_RfQ_ID(rfq.getC_RfQ_ID());
					match.setC_RfQLine_ID(rfqline.getC_RfQLine_ID());
					match.setC_Inquiry_ID(inqLine.getC_Inquiry_ID());
					match.setC_InquiryLine_ID(inqLine.getC_InquiryLine_ID());
					match.saveEx();
					
				}
			}
		}
		
		if(listDocument.isEmpty()){
			return "No Product required RfQ";
		}
		msg.append("RfQ Created ");
		for(int i=0;i<listDocument.size();i++){
			if (i==listDocument.size()){
				msg.append(listDocument.get(i))
				.append("  ");
			}
			else{
				msg.append(listDocument.get(i))
				.append(", ");
			
			}
				
				}
		
		inquiry.set_ValueOfColumn("PHDNO",msg.toString());
		inquiry.saveEx();
		return msg.toString();	
	}
	
	private MInquiryLine[] getLines(int inquiryID, int productCategoryID) {

		StringBuilder whereClause = new StringBuilder(I_C_InquiryLine.COLUMNNAME_C_Inquiry_ID).append("=? AND ")
				.append(I_C_InquiryLine.COLUMNNAME_M_Product_Category_ID).append("=? AND ")
				.append("NOT EXISTS (SELECT 1 FROM M_MatchInquiry mi ")
				.append("WHERE mi.C_InquiryLine_ID=C_InquiryLine.C_InquiryLine_ID AND mi.C_Inquiry_ID=? AND mi.C_RfQLine_ID IS NOT NULL)");

		List<MInquiryLine> list = new Query(getCtx(),MInquiryLine.Table_Name, whereClause.toString(), get_TrxName())
				.setParameters(new Object[] {inquiryID, productCategoryID, inquiryID})
				.setOnlyActiveRecords(true)
				.list();
		
		MInquiryLine[] m_inquiry = new MInquiryLine[list.size()];
		list.toArray(m_inquiry);
		return m_inquiry;
	}
	
}
