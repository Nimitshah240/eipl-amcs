package com.eipl.amcs.operation.procurement.dto;

import com.eipl.amcs.master.global.model.MilkType;

import java.math.BigDecimal;

public class MilkDispatchReceiptWrapper {

    private MilkType milkType;
    private BigDecimal purchase,sale,difference;

    public MilkType getMilkType() {
        return milkType;
    }

    public void setMilkType(MilkType milkType) {
        this.milkType = milkType;
    }

    public BigDecimal getPurchase() {
        return purchase;
    }

    public void setPurchase(BigDecimal purchase) {
        this.purchase = purchase;
    }

    public BigDecimal getSale() {
        return sale;
    }

    public void setSale(BigDecimal sale) {
        this.sale = sale;
    }

    public BigDecimal getDifference() {
        return difference;
    }

    public void setDifference(BigDecimal difference) {
        this.difference = difference;
    }
}
