package com.eipl.amcs.master.account.bootcontroller;

import com.eipl.amcs.base.service.NextCodeService;
import com.eipl.amcs.master.account.model.StaffMember;
import com.eipl.amcs.master.account.service.StaffMemberService;
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
@RequestMapping("/staff-member")
public class StaffMemberController {

    private static final Logger LOGGER = LoggerFactory.getLogger(StaffMemberController.class);
    @Autowired
    private StaffMemberService service;
    @Autowired
    private NextCodeService nextCodeService;

    @GetMapping
    public ResponseEntity<List<StaffMember>> index() {
        try {
            List<StaffMember> list = service.findAll();
            if (list == null || list.isEmpty())
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);

            return new ResponseEntity<List<StaffMember>>(list, HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/next-code")
    public ResponseEntity<String> nextCode(@RequestParam(name = "society", required = true) String societyCode) {
        try {
            LOGGER.info("Next Staff no for Society: {}", societyCode);
            String code = nextCodeService.getNextCode("StaffMember", "code", societyCode, 0);
            if (code == null || code.isEmpty())
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);

            return new ResponseEntity<>(code, HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping
    public ResponseEntity<StaffMember> createStaffMember(@RequestHeader Map<String, String> headers, @RequestBody StaffMember dto) {
        return new ResponseEntity<>(service.save(dto, CommonUtil.getIdentityHeader(headers)), HttpStatus.CREATED);

    }

    @PutMapping
    public ResponseEntity<StaffMember> updateStaffMember(@RequestHeader Map<String, String> headers, @RequestBody StaffMember dto) {

        try {
            LOGGER.info("StaffMember save method");
            dto = service.update(dto, CommonUtil.getIdentityHeader(headers));
            if (dto == null)
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
            return new ResponseEntity<>(dto, HttpStatus.CREATED);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{code}")
    public ResponseEntity<?> deleteStaffMember(@RequestHeader Map<String, String> headers,
                                               @PathVariable("code") String code) {
        service.delete(code, CommonUtil.getIdentityHeader(headers));
        return new ResponseEntity<>(null, HttpStatus.OK);
    }
}
