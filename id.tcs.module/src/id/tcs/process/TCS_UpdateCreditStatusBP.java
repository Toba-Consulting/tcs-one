package id.tcs.process;

import java.math.BigDecimal;
import java.util.logging.Level;

import org.compiere.model.MBPartner;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.Env;

public class TCS_UpdateCreditStatusBP extends SvrProcess {

	private String SOCreditStatus = "";
	
	private BigDecimal SOCreditLimit = Env.ZERO;

	@Override
	protected void prepare() {
		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++) {
			String name = para[i].getParameterName();
			if (para[i].getParameter() == null)
				;
			else if (name.equals("SOCreditStatus"))
				SOCreditStatus = (String)para[i].getParameter();
			else if (name.equals("SO_CreditLimit"))
				SOCreditLimit = (BigDecimal)para[i].getParameter();
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
	}

	@Override
	protected String doIt() throws Exception {
		if (!(getRecord_ID() > 0))
			return "No Business Partner Selected";
		
		int C_BPartner_ID = getRecord_ID();
		
		MBPartner bp = new MBPartner(getCtx(), C_BPartner_ID, get_TrxName());
		
		if(SOCreditStatus!=null)
			bp.setSOCreditStatus(SOCreditStatus);
		
		if(SOCreditLimit.compareTo(Env.ZERO)>0)
			bp.setSO_CreditLimit(SOCreditLimit);
		
		if(!bp.save())
			return "Failed Update Status BP - " + bp.getName();
		
		return "SO Credit Status Updated for " + bp.getName() + 
				" - New Credit Status = " + SOCreditStatus + ", New Credit Limit = " + SOCreditLimit;
	} // doIt

}
