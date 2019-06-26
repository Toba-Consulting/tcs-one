package id.tcs.process;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;

import org.adempiere.exceptions.AdempiereException;
import org.compiere.model.MElementValue;
import org.compiere.model.MOrg;
import org.compiere.model.MTree;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;

public class TCSGeneralLedgerView extends SvrProcess {
	
	
	private int p_start_Account_id=0;
	private int p_end_Account_id=0;
	private int p_C_BPartner_ID=0;
	private int p_UserElement1_ID=0; //tugboat
	private int p_UserElement2_ID=0; //barge
	private int p_AD_Org_ID=0;
	private int p_C_AcctSchema_ID=0;
	private int p_GL_Category_ID=0;
	private int p_M_Product_ID=0;
	private int p_C_Project_ID=0;
	private int p_C_ProjectPhase_ID=0;
	private int p_C_ProjectTask_ID=0;
	private int p_C_Tax_ID=0;
	private int p_M_Locator_ID=0;
	private int p_AD_OrgTrx_ID=0;
	private int p_C_Campaign_ID=0;
	private int p_C_Activity_ID=0;
	private int p_C_SalesRegion_ID=0;
	private int p_LocFrom_ID=0;
	private int p_LocTo_ID=0;
	private int p_C_SubAcct_ID=0;
	private int p_A_Asset_ID=0;
	private int p_User1_ID=0;
	private int p_User2_ID=0;
	private boolean p_AD_Org_ID_IsSummary=false;
	private StringBuilder p_AD_Org_IDs= new StringBuilder();
	
	//search between account_no
	//private BigDecimal p_start_Account_NO= Env.ZERO; ; //account_no
	//private BigDecimal p_end_Account_NO= Env.ZERO;  ; //account_no 
	private Timestamp p_DateFrom = null;
	private Timestamp p_DateTo = null;	
	private ArrayList<Integer> account_id_lists = new ArrayList<Integer>();
	private ArrayList<String> account_no_lists = new ArrayList<String>();
	private ArrayList<String> account_name_lists = new ArrayList<String>();
	
	@Override
	protected void prepare() {
		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++)
		{
			String name = para[i].getParameterName();
			if (para[i].getParameter() == null && para[i].getParameter_To() == null)
				;
			else if(name.equals("AccountFrom"))
			{
				p_start_Account_id = para[i].getParameterAsInt();
			}
			else if(name.equals("AccountTo"))
			{
				p_end_Account_id = para[i].getParameterAsInt();
			}
			else if(name.equals("AD_Org_ID"))
			{
				p_AD_Org_ID = para[i].getParameterAsInt();
			}
			else if(name.equals("C_BPartner_ID"))
			{
				p_C_BPartner_ID = para[i].getParameterAsInt();
			}
/*
			else if(name.equals("HBC_Barge_ID")) //TODO : change this for other project
			{
				p_UserElement1_ID = para[i].getParameterAsInt();
			}
			else if(name.equals("HBC_Tugboat_ID")) //TODO : change this for other project
			{
				p_UserElement2_ID = para[i].getParameterAsInt();
			}
*/
			else if(name.equals("DateAcct")){
				p_DateFrom = para[i].getParameterAsTimestamp();
				p_DateTo = para[i].getParameter_ToAsTimestamp();
			}
			else if(name.equals("C_AcctSchema_ID"))
			{
				p_C_AcctSchema_ID = para[i].getParameterAsInt();
			}
			else if(name.equals("GL_Category_ID"))
			{
				p_GL_Category_ID = para[i].getParameterAsInt();
			}
			else if(name.equals("M_Product_ID"))
			{
				p_M_Product_ID = para[i].getParameterAsInt();
			}
			else if(name.equals("C_Project_ID"))
			{
				p_C_Project_ID = para[i].getParameterAsInt();
			}
			else if(name.equals("C_ProjectPhase_ID"))
			{
				p_C_ProjectPhase_ID = para[i].getParameterAsInt();
			}
			else if(name.equals("C_ProjectTask_ID"))
			{
				p_C_ProjectTask_ID = para[i].getParameterAsInt();
			}
			else if(name.equals("C_Tax_ID"))
			{
				p_C_Tax_ID = para[i].getParameterAsInt();
			}
			else if(name.equals("M_Locator_ID"))
			{
				p_M_Locator_ID = para[i].getParameterAsInt();
			}
			else if(name.equals("AD_OrgTrx_ID"))
			{
				p_AD_OrgTrx_ID = para[i].getParameterAsInt();
			}
			else if(name.equals("C_SalesRegion_ID"))
			{
				p_C_SalesRegion_ID = para[i].getParameterAsInt();
			}
			else if(name.equals("C_Activity_ID"))
			{
				p_C_Activity_ID = para[i].getParameterAsInt();
			}
			else if(name.equals("C_LocFrom_ID"))
			{
				p_LocFrom_ID = para[i].getParameterAsInt();
			}
			else if(name.equals("C_LocTo_ID"))
			{
				p_LocTo_ID = para[i].getParameterAsInt();
			}
			else if(name.equals("C_SubAcct_ID"))
			{
				p_C_SubAcct_ID = para[i].getParameterAsInt();
			}
			else if(name.equals("A_Asset_ID"))
			{
				p_A_Asset_ID = para[i].getParameterAsInt();
			}
			else if(name.equals("User1_ID"))
			{
				p_User1_ID = para[i].getParameterAsInt();
			}
			else if(name.equals("User2_ID"))
			{
				p_User2_ID = para[i].getParameterAsInt();
			}
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
	}
	
