package com.eipl.amcs.master.inventory.repository;

import com.eipl.amcs.base.repository.BaseRepository;
import com.eipl.amcs.master.inventory.model.Product;
import com.eipl.amcs.master.inventory.model.ProductSaleRate;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ProductSaleRateRepository extends BaseRepository<ProductSaleRate, String> {

	@Override
	@EntityGraph(attributePaths = { "society", "product", "union" })
	Optional<ProductSaleRate> findById(String id);

	@Override
	@EntityGraph(attributePaths = { "society", "product", "union" })
	List<ProductSaleRate> findAll(Sort sort);

	@Query(nativeQuery = true, value = "SELECT psr.wef_date FROM product_sale_rate psr WHERE psr.product_code = ?1 AND psr.code != ?2 ORDER BY psr.wef_date desc LIMIT 1")
	LocalDate checkWefDate(String str1, String str2);

	@EntityGraph(attributePaths = { "society", "product", "union" })
	ProductSaleRate findTop1ByProductAndWefDateLessThanEqualOrderByWefDateDesc(Product product, LocalDate date);
}
