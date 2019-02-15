package id.tcs.process;

import id.tcs.model.TCS_MWithholdingCalc;
import id.tcs.model.TCS_MWithholdingCalcLine;
import id.tcs.model.TCS_MWithholdingRate;
import id.tcs.model.TCS_MWithholdingType;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.Level;

import org.adempiere.exceptions.AdempiereException;
import org.compiere.model.MBPartner;
import org.compiere.model.MConversionRate;
import org.compiere.model.MInvoice;
import org.compiere.model.MPeriod;
import org.compiere.model.MWithholding;
import org.compiere.model.Query;
import org.compiere.model.X_I_Conversion_Rate;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;

public class TCS_CalcWithholding extends SvrProcess{

	private int p_TCS_WithholdingType_ID = 0;
	private int p_C_Period_ID = 0;
	
	
	@Override
	protected void prepare() {
		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++)
		{
			String name = para[i].getParameterName();
			if (para[i].getParameter() == null)
				;
			else if (name.equals("TCS_WithholdingType_ID"))
				p_TCS_WithholdingType_ID = para[i].getParameterAsInt();
			else if (name.equals("C_Period_ID"))
				p_C_Period_ID = para[i].getParameterAsInt();
			
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
			
	}

	@Override
	protected String doIt() throws Exception {
		
		if (p_TCS_WithholdingType_ID==0) {
			throw new AdempiereException("Withholding Type Is Mandatory");
		}
		
		if (p_C_Period_ID==0) {
			throw new AdempiereException("Period Is Mandatory");			
		}

		if (!checkIsMostRecentPeriod(p_C_Period_ID)) {
			throw new AdempiereException("Withholding Calculation With More Recent Period Exist");
		}

		TCS_MWithholdingType holdType = new TCS_MWithholdingType(getCtx(), p_TCS_WithholdingType_ID, get_TrxName());
		MPeriod period = new MPeriod(getCtx(), p_C_Period_ID, get_TrxName());
		TCS_MWithholdingRate [] rate = holdType.getLines();
		int rateSeq=0;
		
		StringBuilder sbHead = new StringBuilder();
		sbHead.append("EXISTS( SELECT 1 FROM C_Invoice ci");
		sbHead.append(" JOIN C_InvoiceLine cil on cil.C_Invoice_ID=ci.C_Invoice_ID");
		sbHead.append(" JOIN Fact_Acct fa on fa.AD_Table_ID="+MInvoice.Table_ID+" AND fa.Record_ID=ci.C_Invoice_ID AND fa.Line_ID=cil.C_InvoiceLine_ID");
		sbHead.append(" WHERE ci.DocStatus IN ('CO','CL') ");
		sbHead.append(" AND ci.IsSoTrx='N'");
		sbHead.append(" AND cil.C_Charge_ID="+holdType.getC_Charge_ID());		
		sbHead.append(" AND fa.DateAcct BETWEEN '"+period.getStartDate()+"' AND '"+period.getEndDate()+"'");
		sbHead.append(" AND ci.C_BPartner_ID=C_Bpartner.C_BPartner_ID)");
		
		int [] C_BParnterIDs = new Query(getCtx(), MBPartner.Table_Name, sbHead.toString(), get_TrxName()).setOrderBy("Value").getIDs();
		
		BigDecimal pct50=new BigDecimal(0.5);
		BigDecimal pct200=new BigDecimal(2);
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(period.getEndDate().getTime());
		int a=calendar.get(Calendar.YEAR);
		for (int C_BParnterID : C_BParnterIDs) {

			String sqlMostRecentCalcIDInYear = 
					 "SELECT TCS_WithholdingCalc_ID FROM TCS_WithholdingCalc twc"
					+" JOIN C_Period cp on twc.C_Period_ID=cp.C_Period_ID"
					+" WHERE twc.C_BPartner_ID="+C_BParnterID
					+" AND Date_Part('YEAR', cp.EndDate)="+calendar.get(Calendar.YEAR)
					+" ORDER BY cp.EndDate Desc";
//					+" LIMIT 1";
			
			int MostRecentCalcID=DB.getSQLValue(get_TrxName(), sqlMostRecentCalcIDInYear);
			
			BigDecimal accumBeggining=Env.ZERO;
			if (MostRecentCalcID>0) {
				TCS_MWithholdingCalc MostRecentCalc = new TCS_MWithholdingCalc(getCtx(), MostRecentCalcID, get_TrxName());
				accumBeggining = MostRecentCalc.getAccumulatedAmt();			
			}
			
			
			TCS_MWithholdingCalc head = new TCS_MWithholdingCalc(getCtx(), 0, get_TrxName());
			head.setC_Period_ID(p_C_Period_ID);
			head.setTCS_WithholdingType_ID(p_TCS_WithholdingType_ID);
			head.setC_BPartner_ID(C_BParnterID);
			head.setAccumulatedAmt(accumBeggining);
			head.saveEx();
		
			 while (accumBeggining.compareTo(rate[rateSeq].getMaxValue())>0){
				 rateSeq++;
			 }
			
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			
			StringBuilder sbLine = new StringBuilder();
			sbLine.append("SELECT ci.C_Invoice_ID, (fa.amtacctdr+fa.amtacctcr) AS Amt, fa.DateAcct");
			sbLine.append(" FROM C_Invoice ci");
			sbLine.append(" JOIN C_InvoiceLine cil on cil.C_Invoice_ID=ci.C_Invoice_ID");
			sbLine.append(" JOIN Fact_Acct fa on fa.AD_Table_ID="+MInvoice.Table_ID+" AND fa.Record_ID=ci.C_Invoice_ID AND fa.Line_ID=cil.C_InvoiceLine_ID");
			sbLine.append(" WHERE ci.DocStatus IN ('CO','CL') ");
			sbLine.append(" AND ci.IsSoTrx='N'");
			sbLine.append(" AND cil.C_Charge_ID="+holdType.getC_Charge_ID());		
			sbLine.append(" AND fa.DateAcct BETWEEN '"+period.getStartDate()+"' AND '"+period.getEndDate()+"'");
			sbLine.append(" AND ci.C_BPartner_ID="+C_BParnterID);
			sbLine.append(" ORDER BY fa.DateAcct");
			
			int SeqNo=1;
			
			try
			{
				pstmt = DB.prepareStatement(sbLine.toString(), get_TrxName());
				rs = pstmt.executeQuery();
				while (rs.next())
				{
					BigDecimal Amt = rs.getBigDecimal("Amt");
					BigDecimal HalvedAmt = Amt.multiply(pct50);					
					BigDecimal uncalculatedDPP = HalvedAmt;
					BigDecimal calcLineDPP=Env.ZERO;
					boolean nextRate = false;
					
					while (uncalculatedDPP.compareTo(Env.ZERO)>0) {
						
						if (head.getAccumulatedAmt().add(uncalculatedDPP).compareTo(rate[rateSeq].getMaxValue())>0) {
							calcLineDPP=rate[rateSeq].getMaxValue().subtract(head.getAccumulatedAmt());
							nextRate=true;
							
							if (rateSeq+2>rate.length) {
									throw new AdempiereException("Withholding Type ("+holdType.getName()+") Have No Withholding Rate For Value Over "+rate[rateSeq].getMaxValue());
							}
						}
						else {
							calcLineDPP=uncalculatedDPP;
						}
						
						TCS_MWithholdingCalcLine calcLine = new TCS_MWithholdingCalcLine(getCtx(), 0, get_TrxName());
						calcLine.setTCS_WithholdingCalc_ID(head.getTCS_WithholdingCalc_ID());
						calcLine.setC_Invoice_ID(rs.getInt("C_Invoice_ID"));
						calcLine.setAmt(Amt);
						calcLine.setHalvedAmt(HalvedAmt);
						calcLine.setDPP(calcLineDPP);
						calcLine.setAccumulatedAmt(head.getAccumulatedAmt().add(calcLineDPP));
						calcLine.setRate(rate[rateSeq].getRate());
						calcLine.setDateAcct(rs.getTimestamp("DateAcct"));
						
						BigDecimal pphRate = calcLine.getRate().divide(Env.ONEHUNDRED);
						calcLine.setPPh(calcLine.getDPP().multiply(pphRate));
												
						calcLine.saveEx();
						
						uncalculatedDPP=uncalculatedDPP.subtract(calcLineDPP);
						calcLineDPP=Env.ZERO;
						if (nextRate) {
							rateSeq++;
						}
						
						SeqNo++;
						
				    	if (!invoiceAlreadyExist(head.getTCS_WithholdingCalc_ID(), calcLine.getC_Invoice_ID())) {
				    		
				    		head.setAmt(head.getAmt().add(calcLine.getAmt()));
				    		head.setHalvedAmt(head.getHalvedAmt().add(calcLine.getHalvedAmt()));
						}
				    	
				    	head.setDPP(head.getDPP().add(calcLine.getDPP()));
				    	head.setAccumulatedAmt(head.getAccumulatedAmt().add(calcLine.getDPP()));
				    	head.setPPh(head.getPPh().add(calcLine.getPPh()));
				    	head.saveEx();
					}
				}
			}
			catch (Exception e)
			{
				log.log(Level.SEVERE, sbLine.toString(), e);
			}
			finally
			{
				DB.close(rs, pstmt);
				rs = null;
				pstmt = null;
			}
		
			rateSeq=0;
		}
		return "Success";
	}

/*
 Changed to check by period only
 
	private boolean checkIsMostRecentPeriod(int C_BPartner_ID, int C_Period_ID){
		
		String sqlCheck = 
		"SELECT TCS_WithholdingCalc_ID FROM TCS_WithholdingCalc twc"		
		+" JOIN C_Period cp on twc.C_Period_ID=cp.C_Period_ID"
		+" WHERE twc.C_BPartner_ID="+C_BPartner_ID
		+" ORDER BY cp.EndDate Desc ";
//		+" LIMIT 1";
		
		int MostRecentWitholdingCalcID=DB.getSQLValue(get_TrxName(), sqlCheck);
		
		//is first WithholdingCalc of BP
		if (MostRecentWitholdingCalcID<=0) {
			return true;
		}
	
		TCS_MWithholdingCalc wth =  new TCS_MWithholdingCalc(getCtx(), MostRecentWitholdingCalcID, get_TrxName());

		//Is most recent period, but WithholdingCalc already exist, delete existing to make a new one
		if (wth.getC_Period_ID()==C_Period_ID) {
			String sqlDelete="DELETE FROM TCS_WithholdingCalcLine WHERE TCS_WithholdingCalc_ID="+wth.getTCS_WithholdingCalc_ID();
			DB.executeUpdate(sqlDelete);
			wth.deleteEx(false);
			return true;
		}
		
		MPeriod MostRecentPeriod = new MPeriod(getCtx(), wth.getC_Period_ID(), get_TrxName());
		MPeriod NewPeriod = new MPeriod(getCtx(), C_Period_ID, get_TrxName());
		
		//Is new most recent		
		if (NewPeriod.getEndDate().after(MostRecentPeriod.getEndDate())) {
			return true;
		}
		
		MBPartner bp = new MBPartner(getCtx(), C_BPartner_ID, get_TrxName());
		throw new AdempiereException("BP "+bp.getValue()+" - "+bp.getName()+" Already Has More Recent Withholding Calculation Than This Period");
		
	}
*/
	
