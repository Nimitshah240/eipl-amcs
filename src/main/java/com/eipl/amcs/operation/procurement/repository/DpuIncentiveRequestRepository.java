package com.eipl.amcs.operation.procurement.repository;

import com.eipl.amcs.base.repository.BaseRepository;
import com.eipl.amcs.operation.procurement.model.DpuIncentiveRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface DpuIncentiveRequestRepository extends BaseRepository<DpuIncentiveRequest, String> {

    @Override
    @EntityGraph(attributePaths = {"society"})
    List<DpuIncentiveRequest> findAll(Sort sort);


    @Override
    @EntityGraph(attributePaths = {"society"})
    Optional<DpuIncentiveRequest> findById(String id);

    @EntityGraph(attributePaths = {"society"})
    List<DpuIncentiveRequest> findByFromDateLessThanEqualAndToDateGreaterThanEqual(LocalDate fd, LocalDate td);

    @EntityGraph(attributePaths = {"society"})
    Optional<DpuIncentiveRequest> findTop1ByOrderByCreatedAtDesc();

}
