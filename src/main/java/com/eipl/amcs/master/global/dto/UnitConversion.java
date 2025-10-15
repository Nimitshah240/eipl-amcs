package com.eipl.amcs.master.global.dto;

import com.eipl.amcs.base.model.BaseModel;

import java.math.BigDecimal;

public class UnitConversion extends BaseModel {

    private Integer code;
    private BigDecimal conversionFactor;

    private Unit fromUnit;
    private Unit toUnit;

    public UnitConversion() {
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public BigDecimal getConversionFactor() {
        return conversionFactor;
    }

    public void setConversionFactor(BigDecimal conversionFactor) {
        this.conversionFactor = conversionFactor;
    }

    public Unit getFromUnit() {
        return fromUnit;
    }

    public void setFromUnit(Unit fromUnit) {
        this.fromUnit = fromUnit;
    }

    public Unit getToUnit() {
        return toUnit;
    }

    public void setToUnit(Unit toUnit) {
        this.toUnit = toUnit;
    }
}