package com.eipl.amcs.master.procurement.service;

import com.eipl.amcs.exception.BusinessValidationFailException;
import com.eipl.amcs.master.procurement.dto.SocietyMilkPurchaseRateDto;
import com.eipl.amcs.master.procurement.model.SocietyMilkPurchaseRate;
import com.eipl.amcs.master.procurement.model.SocietyMilkPurchaseRateBased;
import com.eipl.amcs.operation.procurement.dto.MilkDispatchRateAndDetailsDto;

import java.util.List;

public interface SocietyMilkPurchaseRateService {
    List<SocietyMilkPurchaseRate> findAll();

    List<String> fetchRateDetails(String code, Integer milkTypeCode, Integer milkQualityTypeCode);

    MilkDispatchRateAndDetailsDto fetchRateAndDetails(String code);

    String savePurchaseRate(SocietyMilkPurchaseRateDto dto) throws BusinessValidationFailException;

    List<SocietyMilkPurchaseRateBased> fetchRateBased(String code);
}
