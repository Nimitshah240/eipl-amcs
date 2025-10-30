package com.eipl.amcs.master.procurement.dto;

import com.eipl.amcs.master.procurement.model.MemberMilkPurchaseRate;
import com.eipl.amcs.master.procurement.model.MemberMilkPurchaseRateApplicability;
import com.eipl.amcs.master.procurement.model.MemberMilkPurchaseRateBased;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@Setter
@Getter
public class MemberMilkPurchaseRateDto {
    private MemberMilkPurchaseRate purchaseRate;
    private List<MemberMilkPurchaseRateApplicability> listApplicability;
    private List<MemberMilkPurchaseRateBased> listRateBased;
    private List<String> listDetail;
}
