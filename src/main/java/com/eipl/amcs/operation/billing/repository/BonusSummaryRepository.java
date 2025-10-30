package com.eipl.amcs.operation.billing.repository;

import com.eipl.amcs.base.repository.BaseRepository;
import com.eipl.amcs.operation.billing.model.BonusSummary;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BonusSummaryRepository extends BaseRepository<BonusSummary, String> {

    @EntityGraph(attributePaths = {"society", "union"})
    List<BonusSummary> findAll();

    @Override
    @EntityGraph(attributePaths = {"society", "union"})
    Optional<BonusSummary> findById(String id);

}
