package com.eipl.amcs.operation.procurement.dto;

import com.eipl.amcs.master.global.model.MilkType;

import java.math.BigDecimal;

public class CollectionSummary {
    private MilkType milkType;
    private int memberCount;
    private BigDecimal qty;
    private BigDecimal avgFat;
    private BigDecimal amount;
    private BigDecimal avgSnf;

    public CollectionSummary() {
    }

    public CollectionSummary(MilkType milkType, int memberCount, BigDecimal qty, BigDecimal amount) {
        this.milkType = milkType;
        this.memberCount = memberCount;
        this.qty = qty;
        this.amount = amount;
    }

    public CollectionSummary(MilkType milkType, int memberCount, BigDecimal qty, BigDecimal avgFat, BigDecimal avgSnf, BigDecimal amount) {
        this.milkType = milkType;
        this.memberCount = memberCount;
        this.qty = qty;
        this.avgFat = avgFat;
        this.avgSnf = avgSnf;
        this.amount = amount;
    }

    public BigDecimal getAvgSnf() {
        return avgSnf;
    }

    public void setAvgSnf(BigDecimal avgSnf) {
        this.avgSnf = avgSnf;
    }

    public BigDecimal getAvgFat() {
        return avgFat;
    }

    public void setAvgFat(BigDecimal avgFat) {
        this.avgFat = avgFat;
    }

    public MilkType getMilkType() {
        return milkType;
    }

    public void setMilkType(MilkType milkType) {
        this.milkType = milkType;
    }

    public int getMemberCount() {
        return memberCount;
    }

    public void setMemberCount(int memberCount) {
        this.memberCount = memberCount;
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
}
