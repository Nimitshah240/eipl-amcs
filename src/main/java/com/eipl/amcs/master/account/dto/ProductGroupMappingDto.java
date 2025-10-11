package com.eipl.amcs.master.account.dto;

import com.eipl.amcs.master.account.model.Ledger;
import com.eipl.amcs.master.account.model.LedgerMappingProductGroup;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
public class ProductGroupMappingDto {
    private List<LedgerMappingProductGroup> listMapping;
    private List<Ledger> ledgerList;
}
