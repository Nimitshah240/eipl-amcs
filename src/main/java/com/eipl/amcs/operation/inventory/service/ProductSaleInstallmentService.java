package com.eipl.amcs.operation.inventory.service;

import com.eipl.amcs.operation.inventory.model.ProductSaleInstallment;

import java.util.List;
import java.util.Optional;

public interface ProductSaleInstallmentService {
    List<ProductSaleInstallment> findAll();

    ProductSaleInstallment save(ProductSaleInstallment productSaleToMemberInstallment);

    ProductSaleInstallment update(ProductSaleInstallment productSaleToMemberInstallment);

    Optional<ProductSaleInstallment> findById(String installmentNo);

    void delete(String installmentNo);

    void delete(ProductSaleInstallment productSaleToMemberInstallment);

    List<ProductSaleInstallment> fetchInstallmentIsBilled(String str, boolean b);

    List<ProductSaleInstallment> fetchByPaymentCycle(String str);

}
