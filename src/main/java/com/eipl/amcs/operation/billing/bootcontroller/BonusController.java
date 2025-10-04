package com.eipl.amcs.operation.billing.bootcontroller;

import com.eipl.amcs.exception.BusinessValidationFailException;
import com.eipl.amcs.operation.billing.dto.BonusDto;
import com.eipl.amcs.operation.billing.model.Bonus;
import com.eipl.amcs.operation.billing.model.BonusSummary;
import com.eipl.amcs.operation.billing.service.BonusService;
import com.eipl.amcs.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/bonus")
public class BonusController {
	@Autowired
	private BonusService service;

	@GetMapping("/summary")
	public ResponseEntity<List<BonusSummary>> fetchBillSummary() {
		return new ResponseEntity<List<BonusSummary>>(service.findBonusSummaryBetWeen(),
				HttpStatus.OK);
//		return null;
	}

	@GetMapping
	public ResponseEntity<BonusDto> fetchBonus(@RequestParam("code") String code) {
		return new ResponseEntity<BonusDto>(service.findBySummary(code), HttpStatus.OK);
	}

	@GetMapping("/loaddata")
	public ResponseEntity<List<Bonus>> fetchBonus(@RequestParam("fromDate") String fromDate,
			@RequestParam("toDate") String toDate,
			@RequestParam("milkType") Integer milkType
	) {
//		service.callSp("1011003");

		LocalDateTime from = LocalDateTime.parse(fromDate);
		LocalDateTime to = LocalDateTime.parse(toDate);
		return new ResponseEntity<List<Bonus>>(service.loadData(from, to,milkType), HttpStatus.OK);

	}


	@GetMapping("/loadbonus")
	public ResponseEntity<Map<String,Object>> fetchBonusData(@RequestParam("fromDate") String fromDate,
												  @RequestParam("toDate") String toDate,
												  @RequestParam("memberCode") String memberCode
	) {
		LocalDate from = LocalDate.parse(fromDate);
		LocalDate to = LocalDate.parse(toDate);
		return new ResponseEntity<Map<String,Object>>(service.loadDataBonus(from, to,memberCode), HttpStatus.OK);

	}

	@GetMapping("/loadbonussummary")
	public ResponseEntity<List<Map<String, Object>> > fetchBonusDatasummary(@RequestParam("fromDate") String fromDate,
												  @RequestParam("toDate") String toDate

	) {
		LocalDate from = LocalDate.parse(fromDate);
		LocalDate to = LocalDate.parse(toDate);
		return new ResponseEntity<List<Map<String, Object>> >(service.loadDataBonusSummary(from, to), HttpStatus.OK);

	}

	@PostMapping
	public ResponseEntity<BonusDto> createBonus(@RequestHeader Map<String, String> headers,@RequestBody BonusDto dto) throws BusinessValidationFailException {
		return new ResponseEntity<>((service.saveDto(CommonUtil.getIdentityHeader(headers),dto,(short)0)),HttpStatus.OK);
	}

	@PutMapping
	public ResponseEntity<BonusDto> updateBonus(@RequestHeader Map<String, String> headers,@RequestBody BonusDto dto) throws BusinessValidationFailException {
		return new ResponseEntity<>((service.updateDto(CommonUtil.getIdentityHeader(headers),dto)),HttpStatus.OK);
	}
	@PutMapping("/edit")
	public ResponseEntity<BonusDto> editBonus(@RequestHeader Map<String, String> headers,@RequestBody BonusDto dto) throws BusinessValidationFailException {
		return new ResponseEntity<>((service.editDto(CommonUtil.getIdentityHeader(headers),dto)),HttpStatus.OK);
	}

	@DeleteMapping("/{code}")
	public ResponseEntity<?> deleteMember(@RequestHeader Map<String, String> headers,@PathVariable("code") String code) {
		try {
			service.deleteDto(CommonUtil.getIdentityHeader(headers),code);
			return new ResponseEntity<>(null, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}



}
