package com.eipl.amcs.master.account.service;

import com.eipl.amcs.master.account.model.Ledger;
import com.eipl.amcs.master.account.model.LedgerSubLedgerMapping;

import java.util.List;
import java.util.Optional;

public interface LedgerService {
    List<Ledger> findAll();

    Ledger save(Ledger ledger, String identityInfo);

    Ledger update(Ledger ledger, String identityInfo);

    Optional<Ledger> findById(String ledgerTypeNo);

//	Optional<Ledger> findByLedgerType(String ledgerType);


    void delete(String ledgerNo, String identityInfo);

    void delete(Ledger ledger, String identityInfo);

    LedgerSubLedgerMapping save(List<LedgerSubLedgerMapping> ledgerSubLedgerMappingList, String identityInfo);

    List<LedgerSubLedgerMapping> fetchMapping(String societyCode, String ledgerCode, String subLedgerCode);

    List<Ledger> findAllByIsActive();
}
