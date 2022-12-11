package id.tcs.webui.apps.form;

import id.tcs.model.X_M_MatchPR;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Vector;
import java.util.logging.Level;

import org.compiere.apps.IStatusBar;
import org.compiere.grid.CreateFrom;
import org.compiere.minigrid.IMiniTable;
import org.compiere.model.GridTab;
import org.compiere.model.MOrder;
import org.compiere.model.MProductPricing;
import org.compiere.model.MUOM;
import org.compiere.model.MUOMConversion;
import org.compiere.model.MOrderLine;
import org.compiere.model.MRequisitionLine;
import org.compiere.process.DocAction;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.KeyNamePair;
import org.compiere.util.Msg;

/**
 *  Create Order Lines From Requisition
 * @author wilson
 */

public class TCS_CreateFromOrder extends CreateFrom {



	public TCS_CreateFromOrder(GridTab mTab)
	{
		super(mTab);
		if (log.isLoggable(Level.INFO)) log.info(mTab.toString());
	}
	
	@Override
	public boolean dynInit() throws Exception 
	{
		log.config("");
		setTitle(Msg.getElement(Env.getCtx(), "C_Order_ID", false) + " .. " + Msg.translate(Env.getCtx(), "CreateFrom"));
		return true;
	}
	
	protected Vector<Vector<Object>> getRequisitionData(int M_Requisition_ID, int M_Product_ID, Timestamp dateRequired, int C_Project_ID, int C_Charge_ID, int salesRepID)
	{
	//	int C_Order_ID = Env.getContextAsInt(Env.getCtx(), getGridTab().getWindowNo(), "C_Order_ID");
		
		Vector<Vector<Object>> data = new Vector<Vector<Object>>();
		
		/**
         * 1 M_RequisitionLine_ID
         * 2 Line
         * 3 Product Name
         * 4 Qty Entered
         */
        StringBuilder sqlStmt = new StringBuilder();
        
        sqlStmt.append("SELECT rl.M_RequisitionLine_ID, mr.DocumentNo||' - '||rl.Line, "); //1..2
        sqlStmt.append("CASE WHEN rl.M_Product_ID IS NOT NULL THEN (SELECT p.Value||' - '||p.Name FROM M_Product p WHERE p.M_Product_ID = rl.M_Product_ID) END as ProductName, "); 
        sqlStmt.append("COALESCE(rl.Qty,0) as Qty, mr.DateRequired, "); //4..5
        sqlStmt.append("CASE WHEN mr.C_Project_ID IS NOT NULL THEN (SELECT pj.Value||' - '||pj.Name FROM C_Project pj WHERE pj.C_Project_ID = mr.C_Project_ID) END as Project, ");  
        sqlStmt.append("CASE WHEN rl.C_UOM_ID IS NOT NULL THEN (SELECT u.Name FROM C_UOM u WHERE u.C_UOM_ID = rl.C_UOM_ID) END AS uomName, ");
        sqlStmt.append("COALESCE(rl.QtyOrdered,0), COALESCE(rl.C_UOM_ID, 0) AS C_UOM_ID, "); //8 ..9
        sqlStmt.append("CASE WHEN rl.C_Charge_ID IS NOT NULL THEN (SELECT c.Name FROM C_Charge c WHERE c.C_Charge_ID = rl.C_Charge_ID) END as ChargeName ,rl.M_Product_ID "); 
        sqlStmt.append("FROM M_RequisitionLine rl ");
        sqlStmt.append("INNER JOIN M_Requisition mr ON mr.M_Requisition_ID=rl.M_Requisition_ID ");
        sqlStmt.append("WHERE rl.AD_Client_ID=? ");
        
        //@win added filter by org
        int AD_Org_ID = Env.getContextAsInt(Env.getCtx(), getGridTab().getWindowNo(), "AD_Org_ID");
        sqlStmt.append("AND mr.AD_Org_ID=? ");
        //@win end added filter by org
        
        if (M_Requisition_ID > 0) {
        	sqlStmt.append("AND rl.M_Requisition_ID=? ");
        }
        
        if (M_Product_ID > 0) {
        	sqlStmt.append("AND rl.M_Product_ID=? ");
        }
        
        if (dateRequired != null) {
        	sqlStmt.append("AND mr.DateRequired=? ");
        }
        if (C_Project_ID > 0) {
        	sqlStmt.append("AND mr.C_Project_ID=? ");
        } else {
        	sqlStmt.append("AND mr.C_Project_ID IS NULL ");
        }
        if (C_Charge_ID > 0) {
        	sqlStmt.append("AND rl.C_Charge_ID=? ");
        }
        
        /*
        if (salesRepID> 0) {
        	sqlStmt.append("AND mr.SalesRep_ID=? ");
        }*/
        
        //@Stephan, unprocessedpofrompr not available
        //sqlStmt.append("AND (rl.MovementQty-unprocessedpofrompr(rl.M_RequisitionLine_ID)) > 0 AND mr.DocStatus IN (?,?) ");
        
        try
        {
        	int count = 1;
        	PreparedStatement pstmt = DB.prepareStatement(sqlStmt.toString(), null);
            pstmt.setInt(count, Env.getAD_Client_ID(Env.getCtx()));
            
            //@win add filter by org, begin
            count++;
            pstmt.setInt(count, AD_Org_ID);
            //@win add filter by org, end
            
            if (M_Requisition_ID > 0) {
            	count++;
            	pstmt.setInt(count, M_Requisition_ID);
            }
            
            if (M_Product_ID > 0) {
            	count++;
            	pstmt.setInt(count, M_Product_ID);
            }
  
            if (dateRequired != null) {
            	count++;
            	pstmt.setTimestamp(count, dateRequired);
            }
            if (C_Project_ID > 0) {
            	count++;
            	pstmt.setInt(count, C_Project_ID);
            }
            if (C_Charge_ID > 0) {
            	count++;
            	pstmt.setInt(count, C_Charge_ID);
            }
            
            /*
            if (salesRepID > 0) {
            	count++;
            	pstmt.setInt(count, salesRepID);
            }*/
            
            //pstmt.setString(++count, DocAction.STATUS_Completed);
            //pstmt.setString(++count, DocAction.STATUS_Closed);
            
            ResultSet rs = pstmt.executeQuery();
            while (rs.next())
            {
            	
            	StringBuilder sqls = new StringBuilder();
            	sqls.append("SELECT coalesce(sum(a.qtyentered),0) as qty FROM C_OrderLine a "
						+ "LEFT JOIN C_Order b ON a.C_Order_ID=b.C_Order_ID "
						+ "LEFT JOIN M_MatchPR c on b.C_Order_ID=c.C_Order_ID "
						+ "where a.M_Product_ID ="+rs.getInt(11)+" AND c.M_RequisitionLine_ID="+rs.getInt(1));
            	
            	BigDecimal qtys = DB.getSQLValueBD(null, sqls.toString());
                Vector<Object> line = new Vector<Object>(8);
                line.add(Boolean.FALSE);           //  0-Selection
                
                KeyNamePair lineKNPair = new KeyNamePair(rs.getInt(1), rs.getString(2)); // 1-Line
                line.add(lineKNPair);
                line.add(rs.getString(3)); //2-Product
                line.add(rs.getString(10)); //3-Charge                
                BigDecimal qty = rs.getBigDecimal(4); 
                BigDecimal qtyOrdered = rs.getBigDecimal(8); 
                MRequisitionLine reqLine = new MRequisitionLine(Env.getCtx(), rs.getInt(1), null);
                int C_UOM_To_ID = rs.getInt(9);
                if (C_UOM_To_ID > 0 && reqLine.getM_Product_ID() > 0 
                		&& reqLine.getC_UOM_ID()!=reqLine.getM_Product().getC_UOM_ID()) {
        			qtyOrdered = MUOMConversion.convertProductTo (Env.getCtx(), reqLine.getM_Product_ID(), 
        					C_UOM_To_ID, qtyOrdered);
        			if (qtyOrdered == null)
        					qtyOrdered = Env.ZERO;
                }
                qty = qty.subtract(qtyOrdered);
                line.add(qty.subtract(qtys));  // 4 - Qty
                Timestamp p_dateRequired = rs.getTimestamp(5);
                line.add(p_dateRequired); //5 - DateRequired
                line.add(rs.getString(6)); //6 - Project
                line.add(rs.getString(7)); // 7 - UOM
                if(qty.subtract(qtys).compareTo(Env.ZERO)>0)
                data.add(line);
            }
            rs.close();
            pstmt.close();
        }
        catch (SQLException e)
        {
            log.log(Level.SEVERE, sqlStmt.toString(), e);
        }
        
        return data;
	}

	
	protected void configureMiniTable (IMiniTable miniTable)
	{
		miniTable.setColumnClass(0, Boolean.class, false);      //  0-Selection
		miniTable.setColumnClass(1, String.class, true);        //  1-Line
		miniTable.setColumnClass(2, String.class, true);        //  2-Product 
		miniTable.setColumnClass(3, String.class, true);        //  3-Charge
		miniTable.setColumnClass(4, BigDecimal.class, false);   //  4-Qty
		miniTable.setColumnClass(5, Timestamp.class, true);   //  5-DateRequired
		miniTable.setColumnClass(6, String.class, true);   //  6-Project
		miniTable.setColumnClass(7, String.class, true);        //  7-UOM
		 
        //  Table UI
		miniTable.autoSize();
	}

