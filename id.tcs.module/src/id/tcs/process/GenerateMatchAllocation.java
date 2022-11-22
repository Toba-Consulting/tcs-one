package id.tcs.process;

import id.tcs.model.X_TCS_Match_Allocation;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;

import org.compiere.model.MAllocationHdr;
import org.compiere.model.MAllocationLine;
import org.compiere.model.Query;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;

public class GenerateMatchAllocation extends SvrProcess {

	@Override
	protected void prepare() {
		
	}

	@Override
	protected String doIt() throws Exception {
		
		String deleteTable = "DELETE FROM tcs_match_allocation";
		DB.executeUpdateEx(deleteTable, get_TrxName());
		
		//Scenario 1
		/*
		String whereClause = "C_AllocationHdr_ID in (select c_allocationhdr_Id from c_allocationline "
				+ "group by c_allocationhdr_Id having count(c_charge_id) = 0 and count(c_payment_id) = count(c_invoice_id))";
		*/
		
		String whereClause = "C_AllocationHdr_ID IN (SELECT DISTINCT C_AllocationHdr_ID FROM C_AllocationLine "
				+ "WHERE C_Charge_ID=0 AND C_Payment_ID>0 AND C_Invoice_ID>0)";
		
		int[] c_allocationhdr_list = new Query(getCtx(), "C_AllocationHdr", whereClause, get_TrxName()).setOrderBy("C_AllocationHDR_ID").getIDs();
		
		for (int i = 0; i < c_allocationhdr_list.length; i++) {
			MAllocationHdr allochdr = new MAllocationHdr(getCtx(), c_allocationhdr_list[i], get_TrxName());
			for(MAllocationLine line : allochdr.getLines(true)){
				X_TCS_Match_Allocation match_alloc = new X_TCS_Match_Allocation(getCtx(), 0, get_TrxName());
				match_alloc.setAD_Org_ID(line.getAD_Org_ID());
				match_alloc.setC_Invoice_ID(line.getC_Invoice_ID());
				match_alloc.setMatch_Payment_ID(line.getC_Payment_ID());
				match_alloc.setAllocatedAmt(line.getAmount().abs());
				match_alloc.setC_AllocationHdr_ID(line.getC_AllocationHdr_ID());
				match_alloc.saveEx();
			}
		}
		
		//Scenario 2
		whereClause = "c_allocationhdr_Id in (select c_allocationhdr_id from c_allocationline "
				+ "group by c_allocationhdr_Id having count(c_allocationline_id) = 3 and count(c_charge_id) = 1 "
				+ "and ((count(c_invoice_id) = 2 and count(c_payment_id) = 1) or (count(c_payment_id) = 2 and count(c_invoice_id) = 1)))";
		c_allocationhdr_list = new Query(getCtx(), "C_AllocationHdr", whereClause, get_TrxName()).setOrderBy("C_AllocationHDR_ID").getIDs();
		for (int i = 0; i < c_allocationhdr_list.length; i++) {
			MAllocationHdr allochdr = new MAllocationHdr(getCtx(), c_allocationhdr_list[i], get_TrxName());
			int singleinvoice = 0;
			int singlepayment = 0;
			int singlecharge = 0;
			for(MAllocationLine line : allochdr.getLines(true)){
				if(line.getC_Payment_ID() > 0 && line.getC_Invoice_ID() > 0){
					X_TCS_Match_Allocation match_alloc = new X_TCS_Match_Allocation(getCtx(), 0, get_TrxName());
					match_alloc.setAD_Org_ID(line.getAD_Org_ID());

					match_alloc.setC_Invoice_ID(line.getC_Invoice_ID());
					match_alloc.setMatch_Payment_ID(line.getC_Payment_ID());
					match_alloc.setAllocatedAmt(line.getAmount().abs());
					match_alloc.setC_AllocationHdr_ID(line.getC_AllocationHdr_ID());
					match_alloc.saveEx();
					
					X_TCS_Match_Allocation match_alloc2 = new X_TCS_Match_Allocation(getCtx(), 0, get_TrxName());
					match_alloc2.setAD_Org_ID(line.getAD_Org_ID());

					match_alloc2.setC_Payment_ID(line.getC_Payment_ID());
					match_alloc2.setMatch_Invoice_ID(line.getC_Invoice_ID());
					match_alloc2.setAllocatedAmt(line.getAmount().abs());
					match_alloc2.setC_AllocationHdr_ID(line.getC_AllocationHdr_ID());
					match_alloc2.saveEx();
				}
				else if(line.getC_Payment_ID() > 0 && line.getC_Invoice_ID() == 0){
					singlepayment = line.getC_Payment_ID();
//					continue;
				}
				else if(line.getC_Invoice_ID() > 0 && line.getC_Payment_ID() == 0){
					singleinvoice = line.getC_Invoice_ID();
//					continue;
				}
				else if(line.getC_Charge_ID() > 0){
					singlecharge = line.getC_Charge_ID();
//					continue;
				}
				if(singlecharge > 0 && singleinvoice > 0){
					X_TCS_Match_Allocation match_alloc_charge = new X_TCS_Match_Allocation(getCtx(), 0, get_TrxName());
					match_alloc_charge.setC_AllocationHdr_ID(line.getC_AllocationHdr_ID());
					match_alloc_charge.setAD_Org_ID(line.getAD_Org_ID());

					match_alloc_charge.setC_Invoice_ID(singleinvoice);
					match_alloc_charge.setC_Charge_ID(singlecharge);
					match_alloc_charge.setAllocatedAmt(line.getAmount().abs());
					match_alloc_charge.saveEx();
				}
				else if(singlecharge > 0 && singlepayment > 0){
					X_TCS_Match_Allocation match_alloc_charge = new X_TCS_Match_Allocation(getCtx(), 0, get_TrxName());
					match_alloc_charge.setAD_Org_ID(line.getAD_Org_ID());
					match_alloc_charge.setC_AllocationHdr_ID(line.getC_AllocationHdr_ID());
					match_alloc_charge.setC_Payment_ID(singlepayment);
					match_alloc_charge.setC_Charge_ID(singlecharge);
					match_alloc_charge.setAllocatedAmt(line.getAmount().abs());
					match_alloc_charge.saveEx();
				}
			}
		}
		
		//scenario 3
		whereClause = "c_allocationhdr_Id in (select c_allocationhdr_id from c_allocationline group by c_allocationhdr_Id having count(c_charge_id) > 0 and count(c_invoice_id) = 0 and count(c_payment_Id)=1)";
		c_allocationhdr_list = new Query(getCtx(), "C_AllocationHdr", whereClause, get_TrxName()).setOrderBy("C_AllocationHDR_ID").getIDs();
		for (int i = 0; i < c_allocationhdr_list.length; i++) {
			MAllocationHdr allochdr = new MAllocationHdr(getCtx(), c_allocationhdr_list[i], get_TrxName());
			ArrayList<Integer> Charge_ID = new ArrayList<Integer>();
			ArrayList<BigDecimal> amount = new ArrayList<BigDecimal>();
			int Payment_ID = 0;
			for(MAllocationLine line : allochdr.getLines(true)){
				if(line.getC_Payment_ID() > 0){
					Payment_ID = line.getC_Payment_ID();
				}
				else if(line.getC_Charge_ID() > 0){
					Charge_ID.add(line.getC_Charge_ID());
					amount.add(line.getAmount());
				}
			}
			for (int j = 0; j < Charge_ID.size(); j++) {
				X_TCS_Match_Allocation match_alloc_charge = new X_TCS_Match_Allocation(getCtx(), 0, get_TrxName());
				match_alloc_charge.setAD_Org_ID(allochdr.getAD_Org_ID());
				match_alloc_charge.setC_AllocationHdr_ID(allochdr.getC_AllocationHdr_ID());
				match_alloc_charge.setC_Payment_ID(Payment_ID);
				match_alloc_charge.setC_Charge_ID(Charge_ID.get(j));
				match_alloc_charge.setAllocatedAmt(amount.get(j).abs());
				match_alloc_charge.saveEx();
			}
		}
		
		//scenario 4
		whereClause = "c_allocationhdr_Id in (select c_allocationhdr_id from c_allocationline group by c_allocationhdr_Id having count(c_charge_id)=1 and count(c_invoice_id) =1 and count(c_payment_Id) = 0)";
		c_allocationhdr_list = new Query(getCtx(), "C_AllocationHdr", whereClause, get_TrxName()).setOrderBy("C_AllocationHDR_ID").getIDs();
		for (int i = 0; i < c_allocationhdr_list.length; i++) {
			MAllocationHdr allochdr = new MAllocationHdr(getCtx(), c_allocationhdr_list[i], get_TrxName());
			int Charge_ID = 0;
			int Invoice_ID = 0;
			BigDecimal Amount = Env.ZERO;
			
			for(MAllocationLine line : allochdr.getLines(true)){
				if(line.getC_Invoice_ID() > 0){
					Invoice_ID = line.getC_Invoice_ID();
					Amount = line.getAmount();
					continue;
				}
				else if(line.getC_Charge_ID() > 0){
					Charge_ID = line.getC_Charge_ID();
					continue;
				}
			}
			if(Charge_ID > 0 && Invoice_ID > 0){
				X_TCS_Match_Allocation match_alloc = new X_TCS_Match_Allocation(getCtx(), 0, get_TrxName());
				match_alloc.setAD_Org_ID(allochdr.getAD_Org_ID());
				match_alloc.setC_AllocationHdr_ID(allochdr.getC_AllocationHdr_ID());
				match_alloc.setC_Invoice_ID(Invoice_ID);
				match_alloc.setC_Charge_ID(Charge_ID);
				match_alloc.setAllocatedAmt(Amount.abs());
				match_alloc.saveEx();
			}
		}

		//scenario 5
		whereClause = "c_allocationhdr_Id in (select c_allocationhdr_id from c_allocationline group by c_allocationhdr_Id having count(c_charge_id)=1 and count(c_invoice_id) = 2 and count(c_payment_Id) = 0)";
		c_allocationhdr_list = new Query(getCtx(), "C_AllocationHdr", whereClause, get_TrxName()).setOrderBy("C_AllocationHDR_ID").getIDs();
		for (int i = 0; i < c_allocationhdr_list.length; i++) {
			MAllocationHdr allochdr = new MAllocationHdr(getCtx(), c_allocationhdr_list[i], get_TrxName());
			int Charge_ID = 0;
			ArrayList<Integer> Invoice_ID = new ArrayList<Integer>();
			ArrayList<BigDecimal> Amount = new ArrayList<BigDecimal>();
			BigDecimal BiggestAmount = Env.ZERO;
			BigDecimal SmallestAmount = Env.ZERO;
			int BiggestInvoiceIndex = 0;

			for(MAllocationLine line : allochdr.getLines(true)){
				if(line.getC_Invoice_ID() > 0){
					Invoice_ID.add(line.getC_Invoice_ID());
					Amount.add(line.getAmount());
					continue;
				}
				else if(line.getC_Charge_ID() > 0){
					Charge_ID = line.getC_Charge_ID();
					continue;
				}
			}

			SmallestAmount = Collections.min(Amount);
			BiggestAmount = Collections.max(Amount);

			BiggestInvoiceIndex = Amount.indexOf(BiggestAmount);

			X_TCS_Match_Allocation match_alloc = new X_TCS_Match_Allocation(getCtx(), 0, get_TrxName());
			match_alloc.setAD_Org_ID(allochdr.getAD_Org_ID());
			match_alloc.setC_AllocationHdr_ID(allochdr.getC_AllocationHdr_ID());
			match_alloc.setC_Invoice_ID(Invoice_ID.get(0));
			match_alloc.setMatch_Invoice_ID(Invoice_ID.get(1));
			match_alloc.setAllocatedAmt(SmallestAmount.abs());
			match_alloc.saveEx();

			X_TCS_Match_Allocation match_alloc2 = new X_TCS_Match_Allocation(getCtx(), 0, get_TrxName());
			match_alloc2.setAD_Org_ID(allochdr.getAD_Org_ID());
			match_alloc2.setC_AllocationHdr_ID(allochdr.getC_AllocationHdr_ID());
			match_alloc2.setC_Invoice_ID(Invoice_ID.get(1));
			match_alloc2.setMatch_Invoice_ID(Invoice_ID.get(0));
			match_alloc2.setAllocatedAmt(SmallestAmount.abs());
			match_alloc2.saveEx();

			X_TCS_Match_Allocation match_alloc_charge = new X_TCS_Match_Allocation(getCtx(), 0, get_TrxName());
			match_alloc_charge.setAD_Org_ID(allochdr.getAD_Org_ID());
			match_alloc_charge.setC_AllocationHdr_ID(allochdr.getC_AllocationHdr_ID());
			match_alloc_charge.setC_Invoice_ID(Invoice_ID.get(BiggestInvoiceIndex));
			match_alloc_charge.setC_Charge_ID(Charge_ID);
			match_alloc_charge.setAllocatedAmt(BiggestAmount.subtract(SmallestAmount));
			match_alloc_charge.saveEx();
		}
		
		
		//scenario 6
		whereClause = "c_allocationhdr_Id in (select c_allocationhdr_id from c_allocationline group by c_allocationhdr_Id having count(c_charge_id)=1 and count(c_invoice_id) = 0 and count(c_payment_Id) = 2)";
		c_allocationhdr_list = new Query(getCtx(), "C_AllocationHdr", whereClause, get_TrxName()).setOrderBy("C_AllocationHDR_ID").getIDs();
		for (int i = 0; i < c_allocationhdr_list.length; i++) {
			MAllocationHdr allochdr = new MAllocationHdr(getCtx(), c_allocationhdr_list[i], get_TrxName());
			int Charge_ID = 0;
			ArrayList<Integer> Payment_ID = new ArrayList<Integer>();
			ArrayList<BigDecimal> Amount = new ArrayList<BigDecimal>();
			BigDecimal BiggestAmount = Env.ZERO;
			BigDecimal SmallestAmount = Env.ZERO;
			int BiggestPaymentIndex = 0;

			for(MAllocationLine line : allochdr.getLines(true)){
				if(line.getC_Payment_ID() > 0){
					Payment_ID.add(line.getC_Payment_ID());
					Amount.add(line.getAmount());
					continue;
				}
				else if(line.getC_Charge_ID() > 0){
					Charge_ID = line.getC_Charge_ID();
					continue;
				}
			}

			SmallestAmount = Collections.min(Amount);
			BiggestAmount = Collections.max(Amount);

			BiggestPaymentIndex = Amount.indexOf(BiggestAmount);

			X_TCS_Match_Allocation match_alloc = new X_TCS_Match_Allocation(getCtx(), 0, get_TrxName());
			match_alloc.setAD_Org_ID(allochdr.getAD_Org_ID());
			match_alloc.setC_AllocationHdr_ID(allochdr.getC_AllocationHdr_ID());
			match_alloc.setC_Payment_ID(Payment_ID.get(0));
			match_alloc.setMatch_Payment_ID(Payment_ID.get(1));
			match_alloc.setAllocatedAmt(SmallestAmount.abs());
			match_alloc.saveEx();

			X_TCS_Match_Allocation match_alloc2 = new X_TCS_Match_Allocation(getCtx(), 0, get_TrxName());
			match_alloc2.setAD_Org_ID(allochdr.getAD_Org_ID());
			match_alloc2.setC_AllocationHdr_ID(allochdr.getC_AllocationHdr_ID());
			match_alloc2.setC_Payment_ID(Payment_ID.get(1));
			match_alloc2.setMatch_Payment_ID(Payment_ID.get(0));
			match_alloc2.setAllocatedAmt(SmallestAmount.abs());
			match_alloc2.saveEx();

			X_TCS_Match_Allocation match_alloc_charge = new X_TCS_Match_Allocation(getCtx(), 0, get_TrxName());
			match_alloc_charge.setAD_Org_ID(allochdr.getAD_Org_ID());
			match_alloc_charge.setC_AllocationHdr_ID(allochdr.getC_AllocationHdr_ID());
			match_alloc_charge.setC_Payment_ID(Payment_ID.get(BiggestPaymentIndex));
			match_alloc_charge.setC_Charge_ID(Charge_ID);
			match_alloc_charge.setAllocatedAmt(BiggestAmount.subtract(SmallestAmount));
			match_alloc_charge.saveEx();
		}
			
		return null;
	}

}
