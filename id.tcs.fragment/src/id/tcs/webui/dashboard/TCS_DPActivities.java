/******************************************************************************
 * Copyright (C) 2008 Elaine Tan                                              *
 * Copyright (C) 2008 Idalica Corporation                                     *
 * This program is free software; you can redistribute it and/or modify it    *
 * under the terms version 2 of the GNU General Public License as published   *
 * by the Free Software Foundation. This program is distributed in the hope   *
 * that it will be useful, but WITHOUT ANY WARRANTY; without even the implied *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
 * See the GNU General Public License for more details.                       *
 * You should have received a copy of the GNU General Public License along    *
 * with this program; if not, write to the Free Software Foundation, Inc.,    *
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     *
 *****************************************************************************/
package id.tcs.webui.dashboard;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

import org.adempiere.webui.component.Button;
import org.adempiere.webui.dashboard.DPActivities;
import org.adempiere.webui.dashboard.DPActivitiesModel;
import org.adempiere.webui.dashboard.DashboardPanel;
import org.adempiere.webui.desktop.IDesktop;
import org.adempiere.webui.session.SessionManager;
import org.adempiere.webui.theme.ThemeManager;
import org.adempiere.webui.util.ServerPushTemplate;
import org.compiere.model.MRole;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;
import org.compiere.util.Util;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.EventQueue;
import org.zkoss.zk.ui.event.EventQueues;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Box;
import org.zkoss.zul.Vbox;

/**
 * Dashboard item: Workflow activities, notices and requests
 * @author Elaine
 * @date November 20, 2008
 * 
 * Contributors:
 * CarlosRuiz - globalqss - Add unprocessed button to iDempiere
 * 
 * 
 * Contributors: 
 * Deepak Pansheriya - showing only notes message
 */
