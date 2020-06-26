package org.compiere.process;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.logging.Level;

import org.compiere.model.MAcctSchema;
import org.compiere.model.MCost;
import org.compiere.model.MProduct;
import org.compiere.util.DB;
import org.compiere.util.Env;

public class HWHCreateMCostForAvgPO extends SvrProcess {

	private int acctSchema_ID=0;
	@Override
	protected void prepare() {

		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++)
		{
			String name = para[i].getParameterName();
			if (para[i].getParameter() == null)
				;
			else if (name.equals(MCost.COLUMNNAME_C_AcctSchema_ID))
				acctSchema_ID = para[i].getParameterAsInt();

			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
		
	}

	@Override
	protected String doIt() throws Exception {

		String sql = "select m_product_id from m_product mp "
				+ "where not exists (select 1 from m_cost mc where mc.m_product_id=mp.m_product_id and mc.m_costelement_id=1000001)"
				+ "and mp.ad_client_id=1000000 and mp.isactive='Y'";
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int count = 0;
		MAcctSchema acctSchema = new MAcctSchema(getCtx(), acctSchema_ID, get_TrxName());
		try
		{
			pstmt = DB.prepareStatement (sql, null);

			rs = pstmt.executeQuery ();
			while (rs.next()) {
				MProduct mp = new MProduct(getCtx(), rs.getInt(1), get_TrxName());
				MCost mc = new MCost(mp, 0, acctSchema, 0, 1000001);
				mc.setIsCostFrozen(false);
				mc.setCurrentCostPriceLL(Env.ZERO);
				mc.setPercent(0);
				mc.save();
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
