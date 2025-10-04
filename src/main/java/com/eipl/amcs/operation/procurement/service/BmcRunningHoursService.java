package com.eipl.amcs.operation.procurement.service;

import com.eipl.amcs.operation.procurement.model.BmcRunningHours;

import java.time.LocalDate;
import java.util.List;

public interface BmcRunningHoursService {
    List<BmcRunningHours> getAllBmcRunningHours();

    List<BmcRunningHours> findAllBetween(LocalDate date, LocalDate date1);

    BmcRunningHours getBmcRunningHoursByCode(Long code);

    BmcRunningHours createBmcRunningHours(BmcRunningHours bmcRunningHours);

    void deleteBmcRunningHours(Long code);

    BmcRunningHours update(BmcRunningHours bmcRunningHours);

    BmcRunningHours save(BmcRunningHours bmcRunningHours);
}