	private boolean checkIsMostRecentPeriod(int C_Period_ID){
		
		String sqlCheck = 
		"SELECT TCS_WithholdingCalc_ID FROM TCS_WithholdingCalc twc"		
		+" JOIN C_Period cp on twc.C_Period_ID=cp.C_Period_ID"
		+" ORDER BY cp.EndDate Desc ";
		
		int MostRecentWitholdingCalcID=DB.getSQLValue(get_TrxName(), sqlCheck);
		
		//is first WithholdingCalc of BP
		if (MostRecentWitholdingCalcID<=0) {
			return true;
		}
	
		TCS_MWithholdingCalc wth =  new TCS_MWithholdingCalc(getCtx(), MostRecentWitholdingCalcID, get_TrxName());

		//Is most recent period, but WithholdingCalc already exist, delete existing to make a new one
		if (wth.getC_Period_ID()==C_Period_ID) {
			String sqlDeleteLine="DELETE FROM TCS_WithholdingCalcLine wcl "+
								"WHERE TCS_WithholdingCalc_ID IN "+
									"(SELECT TCS_WithholdingCalc_ID FROM TCS_WithholdingCalc "+""
									+ "WHERE C_Period_ID="+C_Period_ID+")";
			String sqlDeleteHead="DELETE FROM TCS_WithholdingCalc WHERE C_Period_ID="+C_Period_ID;
			DB.executeUpdate(sqlDeleteLine);
			DB.executeUpdate(sqlDeleteHead);
			
			return true;
		}
		
		MPeriod MostRecentPeriod = new MPeriod(getCtx(), wth.getC_Period_ID(), get_TrxName());
		MPeriod NewPeriod = new MPeriod(getCtx(), C_Period_ID, get_TrxName());
		
		//Is new most recent		
		if (NewPeriod.getEndDate().after(MostRecentPeriod.getEndDate())) {
			return true;
		}
		
		return false;		
	}
	
    private boolean invoiceAlreadyExist(int TCS_MWithholdingCalc_ID, int C_Invoice_ID){
    	
    	String sql = "C_Invoice_ID="+C_Invoice_ID+" AND TCS_WithholdingCalc_ID="+TCS_MWithholdingCalc_ID;
    	boolean Exist = new Query(getCtx(), TCS_MWithholdingCalcLine.Table_Name, sql, get_TrxName()).match();
    	
    	return Exist;
    }
}