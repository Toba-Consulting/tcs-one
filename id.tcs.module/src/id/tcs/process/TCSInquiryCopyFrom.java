package id.tcs.process;

import java.math.BigDecimal;
import java.util.logging.Level;

import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import id.tcs.model.MInquiry;

public class TCSInquiryCopyFrom extends SvrProcess {

	private int m_C_Inquiry_ID = 0;
	@Override
	protected void prepare() {
		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++)
		{
			String name = para[i].getParameterName();
			if (para[i].getParameter() == null)
				;
			else if (name.equals("C_Inquiry_ID"))
				m_C_Inquiry_ID = para[i].getParameterAsInt();
			else
				log.log(Level.SEVERE, "prepare - Unknown Parameter: " + name);
		}
	}	//	prepare

	/**
	 *  Perform process.
	 *  @return Message 
	 *  @throws Exception if not successful
	 */
	protected String doIt() throws Exception
	{
		int To_C_Invoice_ID = getRecord_ID();
		if (log.isLoggable(Level.INFO)) log.info("From C_Inquiry_ID=" + m_C_Inquiry_ID + " to " + To_C_Invoice_ID);
		if (To_C_Invoice_ID == 0)
			throw new IllegalArgumentException("Target C_Inquiry_ID == 0");
		if (m_C_Inquiry_ID == 0)
			throw new IllegalArgumentException("Source C_Inquiry_ID == 0");
		MInquiry from = new MInquiry (getCtx(), m_C_Inquiry_ID, get_TrxName());
		MInquiry to = new MInquiry (getCtx(), To_C_Invoice_ID, get_TrxName());
		//
		int no = to.copyLinesFrom(from);
		//
		return "@Copied@=" + no;
	}	//	doIt

}
