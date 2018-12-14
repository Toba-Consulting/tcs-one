package id.tcs.process;

import java.math.BigDecimal;
import java.util.ArrayList;

import org.compiere.model.MAllocationHdr;
import org.compiere.model.MAllocationLine;
import org.compiere.model.MInvoice;
import org.compiere.model.MPayment;
import org.compiere.model.Query;
import org.compiere.process.DocAction;
import org.compiere.process.SvrProcess;
import org.compiere.util.Env;

import id.tcs.model.X_T_MatchAllocation;

public class TCS_CreateMatchAllocation extends SvrProcess {

	@Override
	protected void prepare() {		
	}

	@Override
	protected String doIt() throws Exception {
		
		int[] AllocationIDs = new Query(getCtx(), MAllocationHdr.Table_Name, "DocStatus=?", get_TrxName())
		.setOnlyActiveRecords(true)
		.setParameters(new Object[]{DocAction.STATUS_Completed})
		.getIDs();
		
		//Split Up ID and Amount Based On Amount, as ArrayList
		
		for(int AllocationID : AllocationIDs)
		{
			MAllocationHdr hdr = new MAllocationHdr(getCtx(), AllocationID, get_TrxName());
			
			//Invoice
			ArrayList<Integer> plusInvoiceID = new ArrayList<Integer>();
			ArrayList<Integer> minInvoiceID = new ArrayList<Integer>();
			
			ArrayList<BigDecimal> plusAmount = new ArrayList<BigDecimal>();
			ArrayList<BigDecimal> minAmount = new ArrayList<BigDecimal>();
			
			ArrayList<BigDecimal> pDiscountAmt = new ArrayList<BigDecimal>();
			ArrayList<BigDecimal> nDiscountAmt = new ArrayList<BigDecimal>();
			
			ArrayList<BigDecimal> pWriteOffAmt = new ArrayList<BigDecimal>();
			ArrayList<BigDecimal> nWriteOffAmt = new ArrayList<BigDecimal>();
			//
			
			//Payment
			ArrayList<Integer> paymentID = new ArrayList<Integer>();
			ArrayList<Integer> receiptID = new ArrayList<Integer>();
			
			ArrayList<BigDecimal> paymentAmount = new ArrayList<BigDecimal>();
			ArrayList<BigDecimal> receiptAmount = new ArrayList<BigDecimal>();
			//
			
			//Charge
//			int chargeID = 0;
			ArrayList<Integer> chargeID = new ArrayList<Integer>();
			
			BigDecimal chargeAmount = Env.ZERO;
			//
			
			//ChargeDescription
			ArrayList<String> chargeDescription = new ArrayList<String>();
			//
			
			int plusI = 0, minI = 0, pay = 0, rec = 0, charge = 0, count = 0, matched = 0;
					
			//Line loop to get record
			//1. If Line only have Invoice ID without Payment ID and Amount is +
			//2. If Line only have Invoice ID without Payment ID and Amount is -
			//3. If Line only have Payment ID without Invoice ID and Payment is AP Payment
			//4. If Line only have Payment ID without Invoice ID and Payment is AR Receipt
			//5. If Line is Charge
			//6. If Line have both Invoice ID and Payment ID and Payment is AP Payment
			//7. If Line have both Invoice ID and Payment ID and Payment is AR Receipt
			for(MAllocationLine line : hdr.getLines(true)){
				
				if(line.getC_Payment_ID()==0 && line.getC_Invoice_ID()>0 && line.getAmount().compareTo(Env.ZERO)>0){					//1
					plusInvoiceID.add(line.getC_Invoice_ID());
					plusAmount.add(line.getAmount());
					pDiscountAmt.add(line.getDiscountAmt());
					pWriteOffAmt.add(line.getWriteOffAmt());
					plusI++;
				}
				else if(line.getC_Payment_ID()==0 && line.getC_Invoice_ID()>0 && line.getAmount().compareTo(Env.ZERO)<0){				//2
					minInvoiceID.add(line.getC_Invoice_ID());
					minAmount.add(line.getAmount());
					nDiscountAmt.add(line.getDiscountAmt());
					nWriteOffAmt.add(line.getWriteOffAmt());
					minI++;
				}			
				else if(line.getC_Invoice_ID()==0 && line.getC_Payment_ID()>0 && line.getC_Payment().getC_DocType().getDocBaseType().equals("APP")){				//3
					paymentID.add(line.getC_Payment_ID());
					//negate because in allocationline, the payment amount is negated, so its negated here as well
					paymentAmount.add(line.getAmount().negate());
					pay++;
				}
				else if(line.getC_Invoice_ID()==0 && line.getC_Payment_ID()>0 && line.getC_Payment().getC_DocType().getDocBaseType().equals("ARR")){				//4
					receiptID.add(line.getC_Payment_ID());
					receiptAmount.add(line.getAmount());
					rec++;
				}
				else if(line.getC_Charge_ID()>0){																						//5
					//chargeID = line.getC_Charge_ID();
					chargeID.add(line.getC_Charge_ID());
					chargeDescription.add(line.get_ValueAsString("Description"));
					//chargeAmount = line.getAmount();
					chargeAmount = chargeAmount.add(line.getAmount());
					charge++;
				}else if(line.getAmount().compareTo(Env.ZERO)!=0){
					X_T_MatchAllocation match = new X_T_MatchAllocation(getCtx(), 0, get_TrxName());
					match.set_CustomColumn("C_AllocationHdr_ID", hdr.get_ID());
					
					//invoice
					match.set_CustomColumn("C_Invoice_ID", line.getC_Invoice_ID());
					match.set_CustomColumn("DiscountAmt", line.getDiscountAmt());
					match.set_CustomColumn("WriteOffAmt", line.getWriteOffAmt());
					
					MInvoice invoice = new MInvoice(getCtx(), line.getC_Invoice_ID(), get_TrxName());
					match.set_CustomColumn("C_DocType_ID", invoice.getC_DocType_ID());
					
					//payment
					match.set_CustomColumn("C_Payment_ID", line.getC_Payment_ID());
					
					MPayment payment = new MPayment(getCtx(), line.getC_Payment_ID(), get_TrxName());
					match.set_CustomColumn("Match_DocType_ID", payment.getC_DocType_ID());

					match.set_ValueOfColumn("DateAllocated", hdr.getCreated());
					match.set_CustomColumn("AllocationAmt", line.getAmount().abs());
					
					match.set_CustomColumn("Description", line.get_Value("Description"));
					match.saveEx();
					matched++;
					
					/*
					if(line.getC_Payment().getC_DocType().getDocBaseType().equals("APP")){												//6
						
						//match.set_CustomColumn("P_Payment_ID", line.getC_Payment_ID());
						//match.set_CustomColumn("P_Amount", line.getAmount().abs());
						//match.set_CustomColumn("N_Invoice_ID", line.getC_Invoice_ID());
						//match.set_CustomColumn("N_Amount", line.getAmount().abs().negate());
						match.set_CustomColumn("C_DiscountAmt", line.getDiscountAmt());
						match.set_CustomColumn("C_WriteOffAmt", line.getWriteOffAmt());
						
						//MPayment pPayment = new MPayment(getCtx(), line.getC_Payment_ID(), get_TrxName());
						//match.set_CustomColumn("P_DocType_ID", pPayment.getC_DocType_ID());
						
						//MInvoice nInvoice = new MInvoice(getCtx(), line.getC_Invoice_ID(), get_TrxName());
						//match.set_CustomColumn("N_DocType_ID", nInvoice.getC_DocType_ID());
						
					}else if(line.getC_Payment().getC_DocType().getDocBaseType().equals("ARR")){										//7
						
						//match.set_CustomColumn("N_Payment_ID", line.getC_Payment_ID());
						//match.set_CustomColumn("N_Amount", line.getAmount().abs().negate());
						//match.set_CustomColumn("P_Invoice_ID", line.getC_Invoice_ID());
						//match.set_CustomColumn("P_Amount", line.getAmount().abs());
						match.set_CustomColumn("P_DiscountAmt", line.getDiscountAmt());
						match.set_CustomColumn("P_WriteOffAmt", line.getWriteOffAmt());
						
						//MPayment nPayment = new MPayment(getCtx(), line.getC_Payment_ID(), get_TrxName());
						//match.set_CustomColumn("N_DocType_ID", nPayment.getC_DocType_ID());
											
						//MInvoice pInvoice = new MInvoice(getCtx(), line.getC_Invoice_ID(), get_TrxName());
						//match.set_CustomColumn("P_DocType_ID", pInvoice.getC_DocType_ID());
						
					}
					*/
				}
				count++;
			}
			
			int total = plusI+minI+pay+rec+charge;
			count -= matched;
			
			BigDecimal tempPaymentAmt = Env.ZERO;
			BigDecimal tempInvoiceAmt = Env.ZERO;
			
			//Loop for pInvoice
			for(int i = 0;i<plusI;i++){
				tempInvoiceAmt = plusAmount.get(i);
				
				if(!plusInvoiceID.isEmpty() && !minInvoiceID.isEmpty()){
									
					for(int j = 0;j<minInvoiceID.size();j++){
						if(j==0 && minAmount.get(j).compareTo(Env.ZERO)<0){
							//match
							X_T_MatchAllocation match = new X_T_MatchAllocation(getCtx(), 0, get_TrxName());
							match.set_CustomColumn("C_AllocationHdr_ID", hdr.get_ID());
							
							//invoice
							match.set_CustomColumn("C_Invoice_ID", plusInvoiceID.get(i));
							match.set_CustomColumn("DiscountAmt", pDiscountAmt.get(i));
							match.set_CustomColumn("WriteOffAmt", pWriteOffAmt.get(i));
							
							MInvoice pInvoice = new MInvoice(getCtx(), plusInvoiceID.get(i), get_TrxName());
							match.set_CustomColumn("C_DocType_ID", pInvoice.getC_DocType_ID());
							
							//match.set_CustomColumn("P_Invoice_ID", plusInvoiceID.get(i));
							//match.set_CustomColumn("P_Amount", plusAmount.get(i));
							//match.set_CustomColumn("N_Invoice_ID", minInvoiceID.get(j));
							//match.set_CustomColumn("N_Amount", minAmount.get(j));
							
							//invoice1
							match.set_CustomColumn("Match_Invoice_ID", minInvoiceID.get(j));
							match.set_CustomColumn("Match_DiscountAmt", nDiscountAmt.get(j));
							match.set_CustomColumn("Match_WriteOffAmt", nWriteOffAmt.get(j));
							
							MInvoice nInvoice = new MInvoice(getCtx(), minInvoiceID.get(j), get_TrxName());
							match.set_CustomColumn("Match_DocType_ID", nInvoice.getC_DocType_ID());
							
							if(tempInvoiceAmt.compareTo(minAmount.get(j).abs())>0)
								match.set_CustomColumn("AllocationAmt", minAmount.get(j).abs());	
							else
								match.set_CustomColumn("AllocationAmt", tempInvoiceAmt);
							
							match.set_ValueOfColumn("DateAllocated", hdr.getCreated());
							match.saveEx();
							
							//match1
							X_T_MatchAllocation match1 = new X_T_MatchAllocation(getCtx(), 0, get_TrxName());
							match1.set_CustomColumn("C_AllocationHdr_ID", hdr.get_ID());
							
							//invoice
							match1.set_CustomColumn("C_Invoice_ID", minInvoiceID.get(j));
							match1.set_CustomColumn("C_DocType_ID", nInvoice.getC_DocType_ID());
							match1.set_CustomColumn("DiscountAmt", nDiscountAmt.get(j));
							match1.set_CustomColumn("WriteOffAmt", nWriteOffAmt.get(j));
							
							//invoice1
							match1.set_CustomColumn("Match_Invoice_ID", plusInvoiceID.get(i));
							match1.set_CustomColumn("Match_DocType_ID", pInvoice.getC_DocType_ID());
							match1.set_CustomColumn("Match_DiscountAmt", pDiscountAmt.get(i));
							match1.set_CustomColumn("Match_WriteOffAmt", pWriteOffAmt.get(i));
							
							//match1.set_CustomColumn("P_Invoice_ID", plusInvoiceID.get(i));
							//match1.set_CustomColumn("P_Amount", plusAmount.get(i));
							//match1.set_CustomColumn("N_Invoice_ID", minInvoiceID.get(j));
							//match1.set_CustomColumn("N_Amount", minAmount.get(j));
							
							if(tempInvoiceAmt.compareTo(minAmount.get(j).abs())>0)
								match1.set_CustomColumn("AllocationAmt", minAmount.get(j).abs());	
							else
								match1.set_CustomColumn("AllocationAmt", tempInvoiceAmt);
							
							
							
							match1.set_ValueOfColumn("DateAllocated", hdr.getCreated());
							match1.saveEx();
							//
							pDiscountAmt.set(i, Env.ZERO);
							pWriteOffAmt.set(i, Env.ZERO);
							nDiscountAmt.set(j, Env.ZERO);
							nWriteOffAmt.set(j, Env.ZERO);
							
						}
						
						
						if(j!=0 && tempInvoiceAmt.compareTo(Env.ZERO)>0 
								&& minAmount.get(j).compareTo(Env.ZERO)<0){
							//match
							X_T_MatchAllocation match = new X_T_MatchAllocation(getCtx(), 0, get_TrxName());
							match.set_CustomColumn("C_AllocationHdr_ID", hdr.get_ID());
							
							//invoice
							match.set_CustomColumn("C_Invoice_ID", plusInvoiceID.get(i));
							match.set_CustomColumn("DiscountAmt", pDiscountAmt.get(i));
							match.set_CustomColumn("WriteOffAmt", pWriteOffAmt.get(i));
							
							MInvoice pInvoice = new MInvoice(getCtx(), plusInvoiceID.get(i), get_TrxName());
							match.set_CustomColumn("C_DocType_ID", pInvoice.getC_DocType_ID());
							
							//invoice1
							match.set_CustomColumn("Match_Invoice_ID", minInvoiceID.get(j));
							match.set_CustomColumn("Match_DiscountAmt", nDiscountAmt.get(j));
							match.set_CustomColumn("Match_WriteOffAmt", nWriteOffAmt.get(j));
							
							MInvoice nInvoice = new MInvoice(getCtx(), minInvoiceID.get(j), get_TrxName());
							match.set_CustomColumn("Match_DocType_ID", nInvoice.getC_DocType_ID());
							
							//match.set_CustomColumn("P_Invoice_ID", plusInvoiceID.get(i));
							//match.set_CustomColumn("P_Amount", plusAmount.get(i));
							//match.set_CustomColumn("N_Invoice_ID", minInvoiceID.get(j));
							//match.set_CustomColumn("N_Amount", minAmount.get(j));
							
							if(tempInvoiceAmt.compareTo(minAmount.get(j).abs())>0)
								match.set_CustomColumn("AllocationAmt", minAmount.get(j).abs());	
							else
								match.set_CustomColumn("AllocationAmt", tempInvoiceAmt);
							
							match.set_ValueOfColumn("DateAllocated", hdr.getCreated());
							match.saveEx();
							
							//match1				
							X_T_MatchAllocation match1 = new X_T_MatchAllocation(getCtx(), 0, get_TrxName());
							match1.set_CustomColumn("C_AllocationHdr_ID", hdr.get_ID());
							
							
							//invoice
							match1.set_CustomColumn("C_Invoice_ID", minInvoiceID.get(j));
							match1.set_CustomColumn("DiscountAmt", nDiscountAmt.get(j));
							match1.set_CustomColumn("WriteOffAmt", nWriteOffAmt.get(j));
							match1.set_CustomColumn("C_DocType_ID", nInvoice.getC_DocType_ID());
							
							//invoice1
							match1.set_CustomColumn("Match_Invoice_ID", plusInvoiceID.get(i));
							match1.set_CustomColumn("Match_DocType_ID", pInvoice.getC_DocType_ID());
							match1.set_CustomColumn("Match_DiscountAmt", pDiscountAmt.get(i));
							match1.set_CustomColumn("Match_WriteOffAmt", pWriteOffAmt.get(i));
							
							//match1.set_CustomColumn("P_Invoice_ID", plusInvoiceID.get(i));
							//match1.set_CustomColumn("P_Amount", plusAmount.get(i));
							//match1.set_CustomColumn("N_Invoice_ID", minInvoiceID.get(j));
							//match1.set_CustomColumn("N_Amount", minAmount.get(j));
							
							//MInvoice pInvoice = new MInvoice(getCtx(), plusInvoiceID.get(i), get_TrxName());
							//match1.set_CustomColumn("N_DocType_ID", pInvoice.getC_DocType_ID());
							
							//MInvoice nInvoice = new MInvoice(getCtx(), minInvoiceID.get(j), get_TrxName());
							//match1.set_CustomColumn("P_DocType_ID", nInvoice.getC_DocType_ID());
							
							if(tempInvoiceAmt.compareTo(minAmount.get(j).abs())>0)
								match1.set_CustomColumn("AllocationAmt", minAmount.get(j).abs());	
							else
								match1.set_CustomColumn("AllocationAmt", tempInvoiceAmt);
							
							match1.set_ValueOfColumn("DateAllocated", hdr.getCreated());
							match1.saveEx();
							//
							pDiscountAmt.set(i, Env.ZERO);
							pWriteOffAmt.set(i, Env.ZERO);
							nDiscountAmt.set(j, Env.ZERO);
							nWriteOffAmt.set(j, Env.ZERO);
														
						}
						
						tempInvoiceAmt = tempInvoiceAmt.add(minAmount.get(j));
						
						if(tempInvoiceAmt.compareTo(Env.ZERO)>0){
							minAmount.set(j, Env.ZERO);
						}else if(tempInvoiceAmt.compareTo(Env.ZERO)<0){
							plusAmount.set(i, Env.ZERO);
							minAmount.set(j, tempInvoiceAmt);
							break;
						}else if(tempInvoiceAmt.compareTo(Env.ZERO)==0){
							plusAmount.set(i, Env.ZERO);
							minAmount.set(j, Env.ZERO);
							break;
						}
					}
					
				}
				
				BigDecimal totalMinAmount = Env.ZERO;
				for(int k=0; k<minInvoiceID.size(); k++)
				{
					totalMinAmount = totalMinAmount.add(minAmount.get(k));
				}
				if(!chargeID.isEmpty()){
					if(totalMinAmount.compareTo(Env.ZERO)==0 && chargeID.get(0)>0 && tempInvoiceAmt.compareTo(Env.ZERO)>0 && chargeAmount.compareTo(Env.ZERO)>0){					
						X_T_MatchAllocation match = new X_T_MatchAllocation(getCtx(), 0, get_TrxName());
						match.set_CustomColumn("C_AllocationHdr_ID", hdr.get_ID());
						
						match.set_CustomColumn("C_Invoice_ID", plusInvoiceID.get(i));
						match.set_CustomColumn("DiscountAmt", pDiscountAmt.get(i));
						match.set_CustomColumn("WriteOffAmt", pWriteOffAmt.get(i));
						
						MInvoice pInvoice = new MInvoice(getCtx(), plusInvoiceID.get(i), get_TrxName());
						match.set_CustomColumn("C_DocType_ID", pInvoice.getC_DocType_ID());
						
						//match.set_CustomColumn("P_Invoice_ID", plusInvoiceID.get(i));
						//match.set_CustomColumn("P_Amount", tempInvoiceAmt);
						match.set_CustomColumn("C_Charge_ID", chargeID.get(0));
						//match.set_CustomColumn("N_Amount", tempInvoiceAmt.abs().negate());
						
						match.set_CustomColumn("AllocationAmt", tempInvoiceAmt);
						match.set_ValueOfColumn("DateAllocated", hdr.getCreated());
						match.saveEx();
						
						chargeAmount = chargeAmount.add(tempInvoiceAmt);
						
						pDiscountAmt.set(i, Env.ZERO);
						pWriteOffAmt.set(i, Env.ZERO);
						
					} else if(!plusInvoiceID.isEmpty() && minInvoiceID.isEmpty() && chargeID.get(i)>0){
						X_T_MatchAllocation match = new X_T_MatchAllocation(getCtx(), 0, get_TrxName());
						match.set_CustomColumn("C_AllocationHdr_ID", hdr.get_ID());
						match.set_CustomColumn("C_Invoice_ID", plusInvoiceID.get(i));
						match.set_CustomColumn("DiscountAmt", pDiscountAmt.get(i));
						match.set_CustomColumn("WriteOffAmt", pWriteOffAmt.get(i));
						
						MInvoice pInvoice = new MInvoice(getCtx(), plusInvoiceID.get(i), get_TrxName());
						match.set_CustomColumn("C_DocType_ID", pInvoice.getC_DocType_ID());
						//match.set_CustomColumn("P_Invoice_ID", plusInvoiceID.get(i));
						//match.set_CustomColumn("P_Amount", tempInvoiceAmt);
						match.set_CustomColumn("C_Charge_ID", chargeID.get(0));
						//match.set_CustomColumn("N_Amount", tempInvoiceAmt.abs().negate());
						
						//MInvoice pInvoice = new MInvoice(getCtx(), plusInvoiceID.get(i), get_TrxName());
						//match.set_CustomColumn("P_DocType_ID", pInvoice.getC_DocType_ID());
						
						match.set_CustomColumn("AllocationAmt", chargeAmount);
						match.set_ValueOfColumn("DateAllocated", hdr.getCreated());
						match.saveEx();
						
						chargeAmount = chargeAmount.add(tempInvoiceAmt);
						
						pDiscountAmt.set(i, Env.ZERO);
						pWriteOffAmt.set(i, Env.ZERO);
						
					}
				}/*else if(plusInvoiceID.isEmpty() && !minInvoiceID.isEmpty() && chargeID>0){
					X_T_MatchAllocation match = new X_T_MatchAllocation(getCtx(), 0, get_TrxName());
					match.set_CustomColumn("C_AllocationHdr_ID", get_ID());
					match.set_CustomColumn("N_Invoice_ID", minInvoiceID.get(0));
					match.set_CustomColumn("N_Amount", minAmount.get(0));
					match.set_CustomColumn("C_Charge_ID", chargeID);
					match.set_CustomColumn("P_Amount", chargeAmount);
					
					MInvoice nInvoice = new MInvoice(getCtx(), minInvoiceID.get(0), get_TrxName());
					match.set_CustomColumn("N_DocType_ID", nInvoice.getC_DocType_ID());
					
					match.set_CustomColumn("AllocationAmt", chargeAmount.abs());
					
					match.set_CustomColumn("N_DiscountAmt", nDiscountAmt.get(0));
					match.set_CustomColumn("N_WriteOffAmt", nWriteOffAmt.get(0));
					
					nDiscountAmt.set(0, Env.ZERO);
					nWriteOffAmt.set(0, Env.ZERO);
					
					match.saveEx();
				}*/
			}
			//End pInvoice Loop
			
			//Loop for Payment
			for(int i = 0;i<pay;i++){
				tempPaymentAmt = paymentAmount.get(i);
				
				if(!paymentID.isEmpty() && !receiptID.isEmpty()){
					
					for(int j = 0;j<receiptID.size();j++){
						
						if(j==0 && receiptAmount.get(j).compareTo(Env.ZERO)<0){
							//match
							X_T_MatchAllocation match = new X_T_MatchAllocation(getCtx(), 0, get_TrxName());
							match.set_CustomColumn("C_AllocationHdr_ID", hdr.get_ID());
							
							//payment
							match.set_CustomColumn("C_Payment_ID", paymentID.get(i));
							
							MPayment pPayment = new MPayment(getCtx(), paymentID.get(i), get_TrxName());
							match.set_CustomColumn("C_DocType_ID", pPayment.getC_DocType_ID());
							
							//receipt
							match.set_CustomColumn("Match_Payment_ID", receiptID.get(j));
							
							MPayment nPayment = new MPayment(getCtx(), receiptID.get(j), get_TrxName());
							match.set_CustomColumn("Match_DocType_ID", nPayment.getC_DocType_ID());
							//match.set_CustomColumn("P_Payment_ID", paymentID.get(i));
							//match.set_CustomColumn("P_Amount", paymentAmount.get(i));
							//match.set_CustomColumn("N_Payment_ID", receiptID.get(j));
							//match.set_CustomColumn("N_Amount", receiptAmount.get(j));
							
							if(tempPaymentAmt.compareTo(receiptAmount.get(j).abs())>0)
								match.set_CustomColumn("AllocationAmt", receiptAmount.get(j).abs());	
							else
								match.set_CustomColumn("AllocationAmt", tempPaymentAmt);
														
							match.set_ValueOfColumn("DateAllocated", hdr.getCreated());							
							match.saveEx();
							
							//match1
							X_T_MatchAllocation match1 = new X_T_MatchAllocation(getCtx(), 0, get_TrxName());
							match1.set_CustomColumn("C_AllocationHdr_ID", hdr.get_ID());
							
							//receipt
							match1.set_CustomColumn("C_Payment_ID", receiptID.get(j));
							match1.set_CustomColumn("C_DocType_ID", nPayment.getC_DocType_ID());
							
							//payment
							match1.set_CustomColumn("Match_Payment_ID", paymentID.get(i));
							match1.set_CustomColumn("Match_DocType_ID", pPayment.getC_DocType_ID());
							
							//match.set_CustomColumn("P_Payment_ID", paymentID.get(i));
							//match.set_CustomColumn("P_Amount", paymentAmount.get(i));
							//match.set_CustomColumn("N_Payment_ID", receiptID.get(j));
							//match.set_CustomColumn("N_Amount", receiptAmount.get(j));
							
							if(tempPaymentAmt.compareTo(receiptAmount.get(j).abs())>0)
								match1.set_CustomColumn("AllocationAmt", receiptAmount.get(j).abs());	
							else
								match1.set_CustomColumn("AllocationAmt", tempPaymentAmt);
														
							match1.set_ValueOfColumn("DateAllocated", hdr.getCreated());							
							match1.saveEx();
						}
						
						
						if(j!=0 && tempPaymentAmt.compareTo(Env.ZERO)>0 
								&& receiptAmount.get(j).compareTo(Env.ZERO)<0){
							//match
							X_T_MatchAllocation match = new X_T_MatchAllocation(getCtx(), 0, get_TrxName());
							match.set_CustomColumn("C_AllocationHdr_ID", hdr.get_ID());
							
							//payment
							match.set_CustomColumn("C_Payment_ID", paymentID.get(i));
							
							MPayment pPayment = new MPayment(getCtx(), paymentID.get(i), get_TrxName());
							match.set_CustomColumn("C_DocType_ID", pPayment.getC_DocType_ID());
							
							//receipt
							match.set_CustomColumn("Match_Payment_ID", receiptID.get(j));
							
							MPayment nPayment = new MPayment(getCtx(), receiptID.get(j), get_TrxName());
							match.set_CustomColumn("Match_DocType_ID", nPayment.getC_DocType_ID());
							//match.set_CustomColumn("P_Payment_ID", paymentID.get(i));
							//match.set_CustomColumn("P_Amount", tempPaymentAmt);
							//match.set_CustomColumn("N_Payment_ID", receiptID.get(j));
							//match.set_CustomColumn("N_Amount", receiptAmount.get(j));
							
							if(tempPaymentAmt.compareTo(receiptAmount.get(j).abs())>0)
								match.set_CustomColumn("AllocationAmt", receiptAmount.get(j).abs());	
							else
								match.set_CustomColumn("AllocationAmt", tempPaymentAmt);
														
							match.set_ValueOfColumn("DateAllocated", hdr.getCreated());							
							match.saveEx();
							
							//match1
							X_T_MatchAllocation match1 = new X_T_MatchAllocation(getCtx(), 0, get_TrxName());
							match1.set_CustomColumn("C_AllocationHdr_ID", hdr.get_ID());
							
							//receipt
							match1.set_CustomColumn("C_Payment_ID", receiptID.get(j));
							match1.set_CustomColumn("C_DocType_ID", nPayment.getC_DocType_ID());
							
							//payment
							match1.set_CustomColumn("Match_Payment_ID", paymentID.get(i));
							match1.set_CustomColumn("Match_DocType_ID", pPayment.getC_DocType_ID());
							
							//match.set_CustomColumn("P_Payment_ID", paymentID.get(i));
							//match.set_CustomColumn("P_Amount", tempPaymentAmt);
							//match.set_CustomColumn("N_Payment_ID", receiptID.get(j));
							//match.set_CustomColumn("N_Amount", receiptAmount.get(j));
								
							if(tempPaymentAmt.compareTo(receiptAmount.get(j).abs())>0)
								match1.set_CustomColumn("AllocationAmt", receiptAmount.get(j).abs());	
							else
								match1.set_CustomColumn("AllocationAmt", tempPaymentAmt);
														
							match1.set_ValueOfColumn("DateAllocated", hdr.getCreated());
							match1.saveEx();
						}
						
						tempPaymentAmt = tempPaymentAmt.add(receiptAmount.get(j));
						
						if(tempPaymentAmt.compareTo(Env.ZERO)>0){
							receiptAmount.set(j, Env.ZERO);
						}else if(tempPaymentAmt.compareTo(Env.ZERO)<0){
							paymentAmount.set(i, Env.ZERO);
							receiptAmount.set(j, tempPaymentAmt);
							break;
						}else if(tempPaymentAmt.compareTo(Env.ZERO)==0){
							paymentAmount.set(i, Env.ZERO);
							receiptAmount.set(j, Env.ZERO);
							break;
						}
					}
				}
				
				BigDecimal totalReceiptAmount = Env.ZERO;
				for(int k=0; k<minInvoiceID.size(); k++)
				{
					totalReceiptAmount = totalReceiptAmount.add(minAmount.get(k));
				}
				if(!chargeID.isEmpty()){			
					if(totalReceiptAmount.compareTo(Env.ZERO)==0 && chargeID.get(0)>0 && tempPaymentAmt.compareTo(Env.ZERO)>0 && chargeAmount.compareTo(Env.ZERO)>0){					
						X_T_MatchAllocation match = new X_T_MatchAllocation(getCtx(), 0, get_TrxName());
						match.set_CustomColumn("C_AllocationHdr_ID", hdr.get_ID());
						
						//payment
						match.set_CustomColumn("C_Payment_ID", paymentID.get(i));
						
						MPayment pPayment = new MPayment(getCtx(), paymentID.get(i), get_TrxName());
						match.set_CustomColumn("C_DocType_ID", pPayment.getC_DocType_ID());
						//match.set_CustomColumn("P_Payment_ID", paymentID.get(i));
						//match.set_CustomColumn("P_Amount", tempPaymentAmt);
						
						//charge
						match.set_CustomColumn("C_Charge_ID", chargeID.get(i));
						match.set_CustomColumn("Description", chargeDescription.get(i));
						//match.set_CustomColumn("N_Amount", tempPaymentAmt.abs().negate());
						
						match.set_CustomColumn("AllocationAmt", tempPaymentAmt);
						match.set_ValueOfColumn("DateAllocated", hdr.getCreated());				
						match.saveEx();
						
						chargeAmount = chargeAmount.abs().subtract(tempPaymentAmt.abs());
											
					} else if(!paymentID.isEmpty() && receiptID.isEmpty() && chargeID.get(i)>0){
						X_T_MatchAllocation match = new X_T_MatchAllocation(getCtx(), 0, get_TrxName());
						match.set_CustomColumn("C_AllocationHdr_ID", hdr.get_ID());
						
						//payment
						match.set_CustomColumn("C_Payment_ID", paymentID.get(i));
						
						MPayment pPayment = new MPayment(getCtx(), paymentID.get(i), get_TrxName());
						match.set_CustomColumn("C_DocType_ID", pPayment.getC_DocType_ID());
						//match.set_CustomColumn("P_Payment_ID", paymentID.get(i));
						//match.set_CustomColumn("P_Amount", tempPaymentAmt);
						
						//charge
						match.set_CustomColumn("C_Charge_ID", chargeID.get(i));
						//match.set_CustomColumn("N_Amount", tempPaymentAmt.abs().negate());
						
						match.set_CustomColumn("AllocationAmt", tempPaymentAmt);
						match.set_ValueOfColumn("DateAllocated", hdr.getCreated());				
						match.saveEx();
						
						chargeAmount = chargeAmount.abs().subtract(tempPaymentAmt.abs());
							
					}
				}/* else if(paymentID.isEmpty() && !receiptID.isEmpty() && chargeID>0){
					X_T_MatchAllocation match = new X_T_MatchAllocation(getCtx(), 0, get_TrxName());
					match.set_CustomColumn("C_AllocationHdr_ID", get_ID());
					match.set_CustomColumn("N_Payment_ID", receiptID.get(0));
					match.set_CustomColumn("N_Amount", receiptAmount.get(0));
					match.set_CustomColumn("C_Charge_ID", chargeID);
					match.set_CustomColumn("P_Amount", chargeAmount);
					
					MPayment nPayment = new MPayment(getCtx(), receiptID.get(0), get_TrxName());
					match.set_CustomColumn("N_DocType_ID", nPayment.getC_DocType_ID());
					
					match.set_CustomColumn("AllocationAmt", chargeAmount.abs());
					
					match.saveEx();	
				}*/
				
			}
			//End Payment Loop
				
			//Loop for nInvoice
			for(int i=0; i<minI; i++)
			{
				BigDecimal tempnInvoiceAmt = minAmount.get(i);
				if(!chargeID.isEmpty()){
					if(tempnInvoiceAmt.compareTo(Env.ZERO)<0 && chargeID.get(0)>0 && chargeAmount.compareTo(Env.ZERO)<0){
						X_T_MatchAllocation match = new X_T_MatchAllocation(getCtx(), 0, get_TrxName());
						match.set_CustomColumn("C_AllocationHdr_ID", hdr.get_ID());
						
						//invoice
						match.set_CustomColumn("C_Invoice_ID", minInvoiceID.get(i));
						match.set_CustomColumn("DiscountAmt", nDiscountAmt.get(i));
						match.set_CustomColumn("WriteOffAmt", nWriteOffAmt.get(i));
						
						MInvoice nInvoice = new MInvoice(getCtx(), minInvoiceID.get(i), get_TrxName());
						match.set_CustomColumn("C_DocType_ID", nInvoice.getC_DocType_ID());
						
						//match.set_CustomColumn("N_Invoice_ID", minInvoiceID.get(i));
						//match.set_CustomColumn("N_Amount", tempnInvoiceAmt.abs().negate());
						
						//charge
						match.set_CustomColumn("C_Charge_ID", chargeID.get(0));
						//match.set_CustomColumn("P_Amount", tempnInvoiceAmt.abs());
						
						match.set_CustomColumn("AllocationAmt", tempnInvoiceAmt);
						match.set_ValueOfColumn("DateAllocated", hdr.getCreated());
						match.saveEx();
						
						chargeAmount = chargeAmount.add(tempnInvoiceAmt);
						
						nDiscountAmt.set(0, Env.ZERO);
						nWriteOffAmt.set(0, Env.ZERO);
						
					}
				}
			}
			//End nInvoice Loop	
			
			//Loop for Receipt
			for(int i=0; i<rec; i++)
			{
				BigDecimal tempReceiptAmt = receiptAmount.get(i);
				
				if(!chargeID.isEmpty()){
					if(tempReceiptAmt.compareTo(Env.ZERO)!=0 && chargeID.get(i)>0 && chargeAmount.compareTo(Env.ZERO)<0){
						X_T_MatchAllocation match = new X_T_MatchAllocation(getCtx(), 0, get_TrxName());
						match.set_CustomColumn("C_AllocationHdr_ID", hdr.get_ID());
						
						//receipt
						match.set_CustomColumn("C_Payment_ID", receiptID.get(i));
						
						MPayment nPayment = new MPayment(getCtx(), receiptID.get(i), get_TrxName());
						match.set_CustomColumn("C_DocType_ID", nPayment.getC_DocType_ID());
						//match.set_CustomColumn("N_Payment_ID", receiptID.get(i));
						//match.set_CustomColumn("N_Amount", tempReceiptAmt.abs().negate());
						
						//charge
						match.set_CustomColumn("C_Charge_ID", chargeID.get(i));
						//match.set_CustomColumn("P_Amount", tempReceiptAmt.abs());
						
						
						match.set_CustomColumn("AllocationAmt", tempReceiptAmt);
						match.set_ValueOfColumn("DateAllocated", hdr.getCreated());
						match.saveEx();	
						
						chargeAmount = chargeAmount.abs().negate().add(tempPaymentAmt.abs());
					}
				}
			}
			//End Receipt Loop
			
			plusInvoiceID.clear();
			minInvoiceID.clear();
			paymentID.clear();
			receiptID.clear();
			plusAmount.clear();
			minAmount.clear();
			paymentAmount.clear();
			receiptAmount.clear();
			pDiscountAmt.clear();
			nDiscountAmt.clear();
			pWriteOffAmt.clear();
			nWriteOffAmt.clear();
		}
			
			return null;
	}

}
