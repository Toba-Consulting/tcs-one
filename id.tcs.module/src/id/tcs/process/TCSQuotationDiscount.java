package id.tcs.process;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.logging.Level;

import id.tcs.model.MQuotation;
import id.tcs.model.MQuotationLine;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.Env;

public class TCSQuotationDiscount extends SvrProcess{

	int C_Quotation_ID = 0;
	BigDecimal discount = Env.ZERO;
	
	@Override
	protected void prepare() {
		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++) {
			String name = para[i].getParameterName();
			if (para[i].getParameter() == null) {
				;
			} else if (name.equals("Discount")) {
					discount = para[i].getParameterAsBigDecimal();
			} else {
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
			}
		}
	}

	@Override
	protected String doIt() throws Exception {
		C_Quotation_ID = getRecord_ID();
		MQuotation quotation = new MQuotation(getCtx(), C_Quotation_ID, get_TrxName());
		MQuotationLine quotationLines[] = quotation.getLines();
		
		//	check if price list 0 will return
		for (MQuotationLine quotationLine : quotationLines) {
			BigDecimal priceList = quotationLine.getPriceList();
			if(priceList.compareTo(Env.ZERO)<=0)
				return "Price List cant be empty";
		}
		
		//	set discount
		for (MQuotationLine quotationLine : quotationLines) {
			BigDecimal priceList = quotationLine.getPriceList();
			quotationLine.setPriceEntered(priceList.subtract(priceList.multiply(discount).divide(Env.ONEHUNDRED)).setScale(2, RoundingMode.HALF_UP));
			quotationLine.setPriceActual(priceList.subtract(priceList.multiply(discount).divide(Env.ONEHUNDRED)).setScale(2, RoundingMode.HALF_UP));
			quotationLine.setDiscount(discount);
			quotationLine.saveEx();
		}
		return "Successfull aplied discount";
	}

}
