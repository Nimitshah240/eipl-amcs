package com.eipl.amcs.master.procurement.service;

import com.eipl.amcs.exception.BusinessValidationFailException;
import com.eipl.amcs.master.procurement.dto.MemberMilkPurchaseRateDto;
import com.eipl.amcs.master.procurement.model.MemberMilkPurchaseRate;
import com.eipl.amcs.master.procurement.model.MemberMilkPurchaseRateBased;
import com.eipl.amcs.operation.procurement.dto.MilkRateAndDetailsDto;

import java.util.List;

public interface MemberMilkPurchaseRateService {
    List<MemberMilkPurchaseRate> findAll();

    String savePurchaseRate(MemberMilkPurchaseRateDto dto) throws BusinessValidationFailException;

    List<String> fetchRateDetails(String code, Integer milkTypeCode, Integer milkQualityTypeCode);

    MilkRateAndDetailsDto fetchRateAndDetails(String code);

    List<MemberMilkPurchaseRateBased> fetchRateBased(String code);
}
