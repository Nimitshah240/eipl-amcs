package com.eipl.amcs.master.account.service;

import com.eipl.amcs.master.account.model.LedgerSubLedgerMapping;
import com.eipl.amcs.master.account.model.SubLedger;

import java.util.List;
import java.util.Optional;

public interface SubLedgerService {
	 List<SubLedger> findAll();


	SubLedger save(SubLedger subLedger, String identityInfo);

	SubLedger update(SubLedger subLedger, String identityInfo);


	Optional<SubLedger> findById(String subLedgerTypeNo);

	void delete(String subLedgerNo, String identityInfo);

	void delete(SubLedger subLedger, String identityInfo);


	List<LedgerSubLedgerMapping> fetchMapping(String societyCode, String ledgerCode, String subLedgerCode);
}
