package com.eipl.amcs.master.account.dto;

import com.eipl.amcs.base.model.BaseModel;
import com.eipl.amcs.base.model.BaseModelTxn;
import com.eipl.amcs.master.inventory.model.ProductGroup;
import com.eipl.amcs.master.org.model.Society;
import com.eipl.amcs.master.org.model.Union;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.eipl.amcs.master.account.model.Ledger;

public class SubLedgerLedgerConfig extends BaseModelTxn {

    private String code;
    private Integer subLedgerType;
    private Ledger ledger;
    private Society society;
    private String unionCode;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getSubLedgerType() {
        return subLedgerType;
    }

    public void setSubLedgerType(Integer subLedgerType) {
        this.subLedgerType = subLedgerType;
    }

    public Ledger getLedger() {
        return ledger;
    }

    public void setLedger(Ledger ledger) {
        this.ledger = ledger;
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
}
