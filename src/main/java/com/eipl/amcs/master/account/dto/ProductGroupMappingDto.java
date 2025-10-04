package com.eipl.amcs.master.account.dto;

import java.util.List;
import com.eipl.amcs.master.account.model.LedgerMappingProductGroup;
import com.eipl.amcs.master.account.model.Ledger;

public class ProductGroupMappingDto {
    private List<LedgerMappingProductGroup> listMapping;
    private List<Ledger> ledgerList;

    public ProductGroupMappingDto() {
    }

    public ProductGroupMappingDto(List<LedgerMappingProductGroup> listMapping, List<Ledger> ledgerList) {
        this.listMapping = listMapping;
        this.ledgerList = ledgerList;
    }

    public List<LedgerMappingProductGroup> getListMapping() {
        return listMapping;
    }

    public void setListMapping(List<LedgerMappingProductGroup> listMapping) {
        this.listMapping = listMapping;
    }

    public List<Ledger> getLedgerList() {
        return ledgerList;
    }

    public void setLedgerList(List<Ledger> ledgerList) {
        this.ledgerList = ledgerList;
    }
}
