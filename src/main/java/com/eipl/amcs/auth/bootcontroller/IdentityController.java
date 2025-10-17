package com.eipl.amcs.auth.bootcontroller;

import com.eipl.amcs.auth.dto.IdentityDto;
import com.eipl.amcs.auth.service.IdentityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/identity")
public class IdentityController {

    private static final Logger LOGGER = LoggerFactory.getLogger(IdentityController.class);
    @Autowired
    private IdentityService service;

    @GetMapping("/{dockNumber}/{societyCode}/{unionCode}")
    public ResponseEntity<IdentityDto> fetchIdentity(@PathVariable(name = "dockNumber") String dockNumber,
                                                     @PathVariable(name = "societyCode") String societyCode, @PathVariable(name = "unionCode") String unionCode) {
        try {
            return new ResponseEntity<IdentityDto>(service.fetchIdentity(dockNumber, societyCode, unionCode), HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
