package org.compiere.process;

import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;

import org.compiere.model.MProduct;
import org.compiere.model.Query;


public class HWH_BOMExplosion extends SvrProcess{

	private int p_AD_Client_ID = 0;


	protected void prepare() {
		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++)
		{
			String name = para[i].getParameterName();
			if (para[i].getParameter() == null)
				;
			else if (name.equals("AD_Client_ID"))
				p_AD_Client_ID = para[i].getParameterAsInt();

			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
	}

	protected String doIt() throws Exception {

		String whereClause = "isbom='Y' and exists (select 1 from m_product_bom where m_product_id=m_product.m_product_id) " + //product as FG 
				"and not exists (select 1 from m_product_bom where m_productbom_id=m_product_id) " + //product as component
				"and ad_client_id=1000000 and isactive='Y'";

		List<MProduct> finishedGoods = new Query(getCtx(), MProduct.Table_Name, whereClause, get_TrxName())
				.list();

		HashMap<MProduct, Integer> productExplosionLevel = new HashMap<MProduct, Integer>();

		for (MProduct product:finishedGoods) {
			if (!productExplosionLevel.containsKey(product)) {
				productExplosionLevel.put(product, 0);
			} else 
				continue;

			List<MProduct> subAssy1 = getSubAssy(product.get_ID());

			if (subAssy1.isEmpty())
				continue;

			for (MProduct assy1:subAssy1) {
				if (!productExplosionLevel.containsKey(assy1)) {
					productExplosionLevel.put(assy1, 0);
				}

				Integer productLevel = productExplosionLevel.get(product);

				if (productLevel < 1)
					productExplosionLevel.put(product, 1);

				List<MProduct> subAssy2 = getSubAssy(assy1.get_ID());

				if (!subAssy2.isEmpty())
					continue;

				for (MProduct assy2:subAssy2) {
					if (!productExplosionLevel.containsKey(assy2)) {
						productExplosionLevel.put(assy2, 0);
					}

					Integer productLevel2 = productExplosionLevel.get(product);

					if (productLevel2 < 2)
						productExplosionLevel.put(product, 2);

					Integer assy1Level = productExplosionLevel.get(assy1);
					if (assy1Level < 1)
						productExplosionLevel.put(assy1, 1);

					List<MProduct> subAssy3 = getSubAssy(assy2.get_ID());

					if (!subAssy3.isEmpty())
						continue;

					for (MProduct assy3:subAssy3) {
						if (!productExplosionLevel.containsKey(assy3)) {
							productExplosionLevel.put(assy3, 0);
						}

						Integer productLevel3 = productExplosionLevel.get(product);

						if (productLevel3 < 3)
							productExplosionLevel.put(product, 3);

						Integer assy1Level3 = productExplosionLevel.get(assy1);
						if (assy1Level3 < 2)
							productExplosionLevel.put(assy1, 2);

						Integer assy2Level3 = productExplosionLevel.get(assy2);
						if (assy2Level3 < 1)
							productExplosionLevel.put(assy2, 1);

					}

				}

			}

		}





		return "";

	}


	private List<MProduct> getSubAssy(int product_ID) {
		String assyWhereClause = " AD_Client_ID=1000000 AND IsBOM='Y' AND M_Product_BOM.M_Product_ID=? ";
		List<MProduct> subAssy = new Query(getCtx(), MProduct.Table_Name, assyWhereClause, get_TrxName())
				.addJoinClause("JOIN M_Product_BOM ON M_Product.M_Product_ID=M_Product_BOM.M_ProductBOM_ID ")
				.setParameters(product_ID)
				.setOnlyActiveRecords(true)
				.list();

		return subAssy;
	}
}
