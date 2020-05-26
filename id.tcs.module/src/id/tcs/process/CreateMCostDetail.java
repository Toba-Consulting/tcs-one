package id.tcs.process;

import java.sql.Timestamp;
import java.util.logging.Level;

import org.adempiere.exceptions.AdempiereException;
import org.compiere.model.MAcctSchema;
import org.compiere.model.MCostDetail;
import org.compiere.model.MInventoryLine;
import org.compiere.model.MPeriod;
import org.compiere.model.Query;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.Env;
import org.compiere.util.TimeUtil;

/**
 * 
 * @author AhmadIqbal
 * @param C_Period_ID
 * @param C_AcctSchema_ID
 * 
 */
public class CreateMCostDetail extends SvrProcess{
	int p_C_Period_ID = 0;
	int p_C_AcctSchema_ID = 0;
	
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
					"C_AcctSchema_ID"))
				p_C_AcctSchema_ID = para[i].getParameterAsInt();
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
		
	}

	@Override
	protected String doIt() throws Exception {
		if(p_C_Period_ID == 0)
			throw new AdempiereException("Period is mandatory");
		
		if(p_C_AcctSchema_ID == 0)
			throw new AdempiereException("AcctSchema is mandatory");
		
		
		MAcctSchema as = new MAcctSchema(getCtx(), p_C_AcctSchema_ID, get_TrxName());
		MPeriod period = new MPeriod(getCtx(), p_C_Period_ID, get_TrxName());
		Timestamp endDate = TimeUtil.addDays(period.getEndDate(), 1);
		String whereClause = "docstatus in ('CO','CL') "
				+ "and i.AD_Client_ID = ?"
				+ "and M_InventoryLine.m_inventoryline_id not in (select m_inventoryline_id from m_costdetail where m_inventoryline_id is not null) "
				+ "and i.MovementDate >= ? "
				+ "and i.MovementDate < ?";
		int[] id = new Query(getCtx(), MInventoryLine.Table_Name, whereClause, get_TrxName())
								.addJoinClause("join m_inventory i on M_InventoryLine.m_inventory_id = i.m_inventory_id")
								.setParameters(new Object[]{getAD_Client_ID(),period.getStartDate(), endDate})
								.getIDs();
		for (int i = 0; i < id.length; i++) {
			MInventoryLine line = new MInventoryLine(getCtx(), id[i], get_TrxName());
			MCostDetail.createInventory(as, line.getAD_Org_ID(), line.getM_Product_ID(), line.getM_AttributeSetInstance_ID(), line.getM_InventoryLine_ID(), 0, Env.ZERO, line.getQtyInternalUse().negate(), line.getDescription(), get_TrxName());
		}
		
		return "The process is done";
	}

}
