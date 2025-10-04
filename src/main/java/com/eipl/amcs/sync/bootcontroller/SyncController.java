package com.eipl.amcs.sync.bootcontroller;

import com.eipl.amcs.master.geo.model.*;
import com.eipl.amcs.master.global.model.MemberType;
import com.eipl.amcs.master.global.model.MilkType;
import com.eipl.amcs.master.global.model.Shift;
import com.eipl.amcs.master.operation.model.Member;
import com.eipl.amcs.master.operation.model.MemberDetail;
import com.eipl.amcs.master.operation.repository.MemberDetailRepository;
import com.eipl.amcs.master.operation.repository.MemberRepository;
import com.eipl.amcs.master.org.model.Bank;
import com.eipl.amcs.master.org.model.Branch;
import com.eipl.amcs.master.org.model.Society;
import com.eipl.amcs.master.procurement.model.SocietyPaymentCycle;
import com.eipl.amcs.master.procurement.repository.SocietyPaymentCycleRepository;
import com.eipl.amcs.operation.procurement.model.MilkDispatch;
import com.eipl.amcs.operation.procurement.model.MilkDispatchTransaction;
import com.eipl.amcs.operation.procurement.repository.MilkDispatchRepository;
import com.eipl.amcs.operation.procurement.repository.MilkDispatchTransactionRepository;
import com.eipl.amcs.operation.procurement.service.MilkCollectionService;
import com.eipl.amcs.sync.model.Subscribed;
import com.eipl.amcs.sync.repository.BroadcastedRepository;
import com.eipl.amcs.sync.repository.SubscribedRepository;
import com.eipl.amcs.utils.AppConstant;
import com.eipl.amcs.util.CommonUtil;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/sync")
public class SyncController {
    @Autowired
    private BroadcastedRepository repository;
    @Autowired
    private SubscribedRepository subscribedRepository;

    @Autowired
    private MilkCollectionService milkCollectionRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private MemberDetailRepository memberDetailRepository;
    @Autowired
    private SocietyPaymentCycleRepository paymentCycleRepository;
    @Autowired
    private MilkDispatchRepository dispatchRepository;
    @Autowired
    private MilkDispatchTransactionRepository milkDispatchTransactionRepository;

    @GetMapping("/pending-sync")
    public ResponseEntity<Long> fetchPendingSyncCount() {
        return new ResponseEntity<Long>(repository.count(), HttpStatus.OK);
    }


    @PostMapping("/resync")
    public ResponseEntity<List<Map<String, Object>>> reSync(@RequestBody List<Map<String, Object>> list, @RequestHeader Map<String, String> headers) {
        for (Map<String, Object> syncResponse : list) {
            switch ((String) syncResponse.get("tableName")) {
                case "tbl_milk_collection":
                    milkCollectionRepository.findAllCollectionByDate(LocalDateTime.parse((String) syncResponse.get("fromDatetime"), AppConstant.SYNC_DATE_TIME_FMT), LocalDateTime.parse((String) syncResponse.get("toDatetime"), AppConstant.SYNC_DATE_TIME_FMT), CommonUtil.getIdentityHeader(headers));
                    break;
                case "tbl_member":
                    for (Member member : memberRepository.findAll()) {
                        member.setSociety(Hibernate.unproxy(member.getSociety(), Society.class));
                        member.setMemberType(Hibernate.unproxy(member.getMemberType(), MemberType.class));
                        member.setMilkType(Hibernate.unproxy(member.getMilkType(), MilkType.class));
                        memberRepository.customSaveForSync(member, CommonUtil.getIdentityHeader(headers));
                    }
                    for (MemberDetail detail : memberDetailRepository.findAll()) {
                        detail.setState(Hibernate.unproxy(detail.getState(), State.class));
                        detail.setDistrict(Hibernate.unproxy(detail.getDistrict(), District.class));
                        detail.setSubDistrict(Hibernate.unproxy(detail.getSubDistrict(), SubDistrict.class));
                        detail.setVillage(Hibernate.unproxy(detail.getVillage(), Village.class));
                        detail.setHamlet(Hibernate.unproxy(detail.getHamlet(), Hamlet.class));
                        detail.setBank(Hibernate.unproxy(detail.getBank(), Bank.class));
                        detail.setBranch(Hibernate.unproxy(detail.getBranch(), Branch.class));
                        memberDetailRepository.customSaveForSync(detail, CommonUtil.getIdentityHeader(headers));
                    }
                    break;
                case "tbl_dcs_payment_cycle":
                    for (SocietyPaymentCycle societyPaymentCycle : paymentCycleRepository.findAll()) {
                        societyPaymentCycle.setSociety(Hibernate.unproxy(societyPaymentCycle.getSociety(), Society.class));
                        societyPaymentCycle.setFromShift(Hibernate.unproxy(societyPaymentCycle.getFromShift(), Shift.class));
                        societyPaymentCycle.setToShift(Hibernate.unproxy(societyPaymentCycle.getToShift(), Shift.class));
                        paymentCycleRepository.customSaveForSync(societyPaymentCycle, CommonUtil.getIdentityHeader(headers));
                    }
                    break;
                case "tbl_milk_dispatch":
                    for (MilkDispatch md : dispatchRepository.findByFromDateGreaterThanEqualAndToDateLessThanEqual(LocalDateTime.parse((String) syncResponse.get("fromDatetime"), AppConstant.SYNC_DATE_TIME_FMT), LocalDateTime.parse((String) syncResponse.get("toDatetime"), AppConstant.SYNC_DATE_TIME_FMT))) {
                        dispatchRepository.customSaveForSync(md, CommonUtil.getIdentityHeader(headers));
                        for (MilkDispatchTransaction dispatchTransaction : milkDispatchTransactionRepository.findByMilkDispatch(md)) {
                            milkDispatchTransactionRepository.customSaveForSync(dispatchTransaction, CommonUtil.getIdentityHeader(headers));
                        }
                    }
                    break;
                default:
                    break;
            }
        }
        return null;
    }

    @PostMapping("/sentbox")
    public ResponseEntity<List<Subscribed>> saveSubscribed(@RequestBody List<Subscribed> subscribedList) {
        save(subscribedList);
        return null;
    }

    private void save(List<Subscribed> subscribedList) {
        for (Subscribed subscribed : subscribedList) {
            subscribedRepository.save(subscribed);
        }
    }
}
