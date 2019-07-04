/******************************************************************************
 * Product: iDempiere ERP & CRM Smart Business Solution                       *
 * Copyright (C) 1999-2012 ComPiere, Inc. All Rights Reserved.                *
 * This program is free software, you can redistribute it and/or modify it    *
 * under the terms version 2 of the GNU General Public License as published   *
 * by the Free Software Foundation. This program is distributed in the hope   *
 * that it will be useful, but WITHOUT ANY WARRANTY, without even the implied *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
 * See the GNU General Public License for more details.                       *
 * You should have received a copy of the GNU General Public License along    *
 * with this program, if not, write to the Free Software Foundation, Inc.,    *
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     *
 * For the text or an alternative of this public license, you may reach us    *
 * ComPiere, Inc., 2620 Augustine Dr. #245, Santa Clara, CA 95054, USA        *
 * or via info@compiere.org or http://www.compiere.org/license.html           *
 *****************************************************************************/

package id.tcs.module.factory;

import java.lang.reflect.Constructor;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Properties;

import org.adempiere.base.IModelFactory;
import org.compiere.model.I_C_AllocationHdr;
import org.compiere.model.I_C_AllocationLine;
import org.compiere.model.I_C_BankStatement;
import org.compiere.model.I_C_Invoice;
import org.compiere.model.I_C_Order;
import org.compiere.model.I_C_Payment;
import org.compiere.model.I_M_InOut;
import org.compiere.model.I_M_Inventory;
import org.compiere.model.I_M_Movement;
import org.compiere.model.I_M_RMA;
import org.compiere.model.I_M_Requisition;
import org.compiere.model.PO;
import org.compiere.util.Env;

import id.tcs.model.I_TCS_AllocateCharge;
import id.tcs.model.MBankTransfer;



/**
 * Generic Model Factory 
 * 
 * @author Double Click Sistemas C.A. - http://dcs.net.ve
 * @author Saúl Piña - spina@dcs.net.ve
 */
public class TCS_ModelFactory implements IModelFactory {

	private static HashMap<String, String> mapTableModels = new HashMap<String, String>();
	static
	{
		mapTableModels.put(I_M_Requisition.Table_Name, "org.compiere.model.TCS_MRequisition");
		mapTableModels.put(I_C_Order.Table_Name, "org.compiere.model.TCS_MOrder");
		mapTableModels.put(I_M_RMA.Table_Name, "org.compiere.model.TCS_MRMA");
		mapTableModels.put(I_M_InOut.Table_Name, "org.compiere.model.TCS_MInOut");
		mapTableModels.put(I_C_Invoice.Table_Name, "org.compiere.model.TCS_MInvoice");
		mapTableModels.put(I_C_Payment.Table_Name, "org.compiere.model.TCS_MPayment");
		mapTableModels.put(I_C_AllocationHdr.Table_Name, "org.compiere.model.TCS_MAllocationHdr");
		mapTableModels.put(I_C_AllocationLine.Table_Name, "org.compiere.model.TCS_MAllocationLine");
		mapTableModels.put(I_C_BankStatement.Table_Name, "org.compiere.model.TCS_MBankStatement");
		mapTableModels.put(I_M_Inventory.Table_Name, "org.compiere.model.TCS_MInventory");
		mapTableModels.put(I_M_Movement.Table_Name, "org.compiere.model.TCS_MMovement");
		
		mapTableModels.put(I_TCS_AllocateCharge.Table_Name, "id.tcs.model.MTCS_AllocateCharge");
		mapTableModels.put(MBankTransfer.Table_Name, "id.tcs.model.MBankTransfer");
		
	}
	
	@Override
	public Class<?> getClass(String tableName) {

		if (mapTableModels.containsKey(tableName)) {
			Class<?> act = null;
			try {
				act = Class.forName(mapTableModels.get(tableName));
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
				return act;
		
		} else 
			return null;
	}

	@Override
	public PO getPO(String tableName, int Record_ID, String trxName) {
		
		if (mapTableModels.containsKey(tableName)) {
			Class<?> clazz = null;
			Constructor<?> ctor = null;
			PO object = null;
			try {
				clazz = Class.forName(mapTableModels.get(tableName));
				ctor = clazz.getConstructor(Properties.class, int.class, String.class);
				object = (PO) ctor.newInstance(new Object[] {Env.getCtx(), Record_ID, trxName});
				
			} catch (Exception e) {
				e.printStackTrace();
			}
				return object;
		} else 	   
		   return null;
	}

	@Override
	public PO getPO(String tableName, ResultSet rs, String trxName) {
	
		if (mapTableModels.containsKey(tableName)) {
			Class<?> clazz = null;
			Constructor<?> ctor = null;
			PO object = null;
			try {
				clazz = Class.forName(mapTableModels.get(tableName));
				ctor = clazz.getConstructor(Properties.class, ResultSet.class, String.class);
				object = (PO) ctor.newInstance(new Object[] {Env.getCtx(), rs, trxName});
				
			} catch (Exception e) {
				e.printStackTrace();
			}
				return object;
				
		} else  
			return null;
	}

}
