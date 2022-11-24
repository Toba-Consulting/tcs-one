package id.tcs.validator;

import java.math.BigDecimal;
import java.sql.Timestamp;

import org.adempiere.base.event.IEventTopics;
import org.adempiere.exceptions.AdempiereException;
import org.compiere.model.MTable;
import org.compiere.model.MUser;
import org.compiere.model.PO;
import org.compiere.model.Query;
import org.compiere.process.DocAction;
import org.compiere.util.DB;
import org.compiere.util.DisplayType;
import org.compiere.util.Env;
import org.compiere.wf.MWFActivity;
import org.compiere.wf.MWFNode;
import org.osgi.service.event.Event;

import id.tcs.model.I_TCS_WF_Approval_Line;
import id.tcs.model.X_AD_WF_ActivityApprover;
import id.tcs.model.X_TCS_WF_Approval_Header;
import id.tcs.model.X_TCS_WF_Approval_Line;
import id.tcs.model.X_TCS_WF_Approval_Set;

public class TCS_WFActivityValidator {
	public static String executeEvent(Event event, PO po){
		String msg = "";
		MWFActivity wfActivity = (MWFActivity) po;
		if (event.getTopic().equals(IEventTopics.PO_AFTER_NEW)) {
			msg = createApprover(wfActivity, po);
		}
		
		return msg;
	}
	
	//@PhieAlbert
	public static String createApprover(MWFActivity wfActivity, PO po)
	{
		System.out.println(getNodeApproveID(wfActivity));
		if(wfActivity.getAD_WF_Node_ID() == getNodeApproveID(wfActivity))
		{
			int C_DocType_ID = 0;
			
			String tableName = MTable.getTableName(wfActivity.getCtx(), wfActivity.getAD_Table_ID());
			if(!tableName.equals(""))
			{
				//TODO: temporary set PK (tableName + _ID)
				String sql = "SELECT C_DocType_ID FROM "+tableName+" WHERE "+tableName+"_ID = ?";
				C_DocType_ID = DB.getSQLValueEx(wfActivity.get_TrxName(), sql, new Object[]{wfActivity.getRecord_ID()});
			}
			
			//Looking for workflow setup
			String whereClause = "AD_Client_ID = ? AND AD_Workflow_ID = ? AND C_DocType_ID=?";
			int TCS_WF_Approval_Set_ID = new Query(wfActivity.getCtx(), X_TCS_WF_Approval_Set.Table_Name, whereClause, wfActivity.get_TrxName())
										.setParameters(new Object[]{wfActivity.getAD_Client_ID(), wfActivity.getAD_Workflow_ID(), C_DocType_ID})
										.setOnlyActiveRecords(true)
										.firstId();
			
			if(TCS_WF_Approval_Set_ID == -1)
			{
				try {
					wfActivity.setUserChoice(Env.getAD_User_ID(Env.getCtx()), "Y", DisplayType.YesNo, "");
				} catch (Exception e) {
					e.printStackTrace();
				}
				wfActivity.saveEx();
			}
			else
			{
				X_TCS_WF_Approval_Set approvalSetup = new X_TCS_WF_Approval_Set(wfActivity.getCtx(), TCS_WF_Approval_Set_ID, wfActivity.get_TrxName());
				if(approvalSetup.getTCS_WF_Approval_Header_ID() == 0) //mandatory, maybe useless
					throw new AdempiereException("Please choose workflow hierachy for this setup");
				
				int TCS_WF_Approval_Header_ID = approvalSetup.getTCS_WF_Approval_Header_ID();
				X_TCS_WF_Approval_Header approvalHeader = new X_TCS_WF_Approval_Header(wfActivity.getCtx(), TCS_WF_Approval_Header_ID, wfActivity.get_TrxName());
				
				DocAction doc = (DocAction)wfActivity.getPO();
				BigDecimal approvalAmount = Env.ZERO;
				
				if(getCurrencyDocument(tableName, wfActivity) == approvalHeader.get_ValueAsInt("C_Currency_ID"))
					approvalAmount = doc.getApprovalAmt();
				else 
				{
					String sql = "SELECT currencyConvert(?,?,?,?,?,?,?)";
					approvalAmount = DB.getSQLValueBD(wfActivity.get_TrxName(), sql, new Object[]{doc.getApprovalAmt(), 
							getCurrencyDocument(tableName, wfActivity), approvalHeader.get_ValueAsInt("C_Currency_ID"), 
							getConversionDate(tableName, wfActivity, approvalHeader.get_ValueAsString("DateColumn")),
							approvalHeader.get_ValueAsInt("C_ConversionType_ID"),
							Env.getAD_Client_ID(wfActivity.getCtx()), Env.getAD_Org_ID(wfActivity.getCtx())});
					
					if(approvalAmount == null)
						throw new AdempiereException("No currency rate..");
				}
				
				int count = 0;
				for(int id : getLines(approvalHeader))
				{
					count++;
					X_TCS_WF_Approval_Line approvalLine = new X_TCS_WF_Approval_Line(approvalHeader.getCtx(), id, approvalHeader.get_TrxName());
					
					/*
					String sql = "Select HC_Employee_ID FROM mapping_user_employee where AD_User_ID = ? ";
					int HC_Employee_ID = DB.getSQLValueEx(wfActivity.get_TrxName(), sql, new Object[]{approvalLine.getAD_User_ID()});
					
					if(HC_Employee_ID == -1)
						throw new AdempiereException("Please setup User - Business Partner - Employee");
					
					MEmployee employee = new MEmployee(wfActivity.getCtx(), HC_Employee_ID, wfActivity.get_TrxName());
					*/
					MUser user = new MUser(wfActivity.getCtx(), approvalLine.getAD_User_ID(), wfActivity.get_TrxName());
					
					X_AD_WF_ActivityApprover activityApproval = new X_AD_WF_ActivityApprover(wfActivity.getCtx(), 0, wfActivity.get_TrxName());
					activityApproval.setAD_Org_ID(approvalLine.getAD_Org_ID());
					activityApproval.setAD_User_ID(user.get_ValueAsBoolean("isOnLeave") ? approvalLine.get_ValueAsInt("DelegateUser_ID") : approvalLine.getAD_User_ID());
					activityApproval.setAD_WF_Activity_ID(wfActivity.getAD_WF_Activity_ID());
					activityApproval.setApprovalAmt(approvalLine.getApprovalAmt());
					activityApproval.setSequence(approvalLine.getSequence());
					activityApproval.saveEx();
					if(approvalAmount.compareTo(approvalLine.getApprovalAmt()) <= 0)
						break;
					
					if(count == getLines(approvalHeader).length && approvalAmount.compareTo(approvalLine.getApprovalAmt()) > 0)
						throw new AdempiereException("Max Approver's Amount < Document Amount");
					 		
				}
			}
		}
		return "";
	}
	//end @PhieAlbert
	
