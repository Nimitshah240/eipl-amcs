package com.eipl.amcs.operation.procurement.dto;

import com.eipl.amcs.master.procurement.model.MemberMilkPurchaseRate;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Map;

@NoArgsConstructor
@Setter
@Getter
public class MilkRateAndDetailsDto {
    private MemberMilkPurchaseRate memberPurchaseRate;
    private Map<String, BigDecimal> details;
}
