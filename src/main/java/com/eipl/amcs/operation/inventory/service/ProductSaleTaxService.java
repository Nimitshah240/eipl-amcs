package com.eipl.amcs.operation.inventory.service;

import com.eipl.amcs.operation.inventory.model.ProductSaleTax;

import java.util.List;
import java.util.Optional;

public interface ProductSaleTaxService {
    List<ProductSaleTax> findAll();

    ProductSaleTax save(ProductSaleTax productSaleToMemberTaxCalculated);

    ProductSaleTax update(ProductSaleTax productSaleToMemberTaxCalculated);

    Optional<ProductSaleTax> findById(String code);

    void delete(String code);

    void delete(ProductSaleTax productSaleToMemberTaxCalculated);
}
