package com.eipl.amcs.master.procurement.dto;

import java.math.BigDecimal;
import java.util.Map;

public class PurchaseRateGenerate {
    private BigDecimal fat;
    private Map<BigDecimal, BigDecimal> map;

    public PurchaseRateGenerate() {
    }

    public PurchaseRateGenerate(BigDecimal fat, Map<BigDecimal, BigDecimal> map) {
        super();
        this.fat = fat;
        this.map = map;
    }

    public BigDecimal getFat() {
        return fat;
    }

    public void setFat(BigDecimal fat) {
        this.fat = fat;
    }

    public Map<BigDecimal, BigDecimal> getMap() {
        return map;
    }

    public void setMap(Map<BigDecimal, BigDecimal> map) {
        this.map = map;
    }
}