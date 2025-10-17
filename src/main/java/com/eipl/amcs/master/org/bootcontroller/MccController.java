package com.eipl.amcs.master.org.bootcontroller;

import com.eipl.amcs.master.org.model.Mcc;
import com.eipl.amcs.master.org.service.MccService;
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
@RequestMapping("/mccs")
public class MccController {

    private static final Logger LOGGER = LoggerFactory.getLogger(MccController.class);
    @Autowired
    private MccService service;

    @GetMapping
    public ResponseEntity<List<Mcc>> index() {
        try {
            List<Mcc> list = service.findAll();
            if (list == null || list.isEmpty())
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);

            return new ResponseEntity<List<Mcc>>(list, HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
