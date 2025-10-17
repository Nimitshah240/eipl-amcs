package com.eipl.amcs.operation.inventory.bootcontroller;

import com.eipl.amcs.operation.inventory.dto.SaleTxnTaxDto;
import com.eipl.amcs.operation.inventory.model.ProductSaleTransaction;
import com.eipl.amcs.operation.inventory.service.ProductSaleTransactionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/product-sale-transactions")
public class ProductSaleTransactionController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductSaleTransactionController.class);
    @Autowired
    private ProductSaleTransactionService service;

    @GetMapping
    public ResponseEntity<List<ProductSaleTransaction>> index() {
        try {
            List<ProductSaleTransaction> list = service.findAll();
            if (list == null || list.isEmpty())
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);

            return new ResponseEntity<List<ProductSaleTransaction>>(list, HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/ByInvoiceNo")
    public ResponseEntity<List<SaleTxnTaxDto>> indexByProductSale(@RequestParam String code) {
        try {
            List<SaleTxnTaxDto> list = service.findByProductSale(code);
            if (list == null || list.isEmpty())
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);

            return new ResponseEntity<List<SaleTxnTaxDto>>(list, HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping
    public ResponseEntity<ProductSaleTransaction> createMember(@RequestBody ProductSaleTransaction dto) {
        try {
            LOGGER.info("ProductSaleToMemberTransaction save method");
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
    public ResponseEntity<ProductSaleTransaction> updateMember(@RequestBody ProductSaleTransaction dto) {
        try {
            LOGGER.info("ProductSaleToMemberTransaction save method");
            dto = service.update(dto);
            if (dto == null)
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);

            return new ResponseEntity<>(dto, HttpStatus.CREATED);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{invoiceTransactionNo}")
    public ResponseEntity<?> deleteMember(@PathVariable("invoiceTransactionNo") String invoiceTransactionNo) {
        try {
            LOGGER.info("ProductSaleToMemberTransaction delete method");
            Optional<ProductSaleTransaction> memberData = service.findById(invoiceTransactionNo);
            if (memberData == null || !memberData.isPresent())
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);

            service.delete(memberData.get());
            return new ResponseEntity<>(null, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
