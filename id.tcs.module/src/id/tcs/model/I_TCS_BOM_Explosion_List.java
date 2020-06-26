/******************************************************************************
 * Product: iDempiere ERP & CRM Smart Business Solution                       *
 * Copyright (C) 1999-2012 ComPiere, Inc. All Rights Reserved.                *
 * This program is free software, you can redistribute it and/or modify it    *
 * under the terms version 2 of the GNU General Public License as published   *
 * by the Free Software Foundation. This program is distributed in the hope   *
 * that it will be useful, but WITHOUT ANY WARRANTY, without even the implied *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
 * See the GNU General Public License for more details.                       *
 * You should have received a copy of the GNU General Public License along    *
 * with this program, if not, write to the Free Software Foundation, Inc.,    *
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     *
 * For the text or an alternative of this public license, you may reach us    *
 * ComPiere, Inc., 2620 Augustine Dr. #245, Santa Clara, CA 95054, USA        *
 * or via info@compiere.org or http://www.compiere.org/license.html           *
 *****************************************************************************/
package id.tcs.model;

import java.math.BigDecimal;
import org.compiere.model.*;
import org.compiere.util.KeyNamePair;

/** Generated Interface for TCS_BOM_Explosion_List
 *  @author iDempiere (generated) 
 *  @version Release 5.1
 */
@SuppressWarnings("all")
public interface I_TCS_BOM_Explosion_List 
{

    /** TableName=TCS_BOM_Explosion_List */
    public static final String Table_Name = "TCS_BOM_Explosion_List";

    /** AD_Table_ID=300731 */
    public static final int Table_ID = 300731;

    KeyNamePair Model = new KeyNamePair(Table_ID, Table_Name);

    /** AccessLevel = 3 - Client - Org 
     */
    BigDecimal accessLevel = BigDecimal.valueOf(3);

    /** Load Meta Data */

    /** Column name AD_Client_ID */
    public static final String COLUMNNAME_AD_Client_ID = "AD_Client_ID";

	/** Get Client.
	  * Client/Tenant for this installation.
	  */
	public int getAD_Client_ID();

    /** Column name AD_Org_ID */
    public static final String COLUMNNAME_AD_Org_ID = "AD_Org_ID";

	/** Set Organization.
	  * Organizational entity within client
	  */
	public void setAD_Org_ID (int AD_Org_ID);

	/** Get Organization.
	  * Organizational entity within client
	  */
	public int getAD_Org_ID();

    /** Column name bomdistance */
    public static final String COLUMNNAME_bomdistance = "bomdistance";

	/** Set bomdistance	  */
	public void setbomdistance (int bomdistance);

	/** Get bomdistance	  */
	public int getbomdistance();

    /** Column name M_Product_Child_ID */
    public static final String COLUMNNAME_M_Product_Child_ID = "M_Product_Child_ID";

	/** Set M_Product_Child_ID	  */
	public void setM_Product_Child_ID (int M_Product_Child_ID);

	/** Get M_Product_Child_ID	  */
	public int getM_Product_Child_ID();

    /** Column name M_Product_ID */
    public static final String COLUMNNAME_M_Product_ID = "M_Product_ID";

	/** Set Product.
	  * Product, Service, Item
	  */
	public void setM_Product_ID (int M_Product_ID);

	/** Get Product.
	  * Product, Service, Item
	  */
	public int getM_Product_ID();

	public org.compiere.model.I_M_Product getM_Product() throws RuntimeException;

    /** Column name M_Product_Parent_ID */
    public static final String COLUMNNAME_M_Product_Parent_ID = "M_Product_Parent_ID";

	/** Set M_Product_Parent_ID	  */
	public void setM_Product_Parent_ID (int M_Product_Parent_ID);

	/** Get M_Product_Parent_ID	  */
	public int getM_Product_Parent_ID();

    /** Column name TCS_BOM_Explosion_List_ID */
    public static final String COLUMNNAME_TCS_BOM_Explosion_List_ID = "TCS_BOM_Explosion_List_ID";

	/** Set TCS_BOM_Explosion_List	  */
	public void setTCS_BOM_Explosion_List_ID (int TCS_BOM_Explosion_List_ID);

	/** Get TCS_BOM_Explosion_List	  */
	public int getTCS_BOM_Explosion_List_ID();
}
