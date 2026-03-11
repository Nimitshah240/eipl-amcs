package com.eipl.amcs.setting.repository;

import com.eipl.amcs.base.repository.BaseRepository;
import com.eipl.amcs.setting.model.MilkCollectionAccountPosting;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface MilkCollectionAccountPostingRepository extends BaseRepository<MilkCollectionAccountPosting, Long> {


    @Query("SELECT m FROM MilkCollectionAccountPosting m " +
            "WHERE (m.fromDate < :targetToDate OR (m.fromDate = :targetToDate AND m.fromShift <= :targetToShift)) " +
            "AND (m.toDate > :targetFromDate OR (m.toDate = :targetFromDate AND m.toShift >= :targetFromShift))")
    List<MilkCollectionAccountPosting> findOverlappingPostings(
            @Param("targetFromDate") LocalDate targetFromDate,
            @Param("targetFromShift") Integer targetFromShift,
            @Param("targetToDate") LocalDate targetToDate,
            @Param("targetToShift") Integer targetToShift);

    List<MilkCollectionAccountPosting> findByFromDateGreaterThanEqualAndToDateLessThanEqual(
            LocalDate fromDate,
            LocalDate toDate
    );
}
