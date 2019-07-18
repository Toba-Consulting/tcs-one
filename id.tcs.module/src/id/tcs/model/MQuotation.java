package id.tcs.model;

import java.io.File;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;

import org.adempiere.exceptions.BPartnerNoBillToAddressException;
import org.adempiere.exceptions.BPartnerNoShipToAddressException;
import org.adempiere.exceptions.FillMandatoryException;
import org.compiere.model.I_C_OrderTax;
import org.compiere.model.MBPartner;
import org.compiere.model.MBPartnerLocation;
import org.compiere.model.MCurrency;
import org.compiere.model.MOrderTax;
import org.compiere.model.MPriceList;
import org.compiere.model.MPriceListVersion;
import org.compiere.model.MSysConfig;
import org.compiere.model.MUser;
import org.compiere.model.MWarehouse;
import org.compiere.model.ModelValidationEngine;
import org.compiere.model.ModelValidator;
import org.compiere.model.PO;
import org.compiere.model.Query;
import org.compiere.print.MPrintFormat;
import org.compiere.process.DocAction;
import org.compiere.process.DocOptions;
import org.compiere.process.DocumentEngine;
import org.compiere.process.ProcessInfo;
import org.compiere.process.ServerProcessCtl;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;

public class MQuotation extends X_C_Quotation implements DocAction, DocOptions {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8463300781634459249L;

	public MQuotation(Properties ctx, int C_Quotation_ID, String trxName) {
		super(ctx, C_Quotation_ID, trxName);
	}

