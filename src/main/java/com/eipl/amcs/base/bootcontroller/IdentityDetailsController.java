package com.eipl.amcs.base.bootcontroller;

import com.eipl.amcs.base.Identity;
import com.eipl.amcs.base.service.IdentityDetailsService;
import com.eipl.amcs.base.service.NextCodeService;
import com.eipl.amcs.sync.model.Broadcasted;
import com.eipl.amcs.sync.producer.BroadcastedService;
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
@RequestMapping("/identity_details")
public class IdentityDetailsController {

    @Autowired
    private IdentityDetailsService service;

    @Autowired
    private NextCodeService nextCodeService;
    @Autowired
    private BroadcastedService broadcastedService;

    private static final Logger LOGGER = LoggerFactory.getLogger(IdentityDetailsController.class);

    @GetMapping
    public ResponseEntity<List<Identity>> index(@RequestParam(name = "society", required = false) String societyCode) {
        try {
            List<Identity> list = null;
            list = service.findAll();
            if (list == null || list.isEmpty())
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
            return new ResponseEntity<List<Identity>>(list, HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/next-code")
    public ResponseEntity<String> nextCode(@RequestParam(name = "society", required = true) String societyCode) {
        try {
            LOGGER.info("Next identity no for Society: {}", societyCode);
            String code = nextCodeService.getNextCode("Identity", "code", societyCode, 0);
            if (code == null || code.isEmpty())
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);

            return new ResponseEntity<>(code, HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping
    public ResponseEntity<Identity> createIdentity(@RequestHeader Map<String, String> headers, @RequestBody Identity dto) {
        return new ResponseEntity<>(service.save(dto, CommonUtil.getIdentityHeader(headers)), HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<Identity> updateIdentity(@RequestHeader Map<String, String> headers, @RequestBody Identity dto) {
        return new ResponseEntity<>(service.save(dto, CommonUtil.getIdentityHeader(headers)), HttpStatus.CREATED);
    }

    @DeleteMapping("/{code}")
    public ResponseEntity<?> deleteIdentity(@RequestHeader Map<String, String> headers,
                                            @PathVariable("code") String code) {
        service.delete(code, CommonUtil.getIdentityHeader(headers));
        return new ResponseEntity<>(null, HttpStatus.OK);
    }


    @GetMapping("/sendBroadcastedAllInOne")
    public ResponseEntity<String> sendBroadcasted() {
        return new ResponseEntity<>(broadcastedService.sendBroadcastedAll(), HttpStatus.OK);
    }

    @GetMapping("/group-by-table")
    public ResponseEntity<Map<String, List<Broadcasted>>> groupByTableName() {
        return new ResponseEntity<>(broadcastedService.getGroupedByTableName(), HttpStatus.OK);
    }

}