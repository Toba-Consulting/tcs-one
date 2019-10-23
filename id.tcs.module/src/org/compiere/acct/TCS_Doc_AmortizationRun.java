package org.compiere.acct;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.logging.Level;

import org.compiere.model.MAccount;
import org.compiere.model.MAcctSchema;
import id.tcs.model.MTCSAmortizationLine;
import id.tcs.model.MTCSAmortizationRun;
import org.compiere.util.Env;

public class TCS_Doc_AmortizationRun extends Doc{

	

	public TCS_Doc_AmortizationRun(MAcctSchema as, ResultSet rs, String trxName) {
		super(as, MTCSAmortizationRun.class ,rs, null, trxName);
	}

	//private MTCSAmortizationRun m_amortizationrun = null;
	
	//private boolean p_IsReversal = false;
	/**	Contained Doc Lines			*/
	protected DocLine[]			p_lines;
	
	@Override
	protected String loadDocumentDetails() {
		p_lines = loadLines((MTCSAmortizationRun) getPO());
		return null;
	}

	@Override
	public BigDecimal getBalance() {
		return Env.ZERO;
	}

	@Override
	public ArrayList<Fact> createFacts(MAcctSchema as) {
		
		ArrayList<Fact> facts = new ArrayList<Fact>();
		//  create Fact Header
		Fact fact = new Fact(this, as, Fact.POST_Actual);
		facts.add(fact);
		
		setC_Currency_ID (as.getC_Currency_ID());
		//MTCSAmortizationRun amortizationRun = (MTCSAmortizationRun) getPO();
		
		for (int i = 0; i < p_lines.length; i++)
		{
			MTCSAmortizationLine line = (MTCSAmortizationLine) p_lines[i].getPO();
			BigDecimal amt = line.getAmtAcct();
			
			MAccount drAccount = (MAccount) line.getDebit_Account_A();
			MAccount crAccount = (MAccount) line.getCredit_Account_A();
			
			FactUtil.createSimpleOperation(fact, p_lines[i], drAccount, crAccount, getC_Currency_ID(), amt, false);
		}
		
		/*
		if (fl == null) {
			p_Error = "Cant create fact line amortization";
			return null;
		}*/
		
		return facts;
	}

	/**
	 *	Load Invoice Line
	 *	@param invoice invoice
	 *  @return DocLine Array
	 */
	
	private DocLine[] loadLines (MTCSAmortizationRun amortizationRun) {
		ArrayList<DocLine> list = new ArrayList<DocLine>();
		//
		
		MTCSAmortizationLine[] lines = amortizationRun.getLines();
		for (MTCSAmortizationLine line : lines) {
			//MTCSAmortizationLine line = lines[i];
			DocLine docLine = new DocLine(line, this);
			docLine.setAmount(line.getAmtAcct());
			
			if (log.isLoggable(Level.FINE)) log.fine(docLine.toString());
			list.add(docLine);
		}
	
		//	Convert to Array
		DocLine[] dls = new DocLine[list.size()];
		list.toArray(dls);
		
		return dls;
	}
	
	/*
	private boolean isReversal(MTCSAmortizationRun amortizationRun){
		
		if(amortizationRun.getReversal_ID() > 0)
			return true;
		
		return false;
	}
	*/
	
}