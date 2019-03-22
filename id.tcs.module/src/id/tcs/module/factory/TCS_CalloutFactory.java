package id.tcs.module.factory;

import id.tcs.callout.TCS_CalloutAdvRequestLine;
import id.tcs.callout.TCS_CalloutAdvSettlement;
import id.tcs.callout.TCS_CalloutAdvSettlementLine;
import id.tcs.callout.TCS_CalloutAssetMovement;
import id.tcs.callout.TCS_CalloutAssetReval;
import id.tcs.callout.TCS_CalloutAssetTransfer;
import id.tcs.callout.TCS_CalloutDestSettlement;
import id.tcs.callout.TCS_CalloutInvoiceLine;

import java.util.ArrayList;
import java.util.List;

import org.adempiere.base.IColumnCallout;
import org.adempiere.base.IColumnCalloutFactory;


public class TCS_CalloutFactory implements IColumnCalloutFactory {

	@Override
	public IColumnCallout[] getColumnCallouts(String tableName,
			String columnName) {
		List<IColumnCallout> list = new ArrayList<IColumnCallout>();
		
		if (tableName.equals("TCS_AdvSettlement")){
			list.add(new TCS_CalloutAdvSettlement());
		}
		
		if (tableName.equals("TCS_DestSettlement")){
			list.add(new TCS_CalloutDestSettlement());
		}
		
		if (tableName.equals("TCS_AdvSettlementLine")){
			list.add(new TCS_CalloutAdvSettlementLine());
		}
		
		if (tableName.equals("TCS_AdvRequestLine")){
			list.add(new TCS_CalloutAdvRequestLine());
		}
		
		if (tableName.equals("TCS_AdvSettlementLine")){
			list.add(new TCS_CalloutAdvSettlementLine());
		}
		
		if (tableName.equals("C_InvoiceLine")){
			list.add(new TCS_CalloutInvoiceLine());
		}
		
		if (tableName.equals("A_Asset_Reval")){
			list.add(new TCS_CalloutAssetReval());
		}

		if (tableName.equals("A_AssetTransfer")){
			list.add(new TCS_CalloutAssetTransfer());
		}

		if (tableName.equals("A_AssetMovement")){
			list.add(new TCS_CalloutAssetMovement());
		}

		return list != null ? list.toArray(new IColumnCallout[0]) : new IColumnCallout[0];
	}

}
