package com.eipl.amcs.master.procurement.dto;

import com.eipl.amcs.master.procurement.model.MemberMilkPurchaseRate;
import com.eipl.amcs.master.procurement.model.SocietyMilkPurchaseRate;

public class RateViewDto {
    private short rateType;
    private MemberMilkPurchaseRate memberRate;
    private SocietyMilkPurchaseRate societyRate;

    public RateViewDto() {
    }

    public short getRateType() {
        return rateType;
    }

    public void setRateType(short rateType) {
        this.rateType = rateType;
    }

    public MemberMilkPurchaseRate getMemberRate() {
        return memberRate;
    }

    public void setMemberRate(MemberMilkPurchaseRate memberRate) {
        this.memberRate = memberRate;
    }

    public SocietyMilkPurchaseRate getSocietyRate() {
        return societyRate;
    }

    public void setSocietyRate(SocietyMilkPurchaseRate societyRate) {
        this.societyRate = societyRate;
    }
}
