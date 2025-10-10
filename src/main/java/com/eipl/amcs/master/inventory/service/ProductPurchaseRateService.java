package com.eipl.amcs.master.inventory.service;

import com.eipl.amcs.master.inventory.model.Product;
import com.eipl.amcs.master.inventory.model.ProductPurchaseRate;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ProductPurchaseRateService {
    List<ProductPurchaseRate> findAll();

    ProductPurchaseRate save(ProductPurchaseRate productPurchaseRate, String identityInfo);

    ProductPurchaseRate update(ProductPurchaseRate productPurchaseRate, String identityInfo);

    Optional<ProductPurchaseRate> findById(String code);

    void delete(String code, String identityInfo);

    void delete(ProductPurchaseRate productPurchaseRate, String identityInfo);

    LocalDate checkWefDate(String str);

    ProductPurchaseRate findProductRate(Product product, LocalDate date);
}
