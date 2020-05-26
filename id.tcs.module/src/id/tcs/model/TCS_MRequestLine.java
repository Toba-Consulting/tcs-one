package id.tcs.model;

import java.sql.ResultSet;
import java.util.Properties;

import org.compiere.model.MRequest;

public class TCS_MRequestLine extends X_TCS_R_RequestLine{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2877385930571567152L;

	public TCS_MRequestLine(Properties ctx, int R_RequestMaterial_ID,
			String trxName) {
		super(ctx, R_RequestMaterial_ID, trxName);
		// TODO Auto-generated constructor stub
	}
	
	public TCS_MRequestLine(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
		// TODO Auto-generated constructor stub
	}

	public TCS_MRequestLine (MRequest mRequest)
	{
		this (mRequest.getCtx(), 0, mRequest.get_TrxName());
		setClientOrg(mRequest);
		setR_Request_ID(mRequest.getR_Request_ID());
	}
	
}
