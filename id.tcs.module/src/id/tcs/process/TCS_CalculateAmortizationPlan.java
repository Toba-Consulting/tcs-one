package id.tcs.process;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;

import org.adempiere.exceptions.AdempiereException;
import org.compiere.model.MPeriod;
import org.compiere.model.Query;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.TimeUtil;

import id.tcs.model.MTCSAmortizationLine;
import id.tcs.model.MTCSAmortizationPlan;

/**
 * @author Stephan
 * Generate plan detail amortization
 */
public class TCS_CalculateAmortizationPlan extends SvrProcess{

	@Override
	protected void prepare() {
		
	}

	@Override
	protected String doIt() throws Exception {
		
		int AmortizationPlan_ID = getRecord_ID();
		MTCSAmortizationPlan amortizationPlan = new MTCSAmortizationPlan(getCtx(), AmortizationPlan_ID, get_TrxName());
		
		BigDecimal totalAmt = amortizationPlan.getTotalAmt();
		int stdPrecision = amortizationPlan.getStdPrecision().intValue();
		int numberOfPeriod = amortizationPlan.getamortizationperiod();
		
		if(totalAmt.compareTo(Env.ZERO) == 0)
			throw new AdempiereException("Total Amount cant be zero");
		
		if(numberOfPeriod == 0)
			throw new AdempiereException("Amortization Period cant be zero");
		
		//if(amortizationPlan.getLines().length > 0)
			//throw new AdempiereException("Amortization Line not null");
		
		String whereClause = MTCSAmortizationLine.COLUMNNAME_Processed+"='Y' AND "+MTCSAmortizationLine.COLUMNNAME_TCS_AmortizationPlan_ID+"="+AmortizationPlan_ID;
		boolean match = new Query(getCtx(), MTCSAmortizationLine.Table_Name, whereClause, get_TrxName())
						.match();
		if(match)
			throw new AdempiereException("There's a processed line");
		
		cleanAmortizationLine();
		
		BigDecimal amt = totalAmt.divide(new BigDecimal(numberOfPeriod), stdPrecision, RoundingMode.HALF_UP);
		BigDecimal defaultAmt = amt;
		
		int C_Period_ID = amortizationPlan.getC_Period_ID();
		MPeriod nextPeriod = new MPeriod(getCtx(), C_Period_ID, get_TrxName());
		
		//@phie
		int start_period_id = amortizationPlan.get_ValueAsInt("Start_Period_ID");
		if(start_period_id == 0)
			start_period_id = C_Period_ID;
		
		MPeriod startPeriod = new MPeriod(getCtx(), start_period_id, get_TrxName());
		
		if(startPeriod.getStartDate().before(nextPeriod.getStartDate()))
			throw new AdempiereException("Period Start Cannot Before Amortization Period");
		
		//convert start period to startPeriod_no 
		int startPeriod_no = 1;	
		MPeriod TempAmortizationPeriod = new MPeriod(getCtx(), C_Period_ID, get_TrxName());	
		while(true)
		{
			if(TempAmortizationPeriod.getStartDate().equals(startPeriod.getStartDate()))
				break;
			
			Timestamp nextDate = TempAmortizationPeriod.getEndDate();
			nextDate = TimeUtil.addDays(nextDate, 1);
			TempAmortizationPeriod = MPeriod.get(getCtx(), nextDate, 0, get_TrxName());
			
			startPeriod_no++;
		}//end convert start period to startPeriod_no
		
		if(startPeriod_no > numberOfPeriod)
			throw new AdempiereException("Your period start "+startPeriod.getName()+ 
					" is "+startPeriod_no+" month(s) after this amortization period, but your amortization period just "+numberOfPeriod);
		//end phie
		
		//create amortization line
		for(int i=1; i <= numberOfPeriod; i++){
			//@phie 
			if(startPeriod_no > 1 && i < startPeriod_no-1)//skip line but keep do counter period
			{
				Timestamp nextDate = nextPeriod.getEndDate();
				nextDate = TimeUtil.addDays(nextDate, 1);
				nextPeriod = MPeriod.get(getCtx(), nextDate, 0, get_TrxName());
				continue;
			}
			
			
			if(startPeriod_no > 1 && i == startPeriod_no-1)
				amt = amt.multiply(new BigDecimal(startPeriod_no-1)); //calculate amt for line adjusment
			//end phie	
			
			if(i == numberOfPeriod){
				amt = totalAmt.subtract(defaultAmt.multiply(new BigDecimal(numberOfPeriod-1)));
			}
			
			MTCSAmortizationLine line = new MTCSAmortizationLine(getCtx(), 0, get_TrxName());
			line.setAD_Org_ID(amortizationPlan.getAD_Org_ID());
			line.setTCS_AmortizationPlan_ID(AmortizationPlan_ID);
			//@phie
			if(startPeriod_no > 1 && i == startPeriod_no-1){
				line.setDateAcct(startPeriod.getEndDate()); //set line adjusment date acct with end date of start period
				line.setC_Period_ID(startPeriod.getC_Period_ID());
			}
			else{
				line.setDateAcct(nextPeriod.getEndDate());
				line.setC_Period_ID(nextPeriod.getC_Period_ID());
			}
				//end phie
			line.setC_BPartner_ID(amortizationPlan.getC_BPartner_ID());
			line.setC_SalesRegion_ID(amortizationPlan.getC_SalesRegion_ID());
			if((startPeriod_no > 1 && i == startPeriod_no-1) || (i == numberOfPeriod)){
				line.setAmtAcct(amt);
				
			}else{
				line.setAmtAcct(defaultAmt);
			}
			line.setC_Currency_ID(amortizationPlan.getC_Currency_ID());
			line.setC_LocFrom_ID(amortizationPlan.getC_LocFrom_ID());
			line.setC_LocTo_ID(amortizationPlan.getC_LocTo_ID());
			line.setM_Product_ID(amortizationPlan.getM_Product_ID());
			line.setDebit_Account_Acct(amortizationPlan.getDebit_Account_Acct());
			line.setCredit_Account_Acct(amortizationPlan.getCredit_Account_Acct());
			line.setC_SubAcct_ID(amortizationPlan.getC_SubAcct_ID());
			line.setDescription(amortizationPlan.getDescription());
			//line.setC_Period_ID(nextPeriod.getC_Period_ID());
			line.saveEx();
			
			//  add date with number of days in month
			Timestamp nextDate = nextPeriod.getEndDate();
			nextDate = TimeUtil.addDays(nextDate, 1);
			nextPeriod = MPeriod.get(getCtx(), nextDate, 0, get_TrxName());
			
			if(nextPeriod == null)//TODO throw?
				log.severe("No Standard Period For "+nextDate);
		}
		
		return "Created "+numberOfPeriod;
	}
	
	public void cleanAmortizationLine(){
		StringBuilder sb = new StringBuilder();
		sb.append("DELETE FROM "+MTCSAmortizationLine.Table_Name+" WHERE "+
					MTCSAmortizationLine.COLUMNNAME_TCS_AmortizationPlan_ID +" = "+getRecord_ID());
		int no = DB.executeUpdate(sb.toString(), get_TrxName());
		log.info("DELETED Amortization Line#"+no);
	}

	/*
	public List<MPeriod> getListOfPeriod(MPeriod firstPeriod, int numberOfPeriod, int AD_Org_ID){
		
		List<MPeriod> listPeriod = new ArrayList<>();
		listPeriod.add(firstPeriod);
		
		Timestamp date = firstPeriod.getEndDate();
		
		for(int i=0; i < numberOfPeriod-1 ; i++){
			
			date = TimeUtil.addDays(date, 1);
			
			MPeriod nextPeriod = MPeriod.get(getCtx(), date, AD_Org_ID, get_TrxName());
			listPeriod.add(nextPeriod);
			
			date = nextPeriod.getEndDate();
		}
		
		return listPeriod;
	}
	*/
}
