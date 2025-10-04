package com.eipl.amcs.operation.inventory.dto;

import com.eipl.amcs.base.model.BaseModelTxn;
import com.eipl.amcs.master.inventory.model.Product;
import com.eipl.amcs.operation.inventory.model.ProductRequisitionTransaction;
import com.eipl.amcs.operation.inventory.model.ProductRequisition;
import com.eipl.amcs.master.org.model.Society;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ProductDispatchTransaction extends BaseModelTxn {
    private String code;
    private BigDecimal amount;
    private BigDecimal discountAmount;
    private LocalDate dispatchDate;
    private BigDecimal dispatchQty;
    private BigDecimal rate;
    private String productSchemeCode;
    private int passonToMember;
    private String status;
    private String unionCode;

    private ProductDispatch productDispatch;
    private Society society;
    private Product product;
    private ProductRequisition productRequisition;
    private ProductRequisitionTransaction productRequisitionTransaction;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(BigDecimal discountAmount) {
        this.discountAmount = discountAmount;
    }

    public LocalDate getDispatchDate() {
        return dispatchDate;
    }

    public void setDispatchDate(LocalDate dispatchDate) {
        this.dispatchDate = dispatchDate;
    }

    public BigDecimal getDispatchQty() {
        return dispatchQty;
    }

    public void setDispatchQty(BigDecimal dispatchQty) {
        this.dispatchQty = dispatchQty;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public String getProductSchemeCode() {
        return productSchemeCode;
    }

    public void setProductSchemeCode(String productSchemeCode) {
        this.productSchemeCode = productSchemeCode;
    }

    public int getPassonToMember() {
        return passonToMember;
    }

    public void setPassonToMember(int passonToMember) {
        this.passonToMember = passonToMember;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUnionCode() {
        return unionCode;
    }

    public void setUnionCode(String unionCode) {
        this.unionCode = unionCode;
    }

    public ProductDispatch getProductDispatch() {
        return productDispatch;
    }

    public void setProductDispatch(ProductDispatch productDispatch) {
        this.productDispatch = productDispatch;
    }

    public Society getSociety() {
        return society;
    }

    public void setSociety(Society society) {
        this.society = society;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public ProductRequisition getProductRequisition() {
        return productRequisition;
    }

    public void setProductRequisition(ProductRequisition productRequisition) {
        this.productRequisition = productRequisition;
    }

    public ProductRequisitionTransaction getProductRequisitionTransaction() {
        return productRequisitionTransaction;
    }

    public void setProductRequisitionTransaction(ProductRequisitionTransaction productRequisitionTransaction) {
        this.productRequisitionTransaction = productRequisitionTransaction;
    }
}
