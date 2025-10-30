package com.eipl.amcs.operation.billing.repository;

import com.eipl.amcs.base.repository.BaseRepository;
import com.eipl.amcs.master.operation.model.Member;
import com.eipl.amcs.operation.billing.model.Bonus;
import com.eipl.amcs.operation.billing.model.BonusSummary;
import com.eipl.amcs.report.dto.BonusRegister;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public interface BonusRepository extends BaseRepository<Bonus, String> {

    @Override
    @EntityGraph(attributePaths = {"member", "bonusSummary", "society", "union"})
    Optional<Bonus> findById(String id);

    @EntityGraph(attributePaths = {"member", "bonusSummary", "society", "union"})
    Bonus findByMember(Member member);

    @EntityGraph(attributePaths = {"member", "society", "union"})
    List<Bonus> findByBonusSummary(BonusSummary bonusSummary);


    @Query(value = "SELECT sum(qty) as qty,sum(amount)as amt,member_code FROM milk_collection where collection_date between :fromDate" + " and :toDate and milk_type_code=:milk_type_code group by member_code ", nativeQuery = true)
    List<Map<String, Object>> loadData(@Param("fromDate") LocalDateTime d1, @Param("toDate") LocalDateTime d2, @Param("milk_type_code") Integer milkType);

    @Query(value = "select sum(milk_qty) as qty,sum(milk_amount) as amt,sum(bonus_amount) as bonus,member_code,sum(b.x_col1) as kapaat from bonus b join bonus_summary bs on b.bonus_Summary_code=bs.code where member_code=:member_code and from_date >=:fromDate" + " and to_Date<= :toDate", nativeQuery = true)
    Map<String, Object> loadDataBonus(@Param("fromDate") LocalDate d1, @Param("toDate") LocalDate d2, @Param("member_code") String memberCode);

    @Query(value = "select sum(milk_qty) as qty,sum(milk_amount) as amt,sum(bonus_amount) as bonus, member_code,sum(b.x_col1) as kapaat  from bonus b join bonus_summary bs on b.bonus_Summary_code=bs.code  where  from_date >=:fromDate" + " and to_Date<= :toDate group by member_code", nativeQuery = true)
    List<Map<String, Object>> loadDataBonusSummary(@Param("fromDate") LocalDate d1, @Param("toDate") LocalDate d2);


    @Query(value = "SELECT sum(qty) as qty,sum(amount)as amt,member_code FROM milk_collection where collection_date between :fromDate" + " and :toDate group by member_code ", nativeQuery = true)
    List<Map<String, Object>> loadData(@Param("fromDate") LocalDateTime d1, @Param("toDate") LocalDateTime d2);

    @Query(value = "CALL rpt_bonus_register(:p_society_code,:p_bonus_summary_code);", nativeQuery = true)
    List<BonusRegister> findBonus(@Param("p_society_code") String societyCode, @Param("p_bonus_summary_code") String bonusSummaryCode);

    @Query(value = "CALL rpt_bonus_for_all(:p_society_code,:p_from_date,:p_to_date,:p_member_code,:p_locale,:p_payment_mode,:p_bank_code,:p_bonus_type,:p_milk_type_code);", nativeQuery = true)
    List<Map<String, Object>> findAllBonus(@Param("p_society_code") String societyCode, @Param("p_from_date") LocalDate fromDate,
                                           @Param("p_to_date") LocalDate toDate, @Param("p_member_code") String memberCode,
                                           @Param("p_locale") String locale, @Param("p_bank_code") String bankCode,
                                           @Param("p_payment_mode") Integer paymentMode,
                                           @Param("p_bonus_type") Integer bonusType,
                                           @Param("p_milk_type_code") String milkTypeCode)
            ;

    @Query(value = "CALL rpt_bonus_excel(:p_society_code,:p_from_date,:p_to_date,:p_member_code,:p_locale,:p_payment_mode,:p_bank_code,:p_bonus_type);", nativeQuery = true)
    List<Map<String, Object>> findAllExcel(@Param("p_society_code") String societyCode, @Param("p_from_date") LocalDate fromDate,
                                           @Param("p_to_date") LocalDate toDate, @Param("p_member_code") String memberCode,
                                           @Param("p_locale") String locale, @Param("p_bank_code") String bankCode,
                                           @Param("p_payment_mode") Integer paymentMode,
                                           @Param("p_bonus_type") Integer bonusType);
}
