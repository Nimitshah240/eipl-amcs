package com.eipl.amcs.master.procurement.dto;

import com.eipl.amcs.base.model.BaseModelTxn;
import com.eipl.amcs.master.global.model.MilkClass;
import com.eipl.amcs.master.global.model.MilkType;
import com.eipl.amcs.master.org.model.Society;

import java.math.BigDecimal;
import java.time.LocalDate;

public class LocalMilkSaleRate extends BaseModelTxn {
    private String code;
    private BigDecimal rate;
    private String unionCode;
    private LocalDate wefDate;

    private MilkType milkType;
    private MilkClass milkClass;
    private Society society;

    public LocalMilkSaleRate() {

    }

    public LocalDate getWefDate() {
        return wefDate;
    }

    public void setWefDate(LocalDate wefDate) {
        this.wefDate = wefDate;
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

    public String getUnionCode() {
        return unionCode;
    }

    public void setUnionCode(String unionCode) {
        this.unionCode = unionCode;
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
}