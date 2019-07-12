package id.tcs.process;

import java.util.logging.Level;

import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.Msg;
import id.tcs.model.MInquiry;

public class TCSInquiryCreateRevision extends SvrProcess {

	int p_C_Inquiry_ID = 0;
	
	@Override
	protected void prepare() {
		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++) {
			String name = para[i].getParameterName();
			if (para[i].getParameter() == null) {
				;
//			} else if (para[i].getParameterName().equalsIgnoreCase("C_Inquiry_ID")) {
	//			p_C_Inquiry_ID = para[i].getParameterAsInt();
			} else {
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
			}
		}
	}

	@Override
	protected String doIt() throws Exception {

		p_C_Inquiry_ID = getRecord_ID();
		
		MInquiry from = new MInquiry(getCtx(), p_C_Inquiry_ID, get_TrxName());
		MInquiry revision = MInquiry.copyFrom(from, get_TrxName());
		revision.set_ValueOfColumn("DocStatus", "CL");
		revision.saveEx();
		
		String message = Msg.parseTranslation(getCtx(), "@GeneratedRevisionINquiry@ " + revision.getDocumentNo());
		addBufferLog(0, null, null, message, revision.get_Table_ID(), revision.getC_Inquiry_ID());
		
		return "";

	}

}
