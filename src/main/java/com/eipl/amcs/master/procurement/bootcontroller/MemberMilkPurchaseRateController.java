package com.eipl.amcs.master.procurement.bootcontroller;

import com.eipl.amcs.exception.BusinessValidationFailException;
import com.eipl.amcs.master.procurement.dto.MemberMilkPurchaseRateDto;
import com.eipl.amcs.master.procurement.model.MemberMilkPurchaseRate;
import com.eipl.amcs.master.procurement.model.MemberMilkPurchaseRateBased;
import com.eipl.amcs.master.procurement.service.MemberMilkPurchaseRateService;
import com.eipl.amcs.operation.procurement.dto.MilkRateAndDetailsDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/member-milk-purchase-rates")
public class MemberMilkPurchaseRateController {

    private static final Logger LOGGER = LoggerFactory.getLogger(MemberMilkPurchaseRateController.class);
    @Autowired
    private MemberMilkPurchaseRateService service;

    @GetMapping
    public ResponseEntity<List<MemberMilkPurchaseRate>> index() {
        try {
            List<MemberMilkPurchaseRate> list = service.findAll();
            if (list == null || list.isEmpty())
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);

            return new ResponseEntity<List<MemberMilkPurchaseRate>>(list, HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/based")
    public ResponseEntity<List<MemberMilkPurchaseRateBased>> fetchRateBased(@RequestParam(name = "code") String code) {
        return new ResponseEntity<List<MemberMilkPurchaseRateBased>>(service.fetchRateBased(code), HttpStatus.OK);
    }

    @GetMapping("/view/{code}/{milkTypeCode}/{milkQualityTypeCode}")
    public ResponseEntity<List<String>> fetchRateView(@PathVariable(name = "code") String code,
                                                      @PathVariable(name = "milkTypeCode") Integer milkTypeCode,
                                                      @PathVariable(name = "milkQualityTypeCode") Integer milkQualityTypeCode) {
        return new ResponseEntity<List<String>>(service.fetchRateDetails(code, milkTypeCode, milkQualityTypeCode), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<String> save(@RequestBody MemberMilkPurchaseRateDto dto)
            throws BusinessValidationFailException {
        return new ResponseEntity<String>(service.savePurchaseRate(dto), HttpStatus.CREATED);
    }

    @GetMapping("/rate-and-details/{code}")
    public ResponseEntity<MilkRateAndDetailsDto> fetchRateAndDetails(@PathVariable(name = "code") String code) {
        return new ResponseEntity<MilkRateAndDetailsDto>(service.fetchRateAndDetails(code), HttpStatus.OK);
    }
}
