package com.eipl.amcs.operation.inventory.bootcontroller;

import com.eipl.amcs.base.service.NextCodeService;
import com.eipl.amcs.master.inventory.repository.ProductRepository;
import com.eipl.amcs.operation.inventory.model.ProductStock;
import com.eipl.amcs.operation.inventory.service.ProductStockService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/product-stocks")
public class ProductStockController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductStockController.class);
    @Autowired
    private ProductStockService service;
    @Autowired
    private NextCodeService nextCodeService;
    @Autowired
    private ProductRepository repository;

    @GetMapping
    public ResponseEntity<List<ProductStock>> index() {
        try {
            List<ProductStock> list = service.findAll();
            if (list == null || list.isEmpty())
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);

            return new ResponseEntity<List<ProductStock>>(list, HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{code}")
    public ResponseEntity<ProductStock> indexById(@PathVariable("code") String code) {
        try {
            ProductStock list = service.findByProduct(code);
            if (list == null)
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);

            return new ResponseEntity<ProductStock>(list, HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/next-code")
    public ResponseEntity<String> nextCode(@RequestParam(name = "society", required = true) String societyCode) {
        try {
            LOGGER.info("Next productStock no for Society: {}", societyCode);
            String code = nextCodeService.getNextCode("ProductStock", "code", societyCode, 2);
            if (code == null || code.isEmpty())
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);

            return new ResponseEntity<>(code, HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping
    public ResponseEntity<ProductStock> createProduct(@RequestBody ProductStock dto) {
        try {
            LOGGER.info("ProductStock save method");
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
    public ResponseEntity<ProductStock> updateProduct(@RequestBody ProductStock dto) {
        try {
            LOGGER.info("ProductStock save method");
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
    public ResponseEntity<?> deleteProduct(@PathVariable("code") String code) {
        try {
            LOGGER.info("ProductStock delete method");
            Optional<ProductStock> productStockData = service.findById(code);
            if (productStockData == null || !productStockData.isPresent())
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);

            service.delete(productStockData.get());
            return new ResponseEntity<>(null, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
