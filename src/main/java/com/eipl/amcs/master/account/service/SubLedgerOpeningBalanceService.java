package com.eipl.amcs.master.account.service;

import com.eipl.amcs.master.account.model.SubLedgerOpeningBalance;

import java.util.List;
import java.util.Optional;

public interface SubLedgerOpeningBalanceService {
    List<SubLedgerOpeningBalance> findAll();

    SubLedgerOpeningBalance save(SubLedgerOpeningBalance ledgerOpeningBalance, String identityInfo);

    SubLedgerOpeningBalance update(SubLedgerOpeningBalance ledgerOpeningBalance, String identityInfo);

    Optional<SubLedgerOpeningBalance> findById(String ledgerOpeningBalance);

    void delete(String ledgerOpeningBalance, String identityInfo);

    void delete(SubLedgerOpeningBalance ledgerOpeningBalance, String identityInfo);
    List<SubLedgerOpeningBalance> importSubLedgerBalance(List<SubLedgerOpeningBalance> dtoList, String header);
}
