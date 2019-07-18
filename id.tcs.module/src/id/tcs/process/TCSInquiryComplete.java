package id.tcs.process;

import java.util.logging.Level;

import org.compiere.model.MInOutLine;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.Msg;
import id.tcs.model.MInquiry;
import id.tcs.model.MInquiryLine;

public class TCSInquiryComplete extends SvrProcess {

	int p_C_Inquiry_ID = 0;
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
		
		p_C_Inquiry_ID = getRecord_ID();
		
		if (p_C_Inquiry_ID <=0)
			return "Error";
		
		MInquiry inq = new MInquiry(getCtx(), p_C_Inquiry_ID, get_TrxName());
		
		if (inq.get_ValueAsString("DocStatus").equalsIgnoreCase("DR")) {
			inq.setProcessed(true);
			inq.set_ValueOfColumn("DocStatus", "CO");
			
			MInquiryLine[] lines = inq.getLines();
			
			for (MInquiryLine line: lines) {
				line.setProcessed(true);
				line.saveEx();
			}
			inq.saveEx();
			
			String message = Msg.parseTranslation(getCtx(), "@InquiryComplete@ " + inq.getDocumentNo());
			addBufferLog(0, null, null, message, inq.get_Table_ID(), inq.getC_Inquiry_ID());
		
			
		} else {
			return "Only Drafted Inquiry can be completed";
		}
		
		return null;
	}

}
