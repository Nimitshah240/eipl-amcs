package com.eipl.amcs.master.account.bootcontroller;

import com.eipl.amcs.base.service.NextCodeService;
import com.eipl.amcs.exception.BusinessValidationFailException;
import com.eipl.amcs.master.account.model.StaffSalaryMapping;
import com.eipl.amcs.master.account.service.StaffSalaryMappingService;
import com.eipl.amcs.util.CommonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/staff-salary-mapping")
public class StaffSalaryMappingController {

    private static final Logger LOGGER = LoggerFactory.getLogger(StaffSalaryMappingController.class);
    @Autowired
    private StaffSalaryMappingService service;
    @Autowired
    private NextCodeService nextCodeService;

    @GetMapping
    public ResponseEntity<List<StaffSalaryMapping>> index() {
        try {
            List<StaffSalaryMapping> list = service.findAll();
            if (list == null || list.isEmpty())
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);

            return new ResponseEntity<List<StaffSalaryMapping>>(list, HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping
    public ResponseEntity<String> createSocietyPaymentCycle(@RequestHeader Map<String, String> headers,
                                                            @RequestBody List<StaffSalaryMapping> dto) throws BusinessValidationFailException {
        return new ResponseEntity<>(service.save(dto, CommonUtil.getIdentityHeader(headers)), HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<StaffSalaryMapping> updateStaffSalaryMapping(@RequestHeader Map<String, String> headers, @RequestBody StaffSalaryMapping dto) {

        try {
            LOGGER.info("StaffSalaryMapping save method");
            //	dto = service.update(dto, CommonUtil.getIdentityHeader(headers));
            if (dto == null)
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
            return new ResponseEntity<>(dto, HttpStatus.CREATED);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{code}")
    public ResponseEntity<?> deleteStaffSalaryMapping(@RequestHeader Map<String, String> headers,
                                                      @PathVariable("code") String code) {
        service.delete(code, CommonUtil.getIdentityHeader(headers));
        return new ResponseEntity<>(null, HttpStatus.OK);
    }


    @GetMapping("/next-code")
    public ResponseEntity<String> nextCode(@RequestParam(name = "society", required = true) String societyCode) {
        try {
            LOGGER.info("Next product no for StaffSalaryMapping: {}", societyCode);
            String code = nextCodeService.getNextCode("StaffSalaryMapping", "code", societyCode, 2);
            if (code == null || code.isEmpty())
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);

            return new ResponseEntity<>(code, HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
