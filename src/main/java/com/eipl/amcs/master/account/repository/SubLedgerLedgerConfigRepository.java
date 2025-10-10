package com.eipl.amcs.master.account.repository;

import com.eipl.amcs.base.repository.BaseRepository;
import com.eipl.amcs.master.account.model.SubLedgerLedgerConfig;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubLedgerLedgerConfigRepository extends BaseRepository<SubLedgerLedgerConfig, String> {

    @Override
    @EntityGraph(attributePaths = {"ledger", "society"})
    List<SubLedgerLedgerConfig> findAll(Sort sort);

    @EntityGraph(attributePaths = {"ledger", "society"})
    List<SubLedgerLedgerConfig> findBySubLedgerType(Integer type);


    @Override
    Optional<SubLedgerLedgerConfig> findById(String s);
}
