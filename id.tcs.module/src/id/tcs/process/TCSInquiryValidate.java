package id.tcs.process;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.logging.Level;

import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import id.tcs.model.MInquiry;

public class TCSInquiryValidate extends SvrProcess {

	private int p_C_Inquiry_ID=0;
	private int p_C_BPartner_ID=0;
	private int p_SalesRep_ID=0;
	private Timestamp p_DateOrdered=null;
	private Timestamp p_DateOrderedTo=null;
	@Override
	protected void prepare() {
		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++)
		{
			String name = para[i].getParameterName();
			if (para[i].getParameter() == null)
				;
			else if (para[i].getParameterName().equalsIgnoreCase("C_Inquiry_ID")) {
				p_C_Inquiry_ID = para[i].getParameterAsInt();
			}
			else if (para[i].getParameterName().equalsIgnoreCase("C_BPartner_ID")) {
				p_C_BPartner_ID = para[i].getParameterAsInt();
			}
			else if (para[i].getParameterName().equalsIgnoreCase("SalesRep_ID")) {
				p_SalesRep_ID = para[i].getParameterAsInt();
			}
			else if (para[i].getParameterName().equalsIgnoreCase("DateOrdered")) {
				p_DateOrdered = para[i].getParameterAsTimestamp();
				p_DateOrderedTo = para[i].getParameter_ToAsTimestamp();
			}
			else
				log.log(Level.SEVERE, "prepare - Unknown Parameter: " + name);
		}
	}	//	prepare

	@Override
	protected String doIt() throws Exception {
		/*
		m_C_Inquiry_ID = getRecord_ID();
		
		if (m_C_Inquiry_ID <= 0)
			return "Missing Inquiry";

		MInquiry inquiry = new MInquiry(getCtx(), m_C_Inquiry_ID, get_TrxName());
		
		if (!inquiry.getDocStatus().equalsIgnoreCase(DocAction.STATUS_Completed))
			return "Inquiry status is not Complete";
		
		String whereClause = "C_RfQ_ID IN (SELECT DISTINCT C_RfQ_ID "
				+ "FROM M_MatchInquiry WHERE C_Inquiry_ID=?)";
				
		List<MRfQ> rfqList = new Query(getCtx(), MRfQ.Table_Name, whereClause, get_TrxName())
		.setParameters(m_C_Inquiry_ID)
		.list();
		
		if (!rfqList.isEmpty()) {
			StringBuilder listRfQ = new StringBuilder();
			
			for (MRfQ rfq : rfqList) {
				
				String whereClause2 = "IsInternal='Y' AND IsComplete='Y' AND C_RfQ_ID=?";
						
				boolean match = new Query(getCtx(), MRfQResponse.Table_Name, whereClause2, get_TrxName())
				.setParameters(m_C_Inquiry_ID)
				.setOnlyActiveRecords(true)
				.match();
				
				if (!match) {
					listRfQ.append(rfq.getDocumentNo());
					listRfQ.append(",");
				}
			}
			listRfQ.deleteCharAt(listRfQ.length()-1);
			
			if (listRfQ.length()> 0) {
				return "Uncomplete RfQ: " + listRfQ.toString();
			} else {
				return "All RfQ has been complete, Inquiry can be converted to Quotation";
			}
			
			
		} else {
			return "Error: no RfQ found";
		}
		*/
		StringBuilder listInquiry = new StringBuilder();
		
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT DISTINCT m.C_Inquiry_ID, i.DocumentNo FROM M_MatchInquiry m ")
			.append("LEFT JOIN C_Inquiry i on i.C_Inquiry_ID = m.C_Inquiry_ID ")
			.append("LEFT JOIN C_RfQResponse r on r.C_RfQ_ID = m.C_RfQ_ID ")
			.append("WHERE r.IsInternal='Y' ")
			.append("AND r.IsComplete='Y' ")
			;
		
		if(p_C_Inquiry_ID > 0){
			sql.append("AND i.C_Inquiry_ID = ? ");
		}
		
		if(p_SalesRep_ID > 0){
			sql.append("AND i.SalesRep_ID = ? ");
		}
		
		if(p_C_BPartner_ID > 0){
			sql.append("AND i.C_BPartner_ID = ? ");
		}
		
		if(p_DateOrdered != null){
			if(p_DateOrderedTo == null){
				return "FIll all parameter Date Ordered ";
			}
			else{
				sql.append("AND i.DateOrdered  BETWEEN '").append(p_DateOrdered).append("' ")
					.append("AND '").append(p_DateOrderedTo).append("' ");
			}
		}
		sql.append("ORDER BY i.DocumentNO");
		
		ResultSet rs = null;
		PreparedStatement pstmt = null;
		int count = 0;
		try{
			pstmt = DB.prepareStatement(sql.toString(), get_TrxName());
			if(p_C_Inquiry_ID > 0){
				pstmt.setInt(count+=1, p_C_Inquiry_ID);
			}
			if(p_SalesRep_ID > 0){
				pstmt.setInt(count+=1, p_SalesRep_ID);
			}
			if(p_C_BPartner_ID > 0){
				pstmt.setInt(count+=1, p_C_BPartner_ID);
			}
			rs = pstmt.executeQuery();
			while(rs.next()){
				MInquiry inquiry = new MInquiry(getCtx(), rs.getInt(1), get_TrxName());
				inquiry.set_ValueOfColumn("IsResponseReceived", "Y");
				inquiry.saveEx();
				listInquiry.append(rs.getString(2));
				listInquiry.append(",");
			}
		}catch(Exception e){
			return "Error : "+e;
		}finally{
			DB.close(rs, pstmt);
			rs = null;
			pstmt = null;
		}
		
		if(listInquiry.length()>0){
			listInquiry.deleteCharAt(listInquiry.length()-1);
			return "Inquiry that have Response: "+listInquiry.toString();
		}else{
			return "Inquiry does not have any response at all";
		}
		
	}

}
