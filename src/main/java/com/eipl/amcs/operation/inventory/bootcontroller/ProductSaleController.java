package com.eipl.amcs.operation.inventory.bootcontroller;

import com.eipl.amcs.base.service.NextCodeService;
import com.eipl.amcs.exception.BusinessValidationFailException;
import com.eipl.amcs.operation.inventory.dto.ProductSaleDto;
import com.eipl.amcs.operation.inventory.dto.ProductSaleMigrateDto;
import com.eipl.amcs.operation.inventory.model.ProductSale;
import com.eipl.amcs.operation.inventory.service.ProductSaleService;
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
@RequestMapping("/product-sale")
public class ProductSaleController {

	@Autowired
	private ProductSaleService service;
	@Autowired
	private NextCodeService nextCodeService;

	private static final Logger LOGGER = LoggerFactory.getLogger(ProductSaleController.class);

	@GetMapping
	public ResponseEntity<List<ProductSale>> index(@RequestParam(name ="fromDate") String fromDate,
			@RequestParam(name ="toDate") String toDate) {
		LocalDate fromDt = LocalDate.parse(fromDate);
		LocalDate toDt = LocalDate.parse(toDate);
		return new ResponseEntity<List<ProductSale>>(service.findAll(fromDt, toDt), HttpStatus.OK);
	}

	@PostMapping
	public ResponseEntity<ProductSaleDto> createProductSale(@RequestHeader Map<String, String> headers,@RequestBody ProductSaleDto dto)
			throws BusinessValidationFailException {
		return new ResponseEntity<>(service.save(dto,CommonUtil.getIdentityHeader(headers)), HttpStatus.CREATED);
	}

	@GetMapping("/next-code")
	public ResponseEntity<String> nextCode(@RequestHeader Map<String, String> headers,@RequestParam String code) {
		try {
			LOGGER.info("Next Invoice no for ProductSale: {}", code);
			String codeI = nextCodeService.getNextCode("ProductSale", "invoiceNo", code, 6);
			if (codeI == null || codeI.isEmpty())
				return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);

			return new ResponseEntity<>(codeI, HttpStatus.OK);
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PutMapping
	public ResponseEntity<ProductSaleDto> updateProductSale(@RequestHeader Map<String, String> headers,@RequestBody ProductSaleDto dto) throws BusinessValidationFailException {
		return new ResponseEntity<>(service.update(dto,CommonUtil.getIdentityHeader(headers)), HttpStatus.CREATED);

	}

	@DeleteMapping("/delete")
	public ResponseEntity<?> deleteProductSale(@RequestHeader Map<String, String> headers,@RequestParam String invoiceNo) {
		service.delete(invoiceNo,CommonUtil.getIdentityHeader(headers));
		return new ResponseEntity<>(null,HttpStatus.OK);
	}

	@PostMapping("/migrate")
	public ResponseEntity<List<ProductSaleMigrateDto>> migrateData(@RequestHeader Map<String, String> headers,
																   @RequestBody List<ProductSaleMigrateDto> dtoList) {
		return new ResponseEntity<List<ProductSaleMigrateDto>>(
				service.migrateCollections(dtoList, CommonUtil.getIdentityHeader(headers)), HttpStatus.OK);
	}
}
