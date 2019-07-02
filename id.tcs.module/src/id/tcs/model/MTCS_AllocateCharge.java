package id.tcs.model;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.util.Properties;

import org.compiere.model.MPayment;
import org.compiere.model.Query;
import org.compiere.util.DB;

public class MTCS_AllocateCharge extends X_TCS_AllocateCharge{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8936876137233217635L;
	
	public MTCS_AllocateCharge(Properties ctx, int TCS_AllocateCharge_ID, String trxName) {
		super(ctx, TCS_AllocateCharge_ID, trxName);
	}

	public MTCS_AllocateCharge(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}
	
	@Override
	protected boolean afterSave (boolean newRecord, boolean success){

		if (success) {
			MPayment payment = new MPayment(getCtx(),getC_Payment_ID(),get_TrxName());

			if (newRecord && payment.getC_Invoice_ID() > 0)
				payment.setC_Invoice_ID(0);
			if (newRecord && payment.getC_Charge_ID() > 0)
				payment.setC_Charge_ID(0);
			if (newRecord && payment.getC_Order_ID() > 0)
				payment.setC_Order_ID(0);

			//@win: set payment amt as sum total amount on allocate charge
			if (newRecord || is_ValueChanged(COLUMNNAME_Amount)) {
				BigDecimal totalAmt = new Query(getCtx(), MTCS_AllocateCharge.Table_Name,"C_Payment_ID=?", get_TrxName())
				.setParameters(payment.get_ID())
				.setOnlyActiveRecords(true)
				.sum(COLUMNNAME_Amount);
				
				String sql = "SELECT COALESCE(SUM(Amount),0) FROM C_PaymentAllocate WHERE C_Payment_ID="+getC_Payment_ID();
				totalAmt=totalAmt.add(DB.getSQLValueBD(get_TrxName(), sql));
				
				payment.setPayAmt(totalAmt);
			}

			payment.saveEx();
		}
		return true;
	}
	
}
