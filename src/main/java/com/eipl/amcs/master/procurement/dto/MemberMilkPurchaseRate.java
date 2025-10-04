package com.eipl.amcs.master.procurement.dto;

import com.eipl.amcs.base.model.BaseModel;
import com.eipl.amcs.master.global.model.RateType;
import com.eipl.amcs.master.global.model.Shift;
import com.eipl.amcs.master.org.model.Society;
import com.eipl.amcs.utils.CommonUtils;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

public class MemberMilkPurchaseRate extends BaseModel {

    private String code;
    private String description;
    private Short rateGenMethodCode;
    private String unionCode;
    private LocalDateTime wefDate;

    private Shift shift;
    private Shift shiftApplicable;
    private RateType rateType;
    private Society society;

    public MemberMilkPurchaseRate() {
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Short getRateGenMethodCode() {
        return rateGenMethodCode;
    }

    public void setRateGenMethodCode(Short rateGenMethodCode) {
        this.rateGenMethodCode = rateGenMethodCode;
    }

    public String getUnionCode() {
        return unionCode;
    }

    public void setUnionCode(String unionCode) {
        this.unionCode = unionCode;
    }

    public LocalDateTime getWefDate() {
        return wefDate;
    }

    public void setWefDate(LocalDateTime wefDate) {
        this.wefDate = wefDate;
    }

    public Shift getShift() {
        return shift;
    }

    public void setShift(Shift shift) {
        this.shift = shift;
    }

    public Shift getShiftApplicable() {
        return shiftApplicable;
    }

    public void setShiftApplicable(Shift shiftApplicable) {
        this.shiftApplicable = shiftApplicable;
    }

    public Society getSociety() {
        return society;
    }

    public void setSociety(Society society) {
        this.society = society;
    }

    public RateType getRateType() {
        return rateType;
    }

    public void setRateType(RateType rateType) {
        this.rateType = rateType;
    }

    @Override
    public String toString() {
        return this.wefDate.toString().replace("T"," ");
    }
}