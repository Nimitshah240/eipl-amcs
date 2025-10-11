package com.eipl.amcs.master.procurement.dto;

import com.eipl.amcs.base.model.BaseModelTxn;
import com.eipl.amcs.master.global.model.MilkQualityType;
import com.eipl.amcs.master.global.model.MilkType;
import com.eipl.amcs.master.operation.model.Formula;
import com.eipl.amcs.master.procurement.model.SocietyMilkPurchaseRate;

import java.math.BigDecimal;

public class SocietyMilkPurchaseRateBased extends BaseModelTxn {
    private String code; // Purchase Rate Code + AI

    private int rateType; // 1-FAT, 2-FAT+SNF
    private int qualityParam; // 1-FAT, 2-SNF, 3-CLR, 4-TS
    private BigDecimal startVal;
    private BigDecimal endVal;
    private BigDecimal kgRate;

    private int deductionType; //0-NA, 1-Value Addition, 2-Value Deduction,3-Percentage Addition, 4-Percentage Deduction, 5-Per Rate Add, 6-Per Rate Ded
    private int refType; //0-NA, 1-Fixed Point, 2-Actual, 3-KgRate
    private BigDecimal val;
    private BigDecimal fixedPoint;
    private int step;

    private Formula formula;
    private MilkType milkType;
    private MilkQualityType milkQualityType;
    private SocietyMilkPurchaseRate societyMilkPurchaseRate;

    public SocietyMilkPurchaseRateBased() {
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getRateType() {
        return rateType;
    }

    public void setRateType(int rateType) {
        this.rateType = rateType;
    }

    public int getQualityParam() {
        return qualityParam;
    }

    public void setQualityParam(int qualityParam) {
        this.qualityParam = qualityParam;
    }

    public BigDecimal getStartVal() {
        return startVal;
    }

    public void setStartVal(BigDecimal startVal) {
        this.startVal = startVal;
    }

    public BigDecimal getEndVal() {
        return endVal;
    }

    public void setEndVal(BigDecimal endVal) {
        this.endVal = endVal;
    }

    public BigDecimal getKgRate() {
        return kgRate;
    }

    public void setKgRate(BigDecimal kgRate) {
        this.kgRate = kgRate;
    }

    public int getDeductionType() {
        return deductionType;
    }

    public void setDeductionType(int deductionType) {
        this.deductionType = deductionType;
    }

    public int getRefType() {
        return refType;
    }

    public void setRefType(int refType) {
        this.refType = refType;
    }

    public BigDecimal getVal() {
        return val;
    }

    public void setVal(BigDecimal val) {
        this.val = val;
    }

    public BigDecimal getFixedPoint() {
        return fixedPoint;
    }

    public void setFixedPoint(BigDecimal fixedPoint) {
        this.fixedPoint = fixedPoint;
    }

    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        this.step = step;
    }

    public Formula getFormula() {
        return formula;
    }

    public void setFormula(Formula formula) {
        this.formula = formula;
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

    public SocietyMilkPurchaseRate getSocietyMilkPurchaseRate() {
        return societyMilkPurchaseRate;
    }

    public void setSocietyMilkPurchaseRate(SocietyMilkPurchaseRate societyMilkPurchaseRate) {
        this.societyMilkPurchaseRate = societyMilkPurchaseRate;
    }
}
