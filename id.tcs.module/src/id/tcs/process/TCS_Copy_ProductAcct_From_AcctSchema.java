/******************************************************************************
 * Product: Adempiere ERP & CRM Smart Business Solution                       *
 * Copyright (C) 1999-2006 ComPiere, Inc. All Rights Reserved.                *
 * This program is free software; you can redistribute it and/or modify it    *
 * under the terms version 2 of the GNU General Public License as published   *
 * by the Free Software Foundation. This program is distributed in the hope   *
 * that it will be useful, but WITHOUT ANY WARRANTY; without even the implied *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
 * See the GNU General Public License for more details.                       *
 * You should have received a copy of the GNU General Public License along    *
 * with this program; if not, write to the Free Software Foundation, Inc.,    *
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     *
 * For the text or an alternative of this public license, you may reach us    *
 * ComPiere, Inc., 2620 Augustine Dr. #245, Santa Clara, CA 95054, USA        *
 * or via info@compiere.org or http://www.compiere.org/license.html           *
 *****************************************************************************/
package id.tcs.process;

import java.util.logging.Level;

import org.compiere.model.MAcctSchema;
import org.compiere.model.MAcctSchemaDefault;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.AdempiereSystemError;
import org.compiere.util.DB;

/**
 * 	Add or Copy Acct Schema Default Accounts
 *	
 *  @author Jorg Janke
 *  @version $Id: AcctSchemaDefaultCopy.java,v 1.2 2006/07/30 00:51:01 jjanke Exp $
 */
public class TCS_Copy_ProductAcct_From_AcctSchema extends SvrProcess
{
	/**	Acct Schema					*/
	private int			p_C_AcctSchema_ID = 0;
	
	
	/**
	 *  Prepare - e.g., get Parameters.
	 */
	protected void prepare ()
	{
		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++)
		{
			String name = para[i].getParameterName();
			if (para[i].getParameter() == null)
				;
			else if (name.equals("C_AcctSchema_ID"))
				p_C_AcctSchema_ID = para[i].getParameterAsInt();
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
	}	//	prepare
		
