package com.eipl.amcs.master.inventory.dto;

import com.eipl.amcs.base.model.BaseModelTxn;
import com.eipl.amcs.master.org.model.Dock;
import com.eipl.amcs.master.org.model.Society;
import com.eipl.amcs.master.org.model.Union;

import java.math.BigDecimal;
import java.time.LocalDate;
import com.eipl.amcs.master.inventory.model.Product;

public class ProductSaleRate extends BaseModelTxn {

    private String code;
    private BigDecimal rate;
    private LocalDate wefDate;
    private BigDecimal secretaryCommissionRate;
    private String purchaseCode;
    private Society society;
    private Product product;
    private Union union;


    public ProductSaleRate() {
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public LocalDate getWefDate() {
        return wefDate;
    }

    public void setWefDate(LocalDate wefDate) {
        this.wefDate = wefDate;
    }

    public BigDecimal getSecretaryCommissionRate() {
        return secretaryCommissionRate;
    }

    public void setSecretaryCommissionRate(BigDecimal secretaryCommissionRate) {
        this.secretaryCommissionRate = secretaryCommissionRate;
    }

    public String getPurchaseCode() {
        return purchaseCode;
    }

    public void setPurchaseCode(String purchaseCode) {
        this.purchaseCode = purchaseCode;
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

    public Union getUnion() {
        return union;
    }

    public void setUnion(Union union) {
        this.union = union;
    }
}