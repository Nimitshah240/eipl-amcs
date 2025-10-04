package com.eipl.amcs.operation.inventory.service;

import com.eipl.amcs.operation.inventory.model.ProductDispatchTransaction;

import java.util.List;
import java.util.Optional;

public interface ProductDispatchTransactionService {

	List<ProductDispatchTransaction> findAll();

	ProductDispatchTransaction save(ProductDispatchTransaction  productDispatchTransaction);

	Optional<ProductDispatchTransaction> findById(String code);

	void delete(String code);


}
