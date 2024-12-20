package org.compiere.process;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.logging.Level;

import org.compiere.model.MAcctSchema;
import org.compiere.model.MCostDetail;
import org.compiere.model.MInOutLine;
import org.compiere.model.MInventoryLine;
import org.compiere.model.MProductionLine;
import org.compiere.util.DB;
import org.compiere.util.Env;

public class HWHCreateMCostDetailForAvgPO extends SvrProcess {

	int p_C_Period_ID = 0;
	
	@Override
	protected void prepare() {

		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++)
		{
			String name = para[i].getParameterName();
			if (para[i].getParameter() == null)
				;
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}

	}

	@Override
	protected String doIt() throws Exception {

		//shipment

		String sqlShipment = "select m_inoutline_id from m_inoutline where m_inout_id IN (" + 
				"select m_inout_id from m_inout " + 
				"where issotrx='Y' and docstatus IN ('CO','CL') AND movementdate > ? " + 
				"and posted!='Y') and m_inoutline_id NOT IN (select m_inoutline_id from m_costdetail "
				+ "where m_inoutline_id is not null and ad_client_id=1000000)"; 

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int countShipment = 0;
		MAcctSchema acctSchema = new MAcctSchema(getCtx(), 1000000, get_TrxName());
		try
		{
			pstmt = DB.prepareStatement (sqlShipment, get_TrxName());

			rs = pstmt.executeQuery ();
			while (rs.next()) {
				MInOutLine ioLine = new MInOutLine(getCtx(), rs.getInt(1), get_TrxName());
				MCostDetail.createShipment(acctSchema, ioLine.getAD_Org_ID(), ioLine.getM_Product_ID(), 
						ioLine.getM_AttributeSetInstance_ID(), ioLine.get_ID(), 1000001, Env.ONE, ioLine.getMovementQty().negate(), 
						ioLine.getDescription(), ioLine.getParent().isSOTrx(), get_TrxName());
				countShipment++;

			}
		}
		catch (Exception e)
		{
			log.log (Level.SEVERE, sqlShipment, e);
		}
		finally
		{
			DB.close(rs, pstmt);
			rs = null;
			pstmt = null;
		}


		//inventory
		String sqlInventory = "select m_inventoryline_id from m_inventoryline where m_inventory_id IN (" + 
				"select m_inventory_id from m_inventory " + 
				"where docstatus IN ('CO','CL') " + 
				") and m_inventoryline_id NOT IN (select m_inventoryline_id from m_costdetail "
				+ "where m_inventoryline_id is not null and ad_client_id=1000000)"; 

		pstmt = null;
		rs = null;
		int countInventory = 0;
		try
		{
			pstmt = DB.prepareStatement (sqlInventory, get_TrxName());

			rs = pstmt.executeQuery ();
			while (rs.next()) {
				MInventoryLine invLine = new MInventoryLine(getCtx(), rs.getInt(1), get_TrxName());
				MCostDetail.createInventory(acctSchema, invLine.getAD_Org_ID(), invLine.getM_Product_ID(), 
						invLine.getM_AttributeSetInstance_ID(), invLine.get_ID(), 1000001, Env.ONE, invLine.getMovementQty(), 
						invLine.getDescription(), get_TrxName());
				countInventory++;

			}
		}
		catch (Exception e)
		{
			log.log (Level.SEVERE, sqlInventory, e);
		}
		finally
		{
			DB.close(rs, pstmt);
			rs = null;
			pstmt = null;
		}


		//production
		String sqlProduction = "select m_productionline_id from m_productionline where m_production_id IN (" + 
				"select m_production_id from m_production " + 
				"where docstatus IN ('CO','CL','RE') " + 
				"and posted!='Y') and m_productionline_id NOT IN (select m_productionline_id from m_costdetail "
				+ "where m_productionline_id is not null and ad_client_id=1000000)"; 

		pstmt = null;
		rs = null;
		int countProduction = 0;
		try
		{
			pstmt = DB.prepareStatement (sqlProduction, get_TrxName());

			rs = pstmt.executeQuery ();
			while (rs.next()) {
				MProductionLine productionLine = new MProductionLine(getCtx(), rs.getInt(1), get_TrxName());
				MCostDetail.createProduction(acctSchema, productionLine.getAD_Org_ID(), productionLine.getM_Product_ID(), 
						productionLine.getM_AttributeSetInstance_ID(), productionLine.get_ID(), 1000001, Env.ONE, productionLine.getMovementQty(), 
						productionLine.getDescription(), get_TrxName());
				countProduction++;

			}
		}
		catch (Exception e)
		{
			log.log (Level.SEVERE, sqlProduction, e);
		}
		finally
		{
			DB.close(rs, pstmt);
			rs = null;
			pstmt = null;
		}


		return "Created average PO cost record for " + countShipment + " shipments, " + countInventory + " inventories, "
				+ countProduction + " production";
	}

}
