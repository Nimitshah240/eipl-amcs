package com.eipl.amcs.operation.procurement.dto;

import com.eipl.amcs.master.global.model.MilkType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CollectionSummary {
    private MilkType milkType;
    private int memberCount;
    private BigDecimal qty;
    private BigDecimal avgFat;
    private BigDecimal amount;
    private BigDecimal avgSnf;

    public CollectionSummary(MilkType milkType, int memberCount, BigDecimal qty, BigDecimal amount) {
        this.milkType = milkType;
        this.memberCount = memberCount;
        this.qty = qty;
        this.amount = amount;
    }
}
