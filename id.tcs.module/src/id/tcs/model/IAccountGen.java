package id.tcs.model;

import java.util.Properties;

import org.compiere.model.GridField;
import org.compiere.model.GridTab;

public interface IAccountGen {

	String setAccount(Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value, Object oldValue, String columnName);
}
