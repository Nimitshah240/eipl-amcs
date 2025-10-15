package com.eipl.amcs.master.global.dto;

import com.eipl.amcs.utils.CommonUtils;

public class RateType {
    private Integer code;
    private String rateType;

    public RateType() {
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getRateType() {
        return rateType;
    }

    public void setRateType(String rateType) {
        this.rateType = rateType;
    }
    @Override
    public String toString() {
        return CommonUtils.getLocalString(this.rateType, null);
    }
}
