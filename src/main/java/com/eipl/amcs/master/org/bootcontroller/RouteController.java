package com.eipl.amcs.master.org.bootcontroller;

import com.eipl.amcs.master.org.model.Route;
import com.eipl.amcs.master.org.service.RouteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/routes")
public class RouteController {

    private static final Logger LOGGER = LoggerFactory.getLogger(RouteController.class);
    @Autowired
    private RouteService service;

    @GetMapping
    public ResponseEntity<List<Route>> index() {
        try {
            List<Route> list = service.findAll();
            if (list == null || list.isEmpty())
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);

            return new ResponseEntity<List<Route>>(list, HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping
    public ResponseEntity<Route> save(@RequestBody Route obj) {
        return new ResponseEntity<>(service.save(obj), HttpStatus.OK);
    }
}
