package com.eipl.amcs.master.account.bootcontroller;

import com.eipl.amcs.base.service.NextCodeService;
import com.eipl.amcs.master.account.model.LedgerType;
import com.eipl.amcs.master.account.service.LedgerTypeService;
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
@RequestMapping("/ledger-types")
public class LedgerTypeController {

	@Autowired
	private LedgerTypeService service;
	@Autowired
	private NextCodeService nextCodeService;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(LedgerTypeController.class);

	@GetMapping
	public ResponseEntity<List<LedgerType>> index() {
		try {
			List<LedgerType> list = service.findAll();
			if (list == null || list.isEmpty())
				return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
			
			return new ResponseEntity<List<LedgerType>>(list, HttpStatus.OK);
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}



	@GetMapping("/next-code")
	public ResponseEntity<String> nextCode(@RequestParam(name = "society", required = true) String societyCode) {
		try {
			LOGGER.info("Next Ledger Type no for Society: {}", societyCode);
			String code = nextCodeService.getNextCode("LedgerType", "code", societyCode, 0);
			if (code == null || code.isEmpty())
				return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);

			return new ResponseEntity<>(code, HttpStatus.OK);
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping
	public ResponseEntity<LedgerType> createLedgerType(@RequestHeader Map<String, String> headers, @RequestBody LedgerType dto) {
		return new ResponseEntity<>(service.save(dto, CommonUtil.getIdentityHeader(headers)), HttpStatus.CREATED);

	}

	@PutMapping
	public ResponseEntity<LedgerType> updateLedgerType(@RequestHeader Map<String, String> headers, @RequestBody LedgerType dto) {
		return new ResponseEntity<>(service.update(dto, CommonUtil.getIdentityHeader(headers)), HttpStatus.CREATED);

	}

	@DeleteMapping("/{code}")
	public ResponseEntity<?> deleteLedgerType(@RequestHeader Map<String, String> headers,
										   @PathVariable("code") Integer code) {
		service.delete(code, CommonUtil.getIdentityHeader(headers));
		return new ResponseEntity<>(null, HttpStatus.OK);
	}

}
