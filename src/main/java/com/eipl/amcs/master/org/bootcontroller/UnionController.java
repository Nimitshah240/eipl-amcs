package com.eipl.amcs.master.org.bootcontroller;

import com.eipl.amcs.master.org.model.Union;
import com.eipl.amcs.master.org.service.UnionService;
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
@RequestMapping("/unions")
public class UnionController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UnionController.class);
    @Autowired
    private UnionService service;

    @GetMapping
    public ResponseEntity<List<Union>> index() {
        try {
            List<Union> list = service.findAll();
            if (list == null || list.isEmpty())
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);

            return new ResponseEntity<List<Union>>(list, HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
