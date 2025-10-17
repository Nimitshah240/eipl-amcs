package com.eipl.amcs.master.account.bootcontroller;

import com.eipl.amcs.master.account.model.LedgerMappingTaxDetail;
import com.eipl.amcs.master.account.service.LedgerMappingTaxDetailService;
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
@RequestMapping("/ledger-mapping-tax-detail")
public class LedgerMappingTaxDetailController {

    private static final Logger LOGGER = LoggerFactory.getLogger(LedgerMappingTaxDetailController.class);
    @Autowired
    private LedgerMappingTaxDetailService service;

    @GetMapping
    public ResponseEntity<List<LedgerMappingTaxDetail>> index() {
        try {
            List<LedgerMappingTaxDetail> list = service.findAll();
            if (list == null || list.isEmpty())
                return new ResponseEntity<>(null, HttpStatus.OK);

            return new ResponseEntity<List<LedgerMappingTaxDetail>>(list, HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PostMapping
    public ResponseEntity<String> createLedgerType(@RequestHeader Map<String, String> headers, @RequestBody List<LedgerMappingTaxDetail> dto) {
        return new ResponseEntity<>(service.save(dto, CommonUtil.getIdentityHeader(headers)), HttpStatus.CREATED);
    }

}
