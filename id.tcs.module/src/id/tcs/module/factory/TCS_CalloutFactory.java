package id.tcs.module.factory;

import java.util.ArrayList;
import java.util.List;

import org.adempiere.base.IColumnCallout;
import org.adempiere.base.IColumnCalloutFactory;
import org.compiere.model.MOrderLine;
import org.compiere.model.TCS_MInventoryLine;
import org.eevolution.model.MDDOrderLine;
import org.eevolution.model.MPPProductBOMLine;

import id.tcs.callout.CalloutAccountingSchemaDefault;
import id.tcs.callout.CalloutAccountingSchemaGL;
import id.tcs.callout.CalloutBPAcct;
import id.tcs.callout.CalloutBPGroupAcct;
import id.tcs.callout.CalloutBankCash;
import id.tcs.callout.CalloutBankTransfer;
import id.tcs.callout.CalloutChargeAcct;
import id.tcs.callout.CalloutFADefaultAccount;
import id.tcs.callout.CalloutProductAcct;
import id.tcs.callout.CalloutProductCategoryAcct;
import id.tcs.callout.CalloutProjectAcct;
import id.tcs.callout.CalloutTaxRateAcct;
import id.tcs.callout.CalloutWarehouseLocator;
import id.tcs.callout.TCS_CalloutAmortizationPlan;
import id.tcs.callout.TCS_CalloutAsset;
import id.tcs.callout.TCS_CalloutAssetAddition;
import id.tcs.callout.TCS_CalloutAssetMovement;
import id.tcs.callout.TCS_CalloutAssetReval;
import id.tcs.callout.TCS_CalloutAssetTransfer;
import id.tcs.callout.TCS_CalloutDDOrderLine;
import id.tcs.callout.TCS_CalloutInquiryLine;
import id.tcs.callout.TCS_CalloutInventoryLine;
import id.tcs.callout.TCS_CalloutOrderLine;
import id.tcs.callout.TCS_CalloutPayment;
import id.tcs.callout.TCS_CalloutQuotation;
import id.tcs.callout.TCS_CalloutQuotationLine;
import id.tcs.callout.TCS_CalloutRequisition;
import id.tcs.callout.TCS_Callout_PP_Product_BOMLine;
import id.tcs.model.MTCSAmortizationPlan;


public class TCS_CalloutFactory implements IColumnCalloutFactory {

	@Override
	public IColumnCallout[] getColumnCallouts(String tableName,
			String columnName) {
		List<IColumnCallout> list = new ArrayList<IColumnCallout>();

		if (tableName.equals("M_RequisitionLine")){
			list.add(new TCS_CalloutRequisition());
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

		if (tableName.equals("C_Payment")){
			list.add(new TCS_CalloutPayment());
		}

		if (tableName.equals("A_Asset")){
			list.add(new TCS_CalloutAsset());
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

		if(tableName.equals("A_Asset_Addition")) 
			list.add(new TCS_CalloutAssetAddition());

		if(tableName.equals("C_BankTransfer"))
			list.add(new CalloutBankTransfer());

		if(tableName.equals("C_Quotation"))
			list.add(new TCS_CalloutQuotation());

		if(tableName.equals("C_QuotationLine"))
			list.add(new TCS_CalloutQuotationLine());

		if(tableName.equals("C_InquiryLine"))
			list.add(new TCS_CalloutInquiryLine());

		if(tableName.equals(MTCSAmortizationPlan.Table_Name))
			list.add(new TCS_CalloutAmortizationPlan());

		if(tableName.equals(MDDOrderLine.Table_Name))
			list.add(new TCS_CalloutDDOrderLine());

		if(tableName.equals(MPPProductBOMLine.Table_Name))
			list.add(new TCS_Callout_PP_Product_BOMLine());
		
		if(tableName.equals(TCS_MInventoryLine.Table_Name))
			list.add(new TCS_CalloutInventoryLine());
		
		if(tableName.equals(MOrderLine.Table_Name))
			list.add(new TCS_CalloutOrderLine());
		
		
		return list != null ? list.toArray(new IColumnCallout[0]) : new IColumnCallout[0];
	}

}
