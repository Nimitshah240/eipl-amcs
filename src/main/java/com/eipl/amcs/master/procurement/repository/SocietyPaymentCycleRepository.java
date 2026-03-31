package com.eipl.amcs.master.procurement.repository;

import com.eipl.amcs.base.repository.BaseRepository;
import com.eipl.amcs.master.procurement.model.SocietyPaymentCycle;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface SocietyPaymentCycleRepository extends BaseRepository<SocietyPaymentCycle, String>, PagingAndSortingRepository<SocietyPaymentCycle, String> {

    @Override
    @EntityGraph(attributePaths = {"society", "fromShift", "toShift"})
    List<SocietyPaymentCycle> findAll(Sort sort);


    @Query(value = "SELECT spc FROM SocietyPaymentCycle spc WHERE spc.society.code = ?1 AND spc.code != ?2 AND" + " (" + "(" + "(" + "spc.fromDate BETWEEN ?3 AND ?4" + ")" + " OR " + "(" + "spc.toDate BETWEEN ?3 AND ?4)" + ") OR" + " (" + "(" + "?3 >= spc.fromDate and ?3 <= spc.toDate" + ") " + "OR " + "(?4 >= spc.fromDate and ?4 <= spc.toDate)" + ")" + ")")
    List<SocietyPaymentCycle> checkDateRangeConflict(String str1, String str2, LocalDateTime fromDate, LocalDateTime toDate);

    @Query(value = "SELECT pc FROM SocietyPaymentCycle pc LEFT JOIN FETCH pc.society LEFT JOIN FETCH pc.fromShift " + "LEFT JOIN FETCH pc.toShift WHERE pc.fromDate >= ?1 AND pc.toDate <= ?1")
    SocietyPaymentCycle fetchCurrentPaymentCycle(LocalDateTime date, String code);

    @Query(value = "SELECT pc FROM SocietyPaymentCycle pc LEFT JOIN FETCH pc.society LEFT JOIN FETCH pc.fromShift " + "LEFT JOIN FETCH pc.toShift WHERE pc.fromDate <= ?1 AND pc.toDate >= ?1")
    SocietyPaymentCycle findSocietyPaymentCycle(LocalDateTime date);

    SocietyPaymentCycle findTop1ByFromDateLessThanEqualAndToDateGreaterThanEqual(LocalDateTime date, LocalDateTime date1);

    @EntityGraph(attributePaths = {"society", "fromShift", "toShift"})
    List<SocietyPaymentCycle> findByFromDateGreaterThanEqualOrderByFromDate(LocalDateTime fromDate, Pageable page);

    @EntityGraph(attributePaths = {"society", "fromShift", "toShift"})
    List<SocietyPaymentCycle> findByFromDateBetween(LocalDateTime date1, LocalDateTime date2);


    @Override
    @EntityGraph(attributePaths = {"society", "fromShift", "toShift"})
    Optional<SocietyPaymentCycle> findById(String id);

    @Query(value = "SELECT pc FROM SocietyPaymentCycle pc WHERE pc.fromDate >= :from AND pc.toDate <= : to AND :from = (SELECT MIN(pc2.fromDate) FROM SocietyPaymentCycle pc2 WHERE pc2.fromDate >= :from AND pc2.toDate <= :to ) AND :to = (SELECT MAX(pc3.toDate) FROM SocietyPaymentCycle pc3 WHERE pc3.fromDate >= :from AND pc3.toDate <= :to ) ORDER BY pc.fromDate")
    @EntityGraph(attributePaths = {"society", "fromShift", "toShift"})
    List<SocietyPaymentCycle> findCycles(@Param("from") LocalDateTime from, @Param("to") LocalDateTime to);
}
