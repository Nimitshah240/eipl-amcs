package com.eipl.amcs.master.account.dto;

import com.eipl.amcs.master.account.model.VoucherSubLedger;
import com.eipl.amcs.base.model.BaseModelTxn;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.beans.Transient;
import java.math.BigDecimal;
import java.util.List;
import com.eipl.amcs.master.account.model.Ledger;
import com.eipl.amcs.master.account.model.Voucher;


public class VoucherTransaction extends BaseModelTxn {


    private String code;
    private BigDecimal amount;
    private Boolean creditDebit;
    private String narration;
    private Ledger ledger;
    private Voucher voucher;

	@JsonIgnore
	private List<VoucherSubLedger> voucherSubLedgers;

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

	public Ledger getLedger() {
		return ledger;
	}

	public void setLedger(Ledger ledger) {
		this.ledger = ledger;
	}

	public Voucher getVoucher() {
		return voucher;
	}

	public void setVoucher(Voucher voucher) {
		this.voucher = voucher;
	}

	public List<VoucherSubLedger> getVoucherSubLedgers() {
		return voucherSubLedgers;
	}

	public void setVoucherSubLedgers(List<VoucherSubLedger> voucherSubLedgers) {
		this.voucherSubLedgers = voucherSubLedgers;
	}
}
