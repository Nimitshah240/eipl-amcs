package com.eipl.amcs.operation.share.bootcontroller;

import com.eipl.amcs.base.service.NextCodeService;
import com.eipl.amcs.exception.BusinessValidationFailException;
import com.eipl.amcs.operation.share.model.ShareRate;
import com.eipl.amcs.operation.share.service.ShareRateService;
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
import java.util.Optional;

@RestController
@RequestMapping("/share_rate")

public class ShareRateController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ShareRateController.class);
    @Autowired
    private ShareRateService service;
    @Autowired
    private NextCodeService nextCodeService;

    @GetMapping
    public ResponseEntity<List<ShareRate>> findAll() {
        return new ResponseEntity<List<ShareRate>>(service.findAll(), HttpStatus.OK);
    }


    @GetMapping("/rate")
    public ResponseEntity<ShareRate> fetchRate(@RequestParam(name = "date") String date) {
        try {
            LocalDate dt = LocalDate.parse(date);
            return new ResponseEntity<ShareRate>(service.fetchRate(dt), HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping
    public ResponseEntity<ShareRate> createShare(@RequestHeader Map<String, String> headers,
                                                 @RequestBody ShareRate dto) throws BusinessValidationFailException {
        return new ResponseEntity<>(service.save(dto, CommonUtil.getIdentityHeader(headers)), HttpStatus.CREATED);
    }

    @PutMapping()
    public ResponseEntity<ShareRate> update(@RequestHeader Map<String, String> headers,
                                            @RequestBody ShareRate share) {
        return new ResponseEntity<>(service.update(share, CommonUtil.getIdentityHeader(headers)),
                HttpStatus.OK);
    }

    @DeleteMapping("/{code}")
    public ResponseEntity<?> deleteLocalMilkSaleRate(@RequestHeader Map<String, String> headers, @PathVariable("code") String code) {
        try {
            LOGGER.info("LocalMilkSaleRate delete method");
            Optional<ShareRate> shareRate = service.findById(code);
            if (shareRate == null || !shareRate.isPresent())
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
            service.delete(shareRate.get(), CommonUtil.getIdentityHeader(headers));
            return new ResponseEntity<>(null, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/next-code")
    public ResponseEntity<String> nextCode(@RequestParam String code) {
        try {
            LOGGER.info("Next code for ShareRate: {}", code);
            String codes = nextCodeService.getNextCode("ShareRate", "code", code, 2);
            if (codes == null || codes.isEmpty())
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);

            return new ResponseEntity<>(codes, HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
