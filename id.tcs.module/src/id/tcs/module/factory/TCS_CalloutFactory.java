package id.tcs.module.factory;

import id.tcs.callout.CalloutAccountingSchemaDefault;
import id.tcs.callout.CalloutAccountingSchemaGL;
import id.tcs.callout.CalloutBPAcct;
import id.tcs.callout.CalloutBPGroupAcct;
import id.tcs.callout.CalloutBankCash;
import id.tcs.callout.CalloutChargeAcct;
import id.tcs.callout.CalloutFADefaultAccount;
import id.tcs.callout.CalloutProductAcct;
import id.tcs.callout.CalloutProductCategoryAcct;
import id.tcs.callout.CalloutProjectAcct;
import id.tcs.callout.CalloutTaxRateAcct;
import id.tcs.callout.CalloutWarehouseLocator;
import id.tcs.callout.TCS_CalloutAdvRequestLine;
import id.tcs.callout.TCS_CalloutAdvSettlement;
import id.tcs.callout.TCS_CalloutAdvSettlementLine;
import id.tcs.callout.TCS_CalloutDestSettlement;
import id.tcs.callout.TCS_CalloutInvoiceLine;
import id.tcs.model.MAcctSchemaDefault;
import id.tcs.model.MAcctSchemaGL;
import id.tcs.model.MBPCustomerAcct;
import id.tcs.model.MBPGroupAcct;
import id.tcs.model.MBPVendorAcct;
import id.tcs.model.MBankAccountAcct;
import id.tcs.model.MChargeAcct;
import id.tcs.model.MFADefaultAccount;
import id.tcs.model.MProductAcct;
import id.tcs.model.MProductCategoryAcct;
import id.tcs.model.MProjectAcct;
import id.tcs.model.MTaxAcct;
import id.tcs.model.MWarehouseAcct;

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
		
		if(tableName.equals("C_AcctSchema_GL"))
			list.add(new CalloutAccountingSchemaGL());
	
		if(tableName.equals("C_AcctSchema_Default"))
			list.add(new CalloutAccountingSchemaDefault());
		
		if(tableName.equals("M_Warehouse_Acct"))
			list.add(new CalloutWarehouseLocator());
		
		if(tableName.equals("C_BankAccount_Acct"))
			list.add(new CalloutBankCash());
		
		if(tableName.equals("C_Project_Acct"))
			list.add(new CalloutProjectAcct());
		
		if (tableName.equals("C_BP_Customer_Acct") || tableName.equals("C_BP_Vendor_Acct")) 
			list.add(new CalloutBPAcct());
		
		if(tableName.equals("C_BP_Group_Acct"))
			list.add(new CalloutBPGroupAcct());
		
		if(tableName.equals("C_Charge_Acct"))
			list.add(new CalloutChargeAcct());
		
		if(tableName.equals("M_Product_Acct"))
			list.add(new CalloutProductAcct());
		
		if(tableName.equals("M_Product_Category_Acct"))
			list.add(new CalloutProductCategoryAcct());
		
		if(tableName.equals("C_Tax_Acct"))
			list.add(new CalloutTaxRateAcct());
		
		if(tableName.equals("FA_DefaultAccount"))
			list.add(new CalloutFADefaultAccount());
		return list != null ? list.toArray(new IColumnCallout[0]) : new IColumnCallout[0];
	}

}
