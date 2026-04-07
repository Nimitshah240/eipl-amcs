package com.eipl.amcs.setting.repository;

import com.eipl.amcs.base.repository.BaseRepository;
import com.eipl.amcs.master.global.model.Shift;
import com.eipl.amcs.setting.model.AccountPosting;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AccountPostingRepository extends BaseRepository<AccountPosting, String> {


    @Query("SELECT m FROM AccountPosting m " +
            "WHERE (m.fromDate < :targetToDate OR (m.fromDate = :targetToDate AND m.fromShift <= :targetToShift)) " +
            "AND (m.toDate > :targetFromDate OR (m.toDate = :targetFromDate AND m.toShift >= :targetFromShift))" +
            "AND m.eventType = :eventType AND (:code IS NULL OR code != :code)")
    List<AccountPosting> findOverlappingPostings(
            @Param("targetFromDate") LocalDate targetFromDate,
            @Param("targetFromShift") Shift targetFromShift,
            @Param("targetToDate") LocalDate targetToDate,
            @Param("targetToShift") Shift targetToShift,
            @Param("eventType") Integer eventType,
            @Param("code") String code
    );

    @EntityGraph(attributePaths = {"fromShift", "toShift"})
    List<AccountPosting> findByFromDateGreaterThanEqualAndToDateLessThanEqual(
            LocalDate fromDate,
            LocalDate toDate
    );

    @Query("SELECT count(*) FROM AccountPosting ap WHERE :eventType=ap.eventType AND :status= ap.status AND (:date > ap.fromDate OR (:date = ap.fromDate AND :shift >= ap.fromShift.code)) AND (:date < ap.toDate OR (:date = ap.toDate AND :shift <= ap.toShift.code))")
    Integer findValidRange(@Param("date") LocalDate date, @Param("shift") Integer shift, @Param("status") short status, @Param("eventType") int eventType);

    @Query(value = "SELECT CASE WHEN COUNT(*) > 0 THEN 'true' ELSE 'false' END " +
            "FROM account_posting " +
            "WHERE status = :status " +
            "AND event_type = :eventType " +
            "AND (:code IS NULL OR code != :code)",
            nativeQuery = true)
    boolean existsByStatusAndEventTypeAndCodeNotExist(@Param("status") short status,
                                                       @Param("eventType") int eventType,
                                                       @Param("code") String code);
}
