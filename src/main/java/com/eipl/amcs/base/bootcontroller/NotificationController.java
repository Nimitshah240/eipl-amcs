package com.eipl.amcs.base.bootcontroller;

import com.eipl.amcs.base.Notification;
import com.eipl.amcs.base.service.NextCodeService;
import com.eipl.amcs.base.service.NotificationService;
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
@RequestMapping("/notification")
public class NotificationController {

    @Autowired
    private NotificationService service;

    @Autowired
    private NextCodeService nextCodeService;

    private static final Logger LOGGER = LoggerFactory.getLogger(NotificationController.class);

    @GetMapping
    public ResponseEntity<List<Notification>> index(@RequestParam(name = "society", required = false) String societyCode) {
        try {
            List<Notification> list = null;
            list = service.findAll();
            if (list == null || list.isEmpty())
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
            return new ResponseEntity<List<Notification>>(list, HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/next-code")
    public ResponseEntity<String> nextCode(@RequestParam(name = "society", required = true) String societyCode) {
        try {
            LOGGER.info("Next identity no for Society: {}", societyCode);
            String code = nextCodeService.getNextCode("Notification", "code", societyCode, 0);
            if (code == null || code.isEmpty())
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);

            return new ResponseEntity<>(code, HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping
    public ResponseEntity<List<Notification>> createIdentity(@RequestHeader Map<String, String> headers, @RequestBody List<Notification> dto) {
        return new ResponseEntity<>(service.save(dto, CommonUtil.getIdentityHeader(headers)), HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<List<Notification>> updateIdentity(@RequestHeader Map<String, String> headers, @RequestBody List<Notification> dto) {
        return new ResponseEntity<>(service.save(dto, CommonUtil.getIdentityHeader(headers)), HttpStatus.CREATED);
    }

    @DeleteMapping("/{code}")
    public ResponseEntity<?> deleteIdentity(@RequestHeader Map<String, String> headers,
                                            @PathVariable("code") String code) {
        service.delete(code, CommonUtil.getIdentityHeader(headers));
        return new ResponseEntity<>(null, HttpStatus.OK);
    }


}