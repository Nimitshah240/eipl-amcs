package com.eipl.amcs.master.account.service;

import com.eipl.amcs.master.account.model.LedgerMappingProductGroup;

import java.util.List;
import java.util.Optional;

public interface LedgerMappingProductGroupService {
	List<LedgerMappingProductGroup> findAll();

	String save(List<LedgerMappingProductGroup> ledgerMappingProductGroup, String identityInfo);

	Optional<LedgerMappingProductGroup> findById(String ledgerTypeNo);
}
