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

public class CalcInitialPeriodicCost extends SvrProcess{

	private int p_AD_Client_ID = 0;
	private int p_C_Period_ID = 0;
	private int p_M_Product_ID = 0;
	private int p_M_Product_Category_ID = 0;
	private int p_C_AcctSchema_ID = 0;
	private boolean p_isRecalculate = false;
	private int p_Line = 1;

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
			else if (name.equals("Line"))
				p_Line = para[i].getParameterAsInt();
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
		if(p_isRecalculate)
			deleteRelatedPeriodicCost(period);

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
		else{
			for (int id : getInventoryProduct())
				createPeriodicCost(period, id);
		}

		return "Success";
	}

	public int createPeriodicCost(MPeriod period, int M_Product_ID){
		BigDecimal beginningAmount = Env.ZERO;
		BigDecimal beginningQty = Env.ZERO;

		BigDecimal totalAmountReceipt = Env.ZERO;
		BigDecimal totalQtyReceipt = Env.ZERO;

		BigDecimal totalProductionAmt = Env.ZERO;
		BigDecimal totalProductionQty = Env.ZERO;

		BigDecimal invoiceVarianAmount = Env.ZERO;
		BigDecimal averageCostVarianceAmount = Env.ZERO;

		BigDecimal invoiceLandedCostAmount = Env.ZERO;

		BigDecimal totalIssueQty = Env.ZERO;
		BigDecimal totalIssueAmount = Env.ZERO;

		BigDecimal totalShipmentQty = Env.ZERO;
		BigDecimal totalShipmentAmount = Env.ZERO;

		BigDecimal totalAmount = Env.ZERO;
		BigDecimal totalQty = Env.ZERO;
		BigDecimal costPrice = Env.ZERO;

		BigDecimal endingQty = Env.ZERO;
		BigDecimal endingAmount = Env.ZERO;

		MProduct product = new MProduct(getCtx(), M_Product_ID, get_TrxName());	

		StringBuilder sql = new StringBuilder();
		sql.append("SELECT Amt FROM t_initcost WHERE M_Product_ID = ?");
		beginningAmount = DB.getSQLValueBD(get_TrxName(), sql.toString(), new Object[]{M_Product_ID});

		sql = new StringBuilder();
		sql.append("SELECT Qty FROM t_initcost WHERE M_Product_ID = ?");
		beginningQty = DB.getSQLValueBD(get_TrxName(), sql.toString(), new Object[]{M_Product_ID});

		if(beginningAmount == null)
			beginningAmount = Env.ZERO;

		if(beginningQty==null)
			beginningQty = Env.ZERO;

		//Receipt
		totalAmountReceipt = getAmountReceipt(M_Product_ID, period);
		totalQtyReceipt = getQtyReceipt(M_Product_ID, period);

		//Simple Production
		totalProductionAmt = getAmountProduction(M_Product_ID, period);
		totalProductionQty = getQtyProduction(M_Product_ID, period);

		//Invoice Varian
		invoiceVarianAmount = getInvoiceVarianAmount(M_Product_ID, period);
		averageCostVarianceAmount = getAverageCostVarian(M_Product_ID, period);

		//Invoice Landed Cost
		invoiceLandedCostAmount = getInvoiceLandedCost(M_Product_ID, period);

		//Total Amount
		if(getAccountAverageCostVarian(M_Product_ID) != getAccountInvoiceVarian(M_Product_ID))
			totalAmount = beginningAmount.add(totalAmountReceipt).add(totalProductionAmt)
			.add(invoiceVarianAmount).add(averageCostVarianceAmount).add(invoiceLandedCostAmount);
		else
			totalAmount = beginningAmount.add(totalAmountReceipt).add(totalProductionQty)
			.add(invoiceVarianAmount).add(invoiceLandedCostAmount);

		totalQty = beginningQty.add(totalQtyReceipt).add(totalProductionQty);
		costPrice = (totalQty.compareTo(Env.ZERO)!= 0) ? (totalAmount.divide(totalQty, 9, RoundingMode.HALF_UP)) : Env.ZERO;

		//Issue markhere
		totalIssueQty = getIssueQty(M_Product_ID, period);
		totalIssueAmount = totalIssueQty.multiply(costPrice);

		//Shipment
		totalShipmentQty = getShipmentQty(M_Product_ID, period);
		totalShipmentAmount = totalShipmentQty.multiply(costPrice);

		//Ending
		endingQty = totalQty.subtract(totalIssueQty).subtract(totalShipmentQty);
		endingAmount = totalAmount.subtract(totalIssueAmount).subtract(totalShipmentAmount);

		if(beginningQty.compareTo(Env.ZERO) != 0 ||
				beginningAmount.compareTo(Env.ZERO) != 0 ||
				totalAmountReceipt.compareTo(Env.ZERO) != 0 ||
				totalQtyReceipt.compareTo(Env.ZERO) != 0 ||
				invoiceVarianAmount.compareTo(Env.ZERO) != 0 ||
				averageCostVarianceAmount.compareTo(Env.ZERO) != 0 ||
				invoiceLandedCostAmount.compareTo(Env.ZERO) != 0 ||
				totalProductionQty.compareTo(Env.ZERO) != 0 ||
				totalProductionAmt.compareTo(Env.ZERO) != 0 ||
				totalShipmentQty.compareTo(Env.ZERO) != 0 ||
				totalShipmentAmount.compareTo(Env.ZERO) != 0 ||
				totalIssueQty.compareTo(Env.ZERO) != 0 ||
				totalIssueAmount.compareTo(Env.ZERO) != 0)
		{
			//Create Historical Cost for this period
			X_M_Periodic_Cost periodCost = new X_M_Periodic_Cost(getCtx(), 0, get_TrxName());
			periodCost.setAD_Org_ID(Env.getAD_Org_ID(getCtx()));
			periodCost.setC_Period_ID(period.getC_Period_ID());
			periodCost.setM_Product_ID(M_Product_ID);
			periodCost.setM_Product_Category_ID(product.getM_Product_Category_ID());

			periodCost.setbeginningqty(beginningQty);
			periodCost.setbeginningamount(beginningAmount);
			periodCost.setreceiptqty(totalQtyReceipt);
			periodCost.setreceiptamount(totalAmountReceipt);
			if(getAccountAverageCostVarian(M_Product_ID) != getAccountInvoiceVarian(M_Product_ID))
				periodCost.setipv_amount(invoiceVarianAmount.add(averageCostVarianceAmount));
			else
				periodCost.setipv_amount(invoiceVarianAmount);
			periodCost.setlandedcostamount(invoiceLandedCostAmount);

			periodCost.setissueqty(totalIssueQty);
			periodCost.setendingqty(endingQty);
			periodCost.setshippedqty(totalShipmentQty);
			periodCost.setshippedamt(totalShipmentAmount);
			periodCost.setmanufacturedamt(totalProductionAmt);
			periodCost.setmanufacturedqty(totalProductionQty);
			periodCost.setissueamount(totalIssueAmount.setScale(9, RoundingMode.HALF_UP));
			periodCost.setendingamount(endingAmount.setScale(9, RoundingMode.HALF_UP));

			periodCost.setcostprice(costPrice);
			if(!periodCost.save())
				throw new AdempiereException("Cannot save period cost for this product: "+product.getValue());
		}
		return 1;
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

		return new Query(getCtx(), X_M_Periodic_Cost.Table_Name, whereClause, get_TrxName())
				.setParameters(new Object[]{p_AD_Client_ID, period.getC_Period_ID()})
				.match();
	}

	public int getPeriodicCostBeforeThisPeriod(MPeriod period, int M_Product_ID)
	{
		Timestamp date = TimeUtil.addDays(period.getStartDate(), -1);
		String whereClause = "M_Periodic_Cost.AD_Client_ID = ? AND cp.endDate = ? AND M_Product_ID = ?";

		return new Query(getCtx(), X_M_Periodic_Cost.Table_Name, whereClause, get_TrxName())
				.addJoinClause("JOIN C_Period cp ON cp.C_Period_ID = M_Periodic_Cost.C_Period_ID")
				.setParameters(new Object[]{p_AD_Client_ID, date, M_Product_ID})
				.firstId();
	}

	private int[] getInventoryProduct()
	{
		String whereClause = "";
		if (p_Line==1) {
			whereClause = "m_product_id in (1010002,1010337,1010340,1010346,1010504,1010511)";
			return new Query(getCtx(), MProduct.Table_Name, whereClause, get_TrxName())
					.setOnlyActiveRecords(true)
					.getIDs();

		} else if (p_Line==2) {
			whereClause = "m_product_id in (1010512)";
			return new Query(getCtx(), MProduct.Table_Name, whereClause, get_TrxName())
					.setOnlyActiveRecords(true)
					.getIDs();

		} else {
			whereClause = "AD_Client_ID = ? AND ProductType='I'";
			return new Query(getCtx(), MProduct.Table_Name, whereClause, get_TrxName())
					.setParameters(new Object[]{p_AD_Client_ID})
					.setOnlyActiveRecords(true)
					.getIDs();
		}


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

		StringBuilder receiptAmountSql = new StringBuilder();
		receiptAmountSql.append("SELECT COALESCE(SUM(cil.priceActual*mil.movementqty),0) "
				+ "FROM M_InOutLine mil "
				+ "JOIN M_InOut mi ON mi.M_InOut_ID = mil.M_InOut_ID "
				+ "JOIN C_OrderLine cil ON cil.C_OrderLine_ID = mil.C_OrderLine_ID "
				+ "JOIN C_DocType dt ON dt.C_DocType_ID = mi.C_DocType_ID "
				+ "WHERE mil.M_Product_ID = ? AND mil.AD_Client_ID = ? AND mi.docStatus in ('CO','CL','RE') "
				+ "AND mi.DateAcct >= ? AND mi.DateAcct < ? AND dt.DocBaseType='MMR' AND mi.IsSOTrx='N'");

		//+ "AND (mi.DateAcct >= ? AND mi.DateAcct < ?) AND mi.C_DocType_ID IN (1000205, 1000251)");

		BigDecimal materialReceiptAmount = DB.getSQLValueBD(get_TrxName(), receiptAmountSql.toString(), 
				new Object[]{M_Product_ID, p_AD_Client_ID, period.getStartDate(), endDate});

		return materialReceiptAmount;

		/*
		receiptAmountSql = new StringBuilder();
		receiptAmountSql.append("SELECT COALESCE(SUM(qtyMiscReceipt*unitCostEntered),0) FROM M_InventoryLine mil "
								+ "JOIN M_Inventory mi ON mil.M_Inventory_ID = mi.M_Inventory_ID "
								+ "JOIN C_DocType cdt ON cdt.C_DocType_ID = mi.C_DocType_ID "
								+ "WHERE docSubTypeInv = 'MR' AND mi.movementDate >= ? AND mi.movementdate < ? AND "
								+ "mi.AD_Client_ID = ? AND mi.docStatus in ('CO','CL') AND mil.M_Product_ID=? AND isUnitCost='Y'");
		BigDecimal miscReceiptAmount = DB.getSQLValueBDEx(get_TrxName(), receiptAmountSql.toString(), 
									new Object[]{period.getStartDate(), endDate, p_AD_Client_ID, M_Product_ID});

		return materialReceiptAmount.add(miscReceiptAmount);
		 */
	}

	private BigDecimal getAmountProduction(int M_Product_ID, MPeriod period){
		Timestamp endDate = TimeUtil.addDays(period.getEndDate(), 1);

		StringBuilder productionAmountSql = new StringBuilder();
		productionAmountSql.append("select coalesce(sum(mcd.currentcostprice*mcd.qty),0) " + 
				"from m_production mp " + 
				"join m_productionline mpl on mpl.m_production_id=mp.m_production_id " + 
				"join m_costdetail mcd on mcd.m_productionline_id =mpl.m_productionline_id " + 
				"where mpl.m_product_id =mp.m_product_id " + 
				"and mpl.m_product_id=? and mp.docstatus in ('CO','CL','RE') " + 
				"and mp.movementdate >= ? AND mp.movementdate < ?");

		BigDecimal productionAmount = DB.getSQLValueBD(get_TrxName(), productionAmountSql.toString(), 
				new Object[]{M_Product_ID, period.getStartDate(), endDate});

		return productionAmount;
	}

	private BigDecimal getQtyProduction(int M_Product_ID, MPeriod period) {
		Timestamp endDate = TimeUtil.addDays(period.getEndDate(), 1);

		StringBuilder productionQtySql = new StringBuilder();
		productionQtySql.append("select coalesce(sum(mpl.movementqty),0) " + 
				"from m_production mp " + 
				"join m_productionline mpl on mpl.m_production_id=mp.m_production_id " + 
				"where mpl.m_product_id =mp.m_product_id " + 
				"and mpl.m_product_id=? and mp.docstatus in ('CO','CL','RE') " + 
				"and mp.movementdate >= ? AND mp.movementdate < ?");

		BigDecimal productionQty = DB.getSQLValueBD(get_TrxName(), productionQtySql.toString(), 
				new Object[]{M_Product_ID, period.getStartDate(), endDate});																												//MR AND MR FUEL;

		return productionQty;
	}

	private BigDecimal getQtyReceipt(int M_Product_ID, MPeriod period){
		Timestamp endDate = TimeUtil.addDays(period.getEndDate(), 1);

		StringBuilder receiptQtySql = new StringBuilder();
		receiptQtySql.append("SELECT COALESCE(SUM(mil.movementqty),0) "
				+ "FROM M_InOutLine mil "
				+ "JOIN M_InOut mi ON mi.M_InOut_ID = mil.M_InOut_ID "
				+ "JOIN C_OrderLine cil ON cil.C_OrderLine_ID = mil.C_OrderLine_ID "
				+ "JOIN C_DocType dt ON dt.C_DocType_ID = mi.C_DocType_ID "
				+ "WHERE mil.M_Product_ID = ? AND mil.AD_Client_ID = ? AND mi.docStatus in ('CO','CL','RE') "
				+ "AND mi.DateAcct >= ? AND mi.DateAcct < ? AND dt.DocBaseType='MMR' AND mi.IsSOTrx='N'");
		//+ "AND (mi.DateAcct >= ? AND mi.DateAcct < ?) AND mi.C_DocType_ID IN (1000205, 1000251)");

		BigDecimal materialReceiptQty = DB.getSQLValueBD(get_TrxName(), receiptQtySql.toString(), 
				new Object[]{M_Product_ID, p_AD_Client_ID, period.getStartDate(), endDate});																												//MR AND MR FUEL;

		return materialReceiptQty;

		/*
		receiptQtySql = new StringBuilder();
		receiptQtySql.append("SELECT COALESCE(SUM(qtyMiscReceipt),0) FROM M_InventoryLine mil "
								+ "JOIN M_Inventory mi ON mil.M_Inventory_ID = mi.M_Inventory_ID "
								+ "JOIN C_DocType cdt ON cdt.C_DocType_ID = mi.C_DocType_ID "
								+ "WHERE docSubTypeInv = 'MR' AND mi.movementDate >= ? AND mi.movementDate < ? AND "
								+ "mi.AD_Client_ID = ? AND mi.docStatus in ('CO','CL') AND mil.M_Product_ID=? AND isUnitCost='Y'");
		BigDecimal miscReceiptQty = DB.getSQLValueBDEx(get_TrxName(), receiptQtySql.toString(), 
									new Object[]{period.getStartDate(), endDate, p_AD_Client_ID, M_Product_ID});


		return materialReceiptQty.add(miscReceiptQty);
		 */
	}

	private BigDecimal getIssueQty(int M_Product_ID, MPeriod period)
	{
		Timestamp endDate = TimeUtil.addDays(period.getEndDate(), 1);

		//Note Combine Misc Issue And Misc Receipt
		StringBuilder issueQtySql = new StringBuilder();
		issueQtySql.append("SELECT COALESCE(SUM(qtyInternalUse),0) FROM M_InventoryLine mil "
				+ "JOIN M_Inventory mi ON mil.M_Inventory_ID = mi.M_Inventory_ID "
				+ "JOIN C_DocType cdt ON cdt.C_DocType_ID = mi.C_DocType_ID "
				+ "WHERE docSubTypeInv IN ('IU') AND mi.movementDate >= ? AND mi.movementdate < ? AND "
				//+ "WHERE docSubTypeInv IN ('IU','MR') AND mi.movementDate >= ? AND mi.movementdate < ? AND "

				+ "mi.AD_Client_ID = ? AND mi.docStatus in ('CO','CL','RE') AND mil.M_Product_ID=?");
		//+ "mi.AD_Client_ID = ? AND mi.docStatus in ('CO','CL') AND mil.M_Product_ID=? AND mil.isUnitCost = 'N'");

		BigDecimal miscIssueQty = DB.getSQLValueBDEx(get_TrxName(), issueQtySql.toString(), 
				new Object[]{period.getStartDate(), endDate, p_AD_Client_ID, M_Product_ID});

		//Physical Inventory
		StringBuilder physicalInvSql = new StringBuilder();
		physicalInvSql.append("SELECT COALESCE(SUM(qtyBook-qtyCount),0) FROM M_InventoryLine mil "
				+ "JOIN M_Inventory mi ON mil.M_Inventory_ID = mi.M_Inventory_ID "
				+ "JOIN C_DocType cdt ON cdt.C_DocType_ID = mi.C_DocType_ID "
				+ "WHERE docSubTypeInv = 'PI' AND mi.movementDate >= ? AND mi.movementdate < ? AND "
				+ "mi.AD_Client_ID = ? AND mi.docStatus in ('CO','CL','RE') AND mil.M_Product_ID=?");
		BigDecimal physicalInventoryQty = DB.getSQLValueBDEx(get_TrxName(), physicalInvSql.toString(), 
				new Object[]{period.getStartDate(), endDate, p_AD_Client_ID, M_Product_ID});

		//Vendor Return
		StringBuilder vendorReturnSql = new StringBuilder();
		vendorReturnSql.append("SELECT COALESCE(SUM(movementQty),0) FROM M_InOutLine mil "
				+ "JOIN M_InOut mi ON mil.M_InOut_ID = mi.M_InOut_ID "
				+ "JOIN C_DocType cdt ON cdt.C_DocType_ID = mi.C_DocType_ID "
				+ "WHERE docBaseType = 'MMS' AND cdt.IsSOTrx='N' AND mi.DateAcct >= ? AND mi.DateAcct < ? AND "
				//+ "WHERE docBaseType = 'MMS' AND mi.DateAcct >= ? AND mi.DateAcct < ? AND "

				+ "mi.AD_Client_ID = ? AND mi.docStatus in ('CO','CL','RE') AND mil.M_Product_ID=?");
		BigDecimal vendorReturn = DB.getSQLValueBDEx(get_TrxName(), vendorReturnSql.toString(), 
				new Object[]{period.getStartDate(), endDate, p_AD_Client_ID, M_Product_ID});

		//Production Issue
		StringBuilder productionQtySql = new StringBuilder();
		productionQtySql.append("select coalesce(sum(mpl.qtyused),0) " + 
				"from m_production mp " + 
				"join m_productionline mpl on mpl.m_production_id=mp.m_production_id " + 
				"where mpl.m_product_id!=mp.m_product_id " + 
				"and mpl.m_product_id=? and mp.docstatus in ('CO','CL','RE') " + 
				"and mp.movementdate >= ? AND mp.movementdate < ?");

		BigDecimal productionQty = DB.getSQLValueBD(get_TrxName(), productionQtySql.toString(), 
				new Object[]{M_Product_ID, period.getStartDate(), endDate});																												//MR AND MR FUEL;

		return miscIssueQty.add(physicalInventoryQty).add(vendorReturn).add(productionQty);

	}

	private BigDecimal getShipmentQty(int M_Product_ID, MPeriod period)
	{
		Timestamp endDate = TimeUtil.addDays(period.getEndDate(), 1);

		StringBuilder shipmentQtySql = new StringBuilder();
		shipmentQtySql.append("SELECT COALESCE(SUM(movementQty),0) FROM M_InOutLine mil "
				+ "JOIN M_InOut mi ON mil.M_InOut_ID = mi.M_InOut_ID "
				+ "JOIN C_DocType cdt ON cdt.C_DocType_ID = mi.C_DocType_ID "
				+ "WHERE docBaseType = 'MMS' AND cdt.IsSOTrx='Y' AND mi.DateAcct >= ? AND mi.DateAcct < ? AND "
				//+ "WHERE docBaseType = 'MMS' AND mi.DateAcct >= ? AND mi.DateAcct < ? AND "

				+ "mi.AD_Client_ID = ? AND mi.docStatus in ('CO','CL','RE') AND mil.M_Product_ID=?");
		BigDecimal shipmentQty = DB.getSQLValueBDEx(get_TrxName(), shipmentQtySql.toString(), 
				new Object[]{period.getStartDate(), endDate, p_AD_Client_ID, M_Product_ID});

		return shipmentQty;

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
