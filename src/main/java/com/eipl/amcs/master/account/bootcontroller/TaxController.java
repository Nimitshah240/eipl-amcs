package com.eipl.amcs.master.account.bootcontroller;

import com.eipl.amcs.master.account.dto.TaxDto;
import com.eipl.amcs.master.account.service.TaxService;
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
@RequestMapping("/tax")
public class TaxController {

    private static final Logger LOGGER = LoggerFactory.getLogger(TaxController.class);
    @Autowired
    private TaxService service;

    @GetMapping
    public ResponseEntity<List<TaxDto>> index() {
        try {
            List<TaxDto> list = service.findAll();
            if (list == null || list.isEmpty())
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);

            return new ResponseEntity<List<TaxDto>>(list, HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