	/**
	 * 	Process
	 *	@return message
	 *	@throws Exception
	 */
	protected String doIt () throws Exception
	{
		if (p_C_AcctSchema_ID == 0)
			throw new AdempiereSystemError("C_AcctSchema_ID=0");
		MAcctSchema as = MAcctSchema.get(getCtx(), p_C_AcctSchema_ID);
		if (as.get_ID() == 0)
			throw new AdempiereSystemError("Not Found - C_AcctSchema_ID=" + p_C_AcctSchema_ID);
		MAcctSchemaDefault acct = MAcctSchemaDefault.get (getCtx(), p_C_AcctSchema_ID);
		if (acct == null || acct.get_ID() == 0)
			throw new AdempiereSystemError("Default Not Found - C_AcctSchema_ID=" + p_C_AcctSchema_ID);
		
		StringBuilder sql = null;
		int updated = 0;
		int created = 0;
		int updatedTotal = 0;
		int createdTotal = 0;
		
		sql = new StringBuilder("DELETE FROM M_Product_Acct WHERE AD_Client_ID = ?");
		DB.executeUpdateEx(sql.toString(), new Object[]{getAD_Client_ID()}, get_TrxName());
		
		//	Update existing Product Category
		sql = new StringBuilder("UPDATE M_Product_Category_Acct pa ")
			.append("SET P_Revenue_Acct=").append(acct.getP_Revenue_Acct())
			.append(", P_Expense_Acct=").append(acct.getP_Expense_Acct())
			.append(", P_CostAdjustment_Acct=").append(acct.getP_CostAdjustment_Acct())
			.append(", P_InventoryClearing_Acct=").append(acct.getP_InventoryClearing_Acct())
			.append(", P_Asset_Acct=").append(acct.getP_Asset_Acct())
			.append(", P_COGS_Acct=").append(acct.getP_COGS_Acct())
			.append(", P_PurchasePriceVariance_Acct=").append(acct.getP_PurchasePriceVariance_Acct())
			.append(", P_InvoicePriceVariance_Acct=").append(acct.getP_InvoicePriceVariance_Acct())
			.append(", P_AverageCostVariance_Acct=").append(acct.getP_AverageCostVariance_Acct())
			.append(", P_TradeDiscountRec_Acct=").append(acct.getP_TradeDiscountRec_Acct())
			.append(", P_TradeDiscountGrant_Acct=").append(acct.getP_TradeDiscountGrant_Acct())
			.append(", P_RateVariance_Acct=").append(acct.getP_RateVariance_Acct())
			.append(", P_LandedCostClearing_Acct=").append(acct.getP_LandedCostClearing_Acct())
			.append(", Updated=SysDate, UpdatedBy=0 ")
			.append("WHERE pa.C_AcctSchema_ID=").append(p_C_AcctSchema_ID)
			.append(" AND EXISTS (SELECT * FROM M_Product_Category p ")
				.append("WHERE p.M_Product_Category_ID=pa.M_Product_Category_ID)");
		updated = DB.executeUpdate(sql.toString(), get_TrxName());
		
		//	Insert new Product Category
		sql = new StringBuilder("INSERT INTO M_Product_Category_Acct ")
			.append("(M_Product_Category_ID, C_AcctSchema_ID,")
			.append(" AD_Client_ID, AD_Org_ID, IsActive, Created, CreatedBy, Updated, UpdatedBy,")
			.append(" P_Revenue_Acct, P_Expense_Acct, P_CostAdjustment_Acct, P_InventoryClearing_Acct, P_Asset_Acct, P_CoGs_Acct,")
			.append(" P_PurchasePriceVariance_Acct, P_InvoicePriceVariance_Acct, P_AverageCostVariance_Acct,")
			.append(" P_TradeDiscountRec_Acct, P_TradeDiscountGrant_Acct," )
			.append(" P_RateVariance_Acct, P_LandedCostClearing_Acct) ")
			.append(" SELECT p.M_Product_Category_ID, acct.C_AcctSchema_ID,")
			.append(" p.AD_Client_ID, p.AD_Org_ID, 'Y', SysDate, 0, SysDate, 0,")
			.append(" acct.P_Revenue_Acct, acct.P_Expense_Acct, acct.P_CostAdjustment_Acct, acct.P_InventoryClearing_Acct, acct.P_Asset_Acct, acct.P_CoGs_Acct,")
			.append(" acct.P_PurchasePriceVariance_Acct, acct.P_InvoicePriceVariance_Acct, acct.P_AverageCostVariance_Acct,")
			.append(" acct.P_TradeDiscountRec_Acct, acct.P_TradeDiscountGrant_Acct,")
			.append(" acct.P_RateVariance_Acct, acct.P_LandedCostClearing_Acct ") 
			.append("FROM M_Product_Category p")
			.append(" INNER JOIN C_AcctSchema_Default acct ON (p.AD_Client_ID=acct.AD_Client_ID) ")
			.append("WHERE acct.C_AcctSchema_ID=").append(p_C_AcctSchema_ID)
			.append(" AND NOT EXISTS (SELECT * FROM M_Product_Category_Acct pa ")
				.append("WHERE pa.M_Product_Category_ID=p.M_Product_Category_ID")
				.append(" AND pa.C_AcctSchema_ID=acct.C_AcctSchema_ID)");
		created = DB.executeUpdate(sql.toString(), get_TrxName());
		
		sql = new StringBuilder("INSERT INTO M_Product_Acct ")
			.append("(M_Product_ID, C_AcctSchema_ID,")
			.append(" AD_Client_ID, AD_Org_ID, IsActive, Created, CreatedBy, Updated, UpdatedBy,")
			.append(" P_Revenue_Acct, P_Expense_Acct, P_CostAdjustment_Acct, P_InventoryClearing_Acct, P_Asset_Acct, P_CoGs_Acct,")
			.append(" P_PurchasePriceVariance_Acct, P_InvoicePriceVariance_Acct, P_AverageCostVariance_Acct,")
			.append(" P_TradeDiscountRec_Acct, P_TradeDiscountGrant_Acct, ")
			.append(" P_RateVariance_Acct, P_LandedCostClearing_Acct) ") 
			.append("SELECT p.M_Product_ID, acct.C_AcctSchema_ID,")
			.append(" p.AD_Client_ID, p.AD_Org_ID, 'Y', SysDate, 0, SysDate, 0,")
			.append(" acct.P_Revenue_Acct, acct.P_Expense_Acct, acct.P_CostAdjustment_Acct, acct.P_InventoryClearing_Acct, acct.P_Asset_Acct, acct.P_CoGs_Acct,")
			.append(" acct.P_PurchasePriceVariance_Acct, acct.P_InvoicePriceVariance_Acct, acct.P_AverageCostVariance_Acct,")
			.append(" acct.P_TradeDiscountRec_Acct, acct.P_TradeDiscountGrant_Acct,")
			.append(" acct.P_RateVariance_Acct, acct.P_LandedCostClearing_Acct ") 
			.append("FROM M_Product p")
			.append(" INNER JOIN M_Product_Category_Acct acct ON (acct.M_Product_Category_ID=p.M_Product_Category_ID)")
			.append("WHERE acct.C_AcctSchema_ID=").append(p_C_AcctSchema_ID)
			.append(" AND p.M_Product_Category_ID=acct.M_Product_Category_ID")
			.append(" AND NOT EXISTS (SELECT * FROM M_Product_Acct pa ")
				.append("WHERE pa.M_Product_ID=p.M_Product_ID")
				.append(" AND pa.C_AcctSchema_ID=acct.C_AcctSchema_ID)");
		created = DB.executeUpdate(sql.toString(), get_TrxName());
		
		//StringBuilder msgreturn = new StringBuilder("@Created@=").append(createdTotal).append(", @Updated@=").append(updatedTotal);
		return "ok";
	}	//	doIt
	
}	//	AcctSchemaDefaultCopy
