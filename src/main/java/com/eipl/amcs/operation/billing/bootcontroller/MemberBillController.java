package com.eipl.amcs.operation.billing.bootcontroller;

import com.eipl.amcs.exception.EntityNotFoundException;
import com.eipl.amcs.master.procurement.model.SocietyPaymentCycle;
import com.eipl.amcs.master.procurement.service.SocietyPaymentCycleService;
import com.eipl.amcs.operation.billing.dto.FinalizeDto;
import com.eipl.amcs.operation.billing.model.MemberBill;
import com.eipl.amcs.operation.billing.model.MemberBillSummary;
import com.eipl.amcs.operation.billing.model.MemberBillTransaction;
import com.eipl.amcs.operation.billing.repository.MemberBillRepository;
import com.eipl.amcs.operation.billing.service.MemberBillService;
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
@RequestMapping("/member-bill")
public class MemberBillController {
    private static final Logger LOGGER = LoggerFactory.getLogger(MemberBillController.class);
    @Autowired
    private MemberBillService service;
    @Autowired
    private SocietyPaymentCycleService paymentCycleService;
    @Autowired
    private MemberBillRepository repository;

    @GetMapping("/summary")
    public ResponseEntity<List<MemberBillSummary>> fetchBillSummary(@RequestParam("fromDate") String fromDate,
                                                                    @RequestParam("toDate") String toDate) {
        LocalDate fromDt = LocalDate.parse(fromDate);
        LocalDate toDt = LocalDate.parse(toDate);
        return new ResponseEntity<List<MemberBillSummary>>(service.findMemberBillSummaryBetWeen(fromDt, toDt),
                HttpStatus.OK);
    }

    @GetMapping("/findByDate")
    public ResponseEntity<List<MemberBillSummary>> index(@RequestParam(name = "fromDate") String fromDate,
                                                         @RequestParam(name = "toDate") String toDate) {
        try {
            LocalDate fromDt = LocalDate.parse(fromDate);
            LocalDate toDt = LocalDate.parse(toDate);
//            LocalDateTime fromDt = LocalDateTime.of(LocalDate.parse(fromDate), LocalTime.MIN);
//            LocalDateTime toDt = LocalDateTime.of(LocalDate.parse(toDate), LocalTime.MAX);
            return new ResponseEntity<>(service.findMemberBillSummaryBetWeenFromDateAndToDate(fromDt, toDt), HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/transaction")
    public ResponseEntity<List<MemberBillTransaction>> fetchBillTransaction(@RequestParam("code") String code) {
        MemberBill mb = repository.findById(code).get();
        return new ResponseEntity<List<MemberBillTransaction>>(service.findMemberBillTransaction(mb), HttpStatus.OK);
    }

    @GetMapping("/bill")
    public ResponseEntity<List<MemberBill>> fetchBill(@RequestParam("paymentCycleCode") String paymentCycleCode,
                                                      @RequestParam("societyCode") String societyCode, @RequestParam("fromDate") String fromDate,
                                                      @RequestParam("toDate") String toDate, @RequestParam("generate") short generate) {
        SocietyPaymentCycle paymentCycle = paymentCycleService.findById(paymentCycleCode)
                .orElseThrow(() -> new EntityNotFoundException(SocietyPaymentCycle.class, "code", "invalid.paymentcycle"));
        SocietyPaymentCycle prevPaymentCycle = paymentCycleService.fetchCurrentPaymentCycle(paymentCycle.getFromDate().minusDays(3), null);
        if (generate == (short) 1) {
            return new ResponseEntity<>(service.findMemberBill(societyCode, paymentCycle, prevPaymentCycle),
                    HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @GetMapping("/checkBill")
    public ResponseEntity<MemberBillSummary> checkBill(@RequestParam("paymentCycleCode") String paymentCycleCode) {

        SocietyPaymentCycle paymentCycle = paymentCycleService.findById(paymentCycleCode)
                .orElseThrow(() -> new EntityNotFoundException(SocietyPaymentCycle.class, "invalid.paymentcycle"));
        return new ResponseEntity<MemberBillSummary>(service.checkTableData(paymentCycle), HttpStatus.OK);
    }

    @GetMapping("/fetchBill")
    public ResponseEntity<List<MemberBill>> fetChBill(@RequestParam("paymentCycleCode") String paymentCycleCode) {

        SocietyPaymentCycle paymentCycle = paymentCycleService.findById(paymentCycleCode)
                .orElseThrow(() -> new EntityNotFoundException(SocietyPaymentCycle.class, "invalid.paymentcycle"));
        return new ResponseEntity<List<MemberBill>>(service.fetchTableData(paymentCycle), HttpStatus.OK);
    }

    @PostMapping("/finalize")
    public ResponseEntity<Boolean> finalize(@RequestBody FinalizeDto finalizeDto) {
        SocietyPaymentCycle paymentCycle = finalizeDto.getPaymentCycle();
        List<String> memberList = finalizeDto.getMemberCodeList();
        return new ResponseEntity<Boolean>(service.finalize(paymentCycle, memberList), HttpStatus.OK);
    }

    @PostMapping("/disburse")
    public ResponseEntity<Boolean> disburse(@RequestHeader Map<String, String> headers, @RequestBody FinalizeDto finalizeDto) {
        SocietyPaymentCycle paymentCycle = finalizeDto.getPaymentCycle();
        List<String> memberList = finalizeDto.getMemberCodeList();
        return new ResponseEntity<Boolean>(service.disburse(paymentCycle, memberList, CommonUtil.getIdentityHeader(headers)), HttpStatus.OK);
    }


    @PutMapping("/savetrans")
    public ResponseEntity<List<MemberBillTransaction>> savetrans(@RequestBody List<MemberBillTransaction> list) {
        return new ResponseEntity<>(service.saveTrans(list), HttpStatus.OK);
    }


}
