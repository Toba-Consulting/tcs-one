package id.taowi.custom.factory;

import java.util.ArrayList;
import java.util.List;

import org.adempiere.base.IColumnCallout;
import org.adempiere.base.IColumnCalloutFactory;


public class TCS_CalloutFactory implements IColumnCalloutFactory {

	@Override
	public IColumnCallout[] getColumnCallouts(String tableName,
			String columnName) {
		List<IColumnCallout> list = new ArrayList<IColumnCallout>();
		/*
		if (tableName.equals("C_Payment")){
			list.add(new SLU_CalloutPayment());
		}
		*/
		return list != null ? list.toArray(new IColumnCallout[0]) : new IColumnCallout[0];
	}

}
