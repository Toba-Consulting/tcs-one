package id.tcs.process;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.logging.Level;

import org.compiere.model.MProduct;
import org.compiere.model.Query;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;

/**
 * @author stephan
 * inventory transaction report with param date and locator
 */
public class TCS_InventoryTrxReport extends SvrProcess{

	private Timestamp p_movementDate = null;
	private Timestamp p_movementDateTo = null;
	private int p_M_Locator_ID = 0;
	private int[] product_ids = null;
	
	@Override
	protected void prepare() {
		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++) {
			String name = para[i].getParameterName();
			if (para[i].getParameter() == null)
				;
			else if (name.equals("MovementDate")){
				p_movementDate = para[i].getParameterAsTimestamp();
				p_movementDateTo = para[i].getParameter_ToAsTimestamp();
			}
			else if (name.equals("M_Locator_ID"))
				p_M_Locator_ID = para[i].getParameterAsInt();
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
	}

	@Override
	protected String doIt() throws Exception {
		cleanTable();
		createProductLines();
		
		return "P_Instance_ID = "+getAD_PInstance_ID();
	}

	private void createProductLines(){
		
		/*String where = " M_Product_Category_ID IN (SELECT M_Product_Category_ID FROM M_Product_Category "
				//+ "WHERE VALUE LIKE 'G%') "; //GIO-2328 add validation for product in report (use product with value like G%) @Febrian
				+ "WHERE isNonStocked = 'N') ";*/
		String where  = " isStocked = 'Y'";
		product_ids= new Query(getCtx(), MProduct.Table_Name, where, get_TrxName())
		.setOnlyActiveRecords(true)
		.setClient_ID()
		.getIDs();
		
		for (int i : product_ids) {
			BigDecimal firstQty = getQtyFirst(i);
			BigDecimal qtyIn = getQtyIn(i);
			BigDecimal qtyOut = getQtyOut(i);
			//BigDecimal qtyPemutihan = getQtyPemutihan(i);
			BigDecimal lastQty = firstQty.add(qtyIn).add(qtyOut);

			//iqbal
//			BigDecimal firstQtyPcs = getQtyPcsFirst(i);
//			BigDecimal qtyPcsIn = getQtyPcsIn(i);
//			BigDecimal qtyPcsOut = getQtyPcsOut(i);
//			BigDecimal lastQtyPcs = firstQtyPcs.add(qtyPcsIn).add(qtyPcsOut);
//			
			if(lastQty == null)
				lastQty = Env.ZERO;
			
//			StringBuilder sb = new StringBuilder();
//			sb.append("INSERT INTO T_InventoryTrx (AD_PInstance_ID, AD_Client_ID, AD_Org_ID, M_Locator_ID ,M_Product_ID, "
//					+ "FirstQty, QtyIn, QtyOut, LastQty, StartDate, EndDate, FirstQtyPcs, QtyPcsIn, QtyPcsOut, LastQtyPcs) ")
//			.append("VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
//			Object param[] = new Object[]{getAD_PInstance_ID(), getAD_Client_ID(), 
//					Env.getAD_Org_ID(getCtx()), p_M_Locator_ID ,i ,firstQty, qtyIn,
//					qtyOut, lastQty,p_movementDate,p_movementDateTo,
//					firstQtyPcs, qtyPcsIn, qtyPcsOut, lastQtyPcs};
			StringBuilder sb = new StringBuilder();
			sb.append("INSERT INTO T_InventoryTrx (AD_PInstance_ID, AD_Client_ID, AD_Org_ID, M_Locator_ID ,M_Product_ID, "
					+ "FirstQty, QtyIn, QtyOut, LastQty, StartDate, EndDate) ")
			.append("VALUES(?,?,?,?,?,?,?,?,?,?,?)");
			Object param[] = new Object[]{getAD_PInstance_ID(), getAD_Client_ID(), 
					Env.getAD_Org_ID(getCtx()), p_M_Locator_ID ,i ,firstQty, qtyIn,
					qtyOut, lastQty,p_movementDate,p_movementDateTo};
			int no = DB.executeUpdate(sb.toString(), param, true, get_TrxName());
			log.fine("#" + no);
		}
	}
	
