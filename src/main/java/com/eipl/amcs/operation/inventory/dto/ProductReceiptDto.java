package com.eipl.amcs.operation.inventory.dto;

import com.eipl.amcs.operation.inventory.model.ProductReceipt;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class ProductReceiptDto {
    private ProductReceipt productReceipt;
    private List<ReceiptTxnTaxDto> receiptTxnTaxDtoList;

//    public ProductReceiptDto() {
//    }
//
//    public ProductReceiptDto(ProductReceipt productReceipt, List<ReceiptTxnTaxDto> receiptTxnTaxDtoList) {
//        this.productReceipt = productReceipt;
//        this.receiptTxnTaxDtoList = receiptTxnTaxDtoList;
//    }
//
//    public ProductReceipt getProductReceipt() {
//        return productReceipt;
//    }
//
//    public void setProductReceipt(ProductReceipt productReceipt) {
//        this.productReceipt = productReceipt;
//    }
//
//    public List<ReceiptTxnTaxDto> getReceiptTxnTaxDtoList() {
//        return receiptTxnTaxDtoList;
//    }
//
//    public void setReceiptTxnTaxDtoList(List<ReceiptTxnTaxDto> receiptTxnTaxDtoList) {
//        this.receiptTxnTaxDtoList = receiptTxnTaxDtoList;
//    }
}
