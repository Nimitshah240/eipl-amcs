package com.eipl.amcs.master.inventory.service;

import com.eipl.amcs.master.inventory.model.Product;
import com.eipl.amcs.master.inventory.model.ProductSaleRate;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ProductSaleRateService {
    List<ProductSaleRate> findAll();

    ProductSaleRate save(ProductSaleRate productSaleRate, String identityInfo);

    ProductSaleRate update(ProductSaleRate productSaleRate, String identityInfo);

    Optional<ProductSaleRate> findById(String code);

    ProductSaleRate findByProduct(Product product, LocalDate date);

    void delete(String code, String identityInfo);

    void delete(ProductSaleRate productSaleRate, String identityInfo);

    LocalDate checkWefDate(String str1, String str2);

}
