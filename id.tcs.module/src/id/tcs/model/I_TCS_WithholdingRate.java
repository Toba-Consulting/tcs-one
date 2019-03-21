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

/** Generated Interface for TCS_WithholdingRate
 *  @author iDempiere (generated) 
 *  @version Release 5.1
 */
@SuppressWarnings("all")
public interface I_TCS_WithholdingRate 
{

    /** TableName=TCS_WithholdingRate */
    public static final String Table_Name = "TCS_WithholdingRate";

    /** AD_Table_ID=300327 */
    public static final int Table_ID = 300327;

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

    /** Column name MaxAmt */
    public static final String COLUMNNAME_MaxAmt = "MaxAmt";

	/** Set Max Amount.
	  * Maximum Amount in invoice currency
	  */
	public void setMaxAmt (BigDecimal MaxAmt);

	/** Get Max Amount.
	  * Maximum Amount in invoice currency
	  */
	public BigDecimal getMaxAmt();

    /** Column name MinAmt */
    public static final String COLUMNNAME_MinAmt = "MinAmt";

	/** Set Min Amount.
	  * Minimum Amount in invoice currency
	  */
	public void setMinAmt (BigDecimal MinAmt);

	/** Get Min Amount.
	  * Minimum Amount in invoice currency
	  */
	public BigDecimal getMinAmt();

    /** Column name Rate */
    public static final String COLUMNNAME_Rate = "Rate";

	/** Set Rate.
	  * Rate or Tax or Exchange
	  */
	public void setRate (BigDecimal Rate);

	/** Get Rate.
	  * Rate or Tax or Exchange
	  */
	public BigDecimal getRate();

    /** Column name TCS_WithholdingRate_ID */
    public static final String COLUMNNAME_TCS_WithholdingRate_ID = "TCS_WithholdingRate_ID";

	/** Set Withholding Rate	  */
	public void setTCS_WithholdingRate_ID (int TCS_WithholdingRate_ID);

	/** Get Withholding Rate	  */
	public int getTCS_WithholdingRate_ID();

    /** Column name TCS_WithholdingType_ID */
    public static final String COLUMNNAME_TCS_WithholdingType_ID = "TCS_WithholdingType_ID";

	/** Set Withholding Type	  */
	public void setTCS_WithholdingType_ID (int TCS_WithholdingType_ID);

	/** Get Withholding Type	  */
	public int getTCS_WithholdingType_ID();

	public I_TCS_WithholdingType getTCS_WithholdingType() throws RuntimeException;

    /** Column name TCS_WithholdingType_UU */
    public static final String COLUMNNAME_TCS_WithholdingType_UU = "TCS_WithholdingType_UU";

	/** Set TCS_WithholdingType_UU	  */
	public void setTCS_WithholdingType_UU (String TCS_WithholdingType_UU);

	/** Get TCS_WithholdingType_UU	  */
	public String getTCS_WithholdingType_UU();

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
