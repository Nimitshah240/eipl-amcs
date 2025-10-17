package com.eipl.amcs.master.org.bootcontroller;

import com.eipl.amcs.master.org.model.Society;
import com.eipl.amcs.master.org.service.SocietyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/societies")
public class SocietyController {

    private static final Logger LOGGER = LoggerFactory.getLogger(SocietyController.class);
    @Autowired
    private SocietyService service;

    @GetMapping
    public ResponseEntity<List<Society>> index() {
        try {
            List<Society> list = service.findAll();
            if (list == null || list.isEmpty())
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);

            return new ResponseEntity<List<Society>>(list, HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping
    public ResponseEntity<Society> save(@RequestBody Society obj) {
        return new ResponseEntity<>(service.save(obj), HttpStatus.OK);
    }
}
