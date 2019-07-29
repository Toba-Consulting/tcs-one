package id.tcs.process;

import java.util.List;
import java.util.logging.Level;

import org.compiere.model.MClient;
import org.compiere.model.MForm;
import org.compiere.model.MFormAccess;
import org.compiere.model.MInfoWindow;
import org.compiere.model.MInfoWindowAccess;
import org.compiere.model.MProcess;
import org.compiere.model.MProcessAccess;
import org.compiere.model.MRole;
import org.compiere.model.MRoleIncluded;
import org.compiere.model.MTask;
import org.compiere.model.MWindow;
import org.compiere.model.MWindowAccess;
import org.compiere.model.Query;
import org.compiere.model.X_AD_Document_Action_Access;
import org.compiere.model.X_AD_Task_Access;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.wf.MWorkflow;
import org.compiere.wf.MWorkflowAccess;

public class TCS_CopyClientRole extends SvrProcess{

	int p_AD_Client_From_ID=0;
	int p_AD_Client_To_ID=0;

	@Override
	protected void prepare() {
		
		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++)
		{
			String name = para[i].getParameterName();
			if (para[i].getParameter() == null)
				;
			else if (name.equals("AD_Client_ID_From"))
				p_AD_Client_From_ID = para[i].getParameterAsInt();
			else if (name.equals("AD_Client_ID_To"))
				p_AD_Client_To_ID = para[i].getParameterAsInt();
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
	}

	@Override
	protected String doIt() throws Exception {
		
		//Get all role of client from
		String sqlRole = "AD_Client_ID="+p_AD_Client_From_ID;
		List<MRole> rolesFrom = new Query(getCtx(), MRole.Table_Name, sqlRole, get_TrxName())
								.setOrderBy("Name")
								.setOnlyActiveRecords(true)
								.list();
		/*for each role
		 * - check if role with same name exist in client to, if not create one
		 * - copy window access, process access, form access, info access, workflow access, 
		 * 		task access doc action access, and included role  from client from to client to
		 */
		String sqlCheckRoleExist = "SELECT AD_Role_ID FROM AD_Role WHERE AD_Client_ID="+p_AD_Client_To_ID+" AND Name=?";

		for (MRole roleFrom : rolesFrom) {
			
			//Check IF role already exist (role name case sensitive)
			int roleToID = DB.getSQLValue(get_TrxName(), sqlCheckRoleExist, roleFrom.getName());
			if (roleToID<0) roleToID=0;
			MRole roleTo = new MRole(getCtx(), roleToID, get_TrxName());
			
			//Header
			roleTo.set_ValueOfColumn("AD_Client_ID", p_AD_Client_To_ID);
			roleTo.setAD_Org_ID(0);
			roleTo.setName(roleFrom.getName());
			roleTo.setDescription(roleFrom.getDescription());
			roleTo.setIsActive(roleFrom.isActive());
			roleTo.setUserLevel(roleFrom.getUserLevel());
			roleTo.setIsManual(roleFrom.isManual());
			roleTo.setIsMasterRole(roleFrom.isMasterRole());
			roleTo.setC_Currency_ID(roleFrom.getC_Currency_ID());
			roleTo.setAmtApproval(roleFrom.getAmtApproval());
			roleTo.setDaysApprovalAccum(roleFrom.getDaysApprovalAccum());
			roleTo.setIsCanApproveOwnDoc(roleFrom.isCanApproveOwnDoc());
			roleTo.setAD_Tree_Menu_ID(roleFrom.getAD_Tree_Menu_ID());
			roleTo.setPreferenceType(roleFrom.getPreferenceType());
			roleTo.setIsShowAcct(roleFrom.isShowAcct());
			roleTo.setAD_Tree_Org_ID(roleFrom.getAD_Tree_Org_ID());
			roleTo.setIsCanReport(roleFrom.isCanReport());
			roleTo.setIsPersonalLock(roleFrom.isPersonalLock());
			roleTo.setIsAccessAdvanced(roleFrom.isAccessAdvanced());
			roleTo.setOverwritePriceLimit(roleFrom.isOverwritePriceLimit());
			roleTo.setIsChangeLog(roleFrom.isChangeLog());
			roleTo.setIsAccessAllOrgs(roleFrom.isAccessAllOrgs());
			roleTo.setIsUseUserOrgAccess(roleFrom.isUseUserOrgAccess());
			roleTo.setIsCanExport(roleFrom.isCanExport());
			roleTo.setIsPersonalAccess(roleFrom.isPersonalAccess());
			roleTo.setConfirmQueryRecords(roleFrom.getConfirmQueryRecords());
			roleTo.setMaxQueryRecords(roleFrom.getMaxQueryRecords());
			roleTo.setIsMenuAutoExpand(roleFrom.isMenuAutoExpand());
			roleTo.setAllow_Info_Account(roleFrom.isAllow_Info_Account());
			roleTo.setAllow_Info_Schedule(roleFrom.isAllow_Info_Schedule());
			roleTo.saveEx();
						
			copyWindowAccess(roleFrom, roleTo);
			copyProcessAccess(roleFrom, roleTo);
			copyFormAccess(roleFrom, roleTo);
			copyInfoWindowAccess(roleFrom, roleTo);
			copyWorkflowAccess(roleFrom, roleTo);
			copyTaskAccess(roleFrom, roleTo);
			copyDocActionAccess(roleFrom, roleTo);
			copyIncludedRoles(roleFrom, roleTo);
			
		}
		
		return null;
	}

