package com.eipl.amcs.setting.dto;

import com.eipl.amcs.master.account.model.Voucher;
import com.eipl.amcs.master.account.model.VoucherSubLedger;
import com.eipl.amcs.master.account.model.VoucherTransaction;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class AccountPostingDtoNew {
    private Voucher voucher;
    private List<VoucherTransaction> voucherTransactionList;
    private List<VoucherSubLedger> voucherSubLedgerList;
}