	@Override
	protected String doIt() throws Exception {
		if(p_DateTo == null)
			p_DateTo = p_DateFrom;
		
		if(p_end_Account_id == 0)
			p_end_Account_id = p_start_Account_id;

//@David
		MOrg org = new MOrg(getCtx(), p_AD_Org_ID, get_TrxName());
		if (org.isSummary()) {
			p_AD_Org_ID_IsSummary=true;
			getOrgTreeChilds(p_AD_Org_ID);
		}
		else
			p_AD_Org_IDs.append(p_AD_Org_ID);
//@David End
		
		cleanTable(); //clear temporary table
        setAllAccount(); // to array list account_id_lists
		for(int i=0;i<account_id_lists.size();i++)
		{
			
			BigDecimal initialBalance = getinitialBalance(p_DateFrom, p_AD_Org_ID, p_C_AcctSchema_ID, account_id_lists.get(i), p_GL_Category_ID, 
					p_C_BPartner_ID, p_M_Product_ID, p_C_Project_ID, p_C_ProjectPhase_ID, p_C_ProjectTask_ID, p_C_Tax_ID, p_M_Locator_ID, 
					p_AD_OrgTrx_ID, p_C_Campaign_ID, p_C_Activity_ID, p_C_SalesRegion_ID, p_LocFrom_ID, p_LocTo_ID, p_C_SubAcct_ID,
					p_A_Asset_ID, p_UserElement1_ID, p_UserElement2_ID, p_User1_ID, p_User2_ID);
			
			//insert initial balance to temporary table
			createInitialBalanceLine(initialBalance, account_id_lists.get(i), account_name_lists.get(i), account_no_lists.get(i), (i+1));
			//insert all request to temporary table
			createTransactionLines(initialBalance, account_id_lists.get(i), (i+1));
		}  
		return null;
	}
	
