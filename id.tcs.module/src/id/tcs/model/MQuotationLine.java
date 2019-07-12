package id.tcs.model;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.util.Properties;
import java.util.logging.Level;

import org.adempiere.base.Core;
import org.adempiere.exceptions.AdempiereException;
import org.adempiere.model.ITaxProvider;
import org.compiere.model.MCharge;
import org.compiere.model.MCurrency;
import org.compiere.model.MPriceList;
import org.compiere.model.MProduct;
import org.compiere.model.MTax;
import org.compiere.model.MTaxCategory;
import org.compiere.model.MTaxProvider;
import org.compiere.model.MUOM;
import org.compiere.model.MUOMConversion;
import org.compiere.model.Tax;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;

public class MQuotationLine extends X_C_QuotationLine {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3799656970244780117L;

	/**
	 * @author edwinang
	 * @param ctx
	 *            is the context variable
	 * @param C_QuotationLine_ID
	 * @param trxName
	 */
	public MQuotationLine(Properties ctx, int C_QuotationLine_ID, String trxName) {
		super(ctx, C_QuotationLine_ID, trxName);
	}

	/**
	 * 
	 * @param ctx
	 * @param rs
	 * @param trxName
	 */
	public MQuotationLine(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** Tax							*/
	private MTax 		m_tax = null;

	/**	Product					*/
	private MProduct 		m_product = null;
	/**	Charge					*/
	private MCharge 		m_charge = null;

	/**
	 * 
	 * @param quotation
	 */
	public MQuotationLine(MQuotation quotation) {
		this(quotation.getCtx(), 0, quotation.get_TrxName());
		if (quotation.get_ID() == 0)
			throw new IllegalArgumentException("Header not saved");
		setClientOrg(quotation);
		setC_Quotation_ID(quotation.getC_Quotation_ID());
		setC_BPartner_ID(quotation.getC_BPartner_ID());
		setC_BPartner_Location_ID(quotation.getC_BPartner_Location_ID());
		setC_Currency_ID(quotation.getC_Currency_ID());
		// parent

	}

	public int getC_Tax_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_Tax_ID);
		if (ii == null)
			return 0;
		return ii.intValue();
	}

	/**
	 * 	Set Product
	 *	@param product product
	 */
	public void setProduct (MProduct product)
	{
		m_product = product;
		if (m_product != null)
		{
			setM_Product_ID(m_product.getM_Product_ID());
			setC_UOM_ID (m_product.getC_UOM_ID());
		}
		else
		{
			setM_Product_ID(0);
			set_ValueNoCheck ("C_UOM_ID", null);
		}
		setM_AttributeSetInstance_ID(0);
	}	//	setProduct


	protected boolean afterSave(boolean newRecord, boolean success){
		if(!success){
			return success;
		}
		MTax tax = new MTax(getCtx(), getC_Tax_ID(), get_TrxName());
		MTaxProvider provider = new MTaxProvider(tax.getCtx(), tax.getC_TaxProvider_ID(), tax.get_TrxName());
		//Temp Comment to use custom tax provider
		//ITaxProvider calc = Core.getTaxProvider(provider);
		TCS_TaxProvider calc = new TCS_TaxProvider();
		if(calc == null){
			throw new AdempiereException(Msg.getMsg(getCtx(), "TaxNoProvider"));
		}
		return calc.recalculateTax(provider, this, newRecord);
	}//afterSave

	protected boolean beforeSave(boolean newRecord) {
		// Get Line No
		if (getLine() == 0) {
			String sql = "SELECT COALESCE(MAX(Line),0)+10 FROM C_QuotationLine WHERE C_Quotation_ID=?";
			int ii = DB.getSQLValue(get_TrxName(), sql, getC_Quotation_ID());
			setLine(ii);
		}

		//@win - recalculate multi uom on 
		if (newRecord || is_ValueChanged(COLUMNNAME_C_UOM_ID) || 
				is_ValueChanged(COLUMNNAME_M_Product_ID) || is_ValueChanged(COLUMNNAME_QtyEntered)) {
			BigDecimal qtyEntered = getQtyEntered();
			int p_C_UOM_ID = getC_UOM_ID();
			BigDecimal qtyEntered1 = qtyEntered.setScale(MUOM.getPrecision(getCtx(), p_C_UOM_ID), BigDecimal.ROUND_HALF_UP);
			if (qtyEntered.compareTo(qtyEntered1) != 0)
			{
				qtyEntered = qtyEntered1;
				setQtyEntered(qtyEntered);
			}

			BigDecimal qtyOrdered = MUOMConversion.convertProductFrom (getCtx(), getM_Product_ID(),
					p_C_UOM_ID, qtyEntered);

			if (qtyOrdered == null)
				qtyOrdered = qtyEntered;

			if (getQtyOrdered().compareTo(qtyOrdered) != 0)
				setQtyOrdered(qtyOrdered);
		}

		//end @win - recalculate multi uom


		//	Charge
		if (getC_Charge_ID() != 0 && getM_Product_ID() != 0)
			setM_Product_ID(0);
		//	No Product
		if (getM_Product_ID() == 0)
			setM_AttributeSetInstance_ID(0);
		//	Product
		else	//	Set/check Product Price
		{

		}

		//	UOM
		if (getC_UOM_ID() == 0 
				&& (getM_Product_ID() != 0 
				|| getPriceEntered().compareTo(Env.ZERO) != 0
				|| getC_Charge_ID() != 0))
		{
			int C_UOM_ID = MUOM.getDefault_UOM_ID(getCtx());
			if (C_UOM_ID > 0)
				setC_UOM_ID (C_UOM_ID);
		}
		//	Qty Precision
		if (newRecord || is_ValueChanged("QtyEntered"))
			setQtyEntered(getQtyEntered());
		if (newRecord || is_ValueChanged("QtyOrdered"))
			setQtyOrdered(getQtyOrdered());

		//	FreightAmt Not used
		if (Env.ZERO.compareTo(getFreightAmt()) != 0)
			setFreightAmt(Env.ZERO);

		//	Set Tax
		if (getC_Tax_ID() == 0)
			setTax();

		//	Calculations & Rounding
		setLineNetAmt();	//	extended Amount with or without tax
		setDiscount();

		/* Carlos Ruiz - globalqss
		 * IDEMPIERE-178 Orders and Invoices must disallow amount lines without product/charge
		 */
		if (getParent().getC_DocTypeTarget().isChargeOrProductMandatory()) {
			if (getC_Charge_ID() == 0 && getM_Product_ID() == 0 && getPriceEntered().signum() != 0) {
				log.saveError("FillMandatory", Msg.translate(getCtx(), "ChargeOrProductMandatory"));
				return false;
			}
		}

		/*
		 *	stephan
		 *	TAOWI-897 check tax in quotation line must be same with tax header 
		 */
		if(getParent().getC_Tax_ID() == 0)
			return true;
		else if(newRecord && getParent().getC_Tax_ID() != getC_Tax_ID())
			setC_Tax_ID(getParent().getC_Tax_ID());

		return true;
	}//beforeSave

	@Override
	protected boolean beforeDelete()
	{
		String sql = "DELETE FROM M_MatchQuotation WHERE C_QuotationLine_ID="+get_ID();
		DB.executeUpdate(sql, get_TrxName());
		
		String sql2 = "DELETE FROM M_MatchRequest WHERE C_QuotationLine_ID="+get_ID();
		DB.executeUpdate(sql2, get_TrxName());
		
		return true;
	}
	
	/**
	 * 	After Delete
	 *	@param success success
	 *	@return deleted
	 */
	protected boolean afterDelete (boolean success)
	{
		if (!success)
			return success;
		
		return updateHeaderTax();
	}	//	afterDelete
	
	/**
	 *	Set Discount
	 */
	//	public void setDiscount()
	//	{
	//		BigDecimal list = getPriceList();
	//		//	No List Price
	//		if (Env.ZERO.compareTo(list) == 0)
	//			return;
	//		BigDecimal discount = list.subtract(getPriceActual())
	//			.multiply(Env.ONEHUNDRED)
	//			.divide(list, getPrecision(), BigDecimal.ROUND_HALF_UP);
	//		setDiscount(discount);
	//	}	//	setDiscount

	/**
	 * 	Get Tax
	 *	@return tax
	 */
	protected MTax getTax()
	{
		if (m_tax == null)
			m_tax = MTax.get(getCtx(), getC_Tax_ID());
		return m_tax;
	}	//	getTax


	/**
	 * 	Get Product
	 *	@return product or null
	 */
	public MProduct getMProduct()
	{
		if (m_product == null && getM_Product_ID() != 0)
			m_product =  MProduct.get (getCtx(), getM_Product_ID());
		return m_product;
	}	//	getProduct

	/**
	 * 	Get Charge
	 *	@return product or null
	 */
	public MCharge getCharge()
	{
		if (m_charge == null && getC_Charge_ID() != 0)
			m_charge =  MCharge.get (getCtx(), getC_Charge_ID());
		return m_charge;
	}
	/**
	 * 	Calculate Extended Amt.
	 * 	May or may not include tax
	 */
	//	public void setLineNetAmt ()
	//	{
	//		BigDecimal bd = getPriceActual().multiply(getQtyOrdered()); 
	//		
	//		boolean documentLevel = getTax().isDocumentLevel();
	//		
	//		//	juddm: Tax Exempt & Tax Included in Price List & not Document Level - Adjust Line Amount
	//		//  http://sourceforge.net/tracker/index.php?func=detail&aid=1733602&group_id=176962&atid=879332
	//		if (isTaxIncluded() && !documentLevel)	{
	//			BigDecimal taxStdAmt = Env.ZERO, taxThisAmt = Env.ZERO;
	//			
	//			MTax orderTax = getTax();
	//			MTax stdTax = null;
	//			
	//			//	get the standard tax
	//			if (getProduct() == null)
	//			{
	//				if (getCharge() != null)	// Charge 
	//				{
	//					stdTax = new MTax (getCtx(), 
	//							((MTaxCategory) getCharge().getC_TaxCategory()).getDefaultTax().getC_Tax_ID(),
	//							get_TrxName());
	//				}
	//					
	//			}
	//			else	// Product
	//				stdTax = new MTax (getCtx(), 
	//							((MTaxCategory) getProduct().getC_TaxCategory()).getDefaultTax().getC_Tax_ID(), 
	//							get_TrxName());
	//
	//			if (stdTax != null)
	//			{
	//				if (log.isLoggable(Level.FINE)){
	//					log.fine("stdTax rate is " + stdTax.getRate());
	//					log.fine("orderTax rate is " + orderTax.getRate());
	//				}
	//								
	//				taxThisAmt = taxThisAmt.add(orderTax.calculateTax(bd, isTaxIncluded(), getPrecision()));
	//				taxStdAmt = taxStdAmt.add(stdTax.calculateTax(bd, isTaxIncluded(), getPrecision()));
	//				
	//				bd = bd.subtract(taxStdAmt).add(taxThisAmt);
	//				
	//				if (log.isLoggable(Level.FINE)) log.fine("Price List includes Tax and Tax Changed on Order Line: New Tax Amt: " 
	//						+ taxThisAmt + " Standard Tax Amt: " + taxStdAmt + " Line Net Amt: " + bd);	
	//			}
	//			
	//		}
	//		int precision = getPrecision();
	//		if (bd.scale() > precision)
	//			bd = bd.setScale(precision, BigDecimal.ROUND_HALF_UP);
	//		super.setLineNetAmt (bd);
	//	}	//	setLineNetAmt

	/**
	 *	Set Tax
	 *	@return true if tax is set
	 */
	public boolean setTax()
	{
		int ii = Tax.get(getCtx(), getM_Product_ID(), getC_Charge_ID(), getDateOrdered(), getDateOrdered(),
				getAD_Org_ID(), getM_Warehouse_ID(),
				getC_BPartner_Location_ID(),		//	should be bill to
				getC_BPartner_Location_ID(), true, get_TrxName());
		if (ii == 0)
		{
			log.log(Level.SEVERE, "No Tax found");
			return false;
		}
		setC_Tax_ID (ii);
		return true;
	}	//	setTax


	private MQuotation m_parent = null;
	public MQuotation getParent()
	{
		if (m_parent == null)
			m_parent = new MQuotation(getCtx(), getC_Quotation_ID(), get_TrxName());
		return m_parent;
	}

	/**
	 * Recalculate order tax
	 * @param oldTax true if the old C_Tax_ID should be used
	 * @return true if success, false otherwise
	 */
	public boolean updateQuotationTax(boolean oldTax) {
		MQuotationTax tax = MQuotationTax.get(this, getPrecision(), oldTax, get_TrxName());

		if (tax != null) {
			if (!tax.calculateTaxFromLines())
				return false;
			if (tax.getTaxAmt().signum() != 0) {
				if (!tax.save(get_TrxName()))
					return false;
			}
			else {
				if (!tax.is_new() && !tax.delete(false, get_TrxName()))
					return false;
			}
		}
		return true;
	}//updateOrderTax

	private Integer m_precision = null;
	private int m_M_PriceList_ID = 0;

	//Stephan
	public int getPrecision()
	{		
		if (m_precision != null)
			return m_precision.intValue();
		//
		if (getC_Currency_ID() == 0)
		{
			setQuotation(getParent());
			if (m_precision != null)
				return m_precision.intValue();
		}
		if (getC_Currency_ID() != 0)
		{
			MCurrency cur = MCurrency.get(getCtx(), getC_Currency_ID());
			if (cur.get_ID() != 0)
			{
				m_precision = new Integer (cur.getStdPrecision());
				return m_precision.intValue();
			}
		}

		String sql = "SELECT c.StdPrecision "
				+ "FROM C_Currency c INNER JOIN C_Quotation x ON (x.C_Currency_ID=c.C_Currency_ID) "
				+ "WHERE x.C_Quotation_ID=?";
		int i = DB.getSQLValue(get_TrxName(), sql, getC_Quotation_ID());
		m_precision = new Integer(i);
		return m_precision.intValue();
	}//getPrecision

	/**
	 * 	Set Defaults from Quotation
	 * 	Does not set Parent !!
	 * 	@param MQuotation
	 */
	public void setQuotation (MQuotation quotation)
	{
		setClientOrg(quotation);
		setC_BPartner_ID(quotation.getC_BPartner_ID());
		setC_BPartner_Location_ID(quotation.getC_BPartner_Location_ID());
		setM_Warehouse_ID(quotation.getM_Warehouse_ID());
		setDateOrdered(quotation.getDateOrdered());
		setDatePromised(quotation.getDatePromised());
		setC_Currency_ID(quotation.getC_Currency_ID());
		setHeaderInfo(quotation);
	}//setQuotation

	/**
	 * 	Set Header Info
	 *	@param order order
	 */
	public void setHeaderInfo (MQuotation quotation)
	{
		m_parent = quotation;
		m_precision = new Integer(quotation.getPrecision());
		m_M_PriceList_ID = quotation.getM_PriceList_ID();
		quotation.isSOTrx();
	}//setHeaderInfo

	/**
	 *	Is Tax Included in Amount
	 *	@return true if tax calculated
	 */
	public boolean isTaxIncluded()
	{
		if (m_M_PriceList_ID == 0)
		{
			m_M_PriceList_ID = DB.getSQLValue(get_TrxName(),
					"SELECT M_PriceList_ID FROM C_Quotation WHERE C_Quotation_ID=?",
					getC_Quotation_ID());
		}
		MPriceList pl = MPriceList.get(getCtx(), m_M_PriceList_ID, get_TrxName());
		return pl.isTaxIncluded();
	}//isTaxIncluded


	/**
	 *	Update Tax & Header
	 *	@return true if header updated
	 */
	public boolean updateHeaderTax(){
		if(isProcessed() && !is_ValueChanged(COLUMNNAME_Processed)){
			return true;
		}

		MTax tax = new MTax(getCtx(), getC_Tax_ID(), get_TrxName());
		MTaxProvider provider = new MTaxProvider(tax.getCtx(), tax.getC_TaxProvider_ID(), tax.get_TrxName());
		//Temp Comment to use custom tax provider
		//ITaxProvider calc = Core.getTaxProvider(provider);
		TCS_TaxProvider calc = new TCS_TaxProvider();
		if(calc == null){
			throw new AdempiereException(Msg.getMsg(getCtx(), "TaxNoProvider"));
		}
		if(!calc.updateQuotationTax(provider, this)){
			return false;
		}

		return calc.updateHeaderTax(provider, this);
	}//updateHeaderTax

	public void clearParent()
	{
		this.m_parent = null;
	}//clearParent

	/**
	 * 	Calculate Extended Amt.
	 * 	May or may not include tax
	 */
	public void setLineNetAmt ()
	{
		BigDecimal bd = getPriceActual().multiply(getQtyOrdered()); 

		boolean documentLevel = getTax().isDocumentLevel();

		//	juddm: Tax Exempt & Tax Included in Price List & not Document Level - Adjust Line Amount
		//  http://sourceforge.net/tracker/index.php?func=detail&aid=1733602&group_id=176962&atid=879332
		if (isTaxIncluded() && !documentLevel)	{
			BigDecimal taxStdAmt = Env.ZERO, taxThisAmt = Env.ZERO;

			MTax orderTax = getTax();
			MTax stdTax = null;

			//	get the standard tax
			if (getMProduct() == null)
			{
				if (getCharge() != null)	// Charge 
				{
					stdTax = new MTax (getCtx(), 
							((MTaxCategory) getCharge().getC_TaxCategory()).getDefaultTax().getC_Tax_ID(),
							get_TrxName());
				}

			}
			else	// Product
				stdTax = new MTax (getCtx(), 
						((MTaxCategory) getMProduct().getC_TaxCategory()).getDefaultTax().getC_Tax_ID(), 
						get_TrxName());

			if (stdTax != null)
			{
				if (log.isLoggable(Level.FINE)){
					log.fine("stdTax rate is " + stdTax.getRate());
					log.fine("orderTax rate is " + orderTax.getRate());
				}

				taxThisAmt = taxThisAmt.add(orderTax.calculateTax(bd, isTaxIncluded(), getPrecision()));
				taxStdAmt = taxStdAmt.add(stdTax.calculateTax(bd, isTaxIncluded(), getPrecision()));

				bd = bd.subtract(taxStdAmt).add(taxThisAmt);

				if (log.isLoggable(Level.FINE)) log.fine("Price List includes Tax and Tax Changed on Order Line: New Tax Amt: " 
						+ taxThisAmt + " Standard Tax Amt: " + taxStdAmt + " Line Net Amt: " + bd);	
			}

		}
		int precision = getPrecision();
		if (bd.scale() > precision)
			bd = bd.setScale(precision, BigDecimal.ROUND_HALF_UP);
		super.setLineNetAmt (bd);
	}	//	setLineNetAmt

	/**
	 *	Set Discount
	 */
	public void setDiscount()
	{
		BigDecimal list = getPriceList();
		//	No List Price
		if (Env.ZERO.compareTo(list) == 0)
			return;
		BigDecimal discount = list.subtract(getPriceActual())
				.multiply(Env.ONEHUNDRED)
				.divide(list, getPrecision(), BigDecimal.ROUND_HALF_UP);
		setDiscount(discount);
		//@tegar set discount 0 if negate
				if (discount.compareTo(Env.ZERO)<0){
					setDiscount(Env.ZERO);
				}
		
	}	//	setDiscount

}
