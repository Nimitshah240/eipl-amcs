package com.eipl.amcs.operation.procurement.service;

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
import com.eipl.amcs.operation.procurement.dto.MilkDispatchDto;
import com.eipl.amcs.operation.procurement.dto.MilkDispatchSummaryDto;
import com.eipl.amcs.operation.procurement.model.LocalMilkSale;
import com.eipl.amcs.operation.procurement.model.MilkCollection;
import com.eipl.amcs.operation.procurement.model.MilkDispatch;
import com.eipl.amcs.operation.procurement.model.MilkDispatchTransaction;
import com.eipl.amcs.operation.procurement.repository.LocalMilkSaleRepository;
import com.eipl.amcs.operation.procurement.repository.MilkCollectionRepository;
import com.eipl.amcs.operation.procurement.repository.MilkDispatchRepository;
import com.eipl.amcs.operation.procurement.repository.MilkDispatchTransactionRepository;
import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
public class MilkDispatchServiceImpl implements MilkDispatchService {
    private static final Logger log = LoggerFactory.getLogger(MilkDispatchServiceImpl.class);
    @Autowired
    private MilkDispatchRepository dispatchRepository;
    @Autowired
    private MilkDispatchTransactionRepository milkDispatchTransactionRepository;
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
    public List<MilkDispatch> findAll() {
        List<MilkDispatch> list = dispatchRepository.findAll(Sort.by("fromDate", "toDate").descending());
        for (MilkDispatch milkDispatch : list) {
            milkDispatch.setFromShift(Hibernate.unproxy(milkDispatch.getFromShift(), Shift.class));
            milkDispatch.setToShift(Hibernate.unproxy(milkDispatch.getToShift(), Shift.class));
            milkDispatch.setSociety(Hibernate.unproxy(milkDispatch.getSociety(), Society.class));
            milkDispatch.setUnion(Hibernate.unproxy(milkDispatch.getUnion(), Union.class));
        }
        log.info("MilkDispatchs findAll {} items fetched", list.size());
        return list;
    }

    @Override
    @Transactional
    public MilkDispatch save(MilkDispatchDto dto, String identityInfo) {
        MilkDispatch milkDispatch = dto.getMilkDispatch();
        milkDispatch.setInitData();
        dispatchRepository.customSave(milkDispatch, identityInfo);
        List<MilkDispatchTransaction> listDispatchTransactions = dto.getMilkDispatchTransaction();
        int txnCnt = 1;
        for (MilkDispatchTransaction list : listDispatchTransactions) {
            list.setMilkDispatch(milkDispatch);
            list.setTxnCode(milkDispatch.getChallanNo() + "T" + txnCnt);
            list.setInitData();
            milkDispatchTransactionRepository.customSave(list, identityInfo);
            txnCnt++;

        }

        return dto.getMilkDispatch();
    }

    @Override
    public MilkDispatch update(MilkDispatchDto dto, String identityInfo) {
        MilkDispatch milkDispatch = dto.getMilkDispatch();
        milkDispatch.setupdateData();
        dispatchRepository.customUpdate(milkDispatch, identityInfo);
        List<MilkDispatchTransaction> listDispatchTransactions = dto.getMilkDispatchTransaction();
        int txnCnt = 0;
        for (MilkDispatchTransaction list : listDispatchTransactions) {
            list.setMilkDispatch(milkDispatch);
            if (list.getTxnCode() == null) {
                list.setTxnCode(milkDispatch.getChallanNo() + "T" + ++txnCnt);
                list.setInitData();
                milkDispatchTransactionRepository.customSave(list, identityInfo);
            } else {
                String[] code = list.getTxnCode().split("T");
                txnCnt = Integer.parseInt(code[1]);
                list.setupdateData();
                milkDispatchTransactionRepository.customUpdate(list, identityInfo);
            }
        }
        return milkDispatch;
    }

    @Override
    public Optional<MilkDispatch> findById(String code) {
        return dispatchRepository.findById(code);
    }

    @Override
    @Transactional
    public void delete(String code, String identityInfo) {
        Optional<MilkDispatch> dispatch = dispatchRepository.findById(code);
        List<MilkDispatchTransaction> txn = milkDispatchTransactionRepository.findByMilkDispatch(dispatch.get());
        for (MilkDispatchTransaction milkDispatchTransaction : txn) {
            milkDispatchTransactionRepository.customDelete(milkDispatchTransaction, identityInfo);
        }
        dispatchRepository.customDelete(dispatch.get(), identityInfo);
    }

    @Override
    @Transactional
    public void delete(MilkDispatch MilkDispatch, String identityInfo) {
        dispatchRepository.customDelete(MilkDispatch, identityInfo);
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
            rate.setShift(Hibernate.unproxy(rate.getShift(), Shift.class));
        }
        return rate;
    }

    @Override
    public List<MilkDispatchTransaction> findDetailByChallanNo(String code) {
        MilkDispatch member = dispatchRepository.findById(code)
                .orElseThrow(() -> new EntityNotFoundException(Member.class, "invalid.membercode"));
        List<MilkDispatchTransaction> dtl = milkDispatchTransactionRepository.findByMilkDispatch(member);
        return dtl;
    }

    @Override
    public Optional<MilkDispatchTransaction> findTransactionById(String code) {
        return milkDispatchTransactionRepository.findById(code);
    }

    @Override
    public void deleteTransaction(MilkDispatchTransaction milkDispatchTransaction, String identityInfo) {
        milkDispatchTransactionRepository.customDelete(milkDispatchTransaction, identityInfo);
    }

    @Override
    public List<MilkDispatchSummaryDto> fetchMilkDispatchSummary(LocalDateTime fromDt, LocalDateTime toDt) {
        List<MilkDispatchSummaryDto> list = new ArrayList<>();
        List<MilkCollection> listCollection = milkCollectionRepository.findByCollectionDateBetween(fromDt, toDt, null);
        List<LocalMilkSale> listSale = localMilkSaleRepository.findBySaleDateBetween(fromDt, toDt, null);
        List<MilkType> listMilkType = milkTypeRepository.findAll();
        for (MilkType milkType : listMilkType) {
            MilkDispatchSummaryDto dto = new MilkDispatchSummaryDto();
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
            dto.setAmount(listCollection != null && !listCollection.isEmpty() ?
                    BigDecimal.valueOf(listCollection.stream().filter(p -> p.getMilkType().getCode() == milkType.getCode())
                            .mapToDouble(m -> m.getAmount().doubleValue()).sum()).setScale(3, RoundingMode.HALF_UP) : BigDecimal.ZERO);
            dto.setFat(BigDecimal.valueOf(kgFatSum / dto.getMilkCollection().doubleValue() * 100));
            list.add(dto);
        }
        return list;
    }
}