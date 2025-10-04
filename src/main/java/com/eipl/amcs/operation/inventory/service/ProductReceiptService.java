package com.eipl.amcs.operation.inventory.service;

import com.eipl.amcs.operation.inventory.dto.ProductReceiptDto;
import com.eipl.amcs.operation.inventory.model.ProductReceipt;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ProductReceiptService {
	List<ProductReceipt> findAll(LocalDate fromDt, LocalDate toDt);

	ProductReceiptDto save(ProductReceiptDto productReceiptDto, String identityInfo);

	ProductReceiptDto update(ProductReceiptDto productReceiptDto, String identityInfo);

	Optional<ProductReceipt> findById(String grnNo);

	void delete(String grnNo, String identityInfo);

	void delete(ProductReceipt productReceipt, String identityInfo);
}
