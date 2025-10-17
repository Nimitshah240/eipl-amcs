package com.eipl.amcs.master.procurement.bootcontroller;

import com.eipl.amcs.master.procurement.model.HardwareDevice;
import com.eipl.amcs.master.procurement.service.HardwareDeviceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/hardware-devices")
public class HardwareDeviceController {

    private static final Logger LOGGER = LoggerFactory.getLogger(HardwareDeviceController.class);
    @Autowired
    private HardwareDeviceService service;

    @GetMapping
    public ResponseEntity<List<HardwareDevice>> index() {
        try {
            List<HardwareDevice> list = service.findAll();
            if (list == null || list.isEmpty())
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);

            return new ResponseEntity<List<HardwareDevice>>(list, HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
