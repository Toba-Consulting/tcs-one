package id.tcs.model;

import java.sql.ResultSet;
import java.util.Properties;

import org.compiere.util.DB;

public class MInquiryLine extends X_C_InquiryLine {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4238974571248771709L;

	public MInquiryLine(Properties ctx, int C_InquiryLine_ID, String trxName) {
		super(ctx, C_InquiryLine_ID, trxName);
	}

	public MInquiryLine(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	public MInquiryLine(MInquiry mInquiry) {
		this(mInquiry.getCtx(), 0, mInquiry.get_TrxName());
		setClientOrg(mInquiry);
		setC_Inquiry_ID(mInquiry.get_ID());
	}
	
	

	@Override
	protected boolean beforeSave(boolean newRecord) {
		if (newRecord || is_ValueChanged(MInquiryLine.COLUMNNAME_IsNewProduct)
				|| is_ValueChanged(MInquiryLine.COLUMNNAME_M_Product_ID) || is_ValueChanged(MInquiryLine.COLUMNNAME_C_Charge_ID))
		if (!isNewProduct() && getM_Product_ID() == 0 && getC_Charge_ID() == 0)
			return false;
		
		return super.beforeSave(newRecord);
	}

	@Override
	protected boolean beforeDelete()
	{
		String sql = "DELETE FROM M_MatchInquiry WHERE C_InquiryLine_ID="+get_ID();
		DB.executeUpdate(sql, get_TrxName());
		
		String sql2 = "DELETE FROM M_MatchQuotation WHERE C_InquiryLine_ID="+get_ID();
		DB.executeUpdate(sql2, get_TrxName());
		
		String sql3 = "DELETE FROM M_MatchRequest WHERE C_InquiryLine_ID="+get_ID();
		DB.executeUpdate(sql3, get_TrxName());
		
		return true;
	}
	
}
