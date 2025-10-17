package com.eipl.amcs.master.procurement.bootcontroller;

import com.eipl.amcs.exception.BusinessValidationFailException;
import com.eipl.amcs.master.procurement.model.LocalMilkSaleRate;
import com.eipl.amcs.master.procurement.service.LocalMilkSaleRateService;
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
@RequestMapping("/local-milk-sale-rates")
public class LocalMilkSaleRateController {

    private static final Logger LOGGER = LoggerFactory.getLogger(LocalMilkSaleRateController.class);
    @Autowired
    private LocalMilkSaleRateService service;

    @GetMapping
    public ResponseEntity<List<LocalMilkSaleRate>> index() {
        try {
            List<LocalMilkSaleRate> list = service.findAll();
            if (list == null || list.isEmpty())
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);

            return new ResponseEntity<List<LocalMilkSaleRate>>(list, HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping
    public ResponseEntity<LocalMilkSaleRate> saveLocalMilkSaleRate(@RequestHeader Map<String, String> headers,
                                                                   @RequestBody LocalMilkSaleRate localMilkSaleRate) throws BusinessValidationFailException {
//		try {
//		} catch (Exception e) {
//			LOGGER.error(e.getMessage());
//			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
//		}
        return new ResponseEntity<LocalMilkSaleRate>(service.save(localMilkSaleRate, CommonUtil.getIdentityHeader(headers)), HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<LocalMilkSaleRate> updateLocalMilkSaleRateWithMilkType(@RequestHeader Map<String, String> headers, @RequestBody LocalMilkSaleRate dto) {
        try {
            LOGGER.info("LocalMilkSaleRate save method");
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
    public ResponseEntity<?> deleteLocalMilkSaleRate(@RequestHeader Map<String, String> headers, @PathVariable("code") String code) {
        try {
            LOGGER.info("LocalMilkSaleRate delete method");
            Optional<LocalMilkSaleRate> localMilkSaleRateData = service.findById(code);
            if (localMilkSaleRateData == null || !localMilkSaleRateData.isPresent())
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);

            service.delete(localMilkSaleRateData.get(), CommonUtil.getIdentityHeader(headers));
            return new ResponseEntity<>(null, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/rate")
    public ResponseEntity<LocalMilkSaleRate> fetchRate(@RequestParam(name = "date") String date,
                                                       @RequestParam(name = "milkType") Integer milkType, @RequestParam(name = "milkClass") Integer milkClass) {
        try {
            LocalDate dt = LocalDate.parse(date);
            return new ResponseEntity<LocalMilkSaleRate>(service.fetchRate(dt, milkType, milkClass), HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
