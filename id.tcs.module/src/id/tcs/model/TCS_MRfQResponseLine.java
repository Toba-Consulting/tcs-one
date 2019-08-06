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

import org.compiere.util.DB;
import org.compiere.util.TimeUtil;

/**
 *	RfQ Response Line Model	
 *	
 *  @author Jorg Janke
 *  @version $Id: MRfQResponseLine.java,v 1.3 2006/07/30 00:51:03 jjanke Exp $
 *  
 *  @author Teo Sarca, teo.sarca@gmail.com
 *  		<li>BF [ 2892581 ] Cannot load RfQResponseLine
 *  			https://sourceforge.net/tracker/?func=detail&aid=2892581&group_id=176962&atid=879335
 */
public class TCS_MRfQResponseLine extends X_TCS_C_RfQResponseLine
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 3388579962604552288L;

	/**
	 * 	Persistency Constructor
	 *	@param ctx context
	 *	@param C_RfQResponseLine_ID
	 *	@param trxName transaction
	 */
	public TCS_MRfQResponseLine (Properties ctx, int C_RfQResponseLine_ID, String trxName)
	{
		super(ctx, C_RfQResponseLine_ID, trxName);
//		if (ignored != 0)
//			throw new IllegalArgumentException("Multi-Key");
	}	//	MRfQResponseLine

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trxName transaction
	 */
	public TCS_MRfQResponseLine (Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	//	MRfQResponseLine
	
	/**
	 * 	Parent Constructor.
	 * 	Also creates qtys if RfQ Qty
	 * 	Is saved if there are qtys(!)
	 *	@param response response
	 *	@param line line
	 */
	public TCS_MRfQResponseLine (TCS_MRfQResponse response, TCS_MRfQLine line)
	{
		super (response.getCtx(), 0, response.get_TrxName());
		setClientOrg(response);
		setC_RfQResponse_ID (response.getC_RfQResponse_ID());
		//
		setC_RfQLine_ID (line.getC_RfQLine_ID());
		//
		setIsSelectedWinner (false);
		setIsSelfService (false);
		//

	}	//	MRfQResponseLine
	
	/**	RfQ Line				*/
	private TCS_MRfQLine				m_rfqLine = null;
	/**	Quantities				*/
	private TCS_MRfQResponseLineQty[] 	m_qtys = null;
	
	/**
	 * 	Get Quantities
	 *	@return array of quantities
	 */
	public TCS_MRfQResponseLineQty[] getQtys ()
	{
		return getQtys (false);
	}	//	getQtys

	/**
	 * 	Get Quantities
	 * 	@param requery requery
	 *	@return array of quantities
	 */
	public TCS_MRfQResponseLineQty[] getQtys (boolean requery)
	{
		if (m_qtys != null && !requery)
			return m_qtys;
		
		ArrayList<TCS_MRfQResponseLineQty> list = new ArrayList<TCS_MRfQResponseLineQty>();
		String sql = "SELECT * FROM C_RfQResponseLineQty "
			+ "WHERE C_RfQResponseLine_ID=? AND IsActive='Y'";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement (sql, get_TrxName());
			pstmt.setInt (1, getC_RfQResponseLine_ID());
			rs = pstmt.executeQuery ();
			while (rs.next ())
				list.add (new TCS_MRfQResponseLineQty(getCtx(), rs, get_TrxName()));
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
		
		m_qtys = new TCS_MRfQResponseLineQty[list.size ()];
		list.toArray (m_qtys);
		return m_qtys;
	}	//	getQtys
	
	/**
	 * 	Get RfQ Line
	 *	@return rfq line
	 */
	public TCS_MRfQLine getRfQLine()
	{
		if (m_rfqLine == null)
			m_rfqLine = TCS_MRfQLine.get(getCtx(), getC_RfQLine_ID(), get_TrxName());
		return m_rfqLine;
	}	//	getRfQLine
	
	/**
	 * 	String Representation
	 *	@return info
	 */
	public String toString ()
	{
		StringBuilder sb = new StringBuilder ("MRfQResponseLine[");
		sb.append(get_ID()).append(",Winner=").append(isSelectedWinner())
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

		if (!isActive())
			setIsSelectedWinner(false);
		return true;
	}	//	beforeSave
	

	/**
	 * 	After Save
	 *	@param newRecord new
	 *	@param success success
	 *	@return success
	 */
	protected boolean afterSave (boolean newRecord, boolean success)
	{	
		if (!success)
			return success;
		if (!isActive())
		{
			getQtys (false);
			for (int i = 0; i < m_qtys.length; i++)
			{
				TCS_MRfQResponseLineQty qty = m_qtys[i];
				if (qty.isActive())
				{
					qty.setIsActive(false);
					qty.saveEx();
				}
			}
		}
		return success;
	}	//	success
	
}	//	MRfQResponseLine
