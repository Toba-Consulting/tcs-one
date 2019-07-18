package id.tcs.model;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;
import java.util.logging.Level;

import org.compiere.model.MOrderTax;
import org.compiere.model.MTax;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;

public class MQuotationTax extends X_C_QuotationTax{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5544256017125785897L;
	
	public MQuotationTax(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	public MQuotationTax(Properties ctx, int C_QuotationTax_ID, String trxName) {
		super(ctx, C_QuotationTax_ID, trxName);
	}

	private static CLogger	s_log	= CLogger.getCLogger (MOrderTax.class);
	//private int m_precision = 0;
	
	//Stephan
	protected void setPrecision (int precision)
	{
		m_precision = new Integer(precision);
	}
	
	//Stephan
	public static MQuotationTax get(MQuotationLine line, int precision, boolean oldTax, String trxName){
		MQuotationTax retValue = null;
		if(line == null || line.getC_Quotation_ID()==0){
			s_log.fine("No Quotation");
			return null;
		}
		
		int C_Tax_ID = line.getC_Tax_ID();
		int C_Quotation_ID = line.getC_Quotation_ID();
		//MQuotation quotation = new MQuotation(null, C_Quotation_ID, null);
		
		boolean isOldTax = oldTax && line.is_ValueChanged(MQuotationTax.COLUMNNAME_C_Tax_ID);
		if(isOldTax){
			Object old = line.get_ValueOld(MQuotationTax.COLUMNNAME_C_Tax_ID);
			if(old == null){
				s_log.fine("No Old Tax");
				return null;
			}
			C_Tax_ID = ((Integer)old).intValue();
		}
		
		if(C_Tax_ID == 0){
//			if(!quotation.isDescription()){
//				s_log.fine("No Tax");
//			}
		}
		
		String sql = "SELECT * FROM C_QuotationTax WHERE C_Quotation_ID=? AND C_Tax_ID=?";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try{
			pstmt = DB.prepareStatement(sql, trxName);
			pstmt.setInt(1, line.getC_Quotation_ID());
			pstmt.setInt(2, C_Tax_ID);
			rs = pstmt.executeQuery();
			if(rs.next()){
				retValue = new MQuotationTax(line.getCtx(), rs, trxName);
			}
		}catch(Exception e){
			s_log.log(Level.SEVERE, sql, e);
		}finally{
			DB.close(rs, pstmt);
			rs = null;
			pstmt = null;
		}
		
		if(retValue != null){
			retValue.setPrecision(precision);
			retValue.set_TrxName(trxName);
			if(s_log.isLoggable(Level.FINE)){
				s_log.fine("(old=" + oldTax + ") " + retValue);
			}
			return retValue;
		}
		else{
			if(isOldTax){
				return null;
			}
		}
		retValue = new MQuotationTax(line.getCtx(), 0, trxName);
		retValue.set_TrxName(trxName);
		retValue.setClientOrg(line);
		retValue.setC_Quotation_ID(C_Quotation_ID);
		retValue.setC_Tax_ID(line.getC_Tax_ID());
		retValue.setPrecision(precision);
		retValue.setIsTaxIncluded(line.isTaxIncluded());
		if(s_log.isLoggable(Level.FINE)){
			s_log.fine("(new) " + retValue);
		}
		
		return retValue;
	}
	
	private MTax m_tax = null;
	private Integer m_precision = null;
	
	protected MTax getTax()
	{
		if (m_tax == null)
			m_tax = MTax.get(getCtx(), getC_Tax_ID());
		return m_tax;
	}	//	getTax
	
	private int getPrecision ()
	{
		if (m_precision == null)
			return 2;
		return m_precision.intValue();
	}	//	getPrecision
	
	public boolean calculateTaxFromLines(){
		BigDecimal taxBaseAmt = Env.ZERO;
		BigDecimal taxAmt = Env.ZERO;
		
		/*@tegar
		 * 		int C_Tax_ID = getC_Tax_ID();
		 * 		int C_Quotation_ID = getC_Quotation_ID();
		 */
		
		
		boolean documentLevel = getTax().isDocumentLevel();
		MTax tax = getTax();
		
		String sql = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try{
			sql = "SELECT LineNetAmt FROM C_QuotationLine WHERE C_Quotation_ID=? AND C_Tax_ID=?";
			
				pstmt = DB.prepareStatement (sql, get_TrxName());
				pstmt.setInt (1, getC_Quotation_ID());
				pstmt.setInt (2, getC_Tax_ID());
				rs = pstmt.executeQuery ();
				while (rs.next ())
				{
					BigDecimal baseAmt = rs.getBigDecimal(1);
					taxBaseAmt = taxBaseAmt.add(baseAmt);
					//
					//if (!documentLevel)		// calculate line tax
					/*
						taxAmt = taxAmt.add(tax.calculateTax(baseAmt, isTaxIncluded(), getPrecision()));
						List<MQuotationLine> quoteLines = new Query(getCtx(), MQuotationLine.Table_Name, "C_Quotation_ID=? AND C_Tax_ID=?", get_TrxName())
						.setParameters(getC_Quotation_ID(), getC_Tax_ID())
						.setOnlyActiveRecords(true)
						.list();

						for (MQuotationLine quoteLine: quoteLines) {
							BigDecimal baseAmt = quoteLine.getLineNetAmt();
							taxBaseAmt = taxBaseAmt.add(baseAmt);
							if(!documentLevel){
									taxAmt=taxAmt.add(tax.calculateTax(baseAmt, isTaxIncluded(), getPrecision()));
							}
						}*/
				}
		}			
		
		catch(Exception e){
			System.out.println(e);
		}
		finally{
			DB.close(rs, pstmt);
			rs = null;
			pstmt = null;
		}
		
		if(documentLevel){
			taxAmt = tax.calculateTax(taxBaseAmt, isTaxIncluded(), getPrecision());
		}
		
		setTaxAmt(taxAmt);
		
		if(isTaxIncluded()){
			setTaxBaseAmt(taxBaseAmt.subtract(taxAmt));
		}
		else{
			setTaxBaseAmt(taxBaseAmt);
		}
		
		if(log.isLoggable(Level.FINE)){
			log.fine(toString());
		}
		
		return true;
	}
	
}
