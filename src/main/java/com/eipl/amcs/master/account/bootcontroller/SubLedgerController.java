package com.eipl.amcs.master.account.bootcontroller;

import com.eipl.amcs.base.service.NextCodeService;
import com.eipl.amcs.master.account.model.LedgerSubLedgerMapping;
import com.eipl.amcs.master.account.model.SubLedger;
import com.eipl.amcs.master.account.service.SubLedgerService;
import com.eipl.amcs.util.CommonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/sub-ledgers")
public class SubLedgerController {

    private static final Logger LOGGER = LoggerFactory.getLogger(SubLedgerController.class);
    @Autowired
    private SubLedgerService service;
    @Autowired
    private NextCodeService nextCodeService;

    @GetMapping
    public ResponseEntity<List<SubLedger>> index() {
        try {
            List<SubLedger> list = service.findAll();
            if (list == null || list.isEmpty())
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);

            return new ResponseEntity<List<SubLedger>>(list, HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/next-code")
    public ResponseEntity<String> nextCode(@RequestParam(name = "society", required = true) String societyCode) {
        try {
            LOGGER.info("Next subledger no for Society: {}", societyCode);
            String code = nextCodeService.getNextCode("SubLedger", "code", societyCode, 0);
            if (code == null || code.isEmpty())
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
            return new ResponseEntity<>(code, HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/ledger_fetch_by_code")
    public ResponseEntity<SubLedger> fetchSubLedgerCode(@RequestParam String subLedgerCode) {
        try {
            Optional<SubLedger> list = service.findById(subLedgerCode);
            if (list == null || list.isEmpty())
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
            return new ResponseEntity<SubLedger>(list.get(), HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/ledger-subledger-mapping")
    public ResponseEntity<List<LedgerSubLedgerMapping>> fetchMapping(@RequestParam(name = "societyCode", required = true) String societyCode,
                                                                     @RequestParam(name = "ledgerCode", required = false) String ledgerCode,
                                                                     @RequestParam(name = "subLedgerCode", required = false) String subLedgerCode) {
        return new ResponseEntity<List<LedgerSubLedgerMapping>>(service.fetchMapping(societyCode, ledgerCode, subLedgerCode), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<SubLedger> createSubLedger(@RequestHeader Map<String, String> headers, @RequestBody SubLedger dto) {
        return new ResponseEntity<>(service.save(dto, CommonUtil.getIdentityHeader(headers)), HttpStatus.CREATED);

    }

    @PutMapping
    public ResponseEntity<SubLedger> updateSubLedger(@RequestHeader Map<String, String> headers, @RequestBody SubLedger dto) {
        return new ResponseEntity<>(service.update(dto, CommonUtil.getIdentityHeader(headers)), HttpStatus.CREATED);

    }

    @DeleteMapping("/{code}")
    public ResponseEntity<?> deleteSubLedger(@RequestHeader Map<String, String> headers,
                                             @PathVariable("code") String code) {
        service.delete(code, CommonUtil.getIdentityHeader(headers));
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

}
