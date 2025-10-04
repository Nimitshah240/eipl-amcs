package com.eipl.amcs.master.account.dto;

import com.eipl.amcs.base.model.BaseModelTxn;
import com.eipl.amcs.master.org.model.Society;
import com.eipl.amcs.master.account.model.Ledger;
import com.eipl.amcs.master.org.model.Union;
import com.eipl.amcs.master.account.model.VoucherType;

import java.math.BigDecimal;

public class VoucherTypeLedgerConfig extends BaseModelTxn {

    private String code;
    private Boolean creditDebit;
    private Society society;
    private String unionCode;
    private Ledger ledger;
    private VoucherType voucherType;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Boolean getCreditDebit() {
        return creditDebit;
    }

    public void setCreditDebit(Boolean creditDebit) {
        this.creditDebit = creditDebit;
    }

    public Society getSociety() {
        return society;
    }

    public void setSociety(Society society) {
        this.society = society;
    }

    public String getUnionCode() {
        return unionCode;
    }

    public void setUnionCode(String unionCode) {
        this.unionCode = unionCode;
    }

    public Ledger getLedger() {
        return ledger;
    }

    public void setLedger(Ledger ledger) {
        this.ledger = ledger;
    }

    public VoucherType getVoucherType() {
        return voucherType;
    }

    public void setVoucherType(VoucherType voucherType) {
        this.voucherType = voucherType;
    }
}