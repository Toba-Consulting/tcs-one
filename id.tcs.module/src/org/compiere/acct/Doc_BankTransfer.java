package org.compiere.acct;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.util.ArrayList;

import org.compiere.model.MAcctSchema;
import id.tcs.model.MBankTransfer;
import org.compiere.model.MDocType;
import org.compiere.util.Env;

public class Doc_BankTransfer extends Doc{

	public Doc_BankTransfer(MAcctSchema as, ResultSet rs, String trxName) {
		super(as, MBankTransfer.class, rs, MDocType.DOCBASETYPE_GLJournal, trxName);	
	}

	@Override
	protected String loadDocumentDetails() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BigDecimal getBalance() {
		// TODO Auto-generated method stub
		return Env.ZERO;
	}

	@Override
	public ArrayList<Fact> createFacts(MAcctSchema as) {
		ArrayList<Fact> facts = new ArrayList<Fact>();
		return facts;
	}

}
