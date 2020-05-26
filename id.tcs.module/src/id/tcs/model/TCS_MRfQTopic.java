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


/**
 *	RfQ Topic Model
 *	
 *  @author Jorg Janke
 *  @version $Id: MRfQTopic.java,v 1.3 2006/07/30 00:51:03 jjanke Exp $
 */
public class TCS_MRfQTopic extends X_TCS_C_RfQ_Topic
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1569514263521190339L;

	/**
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param C_RfQ_Topic_ID id
	 *	@param trxName transaction
	 */
	public TCS_MRfQTopic (Properties ctx, int C_RfQ_Topic_ID, String trxName)
	{
		super (ctx, C_RfQ_Topic_ID, trxName);
	}	//	MRfQTopic

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trxName transaction
	 */
	public TCS_MRfQTopic (Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	//	MRfQTopic

	/**
	 * 	Get Current Topic Subscribers
	 *	@return array subscribers
	 */
	public TCS_MRfQTopicSubscriber[] getSubscribers()
	{
		ArrayList<TCS_MRfQTopicSubscriber> list = new ArrayList<TCS_MRfQTopicSubscriber>();
		String sql = "SELECT * FROM C_RfQ_TopicSubscriber "
			+ "WHERE C_RfQ_Topic_ID=? AND IsActive='Y'";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement (sql, get_TrxName());
			pstmt.setInt (1, getC_RfQ_Topic_ID());
			rs = pstmt.executeQuery ();
			while (rs.next ())
				list.add (new TCS_MRfQTopicSubscriber (getCtx(), rs, get_TrxName()));
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, "getSubscribers", e);
		}
		finally
		{
			DB.close(rs, pstmt);
			rs = null;
			pstmt = null;
		}
		
		TCS_MRfQTopicSubscriber[] retValue = new TCS_MRfQTopicSubscriber[list.size ()];
		list.toArray (retValue);
		return retValue;
	}	//	getSubscribers
	
}	//	MRfQTopic

