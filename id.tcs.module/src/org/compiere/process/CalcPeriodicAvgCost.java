package org.compiere.process;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.util.logging.Level;

import org.adempiere.exceptions.AdempiereException;
import org.compiere.model.MAccount;
import org.compiere.model.MPeriod;
import org.compiere.model.MProduct;
import org.compiere.model.Query;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.TimeUtil;
import org.compiere.model.X_M_Periodic_Cost;

/**
 * @author Phie Albert
 * Calculate cost per periodic
 * Next : Run process Update Cost Detail
 */
public class CalcPeriodicAvgCost extends SvrProcess{

	private int p_AD_Client_ID = 0;
	private int p_C_Period_ID = 0;
	private int p_M_Product_ID = 0;
	private int p_M_Product_Category_ID = 0;
	private int p_C_AcctSchema_ID = 0;
	private boolean p_isRecalculate = false;

	protected void prepare() {
		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++)
		{
			String name = para[i].getParameterName();
			if (para[i].getParameter() == null)
				;
			else if (name.equals("AD_Client_ID"))
				p_AD_Client_ID = para[i].getParameterAsInt();
			else if (name.equals("C_Period_ID"))
				p_C_Period_ID = para[i].getParameterAsInt();
			else if (name.equals("M_Product_ID"))
				p_M_Product_ID = para[i].getParameterAsInt();
			else if (name.equals("M_Product_Category_ID"))
				p_M_Product_Category_ID = para[i].getParameterAsInt();
			else if (name.equals("isRecalculate"))
				p_isRecalculate = para[i].getParameterAsBoolean();
			else if (name.equals("C_AcctSchema_ID"))
				p_C_AcctSchema_ID = para[i].getParameterAsInt();
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
	}

	protected String doIt() throws Exception {
		MPeriod period = new MPeriod(getCtx(), p_C_Period_ID, get_TrxName());

		if(p_AD_Client_ID == 0)
			throw new AdempiereException("Client is mandatory...");

		if(p_C_Period_ID == 0)
			throw new AdempiereException("Period is mandatory...");

		//Delete Historical Period Cost
		if(p_isRecalculate && p_M_Product_ID > 0) {
			deleteRelatedPeriodicCost(period, p_M_Product_ID);
		} else if(p_M_Product_Category_ID != 0){
			for (int id : getInventoryProductForProductCategory(p_M_Product_Category_ID))
				deleteRelatedPeriodicCost(period, id);
		} else {
			deleteRelatedPeriodicCost(period);
		}
		//
		if(existsPeriodicCostForThisPeriod(period, getListProduct_ID()))
			throw new AdempiereException("There's exists historical cost for this period and this product ");

		//Create Transaction
		if(p_M_Product_ID != 0)
			createPeriodicCost(period, p_M_Product_ID);
		else if(p_M_Product_Category_ID != 0){
			for (int id : getInventoryProductForProductCategory(p_M_Product_Category_ID))
				createPeriodicCost(period, id);
		}
		else {
			for (int id : getInventoryProduct())
				createPeriodicCost(period, id);
		}

		return "Done";
	}

