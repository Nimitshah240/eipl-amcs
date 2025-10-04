package com.eipl.amcs.operation.billing.repository;

import com.eipl.amcs.base.repository.BaseRepository;
import com.eipl.amcs.master.procurement.model.SocietyPaymentCycle;
import com.eipl.amcs.operation.billing.model.MemberBillSummary;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

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

}
