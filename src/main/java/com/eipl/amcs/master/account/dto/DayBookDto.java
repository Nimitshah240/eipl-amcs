package com.eipl.amcs.master.account.dto;

import com.eipl.amcs.master.account.model.Ledger;
import com.eipl.amcs.master.account.model.Voucher;
import com.eipl.amcs.master.account.model.VoucherTransaction;
import com.eipl.amcs.master.account.model.VoucherType;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class DayBookDto {
    private LocalDate voucherDate;
    private String voucherNo;
    private VoucherType voucherType;
    private Ledger ledger;
    private BigDecimal creditAmount;
    private BigDecimal debitAmount;
    private Voucher voucher;
    private VoucherTransaction voucherTransaction;

    public DayBookDto(Voucher voucher, VoucherTransaction txn) {
        this.voucherDate = voucher.getVoucherDate();
        this.voucherNo = voucher.getCode();
        this.voucherType = voucher.getVoucherType();
        this.ledger = txn.getLedger();
        this.voucher = voucher;
        this.voucherTransaction = txn;

        if (txn.getCreditDebit() != null && txn.getCreditDebit()) {
            this.creditAmount = txn.getAmount();
            this.debitAmount = BigDecimal.ZERO;
        } else { // False for Debit
            this.debitAmount = txn.getAmount();
            this.creditAmount = BigDecimal.ZERO;
        }
    }
}
