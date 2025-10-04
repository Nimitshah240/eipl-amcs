package com.eipl.amcs.operation.procurement.bootcontroller;

import com.eipl.amcs.base.repository.NextCodeRepository;
import com.eipl.amcs.master.global.model.Shift;
import com.eipl.amcs.master.global.repository.ShiftRepository;
import com.eipl.amcs.master.org.model.Society;
import com.eipl.amcs.operation.procurement.model.AllowDcsManualCollectionRange;
import com.eipl.amcs.operation.procurement.repository.AllowDcsManualCollectionRangeRepository;
import com.eipl.amcs.util.CommonUtil;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.eipl.amcs.utils.AppConstant.DATE_TIME_FMT;

@RestController
@RequestMapping("/allow_dcs_manual_collection_range")
public class AllowDcsManualCollectionRangeController {

    @Autowired
    private AllowDcsManualCollectionRangeRepository repository;
    @Autowired
    private ShiftRepository shiftRepository;
    @Autowired
    private NextCodeRepository nextCodeRepository;

    @PostMapping
    public AllowDcsManualCollectionRange save(@RequestBody AllowDcsManualCollectionRange allowDcsManualCollectionRange, @RequestHeader Map<String, String> headers) {
        allowDcsManualCollectionRange.setInitData();
        String nextCode = nextCodeRepository.getNextCode("AllowDcsManualCollectionRange", "code", allowDcsManualCollectionRange.getSociety().getCode(), 3);
        allowDcsManualCollectionRange.setCode(Long.valueOf(nextCode));
        repository.customSave(allowDcsManualCollectionRange, CommonUtil.getIdentityHeader(headers));
        return allowDcsManualCollectionRange;
    }

    @PutMapping
    public void customUpdate(@RequestBody AllowDcsManualCollectionRange allowDcsManualCollectionRange, @RequestHeader Map<String, String> headers) {
        allowDcsManualCollectionRange.setupdateData();
        if (allowDcsManualCollectionRange.getStatus() == 1) {
            allowDcsManualCollectionRange.setCancelledAt(LocalDateTime.now());
            allowDcsManualCollectionRange.setStatus(3);
            allowDcsManualCollectionRange.setCancelledBy("SYSTEM");
        }
        repository.customUpdate(allowDcsManualCollectionRange, CommonUtil.getIdentityHeader(headers));
    }

    @GetMapping("/findByDateShift")
    public ResponseEntity<List<AllowDcsManualCollectionRange>> indexByDatee(
            @RequestParam(name = "fromDate", required = false) String fDate,
            @RequestParam(name = "toDate", required = false) String tDate,
            @RequestParam(name = "type", required = false) String type
    ) {
        LocalDateTime fromDate = LocalDateTime.parse(fDate, DATE_TIME_FMT);
        LocalDateTime toDate = LocalDateTime.parse(tDate, DATE_TIME_FMT);
        List<AllowDcsManualCollectionRange> list = new ArrayList<>();
        if (type == null || type.equalsIgnoreCase("")) {
            list = repository.findByFromDateLessThanEqualAndToDateGreaterThanEqual(fromDate, toDate);
        } else {
            list = repository.findByFromDateLessThanEqualAndToDateGreaterThanEqualAndxCol1(fromDate, toDate, type);
        }
        for (AllowDcsManualCollectionRange range : list) {
            range.setSociety(Hibernate.unproxy(range.getSociety(), Society.class));
            range.setFromShift(Hibernate.unproxy(range.getFromShift(), Shift.class));
            range.setToShift(Hibernate.unproxy(range.getToShift(), Shift.class));
        }
        return new ResponseEntity<List<AllowDcsManualCollectionRange>>(list, HttpStatus.OK);
    }

    @GetMapping("/findByDateShiftValidation")
    public ResponseEntity<Boolean> indexByDate(
            @RequestParam(name = "fromDate", required = false) String fDate,
            @RequestParam(name = "toDate", required = false) String tDate,
            @RequestParam(name = "type", required = false) String type,
            @RequestParam(name = "qualityManual", required = false) Boolean qualityManual,
            @RequestParam(name = "weightManual", required = false) Boolean weightManual
    ) {
        LocalDateTime fromDate = LocalDateTime.parse(fDate, DATE_TIME_FMT);
        LocalDateTime toDate = LocalDateTime.parse(tDate, DATE_TIME_FMT);
        List<AllowDcsManualCollectionRange> existingData;

        if (type.equalsIgnoreCase("0")) {
            existingData = repository.findByFromDateLessThanEqualAndToDateGreaterThanEqualAndxCol1AndWeightManualAndQualityManualAndFromShiftAndToShiftAndStatus(fromDate, toDate, type, weightManual, qualityManual, 2);
        } else {
            existingData = repository.findByFromDateBetweenAndToDateBetweenAndxCol1AndWeightManualAndQualityManualAndStatus
                    (fromDate, toDate, type, qualityManual, weightManual, 2);
        }
        if (existingData.isEmpty()) {
            return new ResponseEntity<>(true, HttpStatus.OK);
        }
        return new ResponseEntity<>(false, HttpStatus.OK);
    }


    @GetMapping
    public List<AllowDcsManualCollectionRange> findAll() {
        List<AllowDcsManualCollectionRange> list = repository.findAll(Sort.by("createdAt").descending());
        for (AllowDcsManualCollectionRange request : list) {
            request.setFromShift(Hibernate.unproxy(request.getFromShift(), Shift.class));
            request.setToShift(Hibernate.unproxy(request.getToShift(), Shift.class));
            request.setSociety(Hibernate.unproxy(request.getSociety(), Society.class));
        }
        return list;
    }

    @GetMapping("/id")
    public Optional<AllowDcsManualCollectionRange> indexTransaction(@RequestParam Long id) {
        return repository.findById(id);
    }
}
