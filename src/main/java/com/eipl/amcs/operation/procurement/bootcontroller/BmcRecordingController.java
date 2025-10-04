package com.eipl.amcs.operation.procurement.bootcontroller;

import com.eipl.amcs.operation.procurement.model.BmcRecording;
import com.eipl.amcs.operation.procurement.service.BmcRecordingService;
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
@RequestMapping("/bmc-recordings")
public class BmcRecordingController {

    private final BmcRecordingService service;
    private static final Logger LOGGER = LoggerFactory.getLogger(BmcRecordingController.class);
    @GetMapping
    public ResponseEntity<List<BmcRecording>> index() {
        try {
            List<BmcRecording> list = service.getAllBmcRecordings();
            if (list == null || list.isEmpty())
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
            return new ResponseEntity<List<BmcRecording>>(list, HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Autowired
    public BmcRecordingController(BmcRecordingService service) {
        this.service = service;
    }

    @GetMapping("/{id}")
    public Optional<BmcRecording> getBmcRecordingByCode(@PathVariable Long code) {
        return service.getBmcRecordingByCode(code);
    }

    @PostMapping
    public ResponseEntity<BmcRecording> createBmcRecording(@RequestHeader Map<String, String> headers, @RequestBody BmcRecording recording) {
        return new ResponseEntity<>(service.save(recording, CommonUtil.getIdentityHeader(headers)), HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<BmcRecording> updateBmcRecording(@RequestHeader Map<String, String> headers, @RequestBody BmcRecording recording) {
        return new ResponseEntity<>(service.update(recording), HttpStatus.CREATED);
    }

    @DeleteMapping("/{code}")
    public void deleteBmcRecording(@PathVariable Long code) {
        service.deleteBmcRecording(code);
    }
}

