package com.eipl.amcs.operation.procurement.bootcontroller;

import com.eipl.amcs.base.service.NextCodeService;
import com.eipl.amcs.exception.BusinessValidationFailException;
import com.eipl.amcs.master.procurement.model.SocietyMilkPurchaseRate;
import com.eipl.amcs.operation.procurement.dto.MilkReceiptDto;
import com.eipl.amcs.operation.procurement.dto.MilkReceiptSummaryDto;
import com.eipl.amcs.operation.procurement.model.MilkReceipt;
import com.eipl.amcs.operation.procurement.model.MilkReceiptTransaction;
import com.eipl.amcs.operation.procurement.repository.MilkReceiptRepository;
import com.eipl.amcs.operation.procurement.service.MilkReceiptService;
import com.eipl.amcs.util.CommonUtil;
import com.eipl.amcs.utils.AppConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/milk-receipt")
public class MilkReceiptController {

    private static final Logger LOGGER = LoggerFactory.getLogger(MilkReceiptController.class);
    private static final DateTimeFormatter DATE_TIME_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
    @Autowired
    private MilkReceiptService service;
    @Autowired
    private MilkReceiptRepository repository;
    @Autowired
    private NextCodeService nextCodeService;

    @GetMapping
    public ResponseEntity<List<MilkReceipt>> index() {
        try {
            List<MilkReceipt> list = service.findAll();
            if (list == null || list.isEmpty())
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
            return new ResponseEntity<List<MilkReceipt>>(list, HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/rate-code")
    public ResponseEntity<SocietyMilkPurchaseRate> fetchRate(@RequestParam(name = "date", required = true) String date,
                                                             @RequestParam(name = "shiftCode", required = true) Integer shiftCode,
                                                             @RequestParam(name = "societyCode", required = true) String societyCode) {
        try {
            LocalDateTime dt = LocalDateTime.parse(date, DATE_TIME_FMT);
            SocietyMilkPurchaseRate list = service.fetchPurchaseRateCode(dt, shiftCode, societyCode);
            if (list == null)
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
            return new ResponseEntity<SocietyMilkPurchaseRate>(list, HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/a")
    public ResponseEntity<MilkReceipt> createMilkReceipt(@RequestHeader Map<String, String> headers,
                                                         @RequestBody MilkReceiptDto dto) throws BusinessValidationFailException {
        return new ResponseEntity<>(service.save(dto, CommonUtil.getIdentityHeader(headers)), HttpStatus.CREATED);

    }


    @PutMapping("/a")
    public ResponseEntity<MilkReceipt> updateMilkReceipt(@RequestHeader Map<String, String> headers,
                                                         @RequestBody MilkReceiptDto dto) throws BusinessValidationFailException {
        return new ResponseEntity<>(service.update(dto, CommonUtil.getIdentityHeader(headers)), HttpStatus.CREATED);

    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteMilkReceipt(@RequestHeader Map<String, String> headers, @RequestParam String code) {
        try {
            LOGGER.info("MilkReceipt delete method");
//			Optional<MilkReceipt> dispatchtData = service.findById(code);
//			if (dispatchtData == null || !dispatchtData.isPresent())
//				return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
            service.delete(code, CommonUtil.getIdentityHeader(headers));
            return new ResponseEntity<>(null, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/transaction")
    public ResponseEntity<?> deleteMilkReceiptTransaction(@RequestHeader Map<String, String> headers,
                                                          @RequestParam String code) {
        try {
            LOGGER.info("MilkReceipt delete method");
            Optional<MilkReceiptTransaction> receiptData = service.findTransactionById(code);
            if (receiptData == null || !receiptData.isPresent())
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
            service.deleteTransaction(receiptData.get(), CommonUtil.getIdentityHeader(headers));
            return new ResponseEntity<>(null, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/next-code")
    public ResponseEntity<String> nextCode(@RequestParam String nextCode) {
        try {
            LOGGER.info("Next dock no for Society: {}", nextCode);
            String code = nextCodeService.getNextCode("MilkReceipt", "challanNo", nextCode, 3);
            if (code == null || code.isEmpty())
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);

            return new ResponseEntity<>(code, HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/transaction")
    public ResponseEntity<List<MilkReceiptTransaction>> fetchMemberDetail(@RequestParam String challanNo) {
        return new ResponseEntity<List<MilkReceiptTransaction>>(service.findDetailByChallanNo(challanNo),
                HttpStatus.OK);
    }

    @GetMapping("/milk-dispatch-summary")
    public ResponseEntity<List<MilkReceiptSummaryDto>> fetchMilkReceiptSummary(@RequestParam String fromDate,
                                                                               @RequestParam String toDate) {
        LocalDateTime fromDt = LocalDateTime.parse(fromDate, AppConstant.DATE_TIME_FMT);
        LocalDateTime toDt = LocalDateTime.parse(toDate, AppConstant.DATE_TIME_FMT);
        return new ResponseEntity<List<MilkReceiptSummaryDto>>(service.fetchMilkReceiptSummary(fromDt, toDt), HttpStatus.OK);
    }

    @GetMapping("/prev-record")
    public ResponseEntity<MilkReceipt> fetchPrevRecord(@RequestParam String fromDate) {
        LocalDateTime fromDt = LocalDateTime.parse(fromDate, AppConstant.DATE_TIME_FMT);
        return new ResponseEntity<>(repository.findPreviousRecordOfGoodMilkType(fromDt).get(), HttpStatus.OK);
    }
}