package com.eipl.amcs.operation.inventory.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.eipl.amcs.operation.inventory.model.ProductSale;
import java.util.List;
import com.eipl.amcs.operation.inventory.model.ProductSaleInstallment;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class ProductSaleDto {
    private ProductSale productSale;
    private List<SaleTxnTaxDto> saleTxnTaxDtoList;
    private List<ProductSaleInstallment> saleInstallments;
//
//    public List<ProductSaleInstallment> getSaleInstallments() {
//        return saleInstallments;
//    }
//
//    public void setSaleInstallments(List<ProductSaleInstallment> saleInstallments) {
//        this.saleInstallments = saleInstallments;
//    }
//
//    public ProductSaleDto() {
//    }
//
//    public ProductSaleDto(ProductSale productSale, List<SaleTxnTaxDto> saleTxnTaxDtoList) {
//        this.productSale = productSale;
//        this.saleTxnTaxDtoList = saleTxnTaxDtoList;
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
//    public List<SaleTxnTaxDto> getSaleTxnTaxDtoList() {
//        return saleTxnTaxDtoList;
//    }
//
//    public void setSaleTxnTaxDtoList(List<SaleTxnTaxDto> saleTxnTaxDtoList) {
//        this.saleTxnTaxDtoList = saleTxnTaxDtoList;
//    }
}
