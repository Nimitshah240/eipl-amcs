package com.eipl.amcs.operation.inventory.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.eipl.amcs.operation.inventory.model.ProductSaleTransaction;
import com.eipl.amcs.operation.inventory.model.ProductSaleTax;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class SaleTxnTaxDto {
    private ProductSaleTransaction transaction;
    private List<ProductSaleTax> saleTaxList;
//
//    public SaleTxnTaxDto() {
//    }
//
//    public SaleTxnTaxDto(ProductSaleTransaction transaction, List<ProductSaleTax> saleTaxList) {
//        this.transaction = transaction;
//        this.saleTaxList = saleTaxList;
//    }
//
//    public ProductSaleTransaction getTransaction() {
//        return transaction;
//    }
//
//    public void setTransaction(ProductSaleTransaction transaction) {
//        this.transaction = transaction;
//    }
//
//    public List<ProductSaleTax> getSaleTaxList() {
//        return saleTaxList;
//    }
//
//    public void setSaleTaxList(List<ProductSaleTax> saleTaxList) {
//        this.saleTaxList = saleTaxList;
//    }
}
