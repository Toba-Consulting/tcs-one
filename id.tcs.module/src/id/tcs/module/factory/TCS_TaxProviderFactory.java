package id.tcs.module.factory;

import org.adempiere.base.ITaxProviderFactory;
import org.adempiere.model.ITaxProvider;

import id.tcs.model.TCS_TaxProvider;

public class TCS_TaxProviderFactory implements ITaxProviderFactory{
	
	private static final String DEFAULT_TAX_PROVIDER = "org.compiere.model.StandardTaxProvider"; 
	@Override
	public ITaxProvider newTaxProviderInstance(String className) {
		// TODO Auto-generated method stub
		/*if ( className.equalsIgnoreCase(DEFAULT_TAX_PROVIDER)){
			return new StandardTaxProvider();
		}
		*/
		return new TCS_TaxProvider();
	}

}
