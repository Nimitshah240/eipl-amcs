package com.eipl.amcs.operation.procurement.repository;

import com.eipl.amcs.base.repository.BaseRepository;
import com.eipl.amcs.operation.procurement.model.RejectedMilkCollection;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface RejectedMilkCollectionRepository extends BaseRepository<RejectedMilkCollection, String> {

    @Override
    @EntityGraph(attributePaths = {"shift", "member", "milkType", "dock"})
    List<RejectedMilkCollection> findAll(Sort sort);

    @Query("SELECT r FROM RejectedMilkCollection r WHERE r.date BETWEEN :fromDate AND :toDate")
    @EntityGraph(attributePaths = {"shift", "member", "milkType", "dock"})
    List<RejectedMilkCollection> findAllByDateBetween(
            @Param("fromDate") LocalDateTime fromDate,
            @Param("toDate") LocalDateTime toDate);
}
