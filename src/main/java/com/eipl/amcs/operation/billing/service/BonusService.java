package com.eipl.amcs.operation.billing.service;

import com.eipl.amcs.operation.billing.dto.BonusDto;
import com.eipl.amcs.operation.billing.model.Bonus;
import com.eipl.amcs.operation.billing.model.BonusSummary;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface BonusService {

    List<BonusSummary> findBonusSummaryBetWeen();

    BonusSummary save(String identityInfo, BonusSummary bonusSummary);

    BonusDto updateDto(String identityInfo, BonusDto dto);

    List<Bonus> loadData(LocalDateTime fromDate, LocalDateTime toDate, Integer milkType);

    Map<String,Object> loadDataBonus(LocalDate fromDate, LocalDate toDate, String memberCode);

    List<Map<String, Object>>  loadDataBonusSummary(LocalDate fromDate, LocalDate toDate);

    BonusDto saveDto(String identityInfo, BonusDto dto, short s);

    BonusDto findBySummary(String code);

    Boolean deleteDto(String identityInfo, String dto);

    BonusDto editDto(String identityInfo, BonusDto dto);

}
