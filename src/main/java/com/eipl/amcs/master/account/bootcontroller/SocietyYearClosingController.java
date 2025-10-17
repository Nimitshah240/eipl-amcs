package com.eipl.amcs.master.account.bootcontroller;

import com.eipl.amcs.base.service.NextCodeService;
import com.eipl.amcs.master.account.model.SocietyYearClosing;
import com.eipl.amcs.master.account.service.SocietyYearClosingService;
import com.eipl.amcs.util.CommonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/society_year_closing")
public class SocietyYearClosingController {

    private static final Logger LOGGER = LoggerFactory.getLogger(SocietyYearClosingController.class);
    @Autowired
    private SocietyYearClosingService service;
    @Autowired
    private NextCodeService nextCodeService;

    @GetMapping("/next-code")
    public ResponseEntity<String> nextCode(@RequestParam(name = "society", required = true) String societyCode) {
        try {
            LOGGER.info("Next subledger no for Society: {}", societyCode);
            String code = nextCodeService.getNextCode("SocietyYearClosing", "society_year_closing_code", societyCode, 0);
            if (code == null || code.isEmpty())
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
            return new ResponseEntity<>(code, HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PostMapping
    public ResponseEntity<SocietyYearClosing> createSocietyYearClosing(@RequestHeader Map<String, String> headers, @RequestBody SocietyYearClosing dto) {
        return new ResponseEntity<>(service.save(dto, CommonUtil.getIdentityHeader(headers)), HttpStatus.CREATED);

    }


}
