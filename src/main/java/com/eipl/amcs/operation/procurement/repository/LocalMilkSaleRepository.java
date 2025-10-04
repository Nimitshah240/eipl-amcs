package com.eipl.amcs.operation.procurement.repository;

import com.eipl.amcs.base.repository.BaseRepository;
import com.eipl.amcs.operation.procurement.model.LocalMilkSale;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface LocalMilkSaleRepository extends BaseRepository<LocalMilkSale, String> {

	@Override
	@EntityGraph(attributePaths = { "shift","milkType","milkClass","society","dock"})
	List<LocalMilkSale> findAll(Sort sort);
	
	@EntityGraph(attributePaths = { "shift","milkType","milkClass","society","dock"})
	List<LocalMilkSale> findBySaleDateBetween(LocalDate startDate, LocalDate endDate,Sort sort);
	
	@EntityGraph(attributePaths = { "shift","milkType","milkClass","society","dock"})
	List<LocalMilkSale> findBySaleDateBetween(LocalDateTime startDate, LocalDateTime endDate,Sort sort);
	
	@Override
	@EntityGraph(attributePaths = { "shift","milkType","milkClass","society","dock"})
	Optional<LocalMilkSale> findById(String id);
}