	protected BigDecimal getinitialBalance(Timestamp dateAcct, int AD_Org_ID, int C_AcctSchema_ID, int Account_ID, int GL_Category_ID, 
			int C_BPartner_ID, int M_Product_ID, int C_Project_ID, int C_ProjectPhase_ID, int C_ProjectTask_ID, int C_Tax_ID, int M_Locator_ID, 
			int AD_OrgTrx_ID, int C_Campaign_ID, int C_Activity_ID, int C_SalesRegion_ID, int C_LocFrom_ID, int C_LocTo_ID, int C_SubAcct_ID, 
			int A_Asset_ID, int UserElement1_ID, int UserElement2_ID, int User1_ID, int User2_ID) { 
				
		if (dateAcct == null)
			throw new AdempiereException("Process aborted, no date acct selected");

		//if (C_AcctSchema_ID <= 0)
			//throw new AdempiereException("Process aborted, no accounting schema selected");

		if (Account_ID <= 0)
			throw new AdempiereException("Process aborted, no account selected");
		
		Calendar calFrom = Calendar.getInstance();
		calFrom.setTime(new Date(p_DateFrom.getTime()));
		int yearFrom = calFrom.get(Calendar.YEAR);
		
		Calendar calTo = Calendar.getInstance();
		calTo.setTime(new Date(p_DateTo.getTime()));
		int yearTo = calTo.get(Calendar.YEAR);
		
		if(yearFrom != yearTo)
			throw new AdempiereException("Process aborted.. Year in parameter is different");
		
		boolean debit = false;
		MElementValue ev = new MElementValue(getCtx(), Account_ID, get_TrxName());
		if (ev.getAccountType().equalsIgnoreCase(MElementValue.ACCOUNTTYPE_Asset) ||
				ev.getAccountType().equalsIgnoreCase(MElementValue.ACCOUNTTYPE_Expense) ||
				ev.getAccountType().equalsIgnoreCase(MElementValue.ACCOUNTTYPE_Memo)) {
			
			debit = true;
		}
				
		StringBuilder sql = new StringBuilder();
		if (debit) {
			sql.append("SELECT COALESCE(SUM(AmtAcctDr-AmtAcctCr),0) FROM Fact_Acct fa ");
		} else {
			sql.append("SELECT COALESCE(SUM(AmtAcctCr-AmtAcctDr),0) FROM Fact_Acct fa ");
		}
		
		boolean reset = false;
		if (ev.getAccountType().equalsIgnoreCase(MElementValue.ACCOUNTTYPE_Revenue) ||
				ev.getAccountType().equalsIgnoreCase(MElementValue.ACCOUNTTYPE_Expense) ||
				ev.getAccountType().equalsIgnoreCase(MElementValue.ACCOUNTTYPE_Memo)) {
			
			reset = true;
		}
		
		if(reset){
			try {
				DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
				Date date = dateFormat.parse("01/01/"+yearFrom);
				long time = date.getTime();
				Timestamp firstDay = new Timestamp(time);
				
				if(firstDay.compareTo(p_DateFrom) == 0)
					sql.append("WHERE fa.C_AcctSchema_ID=? AND fa.Account_ID = ? and fa.DateAcct > '"+yearFrom+"-01-01' and fa.DateAcct < ? ");
				else
					sql.append("WHERE fa.C_AcctSchema_ID=? AND fa.Account_ID = ? and fa.DateAcct >= '"+yearFrom+"-01-01' and fa.DateAcct < ? ");
			} catch (Exception e) {
				throw new AdempiereException("Error when parsing date");
			}
		}
		else
			sql.append("WHERE fa.C_AcctSchema_ID=? AND fa.Account_ID = ? and fa.DateAcct < ? ");
		
		ArrayList<Object> params = new ArrayList<Object>();
		params.add(C_AcctSchema_ID);
		params.add(Account_ID);
		params.add(dateAcct);

		if (AD_Org_ID > 0) {
//@David
//			sql.append("AND fa.AD_Org_ID=? ");
//			params.add(AD_Org_ID);			
			sql.append("AND fa.AD_Org_ID IN ("+p_AD_Org_IDs+") ");
//@David End			
		}
		if (GL_Category_ID> 0) {
			sql.append("AND fa.GL_Category_ID=? ");
			params.add(AD_Org_ID);			
		}
		if (C_BPartner_ID > 0) {
			sql.append("AND fa.C_BPartner_ID=? ");
			params.add(C_BPartner_ID);	
		}
		if (M_Product_ID > 0) {
			sql.append("AND fa.M_Product_ID=? ");
			params.add(M_Product_ID);	
		}
		if (C_Project_ID > 0) {
			sql.append("AND fa.C_Project_ID=? ");
			params.add(C_Project_ID);	
		}
		if (C_ProjectPhase_ID > 0) {
			sql.append("AND fa.C_ProjectPhase_ID=? ");
			params.add(C_ProjectPhase_ID);	
		}
		if (C_Tax_ID > 0) {
			sql.append("AND fa.C_Tax_ID=? ");
			params.add(C_Tax_ID);	
		}
		if (M_Locator_ID > 0) {
			sql.append("AND fa.M_Locator_ID=? ");
			params.add(M_Locator_ID);	
		}
		if (AD_OrgTrx_ID > 0) {
			sql.append("AND fa.AD_OrgTrx_ID=? ");
			params.add(AD_OrgTrx_ID);	
		}
		if (C_Campaign_ID > 0) {
			sql.append("AND fa.C_Campaign_ID=? ");
			params.add(C_Campaign_ID);	
		}
		if (C_Activity_ID > 0) {
			sql.append("AND fa.C_Activity_ID=? ");
			params.add(C_Activity_ID);	
		}
		if (C_SalesRegion_ID > 0) {
			sql.append("AND fa.C_SalesRegion_ID=? ");
			params.add(C_SalesRegion_ID);	
		}
		if (C_LocFrom_ID > 0) {
			sql.append("AND fa.C_LocFrom_ID=? ");
			params.add(C_LocFrom_ID);	
		}
		if (C_LocTo_ID > 0) {
			sql.append("AND fa.C_LocTo_ID=? ");
			params.add(C_LocTo_ID);	
		}
		if (C_SubAcct_ID > 0) {
			sql.append("AND fa.C_SubAcct_ID=? ");
			params.add(C_SubAcct_ID);	
		}
		if (A_Asset_ID > 0) {
			sql.append("AND fa.A_Asset_ID=? ");
			params.add(A_Asset_ID);	
		}
		if (UserElement1_ID > 0) {//tugboat
			sql.append("AND fa.UserElement1_ID=? ");
			params.add(UserElement1_ID);	
		}
		if (UserElement2_ID > 0) {//barge
			sql.append("AND fa.UserElement2_ID=? ");
			params.add(UserElement2_ID);	
		}
		if (User1_ID > 0) {
			sql.append("AND fa.User1_ID=? ");
			params.add(User1_ID);	
		}
		if (User2_ID > 0) {
			sql.append("AND fa.User2_ID=? ");
			params.add(User2_ID);	
		}

		BigDecimal returnValue = DB.getSQLValueBD(get_TrxName(), sql.toString(), params);
		if (returnValue==null)
			returnValue = Env.ZERO;
		
		return returnValue;
	}
	protected void setAllAccount()
	{	
		MElementValue elementStart = new MElementValue(getCtx(), p_start_Account_id, get_TrxName());
		MElementValue elementTo = new MElementValue(getCtx(), p_end_Account_id, get_TrxName());
		
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT C_ElementValue_ID, Name, Value FROM C_ElementValue cev "
				//+ "WHERE Value = '"+elementStart.getValue()+"' ");
				+ "WHERE cast(cev.value AS INT) BETWEEN "+elementStart.getValue()
				+" AND "+elementTo.getValue()+ " AND c_elementvalue_id != 1001194 AND isSummary ='N' ORDER By cev.value ASC");
		PreparedStatement pstmt = null;
        ResultSet rs = null;
		
