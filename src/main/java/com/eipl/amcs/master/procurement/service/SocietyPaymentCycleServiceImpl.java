package com.eipl.amcs.master.procurement.service;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.repository.NextCodeRepository;
import com.eipl.amcs.exception.BusinessValidationFailException;
import com.eipl.amcs.exception.EntityNotFoundException;
import com.eipl.amcs.master.global.model.Shift;
import com.eipl.amcs.master.org.model.Society;
import com.eipl.amcs.master.procurement.model.SocietyPaymentCycle;
import com.eipl.amcs.master.procurement.repository.SocietyPaymentCycleRepository;
import com.eipl.amcs.utils.CommonUtils;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.FieldError;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Service
public class SocietyPaymentCycleServiceImpl implements SocietyPaymentCycleService {

    @PersistenceContext
    EntityManager entityManager;
    @Autowired
    private SocietyPaymentCycleRepository societyPaymentCycleRepository;
    @Autowired
    private NextCodeRepository nextCodeRepository;

    @Override
    public List<SocietyPaymentCycle> findAll() {
        return societyPaymentCycleRepository.findAll(Sort.by("fromDate"));
    }

    @Override
    public List<SocietyPaymentCycle> findAll(LocalDateTime fromDt, LocalDateTime toDt) {
        return societyPaymentCycleRepository.findByFromDateBetween(fromDt, toDt);
    }

    @Override
    @Transactional
    public String save(List<SocietyPaymentCycle> societyPaymentCycle, String identityInfo) throws BusinessValidationFailException {

        String nextCode = nextCodeRepository.getNextCode("SocietyPaymentCycle", "code",
                societyPaymentCycle.get(0).getSociety().getCode(), 0);
        for (SocietyPaymentCycle payment : societyPaymentCycle) {
            payment.setCode(nextCode);
            if (checkDateRangeConflict(payment.getSociety().getCode(), payment.getCode(),
                    payment.getFromDate(), payment.getToDate())) {
            } else {
                FieldError rangeNotValid = CommonUtils.getFieldError("societypaymentcycle", "paymentcyclerange",
                        payment.getCode(), "paymentcyclerange.not.valid");
                throw new BusinessValidationFailException(getClass(), rangeNotValid);
            }
            payment.setInitData();
            payment.setBilling(false);
            payment.setLockBillingProcess(false);
//            payment.setUnionCode(payment.getSociety().getUnion().getCode());
            payment.setUnionCode(MainApp.getUser().getUnionCode());
            societyPaymentCycleRepository.customSave(payment, identityInfo);
            nextCode = societyPaymentCycle.get(0).getSociety().getCode()
                    + (Integer.parseInt(nextCode.replace(societyPaymentCycle.get(0).getSociety().getCode(), "")) + 1);
        }

        return "Payment cycle saved successfully!";
    }

    @Override
    public SocietyPaymentCycle update(String code, SocietyPaymentCycle societyPaymentCycle, String identityInfo) {
        if (checkDateRangeConflict(societyPaymentCycle.getSociety().getCode(), societyPaymentCycle.getCode(),
                societyPaymentCycle.getFromDate(), societyPaymentCycle.getToDate())) {
        } else {
            FieldError rangeNotValid = CommonUtils.getFieldError("societypaymentcycle", "paymentcyclerange",
                    societyPaymentCycle.getCode(), "paymentcyclerange.not.valid");
            throw new BusinessValidationFailException(getClass(), rangeNotValid);
        }

        SocietyPaymentCycle cycle = societyPaymentCycleRepository.findById(code)
                .orElseThrow(() -> new EntityNotFoundException(SocietyPaymentCycle.class, "paymentcycle.notfound"));
        cycle.setBilling(societyPaymentCycle.getBilling());
        cycle.setFromDate(societyPaymentCycle.getFromDate());
        cycle.setFromShift(societyPaymentCycle.getFromShift());
        cycle.setToDate(societyPaymentCycle.getToDate());
        cycle.setToShift(societyPaymentCycle.getToShift());
        cycle.setLockBillingProcess(societyPaymentCycle.getLockBillingProcess());
        cycle.setIntervalValue(societyPaymentCycle.getIntervalValue());
        cycle.setupdateData();

        cycle = societyPaymentCycleRepository.customUpdate(cycle, identityInfo);
        cycle.setSociety(societyPaymentCycle.getSociety());
        cycle.setFromShift(societyPaymentCycle.getFromShift());
        cycle.setToShift(societyPaymentCycle.getToShift());

        return cycle;
    }

    @Override
    public Optional<SocietyPaymentCycle> findById(String code) {
        return societyPaymentCycleRepository.findById(code);
    }

    @Override
    public void delete(String code, String identityInfo) throws EntityNotFoundException {
        SocietyPaymentCycle paymentCycle = societyPaymentCycleRepository.findById(code)
                .orElseThrow(() -> new EntityNotFoundException(SocietyPaymentCycle.class, "paymentcycle.notfound"));
        societyPaymentCycleRepository.customDelete(paymentCycle, identityInfo);
    }

    @Override
    @Transactional
    @CacheEvict(value = {"societyPaymentCycleCache"}, allEntries = true)
    public void delete(SocietyPaymentCycle societyPaymentCycle) {
        societyPaymentCycleRepository.deleteById(societyPaymentCycle.getCode());
    }

    @Override
    public boolean checkDateRangeConflict(String str1, String str2, LocalDateTime fromDate, LocalDateTime toDate) {
        return societyPaymentCycleRepository.checkDateRangeConflict(str1, str2, fromDate, toDate).size() >= 0; // TODO NEDD To confirm
    }

    @Override
    public SocietyPaymentCycle fetchCurrentPaymentCycle(LocalDateTime date, String code) {
        return societyPaymentCycleRepository.fetchCurrentPaymentCycle(date, code);
    }

    @Override
    public List<SocietyPaymentCycle> findByToDateGreaterThanEqualOrderByToDate(LocalDateTime date, int limit) {
        SocietyPaymentCycle cycle = societyPaymentCycleRepository.findTop1ByFromDateLessThanEqualAndToDateGreaterThanEqual(date, date);
        List<SocietyPaymentCycle> list = societyPaymentCycleRepository.findByFromDateGreaterThanEqualOrderByFromDate(cycle.getFromDate(), PageRequest.of(0, limit));
        for (SocietyPaymentCycle societyPaymentCycle : list) {
            societyPaymentCycle.setSociety(Hibernate.unproxy(societyPaymentCycle.getSociety(), Society.class));
            societyPaymentCycle.setFromShift(Hibernate.unproxy(societyPaymentCycle.getFromShift(), Shift.class));
            societyPaymentCycle.setToShift(Hibernate.unproxy(societyPaymentCycle.getToShift(), Shift.class));
        }
        return list;
    }
}
