package com.eipl.amcs.master.account.service;

import com.eipl.amcs.master.account.model.VoucherTypeLedgerConfig;

import java.util.List;
import java.util.Optional;

public interface VoucherTypeLedgerConfigService {
    List<VoucherTypeLedgerConfig> findAll();

    String save(List<VoucherTypeLedgerConfig> ledgerMappingProductGroup, String identityInfo);

    Optional<VoucherTypeLedgerConfig> findById(String ledgerTypeNo);
}
