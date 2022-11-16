package id.tcs.model;

import java.sql.ResultSet;
import java.util.Properties;

import org.compiere.process.DocAction;
import org.compiere.process.DocOptions;
import org.compiere.util.Env;
import org.eevolution.model.MDDOrder;
import org.eevolution.model.MDDOrderLine;

public class TCS_MDDOrder extends MDDOrder implements DocOptions{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public TCS_MDDOrder(Properties ctx, int DD_Order_ID, String trxName) {
		super(ctx, DD_Order_ID, trxName);
		// TODO Auto-generated constructor stub
	}

	public TCS_MDDOrder (Properties ctx, ResultSet rs, String trxName)
    {
      super (ctx, rs, trxName);
    }

	@Override
	public int customizeValidActions(String docStatus, Object processing,
			String orderType, String isSOTrx, int AD_Table_ID,
			String[] docAction, String[] options, int index) {

		for (int i = 0; i < options.length; i++) {
			options[i] = null;
		}

		index = 0;

		if (docStatus.equals(DocAction.STATUS_Drafted)) {
			options[index++] = DocAction.ACTION_Complete;
			options[index++] = DocAction.ACTION_Prepare;
			options[index++] = DocAction.ACTION_Void;
		} else if (docStatus.equals(DocAction.STATUS_InProgress)) {
			options[index++] = DocAction.ACTION_Complete;
			options[index++] = DocAction.ACTION_Void;
		} else if (docStatus.equals(DocAction.STATUS_Completed)) {
			options[index++] = DocAction.ACTION_Close;
			options[index++] = DocAction.ACTION_Void;
			options[index++] = DocAction.ACTION_ReActivate;
		} else if (docStatus.equals(DocAction.STATUS_Invalid)) {
			options[index++] = DocAction.ACTION_Complete;
			options[index++] = DocAction.ACTION_Void;
		}

		return index;
		
	}
	
	@Override
	public boolean reActivateIt() {
		
		MDDOrderLine [] lines = getLines();
		for (MDDOrderLine line : lines) {
			line.setQtyReserved(Env.ZERO);
			line.saveEx();
		}
		return super.reActivateIt();
	}
	
}
