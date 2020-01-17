package id.tcs.webui.apps.form;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Vector;
import java.util.logging.Level;

import org.compiere.apps.IStatusBar;
import org.compiere.grid.CreateFrom;
import org.compiere.minigrid.IMiniTable;
import org.compiere.model.GridTab;
import org.compiere.model.MOrderLine;
import org.compiere.model.MProduct;
import org.compiere.model.MRequisitionLine;
import org.compiere.model.MUOM;
import org.compiere.model.MUOMConversion;
import org.compiere.process.DocAction;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.KeyNamePair;
import org.compiere.util.Msg;
import org.eevolution.model.MDDOrder;
import org.eevolution.model.MDDOrderLine;

public class TCS_DDOrderCreateFromCOrder extends CreateFrom{

	public TCS_DDOrderCreateFromCOrder(GridTab mTab) {
		super(mTab);
		if (log.isLoggable(Level.INFO)) log.info(mTab.toString());
	}

	@Override
	public Object getWindow() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean dynInit() throws Exception {
		log.config("");
		setTitle(Msg.getElement(Env.getCtx(), "DD_Order_ID", false) + " .. " + Msg.translate(Env.getCtx(), "CreateFrom"));
		return true;
	}

	@Override
	public void info(IMiniTable miniTable, IStatusBar statusBar) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean save(IMiniTable miniTable, String trxName) {
		log.config("");
		int DD_Order_ID = Env.getContextAsInt(Env.getCtx(), getGridTab().getWindowNo(), "DD_Order_ID");
        int lineNo=DB.getSQLValue(trxName, "SELECT MAX(Line) FROM DD_OrderLine WHERE DD_Order_ID="+DD_Order_ID);

		for (int i = 0; i < miniTable.getRowCount(); i++)
        {
            if (((Boolean)miniTable.getValueAt(i, 0)).booleanValue())
            {
            	BigDecimal qtyEntered = (BigDecimal)miniTable.getValueAt(i, 5); // 3 - Qty
                KeyNamePair pp = (KeyNamePair)miniTable.getValueAt(i, 1);   //  1-Line
                
                int orderLineID = pp.getKey();                
                MOrderLine oLine = new MOrderLine(Env.getCtx(), pp.getKey(), null);
                MProduct product = oLine.getProduct();
                BigDecimal qtyOrdered = qtyEntered;
    			
                //Convert qtyOrdered if orderline.UOM != product.UOM
                if (oLine.getM_Product_ID() > 0) {
	                int C_UOM_From_ID = oLine.getC_UOM_ID();
	                BigDecimal qty1 = qtyEntered.setScale(MUOM.getPrecision(Env.getCtx(), C_UOM_From_ID), BigDecimal.ROUND_HALF_UP);

	                if (oLine.getC_UOM_ID()!=oLine.getM_Product().getC_UOM_ID()) {
	        			qtyOrdered = MUOMConversion.convertProductFrom(Env.getCtx(), oLine.getM_Product_ID(), C_UOM_From_ID, qtyEntered);
	        			if (qtyOrdered == null)
	        					qtyOrdered = oLine.getQtyEntered();
	                }

	                if (qtyEntered.compareTo(qty1) != 0)
	    			{
	    				log.fine("Corrected Qty Scale UOM=" + C_UOM_From_ID 
	    					+ "; Qty=" + qtyEntered + "->" + qty1);  
	    				qtyEntered = qty1;
	    			}
	    			
                }
                
                String sql = null;
                ResultSet rs = null;
                PreparedStatement pstmt = null;
                BigDecimal qtyUsed = new BigDecimal(0);
                
                //get sum(qty) from table M_RequisitionLine
                try{
                	sql = "SELECT COALESCE(SUM(QtyEntered),0) FROM DD_OrderLine "
                			+ "WHERE DD_OrderLine_ID=? AND M_Product_ID=?";
                	pstmt = DB.prepareStatement(sql, trxName);
                	pstmt.setInt(1, oLine.getC_OrderLine_ID());
                	pstmt.setInt(2, oLine.getM_Product_ID());
                	rs = pstmt.executeQuery();
                	if(rs.next()){
                		qtyUsed = rs.getBigDecimal(1);
                	}
                }catch(Exception e){
                	System.out.println("Error : "+e);
                }finally{
                	DB.close(rs, pstmt);
        			rs = null;
        			pstmt = null;
                }
                
                BigDecimal qtyAvaible = oLine.getQtyEntered().subtract(qtyUsed);
                
                if (qtyAvaible.compareTo(qtyOrdered)<0) {
					return false;
				}
                
                MDDOrder ddOrder = new MDDOrder(Env.getCtx(), DD_Order_ID, trxName);
                MDDOrderLine ddLine = new MDDOrderLine(ddOrder);
                
                lineNo+=10;
                ddLine.setLine(lineNo);
                ddLine.setM_Product_ID(oLine.getM_Product_ID());
    			ddLine.setC_UOM_ID(oLine.getC_UOM_ID());
    			ddLine.setIsInvoiced(false);
    			ddLine.set_ValueOfColumn("C_OrderLine_ID", oLine.getC_OrderLine_ID());
    			ddLine.setQtyEntered(qtyEntered);
    			ddLine.setQtyOrdered(qtyOrdered);
    			ddLine.setDescription(oLine.getDescription());    			
    			ddLine.saveEx();
            }
        }
		
		MDDOrder dd = new MDDOrder(Env.getCtx(), DD_Order_ID, trxName);
		KeyNamePair pp = (KeyNamePair)miniTable.getValueAt(1, 1);
		MOrderLine oLine = new MOrderLine(Env.getCtx(), pp.getKey(), trxName);
		dd.setC_Order_ID(oLine.getC_Order_ID());
		dd.saveEx(trxName);
		return false;
	}

