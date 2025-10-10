package com.eipl.amcs.master.procurement.service;

import com.eipl.amcs.exception.BusinessValidationFailException;
import com.eipl.amcs.master.procurement.model.LocalMilkSaleRate;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface LocalMilkSaleRateService {
    List<LocalMilkSaleRate> findAll();

    LocalMilkSaleRate save(LocalMilkSaleRate localMilkSaleRate, String identityInfo) throws BusinessValidationFailException;

    LocalMilkSaleRate update(LocalMilkSaleRate localMilkSaleRate, String identityInfo);

    Optional<LocalMilkSaleRate> findById(String code);

    void delete(String code, String identityInfo);

    void delete(LocalMilkSaleRate localMilkSaleRate, String identityInfo);

    LocalDate fetchLastestDate(String str1, Integer i1, Integer i2);

    LocalMilkSaleRate fetchRate(LocalDate date, Integer milkType, Integer milkClass);

}
