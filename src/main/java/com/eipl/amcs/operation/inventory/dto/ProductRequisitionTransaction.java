package com.eipl.amcs.operation.inventory.dto;

import com.eipl.amcs.base.model.BaseModel;
import com.eipl.amcs.master.inventory.model.Product;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import com.eipl.amcs.operation.inventory.model.ProductRequisition;

public class ProductRequisitionTransaction extends BaseModel {
    private String code;
    private String approvedBy;
    private LocalDate approvedDate;
    private BigDecimal approvedQuantity;
    private LocalDateTime cancelledAt;
    private String cancelledBy;
    private BigDecimal discountAmount;
    private int isApproved;
    private Boolean cancel;
    private BigDecimal amount;
    private BigDecimal rate;
    private BigDecimal quantity;
    private LocalDateTime requisitionDate;
    private LocalDate expectedDeliveryDate;
    private String schemeAddType;
    private String status;
    private Product product;
    private ProductRequisition productRequisition;
    private String productSchemeCode;
    private int passonToMember;
    private String unionCode;
    private String societyCode;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(String approvedBy) {
        this.approvedBy = approvedBy;
    }

    public LocalDate getApprovedDate() {
        return approvedDate;
    }

    public void setApprovedDate(LocalDate approvedDate) {
        this.approvedDate = approvedDate;
    }

    public BigDecimal getApprovedQuantity() {
        return approvedQuantity;
    }

    public void setApprovedQuantity(BigDecimal approvedQuantity) {
        this.approvedQuantity = approvedQuantity;
    }

    public LocalDateTime getCancelledAt() {
        return cancelledAt;
    }

    public void setCancelledAt(LocalDateTime cancelledAt) {
        this.cancelledAt = cancelledAt;
    }

    public String getCancelledBy() {
        return cancelledBy;
    }

    public void setCancelledBy(String cancelledBy) {
        this.cancelledBy = cancelledBy;
    }

    public BigDecimal getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(BigDecimal discountAmount) {
        this.discountAmount = discountAmount;
    }

    public int getIsApproved() {
        return isApproved;
    }

    public void setIsApproved(int isApproved) {
        this.isApproved = isApproved;
    }

    public Boolean getCancel() {
        return cancel;
    }

    public void setCancel(Boolean cancel) {
        this.cancel = cancel;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public LocalDateTime getRequisitionDate() {
        return requisitionDate;
    }

    public void setRequisitionDate(LocalDateTime requisitionDate) {
        this.requisitionDate = requisitionDate;
    }

    public LocalDate getExpectedDeliveryDate() {
        return expectedDeliveryDate;
    }

    public void setExpectedDeliveryDate(LocalDate expectedDeliveryDate) {
        this.expectedDeliveryDate = expectedDeliveryDate;
    }

    public String getSchemeAddType() {
        return schemeAddType;
    }

    public void setSchemeAddType(String schemeAddType) {
        this.schemeAddType = schemeAddType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public String getUnionCode() {
        return unionCode;
    }

    public void setUnionCode(String unionCode) {
        this.unionCode = unionCode;
    }

    public String getSocietyCode() {
        return societyCode;
    }

    public void setSocietyCode(String societyCode) {
        this.societyCode = societyCode;
    }
}
