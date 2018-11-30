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
import java.sql.Timestamp;
import org.compiere.model.*;
import org.compiere.util.KeyNamePair;

/** Generated Interface for TCS_FacilityLine
 *  @author iDempiere (generated) 
 *  @version Release 5.1
 */
@SuppressWarnings("all")
public interface I_TCS_FacilityLine 
{

    /** TableName=TCS_FacilityLine */
    public static final String Table_Name = "TCS_FacilityLine";

    /** AD_Table_ID=1000037 */
    public static final int Table_ID = MTable.getTable_ID(Table_Name);

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

    /** Column name C_Charge_ID */
    public static final String COLUMNNAME_C_Charge_ID = "C_Charge_ID";

	/** Set Charge.
	  * Additional document charges
	  */
	public void setC_Charge_ID (int C_Charge_ID);

	/** Get Charge.
	  * Additional document charges
	  */
	public int getC_Charge_ID();

	public org.compiere.model.I_C_Charge getC_Charge() throws RuntimeException;

    /** Column name Created */
    public static final String COLUMNNAME_Created = "Created";

	/** Get Created.
	  * Date this record was created
	  */
	public Timestamp getCreated();

    /** Column name CreatedBy */
    public static final String COLUMNNAME_CreatedBy = "CreatedBy";

	/** Get Created By.
	  * User who created this records
	  */
	public int getCreatedBy();

    /** Column name HC_Base_ID */
    public static final String COLUMNNAME_HC_Base_ID = "HC_Base_ID";

	/** Set HC_Base	  */
	public void setHC_Base_ID (int HC_Base_ID);

	/** Get HC_Base	  */
	public int getHC_Base_ID();

	public I_HC_Base getHC_Base() throws RuntimeException;

    /** Column name IsActive */
    public static final String COLUMNNAME_IsActive = "IsActive";

	/** Set Active.
	  * The record is active in the system
	  */
	public void setIsActive (boolean IsActive);

	/** Get Active.
	  * The record is active in the system
	  */
	public boolean isActive();

    /** Column name Level */
    public static final String COLUMNNAME_Level = "Level";

	/** Set Level	  */
	public void setLevel (int Level);

	/** Get Level	  */
	public int getLevel();

    /** Column name Price */
    public static final String COLUMNNAME_Price = "Price";

	/** Set Price.
	  * Price
	  */
	public void setPrice (BigDecimal Price);

	/** Get Price.
	  * Price
	  */
	public BigDecimal getPrice();

    /** Column name TCS_FacilityLine_ID */
    public static final String COLUMNNAME_TCS_FacilityLine_ID = "TCS_FacilityLine_ID";

	/** Set TCS_FacilityLine	  */
	public void setTCS_FacilityLine_ID (int TCS_FacilityLine_ID);

	/** Get TCS_FacilityLine	  */
	public int getTCS_FacilityLine_ID();

    /** Column name TCS_FacilityLine_UU */
    public static final String COLUMNNAME_TCS_FacilityLine_UU = "TCS_FacilityLine_UU";

	/** Set TCS_FacilityLine_UU	  */
	public void setTCS_FacilityLine_UU (String TCS_FacilityLine_UU);

	/** Get TCS_FacilityLine_UU	  */
	public String getTCS_FacilityLine_UU();

    /** Column name TCS_TripFacility_ID */
    public static final String COLUMNNAME_TCS_TripFacility_ID = "TCS_TripFacility_ID";

	/** Set TCS_TripFacility	  */
	public void setTCS_TripFacility_ID (int TCS_TripFacility_ID);

	/** Get TCS_TripFacility	  */
	public int getTCS_TripFacility_ID();

	public I_TCS_TripFacility getTCS_TripFacility() throws RuntimeException;

    /** Column name Updated */
    public static final String COLUMNNAME_Updated = "Updated";

	/** Get Updated.
	  * Date this record was updated
	  */
	public Timestamp getUpdated();

    /** Column name UpdatedBy */
    public static final String COLUMNNAME_UpdatedBy = "UpdatedBy";

	/** Get Updated By.
	  * User who updated this records
	  */
	public int getUpdatedBy();
}
