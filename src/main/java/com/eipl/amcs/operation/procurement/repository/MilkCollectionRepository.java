package com.eipl.amcs.operation.procurement.repository;

import com.eipl.amcs.base.repository.BaseRepository;
import com.eipl.amcs.master.global.model.MilkType;
import com.eipl.amcs.master.operation.model.Member;
import com.eipl.amcs.master.org.model.Dock;
import com.eipl.amcs.master.procurement.model.SocietyPaymentCycle;
import com.eipl.amcs.operation.billing.dto.MilkCollectionSummaryData;
import com.eipl.amcs.operation.procurement.model.MilkCollection;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public interface MilkCollectionRepository extends BaseRepository<MilkCollection, String> {

    @Override
    @EntityGraph(attributePaths = {"societyPaymentCycle", "member", "shift", "milkType", "milkQualityType", "society",
            "dock"})
    List<MilkCollection> findAll(Sort sort);

    @EntityGraph(attributePaths = {"societyPaymentCycle", "member", "shift", "milkType", "milkQualityType", "society",
            "dock"})
    List<MilkCollection> findByCollectionDateBetween(LocalDateTime fromDate, LocalDateTime toDate, Sort sort);

    @EntityGraph(attributePaths = {"societyPaymentCycle", "member", "shift", "milkType", "milkQualityType", "society",
            "dock"})
    List<MilkCollection> findByCollectionDate(LocalDateTime date, Sort sort);

    @Override
    @EntityGraph(attributePaths = {"societyPaymentCycle", "member", "shift", "milkType", "milkQualityType", "society",
            "dock"})
    Optional<MilkCollection> findById(String code);

    @Query(nativeQuery = true, value = "SELECT DISTINCT mc.collection_date FROM milk_collection mc WHERE mc.milk_type_code = ?1 "
            + "AND mc.collection_date <= ?2 AND mc.member_code = ?3 ORDER BY mc.collection_date desc limit ?4")
    List<Timestamp> findLastDates(String milktype, LocalDateTime date, String code, int no);

    @Query(nativeQuery = true, value = "SELECT sum(qty) as qty,sum(amount) as amt,count(qty) as cnt from milk_collection where member_code=?1 and " +
            "society_payment_cycle_code = ?2 and milk_type_code=?3")
    Map<String, BigDecimal> findTotals(String code, String no, int milkType);

    @Query(nativeQuery = true, value = "select ROUND(SUM(fat * qty / 100) / SUM(qty) * 100, 2) As fat,ROUND(SUM(snf * qty / 100) / SUM(qty) * 100, 2) As snf,\n"
            + "ROUND(avg(qty),3) AS qty\n" + " from milk_collection where member_code = ?1 \n"
            + " and collection_date between ?2 and ?3")
    Map<String, BigDecimal> findAvgFatAndSnf(String code, LocalDateTime date1, LocalDateTime date2);

    @Query("SELECT new com.eipl.amcs.operation.billing.dto.MilkCollectionSummaryData(" +
            "  m.member, " +
            "  m.milkType, " +
            "  SUM(m.qty), " +
            "  SUM(m.amount)) " +
            "FROM MilkCollection m " +
            "WHERE m.collectionDate >= :startDate " +
            "  AND m.collectionDate < :endDate " +
            "  AND (:milkType IS NULL OR m.milkType.code = :milkType) " +
            "GROUP BY m.member, m.milkType " +
            "ORDER BY SUM(m.qty) DESC"
    )
    List<MilkCollectionSummaryData> findTop10MemberSummaries(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            @Param("milkType") Integer selectedMilkTypeCode, Pageable pageable);

    @EntityGraph(attributePaths = {"societyPaymentCycle", "shift", "milkType", "milkQualityType", "society",
            "dock"})
    List<MilkCollection> findByMemberAndCollectionDateBetweenAndMilkTypeOrderByCollectionDateDesc(Member member, LocalDateTime date1, LocalDateTime date2, MilkType milkType);

    Optional<MilkCollection> findTop1ByDockAndCollectionDateOrderBySampleNoDesc(Dock dock, LocalDateTime dt);

    @EntityGraph(attributePaths = {"societyPaymentCycle", "member", "shift", "milkType", "milkQualityType", "society",
            "dock"})
    List<MilkCollection> findByCollectionDateAndMember(LocalDateTime date, Member member);

    @EntityGraph(attributePaths = {"societyPaymentCycle", "member", "shift", "milkType", "milkQualityType", "society",
            "dock"})
    List<MilkCollection> findByCollectionDateBetweenAndXCol1(LocalDateTime fromDt, LocalDateTime toDt, String xcol1,
                                                             Sort and);

    @EntityGraph(attributePaths = {"societyPaymentCycle", "shift", "milkType", "milkQualityType", "society",
            "dock"})
    Optional<MilkCollection> findByCollectionDateAndMemberAndMilkType(LocalDateTime collectionDate, Member member,
                                                                      MilkType milkType);

    @EntityGraph(attributePaths = {"societyPaymentCycle", "member", "shift", "milkType", "milkQualityType", "society",
            "dock"})
    List<MilkCollection> findByCollectionDateBetweenAndMember(LocalDateTime fromDate, LocalDateTime toDate,
                                                              Member member);

    @EntityGraph(attributePaths = {"societyPaymentCycle", "member", "shift", "milkType", "milkQualityType", "society",
            "dock"})
    List<MilkCollection> findByCollectionDateBetweenAndDock(LocalDateTime fromDt, LocalDateTime toDt, Dock dock);

    @EntityGraph(attributePaths = {"societyPaymentCycle", "member", "shift", "milkType", "milkQualityType", "society",
            "dock"})
    List<MilkCollection> findByMemberAndCollectionDateLessThanEqualOrderByCollectionDateDesc(Member member,
                                                                                             LocalDateTime date,
                                                                                             Pageable ofSize);

    @EntityGraph(attributePaths = {"societyPaymentCycle", "member", "shift", "milkType", "milkQualityType", "society",
            "dock"})
    List<MilkCollection> findByMemberAndSocietyPaymentCycle(Member member, SocietyPaymentCycle cycle);

    @Query(value = "CALL final_amount(:p_member_code, :p_society_payment_cycle_code,:p_from_date,:p_to_date);", nativeQuery = true)
    BigDecimal findTotalCollectionDateBetweenAndMember(@Param("p_member_code") String member_code,
                                                       @Param("p_society_payment_cycle_code") String societyPaymentCycleCode,
                                                       @Param("p_from_date") LocalDateTime fromDate,
                                                       @Param("p_to_date") LocalDateTime toDate

    );

//    @Query("SELECT DISTINCT mc FROM MilkCollection mc " +
//            "JOIN FETCH mc.shift " +
//            "JOIN FETCH mc.milkType " +
//            "WHERE mc.member.code = :code " +
//            "ORDER BY mc.collectionDate DESC")
//    List<MilkCollection> findAllByMemberOrderByCollectionDateDesc(@Param("code") String code);


    @Query("SELECT DISTINCT mc FROM MilkCollection mc " +
            "JOIN FETCH mc.shift " +
            "JOIN FETCH mc.milkType " +
            "WHERE mc.member = :member " +
            "ORDER BY mc.collectionDate DESC")
    List<MilkCollection> findAllByMemberOrderByCollectionDateDesc(@Param("member") Member member);

    @EntityGraph(attributePaths = {"societyPaymentCycle", "member", "shift", "milkType", "milkQualityType", "society",
            "dock"})
    List<MilkCollection> findBySocietyPaymentCycleInOrderByCollectionDateAsc(List<SocietyPaymentCycle> societyPaymentCycleList);

}
