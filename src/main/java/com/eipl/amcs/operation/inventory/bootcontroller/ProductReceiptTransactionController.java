package com.eipl.amcs.operation.inventory.bootcontroller;

import com.eipl.amcs.base.service.NextCodeService;
import com.eipl.amcs.operation.inventory.dto.ReceiptTxnTaxDto;
import com.eipl.amcs.operation.inventory.model.ProductReceiptTransaction;
import com.eipl.amcs.operation.inventory.service.ProductReceiptTransactionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/product-receipt-transactions")
public class ProductReceiptTransactionController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductReceiptTransactionController.class);
    @Autowired
    private ProductReceiptTransactionService service;
    @Autowired
    private NextCodeService nextCodeService;

    @GetMapping
    public ResponseEntity<List<ProductReceiptTransaction>> index() {
        try {
            List<ProductReceiptTransaction> list = service.findAll();
            if (list == null || list.isEmpty())
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);

            return new ResponseEntity<List<ProductReceiptTransaction>>(list, HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/ByGrnNo")
    public ResponseEntity<List<ReceiptTxnTaxDto>> indexByProductReceipt(@RequestParam String code) {
        try {
            List<ReceiptTxnTaxDto> list = service.findByProductReceipt(code);
            if (list == null || list.isEmpty())
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);

            return new ResponseEntity<List<ReceiptTxnTaxDto>>(list, HttpStatus.OK);
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

    @PostMapping
    public ResponseEntity<ProductReceiptTransaction> createProductReceiptTransaction(@RequestBody ProductReceiptTransaction dto) {
        try {
            LOGGER.info("ProductReceiptTransaction save method");
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
    public ResponseEntity<ProductReceiptTransaction> updateProductReceiptTransaction(@RequestBody ProductReceiptTransaction dto) {
        try {
            LOGGER.info("ProductReceiptTransaction save method");
            dto = service.update(dto);
            if (dto == null)
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);

            return new ResponseEntity<>(dto, HttpStatus.CREATED);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{receiptTransactionNo}")
    public ResponseEntity<?> deleteProductReceiptTransaction(@PathVariable("receiptTransactionNo") String receiptTransactionNo) {
        try {
            LOGGER.info("ProductReceiptTransaction delete method");
            Optional<ProductReceiptTransaction> productReceiptTransactionData = service.findById(receiptTransactionNo);
            if (productReceiptTransactionData == null || !productReceiptTransactionData.isPresent())
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);

            service.delete(productReceiptTransactionData.get());
            return new ResponseEntity<>(null, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
