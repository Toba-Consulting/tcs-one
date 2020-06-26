package org.compiere.process;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.logging.Level;

import org.adempiere.exceptions.AdempiereException;
import org.compiere.model.MInOut;
import org.compiere.model.MPeriod;
import org.compiere.model.MProduct;
import org.compiere.model.Query;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.TimeUtil;

/**
 * @author Phie Albert
 * Run process Calculate Costing Periodic before run this process
 */
public class HBC_UpdateCostDetail extends SvrProcess{
	
	private int p_AD_Client_ID = 0;
	private int p_C_Period_ID = 0;
	private int p_M_Product_ID = 0;
	
	
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
			
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
	}
	
	protected String doIt() throws Exception {
		if(p_AD_Client_ID == 0)
			throw new AdempiereException("Client is mandatory...");
		
		if(p_C_Period_ID == 0)
			throw new AdempiereException("Period is mandatory...");
		
		MPeriod period = new MPeriod(getCtx(), p_C_Period_ID, get_TrxName());
		Timestamp endDate = TimeUtil.addDays(period.getEndDate(), 1);
		
		StringBuilder sb;
		for(int id : getInventoryProduct())
		{
			sb = new StringBuilder();
			BigDecimal costPrice = Env.ZERO;
			
			sb.append("SELECT COALESCE(CostPrice,0) FROM M_Periodic_Cost "
					+ "WHERE AD_Client_ID = ? AND M_Product_ID = ? AND C_Period_ID = ?");
			costPrice = DB.getSQLValueBDEx(get_TrxName(), sb.toString(), 
					new Object[]{p_AD_Client_ID, id, p_C_Period_ID});
			
			if(costPrice == null)
				continue;
			
			sb = new StringBuilder("UPDATE M_CostDetail Set CurrentCostPrice = ? WHERE M_Product_ID = ? "
					+ "AND M_InventoryLine_ID IN ("
					+ "SELECT M_InventoryLine_ID "
					+ "FROM M_InventoryLine mil "
					+ "JOIN M_Inventory mi ON mi.M_Inventory_ID = mil.M_Inventory_ID "
					+ "JOIN C_DocType dt ON mi.C_DocType_ID=dt.C_DocType_ID "
					
					+ "WHERE mil.M_Product_ID = ? AND mil.AD_Client_ID = ? AND mi.docStatus IN ('CO','CL','RE') AND dt.DocSubTypeInv IN ('PI','IU') "
					//+ "WHERE isUnitCost='N' AND mil.M_Product_ID = ? AND mil.AD_Client_ID = ? AND mi.docStatus IN ('CO','CL','RE') "
					
					+ "AND (mi.movementDate >= ? AND mi.movementdate < ?)"
					+ ")");
			//@David
			//Include M_InOut
			sb.append(" OR M_InOutLine_ID IN("
					+ "SELECT M_InOutLine_ID "
					+ "FROM M_InOutLine mil "
					+ "JOIN M_InOut mi ON mi.M_InOut_ID=mil.M_InOut_ID "
					+ "WHERE IsSoTrx='Y' AND mil.M_Product_ID = ? AND mil.AD_Client_ID = ? AND mi.DocStatus IN ('CO','CL','RE') "
					+ "AND mi.MovementDate BETWEEN ? AND ?"
					+ ")");
			DB.executeUpdateEx(sb.toString(), new Object[]{costPrice, id, id, 
//				p_AD_Client_ID, period.getStartDate(), endDate}, get_TrxName());
				p_AD_Client_ID, period.getStartDate(), endDate,
				id,	p_AD_Client_ID, period.getStartDate(), endDate}, get_TrxName());
					
			sb = new StringBuilder("UPDATE M_CostDetail Set Amt = CurrentCostPrice*Qty WHERE M_Product_ID = ? "
					+ "AND M_InventoryLine_ID IN ("
					+ "SELECT M_InventoryLine_ID "
					+ "FROM M_InventoryLine mil "
					+ "JOIN M_Inventory mi ON mi.M_Inventory_ID = mil.M_Inventory_ID "
					+ "JOIN C_DocType dt ON mi.C_DocType_ID=dt.C_DocType_ID "
					
					+ "WHERE mil.M_Product_ID = ? AND mil.AD_Client_ID = ? AND mi.docStatus IN ('CO','CL','RE') AND dt.DocSubTypeInv IN ('PI','IU') "
					//+ "WHERE isUnitCost='N' AND mil.M_Product_ID = ? AND mil.AD_Client_ID = ? AND mi.docStatus IN ('CO','CL','RE') "
					
					+ "AND (mi.movementDate >= ? AND mi.movementdate < ?)"
					+ ")");
			sb.append(" OR M_InOutLine_ID IN("
					+ "SELECT M_InOutLine_ID "
					+ "FROM M_InOutLine mil "
					+ "JOIN M_InOut mi ON mi.M_InOut_ID=mil.M_InOut_ID "
					+ "WHERE IsSoTrx='Y' AND mil.M_Product_ID = ? AND mil.AD_Client_ID = ? AND mi.DocStatus IN ('CO','CL','RE') "
					+ "AND mi.MovementDate BETWEEN ? AND ?"
					+ ")");
			DB.executeUpdateEx(sb.toString(), new Object[]{id, id, 
//				p_AD_Client_ID, period.getStartDate(), endDate}, get_TrxName());
				p_AD_Client_ID, period.getStartDate(), endDate,
				id,	p_AD_Client_ID, period.getStartDate(), endDate}, get_TrxName());

		}
		
		//Delete Fact
		sb = new StringBuilder("UPDATE M_Inventory SET Posted='N' "
				+ "WHERE AD_Client_ID =? AND MovementDate >= ? AND MovementDate < ? AND DocStatus IN ('CO','CL','RE')");
		DB.executeUpdateEx(sb.toString(), new Object[]{
			p_AD_Client_ID, period.getStartDate(), endDate}, get_TrxName());
		
		sb = new StringBuilder("DELETE FROM Fact_Acct WHERE AD_Table_ID = 321 AND Record_ID IN ("
				+ "SELECT M_Inventory_ID FROM M_Inventory WHERE AD_Client_ID = ? "
				+ "AND MovementDate >= ? AND MovementDate < ? AND DocStatus IN ('CO','CL','RE'))");
		DB.executeUpdateEx(sb.toString(), new Object[]{p_AD_Client_ID, period.getStartDate(), endDate}, get_TrxName());
		//end Delete Fact
		
		//@David
		//Include M_InOut
		sb = new StringBuilder("UPDATE M_InOut SET Posted='N' "
				+ "WHERE IsSoTrx = 'Y' AND AD_Client_ID =? AND MovementDate >= ? AND MovementDate < ? AND DocStatus IN ('CO','CL','RE')");
		DB.executeUpdateEx(sb.toString(), new Object[]{
			p_AD_Client_ID, period.getStartDate(), endDate}, get_TrxName());
		
		sb = new StringBuilder("DELETE FROM Fact_Acct WHERE AD_Table_ID = 319 AND Record_ID IN ("
				+ "SELECT M_InOut_ID FROM M_InOut WHERE IsSoTrx='Y' AND AD_Client_ID = ? "
				+ "AND MovementDate >= ? AND MovementDate < ? AND DocStatus IN ('CO','CL','RE'))");
		DB.executeUpdateEx(sb.toString(), new Object[]{p_AD_Client_ID, period.getStartDate(), endDate}, get_TrxName());
		
		//Customer Return Doc
		/*
		sb = new StringBuilder("SELECT retur.M_InOut_ID FROM M_InOut retur"
				+ "JOIN M_RMA rma on rma.M_RMA_ID=retur.M_RMA_ID"
				+ "WHERE rma.InOut_ID IN ("
				+ "		SELECT M_InOut_ID FROM M_InOut"
				+ "		WHERE IsSoTrx = 'Y' AND AD_Client_ID =? AND MovementDate >= ? AND MovementDate < ? AND DocStatus IN ('CO','CL')"
				+ ")");
		*/
		sb = new StringBuilder("rma.InOut_ID IN ("
				+ "		SELECT M_InOut_ID FROM M_InOut io"
				+ "		WHERE io.IsSoTrx = 'Y' AND io.AD_Client_ID ="+p_AD_Client_ID+" AND io.MovementDate >= '"+period.getStartDate()+"' AND io.MovementDate < '"+endDate+"' AND io.DocStatus IN ('CO','CL','RE')"
				+ ")");
		int [] retur_ID = new Query(getCtx(), MInOut.Table_Name, sb.toString(), get_TrxName())
						.addJoinClause("JOIN M_RMA rma on rma.M_RMA_ID=M_InOut.M_RMA_ID")
						.getIDs();
		if (retur_ID.length>0) {
			String stringRetur_IDs="";
			for (int i : retur_ID) {
				stringRetur_IDs += i+", ";
			}
			stringRetur_IDs=stringRetur_IDs.substring(0, stringRetur_IDs.length()-2);
			
			sb = new StringBuilder("UPDATE M_InOut SET Posted='N' "
					+ "WHERE M_InOut_ID IN("+stringRetur_IDs+")");
			DB.executeUpdateEx(sb.toString(), get_TrxName());
			
			sb = new StringBuilder("DELETE FROM Fact_Acct WHERE AD_Table_ID = 319 AND Record_ID IN ("+stringRetur_IDs+")");
			DB.executeUpdateEx(sb.toString(), get_TrxName());

		}
		
		//@David End
		
		return "The process is done";
	}
	
	private int[] getInventoryProduct()
	{
		StringBuilder whereClause = new StringBuilder();
		
		if (p_M_Product_ID > 0)
			whereClause.append("M_Product_ID=" + p_M_Product_ID);
		else 
			whereClause.append("ProductType='I' AND AD_Client_ID = " + p_AD_Client_ID);
		
		return new Query(getCtx(), MProduct.Table_Name, whereClause.toString(), get_TrxName())
								.getIDs();
	}
}
