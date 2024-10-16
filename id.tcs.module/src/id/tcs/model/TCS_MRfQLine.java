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
package id.tcs.model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Properties;
import java.util.logging.Level;

import org.compiere.model.MProduct;
import org.compiere.util.CCache;
import org.compiere.util.DB;
import org.compiere.util.TimeUtil;

/**
 *	RfQ Line
 *	
 *  @author Jorg Janke
 *  @version $Id: MRfQLine.java,v 1.3 2006/07/30 00:51:05 jjanke Exp $
 */
public class TCS_MRfQLine extends X_TCS_C_RfQLine
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 5090299865266992874L;

	/**
	 * 	Get MRfQLine from Cache
	 *	@param ctx context
	 *	@param C_RfQLine_ID id
	 *	@param trxName transaction
	 *	@return MRfQLine
	 */
	public static TCS_MRfQLine get (Properties ctx, int C_RfQLine_ID, String trxName)
	{
		Integer key = new Integer (C_RfQLine_ID);
		TCS_MRfQLine retValue = (TCS_MRfQLine) s_cache.get (key);
		if (retValue != null)
			return retValue;
		retValue = new TCS_MRfQLine (ctx, C_RfQLine_ID, trxName);
		if (retValue.get_ID () != 0)
			s_cache.put (key, retValue);
		return retValue;
	} //	get

	/**	Cache						*/
	private static CCache<Integer,TCS_MRfQLine>	s_cache	= new CCache<Integer,TCS_MRfQLine>(Table_Name, 20);
	
	/**
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param C_RfQLine_ID id
	 *	@param trxName transaction
	 */
	public TCS_MRfQLine (Properties ctx, int C_RfQLine_ID, String trxName)
	{
		super (ctx, C_RfQLine_ID, trxName);
		if (C_RfQLine_ID == 0)
		{
			setLine (0);
		}
	}	//	MRfQLine

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trxName transaction
	 */
	public TCS_MRfQLine (Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	//	MRfQLine

	/**
	 * 	Parent Constructor
	 *	@param rfq RfQ
	 */
	public TCS_MRfQLine (TCS_MRfQ rfq)
	{
		this (rfq.getCtx(), 0, rfq.get_TrxName());
		setClientOrg(rfq);
		setC_RfQ_ID(rfq.getC_RfQ_ID());
	}	//	MRfQLine
	
	/**	Qyantities				*/
	private TCS_MRfQLineQty[] 	m_qtys = null;
	
	/**
	 * 	Get Quantities
	 *	@return array of quantities
	 */
	public TCS_MRfQLineQty[] getQtys ()
	{
		return getQtys (false);
	}	//	getQtys
	
	/**
	 * 	Get Quantities
	 * 	@param requery requery
	 *	@return array of quantities
	 */
	public TCS_MRfQLineQty[] getQtys (boolean requery)
	{
		if (m_qtys != null && !requery)
			return m_qtys;
		ArrayList<TCS_MRfQLineQty> list = new ArrayList<TCS_MRfQLineQty>();
		String sql = "SELECT * FROM C_RfQLineQty "
			+ "WHERE C_RfQLine_ID=? AND IsActive='Y' "
			+ "ORDER BY Qty";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement (sql, get_TrxName());
			pstmt.setInt (1, getC_RfQLine_ID());
			rs = pstmt.executeQuery ();
			while (rs.next ())
				list.add (new TCS_MRfQLineQty (getCtx(), rs, get_TrxName()));
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, sql, e);
		}
		finally
		{
			DB.close(rs, pstmt);
			rs = null;
			pstmt = null;
		}
		//	Create Default (1)
		if (list.size() == 0)
		{
			TCS_MRfQLineQty qty = new TCS_MRfQLineQty(this);
			qty.saveEx();
			list.add(qty);
		}
		
		m_qtys = new TCS_MRfQLineQty[list.size ()];
		list.toArray (m_qtys);
		return m_qtys;
	}	//	getQtys
	
	/**
	 * 	Get Product Details
	 *	@return Product Name, etc.
	 */
	public String getProductDetailHTML()
	{
		if (getM_Product_ID() == 0)
			return "";
		StringBuilder sb = new StringBuilder();
		MProduct product = MProduct.get (getCtx(), getM_Product_ID());
		sb.append(product.getName());
		if (product.getDescription() != null && product.getDescription().length() > 0)
			sb.append("<br><i>").append(product.getDescription()).append("</i>");
		return sb.toString();
	}	//	getProductDetails
	
	/**
	 * 	String Representation
	 *	@return info
	 */
	public String toString ()
	{
		StringBuilder sb = new StringBuilder ("MRfQLine[");
		sb.append(get_ID()).append(",").append(getLine())
			.append ("]");
		return sb.toString ();
	}	//	toString
	
	/**
	 * 	Before Save
	 *	@param newRecord new
	 *	@return true
	 */
	protected boolean beforeSave (boolean newRecord)
	{
		//	Calculate Complete Date (also used to verify)
		if (!isDescription() && getM_Product_ID()<=0 && getC_Charge_ID()<=0)
			return false;
		
		if (getDateWorkStart() != null && getDeliveryDays() != 0)
			setDateWorkComplete (TimeUtil.addDays(getDateWorkStart(), getDeliveryDays()));
		//	Calculate Delivery Days
		else if (getDateWorkStart() != null && getDeliveryDays() == 0 && getDateWorkComplete() != null)
			setDeliveryDays (TimeUtil.getDaysBetween(getDateWorkStart(), getDateWorkComplete()));
		//	Calculate Start Date
		else if (getDateWorkStart() == null && getDeliveryDays() != 0 && getDateWorkComplete() != null)
			setDateWorkStart (TimeUtil.addDays(getDateWorkComplete(), getDeliveryDays() * -1));

		return true;
	}	//	beforeSave

	@Override
	protected boolean beforeDelete()
	{
		String sql = "DELETE FROM M_MatchInquiry WHERE C_RfQLine_ID="+get_ID();
		DB.executeUpdate(sql, get_TrxName());
		
		String sql2 = "DELETE FROM M_MatchRfQResponses WHERE C_RfQLine_ID="+get_ID();
		DB.executeUpdate(sql2, get_TrxName());
		
		String sql3 = "DELETE FROM M_MatchQuotation WHERE C_RfQLine_ID="+get_ID();
		DB.executeUpdate(sql3, get_TrxName());
		
		String sql4 = "DELETE FROM M_MatchRequest WHERE C_RfQLine_ID="+get_ID();
		DB.executeUpdate(sql4, get_TrxName());
		
		String sql5 = "UPDATE C_InquiryLine SET C_RfQLine_ID=NULL WHERE C_RfQLine_ID="+get_ID();
		DB.executeUpdate(sql5, get_TrxName());
		
		return true;
	}
	
}	//	MRfQLine
