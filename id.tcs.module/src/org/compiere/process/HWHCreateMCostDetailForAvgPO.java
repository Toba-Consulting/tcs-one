package org.compiere.process;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.logging.Level;

import org.compiere.model.MAcctSchema;
import org.compiere.model.MCostDetail;
import org.compiere.model.MProductionLine;
import org.compiere.util.DB;
import org.compiere.util.Env;

public class HWHCreateMCostDetailForAvgPO extends SvrProcess {

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
		/*
		String sql = "select m_inoutline_id from m_inoutline where m_inout_id IN (" + 
				"select m_inout_id from m_inout " + 
				"where issotrx='Y' and docstatus IN ('CO','CL') " + 
				"and posted!='Y') and m_inoutline_id NOT IN (select m_inoutline_id from m_costdetail "
				+ "where m_inoutline_id is not null and ad_client_id=1000000)"; 

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int count = 0;
		MAcctSchema acctSchema = new MAcctSchema(getCtx(), 1000000, get_TrxName());
		try
		{
			pstmt = DB.prepareStatement (sql, null);

			rs = pstmt.executeQuery ();
			while (rs.next()) {
				MInOutLine ioLine = new MInOutLine(getCtx(), rs.getInt(1), get_TrxName());
				MCostDetail.createShipment(acctSchema, ioLine.getAD_Org_ID(), ioLine.getM_Product_ID(), 
						ioLine.getM_AttributeSetInstance_ID(), ioLine.get_ID(), 1000001, Env.ONE, ioLine.getMovementQty(), 
						ioLine.getDescription(), ioLine.getParent().isSOTrx(), get_TrxName());
				count++;

			}
		}
		catch (Exception e)
		{
			log.log (Level.SEVERE, sql, e);
		}
		finally
		{
			DB.close(rs, pstmt);
			rs = null;
			pstmt = null;
		}
		 */
		//		
		//		//inventory
		//		String sql = "select m_inventoryline_id from m_inventoryline where m_inventory_id IN (" + 
		//				"select m_inventory_id from m_inventory " + 
		//				"where docstatus IN ('CO','CL') " + 
		//				"and posted!='Y') and m_inventoryline_id NOsT IN (select m_inventoryline_id from m_costdetail "
		//				+ "where m_inventoryline_id is not null and ad_client_id=1000000)"; 
		//		
		//		PreparedStatement pstmt = null;
		//		ResultSet rs = null;
		//		int count = 0;
		//		MAcctSchema acctSchema = new MAcctSchema(getCtx(), 1000000, get_TrxName());
		//		try
		//		{
		//			pstmt = DB.prepareStatement (sql, null);
		//
		//			rs = pstmt.executeQuery ();
		//			while (rs.next()) {
		//				MInventoryLine ioLine = new MInventoryLine(getCtx(), rs.getInt(1), get_TrxName());
		//				MCostDetail.createInventory(acctSchema, ioLine.getAD_Org_ID(), ioLine.getM_Product_ID(), 
		//						ioLine.getM_AttributeSetInstance_ID(), ioLine.get_ID(), 1000001, Env.ONE, ioLine.getMovementQty(), 
		//						ioLine.getDescription(), get_TrxName());
		//				count++;
		//				
		//			}
		//		}
		//		catch (Exception e)
		//		{
		//			log.log (Level.SEVERE, sql, e);
		//		}
		//		finally
		//		{
		//			DB.close(rs, pstmt);
		//			rs = null;
		//			pstmt = null;
		//		}


		//production
		String sql = "select m_productionline_id from m_productionline where m_production_id IN (" + 
				"select m_production_id from m_production " + 
				"where docstatus IN ('CO','CL','RE') " + 
				"and posted!='Y') and m_productionline_id NOT IN (select m_productionline_id from m_costdetail "
				+ "where m_productionline_id is not null and ad_client_id=1000000)"; 

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int count = 0;
		MAcctSchema acctSchema = new MAcctSchema(getCtx(), 1000000, get_TrxName());
		try
		{
			pstmt = DB.prepareStatement (sql, null);

			rs = pstmt.executeQuery ();
			while (rs.next()) {
				MProductionLine ioLine = new MProductionLine(getCtx(), rs.getInt(1), get_TrxName());
				MCostDetail.createProduction(acctSchema, ioLine.getAD_Org_ID(), ioLine.getM_Product_ID(), 
						ioLine.getM_AttributeSetInstance_ID(), ioLine.get_ID(), 1000001, Env.ONE, ioLine.getMovementQty(), 
						ioLine.getDescription(), get_TrxName());
				count++;

			}
		}
		catch (Exception e)
		{
			log.log (Level.SEVERE, sql, e);
		}
		finally
		{
			DB.close(rs, pstmt);
			rs = null;
			pstmt = null;
		}


		return "Created average PO cost record for " + count + " products";
	}

}
