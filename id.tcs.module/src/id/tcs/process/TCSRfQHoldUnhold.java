package id.tcs.process;

import java.sql.Timestamp;
import java.util.logging.Level;

import org.compiere.model.MRfQ;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;

/*
 * @albert
 */

public class TCSRfQHoldUnhold extends SvrProcess {
	
	int p_C_RfQ_ID = 0;
		@Override
		protected void prepare() {
			ProcessInfoParameter[] para = getParameter();
			for (int i = 0; i < para.length; i++) {
				String name = para[i].getParameterName();
				if (para[i].getParameter() == null) {
					;
				} else {
					log.log(Level.SEVERE, "Unknown Parameter: " + name);
				}
			}
		
	}

	@Override
	protected String doIt() throws Exception {
		
		p_C_RfQ_ID = getRecord_ID();
		
		if(p_C_RfQ_ID<=0){
			return "Error";
		}
		
		MRfQ rfq = new MRfQ(getCtx(), p_C_RfQ_ID, get_TrxName());
		
		if(rfq.get_ValueAsBoolean("IsHold") == false){
			rfq.set_ValueOfColumnReturningBoolean("IsHold", true);
			rfq.set_CustomColumn("DateHoldA", new Timestamp(System.currentTimeMillis()));
			rfq.saveEx();
		
		}
		else{
			rfq.set_CustomColumnReturningBoolean("IsHold", false);
			rfq.set_CustomColumn("DateUnholdA", new Timestamp(System.currentTimeMillis()));
			rfq.saveEx();
		}
		
		return "";
	}


	
	
	
	
	
	
	
	
	
}
