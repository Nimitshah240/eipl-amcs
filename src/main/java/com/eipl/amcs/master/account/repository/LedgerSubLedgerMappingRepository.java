package com.eipl.amcs.master.account.repository;

import com.eipl.amcs.base.repository.BaseRepository;
import com.eipl.amcs.master.account.model.Ledger;
import com.eipl.amcs.master.account.model.LedgerSubLedgerMapping;
import com.eipl.amcs.master.account.model.SubLedger;
import com.eipl.amcs.master.org.model.Society;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LedgerSubLedgerMappingRepository extends BaseRepository<LedgerSubLedgerMapping, String> {

    @Override
    @EntityGraph(attributePaths = {"ledger", "subLedger", "society"})
    List<LedgerSubLedgerMapping> findAll(Sort sort);

    @EntityGraph(attributePaths = {"ledger", "subLedger", "society"})
    List<LedgerSubLedgerMapping> findBySocietyAndLedger(Society society, Ledger ledger);

    @EntityGraph(attributePaths = {"ledger", "subLedger", "society"})
    List<LedgerSubLedgerMapping> findBySocietyAndSubLedger(Society society, SubLedger subLedger);

    @Override
    Optional<LedgerSubLedgerMapping> findById(String s);
}
