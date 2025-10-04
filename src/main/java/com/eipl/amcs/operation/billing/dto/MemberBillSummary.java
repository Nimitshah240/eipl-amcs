package com.eipl.amcs.operation.billing.dto;

import com.eipl.amcs.master.procurement.model.SocietyPaymentCycle;

import java.math.BigDecimal;

public class MemberBillSummary {
    private String code;
    private BigDecimal milkQty;
    private BigDecimal milkAmount;
    private BigDecimal productSaleAmount;
    private BigDecimal localSaleAmount;
    private BigDecimal loanAmount;
    private BigDecimal otherAddAmount;
    private BigDecimal otherDedAmount;
    private BigDecimal netAmount;
    private BigDecimal disbursedAmount;
    private short status; //1-PENDING
    private SocietyPaymentCycle paymentCycle;

    public MemberBillSummary() {
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public BigDecimal getMilkQty() {
        return milkQty;
    }

    public void setMilkQty(BigDecimal milkQty) {
        this.milkQty = milkQty;
    }

    public BigDecimal getMilkAmount() {
        return milkAmount;
    }

    public void setMilkAmount(BigDecimal milkAmount) {
        this.milkAmount = milkAmount;
    }

    public BigDecimal getProductSaleAmount() {
        return productSaleAmount;
    }

    public void setProductSaleAmount(BigDecimal productSaleAmount) {
        this.productSaleAmount = productSaleAmount;
    }

    public BigDecimal getLocalSaleAmount() {
        return localSaleAmount;
    }

    public void setLocalSaleAmount(BigDecimal localSaleAmount) {
        this.localSaleAmount = localSaleAmount;
    }

    public BigDecimal getLoanAmount() {
        return loanAmount;
    }

    public void setLoanAmount(BigDecimal loanAmount) {
        this.loanAmount = loanAmount;
    }

    public BigDecimal getOtherAddAmount() {
        return otherAddAmount;
    }

    public void setOtherAddAmount(BigDecimal otherAddAmount) {
        this.otherAddAmount = otherAddAmount;
    }

    public BigDecimal getOtherDedAmount() {
        return otherDedAmount;
    }

    public void setOtherDedAmount(BigDecimal otherDedAmount) {
        this.otherDedAmount = otherDedAmount;
    }

    public BigDecimal getNetAmount() {
        return netAmount;
    }

    public void setNetAmount(BigDecimal netAmount) {
        this.netAmount = netAmount;
    }

    public BigDecimal getDisbursedAmount() {
        return disbursedAmount;
    }

    public void setDisbursedAmount(BigDecimal disbursedAmount) {
        this.disbursedAmount = disbursedAmount;
    }

    public short getStatus() {
        return status;
    }

    public void setStatus(short status) {
        this.status = status;
    }

    public SocietyPaymentCycle getPaymentCycle() {
        return paymentCycle;
    }

    public void setPaymentCycle(SocietyPaymentCycle paymentCycle) {
        this.paymentCycle = paymentCycle;
    }
}