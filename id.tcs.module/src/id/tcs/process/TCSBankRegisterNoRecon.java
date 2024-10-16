package id.tcs.process;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.logging.Level;

import org.compiere.model.MBankAccount;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.TimeUtil;

/**
 * @author TommyAng
 * TCSBankRegister Report&View Process
 */
public class TCSBankRegisterNoRecon extends SvrProcess{
	
	private int p_C_BankAccount_ID = 0;										//C_BankAccount_ID	First Parameter
	private Timestamp p_DateFrom = null;									//DateFrom			Second Parameter
	private Timestamp p_DateTo = null;										//DateTo			Third Parameter

	
	protected void prepare() {
		
		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++)
		{
			String name = para[i].getParameterName();
			if (para[i].getParameter() == null && para[i].getParameter_To() == null)
				;
			else if(name.equals("C_BankAccount_ID"))
				p_C_BankAccount_ID = para[i].getParameterAsInt();
			else if(name.equals("DateFrom")){
				p_DateFrom = para[i].getParameterAsTimestamp();
			}
			else if(name.equals("DateTo")){
				p_DateTo = para[i].getParameterAsTimestamp();
			}
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
	}
	
	protected String doIt() throws Exception {
		
		Timestamp p_DateSaldo = TimeUtil.addDays(p_DateFrom, -1);			//DateSaldo			Parameter for TCS_BankInitialBalance Function
		BigDecimal balance = Env.ZERO;										//Initial Balance
		
		String begBalQuery = "SELECT SUM( "
							+" (CASE WHEN ((cp.isReceipt = 'Y' AND cp.PayAmt > 0) OR (cp.isReceipt = 'N' AND cp.PayAmt < 0)) THEN abs(cp.PayAmt) ELSE 0 END) - "
							+" (CASE WHEN ((cp.isReceipt = 'N' AND cp.PayAmt > 0) OR (cp.isReceipt = 'Y' AND cp.PayAmt < 0)) THEN abs(cp.PayAmt) ELSE 0 END) ) "
							+" FROM C_Payment cp"
							//@win remove reversed Payment
//							+" WHERE cp.DocStatus IN ('CO','CL','RE') AND C_BankAccount_ID="+p_C_BankAccount_ID
							+" WHERE cp.DocStatus IN ('CO','CL') AND C_BankAccount_ID="+p_C_BankAccount_ID
							+" AND cp.PayAmt != 0 AND cp.DateAcct<'"+p_DateFrom+"'";

		
		balance = DB.getSQLValueBD(get_TrxName(), begBalQuery);
		if(balance == null) balance = Env.ZERO;
		
		cleanTable();							//Remove Table(View Requirement)[TEMPORARY] TODO Remove This Later!
		createInitialBalanceLine(balance);		//Start Balance
		createUnreconciledLines(balance);					//UnReconciled Transactions
		
		return "";
	}

	/**
	 * Create Second Line to Show Start Balance
	 * Sequence 1
	 */
	protected void createInitialBalanceLine(BigDecimal balance){
		String sqlGetLastDate = "(SELECT MAX(DateAcct) FROM C_BankStatement bs WHERE bs.C_BankAccount_ID = "+p_C_BankAccount_ID+" "
				+ "AND bs.docStatus IN ('CO','CL') AND bs.AD_Client_ID = "+getAD_Client_ID()+" AND DateAcct < '"+p_DateFrom+"')";
		
		StringBuilder sb = new StringBuilder();
		sb.append("INSERT INTO T_TCSBankReport "
				+ "(AD_Client_ID, AD_Org_ID, IsActive, Created, CreatedBy, Updated, UpdatedBy, C_BankAccount_ID, C_BankStatementLine_ID, DateAcct, Description, "
				+ "AmtSourceDR, AmtSourceCR, Balance, AD_PInstance_ID, T_TCSBankReport_UU, C_BankAccount_Name, C_Currency_Name, Reference, Sequence, VoucherNo, DocumentNo, C_BPartner_Value, C_BPartner_Name, DateFrom, DateTo) " 
				+ "VALUES("+getAD_Client_ID()+","+Env.getAD_Org_ID(getCtx())+",null,null,null,null,null,"+p_C_BankAccount_ID+",null,"+sqlGetLastDate+",'Beginning Balance',null,null,"+balance+","+getAD_PInstance_ID()+",null,null,null,null,1, null, null, null, null, '"+p_DateFrom+"','"+p_DateTo+"')");
		
		int no = DB.executeUpdate(sb.toString(), get_TrxName());
		log.fine("#" + no);
		log.finest(sb.toString());
	}
		