	//@PhieAlbert
	public static int[] getLines (X_TCS_WF_Approval_Header approvalHeader)
	{
		String whereClause = "AD_Client_ID = ? AND TCS_WF_Approval_Header_ID=?";
		return new Query(approvalHeader.getCtx(), I_TCS_WF_Approval_Line.Table_Name, whereClause, approvalHeader.get_TrxName())
					.setParameters(new Object[]{approvalHeader.getAD_Client_ID(), approvalHeader.getTCS_WF_Approval_Header_ID()})
					.setOrderBy(X_TCS_WF_Approval_Line.COLUMNNAME_Sequence)
					.getIDs();
	}
	//end @PhieAlbert
	
	//@PhieAlbert
	public static int getCurrencyDocument(String tableName, MWFActivity wfActivity)
	{
		String sql = "SELECT C_Currency_ID FROM "+tableName+" WHERE "+tableName+"_ID = ?";
		return DB.getSQLValueEx(wfActivity.get_TrxName(), sql, new Object[]{wfActivity.getRecord_ID()});
	}
	//end @PhieAlbert
	
	//@PhieAlbert
	public static Timestamp getConversionDate(String tableName, MWFActivity wfActivity, String DateColumn)
	{
		String sql = "SELECT "+DateColumn+" FROM "+tableName+" WHERE "+tableName+"_ID = ?";
		return DB.getSQLValueTSEx(wfActivity.get_TrxName(), sql, new Object[]{wfActivity.getRecord_ID()});
	}
	//end @PhieAlbert
	
	//@PhieAlbert
	public static int getNodeApproveID(MWFActivity wfActivity)
	{
		//TODO: colomn isTCSApproved
		String whereClause = "AD_Workflow_ID = ? AND Action = 'C' AND AD_Client_ID = 0 AND isActive='Y'";
		return new Query(wfActivity.getCtx(), MWFNode.Table_Name, whereClause, wfActivity.get_TrxName())
				.setParameters(new Object[]{wfActivity.getAD_Workflow_ID()})
				.firstId();
	}
	//end @PhieAlbert
}
