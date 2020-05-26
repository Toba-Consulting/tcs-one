package id.tcs.process;

import java.util.List;

import org.compiere.model.MAllocationHdr;
import org.compiere.model.Query;
import org.compiere.process.DocAction;
import org.compiere.process.SvrProcess;

public class TCS_CompleteDraftedAllocationHdr extends SvrProcess{

	private int p_AD_Client_ID = 0;
	@Override
	protected void prepare() {
		p_AD_Client_ID = getAD_Client_ID();
	}

	@Override
	protected String doIt() throws Exception {
		
		String sqlWhere = "DocStatus='DR' AND AD_Client_ID="+p_AD_Client_ID;
		List<MAllocationHdr> allocs = new Query(getCtx(), MAllocationHdr.Table_Name, sqlWhere, get_TrxName())
								.list();
		int cnt = 0;
		for (MAllocationHdr alloc : allocs) {
			alloc.processIt(DocAction.ACTION_Complete);
			alloc.saveEx(get_TrxName());
			cnt++;
		}
		return cnt+" record processed";
	}

}
