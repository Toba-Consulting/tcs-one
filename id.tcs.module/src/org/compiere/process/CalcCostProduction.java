package org.compiere.process;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.util.List;
import java.util.logging.Level;

import org.adempiere.exceptions.AdempiereException;
import org.compiere.model.I_M_Periodic_Cost;
import org.compiere.model.MAcctSchema;
import org.compiere.model.MCostDetail;
import org.compiere.model.MPeriod;
import org.compiere.model.MProduct;
import org.compiere.model.MProduction;
import org.compiere.model.MProductionLine;
import org.compiere.model.Query;
import org.compiere.model.X_M_Periodic_Cost;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.TimeUtil;

public class CalcCostProduction extends SvrProcess{

	private int p_C_Period_ID = 0;
	private int p_M_Product_ID = 0;
	//	private int p_M_Product_Category_ID = 0;
	private int p_C_AcctSchema_ID = 0;
	private MAcctSchema as;
	private int M_CostElement_ID = 0;
	private int p_Line = 1;

	protected void prepare() {
		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++)
		{
			String name = para[i].getParameterName();
			if (para[i].getParameter() == null)
				;
			else if (name.equals("C_Period_ID"))
				p_C_Period_ID = para[i].getParameterAsInt();
			else if (name.equals("M_Product_ID"))
				p_M_Product_ID = para[i].getParameterAsInt();
			//			else if (name.equals("M_Product_Category_ID"))
			//				p_M_Product_Category_ID = para[i].getParameterAsInt();
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

		if(p_C_Period_ID == 0)
			throw new AdempiereException("Period is mandatory...");

		if(p_C_AcctSchema_ID == 0)
			throw new AdempiereException("Acct Schema is mandatory...");

		as = MAcctSchema.get(getCtx(), p_C_AcctSchema_ID);
		M_CostElement_ID=1000001;

		//Get product that will be processes
		if(p_M_Product_ID != 0)
			UpdateProductionCostDetail(period, p_M_Product_ID);
		//		else if(p_M_Product_Category_ID != 0){
		//			for (int productID : getInventoryProductForProductCategory(p_M_Product_Category_ID))
		//				UpdateProductionCostDetail(period, productID);
		//		}
		else{
			for (int productID : getInventoryProduct())
				UpdateProductionCostDetail(period, productID);
		}

		return "Success";
	}


	private void UpdateProductionCostDetail(MPeriod period, int productID) {

		String whereClause = "m_product_id =? and docstatus in ('CO','CL','RE') AND MovementDate BETWEEN ? AND ?";

		Timestamp endDate = TimeUtil.addDays(period.getEndDate(), 1);

		List<MProduction> productions = new Query(getCtx(), MProduction.Table_Name, whereClause, get_TrxName())
				.setParameters(new Object[] {productID, period.getStartDate(), endDate})
				.setOnlyActiveRecords(true)
				.list();

		for (MProduction production: productions) {
			MProductionLine[] prodLines = production.getLines();
			BigDecimal fgAmt = Env.ZERO;
			BigDecimal fgQty = Env.ZERO;
			MProductionLine fgLine = null;

			for (MProductionLine line : prodLines) {
				BigDecimal lineAmt  = Env.ZERO;
				BigDecimal lineQty = Env.ZERO;
				if (line.getM_Product_ID() == production.getM_Product_ID()) {
					fgQty = line.getMovementQty();
					fgLine = line;

				} else {
					X_M_Periodic_Cost linePeriodicCost = new Query(getCtx(), I_M_Periodic_Cost.Table_Name, "M_Product_ID=?", get_TrxName())
							.setParameters(new Object[] {line.getM_Product_ID()})
							.first();

					lineQty = line.getMovementQty();

					if (linePeriodicCost == null) {
						lineAmt = Env.ONE;
						int M_Product_ID = line.getM_Product_ID();
						int M_ProductionLine_ID = line.get_ID();
					} else {
						lineAmt = lineQty.multiply(linePeriodicCost.getcostprice()).abs();
					}

					fgAmt = fgAmt.add(lineAmt);

					//Update Component Cost Detail
					updateProductionLineCostDetail(line, lineAmt.divide(lineQty.abs(), 9, RoundingMode.HALF_DOWN), lineQty);
				}

			}
			//Update Finished Goods Cost Detail
			updateProductionLineCostDetail(fgLine, fgAmt.divide(fgQty, 9, RoundingMode.HALF_DOWN), fgQty);
		}

	}

