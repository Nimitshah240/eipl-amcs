package com.eipl.amcs.master.procurement.repository;

import com.eipl.amcs.master.global.model.MilkQualityType;
import com.eipl.amcs.master.global.model.MilkType;
import com.eipl.amcs.master.procurement.model.SocietyMilkPurchaseRate;
import com.eipl.amcs.master.procurement.model.SocietyMilkPurchaseRateDetail;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SocietyMilkPurchaseRateDetailRepository extends JpaRepository<SocietyMilkPurchaseRateDetail, String> {

    @Override
    @EntityGraph(attributePaths = {"milkType", "milkQualityType", "societyMilkPurchaseRate"})
    List<SocietyMilkPurchaseRateDetail> findAll(Sort sort);

    @EntityGraph(attributePaths = {"milkType", "milkQualityType", "societyMilkPurchaseRate"})
    List<SocietyMilkPurchaseRateDetail> findBySocietyMilkPurchaseRateAndMilkTypeAndMilkQualityType(
            SocietyMilkPurchaseRate societyMilkPurchaseRate, MilkType milkType, MilkQualityType milkQualityType,
            Sort sort);

    @Override
    @EntityGraph(attributePaths = {"milkType", "milkQualityType", "societyMilkPurchaseRate"})
    Optional<SocietyMilkPurchaseRateDetail> findById(String id);
}
