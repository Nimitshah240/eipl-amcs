package com.eipl.amcs.master.procurement.repository;

import com.eipl.amcs.master.procurement.model.SocietyMilkPurchaseRate;
import com.eipl.amcs.master.procurement.model.SocietyMilkPurchaseRateBased;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SocietyMilkPurchaseRateBasedRepository extends JpaRepository<SocietyMilkPurchaseRateBased, String> {

    @EntityGraph(attributePaths = {"formula" , "milkType", "milkQualityType"})
    List<SocietyMilkPurchaseRateBased> findBySocietyMilkPurchaseRate(SocietyMilkPurchaseRate rate);
}