	public MQuotation(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** Process Message */
	private String m_processMsg = null;
	/** Just Prepared Flag */
	//private boolean m_justPrepared = false;
	private MPrintFormat format;

	/**
	 * This method returns array of quotation line for the selected quotation
	 * 
	 * @author edwinang
	 * @return
	 */
	public MQuotationLine[] getLines() {

		final String whereClause = I_C_QuotationLine.COLUMNNAME_C_Quotation_ID
				+ "=?";
		List<MQuotationLine> list = new Query(getCtx(),
				MQuotationLine.Table_Name, whereClause, get_TrxName())
				.setParameters(get_ID()).setOnlyActiveRecords(true).list();
		//
		MQuotationLine[] m_quotation = new MQuotationLine[list.size()];
		list.toArray(m_quotation);
		return m_quotation;
	}

	public X_M_MatchQuotation[] getMatchQuotation() {

		final String whereClause = I_M_MatchQuotation.COLUMNNAME_C_Quotation_ID+ "=?";
		List<X_M_MatchQuotation> list = new Query(getCtx(),I_M_MatchQuotation.Table_Name, whereClause, get_TrxName())
				.setParameters(get_ID()).list();
		//
		X_M_MatchQuotation[] m_match = new X_M_MatchQuotation[list.size()];
		list.toArray(m_match);
		return m_match;
	}
	
	public boolean hasMatchQuotationSO() {

		final String whereClause = I_M_MatchQuotation.COLUMNNAME_C_Quotation_ID + "=? AND C_Order_ID IS NOT NULL";
		boolean match = new Query(getCtx(),X_M_MatchQuotation.Table_Name, whereClause, get_TrxName())
				.setParameters(get_ID())
				.match();

		return match;
	}
	
	public boolean hasMatchQuotationPR() {

		final String whereClause = I_M_MatchQuotation.COLUMNNAME_C_Quotation_ID + "=? AND M_Requisition_ID IS NOT NULL";
		boolean match = new Query(getCtx(),X_M_MatchQuotation.Table_Name, whereClause, get_TrxName())
				.setParameters(get_ID())
				.match();

		return match;
	}
	
	// getLines

	@Override
	public boolean processIt(String action) throws Exception {
		m_processMsg = null;
		DocumentEngine engine = new DocumentEngine(this, getDocStatus());
		return engine.processIt(action, getDocAction());
	}

	@Override
	public boolean unlockIt() {
		if (log.isLoggable(Level.INFO))
			log.info(toString());
		setProcessing(false);
		return true;
	} // unlockIt

	@Override
	public boolean invalidateIt() {
		if (log.isLoggable(Level.INFO))
			log.info(toString());
		setDocAction(DOCACTION_Prepare);
		return true;
	}

	@Override
	public String prepareIt() {
		log.info(toString());

		// get System Configurator
		boolean SKIP_VALIDATION = MSysConfig.getBooleanValue("SKIP_VALIDATION",
				false, getAD_Client_ID());
		if (SKIP_VALIDATION) {
			setDocAction(DocAction.ACTION_Complete);
			return DOCSTATUS_InProgress;
		}

		m_processMsg = ModelValidationEngine.get().fireDocValidate(this,
				ModelValidator.TIMING_BEFORE_PREPARE);
		if (m_processMsg != null)
			return DocAction.STATUS_Invalid;

		// put validation code here

		m_processMsg = ModelValidationEngine.get().fireDocValidate(this,
				ModelValidator.TIMING_AFTER_PREPARE);
		if (m_processMsg != null)
			return DocAction.STATUS_Invalid;

		setDocAction(DocAction.ACTION_Complete);

		return DocAction.STATUS_InProgress;
	}

	@Override
	public boolean approveIt() {
		if (log.isLoggable(Level.INFO))
			log.info(toString());
		setIsApproved(true);
		return true;
	}

	@Override
	public boolean rejectIt() {
		if (log.isLoggable(Level.INFO))
			log.info(toString());
		setIsApproved(false);
		return true;
	}

	@Override
	public String completeIt() {

		String status = prepareIt();

		if (!DocAction.STATUS_InProgress.equals(status))
			return status;

		/*
		 *	@stephan
		 *	TAOWI-897 check tax in quotation line must be same with tax header
		 */
		if(getC_Tax_ID() > 0){
			for (MQuotationLine quotationLine : getLines()) {
				if(getC_Tax_ID() != quotationLine.getC_Tax_ID()){
					quotationLine.setC_Tax_ID(getC_Tax_ID());
					quotationLine.saveEx();
				}
			}
		}
		
		m_processMsg = ModelValidationEngine.get().fireDocValidate(this,
				ModelValidator.TIMING_BEFORE_COMPLETE);
		if (m_processMsg != null)
			return DocAction.STATUS_Invalid;

		// User Validation
		String valid = ModelValidationEngine.get().fireDocValidate(this,
				ModelValidator.TIMING_AFTER_COMPLETE);
		if (valid != null) {
			m_processMsg = valid;
			return DocAction.STATUS_Invalid;
		}

		setProcessed(true);
		setDocAction(DocAction.ACTION_Close);

		return DocAction.STATUS_Completed;
	}

	@Override
	public boolean voidIt() {

		String valid = ModelValidationEngine.get().fireDocValidate(this,
				ModelValidator.TIMING_BEFORE_VOID);
		if (valid != null) {
			m_processMsg = valid;
			return false;
		}
		// put logic here
		setProcessed(true);
		setDocAction(DocAction.ACTION_None);
		setDocStatus(DOCSTATUS_Voided);
		saveEx();

		// User Validation
		valid = ModelValidationEngine.get().fireDocValidate(this,
				ModelValidator.TIMING_AFTER_VOID);
		if (valid != null) {
			m_processMsg = valid;
			return false;
		}

		return true;
	}

	@Override
	public boolean closeIt() {
		return false;
	}

	@Override
	public boolean reverseCorrectIt() {
		return false;
	}

	@Override
	public boolean reverseAccrualIt() {
		return false;
	}

	@Override
	public boolean reActivateIt() {
		
		setProcessed(false);
		setDocAction(DocAction.ACTION_Complete);
		setDocStatus(MQuotation.DOCSTATUS_InProgress);
		MQuotationLine[] lines = getLines();
		for(MQuotationLine line: lines) {
			line.setProcessed(false);
			line.saveEx();
		}
		
		saveEx();
		
		return true;
		
	}

	@Override
	public String getSummary() {
		StringBuilder sb = new StringBuilder();
		sb.append(getDocumentNo());
		// - Description
		if (getDescription() != null && getDescription().length() > 0)
			sb.append(" : ").append(getDescription());
		return sb.toString();
	}

	@Override
	public String getDocumentInfo() {

		return null;
	}

	@Override
	public File createPDF() {
		try {
			StringBuilder msgfile = new StringBuilder().append(get_TableName())
					.append(get_ID()).append("_");
			File temp = File.createTempFile(msgfile.toString(), ".pdf");
			return createPDF(temp);
		} catch (Exception e) {
			log.severe("Could not create PDF - " + e.getMessage());
		}
		return null;
	}

	/**
	 * Create PDF file
	 *
	 * @param file
	 *            output file
	 * @return file if success
	 */
	public File createPDF(File file) {
		format = null;
		// TODO: Where to get format from
		// We have a Jasper Print Format
		// ==============================
		if (format.getJasperProcess_ID() == 0) {
			ProcessInfo pi = new ProcessInfo("", format.getJasperProcess_ID());
			pi.setRecord_ID(getC_Quotation_ID());
			pi.setIsBatch(true);

			ServerProcessCtl.process(pi, null);

			return pi.getPDFReport();
		}
		// Standard Print Format (Non-Jasper)
		// ==================================
		return null;
	} // createPDF

	@Override
	public String getProcessMsg() {
		return m_processMsg;
	}

	@Override
	public int getDoc_User_ID() {
		return getCreatedBy();
	}

	@Override
	public BigDecimal getApprovalAmt() {
		return getTotalLines();
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
			options[index++] = DocAction.ACTION_Void;
		} else if (docStatus.equals(DocAction.STATUS_InProgress)) {
			options[index++] = DocAction.ACTION_Complete;
			options[index++] = DocAction.ACTION_Void;
		} else if (docStatus.equals(DocAction.STATUS_Completed)) {
			options[index++] = DocAction.ACTION_Void;
			options[index++] = DocAction.ACTION_ReActivate;
		} else if (docStatus.equals(DocAction.STATUS_Invalid)) {
			options[index++] = DocAction.ACTION_Complete;
			options[index++] = DocAction.ACTION_Void;
		}

		return index;

	}
	
