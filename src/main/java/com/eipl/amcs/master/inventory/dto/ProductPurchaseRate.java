package com.eipl.amcs.master.inventory.dto;

import com.eipl.amcs.base.model.BaseModelTxn;
import com.eipl.amcs.master.inventory.model.Product;
import com.eipl.amcs.master.org.model.Society;
import com.eipl.amcs.master.org.model.Union;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ProductPurchaseRate extends BaseModelTxn {

    private String code;
    private BigDecimal rate;
    private Short entryType;
    private LocalDate wefDate;
    private Society society;
    private Union union;
    private Product product;

    public ProductPurchaseRate() {
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

    public Short getEntryType() {
        return entryType;
    }

    public void setEntryType(Short entryType) {
        this.entryType = entryType;
    }

    public LocalDate getWefDate() {
        return wefDate;
    }

    public void setWefDate(LocalDate wefDate) {
        this.wefDate = wefDate;
    }

    public Society getSociety() {
        return society;
    }

    public void setSociety(Society society) {
        this.society = society;
    }

    public Union getUnion() {
        return union;
    }

    public void setUnion(Union union) {
        this.union = union;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}