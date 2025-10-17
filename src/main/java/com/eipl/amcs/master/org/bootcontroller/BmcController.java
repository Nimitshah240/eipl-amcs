package com.eipl.amcs.master.org.bootcontroller;

import com.eipl.amcs.master.org.model.Bmc;
import com.eipl.amcs.master.org.service.BmcService;
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
@RequestMapping("/bmcs")
public class BmcController {

    private static final Logger LOGGER = LoggerFactory.getLogger(BmcController.class);
    @Autowired
    private BmcService service;

    @GetMapping
    public ResponseEntity<List<Bmc>> index() {
        try {
            List<Bmc> list = service.findAll();
            if (list == null || list.isEmpty())
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);

            return new ResponseEntity<List<Bmc>>(list, HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
