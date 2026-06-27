package com.eipl.amcs.operation.billing.repository;

import com.eipl.amcs.base.repository.BaseRepository;
import com.eipl.amcs.operation.billing.model.MemberBill;
import com.eipl.amcs.operation.billing.model.MemberBillTransaction;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Repository
public interface MemberBillTransactionRepository extends BaseRepository<MemberBillTransaction, String> {

    @EntityGraph(attributePaths = {"memberBill", "billHead"})
    List<MemberBillTransaction> findByMemberBill(MemberBill memberBill);

    @EntityGraph(attributePaths = {"memberBill", "billHead"})
    List<MemberBillTransaction> findByMemberBillAndBillHead_CodeNot(MemberBill memberBill, String billHeadCode);

    @Query(value = "CALL process_member_billing(:p_society_payment_cycle_code, :p_prev_society_payment_cycle_code,:p_from_date,:p_to_date,:p_processed,:p_society_code,:p_user_code, :p_deduction_from_date, :p_deduction_to_date);", nativeQuery = true)
    List<Map<String, Object>> findBillTransaction(@Param("p_society_payment_cycle_code") String societyPaymentCycleCode,
                                                  @Param("p_prev_society_payment_cycle_code") String prevSocietyPaymentCycleCode,
                                                  @Param("p_from_date") LocalDateTime fromDate, @Param("p_to_date") LocalDateTime toDate,
                                                  @Param("p_processed") Integer processed, @Param("p_society_code") String societyCode,
                                                  @Param("p_user_code") String userCode,
                                                  @Param("p_deduction_from_date") LocalDate deductionFromDate,
                                                  @Param("p_deduction_to_date") LocalDate deductionToDate);
}
