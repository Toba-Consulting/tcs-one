package id.tcs.validator;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;

import org.adempiere.base.event.IEventTopics;
import org.adempiere.exceptions.AdempiereException;
import org.adempiere.exceptions.DBException;
import org.compiere.model.MOrderLine;
import org.compiere.model.MPriceList;
import org.compiere.model.MRequisitionLine;
import org.compiere.model.PO;
import org.compiere.model.Query;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;
import org.compiere.util.Trx;
import org.osgi.service.event.Event;
import org.zkoss.util.logging.Log;

import id.tcs.model.MMatchPR;
import id.tcs.model.TCS_MProductPO;
import id.tcs.model.X_M_MatchPR;
import id.tcs.model.X_M_MatchQuotation;

public class TCS_ProductPOValidator {
	public static String executeEvent(Event event, PO po) {
		String msg = "";
		TCS_MProductPO prodPO = (TCS_MProductPO) po;

		if (event.getTopic().equals(IEventTopics.PO_AFTER_CHANGE) ||
				event.getTopic().equals(IEventTopics.PO_AFTER_NEW)) {
			msg += validateCurrentVendor(prodPO);
		}


		return msg;
	}

	private static String validateCurrentVendor(TCS_MProductPO prodPO) {
		if (prodPO.isActive() && prodPO.isCurrentVendor())
		{
			int cnt = DB.getSQLValue(prodPO.get_TrxName(),
							"SELECT COUNT(*) FROM M_Product_PO WHERE IsActive='Y' AND IsCurrentVendor='Y' AND C_BPartner_ID!=? AND M_Product_ID=?",
							prodPO.getC_BPartner_ID(), prodPO.getM_Product_ID());
			if (cnt > 0) {
				throw new AdempiereException("SaveError: Cannot have more than 1 current vendor");
			}
		}
		return "";
	}


}