	@Override
	public boolean save(IMiniTable miniTable, String trxName) 
	{
		log.config("");
		int C_Order_ID = Env.getContextAsInt(Env.getCtx(), getGridTab().getWindowNo(), "C_Order_ID");
        
        for (int i = 0; i < miniTable.getRowCount(); i++)
        {
            if (((Boolean)miniTable.getValueAt(i, 0)).booleanValue())
            {
                BigDecimal qty = (BigDecimal)miniTable.getValueAt(i, 4); // 3 - Qty
                KeyNamePair pp = (KeyNamePair)miniTable.getValueAt(i, 1);   //  1-Line
                
                int requisitionLineID = pp.getKey();
                MRequisitionLine reqLine = new MRequisitionLine(Env.getCtx(), requisitionLineID, null);
                BigDecimal qtyOrdered = qty;
    			
                if (reqLine.getM_Product_ID() > 0) {
	                int C_UOM_To_ID = reqLine.getC_UOM_ID();
	                BigDecimal qty1 = qty.setScale(MUOM.getPrecision(Env.getCtx(), C_UOM_To_ID), RoundingMode.HALF_UP);
	    			if (qty.compareTo(qty1) != 0)
	    			{
	    				log.fine("Corrected Qty Scale UOM=" + C_UOM_To_ID 
	    					+ "; Qty=" + qty + "->" + qty1);  
	    				qty = qty1;
	    			}
	    			
	                if (reqLine.getC_UOM_ID()!=reqLine.getM_Product().getC_UOM_ID()) {
	        			qtyOrdered = MUOMConversion.convertProductFrom (Env.getCtx(), reqLine.getM_Product_ID(), 
	        					C_UOM_To_ID, reqLine.getQty());
	        			if (qtyOrdered == null)
	        					qtyOrdered = reqLine.getQty();
	                }
                }
                //@Stephan, validation requestedby:sisi
                String sql = null;
                ResultSet rs = null;
                PreparedStatement pstmt = null;
                BigDecimal totalQtyMatch = new BigDecimal(0);
                BigDecimal totalQtyReq = new BigDecimal(0);
                
                //get sum(qty) from table M_RequisitionLine
                try{
                	sql = "SELECT SUM(Qty) FROM M_RequisitionLine "
                			+ "WHERE M_Requisition_ID=? AND M_Product_ID=?";
                	pstmt = DB.prepareStatement(sql, trxName);
                	pstmt.setInt(1, reqLine.getM_Requisition_ID());
                	pstmt.setInt(2, reqLine.getM_Product_ID());
                	rs = pstmt.executeQuery();
                	if(rs.next()){
                		totalQtyReq = rs.getBigDecimal(1);
                	}
                }catch(Exception e){
                	System.out.println("Error : "+e);
                }finally{
                	DB.close(rs, pstmt);
        			rs = null;
        			pstmt = null;
                }
                
                //get sum(qty) from table C_OrderLine
                try{
                	sql = "SELECT SUM(QtyEntered) FROM C_OrderLine "
                			+ "WHERE C_Order_ID IN("
                			+ "SELECT C_Order_ID from M_MatchPR "
                			+ "WHERE M_Requisition_ID=?) "
                			+ "AND M_Product_ID=?";
                	pstmt = DB.prepareStatement(sql, trxName);
                	pstmt.setInt(1, reqLine.getM_Requisition_ID());
                	pstmt.setInt(2, reqLine.getM_Product_ID());
                	rs = pstmt.executeQuery();
                	if(rs.next()){
                		totalQtyMatch = rs.getBigDecimal(1);
                	}
                }catch(Exception e){
                	System.out.println("Error : "+e);
                }
                finally{
                	DB.close(rs, pstmt);
        			rs = null;
        			pstmt = null;
                }
                
                //compare qty from orderline and qty from qty requisitionline
                if(totalQtyMatch==null){
                	totalQtyMatch = Env.ZERO;
                	totalQtyReq = Env.ZERO;
                }
                
                else if(totalQtyMatch.add(qty).compareTo(totalQtyReq)==1){
                	//return or break here
                	return false;
                }
                
                //getprice from pricelist
                /*
                MOrder order = new MOrder(Env.getCtx(), C_Order_ID, null);
                Timestamp today = new Timestamp(System.currentTimeMillis());
                String sqlString = "SELECT plv.M_PriceList_Version_ID "
        				+ "FROM M_PriceList_Version plv "
        				+ "WHERE plv.M_PriceList_ID=? "						//	1
        				+ " AND plv.ValidFrom <= ? "
        				+ "ORDER BY plv.ValidFrom DESC";
        		int	M_PriceList_Version_ID = DB.getSQLValueEx(null, sqlString,order.getM_PriceList_ID(), today);
                MProductPricing pricing = new MProductPricing (reqLine.getM_Product_ID(), 
						reqLine.getC_BPartner_ID(), reqLine.getQty(), true);
				pricing.setM_PriceList_ID(order.getM_PriceList_ID());
				pricing.setM_PriceList_Version_ID(M_PriceList_Version_ID);
				pricing.setPriceDate(today);
				*/
                
                MOrder order = new MOrder(Env.getCtx(), C_Order_ID, null);
                
        		int C_BPartner_ID = order.getC_BPartner_ID();
        		
        		BigDecimal Qty = reqLine.getQty();
        		boolean IsSOTrx = order.isSOTrx();
        		MProductPricing pricing = new MProductPricing (reqLine.getM_Product_ID(), C_BPartner_ID, Qty, IsSOTrx, null);
        		int M_PriceList_ID = order.getM_PriceList_ID();
        		pricing.setM_PriceList_ID(M_PriceList_ID);
        		
        		String sqlString = "SELECT plv.M_PriceList_Version_ID "
        				+ "FROM M_PriceList_Version plv "
        				+ "WHERE plv.M_PriceList_ID=? "						//	1
        				+ " AND plv.ValidFrom <= ? "
        				+ "ORDER BY plv.ValidFrom DESC";
        			//	Use newest price list - may not be future

        		int M_PriceList_Version_ID = DB.getSQLValueEx(null, sqlString,order.getM_PriceList_ID(), order.getDateOrdered());
        			
        		pricing.setM_PriceList_Version_ID(M_PriceList_Version_ID);
        		pricing.setPriceDate(order.getDateOrdered());
        		
                //create line
                //MOrderLine orderLine = new MOrderLine(reqLine);
        		MOrderLine orderLine = new MOrderLine(reqLine.getCtx(), 0, reqLine.get_TrxName());
        		orderLine.setAD_Org_ID(reqLine.getAD_Org_ID());
        		orderLine.set_ValueNoCheck("M_RequisitionLine_ID",reqLine.getM_RequisitionLine_ID());
        		orderLine.setQtyDelivered(Env.ZERO);
        		orderLine.setQtyInvoiced(Env.ZERO);
        		orderLine.setM_Product_ID(reqLine.getM_Product_ID());
        		orderLine.setC_Charge_ID(reqLine.getC_Charge_ID());
        		orderLine.setC_UOM_ID(reqLine.getC_UOM_ID());
                orderLine.setC_Order_ID(C_Order_ID);
                orderLine.setQtyEntered(qty);
                orderLine.setQtyOrdered(qtyOrdered);
                orderLine.setLine(reqLine.getLine());
                orderLine.setPrice(pricing.getPriceStd());
                orderLine.setPriceEntered(pricing.getPriceStd().subtract(pricing.getPriceStd().multiply(orderLine.getDiscount())));
                orderLine.setPriceActual(pricing.getPriceStd());
                orderLine.setPriceList(pricing.getPriceList());
                orderLine.setPriceLimit(pricing.getPriceLimit());
                orderLine.saveEx();
                
                //end of code
            }
        }
        return true;
	}
	
