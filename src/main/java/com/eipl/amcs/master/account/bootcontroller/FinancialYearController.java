package com.eipl.amcs.master.account.bootcontroller;

import com.eipl.amcs.master.account.dto.YearClosingDto;
import com.eipl.amcs.master.account.model.FinancialYear;
import com.eipl.amcs.master.account.repository.FinancialYearRepository;
import com.eipl.amcs.master.account.service.FinancialYearService;
import com.eipl.amcs.util.CommonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/financial-years")
public class FinancialYearController {

	@Autowired
	private FinancialYearService service;

	@Autowired
	private FinancialYearRepository financialYearRepository;

	private static final Logger LOGGER = LoggerFactory.getLogger(FinancialYearController.class);

	@GetMapping
	public ResponseEntity<List<FinancialYear>> index() {
		try {
			List<FinancialYear> list = service.findAll();
			if (list == null || list.isEmpty())
				return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
			
			return new ResponseEntity<List<FinancialYear>>(list, HttpStatus.OK);
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	@GetMapping("/next_fy_date")
	public ResponseEntity<List<LocalDate>> nextFyDate(@RequestParam String date) {
		LocalDate currentDate = LocalDate.parse(date);
		try {
			List<LocalDate> list = financialYearRepository.fetchByDate(currentDate);
			if (list == null || list.isEmpty())
				return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
			return new ResponseEntity<List<LocalDate>>(list, HttpStatus.OK);
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/fetch_code")
	public ResponseEntity<Boolean> nextFyYear(String financialYearCode) {
		try {
			Integer  ints = financialYearRepository.fetchByCode(financialYearCode);
			if (ints>0)
				return new ResponseEntity<Boolean>(false, HttpStatus.OK);
			else
				return new ResponseEntity<>(true, HttpStatus.OK);
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}


	@PostMapping
	public ResponseEntity<YearClosingDto> createLedgerOpeningBalance(@RequestHeader Map<String, String> headers, @RequestBody YearClosingDto dto) {
		return new ResponseEntity<>(service.saveDto(dto, CommonUtil.getIdentityHeader(headers)), HttpStatus.CREATED);
	}

}
