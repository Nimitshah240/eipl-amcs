package com.eipl.amcs.master.procurement.repository;

import com.eipl.amcs.master.global.model.Shift;
import com.eipl.amcs.master.org.model.Society;
import com.eipl.amcs.master.procurement.model.MemberMilkPurchaseRateApplicability;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface MemberMilkPurchaseRateApplicabilityRepository  extends JpaRepository<MemberMilkPurchaseRateApplicability, String> {

	@Override
	@EntityGraph(attributePaths = { "shift", "memberMilkPurchaseRate", "society"})
	List<MemberMilkPurchaseRateApplicability> findAll(Sort sort);
	
	@Query("SELECT app FROM MemberMilkPurchaseRateApplicability app LEFT JOIN FETCH app.shift LEFT JOIN FETCH app.memberMilkPurchaseRate rate "
			+ "LEFT JOIN FETCH app.society WHERE app.wefDate <= ?1 AND (rate.shift = ?2 OR rate.shiftApplicable = ?3) AND app.society = ?4 "
			+ "ORDER BY app.wefDate DESC")
	List<MemberMilkPurchaseRateApplicability> findRateApplicabilityTop2(LocalDateTime date, Shift shift, Shift shiftApp, Society society);
	
	@Override
	@EntityGraph(attributePaths = { "shift", "memberMilkPurchaseRate", "society"})
	Optional<MemberMilkPurchaseRateApplicability> findById(String id);
}
