package com.eipl.amcs.master.account.repository;

import com.eipl.amcs.base.repository.BaseRepository;
import com.eipl.amcs.master.account.model.LedgerOpeningBalance;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LedgerOpeningBalanceRepository extends BaseRepository<LedgerOpeningBalance, String> {

	@Override
	@EntityGraph(attributePaths = {"society","ledger"})
	List<LedgerOpeningBalance> findAll(Sort sort);

	@Override
	@EntityGraph(attributePaths = {"society","ledger"})
	Optional<LedgerOpeningBalance> findById(String s);
}
