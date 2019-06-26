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
import org.compiere.model.I_C_BankStatement;
import org.compiere.model.I_C_Payment;
import org.compiere.model.PO;
import org.compiere.util.Env;

import id.tcs.model.I_TCS_AllocateCharge;
import id.tcs.model.MBankTransfer;
import id.tcs.model.TCS_MAdvRequest;
import id.tcs.model.TCS_MAdvRequestLine;
import id.tcs.model.TCS_MAdvSettlement;
import id.tcs.model.TCS_MAdvSettlementLine;
import id.tcs.model.TCS_MDestRequest;
import id.tcs.model.TCS_MDestSettlement;
import id.tcs.model.TCS_MExpenseLine;
import id.tcs.model.TCS_MTravelExpense;
import id.tcs.model.TCS_MTripFacility;

import org.compiere.model.TCS_MAllocationHdr;



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
		mapTableModels.put(I_C_Payment.Table_Name, "id.tcs.model.TCS_MPayment");
		mapTableModels.put(I_TCS_AllocateCharge.Table_Name, "id.tcs.model.MTCS_AllocateCharge");
		mapTableModels.put(I_C_BankStatement.Table_Name, "id.tcs.model.TCS_MBankStatement");
		mapTableModels.put(I_C_BankStatement.Table_Name, "id.tcs.model.TCS_MBankStatement");
		mapTableModels.put(TCS_MAllocationHdr.Table_Name, "org.compiere.model.TCS_MAllocationHdr");
		mapTableModels.put(TCS_MAdvRequest.Table_Name, "id.tcs.model.TCS_MAdvRequest");
		mapTableModels.put(TCS_MDestRequest.Table_Name, "id.tcs.model.TCS_MDestRequest");
		mapTableModels.put(TCS_MAdvRequestLine.Table_Name, "id.tcs.model.TCS_MAdvRequestLine");
		mapTableModels.put(TCS_MAdvSettlement.Table_Name, "id.tcs.model.TCS_MAdvSettlement");
		mapTableModels.put(TCS_MDestSettlement.Table_Name, "id.tcs.model.TCS_MDestSettlement");
		mapTableModels.put(TCS_MAdvSettlementLine.Table_Name, "id.tcs.model.TCS_MAdvSettlementLine");
		mapTableModels.put(TCS_MTripFacility.Table_Name, "id.tcs.model.TCS_MTripFacility");
		mapTableModels.put(TCS_MTravelExpense.Table_Name, "id.tcs.model.TCS_MTravelExpense");
		mapTableModels.put(TCS_MExpenseLine.Table_Name, "id.tcs.model.TCS_MExpenseLine");
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
