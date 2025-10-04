package com.eipl.amcs.operation.procurement.dto;

import com.eipl.amcs.base.model.BaseModel;
import com.eipl.amcs.master.global.model.MilkClass;
import com.eipl.amcs.master.global.model.MilkType;
import com.eipl.amcs.master.global.model.Shift;
import com.eipl.amcs.master.org.model.Dock;
import com.eipl.amcs.master.org.model.Society;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class LocalMilkSale extends BaseModel {

    private String code;
    private String invoiceNo;
    private Short consumerType;// 1-Member, 2-Institute
    private String consumerCode;
    private LocalDateTime saleDate;
    private Short entryType;// 1-Single Per Shift, 2-Multiple Per Shift, 3-Consumer wise
    private Short paymentMode;// 0-Cash, 1-Credit, 2-Coupon
    private BigDecimal quantity;
    private Short quantityMode;
    private BigDecimal convertedQuantity;
    private Short convertedQuantityMode;
    private BigDecimal rate;
    private BigDecimal amount;
    private BigDecimal cash;
    private BigDecimal coupon;
    private BigDecimal credit;
    private String unionCode;
    private String voucherNo;
    private Shift shift;
    private MilkType milkType;
    private MilkClass milkClass;
    private Society society;
    private Dock dock;

    public String getVoucherNo() {
        return voucherNo;
    }

    public void setVoucherNo(String voucherNo) {
        this.voucherNo = voucherNo;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public Short getConsumerType() {
        return consumerType;
    }

    public void setConsumerType(Short consumerType) {
        this.consumerType = consumerType;
    }

    public String getConsumerCode() {
        return consumerCode;
    }

    public void setConsumerCode(String consumerCode) {
        this.consumerCode = consumerCode;
    }

    public LocalDateTime getSaleDate() {
        return saleDate;
    }

    public void setSaleDate(LocalDateTime saleDate) {
        this.saleDate = saleDate;
    }

    public Short getEntryType() {
        return entryType;
    }

    public void setEntryType(Short entryType) {
        this.entryType = entryType;
    }

    public Short getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(Short paymentMode) {
        this.paymentMode = paymentMode;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public Short getQuantityMode() {
        return quantityMode;
    }

    public void setQuantityMode(Short quantityMode) {
        this.quantityMode = quantityMode;
    }

    public BigDecimal getConvertedQuantity() {
        return convertedQuantity;
    }

    public void setConvertedQuantity(BigDecimal convertedQuantity) {
        this.convertedQuantity = convertedQuantity;
    }

    public Short getConvertedQuantityMode() {
        return convertedQuantityMode;
    }

    public void setConvertedQuantityMode(Short convertedQuantityMode) {
        this.convertedQuantityMode = convertedQuantityMode;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getCash() {
        return cash;
    }

    public void setCash(BigDecimal cash) {
        this.cash = cash;
    }

    public BigDecimal getCoupon() {
        return coupon;
    }

    public void setCoupon(BigDecimal coupon) {
        this.coupon = coupon;
    }

    public BigDecimal getCredit() {
        return credit;
    }

    public void setCredit(BigDecimal credit) {
        this.credit = credit;
    }

    public String getUnionCode() {
        return unionCode;
    }

    public void setUnionCode(String unionCode) {
        this.unionCode = unionCode;
    }

    public Shift getShift() {
        return shift;
    }

    public void setShift(Shift shift) {
        this.shift = shift;
    }

    public MilkType getMilkType() {
        return milkType;
    }

    public void setMilkType(MilkType milkType) {
        this.milkType = milkType;
    }

    public MilkClass getMilkClass() {
        return milkClass;
    }

    public void setMilkClass(MilkClass milkClass) {
        this.milkClass = milkClass;
    }

    public Society getSociety() {
        return society;
    }

    public void setSociety(Society society) {
        this.society = society;
    }

    public Dock getDock() {
        return dock;
    }

    public void setDock(Dock dock) {
        this.dock = dock;
    }

}