	private BigDecimal getQtyFirst(int i) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT SUM(MovementQty) FROM M_Transaction mt "
				+ "WHERE MovementDate < '" + p_movementDate + "' "
				+ "AND M_Product_ID=? AND M_Locator_ID=? "
				//@phie
				+ "AND (exists ( "
				+ "select 1 from m_inoutline iol "
				+ "join m_inout io on iol.m_inout_id = io.m_inout_id "
				+ "where io.docStatus in ('CO', 'CL') and iol.m_inoutline_id = coalesce(mt.m_inoutline_id,0) "
				+ ") OR exists ( "
				+ "select 1 from m_inventoryline mil "
				+ "join m_inventory mi on mil.m_inventory_id = mi.m_inventory_id "
				+ "where mi.docStatus in ('CO', 'CL') and mil.m_inventoryline_id = coalesce( mt.m_inventoryline_id,0) "
				+ ") OR exists ( "
				+ "select 1 from m_productionline mpl "
				+ "join m_production mp on mpl.m_production_id = mp.m_production_id "
				+ "where mp.docStatus in ('CO', 'CL') and mpl.m_productionline_id = coalesce( mt.m_productionline_id,0) "
				+ ") OR exists ( "
				+ "select 1 from m_movementline mml "
				+ "join m_movement mm on mml.m_movement_id = mm.m_movement_id "
				+ "where mm.docStatus in ('CO', 'CL') and mml.m_movementline_id = coalesce( mt.m_movementline_id,0)))");
				//@phie
		BigDecimal sumQty = new BigDecimal(0);
		sumQty = DB.getSQLValueBD(get_TrxName(), sql.toString(), new Object[]{i,p_M_Locator_ID});
		
		if(sumQty == null)
			return Env.ZERO;
		
		return sumQty;
	}
	
	private BigDecimal getQtyIn(int i){
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT SUM(MovementQty) FROM M_Transaction mt "
				+ "WHERE MovementDate BETWEEN '" + p_movementDate + "' AND '"+p_movementDateTo+"' "
				+ "AND M_Product_ID=? AND M_Locator_ID=? AND MovementType LIKE '%+' "
				//@phie
				+ "AND (exists ( "
				+ "select 1 from m_inoutline iol "
				+ "join m_inout io on iol.m_inout_id = io.m_inout_id "
				+ "where io.docStatus in ('CO', 'CL') and iol.m_inoutline_id = coalesce(mt.m_inoutline_id,0) "
				+ ") OR exists ( "
				+ "select 1 from m_inventoryline mil "
				+ "join m_inventory mi on mil.m_inventory_id = mi.m_inventory_id "
				+ "where mi.docStatus in ('CO', 'CL') and mil.m_inventoryline_id = coalesce( mt.m_inventoryline_id,0) "
				+ ") OR exists ( "
				+ "select 1 from m_productionline mpl "
				+ "join m_production mp on mpl.m_production_id = mp.m_production_id "
				+ "where mp.docStatus in ('CO', 'CL') and mpl.m_productionline_id = coalesce( mt.m_productionline_id,0) "
				+ ") OR exists ( "
				+ "select 1 from m_movementline mml "
				+ "join m_movement mm on mml.m_movement_id = mm.m_movement_id "
				+ "where mm.docStatus in ('CO', 'CL') and mml.m_movementline_id = coalesce( mt.m_movementline_id,0)))");
				//end phie
		BigDecimal sumQty = new BigDecimal(0);
		sumQty = DB.getSQLValueBD(get_TrxName(), sql.toString(), new Object[]{i,p_M_Locator_ID});
		
		if(sumQty == null)
			return Env.ZERO;
		
		return sumQty;
	}
	
	private BigDecimal getQtyOut(int i){
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT SUM(MovementQty) FROM M_Transaction mt "
				+ "WHERE MovementDate BETWEEN '" + p_movementDate + "' AND '"+p_movementDateTo+"' "
				+ "AND M_Product_ID=? AND M_Locator_ID=? AND MovementType LIKE '%-' "
				//@phie
				+ "AND (exists ( "
				+ "select 1 from m_inoutline iol "
				+ "join m_inout io on iol.m_inout_id = io.m_inout_id "
				+ "where io.docStatus in ('CO', 'CL') and iol.m_inoutline_id = coalesce(mt.m_inoutline_id,0) "
				+ ") OR exists ( "
				+ "select 1 from m_inventoryline mil "
				+ "join m_inventory mi on mil.m_inventory_id = mi.m_inventory_id "
				+ "where mi.docStatus in ('CO', 'CL') and mil.m_inventoryline_id = coalesce( mt.m_inventoryline_id,0) "
				+ ") OR exists ( "
				+ "select 1 from m_productionline mpl "
				+ "join m_production mp on mpl.m_production_id = mp.m_production_id "
				+ "where mp.docStatus in ('CO', 'CL') and mpl.m_productionline_id = coalesce( mt.m_productionline_id,0) "
				+ ") OR exists ( "
				+ "select 1 from m_movementline mml "
				+ "join m_movement mm on mml.m_movement_id = mm.m_movement_id "
				+ "where mm.docStatus in ('CO', 'CL') and mml.m_movementline_id = coalesce( mt.m_movementline_id,0)))");
				//end phie
		BigDecimal sumQty = new BigDecimal(0);
		sumQty = DB.getSQLValueBD(get_TrxName(), sql.toString(), new Object[]{i,p_M_Locator_ID});
		
		if(sumQty == null)
			return Env.ZERO;
		
		return sumQty;
	}
	
	//@iqbal commented out, not using qtypiece anymore
