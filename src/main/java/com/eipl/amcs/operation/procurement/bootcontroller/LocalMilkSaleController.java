package com.eipl.amcs.operation.procurement.bootcontroller;

import com.eipl.amcs.base.service.NextCodeService;
import com.eipl.amcs.exception.BusinessValidationFailException;
import com.eipl.amcs.operation.procurement.model.LocalMilkSale;
import com.eipl.amcs.operation.procurement.service.LocalMilkSaleService;
import com.eipl.amcs.util.CommonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("local-milk-sale")
public class LocalMilkSaleController<DateTime> {

	@Autowired
	private NextCodeService nextCodeService;
	@Autowired
	private LocalMilkSaleService service;

	private static final Logger LOGGER = LoggerFactory.getLogger(LocalMilkSaleController.class);

	@GetMapping
	public ResponseEntity<List<LocalMilkSale>> index(@RequestParam(name = "fromDate") String fromDate,
			@RequestParam(name = "toDate") String toDate) {
		LocalDateTime fromDt = LocalDateTime.of(LocalDate.parse(fromDate), LocalTime.MIN);
		LocalDateTime toDt = LocalDateTime.of(LocalDate.parse(toDate), LocalTime.MAX);
		return new ResponseEntity<List<LocalMilkSale>>(service.findAll(fromDt, toDt), HttpStatus.OK);
	}

	@GetMapping("/fetchInvoiceNo")
	public ResponseEntity<String> getInvoiceNoNextCode(@RequestParam String code) {
		try {
			LOGGER.info("Next grn no for Society: {}", code);
			String codes = nextCodeService.getNextCode("LocalMilkSale", "invoiceNo", code, 5);
			if (codes == null || codes.isEmpty())
				return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);

			return new ResponseEntity<>(codes, HttpStatus.OK);
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping
	public ResponseEntity<LocalMilkSale> createLocalMilkSale(@RequestHeader Map<String, String> headers,
			@RequestBody @Valid LocalMilkSale dto) throws BusinessValidationFailException {
		return new ResponseEntity<>(service.save(dto, CommonUtil.getIdentityHeader(headers)), HttpStatus.CREATED);
	}

	@PutMapping("/{code}")
	public ResponseEntity<LocalMilkSale> updateLocalMilkSale(@RequestHeader Map<String, String> headers,
			@RequestBody LocalMilkSale dto) {
		return new ResponseEntity<>(service.update(dto, CommonUtil.getIdentityHeader(headers)), HttpStatus.CREATED);
	}

	@DeleteMapping("/{code}")
	public ResponseEntity<?> deleteLocalMilkSale(@RequestHeader Map<String, String> headers,
			@PathVariable("code") String code) {
		service.delete(code, CommonUtil.getIdentityHeader(headers));
		return new ResponseEntity<>(null, HttpStatus.OK);
	}

	@PostMapping("/migrate")
	public ResponseEntity<List<LocalMilkSale>> migrateData(@RequestHeader Map<String, String> headers,
																 @RequestBody List<LocalMilkSale> dtoList) {
		return new ResponseEntity<List<LocalMilkSale>>(
				service.migrateCollections(dtoList, CommonUtil.getIdentityHeader(headers)), HttpStatus.OK);
	}
}