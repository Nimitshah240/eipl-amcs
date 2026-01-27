package com.eipl.amcs.master.procurement.repository;

import com.eipl.amcs.master.global.model.MilkQualityType;
import com.eipl.amcs.master.global.model.MilkType;
import com.eipl.amcs.master.procurement.model.MemberMilkPurchaseRate;
import com.eipl.amcs.master.procurement.model.MemberMilkPurchaseRateDetail;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface MemberMilkPurchaseRateDetailRepository extends JpaRepository<MemberMilkPurchaseRateDetail, String> {

    @EntityGraph(attributePaths = {"milkType", "milkQualityType", "memberMilkPurchaseRate"})
    List<MemberMilkPurchaseRateDetail> findByMemberMilkPurchaseRateAndMilkTypeAndMilkQualityType(MemberMilkPurchaseRate rate,
                                                                                                 MilkType milkType, MilkQualityType milkQualityType, Sort sort);

    @Override
    @EntityGraph(attributePaths = {"milkType", "milkQualityType", "memberMilkPurchaseRate"})
    Optional<MemberMilkPurchaseRateDetail> findById(String id);

    @Query(value =
            "SELECT m.code " +
                    "FROM member_milk_purchase_rate_details d " +
                    "JOIN member_milk_purchase_rate m " +
                    "ON d.member_milk_purchase_rate_code = m.code " +
                    "WHERE d.fat = :fat " +
                    "AND d.snf = :snf " +
                    "AND m.wef_date <= CURRENT_DATE " +
                    "ORDER BY m.wef_date DESC " +
                    "LIMIT 1", nativeQuery = true)
    String findRateCode(
            @Param("fat") BigDecimal fat,
            @Param("snf") BigDecimal snf
    );

}
