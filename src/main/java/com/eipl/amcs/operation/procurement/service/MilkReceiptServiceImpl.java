package com.eipl.amcs.operation.procurement.service;

import com.eipl.amcs.base.repository.NextCodeRepository;
import com.eipl.amcs.exception.BusinessValidationFailException;
import com.eipl.amcs.exception.EntityNotFoundException;
import com.eipl.amcs.master.global.model.MilkType;
import com.eipl.amcs.master.global.model.Shift;
import com.eipl.amcs.master.global.repository.MilkTypeRepository;
import com.eipl.amcs.master.global.repository.ShiftRepository;
import com.eipl.amcs.master.operation.model.Member;
import com.eipl.amcs.master.org.model.Society;
import com.eipl.amcs.master.org.model.Union;
import com.eipl.amcs.master.org.repository.SocietyRepository;
import com.eipl.amcs.master.procurement.model.SocietyMilkPurchaseRate;
import com.eipl.amcs.master.procurement.model.SocietyMilkPurchaseRateApplicability;
import com.eipl.amcs.master.procurement.repository.SocietyMilkPurchaseRateApplicabilityRepository;
import com.eipl.amcs.operation.procurement.dto.MilkReceiptDto;
import com.eipl.amcs.operation.procurement.dto.MilkReceiptSummaryDto;
import com.eipl.amcs.operation.procurement.model.*;
import com.eipl.amcs.operation.procurement.repository.LocalMilkSaleRepository;
import com.eipl.amcs.operation.procurement.repository.MilkCollectionRepository;
import com.eipl.amcs.operation.procurement.repository.MilkReceiptRepository;
import com.eipl.amcs.operation.procurement.repository.MilkReceiptTransactionRepository;
import com.eipl.amcs.utils.CommonUtils;
import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.FieldError;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
public class MilkReceiptServiceImpl implements MilkReceiptService {

    private static final Logger log = LoggerFactory.getLogger(MilkReceiptServiceImpl.class);
    @Autowired
    private MilkReceiptRepository milkReceiptRepository;
    @Autowired
    private MilkReceiptTransactionRepository milkReceiptTransactionRepository;
    @Autowired
    private NextCodeRepository nextCodeRepository;
    @Autowired
    private SocietyMilkPurchaseRateApplicabilityRepository societyMilkPurchaseRateApplicabilityRepository;
    @Autowired
    private ShiftRepository shiftRepository;
    @Autowired
    private SocietyRepository societyRepository;
    @Autowired
    private MilkCollectionRepository milkCollectionRepository;
    @Autowired
    private LocalMilkSaleRepository localMilkSaleRepository;
    @Autowired
    private MilkTypeRepository milkTypeRepository;

    @Override
    @Transactional(readOnly = true)
    public List<MilkReceipt> findAll() {

        List<MilkReceipt> list = milkReceiptRepository.findAll(Sort.by("fromDate").descending());
//        for (MilkReceipt milkReceipt : list) {
//            milkReceipt.setMilkDispatch(Hibernate.unproxy(milkReceipt.getMilkDispatch(), MilkDispatch.class));
//            milkReceipt.setFromShift(Hibernate.unproxy(milkReceipt.getFromShift(), Shift.class));
//            milkReceipt.setToShift(Hibernate.unproxy(milkReceipt.getToShift(), Shift.class));
//            milkReceipt.setSociety(Hibernate.unproxy(milkReceipt.getSociety(), Society.class));
//            milkReceipt.setUnion(Hibernate.unproxy(milkReceipt.getUnion(), Union.class));
//        }

        log.info("MilkDispatchs findAll {} items fetched", list.size());
        return list;
    }


