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
import org.compiere.util.KeyNamePair;

/** Generated Interface for M_Periodic_Cost
 *  @author iDempiere (generated) 
 *  @version Release 5.1
 */
public interface I_M_Periodic_Cost 
{

    /** TableName=M_Periodic_Cost */
    public static final String Table_Name = "M_Periodic_Cost";

    /** AD_Table_ID=300726 */
    public static final int Table_ID = 300726;

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

    /** Column name beginningamount */
    public static final String COLUMNNAME_beginningamount = "beginningamount";

	/** Set beginningamount	  */
	public void setbeginningamount (BigDecimal beginningamount);

	/** Get beginningamount	  */
	public BigDecimal getbeginningamount();

    /** Column name beginningqty */
    public static final String COLUMNNAME_beginningqty = "beginningqty";

	/** Set beginningqty	  */
	public void setbeginningqty (BigDecimal beginningqty);

	/** Get beginningqty	  */
	public BigDecimal getbeginningqty();

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

    /** Column name costprice */
    public static final String COLUMNNAME_costprice = "costprice";

	/** Set costprice	  */
	public void setcostprice (BigDecimal costprice);

	/** Get costprice	  */
	public BigDecimal getcostprice();

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

    /** Column name endingamount */
    public static final String COLUMNNAME_endingamount = "endingamount";

	/** Set endingamount	  */
	public void setendingamount (BigDecimal endingamount);

	/** Get endingamount	  */
	public BigDecimal getendingamount();

    /** Column name endingqty */
    public static final String COLUMNNAME_endingqty = "endingqty";

	/** Set endingqty	  */
	public void setendingqty (BigDecimal endingqty);

	/** Get endingqty	  */
	public BigDecimal getendingqty();

    /** Column name ipv_amount */
    public static final String COLUMNNAME_ipv_amount = "ipv_amount";

	/** Set ipv_amount	  */
	public void setipv_amount (BigDecimal ipv_amount);

	/** Get ipv_amount	  */
	public BigDecimal getipv_amount();

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

    /** Column name issueamount */
    public static final String COLUMNNAME_issueamount = "issueamount";

	/** Set issueamount	  */
	public void setissueamount (BigDecimal issueamount);

	/** Get issueamount	  */
	public BigDecimal getissueamount();

    /** Column name issueqty */
    public static final String COLUMNNAME_issueqty = "issueqty";

	/** Set issueqty	  */
	public void setissueqty (BigDecimal issueqty);

	/** Get issueqty	  */
	public BigDecimal getissueqty();

    /** Column name landedcostamount */
    public static final String COLUMNNAME_landedcostamount = "landedcostamount";

	/** Set landedcostamount	  */
	public void setlandedcostamount (BigDecimal landedcostamount);

	/** Get landedcostamount	  */
	public BigDecimal getlandedcostamount();

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

    /** Column name m_periodic_cost_id */
    public static final String COLUMNNAME_m_periodic_cost_id = "m_periodic_cost_id";

	/** Set m_periodic_cost_id	  */
	public void setm_periodic_cost_id (int m_periodic_cost_id);

	/** Get m_periodic_cost_id	  */
	public int getm_periodic_cost_id();

    /** Column name m_periodic_cost_uu */
    public static final String COLUMNNAME_m_periodic_cost_uu = "m_periodic_cost_uu";

	/** Set m_periodic_cost_uu	  */
	public void setm_periodic_cost_uu (String m_periodic_cost_uu);

	/** Get m_periodic_cost_uu	  */
	public String getm_periodic_cost_uu();

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

    /** Column name manufacturedamt */
    public static final String COLUMNNAME_manufacturedamt = "manufacturedamt";

	/** Set manufacturedamt	  */
	public void setmanufacturedamt (BigDecimal manufacturedamt);

	/** Get manufacturedamt	  */
	public BigDecimal getmanufacturedamt();

    /** Column name manufacturedqty */
    public static final String COLUMNNAME_manufacturedqty = "manufacturedqty";

	/** Set manufacturedqty	  */
	public void setmanufacturedqty (BigDecimal manufacturedqty);

	/** Get manufacturedqty	  */
	public BigDecimal getmanufacturedqty();

    /** Column name receiptamount */
    public static final String COLUMNNAME_receiptamount = "receiptamount";

	/** Set receiptamount	  */
	public void setreceiptamount (BigDecimal receiptamount);

	/** Get receiptamount	  */
	public BigDecimal getreceiptamount();

    /** Column name receiptqty */
    public static final String COLUMNNAME_receiptqty = "receiptqty";

	/** Set receiptqty	  */
	public void setreceiptqty (BigDecimal receiptqty);

	/** Get receiptqty	  */
	public BigDecimal getreceiptqty();

    /** Column name shippedamt */
    public static final String COLUMNNAME_shippedamt = "shippedamt";

	/** Set shippedamt	  */
	public void setshippedamt (BigDecimal shippedamt);

	/** Get shippedamt	  */
	public BigDecimal getshippedamt();

    /** Column name shippedqty */
    public static final String COLUMNNAME_shippedqty = "shippedqty";

	/** Set shippedqty	  */
	public void setshippedqty (BigDecimal shippedqty);

	/** Get shippedqty	  */
	public BigDecimal getshippedqty();

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
