package id.tcs.process;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.logging.Level;

import org.adempiere.exceptions.AdempiereException;
import org.compiere.model.MOrder;
import org.compiere.model.MOrderLine;
import org.compiere.model.Query;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.Env;
import org.eevolution.model.MDDOrder;
import org.eevolution.model.MDDOrderLine;

public class TCS_COrderCreateDDOrder extends SvrProcess{

	//Create DD_Order with lines from C_Order
	private int p_C_Order_ID = 0;
	private int p_C_DocType_ID = 0;
	private int p_M_WarehouseFrom_ID = 0;
	private Timestamp p_DateOrdered = null;
	
	@Override
	protected void prepare() {
		
		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++)
		{
			String name = para[i].getParameterName();
			if (para[i].getParameter() == null)
				;
			else if (name.equals("C_Order_ID"))
				p_C_Order_ID = para[i].getParameterAsInt();
			else if (name.equals("C_DocType_ID"))
				p_C_DocType_ID = para[i].getParameterAsInt();
			else if (name.equals("M_Warehouse_ID"))
				p_M_WarehouseFrom_ID = para[i].getParameterAsInt();
			else if (name.equals("DateOrdered"))
				p_DateOrdered = para[i].getParameterAsTimestamp();
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
	}

	@Override
	protected String doIt() throws Exception {
		
		if (p_C_Order_ID == 0) 
			throw new AdempiereException("Order is mandatory");
		if (p_C_DocType_ID == 0) 
			throw new AdempiereException("Document Type is mandatory");
		if (p_M_WarehouseFrom_ID == 0) 
			throw new AdempiereException("Warehouse is mandatory");
		if (p_DateOrdered == null) 
			throw new AdempiereException("DateOrdered is mandatory");
		
		MOrder order = new MOrder(getCtx(), p_C_Order_ID, get_TrxName());
		MDDOrder inter = new MDDOrder(getCtx(), 0, get_TrxName());
		
		inter.setAD_Org_ID(order.getAD_Org_ID());
		inter.setC_DocType_ID(p_C_DocType_ID);
		inter.setDateOrdered(p_DateOrdered);
		inter.setC_Order_ID(p_C_Order_ID);
		inter.setM_Warehouse_ID(p_M_WarehouseFrom_ID);
		inter.setIsInDispute(false);
		inter.setIsInTransit(false);
		inter.saveEx();
		
		MOrderLine [] oLines = order.getLines();
		for (MOrderLine oLine : oLines) {
			MDDOrderLine ddLine = new MDDOrderLine(inter);
			ddLine.setM_Product_ID(oLine.getM_Product_ID());
			ddLine.setC_UOM_ID(oLine.getC_UOM_ID());
			ddLine.setIsInvoiced(false);
			ddLine.set_ValueOfColumn("C_OrderLine_ID", oLine.getC_OrderLine_ID());

			//Use Remaining Qty C_Order not yet used to create DD_ORder
			String sqlSumUsedQty = "C_OrderLine_ID="+oLine.getC_OrderLine_ID()+" AND dd.DocStatus IN ('CO','CL')";
			BigDecimal UsedQty = new Query(getCtx(), MDDOrderLine.Table_Name, sqlSumUsedQty, get_TrxName())
									.addJoinClause("JOIN DD_Order dd on dd.DD_Order_ID=DD_OrderLine.DD_Order_ID ")
									.sum("QtyEntered");
			BigDecimal remainingQty = oLine.getQtyEntered().subtract(UsedQty);
			if (remainingQty.compareTo(Env.ZERO)<=0) {
				continue;
			}
			ddLine.setQtyEntered(remainingQty);
			ddLine.setQtyOrdered(remainingQty);
			ddLine.saveEx();
		}
		return "Created DD_Order : '"+inter.getDocumentNo()+"'";
	}

}