	public void createPeriodicCost(MPeriod period, int M_Product_ID){
		BigDecimal beginningAmount = Env.ZERO;
		BigDecimal beginningQty = Env.ZERO;

		BigDecimal totalAmountReceipt = Env.ZERO;
		BigDecimal totalQtyReceipt = Env.ZERO;

		BigDecimal invoiceVarianAmount = Env.ZERO;
		BigDecimal averageCostVarianceAmount = Env.ZERO;

		BigDecimal invoiceLandedCostAmount = Env.ZERO;

		BigDecimal totalIssueQty = Env.ZERO;
		BigDecimal totalIssueAmount = Env.ZERO;

		BigDecimal totalAmount = Env.ZERO;
		BigDecimal totalQty = Env.ZERO;
		BigDecimal costPrice = Env.ZERO;

		BigDecimal endingQty = Env.ZERO;
		BigDecimal endingAmount = Env.ZERO;

		MProduct product = new MProduct(getCtx(), M_Product_ID, get_TrxName());	

		int lastPeriodCost_ID = getPeriodicCostBeforeThisPeriod(period, M_Product_ID);
		if(lastPeriodCost_ID != -1){
			//Beginning
//			X_M_Periodic_Cost lastPeriodicCost = new X_M_Periodic_Cost(getCtx(), lastPeriodCost_ID, get_TrxName());
//			beginningAmount = lastPeriodicCost.getEndingAmount();
//			beginningQty = lastPeriodicCost.getEndingQty();
		}

		//Receipt

		totalAmountReceipt = getAmountReceipt(M_Product_ID, period);
		totalQtyReceipt = getQtyReceipt(M_Product_ID, period);

		//Invoice Varian
		invoiceVarianAmount = getInvoiceVarianAmount(M_Product_ID, period);
		averageCostVarianceAmount = getAverageCostVarian(M_Product_ID, period);

		//Invoice Landed Cost
		invoiceLandedCostAmount = getInvoiceLandedCost(M_Product_ID, period);

		//Total Amount
		if(getAccountAverageCostVarian(M_Product_ID) != getAccountInvoiceVarian(M_Product_ID))
			totalAmount = beginningAmount.add(totalAmountReceipt).add(invoiceVarianAmount).add(averageCostVarianceAmount).add(invoiceLandedCostAmount);
		else
			totalAmount = beginningAmount.add(totalAmountReceipt).add(invoiceVarianAmount).add(invoiceLandedCostAmount);
		totalQty = beginningQty.add(totalQtyReceipt);
		costPrice = (totalQty.compareTo(Env.ZERO)!= 0) ? (totalAmount.divide(totalQty, 6, RoundingMode.HALF_UP)) : Env.ZERO;

		//Issue
		totalIssueQty = getIssueQty(M_Product_ID, period);
		totalIssueAmount = totalIssueQty.multiply(costPrice);

		//Ending
		endingQty = totalQty.add(totalIssueQty);
		//endingAmount = totalAmount.add(totalIssueAmount);
		endingAmount = endingQty.multiply(costPrice);

		if(beginningQty.compareTo(Env.ZERO) != 0 ||
				beginningAmount.compareTo(Env.ZERO) != 0 ||
				totalAmountReceipt.compareTo(Env.ZERO) != 0 ||
				totalQtyReceipt.compareTo(Env.ZERO) != 0 ||
				invoiceVarianAmount.compareTo(Env.ZERO) != 0 ||
				averageCostVarianceAmount.compareTo(Env.ZERO) != 0 ||
				invoiceLandedCostAmount.compareTo(Env.ZERO) != 0 ||
				totalIssueQty.compareTo(Env.ZERO) != 0 ||
				totalIssueAmount.compareTo(Env.ZERO) != 0)
		{
			//Create Historical Cost for this period
//			X_M_Periodic_Cost periodCost = new X_M_Periodic_Cost(getCtx(), 0, get_TrxName());
//			periodCost.setAD_Org_ID(Env.getAD_Org_ID(getCtx()));
//			periodCost.setC_Period_ID(period.getC_Period_ID());
//			periodCost.setM_Product_ID(M_Product_ID);
//			periodCost.setM_Product_Category_ID(product.getM_Product_Category_ID());
//
//			periodCost.setBeginningQty(beginningQty);
//			periodCost.setBeginnningAmount(beginningAmount);
//			periodCost.setReceiptQty(totalQtyReceipt);
//			periodCost.setReceiptAmount(totalAmountReceipt);
//			if(getAccountAverageCostVarian(M_Product_ID) != getAccountInvoiceVarian(M_Product_ID))
//				periodCost.setIPV_Amount(invoiceVarianAmount.add(averageCostVarianceAmount));
//			else
//				periodCost.setIPV_Amount(invoiceVarianAmount);
//			periodCost.setLandedCostAmount(invoiceLandedCostAmount);
//
//			periodCost.setIssueQty(totalIssueQty);
//			periodCost.setEndingQty(endingQty);
//			periodCost.setIssueAmount(totalIssueAmount);
//			periodCost.setEndingAmount(endingAmount);
//
//			periodCost.setCostPrice(costPrice);
//			if(!periodCost.save())
//				throw new AdempiereException("Cannot save period cost for this product: "+product.getValue());
		}
	}

	public void deleteRelatedPeriodicCost(MPeriod period, int M_Product_ID)
	{
		StringBuilder sb = new StringBuilder();
		sb.append("DELETE FROM M_Periodic_Cost "
				+ "WHERE M_Periodic_Cost_ID IN ("
				+ "SELECT M_Periodic_Cost_ID FROM M_Periodic_Cost "
				+ "JOIN C_Period cp ON cp.C_Period_ID = M_Periodic_Cost.C_Period_ID "
				+ "WHERE cp.startDate >= ? AND M_Product_ID=?)");
		DB.executeUpdateEx(sb.toString(), new Object[]{period.getStartDate(), M_Product_ID} , get_TrxName());
	}

