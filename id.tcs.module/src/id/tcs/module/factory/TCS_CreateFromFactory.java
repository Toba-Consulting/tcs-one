package id.tcs.module.factory;

import id.tcs.webui.apps.form.TCS_WCreateFromOrder;

import org.compiere.grid.ICreateFrom;
import org.compiere.grid.ICreateFromFactory;
import org.compiere.model.GridTab;
import org.compiere.model.MDocType;
import org.compiere.model.MOrder;
import org.compiere.util.Env;

public class TCS_CreateFromFactory implements ICreateFromFactory{

	@Override
	public ICreateFrom create(GridTab mTab) {

		String tableName = mTab.getTableName();
		if (tableName.equals(MOrder.Table_Name))
		{
			MOrder order = new MOrder(Env.getCtx(),(Integer)mTab.getValue("C_Order_ID"),null);
			int docType_ID = order.getC_DocType_ID()==0 ? order.getC_DocTypeTarget_ID() : order.getC_DocType_ID() ;
			MDocType docType = new MDocType(Env.getCtx(), docType_ID, null);
			if (!order.isSOTrx() && docType.getDocBaseType().equals("POO")) {
				return new TCS_WCreateFromOrder(mTab);
				
			}
		}
		return null;
	}
}
