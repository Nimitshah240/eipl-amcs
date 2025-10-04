package com.eipl.amcs.operation.share.bootcontroller;

import com.eipl.amcs.base.service.NextCodeService;
import com.eipl.amcs.exception.BusinessValidationFailException;
import com.eipl.amcs.master.operation.model.Member;
import com.eipl.amcs.master.operation.repository.MemberRepository;
import com.eipl.amcs.operation.share.model.Share;
import com.eipl.amcs.operation.share.service.ShareService;
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
@RequestMapping("/share")

public class ShareController {
    @Autowired
    private ShareService service;
    @Autowired
    private NextCodeService nextCodeService;
    @Autowired
    private MemberRepository memberRepository;

    private static final Logger LOGGER = LoggerFactory.getLogger(ShareController.class);


    @GetMapping("/by_member")
    public ResponseEntity<List<Share>> index(@RequestParam(name = "memCode") String memCode) {
        Member member = memberRepository.findByCode(memCode);
        return new ResponseEntity<List<Share>>(service.findByMember(member), HttpStatus.OK);
    }
    @GetMapping("/by_share_code")
    public ResponseEntity<List<Share>> indexbyShareCode(@RequestParam(name = "shareCode") String shareCode) {
        return new ResponseEntity<List<Share>>(service.findByShareCode(shareCode), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<Share>> index(@RequestParam(name = "fromDate", required = false) String fromDate,
                                             @RequestParam(name = "toDate", required = false) String toDate) {
        if (fromDate == null || toDate == null) {
            return new ResponseEntity<List<Share>>(service.findAll(), HttpStatus.OK);
        }
        LocalDate fromDt = LocalDate.parse(fromDate);
        LocalDate toDt = LocalDate.parse(toDate);
        return new ResponseEntity<List<Share>>(service.findAllData(fromDt, toDt), HttpStatus.OK);
    }

//    @GetMapping
//    public ResponseEntity<List<Share>> findAll() {
//        return new ResponseEntity<List<Share>>(service.findAll(), HttpStatus.OK);
//    }

    @GetMapping("/byDate")
    public ResponseEntity<List<Share>> findByDate(@RequestParam(name = "fromDate") String fromDate,
                                                  @RequestParam(name = "toDate") String toDate) {
        LocalDate fromDt = LocalDate.parse(fromDate);
        LocalDate toDt = LocalDate.parse(toDate);
        return new ResponseEntity<List<Share>>(service.findAllData(fromDt, toDt), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Share> createShare(@RequestHeader Map<String, String> headers,
                                             @RequestBody Share dto) throws BusinessValidationFailException {
        return new ResponseEntity<>(service.save(dto, CommonUtil.getIdentityHeader(headers)), HttpStatus.CREATED);
    }

    @PutMapping()
    public ResponseEntity<Share> update(@RequestHeader Map<String, String> headers,
                                        @RequestBody Share share) {
        return new ResponseEntity<Share>(service.update(share, CommonUtil.getIdentityHeader(headers)),
                HttpStatus.OK);
    }

    @DeleteMapping("/{code}")
    public ResponseEntity<?> cancelShare(@RequestHeader Map<String, String> headers, @PathVariable("code") String code) {
        try {
            LOGGER.info("Share delete method");
            service.cancel(code, CommonUtil.getIdentityHeader(headers));
            return new ResponseEntity<>(null, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/permanent/{code}")
    public ResponseEntity<?> permanentDeleteShare(@RequestHeader Map<String, String> headers, @PathVariable("code") String code) {
        try {
            LOGGER.info("Share delete method");
            service.delete(code, CommonUtil.getIdentityHeader(headers));
            return new ResponseEntity<>(null, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/next-code")
    public ResponseEntity<String> nextCode(@RequestParam String code) {
        try {
            LOGGER.info("Next code for Share: {}", code);
            String codes = nextCodeService.getNextCode("Share", "code", code, 5);
            if (codes == null || codes.isEmpty())
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);

            return new ResponseEntity<>(codes, HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/revert/{code}")
    public ResponseEntity<?> revertShare(@RequestHeader Map<String, String> headers, @PathVariable("code") String code) {
        try {
            LOGGER.info("Share delete method");
            service.revert(code, CommonUtil.getIdentityHeader(headers));
            return new ResponseEntity<>(null, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @DeleteMapping("/transfer_revert/{code}")
    public ResponseEntity<?> transferRevertShare(@RequestHeader Map<String, String> headers, @PathVariable("code") String code) {
        try {
            LOGGER.info("Share delete method");
            service.transferRevert(code, CommonUtil.getIdentityHeader(headers));
            return new ResponseEntity<>(null, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
