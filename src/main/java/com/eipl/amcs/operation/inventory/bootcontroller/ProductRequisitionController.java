package com.eipl.amcs.operation.inventory.bootcontroller;

import com.eipl.amcs.base.service.NextCodeService;
import com.eipl.amcs.exception.BusinessValidationFailException;
import com.eipl.amcs.operation.inventory.controller.ProductReceiptController;
import com.eipl.amcs.operation.inventory.dto.ProductRequisitionDto;
import com.eipl.amcs.operation.inventory.model.ProductRequisition;
import com.eipl.amcs.operation.inventory.model.ProductRequisitionTransaction;
import com.eipl.amcs.operation.inventory.service.ProductRequisitionService;
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
import java.util.Optional;


@RestController
@RequestMapping("/product-requisition")
public class ProductRequisitionController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProductReceiptController.class);

    @Autowired
    private ProductRequisitionService service;

    @Autowired
    private NextCodeService nextCodeService;


    @GetMapping("/{code}")
    Optional<ProductRequisition> findByID(@PathVariable String code) {
        return service.findById(code);
    }

    @PostMapping
    public ResponseEntity<ProductRequisitionDto> createProductReceipt(@RequestHeader Map<String, String> headers, @RequestBody ProductRequisitionDto dto)
            throws BusinessValidationFailException {
        return new ResponseEntity<>(service.save(dto, CommonUtil.getIdentityHeader(headers)), HttpStatus.CREATED);
    }

    @PostMapping("/manualRequisition")
    public ResponseEntity<ProductRequisition> createProductReceiptManual(@RequestHeader Map<String, String> headers, @RequestBody ProductRequisition dto)
            throws BusinessValidationFailException {
        return new ResponseEntity<>(service.save(dto, CommonUtil.getIdentityHeader(headers)), HttpStatus.CREATED);
    }

    @PostMapping("/manualRequisitionTransaction")
    public ResponseEntity<ProductRequisitionTransaction> createProductReceiptTransactionManual(@RequestHeader Map<String, String> headers, @RequestBody ProductRequisitionTransaction dto)
            throws BusinessValidationFailException {
        return new ResponseEntity<>(service.save(dto, CommonUtil.getIdentityHeader(headers)), HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<ProductRequisitionDto> updateProductReceipt(@RequestHeader Map<String, String> headers, @RequestBody ProductRequisitionDto dto)
            throws BusinessValidationFailException {
        return new ResponseEntity<>(service.update(dto, CommonUtil.getIdentityHeader(headers)), HttpStatus.CREATED);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteProductSale(@RequestHeader Map<String, String> headers, @RequestParam String grnNo) {
        service.delete(grnNo, CommonUtil.getIdentityHeader(headers));
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<ProductRequisition>> index(@RequestParam(name = "fromDate") String fromDate,
                                                          @RequestParam(name = "toDate") String toDate) {
        LocalDate fromDt = LocalDate.parse(fromDate);
        LocalDate toDt = LocalDate.parse(toDate);
        return new ResponseEntity<List<ProductRequisition>>(service.findByDate(fromDt, toDt), HttpStatus.OK);
    }

    @GetMapping("/fetchGrnNo")
    public ResponseEntity<String> getGrnNoNextCode(@RequestParam String code) {
        try {
            LOGGER.info("Next grn no for Society: {}", code);
            String codes = nextCodeService.getNextCode("ProductRequisition", "code", code, 0);
            if (codes == null || codes.isEmpty())
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);

            return new ResponseEntity<>(codes, HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
