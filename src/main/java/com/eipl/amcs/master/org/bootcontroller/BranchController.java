package com.eipl.amcs.master.org.bootcontroller;

import com.eipl.amcs.master.org.model.Branch;
import com.eipl.amcs.master.org.service.BranchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/branches")
public class BranchController {

    private static final Logger LOGGER = LoggerFactory.getLogger(BranchController.class);
    @Autowired
    private BranchService service;

    @GetMapping
    public ResponseEntity<List<Branch>> index(@RequestParam(name = "bankCode", required = false) String bankCode) {
        try {
            List<Branch> list = null;
            if (bankCode == null || bankCode.isEmpty())
                list = service.findAll();
            else
                list = service.findAll(bankCode);

            if (list == null || list.isEmpty())
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);

            return new ResponseEntity<List<Branch>>(list, HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
