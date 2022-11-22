package id.tcs.process;

import java.math.BigDecimal;
import java.util.logging.Level;

import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;

public class CreateAssetFromAssetAddition extends SvrProcess{
	
	private int p_A_Asset_Addition;
	private int p_M_Product_ID;
	private int p_ManufacturedYear;
	
	@Override
	protected void prepare() {
		p_A_Asset_Addition = getRecord_ID();
		
		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++)
		{
			String name = para[i].getParameterName();
			if (para[i].getParameter() == null)
				;
			else if (name.equals("M_Product_ID"))
				p_M_Product_ID = ((BigDecimal)para[i].getParameter()).intValue();
			else if (name.equals("ManufacturedYear"))
				p_ManufacturedYear = ((BigDecimal)para[i].getParameter()).intValue();
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
		
	}

	@Override
	protected String doIt() throws Exception {
		
		/* @win temporary solution
		
		MAssetAddition assetAddition = new MAssetAddition(getCtx(), p_A_Asset_Addition, get_TrxName());
		
		MAsset asset = new MAsset(getCtx(), 0, get_TrxName());
		asset.setAD_Org_ID(assetAddition.getAD_Org_ID());
		asset.setName(assetAddition.getA_NewAsset_Name());
		asset.setValue(assetAddition.getA_NewAsset_Value());
		asset.setA_Asset_Group_ID(assetAddition.getA_Asset_Group_ID());
		asset.setA_Asset_CreateDate(assetAddition.getCreated());
		asset.setM_Product_ID(p_M_Product_ID);
//		asset.setA_Asset_Status(MAsset.A_ASSET_STATUS_Activated);
		System.out.println(p_ManufacturedYear);
		asset.setManufacturedYear(p_ManufacturedYear);
		asset.setLocationComment(assetAddition.getLocationComment());
		asset.saveEx();
		
		
		assetAddition.setA_Asset_ID(asset.getA_Asset_ID());
		assetAddition.saveEx();
		*/
		return "Asset Created";
	}

}
