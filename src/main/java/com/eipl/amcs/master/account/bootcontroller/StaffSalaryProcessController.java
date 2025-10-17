package com.eipl.amcs.master.account.bootcontroller;

import com.eipl.amcs.master.account.model.StaffSalaryProcess;
import com.eipl.amcs.master.account.service.StaffSalaryProcessService;
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
@RequestMapping("/staff-salary-process")
public class StaffSalaryProcessController {

    private static final Logger LOGGER = LoggerFactory.getLogger(StaffSalaryProcessController.class);
    @Autowired
    private StaffSalaryProcessService service;

    @GetMapping
    public ResponseEntity<List<StaffSalaryProcess>> index() {
        try {
            List<StaffSalaryProcess> list = service.findAll();
            if (list == null || list.isEmpty())
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);

            return new ResponseEntity<List<StaffSalaryProcess>>(list, HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
