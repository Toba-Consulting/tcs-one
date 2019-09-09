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

import java.io.File;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Properties;
import java.util.logging.Level;

import org.compiere.model.MBPartner;
import org.compiere.model.MClient;
import org.compiere.model.MMailText;
import org.compiere.model.MProductCategory;
import org.compiere.model.MRfQTopicSubscriber;
import org.compiere.model.MUser;
import org.compiere.print.MPrintFormat;
import org.compiere.print.ReportEngine;
import org.compiere.process.ProcessInfo;
import org.compiere.process.ServerProcessCtl;
import org.compiere.util.DB;
import org.compiere.util.EMail;
import org.compiere.util.Env;
import org.compiere.util.TimeUtil;

/**
 *	RfQ Response Model	
 *	
 *  @author Jorg Janke
 *  @version $Id: MRfQResponse.java,v 1.3 2006/07/30 00:51:05 jjanke Exp $
 */
public class TCS_MRfQResponse extends X_TCS_C_RfQResponse
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1472377321844135042L;


	/**
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param C_RfQResponse_ID id
	 *	@param trxName transaction
	 */
	public TCS_MRfQResponse (Properties ctx, int C_RfQResponse_ID, String trxName)
	{
		super (ctx, C_RfQResponse_ID, trxName);
		if (C_RfQResponse_ID == 0)
		{
			setIsComplete (false);
			setIsSelectedWinner (false);
			setIsSelfService (false);
			setPrice (Env.ZERO);
			setProcessed(false);
			setProcessing(false);
		}
	}	//	MRfQResponse

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trxName transaction
	 */
	public TCS_MRfQResponse (Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	//	MRfQResponse

	/**
	 * 	Parent Constructor
	 *	@param rfq rfq
	 *	@param subscriber subscriber
	 */
	public TCS_MRfQResponse (TCS_MRfQ rfq, TCS_MRfQTopicSubscriber subscriber)
	{
		this (rfq, subscriber, 
			subscriber.getC_BPartner_ID(), 
			subscriber.getC_BPartner_Location_ID(), 
			subscriber.getAD_User_ID());
	}	//	MRfQResponse

	/**
	 * 	Parent Constructor
	 *	@param rfq rfq
	 *	@param partner web response
	 */
	public TCS_MRfQResponse (TCS_MRfQ rfq, MBPartner partner)
	{
		this (rfq, null, 
			partner.getC_BPartner_ID(), 
			partner.getPrimaryC_BPartner_Location_ID(), 
			partner.getPrimaryAD_User_ID());
	}	//	MRfQResponse
	
	/**
	 * 	Parent Constructor.
	 * 	Automatically saved if lines were created 
	 * 	Saved automatically
	 *	@param rfq rfq
	 *	@param subscriber optional subscriber
	 *	@param C_BPartner_ID bpartner
	 *	@param C_BPartner_Location_ID bpartner location
	 *	@param AD_User_ID bpartner user
	 */
	public TCS_MRfQResponse (TCS_MRfQ rfq, TCS_MRfQTopicSubscriber subscriber,
		int C_BPartner_ID, int C_BPartner_Location_ID, int AD_User_ID)
	{
		this (rfq.getCtx(), 0, rfq.get_TrxName());
		setClientOrg(rfq);
		setC_RfQ_ID(rfq.getC_RfQ_ID());
		setC_Currency_ID (rfq.getC_Currency_ID());
		setName (rfq.getName());
		setDateInvited(new Timestamp(System.currentTimeMillis()));
		setDateResponse(rfq.getDateResponse());
		setDateWorkStart(rfq.getDateWorkStart());
		setDateWorkComplete(rfq.getDateWorkComplete());
		setDeliveryDays(rfq.getDeliveryDays());
		setIsInternal(subscriber.isInternal());
		
		// set orgTrx and Project
		if(rfq.get_ValueAsInt("AD_OrgTrx_ID") > 0){
			set_ValueOfColumn("AD_OrgTrx_ID", rfq.get_ValueAsInt("AD_OrgTrx_ID"));
		}
		if(rfq.getC_Project_ID() > 0){
			set_ValueOfColumn("C_Project_ID", rfq.getC_Project_ID());
		}
		
		m_rfq = rfq;
		//	Subscriber info
		setC_BPartner_ID (C_BPartner_ID);
		setC_BPartner_Location_ID (C_BPartner_Location_ID);
		setAD_User_ID(AD_User_ID);
		
		//	Create Lines
		TCS_MRfQLine[] lines = rfq.getLines();
		
		//	@Stephan Generate Line Number
		int LineNo = 0;
		//
		
		for (TCS_MRfQLine line : lines)
		{
			if (!line.isActive())
				continue;
			
			//	Product on "Only" list
			if (subscriber != null 
				&& !subscriber.isIncluded(line.getM_Product_ID() ))
				continue;
			//
			if (get_ID() == 0)	//	save Response
				saveEx();

			TCS_MRfQResponseLine respLine = new TCS_MRfQResponseLine (this, line);
			respLine.setM_Product_ID(line.getM_Product_ID());
			respLine.setIsDescription(line.isDescription());
			respLine.setC_Charge_ID(line.getC_Charge_ID());
			respLine.setDescription(line.getDescription());
			respLine.setQty(line.getQty());
			respLine.setPriceActual(Env.ZERO);
			respLine.setPriceStd(Env.ZERO);
			//respLine.setFaktorKondisi(Env.ZERO);
			if (line.getM_Product_Category_ID() > 0) {
				respLine.setM_Product_Category_ID(line.getM_Product_Category_ID());
				MProductCategory productCategory = MProductCategory.get(getCtx(), line.getM_Product_Category_ID());
				if (productCategory.get_Value("FaktorKondisi") != null) {
					BigDecimal faktorKondisi = (BigDecimal) productCategory.get_Value("FaktorKondisi");
					respLine.setFaktorKondisi(faktorKondisi);
				} else {
					respLine.setFaktorKondisi(Env.ZERO);
				}
			}
			if (line.getProduct()!=null) {
				respLine.setProduct(line.getProduct());
			}
			if (line.getSize()!=null) {
				respLine.setSize(line.getSize());
			}
			respLine.setC_UOM_ID(line.getC_UOM_ID());
			respLine.setDescription(line.getDescription());
			respLine.setHelp(line.getHelp());
			respLine.setDateWorkStart(line.getDateWorkStart());
			respLine.setDateWorkComplete(line.getDateWorkComplete());
			respLine.setDeliveryDays(line.getDeliveryDays());
			//@Stephan
			respLine.setLineNo(LineNo+=10);
			if(line.get_ValueAsInt("AD_OrgTrx_ID") > 0){
				respLine.set_ValueOfColumn("AD_OrgTrx_ID", line.get_ValueAsInt("AD_OrgTrx_ID"));
			}
			//end here
			respLine.saveEx();
			//	line is not saved (dumped) if there are no Qtys 
		}
	}	//	MRfQResponse

	/**	underlying RfQ				*/
	private TCS_MRfQ				m_rfq = null;
	/** Lines						*/
	private TCS_MRfQResponseLine[]	m_lines = null;
	
	
	/**************************************************************************
	 * 	Get Response Lines
	 * 	@param requery requery
	 *	@return array of Response Lines
	 */
	public TCS_MRfQResponseLine[] getLines(boolean requery)
	{
		if (m_lines != null && !requery) {
			set_TrxName(m_lines, get_TrxName());
			return m_lines;
		}
		ArrayList<TCS_MRfQResponseLine> list = new ArrayList<TCS_MRfQResponseLine>();
		String sql = "SELECT * FROM C_RfQResponseLine "
			+ "WHERE C_RfQResponse_ID=? AND IsActive='Y'";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement (sql, get_TrxName());
			pstmt.setInt (1, getC_RfQResponse_ID());
			rs = pstmt.executeQuery ();
			while (rs.next ())
				list.add(new TCS_MRfQResponseLine(getCtx(), rs, get_TrxName()));
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, "getLines", e);
		}
		finally
		{
			DB.close(rs, pstmt);
			rs = null;
			pstmt = null;
		}
		
		m_lines = new TCS_MRfQResponseLine[list.size ()];
		list.toArray (m_lines);
		return m_lines;
	}	//	getLines
	
	/**
	 * 	Get Response Lines (no requery)
	 *	@return array of Response Lines
	 */
	public TCS_MRfQResponseLine[] getLines ()
	{
		return getLines (false);
	}	//	getLines
	
	
	/**
	 * 	Get RfQ
	 *	@return rfq
	 */
	public TCS_MRfQ getRfQ()
	{
		if (m_rfq == null)
			m_rfq = TCS_MRfQ.get (getCtx(), getC_RfQ_ID(), get_TrxName());
		return m_rfq;
	}	//	getRfQ
	
	/**
	 * 	String Representation
	 *	@return info
	 */
	public String toString ()
	{
		StringBuilder sb = new StringBuilder ("MRfQResponse[");
		sb.append(get_ID())
			.append(",Complete=").append(isComplete())
			.append(",Winner=").append(isSelectedWinner())
			.append ("]");
		return sb.toString ();
	}	//	toString
	
	
	/**************************************************************************
	 * 	Send RfQ
	 *	@return true if RfQ is sent per email.
	 */
	public boolean sendRfQ(MMailText mailText)
	{
		MUser to = MUser.get(getCtx(), getAD_User_ID());
		if (to.get_ID() == 0 || to.getEMail() == null || to.getEMail().length() == 0)
		{
			log.log(Level.SEVERE, "No User or no EMail - " + to);
			return false;
		}
		MClient client = MClient.get(getCtx());
		mailText.setBPartner(getC_BPartner_ID());
		
		StringBuilder message = new StringBuilder(mailText.getMailText(true));
		EMail email = client.createEMail(to.getEMail(), mailText.getMailHeader(), message.toString());
		if (mailText.isHtml())
			email.setMessageHTML(mailText.getMailHeader(), message.toString());
		else
		{
			email.setSubject (mailText.getMailHeader());
			email.setMessageText (message.toString());
		}
		if (!email.isValid() && !email.isValid(true))
		{
			log.warning("NOT VALID - " + email);
			to.setIsActive(false);
			to.addDescription("Invalid EMail");
			to.saveEx();
			return Boolean.FALSE;
		}
		email.addAttachment(createPDF());
		if (EMail.SENT_OK.equals(email.send()))
		{
			setDateInvited(new Timestamp (System.currentTimeMillis()));
			saveEx();
			return true;
		}
		return false;
	}	//	sendRfQ
	
	/**
	 * 	Create PDF
	 *	@return File or null
	 */
	public File createPDF ()
	{
		return createPDF (null);
	}	//	getPDF
	
	/**
	 * 	Create PDF file
	 *	@param file output file
	 *	@return File or null
	 */
	public File createPDF (File file)
	{
		MPrintFormat format = null;
		ReportEngine re = null;
		
		if (getC_RfQ().getC_RfQ_Topic().getAD_PrintFormat() != null) {
			format = (MPrintFormat) getC_RfQ().getC_RfQ_Topic().getAD_PrintFormat();
		} else {
			re = ReportEngine.get (getCtx(), ReportEngine.RFQ, getC_RfQResponse_ID());
			if (re == null)
				return null;
			
			format = re.getPrintFormat();
		}
		// We have a Jasper Print Format
		// ==============================
		if(format.getJasperProcess_ID() > 0)	
		{
			ProcessInfo pi = new ProcessInfo ("", format.getJasperProcess_ID());
			pi.setRecord_ID (getC_RfQResponse_ID());
			pi.setIsBatch(true);
			
			ServerProcessCtl.process(pi, null);
			
			return pi.getPDFReport();
		}
		// Standard Print Format (Non-Jasper)
		// ==================================
		return re.getPDF(file);
	}	//	getPDF

	
	/**************************************************************************
	 * 	Check if Response is Complete
	 *	@return null if complere - error message otherwise
	 */
	public String checkComplete()
	{
		if (isComplete())
			setIsComplete(false);
		TCS_MRfQ rfq = getRfQ();
		
		//	Is RfQ Total valid
		String error = rfq.checkQuoteTotalAmtOnly();
		if (error != null && error.length() > 0)
			return error;
		
		//	Do we have Total Amount ?
		if (rfq.isQuoteTotalAmt() || rfq.isQuoteTotalAmtOnly())
		{
			BigDecimal amt = getPrice();
			if (amt == null || Env.ZERO.compareTo(amt) >= 0)
				return "No Total Amount";
		}
		
		//	Do we have an amount/qty for all lines
		if (rfq.isQuoteAllLines())
		{
			TCS_MRfQResponseLine[] lines = getLines(false);
			for (int i = 0; i < lines.length; i++)
			{
				TCS_MRfQResponseLine line = lines[i];
				if (!line.isActive())
					return "Line " + line.getRfQLine().getLine()
						+ ": Not Active";
				boolean validAmt = false;
				TCS_MRfQResponseLineQty[] qtys = line.getQtys(false);
				for (int j = 0; j < qtys.length; j++)
				{
					TCS_MRfQResponseLineQty qty = qtys[j];
					if (!qty.isActive())
						continue;
					BigDecimal amt = qty.getNetAmt();
					if (amt != null && Env.ZERO.compareTo(amt) < 0)
					{
						validAmt = true;
						break;
					}
				}
				if (!validAmt)
					return "Line " + line.getRfQLine().getLine()
						+ ": No Amount";
			}
		}
		
		//	Do we have an amount for all line qtys
		if (rfq.isQuoteAllQty())
		{
			TCS_MRfQResponseLine[] lines = getLines(false);
			for (int i = 0; i < lines.length; i++)
			{
				TCS_MRfQResponseLine line = lines[i];
				TCS_MRfQResponseLineQty[] qtys = line.getQtys(false);
				for (int j = 0; j < qtys.length; j++)
				{
					TCS_MRfQResponseLineQty qty = qtys[j];
					if (!qty.isActive())
						return "Line " + line.getRfQLine().getLine()
						+ " Qty=" + qty.getRfQLineQty().getQty()
						+ ": Not Active";
					BigDecimal amt = qty.getNetAmt();
					if (amt == null || Env.ZERO.compareTo(amt) >= 0)
						return "Line " + line.getRfQLine().getLine()
							+ " Qty=" + qty.getRfQLineQty().getQty()
							+ ": No Amount";
				}
			}
		}
		
		setIsComplete(true);
		return null;
	}	//	checkComplete
	
	/**
	 * 	Is Quote Total Amt Only
	 *	@return true if only Total
	 */
	public boolean isQuoteTotalAmtOnly()
	{
		return getRfQ().isQuoteTotalAmtOnly();
	}	//	isQuoteTotalAmtOnly
	
	/**
	 * 	Before Save
	 *	@param newRecord new
	 *	@return true
	 */
	protected boolean beforeSave (boolean newRecord)
	{
		//	Calculate Complete Date (also used to verify)
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

}	//	MRfQResponse
