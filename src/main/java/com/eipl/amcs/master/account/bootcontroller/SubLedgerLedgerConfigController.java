package com.eipl.amcs.master.account.bootcontroller;

import com.eipl.amcs.master.account.model.SubLedgerLedgerConfig;
import com.eipl.amcs.master.account.service.SubLedgerLedgerConfigService;
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
@RequestMapping("/sub_ledger_ledger_config")
public class SubLedgerLedgerConfigController {

    private static final Logger LOGGER = LoggerFactory.getLogger(SubLedgerLedgerConfigController.class);
    @Autowired
    private SubLedgerLedgerConfigService service;

    @GetMapping
    public ResponseEntity<List<SubLedgerLedgerConfig>> index() {
        try {
            List<SubLedgerLedgerConfig> list = service.findAll();
            if (list == null || list.isEmpty())
                return new ResponseEntity<>(null, HttpStatus.OK);

            return new ResponseEntity<List<SubLedgerLedgerConfig>>(list, HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/by-subledger-type")
    public ResponseEntity<List<SubLedgerLedgerConfig>> indexBySubLedgerType(
            @RequestParam(name = "code", required = true) Integer code) {
        try {
            List<SubLedgerLedgerConfig> list = service.findBySubLedgerType(code);
            if (list == null || list.isEmpty())
                return new ResponseEntity<>(null, HttpStatus.OK);
            return new ResponseEntity<List<SubLedgerLedgerConfig>>(list, HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping
    public ResponseEntity<String> createLedgerType(@RequestHeader Map<String, String> headers,
                                                   @RequestBody List<SubLedgerLedgerConfig> dto, @RequestParam String code) {
        return new ResponseEntity<>(service.save(dto, code, CommonUtil.getIdentityHeader(headers)), HttpStatus.CREATED);
    }

}
