package com.eipl.amcs.master.account.repository;

import com.eipl.amcs.base.repository.BaseRepository;
import com.eipl.amcs.master.account.model.LedgerMappingProductGroup;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LedgerMappingProductGroupRepository extends BaseRepository<LedgerMappingProductGroup, String> {

    @Override
    @EntityGraph(attributePaths = {"ledgerSaleCode", "ledgerPurchaseCode", "society", "productGroup"})
    List<LedgerMappingProductGroup> findAll(Sort sort);


    @Override
    Optional<LedgerMappingProductGroup> findById(String s);
}
