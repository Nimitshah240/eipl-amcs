package com.eipl.amcs.master.account.bootcontroller;

import com.eipl.amcs.base.service.NextCodeService;
import com.eipl.amcs.master.account.model.SubLedgerOpeningBalance;
import com.eipl.amcs.master.account.service.SubLedgerOpeningBalanceService;
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
@RequestMapping("/sub_ledger_opening_balance")
public class SubLedgerOpeningBalanceController {

	@Autowired
	private SubLedgerOpeningBalanceService service;
	@Autowired
	private NextCodeService nextCodeService;

	private static final Logger LOGGER = LoggerFactory.getLogger(SubLedgerOpeningBalanceController.class);

	@GetMapping
	public ResponseEntity<List<SubLedgerOpeningBalance>> index() {
		try {
			List<SubLedgerOpeningBalance> list = service.findAll();
			if (list == null || list.isEmpty())
				return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
			return new ResponseEntity<List<SubLedgerOpeningBalance>>(list, HttpStatus.OK);
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping
	public ResponseEntity<SubLedgerOpeningBalance> createSubLedgerOpeningBalance(@RequestHeader Map<String, String> headers, @RequestBody SubLedgerOpeningBalance dto) {
	return new ResponseEntity<>(service.save(dto, CommonUtil.getIdentityHeader(headers)), HttpStatus.CREATED);

	}

	@PutMapping
	public ResponseEntity<SubLedgerOpeningBalance> updateSubLedgerOpeningBalance(@RequestHeader Map<String, String> headers, @RequestBody SubLedgerOpeningBalance dto) {
		return new ResponseEntity<>(service.update(dto, CommonUtil.getIdentityHeader(headers)), HttpStatus.CREATED);
	}


	@DeleteMapping("/{code}")
	public ResponseEntity<?> deleteSubLedgerOpeningBalance(@RequestHeader Map<String, String> headers,
										   @PathVariable("code") String code) {
		service.delete(code, CommonUtil.getIdentityHeader(headers));
		return new ResponseEntity<>(null, HttpStatus.OK);
	}
	@PostMapping("/import")
	public ResponseEntity<List<SubLedgerOpeningBalance>> importSubLedgerBalance(@RequestHeader Map<String, String> headers,
															  @RequestBody List<SubLedgerOpeningBalance> dtoList) {
		return new ResponseEntity<List<SubLedgerOpeningBalance>>(
				service.importSubLedgerBalance(dtoList, CommonUtil.getIdentityHeader(headers)), HttpStatus.OK);
	}

	}


