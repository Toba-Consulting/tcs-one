package id.tcs.process;


import org.adempiere.exceptions.AdempiereException;
import org.compiere.model.MPeriod;
import id.tcs.model.MTCSAmortizationLine;
import id.tcs.model.MTCSAmortizationRun;
import org.compiere.model.Query;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.CLogger;
/**
 * @author Stephan
 * copy from amortization line to amortization run
 */
public class GenerateAmortizationPlan extends SvrProcess{

	private CLogger log = CLogger.getCLogger(GenerateAmortizationPlan.class);
	
	@Override
	protected void prepare() {
		
	}

	@Override
	protected String doIt() throws Exception {
		
		int AmortizationRun_ID = getRecord_ID();
		MTCSAmortizationRun amortizationRun = new MTCSAmortizationRun(getCtx(), AmortizationRun_ID, get_TrxName());
		
		int C_Period_ID = amortizationRun.getC_Period_ID();
		
		if(C_Period_ID <= 0)
			throw new AdempiereException("Please Select Period");
		
		updateAmortizationLine(AmortizationRun_ID);
		
		//String where = MPeriod.COLUMNNAME_C_Period_ID+"=? AND "+MTCSAmortizationRun.COLUMNNAME_TCS_AmortizationRun_ID+" IS NULL";
		String where = MPeriod.COLUMNNAME_C_Period_ID+"=? AND "+MTCSAmortizationLine.COLUMNNAME_Processed+"='N'";
		int[] line_IDs = new Query(getCtx(), MTCSAmortizationLine.Table_Name, where, get_TrxName())
			.setClient_ID()
			.setOnlyActiveRecords(true)
			.setParameters(C_Period_ID)
			.getIDs();
		
		//BigDecimal totalLines = Env.ZERO;
		for (int line_ID : line_IDs) {
			MTCSAmortizationLine line = new MTCSAmortizationLine(getCtx(), line_ID, get_TrxName());
			line.setTCS_AmortizationRun_ID(AmortizationRun_ID);
			//line.setProcessed(true);
			line.saveEx();
			
			//totalLines = totalLines.add(line.getAmtAcct());
		}
		
		//amortizationRun.setTotalLines(totalLines);
		//amortizationRun.setGrandTotal(totalLines);
		//amortizationRun.saveEx();
		
		return "";
	}
	
	public void updateAmortizationLine(int AmortizationRun_ID){
		StringBuilder sb = new StringBuilder();
		sb.append("UPDATE "+MTCSAmortizationLine.Table_Name+" SET "+MTCSAmortizationLine.COLUMNNAME_TCS_AmortizationRun_ID+" = NULL WHERE "+
					MTCSAmortizationLine.COLUMNNAME_TCS_AmortizationRun_ID +" = "+AmortizationRun_ID);
		int no = DB.executeUpdate(sb.toString(), get_TrxName());
		log.info("Updated Amortization Line#"+no);
	}

}
