package id.tcs.process;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;

import org.compiere.model.MAcctSchema;
import org.compiere.model.MAcctSchemaDefault;
import org.compiere.model.MBankAccount;
import org.compiere.model.MConversionRate;
import org.compiere.model.MDocType;
import org.compiere.model.MElementValue;
import org.compiere.model.MGLCategory;
import org.compiere.model.MJournal;
import org.compiere.model.MJournalLine;
import org.compiere.model.MOrder;
import org.compiere.model.MOrg;
import org.compiere.model.MPeriod;
import org.compiere.model.Query;
import org.compiere.model.X_C_BankAccount_Acct;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;

import id.tcs.model.X_T_BankRevaluation;

public class TCS_ForeignBankRevaluationNoBankStatement extends SvrProcess {

	/**	Mandatory Acct Schema			*/
	private int	p_C_AcctSchema_ID = 0;
	/** Mandatory Conversion Type		*/
	private int	p_C_ConversionTypeReval_ID = 0;

	/** Mandatory Org		*/
	private int	p_AD_Org_ID = 0;

	/** Revaluation Period				*/
	private int	p_C_Period_ID = 0;

	/** GL Document Type				*/
	private int	p_C_DocTypeReval_ID = 0;

	@Override
	protected void prepare() {
		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++)
		{
			String name = para[i].getParameterName();
			if (para[i].getParameter() == null)
				;
			else if (name.equals("C_AcctSchema_ID"))
				p_C_AcctSchema_ID = para[i].getParameterAsInt();
			else if (name.equals("C_ConversionType_ID"))
				p_C_ConversionTypeReval_ID = para[i].getParameterAsInt();
			else if (name.equals("C_Period_ID"))
				p_C_Period_ID = para[i].getParameterAsInt();
			else if (name.equals("C_DocType_ID"))
				p_C_DocTypeReval_ID = para[i].getParameterAsInt();
			else if (name.equals("AD_Org_ID"))
				p_AD_Org_ID = para[i].getParameterAsInt();
			
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}

	}

	@Override
	protected String doIt() throws Exception {
		String returnMsg = null;
		if (log.isLoggable(Level.INFO)) log.info("C_AcctSchema_ID=" + p_C_AcctSchema_ID 
				+ ",C_ConversionTypeReval_ID=" + p_C_ConversionTypeReval_ID
				+ ",C_Period_ID=" + p_C_Period_ID
				+ ", C_DocType_ID=" + p_C_DocTypeReval_ID);

		//	Parameter
		if (p_C_Period_ID == 0)
			return "Process aborted.. Period is mandatory";

		if (p_C_ConversionTypeReval_ID == 0)
			return "Process aborted.. Conversion Type is mandatory";

		if (p_C_AcctSchema_ID == 0)
			return "Process aborted.. Accounting Schema is mandatory";

		if (p_C_DocTypeReval_ID== 0)
			return "Process aborted.. Document Type is mandatory";

		if (p_AD_Org_ID== 0)
			return "Process aborted.. Org is mandatory";

		//	Delete - just to be sure
		StringBuilder sql = new StringBuilder("DELETE FROM T_BankRevaluation WHERE AD_PInstance_ID=").append(getAD_PInstance_ID());
		int no = DB.executeUpdate(sql.toString(), get_TrxName());
		if (no > 0)
			if (log.isLoggable(Level.INFO)) log.info("Deleted #" + no);
		
		MPeriod period = new MPeriod(getCtx(), p_C_Period_ID, get_TrxName());

		MAcctSchema as = new MAcctSchema(getCtx(), p_C_AcctSchema_ID, get_TrxName());
		int glPrecision = as.getC_Currency().getStdPrecision();
		
		//check if there are bankaccount not associated with elementvalue
		returnMsg = checkBankAccountAssignmentAccountElement(as);
		if (returnMsg!=null)
			return returnMsg;
		
		//check if there is any bank account associated with more than one element value
		returnMsg = checkBankAccountMoreThanOneAccountElement();
		if (returnMsg!=null)
			return returnMsg;
		
		
		//get all the bank accounts
		String whereBankAccount = "AD_Org_ID=? AND C_Currency_ID!=?";
		List<MBankAccount> bankAccounts = new Query(getCtx(), MBankAccount.Table_Name, whereBankAccount, get_TrxName())
		.setParameters(new Object[]{p_AD_Org_ID, as.getC_Currency_ID()})
		.setOnlyActiveRecords(true)
		.list();

		for (MBankAccount bankAcct: bankAccounts) {
			int bankPrecision = bankAcct.getC_Currency().getStdPrecision();
			
			String sqlTrxBalance = "SELECT COALESCE(currencyconvert((SELECT SUM(PayAmt) FROM C_Payment WHERE DateAcct <= ? AND C_BankAccount_ID = ?), ?, ?, ?, ?, ?, ?),0) FROM dual";
			Object[] params = new Object[]{period.getEndDate(), bankAcct.get_ID(), bankAcct.getC_Currency_ID(), 
					as.getC_Currency_ID(), period.getEndDate(), p_C_ConversionTypeReval_ID, Env.getAD_Client_ID(getCtx()), p_AD_Org_ID};

			BigDecimal trxBalance = DB.getSQLValueBD(get_TrxName(), sqlTrxBalance, params)
					.setScale(glPrecision, RoundingMode.HALF_UP);

			String sqlAccountElement = "SELECT C_ElementValue_ID FROM C_ElementValue WHERE C_BankAccount_ID=?";
			int accountElementID = DB.getSQLValue(get_TrxName(), sqlAccountElement, bankAcct.get_ID());

			String sqlFactAcctBalance = "SELECT COALESCE(SUM(AmtAcctDR-AmtAcctCR),0) FROM Fact_Acct WHERE DateAcct <= ? AND Account_ID=?";
			String sqlFactSourceBalance = "SELECT COALESCE(SUM(AmtSourceDR-AmtSourceCR),0) FROM Fact_Acct WHERE DateAcct <= ? AND Account_ID=? AND AD_Table_ID != 224";
			Object[] paramFact = new Object[]{period.getEndDate(), accountElementID}; 
			
			BigDecimal factAcctBalance = DB.getSQLValueBD(get_TrxName(), sqlFactAcctBalance, paramFact)
					.setScale(glPrecision, RoundingMode.HALF_UP);
			
			BigDecimal factSourceBalance = DB.getSQLValueBD(get_TrxName(), sqlFactSourceBalance, paramFact)
					.setScale(bankPrecision, RoundingMode.HALF_UP);
			
			
//			BigDecimal diff = trxBalance.subtract(factAcctBalance, new MathContext(glPrecision)); 
			BigDecimal diff = trxBalance.subtract(factAcctBalance).setScale(glPrecision, RoundingMode.HALF_UP);
			if (diff.compareTo(Env.ZERO)==0)
				continue;
			
			/*
			String gainLoss= "";
			if (diff.compareTo(Env.ZERO) < 0)
				gainLoss = "Loss";
			else gainLoss = "Gain";
			*/

			BigDecimal rate = MConversionRate.getRate(bankAcct.getC_Currency_ID(), as.getC_Currency_ID(), period.getEndDate(),
					p_C_ConversionTypeReval_ID,Env.getAD_Client_ID(getCtx()), bankAcct.getAD_Org_ID());

			X_T_BankRevaluation bankReval = new X_T_BankRevaluation(getCtx(), 0, get_TrxName());
			bankReval.setAD_Org_ID(bankAcct.getAD_Org_ID());
			bankReval.setC_AcctSchema_ID(p_C_AcctSchema_ID);
			bankReval.setC_Period_ID(p_C_Period_ID);
			bankReval.setAD_PInstance_ID(getAD_PInstance_ID());
			bankReval.setC_BankAccount_ID(bankAcct.get_ID());
			bankReval.setEndingBalance(trxBalance);
			bankReval.setAmtSourceBalance(factSourceBalance);
			bankReval.setAmtAcctBalance(factAcctBalance);
			bankReval.setRate(rate);
			bankReval.setAmtRevalDiff(diff);
			bankReval.setC_DocTypeReval_ID(p_C_DocTypeReval_ID);
			bankReval.setDateReval(period.getEndDate());
			bankReval.setC_ConversionTypeReval_ID(p_C_ConversionTypeReval_ID);
			bankReval.saveEx();
			
		}

		//	Create Document
		String info = createGLJournal(period);
		StringBuilder msgreturn = new StringBuilder("#").append(info);
		return msgreturn.toString();
	}

	/**
	 * 	Create GL Journal
	 * 	@return document info
	 */
	private String createGLJournal(MPeriod period)
	{
		final String whereClause = "AD_PInstance_ID=? AND AD_Org_ID=?";
		//@Commented By David
		//
//		List <X_T_BankRevaluation> bankRevals = new Query(getCtx(), X_T_BankRevaluation.Table_Name, whereClause, get_TrxName())
		int [] bankRevalIDs = new Query(getCtx(), X_T_BankRevaluation.Table_Name, whereClause, get_TrxName())
		.setParameters(new Object[]{getAD_PInstance_ID(), p_AD_Org_ID})
		.getIDs();	

		if (bankRevalIDs.length == 0)
			return " - No Records found";

		MAcctSchema as = MAcctSchema.get(getCtx(), p_C_AcctSchema_ID);
		MAcctSchemaDefault asDefaultAccts = MAcctSchemaDefault.get(getCtx(), p_C_AcctSchema_ID);
		 		
		MDocType docType = MDocType.get(getCtx(), p_C_DocTypeReval_ID);
		MGLCategory cat = MGLCategory.get(getCtx(), docType.getGL_Category_ID());
		MOrg org = MOrg.get(getCtx(), p_AD_Org_ID);
		
		MJournal journal = new MJournal (getCtx(), 0, get_TrxName());
		journal.setAD_Org_ID(p_AD_Org_ID);
		journal.setC_DocType_ID(p_C_DocTypeReval_ID);
		journal.setPostingType(MJournal.POSTINGTYPE_Actual);
		journal.setDateDoc(period.getEndDate());
		journal.setDateAcct(period.getEndDate()); // sets the period too
		journal.setC_Currency_ID(as.getC_Currency_ID());
		journal.setC_AcctSchema_ID (as.getC_AcctSchema_ID());
		journal.setC_ConversionType_ID(p_C_ConversionTypeReval_ID);
		journal.setGL_Category_ID (cat.getGL_Category_ID());
		journal.setDescription("Cash/Bank Revaluation - Org " + org.getName() + ", Period " + period.getName() ); // updated below
		journal.setC_Period_ID(period.getC_Period_ID());
		if (!journal.save())
			return " - Could not create Journal";
		//
		int i=0;
		for (int bankRevalID : bankRevalIDs)
		{
			X_T_BankRevaluation bankReval = new X_T_BankRevaluation(getCtx(), bankRevalID, get_TrxName());
			i++;
			MBankAccount bankAccount = new MBankAccount(getCtx(), bankReval.getC_BankAccount_ID(), null);
			String whereSQL = "C_BankAccount_ID=? AND C_AcctSchema_ID=?";
			X_C_BankAccount_Acct bankAccounting = new Query(getCtx(), X_C_BankAccount_Acct.Table_Name, whereSQL, get_TrxName())
						.setParameters(new Object[]{bankAccount.get_ID(), p_C_AcctSchema_ID})
						.firstOnly();
			

			if (bankAccount.getC_Currency_ID() == as.getC_Currency_ID())
				continue;
		
			//
			MJournalLine line = new MJournalLine(journal);
			line.setLine(((2*(i+1))-1) * 10);
			line.setDescription(bankAccount.getValue() +" - " + bankAccount.getName() + ": Revaluation");
			line.setC_ValidCombination_ID(bankAccounting.getB_Asset_Acct());
			
			BigDecimal dr = Env.ZERO;
			BigDecimal cr = Env.ZERO;
			
			//BigDecimal trxBalance = bankReval.getEndingBalance().abs().multiply(bankReval.getRate());
			//BigDecimal factAcctBalance = bankReval.getAmtAcctBalance();
			
			BigDecimal diff = bankReval.getAmtRevalDiff();
			
			//		trxBalance.subtract(factAcctBalance, new MathContext(0));
			int acct = 0;
			
			if(diff.compareTo(Env.ZERO) > 0) {
				dr = diff;
				acct = asDefaultAccts.getRealizedGain_Acct();
			}
			else {
				cr = diff.abs();
				acct = asDefaultAccts.getRealizedLoss_Acct();
			}
			line.setAmtSourceDr (dr);
			line.setAmtAcctDr (dr);
			line.setAmtSourceCr (cr);
			line.setAmtAcctCr (cr);
			line.saveEx();
			//
			MJournalLine line2 = new MJournalLine(journal);
			line2.setLine((2*(i+1)) * 10);
			line2.setDescription(bankAccount.getValue() +" - " + bankAccount.getName() + ": Revaluation");
			//
			//MFactAcct fa = new MFactAcct (getCtx(), gl.getFact_Acct_ID(), null);
			line2.setC_ValidCombination_ID(acct);
			BigDecimal temp = dr;
			dr = cr;
			cr = temp;
			
			line2.setAmtSourceDr (dr);
			line2.setAmtAcctDr (dr);
			line2.setAmtSourceCr (cr);
			line2.setAmtAcctCr (cr);
			line2.saveEx();
		}
		//createBalancing (asDefaultAccts, journal, gainTotal, lossTotal, p_AD_Org_ID, (list.size()+1) * 10);

		StringBuilder msgreturn = new StringBuilder(" - ").append(journal.getDocumentNo()).append(" #").append(bankRevalIDs.length);
		addLog(journal.getGL_Journal_ID(), null, null, msgreturn.toString(), MJournal.Table_ID, journal.getGL_Journal_ID());
		return "OK";
	}	//	createGLJournal

	/**
	 * 	Create Balancing Entry
	 *	@param asDefaultAccts acct schema default accounts
	 *	@param journal journal
	 *	@param gainTotal dr
	 *	@param lossTotal cr
	 *	@param AD_Org_ID org
	 *	@param lineNo base line no
	 */
	/*
	private void createBalancing (MAcctSchemaDefault asDefaultAccts, MJournal journal, 
			BigDecimal gainTotal, BigDecimal lossTotal, int AD_Org_ID, int lineNo)
	{
		if (journal == null)
			throw new IllegalArgumentException("Journal is null");
		//		CR Entry = Gain
		if (gainTotal.signum() != 0)
		{
			MJournalLine line = new MJournalLine(journal);
			line.setLine(lineNo+1);
			MAccount base = MAccount.get(getCtx(), asDefaultAccts.getRealizedGain_Acct());
			MAccount acct = MAccount.get(getCtx(), asDefaultAccts.getAD_Client_ID(), AD_Org_ID, 
					asDefaultAccts.getC_AcctSchema_ID(), base.getAccount_ID(), base.getC_SubAcct_ID(),
					base.getM_Product_ID(), base.getC_BPartner_ID(), base.getAD_OrgTrx_ID(), 
					base.getC_LocFrom_ID(), base.getC_LocTo_ID(), base.getC_SalesRegion_ID(), 
					base.getC_Project_ID(), base.getC_Campaign_ID(), base.getC_Activity_ID(),
					base.getUser1_ID(), base.getUser2_ID(), base.getUser3_ID(), base.getUser4_ID(), 
					base.getUser5_ID(), base.getUser6_ID(), base.getUser7_ID(), base.getUser8_ID(), 
					base.getUser9_ID(), base.getUser10_ID(),base.getUserElement1_ID(), base.getUserElement2_ID(),
					base.getUserElement3_ID(), base.getUserElement4_ID(), base.getUserElement5_ID(),
					base.getUserElement6_ID(), base.getUserElement7_ID(), base.getUserElement8_ID(),
					base.getUserElement9_ID(), base.getUserElement10_ID(), get_TrxName());
			line.setDescription(Msg.getElement(getCtx(), "RealizedGain_Acct"));
			line.setC_ValidCombination_ID(acct.getC_ValidCombination_ID());
			line.setAmtSourceCr (gainTotal);
			line.setAmtAcctCr (gainTotal);
			line.saveEx();
		}
		//	DR Entry = Loss
		if (lossTotal.signum() != 0)
		{
			MJournalLine line = new MJournalLine(journal);
			line.setLine(lineNo+2);
			MAccount base = MAccount.get(getCtx(), asDefaultAccts.getRealizedLoss_Acct());
			MAccount acct = MAccount.get(getCtx(), asDefaultAccts.getAD_Client_ID(), AD_Org_ID, 
					asDefaultAccts.getC_AcctSchema_ID(), base.getAccount_ID(), base.getC_SubAcct_ID(),
					base.getM_Product_ID(), base.getC_BPartner_ID(), base.getAD_OrgTrx_ID(), 
					base.getC_LocFrom_ID(), base.getC_LocTo_ID(), base.getC_SalesRegion_ID(), 
					base.getC_Project_ID(), base.getC_Campaign_ID(), base.getC_Activity_ID(),
					base.getUser1_ID(), base.getUser2_ID(), base.getUser3_ID(), base.getUser4_ID(), 
					base.getUser5_ID(), base.getUser6_ID(), base.getUser7_ID(), base.getUser8_ID(), 
					base.getUser9_ID(), base.getUser10_ID(),base.getUserElement1_ID(), 
					base.getUserElement2_ID(), base.getUserElement3_ID(), base.getUserElement4_ID(),
					base.getUserElement5_ID(), base.getUserElement6_ID(), base.getUserElement7_ID(),
					base.getUserElement8_ID(), base.getUserElement9_ID(), base.getUserElement10_ID(),
					get_TrxName());
			line.setDescription(Msg.getElement(getCtx(), "RealizedLoss_Acct"));
			line.setC_ValidCombination_ID(acct.getC_ValidCombination_ID());
			line.setAmtSourceDr (lossTotal);
			line.setAmtAcctDr (lossTotal);
			line.saveEx();
		}
	}	//	createBalancing
	*/

	private String checkBankAccountAssignmentAccountElement(MAcctSchema as) {
		//@phie filter ad_client_id
		String sqlWhere = "NOT EXISTS (SELECT 1 FROM C_ElementValue cev WHERE cev.AD_Client_ID=? "
				+ "AND cev.C_BankAccount_ID=C_BankAccount.C_BankAccount_ID) AND C_Currency_ID!=? AND AD_Client_ID=?";

		List<MBankAccount> bankAccts= new Query(getCtx(), MBankAccount.Table_Name, sqlWhere, get_TrxName())
		.setParameters(new Object[]{Env.getAD_Client_ID(getCtx()),as.getC_Currency_ID(),Env.getAD_Client_ID(getCtx())})
		.setOnlyActiveRecords(true)
		.list();

		if (bankAccts.size() > 0) {
			StringBuilder banks = new StringBuilder();
			for (MBankAccount bankAcct : bankAccts) {
				banks.append(bankAcct.getAccountNo());
				banks.append(",");
			}
			banks.delete(banks.length()-1, banks.length());

			return "Process aborted.. These "+banks+" is not associate with any account element";
		}
		return null;
	}

	private String checkBankAccountMoreThanOneAccountElement() {
		String sql = "SELECT cev.C_BankAccount_ID, count(1) FROM C_ElementValue cev " +
				"JOIN C_BankAccount cba ON cba.C_BankAccount_ID=cev.C_BankAccount_ID " +		
				"WHERE cev.AD_Client_ID=? AND cba.IsActive='Y' AND cev.IsActive='Y'" +
				"GROUP BY cev.C_BankAccount_ID " +
				"HAVING COUNT(1) > 1 ";

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder banks = new StringBuilder("(");
		try {
			pstmt = DB.prepareStatement(sql, get_TrxName());
			pstmt.setInt(1, Env.getAD_Client_ID(getCtx()));
			rs = pstmt.executeQuery();

			while (rs.next()) {
				banks.append(rs.getString(1));
				banks.append(",");
			}
			banks.delete(banks.length()-1, banks.length());
			banks.append(")");

		} catch (SQLException e) {
			log.log(Level.SEVERE, sql, e);
		}
		finally
		{
			DB.close(rs, pstmt);
			pstmt = null; rs = null;

		}

		if (banks.length()>1) {
			StringBuilder elementList = new StringBuilder("(");
			String sqlWhere = "C_BankAccount_ID IN " + banks;
			List<MElementValue> elementValues = new Query(getCtx(), MElementValue.Table_Name, sqlWhere, get_TrxName())
			.setOnlyActiveRecords(true)
			.list();

			for (MElementValue ev : elementValues) {
				elementList.append(ev.getValue());
				elementList.append(",");
			}
			elementList.delete(elementList.length()-1, elementList.length());
			elementList.append(")");	
			return "Process aborted.. Duplicate bank account on these account elements" + elementList;

		}

		return null;
	}
}

