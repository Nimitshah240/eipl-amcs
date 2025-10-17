package com.eipl.amcs.master.account.bootcontroller;

import com.eipl.amcs.base.service.NextCodeService;
import com.eipl.amcs.master.account.model.LedgerGroup;
import com.eipl.amcs.master.account.service.LedgerGroupService;
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
@RequestMapping("/ledger-groups")
public class LedgerGroupController {

    private static final Logger LOGGER = LoggerFactory.getLogger(LedgerGroupController.class);
    @Autowired
    private LedgerGroupService service;
    @Autowired
    private NextCodeService nextCodeService;

    @GetMapping
    public ResponseEntity<List<LedgerGroup>> index() {
        try {
            List<LedgerGroup> list = service.findAll();
            if (list == null || list.isEmpty())
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);

            return new ResponseEntity<List<LedgerGroup>>(list, HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/findByLedgerType")
    public ResponseEntity<List<LedgerGroup>> indexByLedgerType(@RequestParam(name = "ledgerTypeCode", required = true) Integer ledgerTypeCode) {
        try {
            List<LedgerGroup> list = service.findByLedgerType(ledgerTypeCode);
            if (list == null || list.isEmpty())
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
            return new ResponseEntity<List<LedgerGroup>>(list, HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/next-code")
    public ResponseEntity<String> nextCode(@RequestParam(name = "society", required = true) String societyCode) {
        try {
            LOGGER.info("Next Ledger Group no for Society: {}", societyCode);
            String code = nextCodeService.getNextCode("LedgerGroup", "code", societyCode, 0);
            if (code == null || code.isEmpty())
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);

            return new ResponseEntity<>(code, HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping
    public ResponseEntity<LedgerGroup> createLedgerType(@RequestHeader Map<String, String> headers, @RequestBody LedgerGroup dto) {
        return new ResponseEntity<>(service.save(dto, CommonUtil.getIdentityHeader(headers)), HttpStatus.CREATED);

    }

    @PutMapping
    public ResponseEntity<LedgerGroup> updateLedgerType(@RequestHeader Map<String, String> headers, @RequestBody LedgerGroup dto) {
        return new ResponseEntity<>(service.update(dto, CommonUtil.getIdentityHeader(headers)), HttpStatus.CREATED);

    }

    @DeleteMapping("/{code}")
    public ResponseEntity<?> deleteLedgerType(@RequestHeader Map<String, String> headers,
                                              @PathVariable("code") Integer code) {
        service.delete(code, CommonUtil.getIdentityHeader(headers));
        return new ResponseEntity<>(null, HttpStatus.OK);
    }


}
