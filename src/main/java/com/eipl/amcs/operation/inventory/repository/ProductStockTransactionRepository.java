package com.eipl.amcs.operation.inventory.repository;

import com.eipl.amcs.base.repository.BaseRepository;
import com.eipl.amcs.operation.inventory.model.ProductStockTransaction;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductStockTransactionRepository extends BaseRepository<ProductStockTransaction, String> {

	@Override
	@EntityGraph(attributePaths = { "society", "product" })
	Optional<ProductStockTransaction> findById(String id);
}