	//Stephan
	public int getPrecision()
	{
		return MCurrency.getStdPrecision(getCtx(), getC_Currency_ID());
	}
	
	protected boolean beforeSave (boolean newRecord)
	{
		//	Client/Org Check
		if (getAD_Org_ID() == 0)
		{
			int context_AD_Org_ID = Env.getAD_Org_ID(getCtx());
			if (context_AD_Org_ID != 0)
			{
				setAD_Org_ID(context_AD_Org_ID);
				log.warning("Changed Org to Context=" + context_AD_Org_ID);
			}
		}
		if (getAD_Client_ID() == 0)
		{
			m_processMsg = "AD_Client_ID = 0";
			return false;
		}
		
		//	New Record Doc Type - make sure DocType set to 0
		if (newRecord && getC_DocType_ID() == 0)
			setC_DocType_ID (0);

		//	Default Warehouse
		if (getM_Warehouse_ID() == 0)
		{
			int ii = Env.getContextAsInt(getCtx(), "#M_Warehouse_ID");
			if (ii != 0)
				setM_Warehouse_ID(ii);
			else
			{
				throw new FillMandatoryException(COLUMNNAME_M_Warehouse_ID);
			}
		}
		MWarehouse wh = MWarehouse.get(getCtx(), getM_Warehouse_ID());
		//	Warehouse Org
		if (newRecord 
			|| is_ValueChanged("AD_Org_ID") || is_ValueChanged("M_Warehouse_ID"))
		{
			if (wh.getAD_Org_ID() != getAD_Org_ID())
				log.saveWarning("WarehouseOrgConflict", "");
		}

		boolean disallowNegInv = wh.isDisallowNegativeInv();
		String DeliveryRule = getDeliveryRule();
		if((disallowNegInv && DELIVERYRULE_Force.equals(DeliveryRule)) ||
				(DeliveryRule == null || DeliveryRule.length()==0))
			setDeliveryRule(DELIVERYRULE_Availability);
		
		//	No Partner Info - set Template
		if (getC_BPartner_ID() == 0)
			setBPartner(MBPartner.getTemplate(getCtx(), getAD_Client_ID()));
		if (getC_BPartner_Location_ID() == 0)
			setBPartner(new MBPartner(getCtx(), getC_BPartner_ID(), null));
		//	No Bill - get from Ship
		if (getBill_BPartner_ID() == 0)
		{
			setBill_BPartner_ID(getC_BPartner_ID());
			setBill_Location_ID(getC_BPartner_Location_ID());
		}
		if (getBill_Location_ID() == 0)
			setBill_Location_ID(getC_BPartner_Location_ID());

		//	Default Price List
		if (getM_PriceList_ID() == 0)
		{
			int ii = DB.getSQLValueEx(null,
				"SELECT M_PriceList_ID FROM M_PriceList "
				+ "WHERE AD_Client_ID=? AND IsSOPriceList=? AND IsActive=?"
				+ "ORDER BY IsDefault DESC", getAD_Client_ID(), isSOTrx(), true);
			if (ii != 0)
				setM_PriceList_ID (ii);
		}
		//	Default Currency
		if (getC_Currency_ID() == 0)
		{
			String sql = "SELECT C_Currency_ID FROM M_PriceList WHERE M_PriceList_ID=?";
			int ii = DB.getSQLValue (null, sql, getM_PriceList_ID());
			if (ii != 0)
				setC_Currency_ID (ii);
			else
				setC_Currency_ID(Env.getContextAsInt(getCtx(), "#C_Currency_ID"));
		}

		//	Default Sales Rep
		if (getSalesRep_ID() == 0)
		{
			int ii = Env.getContextAsInt(getCtx(), "#SalesRep_ID");
			if (ii != 0)
				setSalesRep_ID (ii);
		}

		//	Default Payment Term
		if (getC_PaymentTerm_ID() == 0)
		{
			int ii = Env.getContextAsInt(getCtx(), "#C_PaymentTerm_ID");
			if (ii != 0)
				setC_PaymentTerm_ID(ii);
			else
			{
				String sql = "SELECT C_PaymentTerm_ID FROM C_PaymentTerm WHERE AD_Client_ID=? AND IsDefault='Y'";
				ii = DB.getSQLValue(null, sql, getAD_Client_ID());
				if (ii != 0)
					setC_PaymentTerm_ID (ii);
			}
		}

		// IDEMPIERE-1597 Price List and Date must be not-updateable
		if (!newRecord && (is_ValueChanged(COLUMNNAME_M_PriceList_ID) || is_ValueChanged(COLUMNNAME_DateOrdered))) {
			int cnt = DB.getSQLValueEx(get_TrxName(), "SELECT COUNT(*) FROM C_QuotationLine WHERE C_Quotation_ID=? AND M_Product_ID>0", getC_Quotation_ID());
			if (cnt > 0) {
				if (is_ValueChanged(COLUMNNAME_M_PriceList_ID)) {
					log.saveError("Error", Msg.getMsg(getCtx(), "CannotChangePl"));
					return false;
				}
				if (is_ValueChanged(COLUMNNAME_DateOrdered)) {
					MPriceList pList =  MPriceList.get(getCtx(), getM_PriceList_ID(), null);
					MPriceListVersion plOld = pList.getPriceListVersion((Timestamp)get_ValueOld(COLUMNNAME_DateOrdered));
					MPriceListVersion plNew = pList.getPriceListVersion((Timestamp)get_Value(COLUMNNAME_DateOrdered));
					if (plNew == null || !plNew.equals(plOld)) {
						log.saveError("Error", Msg.getMsg(getCtx(), "CannotChangeDateOrdered"));
						return false;
					}
				}
			}
		}

		return true;
	}	//	beforeSave

