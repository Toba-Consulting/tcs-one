/******************************************************************************
 * Copyright (C) 2013 Elaine Tan                                              *
 * Copyright (C) 2013 Trek Global
 * This program is free software; you can redistribute it and/or modify it    *
 * under the terms version 2 of the GNU General Public License as published   *
 * by the Free Software Foundation. This program is distributed in the hope   *
 * that it will be useful, but WITHOUT ANY WARRANTY; without even the implied *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
 * See the GNU General Public License for more details.                       *
 * You should have received a copy of the GNU General Public License along    *
 * with this program; if not, write to the Free Software Foundation, Inc.,    *
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     *
 *****************************************************************************/
package id.tcs.model;

import org.compiere.model.MInvoice;
import org.compiere.model.MInvoiceLine;
import org.compiere.model.MOrder;
import org.compiere.model.MOrderLine;
import org.compiere.model.MRMA;
import org.compiere.model.MRMALine;
import org.compiere.model.MTaxProvider;
import org.compiere.process.ProcessInfo;

/**
 * Tax provider interface
 * @author Elaine
 *
 * @contributor Murilo H. Torquato <muriloht@devcoffee.com.br>
 *
 */
public interface TCS_ITaxProvider extends org.adempiere.model.ITaxProvider{
		

	public boolean calculateQuotationTaxTotal(MTaxProvider provider, MQuotation order);
	
	public boolean updateQuotationTax(MTaxProvider provider, MQuotationLine line);

	public boolean recalculateTax(MTaxProvider provider, MQuotationLine line, boolean newRecord);

	public boolean updateHeaderTax(MTaxProvider provider, MQuotationLine line);
	
	
}