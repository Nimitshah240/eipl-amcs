package com.eipl.amcs.operation.inventory.repository;

import com.eipl.amcs.base.repository.BaseRepository;
import com.eipl.amcs.operation.inventory.model.ProductRequisition;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ProductRequisitionRepository extends BaseRepository<ProductRequisition, String> {

    @EntityGraph(attributePaths = {"society"})
    List<ProductRequisition> findByRequisitionDateBetween(LocalDateTime fromDate, LocalDateTime toDate);


    List<ProductRequisition> findByRequisitionDateBetweenOrderByRequisitionDateDesc(LocalDateTime atStartOfDay, LocalDateTime atTime);
}
