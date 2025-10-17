package com.eipl.amcs.master.operation.bootcontroller;

import com.eipl.amcs.master.operation.model.MemberCreditLimit;
import com.eipl.amcs.master.operation.service.MemberCreditLimitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/membercreditlimit")
public class MemberCreditLimitController {

    @Autowired
    private MemberCreditLimitService service;

    @GetMapping
    public ResponseEntity<MemberCreditLimit> index(@RequestParam String code, @RequestParam Short type) {
        try {
            Optional<MemberCreditLimit> list = service.findByConsumerCodeAndConsumerType(code, type);
            if (list == null || !list.isPresent())
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);

            return new ResponseEntity<MemberCreditLimit>(list.get(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/codeAndType")
    public ResponseEntity<MemberCreditLimit> findByCodeAndType(@RequestParam String code, @RequestParam Short type) {
        try {
            Optional<MemberCreditLimit> list = service.findByConsumerCodeAndConsumerType(code, type);
            if (list == null || !list.isPresent())
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);

            return new ResponseEntity<MemberCreditLimit>(list.get(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
