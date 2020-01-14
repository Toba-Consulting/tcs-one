package id.tcs.webui.apps.form;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Vector;
import java.util.logging.Level;

import org.adempiere.webui.apps.AEnv;
import org.adempiere.webui.apps.form.WCreateFromWindow;
import org.adempiere.webui.component.Grid;
import org.adempiere.webui.component.GridFactory;
import org.adempiere.webui.component.Label;
import org.adempiere.webui.component.ListItem;
import org.adempiere.webui.component.ListModelTable;
import org.adempiere.webui.component.Listbox;
import org.adempiere.webui.component.ListboxFactory;
import org.adempiere.webui.component.Panel;
import org.adempiere.webui.component.Row;
import org.adempiere.webui.component.Rows;
import org.adempiere.webui.editor.WEditor;
import org.adempiere.webui.editor.WSearchEditor;
import org.adempiere.webui.event.ValueChangeEvent;
import org.adempiere.webui.event.ValueChangeListener;
import org.compiere.model.GridTab;
import org.compiere.model.MLookup;
import org.compiere.model.MLookupFactory;
import org.compiere.model.MOrder;
import org.compiere.model.MOrderLine;
import org.compiere.process.DocAction;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.DisplayType;
import org.compiere.util.Env;
import org.compiere.util.KeyNamePair;
import org.compiere.util.Msg;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Borderlayout;
import org.zkoss.zul.Center;

public class TCS_WDDOrderCreateFromCOrder extends TCS_DDOrderCreateFromCOrder implements EventListener<Event>, ValueChangeListener{

	public TCS_WDDOrderCreateFromCOrder(GridTab mTab) {
		super(mTab);
		log.info(getGridTab().toString());
		
		window = new WCreateFromWindow(this, getGridTab().getWindowNo());
		
		p_WindowNo = getGridTab().getWindowNo();

		try
		{
			if (!dynInit())
				return;
			zkInit();
			setInitOK(true);
		}
		catch(Exception e)
		{
			log.log(Level.SEVERE, "", e);
			setInitOK(false);
		}
		AEnv.showWindow(window);
	}

	private WCreateFromWindow window;
	private int p_WindowNo;
	private boolean 	m_actionActive = false;

	private CLogger log = CLogger.getCLogger(getClass());
	
	protected Label orderLabel = new Label();
//	protected Listbox orderField = ListboxFactory.newDropdownListbox();
	protected WEditor orderFielda;
//	protected Label productLabel = new Label();
//	protected WEditor productField;
//	protected Label dateRequiredLabel = new Label();
//	protected WEditor dateRequiredField;
//	protected Label projectLabel = new Label();
//	protected WEditor projectField;
	

	protected void zkInit() throws Exception
	{
		initSelectionFieldsOptions();

		orderLabel.setText(Msg.getElement(Env.getCtx(), "C_Order_ID", false));
//		productLabel.setText(Msg.getElement(Env.getCtx(), "M_Product_ID", false));
//		dateRequiredLabel.setText(Msg.getElement(Env.getCtx(), "DateRequired", false));
//		projectLabel.setText(Msg.getElement(Env.getCtx(), "C_Project_ID", false));
		//chargeLabel.setText(Msg.getElement(Env.getCtx(), "C_Charge_ID", false));
		
    
		Borderlayout parameterLayout = new Borderlayout();
		parameterLayout.setHeight("110px");
		parameterLayout.setWidth("100%");
    	Panel parameterPanel = window.getParameterPanel();
		parameterPanel.appendChild(parameterLayout);
		
		Grid parameterStdLayout = GridFactory.newGridLayout();
    	Panel parameterStdPanel = new Panel();
		parameterStdPanel.appendChild(parameterStdLayout);

		Center center = new Center();
		parameterLayout.appendChild(center);
		center.appendChild(parameterStdPanel);
		
		Rows rows = (Rows) parameterStdLayout.newRows();
		
		Row row = rows.newRow();
		
		row.appendChild(orderLabel.rightAlign());
//		orderField.setHflex("1");
//		row.appendChild(orderField);

//		row.appendChild(orderLabel.rightAlign());
		row.appendChild(orderFielda.getComponent());

//		row = rows.newRow();
//		
//		row.appendChild(projectLabel.rightAlign());
//		if (projectField != null)
//			row.appendChild(projectField.getComponent());	
//		
//		row = rows.newRow();
		
	}
	
