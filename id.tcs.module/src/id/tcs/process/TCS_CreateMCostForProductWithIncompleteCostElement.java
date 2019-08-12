package id.tcs.process;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.stream.Collectors;

import org.adempiere.exceptions.AdempiereException;
import org.compiere.model.MCost;
import org.compiere.model.MCostElement;
import org.compiere.model.MPeriod;
import org.compiere.model.MProduct;
import org.compiere.model.Query;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;

public class TCS_CreateMCostForProductWithIncompleteCostElement extends SvrProcess{

	/* Create M_Cost for product that don't have one of existing cost element (Cost Element Type : Material)
	 * Param : C_Period_ID, C_DocType_ID
	 * Get Product_IDs from M_Inventory with C_DocType_ID & MovementDate from parameter
	 */
	private int p_C_Period_ID=0;
	private int p_C_DocType_ID=0;
	
	private int p_M_CostType=0;
	private int p_C_AcctSchema_ID=0;
	@Override
	protected void prepare() {
		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++)
		{
			String name = para[i].getParameterName();
			if (para[i].getParameter() == null)
				;
			else if (para[i].getParameterName().equalsIgnoreCase(
					"C_Period_ID"))
				p_C_Period_ID = para[i].getParameterAsInt();
			else if (para[i].getParameterName().equalsIgnoreCase(
					"C_DocType_ID"))
				p_C_DocType_ID = para[i].getParameterAsInt();
			else if (para[i].getParameterName().equalsIgnoreCase(
					"M_CostType_ID"))
				p_M_CostType = para[i].getParameterAsInt();
			else if (para[i].getParameterName().equalsIgnoreCase(
					"C_AcctSchema_ID"))
				p_C_AcctSchema_ID = para[i].getParameterAsInt();
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
	}

	@Override
	protected String doIt() throws Exception {

		if (p_C_Period_ID==0) throw new AdempiereException("Period is mandatory");
		if (p_C_DocType_ID==0) throw new AdempiereException("Document Type is mandatory");
		if (p_M_CostType==0) throw new AdempiereException("Cost Type is mandatory");
		if (p_C_AcctSchema_ID==0) throw new AdempiereException("Accounting Schema is mandatory");
		
		
		int cnt=0;
		
		String sqlWhereCostElement = "AD_Client_ID="+getAD_Client_ID()+" AND CostElementType='M'";
		int [] costElement_IDs = new Query(getCtx(), MCostElement.Table_Name, sqlWhereCostElement, get_TrxName())
							.getIDs();	
		
		MPeriod period = new MPeriod(getCtx(), p_C_Period_ID, get_TrxName());
		String sqlWhereProductIDs = "M_Product_ID IN (SELECT M_Product_ID FROM M_Inventory mi "
								+ "JOIN M_InventoryLine mil ON mi.M_Inventory_ID=mil.M_Inventory_ID "
								+ "WHERE mi.C_DocType_ID="+p_C_DocType_ID+" AND "
								+ "mi.DocStatus IN ('CO','CL') AND "
								+ "mi.MovementDate BETWEEN '"+period.getStartDate()+"' AND '"+period.getEndDate()+"' "
								+ ")";
		
		int [] product_IDs = new Query(getCtx(), MProduct.Table_Name, sqlWhereProductIDs, get_TrxName())
						.getIDs();
		
		String sqlWhereProductMCostElement_IDs = "M_CostElement_ID IN (SELECT M_CostElement_ID FROM M_Cost WHERE M_Product_ID=?)";
		int [] productCostElement_IDs;
		for (int product_ID : product_IDs) {
			MProduct product = new MProduct(getCtx(), product_ID, get_TrxName());
			if (!product.isStocked()) {
				continue;
			}
			
			productCostElement_IDs = new Query(getCtx(), MCostElement.Table_Name, sqlWhereProductMCostElement_IDs, get_TrxName())
									.setParameters(product_ID)
									.getIDs();
			List <Integer> listProductCostElement = Arrays.stream(productCostElement_IDs).boxed().collect( Collectors.toList() );

			for (Integer costElement_ID : costElement_IDs) {
				if (listProductCostElement.contains(costElement_ID)) {
					continue;
				}
				else{
					MCost newCost = new MCost(getCtx(), 0, get_TrxName());
					newCost.setM_Product_ID(product_ID);
					newCost.setM_CostElement_ID(costElement_ID);
					newCost.setAD_Org_ID(0);
					newCost.setM_CostType_ID(p_M_CostType);
					newCost.setC_AcctSchema_ID(p_C_AcctSchema_ID);
					newCost.saveEx();
					
					cnt++;
				}
			}
		}
		return cnt+" M_Cost Created";
	}
}
