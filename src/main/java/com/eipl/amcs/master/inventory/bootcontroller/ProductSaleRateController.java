package com.eipl.amcs.master.inventory.bootcontroller;

import com.eipl.amcs.base.service.NextCodeService;
import com.eipl.amcs.exception.BusinessValidationFailException;
import com.eipl.amcs.master.inventory.model.ProductSaleRate;
import com.eipl.amcs.master.inventory.repository.ProductRepository;
import com.eipl.amcs.master.inventory.service.ProductSaleRateService;
import com.eipl.amcs.master.org.model.Union;
import com.eipl.amcs.util.CommonUtil;
import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/product-sale-rates")
public class ProductSaleRateController {

	@Autowired
	private ProductSaleRateService service;
	@Autowired
	private NextCodeService nextCodeService;
	@Autowired
	private ProductRepository repository;

	private static final Logger LOGGER = LoggerFactory.getLogger(ProductSaleRateController.class);

	@GetMapping
	public ResponseEntity<List<ProductSaleRate>> index() {
		try {
			List<ProductSaleRate> list = service.findAll();
			if (list == null || list.isEmpty())
				return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);

			return new ResponseEntity<List<ProductSaleRate>>(list, HttpStatus.OK);
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/findRate")
	public ResponseEntity<ProductSaleRate> indexByProduct(@RequestParam("code") String code,
			@RequestParam("date") String date) {
		try {
			LocalDate dt = LocalDate.parse(date);
			ProductSaleRate list = service.findByProduct(repository.findById(code).get(), dt);
			list.setUnion(Hibernate.unproxy(list.getUnion(), Union.class));
			return new ResponseEntity<ProductSaleRate>(list, HttpStatus.OK);
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping
	public ResponseEntity<ProductSaleRate> createProductSaleRate(@RequestHeader Map<String, String> headers,
			@RequestBody ProductSaleRate dto) throws BusinessValidationFailException {
//		try {
//			LOGGER.info("ProductSaleRate save method");
//			dto = service.save(dto);
//			if (dto == null)
//				return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
//			return new ResponseEntity<>(dto, HttpStatus.CREATED);
//		} catch (Exception e) {
//			LOGGER.error(e.getMessage());
//			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
//		}
		return new ResponseEntity<>(service.save(dto, CommonUtil.getIdentityHeader(headers)), HttpStatus.CREATED);

	}

	@PutMapping
	public ResponseEntity<ProductSaleRate> updateProductSaleRate(@RequestHeader Map<String, String> headers,
			@RequestBody ProductSaleRate dto) throws BusinessValidationFailException {
//		try {
//			LOGGER.info("ProductSaleRate save method");
//			dto = service.save(dto);
//			if (dto == null)
//				return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
//			return new ResponseEntity<>(dto, HttpStatus.CREATED);
//		} catch (Exception e) {
//			LOGGER.error(e.getMessage());
//			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
//		}
		return new ResponseEntity<>(service.update(dto, CommonUtil.getIdentityHeader(headers)), HttpStatus.CREATED);

	}

	@DeleteMapping("/{code}")
	public ResponseEntity<?> deleteProductSaleRate(@RequestHeader Map<String, String> headers,
			@PathVariable("code") String code) {
		try {
			LOGGER.info("ProductSaleRate delete method");
			Optional<ProductSaleRate> productData = service.findById(code);
			if (productData == null || !productData.isPresent())
				return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);

			service.delete(productData.get(), CommonUtil.getIdentityHeader(headers));
			return new ResponseEntity<>(null, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/next-code")
	public ResponseEntity<String> nextCode(@RequestParam(name = "society", required = true) String societyCode) {
		try {
			LOGGER.info("Next dock no for Society: {}", societyCode);
			String code = nextCodeService.getNextCode("ProductSaleRate", "code", societyCode, 4);
			if (code == null || code.isEmpty())
				return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);

			return new ResponseEntity<>(code, HttpStatus.OK);
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
