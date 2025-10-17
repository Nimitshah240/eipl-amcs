package com.eipl.amcs.master.global.bootcontroller;

import com.eipl.amcs.master.global.model.MilkQualityType;
import com.eipl.amcs.master.global.service.MilkQualityTypeService;
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
@RequestMapping("/milk-quality-types")
public class MilkQualityTypeController {

    private static final Logger LOGGER = LoggerFactory.getLogger(MilkQualityTypeController.class);
    @Autowired
    private MilkQualityTypeService service;

    @GetMapping
    public ResponseEntity<List<MilkQualityType>> index() {
        try {
            List<MilkQualityType> list = service.findAll();
            if (list == null || list.isEmpty())
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);

            return new ResponseEntity<List<MilkQualityType>>(list, HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