    @Override
    @Transactional
    public MilkReceipt save(MilkReceiptDto dto, String identityInfo) {

        MilkReceipt milkReceipt = dto.getMilkReceipt();
        List<MilkReceipt> milkReceiptList = milkReceiptRepository.findAll();
        Optional<MilkReceipt> milkReceipt1 = milkReceiptList.stream().filter(p -> p.getMilkDispatch() != null && p.getMilkDispatch().getChallanNo().equalsIgnoreCase(milkReceipt.getMilkDispatch().getChallanNo())).findAny();
        if (!milkReceipt1.isEmpty()) {
            FieldError wefdateNotValid = CommonUtils.getFieldError("milkreceipt", "MilkDispatch",
                    milkReceipt.getMilkDispatch().getChallanNo(), "challanno.not.valid");
            throw new BusinessValidationFailException(getClass(), wefdateNotValid);
        }

        milkReceipt.setInitData();
        String milkReceiptCode = nextCodeRepository.getNextCode("MilkReceipt", "code", milkReceipt.getSociety().getCode(), 0);
        milkReceipt.setCode(milkReceiptCode);
        milkReceiptRepository.save(milkReceipt);
        List<MilkReceiptTransaction> listReceiptTransactions = dto.getMilkReceiptTransaction();
        int txnCnt = 1;
        for (MilkReceiptTransaction list : listReceiptTransactions) {
            list.setMilkReceipt(milkReceipt);
            list.setTxnCode(milkReceipt.getCode() + "T" + txnCnt);
            list.setInitData();
            milkReceiptTransactionRepository.save(list);
            txnCnt++;
        }
        return dto.getMilkReceipt();
    }

    @Override
    public MilkReceipt update(MilkReceiptDto dto, String identityInfo) {
        MilkReceipt milkReceipt = dto.getMilkReceipt();
        List<MilkReceipt> milkReceiptList = milkReceiptRepository.findAll();
        Optional<MilkReceipt> milkReceipt1 = milkReceiptList.stream().filter(p -> p.getMilkDispatch() != null && !p.getMilkDispatch().getChallanNo().equalsIgnoreCase(milkReceipt.getMilkDispatch().getChallanNo())).findAny();
        if (!milkReceipt1.isEmpty()) {
            FieldError wefdateNotValid = CommonUtils.getFieldError("milkreceipt", "MilkDispatch",
                    milkReceipt.getMilkDispatch().getChallanNo(), "challanno.not.valid");
            throw new BusinessValidationFailException(getClass(), wefdateNotValid);
        }

        milkReceipt.setupdateData();
        milkReceiptRepository.save(milkReceipt);
        List<MilkReceiptTransaction> listReceiptTransactions = dto.getMilkReceiptTransaction();
        int txnCnt = 0;
        for (MilkReceiptTransaction list : listReceiptTransactions) {
            list.setMilkReceipt(milkReceipt);
            if (list.getTxnCode() == null) {
                list.setTxnCode(milkReceipt.getCode() + "T" + ++txnCnt);
                list.setInitData();
                milkReceiptTransactionRepository.customSave(list, identityInfo);
            } else {
                String[] code = list.getTxnCode().split("T");
                txnCnt = Integer.parseInt(code[1]);
                list.setupdateData();
                milkReceiptTransactionRepository.save(list);
            }
        }
        return milkReceipt;
    }

    @Override
    public Optional<MilkReceipt> findById(String code) {
        return milkReceiptRepository.findById(code);
    }

    @Override
    @Transactional
    public void delete(String code, String identityInfo) {
        Optional<MilkReceipt> receipt = milkReceiptRepository.findById(code);
        List<MilkReceiptTransaction> txn = milkReceiptTransactionRepository.findByMilkReceipt(receipt.get());
        for (MilkReceiptTransaction milkReceiptTransaction : txn) {
            milkReceiptTransactionRepository.delete(milkReceiptTransaction);
        }
        milkReceiptRepository.delete(receipt.get());
    }

    @Override
    @Transactional
    public void delete(MilkReceipt MilkReceipt, String identityInfo) {
        milkReceiptRepository.customDelete(MilkReceipt, identityInfo);
    }

