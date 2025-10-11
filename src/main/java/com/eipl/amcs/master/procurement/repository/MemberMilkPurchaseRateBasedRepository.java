package com.eipl.amcs.master.procurement.repository;

import com.eipl.amcs.master.procurement.model.MemberMilkPurchaseRate;
import com.eipl.amcs.master.procurement.model.MemberMilkPurchaseRateBased;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemberMilkPurchaseRateBasedRepository extends JpaRepository<MemberMilkPurchaseRateBased, String> {

    @EntityGraph(attributePaths = {"formula", "milkType", "milkQualityType"})
    List<MemberMilkPurchaseRateBased> findByMemberMilkPurchaseRate(MemberMilkPurchaseRate rate);
}