	public void deleteRelatedPeriodicCost(MPeriod period)
	{
		StringBuilder sb = new StringBuilder();
		sb.append("DELETE FROM M_Periodic_Cost "
				+ "WHERE M_Periodic_Cost_ID IN ("
				+ "SELECT M_Periodic_Cost_ID FROM M_Periodic_Cost "
				+ "JOIN C_Period cp ON cp.C_Period_ID = M_Periodic_Cost.C_Period_ID "
				+ "WHERE cp.startDate >= ?)");
		DB.executeUpdateEx(sb.toString(), new Object[]{period.getStartDate()} , get_TrxName());
	}

	public boolean existsPeriodicCostForThisPeriod(MPeriod period, String M_Product_IDs)
	{
		String whereClause = "AD_Client_ID = ? AND M_Product_ID IN ("+M_Product_IDs+") AND C_Period_ID = ?";

//		return new Query(getCtx(), X_M_Periodic_Cost.Table_Name, whereClause, get_TrxName())
//				.setParameters(new Object[]{p_AD_Client_ID, period.getC_Period_ID()})
//				.match();
		return true;
	}

	public int getPeriodicCostBeforeThisPeriod(MPeriod period, int M_Product_ID)
	{
		Timestamp date = TimeUtil.addDays(period.getStartDate(), -1);
		String whereClause = "M_Periodic_Cost.AD_Client_ID = ? AND cp.endDate = ? AND M_Product_ID = ?";

//		return new Query(getCtx(), X_M_Periodic_Cost.Table_Name, whereClause, get_TrxName())
//				.addJoinClause("JOIN C_Period cp ON cp.C_Period_ID = M_Periodic_Cost.C_Period_ID")
//				.setParameters(new Object[]{p_AD_Client_ID, date, M_Product_ID})
//				.firstId();
		return -1;
	}

	private int[] getInventoryProduct()
	{
		String whereClause = "AD_Client_ID = ? AND ProductType='I'";
		return new Query(getCtx(), MProduct.Table_Name, whereClause, get_TrxName())
				.setParameters(new Object[]{p_AD_Client_ID})						
				.getIDs();
	}

	private int[] getInventoryProductForProductCategory(int M_Product_Category_ID)
	{
		String whereClause = "AD_Client_ID = ? AND ProductType='I' AND M_Product_Category_ID = ?";
		return new Query(getCtx(), MProduct.Table_Name, whereClause, get_TrxName())
				.setParameters(new Object[]{p_AD_Client_ID, M_Product_Category_ID})						
				.getIDs();
	}

	private BigDecimal getAmountReceipt(int M_Product_ID, MPeriod period){
		Timestamp endDate = TimeUtil.addDays(period.getEndDate(), 1);

		StringBuilder matReceiptAmtSql = new StringBuilder();
		matReceiptAmtSql.append("SELECT COALESCE(SUM(currencyconvert(cil.priceActual,ci.C_Currency_ID,303,mi.dateacct,ci.C_ConversionType_ID,ci.AD_Client_ID, ci.AD_Org_ID)*mil.movementqty),0) "
				+ "FROM M_InOutLine mil "
				+ "JOIN M_InOut mi ON mi.M_InOut_ID = mil.M_InOut_ID "
				+ "JOIN C_OrderLine cil ON cil.C_OrderLine_ID = mil.C_OrderLine_ID "
				+ "JOIN C_Order ci ON cil.C_Order_ID = ci.C_Order_ID "
				+ "WHERE mil.M_Product_ID = ? AND mil.AD_Client_ID = ? AND mi.docStatus in ('CO','CL', 'RE') "
				+ "AND (mi.DateAcct >= ? AND mi.DateAcct < ?) AND mi.C_DocType_ID IN (1000205, 1000251)");

		BigDecimal materialReceiptAmount = DB.getSQLValueBD(get_TrxName(), matReceiptAmtSql.toString(), 
				new Object[]{M_Product_ID, p_AD_Client_ID, period.getStartDate(), endDate});		

		StringBuilder miscReceiptAmountSql = new StringBuilder();
		miscReceiptAmountSql.append("SELECT COALESCE(SUM(qtyMiscReceipt*unitCostEntered),0) FROM M_InventoryLine mil "
				+ "JOIN M_Inventory mi ON mil.M_Inventory_ID = mi.M_Inventory_ID "
				+ "JOIN C_DocType cdt ON cdt.C_DocType_ID = mi.C_DocType_ID "
				+ "WHERE docSubTypeInv = 'MR' AND mi.movementDate >= ? AND mi.movementdate < ? AND "
				+ "mi.AD_Client_ID = ? AND mi.docStatus in ('CO','CL','RE') AND mil.M_Product_ID=? AND isUnitCost='Y'");
		BigDecimal miscReceiptAmount = DB.getSQLValueBDEx(get_TrxName(), miscReceiptAmountSql.toString(), 
				new Object[]{period.getStartDate(), endDate, p_AD_Client_ID, M_Product_ID});

		return materialReceiptAmount.add(miscReceiptAmount);
	}

