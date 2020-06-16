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
package org.compiere.model;

import java.math.BigDecimal;
import java.sql.Timestamp;
import org.compiere.model.*;
import org.compiere.util.KeyNamePair;

/** Generated Interface for M_Periodic_Cost
 *  @author iDempiere (generated) 
 *  @version Release 3.1
 */
@SuppressWarnings("all")
public interface I_M_Periodic_Cost 
{

    /** TableName=M_Periodic_Cost */
    public static final String Table_Name = "M_Periodic_Cost";

    /** AD_Table_ID=1100441 */
    public static final int Table_ID = MTable.getTable_ID(Table_Name);

    KeyNamePair Model = new KeyNamePair(Table_ID, Table_Name);

    /** AccessLevel = 4 - System 
     */
    BigDecimal accessLevel = BigDecimal.valueOf(4);

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

    /** Column name BeginningQty */
    public static final String COLUMNNAME_BeginningQty = "BeginningQty";

	/** Set BeginningQty	  */
	public void setBeginningQty (BigDecimal BeginningQty);

	/** Get BeginningQty	  */
	public BigDecimal getBeginningQty();

    /** Column name BeginnningAmount */
    public static final String COLUMNNAME_BeginningAmount = "BeginningAmount";

	/** Set BeginnningAmount	  */
	public void setBeginningAmount (BigDecimal BeginnningAmount);

	/** Get BeginnningAmount	  */
	public BigDecimal getBeginningAmount();

    /** Column name C_Period_ID */
    public static final String COLUMNNAME_C_Period_ID = "C_Period_ID";

	/** Set Period.
	  * Period of the Calendar
	  */
	public void setC_Period_ID (int C_Period_ID);

	/** Get Period.
	  * Period of the Calendar
	  */
	public int getC_Period_ID();

	public org.compiere.model.I_C_Period getC_Period() throws RuntimeException;

    /** Column name CostPrice */
    public static final String COLUMNNAME_CostPrice = "CostPrice";

	/** Set Cost Price	  */
	public void setCostPrice (BigDecimal CostPrice);

	/** Get Cost Price	  */
	public BigDecimal getCostPrice();

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

    /** Column name EndingAmount */
    public static final String COLUMNNAME_EndingAmount = "EndingAmount";

	/** Set EndingAmount	  */
	public void setEndingAmount (BigDecimal EndingAmount);

	/** Get EndingAmount	  */
	public BigDecimal getEndingAmount();

    /** Column name EndingQty */
    public static final String COLUMNNAME_EndingQty = "EndingQty";

	/** Set EndingQty	  */
	public void setEndingQty (BigDecimal EndingQty);

	/** Get EndingQty	  */
	public BigDecimal getEndingQty();

    /** Column name IPV_Amount */
    public static final String COLUMNNAME_IPV_Amount = "IPV_Amount";

	/** Set IPV_Amount	  */
	public void setIPV_Amount (BigDecimal IPV_Amount);

	/** Get IPV_Amount	  */
	public BigDecimal getIPV_Amount();

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

    /** Column name IssueAmount */
    public static final String COLUMNNAME_IssueAmount = "IssueAmount";

	/** Set IssueAmount	  */
	public void setIssueAmount (BigDecimal IssueAmount);

	/** Get IssueAmount	  */
	public BigDecimal getIssueAmount();

    /** Column name IssueQty */
    public static final String COLUMNNAME_IssueQty = "IssueQty";

	/** Set IssueQty	  */
	public void setIssueQty (BigDecimal IssueQty);

	/** Get IssueQty	  */
	public BigDecimal getIssueQty();

    /** Column name LandedCostAmount */
    public static final String COLUMNNAME_LandedCostAmount = "LandedCostAmount";

	/** Set LandedCostAmount	  */
	public void setLandedCostAmount (BigDecimal LandedCostAmount);

	/** Get LandedCostAmount	  */
	public BigDecimal getLandedCostAmount();

    /** Column name M_AttributeSetInstance_ID */
    public static final String COLUMNNAME_M_AttributeSetInstance_ID = "M_AttributeSetInstance_ID";

	/** Set Attribute Set Instance.
	  * Product Attribute Set Instance
	  */
	public void setM_AttributeSetInstance_ID (int M_AttributeSetInstance_ID);

	/** Get Attribute Set Instance.
	  * Product Attribute Set Instance
	  */
	public int getM_AttributeSetInstance_ID();

	public I_M_AttributeSetInstance getM_AttributeSetInstance() throws RuntimeException;

    /** Column name M_Periodic_Cost_ID */
    public static final String COLUMNNAME_M_Periodic_Cost_ID = "M_Periodic_Cost_ID";

	/** Set M_Periodic_Cost	  */
	public void setM_Periodic_Cost_ID (int M_Periodic_Cost_ID);

	/** Get M_Periodic_Cost	  */
	public int getM_Periodic_Cost_ID();

    /** Column name M_Periodic_Cost_UU */
    public static final String COLUMNNAME_M_Periodic_Cost_UU = "M_Periodic_Cost_UU";

	/** Set M_Periodic_Cost_UU	  */
	public void setM_Periodic_Cost_UU (String M_Periodic_Cost_UU);

	/** Get M_Periodic_Cost_UU	  */
	public String getM_Periodic_Cost_UU();

    /** Column name M_Product_Category_ID */
    public static final String COLUMNNAME_M_Product_Category_ID = "M_Product_Category_ID";

	/** Set Product Category.
	  * Category of a Product
	  */
	public void setM_Product_Category_ID (int M_Product_Category_ID);

	/** Get Product Category.
	  * Category of a Product
	  */
	public int getM_Product_Category_ID();

	public org.compiere.model.I_M_Product_Category getM_Product_Category() throws RuntimeException;

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

    /** Column name ReceiptAmount */
    public static final String COLUMNNAME_ReceiptAmount = "ReceiptAmount";

	/** Set ReceiptAmount	  */
	public void setReceiptAmount (BigDecimal ReceiptAmount);

	/** Get ReceiptAmount	  */
	public BigDecimal getReceiptAmount();

    /** Column name ReceiptQty */
    public static final String COLUMNNAME_ReceiptQty = "ReceiptQty";

	/** Set ReceiptQty	  */
	public void setReceiptQty (BigDecimal ReceiptQty);

	/** Get ReceiptQty	  */
	public BigDecimal getReceiptQty();

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
