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

//    public MilkDispatchRateAndDetailsDto() {
//    }
//
//    public SocietyMilkPurchaseRate getSocietyMilkPurchaseRate() {
//        return societyMilkPurchaseRate;
//    }
//
//    public void setSocietyMilkPurchaseRate(SocietyMilkPurchaseRate societyMilkPurchaseRate) {
//        this.societyMilkPurchaseRate = societyMilkPurchaseRate;
//    }
//
//    public Map<String, BigDecimal> getDetails() {
//        return details;
//    }
//
//    public void setDetails(Map<String, BigDecimal> details) {
//        this.details = details;
//    }
}
