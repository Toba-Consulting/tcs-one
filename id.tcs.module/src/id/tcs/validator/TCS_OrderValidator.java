package id.tcs.validator;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;

import org.adempiere.base.event.IEventTopics;
import org.adempiere.exceptions.AdempiereException;
import org.compiere.model.MDocType;
import org.compiere.model.MInOutLine;
import org.compiere.model.MLocator;
import org.compiere.model.MMatchPO;
import org.compiere.model.MOrder;
import org.compiere.model.MOrderLine;
import org.compiere.model.MPayment;
import org.compiere.model.MProduct;
import org.compiere.model.MRequisition;
import org.compiere.model.MRequisitionLine;
import org.compiere.model.MStorageOnHand;
import org.compiere.model.MStorageReservation;
import org.compiere.model.MWarehouse;
import org.compiere.model.PO;
import org.compiere.model.Query;
import org.compiere.model.TCS_MOrder;
import org.compiere.process.DocAction;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Trx;
import org.osgi.service.event.Event;

import id.tcs.model.MMatchPR;
import id.tcs.model.X_M_MatchPR;
import id.tcs.model.X_M_MatchQuotation;

public class TCS_OrderValidator {


	public static String executeEvent(Event event, PO po) {
		String msg = "";
		TCS_MOrder order = (TCS_MOrder) po;
		if (event.getTopic().equals(IEventTopics.DOC_BEFORE_REACTIVATE)) {
			msg += checkMatchPO(order);
			//			msg += checkLinkedPayment(order);
			msg += checkActiveLinkedInOut(order);
		} 
		else if (event.getTopic().equals(IEventTopics.DOC_BEFORE_VOID)) {
			msg += checkMatchPO(order);
			//			msg += checkLinkedPayment(order);
			msg += checkActiveLinkedInOut(order);
			//		} else  if (event.getTopic().equals(IEventTopics.DOC_BEFORE_COMPLETE)){
			//			msg += generateRequisition(order);

		} else  if (event.getTopic().equals(IEventTopics.DOC_BEFORE_REVERSEACCRUAL) || 
				event.getTopic().equals(IEventTopics.DOC_BEFORE_REVERSECORRECT)){
			if (!order.isSOTrx())
				msg += removeRequisition(order);
		}
		else if (event.getTopic().equals(IEventTopics.DOC_BEFORE_COMPLETE)) {
			if (order.isSOTrx()) {
				msg += checkBOMDrop(order);
				msg += validateOnhand(order);
			}
		}
		else if (event.getTopic().equals(IEventTopics.DOC_AFTER_REACTIVATE)) {
			if (order.isSOTrx()) {
				msg += removePayment(order);
			}
			msg += unreserveQty(order);
		} 

		else if (event.getTopic().equals(IEventTopics.DOC_AFTER_VOID)) {
			if (order.isSOTrx()) {
				msg += removeMatchQuotation(order);
			} else {
				msg += removeMatchPR(order);
			}

		} 

		else if (event.getTopic().equals(IEventTopics.DOC_AFTER_CLOSE)) {
			if (!order.isSOTrx()) {
				//Update QtyOrdered Information on all related requisition line when closing order
				msg += updateRequisitionQtyOrdered(order);
			} 

		} 

		return msg;
	}

	private static String validateOnhand(TCS_MOrder order) {
		MOrderLine[] olines = order.getLines(true, null);
		for(MOrderLine oline : olines) {
			MProduct prod = new MProduct(Env.getCtx(), oline.getM_Product_ID(), order.get_TrxName());
			if(prod.getProductType().equals("I") && prod.get_ValueAsBoolean("IsStocked")) {
				MWarehouse wh = new MWarehouse(Env.getCtx(), order.getM_Warehouse_ID(), order.get_TrxName());
				MLocator locator = new MLocator (Env.getCtx(), wh.getDefaultLocator().get_ID(), order.get_TrxName());
				if(wh.get_ValueAsBoolean("IsDisallowNegativeInv")) {

					String whereQtyOrdered = "M_Product_ID = ? and c_order_id = ?";
					BigDecimal sumQtyOrdered = new Query(Env.getCtx(), MOrderLine.Table_Name, whereQtyOrdered, order.get_TrxName())
							.setParameters(new Object[] {oline.getM_Product_ID(), order.getC_Order_ID()})
							.sum("QtyOrdered");
					
					BigDecimal newQty = sumQtyOrdered;
	
	
					String whereOnhand = "M_Product_ID = ? and m_locator_id = ? and datematerialpolicy <= ?";
					BigDecimal sumQtyOnHand = new Query(Env.getCtx(), MStorageOnHand.Table_Name, whereOnhand, order.get_TrxName())
							.setParameters(new Object[] {oline.getM_Product_ID(), locator.getM_Locator_ID(), order.getDateOrdered()})
							.sum("QtyOnHand");
					
					if(newQty.compareTo(sumQtyOnHand) > 0) {
						BigDecimal diff = newQty.subtract(sumQtyOnHand);
						throw new AdempiereException("Negative Inventory, Product: " + oline.getM_Product().getName() + 
								" , Current Onhand: " + sumQtyOnHand + " , SO Quantity: " + newQty +
								", Shortage of: " + diff);
					}
				}				
			}
		}
		return "";
	}

