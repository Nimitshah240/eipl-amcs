package com.eipl.amcs.operation.procurement.dto;

import com.eipl.amcs.base.model.BaseModel;
import com.eipl.amcs.master.global.model.MilkQualityType;
import com.eipl.amcs.master.global.model.MilkType;

import java.math.BigDecimal;
import com.eipl.amcs.operation.procurement.model.MilkReceipt;

public class MilkReceiptTransaction extends BaseModel {

    private String txnCode;
    private String societyPurchaseRateCode;
    private BigDecimal avgClr;
    private BigDecimal avgFat;
    private BigDecimal avgSnf;
    private BigDecimal rate;
    private BigDecimal amount;
    private BigDecimal acidity;
    private BigDecimal density;
    private BigDecimal freezingPoint;
    private BigDecimal lactose;
    private BigDecimal protein;
    private BigDecimal temp;
    private BigDecimal water;
    private BigDecimal convertedQuantity;
    private BigDecimal qty;
    private Integer quantityMode;
    private Integer convertedQuantityMode;
    private MilkReceipt milkDispatch;
    private MilkQualityType milkQualityType;
    private MilkType milkType;


    public MilkReceipt getMilkDispatch() {
        return milkDispatch;
    }

    public void setMilkDispatch(MilkReceipt milkDispatch) {
        this.milkDispatch = milkDispatch;
    }

    public String getTxnCode() {
        return txnCode;
    }

    public void setTxnCode(String txnCode) {
        this.txnCode = txnCode;
    }



    public BigDecimal getAcidity() {
        return acidity;
    }

    public void setAcidity(BigDecimal acidity) {
        this.acidity = acidity;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getAvgClr() {
        return avgClr;
    }

    public void setAvgClr(BigDecimal avgClr) {
        this.avgClr = avgClr;
    }

    public BigDecimal getAvgFat() {
        return avgFat;
    }

    public void setAvgFat(BigDecimal avgFat) {
        this.avgFat = avgFat;
    }

    public BigDecimal getAvgSnf() {
        return avgSnf;
    }

    public void setAvgSnf(BigDecimal avgSnf) {
        this.avgSnf = avgSnf;
    }

    public BigDecimal getDensity() {
        return density;
    }

    public void setDensity(BigDecimal density) {
        this.density = density;
    }

    public BigDecimal getFreezingPoint() {
        return freezingPoint;
    }

    public void setFreezingPoint(BigDecimal freezingPoint) {
        this.freezingPoint = freezingPoint;
    }

    public BigDecimal getLactose() {
        return lactose;
    }

    public void setLactose(BigDecimal lactose) {
        this.lactose = lactose;
    }

    public BigDecimal getProtein() {
        return protein;
    }

    public void setProtein(BigDecimal protein) {
        this.protein = protein;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public BigDecimal getTemp() {
        return temp;
    }

    public void setTemp(BigDecimal temp) {
        this.temp = temp;
    }

    public BigDecimal getWater() {
        return water;
    }

    public void setWater(BigDecimal water) {
        this.water = water;
    }

    public BigDecimal getConvertedQuantity() {
        return convertedQuantity;
    }

    public void setConvertedQuantity(BigDecimal convertedQuantity) {
        this.convertedQuantity = convertedQuantity;
    }

    public BigDecimal getQty() {
        return qty;
    }

    public void setQty(BigDecimal qty) {
        this.qty = qty;
    }


    public MilkQualityType getMilkQualityType() {
        return milkQualityType;
    }

    public void setMilkQualityType(MilkQualityType milkQualityType) {
        this.milkQualityType = milkQualityType;
    }

    public MilkType getMilkType() {
        return milkType;
    }

    public void setMilkType(MilkType milkType) {
        this.milkType = milkType;
    }

    public Integer getQuantityMode() {
        return quantityMode;
    }

    public void setQuantityMode(Integer quantityMode) {
        this.quantityMode = quantityMode;
    }

    public Integer getConvertedQuantityMode() {
        return convertedQuantityMode;
    }

    public void setConvertedQuantityMode(Integer convertedQuantityMode) {
        this.convertedQuantityMode = convertedQuantityMode;
    }

    public String getSocietyPurchaseRateCode() {
        return societyPurchaseRateCode;
    }

    public void setSocietyPurchaseRateCode(String societyPurchaseRateCode) {
        this.societyPurchaseRateCode = societyPurchaseRateCode;
    }
}
