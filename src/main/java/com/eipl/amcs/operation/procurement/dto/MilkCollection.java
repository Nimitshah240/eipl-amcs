package com.eipl.amcs.operation.procurement.dto;

import com.eipl.amcs.base.model.BaseModelTxn;
import com.eipl.amcs.master.global.model.MilkQualityType;
import com.eipl.amcs.master.global.model.MilkType;
import com.eipl.amcs.master.global.model.Shift;
import com.eipl.amcs.master.operation.model.Member;
import com.eipl.amcs.master.org.model.Dock;
import com.eipl.amcs.master.org.model.Society;
import com.eipl.amcs.master.procurement.model.SocietyPaymentCycle;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class MilkCollection extends BaseModelTxn {
    private String code;
    private int sampleNo;
    private LocalDateTime collectionDate;
    private BigDecimal fat;
    private BigDecimal snf;
    private BigDecimal clr;
    private BigDecimal water;
    private BigDecimal density;
    private BigDecimal lectose;
    private BigDecimal protein;
    private BigDecimal rtpl;
    private BigDecimal qty;
    private BigDecimal amount;
    private boolean weightAuto;
    private boolean qualityAuto;
    private boolean avgParam;
    private LocalDateTime qualityAt;
    private LocalDateTime weightAt;
    private String voucherNo;
    private String rateCode;
    private String wsCode;
    private String analyserCode;
    private String unionCode;
    private int qtyMode;
    private BigDecimal convertedQty;
    private int convertedQtyMode;

    private SocietyPaymentCycle societyPaymentCycle;
    private Member member;
    private Shift shift;
    private MilkType milkType;
    private MilkQualityType milkQualityType;
    private Society society;
    private Dock dock;

    @JsonIgnore
    private BigDecimal newRate;
    @JsonIgnore
    private BigDecimal newAmount;

    private String xCol4;
    private String xCol5;

    public MilkCollection() {
    }

    public String getxCol4() {
        return xCol4;
    }

    public void setxCol4(String xCol4) {
        this.xCol4 = xCol4;
    }

    public String getxcol4() {
        return xCol4;
    }

    public String getxCol5() {
        return xCol5;
    }

    public void setxCol5(String xCol5) {
        this.xCol5 = xCol5;
    }

    public String getxcol5() {
        return xCol5;
    }

    public BigDecimal getNewRate() {
        return newRate;
    }

    public void setNewRate(BigDecimal newRate) {
        this.newRate = newRate;
    }

    public BigDecimal getNewAmount() {
        return newAmount;
    }

    public void setNewAmount(BigDecimal newAmount) {
        this.newAmount = newAmount;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getSampleNo() {
        return sampleNo;
    }

    public void setSampleNo(int sampleNo) {
        this.sampleNo = sampleNo;
    }

    public LocalDateTime getCollectionDate() {
        return collectionDate;
    }

    public void setCollectionDate(LocalDateTime collectionDate) {
        this.collectionDate = collectionDate;
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

    public BigDecimal getClr() {
        return clr;
    }

    public void setClr(BigDecimal clr) {
        this.clr = clr;
    }

    public BigDecimal getWater() {
        return water;
    }

    public void setWater(BigDecimal water) {
        this.water = water;
    }

    public BigDecimal getDensity() {
        return density;
    }

    public void setDensity(BigDecimal density) {
        this.density = density;
    }

    public BigDecimal getLectose() {
        return lectose;
    }

    public void setLectose(BigDecimal lectose) {
        this.lectose = lectose;
    }

    public BigDecimal getProtein() {
        return protein;
    }

    public void setProtein(BigDecimal protein) {
        this.protein = protein;
    }

    public BigDecimal getRtpl() {
        return rtpl;
    }

    public void setRtpl(BigDecimal rtpl) {
        this.rtpl = rtpl;
    }

    public BigDecimal getQty() {
        return qty;
    }

    public void setQty(BigDecimal qty) {
        this.qty = qty;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public boolean isWeightAuto() {
        return weightAuto;
    }

    public void setWeightAuto(boolean weightAuto) {
        this.weightAuto = weightAuto;
    }

    public boolean isQualityAuto() {
        return qualityAuto;
    }

    public void setQualityAuto(boolean qualityAuto) {
        this.qualityAuto = qualityAuto;
    }

    public boolean isAvgParam() {
        return avgParam;
    }

    public void setAvgParam(boolean avgParam) {
        this.avgParam = avgParam;
    }

    public LocalDateTime getQualityAt() {
        return qualityAt;
    }

    public void setQualityAt(LocalDateTime qualityAt) {
        this.qualityAt = qualityAt;
    }

    public LocalDateTime getWeightAt() {
        return weightAt;
    }

    public void setWeightAt(LocalDateTime weightAt) {
        this.weightAt = weightAt;
    }

    public String getVoucherNo() {
        return voucherNo;
    }

    public void setVoucherNo(String voucherNo) {
        this.voucherNo = voucherNo;
    }

    public String getRateCode() {
        return rateCode;
    }

    public void setRateCode(String rateCode) {
        this.rateCode = rateCode;
    }

    public String getWsCode() {
        return wsCode;
    }

    public void setWsCode(String wsCode) {
        this.wsCode = wsCode;
    }

    public String getAnalyserCode() {
        return analyserCode;
    }

    public void setAnalyserCode(String analyserCode) {
        this.analyserCode = analyserCode;
    }

    public String getUnionCode() {
        return unionCode;
    }

    public void setUnionCode(String unionCode) {
        this.unionCode = unionCode;
    }

    public int getQtyMode() {
        return qtyMode;
    }

    public void setQtyMode(int qtyMode) {
        this.qtyMode = qtyMode;
    }

    public BigDecimal getConvertedQty() {
        return convertedQty;
    }

    public void setConvertedQty(BigDecimal convertedQty) {
        this.convertedQty = convertedQty;
    }

    public int getConvertedQtyMode() {
        return convertedQtyMode;
    }

    public void setConvertedQtyMode(int convertedQtyMode) {
        this.convertedQtyMode = convertedQtyMode;
    }

    public SocietyPaymentCycle getSocietyPaymentCycle() {
        return societyPaymentCycle;
    }

    public void setSocietyPaymentCycle(SocietyPaymentCycle societyPaymentCycle) {
        this.societyPaymentCycle = societyPaymentCycle;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public Shift getShift() {
        return shift;
    }

    public void setShift(Shift shift) {
        this.shift = shift;
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

    public Society getSociety() {
        return society;
    }

    public void setSociety(Society society) {
        this.society = society;
    }

    public Dock getDock() {
        return dock;
    }

    public void setDock(Dock dock) {
        this.dock = dock;
    }
}
