package com.eipl.amcs.operation.inventory.service;

import com.eipl.amcs.operation.inventory.dto.ProductSaleDto;
import com.eipl.amcs.operation.inventory.dto.ProductSaleMigrateDto;
import com.eipl.amcs.operation.inventory.model.ProductSale;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ProductSaleService {
    List<ProductSale> findAll(LocalDate fromDt, LocalDate toDt);

    ProductSaleDto save(ProductSaleDto productSaleDto, String identityInfo);

    ProductSaleDto update(ProductSaleDto productSaleDto, String identityInfo);

    Optional<ProductSale> findById(String invoiceNo);

    void delete(String invoiceNo, String identityInfo);

    void delete(ProductSale productSale, String identityInfo);

    List<ProductSaleMigrateDto> migrateCollections(List<ProductSaleMigrateDto> dtoList, String header);

    List<ProductSale> findByMemberCodeAndDate(String memberCode, LocalDate saleFromDate, LocalDate saleToDate);
}
