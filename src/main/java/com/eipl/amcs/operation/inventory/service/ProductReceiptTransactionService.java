package com.eipl.amcs.operation.inventory.service;

import com.eipl.amcs.operation.inventory.dto.ReceiptTxnTaxDto;
import com.eipl.amcs.operation.inventory.model.ProductReceiptTransaction;

import java.util.List;
import java.util.Optional;

public interface ProductReceiptTransactionService {
	 List<ProductReceiptTransaction> findAll();
	 
	 ProductReceiptTransaction save(ProductReceiptTransaction productReceiptTransaction);
		ProductReceiptTransaction update(ProductReceiptTransaction productReceiptTransaction);
		
		Optional<ProductReceiptTransaction> findById(String grnTxnNo);
		
		void delete(String grnTxnNo);

		void delete(ProductReceiptTransaction productReceiptTransaction);
		
		List<ReceiptTxnTaxDto> findByProductReceipt(String code);
}
