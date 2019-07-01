package id.tcs.validator;

import org.adempiere.base.event.IEventTopics;
import org.compiere.model.MOrder;
import org.compiere.model.MOrderLine;
import org.compiere.model.PO;
import org.compiere.util.Env;
import org.osgi.service.event.Event;

public class TCS_OrderValidator {

	
	public static String executeEvent(Event event, PO po) {
		String msg = "";
		MOrder order = (MOrder) po;
		if (event.getTopic().equals(IEventTopics.DOC_AFTER_REACTIVATE)) {
			msg = unreserveQty(order);
		} 
		return msg;
	}
	
	public static String unreserveQty(MOrder order){
		MOrderLine [] lines = order.getLines();
		for (MOrderLine line : lines) {
			line.setQtyReserved(Env.ZERO);
			line.saveEx();
		}
		
		return "";
	}
}
