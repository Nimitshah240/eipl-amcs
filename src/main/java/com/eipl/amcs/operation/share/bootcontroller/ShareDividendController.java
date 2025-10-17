package com.eipl.amcs.operation.share.bootcontroller;

import com.eipl.amcs.base.service.NextCodeService;
import com.eipl.amcs.exception.BusinessValidationFailException;
import com.eipl.amcs.operation.procurement.controller.LocalMilkSaleController;
import com.eipl.amcs.operation.share.model.ShareDividend;
import com.eipl.amcs.operation.share.service.ShareDividendService;
import com.eipl.amcs.util.CommonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/share_dividend")

public class ShareDividendController {
    private static final Logger LOGGER = LoggerFactory.getLogger(LocalMilkSaleController.class);
    @Autowired
    private ShareDividendService service;
    @Autowired
    private NextCodeService nextCodeService;

    @GetMapping
    public ResponseEntity<List<ShareDividend>> findAll() {
        return new ResponseEntity<List<ShareDividend>>(service.findAll(), HttpStatus.OK);
    }

    //    @PostMapping
//    public ResponseEntity<ShareDividend> createShareDividend(@RequestHeader Map<String, String> headers,
//                                             @RequestBody  ShareDividend dto) throws BusinessValidationFailException {
//        return new ResponseEntity<>(service.save(dto, CommonUtil.getIdentityHeader(headers)), HttpStatus.CREATED);
//    }
    @PostMapping
    public ResponseEntity<String> dev(@RequestHeader Map<String, String> headers,
                                      @RequestBody List<ShareDividend> dto) throws BusinessValidationFailException {
        return new ResponseEntity<>(service.save(dto, CommonUtil.getIdentityHeader(headers)), HttpStatus.CREATED);
    }

    @PutMapping()
    public ResponseEntity<ShareDividend> update(@RequestHeader Map<String, String> headers,
                                                @RequestBody ShareDividend sharedividend) {
        return new ResponseEntity<ShareDividend>(service.update(sharedividend, CommonUtil.getIdentityHeader(headers)),
                HttpStatus.OK);
    }

    @DeleteMapping("/cancel")
    public ResponseEntity<?> cancelShareDividend(@RequestHeader Map<String, String> headers, @PathVariable("code") String code) {
        try {
            LOGGER.info("ShareDividend delete method");
            service.cancel(code, CommonUtil.getIdentityHeader(headers));
            return new ResponseEntity<>(null, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{code}")
    public ResponseEntity<?> deleteShareDividend(@RequestHeader Map<String, String> headers, @PathVariable("code") String code) {
        try {
            LOGGER.info("Share delete method");
            service.delete(code, CommonUtil.getIdentityHeader(headers));
            return new ResponseEntity<>(null, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/all")
    public ResponseEntity<?> deleteAllShare(@RequestParam(name = "fromDate") String fromDate,
                                            @RequestParam(name = "toDate") String toDate) {
        try {
            LOGGER.info("ShareDividend delete method");
            LocalDate fromDt = LocalDate.parse(fromDate);
            LocalDate toDt = LocalDate.parse(toDate);
            return new ResponseEntity<>(service.deleteAll(fromDt, toDt), HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.OK);
        }

    }

    @GetMapping("/next-code")
    public ResponseEntity<String> nextCode(@RequestParam String code) {
        try {
            LOGGER.info("Next code for ShareDividend: {}", code);
            String codes = nextCodeService.getNextCode("Share", "code", code, 5);
            if (codes == null || codes.isEmpty())
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);

            return new ResponseEntity<>(codes, HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/byDate")
    public ResponseEntity<List<ShareDividend>> findByDate(@RequestParam(name = "fromDate") String fromDate,
                                                          @RequestParam(name = "toDate") String toDate) {
        LocalDate fromDt = LocalDate.parse(fromDate);
        LocalDate toDt = LocalDate.parse(toDate);
        return new ResponseEntity<List<ShareDividend>>(service.findAllData(fromDt, toDt), HttpStatus.OK);
    }

}