	private void copyWindowAccess(MRole roleFrom, MRole roleTo){
		String sqlWhereWindow = "AD_Window_ID IN (SELECT AD_Window_ID FROM AD_Window_Access WHERE AD_Role_ID="+roleFrom.getAD_Role_ID()+")";
		List<MWindow> windows = new Query(getCtx(), MWindow.Table_Name, sqlWhereWindow, get_TrxName())
								.list();
		boolean exist;
		for (MWindow windowFrom : windows) {
			
			//Check If Access Already Exist
			exist = roleTo.getWindowAccess(windowFrom.getAD_Window_ID());
			if (exist) 
				continue;
			
			//Create New
			MWindowAccess windowAccTo = new MWindowAccess(windowFrom, roleTo.getAD_Role_ID());
			windowAccTo.saveEx();
		}
	}
	
	private void copyProcessAccess(MRole roleFrom, MRole roleTo){
		String sqlWhereProcess = "AD_Process_ID IN (SELECT AD_Process_ID FROM AD_Process_Access WHERE AD_Role_ID="+roleFrom.getAD_Role_ID()+")";
		List<MProcess> processes = new Query(getCtx(), MProcess.Table_Name, sqlWhereProcess, get_TrxName())
								.list();
		boolean exist;
		for (MProcess processFrom : processes) {
			
			//Check If Access Already Exist
			exist = roleTo.getProcessAccess(processFrom.getAD_Process_ID());
			if (exist) 
				continue;
			
			//Create New
			MProcessAccess processAccTo = new MProcessAccess(processFrom, roleTo.getAD_Role_ID());
			processAccTo.saveEx();
		}

	}

	private void copyFormAccess(MRole roleFrom, MRole roleTo){
		String sqlWhereForm = "AD_Form_ID IN (SELECT AD_Form_ID FROM AD_Form_Access WHERE AD_Role_ID="+roleFrom.getAD_Role_ID()+")";
		List<MForm> forms = new Query(getCtx(), MForm.Table_Name, sqlWhereForm, get_TrxName())
								.list();
		boolean exist;
		for (MForm formFrom : forms) {
			
			//Check If Access Already Exist
			exist = roleTo.getFormAccess(formFrom.getAD_Form_ID());
			if (exist) 
				continue;
			
			//Create New
			MFormAccess formAccTo = new MFormAccess(formFrom, roleTo.getAD_Role_ID());
			formAccTo.saveEx();
		}

	}

	private void copyInfoWindowAccess(MRole roleFrom, MRole roleTo){
		String sqlWhereInfoWindow = "AD_InfoWindow_ID IN (SELECT AD_InfoWindow_ID FROM AD_InfoWindow_Access WHERE AD_Role_ID="+roleFrom.getAD_Role_ID()+")";
		List<MInfoWindow> infoWindows = new Query(getCtx(), MInfoWindow.Table_Name, sqlWhereInfoWindow, get_TrxName())
								.list();
		boolean exist;
		for (MInfoWindow infoWindowFrom : infoWindows) {
			
			//Check If Access Already Exist
			exist = roleTo.getInfoAccess(infoWindowFrom.getAD_InfoWindow_ID());
			if (exist) 
				continue;
			
			//Create New
			MInfoWindowAccess infoWindowAccTo = new MInfoWindowAccess(infoWindowFrom, roleTo.getAD_Role_ID());
			infoWindowAccTo.saveEx();
		}

	}