//	private BigDecimal getQtyPcsFirst(int i) {
//		StringBuilder sql = new StringBuilder();
//		sql.append("SELECT SUM(QtySecondary) FROM M_Transaction mt "
//				+ "WHERE MovementDate < '" + p_movementDate + "' "
//				+ "AND M_Product_ID=? AND M_Locator_ID=?"
//				//end phie
//				+ "AND (exists ( "
//				+ "select 1 from m_inoutline iol "
//				+ "join m_inout io on iol.m_inout_id = io.m_inout_id "
//				+ "where io.docStatus in ('CO', 'CL') and iol.m_inoutline_id = coalesce(mt.m_inoutline_id,0) "
//				+ ") OR exists ( "
//				+ "select 1 from m_inventoryline mil "
//				+ "join m_inventory mi on mil.m_inventory_id = mi.m_inventory_id "
//				+ "where mi.docStatus in ('CO', 'CL') and mil.m_inventoryline_id = coalesce( mt.m_inventoryline_id,0) "
//				+ ") OR exists ( "
//				+ "select 1 from m_productionline mpl "
//				+ "join m_production mp on mpl.m_production_id = mp.m_production_id "
//				+ "where mp.docStatus in ('CO', 'CL') and mpl.m_productionline_id = coalesce( mt.m_productionline_id,0) "
//				+ ") OR exists ( "
//				+ "select 1 from m_movementline mml "
//				+ "join m_movement mm on mml.m_movement_id = mm.m_movement_id "
//				+ "where mm.docStatus in ('CO', 'CL') and mml.m_movementline_id = coalesce( mt.m_movementline_id,0)))");
//		BigDecimal sumQty = new BigDecimal(0);
//		sumQty = DB.getSQLValueBD(get_TrxName(), sql.toString(), new Object[]{i,p_M_Locator_ID});
//		
//		if(sumQty == null)
//			return Env.ZERO;
//		
//		return sumQty;
//	}
//	
//	private BigDecimal getQtyPcsIn(int i){
//		StringBuilder sql = new StringBuilder();
//		sql.append("SELECT SUM(QtySecondary) FROM M_Transaction mt "
//				+ "WHERE MovementDate BETWEEN '" + p_movementDate + "' AND '"+p_movementDateTo+"' "
//				+ "AND M_Product_ID=? AND M_Locator_ID=? AND MovementType LIKE '%+' "
//				//@phie
//				+ "AND (exists ( "
//				+ "select 1 from m_inoutline iol "
//				+ "join m_inout io on iol.m_inout_id = io.m_inout_id "
//				+ "where io.docStatus in ('CO', 'CL') and iol.m_inoutline_id = coalesce(mt.m_inoutline_id,0) "
//				+ ") OR exists ( "
//				+ "select 1 from m_inventoryline mil "
//				+ "join m_inventory mi on mil.m_inventory_id = mi.m_inventory_id "
//				+ "where mi.docStatus in ('CO', 'CL') and mil.m_inventoryline_id = coalesce( mt.m_inventoryline_id,0) "
//				+ ") OR exists ( "
//				+ "select 1 from m_productionline mpl "
//				+ "join m_production mp on mpl.m_production_id = mp.m_production_id "
//				+ "where mp.docStatus in ('CO', 'CL') and mpl.m_productionline_id = coalesce( mt.m_productionline_id,0) "
//				+ ") OR exists ( "
//				+ "select 1 from m_movementline mml "
//				+ "join m_movement mm on mml.m_movement_id = mm.m_movement_id "
//				+ "where mm.docStatus in ('CO', 'CL') and mml.m_movementline_id = coalesce( mt.m_movementline_id,0)))");
//				//end phie
//		BigDecimal sumQty = new BigDecimal(0);
//		sumQty = DB.getSQLValueBD(get_TrxName(), sql.toString(), new Object[]{i,p_M_Locator_ID});
//		
//		if(sumQty == null)
//			return Env.ZERO;
//		
//		return sumQty;
//	}
//	
//	private BigDecimal getQtyPcsOut(int i){
//		StringBuilder sql = new StringBuilder();
//		sql.append("SELECT SUM(QtySecondary) FROM M_Transaction mt "
//				+ "WHERE MovementDate BETWEEN '" + p_movementDate + "' AND '"+p_movementDateTo+"' "
//				+ "AND M_Product_ID=? AND M_Locator_ID=? AND MovementType LIKE '%-' "
//				//@phie
//				+ "AND (exists ( "
//				+ "select 1 from m_inoutline iol "
//				+ "join m_inout io on iol.m_inout_id = io.m_inout_id "
//				+ "where io.docStatus in ('CO', 'CL') and iol.m_inoutline_id = coalesce(mt.m_inoutline_id,0) "
//				+ ") OR exists ( "
//				+ "select 1 from m_inventoryline mil "
//				+ "join m_inventory mi on mil.m_inventory_id = mi.m_inventory_id "
//				+ "where mi.docStatus in ('CO', 'CL') and mil.m_inventoryline_id = coalesce( mt.m_inventoryline_id,0) "
//				+ ") OR exists ( "
//				+ "select 1 from m_productionline mpl "
//				+ "join m_production mp on mpl.m_production_id = mp.m_production_id "
//				+ "where mp.docStatus in ('CO', 'CL') and mpl.m_productionline_id = coalesce( mt.m_productionline_id,0) "
//				+ ") OR exists ( "
//				+ "select 1 from m_movementline mml "
//				+ "join m_movement mm on mml.m_movement_id = mm.m_movement_id "
//				+ "where mm.docStatus in ('CO', 'CL') and mml.m_movementline_id = coalesce( mt.m_movementline_id,0)))");
//				//end phie
//		BigDecimal sumQty = new BigDecimal(0);
//		sumQty = DB.getSQLValueBD(get_TrxName(), sql.toString(), new Object[]{i,p_M_Locator_ID});
//		
//		if(sumQty == null)
//			return Env.ZERO;
//		
//		return sumQty;
//	}
	//iqbal end
	
	/*
	private BigDecimal getQtyPemutihan(int i) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT SUM(cbg.DifferenceQty) FROM C_Barcode_Generator cbg "
				+ "WHERE C_Barcode_Generator_ID IN "
				+ "(SELECT C_Barcode_Generator_ID FROM M_MovementLine ml "
				+ "JOIN M_Transaction mt ON ml.M_MovementLine_ID=mt.M_MovementLine_ID "
				+ "WHERE mt.MovementDate BETWEEN '" + p_movementDate + "' AND '"+p_movementDateTo+"' "
				+ "AND mt.M_Product_ID=? AND mt.M_Locator_ID=?) ");
		BigDecimal sumQty = new BigDecimal(0);
		sumQty = DB.getSQLValueBD(get_TrxName(), sql.toString(), new Object[]{i,p_M_Locator_ID});
		
		if(sumQty == null)
			return Env.ZERO;
		
		return sumQty;
	}
	*/
	
	private void cleanTable(){
		StringBuilder sql = new StringBuilder();
		sql.append("DELETE FROM T_InventoryTrx");
		DB.executeUpdate(sql.toString(), get_TrxName());
	}
	
}