public class TCS_DPActivities extends DashboardPanel implements EventListener<Event> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3787249181565314148L;

	@SuppressWarnings("unused")
	private static final CLogger logger = CLogger.getCLogger(DPActivities.class);

	private Button btnNotice, btnRequest, btnWorkflow, btnUnprocessed;

	private String labelN, labelR, labelW, labelU;
	
	private int noOfNotice, noOfRequest, noOfWorkflow, noOfUnprocessed;

	public TCS_DPActivities()
	{
		super();
		this.setSclass("activities-box");
        this.appendChild(createActivitiesPanel());
	}

	private Box createActivitiesPanel()
	{
		Vbox vbox = new Vbox();

        btnNotice = new Button();
        vbox.appendChild(btnNotice);
        labelN = Util.cleanAmp(Msg.translate(Env.getCtx(), "AD_Note_ID"));
        btnNotice.setLabel(labelN + " : 0");
        btnNotice.setTooltiptext(labelN);
        btnNotice.setImage(ThemeManager.getThemeResource("images/GetMail16.png"));
        int AD_Menu_ID = DB.getSQLValue(null, "SELECT AD_Menu_ID FROM AD_Menu WHERE Name = 'Notice' AND IsSummary = 'N'");
        btnNotice.setName(String.valueOf(AD_Menu_ID));
        btnNotice.addEventListener(Events.ON_CLICK, this);

        btnRequest = new Button();
        vbox.appendChild(btnRequest);
        labelR = Util.cleanAmp(Msg.translate(Env.getCtx(), "R_Request_ID"));
        btnRequest.setLabel(labelR + " : 0");
        btnRequest.setTooltiptext(labelR);
        btnRequest.setImage(ThemeManager.getThemeResource("images/Request16.png"));
        AD_Menu_ID = DB.getSQLValue(null, "SELECT AD_Menu_ID FROM AD_Menu WHERE Name = 'Request' AND IsSummary = 'N'");
        btnRequest.setName(String.valueOf(AD_Menu_ID));
        btnRequest.addEventListener(Events.ON_CLICK, this);

        btnWorkflow = new Button();
        vbox.appendChild(btnWorkflow);
        labelW = Util.cleanAmp(Msg.translate(Env.getCtx(), "WorkflowActivities"));
        btnWorkflow.setLabel(labelW + " : 0");
        btnWorkflow.setTooltiptext(labelW);
        btnWorkflow.setImage(ThemeManager.getThemeResource("images/Assignment16.png"));
        AD_Menu_ID = DB.getSQLValue(null, "SELECT AD_Menu_ID FROM AD_Menu WHERE Name = 'Workflow Activities' AND IsSummary = 'N'");
        btnWorkflow.setName(String.valueOf(AD_Menu_ID));
        btnWorkflow.addEventListener(Events.ON_CLICK, this);

        if (DPActivitiesModel.isShowUnprocessed()) {
            btnUnprocessed = new Button();
            vbox.appendChild(btnUnprocessed);
            labelU = Util.cleanAmp(Msg.translate(Env.getCtx(), "UnprocessedDocs"));
            btnUnprocessed.setLabel(labelU + " : 0");
            btnUnprocessed.setTooltiptext(labelU);
            btnUnprocessed.setImage(ThemeManager.getThemeResource("images/Open16.png"));
            AD_Menu_ID = DB.getSQLValue(null, "SELECT AD_Menu_ID FROM AD_Menu WHERE Name = 'My Unprocessed Documents' AND IsSummary = 'N'");
            btnUnprocessed.setName(String.valueOf(AD_Menu_ID));
            btnUnprocessed.addEventListener(Events.ON_CLICK, this);
        }

        return vbox;
	}

	

	@Override
    public void refresh(ServerPushTemplate template)
	{
		int notice = DPActivitiesModel.getNoticeCount();
		int request = DPActivitiesModel.getRequestCount();
		int workflow = getActivitiesCount();
		int unprocessed = DPActivitiesModel.getUnprocessedCount();
		if (noOfNotice != notice || noOfRequest != request 
			|| noOfWorkflow != workflow || noOfUnprocessed != unprocessed )
		{
			noOfNotice = notice;
			noOfRequest = request;
			noOfWorkflow = workflow;
			noOfUnprocessed = unprocessed;
			template.executeAsync(this);
		}
	}

    @Override
	public void updateUI() {
    	btnNotice.setLabel(labelN + " : " + noOfNotice);
		btnRequest.setLabel(labelR + " : " + noOfRequest);
		btnWorkflow.setLabel(labelW + " : " + noOfWorkflow);
		if (DPActivitiesModel.isShowUnprocessed()) 
			btnUnprocessed.setLabel(labelU + " : " + noOfUnprocessed);
		
		EventQueue<Event> queue = EventQueues.lookup(IDesktop.ACTIVITIES_EVENT_QUEUE, true);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("notice", noOfNotice);
		map.put("request", noOfRequest);
		map.put("workflow", noOfWorkflow);
		map.put("unprocessed", noOfUnprocessed);
		Event event = new Event(IDesktop.ON_ACTIVITIES_CHANGED_EVENT, null, map);
		queue.publish(event);
	}

	@Override
	public boolean isPooling() {
		return true;
	}

	public void onEvent(Event event)
    {
        Component comp = event.getTarget();
        String eventName = event.getName();

        if(eventName.equals(Events.ON_CLICK))
        {
            if(comp instanceof Button)
            {
            	Button btn = (Button) comp;

            	int menuId = 0;
            	try
            	{
            		menuId = Integer.valueOf(btn.getName());
            	}
            	catch (Exception e) {

				}

            	if(menuId > 0) SessionManager.getAppDesktop().onMenuSelected(menuId);
            }
        }
	}
	
	//@PhieAlbert
	public int getActivitiesCount()
	{
		int count = 0;

		String sql = "SELECT COUNT(*) FROM AD_WF_Activity a "
			+ "WHERE " + getWhereActivities();
		int AD_User_ID = Env.getAD_User_ID(Env.getCtx());
		int AD_Client_ID = Env.getAD_Client_ID(Env.getCtx());
		MRole role = MRole.get(Env.getCtx(), Env.getAD_Role_ID(Env.getCtx()));
		sql = role.addAccessSQL(sql, "a", true, false);
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement (sql, null);
			pstmt.setInt (1, AD_User_ID);
			pstmt.setInt (2, AD_Client_ID);
			rs = pstmt.executeQuery ();
			if (rs.next ()) {
				count = rs.getInt(1);
			}
		}
		catch (Exception e)
		{
			//log.log(Level.SEVERE, sql, e);
		}
		finally
		{
			DB.close(rs, pstmt);
			rs = null; pstmt = null;
		}

		return count;

	}
	//end @PhieAlbert
	
	//@PhieAlbert
	private String getWhereActivities() {
		final String where =
			"a.Processed='N' AND a.WFState='OS' AND ("
			//	Owner of Activity
			//+ " a.AD_User_ID=? OR"	//	#1
			//	Invoker (if no invoker = all)
			//+ "  EXISTS (SELECT * FROM AD_WF_Responsible r WHERE a.AD_WF_Responsible_ID=r.AD_WF_Responsible_ID"
			//+ " AND r.ResponsibleType='H' AND COALESCE(r.AD_User_ID,0)=0 AND COALESCE(r.AD_Role_ID,0)=0 AND (a.AD_User_ID=? OR a.AD_User_ID IS NULL))"	//	#2
			//  Responsible User
			//+ " OR EXISTS (SELECT * FROM AD_WF_Responsible r WHERE a.AD_WF_Responsible_ID=r.AD_WF_Responsible_ID"
			//+ " AND r.ResponsibleType='H' AND r.AD_User_ID=?)"		//	#3
			//	Responsible Role
			//+ " OR EXISTS (SELECT * FROM AD_WF_Responsible r INNER JOIN AD_User_Roles ur ON (r.AD_Role_ID=ur.AD_Role_ID)"
			//+ " WHERE a.AD_WF_Responsible_ID=r.AD_WF_Responsible_ID AND r.ResponsibleType='R' AND ur.AD_User_ID=? AND ur.isActive = 'Y')"	//	#4
			///* Manual Responsible */ 
			/*
			+ " OR EXISTS (SELECT * FROM AD_WF_ActivityApprover r "
			+ " WHERE a.AD_WF_Activity_ID=r.AD_WF_Activity_ID AND r.AD_User_ID=? AND r.isActive = 'Y')" 
			+ ") AND a.AD_Client_ID=?";	//	#5
			*/
			//PhieAlbert
			+ " EXISTS (SELECT * FROM AD_WF_ActivityApprover r "
			+ " WHERE a.AD_WF_Activity_ID=r.AD_WF_Activity_ID AND r.AD_User_ID=? AND r.isActive = 'Y' AND isApproved='N'"
			+ " AND Sequence=(SELECT COALESCE(MIN(Sequence),0) FROM AD_WF_ActivityApprover r2 WHERE a.AD_WF_Activity_ID=r2.AD_WF_Activity_ID  AND r2.isApproved='N'))" 
			+ ") AND a.AD_Client_ID=?";	//	#5
			//end @PhieAlbert
		return where;
	}
	//end @PhieAlbert
}
