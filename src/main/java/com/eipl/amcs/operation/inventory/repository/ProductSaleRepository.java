package com.eipl.amcs.operation.inventory.repository;

import com.eipl.amcs.base.repository.BaseRepository;
import com.eipl.amcs.operation.inventory.model.ProductSale;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ProductSaleRepository extends BaseRepository<ProductSale, String> {

	@Override
	@EntityGraph(attributePaths = { "dock", "union", "society" })
	Optional<ProductSale> findById(String id);

	@Override
	@EntityGraph(attributePaths = { "dock", "union", "society" })
	List<ProductSale> findAll(Sort sort);

	@EntityGraph(attributePaths = { "dock", "union", "society" })
	List<ProductSale> findByInvoiceDateBetween(LocalDate fromDate, LocalDate toDate, Sort sort);

}
