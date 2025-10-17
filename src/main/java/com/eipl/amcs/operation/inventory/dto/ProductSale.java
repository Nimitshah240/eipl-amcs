package com.eipl.amcs.operation.inventory.dto;

import com.eipl.amcs.base.model.BaseModel;
import com.eipl.amcs.master.org.model.Dock;
import com.eipl.amcs.master.org.model.Society;
import com.eipl.amcs.master.org.model.Union;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ProductSale extends BaseModel {
    private String invoiceNo;
    private BigDecimal amount;
    private BigDecimal discount;
    private BigDecimal netAmount;
    private BigDecimal taxAmount;
    private LocalDate invoiceDate;
    private LocalDate deductionStartDate;
    private Short noOfInstallments;
    private Short paymentMode;
    private Short consumerType;
    private Short transactionType;
    private String voucherNo;
    private String consumerCode;
    private Dock dock;
    private Union union;
    private Society society;

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    public BigDecimal getNetAmount() {
        return netAmount;
    }

    public void setNetAmount(BigDecimal netAmount) {
        this.netAmount = netAmount;
    }

    public BigDecimal getTaxAmount() {
        return taxAmount;
    }

    public void setTaxAmount(BigDecimal taxAmount) {
        this.taxAmount = taxAmount;
    }

    public LocalDate getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(LocalDate invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public LocalDate getDeductionStartDate() {
        return deductionStartDate;
    }

    public void setDeductionStartDate(LocalDate deductionStartDate) {
        this.deductionStartDate = deductionStartDate;
    }

    public Short getNoOfInstallments() {
        return noOfInstallments;
    }

    public void setNoOfInstallments(Short noOfInstallments) {
        this.noOfInstallments = noOfInstallments;
    }

    public Short getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(Short paymentMode) {
        this.paymentMode = paymentMode;
    }

    public Short getConsumerType() {
        return consumerType;
    }

    public void setConsumerType(Short consumerType) {
        this.consumerType = consumerType;
    }

    public Short getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(Short transactionType) {
        this.transactionType = transactionType;
    }

    public String getVoucherNo() {
        return voucherNo;
    }

    public void setVoucherNo(String voucherNo) {
        this.voucherNo = voucherNo;
    }

    public String getConsumerCode() {
        return consumerCode;
    }

    public void setConsumerCode(String consumerCode) {
        this.consumerCode = consumerCode;
    }

    public Dock getDock() {
        return dock;
    }

    public void setDock(Dock dock) {
        this.dock = dock;
    }

    public Union getUnion() {
        return union;
    }

    public void setUnion(Union union) {
        this.union = union;
    }

    public Society getSociety() {
        return society;
    }

    public void setSociety(Society society) {
        this.society = society;
    }
}
