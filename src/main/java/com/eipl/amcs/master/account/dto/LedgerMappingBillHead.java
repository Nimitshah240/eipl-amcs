package com.eipl.amcs.master.account.dto;

import com.eipl.amcs.base.model.BaseModel;
import com.eipl.amcs.master.inventory.model.ProductGroup;
import com.eipl.amcs.master.operation.model.BillCriteria;
import com.eipl.amcs.master.operation.model.BillHead;
import com.eipl.amcs.master.org.model.Society;
import com.eipl.amcs.master.org.model.Union;
import com.eipl.amcs.master.account.model.Ledger;


import java.math.BigInteger;

public class LedgerMappingBillHead extends BaseModel {

    private String code;
    private Integer type;
    private Boolean hasSubLedger;
    private Boolean creditDebit;
    private Ledger ledger;
    private BillHead billHead;
    private Society society;
    private BillCriteria billCriteria;
    private String unionCode;

    //    xcol1 1-On , 0-Off


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Boolean getHasSubLedger() {
        return hasSubLedger;
    }

    public void setHasSubLedger(Boolean hasSubLedger) {
        this.hasSubLedger = hasSubLedger;
    }

    public Boolean getCreditDebit() {
        return creditDebit;
    }

    public void setCreditDebit(Boolean creditDebit) {
        this.creditDebit = creditDebit;
    }

    public Ledger getLedger() {
        return ledger;
    }

    public void setLedger(Ledger ledger) {
        this.ledger = ledger;
    }

    public BillHead getBillHead() {
        return billHead;
    }

    public void setBillHead(BillHead billHead) {
        this.billHead = billHead;
    }

    public Society getSociety() {
        return society;
    }

    public void setSociety(Society society) {
        this.society = society;
    }

    public BillCriteria getBillCriteria() {
        return billCriteria;
    }

    public void setBillCriteria(BillCriteria billCriteria) {
        this.billCriteria = billCriteria;
    }

    public String getUnionCode() {
        return unionCode;
    }

    public void setUnionCode(String unionCode) {
        this.unionCode = unionCode;
    }

    public boolean isHasSubLedger() {
        return hasSubLedger;
    }
}
