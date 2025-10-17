package com.eipl.amcs.master.procurement.bootcontroller;

import com.eipl.amcs.exception.BusinessValidationFailException;
import com.eipl.amcs.exception.EntityNotFoundException;
import com.eipl.amcs.master.procurement.model.SocietyPaymentCycle;
import com.eipl.amcs.master.procurement.service.SocietyPaymentCycleService;
import com.eipl.amcs.util.CommonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/society-payment-cycles")
public class SocietyPaymentCycleController {

    private static final Logger LOGGER = LoggerFactory.getLogger(SocietyPaymentCycleController.class);
    @Autowired
    private SocietyPaymentCycleService service;

    @GetMapping
    public ResponseEntity<List<SocietyPaymentCycle>> index() {

        try {
            List<SocietyPaymentCycle> list = service.findAll();
            if (list == null || list.isEmpty())
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);

            return new ResponseEntity<List<SocietyPaymentCycle>>(list, HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @GetMapping("/findByDate")
    public ResponseEntity<List<SocietyPaymentCycle>> index(@RequestParam(name = "fromDate") String fromDate,
                                                           @RequestParam(name = "toDate") String toDate) {
        try {
            LocalDateTime fromDt = LocalDateTime.of(LocalDate.parse(fromDate), LocalTime.MIN);
            LocalDateTime toDt = LocalDateTime.of(LocalDate.parse(toDate), LocalTime.MAX);
            return new ResponseEntity<>(service.findAll(fromDt, toDt), HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/fetchAll")
    public ResponseEntity<List<SocietyPaymentCycle>> fetchAll(@RequestParam String date, @RequestParam int limit) {
        try {

            LocalDateTime dt = LocalDateTime.of(LocalDate.parse(date), LocalTime.of(12, 0));
            List<SocietyPaymentCycle> list = service.findByToDateGreaterThanEqualOrderByToDate(dt, limit);
            if (list == null || list.isEmpty())
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);

            return new ResponseEntity<List<SocietyPaymentCycle>>(list, HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping
    public ResponseEntity<String> createSocietyPaymentCycle(@RequestHeader Map<String, String> headers,
                                                            @RequestBody List<SocietyPaymentCycle> dto) throws BusinessValidationFailException {
        return new ResponseEntity<>(service.save(dto, CommonUtil.getIdentityHeader(headers)), HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<SocietyPaymentCycle> updateSocietyPaymentCycle(@RequestHeader Map<String, String> headers,
                                                                         @RequestParam("code") String code, @RequestBody SocietyPaymentCycle dto) {
        try {
            LOGGER.info("SocietyPaymentCycle save method");
            dto = service.update(code, dto, CommonUtil.getIdentityHeader(headers));
            if (dto == null)
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
            return new ResponseEntity<>(dto, HttpStatus.CREATED);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{code}")
    public ResponseEntity<?> deleteSocietyPaymentCycle(@RequestHeader Map<String, String> headers,
                                                       @PathVariable("code") String code) throws EntityNotFoundException {
        service.delete(code, CommonUtil.getIdentityHeader(headers));
        return new ResponseEntity<>(null, HttpStatus.OK);
    }
}
