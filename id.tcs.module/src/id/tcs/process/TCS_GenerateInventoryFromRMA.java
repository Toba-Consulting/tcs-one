package id.tcs.process;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.util.List;
import java.util.logging.Level;

import org.compiere.model.MDocType;
import org.compiere.model.MProduct;
import org.compiere.model.MRMA;
import org.compiere.model.MRMALine;
import org.compiere.model.MUOMConversion;
import org.compiere.model.Query;
import org.compiere.model.TCS_MInventory;
import org.compiere.model.TCS_MInventoryLine;
import org.compiere.model.X_M_RMALine;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.Env;
import org.compiere.util.Msg;

public class TCS_GenerateInventoryFromRMA extends SvrProcess {
	
	int p_M_RMA_ID = 0;
	int p_C_Charge_ID = 0;
	Timestamp p_MovementDate = null;
	int p_C_DocType_ID = 0;
	int p_M_Warehouse_ID = 0;
	int p_M_Locator_ID = 0;
	
	@Override
	protected void prepare() {
		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++)
		{
			String name = para[i].getParameterName();
			if (para[i].getParameter() == null)
				;
			else if (name.equals("C_Charge_ID"))
				p_C_Charge_ID = para[i].getParameterAsInt();
			else if (name.equals("MovementDate"))
				p_MovementDate = para[i].getParameterAsTimestamp();
			else if (name.equals("C_DocType_ID"))
				p_C_DocType_ID = para[i].getParameterAsInt();
			else if (name.equals("M_Warehouse_ID"))
				p_M_Warehouse_ID = para[i].getParameterAsInt();
			else if (name.equals("M_Locator_ID"))
				p_M_Locator_ID = para[i].getParameterAsInt();

			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}

		p_M_RMA_ID = getRecord_ID();
	}

	@Override
	protected String doIt() throws Exception {
		MRMA rma = new MRMA(getCtx(), p_M_RMA_ID, get_TrxName());
		
		TCS_MInventory inventory = new TCS_MInventory(getCtx(), 0, get_TrxName());
		inventory.setM_Warehouse_ID(p_M_Warehouse_ID);
		inventory.setC_DocType_ID(p_C_DocType_ID);
		inventory.setMovementDate(p_MovementDate);
		inventory.setAD_Org_ID(rma.getAD_Org_ID());
		inventory.set_ValueOfColumn("AD_Client_ID", rma.getAD_Client_ID());
		inventory.set_ValueOfColumn("M_RMA_ID", rma.get_ID());
		inventory.saveEx();
		
		String whereClause = " M_RMA_ID = ?";
		List<MRMALine> list = new Query(rma.getCtx(), X_M_RMALine.Table_Name, whereClause , rma.get_TrxName())
					.setParameters(rma.get_ID())
					.list();

		for(MRMALine line : list) {
			TCS_MInventoryLine inventoryLine = new TCS_MInventoryLine(getCtx(), 0, get_TrxName());
			inventoryLine.setM_Inventory_ID(inventory.getM_Inventory_ID());
			inventoryLine.setM_Locator_ID(p_M_Locator_ID);
			inventoryLine.setM_Product_ID(line.getM_Product_ID());
			inventoryLine.setC_Charge_ID(p_C_Charge_ID);
			inventoryLine.setAD_Org_ID(line.getAD_Org_ID());
			inventoryLine.set_ValueOfColumn("QtyEntered", line.getQty());
			inventoryLine.set_ValueOfColumn("AD_Client_ID", line.getAD_Client_ID());
			inventoryLine.set_ValueOfColumn("C_UOM_ID", line.getC_UOM_ID());
			
			BigDecimal newQty = Env.ZERO;
			BigDecimal QtyEntered = Env.ZERO;
			int M_Product_ID = line.getM_Product_ID();
			
			int C_UOM_To_ID = line.getM_Product().getC_UOM_ID();
			
			QtyEntered = line.getQty();
			
			int C_DocType_ID = p_C_DocType_ID;
			boolean isInternalUse = false;
			boolean isMiscReceipt = false;
			MDocType docType = MDocType.get(Env.getCtx(), C_DocType_ID);
			
			if (docType.getDocSubTypeInv().equals(MDocType.DOCSUBTYPEINV_InternalUseInventory)) {
				isInternalUse = true;
			}
			else if (docType.getDocSubTypeInv().equals("MR")) {
				isMiscReceipt = true;
			}
			
			MProduct product = MProduct.get(Env.getCtx(), M_Product_ID);
			
			int precision = product.getUOMPrecision();
			BigDecimal QtyEntered1 = QtyEntered.setScale(precision, RoundingMode.HALF_UP);
			
			newQty = MUOMConversion.convertProductFrom (Env.getCtx(), M_Product_ID,
				C_UOM_To_ID, QtyEntered);
			
			if (newQty == null)
				newQty = QtyEntered;
			
			if(isInternalUse) {
				inventoryLine.setQtyInternalUse(newQty);
				inventoryLine.set_ValueOfColumn("qtymiscreceipt", newQty.negate());
			} else if(isMiscReceipt) {
				inventoryLine.set_ValueOfColumn("qtymiscreceipt", newQty);
				inventoryLine.setQtyInternalUse(newQty.negate());
			}
			
			
			inventoryLine.saveEx();

		}
		
		String message = Msg.parseTranslation(getCtx(), "@GeneratedInventory@"+ inventory.getDocumentNo());
		addBufferLog(0, null, null, message, inventory.get_Table_ID(),inventory.getM_Inventory_ID());

		
		return "";

	}

}