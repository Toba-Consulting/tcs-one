package id.tcs.process;

import java.util.logging.Level;

import org.compiere.model.MRfQResponse;
import org.compiere.model.MRfQResponseLine;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.Env;

/*
 * @albert
 */

public class TaoResponseLineInputBP extends SvrProcess{

	int p_c_rfqresponse_id = 0;
	int C_BPartnerRelation_ID = 0;
	int C_BPartner_Location_ID = 0;
	
	@Override
		protected void prepare() {
			ProcessInfoParameter[] para = getParameter();
			for (int i = 0; i < para.length; i++) {
				String name = para[i].getParameterName();
				if (para[i].getParameter() == null) {
					;
				} 
				else if(para[i].getParameterName().equalsIgnoreCase("C_BPartnerRelation_ID")){
					C_BPartnerRelation_ID = para[i].getParameterAsInt();
				}
				else if(para[i].getParameterName().equalsIgnoreCase("C_BPartner_Location_ID")){
					C_BPartner_Location_ID = para[i].getParameterAsInt();
				}
				else {
					log.log(Level.SEVERE, "Unknown Parameter: " + name);
				}
			}
			p_c_rfqresponse_id = getRecord_ID();
	}

	@Override
	protected String doIt() throws Exception {
		
		
		MRfQResponse response = new MRfQResponse(Env.getCtx(), p_c_rfqresponse_id,get_TrxName());
		
		MRfQResponseLine[] responseLines = response.getLines();
		
		for(MRfQResponseLine responseline: responseLines){
			responseline.set_ValueOfColumn("C_BPartner_ID", C_BPartnerRelation_ID);
			responseline.set_ValueOfColumn("C_BPartner_Location_ID", C_BPartner_Location_ID);
			responseline.saveEx();
		}
		
		return "Complete";
	}
	

}
