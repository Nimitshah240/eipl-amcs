package com.eipl.amcs.master.account.dto;

import java.util.List;
import com.eipl.amcs.master.account.model.Ledger;
import com.eipl.amcs.master.account.model.LedgerMappingTaxDetail;

public class TaxDetailMappingDto {
    private List<LedgerMappingTaxDetail> listMapping;
    private List<Ledger> ledgerList;

    public TaxDetailMappingDto() {
    }

    public TaxDetailMappingDto(List<LedgerMappingTaxDetail> listMapping, List<Ledger> ledgerList) {
        this.listMapping = listMapping;
        this.ledgerList = ledgerList;
    }

    public List<LedgerMappingTaxDetail> getListMapping() {
        return listMapping;
    }

    public void setListMapping(List<LedgerMappingTaxDetail> listMapping) {
        this.listMapping = listMapping;
    }

    public List<Ledger> getLedgerList() {
        return ledgerList;
    }

    public void setLedgerList(List<Ledger> ledgerList) {
        this.ledgerList = ledgerList;
    }
}
