package com.eipl.amcs.operation.billing.dto;

import com.eipl.amcs.master.global.model.MilkQualityType;
import com.eipl.amcs.master.global.model.MilkType;
import com.eipl.amcs.master.operation.model.Member;
import com.eipl.amcs.master.org.model.Dock;
import com.eipl.amcs.master.org.model.Society;
import com.eipl.amcs.master.org.model.Union;
import com.eipl.amcs.master.procurement.model.SocietyPaymentCycle;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

public class MilkSummaryDataEntry {

    private LocalDate date;
    private SocietyPaymentCycle paymentCycle;
    private Member member;
    private MilkType milkType;
    private BigDecimal milkQuantity;
    private BigDecimal milkAmount;
    private Dock dock;
    private Society society;
    private MilkQualityType milkQualityType;
    private String union;

    public MilkSummaryDataEntry() {
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public SocietyPaymentCycle getPaymentCycle() {
        return paymentCycle;
    }

    public void setPaymentCycle(SocietyPaymentCycle paymentCycle) {
        this.paymentCycle = paymentCycle;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public MilkType getMilkType() {
        return milkType;
    }

    public void setMilkType(MilkType milkType) {
        this.milkType = milkType;
    }

    public BigDecimal getMilkQuantity() {
        return milkQuantity;
    }

    public void setMilkQuantity(BigDecimal milkQuantity) {
        this.milkQuantity = milkQuantity;
    }

    public BigDecimal getMilkAmount() {
        return milkAmount;
    }

    public void setMilkAmount(BigDecimal milkAmount) {
        this.milkAmount = milkAmount;
    }

    public Dock getDock() {
        return dock;
    }

    public void setDock(Dock dock) {
        this.dock = dock;
    }

    public Society getSociety() {
        return society;
    }

    public void setSociety(Society society) {
        this.society = society;
    }

    public MilkQualityType getMilkQualityType() {
        return milkQualityType;
    }

    public void setMilkQualityType(MilkQualityType milkQualityType) {
        this.milkQualityType = milkQualityType;
    }

    public String getUnion() {
        return union;
    }

    public void setUnion(String union) {
        this.union = union;
    }
}
