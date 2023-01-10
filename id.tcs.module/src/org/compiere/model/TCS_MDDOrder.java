package org.compiere.model;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;

import org.adempiere.exceptions.AdempiereException;
import org.compiere.process.DocAction;
import org.compiere.process.DocOptions;
import org.compiere.util.Env;
import org.compiere.util.Msg;
import org.eevolution.model.MDDOrder;
import org.eevolution.model.MDDOrderLine;

public class TCS_MDDOrder extends MDDOrder implements DocOptions{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public TCS_MDDOrder(Properties ctx, int DD_Order_ID, String trxName) {
		super(ctx, DD_Order_ID, trxName);
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
	
	public int copyLinesFrom (MDDOrder otherOrder, boolean counter, boolean copyASI)
	{
		if (isProcessed() || isPosted() || otherOrder == null)
			return 0;
		MDDOrderLine[] fromLines = otherOrder.getLines(false, null);
		String whereClause = "DD_Order_ID= " + otherOrder.getDD_Order_ID();
		List<MDDOrderLine> lines = new Query(getCtx(), MDDOrderLine.Table_Name, whereClause, get_TrxName())
									.list();
		
		int count = lines.size();
		for (MDDOrderLine line : lines)
		{	
			MDDOrderLine newLine = new MDDOrderLine(otherOrder);
			newLine.set_ValueOfColumn("Ref_InternalOrderLine_ID", line.getDD_OrderLine_ID());
			newLine.setLine(line.getLine());
			newLine.setDD_Order_ID(getDD_Order_ID());
			newLine.setOrder(this);
			newLine.setM_Product_ID(line.getM_Product_ID());
			newLine.setC_Charge_ID(line.getC_Charge_ID());
			newLine.setC_Project_ID(line.getC_Project_ID());
			newLine.setC_UOM_ID(line.getC_UOM_ID());
			newLine.setDateDelivered(line.getDateDelivered());
			newLine.setDateOrdered(line.getDateOrdered());
			newLine.setDatePromised(line.getDatePromised());
			newLine.setDescription(line.getDescription());
			newLine.setFreightAmt(line.getFreightAmt());
			newLine.setLineNetAmt(line.getLineNetAmt());
			newLine.setM_Locator_ID(line.getM_Locator_ID());
			newLine.setM_LocatorTo_ID(line.getM_LocatorTo_ID());
			newLine.setAD_Org_ID(line.getAD_Org_ID());
			newLine.setQtyOrdered(line.getQtyOrdered());
			newLine.setQtyEntered(line.getQtyEntered());
			//	References
			if (!copyASI)
			{
				newLine.setM_AttributeSetInstance_ID(0);
				//line.setS_ResourceAssignment_ID(0);
			}

			newLine.setQtyDelivered(Env.ZERO);
			newLine.setQtyReserved(Env.ZERO);
			newLine.setDateDelivered(null);

			newLine.setProcessed(false);
			if (newLine.save(get_TrxName()))
				count++;
		}
		if (fromLines.length != count)
			log.log(Level.SEVERE, "Line difference - From=" + fromLines.length + " <> Saved=" + count);
		return count;
	}	//	copyLinesFrom
	
	
	/**
	 * 	Create new Order by copying
	 * 	@param from order
	 * 	@param dateDoc date of the document date
	 * 	@param C_DocType_ID target document type
	 * 	@param isSOTrx sales order 
	 * 	@param counter create counter links
	 *	@param copyASI copy line attributes Attribute Set Instance, Resaouce Assignment
	 * 	@param trxName trx
	 *	@return Order
	 */
	public static TCS_MDDOrder copyFrom (TCS_MDDOrder from, Timestamp dateDoc, 
		int C_DocType_ID, boolean isSOTrx, boolean counter, boolean copyASI, 
		String trxName)
	{
		TCS_MDDOrder to = new TCS_MDDOrder (from.getCtx(), 0, trxName);
		to.set_TrxName(trxName);
		PO.copyValues(from, to, from.getAD_Client_ID(), from.getC_BPartner().getAD_OrgBP_ID());
		to.set_ValueNoCheck ("C_Order_ID", null);
		to.set_ValueNoCheck ("DocumentNo", null);
		to.setC_DocType_ID(C_DocType_ID);
		to.setIsSOTrx(isSOTrx);

		to.setDocStatus (DOCSTATUS_Drafted);		//	Draft
		to.setDocAction(DOCACTION_Complete);
		//
		to.setIsSelected (false);
		to.setDateOrdered (dateDoc);
		to.setDatePromised (dateDoc);	//	assumption
		to.setDatePrinted(null);
		to.setIsPrinted (false);
		//
		to.setIsApproved (false);
		//	Amounts are updated  when adding lines
		//
		to.setDeliveryRule(from.getDeliveryRule());
		to.setDeliveryViaRule(from.getDeliveryViaRule());
		to.setIsDelivered(false);
		to.setPosted (false);
		to.setProcessed (false);
		
		if (from.getC_Project() != null)
			to.setC_Project_ID(from.getC_Project_ID());
		
		if (from.getC_Campaign() != null)
			to.setC_Campaign_ID(from.getC_Campaign_ID());
		
		if (from.getC_Activity() != null)
			to.setC_Activity_ID(from.getC_Activity_ID());
		
		
		if (counter) {
			to.set_ValueOfColumn("Ref_InternalOrder_ID", from.getDD_Order_ID());
			to.setDescription("Reference Order = " + from.getDocumentNo());
			MOrg org = MOrg.get(from.getCtx(), from.getAD_Org_ID());
			int counterC_BPartner_ID = org.getLinkedC_BPartner_ID(trxName);
			if (counterC_BPartner_ID == 0)
				return null;
			to.setBPartner(MBPartner.get(from.getCtx(), counterC_BPartner_ID));
		} else
			to.set_ValueOfColumn("Ref_InternalOrder_ID",null);
		//
		if (!to.save(trxName))
			throw new IllegalStateException("Could not create Order");
		if (counter){
			// save to other counter document can re-get refer document  
			from.set_ValueOfColumn("Ref_InternalOrder_ID", to.getDD_Order_ID());
			from.setDescription("Reference Order = " + to.getDocumentNo());
			from.saveEx();
		}

		if (to.copyLinesFrom(from, counter, copyASI) == 0)
			throw new IllegalStateException("Could not create Order Lines");
		
		return to;
	}	//	copyFrom
	
