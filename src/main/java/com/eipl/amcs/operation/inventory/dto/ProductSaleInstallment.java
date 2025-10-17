package com.eipl.amcs.operation.inventory.dto;

import com.eipl.amcs.base.model.BaseModelTxn;
import com.eipl.amcs.master.operation.model.Member;
import com.eipl.amcs.master.procurement.model.SocietyPaymentCycle;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ProductSaleInstallment extends BaseModelTxn {
    private String code;
    private LocalDate deductionDate;
    private BigDecimal actualInstallment;
    private BigDecimal installmentAmount;
    private BigDecimal previousPendingAmount;
    private Integer type;
    private Boolean isBilling;
    private String invoiceNo;
    private String unionCode;
    private String societyCode;
    private SocietyPaymentCycle societyPaymentCycle;
    private Member member;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public LocalDate getDeductionDate() {
        return deductionDate;
    }

    public void setDeductionDate(LocalDate deductionDate) {
        this.deductionDate = deductionDate;
    }

    public BigDecimal getActualInstallment() {
        return actualInstallment;
    }

    public void setActualInstallment(BigDecimal actualInstallment) {
        this.actualInstallment = actualInstallment;
    }

    public BigDecimal getInstallmentAmount() {
        return installmentAmount;
    }

    public void setInstallmentAmount(BigDecimal installmentAmount) {
        this.installmentAmount = installmentAmount;
    }

    public BigDecimal getPreviousPendingAmount() {
        return previousPendingAmount;
    }

    public void setPreviousPendingAmount(BigDecimal previousPendingAmount) {
        this.previousPendingAmount = previousPendingAmount;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Boolean getBilling() {
        return isBilling;
    }

    public void setBilling(Boolean billing) {
        isBilling = billing;
    }

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
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

    public SocietyPaymentCycle getSocietyPaymentCycle() {
        return societyPaymentCycle;
    }

    public void setSocietyPaymentCycle(SocietyPaymentCycle societyPaymentCycle) {
        this.societyPaymentCycle = societyPaymentCycle;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

}
