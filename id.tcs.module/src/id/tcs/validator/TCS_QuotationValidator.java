package id.tcs.validator;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.adempiere.base.event.IEventTopics;
import id.tcs.model.MQuotation;
import id.tcs.model.MQuotationLine;

import org.compiere.model.MMatchPO;
import org.compiere.model.MOrder;
import org.compiere.model.MOrderLine;
import org.compiere.model.MPayment;
import org.compiere.model.PO;
import org.compiere.model.Query;

import id.tcs.model.X_M_MatchQuotation;
import org.compiere.util.DB;
import org.osgi.service.event.Event;

public class TCS_QuotationValidator {

	public static String executeEvent(Event event, PO po) {
		String msgQuotation = "";
		MQuotation quotation = (MQuotation) po;

		
		if (event.getTopic().equals(IEventTopics.DOC_BEFORE_VOID)){
			msgQuotation = checkQuotationDependency(quotation);
			msgQuotation = checkLinkedOrder(quotation);
		}

		if (event.getTopic().equals(IEventTopics.DOC_AFTER_VOID)){
			msgQuotation = removeMatchQuotation(quotation);
		}
		
		if (event.getTopic().equals(IEventTopics.DOC_BEFORE_REACTIVATE)) {
			msgQuotation = checkLinkedOrder(quotation);
		}
		
		
		return msgQuotation;

	}
	
	private static String removeMatchQuotation(MQuotation quotation) {
		StringBuilder sql = new StringBuilder("DELETE FROM ").append(X_M_MatchQuotation.Table_Name)
				.append(" WHERE C_Quotation_ID=?");
		DB.executeUpdateEx(sql.toString(), new Object[] {quotation.get_ID()}, quotation.get_TrxName());

		return "";
	}

	private static String checkQuotationDependency(MQuotation quotation) {
		MQuotationLine quotationLines[] = quotation.getLines();
		//StringBuilder sqlWhere = new StringBuilder("C_QuotationLine_ID=?");

		StringBuilder sql = new StringBuilder();
		ResultSet rs = null;
		PreparedStatement pstmt = null;

		String orderNo = "";
		int orderID = 0;
		boolean match = false;
		for (MQuotationLine quotationLine : quotationLines) {
			/*
			match = new Query(quotation.getCtx(), X_M_MatchQuotation.Table_Name, sqlWhere.toString(), quotation.get_TrxName())
			.setParameters(quotationLine.getC_QuotationLine_ID())
			.match();
			*/
			
			try{
				//sql.append("LEFT JOIN C_Order o ON o.C_Order_ID = m.C_Order_ID ");
				sql = new StringBuilder();
				sql.append("SELECT o.DocumentNo, m.C_Order_ID FROM M_MatchQuotation m LEFT JOIN C_Order o ON o.C_Order_ID = m.C_Order_ID ");
				sql.append("WHERE m.C_Quotation_ID=? AND o.documentno is not null ");
				pstmt = DB.prepareStatement(sql.toString(), quotation.get_TrxName());
				pstmt.setInt(1, quotationLine.getC_Quotation_ID());
				rs = pstmt.executeQuery();
				if(rs.next()){
					orderNo = rs.getString(1);
					orderID = rs.getInt(2);
				}
			}catch(Exception e){
				return "Error: "+e;
			}finally{
				DB.close(rs, pstmt);
				rs = null;
				pstmt = null;
				sql = null;
			}
			
			if (orderID == 0){
				match = false;
			}else{
				match = true;
			}

			if(match){
				return "Sales Order "+orderNo+" must be void first";
			}
		}
		
		return "";

	}
	
	public static String checkLinkedOrder(MQuotation quotation){
		/*
		String sqlWhere="q.C_Quotation_ID="+quotation.getC_Quotation_ID()+" AND q.DocStatus IN ('CO','CL','IP')";
		boolean match = new Query(quotation.getCtx(), MOrder.Table_Name, sqlWhere, quotation.get_TrxName())
						.addJoinClause("JOIN M_MatchQuotation mq ON mq.C_Order_ID=C_Order.C_Order_ID ")
						.addJoinClause("JOIN C_Quotation q ON q.C_Quotation_ID=mq.C_Quotation_ID")
						.match();
		
		if (match) return "Cannot Reactivate / Void : Linked Order Exist";
*/

		String sqlWhere="C_Quotation_ID="+quotation.getC_Quotation_ID();
		boolean match = new Query(quotation.getCtx(), X_M_MatchQuotation.Table_Name, sqlWhere, quotation.get_TrxName())
						.match();

		if (match) return "Cannot Reactivate / Void : Match Quotation Exist";

		return "";
	}

}
