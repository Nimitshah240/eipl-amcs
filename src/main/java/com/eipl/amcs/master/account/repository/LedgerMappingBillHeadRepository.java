package com.eipl.amcs.master.account.repository;

import com.eipl.amcs.base.repository.BaseRepository;
import com.eipl.amcs.master.account.model.LedgerMappingBillHead;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LedgerMappingBillHeadRepository extends BaseRepository<LedgerMappingBillHead, String> {

    @Override
    @EntityGraph(attributePaths = {"ledger", "billHead", "society", "billCriteria"})
    List<LedgerMappingBillHead> findAll(Sort sort);

    @Override
    Optional<LedgerMappingBillHead> findById(String s);
}
