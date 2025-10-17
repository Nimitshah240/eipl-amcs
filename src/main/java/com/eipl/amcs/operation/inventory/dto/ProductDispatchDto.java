package com.eipl.amcs.operation.inventory.dto;

import java.util.List;

public class ProductDispatchDto {
    private ProductDispatch productDispatch;
    private List<ProductDispatchTransaction> dispatchTransactions;

    public ProductDispatchDto(ProductDispatch productDispatch, List<ProductDispatchTransaction> dispatchTransactions) {
        this.productDispatch = productDispatch;
        this.dispatchTransactions = dispatchTransactions;
    }

    public ProductDispatchDto() {
    }

    public ProductDispatch getProductDispatch() {
        return productDispatch;
    }

    public void setProductDispatch(ProductDispatch productDispatch) {
        this.productDispatch = productDispatch;
    }

    public List<ProductDispatchTransaction> getDispatchTransactions() {
        return dispatchTransactions;
    }

    public void setDispatchTransactions(List<ProductDispatchTransaction> dispatchTransactions) {
        this.dispatchTransactions = dispatchTransactions;
    }
}