	protected Vector<String> getOISColumnNames()
	{
		//  Header Info
        Vector<String> columnNames = new Vector<String>(7);
        columnNames.add(Msg.getMsg(Env.getCtx(), "Select"));
        columnNames.add("Line");
        columnNames.add(Msg.translate(Env.getCtx(), "M_Product_ID"));
        columnNames.add(Msg.translate(Env.getCtx(), "C_Charge_ID"));
        columnNames.add(Msg.translate(Env.getCtx(), "Quantity"));
        columnNames.add(Msg.translate(Env.getCtx(), "DateRequired"));
        columnNames.add(Msg.translate(Env.getCtx(), "C_Project_ID"));
        columnNames.add(Msg.translate(Env.getCtx(), "C_UOM_ID"));
	    return columnNames;
	}
	
	/**
	 *  Load PBartner dependent Order/Invoice/Shipment Field.
	 *  @param C_BPartner_ID BPartner
	 *  @param forInvoice for invoice
	 */
	protected ArrayList<KeyNamePair> loadRequisitionData (int C_BPartner_ID, int C_Project_ID, int salesRepID)
	{
		ArrayList<KeyNamePair> list = new ArrayList<KeyNamePair>();

		int AD_Client_ID = Env.getContextAsInt(Env.getCtx(), getGridTab().getWindowNo(), "AD_Client_ID");
	     
		//	Display
		StringBuilder display = new StringBuilder("r.DocumentNo");
			//.append(DB.TO_CHAR("r.TotaLines", DisplayType.Amount, Env.getAD_Language(Env.getCtx())));
		
		StringBuilder sql = new StringBuilder("SELECT DISTINCT r.M_Requisition_ID,").append(display)
			.append(" FROM M_Requisition r ")
			.append(" WHERE EXISTS (SELECT 1 FROM M_RequisitionLine l WHERE r.M_Requisition_ID=l.M_Requisition_ID")
			.append(" AND r.AD_Client_ID=? AND r.DocStatus=?)")
			.append(" AND r.AD_Org_ID=? ");
		//	.append(" AND (l.QtyRequisite - unprocessedpofrompr(m_requisitionline_ID)) > 0) ");
		//@Stephan
		if (C_Project_ID > 0) {
			sql.append(" AND r.C_Project_ID=? ");
		} else {
			sql.append(" AND r.C_Project_ID IS NULL ");
		}
//        if (salesRepID > 0) {
//        	sql.append("AND r.AD_User_ID=? ");
//        }
        
		sql = sql.append(" ORDER BY r.DocumentNo");
		//
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			int count = 0;
			pstmt = DB.prepareStatement(sql.toString(), null);
			pstmt.setInt(++count, AD_Client_ID);
			pstmt.setString(++count, DocAction.STATUS_Completed);
			int AD_Org_ID = Env.getContextAsInt(Env.getCtx(), getGridTab().getWindowNo(), "AD_Org_ID");
			pstmt.setInt(++count, AD_Org_ID);
			if (C_Project_ID > 0) {
				pstmt.setInt(++count, C_Project_ID);
			}
//			if (salesRepID > 0) {
//	        	pstmt.setInt(++count, salesRepID);
//	        }
	        
			rs = pstmt.executeQuery();
			while (rs.next())
			{
				list.add(new KeyNamePair(rs.getInt(1), rs.getString(2)));
			}
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, sql.toString(), e);
		}
		finally
		{
			DB.close(rs, pstmt);
			rs = null; pstmt = null;
		}

		return list;
	}   //  initBPartnerOIS
	
	
	@Override
	public Object getWindow() {
		return null;
	}

	@Override
	public void info(IMiniTable miniTable, IStatusBar statusBar) {
		
	}
}
