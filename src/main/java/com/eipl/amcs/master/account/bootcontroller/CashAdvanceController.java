package com.eipl.amcs.master.account.bootcontroller;

import com.eipl.amcs.base.service.NextCodeService;
import com.eipl.amcs.master.account.dto.CashAdvanceDto;
import com.eipl.amcs.master.account.model.CashAdvance;
import com.eipl.amcs.master.account.service.CashAdvanceService;
import com.eipl.amcs.util.CommonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/cash_advance")
public class CashAdvanceController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CashAdvanceController.class);
    @Autowired
    private CashAdvanceService service;
    @Autowired
    private NextCodeService nextCodeService;

    @GetMapping
    public ResponseEntity<List<CashAdvance>> index() {
        try {
            List<CashAdvance> list = service.findAll();
            if (list == null || list.isEmpty())
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
            return new ResponseEntity<List<CashAdvance>>(list, HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping
    public ResponseEntity<CashAdvance> createCashAdvance(@RequestHeader Map<String, String> headers, @RequestBody CashAdvanceDto dto) {
        return new ResponseEntity<>(service.save(dto, CommonUtil.getIdentityHeader(headers)), HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<CashAdvance> updateCashAdvance(@RequestHeader Map<String, String> headers, @RequestBody CashAdvanceDto dto) {
        return new ResponseEntity<>(service.update(dto, CommonUtil.getIdentityHeader(headers)), HttpStatus.CREATED);
    }


    @DeleteMapping("/{code}")
    public ResponseEntity<?> deleteCashAdvance(@RequestHeader Map<String, String> headers,
                                               @PathVariable("code") String code) {
        service.delete(code, CommonUtil.getIdentityHeader(headers));
        return new ResponseEntity<>(null, HttpStatus.OK);
    }


}

