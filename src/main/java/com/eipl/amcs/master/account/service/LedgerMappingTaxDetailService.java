package com.eipl.amcs.master.account.service;

import com.eipl.amcs.master.account.model.LedgerMappingTaxDetail;

import java.util.List;
import java.util.Optional;

public interface LedgerMappingTaxDetailService {
    List<LedgerMappingTaxDetail> findAll();

    String save(List<LedgerMappingTaxDetail> ledgerMappingTaxDetail, String identityInfo);

    Optional<LedgerMappingTaxDetail> findById(String ledgerTypeNo);
}
