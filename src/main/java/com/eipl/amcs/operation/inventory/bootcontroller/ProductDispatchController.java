package com.eipl.amcs.operation.inventory.bootcontroller;

import com.eipl.amcs.exception.BusinessValidationFailException;
import com.eipl.amcs.operation.inventory.bootdto.ProductDispatchDto;
import com.eipl.amcs.operation.inventory.model.ProductDispatch;
import com.eipl.amcs.operation.inventory.model.ProductDispatchTransaction;
import com.eipl.amcs.operation.inventory.service.ProductDispatchService;
import com.eipl.amcs.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@RestController
@RequestMapping("/product-dispatch")
public class ProductDispatchController {

    @Autowired
    private ProductDispatchService service;

    //	@GetMapping
//	List<ProductDispatch> findAll() {
//		return service.findAll();
//	}
    @GetMapping
    public ResponseEntity<List<ProductDispatchTransaction>> index(@RequestParam(name = "fromDate") String fromDate,
                                                                  @RequestParam(name = "toDate") String toDate) {
        LocalDate fromDt = LocalDate.parse(fromDate);
        LocalDate toDt = LocalDate.parse(toDate);
        return new ResponseEntity<List<ProductDispatchTransaction>>(service.findByDispatchDate(fromDt, toDt), HttpStatus.OK);
    }

    @GetMapping("/{challanNo}")
    Optional<ProductDispatch> findByID(@PathVariable String challanNo) {
        return service.findById(challanNo);
    }

//		@PutMapping("/{challanNo}")
//	ProductDispatch update(@PathVariable String challanNo,@RequestBody ProductDispatch productDispatch){
//		productDispatch.setChallanNo(challanNo);
//		return service.save(productDispatch);
//	}

    @PutMapping
    public ResponseEntity<ProductDispatchDto> updateProductReceipt(@RequestHeader Map<String, String> headers, @RequestBody ProductDispatchDto dto)
            throws BusinessValidationFailException {
        return new ResponseEntity<>(service.update(dto, CommonUtil.getIdentityHeader(headers)), HttpStatus.CREATED);
    }

    @PostMapping
    public ResponseEntity<ProductDispatchDto> createProductReceipt(@RequestHeader Map<String, String> headers, @RequestBody ProductDispatchDto dto)
            throws BusinessValidationFailException {
        return new ResponseEntity<>(service.save(dto, CommonUtil.getIdentityHeader(headers)), HttpStatus.CREATED);
    }

    @PostMapping("/manualDispatch")
    public ResponseEntity<ProductDispatch> createProductDispatchManual(@RequestHeader Map<String, String> headers, @RequestBody ProductDispatch dto)
            throws BusinessValidationFailException {
        return new ResponseEntity<>(service.save(dto, CommonUtil.getIdentityHeader(headers)), HttpStatus.CREATED);
    }

    @PostMapping("/manualDispatchTransaction")
    public ResponseEntity<ProductDispatchTransaction> createProductDispatchTransactionManual(@RequestHeader Map<String, String> headers, @RequestBody ProductDispatchTransaction dto)
            throws BusinessValidationFailException {
        return new ResponseEntity<>(service.save(dto, CommonUtil.getIdentityHeader(headers)), HttpStatus.CREATED);
    }

    @DeleteMapping("/{challanNo}")
    void delete(@PathVariable String challanNo) {
        service.delete(challanNo);
    }

}
