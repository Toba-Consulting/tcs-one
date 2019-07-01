package id.tcs.module.factory;

import id.tcs.callout.TCS_CalloutAsset;
import id.tcs.callout.TCS_CalloutAssetMovement;
import id.tcs.callout.TCS_CalloutAssetReval;
import id.tcs.callout.TCS_CalloutAssetTransfer;
import id.tcs.callout.TCS_CalloutPayment;

import java.util.ArrayList;
import java.util.List;

import org.adempiere.base.IColumnCallout;
import org.adempiere.base.IColumnCalloutFactory;


public class TCS_CalloutFactory implements IColumnCalloutFactory {

	@Override
	public IColumnCallout[] getColumnCallouts(String tableName,
			String columnName) {
		List<IColumnCallout> list = new ArrayList<IColumnCallout>();
				
		if (tableName.equals("A_Asset_Reval")){
			list.add(new TCS_CalloutAssetReval());
		}

		if (tableName.equals("A_AssetTransfer")){
			list.add(new TCS_CalloutAssetTransfer());
		}

		if (tableName.equals("A_AssetMovement")){
			list.add(new TCS_CalloutAssetMovement());
		}

		if (tableName.equals("C_Payment")){
			list.add(new TCS_CalloutPayment());
		}

		if (tableName.equals("A_Asset")){
			list.add(new TCS_CalloutAsset());
		}

		return list != null ? list.toArray(new IColumnCallout[0]) : new IColumnCallout[0];
	}

}
