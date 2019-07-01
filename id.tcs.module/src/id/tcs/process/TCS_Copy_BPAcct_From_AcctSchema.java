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
public class TCS_Copy_BPAcct_From_AcctSchema extends SvrProcess
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
		
		sql = new StringBuilder("DELETE FROM C_BP_Customer_Acct WHERE AD_Client_ID = ?");
		DB.executeUpdateEx(sql.toString(), new Object[]{getAD_Client_ID()}, get_TrxName());
		
		sql = new StringBuilder("DELETE FROM C_BP_Vendor_Acct WHERE AD_Client_ID = ?");
		DB.executeUpdateEx(sql.toString(), new Object[]{getAD_Client_ID()}, get_TrxName());
		
		//	Update Business Partner Group
		sql = new StringBuilder("UPDATE C_BP_Group_Acct a ")
			.append("SET C_Receivable_Acct=").append(acct.getC_Receivable_Acct())
			.append(", C_Receivable_Services_Acct=").append(acct.getC_Receivable_Services_Acct())
			.append(", C_Prepayment_Acct=").append(acct.getC_Prepayment_Acct())
			.append(", V_Liability_Acct=").append(acct.getV_Liability_Acct())
			.append(", V_Liability_Services_Acct=").append(acct.getV_Liability_Services_Acct())
			.append(", V_Prepayment_Acct=").append(acct.getV_Prepayment_Acct())
			.append(", PayDiscount_Exp_Acct=").append(acct.getPayDiscount_Exp_Acct())
			.append(", PayDiscount_Rev_Acct=").append(acct.getPayDiscount_Rev_Acct())
			.append(", WriteOff_Acct=").append(acct.getWriteOff_Acct())
			.append(", NotInvoicedReceipts_Acct=").append(acct.getNotInvoicedReceipts_Acct())
			.append(", UnEarnedRevenue_Acct=").append(acct.getUnEarnedRevenue_Acct())
			.append(", Updated=SysDate, UpdatedBy=0 ")
			.append("WHERE a.C_AcctSchema_ID=").append(p_C_AcctSchema_ID)
			.append(" AND EXISTS (SELECT * FROM C_BP_Group_Acct x ")
				.append("WHERE x.C_BP_Group_ID=a.C_BP_Group_ID)");
		updated = DB.executeUpdate(sql.toString(), get_TrxName());
		//addLog(0, null, new BigDecimal(updated), "@Updated@ @C_BP_Group_ID@");
		//updatedTotal += updated;
			
		// Insert Business Partner Group
		sql = new StringBuilder("INSERT INTO C_BP_Group_Acct ")
			.append("(C_BP_Group_ID, C_AcctSchema_ID,")
			.append(" AD_Client_ID, AD_Org_ID, IsActive, Created, CreatedBy, Updated, UpdatedBy,")
			.append(" C_Receivable_Acct, C_Receivable_Services_Acct, C_PrePayment_Acct,")
			.append(" V_Liability_Acct, V_Liability_Services_Acct, V_PrePayment_Acct,")
			.append(" PayDiscount_Exp_Acct, PayDiscount_Rev_Acct, WriteOff_Acct,")
			.append(" NotInvoicedReceipts_Acct, UnEarnedRevenue_Acct) ")
			.append("SELECT x.C_BP_Group_ID, acct.C_AcctSchema_ID,")
			.append(" x.AD_Client_ID, x.AD_Org_ID, 'Y', SysDate, 0, SysDate, 0,")
			.append(" acct.C_Receivable_Acct, acct.C_Receivable_Services_Acct, acct.C_PrePayment_Acct,")
			.append(" acct.V_Liability_Acct, acct.V_Liability_Services_Acct, acct.V_PrePayment_Acct,")
			.append(" acct.PayDiscount_Exp_Acct, acct.PayDiscount_Rev_Acct, acct.WriteOff_Acct,")
			.append(" acct.NotInvoicedReceipts_Acct, acct.UnEarnedRevenue_Acct ")
			.append("FROM C_BP_Group x")
			.append(" INNER JOIN C_AcctSchema_Default acct ON (x.AD_Client_ID=acct.AD_Client_ID) ")
			.append("WHERE acct.C_AcctSchema_ID=").append(p_C_AcctSchema_ID)
			.append(" AND NOT EXISTS (SELECT * FROM C_BP_Group_Acct a ")
				.append("WHERE a.C_BP_Group_ID=x.C_BP_Group_ID")
				.append(" AND a.C_AcctSchema_ID=acct.C_AcctSchema_ID)");
		created = DB.executeUpdate(sql.toString(), get_TrxName());
		//addLog(0, null, new BigDecimal(created), "@Created@ @C_BP_Group_ID@");
		//createdTotal += created;
		
		//if (!p_CopyOverwriteAcct)
		//{
			sql = new StringBuilder("INSERT INTO C_BP_Customer_Acct ")
				.append("(C_BPartner_ID, C_AcctSchema_ID,")
				.append(" AD_Client_ID, AD_Org_ID, IsActive, Created, CreatedBy, Updated, UpdatedBy,")
				.append(" C_Receivable_Acct, C_Receivable_Services_Acct, C_PrePayment_Acct) ")
				.append("SELECT p.C_BPartner_ID, acct.C_AcctSchema_ID,")
				.append(" p.AD_Client_ID, p.AD_Org_ID, 'Y', SysDate, 0, SysDate, 0,")
				.append(" acct.C_Receivable_Acct, acct.C_Receivable_Services_Acct, acct.C_PrePayment_Acct ")
				.append("FROM C_BPartner p")
				.append(" INNER JOIN C_BP_Group_Acct acct ON (acct.C_BP_Group_ID=p.C_BP_Group_ID)")
				.append("WHERE acct.C_AcctSchema_ID=").append(p_C_AcctSchema_ID)			//	#
				.append(" AND p.C_BP_Group_ID=acct.C_BP_Group_ID")
				.append(" AND NOT EXISTS (SELECT * FROM C_BP_Customer_Acct ca ")
					.append("WHERE ca.C_BPartner_ID=p.C_BPartner_ID")
					.append(" AND ca.C_AcctSchema_ID=acct.C_AcctSchema_ID)");
			created = DB.executeUpdate(sql.toString(), get_TrxName());
			//addLog(0, null, new BigDecimal(created), "@Created@ @C_BPartner_ID@ @IsCustomer@");
			//createdTotal += created;
			//
			sql = new StringBuilder("INSERT INTO C_BP_Vendor_Acct ")
				.append("(C_BPartner_ID, C_AcctSchema_ID,")
				.append(" AD_Client_ID, AD_Org_ID, IsActive, Created, CreatedBy, Updated, UpdatedBy,")
				.append(" V_Liability_Acct, V_Liability_Services_Acct, V_PrePayment_Acct) ")
				.append("SELECT p.C_BPartner_ID, acct.C_AcctSchema_ID,")
				.append(" p.AD_Client_ID, p.AD_Org_ID, 'Y', SysDate, 0, SysDate, 0,")
				.append(" acct.V_Liability_Acct, acct.V_Liability_Services_Acct, acct.V_PrePayment_Acct ")
				.append("FROM C_BPartner p")
				.append(" INNER JOIN C_BP_Group_Acct acct ON (acct.C_BP_Group_ID=p.C_BP_Group_ID)")
				.append("WHERE acct.C_AcctSchema_ID=").append(p_C_AcctSchema_ID)			//	#
				.append(" AND p.C_BP_Group_ID=acct.C_BP_Group_ID")
				.append(" AND NOT EXISTS (SELECT * FROM C_BP_Vendor_Acct va ")
					.append("WHERE va.C_BPartner_ID=p.C_BPartner_ID AND va.C_AcctSchema_ID=acct.C_AcctSchema_ID)");
			created = DB.executeUpdate(sql.toString(), get_TrxName());
			//addLog(0, null, new BigDecimal(created), "@Created@ @C_BPartner_ID@ @IsVendor@");
			//createdTotal += created;
		//}
		
		//StringBuilder msgreturn = new StringBuilder("@Created@=").append(createdTotal).append(", @Updated@=").append(updatedTotal);
		return "ok";
	}	//	doIt
	
}	//	AcctSchemaDefaultCopy
