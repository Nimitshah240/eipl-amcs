package com.eipl.amcs.utils;

import com.eipl.amcs.master.account.model.*;
import com.eipl.amcs.master.org.model.Society;

import java.math.BigDecimal;
import java.time.LocalDate;

public class VoucherUtil {
    public static Voucher getVoucherInstance(String code, String billNo, LocalDate billDate, LocalDate voucherDate, String remarks,
                                             VoucherType voucherType, String financialYear, Society society,
                                             String unionCode, String dockNo) {
        Voucher voucher = new Voucher();
        voucher.setCode(code);
        voucher.setVoucherDate(voucherDate);
        voucher.setBillNo(billNo);
        voucher.setBillDate(billDate);
        voucher.setRemarks(remarks);
        voucher.setVoucherType(voucherType);
        voucher.setCancelled(false);
        voucher.setAutoPosted(true);
        voucher.setSociety(society);
        voucher.setDockCode(dockNo);
        voucher.setUnionCode(unionCode);
        voucher.setInitData();
        voucher.setFinancialYearsCode(financialYear);

        return voucher;
    }

    public static VoucherTransaction getVoucherTxn(Voucher voucher, BigDecimal amount, boolean creditDebit, Ledger ledger,
                                                   String narration, String code) {
        VoucherTransaction txn = new VoucherTransaction();
        txn.setCode(voucher.getCode() + "T" + code);
        txn.setAmount(amount);
        txn.setCreditDebit(creditDebit);
        txn.setLedger(ledger);
        txn.setNarration(narration);
        txn.setVoucher(voucher);
        txn.setInitData();

        return txn;
    }

    public static VoucherSubLedger getVoucherSubLedger(Voucher voucher, VoucherTransaction txn, String code,
                                                       BigDecimal amount, boolean creditDebit, String narration,
                                                       SubLedger subLedger) {
        VoucherSubLedger voucherSubLedger = new VoucherSubLedger();
        voucherSubLedger.setCode(txn.getCode() + "S" + code);
        voucherSubLedger.setAmount(amount);
        voucherSubLedger.setCreditDebit(creditDebit);
        voucherSubLedger.setNarration(narration);
        voucherSubLedger.setSubLedger(subLedger);
        voucherSubLedger.setVoucher(voucher);
        voucherSubLedger.setVoucherTransaction(txn);
        voucherSubLedger.setInitData();

        return voucherSubLedger;
    }
}
