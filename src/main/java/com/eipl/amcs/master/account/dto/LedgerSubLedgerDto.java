package com.eipl.amcs.master.account.dto;

import com.eipl.amcs.master.account.model.Ledger;
import com.eipl.amcs.master.account.model.LedgerSubLedgerMapping;
import com.eipl.amcs.master.account.model.SubLedger;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
public class LedgerSubLedgerDto {
    private Ledger ledger;
    private SubLedger subLedger;
    private List<Ledger> ledgerList;
    private List<SubLedger> subLedgerList;
    private List<LedgerSubLedgerMapping> ledgerSubLedgerMappingList;
}
