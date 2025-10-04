package com.eipl.amcs.master.account.bootcontroller;

import com.eipl.amcs.base.service.NextCodeService;
import com.eipl.amcs.master.account.dto.VoucherDto;
import com.eipl.amcs.master.account.model.Voucher;
import com.eipl.amcs.master.account.model.VoucherSubLedger;
import com.eipl.amcs.master.account.model.VoucherTransaction;
import com.eipl.amcs.master.account.repository.VoucherRepository;
import com.eipl.amcs.master.account.repository.VoucherTransactionRepository;
import com.eipl.amcs.master.account.service.VoucherService;
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
@RequestMapping("/voucher")
public class VoucherController {

    @Autowired
    private VoucherService service;

    @Autowired
    private NextCodeService nextCodeService;
    @Autowired
    private VoucherRepository repository;
    @Autowired
    private VoucherTransactionRepository transactionRepository;

    private static final Logger LOGGER = LoggerFactory.getLogger(VoucherController.class);

    @GetMapping
    public ResponseEntity<List<VoucherDto>> index() {
        try {
            List<VoucherDto> list = service.findAll();
            if (list == null || list.isEmpty())
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
            return new ResponseEntity<List<VoucherDto>>(list, HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/trans")
    public ResponseEntity<List<VoucherTransaction>> indexTransaction(@RequestParam String code) {
        try {
            Optional<Voucher> voucher = repository.findById(code);
            if (voucher.isPresent()) {
                List<VoucherTransaction> list = service.findAllTransaction(voucher.get());
                if (list == null || list.isEmpty())
                    return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
                return new ResponseEntity<List<VoucherTransaction>>(list, HttpStatus.OK);
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
		return null;
	}

    @GetMapping("/voucher-sub-ledger")
    public ResponseEntity<List<VoucherSubLedger>> indexVoucherSubLedger(@RequestParam String code) {
        try {
            VoucherTransaction voucherTransaction = transactionRepository.findById(code).get();
            List<VoucherSubLedger> list = service.findAllVoucherSubLedger(voucherTransaction);
            if (list == null || list.isEmpty())
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
            return new ResponseEntity<List<VoucherSubLedger>>(list, HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping
    public ResponseEntity<Voucher> createVoucher(@RequestHeader Map<String, String> headers, @RequestBody VoucherDto dto) {
        return new ResponseEntity<>(service.save(dto, CommonUtil.getIdentityHeader(headers)), HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<Voucher> updateVoucher(@RequestHeader Map<String, String> headers, @RequestBody VoucherDto dto) {
        return new ResponseEntity<>(service.update(dto, CommonUtil.getIdentityHeader(headers)), HttpStatus.CREATED);
    }


    @DeleteMapping
    public ResponseEntity<?> deleteVoucher(@RequestHeader Map<String, String> headers,
                                           @RequestParam("code") String code) {
        Optional<Voucher> voucher = repository.findById(code);
        service.delete(voucher.get(), CommonUtil.getIdentityHeader(headers));
        return new ResponseEntity<>(null, HttpStatus.OK);
    }


    @GetMapping("/next-code")
    public ResponseEntity<String> nextCode(@RequestHeader Map<String, String> headers,@RequestParam String code) {
        try {
            LOGGER.info("Next Voucher no for Voucher: {}", code);
            String codeI = nextCodeService.getNextCode("Voucher", "code", code, 6);
            if (codeI == null || codeI.isEmpty())
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);

            return new ResponseEntity<>(codeI, HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}