    public SocietyMilkPurchaseRate fetchPurchaseRateCode(LocalDateTime date, Integer shiftCode, String societyCode) {
        Shift shift = shiftRepository.findById(shiftCode)
                .orElseThrow(() -> new EntityNotFoundException(Shift.class, "invalid.shift"));
        Shift shiftAll = shiftRepository.findById(3)
                .orElseThrow(() -> new EntityNotFoundException(Shift.class, "invalid.shift"));
        Society society = societyRepository.findById(societyCode)
                .orElseThrow(() -> new EntityNotFoundException(Shift.class, "invalid.society"));
        List<SocietyMilkPurchaseRateApplicability> appList = societyMilkPurchaseRateApplicabilityRepository.findRateApplicabilityTop2(date,
                shift, shiftAll, society);
        SocietyMilkPurchaseRate rate = null;
        if (appList != null) {
            rate = appList.get(0).getSocietyMilkPurchaseRate();
        }
        return rate;
    }

    @Override
    public List<MilkReceiptTransaction> findDetailByChallanNo(String code) {
        MilkReceipt member = milkReceiptRepository.findById(code)
                .orElseThrow(() -> new EntityNotFoundException(Member.class, "invalid.membercode"));
        List<MilkReceiptTransaction> dtl = milkReceiptTransactionRepository.findByMilkReceipt(member);
        return dtl;
    }

    @Override
    public Optional<MilkReceiptTransaction> findTransactionById(String code) {
        return milkReceiptTransactionRepository.findById(code);
    }

    @Override
    public void deleteTransaction(MilkReceiptTransaction milkReceiptTransaction, String identityInfo) {
        milkReceiptTransactionRepository.customDelete(milkReceiptTransaction, identityInfo);
    }


    @Override
    public List<MilkReceiptSummaryDto> fetchMilkReceiptSummary(LocalDateTime fromDt, LocalDateTime toDt) {
        List<MilkReceiptSummaryDto> list = new ArrayList<>();
        List<MilkCollection> listCollection = milkCollectionRepository.findByCollectionDateBetween(fromDt, toDt, null);
        List<LocalMilkSale> listSale = localMilkSaleRepository.findBySaleDateBetween(fromDt, toDt, null);
        List<MilkType> listMilkType = milkTypeRepository.findAll();
        for (MilkType milkType : listMilkType) {
            MilkReceiptSummaryDto dto = new MilkReceiptSummaryDto();
            dto.setMilkCollection(listCollection != null && !listCollection.isEmpty() ? BigDecimal.valueOf(listCollection.stream().filter(p -> p.getMilkType().getCode() == milkType.getCode())
                    .mapToDouble(m -> m.getQty().doubleValue()).sum()).setScale(3, RoundingMode.HALF_UP) : BigDecimal.ZERO);
            if (dto.getMilkCollection().compareTo(BigDecimal.ZERO) == 0)
                continue;
            double kgFatSum = listCollection.stream().filter(p -> p.getMilkType().getCode() == milkType.getCode())
                    .mapToDouble(m -> m.getFat().doubleValue() * m.getQty().doubleValue() / 100)
                    .sum();
            dto.setMilkType(Hibernate.unproxy(milkType, MilkType.class));
            dto.setMilkSale(listSale != null && !listSale.isEmpty() ? BigDecimal.valueOf(listSale.stream().filter(p -> p.getMilkType().getCode() == milkType.getCode())
                    .mapToDouble(m -> m.getQuantity().doubleValue()).sum()).setScale(3, RoundingMode.HALF_UP) : BigDecimal.ZERO);
            dto.setMilkBalance(dto.getMilkCollection().subtract(dto.getMilkSale()));
            dto.setAmount(listCollection != null && !listCollection.isEmpty() ? BigDecimal.valueOf(listCollection.stream().filter(p -> p.getMilkType().getCode() == milkType.getCode())
                    .mapToDouble(m -> m.getAmount().doubleValue()).sum()).setScale(3, RoundingMode.HALF_UP) : BigDecimal.ZERO);
            dto.setFat(BigDecimal.valueOf(kgFatSum / dto.getMilkCollection().doubleValue() * 100));
            list.add(dto);
        }
        return list;
    }
}