	private BigDecimal getQtyReceipt(int M_Product_ID, MPeriod period){
		Timestamp endDate = TimeUtil.addDays(period.getEndDate(), 1);

		StringBuilder receiptQtySql = new StringBuilder();
		receiptQtySql.append("SELECT COALESCE(SUM(mil.movementqty),0) "
				+ "FROM M_InOutLine mil "
				+ "JOIN M_InOut mi ON mi.M_InOut_ID = mil.M_InOut_ID "
				+ "JOIN C_OrderLine cil ON cil.C_OrderLine_ID = mil.C_OrderLine_ID "
				+ "WHERE mil.M_Product_ID = ? AND mil.AD_Client_ID = ? AND mi.docStatus in ('CO','CL','RE') "
				+ "AND (mi.DateAcct >= ? AND mi.DateAcct < ?) AND mi.C_DocType_ID IN (1000205, 1000251)");
		BigDecimal materialReceiptQty = DB.getSQLValueBD(get_TrxName(), receiptQtySql.toString(), 
				new Object[]{M_Product_ID, p_AD_Client_ID, period.getStartDate(), endDate});																												//MR AND MR FUEL;

		StringBuilder miscReceiptQtySql = new StringBuilder();
		miscReceiptQtySql.append("SELECT COALESCE(SUM(qtyMiscReceipt),0) FROM M_InventoryLine mil "
				+ "JOIN M_Inventory mi ON mil.M_Inventory_ID = mi.M_Inventory_ID "
				+ "JOIN C_DocType cdt ON cdt.C_DocType_ID = mi.C_DocType_ID "
				+ "WHERE docSubTypeInv = 'MR' AND mi.movementDate >= ? AND mi.movementdate < ? AND "
				+ "mi.AD_Client_ID = ? AND mi.docStatus in ('CO','CL','RE') AND mil.M_Product_ID=? AND isUnitCost='Y'");
		BigDecimal miscReceiptQty = DB.getSQLValueBDEx(get_TrxName(), miscReceiptQtySql.toString(), 
				new Object[]{period.getStartDate(), endDate, p_AD_Client_ID, M_Product_ID});


		return materialReceiptQty.add(miscReceiptQty);
	}

