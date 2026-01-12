package com.eipl.amcs.operation.procurement.service;

import com.eipl.amcs.master.operation.model.Member;
import com.eipl.amcs.operation.billing.dto.MilkCollectionSummaryData;
import com.eipl.amcs.operation.procurement.dto.CollectionImportDto;
import com.eipl.amcs.operation.procurement.dto.MemberWiseCollectionDto;
import com.eipl.amcs.operation.procurement.dto.MilkCollectionPreReqDto;
import com.eipl.amcs.operation.procurement.model.MilkCollection;
import org.springframework.data.domain.Sort;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface MilkCollectionService {

    List<MilkCollection> findAllByMember(String code);

    List<MilkCollection> findAllBetween(LocalDateTime fromDt, LocalDateTime toDt);

    List<MilkCollectionSummaryData> findTop10MemberSummaries(int year, int month, Integer selectedMilkTypeCode);

    List<MilkCollection> findAllCollectionByDate(LocalDateTime fromDt, LocalDateTime toDt, String heades);

    MilkCollection save(MilkCollection collection, String identityInfo);

    MilkCollection update(MilkCollection collection, String identityInfo);

    void delete(String code, String identityInfo);

    MilkCollectionPreReqDto fetchPreRequsite(LocalDateTime date, Integer shiftCode, String societyCode);

    Number fetchNextSampleNo(LocalDateTime dt, String dockCode);

    List<CollectionImportDto> importCollections(List<MilkCollection> dtoList, String header);

    List<CollectionImportDto> migrateCollections(List<MilkCollection> dtoList, String header);

    List<MilkCollection> findAllCollection(LocalDateTime date);

    Optional<MilkCollection> findById(String code);

    Map<String, BigDecimal> findAvgFatAndSnf(String code, int no, String milktype, LocalDate date, int shiftCode);

    Map<String, BigDecimal> findTotals(String code, String no, int milkType);

    List<MilkCollection> findByMemberAndDate(LocalDateTime date, String code);

    MilkCollectionSummaryData saveMilkCollectionSummaryData(MilkCollectionSummaryData collection, String identityInfo);

    List<MilkCollection> findAllSummaryDataBetween(LocalDateTime fromDt, LocalDateTime toDt);

    MilkCollectionSummaryData updateMilkCollectionSummaryData(MilkCollectionSummaryData data, String identityHeader);

    List<CollectionImportDto> importCollectionSummaryData(List<MilkCollectionSummaryData> data, String identityHeader);

    List<MilkCollection> findAllCollectionByMember(LocalDateTime fromDt, LocalDateTime toDate, String code);

    BigDecimal findTotalAmount(String societyPaymentCycle, String code);

    List<MilkCollection> findAllCollectionByDockNo(LocalDateTime fromDt, LocalDateTime toDt, String dockNo);

    MemberWiseCollectionDto findAllInOne(String code, int parseInt, String milktype, LocalDate d, int shiftCode, String paymentCycleCode);
}
