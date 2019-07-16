package id.tcs.validator;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.adempiere.base.event.IEventTopics;
import id.tcs.model.MQuotation;
import id.tcs.model.MQuotationLine;
import org.compiere.model.PO;
import id.tcs.model.X_M_MatchQuotation;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.osgi.service.event.Event;

public class TCS_QuotationLineValidator {

	public static String executeEvent(Event event, PO po) {
		String msgQuotation = "";
		MQuotationLine quotationLine = (MQuotationLine) po;
		
		if (event.getTopic().equals(IEventTopics.PO_AFTER_DELETE)){
			msgQuotation +=setTotalLines(quotationLine);
			msgQuotation += removeMatchQuotation(quotationLine);

		}
		if (event.getTopic().equals(IEventTopics.PO_AFTER_NEW) 
				|| event.getTopic().equals(IEventTopics.PO_AFTER_CHANGE)){
			msgQuotation +=setTotalLines(quotationLine);
		}

		
		return msgQuotation;

	}

	private static String removeMatchQuotation(MQuotationLine quotationLine) {
		StringBuilder sql = new StringBuilder("DELETE FROM ").append(X_M_MatchQuotation.Table_Name)
				.append(" WHERE C_QuotationLine_ID=?");
		DB.executeUpdateEx(sql.toString(), new Object[] {quotationLine.get_ID()}, quotationLine.get_TrxName());

		return "";
	}

	public static String setTotalLines(MQuotationLine quotationLine){
		BigDecimal price = Env.ZERO;
		MQuotation quotation = quotationLine.getParent();
		if(quotation.getTotalLines().compareTo(Env.ZERO)>0){
			StringBuilder sqlprice = new StringBuilder();
			sqlprice.append("SELECT SUM(LineNetAmt) FROM C_QuotationLine WHERE C_Quotation_ID = "+ quotation.getC_Quotation_ID());
			price = DB.getSQLValueBDEx(quotation.get_TrxName(), sqlprice.toString());
			
			MQuotation quot = new MQuotation(quotationLine.getCtx(), quotationLine.getC_Quotation_ID(), quotationLine.get_TrxName());
			quot.setTotalLines(price.setScale(0, RoundingMode.HALF_UP));
			quot.saveEx();
		}
		
		return "";
	}
	
}
