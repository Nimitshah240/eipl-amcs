package com.eipl.amcs.operation.procurement.bootcontroller;

import com.eipl.amcs.base.service.NextCodeService;
import com.eipl.amcs.exception.BusinessValidationFailException;
import com.eipl.amcs.master.procurement.model.SocietyMilkPurchaseRate;
import com.eipl.amcs.operation.procurement.dto.MilkDispatchDto;
import com.eipl.amcs.operation.procurement.dto.MilkDispatchSummaryDto;
import com.eipl.amcs.operation.procurement.model.MilkDispatch;
import com.eipl.amcs.operation.procurement.model.MilkDispatchTransaction;
import com.eipl.amcs.operation.procurement.repository.MilkDispatchRepository;
import com.eipl.amcs.operation.procurement.service.MilkDispatchService;
import com.eipl.amcs.utils.AppConstant;
import com.eipl.amcs.util.CommonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/milk-dispatch")
public class MilkDispatchController {

	@Autowired
	private MilkDispatchService service;
	@Autowired
	private MilkDispatchRepository repository;
	@Autowired
	private NextCodeService nextCodeService;

	private static final Logger LOGGER = LoggerFactory.getLogger(MilkDispatchController.class);
	private static final DateTimeFormatter DATE_TIME_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

	@GetMapping
	public ResponseEntity<List<MilkDispatch>> index() {
		try {
			List<MilkDispatch> list = service.findAll();
			if (list == null || list.isEmpty())
				return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
			return new ResponseEntity<List<MilkDispatch>>(list, HttpStatus.OK);
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/rate-code")
	public ResponseEntity<SocietyMilkPurchaseRate> fetchRate(@RequestParam(name = "date", required = true) String date,
			@RequestParam(name = "shiftCode", required = true) Integer shiftCode,
			@RequestParam(name = "societyCode", required = true) String societyCode) {
		try {
			LocalDateTime dt = LocalDateTime.parse(date, DATE_TIME_FMT);
			SocietyMilkPurchaseRate list = service.fetchPurchaseRateCode(dt, shiftCode, societyCode);
			if (list == null)
				return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
			return new ResponseEntity<SocietyMilkPurchaseRate>(list, HttpStatus.OK);
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping
	public ResponseEntity<MilkDispatch> createMilkDispatch(@RequestHeader Map<String, String> headers,
			@RequestBody MilkDispatchDto dto) throws BusinessValidationFailException {
//		try {
		return new ResponseEntity<>(service.save(dto, CommonUtil.getIdentityHeader(headers)), HttpStatus.CREATED);

//		} catch (Exception e) {
//			LOGGER.error(e.getMessage());
//			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
//		}
	}

	@PutMapping
	public ResponseEntity<MilkDispatch> updateMilkDispatch(@RequestHeader Map<String, String> headers,
			@RequestBody MilkDispatchDto dto) throws BusinessValidationFailException {
		return new ResponseEntity<>(service.update(dto, CommonUtil.getIdentityHeader(headers)), HttpStatus.CREATED);
	}

	@DeleteMapping("/delete")
	public ResponseEntity<?> deleteMilkDispatch(@RequestHeader Map<String, String> headers, @RequestParam String code) {
		try {
			LOGGER.info("MilkDispatch delete method");
//			Optional<MilkDispatch> dispatchtData = service.findById(code);
//			if (dispatchtData == null || !dispatchtData.isPresent())
//				return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
			service.delete(code, CommonUtil.getIdentityHeader(headers));
			return new ResponseEntity<>(null, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@DeleteMapping("/transaction")
	public ResponseEntity<?> deleteMilkDispatchTransaction(@RequestHeader Map<String, String> headers,
			@RequestParam String code) {
		try {
			LOGGER.info("MilkDispatch delete method");
			Optional<MilkDispatchTransaction> dispatchData = service.findTransactionById(code);
			if (dispatchData == null || !dispatchData.isPresent())
				return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
			service.deleteTransaction(dispatchData.get(), CommonUtil.getIdentityHeader(headers));
			return new ResponseEntity<>(null, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/next-code")
	public ResponseEntity<String> nextCode(@RequestParam String nextCode) {
		try {
			LOGGER.info("Next dock no for Society: {}", nextCode);
			String code = nextCodeService.getNextCode("MilkDispatch", "challanNo", nextCode, 3);
			if (code == null || code.isEmpty())
				return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);

			return new ResponseEntity<>(code, HttpStatus.OK);
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/transaction")
	public ResponseEntity<List<MilkDispatchTransaction>> fetchMemberDetail(@RequestParam String challanNo) {
		return new ResponseEntity<List<MilkDispatchTransaction>>(service.findDetailByChallanNo(challanNo),
				HttpStatus.OK);
	}
	
	@GetMapping("/milk-dispatch-summary")
	public ResponseEntity<List<MilkDispatchSummaryDto>> fetchMilkDispatchSummary(@RequestParam String fromDate,
	@RequestParam String toDate){
		LocalDateTime fromDt = LocalDateTime.parse(fromDate, AppConstant.DATE_TIME_FMT);
		LocalDateTime toDt = LocalDateTime.parse(toDate, AppConstant.DATE_TIME_FMT);
		return new ResponseEntity<List<MilkDispatchSummaryDto>>(service.fetchMilkDispatchSummary(fromDt, toDt), HttpStatus.OK);
	}
	@GetMapping("/milk-dispatch-by-challan-no")
	public ResponseEntity<MilkDispatch> fetchDispatch(@RequestParam String challanNo){
		return new ResponseEntity<MilkDispatch>(service.findById(challanNo).get(), HttpStatus.OK);
	}

	@GetMapping("/prev-record")
	public ResponseEntity<MilkDispatch> fetchPrevRecord(@RequestParam String fromDate) {
		LocalDateTime fromDt = LocalDateTime.parse(fromDate, AppConstant.DATE_TIME_FMT);
		return new ResponseEntity<>(repository.findPreviousRecordOfGoodMilkType(fromDt).get(), HttpStatus.OK);
	}
}