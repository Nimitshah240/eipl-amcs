package com.eipl.amcs.master.procurement.repository;

import com.eipl.amcs.master.procurement.model.SocietyMilkPurchaseRate;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface SocietyMilkPurchaseRateRepository extends JpaRepository<SocietyMilkPurchaseRate, String> {

	@Override
	@EntityGraph(attributePaths = { "shift", "shiftApplicable", "rateType" })
	List<SocietyMilkPurchaseRate> findAll(Sort sort);

	@EntityGraph(attributePaths = { "shift", "shiftApplicable", "rateType" })
	Optional<SocietyMilkPurchaseRate> findByWefDateGreaterThanEqual(LocalDateTime wefDate);

	@Override
	@EntityGraph(attributePaths = { "shift", "shiftApplicable", "rateType" })
	Optional<SocietyMilkPurchaseRate> findById(String id);

	Optional<SocietyMilkPurchaseRate> findTop1ByWefDateGreaterThanEqual(LocalDateTime wefDate);
}
