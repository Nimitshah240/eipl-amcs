package com.eipl.amcs.master.operation.repository;

import com.eipl.amcs.base.repository.BaseRepository;
import com.eipl.amcs.master.operation.model.Formula;
import org.springframework.data.jpa.repository.EntityGraph;

import java.util.List;

public interface FormulaRepository extends BaseRepository<Formula, String> {

    @Override
    @EntityGraph(attributePaths = {"union"})
    List<Formula> findAll();
}