	protected void initSelectionFieldsOptions(){
		
//		String sql = "SELECT AD_Column_ID FROM AD_Column ac JOIN AD_Table at on at.AD_Table_ID=ac.AD_Table_ID WHERE at.AD_Table_ID="+MOrder.Table_ID+" AND ac.ColumnName='"+MOrderLine.COLUMNNAME_M_Product_ID+"'";
//		int productColumn_ID = DB.getSQLValue(null, sql);
//		MLookup lookupProduct = MLookupFactory.get (Env.getCtx(), p_WindowNo, 0, productColumn_ID, DisplayType.Search);
//		productField = new WSearchEditor ("M_Product_ID", true, false, true, lookupProduct);
		
//		String sql = "SELECT AD_Column_ID FROM AD_Column ac JOIN AD_Table at on at.AD_Table_ID=ac.AD_Table_ID WHERE at.AD_Table_ID="+MOrder.Table_ID+" AND ac.ColumnName='"+MOrderLine.COLUMNNAME_C_Project_ID+"'";
//		int projectColumn_ID = DB.getSQLValue(null, sql);
//		MLookup lookupProject = MLookupFactory.get (Env.getCtx(), p_WindowNo, 0, projectColumn_ID, DisplayType.Search);
//		projectField = new WSearchEditor ("C_Project_ID", false, false, true, lookupProject);
//		int C_Project_ID = Env.getContextAsInt(Env.getCtx(), p_WindowNo, "C_Project_ID");
//		projectField.setValue(new Integer(C_Project_ID));

		String sql = "SELECT AD_Column_ID FROM AD_Column ac JOIN AD_Table at on at.AD_Table_ID=ac.AD_Table_ID WHERE at.AD_Table_ID="+MOrder.Table_ID+" AND ac.ColumnName='"+MOrder.COLUMNNAME_C_Order_ID+"'";
		int orderColumn_ID = DB.getSQLValue(null, sql);
		MLookup lookupOrder = MLookupFactory.get (Env.getCtx(), p_WindowNo, 0, orderColumn_ID, DisplayType.Search);
		orderFielda = new WSearchEditor ("C_Order_ID", false, false, true, lookupOrder);
		orderFielda.setValue(null);
		orderFielda.addValueChangeListener(this);
		
		KeyNamePair pp = new KeyNamePair(0,"");
//		orderField.removeActionListener(this);
//		orderField.removeAllItems();
//		orderField.addItem(pp);
		
//		ArrayList<KeyNamePair> list = loadOrderData(C_Project_ID);
		ArrayList<KeyNamePair> list = loadOrderData(0);
//		for(KeyNamePair knp : list)
//			orderField.addItem(knp);

//		orderField.setSelectedIndex(0);
//		orderField.addActionListener(this);
//		productField.addValueChangeListener(this);
//		dateRequiredField.addValueChangeListener(this);
//		projectField.addValueChangeListener(this);
		
		
	}
	
