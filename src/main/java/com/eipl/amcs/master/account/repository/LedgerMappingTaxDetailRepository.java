package com.eipl.amcs.master.account.repository;

import com.eipl.amcs.base.repository.BaseRepository;
import com.eipl.amcs.master.account.model.LedgerMappingTaxDetail;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LedgerMappingTaxDetailRepository extends BaseRepository<LedgerMappingTaxDetail, String> {

    @Override
    @EntityGraph(attributePaths = {"taxDetail", "ledger", "society"})
    List<LedgerMappingTaxDetail> findAll(Sort sort);

    @Override
    Optional<LedgerMappingTaxDetail> findById(String s);
}
