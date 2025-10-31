package com.eipl.amcs.operation.procurement.dto;

import com.eipl.amcs.master.global.model.MilkType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@NoArgsConstructor
@Setter
@Getter
public class MilkDispatchSummaryDto {
    private MilkType milkType;
    private BigDecimal milkCollection;
    private BigDecimal milkSale;
    private BigDecimal milkBalance;
    private BigDecimal amount;
    private BigDecimal fat;
}
