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

import id.tcs.model.I_C_Quotation;
import id.tcs.model.I_C_QuotationLine;
import id.tcs.model.I_TCS_R_RequestLine;
import org.compiere.model.MTable;
import org.compiere.util.KeyNamePair;

/** Generated Interface for M_MatchRequest
 *  @author iDempiere (generated) 
 *  @version Release 2.1
 */
public interface I_M_MatchRequest 
{

    /** TableName=M_MatchRequest */
    public static final String Table_Name = "M_MatchRequest";

    /** AD_Table_ID=1000128 */
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

    /** Column name C_Inquiry_ID */
    public static final String COLUMNNAME_C_Inquiry_ID = "C_Inquiry_ID";

	/** Set C_Inquiry_ID	  */
	public void setC_Inquiry_ID (int C_Inquiry_ID);

	/** Get C_Inquiry_ID	  */
	public int getC_Inquiry_ID();

	public I_C_Inquiry getC_Inquiry() throws RuntimeException;

    /** Column name C_InquiryLine_ID */
    public static final String COLUMNNAME_C_InquiryLine_ID = "C_InquiryLine_ID";

	/** Set Inquiry Line	  */
	public void setC_InquiryLine_ID (int C_InquiryLine_ID);

	/** Get Inquiry Line	  */
	public int getC_InquiryLine_ID();

	public I_C_InquiryLine getC_InquiryLine() throws RuntimeException;

    /** Column name ConvertStatus */
    public static final String COLUMNNAME_ConvertStatus = "ConvertStatus";

	/** Set ConvertStatus.
	  * Status of Inquiry Conversion (to RfP or to RfI)
	  */
	public void setConvertStatus (String ConvertStatus);

	/** Get ConvertStatus.
	  * Status of Inquiry Conversion (to RfP or to RfI)
	  */
	public String getConvertStatus();

    /** Column name C_Order_ID */
    public static final String COLUMNNAME_C_Order_ID = "C_Order_ID";

	/** Set Order.
	  * Order
	  */
	public void setC_Order_ID (int C_Order_ID);

	/** Get Order.
	  * Order
	  */
	public int getC_Order_ID();

	public org.compiere.model.I_C_Order getC_Order() throws RuntimeException;

    /** Column name C_OrderLine_ID */
    public static final String COLUMNNAME_C_OrderLine_ID = "C_OrderLine_ID";

	/** Set Sales Order Line.
	  * Sales Order Line
	  */
	public void setC_OrderLine_ID (int C_OrderLine_ID);

	/** Get Sales Order Line.
	  * Sales Order Line
	  */
	public int getC_OrderLine_ID();

	public org.compiere.model.I_C_OrderLine getC_OrderLine() throws RuntimeException;

    /** Column name C_Payment_ID */
    public static final String COLUMNNAME_C_Payment_ID = "C_Payment_ID";

	/** Set Payment.
	  * Payment identifier
	  */
	public void setC_Payment_ID (int C_Payment_ID);

	/** Get Payment.
	  * Payment identifier
	  */
	public int getC_Payment_ID();

	public org.compiere.model.I_C_Payment getC_Payment() throws RuntimeException;

    /** Column name C_Quotation_ID */
    public static final String COLUMNNAME_C_Quotation_ID = "C_Quotation_ID";

	/** Set Quotation	  */
	public void setC_Quotation_ID (int C_Quotation_ID);

	/** Get Quotation	  */
	public int getC_Quotation_ID();

	public I_C_Quotation getC_Quotation() throws RuntimeException;

    /** Column name C_QuotationLine_ID */
    public static final String COLUMNNAME_C_QuotationLine_ID = "C_QuotationLine_ID";

	/** Set Quotation Line	  */
	public void setC_QuotationLine_ID (int C_QuotationLine_ID);

	/** Get Quotation Line	  */
	public int getC_QuotationLine_ID();

	public I_C_QuotationLine getC_QuotationLine() throws RuntimeException;

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

    /** Column name C_RfQ_ID */
    public static final String COLUMNNAME_C_RfQ_ID = "C_RfQ_ID";

	/** Set RfQ.
	  * Request for Quotation
	  */
	public void setC_RfQ_ID (int C_RfQ_ID);

	/** Get RfQ.
	  * Request for Quotation
	  */
	public int getC_RfQ_ID();

	public org.compiere.model.I_C_RfQ getC_RfQ() throws RuntimeException;

    /** Column name C_RfQLine_ID */
    public static final String COLUMNNAME_C_RfQLine_ID = "C_RfQLine_ID";

	/** Set RfQ Line.
	  * Request for Quotation Line
	  */
	public void setC_RfQLine_ID (int C_RfQLine_ID);

	/** Get RfQ Line.
	  * Request for Quotation Line
	  */
	public int getC_RfQLine_ID();

	public org.compiere.model.I_C_RfQLine getC_RfQLine() throws RuntimeException;

    /** Column name DateTrx */
    public static final String COLUMNNAME_DateTrx = "DateTrx";

	/** Set Transaction Date.
	  * Transaction Date
	  */
	public void setDateTrx (Timestamp DateTrx);