        try{
            pstmt = DB.prepareStatement(sb.toString(), get_TrxName());
            rs = pstmt.executeQuery();
             
            while(rs.next())
            {
            	account_id_lists.add(rs.getInt(1));
            	account_name_lists.add(rs.getString(2));
            	account_no_lists.add(rs.getString(3));
            }
             
        }catch(Exception e){
            log.severe(e.toString());
        }finally{
            DB.close(rs, pstmt);
            rs = null;
            pstmt = null;
        }
	}

	protected void cleanTable(){
		StringBuilder sql = new StringBuilder();
		sql.append("DELETE FROM T_TCSGeneralLedgerView WHERE AD_Client_ID=" + Env.getAD_Client_ID(getCtx()));
		DB.executeUpdate(sql.toString(), get_TrxName());
	}
	
	protected void createInitialBalanceLine(BigDecimal initialBalance, int account_id, String account_name, String account_no, int accountSequence){
		
		StringBuilder sb = new StringBuilder();
		//@phie add column gl_category_name HABCO 2596
		sb.append("INSERT INTO T_TCSGeneralLedgerView ")
		.append("(AccountSequence, Fact_Acct_ID, AD_Client_ID, AD_Org_ID, AD_Org_Name, C_AcctSchema_ID, C_Acct_Schema_Name, Account_ID, Account_No, Account_Name, ")
		.append("DateAcct, C_Period_ID, C_Period_Name, PostingType, AmtAcctDr, AmtAcctCr, AmtSourceCr, AmtSourceDr, AmtAcctBalance, ")
		.append("ISO_Code, Description, AD_PInstance_ID, Sequence, GL_Category_Name, AccountFrom, AccountTo, DocumentNo, Line, ")
		.append("C_BPartner_ID, M_Product_ID, ")
		.append("GL_Category_ID, C_Project_ID, C_ProjectPhase_ID, C_ProjectTask_ID, C_Tax_ID, M_Locator_ID, AD_OrgTrx_ID, C_Campaign_ID, C_Activity_ID, C_SalesRegion_ID, ")
		.append("C_LocFrom_ID, C_LocTo_ID, C_SubAcct_ID, A_Asset_ID, User1_ID, User2_ID, Qty)");
		
		sb.append(" VALUES( ");
		sb.append(accountSequence);
		sb.append(", null, ");
		sb.append(getAD_Client_ID()).append(",");
		if(p_AD_Org_ID > 0)
			sb.append(p_AD_Org_ID);
		else
			sb.append("null");
		sb.append(", '-',");
		
		if(p_C_AcctSchema_ID > 0)
			sb.append(p_C_AcctSchema_ID);
		else
			sb.append("null");
		sb.append(", null,");
		sb.append(account_id).append(", ");
		sb.append(account_no).append(", '");
		sb.append(account_name).append("', '");
		sb.append(p_DateFrom).append("', null, null, null, null, null, null, null, ");
		sb.append(initialBalance).append(", null, 'Begining Balance', ");
		sb.append(getAD_PInstance_ID()).append(", 1, null, ");
		sb.append(p_start_Account_id).append(", ");
		sb.append(p_end_Account_id).append(", null, null,");
		
		if (p_C_BPartner_ID > 0)
			sb.append(p_C_BPartner_ID);
		else sb.append("null");
		sb.append(",");
		
		if (p_M_Product_ID > 0)
			sb.append(p_M_Product_ID);
		else sb.append("null");
		sb.append(",");
		if (p_GL_Category_ID > 0)
			sb.append(p_GL_Category_ID);
		else sb.append("null");
		sb.append(", ");
		
		if (p_C_Project_ID > 0)
			sb.append(p_C_Project_ID);
		else sb.append("null");
		sb.append(", ");
		
		if (p_C_ProjectPhase_ID > 0)
			sb.append(p_C_ProjectPhase_ID);
		else sb.append("null");
		sb.append(", ");
		
		if (p_C_ProjectTask_ID > 0)
			sb.append(p_C_ProjectTask_ID);
		else sb.append("null");
		sb.append(", ");
		
		if (p_C_Tax_ID > 0)
			sb.append(p_C_Tax_ID);
		else sb.append("null");
		sb.append(", ");
		
		if (p_M_Locator_ID > 0)
			sb.append(p_M_Locator_ID);
		else sb.append("null");
		sb.append(", ");
		
		if (p_AD_OrgTrx_ID > 0)
			sb.append(p_AD_OrgTrx_ID);
		else sb.append("null");
		sb.append(", ");
		
		if (p_C_Campaign_ID > 0)
			sb.append(p_C_Campaign_ID);
		else sb.append("null");
		sb.append(", ");
		
		if (p_C_Activity_ID > 0)
			sb.append(p_C_Activity_ID);
		else sb.append("null");
		sb.append(", ");
		
		if (p_C_SalesRegion_ID > 0)
			sb.append(p_C_SalesRegion_ID);
		else sb.append("null");
		sb.append(", ");
		
		if (p_LocFrom_ID > 0)
			sb.append(p_LocFrom_ID);
		else sb.append("null");
		sb.append(", ");
		
		if (p_LocTo_ID > 0)
			sb.append(p_LocTo_ID);
		else sb.append("null");
		sb.append(", ");
		
		if (p_C_SubAcct_ID > 0)
			sb.append(p_C_SubAcct_ID);
		else sb.append("null");
		sb.append(", ");
		
		if (p_A_Asset_ID > 0)
			sb.append(p_A_Asset_ID);
		else sb.append("null");
		sb.append(", ");
		
		if (p_User1_ID > 0)
			sb.append(p_User1_ID);
		else sb.append("null");
		sb.append(", ");
		
		if (p_User2_ID > 0)
			sb.append(p_User2_ID);
		else sb.append("null, null");
		
		sb.append(")"); 
		//end phie
		int no = DB.executeUpdate(sb.toString(), get_TrxName());
		log.fine("#" + no);
		log.finest(sb.toString());
	}
	
	protected void createTransactionLines(BigDecimal initialBalance,int account_id, int AccountSequence){
		
		/**
		 * Note : Order by untuk liat hasil : accountSequence, sequence, dateacct, fact_acct_id
		 */
		
        StringBuilder sb = new StringBuilder("INSERT INTO T_TCSGeneralLedgerView ");
        sb.append("(AD_Client_ID, AD_Org_ID, AD_Org_Name, C_AcctSchema_ID, C_Acct_Schema_Name, Account_ID, Account_No, Account_Name, Dateacct, C_Period_ID, C_Period_Name, ");
        sb.append("PostingType, AmtAcctDr, AmtAcctCr, AmtSourceDr, AmtSourceCr, AmtAcctBalance, ISO_Code, Description, AD_PInstance_ID, Sequence, GL_Category_Name, ");
//        sb.append("accountfrom, accountto, DocumentNo, Line, C_BPartner_ID, M_Product_ID, HBC_Tugboat_ID, HBC_Barge_ID, Fact_Acct_ID, AccountSequence, ");
        sb.append("AccountFrom, AccountTo, DocumentNo, Line, C_BPartner_ID, M_Product_ID, Fact_Acct_ID, AccountSequence, ");
        sb.append("GL_Category_ID, C_Project_ID, C_ProjectPhase_ID, C_ProjectTask_ID, C_Tax_ID, M_Locator_ID, AD_OrgTrx_ID, C_Campaign_ID, C_Activity_ID, C_SalesRegion_ID, ");
		sb.append("C_LocFrom_ID, C_LocTo_ID, C_SubAcct_ID, A_Asset_ID, User1_ID, User2_ID, Qty)");

        
        sb.append("SELECT fa.ad_client_id, fa.ad_org_id, ao.name, fa.c_acctschema_id, ")
        .append("cas.name, fa.account_id, cev.value, cev.name , ")
        .append("fa.dateacct, fa.c_period_id, cp.name, fa.postingtype, fa.amtacctdr, fa.amtacctcr, fa.amtsourcedr, fa.amtsourcecr, (")
        .append(initialBalance)
        .append("+(SUM((fa.amtacctdr-fa.amtacctcr)*(case when cev.accounttype IN ('A','E','M') then 1 else -1 end)) OVER (ORDER BY dateacct, fact_acct_id))), ")
        .append("cc.iso_code, fa.description, ")
        .append(getAD_PInstance_ID())
        .append(", 2, gc.name, ")
        .append(p_start_Account_id).append(" , ")
        .append(p_end_Account_id)
//        .append(", fa.DocumentNo, fa.Line, fa.C_BPartner_ID, fa.M_Product_ID, fa.UserElement1_ID, fa.UserElement2_ID, fa.Fact_Acct_ID, "+AccountSequence+" ")
        .append(", fa.DocumentNo, fa.Line, fa.C_BPartner_ID, fa.M_Product_ID, fa.Fact_Acct_ID, "+AccountSequence+" ")
        .append(", fa.GL_Category_ID, fa.C_Project_ID, fa.C_ProjectPhase_ID, fa.C_ProjectTask_ID, fa.C_Tax_ID, fa.M_Locator_ID, fa.AD_OrgTrx_ID, fa.C_Campaign_ID")
        .append(", fa.C_Activity_ID, fa.C_SalesRegion_ID, fa.C_LocFrom_ID, fa.C_LocTo_ID, fa.C_SubAcct_ID, fa.A_Asset_ID, fa.User1_ID, fa.User2_ID, fa.Qty ")
        .append("FROM fact_acct fa ")
        .append("JOIN ad_org ao ON ao.ad_org_id=fa.ad_org_id ")
        .append("JOIN c_acctschema cas ON cas.c_acctschema_id=fa.c_acctschema_id ")
        .append("JOIN c_elementvalue cev ON cev.c_elementvalue_id=fa.account_id ")
        .append("JOIN c_period cp ON cp.c_period_id=fa.c_period_id ")
        .append("JOIN c_currency cc ON fa.c_currency_id = cc.c_currency_id ")
        .append("JOIN gl_category gc ON gc.gl_category_id = fa.gl_category_id ")
        .append("WHERE account_id="+account_id)
        .append(" AND fa.AD_Client_ID = "+getAD_Client_ID())
        .append(" AND (dateacct BETWEEN '"+p_DateFrom+"' AND '"+p_DateTo+"') ");
        
        if (p_C_BPartner_ID > 0)
			sb.append("AND fa.C_BPartner_ID=").append(p_C_BPartner_ID).append(" ");
		
		if (p_UserElement1_ID > 0)
			sb.append("AND fa.UserElement1_ID=").append(p_UserElement1_ID).append(" ");
		
		if (p_UserElement2_ID > 0)
			sb.append("AND fa.UserElement2_ID=").append(p_UserElement2_ID).append(" ");

//@David
//		if (p_AD_Org_ID > 0)
//			sb.append("AND fa.AD_Org_ID=").append(p_AD_Org_ID).append(" ");
		if (p_AD_Org_ID > 0)
			sb.append("AND fa.AD_Org_ID IN (").append(p_AD_Org_IDs).append(") ");
//@David End		
		if (p_C_AcctSchema_ID > 0)
			sb.append("AND fa.C_AcctSchema_ID=").append(p_C_AcctSchema_ID).append(" ");
		
		if (p_GL_Category_ID > 0)
			sb.append("AND fa.GL_Category_ID=").append(p_GL_Category_ID).append(" ");
		
		if (p_M_Product_ID > 0)
			sb.append("AND fa.M_Product_ID=").append(p_M_Product_ID).append(" ");
		
		if (p_C_Project_ID > 0)
			sb.append("AND fa.C_Project_ID=").append(p_C_Project_ID).append(" ");
		
		if (p_C_ProjectPhase_ID > 0)
			sb.append("AND fa.C_ProjectPhase_ID=").append(p_C_ProjectPhase_ID).append(" ");
		
		if (p_C_ProjectTask_ID > 0)
			sb.append("AND fa.C_ProjectTask_ID=").append(p_C_ProjectTask_ID).append(" ");
		
		if (p_C_Tax_ID > 0)
			sb.append("AND fa.C_Tax_ID=").append(p_C_Tax_ID).append(" ");
		
		if (p_M_Locator_ID > 0)
			sb.append("AND fa.M_Locator_ID=").append(p_M_Locator_ID).append(" ");
		
		if (p_AD_OrgTrx_ID > 0)
			sb.append("AND fa.p_AD_OrgTrx_ID=").append(p_AD_OrgTrx_ID).append(" ");
		
		if (p_C_Campaign_ID > 0)
			sb.append("AND fa.C_Campaign_ID=").append(p_C_Campaign_ID).append(" ");
		
		if (p_C_Activity_ID > 0)
			sb.append("AND fa.C_Activity_ID=").append(p_C_Activity_ID).append(" ");
        
		if (p_C_SalesRegion_ID > 0)
			sb.append("AND fa.C_SalesRegion_ID=").append(p_C_SalesRegion_ID).append(" ");
		
		if (p_LocFrom_ID > 0)
			sb.append("AND fa.LocFrom_ID=").append(p_LocFrom_ID).append(" ");
		
		if (p_LocTo_ID > 0)
			sb.append("AND fa.LocTo_ID=").append(p_LocTo_ID ).append(" ");
		
		if (p_C_SubAcct_ID > 0)
			sb.append("AND fa.C_SubAcct_ID=").append(p_C_SubAcct_ID).append(" ");
		
		if (p_A_Asset_ID > 0)
			sb.append("AND fa.A_Asset_ID=").append(p_A_Asset_ID).append(" ");
		
		if (p_User1_ID > 0)
			sb.append("AND fa.User1_ID=").append(p_User1_ID).append(" ");
		
		if (p_User2_ID > 0)
			sb.append("AND fa.User2_ID=").append(p_User2_ID).append(" ");
		
        sb.append("GROUP BY fa.AD_Client_ID, fa.AD_Org_ID, ao.Name, fa.C_AcctSchema_ID, fa.Account_ID, ")
        .append("cev.Value, cev.Name, fa.DateAcct, fa.C_Period_ID, fa.PostingType, cev.AccountType, ")
        .append("cas.Name, cp.Name, cc.ISO_Code, fa.Description, gc.Name, fa.Fact_Acct_ID, ")
        .append("fa.C_BPartner_ID, fa.UserElement1_ID, fa.UserElement2_ID, fa.AD_Org_ID ");
        sb.append("ORDER BY DateAcct, Fact_Acct_ID");
        
        int no = DB.executeUpdate(sb.toString(), get_TrxName());
		log.fine("#" + no);
		log.finest(sb.toString());

	
	}
	
	
	//@David
	//If Org.IsSummary='Y' Set p_AD_Org_ID = TreeChildNodes
	
	protected void getOrgTreeChilds(int AD_Org_ID) throws SQLException{
		
		MOrg inputOrg = new MOrg(getCtx(), AD_Org_ID, get_TrxName());
		if (inputOrg.isSummary()) {
			
			String treeIDWhere = "SELECT AD_Tree_ID FROM AD_Tree WHERE AD_Client_ID=? AND TreeType=? AND IsActive=?";
			int treeID = DB.getSQLValue(get_TrxName(), treeIDWhere, Env.getAD_Client_ID(getCtx()),MTree.TREETYPE_Organization,"Y");
		
			String sql="SELECT Node_ID FROM AD_TreeNode WHERE AD_Tree_ID=? AND Parent_ID=?";
		
			ResultSet rs = null;
			PreparedStatement pstmt=null;
			try{
			
				pstmt = DB.prepareStatement(sql,get_TrxName());
				pstmt.setInt(1, treeID);
				pstmt.setInt(2, AD_Org_ID);
				rs=pstmt.executeQuery();
				while (rs.next()) {
					
					MOrg org = new MOrg(getCtx(), rs.getInt("Node_ID"), get_TrxName());
					if (org.isSummary()) 
						getOrgTreeChilds(org.getAD_Org_ID());
					
					else {
						if (p_AD_Org_IDs.length()==0) {
							p_AD_Org_IDs.append(org.getAD_Org_ID());
						}
						else
							p_AD_Org_IDs.append(", "+org.getAD_Org_ID());
					}
				}
			}
			finally {
				try {
					if (rs != null) rs.close();
					if (pstmt != null) pstmt.close();
				} catch (SQLException ex) {/*ignored*/}
				rs = null;
				pstmt = null;
			}			
		}
	}
	//@David End
	
}
