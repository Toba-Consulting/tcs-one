package id.tcs.validator;

import org.compiere.model.PO;
import org.compiere.util.DB;
import org.eevolution.model.MDDOrderLine;
import org.osgi.service.event.Event;


public class TCS_DDOrderLineValidator {
	public static String executeEvent(Event event, PO po) {
		String msg = "";
		MDDOrderLine ddorderline= (MDDOrderLine) po;

//		if (event.getTopic().equals(IEventTopics.PO_BEFORE_NEW)) {
//			msg += setLocator(ddorderline);
//
//		}
		return msg;
	}

	private static String setLocator(MDDOrderLine ddorderline) {
		String sql = "select m_locator_id from m_locator where isdefault='Y' and m_warehouse_id = ?";
		int locatorID = DB.getSQLValue(null, sql, ddorderline.getDD_Order().getM_Warehouse_ID());
		
		ddorderline.setM_Locator_ID(locatorID);
		ddorderline.saveEx();
		
		return "";
	}
}
