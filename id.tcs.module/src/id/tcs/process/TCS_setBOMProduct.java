package id.tcs.process;

import java.util.List;
import java.util.ArrayList;

import org.compiere.model.MProduct;
import org.compiere.model.MProductBOM;
import org.compiere.model.Query;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;

public class TCS_setBOMProduct extends SvrProcess{

	@Override
	protected void prepare() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected String doIt() throws Exception {
		// TODO Auto-generated method stub
		String where = "value ilike 'IR-F%'";
		
		List<MProduct> lists = new Query(getCtx(), MProduct.Table_Name, where, get_TrxName()).list();
		
		for(MProduct product : lists) {
			product.setIsBOM(true);
			product.saveEx();
			
			String bomValue = product.getValue().replace("IR-F", "IR-RW");
				
			String sql = "select m_product_id from m_product where value ilike ?";
			
			int M_ProductBOM_Id = DB.getSQLValue(get_TrxName(), sql, bomValue);
			
			MProductBOM prodBOM = new MProductBOM(getCtx(), 0, get_TrxName());
			prodBOM.setM_Product_ID(product.getM_Product_ID());
			prodBOM.setM_ProductBOM_ID(M_ProductBOM_Id);
			prodBOM.setLine(10);
			prodBOM.setBOMQty(Env.ONE);
			prodBOM.setBOMType("P");
			prodBOM.saveEx();
			
		}
		
		return "Success";
	}

}
