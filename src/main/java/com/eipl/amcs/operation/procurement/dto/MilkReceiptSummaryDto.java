package com.eipl.amcs.operation.procurement.dto;

import com.eipl.amcs.master.global.model.MilkType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@NoArgsConstructor
@Setter
@Getter
public class MilkReceiptSummaryDto {
    private MilkType milkType;
    private BigDecimal milkCollection;
    private BigDecimal milkSale;
    private BigDecimal milkBalance;
    private BigDecimal amount;
    private BigDecimal fat;

//
//    public BigDecimal getAmount() {
//        return amount;
//    }
//
//    public void setAmount(BigDecimal amount) {
//        this.amount = amount;
//    }
//
//    public BigDecimal getFat() {
//        return fat;
//    }
//
//    public void setFat(BigDecimal fat) {
//        this.fat = fat;
//    }
//
//    public MilkReceiptSummaryDto() {
//    }
//
//    public MilkType getMilkType() {
//        return milkType;
//    }
//
//    public void setMilkType(MilkType milkType) {
//        this.milkType = milkType;
//    }
//
//    public BigDecimal getMilkCollection() {
//        return milkCollection;
//    }
//
//    public void setMilkCollection(BigDecimal milkCollection) {
//        this.milkCollection = milkCollection;
//    }
//
//    public BigDecimal getMilkSale() {
//        return milkSale;
//    }
//
//    public void setMilkSale(BigDecimal milkSale) {
//        this.milkSale = milkSale;
//    }
//
//    public BigDecimal getMilkBalance() {
//        return milkBalance;
//    }
//
//    public void setMilkBalance(BigDecimal milkBalance) {
//        this.milkBalance = milkBalance;
//    }
}