	private void copyWorkflowAccess(MRole roleFrom, MRole roleTo){
		String sqlWhereWorkflow = "AD_Workflow_ID IN (SELECT AD_Workflow_ID FROM AD_Workflow_Access WHERE AD_Role_ID="+roleFrom.getAD_Role_ID()+")";
		List<MWorkflow> workflows = new Query(getCtx(), MWorkflow.Table_Name, sqlWhereWorkflow, get_TrxName())
								.list();
		boolean exist;
		for (MWorkflow workflowFrom : workflows) {
			
			//Check If Access Already Exist
			exist = roleTo.getWorkflowAccess(workflowFrom.getAD_Workflow_ID());
			if (exist) 
				continue;
			
			//Create New
			MWorkflowAccess workflowTo = new MWorkflowAccess(workflowFrom, roleTo.getAD_Role_ID());
			workflowTo.saveEx();
		}

	}

	private void copyTaskAccess(MRole roleFrom, MRole roleTo){
		String sqlWhereTask = "AD_Task_ID IN (SELECT AD_Task_ID FROM AD_Task_Access WHERE AD_Role_ID="+roleFrom.getAD_Role_ID()+")";
		List<MTask> tasks = new Query(getCtx(), MTask.Table_Name, sqlWhereTask, get_TrxName())
								.list();
		boolean exist;
		for (MTask taskFrom : tasks) {
			
			//Check If Access Already Exist
			exist = roleTo.getTaskAccess(taskFrom.getAD_Task_ID());
			if (exist) 
				continue;
			
			//Create New
			X_AD_Task_Access taskTo = new X_AD_Task_Access(getCtx(), 0, get_TrxName());
			taskTo.set_ValueOfColumn("AD_Client_ID", roleTo.getAD_Client_ID());
			taskTo.setAD_Org_ID(0);
			taskTo.setAD_Role_ID(roleTo.getAD_Role_ID());
			taskTo.setAD_Task_ID(taskFrom.getAD_Task_ID());
			taskTo.saveEx();
		}

	}

	private void copyDocActionAccess(MRole roleFrom, MRole roleTo){
		String sqlWhereDocAction = "AD_Role_ID="+roleFrom.getAD_Role_ID();
		List<X_AD_Document_Action_Access> DocActionAccesses = new Query(getCtx(), X_AD_Document_Action_Access.Table_Name, sqlWhereDocAction, get_TrxName())
								.setOrderBy("C_DocType_ID, AD_Ref_List_ID")
								.list();
		boolean exist;
		String checkDocActionAccessExist = "AD_Role_ID="+roleTo.getAD_Role_ID()+" AND C_DocType_ID=? AND AD_Ref_List_ID=?";
		for (X_AD_Document_Action_Access DocActionAccessFrom : DocActionAccesses) {
			
			//Check If Access Already Exist
			exist = new Query(getCtx(), X_AD_Document_Action_Access.Table_Name, checkDocActionAccessExist, get_TrxName())
					.setParameters(DocActionAccessFrom.getC_DocType_ID(), DocActionAccessFrom.getAD_Ref_List_ID())
					.match();
			if (exist) 
				continue;
			
			//Create New
			X_AD_Document_Action_Access DocActionAccessTo = new X_AD_Document_Action_Access(getCtx(), 0, get_TrxName());
			DocActionAccessTo.set_ValueOfColumn("AD_Client_ID", roleTo.getAD_Client_ID());
			DocActionAccessTo.setAD_Org_ID(0);
			DocActionAccessTo.setAD_Role_ID(roleTo.getAD_Role_ID());
			DocActionAccessTo.setC_DocType_ID(DocActionAccessFrom.getC_DocType_ID());
			DocActionAccessTo.setAD_Ref_List_ID(DocActionAccessFrom.getAD_Ref_List_ID());
			DocActionAccessTo.saveEx();
		}

	}

	private void copyIncludedRoles(MRole roleFrom, MRole roleTo){
		List<MRole> roles = roleFrom.getIncludedRoles(false);
		boolean exist;
		String sqlWhereRoleIncExist = "AD_Client_ID="+roleTo.getAD_Client_ID()+" AND AD_Role_ID="+roleTo.getAD_Role_ID()+" AND Included_Role_ID=?";
		for (MRole roleIncFrom : roles) {
			
			//Check If Access Already Exist
			exist = new Query(getCtx(), MRoleIncluded.Table_Name, sqlWhereRoleIncExist, get_TrxName())
					.setParameters(roleIncFrom.getAD_Role_ID())
					.match();
			if (exist) 
				continue;
			
			//Create New
			MRoleIncluded roleIncTo = new MRoleIncluded(getCtx(), 0, get_TrxName());
			roleIncTo.set_ValueOfColumn("AD_Client_ID", roleTo.getAD_Client_ID());
			roleIncTo.setAD_Org_ID(0);
			roleIncTo.setAD_Role_ID(roleTo.getAD_Role_ID());
			roleIncTo.setIncluded_Role_ID(roleIncFrom.getAD_Role_ID());
			roleIncTo.saveEx();
		}
	}

}
