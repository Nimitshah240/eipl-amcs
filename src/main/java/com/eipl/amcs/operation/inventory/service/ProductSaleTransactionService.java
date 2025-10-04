package com.eipl.amcs.operation.inventory.service;

import com.eipl.amcs.operation.inventory.dto.SaleTxnTaxDto;
import com.eipl.amcs.operation.inventory.model.ProductSaleTransaction;

import java.util.List;
import java.util.Optional;

public interface ProductSaleTransactionService {
	List<ProductSaleTransaction> findAll();

	ProductSaleTransaction save(ProductSaleTransaction productSaleToMemberTransaction);

	ProductSaleTransaction update(ProductSaleTransaction productSaleToMemberTransaction);

	Optional<ProductSaleTransaction> findById(String invoiceTransactionNo);

	void delete(String invoiceTransactionNo);

	void delete(ProductSaleTransaction productSaleToMemberTransaction);
	
	List<SaleTxnTaxDto> findByProductSale(String code);

}
