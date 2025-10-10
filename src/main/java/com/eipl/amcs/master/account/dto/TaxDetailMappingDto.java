package com.eipl.amcs.master.account.dto;

import com.eipl.amcs.master.account.model.Ledger;
import com.eipl.amcs.master.account.model.LedgerMappingTaxDetail;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Setter
@Getter
@AllArgsConstructor
public class TaxDetailMappingDto {
    private List<LedgerMappingTaxDetail> listMapping;
    private List<Ledger> ledgerList;
}
