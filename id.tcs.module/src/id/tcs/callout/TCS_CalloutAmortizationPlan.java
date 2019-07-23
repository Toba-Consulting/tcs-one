package id.tcs.callout;

import java.util.Properties;

import org.adempiere.base.IColumnCallout;
import org.compiere.model.CalloutEngine;
import org.compiere.model.GridField;
import org.compiere.model.GridTab;
import org.compiere.model.MPeriod;
import org.compiere.util.Env;

public class TCS_CalloutAmortizationPlan extends CalloutEngine implements IColumnCallout{

	@Override
	public String start(Properties ctx, int WindowNo, GridTab mTab,
			GridField mField, Object value, Object oldValue) {
		
		if (mField.getColumnName().equals("Start_Period_ID")){
			return StartPeriod(ctx, WindowNo, mTab, mField, value);
		}
		
		return "";
	}

	/**
	 * @param ctx, windowNo, mTab, mField, value
	 * @return empty string
	 */
	//@Phie
	private String StartPeriod(Properties ctx, int windowNo, GridTab mTab,
			GridField mField, Object value) {
		
		if(value == null)
		{
			int C_Period_ID = (int) mTab.getValue("C_Period_ID");
			MPeriod period = new MPeriod(Env.getCtx(), C_Period_ID, null);
			mTab.setValue("DateAcct", period.getEndDate());
			mTab.setValue("DateDoc", period.getEndDate());
			return "";
		}
		
		int Start_Period_ID = (int) value;
		MPeriod period = new MPeriod(Env.getCtx(), Start_Period_ID, null);
		mTab.setValue("DateAcct", period.getEndDate());
		mTab.setValue("DateDoc", period.getEndDate());
		
		return "";
	}
	//end phie
}
