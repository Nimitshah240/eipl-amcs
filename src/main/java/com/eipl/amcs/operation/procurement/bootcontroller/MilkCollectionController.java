package com.eipl.amcs.operation.procurement.bootcontroller;

import com.eipl.amcs.operation.billing.dto.MilkCollectionSummaryData;
import com.eipl.amcs.operation.procurement.dto.CollectionImportDto;
import com.eipl.amcs.operation.procurement.dto.MemberWiseCollectionDto;
import com.eipl.amcs.operation.procurement.dto.MilkCollectionPreReqDto;
import com.eipl.amcs.operation.procurement.model.AllowDcsManualCollectionRange;
import com.eipl.amcs.operation.procurement.model.DpuIncentiveRequest;
import com.eipl.amcs.operation.procurement.model.MilkCollection;
import com.eipl.amcs.operation.procurement.repository.AllowDcsManualCollectionRangeRepository;
import com.eipl.amcs.operation.procurement.repository.DpuIncentiveRequestRepository;
import com.eipl.amcs.operation.procurement.service.MilkCollectionService;
import com.eipl.amcs.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("milk_collection")
public class MilkCollectionController {

    @Autowired
    private MilkCollectionService service;
    @Autowired
    private AllowDcsManualCollectionRangeRepository allowDcsManualCollectionRangeRepository;
    @Autowired
    private DpuIncentiveRequestRepository dpuIncentiveRequestRepository;

    private static final DateTimeFormatter DATE_TIME_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @GetMapping
    public ResponseEntity<List<MilkCollection>> index(@RequestParam(name = "fromDate", required = false) String fromDate, @RequestParam(name = "toDate", required = false) String toDate, @RequestParam(name = "type", required = false) String type, @RequestParam(name = "code", required = false) String code, @RequestParam(name = "sync", required = false) int sync, @RequestParam(name = "dockNo", required = false) String dockNo, @RequestHeader Map<String, String> headers) {
        LocalDateTime fromDt = null, toDt = null;
        if (fromDate != null && !fromDate.isEmpty()) fromDt = LocalDateTime.parse(fromDate, DATE_TIME_FMT);
        if (toDate != null && !toDate.isEmpty()) toDt = LocalDateTime.parse(toDate, DATE_TIME_FMT);

        if (sync == 1) {
            service.findAllCollectionByDate(fromDt, toDt, CommonUtil.getIdentityHeader(headers));
            return null;
        }

        if (code != null)
            return new ResponseEntity<List<MilkCollection>>(service.findAllCollectionByMember(fromDt, toDt, code), HttpStatus.OK);

        if (dockNo != null)
            return new ResponseEntity<List<MilkCollection>>(service.findAllCollectionByDockNo(fromDt, toDt, dockNo), HttpStatus.OK);

        if (type == null || type.isEmpty()) {
            if (toDate == null)
                return new ResponseEntity<List<MilkCollection>>(service.findAllCollection(fromDt), HttpStatus.OK);

            return new ResponseEntity<List<MilkCollection>>(service.findAllBetween(fromDt, toDt), HttpStatus.OK);
        } else {
            List<MilkCollection> list = service.findAllBetween(fromDt, toDt);
            return new ResponseEntity<List<MilkCollection>>(list == null || list.isEmpty() ? list : list.stream().filter(p -> "SUMMARY".equalsIgnoreCase(p.getxCol1())).collect(Collectors.toList()), HttpStatus.OK);
        }

    }

    @GetMapping("/member-data")
    public ResponseEntity<List<MilkCollection>> indexByMemberAndDate(@RequestParam(name = "date") String date, @RequestParam(name = "code") String code) {
        LocalDateTime fromDt = null;
        if (date != null && !date.isEmpty()) {
            fromDt = LocalDateTime.parse(date, DATE_TIME_FMT);
            return new ResponseEntity<List<MilkCollection>>(service.findByMemberAndDate(fromDt, code), HttpStatus.OK);
        }
        return null;
    }

