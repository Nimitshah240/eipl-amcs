package com.eipl.amcs.master.inventory.bootcontroller;

import com.eipl.amcs.base.service.NextCodeService;
import com.eipl.amcs.master.inventory.model.Product;
import com.eipl.amcs.master.inventory.model.ProductAndSaleRateDto;
import com.eipl.amcs.master.inventory.service.ProductService;
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
@RequestMapping("/products")
public class ProductController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductController.class);
    @Autowired
    private ProductService service;
    @Autowired
    private NextCodeService nextCodeService;

    @GetMapping
    public ResponseEntity<List<Product>> index(@RequestParam(name = "society", required = false) String societyCode) {
        try {
            List<Product> list = null;
            if (societyCode == null || societyCode.isEmpty())
                list = service.findAll();
            else
                list = service.findAllBySociety(societyCode);
            if (list == null || list.isEmpty())
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
            return new ResponseEntity<List<Product>>(list, HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/next-code")
    public ResponseEntity<String> nextCode(@RequestParam(name = "society", required = true) String societyCode) {
        try {
            LOGGER.info("Next product no for Society: {}", societyCode);
            String code = nextCodeService.getNextCode("Product", "code", societyCode, 0);
            if (code == null || code.isEmpty())
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);

            return new ResponseEntity<>(code, HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestHeader Map<String, String> headers, @RequestBody Product dto) {
        return new ResponseEntity<>(service.save(dto, CommonUtil.getIdentityHeader(headers)), HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<Product> updateProduct(@RequestHeader Map<String, String> headers, @RequestBody Product dto) {

        try {
            LOGGER.info("Product save method");
            dto = service.update(dto, CommonUtil.getIdentityHeader(headers));
            if (dto == null)
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
            return new ResponseEntity<>(dto, HttpStatus.CREATED);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{code}")
    public ResponseEntity<?> deleteProduct(@RequestHeader Map<String, String> headers,
                                           @PathVariable("code") String code) {
        service.delete(code, CommonUtil.getIdentityHeader(headers));
        return new ResponseEntity<>(null, HttpStatus.OK);
    }


    @PostMapping("/migrate")
    public ResponseEntity<List<ProductAndSaleRateDto>> migrateData(@RequestHeader Map<String, String> headers,
                                                                   @RequestBody List<ProductAndSaleRateDto> dtoList) {
        return new ResponseEntity<List<ProductAndSaleRateDto>>(
                service.migrateCollections(dtoList, CommonUtil.getIdentityHeader(headers)), HttpStatus.OK);
    }
}