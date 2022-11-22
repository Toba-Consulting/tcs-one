package id.tcs.module.factory;

import java.util.logging.Level;

import org.adempiere.webui.factory.IFormFactory;
import org.adempiere.webui.panel.ADForm;
import org.adempiere.webui.panel.IFormController;
import org.compiere.util.CLogger;

public class TCS_FormFactory implements IFormFactory{

	protected transient CLogger log = CLogger.getCLogger(getClass());

	@Override
	public ADForm newFormInstance(String formName) {
		if(formName.startsWith("id.tcs.webui.apps.form")){
			Object form = null;
			Class<?> clazz = null;
			ClassLoader loader = getClass().getClassLoader();
			try {
				clazz = loader.loadClass(formName);
			} catch (Exception e) {
				log.log(Level.FINE,"Load Form Class Failed in id.tcs.module.factory.TCS_FormFactory",e);
			}
			//
			if(clazz != null){
				try {
					form = clazz.newInstance();
				} catch (Exception e) {
					log.log(Level.FINE,"Form Class Initiate Failed in id.tcs.module.factory.TCS_FormFactory",e);
				}
			}
			//
			if(form != null){
				if(form instanceof ADForm){
					return (ADForm) form;
				}else if(form instanceof IFormController){
					IFormController controller = (IFormController) form;
					ADForm adForm = controller.getForm();
					adForm.setICustomForm(controller);
					return adForm;
				}
			}

		}
		return null;
	}
}