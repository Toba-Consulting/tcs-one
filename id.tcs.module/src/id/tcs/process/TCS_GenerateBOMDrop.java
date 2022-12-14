package id.tcs.process;

import java.math.BigDecimal;
import java.util.List;
import java.util.logging.Level;

import org.compiere.model.I_M_Product_BOM;
import org.compiere.model.MOrder;
import org.compiere.model.MOrderLine;
import org.compiere.model.MProduct;
import org.compiere.model.MProductBOM;
import org.compiere.model.Query;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;


public class TCS_GenerateBOMDrop extends SvrProcess {

	int p_C_Order_ID = 0;
	
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

		p_C_Order_ID = getRecord_ID();
	}

	@Override
	protected String doIt() throws Exception {
		MOrder order = new MOrder(getCtx(), p_C_Order_ID, get_TrxName());
		
		MOrderLine[] oLines = order.getLines();
		
		for(MOrderLine oLine : oLines) {
			if(oLine.getM_Product().isBOM()) {
				// Bom Product
				MProduct BOMProd = new MProduct(getCtx(), oLine.getM_Product_ID(), get_TrxName());
				
				String whereClause = "M_Product_ID = ?";
				List<MProductBOM> prodBoms = new Query(getCtx(), I_M_Product_BOM.Table_Name, whereClause, get_TrxName())
						.setParameters(new Object[] {BOMProd.getM_Product_ID()})
						.list();
				
				for (MProductBOM prodBom : prodBoms) {
					MOrderLine bomLine = new MOrderLine(getCtx(), 0, get_TrxName());
					// base product
					MProduct prod = new MProduct(getCtx(), prodBom.getM_ProductBOM_ID(), get_TrxName());
					BigDecimal newQty = prodBom.getBOMQty().multiply(oLine.getQtyEntered());
					
					String sqlTax = "Select c_tax_id from c_tax where istaxexempt = 'Y' and ad_client_id=1000000";
					int C_Tax_ID = DB.getSQLValue(get_TrxName(), sqlTax);
					
					bomLine.setC_Order_ID(p_C_Order_ID);
					bomLine.setM_Product_ID(prodBom.getM_ProductBOM_ID());
					bomLine.setC_UOM_ID(prod.getC_UOM_ID());
					bomLine.setQtyEntered(newQty);
					bomLine.setQtyOrdered(newQty);
					bomLine.setC_Tax_ID(C_Tax_ID);
					bomLine.setPriceEntered(Env.ZERO);
					bomLine.setPriceActual(Env.ZERO);
					bomLine.setPriceList(Env.ZERO);
					bomLine.setLineNetAmt(Env.ZERO);
					bomLine.set_ValueOfColumn("IsBomDrop", true);
					bomLine.set_ValueOfColumn("IsPrinted", false);
					bomLine.saveEx();
					
				}
	
			}
		}
		
		return "";

	}

}