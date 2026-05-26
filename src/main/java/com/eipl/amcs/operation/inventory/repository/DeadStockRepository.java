package com.eipl.amcs.operation.inventory.repository;

import com.eipl.amcs.base.repository.BaseRepository;
import com.eipl.amcs.operation.inventory.model.DeadStock;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeadStockRepository extends BaseRepository<DeadStock, String> {

    @Override
    @EntityGraph(attributePaths = {"ledger"})
    List<DeadStock> findAll(Sort sort);
}
