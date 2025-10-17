package com.eipl.amcs.operation.inventory.bootcontroller;

import com.eipl.amcs.operation.inventory.model.ProductSaleInstallment;
import com.eipl.amcs.operation.inventory.service.ProductSaleInstallmentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/product-sale-to-member-installments")
public class ProductSaleInstallmentController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductSaleInstallmentController.class);
    @Autowired
    private ProductSaleInstallmentService service;

    @GetMapping
    public ResponseEntity<List<ProductSaleInstallment>> index() {
        try {
            List<ProductSaleInstallment> list = service.findAll();
            if (list == null || list.isEmpty())
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);

            return new ResponseEntity<List<ProductSaleInstallment>>(list, HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{invoiceNo}/{isBilling}")
    public ResponseEntity<List<ProductSaleInstallment>> indexByInstallmentNo(
            @PathVariable("invoiceNo") String invoiceNo, @PathVariable("isBilling") boolean b) {
        try {
            List<ProductSaleInstallment> list = service.fetchInstallmentIsBilled(invoiceNo, b);
            if (list == null || list.isEmpty())
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);

            return new ResponseEntity<List<ProductSaleInstallment>>(list, HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/invoiceNo")
    public ResponseEntity<List<ProductSaleInstallment>> indexByInstallmentNoAndPaymentCycle(
            @RequestParam("invoiceNo") String invoiceNo) {
        try {
            List<ProductSaleInstallment> list = service.fetchByPaymentCycle(invoiceNo);
            if (list == null || list.isEmpty())
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);

            return new ResponseEntity<List<ProductSaleInstallment>>(list, HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PostMapping
    public ResponseEntity<ProductSaleInstallment> createInstallment(@RequestBody ProductSaleInstallment dto) {
        try {
            LOGGER.info("ProductSaleToMemberInstallment save method");
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
    public ResponseEntity<ProductSaleInstallment> updateInstallment(@RequestBody ProductSaleInstallment dto) {
        try {
            LOGGER.info("ProductSaleToMemberInstallment save method");
            dto = service.update(dto);
            if (dto == null)
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);

            return new ResponseEntity<>(dto, HttpStatus.CREATED);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{installmentNo}")
    public ResponseEntity<?> deleteInstallment(@PathVariable("installmentNo") String installmentNo) {
        try {
            LOGGER.info("ProductSaleToMemberInstallment delete method");
            Optional<ProductSaleInstallment> memberData = service.findById(installmentNo);
            if (memberData == null || !memberData.isPresent())
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);

            service.delete(memberData.get());
            return new ResponseEntity<>(null, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