    @GetMapping("/Allow-data")
    public ResponseEntity<List<AllowDcsManualCollectionRange>> indexByMemberAndDate(@RequestParam(name = "fromDate", required = false) String fromDate) {
        LocalDateTime fromdate = LocalDateTime.parse(fromDate, DATE_TIME_FMT);
        return new ResponseEntity<List<AllowDcsManualCollectionRange>>(allowDcsManualCollectionRangeRepository.findByFromDateGreaterThanEqualAndToDateLessThanEqual(fromdate, fromdate), HttpStatus.OK);
    }

//    @GetMapping("/incentive-data")
//    public ResponseEntity<List<DpuIncentiveRequest>> indexByMemberAndDate() {
//        return new ResponseEntity<List<DpuIncentiveRequest>>(dpuIncentiveRequestRepository.findAll(), HttpStatus.OK);
//    }

    @GetMapping("/findByDate")
    public ResponseEntity<DpuIncentiveRequest> indexByDate(@RequestParam(name = "fromDate", required = false) String fDate) {
        LocalDate fromdate = LocalDate.parse(fDate, DATE_FMT);
//        return new ResponseEntity<List<DpuIncentiveRequest>>(dpuIncentiveRequestRepository.findByFromDateLessThanEqualAndToDateGreaterThanEqual(fromdate, fromdate), HttpStatus.OK);
        if (dpuIncentiveRequestRepository.findTop1ByOrderByCreatedAtDesc().isPresent())
            return new ResponseEntity<DpuIncentiveRequest>(dpuIncentiveRequestRepository.findTop1ByOrderByCreatedAtDesc().get(), HttpStatus.OK);
        else
            return null;
    }

    @GetMapping("/pre-req")
    public ResponseEntity<MilkCollectionPreReqDto> fetchPreRequisite(@RequestParam(name = "date", required = true) String date, @RequestParam(name = "shiftCode", required = true) Integer shiftCode, @RequestParam(name = "societyCode", required = true) String societyCode) {
        LocalDateTime dt = LocalDateTime.parse(date, DATE_TIME_FMT);
        return new ResponseEntity<MilkCollectionPreReqDto>(service.fetchPreRequsite(dt, shiftCode, societyCode), HttpStatus.OK);
    }

    @GetMapping("/avg")
    public ResponseEntity<Map<String, BigDecimal>> fetchAvgParameters(@RequestParam(name = "code", required = true) String code, @RequestParam(name = "no", required = true) String no, @RequestParam(name = "milktype", required = true) String milktype, @RequestParam(name = "date", required = true) String date, @RequestParam(name = "shiftCode", required = true) int shiftCode) {
        LocalDate d = LocalDate.parse(date);
        return new ResponseEntity<Map<String, BigDecimal>>(service.findAvgFatAndSnf(code, Integer.parseInt(no), milktype, d, shiftCode), HttpStatus.OK);
    }

    @GetMapping("/allinone")
    public ResponseEntity<MemberWiseCollectionDto> findAllInOne(@RequestParam(name = "code", required = true) String code, @RequestParam(name = "no", required = true) String no, @RequestParam(name = "milktype", required = true) String milktype, @RequestParam(name = "date", required = true) String date, @RequestParam(name = "paymentCycleCode", required = true) String paymentCycleCode, @RequestParam(name = "shiftCode", required = true) int shiftCode) {
        LocalDate d = LocalDate.parse(date);
        return new ResponseEntity<MemberWiseCollectionDto>(service.findAllInOne(code, Integer.parseInt(no), milktype, d, shiftCode, paymentCycleCode), HttpStatus.OK);
    }

    @GetMapping("/total")
    public ResponseEntity<Map<String, BigDecimal>> fetchTotals(@RequestParam(name = "code", required = true) String code, @RequestParam(name = "no", required = true) String no, @RequestParam(name = "milktype", required = true) int milktype) {
        return new ResponseEntity<Map<String, BigDecimal>>(service.findTotals(code, no, milktype), HttpStatus.OK);
    }

