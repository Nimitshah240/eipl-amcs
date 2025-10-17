package com.eipl.amcs.operation.inventory.bootcontroller;


import com.eipl.amcs.base.service.NextCodeService;
import com.eipl.amcs.operation.inventory.dto.ProductRequisitionDto;
import com.eipl.amcs.operation.inventory.model.ProductRequisitionTransaction;
import com.eipl.amcs.operation.inventory.service.ProductRequisitionTransactionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/product-requisition-transaction")
public class ProductRequisitionTransactionController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductReceiptTransactionController.class);
    @Autowired
    private ProductRequisitionTransactionService service;
    @Autowired
    private NextCodeService nextCodeService;

    @GetMapping
    List<ProductRequisitionTransaction> findAll() {
        return service.findAll();
    }

    @GetMapping("/{code}")
    Optional<ProductRequisitionTransaction> findByID(@PathVariable String code) {
        return service.findById(code);
    }

    @PostMapping("")
    ProductRequisitionTransaction save(@RequestBody ProductRequisitionTransaction productRequisitionTransaction) {
        return service.save(productRequisitionTransaction);
    }


    @PutMapping("/{code}")
    ProductRequisitionTransaction update(@PathVariable String code, @RequestBody ProductRequisitionTransaction productRequisitionTransaction) {
        productRequisitionTransaction.setCode(code);
        return service.save(productRequisitionTransaction);
    }

    @DeleteMapping("/{code}")
    void delete(@PathVariable String code) {
        service.delete(code);
    }

    @GetMapping("/ByGrnNo")
    public ResponseEntity<ProductRequisitionDto> indexByProductReceipt(@RequestParam String code) {
        try {
            ProductRequisitionDto list = service.findByProductReceipt(code);
            if (list == null)
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
            return new ResponseEntity<ProductRequisitionDto>(list, HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/next-receiptTransactionNo")
    public ResponseEntity<String> nextCode(@RequestParam(name = "society", required = true) String societyCode) {
        try {
            LOGGER.info("Next productReceiptTransaction no for Society: {}", societyCode);
            String receiptTransactionNo = nextCodeService.getNextCode("ProductReceiptTransaction", "grnTxnNo", societyCode, 2);
            if (receiptTransactionNo == null || receiptTransactionNo.isEmpty())
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);

            return new ResponseEntity<>(receiptTransactionNo, HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
