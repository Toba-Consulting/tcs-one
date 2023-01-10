package id.tcs.process;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.logging.Level;

import org.adempiere.exceptions.AdempiereException;
import org.compiere.model.MBPartner;
import org.compiere.model.MDocType;
import org.compiere.model.MLocator;
import org.compiere.model.MMovementLine;
import org.compiere.model.MOrderLine;
import org.compiere.model.MOrg;
import org.compiere.model.MOrgInfo;
import org.compiere.model.MStorageOnHand;
import org.compiere.model.MUOMConversion;
import org.compiere.model.MWarehouse;
import org.compiere.model.Query;
import org.compiere.process.DocAction;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;
import org.compiere.util.Util;
import org.eevolution.model.MDDOrder;
import org.eevolution.model.MDDOrderLine;

import org.compiere.model.TCS_MMovement;

public class TCS_InterWHCreateOutbound extends SvrProcess {

	private int p_C_DocType_ID = 0;
	private int p_DD_Order_ID = 0;
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
		
		if (internalOrder.get_ValueAsInt("M_MovementTo_ID") > 0) {
			return "Error: Inbound Movement Has Been Created";
		}
		
		if (!internalOrder.isSOTrx())
			return "Error: Internal PO Document Cannot Generate Outbound Movement";
		
		if (p_C_DocType_ID <= 0) {
			return "Error: No Document Type Selected for Inbound Movement";
		} else {
			MDocType docType = new MDocType(getCtx(), p_C_DocType_ID, get_TrxName());
			if (!docType.getDocBaseType().equals(MDocType.DOCBASETYPE_MaterialMovement)) {
				return "Error: Selected Document Type Is Not Material Movement";
			}
		}

		//	Org Must be linked to BPartner
		MOrg org = MOrg.get(getCtx(), internalOrder.getAD_Org_ID());
		int counterC_BPartner_ID = org.getLinkedC_BPartner_ID(get_TrxName()); 
		if (counterC_BPartner_ID == 0)
			return null;
		//	Business Partner needs to be linked to Org
		MBPartner bp = new MBPartner (getCtx(), internalOrder.getC_BPartner_ID(), get_TrxName());
		int counterAD_Org_ID = bp.getAD_OrgBP_ID(); 
		if (counterAD_Org_ID == 0)
			return null;
		
		MOrgInfo counterOrgInfo = MOrgInfo.get(getCtx(), counterAD_Org_ID, get_TrxName());

		MWarehouse whFrom = new MWarehouse(getCtx(), internalOrder.getM_Warehouse_ID(), get_TrxName());

		MWarehouse whTransit = new MWarehouse(getCtx(), counterOrgInfo.get_ValueAsInt("Transit_Warehouse_ID"), get_TrxName());

		MLocator locatorTransit = whTransit.getDefaultLocator();

		//int M_WarehouseTo_ID = interWH.get_ValueAsInt("M_WarehouseTo_ID");
		//int M_WarehouseTo_ID = whTransit.getM_Warehouse_ID();
		
		//Create outbound movement
		TCS_MMovement outbound = new TCS_MMovement(getCtx(), 0, get_TrxName());		
		outbound.setAD_Org_ID(internalOrder.getAD_Org_ID());
		if (internalOrder.getAD_OrgTrx_ID() > 0) {
			outbound.setAD_OrgTrx_ID(internalOrder.getAD_OrgTrx_ID());
		}
		outbound.setMovementDate(p_MovementDate);
		outbound.setC_Project_ID(internalOrder.getC_Project_ID());
		outbound.setC_BPartner_ID(internalOrder.getC_BPartner_ID());
		outbound.setC_BPartner_Location_ID(internalOrder.getC_BPartner_Location_ID());
		outbound.setM_Shipper_ID(internalOrder.getM_Shipper_ID());
		outbound.setDocAction(DocAction.ACTION_Complete);
		outbound.setDocStatus(DocAction.STATUS_Drafted);
		outbound.setC_DocType_ID(p_C_DocType_ID);
		outbound.set_ValueOfColumn("M_Warehouse_ID", internalOrder.getM_Warehouse_ID());
		outbound.set_ValueOfColumn("M_WarehouseTo_ID", whTransit.getM_Warehouse_ID());
		outbound.set_ValueOfColumn("M_InternalPO_ID", internalOrder.get_ValueAsInt("Ref_InternalOrder_ID"));
		outbound.setDD_Order_ID(internalOrder.getDD_Order_ID());
		outbound.set_ValueOfColumn("IsOutbound", "Y");
		outbound.saveEx();
		