	@Override
	protected boolean beforeDelete()
	{
		String sql = "DELETE FROM M_MatchQuotation WHERE C_Quotation_ID="+get_ID();
		DB.executeUpdate(sql, get_TrxName());
		
		String sql2 = "DELETE FROM M_MatchRequest WHERE C_Quotation_ID="+get_ID();
		DB.executeUpdate(sql2, get_TrxName());
		
		String sql3 = "DELETE FROM C_QuotationTax WHERE C_Quotation_ID="+get_ID();
		DB.executeUpdate(sql3, get_TrxName());
		
		String sql4 = "DELETE FROM C_QuotationLine WHERE C_Quotation_ID="+get_ID();
		DB.executeUpdate(sql4, get_TrxName());
		
		return true;
	}
	
	public void setBPartner (MBPartner bp)
	{
		if (bp == null)
			return;

		setC_BPartner_ID(bp.getC_BPartner_ID());
		//	Defaults Payment Term
		int ii = 0;
		if (isSOTrx())
			ii = bp.getC_PaymentTerm_ID();
		else
			ii = bp.getPO_PaymentTerm_ID();
		if (ii != 0)
			setC_PaymentTerm_ID(ii);
		//	Default Price List
		if (isSOTrx())
			ii = bp.getM_PriceList_ID();
		else
			ii = bp.getPO_PriceList_ID();
		if (ii != 0)
			setM_PriceList_ID(ii);
		//	Default Delivery/Via Rule
		String ss = bp.getDeliveryRule();
		if (ss != null)
			setDeliveryRule(ss);
		ss = bp.getDeliveryViaRule();
		if (ss != null)
			setDeliveryViaRule(ss);
		//	Default Invoice/Payment Rule
		ss = bp.getInvoiceRule();
		if (ss != null)
			setInvoiceRule(ss);
		ss = bp.getPaymentRule();
		if (ss != null)
			setPaymentRule(ss);
		//	Sales Rep
		ii = bp.getSalesRep_ID();
		if (ii != 0)
			setSalesRep_ID(ii);


		//	Set Locations
		MBPartnerLocation[] locs = bp.getLocations(false);
		if (locs != null)
		{
			for (int i = 0; i < locs.length; i++)
			{
				if (locs[i].isShipTo())
					super.setC_BPartner_Location_ID(locs[i].getC_BPartner_Location_ID());
				if (locs[i].isBillTo())
					setBill_Location_ID(locs[i].getC_BPartner_Location_ID());
			}
			//	set to first
			if (getC_BPartner_Location_ID() == 0 && locs.length > 0)
				super.setC_BPartner_Location_ID(locs[0].getC_BPartner_Location_ID());
			if (getBill_Location_ID() == 0 && locs.length > 0)
				setBill_Location_ID(locs[0].getC_BPartner_Location_ID());
		}
		if (getC_BPartner_Location_ID() == 0)
		{	
			throw new BPartnerNoShipToAddressException(bp);
		}	
			
		if (getBill_Location_ID() == 0)
		{
			throw new BPartnerNoBillToAddressException(bp);
		}	

		//	Set Contact
		MUser[] contacts = bp.getContacts(false);
		if (contacts != null && contacts.length == 1)
			setAD_User_ID(contacts[0].getAD_User_ID());
				
	}	//	setBPartner
	
