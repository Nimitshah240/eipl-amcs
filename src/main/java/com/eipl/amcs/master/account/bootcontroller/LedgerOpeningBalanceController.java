package com.eipl.amcs.master.account.bootcontroller;

import com.eipl.amcs.base.service.NextCodeService;
import com.eipl.amcs.master.account.model.LedgerOpeningBalance;
import com.eipl.amcs.master.account.service.LedgerOpeningBalanceService;
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
@RequestMapping("/ledger_opening_balance")
public class LedgerOpeningBalanceController {

	@Autowired
	private LedgerOpeningBalanceService service;

	@Autowired
	private NextCodeService nextCodeService;

	private static final Logger LOGGER = LoggerFactory.getLogger(LedgerOpeningBalanceController.class);

	@GetMapping
	public ResponseEntity<List<LedgerOpeningBalance>> index() {
		try {
			List<LedgerOpeningBalance> list = service.findAll();
			if (list == null || list.isEmpty())
				return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
			return new ResponseEntity<List<LedgerOpeningBalance>>(list, HttpStatus.OK);
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping
	public ResponseEntity<LedgerOpeningBalance> createLedgerOpeningBalance(@RequestHeader Map<String, String> headers, @RequestBody LedgerOpeningBalance dto) {
	return new ResponseEntity<>(service.save(dto, CommonUtil.getIdentityHeader(headers)), HttpStatus.CREATED);
	}

	@PutMapping
	public ResponseEntity<LedgerOpeningBalance> updateLedgerOpeningBalance(@RequestHeader Map<String, String> headers, @RequestBody LedgerOpeningBalance dto) {
		return new ResponseEntity<>(service.update(dto, CommonUtil.getIdentityHeader(headers)), HttpStatus.CREATED);
	}


	@DeleteMapping("/{code}")
	public ResponseEntity<?> deleteLedgerOpeningBalance(@RequestHeader Map<String, String> headers,
										   @PathVariable("code") String code) {
		service.delete(code, CommonUtil.getIdentityHeader(headers));
		return new ResponseEntity<>(null, HttpStatus.OK);
	}

	@PostMapping("/import")
	public ResponseEntity<List<LedgerOpeningBalance>> importSubLedgerBalance(@RequestHeader Map<String, String> headers,
																				@RequestBody List<LedgerOpeningBalance> dtoList) {
		return new ResponseEntity<List<LedgerOpeningBalance>>(
				service.importLedgerBalance(dtoList, CommonUtil.getIdentityHeader(headers)), HttpStatus.OK);
	}


}

