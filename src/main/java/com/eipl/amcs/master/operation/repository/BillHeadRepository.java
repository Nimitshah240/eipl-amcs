package com.eipl.amcs.master.operation.repository;

import com.eipl.amcs.base.repository.BaseRepository;
import com.eipl.amcs.master.operation.model.BillHead;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BillHeadRepository extends BaseRepository<BillHead, String> {

    @Override
    @EntityGraph(attributePaths = {"society", "union"})
    List<BillHead> findAll();

    @Override
    @EntityGraph(attributePaths = {"society", "union"})
    Optional<BillHead> findById(String id);
}
