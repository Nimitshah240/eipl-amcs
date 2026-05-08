package com.eipl.amcs.base;

import com.eipl.amcs.master.account.model.VoucherTransaction;
import com.eipl.amcs.operation.inventory.dto.ReceiptTxnTaxDto;
import com.eipl.amcs.operation.inventory.dto.SaleTxnTaxDto;

import java.util.List;

public interface PopupCallback {
    default void reloadData(boolean flag) {
    }

    default void reloadHardwareSetting(boolean flag) {
    }

    default void returnProductReceiptTxnTaxDto(ReceiptTxnTaxDto dto) {
    }

    default void returnProductSaleTxnTaxDto(SaleTxnTaxDto dto) {
    }

    default void returnHavalaTransaction(List<VoucherTransaction> voucherTransactionList, boolean credit_debit) {

    }
}