package com.eipl.amcs.master.procurement.dto;

import com.eipl.amcs.base.model.BaseModel;
import com.eipl.amcs.master.global.model.Shift;
import com.eipl.amcs.master.org.model.Society;
import com.eipl.amcs.master.procurement.model.MemberMilkPurchaseRate;

import java.time.LocalDateTime;


public class MemberMilkPurchaseRateApplicability extends BaseModel {

    private String code;
    private LocalDateTime wefDate;
    private String unionCode;

    private Shift shift;
    private MemberMilkPurchaseRate memberMilkPurchaseRate;
    private Society society;

    public MemberMilkPurchaseRateApplicability() {

    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public LocalDateTime getWefDate() {
        return wefDate;
    }

    public void setWefDate(LocalDateTime wefDate) {
        this.wefDate = wefDate;
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

    public MemberMilkPurchaseRate getMemberMilkPurchaseRate() {
        return memberMilkPurchaseRate;
    }

    public void setMemberMilkPurchaseRate(MemberMilkPurchaseRate memberMilkPurchaseRate) {
        this.memberMilkPurchaseRate = memberMilkPurchaseRate;
    }

    public Society getSociety() {
        return society;
    }

    public void setSociety(Society society) {
        this.society = society;
    }
}