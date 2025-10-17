package com.eipl.amcs.master.account.bootcontroller;

import com.eipl.amcs.master.account.model.Designation;
import com.eipl.amcs.master.account.service.DesignationService;
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
@RequestMapping("/designation")
public class DesignationController {

    private static final Logger LOGGER = LoggerFactory.getLogger(DesignationController.class);
    @Autowired
    private DesignationService service;

    @GetMapping
    public ResponseEntity<List<Designation>> index() {
        try {
            List<Designation> list = service.findAll();
            if (list == null || list.isEmpty())
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);

            return new ResponseEntity<List<Designation>>(list, HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
