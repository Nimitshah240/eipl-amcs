package com.eipl.amcs.operation.inventory.service;



import com.eipl.amcs.operation.inventory.dto.ProductRequisitionDto;
import com.eipl.amcs.operation.inventory.model.ProductRequisitionTransaction;

import java.util.List;
import java.util.Optional;
public interface ProductRequisitionTransactionService {

	List<ProductRequisitionTransaction> findAll();

	ProductRequisitionTransaction save(ProductRequisitionTransaction  productRequisitionTransaction);

	Optional<ProductRequisitionTransaction> findById(String code);

	void delete(String code);

	ProductRequisitionDto findByProductReceipt(String code);


}
