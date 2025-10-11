package com.eipl.amcs.master.account.service;

import com.eipl.amcs.master.account.model.LedgerMappingBillHead;

import java.util.List;
import java.util.Optional;

public interface LedgerMappingBillHeadService {
    List<LedgerMappingBillHead> findAll();

    String save(List<LedgerMappingBillHead> ledgerMappingBillHead, String identityInfo);

    Optional<LedgerMappingBillHead> findById(String ledgerTypeNo);
}
