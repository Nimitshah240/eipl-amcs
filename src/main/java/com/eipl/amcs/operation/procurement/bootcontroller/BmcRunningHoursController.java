package com.eipl.amcs.operation.procurement.bootcontroller;

import com.eipl.amcs.base.service.NextCodeService;
import com.eipl.amcs.master.account.controller.LedgerController;
import com.eipl.amcs.operation.procurement.model.BmcRunningHours;
import com.eipl.amcs.operation.procurement.service.BmcRunningHoursService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/bmc-running-hours")
public class BmcRunningHoursController {

    @Autowired
    private NextCodeService nextCodeService;
    @Autowired
    private final BmcRunningHoursService service;
    private static final Logger LOGGER = LoggerFactory.getLogger(LedgerController.class);

    @Autowired
    public BmcRunningHoursController(BmcRunningHoursService service) {
        this.service = service;
    }

    @GetMapping("/All")
    public List<BmcRunningHours> getAll() {
        return service.getAllBmcRunningHours();
    }

    @GetMapping("/{id}")
    public BmcRunningHours getBmcRunningHoursByCode(@PathVariable Long code) {
        return service.getBmcRunningHoursByCode(code);
    }

    @PostMapping("/")
    public BmcRunningHours createBmcRunningHours(@RequestBody BmcRunningHours bmcRunningHours) {
        return service.createBmcRunningHours(bmcRunningHours);
    }

    @PostMapping()
    public ResponseEntity<BmcRunningHours> saveBmcRunningHours(@RequestBody BmcRunningHours bmcRunningHours) {
        try {
            BmcRunningHours savedBmcRunningHours = service.save(bmcRunningHours);
            return new ResponseEntity<>(savedBmcRunningHours, HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping()
    public ResponseEntity<BmcRunningHours> update(@RequestHeader Map<String, String> headers,
                                                  @RequestBody BmcRunningHours bmcRunningHours) {
        return new ResponseEntity<BmcRunningHours>(service.update(bmcRunningHours), HttpStatus.OK);
    }

    @DeleteMapping("/{code}")
    public void deleteBmcRunningHours(@PathVariable Long code) {
        service.deleteBmcRunningHours(code);
    }
}


