package com.eipl.amcs.operation.inventory.dto;

import com.eipl.amcs.operation.inventory.model.ProductSale;
import com.eipl.amcs.operation.inventory.model.ProductSaleTransaction;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class ProductSaleMigrateDto {
//    public ProductSaleMigrateDto() {
//    }

    public ProductSale productSale;
    public ProductSaleTransaction productSaleTransaction;

//    public ProductSaleMigrateDto(ProductSale productSale, ProductSaleTransaction productSaleTransaction) {
//        this.productSale = productSale;
//        this.productSaleTransaction = productSaleTransaction;
//    }
//
//    public ProductSale getProductSale() {
//        return productSale;
//    }
//
//    public void setProductSale(ProductSale productSale) {
//        this.productSale = productSale;
//    }
//
//    public ProductSaleTransaction getProductSaleTransaction() {
//        return productSaleTransaction;
//    }
//
//    public void setProductSaleTransaction(ProductSaleTransaction productSaleTransaction) {
//        this.productSaleTransaction = productSaleTransaction;
//    }
}
