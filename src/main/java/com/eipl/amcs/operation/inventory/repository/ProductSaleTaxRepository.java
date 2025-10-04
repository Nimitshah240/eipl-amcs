package com.eipl.amcs.operation.inventory.repository;

import com.eipl.amcs.base.repository.BaseRepository;
import com.eipl.amcs.operation.inventory.model.ProductSale;
import com.eipl.amcs.operation.inventory.model.ProductSaleTax;
import com.eipl.amcs.operation.inventory.model.ProductSaleTransaction;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;

import java.util.List;
import java.util.Optional;

public interface ProductSaleTaxRepository extends BaseRepository<ProductSaleTax, String> {

	@Override
	@EntityGraph(attributePaths = { "productSale", "productSaleTransaction", "taxDetail" })
	Optional<ProductSaleTax> findById(String id);

	@Override
	@EntityGraph(attributePaths = { "productSale", "productSaleTransaction", "taxDetail" })
	List<ProductSaleTax> findAll(Sort sort);

	void deleteByProductSale(ProductSale productSale);

	@EntityGraph(attributePaths = { "productSale", "productSaleTransaction", "taxDetail" })
	List<ProductSaleTax> findByProductSale(ProductSale productSale);

	@EntityGraph(attributePaths = { "productSale", "productSaleTransaction", "taxDetail" })
	List<ProductSaleTax> findByproductSaleTransaction(ProductSaleTransaction productSaleTransaction);
}
