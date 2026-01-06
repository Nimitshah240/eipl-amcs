package com.eipl.amcs.operation.billing.dto;

import com.eipl.amcs.master.global.model.MilkQualityType;
import com.eipl.amcs.master.global.model.MilkType;
import com.eipl.amcs.master.operation.model.Member;
import com.eipl.amcs.master.org.model.Dock;
import com.eipl.amcs.master.org.model.Society;
import com.eipl.amcs.master.procurement.model.SocietyPaymentCycle;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class MilkCollectionSummaryData {
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


    public MilkCollectionSummaryData(Member member, MilkType milkType, BigDecimal milkQuantity, BigDecimal milkAmount) {
        this.member = member;
        this.milkType = milkType;
        this.milkQuantity = milkQuantity;
        this.milkAmount = milkAmount;
    }
}