	private BigDecimal getIssueQty(int M_Product_ID, MPeriod period)
	{
		Timestamp endDate = TimeUtil.addDays(period.getEndDate(), 1);

		//Note Combine Misc Issue And Misc Receipt (Unit Cost false only)
		StringBuilder issueQtySql = new StringBuilder();
		issueQtySql.append("SELECT COALESCE(SUM(qtyInternalUse)*-1,0) FROM M_InventoryLine mil "
				+ "JOIN M_Inventory mi ON mil.M_Inventory_ID = mi.M_Inventory_ID "
				+ "JOIN C_DocType cdt ON cdt.C_DocType_ID = mi.C_DocType_ID "
				+ "WHERE docSubTypeInv IN ('IU','MR') AND mi.movementDate >= ? AND mi.movementdate < ? AND "
				+ "mi.AD_Client_ID = ? AND mi.docStatus in ('CO','CL','RE') AND mil.M_Product_ID=? AND mil.isUnitCost = 'N'");

		BigDecimal miscIssueQty = DB.getSQLValueBDEx(get_TrxName(), issueQtySql.toString(), 
				new Object[]{period.getStartDate(), endDate, p_AD_Client_ID, M_Product_ID});

		//Physical Inventory
		StringBuilder physicalInventoryQtySql = new StringBuilder();
		physicalInventoryQtySql.append("SELECT COALESCE(SUM(qtyCount-qtyBook),0) FROM M_InventoryLine mil "
				+ "JOIN M_Inventory mi ON mil.M_Inventory_ID = mi.M_Inventory_ID "
				+ "JOIN C_DocType cdt ON cdt.C_DocType_ID = mi.C_DocType_ID "
				+ "WHERE docSubTypeInv = 'PI' AND mi.movementDate >= ? AND mi.movementdate < ? AND "
				+ "mi.AD_Client_ID = ? AND mi.docStatus in ('CO','CL','RE') AND mil.M_Product_ID=?");
		BigDecimal physicalInventoryQty = DB.getSQLValueBDEx(get_TrxName(), physicalInventoryQtySql.toString(), 
				new Object[]{period.getStartDate(), endDate, p_AD_Client_ID, M_Product_ID});

		//Vendor Return
		StringBuilder vendorReturnQtySql = new StringBuilder();
		vendorReturnQtySql.append("SELECT COALESCE(SUM(movementQty*-1),0) FROM M_InOutLine mil "
				+ "JOIN M_InOut mi ON mil.M_InOut_ID = mi.M_InOut_ID "
				+ "JOIN C_DocType cdt ON cdt.C_DocType_ID = mi.C_DocType_ID "
				+ "WHERE docBaseType = 'MMS' AND mi.DateAcct >= ? AND mi.DateAcct < ? AND "
				+ "mi.AD_Client_ID = ? AND mi.docStatus in ('CO','CL','RE') AND mil.M_Product_ID=?");
		BigDecimal vendorReturnQty = DB.getSQLValueBDEx(get_TrxName(), vendorReturnQtySql.toString(), 
				new Object[]{period.getStartDate(), endDate, p_AD_Client_ID, M_Product_ID});

		return miscIssueQty.add(physicalInventoryQty).add(vendorReturnQty);

	}

	private int getAccountInvoiceVarian(int M_Product_ID)
	{
		MProduct product = new MProduct(getCtx(), M_Product_ID, get_TrxName());
		//get account
		String sql = "SELECT P_Asset_Acct FROM M_Product_Acct WHERE M_Product_ID=? AND C_AcctSchema_ID=?";
		int C_ValidCombination_ID = DB.getSQLValue(get_TrxName(), sql, new Object[]{M_Product_ID, p_C_AcctSchema_ID});
		if(C_ValidCombination_ID == -1)
			throw new AdempiereException("There's no accounting setup for product "+product.getValue());

		MAccount account = new MAccount(getCtx(), C_ValidCombination_ID, get_TrxName());

		return account.getAccount_ID();
	}

	private int getAccountAverageCostVarian(int M_Product_ID)
	{
		MProduct product = new MProduct(getCtx(), M_Product_ID, get_TrxName());
		//get account
		String sql = "SELECT P_AverageCostVariance_Acct FROM M_Product_Acct WHERE M_Product_ID=? AND C_AcctSchema_ID=?";
		int C_ValidCombination_ID = DB.getSQLValue(get_TrxName(), sql, new Object[]{M_Product_ID, p_C_AcctSchema_ID});
		if(C_ValidCombination_ID == -1)
			throw new AdempiereException("There's no accounting setup for product "+product.getValue());

		MAccount account = new MAccount(getCtx(), C_ValidCombination_ID, get_TrxName());

		return account.getAccount_ID();
	}

	//Setup product asset
	private BigDecimal getInvoiceVarianAmount(int M_Product_ID, MPeriod period){
		MProduct product = new MProduct(getCtx(), M_Product_ID, get_TrxName());
		//get account
		String sql = "SELECT P_Asset_Acct FROM M_Product_Acct WHERE M_Product_ID=? AND C_AcctSchema_ID=?";
		int C_ValidCombination_ID = DB.getSQLValue(get_TrxName(), sql, new Object[]{M_Product_ID, p_C_AcctSchema_ID});
		if(C_ValidCombination_ID == -1)
			throw new AdempiereException("There's no accounting setup for product "+product.getValue());

		MAccount account = new MAccount(getCtx(), C_ValidCombination_ID, get_TrxName());

		StringBuilder ivpSql = new StringBuilder();
		ivpSql.append("SELECT COALESCE(Sum(Amtacctdr-Amtacctcr),0) FROM Fact_Acct "
				+ "WHERE AD_Client_ID = ? AND AD_Table_ID = 472 AND Account_ID = ? AND M_Product_ID = ?"
				+ " AND Record_ID IN (Select M_MatchInv_ID FROM M_MatchInv WHERE DateAcct BETWEEN ? AND ?)");
		BigDecimal IVP_Amount = DB.getSQLValueBD(get_TrxName(), ivpSql.toString(), 
				new Object[]{p_AD_Client_ID, account.getAccount_ID(), M_Product_ID, period.getStartDate(), period.getEndDate()});

		return IVP_Amount;
	}

