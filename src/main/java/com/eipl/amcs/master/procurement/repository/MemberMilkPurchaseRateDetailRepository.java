package com.eipl.amcs.master.procurement.repository;

import com.eipl.amcs.master.global.model.MilkQualityType;
import com.eipl.amcs.master.global.model.MilkType;
import com.eipl.amcs.master.procurement.model.MemberMilkPurchaseRate;
import com.eipl.amcs.master.procurement.model.MemberMilkPurchaseRateDetail;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MemberMilkPurchaseRateDetailRepository  extends JpaRepository<MemberMilkPurchaseRateDetail, String> {

	@EntityGraph(attributePaths = { "milkType", "milkQualityType", "memberMilkPurchaseRate"})
	List<MemberMilkPurchaseRateDetail> findByMemberMilkPurchaseRateAndMilkTypeAndMilkQualityType(MemberMilkPurchaseRate rate, 
			MilkType milkType, MilkQualityType milkQualityType, Sort sort);
	
	@Override
	@EntityGraph(attributePaths = { "milkType", "milkQualityType", "memberMilkPurchaseRate"})
	Optional<MemberMilkPurchaseRateDetail> findById(String id);
}
