package com.eipl.amcs.master.account.repository;

import com.eipl.amcs.base.repository.BaseRepository;
import com.eipl.amcs.master.account.model.SubLedgerOpeningBalance;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubLedgerOpeningBalanceRepository extends BaseRepository<SubLedgerOpeningBalance, String> {

    @Override
    @EntityGraph(attributePaths = {"society", "ledger", "subLedger"})
    List<SubLedgerOpeningBalance> findAll(Sort sort);

    @Override
    @EntityGraph(attributePaths = {"society", "ledger", "subLedger"})
    Optional<SubLedgerOpeningBalance> findById(String s);
}
