package id.tcs.validator;

import org.adempiere.base.event.IEventTopics;
import org.compiere.model.MProduct;
import org.compiere.model.MUOM;
import org.compiere.model.PO;
import org.compiere.util.Env;
import org.eevolution.model.MPPProductBOMLine;
import org.osgi.service.event.Event;

public class TCS_PPProductBomLineValidator {
	public static String executeEvent(Event event, PO po) {
		String msg = "";
		MPPProductBOMLine product_BOMLine = (MPPProductBOMLine) po;
		if (event.getTopic().equals(IEventTopics.PO_AFTER_NEW)) {
			msg += updateUOM(product_BOMLine);
		}
		return msg;
	}

	private static String updateUOM(MPPProductBOMLine product_BOMLine) {
		MProduct product = new MProduct(Env.getCtx(), product_BOMLine.getM_Product_ID(), product_BOMLine.get_TrxName());
		
		product_BOMLine.setC_UOM_ID(product.getC_UOM_ID());
		product_BOMLine.saveEx();

		return "";
	}
}
