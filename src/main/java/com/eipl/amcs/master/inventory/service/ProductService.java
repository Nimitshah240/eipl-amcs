package com.eipl.amcs.master.inventory.service;

import com.eipl.amcs.master.inventory.model.Product;
import com.eipl.amcs.master.inventory.model.ProductAndSaleRateDto;

import java.util.List;
import java.util.Optional;

public interface ProductService {
    List<Product> findAll();


    Product save(Product product, String identityInfo);

    Product update(Product product, String identityInfo);


    Optional<Product> findById(String productNo);

    void delete(String productNo, String identityInfo);

    void delete(Product product, String identityInfo);

    boolean checkName(String name, String Code);

    List<ProductAndSaleRateDto> migrateCollections(List<ProductAndSaleRateDto> dtoList, String header);

    List<Product> findAllBySociety(String societyCode);
}
