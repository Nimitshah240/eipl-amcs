package com.eipl.amcs.operation.inventory.bootcontroller;

import com.eipl.amcs.base.service.NextCodeService;
import com.eipl.amcs.exception.BusinessValidationFailException;
import com.eipl.amcs.operation.inventory.dto.ProductReceiptDto;
import com.eipl.amcs.operation.inventory.model.ProductReceipt;
import com.eipl.amcs.operation.inventory.service.ProductReceiptService;
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
@RequestMapping("/product-receipt")
public class ProductReceiptController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductReceiptController.class);
    @Autowired
    private ProductReceiptService service;
    @Autowired
    private NextCodeService nextCodeService;

    @GetMapping
    public ResponseEntity<List<ProductReceipt>> index(@RequestParam(name = "fromDate") String fromDate,
                                                      @RequestParam(name = "toDate") String toDate) {
        LocalDate fromDt = LocalDate.parse(fromDate);
        LocalDate toDt = LocalDate.parse(toDate);
        return new ResponseEntity<List<ProductReceipt>>(service.findAll(fromDt, toDt), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ProductReceiptDto> createProductReceipt(@RequestHeader Map<String, String> headers, @RequestBody ProductReceiptDto dto)
            throws BusinessValidationFailException {
        return new ResponseEntity<>(service.save(dto, CommonUtil.getIdentityHeader(headers)), HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<ProductReceiptDto> updateProductReceipt(@RequestHeader Map<String, String> headers, @RequestBody ProductReceiptDto dto)
            throws BusinessValidationFailException {
        return new ResponseEntity<>(service.update(dto, CommonUtil.getIdentityHeader(headers)), HttpStatus.CREATED);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteProductSale(@RequestHeader Map<String, String> headers, @RequestParam String grnNo) {
        service.delete(grnNo, CommonUtil.getIdentityHeader(headers));
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @GetMapping("/fetchGrnNo")
    public ResponseEntity<String> getGrnNoNextCode(@RequestParam String code) {
        try {
            LOGGER.info("Next grn no for Society: {}", code);
            String codes = nextCodeService.getNextCode("ProductReceipt", "grnNo", code, 0);
            if (codes == null || codes.isEmpty())
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);

            return new ResponseEntity<>(codes, HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
