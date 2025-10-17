package com.eipl.amcs.master.operation.bootcontroller;

import com.eipl.amcs.base.service.NextCodeService;
import com.eipl.amcs.exception.BusinessValidationFailException;
import com.eipl.amcs.master.operation.model.Customer;
import com.eipl.amcs.master.operation.model.CustomerDetails;
import com.eipl.amcs.master.operation.model.CustomerDto;
import com.eipl.amcs.master.operation.service.CustomerService;
import com.eipl.amcs.util.CommonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerController.class);
    @Autowired
    private CustomerService service;
    @Autowired
    private NextCodeService nextCodeService;

    @GetMapping
    public ResponseEntity<List<Customer>> index(@RequestParam(name = "society", required = false) String societyCode) {
        try {
            List<Customer> list = null;

            if (societyCode == null || societyCode.isEmpty())
                list = service.findAll();
            else
                list = service.findAllBySociety(societyCode);
            if (list == null || list.isEmpty())
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
            return new ResponseEntity<List<Customer>>(list, HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

//	@GetMapping("/{code}")
//	public ResponseEntity<Customer> findByCustomerCode(@PathVariable("code") String code) {
//		System.out.println(service.findByCustomerCode(code));
//		System.out.println("a");
//		return new ResponseEntity<>(service.findNameByCustomerCode(code), HttpStatus.OK);
//	}

    @GetMapping("/{code}")
    public ResponseEntity<Customer> findByCustomerCode(@PathVariable("code") String code) {
        return new ResponseEntity<>(service.findByCustomerCode(code), HttpStatus.OK);
    }

    @GetMapping("/next-code")
    public ResponseEntity<String> nextCode(@RequestParam(name = "society", required = true) String societyCode) {
        try {
            LOGGER.info("Next customer no for Society: {}", societyCode);
            String code = nextCodeService.getNextCode("Customer", "code", societyCode, 4);
            if (code == null || code.isEmpty())
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);

            return new ResponseEntity<>(code, HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/customer-details/{code}")
    public ResponseEntity<CustomerDetails> fetchCustomerDetail(
            @PathVariable(name = "code", required = true) String code) {
        Customer customer = service.findByCustomerCode(code);

        return new ResponseEntity<CustomerDetails>(service.findDetailByCustomer(customer), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<CustomerDto> createCustomer(@RequestHeader Map<String, String> headers,
                                                      @RequestBody CustomerDto dto) throws BusinessValidationFailException {
        CustomerDto dtoNew = service.save(dto, CommonUtil.getIdentityHeader(headers));

        dtoNew.getCustomer().setSociety(dto.getCustomer().getSociety());
        dtoNew.getCustomer().setUnion(dto.getCustomer().getUnion());
        dtoNew.getCustomerDetail().setBank(dto.getCustomerDetail().getBank());
        dtoNew.getCustomerDetail().setBranch(dto.getCustomerDetail().getBranch());
        dtoNew.getCustomerDetail().setState(dto.getCustomerDetail().getState());
        dtoNew.getCustomerDetail().setDistrict(dto.getCustomerDetail().getDistrict());
        dtoNew.getCustomerDetail().setSubDistrict(dto.getCustomerDetail().getSubDistrict());
        dtoNew.getCustomerDetail().setVillage(dto.getCustomerDetail().getVillage());
        dtoNew.getCustomerDetail().setCustomer(dto.getCustomerDetail().getCustomer());

        return new ResponseEntity<>(dtoNew, HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<CustomerDto> updateCustomer(@RequestHeader Map<String, String> headers,
                                                      @RequestBody CustomerDto dto) {
        try {
            CustomerDto dtoNew = service.update(dto, CommonUtil.getIdentityHeader(headers));

            dtoNew.getCustomer().setSociety(dto.getCustomer().getSociety());
            dtoNew.getCustomer().setUnion(dto.getCustomer().getUnion());

            dtoNew.getCustomerDetail().setBank(dto.getCustomerDetail().getBank());
            dtoNew.getCustomerDetail().setBranch(dto.getCustomerDetail().getBranch());
            dtoNew.getCustomerDetail().setState(dto.getCustomerDetail().getState());
            dtoNew.getCustomerDetail().setDistrict(dto.getCustomerDetail().getDistrict());
            dtoNew.getCustomerDetail().setSubDistrict(dto.getCustomerDetail().getSubDistrict());
            dtoNew.getCustomerDetail().setVillage(dto.getCustomerDetail().getVillage());
            dtoNew.getCustomerDetail().setCustomer(dto.getCustomerDetail().getCustomer());

            return new ResponseEntity<>(dtoNew, HttpStatus.CREATED);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{code}")
    public ResponseEntity<?> deleteCustomer(@RequestHeader Map<String, String> headers,
                                            @PathVariable("code") String code) {
        try {
            Optional<Customer> customerData = service.findById(code);
            if (customerData == null || !customerData.isPresent())
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);

            service.delete(customerData.get().getCode(), CommonUtil.getIdentityHeader(headers));
            return new ResponseEntity<>(null, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
