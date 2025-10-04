package com.eipl.amcs.master.inventory.repository;

import com.eipl.amcs.base.repository.BaseRepository;
import com.eipl.amcs.master.inventory.model.Product;
import com.eipl.amcs.master.org.model.Society;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends BaseRepository<Product, String> {

	@Override
	@EntityGraph(attributePaths = { "conversionUnit", "primaryUom", "productGroup", "tax", "secondaryPackaging",
			"union", "society" })
	Optional<Product> findById(String id);

	@Override
	@EntityGraph(attributePaths = { "conversionUnit", "primaryUom", "productGroup", "tax", "secondaryPackaging",
			"union", "society" })
	List<Product> findAll(Sort sort);

	@Query(value = "SELECT p FROM Product p where p.name =?1 and p.code= ?2")
	List<Product> checkName(String str, String Code);

	@EntityGraph(attributePaths = { "conversionUnit", "primaryUom", "productGroup", "tax", "secondaryPackaging",
			"union", "society" })
	List<Product> findAllBySociety(Society society, Sort by);
	
	@EntityGraph(attributePaths = { "conversionUnit", "primaryUom", "productGroup", "tax", "secondaryPackaging",
			"union" })
	List<Product> findAllBySocietyIsNull(Sort by);
}
