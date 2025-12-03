package com.eipl.amcs.master.account.service;

import com.eipl.amcs.master.account.model.LedgerGroup;

import java.util.List;
import java.util.Optional;

public interface LedgerGroupService {
    List<LedgerGroup> findAll();

    LedgerGroup save(LedgerGroup ledgerGroup, String identityInfo);

    LedgerGroup update(LedgerGroup ledgerGroup, String identityInfo);


    Optional<LedgerGroup> findById(String ledgerTypeNo);

    void delete(String ledgerTypeNo, String identityInfo);

    void delete(LedgerGroup ledgerGroup, String identityInfo);

    List<LedgerGroup> findByLedgerType(String code);

}
