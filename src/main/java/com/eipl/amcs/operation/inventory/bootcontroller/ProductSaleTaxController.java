package com.eipl.amcs.operation.inventory.bootcontroller;

import com.eipl.amcs.operation.inventory.model.ProductSaleTax;
import com.eipl.amcs.operation.inventory.service.ProductSaleTaxService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/product-sale-to-member-tax-calculates")
public class ProductSaleTaxController {

	@Autowired
	private ProductSaleTaxService service;

	private static final Logger LOGGER = LoggerFactory.getLogger(ProductSaleTaxController.class);

	@GetMapping
	public ResponseEntity<List<ProductSaleTax>> index() {
		try {
			List<ProductSaleTax> list = service.findAll();
			if (list == null || list.isEmpty())
				return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);

			return new ResponseEntity<List<ProductSaleTax>>(list, HttpStatus.OK);
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping
	public ResponseEntity<ProductSaleTax> createMember(@RequestBody ProductSaleTax dto) {
		try {
			LOGGER.info("ProductSaleToMemberTaxCalculated save method");
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
	public ResponseEntity<ProductSaleTax> updateMember(@RequestBody ProductSaleTax dto) {
		try {
			LOGGER.info("ProductSaleToMemberTaxCalculated save method");
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
	public ResponseEntity<?> deleteMember(@PathVariable("code") String code) {
		try {
			LOGGER.info("ProductSaleToMemberTaxCalculated delete method");
			Optional<ProductSaleTax> memberData = service.findById(code);
			if (memberData == null || !memberData.isPresent())
				return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);

			service.delete(memberData.get());
			return new ResponseEntity<>(null, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
