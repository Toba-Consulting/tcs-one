package id.tcs.model;

import java.sql.ResultSet;
import java.util.List;
import java.util.Properties;

import org.compiere.model.I_C_OrderLine;
import org.compiere.model.I_M_InOutLine;
import org.compiere.model.MInOutLine;
import org.compiere.model.MOrderLine;
import org.compiere.model.Query;
import org.compiere.util.DB;
import org.compiere.util.Util;

public class TCS_MWithholdingType extends X_TCS_WithholdingType{

	protected TCS_MWithholdingRate[] 	m_lines = null;
	
	public TCS_MWithholdingType(Properties ctx, int TCS_WithholdingType_ID, String trxName) {
		super(ctx, TCS_WithholdingType_ID, trxName);
	}

    public TCS_MWithholdingType (Properties ctx, ResultSet rs, String trxName)
    {
      super (ctx, rs, trxName);
    }
    
    public TCS_MWithholdingRate [] getLines(){
    	
    	int [] withholdingRatesIDs = new Query(getCtx(), TCS_MWithholdingRate.Table_Name, "TCS_WithholdingType_ID=?", get_TrxName())
    			.setParameters(getTCS_WithholdingType_ID())
    			.setOrderBy(TCS_MWithholdingRate.COLUMNNAME_MaxAmt)
    			.getIDs();
    	
    	TCS_MWithholdingRate [] array = new TCS_MWithholdingRate[withholdingRatesIDs.length];
    	for (int i = 0; i < withholdingRatesIDs.length; i++) {
			array[i] = new TCS_MWithholdingRate(getCtx(), withholdingRatesIDs[i], get_TrxName());			
		}
    	
    	return array;
	}
    
    
    
}
