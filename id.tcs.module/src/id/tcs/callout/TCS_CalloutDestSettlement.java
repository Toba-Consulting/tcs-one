package id.tcs.callout;

import id.tcs.model.TCS_MDestRequest;

import java.util.Properties;

import org.adempiere.base.IColumnCallout;
import org.compiere.model.GridField;
import org.compiere.model.GridTab;
import org.compiere.util.Env;

public class TCS_CalloutDestSettlement implements IColumnCallout{

	private String destRequest(Properties ctx, int WindowNo, GridTab mTab,
			GridField mField, Object value, Object oldValue) {
		
		if (value!=null) {
			TCS_MDestRequest destRequest = new TCS_MDestRequest(Env.getCtx(),(Integer)value,null);
			
			mTab.setValue("DateFrom", destRequest.getDateFrom());
			mTab.setValue("DateTo", destRequest.getDateTo());
			mTab.setValue("HC_BaseCityFrom_ID", destRequest.getHC_BaseCityFrom_ID());
			mTab.setValue("HC_BaseCityTo_ID", destRequest.getHC_BaseCityTo_ID());
			mTab.setValue("IsReturnTrip", destRequest.isReturnTrip());
		}
		else{
			mTab.setValue("DateFrom", null);
			mTab.setValue("DateTo", null);
			mTab.setValue("HC_BaseCityFrom_ID", null);
			mTab.setValue("HC_BaseCityTo_ID", null);
			mTab.setValue("IsReturnTrip", "N");
		}
		
		return "";
	}
	
	
	
	@Override
	public String start(Properties ctx, int WindowNo, GridTab mTab,
			GridField mField, Object value, Object oldValue) {

		if(mField.getColumnName().equals("TCS_DestRequest_ID"))
			return destRequest(ctx, WindowNo, mTab, mField, value,
			oldValue);
		
		return null;
	}

}
