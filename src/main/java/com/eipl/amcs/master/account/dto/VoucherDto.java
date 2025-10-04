package com.eipl.amcs.master.account.dto;

import com.eipl.amcs.base.model.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import com.eipl.amcs.master.account.model.Voucher;
import com.eipl.amcs.master.account.model.VoucherSubLedger;
import com.eipl.amcs.master.account.model.VoucherTransaction;

@NoArgsConstructor
@Setter
@Getter
@AllArgsConstructor
public class VoucherDto extends BaseModel {

    private Voucher voucher;
    private List<VoucherTransaction> voucherTransactions;
    private List<VoucherSubLedger> voucherSubLedgers;

//    public Voucher getVoucher() {
//        return voucher;
//    }
//
//    public void setVoucher(Voucher voucher) {
//        this.voucher = voucher;
//    }
//
//    public List<VoucherTransaction> getVoucherTransactions() {
//        return voucherTransactions;
//    }
//
//    public List<VoucherSubLedger> getVoucherSubLedgers() {
//        return voucherSubLedgers;
//    }
//
//    public void setVoucherSubLedgers(List<VoucherSubLedger> voucherSubLedgers) {
//        this.voucherSubLedgers = voucherSubLedgers;
//    }
//
//    public void setVoucherTransactions(List<VoucherTransaction> voucherTransactions) {
//        this.voucherTransactions = voucherTransactions;
//    }
//
//
//    public VoucherDto(Voucher voucher, List<VoucherTransaction> voucherTransactions, List<VoucherSubLedger> voucherSubLedgers) {
//        this.voucher = voucher;
//        this.voucherTransactions = voucherTransactions;
//        this.voucherSubLedgers = voucherSubLedgers;
//    }
//
//    public VoucherDto() {
//    }
}