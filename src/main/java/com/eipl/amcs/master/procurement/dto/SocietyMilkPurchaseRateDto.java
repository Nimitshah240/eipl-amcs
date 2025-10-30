package com.eipl.amcs.master.procurement.dto;

import com.eipl.amcs.master.procurement.model.SocietyMilkPurchaseRate;
import com.eipl.amcs.master.procurement.model.SocietyMilkPurchaseRateApplicability;
import com.eipl.amcs.master.procurement.model.SocietyMilkPurchaseRateBased;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


@NoArgsConstructor
@Setter
@Getter
public class SocietyMilkPurchaseRateDto {
    private SocietyMilkPurchaseRate purchaseRate;
    private List<SocietyMilkPurchaseRateApplicability> listApplicability;
    private List<SocietyMilkPurchaseRateBased> listRateBased;
    private List<String> listDetail;
}
