package com.eipl.amcs.master.account.dto;

import com.eipl.amcs.master.account.model.Ledger;
import com.eipl.amcs.master.account.model.VoucherTypeLedgerConfig;
import java.util.List;

public class VoucherTypeMappingDto {
    private List<VoucherTypeLedgerConfig> listMapping;
    private List<Ledger> ledgerList;

    public VoucherTypeMappingDto() {
    }

    public VoucherTypeMappingDto(List<VoucherTypeLedgerConfig> listMapping, List<Ledger> ledgerList) {
        this.listMapping = listMapping;
        this.ledgerList = ledgerList;
    }

    public List<VoucherTypeLedgerConfig> getListMapping() {
        return listMapping;
    }

    public void setListMapping(List<VoucherTypeLedgerConfig> listMapping) {
        this.listMapping = listMapping;
    }

    public List<Ledger> getLedgerList() {
        return ledgerList;
    }

    public void setLedgerList(List<Ledger> ledgerList) {
        this.ledgerList = ledgerList;
    }
}
