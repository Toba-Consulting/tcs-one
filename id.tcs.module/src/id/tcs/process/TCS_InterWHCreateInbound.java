package id.tcs.process;

import java.sql.Timestamp;
import java.util.logging.Level;

import org.adempiere.exceptions.AdempiereException;
import org.compiere.model.MDocType;
import org.compiere.model.MMovement;
import org.compiere.model.MMovementLine;
import org.compiere.model.MOrgInfo;
import org.compiere.model.MWarehouse;
import org.compiere.process.DocAction;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.Msg;
import org.eevolution.model.MDDOrder;

public class TCS_InterWHCreateInbound extends SvrProcess {

	private int p_C_DocType_ID = 0;
	private int p_DD_Order_ID = 0;
	private Timestamp p_MovementDate = null;
	
	protected void prepare() {
		
		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++) {
			String name = para[i].getParameterName();
			if (para[i].getParameter() == null)
				;
			else if (name.equals("C_DocType_ID"))
				p_C_DocType_ID = para[i].getParameterAsInt();
			else if (name.equals("MovementDate"))
				p_MovementDate = para[i].getParameterAsTimestamp();
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
		p_DD_Order_ID = getRecord_ID();
	}

	protected String doIt() throws Exception {
		
		//Validate DD_Order_ID
		if (p_DD_Order_ID <= 0) {
			return "Error: No Selected Inter-warehouse Document";
		}

		if (p_MovementDate == null) {
			return "Error: No Movement Date";
		}
		
		//Validate status of InterWarehouse Movement= CO
		MDDOrder internalOrder = new MDDOrder(getCtx(), p_DD_Order_ID, get_TrxName());
		if (!internalOrder.getDocStatus().equals(DocAction.ACTION_Complete)) {
			return "Error: Only Completed Inter-warehouse Document Can be Processed";
		}
		
		/*
		//Validate Transit Warehouse Is Set
		String sqlCheck = "SELECT AD_OrgInv_ID FROM AD_InventoryOrg WHERE AD_InventoryOrg_ID=?";
		int OrgTo = DB.getSQLValueEx(get_TrxName(), sqlCheck, new Object[]{interWH.get_ValueAsInt("AD_InventoryOrg_ID")});
		 
		MOrgInfo orgInfo = MOrgInfo.get(getCtx(), OrgTo, get_TrxName());

		//MOrgInfo orgInfo = MOrgInfo.get(getCtx(), interWH.getAD_Org_ID(), get_TrxName());
		if (orgInfo.get_ValueAsInt("Transit_Warehouse_ID") <= 0) {
			MOrg org = new MOrg(getCtx(), interWH.getAD_Org_ID(), get_TrxName());
			return "Missing Setup: Transit Warehouse for Org "+ org.getName();
		}
		*/
		
		//only allow running process for role with access to warehouse destination
		//@win commented
//		boolean match = new Query(getCtx(), X_AD_Role_WHAccess.Table_Name, "AD_Role_ID=? AND M_Warehouse_ID=?", get_TrxName())
//						.setParameters(Env.getContextAsInt(getCtx(), Env.AD_ROLE_ID), p_M_Warehouse_ID)
//						.setOnlyActiveRecords(true)
//						.match();
//		
//		if (!match) {
//			return "Error: User Role Does Not Access to Warehouse Destination";
//		}
		
		/*Bug #2990 Create multiple Outbound and Inbound
		//Check whether an existing inbound is already exists
		if (interWH.get_ValueAsInt("M_MovementTo_ID") <= 0) {
			return "Error: Must Create Outbound Movement Before Create Inbound";
		}
		*/
		
		if (internalOrder.get_ValueAsInt("M_MovementIn_ID") > 0) {
			return "Error: Inbound Movement Has Been Created";
		}
		
		//Validate DocType = Material Movement
		if (p_C_DocType_ID <= 0) {
			return "Error: No Document Type Selected for Inbound Movement";
		} else {
			MDocType docType = new MDocType(getCtx(), p_C_DocType_ID, get_TrxName());
			if (!docType.getDocBaseType().equals(MDocType.DOCBASETYPE_MaterialMovement)) {
				return "Error: Selected Document Type Is Not Material Movement";
			}
		}
		
		if (internalOrder.isSOTrx())
			return "Error: Internal SO Document Cannot Create Inbound";
		
		MOrgInfo orgInfo = MOrgInfo.get(getCtx(), internalOrder.getAD_Org_ID(), get_TrxName());
		
		MWarehouse whFrom = new MWarehouse(getCtx(), orgInfo.get_ValueAsInt("Transit_Warehouse_ID"), get_TrxName());

		MWarehouse whTo = new MWarehouse(getCtx(), internalOrder.getM_Warehouse_ID(), get_TrxName());
		
		//TODO: need correction
		MMovement outbound = new MMovement(getCtx(), internalOrder.get_ValueAsInt("M_MovementTo_ID"), get_TrxName());
		MMovementLine [] outboundLines = outbound.getLines(true);
		
		//Create inbound movement
		MMovement inbound = new MMovement(getCtx(), 0, get_TrxName());
		inbound.setAD_Org_ID(internalOrder.getAD_Org_ID());
		inbound.setMovementDate(p_MovementDate);
		inbound.setC_DocType_ID(p_C_DocType_ID);
		inbound.set_ValueOfColumn("M_Warehouse_ID", whFrom.get_ID());
		inbound.set_ValueOfColumn("M_WarehouseTo_ID", whTo.get_ID());
		inbound.setDocStatus(DocAction.STATUS_Drafted);
		inbound.setDocAction(DocAction.ACTION_Complete);
		inbound.setDD_Order_ID(internalOrder.getDD_Order_ID());
		inbound.setC_Project_ID(internalOrder.getC_Project_ID());
		inbound.setC_BPartner_ID(internalOrder.getC_BPartner_ID());
		inbound.setC_BPartner_Location_ID(internalOrder.getC_BPartner_Location_ID());
		inbound.setM_Shipper_ID(outbound.getM_Shipper_ID());
		inbound.setAD_User_ID(internalOrder.getAD_User_ID());
		inbound.set_ValueOfColumn("IsInbound", "Y");
		inbound.saveEx();
		
		for (MMovementLine line : outboundLines) {
			MMovementLine moveLine = new MMovementLine(inbound);
			moveLine.setLine(line.getLine());
			moveLine.setAD_Org_ID(internalOrder.getAD_Org_ID());
			moveLine.setM_Product_ID(line.getM_Product_ID());
			//moveLine.setQtyEntered(line.getQtyEntered());
			moveLine.set_ValueOfColumn("QtyEntered", line.get_Value("QtyEntered"));
			moveLine.setMovementQty(line.getMovementQty());
			moveLine.setM_Locator_ID(whFrom.getDefaultLocator().get_ID());
			moveLine.setM_LocatorTo_ID(whTo.getDefaultLocator().get_ID());
			//moveLine.setC_UOM_ID(line.getC_UOM_ID());
			moveLine.set_ValueOfColumn("C_UOM_ID", line.get_Value("C_UOM_ID"));
//			moveLine.setDD_OrderLine_ID(line.getDD_OrderLine_ID());
	//		moveLine.set_ValueOfColumn("M_OutBoundLineFrom_ID", line.getM_MovementLine_ID());
			moveLine.saveEx();
		}
		
		
		//Complete movement
		if(!inbound.processIt(DocAction.ACTION_Complete)){
			throw new AdempiereException("Failed to complete inbound");
		}
		inbound.saveEx();
		
		//Bug #2990 Create multiple Outbound and Inbound
		//Set Inbound Movement Link to Inter-warehouse
		internalOrder.set_ValueOfColumn("M_MovementIn_ID", inbound.get_ID());
		internalOrder.saveEx();
		
		String message = Msg.parseTranslation(getCtx(), "@GeneratedInbound@"+ inbound.getDocumentNo());
		addBufferLog(0, null, null, message, inbound.get_Table_ID(),inbound.getM_Movement_ID());

		return "";
	}

}
