package com.eipl.amcs.master.account.repository;

import com.eipl.amcs.base.repository.BaseRepository;
import com.eipl.amcs.master.account.model.LedgerGroup;
import com.eipl.amcs.master.account.model.LedgerType;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LedgerGroupRepository extends BaseRepository<LedgerGroup, String> {

    @Override
    @EntityGraph(attributePaths = {"ledgerType"})
    List<LedgerGroup> findAll(Sort sort);

    @EntityGraph(attributePaths = {"ledgerType"})
    List<LedgerGroup> findAllByActive(Boolean active, Sort sort);

    @EntityGraph(attributePaths = {"ledgerType"})
    List<LedgerGroup> findByLedgerType(LedgerType ledgerType);

    @Override
    @EntityGraph(attributePaths = {"ledgerType"})
    Optional<LedgerGroup> findById(String id);


    @EntityGraph(attributePaths = {"ledgerType"})
    List<LedgerGroup> findByLedgerTypeAndActive(LedgerType ledgerType, boolean b);

    @EntityGraph(attributePaths = {"ledgerType"})
    List<LedgerGroup> findByIsCash(boolean b);
}
