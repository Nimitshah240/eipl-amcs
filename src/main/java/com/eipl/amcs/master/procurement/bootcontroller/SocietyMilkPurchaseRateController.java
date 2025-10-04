package com.eipl.amcs.master.procurement.bootcontroller;

import com.eipl.amcs.exception.BusinessValidationFailException;
import com.eipl.amcs.master.procurement.dto.SocietyMilkPurchaseRateDto;
import com.eipl.amcs.master.procurement.model.SocietyMilkPurchaseRate;
import com.eipl.amcs.master.procurement.model.SocietyMilkPurchaseRateBased;
import com.eipl.amcs.master.procurement.service.SocietyMilkPurchaseRateService;
import com.eipl.amcs.operation.procurement.dto.MilkDispatchRateAndDetailsDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/society-milk-purchase-rates")
public class SocietyMilkPurchaseRateController {

	@Autowired
	private SocietyMilkPurchaseRateService service;

	private static final Logger LOGGER = LoggerFactory.getLogger(SocietyMilkPurchaseRateController.class);

	@GetMapping
	public ResponseEntity<List<SocietyMilkPurchaseRate>> index() {
		try {
			List<SocietyMilkPurchaseRate> list = service.findAll();
			if (list == null || list.isEmpty())
				return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);

			return new ResponseEntity<List<SocietyMilkPurchaseRate>>(list, HttpStatus.OK);
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/view/{code}/{milkTypeCode}/{milkQualityTypeCode}")
	public ResponseEntity<List<String>> fetchRateView(@PathVariable(name = "code") String code,
			@PathVariable(name = "milkTypeCode") Integer milkTypeCode,
			@PathVariable(name = "milkQualityTypeCode") Integer milkQualityTypeCode) {
		return new ResponseEntity<List<String>>(service.fetchRateDetails(code, milkTypeCode, milkQualityTypeCode), HttpStatus.OK);
	}

	@PostMapping
	public ResponseEntity<String> save(@RequestBody SocietyMilkPurchaseRateDto dto)
			throws BusinessValidationFailException {
		return new ResponseEntity<String>(service.savePurchaseRate(dto), HttpStatus.CREATED);
	}

	@GetMapping("/rate-and-details/{code}")
	public ResponseEntity<MilkDispatchRateAndDetailsDto> fetchRateAndDetails(@PathVariable(name = "code") String code) {
		return new ResponseEntity<MilkDispatchRateAndDetailsDto>(service.fetchRateAndDetails(code), HttpStatus.OK);
	}
	@GetMapping("/based")
	public ResponseEntity<List<SocietyMilkPurchaseRateBased>> fetchRateBased(@RequestParam(name = "code") String code) {
		return new ResponseEntity<List<SocietyMilkPurchaseRateBased>>(service.fetchRateBased(code), HttpStatus.OK);
	}
}
