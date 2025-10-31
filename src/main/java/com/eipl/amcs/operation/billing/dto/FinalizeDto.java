package com.eipl.amcs.operation.billing.dto;

import com.eipl.amcs.master.procurement.model.SocietyPaymentCycle;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class FinalizeDto {
    private SocietyPaymentCycle paymentCycle;
    private List<String> memberCodeList;
}
