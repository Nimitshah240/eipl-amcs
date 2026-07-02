package com.eipl.amcs.operation.billing.repository;

import com.eipl.amcs.base.repository.BaseRepository;
import com.eipl.amcs.master.operation.model.Member;
import com.eipl.amcs.master.procurement.model.SocietyPaymentCycle;
import com.eipl.amcs.operation.billing.model.MemberBill;
import com.eipl.amcs.report.dto.PaymentForBankProjection;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemberBillRepository extends BaseRepository<MemberBill, String> {

    @EntityGraph(attributePaths = {"society", "union", "member", "paymentCycle"})
    List<MemberBill> findByPaymentCycle(SocietyPaymentCycle paymentCycle);

    @EntityGraph(attributePaths = {"society", "union", "member", "paymentCycle"})
    MemberBill findByMember(Member member);

    @EntityGraph(attributePaths = {"society", "union", "member", "paymentCycle"})
    MemberBill findByMemberAndPaymentCycle(Member member, SocietyPaymentCycle paymentCycle);

    @Query(value = "CALL rpt_payment_register_bank_excel(:p_society_code,:p_society_payment_cycle_code,:p_payment_mode,:p_bank_code,:p_locale);", nativeQuery = true)
    List<PaymentForBankProjection> findPaymentRegisterReportExcel(@Param("p_society_code") String societyCode,
                                                        @Param("p_society_payment_cycle_code") String societyPaymentCycleCode,
                                                        @Param("p_payment_mode") Integer paymentMode,
                                                        @Param("p_bank_code") String bankCode,
                                                        @Param("p_locale") String locale
    );
}