	//Setup average cost varian
	private BigDecimal getAverageCostVarian(int M_Product_ID, MPeriod period){
		MProduct product = new MProduct(getCtx(), M_Product_ID, get_TrxName());
		//get account
		String sql = "SELECT P_AverageCostVariance_Acct FROM M_Product_Acct WHERE M_Product_ID=? AND C_AcctSchema_ID=?";
		int C_ValidCombination_ID = DB.getSQLValue(get_TrxName(), sql, new Object[]{M_Product_ID, p_C_AcctSchema_ID});
		if(C_ValidCombination_ID == -1)
			throw new AdempiereException("There's no accounting setup for product "+product.getValue());

		MAccount account = new MAccount(getCtx(), C_ValidCombination_ID, get_TrxName());

		StringBuilder ivpSql = new StringBuilder();
		ivpSql.append("SELECT COALESCE(Sum(Amtacctdr-Amtacctcr),0) FROM Fact_Acct "
				+ "WHERE AD_Client_ID = ? AND AD_Table_ID = 472 AND Account_ID = ? AND M_Product_ID = ?"
				+ " AND Record_ID IN (Select M_MatchInv_ID FROM M_MatchInv WHERE DateAcct BETWEEN ? AND ?)");
		BigDecimal IVP_Amount = DB.getSQLValueBD(get_TrxName(), ivpSql.toString(), 
				new Object[]{p_AD_Client_ID, account.getAccount_ID(), M_Product_ID, period.getStartDate(), period.getEndDate()});

		return IVP_Amount;
	}

	private BigDecimal getInvoiceLandedCost(int M_Product_ID, MPeriod period){

		MProduct product = new MProduct(getCtx(), M_Product_ID, get_TrxName());
		//get account
		String sql = "SELECT P_Asset_Acct FROM M_Product_Acct WHERE M_Product_ID=? AND C_AcctSchema_ID=?";
		int C_ValidCombination_ID = DB.getSQLValue(get_TrxName(), sql, new Object[]{M_Product_ID, p_C_AcctSchema_ID});
		if(C_ValidCombination_ID == -1)
			throw new AdempiereException("There's no accounting setup for product "+product.getValue());

		MAccount account = new MAccount(getCtx(), C_ValidCombination_ID, get_TrxName());

		StringBuilder landedCost = new StringBuilder();
		landedCost.append("SELECT COALESCE(Sum(Amtacctdr-Amtacctcr),0) FROM Fact_Acct "
				+ "WHERE AD_Client_ID = ? AND AD_Table_ID = 318 AND Account_ID = ? AND M_Product_ID = ?"
				+ " AND Record_ID IN (Select C_Invoice_ID FROM C_Invoice WHERE DateAcct BETWEEN ? AND ?)");
		BigDecimal landedCostAmount = DB.getSQLValueBD(get_TrxName(), landedCost.toString(), 
				new Object[]{p_AD_Client_ID, account.getAccount_ID(), M_Product_ID, period.getStartDate(), period.getEndDate()});

		return landedCostAmount;
	}

	private String getListProduct_ID()
	{
		StringBuilder tmp = new StringBuilder();
		if(p_M_Product_ID != 0)
			return Integer.toString(p_M_Product_ID);
		else if(p_M_Product_Category_ID != 0)
		{
			for(int id : getInventoryProductForProductCategory(p_M_Product_Category_ID)){
				tmp.append(id);
				tmp.append(",");
			}
			tmp.delete(tmp.length()-1, tmp.length());
			return tmp.toString();
		}
		else {
			for(int id : getInventoryProduct()){
				tmp.append(id);
				tmp.append(",");
			}
			tmp.delete(tmp.length()-1, tmp.length());
			return tmp.toString();
		}
	}
}
