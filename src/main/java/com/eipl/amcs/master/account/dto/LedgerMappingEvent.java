package com.eipl.amcs.master.account.dto;

import com.eipl.amcs.base.model.BaseModelTxn;
import com.eipl.amcs.master.org.model.Society;
import com.eipl.amcs.master.org.model.Union;
import com.eipl.amcs.master.account.model.Events;
import com.eipl.amcs.master.account.model.VoucherType;
import com.eipl.amcs.master.account.model.Ledger;

public class LedgerMappingEvent extends BaseModelTxn {

    private Integer code;
    private Boolean creditSubLedger;
    private Boolean debitSubLedger;
    private Society society;
    private String unionCode;
    private Integer eventcode;
    private Events events;
    private Ledger creditLedger;
    private Ledger debitLedger;
    private VoucherType voucherType;

//    xcol1 1-On , 0-Off

    public Integer getCode() {
        return code;
    }

    public Integer getEventcode() {
        return eventcode;
    }

    public void setEventcode(Integer eventcode) {
        this.eventcode = eventcode;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public Boolean getCreditSubLedger() {
        return creditSubLedger;
    }

    public void setCreditSubLedger(Boolean creditSubLedger) {
        this.creditSubLedger = creditSubLedger;
    }

    public Boolean getDebitSubLedger() {
        return debitSubLedger;
    }

    public void setDebitSubLedger(Boolean debitSubLedger) {
        this.debitSubLedger = debitSubLedger;
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

    public Events getEvents() {
        return events;
    }

    public void setEvents(Events events) {
        this.events = events;
    }

    public Ledger getCreditLedger() {
        return creditLedger;
    }

    public void setCreditLedger(Ledger creditLedger) {
        this.creditLedger = creditLedger;
    }

    public Ledger getDebitLedger() {
        return debitLedger;
    }

    public void setDebitLedger(Ledger debitLedger) {
        this.debitLedger = debitLedger;
    }

    public VoucherType getVoucherType() {
        return voucherType;
    }

    public void setVoucherType(VoucherType voucherType) {
        this.voucherType = voucherType;
    }
}
