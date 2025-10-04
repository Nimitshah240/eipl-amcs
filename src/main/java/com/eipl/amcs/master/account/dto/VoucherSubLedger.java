package com.eipl.amcs.master.account.dto;

import com.eipl.amcs.master.account.model.SubLedger;
import com.eipl.amcs.master.account.model.Voucher;
import com.eipl.amcs.base.model.BaseModelTxn;
import com.eipl.amcs.master.account.model.VoucherTransaction;

import java.math.BigDecimal;


public class VoucherSubLedger extends BaseModelTxn {

    private String code;
    private BigDecimal amount;
    private Boolean creditDebit;
    private String narration;
    private SubLedger subLedger;
    private Voucher voucher;
    private VoucherTransaction voucherTransaction;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public Boolean getCreditDebit() {
		return creditDebit;
	}

	public void setCreditDebit(Boolean creditDebit) {
		this.creditDebit = creditDebit;
	}

	public String getNarration() {
		return narration;
	}

	public void setNarration(String narration) {
		this.narration = narration;
	}

	public SubLedger getSubLedger() {
		return subLedger;
	}

	public void setSubLedger(SubLedger subLedger) {
		this.subLedger = subLedger;
	}

	public Voucher getVoucher() {
		return voucher;
	}

	public void setVoucher(Voucher voucher) {
		this.voucher = voucher;
	}

	public VoucherTransaction getVoucherTransaction() {
		return voucherTransaction;
	}

	public void setVoucherTransaction(VoucherTransaction voucherTransaction) {
		this.voucherTransaction = voucherTransaction;
	}
}
