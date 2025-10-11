package com.eipl.amcs.master.account.dto;

import com.eipl.amcs.master.account.model.Ledger;
import com.eipl.amcs.master.account.model.VoucherTypeLedgerConfig;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class VoucherTypeMappingDto {
    private List<VoucherTypeLedgerConfig> listMapping;
    private List<Ledger> ledgerList;
}
