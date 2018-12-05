package id.tcs.callout;

import id.tcs.model.TCS_MExpenseLine;

import java.util.Properties;

import org.adempiere.base.IColumnCallout;
import org.compiere.model.GridField;
import org.compiere.model.GridTab;
import org.compiere.util.Env;

public class TCS_CalloutInvoiceLine implements IColumnCallout{

	
	private String travelExpenseLine(Properties ctx, int WindowNo, GridTab mTab,
			GridField mField, Object value, Object oldValue) {
		
		if (value==null) {
			return "";
		}
		
		TCS_MExpenseLine expenseLine = new TCS_MExpenseLine(Env.getCtx(), (Integer)value, null);
		
		mTab.setValue("C_Charge_ID", expenseLine.getC_Charge_ID());
		mTab.setValue("PriceActual", expenseLine.getPriceEntered());
		mTab.setValue("QtyEntered", expenseLine.getQty());
		mTab.setValue("QtyInvoiced", expenseLine.getQty());
		mTab.setValue("LineNetAmt", expenseLine.getAmt());
		return "";
	}
	
	
	@Override
	public String start(Properties ctx, int WindowNo, GridTab mTab,
			GridField mField, Object value, Object oldValue) {

		if(mField.getColumnName().equals("TCS_ExpenseLine_ID"))
			return travelExpenseLine(ctx, WindowNo, mTab, mField, value,oldValue);
		
	return null;
	}

}
