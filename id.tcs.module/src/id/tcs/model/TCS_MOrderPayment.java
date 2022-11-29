package id.tcs.model;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;

import org.adempiere.exceptions.AdempiereException;
import org.compiere.model.MBPartner;
import org.compiere.model.MDocType;
import org.compiere.model.MDocTypeCounter;
import org.compiere.model.MMovementLine;
import org.compiere.model.MOrg;
import org.compiere.model.MOrgInfo;
import org.compiere.model.PO;
import org.compiere.model.Query;
import org.compiere.process.DocAction;
import org.compiere.process.DocOptions;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;
import org.eevolution.model.MDDOrder;
import org.eevolution.model.MDDOrderLine;

public class TCS_MOrderPayment extends X_C_OrderPayment{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public TCS_MOrderPayment(Properties ctx, int C_OrderPayment_ID, String trxName) {
		super(ctx, C_OrderPayment_ID, trxName);
		// TODO Auto-generated constructor stub
	}

	public TCS_MOrderPayment (Properties ctx, ResultSet rs, String trxName)
    {
      super (ctx, rs, trxName);
    }

	protected boolean beforeSave (boolean newRecord)
	{
		BigDecimal currentAmt = Env.ZERO;
		String sql = "SELECT COALESCE(SUM(amt),0) FROM C_OrderPayment WHERE C_Order_ID = ? and C_OrderPayment_ID != " + getC_OrderPayment_ID();
		BigDecimal sumAmt = DB.getSQLValueBD(get_TrxName(), sql, getC_Order_ID());
		currentAmt = sumAmt.add(getAmt());
		
		if(currentAmt.compareTo(getC_Order().getGrandTotal()) > 0)
			throw new AdempiereException("Payment Amount cannot be greater than Order Grand Total");
		
		return true;
	}	//	beforeSave

	
}
