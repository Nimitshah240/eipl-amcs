package com.eipl.amcs.operation.inventory.bootcontroller;

import com.eipl.amcs.base.service.NextCodeService;
import com.eipl.amcs.operation.inventory.model.ProductReceiptTax;
import com.eipl.amcs.operation.inventory.service.ProductReceiptTaxService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/product-receipt-tax")
public class ProductReceiptTaxController {

	@Autowired
	private ProductReceiptTaxService service;

	@Autowired
	private NextCodeService nextCodeService;
	private static final Logger LOGGER = LoggerFactory.getLogger(ProductReceiptTaxController.class);

	@GetMapping
	public ResponseEntity<List<ProductReceiptTax>> index() {
		try {
			List<ProductReceiptTax> list = service.findAll();
			if (list == null || list.isEmpty())
				return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);

			return new ResponseEntity<List<ProductReceiptTax>>(list, HttpStatus.OK);
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/next-code")
	public ResponseEntity<String> nextCode(@RequestParam(name = "society", required = true) String societyCode) {
		try {
			LOGGER.info("Next productReceiptTax no for Society: {}", societyCode);
			String code = nextCodeService.getNextCode("ProductReceiptTax", "code", societyCode, 2);
			if (code == null || code.isEmpty())
				return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);

			return new ResponseEntity<>(code, HttpStatus.OK);
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping
	public ResponseEntity<ProductReceiptTax> createProductReceiptTaxCalculated(
			@RequestBody ProductReceiptTax dto) {
		try {
			LOGGER.info("ProductReceiptTax save method");
			dto = service.save(dto);
			if (dto == null)
				return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);

			return new ResponseEntity<>(dto, HttpStatus.CREATED);
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PutMapping
	public ResponseEntity<ProductReceiptTax> updateProductReceiptTaxCalculated(
			@RequestBody ProductReceiptTax dto) {
		try {
			LOGGER.info("ProductReceiptTax save method");
			dto = service.update(dto);
			if (dto == null)
				return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);

			return new ResponseEntity<>(dto, HttpStatus.CREATED);
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@DeleteMapping("/{code}")
	public ResponseEntity<?> deleteProductReceiptTaxCalculated(@PathVariable("code") String code) {
		try {
			LOGGER.info("ProductReceiptTax delete method");
			Optional<ProductReceiptTax> productReceiptTaxCalculatedData = service.findById(code);
			if (productReceiptTaxCalculatedData == null || !productReceiptTaxCalculatedData.isPresent())
				return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);

			service.delete(productReceiptTaxCalculatedData.get());
			return new ResponseEntity<>(null, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