		//Create outbound movement lines
//		MDDOrderLine[] lines = internalOrder.getLines();
		final String whereClause = "qtyentered != qtydelivered AND DD_Order_ID=" + p_DD_Order_ID;
		List<MDDOrderLine> lines = new Query(getCtx(), MDDOrderLine.Table_Name, whereClause, get_TrxName())
				.list();


		for (MDDOrderLine line : lines) {
			String sql = "SELECt coalesce(sum(ml.movementqty),0) from m_movementline ml join m_movement mm on ml.m_movement_id = mm.m_movement_id "
					+ " where mm.docstatus = 'CO' and dd_orderline_id = " + line.getDD_OrderLine_ID();
			BigDecimal outboundQty = DB.getSQLValueBD(get_TrxName(), sql);
			BigDecimal MovementQty = MUOMConversion.convertProductFrom (getCtx(), line.getM_Product_ID(),
					line.getC_UOM_ID(), line.getQtyOrdered());
			if (MovementQty == null)
				MovementQty = line.getQtyOrdered();

			// Validate Negative
			MLocator locator = new MLocator (line.getCtx(), whFrom.getDefaultLocator().get_ID(), line.get_TrxName());
			Timestamp dateMPolicy = p_MovementDate;
//
			if (dateMPolicy != null)
				dateMPolicy = Util.removeTime(dateMPolicy);
//			//	Get Storage
			if(whFrom.get_ValueAsBoolean("IsDisallowNegativeInv")) {
				String whereMovementQty = "M_Product_ID = ? and DD_Order_ID = ?";
				BigDecimal sumMovementQty = new Query(Env.getCtx(), MDDOrderLine.Table_Name, whereMovementQty, line.get_TrxName())
						.setParameters(new Object[] {line.getM_Product_ID(), line.getDD_Order_ID()})
						.sum("QtyOrdered");
				
				BigDecimal newQty = sumMovementQty;
				
				String whereOnhand = "M_Product_ID = ? and m_locator_id = ? and datematerialpolicy <= ?";
				BigDecimal sumQtyOnHand = new Query(Env.getCtx(), MStorageOnHand.Table_Name, whereOnhand, line.get_TrxName())
						.setParameters(new Object[] {line.getM_Product_ID(), locator.getM_Locator_ID(), p_MovementDate})
						.sum("QtyOnHand");
				
				if(newQty.compareTo(sumQtyOnHand) > 0) {
					BigDecimal diff = newQty.subtract(sumQtyOnHand);
					throw new AdempiereException("Negative Inventory, Product: " + line.getM_Product().getName() + 
							" , Current Onhand: " + sumQtyOnHand + " , Internal PO Quantity: " + newQty +
							", Shortage of: " + diff);
				}
	
			}							
			
			if (outboundQty.compareTo(line.getQtyEntered())<0) {
				BigDecimal newMovementQty = line.getQtyEntered()
						.subtract(outboundQty);
			
				MMovementLine moveLine = new MMovementLine(outbound);
				moveLine.setLine(line.getLine());
				moveLine.setM_Product_ID(line.getM_Product_ID());
				moveLine.set_ValueOfColumn("QtyEntered", line.getQtyEntered());
				moveLine.setMovementQty(line.getQtyOrdered());
				moveLine.setM_Locator_ID(whFrom.getDefaultLocator().get_ID());
				moveLine.setM_LocatorTo_ID(locatorTransit.get_ID());
				moveLine.set_ValueOfColumn("C_UOM_ID", line.getC_UOM_ID());
				moveLine.set_ValueOfColumn("M_InternalPOLine_ID", line.get_Value("Ref_InternalOrderLine_ID"));
				moveLine.setDD_OrderLine_ID(line.getDD_OrderLine_ID());
				moveLine.saveEx();
			
			}else {
				throw new AdempiereException("There is no line to be processes.");
			}
		}
		
		//Complete movement
		if(p_Docstatus.equals("CO")) {
			outbound.processIt(DocAction.ACTION_Complete); 
		}
		outbound.saveEx();

		internalOrder.set_ValueOfColumn("M_MovementTo_ID", outbound.get_ID());
		internalOrder.saveEx();
		
		String message = Msg.parseTranslation(getCtx(), "@GeneratedOutbound@"+ outbound.getDocumentNo());
		addBufferLog(0, null, null, message, outbound.get_Table_ID(),outbound.getM_Movement_ID());

		return "Successfully Created Outbound Movement #" + outbound.getDocumentNo();
		
	}
	
}