	protected ArrayList<KeyNamePair> loadOrderData (int C_Project_ID)
	{

		ArrayList<KeyNamePair> list = new ArrayList<KeyNamePair>();

		int AD_Client_ID = Env.getContextAsInt(Env.getCtx(), getGridTab().getWindowNo(), "AD_Client_ID");
        
		//	Display
		StringBuilder display = new StringBuilder("DocumentNo");
			//.append(DB.TO_CHAR("r.TotaLines", DisplayType.Amount, Env.getAD_Language(Env.getCtx())));
		
		StringBuilder sql = new StringBuilder("SELECT DISTINCT co.C_Order_ID, co.").append(display)
			.append(" FROM C_Order co ")
			.append(" WHERE EXISTS (SELECT 1 FROM C_OrderLine l WHERE co.C_Order_ID=l.C_Order_ID")
			.append(" AND l.AD_Client_ID=? AND co.DocStatus IN (?,?))");

		if (C_Project_ID > 0) {
			sql.append(" AND co.C_Project_ID=? ");
		} else {
			sql.append(" AND co.C_Project_ID IS NULL ");
		}
        
		sql = sql.append(" ORDER BY co.DocumentNo DESC");
		//
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			int count = 0;
			pstmt = DB.prepareStatement(sql.toString(), null);
			pstmt.setInt(++count, AD_Client_ID);
			pstmt.setString(++count, DocAction.STATUS_Completed);
			pstmt.setString(++count, DocAction.STATUS_Closed);
			if (C_Project_ID > 0) {
				pstmt.setInt(++count, C_Project_ID);
			}

			rs = pstmt.executeQuery();
			while (rs.next())
			{
				list.add(new KeyNamePair(rs.getInt(1), rs.getString(2)));
			}
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, sql.toString(), e);
		}
		finally
		{
			DB.close(rs, pstmt);
			rs = null; pstmt = null;
		}

		return list;
	
	}
	
	protected void loadOrder(int C_Order_ID, int C_Project_ID){
		loadTableOIS(getOrderData(C_Order_ID, C_Project_ID));
	}

	protected void loadTableOIS (Vector<?> data)
	{
		window.getWListbox().clear();
		
		//  Remove previous listeners
		window.getWListbox().getModel().removeTableModelListener(window);
		//  Set Model
		ListModelTable model = new ListModelTable(data);
		model.addTableModelListener(window);
		window.getWListbox().setData(model, getOISColumnNames());
		//
		
		configureMiniTable(window.getWListbox());
	}   //  loadOrder
	
	@Override
	public void valueChange(ValueChangeEvent e) {
		if (log.isLoggable(Level.CONFIG)) log.config(e.getPropertyName() + "=" + e.getNewValue());
		int C_Order_ID = 0;
		int C_Project_ID = 0;

		if (e.getPropertyName().equals("C_Order_ID"))
		{
			if (e.getNewValue() != null)
				C_Order_ID = ((Integer)e.getNewValue()).intValue();
						
//			if (projectField.getValue() != null)
//				C_Project_ID =  ((Integer)projectField.getValue());
			
			loadOrder(C_Order_ID,C_Project_ID);

		}
		
//		else if (e.getPropertyName().equals("C_Project_ID"))
//		{
//			ListItem li = orderField.getSelectedItem();
//			if (li != null && li.getValue() != null)
//				C_Order_ID = ((Integer) li.getValue()).intValue();
//
//			if (e.getNewValue() != null)
//				C_Project_ID =  ((Integer)e.getNewValue()).intValue();
//			
//			loadOrder(C_Order_ID,C_Project_ID);
//		}
	}

	@Override
	public void onEvent(Event e) throws Exception {
		if (m_actionActive)
			return;
		m_actionActive = true;
		
//		ListItem li = null;
//		Timestamp dateRequired = null;
//		int M_Product_ID = 0;
//		int C_Charge_ID = 0;
//		int C_Project_ID = 0;
//		int C_Order_ID = 0;
//		int salesRepID = Env.getContextAsInt(Env.getCtx(), p_WindowNo, "SalesRep_ID");
		
		//  Requisition
//		if (e.getTarget().equals(orderField)) 
//		if (e.getTarget().equals(orderField) || 
//				e.getTarget().equals(productField) || 
//				e.getTarget().equals(projectField))
//		{
//			li = orderField.getSelectedItem();
//			if (li != null && li.getValue() != null)
//				C_Order_ID = ((Integer) li.getValue()).intValue();
			
//			if (productField.getValue() != null) 
//				M_Product_ID = (Integer) productField.getValue();
						
//			if (projectField.getValue() != null) 
//				C_Project_ID = (Integer) projectField.getValue();
			
//			if (chargeField.getValue() != null) 
//				C_Charge_ID = (Integer) chargeField.getValue();
			
//			loadOrder(C_Order_ID, C_Project_ID);
//		} 
		
		m_actionActive = false;
	}

	public void showWindow()
	{
		window.setVisible(true);
	}
	
	public void closeWindow()
	{
		window.dispose();
	}

	@Override
	public Object getWindow() {
		return window;
	}
}
