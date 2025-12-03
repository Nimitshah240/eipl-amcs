package com.eipl.amcs.master.account.service;

import com.eipl.amcs.master.account.model.LedgerType;

import java.util.List;
import java.util.Optional;

public interface LedgerTypeService {
    List<LedgerType> findAll();


    LedgerType save(LedgerType ledgerType, String identityInfo);

    LedgerType update(LedgerType ledgerType, String identityInfo);


    Optional<LedgerType> findById(String ledgerTypeNo);

    void delete(String ledgerTypeNo, String identityInfo);

    void delete(LedgerType ledgerType, String identityInfo);


}