	/**
	 * 	Set Processed.
	 * 	Propagate to Lines/Taxes
	 *	@param processed processed
	 */
	public void setProcessed (boolean processed)
	{
		super.setProcessed (processed);
		if (get_ID() == 0)
			return;
		String set = "SET Processed='"
			+ (processed ? "Y" : "N")
			+ "' WHERE C_Quotation_ID=" + getC_Quotation_ID();
		int noLine = DB.executeUpdateEx("UPDATE C_QuotationLine " + set, get_TrxName());
		int noTax = DB.executeUpdateEx("UPDATE C_QuotationTax " + set, get_TrxName());
		if (log.isLoggable(Level.FINE)) log.fine("setProcessed - " + processed + " - Lines=" + noLine + ", Tax=" + noTax);
	}	//	setProcessed

	public static MQuotation copyFrom(MQuotation from, String trxName) {
		MQuotation to = new MQuotation (from.getCtx(), 0, trxName);
		to.set_TrxName(trxName);
		PO.copyValues(from, to, from.getAD_Client_ID(), from.getAD_Org_ID());
		to.set_ValueNoCheck ("C_Quotation_ID", I_ZERO);
		to.set_ValueNoCheck ("DocumentNo", from.getDocumentNo()+"R");
		to.setC_BPartner_ID(from.getC_BPartner_ID());
		to.setC_BPartner_Location_ID(from.getC_BPartner_Location_ID());
		to.setAD_User_ID(from.getAD_User_ID());
		to.setBill_BPartner_ID(from.getBill_BPartner_ID());
		to.setBill_Location_ID(from.getBill_Location_ID());
		to.setBill_User_ID(from.getBill_User_ID());
		to.setC_Activity_ID(from.getC_Activity_ID());
		to.setC_Campaign_ID(from.getC_Campaign_ID());
		to.setC_Currency_ID(from.getC_Currency_ID());
		to.setC_ConversionType_ID(from.getC_ConversionType_ID());
		to.setC_DocType_ID(from.getC_DocType_ID());
		to.setC_DocTypeTarget_ID(from.getC_DocTypeTarget_ID());
		to.setC_PaymentTerm_ID(from.getC_PaymentTerm_ID());
		to.setC_Project_ID(from.getC_Project_ID());
		to.setDateAcct(from.getDateAcct());
		to.setDateOrdered(from.getDateOrdered());
		to.setDatePromised(from.getDatePromised());
		to.setDaysDue(from.getDaysDue());
		to.setDescription(from.getDescription());
		to.setAD_OrgTrx_ID(from.getAD_OrgTrx_ID());
		to.setDeliveryRule(from.getDeliveryRule());
		to.setDeliveryViaRule(from.getDeliveryViaRule());
		to.setDocAction(from.getDocAction());
		to.setDocStatus(from.getDocStatus());
		to.setM_PriceList_ID(from.getM_PriceList_ID());
		to.setSalesRep_ID(from.getSalesRep_ID());
		to.setFreightAmt(from.getFreightAmt());
		to.setFreightCostRule(from.getFreightCostRule());
		to.setGrandTotal(from.getGrandTotal());
		to.setInvoiceRule(from.getInvoiceRule());
		to.setIsQuotationAccepted(false);
		to.setIsApproved(from.isApproved());
		to.setIsSOTrx(from.isSOTrx());
		to.setM_Shipper_ID(from.getM_Shipper_ID());
		to.setM_Warehouse_ID(from.getM_Warehouse_ID());
		to.setPaymentRule(from.getPaymentRule());
		to.setPOReference(from.getPOReference());
		to.setPriorityRule(from.getPriorityRule());
		to.setTotalLines(from.getTotalLines());
		to.setUser1_ID(from.getUser1_ID());
		to.setUser2_ID(from.getUser2_ID());
		to.setProcessed(true);
		to.setProcessing(false);		
		if (!to.save(trxName))
			throw new IllegalStateException("Could not create Quotation");
	
		//
		//
		//
		
		if (to.copyLinesFrom(from) == 0)
			throw new IllegalStateException("Could not create Quotation Lines");

		return to;
	}	//	copyFrom
	
