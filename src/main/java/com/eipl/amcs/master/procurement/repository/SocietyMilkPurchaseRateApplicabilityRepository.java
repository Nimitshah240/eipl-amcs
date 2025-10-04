package com.eipl.amcs.master.procurement.repository;

import com.eipl.amcs.master.global.model.Shift;
import com.eipl.amcs.master.org.model.Society;
import com.eipl.amcs.master.procurement.model.SocietyMilkPurchaseRateApplicability;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface SocietyMilkPurchaseRateApplicabilityRepository
		extends JpaRepository<SocietyMilkPurchaseRateApplicability, String> {

	@Override
	@EntityGraph(attributePaths = { "shift", "societyMilkPurchaseRate", "society" })
	List<SocietyMilkPurchaseRateApplicability> findAll(Sort sort);

//	@EntityGraph(attributePaths = { "shift", "societyMilkPurchaseRate","society"})
	@Query("SELECT app FROM SocietyMilkPurchaseRateApplicability app LEFT JOIN FETCH app.shift LEFT JOIN FETCH app.societyMilkPurchaseRate rate "
			+ "LEFT JOIN FETCH app.society LEFT JOIN FETCH rate.rateType WHERE app.wefDate <= ?1 AND (rate.shift = ?2 OR rate.shiftApplicable = ?3) AND app.society = ?4 "
			+ "ORDER BY app.wefDate DESC")
	List<SocietyMilkPurchaseRateApplicability> findRateApplicabilityTop2(LocalDateTime date, Shift shift,
			Shift shiftApp, Society society);
	
	@Override
	@EntityGraph(attributePaths = { "shift", "societyMilkPurchaseRate", "society" })
	Optional<SocietyMilkPurchaseRateApplicability> findById(String id);
}
