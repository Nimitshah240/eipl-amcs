package com.eipl.amcs.master.account.bootcontroller;

import com.eipl.amcs.master.account.model.LedgerMappingProductGroup;
import com.eipl.amcs.master.account.service.LedgerMappingProductGroupService;
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
@RequestMapping("/ledger-mapping-product-group")
public class LedgerMappingProductGroupController {

    private static final Logger LOGGER = LoggerFactory.getLogger(LedgerMappingProductGroupController.class);
    @Autowired
    private LedgerMappingProductGroupService service;

    @GetMapping
    public ResponseEntity<List<LedgerMappingProductGroup>> index() {
        try {
            List<LedgerMappingProductGroup> list = service.findAll();
            if (list == null || list.isEmpty())
                return new ResponseEntity<>(null, HttpStatus.OK);

            return new ResponseEntity<List<LedgerMappingProductGroup>>(list, HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PostMapping
    public ResponseEntity<String> createLedgerType(@RequestHeader Map<String, String> headers, @RequestBody List<LedgerMappingProductGroup> dto) {
        return new ResponseEntity<>(service.save(dto, CommonUtil.getIdentityHeader(headers)), HttpStatus.CREATED);
    }

}