	protected Vector<Vector<Object>> getOrderData(int C_Order_ID, int C_Project_ID)
	{
		Vector<Vector<Object>> data = new Vector<Vector<Object>>();
		/**
         * 1 M_RequisitionLine_ID
         * 2 Line
         * 3 Product Name
         * 4 Qty Entered
         */
		
		StringBuilder sqlStmt = new StringBuilder();
        
        sqlStmt.append("SELECT col.C_OrderLine_ID, co.DocumentNo||' - '||col.Line, "); //1..2
        sqlStmt.append("CASE WHEN col.M_Product_ID IS NOT NULL THEN (SELECT p.Value||' - '||p.Name FROM M_Product p WHERE p.M_Product_ID = col.M_Product_ID) END as ProductName, "); 
        sqlStmt.append("COALESCE(col.QtyEntered,0) as Qty, "); //4
        sqlStmt.append("CASE WHEN co.C_Project_ID IS NOT NULL THEN (SELECT pj.Value||' - '||pj.Name FROM C_Project pj WHERE pj.C_Project_ID = col.C_Project_ID) END as Project, "); //5 
        sqlStmt.append("CASE WHEN col.C_UOM_ID IS NOT NULL THEN (SELECT u.Name FROM C_UOM u WHERE u.C_UOM_ID = col.C_UOM_ID) END AS uomName, "); //6
        sqlStmt.append("COALESCE(col.QtyOrdered,0), COALESCE(col.C_UOM_ID, 0) AS C_UOM_ID, "); //7 ..8
        sqlStmt.append("CASE WHEN col.C_Charge_ID IS NOT NULL THEN (SELECT c.Name FROM C_Charge c WHERE c.C_Charge_ID = col.C_Charge_ID) END as ChargeName , "); //9
        sqlStmt.append("col.M_Product_ID ,");//10
        sqlStmt.append("vd.name as bpname ");//11
        sqlStmt.append("FROM C_OrderLine col ");
        sqlStmt.append("INNER JOIN C_Order co ON co.C_Order_ID=col.C_Order_ID ");
        sqlStmt.append("INNER JOIN C_BPartner bp ON co.C_BPartner_ID=bp.C_BPartner_ID ");
        sqlStmt.append("LEFT JOIN C_BPartner vd ON col.Vendor_BPartner_ID=vd.C_BPartner_ID ");
        sqlStmt.append("WHERE col.AD_Client_ID=? AND co.DocStatus IN (?) ");
        
//        if (C_Order_ID > 0) {
        	sqlStmt.append("AND col.C_Order_ID=? ");
//        }
        
        if (C_Project_ID > 0) {
        	sqlStmt.append("AND col.C_Project_ID=? ");
        }
        sqlStmt.append("ORDER BY col.C_Order_ID,col.Line");
        
        try
        {
        	int count = 1;
        	PreparedStatement pstmt = DB.prepareStatement(sqlStmt.toString(), null);
            pstmt.setInt(count, Env.getAD_Client_ID(Env.getCtx()));
            pstmt.setString(++count, DocAction.STATUS_Completed);
            
//            if (C_Order_ID > 0) {
            	count++;
            	pstmt.setInt(count, C_Order_ID);
//            }
            
            if (C_Project_ID > 0) {
            	count++;
            	pstmt.setInt(count, C_Project_ID);
            }
            
            ResultSet rs = pstmt.executeQuery();
            while (rs.next())
            {
            	
            	StringBuilder sqls = new StringBuilder();
            	sqls.append("SELECT COALESCE(sum(ddl.QtyEntered),0) as qty "
            			+ "FROM DD_OrderLine ddl "
						+ "JOIN DD_Order dd ON ddl.DD_Order_ID=dd.DD_Order_ID "
						+ "WHERE ddl.C_OrderLine_ID="+rs.getInt(1));
            	
            	BigDecimal qtys = DB.getSQLValueBD(null, sqls.toString());
                Vector<Object> line = new Vector<Object>(8);
                line.add(new Boolean(false));           //  0-Selection
                
                KeyNamePair lineKNPair = new KeyNamePair(rs.getInt(1), rs.getString(2)); // 1-Line
                line.add(lineKNPair);
                line.add(rs.getString(11)); // 5 - Vendor
                line.add(rs.getString(3)); //2-Product
                line.add(rs.getString(9)); //3-Charge                
                BigDecimal qty = rs.getBigDecimal(4); //4-QtyEntered
//                BigDecimal qtyOrdered = rs.getBigDecimal(7); //7-QtyOrdered
//                MOrderLine oLine = new MOrderLine(Env.getCtx(), rs.getInt(1), null);
//                int C_UOM_To_ID = rs.getInt(9);
//                if (C_UOM_To_ID > 0 && oLine.getM_Product_ID() > 0 
//                		&& oLine.getC_UOM_ID()!=oLine.getM_Product().getC_UOM_ID()) {
//        			qtyOrdered = MUOMConversion.convertProductTo (Env.getCtx(), oLine.getM_Product_ID(), 
//        					C_UOM_To_ID, qtyOrdered);
//        			if (qtyOrdered == null)
//        					qtyOrdered = Env.ZERO;
//                }
//                qty = qty.subtract(qtyOrdered);
                line.add(qty.subtract(qtys));  // 4 - Qty
                line.add(rs.getString(5)); //6 - Project
                line.add(rs.getString(6)); // 7 - UOM
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
		miniTable.setColumnClass(2, String.class, true);        //  5-Vendor
		miniTable.setColumnClass(3, String.class, true);        //  2-Product 
		miniTable.setColumnClass(4, String.class, true);        //  3-Charge
		miniTable.setColumnClass(5, BigDecimal.class, false);   //  4-Qty
		miniTable.setColumnClass(6, String.class, true);   		//  6-Project
		miniTable.setColumnClass(7, String.class, true);        //  7-UOM
		 
        //  Table UI
		miniTable.autoSize();
	}
	
	protected Vector<String> getOISColumnNames()
	{
		//  Header Info
        Vector<String> columnNames = new Vector<String>(7);
        columnNames.add(Msg.getMsg(Env.getCtx(), "Select"));
        columnNames.add("Line");
        columnNames.add(Msg.translate(Env.getCtx(), "Vendor"));
        columnNames.add(Msg.translate(Env.getCtx(), "M_Product_ID"));
        columnNames.add(Msg.translate(Env.getCtx(), "C_Charge_ID"));
        columnNames.add(Msg.translate(Env.getCtx(), "Quantity"));
        columnNames.add(Msg.translate(Env.getCtx(), "C_Project_ID"));
        columnNames.add(Msg.translate(Env.getCtx(), "C_UOM_ID"));
	    return columnNames;
	}
	
	
}
