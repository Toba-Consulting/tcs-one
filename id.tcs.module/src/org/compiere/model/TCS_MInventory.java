package org.compiere.model;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;

import org.adempiere.exceptions.NegativeInventoryDisallowedException;
import org.compiere.process.DocAction;
import org.compiere.process.DocOptions;
import org.compiere.util.CLogger;
import org.compiere.util.Env;
import org.compiere.util.Msg;
import org.compiere.util.Util;

public class TCS_MInventory extends MInventory implements DocOptions {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4470831310287534076L;

	public TCS_MInventory(Properties ctx, int M_Inventory_ID, String trxName) {
		super(ctx, M_Inventory_ID, trxName);
	}

	public TCS_MInventory(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	private String DOCSUBTYPEINV_MiscReceipt = "MR";

	/**
	 * 	Complete Document
	 * 	@return new status (Complete, In Progress, Invalid, Waiting ..)
	 */
	public String completeIt()
	{
		MDocType dt = MDocType.get(getC_DocType_ID());
		String docSubTypeInv = dt.getDocSubTypeInv();
		if (Util.isEmpty(docSubTypeInv)) {
			m_processMsg = "Document inventory subtype not configured, cannot complete";
			return DocAction.STATUS_Invalid;
		}

		//	Re-Check
		if (!m_justPrepared)
		{
			String status = prepareIt();
			m_justPrepared = false;
			if (!DocAction.STATUS_InProgress.equals(status))
				return status;
		}

		// Set the definite document number after completed (if needed)
		setDefiniteDocumentNo();

		m_processMsg = ModelValidationEngine.get().fireDocValidate(this, ModelValidator.TIMING_BEFORE_COMPLETE);
		if (m_processMsg != null)
			return DocAction.STATUS_Invalid;

		//	Implicit Approval
		if (!isApproved())
			approveIt();
		if (log.isLoggable(Level.INFO)) log.info(toString());

		StringBuilder errors = new StringBuilder();
		MInventoryLine[] lines = getLines(false);
		for (MInventoryLine line : lines)
		{
			if (!line.isActive())
				continue;

			MProduct product = line.getProduct();	
			try
			{
				BigDecimal qtyDiff = Env.ZERO;
				if (MDocType.DOCSUBTYPEINV_InternalUseInventory.equals(docSubTypeInv) 
						| DOCSUBTYPEINV_MiscReceipt.equals(docSubTypeInv))
					qtyDiff = line.getQtyInternalUse().negate();
				else if (MDocType.DOCSUBTYPEINV_PhysicalInventory.equals(docSubTypeInv))
					qtyDiff = line.getQtyCount().subtract(line.getQtyBook());
				else if (MDocType.DOCSUBTYPEINV_CostAdjustment.equals(docSubTypeInv))
				{
					if (!isReversal())
					{
						BigDecimal currentCost = line.getCurrentCostPrice();
						MClient client = MClient.get(getCtx(), getAD_Client_ID());
						MAcctSchema as = client.getAcctSchema();
						MAcctSchema[] ass = MAcctSchema.getClientAcctSchema(getCtx(), client.get_ID());

						if (as.getC_Currency_ID() != getC_Currency_ID()) 
						{
							for (int i = 0; i < ass.length ; i ++)
							{
								MAcctSchema a =  ass[i];
								if (a.getC_Currency_ID() ==  getC_Currency_ID()) 
									as = a ; 
							}
						}

						MCost cost = product.getCostingRecord(as, getAD_Org_ID(), line.getM_AttributeSetInstance_ID(), getCostingMethod());
						if (cost != null && cost.getCurrentCostPrice().compareTo(currentCost) != 0) 
						{
							m_processMsg = "Current Cost for Line " + line.getLine() + " have changed.";
							return DocAction.STATUS_Invalid; 
						}
					}
				}

				//If Quantity Count minus Quantity Book = Zero, then no change in Inventory
				if (qtyDiff.signum() == 0)
					continue;

				//Ignore the Material Policy when is Reverse Correction
				if(!isReversal()){
					BigDecimal qtyOnLineMA = MInventoryLineMA.getManualQty(line.getM_InventoryLine_ID(), get_TrxName());

					if(qtyDiff.signum()<0){
						if(qtyOnLineMA.compareTo(qtyDiff)<0){
							m_processMsg = "@Over_Qty_On_Attribute_Tab@ " + line.getLine();
							return DOCSTATUS_Invalid;
						}
					}else{
						if(qtyOnLineMA.compareTo(qtyDiff)>0){
							m_processMsg = "@Over_Qty_On_Attribute_Tab@ " + line.getLine();
							return DOCSTATUS_Invalid;
						}
					}
					checkMaterialPolicy(line, qtyDiff.subtract(qtyOnLineMA));
				}
				//	Stock Movement - Counterpart MOrder.reserveStock
				if (product != null 
						&& product.isStocked() )
				{
					log.fine("Material Transaction");
					MTransaction mtrx = null; 

					//If AttributeSetInstance = Zero then create new  AttributeSetInstance use Inventory Line MA else use current AttributeSetInstance
					if (line.getM_AttributeSetInstance_ID() == 0 || qtyDiff.compareTo(Env.ZERO) == 0)
					{
						MInventoryLineMA mas[] = MInventoryLineMA.get(getCtx(),
								line.getM_InventoryLine_ID(), get_TrxName());

						for (int j = 0; j < mas.length; j++)
						{
							MInventoryLineMA ma = mas[j];
							BigDecimal QtyMA = ma.getMovementQty();
							BigDecimal QtyNew = QtyMA.add(qtyDiff);
							if (log.isLoggable(Level.FINE)) log.fine("Diff=" + qtyDiff 
									+ " - Instance OnHand=" + QtyMA + "->" + QtyNew);

							if (!MStorageOnHand.add(getCtx(), getM_Warehouse_ID(),
									line.getM_Locator_ID(),
									line.getM_Product_ID(), 
									ma.getM_AttributeSetInstance_ID(), 
									QtyMA.negate(),ma.getDateMaterialPolicy(), get_TrxName()))
							{
								String lastError = CLogger.retrieveErrorString("");
								m_processMsg = "Cannot correct Inventory (MA) - " + lastError;
								return DocAction.STATUS_Invalid;
							}

							// Only Update Date Last Inventory if is a Physical Inventory
							if (MDocType.DOCSUBTYPEINV_PhysicalInventory.equals(docSubTypeInv))
							{	
								MStorageOnHand storage = MStorageOnHand.get(getCtx(), line.getM_Locator_ID(), 
										line.getM_Product_ID(), ma.getM_AttributeSetInstance_ID(),ma.getDateMaterialPolicy(),get_TrxName());						
								storage.setDateLastInventory(getMovementDate());
								if (!storage.save(get_TrxName()))
								{
									m_processMsg = "Storage not updated(2)";
									return DocAction.STATUS_Invalid;
								}
							}

							String m_MovementType =null;
							if(QtyMA.negate().compareTo(Env.ZERO) > 0 )
								m_MovementType = MTransaction.MOVEMENTTYPE_InventoryIn;
							else
								m_MovementType = MTransaction.MOVEMENTTYPE_InventoryOut;
							//	Transaction
							mtrx = new MTransaction (getCtx(), line.getAD_Org_ID(), m_MovementType,
									line.getM_Locator_ID(), line.getM_Product_ID(), ma.getM_AttributeSetInstance_ID(),
									QtyMA.negate(), getMovementDate(), get_TrxName());

							mtrx.setM_InventoryLine_ID(line.getM_InventoryLine_ID());
							if (!mtrx.save())
							{
								m_processMsg = "Transaction not inserted(2)";
								return DocAction.STATUS_Invalid;
							}

							qtyDiff = QtyNew;						

						}	
					}

					//sLine.getM_AttributeSetInstance_ID() != 0
					// Fallback
					if (mtrx == null)
					{
						Timestamp dateMPolicy= qtyDiff.signum() > 0 ? getMovementDate() : null;
						if (line.getM_AttributeSetInstance_ID() > 0)
						{
							Timestamp t = MStorageOnHand.getDateMaterialPolicy(line.getM_Product_ID(), line.getM_AttributeSetInstance_ID(), line.getM_Locator_ID(), line.get_TrxName());
							if (t != null)
								dateMPolicy = t;
						}

						//Fallback: Update Storage - see also VMatch.createMatchRecord
						if (!MStorageOnHand.add(getCtx(), getM_Warehouse_ID(),
								line.getM_Locator_ID(),
								line.getM_Product_ID(), 
								line.getM_AttributeSetInstance_ID(), 
								qtyDiff,dateMPolicy,get_TrxName()))
						{
							String lastError = CLogger.retrieveErrorString("");
							m_processMsg = "Cannot correct Inventory OnHand (MA) - " + lastError;
							return DocAction.STATUS_Invalid;
						}

						// Only Update Date Last Inventory if is a Physical Inventory
						if (MDocType.DOCSUBTYPEINV_PhysicalInventory.equals(docSubTypeInv))
						{	
							MStorageOnHand storage = MStorageOnHand.get(getCtx(), line.getM_Locator_ID(), 
									line.getM_Product_ID(), line.getM_AttributeSetInstance_ID(),dateMPolicy, get_TrxName());						

							storage.setDateLastInventory(getMovementDate());
							if (!storage.save(get_TrxName()))
							{
								m_processMsg = "Storage not updated(2)";
								return DocAction.STATUS_Invalid;
							}
						}

						String m_MovementType = null;
						if(qtyDiff.compareTo(Env.ZERO) > 0 )
							m_MovementType = MTransaction.MOVEMENTTYPE_InventoryIn;
						else
							m_MovementType = MTransaction.MOVEMENTTYPE_InventoryOut;
						//	Transaction
						mtrx = new MTransaction (getCtx(), line.getAD_Org_ID(), m_MovementType,
								line.getM_Locator_ID(), line.getM_Product_ID(), line.getM_AttributeSetInstance_ID(),
								qtyDiff, getMovementDate(), get_TrxName());
						mtrx.setM_InventoryLine_ID(line.getM_InventoryLine_ID());
						if (!mtrx.save())
						{
							m_processMsg = "Transaction not inserted(2)";
							return DocAction.STATUS_Invalid;
						}					
					}	//	Fallback
				}	//	stock movement
			}
			catch (NegativeInventoryDisallowedException e)
			{
				log.severe(e.getMessage());
				errors.append(Msg.getElement(getCtx(), "Line")).append(" ").append(line.getLine()).append(": ");
				errors.append(e.getMessage()).append("\n");
			}

		}	//	for all lines

		if (errors.toString().length() > 0)
		{
			m_processMsg = errors.toString();
			return DocAction.STATUS_Invalid;
		}

		//	User Validation
		String valid = ModelValidationEngine.get().fireDocValidate(this, ModelValidator.TIMING_AFTER_COMPLETE);
		if (valid != null)
		{
			m_processMsg = valid;
			return DocAction.STATUS_Invalid;
		}

		//
		setProcessed(true);
		setDocAction(DOCACTION_Close);
		return DocAction.STATUS_Completed;
	}	//	completeIt

	protected TCS_MInventory reverse(boolean accrual) {
		Timestamp reversalDate = accrual ? Env.getContextAsDate(getCtx(), "#Date") : getMovementDate();
		if (reversalDate == null) {
			reversalDate = new Timestamp(System.currentTimeMillis());
		}

		MDocType dt = MDocType.get(getC_DocType_ID());
		MPeriod.testPeriodOpen(getCtx(), reversalDate, dt.getDocBaseType(), getAD_Org_ID());

		//	Deep Copy
		TCS_MInventory reversal = new TCS_MInventory(getCtx(), 0, get_TrxName());
		copyValues(this, reversal, getAD_Client_ID(), getAD_Org_ID());
		reversal.setDocumentNo(getDocumentNo() + REVERSE_INDICATOR);	//	indicate reversals
		reversal.setMovementDate(reversalDate);
		reversal.setDocStatus(DOCSTATUS_Drafted);
		reversal.setDocAction(DOCACTION_Complete);
		reversal.setIsApproved (false);
		reversal.setPosted(false);
		reversal.setProcessed(false);
		StringBuilder msgd = new StringBuilder("{->").append(getDocumentNo()).append(")");
		reversal.addDescription(msgd.toString());
		//FR1948157
		reversal.setReversal_ID(getM_Inventory_ID());
		reversal.saveEx();
		reversal.setReversal(true);

		//	Reverse Line Qty
		List<TCS_MInventoryLine> oLines = getLines();
		for (TCS_MInventoryLine oLine : oLines)
		{
			TCS_MInventoryLine rLine = new TCS_MInventoryLine(getCtx(), 0, get_TrxName());
			copyValues(oLine, rLine, oLine.getAD_Client_ID(), oLine.getAD_Org_ID());
			rLine.setM_Inventory_ID(reversal.getM_Inventory_ID());
			//rLine.setParent(reversal);
			//AZ Goodwill
			// store original (voided/reversed) document line
			rLine.setReversalLine_ID(oLine.getM_InventoryLine_ID());
			//
			rLine.setQtyBook (oLine.getQtyCount());		//	switch
			rLine.setQtyCount (oLine.getQtyBook());
			rLine.setQtyInternalUse (oLine.getQtyInternalUse().negate());		
			rLine.setNewCostPrice(oLine.getCurrentCostPrice());
			rLine.setCurrentCostPrice(oLine.getNewCostPrice());

			rLine.saveEx();

			//We need to copy MA
			if (rLine.getM_AttributeSetInstance_ID() == 0)
			{
				MInventoryLineMA mas[] = MInventoryLineMA.get(getCtx(),
						oLine.getM_InventoryLine_ID(), get_TrxName());
				for (int j = 0; j < mas.length; j++)
				{
					MInventoryLineMA ma = new MInventoryLineMA (rLine, 
							mas[j].getM_AttributeSetInstance_ID(),
							mas[j].getMovementQty().negate(),mas[j].getDateMaterialPolicy(),true);
					ma.saveEx();
				}
			}
		}
		//
		if (!reversal.processIt(DocAction.ACTION_Complete))
		{
			m_processMsg = "Reversal ERROR: " + reversal.getProcessMsg();
			return null;
		}
		reversal.closeIt();
		reversal.setDocStatus(DOCSTATUS_Reversed);
		reversal.setDocAction(DOCACTION_None);
		reversal.saveEx();

		//	Update Reversed (this)
		msgd = new StringBuilder("(").append(reversal.getDocumentNo()).append("<-)");
		addDescription(msgd.toString());
		setProcessed(true);
		//FR1948157
		setReversal_ID(reversal.getM_Inventory_ID());
		setDocStatus(DOCSTATUS_Reversed);	//	may come from void
		setDocAction(DOCACTION_None);

		return reversal;
	}

	@Override
	public int customizeValidActions(String docStatus, Object processing, String orderType, String isSOTrx,
			int AD_Table_ID, String[] docAction, String[] options, int index) {

		for (int i = 0; i < options.length; i++) {
			options[i] = null;
		}

		index = 0;

		if (docStatus.equals(DocAction.STATUS_Drafted)) {
			options[index++] = DocAction.ACTION_Prepare;
			options[index++] = DocAction.ACTION_Void;
		} else if (docStatus.equals(DocAction.STATUS_InProgress)) {
			options[index++] = DocAction.ACTION_Complete;
			options[index++] = DocAction.ACTION_Void;
		} else if (docStatus.equals(DocAction.STATUS_Completed)) {
			options[index++] = DocAction.ACTION_Reverse_Accrual;
			options[index++] = DocAction.ACTION_Reverse_Correct;
		} else if (docStatus.equals(DocAction.STATUS_Invalid)) {
			options[index++] = DocAction.ACTION_Complete;
			options[index++] = DocAction.ACTION_Void;
		} else if (docStatus.equals(DocAction.STATUS_NotApproved)) {
			options[index++] = DocAction.ACTION_Complete;
			options[index++] = DocAction.ACTION_Void;
		}

		return index;
	}

	/**
	 * 	Get Lines
	 *	@param requery requery
	 *	@return array of lines
	 */
	public List<TCS_MInventoryLine>  getLines()
	{

		List<TCS_MInventoryLine> list = new Query(getCtx(), I_M_InventoryLine.Table_Name, "M_Inventory_ID=?", get_TrxName())
				.setParameters(get_ID())
				.setOrderBy(MInventoryLine.COLUMNNAME_Line)
				.list();
		return list;	
	}	//	getLines
}
