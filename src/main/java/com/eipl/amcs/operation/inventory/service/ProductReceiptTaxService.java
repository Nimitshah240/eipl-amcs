package com.eipl.amcs.operation.inventory.service;

import com.eipl.amcs.operation.inventory.model.ProductReceiptTax;

import java.util.List;
import java.util.Optional;

public interface ProductReceiptTaxService {
    List<ProductReceiptTax> findAll();

    ProductReceiptTax save(ProductReceiptTax productReceiptTax);

    ProductReceiptTax update(ProductReceiptTax productReceiptTax);

    Optional<ProductReceiptTax> findById(String code);

    void delete(String code);

    void delete(ProductReceiptTax productReceiptTax);
}
