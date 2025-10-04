package com.eipl.amcs.master.procurement.dto;

import com.eipl.amcs.base.model.BaseModelTxn;
import com.eipl.amcs.master.global.model.MilkQualityType;
import com.eipl.amcs.master.global.model.MilkType;

import java.math.BigDecimal;
import com.eipl.amcs.master.procurement.model.MemberMilkPurchaseRate;

public class MemberMilkPurchaseRateDetail extends BaseModelTxn {

    private String code;
    private BigDecimal fat;
    private BigDecimal snf;
    private BigDecimal rate;

    private MilkType milkType;
    private MilkQualityType milkQualityType;
    private MemberMilkPurchaseRate memberMilkPurchaseRate;

    public MemberMilkPurchaseRateDetail() {
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public BigDecimal getFat() {
        return fat;
    }

    public void setFat(BigDecimal fat) {
        this.fat = fat;
    }

    public BigDecimal getSnf() {
        return snf;
    }

    public void setSnf(BigDecimal snf) {
        this.snf = snf;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public MilkType getMilkType() {
        return milkType;
    }

    public void setMilkType(MilkType milkType) {
        this.milkType = milkType;
    }

    public MilkQualityType getMilkQualityType() {
        return milkQualityType;
    }

    public void setMilkQualityType(MilkQualityType milkQualityType) {
        this.milkQualityType = milkQualityType;
    }

    public MemberMilkPurchaseRate getMemberMilkPurchaseRate() {
        return memberMilkPurchaseRate;
    }

    public void setMemberMilkPurchaseRate(MemberMilkPurchaseRate memberMilkPurchaseRate) {
        this.memberMilkPurchaseRate = memberMilkPurchaseRate;
    }
}