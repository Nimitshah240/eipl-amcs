package com.eipl.amcs.operation.inventory.dto;

import com.eipl.amcs.operation.inventory.model.ProductRequisition;
import com.eipl.amcs.operation.inventory.model.ProductRequisitionTransaction;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ProductRequisitionDto {
    private ProductRequisition productRequisition;
    private List<ProductRequisitionTransaction> transactionList;

//    public ProductRequisition getProductRequisition() {
//        return productRequisition;
//    }
//
//    public void setProductRequisition(ProductRequisition productRequisition) {
//        this.productRequisition = productRequisition;
//    }
//
//    public List<ProductRequisitionTransaction> getTransactionList() {
//        return transactionList;
//    }
//
//    public void setTransactionList(List<ProductRequisitionTransaction> transactionList) {
//        this.transactionList = transactionList;
//    }
//
//    public ProductRequisitionDto(ProductRequisition productRequisition, List<ProductRequisitionTransaction> transactionList) {
//        this.productRequisition = productRequisition;
//        this.transactionList = transactionList;
//    }
//
//    public ProductRequisitionDto() {
//    }
}
