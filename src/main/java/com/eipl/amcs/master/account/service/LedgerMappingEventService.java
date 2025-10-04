package com.eipl.amcs.master.account.service;

import com.eipl.amcs.master.account.model.LedgerMappingEvent;

import java.util.List;
import java.util.Optional;

public interface LedgerMappingEventService {
	List<LedgerMappingEvent> findAll();

	String save(List<LedgerMappingEvent> ledgerMappingEvent, String identityInfo);

	Optional<LedgerMappingEvent> findById(String ledgerTypeNo);
}
