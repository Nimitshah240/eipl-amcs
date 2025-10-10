package com.eipl.amcs.master.account.service;

import com.eipl.amcs.master.account.model.SubLedgerLedgerConfig;

import java.util.List;
import java.util.Optional;

public interface SubLedgerLedgerConfigService {
    List<SubLedgerLedgerConfig> findAll();

    List<SubLedgerLedgerConfig> findBySubLedgerType(Integer code);


    String save(List<SubLedgerLedgerConfig> subLedgerLedgerConfig, String subLedgerTypeCode, String identityInfo);

    Optional<SubLedgerLedgerConfig> findById(String ledgerTypeNo);
}
