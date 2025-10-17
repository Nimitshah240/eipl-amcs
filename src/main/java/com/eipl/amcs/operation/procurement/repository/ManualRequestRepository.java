package com.eipl.amcs.operation.procurement.repository;

import com.eipl.amcs.base.repository.BaseRepository;
import com.eipl.amcs.operation.procurement.model.ManualRequest;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface ManualRequestRepository extends BaseRepository<ManualRequest, Long> {
    @Override
    @EntityGraph(attributePaths = {"fromShift", "toShift", "society"})
    List<ManualRequest> findAll();


    @Override
    @EntityGraph(attributePaths = {"fromShift", "toShift", "society"})
    Optional<ManualRequest> findById(Long id);


}