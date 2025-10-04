package com.eipl.amcs.master.operation.repository;

import com.eipl.amcs.base.repository.BaseRepository;
import com.eipl.amcs.master.operation.model.BillCriteria;
import org.springframework.data.jpa.repository.EntityGraph;

import java.util.List;
import java.util.Optional;

public interface BillCriteriaRepository extends BaseRepository<BillCriteria, String> {

    @Override
    @EntityGraph(attributePaths = {"society", "union", "formulaCode", "billHeadCode"})
    List<BillCriteria> findAll();

    @Override
    @EntityGraph(attributePaths = {"society", "union", "formulaCode", "billHeadCode"})
    Optional<BillCriteria> findById(String id);

}
