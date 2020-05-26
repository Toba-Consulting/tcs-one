package id.tcs.model;

import java.math.BigDecimal;
import java.util.ArrayList;

import org.adempiere.model.ITaxProvider;
import org.compiere.model.MOrder;
import org.compiere.model.MOrderLine;
import org.compiere.model.MOrderTax;
import org.compiere.model.MTax;
import org.compiere.model.MTaxProvider;
import org.compiere.model.StandardTaxProvider;
import org.compiere.util.DB;
import org.compiere.util.Env;

public class TCS_TaxProvider extends StandardTaxProvider implements ITaxProvider{

	public boolean calculateQuotationTaxTotal(MTaxProvider provider, MQuotation quot) {
		//	Lines
		BigDecimal totalLines = Env.ZERO;
		ArrayList<Integer> taxList = new ArrayList<Integer>();
		MQuotationLine[] lines = quot.getLines();
		for (int i = 0; i < lines.length; i++)
		{
			MQuotationLine line = lines[i];
			totalLines = totalLines.add(line.getLineNetAmt());
			Integer taxID = new Integer(line.getC_Tax_ID());
			if (!taxList.contains(taxID))
			{
				MTax tax = new MTax(quot.getCtx(), taxID, quot.get_TrxName());
				if (tax.getC_TaxProvider_ID() != 0)
					continue;
				MQuotationTax qTax = MQuotationTax.get (line, quot.getPrecision(), false, quot.get_TrxName());	//	current Tax
				//Temporary Default NO
				//qTax.setIsTaxIncluded(quot.isTaxIncluded());
				if (!qTax.calculateTaxFromLines())
					return false;
				if (!qTax.save(quot.get_TrxName()))
					return false;
				taxList.add(taxID);
			}
		}
		
		//	Taxes
		BigDecimal grandTotal = totalLines;
		MQuotationTax[] taxes = quot.getTaxes(true);
		for (int i = 0; i < taxes.length; i++)
		{
			MQuotationTax oTax = taxes[i];
			//Temporary Default NO
			/*if (oTax.getC_TaxProvider_ID() != 0) {
				if (!quot.isTaxIncluded())
					grandTotal = grandTotal.add(oTax.getTaxAmt());
				continue;
			}
			*/
			MTax tax = oTax.getTax();
			if (tax.isSummary())
			{
				MTax[] cTaxes = tax.getChildTaxes(false);
				for (int j = 0; j < cTaxes.length; j++)
				{
					MTax cTax = cTaxes[j];
					BigDecimal taxAmt = cTax.calculateTax(oTax.getTaxBaseAmt(), false, quot.getPrecision());
					//
					MQuotationTax newOTax = new MQuotationTax(quot.getCtx(), 0, quot.get_TrxName());
					newOTax.setAD_Org_ID(quot.getAD_Org_ID());
				
					newOTax.setC_Quotation_ID(quot.getC_Quotation_ID());
					newOTax.setC_Tax_ID(cTax.getC_Tax_ID());
					newOTax.setPrecision(quot.getPrecision());
					//Temporary Default NO
					//newOTax.setIsTaxIncluded(quot.isTaxIncluded());
					newOTax.setTaxBaseAmt(oTax.getTaxBaseAmt());
					newOTax.setTaxAmt(taxAmt);
					if (!newOTax.save(quot.get_TrxName()))
						return false;
					//
					//Temporary Default NO
					//if (!quot.isTaxIncluded())
					//	grandTotal = grandTotal.add(taxAmt);
				}
				if (!oTax.delete(true, quot.get_TrxName()))
					return false;
				if (!oTax.save(quot.get_TrxName()))
					return false;
			}
			else
			{
				//Temporary Default NO
				//if (!quot.isTaxIncluded())
				//	grandTotal = grandTotal.add(oTax.getTaxAmt());
			}
		}		
		//
		quot.setTotalLines(totalLines);
		quot.setGrandTotal(grandTotal);
		return true;
	}

	public boolean updateQuotationTax(MTaxProvider provider, MQuotationLine line) {
		MTax mtax = new MTax(line.getCtx(), line.getC_Tax_ID(), line.get_TrxName());
    	if (mtax.getC_TaxProvider_ID() == 0)
    		return line.updateQuotationTax(false);
    	return true;
	}

	public boolean recalculateTax(MTaxProvider provider, MQuotationLine line, boolean newRecord)
	{
		if (!newRecord && line.is_ValueChanged(MOrderLine.COLUMNNAME_C_Tax_ID) && !line.getParent().isProcessed())
		{
			MTax mtax = new MTax(line.getCtx(), line.getC_Tax_ID(), line.get_TrxName());
	    	if (mtax.getC_TaxProvider_ID() == 0)
	    	{
				//	Recalculate Tax for old Tax
				if (!line.updateQuotationTax(true))
					return false;
	    	}
		}
		return line.updateHeaderTax();
	}

	public boolean updateHeaderTax(MTaxProvider provider, MQuotationLine line)
	{
		//		Update Order Header
		String sql = "UPDATE C_Quotation i"
			+ " SET TotalLines="
				+ "(SELECT COALESCE(SUM(LineNetAmt),0) FROM C_QuotationLine il WHERE i.C_Quotation_ID=il.C_Quotation_ID) "
			+ "WHERE C_Quotation_ID=" + line.getC_Quotation_ID();
		int no = DB.executeUpdate(sql, line.get_TrxName());
		if (no != 1)
			log.warning("(1) #" + no);

		if (line.isTaxIncluded())
			sql = "UPDATE C_Quotation i "
				+ " SET GrandTotal=TotalLines "
				+ "WHERE C_Quotation_ID=" + line.getC_Quotation_ID();
		else
			sql = "UPDATE C_Quotation i "
				+ " SET GrandTotal=TotalLines+"
					+ "(SELECT COALESCE(SUM(TaxAmt),0) FROM C_QuotationTax it WHERE i.C_Quotation_ID=it.C_Quotation_ID) "
					+ "WHERE C_Quotation_ID=" + line.getC_Quotation_ID();
		no = DB.executeUpdate(sql, line.get_TrxName());
		if (no != 1)
			log.warning("(2) #" + no);

		line.clearParent();
		return no == 1;
	}

}
