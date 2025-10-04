package com.eipl.amcs.master.account.dto;
import com.eipl.amcs.master.account.model.Ledger;

import com.eipl.amcs.master.account.model.LedgerSubLedgerMapping;
import com.eipl.amcs.master.account.model.SubLedger;
import java.util.List;

public class LedgerSubLedgerDto {
    private Ledger ledger;
    private SubLedger subLedger;
    private List<Ledger> ledgerList;
    private List<SubLedger> subLedgerList;
    private List<LedgerSubLedgerMapping> ledgerSubLedgerMappingList;

    public LedgerSubLedgerDto() {
    }

    public LedgerSubLedgerDto(Ledger ledger, List<LedgerSubLedgerMapping> ledgerSubLedgerMappingList) {
        this.ledger = ledger;
        this.ledgerSubLedgerMappingList = ledgerSubLedgerMappingList;
    }

    public LedgerSubLedgerDto(SubLedger subLedger, List<LedgerSubLedgerMapping> ledgerSubLedgerMappingList) {
        this.subLedger = subLedger;
        this.ledgerSubLedgerMappingList = ledgerSubLedgerMappingList;
    }

    public List<Ledger> getLedgerList() {
        return ledgerList;
    }

    public void setLedgerList(List<Ledger> ledgerList) {
        this.ledgerList = ledgerList;
    }

    public List<SubLedger> getSubLedgerList() {
        return subLedgerList;
    }

    public void setSubLedgerList(List<SubLedger> subLedgerList) {
        this.subLedgerList = subLedgerList;
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

    public List<LedgerSubLedgerMapping> getLedgerSubLedgerMappingList() {
        return ledgerSubLedgerMappingList;
    }

    public void setLedgerSubLedgerMappingList(List<LedgerSubLedgerMapping> ledgerSubLedgerMappingList) {
        this.ledgerSubLedgerMappingList = ledgerSubLedgerMappingList;
    }
}
