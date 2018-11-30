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

/** Generated Interface for TCS_DestRequest
 *  @author iDempiere (generated) 
 *  @version Release 5.1
 */
@SuppressWarnings("all")
public interface I_TCS_DestRequest 
{

    /** TableName=TCS_DestRequest */
    public static final String Table_Name = "TCS_DestRequest";

    /** AD_Table_ID=1000025 */
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

    /** Column name DateFrom */
    public static final String COLUMNNAME_DateFrom = "DateFrom";

	/** Set Date From.
	  * Starting date for a range
	  */
	public void setDateFrom (Timestamp DateFrom);

	/** Get Date From.
	  * Starting date for a range
	  */
	public Timestamp getDateFrom();

    /** Column name DateTo */
    public static final String COLUMNNAME_DateTo = "DateTo";

	/** Set Date To.
	  * End date of a date range
	  */
	public void setDateTo (Timestamp DateTo);

	/** Get Date To.
	  * End date of a date range
	  */
	public Timestamp getDateTo();

    /** Column name HC_BaseCityFrom_ID */
    public static final String COLUMNNAME_HC_BaseCityFrom_ID = "HC_BaseCityFrom_ID";

	/** Set HC_BaseCityFrom	  */
	public void setHC_BaseCityFrom_ID (int HC_BaseCityFrom_ID);

	/** Get HC_BaseCityFrom	  */
	public int getHC_BaseCityFrom_ID();

	public I_HC_BaseCity getHC_BaseCityFrom() throws RuntimeException;

    /** Column name HC_BaseCityTo_ID */
    public static final String COLUMNNAME_HC_BaseCityTo_ID = "HC_BaseCityTo_ID";

	/** Set HC_BaseCityTo	  */
	public void setHC_BaseCityTo_ID (int HC_BaseCityTo_ID);

	/** Get HC_BaseCityTo	  */
	public int getHC_BaseCityTo_ID();

	public I_HC_BaseCity getHC_BaseCityTo() throws RuntimeException;

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

    /** Column name Processed */
    public static final String COLUMNNAME_Processed = "Processed";

	/** Set Processed.
	  * The document has been processed
	  */
	public void setProcessed (boolean Processed);

	/** Get Processed.
	  * The document has been processed
	  */
	public boolean isProcessed();

    /** Column name TCS_AdvRequest_ID */
    public static final String COLUMNNAME_TCS_AdvRequest_ID = "TCS_AdvRequest_ID";

	/** Set TCS_AdvRequest	  */
	public void setTCS_AdvRequest_ID (int TCS_AdvRequest_ID);

	/** Get TCS_AdvRequest	  */
	public int getTCS_AdvRequest_ID();

	public I_TCS_AdvRequest getTCS_AdvRequest() throws RuntimeException;

    /** Column name TCS_DestRequest_ID */
    public static final String COLUMNNAME_TCS_DestRequest_ID = "TCS_DestRequest_ID";

	/** Set TCS_DestRequest	  */
	public void setTCS_DestRequest_ID (int TCS_DestRequest_ID);

	/** Get TCS_DestRequest	  */
	public int getTCS_DestRequest_ID();

    /** Column name TCS_DestRequest_UU */
    public static final String COLUMNNAME_TCS_DestRequest_UU = "TCS_DestRequest_UU";

	/** Set TCS_DestRequest_UU	  */
	public void setTCS_DestRequest_UU (String TCS_DestRequest_UU);

	/** Get TCS_DestRequest_UU	  */
	public String getTCS_DestRequest_UU();

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
