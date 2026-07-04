package com.eipl.amcs.operation.billing.repository;

import com.eipl.amcs.base.repository.BaseRepository;
import com.eipl.amcs.master.procurement.model.SocietyPaymentCycle;
import com.eipl.amcs.operation.billing.model.MemberBillSummary;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface MemberBillSummaryRepository extends BaseRepository<MemberBillSummary, String> {

    @EntityGraph(attributePaths = {"paymentCycle"})
    List<MemberBillSummary> findByPaymentCycleIn(List<SocietyPaymentCycle> paymentCycleList);

    @EntityGraph(attributePaths = {"paymentCycle"})
    Optional<MemberBillSummary> findByPaymentCycle(SocietyPaymentCycle paymentCycle);

    @Query(value = "select * from member_bill_summary order by (cast(society_payment_cycle_code as SIGNED INTEGER)) desc;", nativeQuery = true)
    List<MemberBillSummary> findAllByOrderByPaymentCycleDesc();

    MemberBillSummary findTop1ByDeductionFromDateLessThanEqualAndDeductionToDateGreaterThanEqual(LocalDate fromDate, LocalDate toDate);

    @Query("SELECT COUNT(m) FROM MemberBillSummary m " +
            "WHERE m.deductionFromDate <= :toDate " +
            "AND m.deductionToDate >= :fromDate " +
            "AND m.paymentCycle.code != :paymentCycleCode"
    )
    long countByDeductionDateOverlap(
            @Param("fromDate") LocalDate deductionFromDate,
            @Param("toDate") LocalDate deductionToDate,
            @Param("paymentCycleCode") String paymentCycleCode
    );

    List<MemberBillSummary> findByStatusNotAndPaymentCycle_Code(short status, String previousPaymentCycle);
}
