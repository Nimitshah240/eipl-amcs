package com.eipl.amcs.master.account.dto;

import com.eipl.amcs.base.model.BaseModel;
import com.eipl.amcs.master.account.model.Voucher;
import com.eipl.amcs.master.account.model.VoucherSubLedger;
import com.eipl.amcs.master.account.model.VoucherTransaction;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@Setter
@Getter
@AllArgsConstructor
public class VoucherDto extends BaseModel {

    private Voucher voucher;
    private List<VoucherTransaction> voucherTransactions;
    private List<VoucherSubLedger> voucherSubLedgers;
}