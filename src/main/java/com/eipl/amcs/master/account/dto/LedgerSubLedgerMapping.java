package com.eipl.amcs.master.account.dto;

import com.eipl.amcs.master.account.model.SubLedger;
import com.eipl.amcs.base.model.BaseModel;
import com.eipl.amcs.base.model.BaseModelTxn;
import com.eipl.amcs.master.org.model.Society;
import com.eipl.amcs.master.org.model.Union;
import com.eipl.amcs.master.account.model.Ledger;

public class LedgerSubLedgerMapping  extends BaseModelTxn {

    private String code;
    private Ledger ledger;
    private SubLedger subLedger;
    private Society society;
    private String unionCode;

    public String getUnionCode() {
        return unionCode;
    }

    public void setUnionCode(String unionCode) {
        this.unionCode = unionCode;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Ledger getLedger() {
        return ledger;
    }

    public void setLedger(Ledger ledger) {
        this.ledger = ledger;
    }

    public SubLedger getSubLedger() {
        return subLedger;
    }

    public void setSubLedger(SubLedger subLedger) {
        this.subLedger = subLedger;
    }

    public Society getSociety() {
        return society;
    }

    public void setSociety(Society society) {
        this.society = society;
    }

}