	private void updateProductionLineCostDetail (MProductionLine line, BigDecimal lineCost, BigDecimal lineQty) {

		//Check for cost detail, if not exists then create cost detail
		int p_CostDetail = new Query(getCtx(), MCostDetail.Table_Name, "M_ProductionLine_ID=?", get_TrxName())
				.setParameters(new Object[] {line.get_ID()})
				.firstIdOnly();

		if (p_CostDetail == -1) {
			MCostDetail.createProduction(as, line.getAD_Org_ID(), line.getM_Product_ID(), line.getM_AttributeSetInstance_ID(),
					line.get_ID(), M_CostElement_ID, lineCost.multiply(lineQty), lineQty, line.getDescription(), get_TrxName());

		} else {
			//Update Component Cost Detail

			BigDecimal amt = lineCost.multiply(lineQty);
			String sql = "UPDATE M_CostDetail Set CurrentCostPrice = ?, Qty = ?, Amt = ? WHERE M_CostDetail_ID=?";

			DB.executeUpdateEx(sql, new Object[]{lineCost, lineQty, amt, p_CostDetail}, get_TrxName());

			//			MCostDetail compCostDetail = new MCostDetail(getCtx(), p_CostDetail, get_TrxName());
			//			compCostDetail.setCurrentCostPrice(lineCost);
			//			compCostDetail.setAmt(lineCost.multiply(lineQty));
			//			compCostDetail.setQty(lineQty);
			//			compCostDetail.saveEx();	
		}
	}
	private int[] getInventoryProduct()
	{
		//		String whereClause = "AD_Client_ID = ? AND ProductType='I' AND IsBOM='Y' "
		//				+ "and exists (select 1 from m_product_bom where m_product_bom.m_product_id = m_product.m_product_id) "
		//				+ "and exists (select 1 from m_production mp where mp.m_product_id = m_product.m_product_id and docstatus in ('CO','CL','RE') "
		//				+ "and mp.movementdate between date '2020-01-01' and date'2020-01-31')";

		String whereClause = "";
		if (p_Line==1) {
			whereClause = "m_product_id in (1010002,1010337,1010340,1010346,1010504,1010511)";
		} else {
			//whereClause = "m_product_id in (1007069,1007073,1007095,1007105,1010089,1010091,1010092,1010398,1010512,1010603)";
			whereClause = "m_product_id in (1010512)";
		}

		return new Query(getCtx(), MProduct.Table_Name, whereClause, get_TrxName())
				//.setParameters(new Object[]{Env.getAD_Client_ID(getCtx())})
				.getIDs();
	}

	private int[] getInventoryProductForProductCategory(int M_Product_Category_ID)
	{
		String whereClause = "AD_Client_ID = ? AND ProductType='I' AND IsBOM='Y' AND M_Product_Category_ID = ?";
		return new Query(getCtx(), MProduct.Table_Name, whereClause, get_TrxName())
				.setParameters(new Object[]{Env.getAD_Client_ID(getCtx()), M_Product_Category_ID})						
				.getIDs();
	}

	private String getListProduct_ID()
	{
		StringBuilder tmp = new StringBuilder();
		if(p_M_Product_ID != 0)
			return Integer.toString(p_M_Product_ID);
		//		else if(p_M_Product_Category_ID != 0)
		//		{
		//			for(int id : getInventoryProductForProductCategory(p_M_Product_Category_ID)){
		//				tmp.append(id);
		//				tmp.append(",");
		//			}
		//			tmp.delete(tmp.length()-1, tmp.length());
		//			return tmp.toString();
		//		}
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
