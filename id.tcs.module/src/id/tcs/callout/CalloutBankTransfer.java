package id.tcs.callout;

import java.math.BigDecimal;
import java.util.Properties;

import org.adempiere.base.IColumnCallout;
import id.tcs.model.MBankTransfer;
import org.compiere.model.GridField;
import org.compiere.model.GridTab;
import org.compiere.model.MBankAccount;
import org.compiere.util.Env;

public class CalloutBankTransfer implements IColumnCallout{
	
	@Override
	public String start(Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value, Object oldValue) {
		if(mField.getColumnName().equals(MBankTransfer.COLUMNNAME_C_BankAccount_From_ID))
			return setCurrencyFrom(ctx, WindowNo, mTab, mField, value, oldValue);
		
		else if(mField.getColumnName().equals(MBankTransfer.COLUMNNAME_C_BankAccount_To_ID))
			return SetCurrencyTo(ctx, WindowNo, mTab, mField, value, oldValue);
		
		else if(mField.getColumnName().equals(MBankTransfer.COLUMNNAME_ChargeAmt))
			return setPayAmt(ctx, WindowNo, mTab, mField, value, oldValue);
		
		else if(mField.getColumnName().equals(MBankTransfer.COLUMNNAME_AmountFrom))
			return setPayAmt(ctx, WindowNo, mTab, mField, value, oldValue);
		
		else if(mField.getColumnName().equals(MBankTransfer.COLUMNNAME_AmountTo))
			return setPayAmt(ctx, WindowNo, mTab, mField, value, oldValue);
		
		else if(mField.getColumnName().equals(MBankTransfer.COLUMNNAME_TransferFeeType))
			return setPayAmt(ctx, WindowNo, mTab, mField, value, oldValue);
		
		return null;
	}
	
	//trigger bankAccountFrom
	protected String setCurrencyFrom(Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value, Object oldValue) {
		
		if(value == null)
			return "";
		
		int C_BankAccount_From_ID = (int) value;

		MBankAccount bankAccountFrom = new MBankAccount(Env.getCtx(), C_BankAccount_From_ID, null);
		
		mTab.setValue("C_Currency_From_ID", bankAccountFrom.getC_Currency_ID());
		
		return "";
	}
	
	//trigger bankAccountTo
	protected String SetCurrencyTo(Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value, Object oldValue) {
		if(value == null)
			return "";
		
		int C_BankAccount_To_ID = (int) value;

		MBankAccount bankAccountTo = new MBankAccount(Env.getCtx(), C_BankAccount_To_ID, null);
		mTab.setValue("C_Currency_To_ID", bankAccountTo.getC_Currency_ID());

		return "";
	}

	//Trigger ChargeAmt, AmountFrom, AmountTo
	protected String setPayAmt(Properties ctx, int windowNo, GridTab mTab, GridField mField, Object value, Object oldValue) {
		if(value != null){
	
			BigDecimal payAmtFrom = (BigDecimal) mTab.getValue("AmountFrom") ;
			BigDecimal payAmtTo = (BigDecimal) mTab.getValue("AmountTo") ;
			
			if(mTab.getValueAsBoolean("isHasTransferFee"))
			{
				if(mTab.getValue("TransferFeeType").equals(MBankTransfer.TRANSFERFEETYPE_ChargeOnBankFrom)) {				
					BigDecimal totalFrom = payAmtFrom;
					mTab.setValue("PayAmtFrom", totalFrom);
					mTab.setValue("PayAmtTo", payAmtTo);
				}
				else if(mTab.getValue("TransferFeeType").equals(MBankTransfer.TRANSFERFEETYPE_ChargeOnBankTo)){				
					BigDecimal totalTo = payAmtTo;
					mTab.setValue("PayAmtFrom", payAmtFrom);
					mTab.setValue("PayAmtTo", totalTo);
				}
			}
			else {
				mTab.setValue("PayAmtFrom", payAmtFrom);
				mTab.setValue("PayAmtTo", payAmtTo);		
			}
			return "";
		}
		return "";
	}
}