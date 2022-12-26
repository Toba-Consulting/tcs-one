package id.tcs.process;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.logging.Level;

import org.adempiere.exceptions.AdempiereException;
import org.compiere.model.MDocType;
import org.compiere.model.MMovement;
import org.compiere.model.MMovementLine;
import org.compiere.model.MWarehouse;
import org.compiere.model.Query;
import org.compiere.process.DocAction;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Msg;
import org.eevolution.model.MDDOrder;

import org.compiere.model.TCS_MMovement;


public class TCS_InterWHCreateInbound extends SvrProcess {

	private int p_C_DocType_ID = 0;
	private int p_DD_Order_ID = 0;
	private int p_OutBound_ID = 0;
	private String p_Docstatus = "";
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
			else if (name.equals("DocStatus"))
				p_Docstatus = para[i].getParameterAsString();
			else if (name.equals("M_Movement_ID"))
				p_OutBound_ID = para[i].getParameterAsInt();
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
		MDDOrder internalPO = new MDDOrder(getCtx(), p_DD_Order_ID, get_TrxName());
		if (!internalPO.getDocStatus().equals(DocAction.ACTION_Complete)) {
			return "Error: Only Completed Inter-warehouse Document Can be Processed";
		}
		
		if (internalPO.get_ValueAsInt("M_MovementIn_ID") > 0) {
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
		
		MWarehouse whFrom = new MWarehouse(getCtx(), internalPO.getM_Warehouse_ID(), get_TrxName());
//		MWarehouse whTo = new MWarehouse(getCtx(), internalPO.get_ValueAsInt("M_WarehouseTo_ID"), get_TrxName());
//		locator nya isactive isdefault dari mwarhouse id internalpo
		String sqlLocator = "SELECT m_locator_id FROM M_Locator ml where M_Warehouse_ID = " + whFrom.get_ID() + " and isactive = 'Y' and isdefault ='Y'";
		int M_LocatorTo_ID = DB.getSQLValue(get_TrxName(), sqlLocator);
		
		MMovement outbound = new MMovement(getCtx(), p_OutBound_ID, get_TrxName());
		final String whereClause = "qtyentered != qtydelivered AND M_Movement_ID=" + p_OutBound_ID;
//		MMovementLine [] lines = outbound.getLines(true);
		List<MMovementLine> lines = new Query(getCtx(), MMovementLine.Table_Name, whereClause, get_TrxName())
									.list();
		
		//Create inbound movement
		TCS_MMovement inbound = new TCS_MMovement(getCtx(), 0, get_TrxName());
		inbound.setAD_Org_ID(internalPO.getAD_Org_ID());
		inbound.setMovementDate(p_MovementDate);
		inbound.setC_DocType_ID(p_C_DocType_ID);
		//inbound.setM_Warehouse_ID(orgInfo.getTransit_Warehouse_ID());
		
		//inbound.set_ValueOfColumn("M_Warehouse_ID", orgInfo.get_ValueAsInt("Transit_Warehouse_ID"));
		inbound.set_ValueOfColumn("M_Warehouse_ID",  whFrom.get_ID());
		//inbound.setM_WarehouseTo_ID(p_M_Warehouse_ID);
		inbound.set_ValueOfColumn("M_WarehouseTo_ID", whFrom.get_ID());
		
		inbound.setDocStatus(DocAction.STATUS_Drafted);
		inbound.setDocAction(DocAction.ACTION_Complete);
		inbound.setDD_Order_ID(internalPO.getDD_Order_ID());
		inbound.setC_Project_ID(internalPO.getC_Project_ID());
		inbound.setC_BPartner_ID(internalPO.getC_BPartner_ID());
		inbound.setC_BPartner_Location_ID(outbound.getC_BPartner_Location_ID());
		inbound.setM_Shipper_ID(outbound.getM_Shipper_ID());
		inbound.setAD_User_ID(outbound.getAD_User_ID());
		inbound.set_ValueOfColumn("IsInbound", "Y");
		inbound.set_ValueOfColumn("M_OutboundFrom_ID", outbound.getM_Movement_ID());
		inbound.saveEx();
		
		for (MMovementLine line : lines) {
			String sql = "SELECt coalesce(sum(qtyentered),0) from m_movementline ml join m_movement mm "
					+ "on ml.m_movement_id = mm.m_movement_id where docstatus ='CO' and M_OutboundLine_ID = " + line.getM_MovementLine_ID();
			BigDecimal inboundQty = DB.getSQLValueBD(get_TrxName(), sql);
			BigDecimal qtyEntered = (BigDecimal) line.get_Value("QtyEntered"); 
			if (inboundQty.compareTo(qtyEntered)<0) {
				BigDecimal newMovementQty = qtyEntered.subtract(inboundQty);
				MMovementLine moveLine = new MMovementLine(inbound);
				moveLine.setLine(line.getLine());
				moveLine.setAD_Org_ID(internalPO.getAD_Org_ID());
				moveLine.setM_Product_ID(line.getM_Product_ID());
				//moveLine.setQtyEntered(line.getQtyEntered());
				moveLine.set_ValueOfColumn("QtyEntered", line.get_Value("QtyEntered"));
				moveLine.setMovementQty(line.getMovementQty());
				moveLine.setM_Locator_ID(line.getM_LocatorTo_ID());
				moveLine.setM_LocatorTo_ID(M_LocatorTo_ID);
				//moveLine.setC_UOM_ID(line.getC_UOM_ID());
				moveLine.set_ValueOfColumn("C_UOM_ID", line.get_Value("C_UOM_ID"));
				moveLine.setDD_OrderLine_ID(line.get_ValueAsInt("M_InternalPOLine_ID"));
				moveLine.set_ValueOfColumn("M_OutboundLine_ID", line.getM_MovementLine_ID());
				moveLine.saveEx();				
			}else 
				throw new AdempiereException("There is no line to be processes.");
			
		}
		
		
		//Complete movement
		if(p_Docstatus.equals("CO")) {
			if(!inbound.processIt(DocAction.ACTION_Complete)){
				throw new AdempiereException("Failed to complete inbound");
			}
			
		}
		inbound.saveEx();
		
		/*Bug #2990 Create multiple Outbound and Inbound
		//Set Inbound Movement Link to Inter-warehouse
		//interWH.setM_MovementIn_ID(inbound.get_ID());
		interWH.set_ValueOfColumn("M_MovementIn_ID", inbound.get_ID());
		*/
		internalPO.saveEx();
		
		String message = Msg.parseTranslation(getCtx(), "@GeneratedInbound@"+ inbound.getDocumentNo());
		addBufferLog(0, null, null, message, inbound.get_Table_ID(),inbound.getM_Movement_ID());

		return "";
	}

}
