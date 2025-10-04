package com.eipl.amcs.master.inventory.repository;

import com.eipl.amcs.base.repository.BaseRepository;
import com.eipl.amcs.master.inventory.model.Product;
import com.eipl.amcs.master.inventory.model.ProductPurchaseRate;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ProductPurchaseRateRepository extends BaseRepository<ProductPurchaseRate, String> {

	@Override
	@EntityGraph(attributePaths = { "society", "union", "product" })
	Optional<ProductPurchaseRate> findById(String id);

	@Override
	@EntityGraph(attributePaths = { "society", "union", "product" })
	List<ProductPurchaseRate> findAll(Sort sort);

	@Query(nativeQuery = true, value = "SELECT wef_Date FROM product_purchase_rate ppr WHERE ppr.product_code = ?1 ORDER BY ppr.wef_Date DESC LIMIT 1")
	LocalDate checkWefDate(String str);

	@EntityGraph(attributePaths = { "society", "union", "product" })
	ProductPurchaseRate findTop1ByProductAndWefDateLessThanEqualOrderByWefDateDesc(Product product, LocalDate date);
}