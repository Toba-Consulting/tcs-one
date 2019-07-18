package id.tcs.model;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;

import org.compiere.model.MOrderLine;
import org.compiere.model.PO;
import org.compiere.model.Query;
import org.compiere.util.DB;
import org.compiere.util.Msg;

public class MInquiry extends X_C_Inquiry{


	/**
	 * 
	 */
	private static final long serialVersionUID = 372596490918656687L;


	public MInquiry(Properties ctx, int C_Inquiry_ID, String trxName) {
		super(ctx, C_Inquiry_ID, trxName);
	}
	

	public MInquiry(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	@Override
	protected boolean beforeDelete()
	{
		String sql = "DELETE FROM M_MatchInquiry WHERE C_Inquiry_ID="+get_ID();
		DB.executeUpdate(sql, get_TrxName());
		
		String sql2 = "DELETE FROM M_MatchQuotation WHERE C_Inquiry_ID="+get_ID();
		DB.executeUpdate(sql2, get_TrxName());
		
		String sql3 = "DELETE FROM M_MatchRequest WHERE C_Inquiry_ID="+get_ID();
		DB.executeUpdate(sql3, get_TrxName());
		
		return true;
	}
	
	@Override
	protected boolean beforeSave(boolean newRecord)
	{
		if (getAD_Org_ID() == 0) {
			log.saveError("Error", Msg.getMsg(getCtx(), "CannotUseOrg*"));
			return false;
		}
				
		return true;
	}
	
	public static MInquiry copyFrom (MInquiry from,	String trxName)
	{
		MInquiry to = new MInquiry (from.getCtx(), 0, trxName);
		to.set_TrxName(trxName);
		PO.copyValues(from, to, from.getAD_Client_ID(), from.getAD_Org_ID());
		to.set_ValueNoCheck ("C_Inquiry_ID", I_ZERO);
		to.set_ValueNoCheck ("DocumentNo", from.getDocumentNo()+"R");
		to.setAddress(from.getAddress());
		to.setC_BPartner_ID(from.getC_BPartner_ID());
		to.setC_BPartner_Location_ID(from.getC_BPartner_Location_ID());
		to.setC_RfQ_ID(0);
		to.setCustomerType(from.getCustomerType());
		to.setDateOrdered(from.getDateOrdered());
		to.setDescription(from.getDescription());
		to.setInquiryStatus(from.getInquiryStatus());
		to.setInquirySubject(from.getInquirySubject());
		to.setIsNewCustomer(from.isNewCustomer());
		to.setM_PriceList_ID(from.getM_PriceList_ID());
		to.setName(from.getName());
		to.setReferenceType(from.getReferenceType());
		to.setSalesRep_ID(from.getSalesRep_ID());
		to.set_ValueOfColumn("DocStatus", from.get_ValueAsString("DocStatus"));
		if (!to.save(trxName))
			throw new IllegalStateException("Could not create Inquiry");
	
		//
		//
		//
		
		if (to.copyLinesFrom(from) == 0)
			throw new IllegalStateException("Could not create Inquiry Lines");

		return to;
	}	//	copyFrom
	
	public int copyLinesFrom (MInquiry otherInquiry)
	{
		if (otherInquiry == null)
			return 0;
		
		String sql = "SELECT MAX(LineNo) FROM C_InquiryLine WHERE C_Inquiry_ID=?";
		int no = DB.getSQLValueEx(get_TrxName(), sql, getC_Inquiry_ID());
		
		MInquiryLine[] fromLines = otherInquiry.getLines();
		int count = 0;
		for (MInquiryLine fromLine: fromLines)
		{
			MInquiryLine line = new MInquiryLine (this);
			PO.copyValues(fromLine, line, getAD_Client_ID(), getAD_Org_ID());
			line.setC_Inquiry_ID(getC_Inquiry_ID());
			//
			line.set_ValueNoCheck ("C_InquiryLine_ID", I_ZERO);	//	new
			line.setC_Charge_ID(fromLine.getC_Charge_ID());
			line.set_ValueNoCheck ("C_RfQLine_ID", null);	//	new
			line.setC_UOM_ID(fromLine.getC_UOM_ID());
			line.setDescription(fromLine.getDescription());
			line.setIsNewProduct(fromLine.isNewProduct());
			line.setLineNo(fromLine.getLineNo() + no);
			line.setM_Product_ID(fromLine.getM_Product_ID());
			line.set_ValueNoCheck ("M_Product_Category_ID", fromLine.get_ValueAsInt("M_Product_Category_ID"));	//	new
			line.setProduct(fromLine.getProduct());
			line.setQty(fromLine.getQty());
			line.setPriceActual(fromLine.getPriceActual());
			line.setPriceEntered(fromLine.getPriceEntered());
			line.setPriceList(fromLine.getPriceList());
			line.set_ValueNoCheck ("Size", fromLine.get_ValueAsString("Size"));	//	new
			//
			
			line.setProcessed(false);
			if (line.save(get_TrxName()))
				count++;
		}
		if (fromLines.length != count)
			log.log(Level.SEVERE, "Line difference - From=" + fromLines.length + " <> Saved=" + count);
		return count;
	}	//	copyLinesFrom
	
	public MInquiryLine[] getLines() {

		StringBuilder whereClause = new StringBuilder(I_C_InquiryLine.COLUMNNAME_C_Inquiry_ID).append("=?");	

		List<MInquiryLine> list = new Query(getCtx(),MInquiryLine.Table_Name, whereClause.toString(), get_TrxName())
				.setParameters(getC_Inquiry_ID())
				.setOnlyActiveRecords(true)
				.list();
		
		MInquiryLine [] m_inquiry = new MInquiryLine[list.size()];
		list.toArray(m_inquiry);
		return m_inquiry;
		
		/*
		MInquiryLine[] m_inquiry = list.toArray(new MInquiryLine[list.size()]);
		return m_inquiry;
		*/
	}

	
	public MInquiryLine[] getLines(boolean isNewProduct) {

		StringBuilder whereClause = new StringBuilder(I_C_InquiryLine.COLUMNNAME_C_Inquiry_ID);
		whereClause.append("=? AND " ).append(I_C_InquiryLine.COLUMNNAME_IsNewProduct).append("=?");
		
		ArrayList<Object> params = new ArrayList<Object>();
		params.add(get_ID());
		params.add(isNewProduct);
		
		List<MInquiryLine> list = new Query(getCtx(),MInquiryLine.Table_Name, whereClause.toString(), get_TrxName())
				.setParameters(params)
				.setOnlyActiveRecords(true)
				.list();
		
		MInquiryLine[] m_inquiry = new MInquiryLine[list.size()];
		list.toArray(m_inquiry);
		return m_inquiry;
	}

	public boolean hasMatchQuotation() {

		final String whereClause = I_M_MatchQuotation.COLUMNNAME_C_Inquiry_ID + "=?";
		boolean match = new Query(getCtx(),X_M_MatchQuotation.Table_Name, whereClause, get_TrxName())
				.setParameters(get_ID())
				.match();

		return match;
	}

	public boolean hasMatchRfQ() {

		final String whereClause = I_M_MatchQuotation.COLUMNNAME_C_Inquiry_ID + "=?";
		boolean match = new Query(getCtx(),X_M_MatchInquiry.Table_Name, whereClause, get_TrxName())
				.setParameters(get_ID())
				.match();

		return match;
	}
}