	/**
	 * 	Create Counter Document
	 * 	@return counter order
	 */
	public TCS_MDDOrder createCounterDoc()
	{
		//	Is this itself a counter doc ?
		if (get_ValueAsInt("Ref_InternalOrder_ID") != 0)
			return null;
		
		//	Org Must be linked to BPartner
		MOrg org = MOrg.get(getCtx(), getAD_Org_ID());
		int counterC_BPartner_ID = org.getLinkedC_BPartner_ID(get_TrxName()); 
		if (counterC_BPartner_ID == 0)
			return null;
		//	Business Partner needs to be linked to Org
		MBPartner bp = new MBPartner (getCtx(), getC_BPartner_ID(), get_TrxName());
		int counterAD_Org_ID = bp.getAD_OrgBP_ID(); 
		if (counterAD_Org_ID == 0)
			return null;
		
		MBPartner counterBP = new MBPartner (getCtx(), counterC_BPartner_ID, null);
		MOrgInfo counterOrgInfo = MOrgInfo.get(getCtx(), counterAD_Org_ID, get_TrxName());
		if (log.isLoggable(Level.INFO)) log.info("Counter BP=" + counterBP.getName());

		//	Document Type
		int p_C_DocType_ID = 0;
		MDocTypeCounter counterDT = MDocTypeCounter.getCounterDocType(getCtx(), getC_DocType_ID());
		if (counterDT != null)
		{
			if (log.isLoggable(Level.FINE)) log.fine(counterDT.toString());
			if (!counterDT.isCreateCounter() || !counterDT.isValid())
				return null;
			p_C_DocType_ID = counterDT.getCounter_C_DocType_ID();
		}
		else	//	indirect
		{
			p_C_DocType_ID = MDocTypeCounter.getCounterDocType_ID(getCtx(), getC_DocType_ID());
			if (log.isLoggable(Level.FINE)) log.fine("Indirect C_DocType_ID=" + p_C_DocType_ID);
			if (p_C_DocType_ID <= 0)
				return null;
		}
		//	Deep Copy
		TCS_MDDOrder counter = copyFrom (this, getDateOrdered(), 
			p_C_DocType_ID, !isSOTrx(), true, false, get_TrxName());
		//
		counter.setAD_Org_ID(counterAD_Org_ID);
		counter.setM_Warehouse_ID(counterOrgInfo.getM_Warehouse_ID());
		//
//		counter.setBPartner(counterBP); // was set on copyFrom
		counter.setDatePromised(getDatePromised());		// default is date ordered 
		//	References (Should not be required)
		counter.setSalesRep_ID(getSalesRep_ID());
		counter.saveEx(get_TrxName());
		
		MWarehouse wh = new MWarehouse(getCtx(), counterOrgInfo.getM_Warehouse_ID(), get_TrxName());
		//	Update copied lines
		MDDOrderLine[] counterLines = counter.getLines(true, null);
		for (int i = 0; i < counterLines.length; i++)
		{
			MDDOrderLine counterLine = counterLines[i];
			counterLine.setOrder(counter);	//	copies header values (BP, etc.)
			counterLine.setM_Locator_ID(wh.getDefaultLocator().getM_Locator_ID());
			counterLine.saveEx(get_TrxName());
		}
		if (log.isLoggable(Level.FINE)) log.fine(counter.toString());
		
		//	Document Action
		if (counterDT != null)
		{
			if (counterDT.getDocAction() != null)
			{
				counter.setDocAction(counterDT.getDocAction());
				// added AdempiereException by zuhri
				if (!counter.processIt(counterDT.getDocAction()))
					throw new AdempiereException(Msg.getMsg(getCtx(), "FailedProcessingDocument") + " - " + counter.getProcessMsg());
				// end added
				counter.saveEx(get_TrxName());
			}
		}
		return counter;
	}	//	createCounterDoc
	
}
