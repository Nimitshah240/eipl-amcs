package com.eipl.amcs.master.account.service;

import com.eipl.amcs.master.account.model.LedgerOpeningBalance;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface LedgerOpeningBalanceService {
    List<LedgerOpeningBalance> findAll();

    LedgerOpeningBalance save(LedgerOpeningBalance ledgerOpeningBalance, String identityInfo);

    LedgerOpeningBalance update(LedgerOpeningBalance ledgerOpeningBalance, String identityInfo);

    Optional<LedgerOpeningBalance> findById(String ledgerOpeningBalance);

    void delete(String ledgerOpeningBalance, String identityInfo);

    void delete(LedgerOpeningBalance ledgerOpeningBalance, String identityInfo);

    List<LedgerOpeningBalance> importLedgerBalance(List<LedgerOpeningBalance> dtoList, String header);

    BigDecimal getLedgerOpeningBalanceOfTypeCash(LocalDate fromDate, LocalDate toDate);
}
