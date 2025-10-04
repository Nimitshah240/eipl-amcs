package com.eipl.amcs.master.account.repository;

import com.eipl.amcs.base.repository.BaseRepository;
import com.eipl.amcs.master.account.model.LedgerMappingEvent;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LedgerMappingEventRepository extends BaseRepository<LedgerMappingEvent, String> {

	@Override
	@EntityGraph(attributePaths = {  "society","voucherType","creditLedger","debitLedger","events" })
	List<LedgerMappingEvent> findAll(Sort sort);

	@Override
	@EntityGraph(attributePaths = {  "society","voucherType","creditLedger","debitLedger","events" })
	Optional<LedgerMappingEvent> findById(String code);

	@EntityGraph(attributePaths = {  "society","voucherType","creditLedger","debitLedger","events" })
	List<LedgerMappingEvent> findByEventcode(int eventCode);

}
