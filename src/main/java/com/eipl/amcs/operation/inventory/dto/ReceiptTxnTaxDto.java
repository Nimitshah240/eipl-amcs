package com.eipl.amcs.operation.inventory.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Setter;
import com.eipl.amcs.operation.inventory.model.ProductReceiptTax;
import java.util.List;
import com.eipl.amcs.operation.inventory.model.ProductReceiptTransaction;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class ReceiptTxnTaxDto {
    private ProductReceiptTransaction transaction;
    private List<ProductReceiptTax> receiptTaxList;

//    public ReceiptTxnTaxDto() {
//    }
//
//    public ReceiptTxnTaxDto(ProductReceiptTransaction transaction, List<ProductReceiptTax> receiptTaxList) {
//        this.transaction = transaction;
//        this.receiptTaxList = receiptTaxList;
//    }
//
//    public ProductReceiptTransaction getTransaction() {
//        return transaction;
//    }
//
//    public void setTransaction(ProductReceiptTransaction transaction) {
//        this.transaction = transaction;
//    }
//
//    public List<ProductReceiptTax> getReceiptTaxList() {
//        return receiptTaxList;
//    }
//
//    public void setReceiptTaxList(List<ProductReceiptTax> receiptTaxList) {
//        this.receiptTaxList = receiptTaxList;
//    }
}