	/** Get Transaction Date.
	  * Transaction Date
	  */
	public Timestamp getDateTrx();

    /** Column name DD_Order_ID */
    public static final String COLUMNNAME_DD_Order_ID = "DD_Order_ID";

	/** Set Distribution Order	  */
	public void setDD_Order_ID (int DD_Order_ID);

	/** Get Distribution Order	  */
	public int getDD_Order_ID();

	public org.eevolution.model.I_DD_Order getDD_Order() throws RuntimeException;

    /** Column name DD_OrderLine_ID */
    public static final String COLUMNNAME_DD_OrderLine_ID = "DD_OrderLine_ID";

	/** Set Distribution Order Line	  */
	public void setDD_OrderLine_ID (int DD_OrderLine_ID);

	/** Get Distribution Order Line	  */
	public int getDD_OrderLine_ID();

	public org.eevolution.model.I_DD_OrderLine getDD_OrderLine() throws RuntimeException;

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

    /** Column name M_MatchRequest_ID */
    public static final String COLUMNNAME_M_MatchRequest_ID = "M_MatchRequest_ID";

	/** Set Match Request	  */
	public void setM_MatchRequest_ID (int M_MatchRequest_ID);

	/** Get Match Request	  */
	public int getM_MatchRequest_ID();

    /** Column name M_MatchRequest_UU */
    public static final String COLUMNNAME_M_MatchRequest_UU = "M_MatchRequest_UU";

	/** Set M_MatchRequest_UU	  */
	public void setM_MatchRequest_UU (String M_MatchRequest_UU);

	/** Get M_MatchRequest_UU	  */
	public String getM_MatchRequest_UU();

    /** Column name M_Movement_ID */
    public static final String COLUMNNAME_M_Movement_ID = "M_Movement_ID";

	/** Set Inventory Move.
	  * Movement of Inventory
	  */
	public void setM_Movement_ID (int M_Movement_ID);

	/** Get Inventory Move.
	  * Movement of Inventory
	  */
	public int getM_Movement_ID();

	public org.compiere.model.I_M_Movement getM_Movement() throws RuntimeException;

    /** Column name M_MovementLine_ID */
    public static final String COLUMNNAME_M_MovementLine_ID = "M_MovementLine_ID";

	/** Set Move Line.
	  * Inventory Move document Line
	  */
	public void setM_MovementLine_ID (int M_MovementLine_ID);

	/** Get Move Line.
	  * Inventory Move document Line
	  */
	public int getM_MovementLine_ID();

	public org.compiere.model.I_M_MovementLine getM_MovementLine() throws RuntimeException;

    /** Column name M_Requisition_ID */
    public static final String COLUMNNAME_M_Requisition_ID = "M_Requisition_ID";

	/** Set Requisition.
	  * Material Requisition
	  */
	public void setM_Requisition_ID (int M_Requisition_ID);

	/** Get Requisition.
	  * Material Requisition
	  */
	public int getM_Requisition_ID();

	public org.compiere.model.I_M_Requisition getM_Requisition() throws RuntimeException;

    /** Column name M_RequisitionLine_ID */
    public static final String COLUMNNAME_M_RequisitionLine_ID = "M_RequisitionLine_ID";

	/** Set Requisition Line.
	  * Material Requisition Line
	  */
	public void setM_RequisitionLine_ID (int M_RequisitionLine_ID);

	/** Get Requisition Line.
	  * Material Requisition Line
	  */
	public int getM_RequisitionLine_ID();

	public org.compiere.model.I_M_RequisitionLine getM_RequisitionLine() throws RuntimeException;

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

    /** Column name QtyOrdered */
    public static final String COLUMNNAME_QtyOrdered = "QtyOrdered";

	/** Set Ordered Quantity.
	  * Ordered Quantity
	  */
	public void setQtyOrdered (BigDecimal QtyOrdered);

	/** Get Ordered Quantity.
	  * Ordered Quantity
	  */
	public BigDecimal getQtyOrdered();

    /** Column name RequestAmt */
    public static final String COLUMNNAME_RequestAmt = "RequestAmt";

	/** Set Request Amount.
	  * Amount associated with this request
	  */
	public void setRequestAmt (BigDecimal RequestAmt);

	/** Get Request Amount.
	  * Amount associated with this request
	  */
	public BigDecimal getRequestAmt();

    /** Column name R_Request_ID */
    public static final String COLUMNNAME_R_Request_ID = "R_Request_ID";

	/** Set Request.
	  * Request from a Business Partner or Prospect
	  */
	public void setR_Request_ID (int R_Request_ID);

	/** Get Request.
	  * Request from a Business Partner or Prospect
	  */
	public int getR_Request_ID();

	public org.compiere.model.I_R_Request getR_Request() throws RuntimeException;

    /** Column name R_RequestLine_ID */
    public static final String COLUMNNAME_R_RequestLine_ID = "R_RequestLine_ID";

	/** Set Request Line	  */
	public void setR_RequestLine_ID (int R_RequestLine_ID);

	/** Get Request Line	  */
	public int getR_RequestLine_ID();

	public I_TCS_R_RequestLine getR_RequestLine() throws RuntimeException;

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