	public int copyLinesFrom (MQuotation otherQuote)
	{
		if (otherQuote == null)
			return 0;
		MQuotationLine[] fromLines = otherQuote.getLines();
		int count = 0;
		for (MQuotationLine fromLine: fromLines)
		{
			MQuotationLine line = new MQuotationLine (this);
			PO.copyValues(fromLine, line, getAD_Client_ID(), getAD_Org_ID());
			line.setC_Quotation_ID(getC_Quotation_ID());
			line.set_ValueNoCheck ("C_QuotationLine_ID", null);	
			line.setAD_OrgTrx_ID(fromLine.getAD_OrgTrx_ID());
			line.setC_Activity_ID(fromLine.getC_Activity_ID());
			line.setC_BPartner_ID(fromLine.getC_BPartner_ID());
			line.setC_BPartner_Location_ID(fromLine.getC_BPartner_Location_ID());
			line.setC_Campaign_ID(fromLine.getC_Campaign_ID());
			line.setC_Charge_ID(fromLine.getC_Charge_ID());
			line.setC_Project_ID(fromLine.getC_Project_ID());
			line.setC_ProjectPhase_ID(fromLine.getC_ProjectPhase_ID());
			line.setC_ProjectTask_ID(fromLine.getC_ProjectTask_ID());
			line.setC_Tax_ID(fromLine.getC_Tax_ID());
			line.setC_UOM_ID(fromLine.getC_UOM_ID());
			line.setDateOrdered(fromLine.getDateOrdered());
			line.setDatePromised(fromLine.getDatePromised());
			line.setDiscount(fromLine.getDiscount());
			line.setDescription(fromLine.getDescription());
			line.setLine(fromLine.getLine());
			line.setFreightAmt(fromLine.getFreightAmt());
			line.setLineNetAmt(fromLine.getLineNetAmt());
			line.setM_Shipper_ID(fromLine.getM_Shipper_ID());
			line.setM_Warehouse_ID(fromLine.getM_Warehouse_ID());
			line.setM_Product_ID(fromLine.getM_Product_ID());
			line.set_ValueNoCheck ("M_Product_Category_ID", fromLine.get_ValueAsInt("M_Product_Category_ID"));	
			line.setProduct(fromLine.getMProduct());
			line.setPriceActual(fromLine.getPriceActual());
			line.setPriceEntered(fromLine.getPriceEntered());
			line.setPrice(fromLine.getPrice());
			line.setPriceActual(fromLine.getPriceActual());
			line.setPriceCost(fromLine.getPriceCost());
			line.setPriceList(fromLine.getPriceList());
			line.setPriceLimit(fromLine.getPriceLimit());
			line.setQtyEntered(fromLine.getQtyEntered());
			line.setQtyOrdered(fromLine.getQtyOrdered());
			line.set_ValueNoCheck ("Size", fromLine.get_ValueAsString("Size"));	//	new
			line.setUser1_ID(fromLine.getUser1_ID());
			line.setUser2_ID(fromLine.getUser2_ID());
			//
			
			line.setProcessed(true);
			if (line.save(get_TrxName()))
				count++;
		}
		if (fromLines.length != count)
			log.log(Level.SEVERE, "Line difference - From=" + fromLines.length + " <> Saved=" + count);
		return count;
	}	//	copyLinesFrom

	public MQuotationTax[] getTaxes(boolean requery)
	{
		List<MQuotationTax> list = new Query(getCtx(), MQuotationTax.Table_Name, "C_Quotation_ID=?", get_TrxName())
									.setParameters(get_ID())
									.list();
		MQuotationTax[] taxes = list.toArray(new MQuotationTax[list.size()]);
		return taxes;
	}	//	getTaxes
	
}
