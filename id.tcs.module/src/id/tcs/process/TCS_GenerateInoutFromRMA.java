package id.tcs.process;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.logging.Level;

import org.adempiere.exceptions.AdempiereException;
import org.compiere.model.MBPartnerLocation;
import org.compiere.model.MDocType;
import org.compiere.model.MInOut;
import org.compiere.model.MInOutLine;
import org.compiere.model.MProduct;
import org.compiere.model.MRMA;
import org.compiere.model.MRMALine;
import org.compiere.model.MUOMConversion;
import org.compiere.model.MWarehouse;
import org.compiere.model.Query;
import org.compiere.model.TCS_MInventory;
import org.compiere.model.TCS_MInventoryLine;
import org.compiere.model.TCS_MRMA;
import org.compiere.model.X_M_RMALine;
import org.compiere.process.DocAction;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;
import org.compiere.util.Trx;

public class TCS_GenerateInoutFromRMA extends SvrProcess {
	
	int p_M_RMA_ID = 0;
	
	@Override
	protected void prepare() {
		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++)
		{
			String name = para[i].getParameterName();
			if (para[i].getParameter() == null)
				;
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}

		p_M_RMA_ID = getRecord_ID();
	}

	@Override
	protected String doIt() throws Exception {
		TCS_MRMA rma = new TCS_MRMA(getCtx(), p_M_RMA_ID, get_TrxName());
		
		String msg ="";
		MInOut inout = new MInOut (Env.getCtx(), 0, rma.get_TrxName());
		String sqlWarehouse = "select m_warehouse_id from m_warehouse mw where ad_org_id =" + rma.getAD_Org_ID()+ " and isintransit ='N' and isrepair ='N'";
		int M_Warehouse_ID = DB.getSQLValue(rma.get_TrxName(), sqlWarehouse);
		
		MWarehouse wh = MWarehouse.get(Env.getCtx(), M_Warehouse_ID);
		int M_Locator_ID = wh.getDefaultLocator().getM_Locator_ID();
		
		String whereClause= "isactive = 'Y' and c_Bpartner_id = " + rma.getC_BPartner_ID();
		int bplocation_id = new Query(Env.getCtx(), MBPartnerLocation.Table_Name, whereClause, rma.get_TrxName()).firstId();
		
		
		inout.setM_RMA_ID(rma.getM_RMA_ID());
		inout.setC_DocType_ID(1000015);
		inout.setMovementDate((Timestamp) rma.get_Value("DateDoc"));
		inout.setDateAcct((Timestamp) rma.get_Value("DateDoc"));
		inout.setFreightCostRule("I");
		inout.setPriorityRule("5");
		inout.setMovementType(inout.MOVEMENTTYPE_CustomerReturns);
		inout.setIsSOTrx(true);
		inout.setC_BPartner_ID(rma.getC_BPartner_ID());
		inout.setC_BPartner_Location_ID(bplocation_id);
		inout.setM_Warehouse_ID(M_Warehouse_ID);
		inout.setAD_Org_ID(rma.getAD_Org_ID());
		inout.saveEx();
		
		
		if (!inout.save(rma.get_TrxName()))
		{
			msg = "Could not create Shipment";
			return null;
		}
		//
		MRMALine[] rLines = rma.getLines(true);
		for (int i = 0; i < rLines.length; i++)
		{
			MRMALine rLine = rLines[i];
			//
			MInOutLine ioLine = new MInOutLine(inout);
			//

			ioLine.setQty(rLine.getQty());
			ioLine.setM_RMALine_ID(rLine.getM_RMALine_ID());
			ioLine.setQtyEntered(rLine.getQty());
			ioLine.setM_Product_ID(rLine.getM_Product_ID());
			ioLine.setM_Locator_ID(M_Locator_ID);
			if (!ioLine.save(rma.get_TrxName()))
			{
				msg = "Could not create Shipment Line";
				return null;
			}
			Trx trx = Trx.get(rma.get_TrxName(), false);

			try {
				trx.commit(true);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		// added AdempiereException by zuhri
		if (!inout.processIt(DocAction.ACTION_Complete))
			throw new AdempiereException(Msg.getMsg(Env.getCtx(), "FailedProcessingDocument") + " - " + inout.getProcessMsg());
		// end added
		inout.saveEx(rma.get_TrxName());
		if (!inout.getDocStatus().equals("CO"))
		{
			msg = "@M_InOut_ID@: " + inout.getProcessMsg();
			return null;
		}


		return "Generated Inout with Document No : " + inout.getDocumentNo();

	}

}