    @GetMapping("/next-sampleno")
    public ResponseEntity<Number> fetchNextSampleNo(@RequestParam(name = "date", required = true) String date,
                                                    @RequestParam(name = "dockCode", required = true) String dockCode,
                                                    @RequestParam(name = "milkType", required = false) Integer milkType) {
        LocalDateTime dt = LocalDateTime.parse(date, DATE_TIME_FMT);
        return new ResponseEntity<Number>(service.fetchNextSampleNo(dt, dockCode), HttpStatus.OK);
    }

//    @GetMapping("/next-sampleno")
//    public ResponseEntity<Number> fetchNextSampleNo(@RequestParam(name = "date", required = true) String date,@RequestParam(name = "dockCode", required = true) String dockCode, @RequestParam(name = "milkTypeCode", defaultValue = "0") int milkTypeCode) {
//        LocalDateTime dt = LocalDateTime.parse(date, DATE_TIME_FMT);
//        Number nextSampleNo = service.fetchNextSampleNo(dt, dockCode, milkTypeCode);
//        return new ResponseEntity<>(nextSampleNo, HttpStatus.OK);
//    }
    @PostMapping("/import")
    public ResponseEntity<List<CollectionImportDto>> importCollection(@RequestHeader Map<String, String> headers, @RequestBody List<MilkCollection> dtoList) {
        return new ResponseEntity<List<CollectionImportDto>>(service.importCollections(dtoList, CommonUtil.getIdentityHeader(headers)), HttpStatus.OK);
    }

    @PostMapping("/migrate")
    public ResponseEntity<List<CollectionImportDto>> migrateData(@RequestHeader Map<String, String> headers, @RequestBody List<MilkCollection> dtoList) {
        return new ResponseEntity<List<CollectionImportDto>>(service.migrateCollections(dtoList, CommonUtil.getIdentityHeader(headers)), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<MilkCollection> create(@RequestHeader Map<String, String> headers, @RequestBody MilkCollection collection) {
        return new ResponseEntity<MilkCollection>(service.save(collection, CommonUtil.getIdentityHeader(headers)), HttpStatus.CREATED);
    }

    @PutMapping()
    public ResponseEntity<MilkCollection> update(@RequestHeader Map<String, String> headers, @RequestBody MilkCollection collection) {
        return new ResponseEntity<MilkCollection>(service.update(collection, CommonUtil.getIdentityHeader(headers)), HttpStatus.OK);
    }

    @DeleteMapping("/{code}")
    public ResponseEntity<?> delete(@RequestHeader Map<String, String> headers, @PathVariable("code") String code) {
        try {
            Optional<MilkCollection> collectionData = service.findById(code);
            if (collectionData == null || !collectionData.isPresent())
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);

            service.delete(collectionData.get().getCode(), CommonUtil.getIdentityHeader(headers));
            return new ResponseEntity<>(null, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/collection-summary-data")
    public ResponseEntity<MilkCollectionSummaryData> insertMilkCollectionSummaryData(@RequestHeader Map<String, String> headers, @RequestBody MilkCollectionSummaryData data) {
        return new ResponseEntity<MilkCollectionSummaryData>(service.saveMilkCollectionSummaryData(data, CommonUtil.getIdentityHeader(headers)), HttpStatus.CREATED);
    }

    @PutMapping("/collection-summary-data")
    public ResponseEntity<MilkCollectionSummaryData> updateMilkCollectionSummaryData(@RequestHeader Map<String, String> headers, @RequestBody MilkCollectionSummaryData data) {
        return new ResponseEntity<MilkCollectionSummaryData>(service.updateMilkCollectionSummaryData(data, CommonUtil.getIdentityHeader(headers)), HttpStatus.CREATED);
    }

    @PostMapping("/collection-summary-data/import")
    public ResponseEntity<List<CollectionImportDto>> insertMilkCollectionSummaryDataImport(@RequestHeader Map<String, String> headers, @RequestBody List<MilkCollectionSummaryData> data) {
        return new ResponseEntity<List<CollectionImportDto>>(service.importCollectionSummaryData(data, CommonUtil.getIdentityHeader(headers)), HttpStatus.OK);
    }


    @GetMapping("total_amount")
    public ResponseEntity<BigDecimal> fetchTotal(@RequestParam(name = "societyPaymentCycleCode", required = true) String societyPaymentCycleCode, @RequestParam(name = "memberCode", required = true) String memberCode) {
        return new ResponseEntity<BigDecimal>(service.findTotalAmount(societyPaymentCycleCode, memberCode), HttpStatus.OK);
    }

}