	/**
	 * Create Lines for UnReconciled Transactions
	 */
	protected void createUnreconciledLines(BigDecimal balance){
		
		MBankAccount bankAcc = new MBankAccount(getCtx(), p_C_BankAccount_ID, get_TrxName());
		String currencyName = bankAcc.getC_Currency().getISO_Code();
		String bankAccountName = bankAcc.get_ValueAsString("name");
		StringBuffer sb = new StringBuffer("INSERT INTO T_TCSBankReport "
				+ "(AD_Client_ID, AD_Org_ID, IsActive, Created, CreatedBy, Updated, UpdatedBy, C_BankAccount_ID, C_BankStatementLine_ID, DateAcct, Description, "
				+ "AmtSourceDR, AmtSourceCR, Balance, AD_PInstance_ID, T_TCSBankReport_UU, C_BankAccount_Name, C_Currency_Name, Reference, Sequence, VoucherNo, DocumentNo, C_BPartner_Value, C_BPartner_Name, DateFrom, DateTo) " );
				
		sb.append("select cp.AD_Client_ID, cp.AD_Org_ID, cp.IsActive, cp.Created, cp.CreatedBy, cp.Updated, cp.UpdatedBy, cp.c_bankaccount_id, ")
		.append("null, cp.dateacct, cp.Description, ")
		.append("CASE ")
		//.append("when cp.payamt<0 ") //Comment by @PhieAlbert
		//.append("then cp.payamt*-1")
		.append("when ((cp.isReceipt = 'Y' AND cp.PayAmt > 0) OR (cp.isReceipt = 'N' AND cp.PayAmt < 0)) ") //@PhieAlbert
		.append("then abs(cp.payamt) ")//@PhieAlbert
		.append("else null ")
		.append("end, ")
		.append("CASE ")
		//.append("when cp.payamt>0 ") //Comment by @PhieAlbert
		//.append("then cp.payamt ")
		.append("when ((cp.isReceipt = 'N' AND cp.PayAmt > 0) OR (cp.isReceipt = 'Y' AND cp.PayAmt < 0)) ")//@PhieAlbert
		.append("then abs(cp.PayAmt) ")//@PhieAlbert
		.append("else null ")
		.append("end, (")
		//@PhieAlbert
		.append(balance).append("+(sum("
		/*DR*/	+ "(case when ((cp.isReceipt = 'Y' AND cp.PayAmt > 0) OR (cp.isReceipt = 'N' AND cp.PayAmt < 0)) then abs(cp.payamt) else 0 end) "
		/*min*/ + "- "
		/*CR*/	+ "(case when ((cp.isReceipt = 'N' AND cp.PayAmt > 0) OR (cp.isReceipt = 'Y' AND cp.PayAmt < 0)) then abs(cp.payamt) else 0 end) "
				+ ") over(order by cp.dateacct, cp.c_payment_id))), ")
		//end @PhieAlbert
		.append(getAD_PInstance_ID())
		.append(", null, '")
		.append(bankAccountName).append("', '").append(currencyName).append("', ")
		.append("cp.documentno ||' - '|| bp.value || ' - ' || bp.name, ")
		.append("2, cp.VoucherNo, cp.documentno, bp.value, bp.name, ?,?") // add column voucher @phie
		.append(" from c_payment cp ") 
		.append("join c_bpartner bp on bp.c_bpartner_id=cp.c_bpartner_id ")
		//.append("join c_bankaccount ba on ba.c_bankaccount_id=cp.c_bankaccount_id ")
		.append("where cp.c_bankaccount_id="+p_C_BankAccount_ID) 
		//@win remove reversed payment
		//		.append(" and cp.docstatus IN ('CO','CL','RE') AND cp.isreconciled='N'") 
		.append(" and cp.docstatus IN ('CO','CL') AND cp.isreconciled='N'") 

		//		.append(" and cp.dateacct <= '"+p_DateTo+"'")
		.append(" and cp.dateacct BETWEEN '"+p_DateFrom+"' AND '"+p_DateTo+"'")
		.append(" and cp.payamt != 0")
		.append(" order by cp.dateacct, cp.c_payment_id ");
		
		int no = DB.executeUpdateEx(sb.toString(), new Object[]{p_DateFrom, p_DateTo}, get_TrxName());
		log.fine("#" + no);
		log.finest(sb.toString());
	}
	
	protected void cleanTable(){
		StringBuilder sql = new StringBuilder();
		sql.append("DELETE FROM T_TCSBankReport");
		DB.executeUpdate(sql.toString(), get_TrxName());
	}
}
	
