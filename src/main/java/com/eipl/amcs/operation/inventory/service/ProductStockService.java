package com.eipl.amcs.operation.inventory.service;

import com.eipl.amcs.operation.inventory.model.ProductStock;

import java.util.List;
import java.util.Optional;

public interface ProductStockService {
    List<ProductStock> findAll();


    ProductStock save(ProductStock productStock);

    ProductStock update(ProductStock productStock);


    Optional<ProductStock> findById(String code);

    void delete(String code);

    ProductStock findByProduct(String code);

    void delete(ProductStock productStock);

}