	private static String checkBOMDrop(TCS_MOrder order) {
		String whereClause = " isBOM='Y' and c_order_id = " + order.getC_Order_ID();
		List<MOrderLine> oLines = new Query(order.getCtx(), MOrderLine.Table_Name, whereClause, order.get_TrxName()).list();
		
		for(MOrderLine oLine : oLines) {
			if(!oLine.get_ValueAsBoolean("IsGeneratedBOMDrop"))
				throw new AdempiereException("BOM Line must generates BOM Drop First before Complete.");
		}
		
		return "";
	}

	private static String deleteCostDetail(TCS_MOrder order) {
		MOrderLine lines[] = order.getLines(true, null);
		for(MOrderLine line : lines) {
			String sqlDelete = "Delete from m_costdetail where c_orderline_id = " + line.getC_OrderLine_ID();
			DB.executeUpdate(sqlDelete, order.get_TrxName());
		}
		return "";
	}

	private static String removePayment(TCS_MOrder order) {
		order.set_ValueOfColumn("C_Payment_ID", null);
		order.saveEx();
		
		return "";
	}

	private static String updateRequisitionQtyOrdered(TCS_MOrder order) {
		MOrderLine[] orderLines = order.getLines();
		for (MOrderLine oLine : orderLines) {
			MMatchPR matchPR = new Query(Env.getCtx(), MMatchPR.Table_Name, "C_OrderLine_ID=?", null)
					.setParameters(oLine.getC_OrderLine_ID())
					.first();

			if (matchPR != null) {				
				BigDecimal diffQty = matchPR.getQtyOrdered().subtract(oLine.getQtyDelivered());
				matchPR.setQtyOrdered(oLine.getQtyDelivered());
				matchPR.saveEx();
				MRequisitionLine rLine = new MRequisitionLine(Env.getCtx(), matchPR.getM_RequisitionLine_ID(), null);
				BigDecimal reqLineQtyOrdered = (BigDecimal) rLine.get_Value("QtyOrdered");
				rLine.set_CustomColumn("QtyOrdered", reqLineQtyOrdered.subtract(diffQty));
				rLine.saveEx();
			}
		}
		return "";
	}

	private static String generateRequisition(TCS_MOrder order) {
		if(!order.isSOTrx())
			updateRequisition(order);

		return "";
	}

	private static String updateRequisition(TCS_MOrder order){
		String whereClause = "DocStatus!='CO' AND M_Requisition_ID IN (SELECT DISTINCT M_Requisition_ID "
				+ "FROM M_RequisitionLine mrl "
				+ "JOIN C_OrderLine col ON col.M_RequisitionLine_ID=mrl.M_RequisitionLine_ID "
				+ "WHERE col.C_Order_ID=?)";

		List<MRequisition> requisitions = new Query(Env.getCtx(), MRequisition.Table_Name, whereClause, null)
				.setParameters(order.getC_Order_ID())
				.setOnlyActiveRecords(true)
				.list();

		if (!requisitions.isEmpty()) {
			StringBuilder sb = new StringBuilder();
			for (MRequisition req : requisitions) {
				sb.append(req.getDocumentNo()).append(",");
			}
			sb.deleteCharAt(sb.length()-1);
			return "Abort.. Please check document status for Requisition: ";

		}

		for (MOrderLine orderLine : order.getLines()) {
			int M_RequisitionLine_ID = orderLine.get_ValueAsInt("M_RequisitionLine_ID");
			if(M_RequisitionLine_ID <= 0)
				continue;
			MRequisitionLine requisitionLine = new MRequisitionLine(Env.getCtx(), M_RequisitionLine_ID, null);	
			requisitionLine.set_ValueOfColumn("QtyRequired", ((BigDecimal) requisitionLine.get_Value("QtyRequired")).subtract(orderLine.getQtyOrdered()));
			requisitionLine.set_ValueOfColumn("QtyOrdered", requisitionLine.getQtyOrdered().add(orderLine.getQtyOrdered()));
			requisitionLine.saveEx();

			X_M_MatchPR matchPR = new X_M_MatchPR(Env.getCtx(),0,null);
			matchPR.setC_OrderLine_ID(orderLine.getC_OrderLine_ID());
			matchPR.setM_Requisition_ID(requisitionLine.getM_Requisition_ID());
			matchPR.setM_RequisitionLine_ID(requisitionLine.get_ID());
			matchPR.setC_Order_ID(orderLine.getC_Order_ID());
			matchPR.setDateTrx(orderLine.getC_Order().getDateOrdered());
			matchPR.setQtyOrdered(orderLine.getQtyOrdered());
			matchPR.saveEx();
		}

		return "";
	}

