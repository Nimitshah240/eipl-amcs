package com.eipl.amcs.operation.procurement.dto;

import com.eipl.amcs.master.procurement.model.SocietyMilkPurchaseRate;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Map;

@NoArgsConstructor
@Setter
@Getter
public class MilkDispatchRateAndDetailsDto {

    @JsonIgnoreProperties(value = {"shift", "shiftApplicable", "rateType"})
    private SocietyMilkPurchaseRate societyMilkPurchaseRate;
    private Map<String, BigDecimal> details;
}