	private static String removeRequisition(TCS_MOrder order){
		if (order.getDocStatus().equals("CO")) {
			for (MOrderLine orderLine : order.getLines()) {
				int M_RequisitionLine_ID = orderLine.get_ValueAsInt("M_RequisitionLine_ID");
				if(M_RequisitionLine_ID <= 0)
					continue;
				MRequisitionLine requisitionLine = new MRequisitionLine(Env.getCtx(), M_RequisitionLine_ID, null);
				requisitionLine.set_ValueOfColumn("QtyOrdered",requisitionLine.getQtyOrdered().subtract(orderLine.getQtyOrdered()));
				requisitionLine.saveEx();	
			}
			String sqlDelete = "DELETE FROM M_MatchPR WHERE C_Order_ID=?";
			DB.executeUpdate(sqlDelete, order.get_ID(), null);

		}
		return "";
		/*//@win: for now we prefer not removing the reference to Requisition Line to preserve audit reference
		if (DocAction.equalsIgnoreCase(DOCACTION_Void)) {
	        String sql = "UPDATE C_OrderLine SET M_RequisitionLine_ID = NULL WHERE C_Order_ID=?";
	        int no = DB.executeUpdate(sql, get_ID(), get_TrxName());
	        log.info("UPDATED Order Line "+no);
		}
		 */	
	}

	public static String unreserveQty(TCS_MOrder order){
		if (!reserveStock(order)) {
			return "Cannot unreserve stock, Failed to update reservations";
		}

		return "";
	}

	public static String checkMatchPO(TCS_MOrder order){
		int [] temp = new Query(order.getCtx(), MOrderLine.Table_Name, "C_Order_ID="+order.getC_Order_ID(), order.get_TrxName())
				.getIDs();
		if (temp==null || temp.length==0) {
			return "";
		}
		String IDs = "";
		for (int i : temp) {
			IDs+=i;
			IDs+=", ";
		}
		IDs=IDs.substring(0, IDs.length()-2);
		//match PO tetap ada setelah MR direverse
		String sqlWhere = "M_MatchPO.C_OrderLine_ID IN ("+IDs+") AND mi.DocStatus IN ('CO','CL','IP')";
		boolean match = new Query(order.getCtx(), MMatchPO.Table_Name, sqlWhere, order.get_TrxName())
				.addJoinClause("JOIN M_InOutLine mil on mil.M_InOutline_ID=M_MatchPO.M_InOutLine_ID")
				.addJoinClause("JOIN M_InOut mi on mi.M_InOut_ID=mil.M_InOut_ID")
				.match();
		if (match) {
			return "Cannot Reverse Order : Existing Match PO Exist For Order Line";
		}
		return "";
	}

	public static String checkLinkedPayment(TCS_MOrder order){
		String sqlWhere="C_Order_ID="+order.getC_Order_ID()+" AND DocStatus IN ('CO','CL','IP')";
		boolean match = new Query(order.getCtx(), MPayment.Table_Name, sqlWhere, order.get_TrxName())
				.match();

		if (match) return "Cannot Reactivate / Void : Linked Payment Exist";
		return "";
	}

	public static String checkActiveLinkedInOut(TCS_MOrder order){
		int [] temp = new Query(order.getCtx(), MOrderLine.Table_Name, "C_Order_ID="+order.getC_Order_ID(), order.get_TrxName())
				.getIDs();
		if (temp==null || temp.length==0) {
			return "";
		}
		String IDs = "";
		for (int i : temp) {
			IDs+=i;
			IDs+=", ";
		}
		IDs=IDs.substring(0, IDs.length()-2);
		String sqlWhere = "C_OrderLine_ID IN ("+IDs+") AND mi.DocStatus IN ('CO','CL','IP')";
		boolean match = new Query(order.getCtx(), MInOutLine.Table_Name, sqlWhere, order.get_TrxName())
				.addJoinClause("JOIN M_InOut mi on mi.M_InOut_ID=M_InOutLine.M_InOut_ID")
				.match();
		if (match) {
			return "Cannot Reactivate Order : Active InOut Exists For Order Line";
		}
		return "";
	}
	private static String removeMatchQuotation(TCS_MOrder order) {
		StringBuilder sql = new StringBuilder("DELETE FROM ").append(X_M_MatchQuotation.Table_Name)
				.append(" WHERE C_Order_ID=?");
		DB.executeUpdateEx(sql.toString(), new Object[] {order.get_ID()}, order.get_TrxName());

		return "";
	}

	private static boolean reserveStock (TCS_MOrder order)
	{
		MDocType dt = MDocType.get(order.getCtx(), order.getC_DocType_ID());
		MOrderLine [] lines = order.getLines();
		//	Binding
		boolean binding = !dt.isProposal();
		//	Not binding - i.e. Target=0
		if (DocAction.ACTION_Void.equals(order.getDocAction())
				//	Closing Binding Quotation
				|| (MDocType.DOCSUBTYPESO_Quotation.equals(dt.getDocSubTypeSO()) 
						&& DocAction.ACTION_Close.equals(order.getDocAction())) 
				) // || isDropShip() )
			binding = false;
		boolean isSOTrx = order.isSOTrx();
		//	Force same WH for all but SO/PO
		int header_M_Warehouse_ID = order.getM_Warehouse_ID();
		if (MDocType.DOCSUBTYPESO_StandardOrder.equals(dt.getDocSubTypeSO())
				|| MDocType.DOCBASETYPE_PurchaseOrder.equals(dt.getDocBaseType()))
			header_M_Warehouse_ID = 0;		//	don't enforce

		BigDecimal Volume = Env.ZERO;
		BigDecimal Weight = Env.ZERO;

		//	Always check and (un) Reserve Inventory		
		for (int i = 0; i < lines.length; i++)
		{
			MOrderLine line = lines[i];
			//	Check/set WH/Org
			if (header_M_Warehouse_ID != 0)	//	enforce WH
			{
				if (header_M_Warehouse_ID != line.getM_Warehouse_ID())
					line.setM_Warehouse_ID(header_M_Warehouse_ID);
				if (order.getAD_Org_ID() != line.getAD_Org_ID())
					line.setAD_Org_ID(order.getAD_Org_ID());
			}
			//	Binding
			BigDecimal target = Env.ZERO; 
			BigDecimal difference = target
					.subtract(line.getQtyOrdered());

			if (difference.signum() == 0 || line.getQtyOrdered().signum() < 0)
			{
				if (difference.signum() == 0 || line.getQtyReserved().signum() == 0)
				{
					MProduct product = line.getProduct();
					if (product != null)
					{
						Volume = Volume.add(product.getVolume().multiply(line.getQtyOrdered()));
						Weight = Weight.add(product.getWeight().multiply(line.getQtyOrdered()));
					}
					continue;
				}
				else if (line.getQtyOrdered().signum() < 0 && line.getQtyReserved().signum() > 0)
				{
					difference = line.getQtyReserved().negate();
				}
			}

			//	Check Product - Stocked and Item
			MProduct product = line.getProduct();
			if (product != null) 
			{
				if (product.isStocked())
				{
					//	Update Reservation Storage
					if (!MStorageReservation.add(order.getCtx(), line.getM_Warehouse_ID(), 
							line.getM_Product_ID(), 
							line.getM_AttributeSetInstance_ID(),
							difference, isSOTrx, order.get_TrxName()))
						return false;
				}	//	stocked
				//
				Volume = Volume.add(product.getVolume().multiply(line.getQtyOrdered()));
				Weight = Weight.add(product.getWeight().multiply(line.getQtyOrdered()));
			}	//	product
		}	//	reverse inventory

		order.setVolume(Volume);
		order.setWeight(Weight);
		return true;
	}	//	reserveStock

	/**
	 * Delete related Match PR records for a certain Purchase Order
	 * @param order
	 * @return
	 */
	private static String removeMatchPR(TCS_MOrder order) {
		for (MOrderLine orderLine : order.getLines()) {
			int M_RequisitionLine_ID = orderLine.get_ValueAsInt("M_RequisitionLine_ID");
			if(M_RequisitionLine_ID <= 0)
				continue;
			MRequisitionLine requisitionLine = new MRequisitionLine(Env.getCtx(), M_RequisitionLine_ID, order.get_TrxName());
			requisitionLine.set_ValueOfColumn("QtyOrdered",requisitionLine.getQtyOrdered().subtract(orderLine.getQtyOrdered()));
			requisitionLine.save();
			
			Trx trx = Trx.get(order.get_TrxName(), false);

			try {
				trx.commit(true);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		StringBuilder sql = new StringBuilder("DELETE FROM ").append(X_M_MatchPR.Table_Name)
				.append(" WHERE C_Order_ID=?");
		DB.executeUpdateEx(sql.toString(), new Object[] {order.get_ID()}, order.get_TrxName());

		return "";
	}
}
