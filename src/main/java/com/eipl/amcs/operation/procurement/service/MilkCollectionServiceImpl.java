package com.eipl.amcs.operation.procurement.service;

import com.eipl.amcs.base.repository.NextCodeRepository;
import com.eipl.amcs.exception.BusinessValidationFailException;
import com.eipl.amcs.exception.EntityNotFoundException;
import com.eipl.amcs.master.account.model.*;
import com.eipl.amcs.master.account.repository.*;
import com.eipl.amcs.master.global.model.MilkQualityType;
import com.eipl.amcs.master.global.model.Shift;
import com.eipl.amcs.master.global.repository.MilkQualityTypeRepository;
import com.eipl.amcs.master.global.repository.MilkTypeRepository;
import com.eipl.amcs.master.global.repository.ShiftRepository;
import com.eipl.amcs.master.operation.model.Member;
import com.eipl.amcs.master.operation.repository.MemberRepository;
import com.eipl.amcs.master.org.model.Dock;
import com.eipl.amcs.master.org.model.Society;
import com.eipl.amcs.master.org.repository.DockRepository;
import com.eipl.amcs.master.org.repository.SocietyRepository;
import com.eipl.amcs.master.procurement.model.MemberMilkPurchaseRate;
import com.eipl.amcs.master.procurement.model.MemberMilkPurchaseRateApplicability;
import com.eipl.amcs.master.procurement.model.SocietyPaymentCycle;
import com.eipl.amcs.master.procurement.repository.MemberMilkPurchaseRateApplicabilityRepository;
import com.eipl.amcs.master.procurement.repository.SocietyPaymentCycleRepository;
import com.eipl.amcs.operation.billing.dto.MilkCollectionSummaryData;
import com.eipl.amcs.operation.procurement.dto.CollectionImportDto;
import com.eipl.amcs.operation.procurement.dto.MemberWiseCollectionDto;
import com.eipl.amcs.operation.procurement.dto.MilkCollectionPreReqDto;
import com.eipl.amcs.operation.procurement.model.MilkCollection;
import com.eipl.amcs.operation.procurement.repository.MilkCollectionRepository;
import com.eipl.amcs.setting.repository.HardwareDeviceConfigRepository;
import com.eipl.amcs.utils.AppConstant;
import com.eipl.amcs.utils.CommonUtils;
import com.eipl.amcs.utils.VoucherUtil;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.validation.FieldError;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class MilkCollectionServiceImpl implements MilkCollectionService {

    private final DateTimeFormatter CODE_DATE_FMT = DateTimeFormatter.ofPattern("yyMMdd");
    @Autowired
    private MilkCollectionRepository milkCollectionRepository;
    @Autowired
    private SocietyPaymentCycleRepository paymentCycleRepository;
    @Autowired
    private HardwareDeviceConfigRepository hardwareRepository;
    @Autowired
    private ShiftRepository shiftRepository;
    @Autowired
    private SocietyRepository societyRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private MilkTypeRepository milkTypeRepository;
    @Autowired
    private LedgerMappingEventRepository ledgerMappingEventRepository;
    @Autowired
    private NextCodeRepository nextCodeRepository;
    @Autowired
    private FinancialYearRepository financialYearRepository;
    @Autowired
    private SubLedgerRepository subLedgerRepository;
    @Autowired
    private VoucherRepository voucherRepository;
    @Autowired
    private VoucherTransactionRepository voucherTxnRepository;
    @Autowired
    private VoucherSubLedgerRepository voucherSubLedgerRepository;
    @Autowired
    private MemberMilkPurchaseRateApplicabilityRepository memberRateAppRepository;
    @Autowired
    private MilkQualityTypeRepository milkQualityRepository;
    @Autowired
    private DockRepository dockRepository;

    @Override
    public List<MilkCollection> findAllByMember(String code) {
        Member member = memberRepository.getById(code);
        return milkCollectionRepository.findAllByMemberOrderByCollectionDateDesc(member);
    }

    @Override
    public List<MilkCollection> findAllBetween(LocalDateTime fromDt, LocalDateTime toDt) {
        return milkCollectionRepository.findByCollectionDateBetween(fromDt, toDt,
                Sort.by("collectionDate").descending().and(Sort.by("sampleNo")));
    }

    @Override
    public List<MilkCollectionSummaryData> findTop10MemberSummaries(int year, int month, Integer selectedMilkTypeCode) {

        LocalDateTime start = LocalDateTime.of(year, month, 1, 0, 0);
        LocalDateTime end = start.plusMonths(1);
        Pageable topTen = PageRequest.of(0, 10);

        return milkCollectionRepository.findTop10MemberSummaries(start, end, selectedMilkTypeCode, topTen);
    }
    @Override
    public List<MilkCollection> findAllCollectionByDate(LocalDateTime fromDt, LocalDateTime toDt, String headers) {
        List<MilkCollection> milkCollectionList = milkCollectionRepository.findByCollectionDateBetween(fromDt, toDt, Sort.by("collectionDate"));
        for (MilkCollection milkCollection : milkCollectionList) {
            milkCollectionRepository.customSaveForSync(milkCollection, headers);
        }
        return null;
    }

    @Override
    public List<MilkCollection> findAllSummaryDataBetween(LocalDateTime fromDt, LocalDateTime toDt) {
        return milkCollectionRepository.findByCollectionDateBetweenAndXCol1(fromDt, toDt, "SUMMARY",
                Sort.by("collectionDate").descending().and(Sort.by("sampleNo")));
    }

    @Override
    public List<MilkCollection> findAllCollection(LocalDateTime date) {
        return milkCollectionRepository.findByCollectionDate(date, Sort.by("sampleNo"));
    }

    @Override
    public MilkCollection save(MilkCollection collection, String identityInfo) {
        //voucher entry
        String existngVoucherNo = null;
        if (collection.getSampleNo() > 1) {
            Optional<MilkCollection> collPrev = milkCollectionRepository.findTop1ByDockAndCollectionDateOrderBySampleNoDesc(collection.getDock(), collection.getCollectionDate());
            if (collPrev.isPresent())
                existngVoucherNo = collPrev.get().getVoucherNo();
        }
        String voucherNo = createVoucher(collection, existngVoucherNo, identityInfo, (short) 1, null);
        collection.setCode(collection.getDock().getDockNo() + "-" + collection.getCollectionDate().format(CODE_DATE_FMT)
                + collection.getShift().getCode() + "-" + collection.getSampleNo());
        collection.setVoucherNo(voucherNo);
        collection.setInitData();
        MilkCollection collNew = milkCollectionRepository.customSave(collection, identityInfo);
        collNew.setSociety(collection.getSociety());
        collNew.setDock(collection.getDock());
        collNew.setSocietyPaymentCycle(collection.getSocietyPaymentCycle());
        collNew.setShift(collection.getShift());
        collNew.setMember(collection.getMember());
        collNew.setMilkQualityType(collection.getMilkQualityType());
        collNew.setMilkType(collection.getMilkType());
        return collNew;
    }

    private String createVoucher(MilkCollection collection, String voucherNo, String identityInfo, short isInsert, BigDecimal prevAmount) {
        try {
            Member member = memberRepository.getById(collection.getMember().getCode());
            List<LedgerMappingEvent> eventsList = ledgerMappingEventRepository.findByEventcode(AppConstant.EventCode.MILK_COLLECTION);
            if (eventsList == null || eventsList.isEmpty())
                return null;

            if (eventsList.stream().anyMatch(e -> e.getXCol1().equalsIgnoreCase("0"))) return null;

            Optional<FinancialYear> financialYear = financialYearRepository.findCurrentFinancialYear(collection.getCollectionDate().toLocalDate());
            if (voucherNo == null) {
                voucherNo = nextCodeRepository.getNextCode("Voucher", "code",
                        collection.getSociety().getCode() + "/" + financialYear.get().getCode() + "/", 6);
                if (voucherNo == null)
                    return null;
                Voucher voucher = VoucherUtil.getVoucherInstance(voucherNo, null, collection.getCollectionDate().toLocalDate(),
                        collection.getCollectionDate().toLocalDate(), "Milk Collection Auto Posting " + collection.getCollectionDate(),
                        eventsList.get(0).getVoucherType(), financialYear.isPresent() ? financialYear.get().getCode() : null,
                        collection.getSociety(), collection.getUnionCode(), collection.getDock().getDockNo());
                voucher.setVoucherTransactions(new ArrayList<>());
                // Debit Txn
                VoucherTransaction debitTxn = VoucherUtil.getVoucherTxn(voucher, collection.getAmount(), false, eventsList.get(0).getDebitLedger(),
                        "Milk Collection Auto Posting For Purchase Ac", "1");
                if (debitTxn != null)
                    voucher.getVoucherTransactions().add(debitTxn);
                // Credit Txn
                VoucherTransaction creditTxn = VoucherUtil.getVoucherTxn(voucher, collection.getAmount(), true, eventsList.get(0).getCreditLedger(),
                        "Milk Collection Auto Posting For Purchase Ac", "2");
                if (creditTxn != null)
                    voucher.getVoucherTransactions().add(creditTxn);
                if (eventsList.get(0).getCreditSubLedger()) {
                    VoucherSubLedger voucherSubLedger = null;
                    Optional<SubLedger> subLedger = subLedgerRepository.findByTypeAndReferenceCode(member.getMemberType().getCode().shortValue(), collection.getMember().getCode());
                    if (subLedger.isPresent()) {
                        creditTxn.setVoucherSubLedgers(new ArrayList<>());
                        voucherSubLedger = VoucherUtil.getVoucherSubLedger(voucher, creditTxn, "1", collection.getAmount(), true,
                                "Milk Collection Auto Posting For Purchase Ac", subLedger.get());
                        if (voucherSubLedger != null)
                            creditTxn.getVoucherSubLedgers().add(voucherSubLedger);
                    }
                }
                voucherRepository.customSave(voucher, identityInfo);
                if (!voucher.getVoucherTransactions().isEmpty()) {
                    for (VoucherTransaction voucherTransaction : voucher.getVoucherTransactions()) {
                        voucherTxnRepository.customSave(voucherTransaction, identityInfo);
                        if (voucherTransaction.getVoucherSubLedgers() != null && !voucherTransaction.getVoucherSubLedgers().isEmpty()) {
                            for (VoucherSubLedger voucherSubLedger : voucherTransaction.getVoucherSubLedgers()) {
                                voucherSubLedgerRepository.customSave(voucherSubLedger, identityInfo);
                            }
                        }
                    }
                }
                return voucher.getCode();
            } else {
                Optional<Voucher> voucher = voucherRepository.findById(voucherNo);
                if (!voucher.isPresent())
                    return null;
                List<VoucherTransaction> txnList = voucherTxnRepository.findByVoucher(voucher.get());
                if (txnList == null || txnList.isEmpty())
                    return null;
                // Debit Txn
                Optional<VoucherTransaction> debitTxn = txnList.stream().filter(p -> p.getLedger().getCode().equals(eventsList.get(0).getDebitLedger().getCode()))
                        .findFirst();
                if (debitTxn.isPresent()) {
                    VoucherTransaction txn = debitTxn.get();
                    if (isInsert == (short) 1)
                        txn.setAmount(txn.getAmount().add(collection.getAmount()));
                    else if (isInsert == (short) 2)
                        txn.setAmount(txn.getAmount().add(collection.getAmount()).subtract(prevAmount));
                    else
                        txn.setAmount(txn.getAmount().subtract(prevAmount));
                    txn.setupdateData();
                    voucherTxnRepository.customUpdate(txn, identityInfo);
                }
                // Credit Txn
                Optional<VoucherTransaction> creditTxn = txnList.stream().filter(p -> p.getLedger().getCode().equals(eventsList.get(0).getCreditLedger().getCode()))
                        .findFirst();
                if (creditTxn.isPresent()) {
                    VoucherTransaction txn = creditTxn.get();
                    if (isInsert == (short) 1)
                        txn.setAmount(txn.getAmount().add(collection.getAmount()));
                    else if (isInsert == (short) 2)
                        txn.setAmount(txn.getAmount().add(collection.getAmount()).subtract(prevAmount));
                    else
                        txn.setAmount(txn.getAmount().subtract(prevAmount));
                    txn.setupdateData();
                    voucherTxnRepository.customUpdate(txn, identityInfo);
                    // credit subledger
                    if (eventsList.get(0).getCreditSubLedger()) {
                        VoucherSubLedger voucherSubLedger = null;
                        Optional<SubLedger> subLedger = subLedgerRepository.findByTypeAndReferenceCode(member.getMemberType().getCode().shortValue(), collection.getMember().getCode());
                        if (subLedger.isPresent()) {
                            List<VoucherSubLedger> subLedgerList = voucherSubLedgerRepository.findByVoucherTransaction(txn);
                            Optional<VoucherSubLedger> vSubLedger = subLedgerList.stream().filter(p -> p.getSubLedger().getReferenceCode().equals(collection.getMember().getCode()))
                                    .findFirst();
                            if (vSubLedger.isPresent()) {
                                VoucherSubLedger subLedger1 = vSubLedger.get();
                                if (isInsert == (short) 1) {
                                    subLedger1.setAmount(subLedger1.getAmount().add(collection.getAmount()));
                                    subLedger1.setupdateData();
                                    voucherSubLedgerRepository.customUpdate(subLedger1, identityInfo);
                                } else if (isInsert == (short) 2) {
                                    subLedger1.setAmount(subLedger1.getAmount().add(collection.getAmount()).subtract(prevAmount));
                                    subLedger1.setupdateData();
                                    voucherSubLedgerRepository.customUpdate(subLedger1, identityInfo);
                                } else {
                                    voucherSubLedgerRepository.customDelete(subLedger1, identityInfo);
                                }
                            } else {
                                if (isInsert == (short) 1 || isInsert == (short) 2) {
                                    voucherSubLedger = VoucherUtil.getVoucherSubLedger(voucher.get(), txn, String.valueOf(subLedgerList.size() + 1), collection.getAmount(), true,
                                            "Milk Collection Auto Posting For Purchase Ac", subLedger.get());
                                    voucherSubLedgerRepository.customSave(voucherSubLedger, identityInfo);
                                }
                            }
                        }
                    }
                }
                return voucher.get().getCode();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return voucherNo;
        }
    }

    @Override
    public MilkCollection update(MilkCollection collection, String identityInfo) {
        MilkCollection prevData = milkCollectionRepository.findById(collection.getCode())
                .orElseThrow(() -> new EntityNotFoundException(MilkCollection.class, "invalid.collectiondata"));
        String voucherNo = createVoucher(collection, prevData.getVoucherNo(), identityInfo, (short) 2, prevData.getAmount());
        collection.setupdateData();
        collection.setVoucherNo(voucherNo);
        MilkCollection collNew = milkCollectionRepository.customUpdate(collection, identityInfo);
        collNew.setSociety(collection.getSociety());
        collNew.setDock(collection.getDock());
        collNew.setSocietyPaymentCycle(collection.getSocietyPaymentCycle());
        collNew.setShift(collection.getShift());
        collNew.setMember(collection.getMember());
        collNew.setMilkQualityType(collection.getMilkQualityType());
        collNew.setMilkType(collection.getMilkType());
        return collNew;
    }

    @Override
    public void delete(String code, String identityInfo) {
        MilkCollection data = milkCollectionRepository.findById(code)
                .orElseThrow(() -> new EntityNotFoundException(MilkCollection.class, "invalid.collectiondata"));
        createVoucher(data, data.getVoucherNo(), identityInfo, (short) 3, data.getAmount());
        milkCollectionRepository.customDelete(data, identityInfo);
    }

    @Override
    public MilkCollectionPreReqDto fetchPreRequsite(LocalDateTime date, Integer shiftCode, String societyCode) {
        MilkCollectionPreReqDto dto = new MilkCollectionPreReqDto();
        dto.setPaymentCycle(paymentCycleRepository.findSocietyPaymentCycle(date));
        dto.setHardwareConfigList(hardwareRepository.findAll());
        Shift shift = shiftRepository.findById(shiftCode)
                .orElseThrow(() -> new EntityNotFoundException(Shift.class, "invalid.shift"));
        Shift shiftAll = shiftRepository.findById(3)
                .orElseThrow(() -> new EntityNotFoundException(Shift.class, "invalid.shift"));
        Society society = societyRepository.findById(societyCode)
                .orElseThrow(() -> new EntityNotFoundException(Shift.class, "invalid.society"));
        List<MemberMilkPurchaseRateApplicability> appList = memberRateAppRepository.findRateApplicabilityTop2(date,
                shift, shiftAll, society);

        MemberMilkPurchaseRate rate = null;
        if (appList != null) {
            rate = appList.get(0).getMemberMilkPurchaseRate();

            if (appList.size() > 1) {
                List<MemberMilkPurchaseRateApplicability> appList2 = new ArrayList<>();
                appList2.add(appList.get(0));
                appList2.add(appList.get(1));
                Optional<MemberMilkPurchaseRate> rate1 = appList2.stream()
                        .filter(p -> p.getMemberMilkPurchaseRate().getSociety() != null)
                        .map(m -> m.getMemberMilkPurchaseRate()).findFirst();
                if (rate1.isPresent())
                    rate = rate1.get();
            }
        }
        dto.setMemberRate(rate.getCode());
        return dto;
    }

    @Override
    public Number fetchNextSampleNo(LocalDateTime dt, String dockCode) {
        Dock dock = dockRepository.findById(dockCode)
                .orElseThrow(() -> new EntityNotFoundException(Dock.class, "invalid.dock"));
        Optional<MilkCollection> collection = milkCollectionRepository.findTop1ByDockAndCollectionDateOrderBySampleNoDesc(dock, dt);
        if (collection.isPresent())
            return collection.get().getSampleNo() + 1;
        return 1;
    }

    @Override
    public List<CollectionImportDto> importCollections(List<MilkCollection> dtoList, String header) {
        List<CollectionImportDto> list = new ArrayList<>();
        Map<LocalDateTime, SocietyPaymentCycle> query = new HashMap<>();

        dtoList.forEach(item -> {
            try {
                item.setCode(item.getDock().getDockNo() + "-" + item.getCollectionDate().format(CODE_DATE_FMT)
                        + item.getShift().getCode() + "-" + item.getSampleNo());
                Optional<MilkCollection> collectionData = milkCollectionRepository.findById(item.getCode());
                if (collectionData.isPresent()) {
                    MilkCollection milkCollectionOld = collectionData.get();
                    milkCollectionOld.setAmount(item.getAmount());
                    milkCollectionOld.setRateCode(item.getRateCode());
                    milkCollectionOld.setRtpl(item.getRtpl());
                    milkCollectionOld.setQty(item.getQty());
                    milkCollectionOld.setSnf(item.getSnf());
                    milkCollectionOld.setFat(item.getFat());
                    milkCollectionOld.setMember(item.getMember());
                    milkCollectionOld.setShift(item.getShift());
                    milkCollectionOld.setCollectionDate(item.getCollectionDate());
                    milkCollectionOld.setMilkType(item.getMilkType());
                    milkCollectionOld.setupdateData();
                    milkCollectionRepository.customUpdate(milkCollectionOld, header);
                    list.add(new CollectionImportDto(item.getMember().getCode(), "update", "success"));
                } else {
                    MilkCollection milkCollection = item;
                    if (query.containsKey(item.getCollectionDate())) {
                        milkCollection.setSocietyPaymentCycle(query.get(milkCollection.getCollectionDate()));
                    } else {
                        query.put(item.getCollectionDate(),
                                paymentCycleRepository.findSocietyPaymentCycle(item.getCollectionDate()));
                        milkCollection.setSocietyPaymentCycle(query.get(item.getCollectionDate()));
                    }
                    milkCollection.setInitData();
                    milkCollection.setCode(milkCollection.getDock().getDockNo() + "-"
                            + milkCollection.getCollectionDate().format(CODE_DATE_FMT)
                            + milkCollection.getShift().getCode() + "-" + milkCollection.getSampleNo());
                    milkCollectionRepository.customSave(milkCollection, header);
                    list.add(new CollectionImportDto(item.getCode(), "insert", "success"));
                }
            } catch (Exception e) {
                list.add(new CollectionImportDto(item.getCode(), e.getMessage(), "error"));
            }
        });
        return list;
    }

    @Override
    public List<CollectionImportDto> migrateCollections(List<MilkCollection> dtoList, String header) {
        List<CollectionImportDto> list = new ArrayList<>();
        Map<LocalDateTime, SocietyPaymentCycle> query = new HashMap<>();
        dtoList.forEach(item -> {
            try {
                item.setCode(item.getDock().getDockNo() + "-" + item.getCollectionDate().format(CODE_DATE_FMT)
                        + item.getShift().getCode() + "-" + item.getSampleNo());
                Optional<MilkCollection> collectionData = milkCollectionRepository.findById(item.getCode());
                if (collectionData.isPresent()) {
                    MilkCollection milkCollectionOld = collectionData.get();
                    milkCollectionOld.setAmount(item.getAmount());
                    milkCollectionOld.setRateCode(item.getRateCode());
                    milkCollectionOld.setRtpl(item.getRtpl());
                    milkCollectionOld.setQty(item.getQty());
                    milkCollectionOld.setSnf(item.getSnf());
                    milkCollectionOld.setFat(item.getFat());
                    milkCollectionOld.setMember(item.getMember());
                    milkCollectionOld.setShift(item.getShift());
                    milkCollectionOld.setCollectionDate(item.getCollectionDate());
                    milkCollectionOld.setMilkType(item.getMilkType());
                    milkCollectionOld.setupdateData();
                    milkCollectionRepository.save(milkCollectionOld);
                    list.add(new CollectionImportDto(item.getMember().getCode(), "update", "success"));
                } else {
                    MilkCollection milkCollection = item;
                    if (query.containsKey(item.getCollectionDate())) {
                        milkCollection.setSocietyPaymentCycle(query.get(milkCollection.getCollectionDate()));
                    } else {
                        query.put(item.getCollectionDate(),
                                paymentCycleRepository.findSocietyPaymentCycle(item.getCollectionDate()));
                        milkCollection.setSocietyPaymentCycle(query.get(item.getCollectionDate()));
                    }
                    milkCollection.setInitData();
                    milkCollection.setCode(milkCollection.getDock().getDockNo() + "-"
                            + milkCollection.getCollectionDate().format(CODE_DATE_FMT)
                            + milkCollection.getShift().getCode() + "-" + milkCollection.getSampleNo());
                    milkCollectionRepository.save(milkCollection);
                    list.add(new CollectionImportDto(item.getCode(), "insert", "success"));
                }
            } catch (Exception e) {
                list.add(new CollectionImportDto(item.getCode(), e.getMessage(), "error"));
            }
        });
        return list;
    }

    @Override
    public List<MilkCollection> findByMemberAndDate(LocalDateTime date, String code) {
        Member member = memberRepository.findById(code).get();
        member.setSociety(Hibernate.unproxy(member.getSociety(), Society.class));
        return milkCollectionRepository.findByCollectionDateAndMember(date, member);
    }

    @Override
    public Optional<MilkCollection> findById(String code) {
        return milkCollectionRepository.findById(code);
    }

    @Override
    public MilkCollectionSummaryData saveMilkCollectionSummaryData(MilkCollectionSummaryData collection,
                                                                   String identityInfo) {
        LocalDateTime collectionDate = LocalDateTime.of(collection.getDate(), LocalTime.of(6, 0));
        Optional<MilkCollection> collectionData = milkCollectionRepository.findByCollectionDateAndMemberAndMilkType(collectionDate,
                collection.getMember(), collection.getMilkType());
        if (collectionData.isPresent()) {
            FieldError nameNotValid = CommonUtils.getFieldError("MilkCollection", "date",
                    collection.getDate().toString(), "collectionrecord.alreadyexists");
            throw new BusinessValidationFailException(getClass(), nameNotValid);
        }

        MilkCollection data = new MilkCollection();
        Shift shift = shiftRepository.findById(1).get();
        data.setCode(collection.getDock().getDockNo() + "-" + collection.getDate().format(CODE_DATE_FMT)
                + shift.getCode() + "-" + collection.getMilkType().getCode() + collection.getMember().getCodeEx());
        data.setSampleNo(0);
        data.setCollectionDate(collectionDate);
        data.setFat(BigDecimal.ZERO);
        data.setSnf(BigDecimal.ZERO);
        data.setClr(BigDecimal.ZERO);
        data.setWater(BigDecimal.ZERO);
        data.setDensity(BigDecimal.ZERO);
        data.setLectose(BigDecimal.ZERO);
        data.setProtein(BigDecimal.ZERO);
        data.setRtpl(BigDecimal.ZERO);
        data.setQty(collection.getMilkQuantity());
        data.setAmount(collection.getMilkAmount());
        data.setWeightAuto(false);
        data.setQualityAuto(false);
        data.setAvgParam(false);
        data.setVoucherNo(null);
        data.setRateCode(null);
        data.setWsCode(null);
        data.setAnalyserCode(null);
        data.setQtyMode(0);
        data.setConvertedQty(BigDecimal.ZERO); // need to change
        data.setConvertedQtyMode(1);
        data.setSocietyPaymentCycle(collection.getPaymentCycle());
        data.setMilkType(collection.getMilkType());
        data.setMember(collection.getMember());
        data.setSociety(collection.getSociety());
        data.setDock(collection.getDock());
        data.setMilkQualityType(collection.getMilkQualityType());
        data.setShift(shift);
        data.setxCol1("SUMMARY");
        data.setUnionCode(collection.getUnion());
        data.setInitData();
        milkCollectionRepository.customSave(data, identityInfo);
        return collection;
    }

    @Override
    public MilkCollectionSummaryData updateMilkCollectionSummaryData(MilkCollectionSummaryData collection,
                                                                     String identityHeader) {
        LocalDateTime collectionDate = LocalDateTime.of(collection.getDate(), LocalTime.of(6, 0));
        Optional<MilkCollection> collectionData = milkCollectionRepository.findByCollectionDateAndMemberAndMilkType(collectionDate,
                collection.getMember(), collection.getMilkType());
        if (collectionData.isPresent()) {
            MilkCollection data = collectionData.get();
            data.setSampleNo(0);
            data.setQty(collection.getMilkQuantity());
            data.setAmount(collection.getMilkAmount());
            data.setSocietyPaymentCycle(collection.getPaymentCycle());
            data.setMilkType(collection.getMilkType());
            data.setMember(collection.getMember());
            data.setSociety(collection.getSociety());
            data.setDock(collection.getDock());
            data.setMilkQualityType(collection.getMilkQualityType());
            data.setupdateData();

            milkCollectionRepository.customUpdate(data, identityHeader);
        }
        return collection;
    }

    @Override
    public Map<String, BigDecimal> findAvgFatAndSnf(String code, int no, String milktype, LocalDate date,
                                                    int shiftCode) {
        List<Timestamp> listDates = milkCollectionRepository.findLastDates(milktype,
                CommonUtils.getLocalDateTimeFromDateAndShift(date, shiftRepository.findById(shiftCode).get()), code, no);
        Map<String, BigDecimal> avg = new HashMap<>();
        Map<String, BigDecimal> total = new HashMap<>();
        if (!listDates.isEmpty()) {
            avg = milkCollectionRepository.findAvgFatAndSnf(code, listDates.get(listDates.size() - 1).toLocalDateTime(),
                    CommonUtils.getLocalDateTimeFromDateAndShift(date, shiftRepository.findById(shiftCode).get()));
            return avg;
        } else
            return null;
    }

    @Override
    public Map<String, BigDecimal> findTotals(String code, String no, int milkType) {
        Map<String, BigDecimal> total = new HashMap<>();
        total = milkCollectionRepository.findTotals(code, no, milkType);
        System.out.println(total.get("amt"));
        System.out.println(total.get("qty"));
        return total;
    }

    @Override
    public List<CollectionImportDto> importCollectionSummaryData(List<MilkCollectionSummaryData> data,
                                                                 String identityHeader) {
        List<CollectionImportDto> list = new ArrayList<>();
        data.forEach(item -> {
            try {
                LocalDateTime collectionDate = LocalDateTime.of(item.getDate(), LocalTime.of(6, 0));
                Optional<MilkCollection> collectionData = milkCollectionRepository
                        .findByCollectionDateAndMemberAndMilkType(collectionDate, item.getMember(), item.getMilkType());
                Shift shift = shiftRepository.findById(1).get();
                MilkQualityType milkQualityType = milkQualityRepository.findById(1).get();
                Map<LocalDate, SocietyPaymentCycle> mapPaymentCycle = new HashMap<>();
                if (collectionData.isPresent()) {
                    MilkCollection dataColl = collectionData.get();
                    dataColl.setSampleNo(0);
                    dataColl.setQty(item.getMilkQuantity());
                    dataColl.setAmount(item.getMilkAmount());
                    dataColl.setupdateData();

                    milkCollectionRepository.customUpdate(dataColl, identityHeader);
                    list.add(new CollectionImportDto(dataColl.getCode(), "update", "success"));
                } else {
                    MilkCollection dataColl = new MilkCollection();
                    dataColl.setCode(item.getDock().getDockNo() + "-" + item.getDate().format(CODE_DATE_FMT)
                            + shift.getCode() + "-" + item.getMilkType().getCode() + item.getMember().getCodeEx());
                    dataColl.setSampleNo(0);
                    dataColl.setCollectionDate(collectionDate);
                    dataColl.setFat(BigDecimal.ZERO);
                    dataColl.setSnf(BigDecimal.ZERO);
                    dataColl.setClr(BigDecimal.ZERO);
                    dataColl.setWater(BigDecimal.ZERO);
                    dataColl.setDensity(BigDecimal.ZERO);
                    dataColl.setLectose(BigDecimal.ZERO);
                    dataColl.setProtein(BigDecimal.ZERO);
                    dataColl.setRtpl(BigDecimal.ZERO);
                    dataColl.setQty(item.getMilkQuantity());
                    dataColl.setAmount(item.getMilkAmount());
                    dataColl.setWeightAuto(false);
                    dataColl.setQualityAuto(false);
                    dataColl.setAvgParam(false);
                    dataColl.setVoucherNo(null);
                    dataColl.setRateCode(null);
                    dataColl.setWsCode(null);
                    dataColl.setAnalyserCode(null);
                    dataColl.setQtyMode(0);
                    dataColl.setConvertedQty(BigDecimal.ZERO); // need to change
                    dataColl.setConvertedQtyMode(1);
                    SocietyPaymentCycle paymentCycle = mapPaymentCycle.get(item.getDate());
                    if (paymentCycle == null)
                        mapPaymentCycle.put(item.getDate(), paymentCycleRepository
                                .findSocietyPaymentCycle(LocalDateTime.of(item.getDate(), LocalTime.NOON)));
                    dataColl.setSocietyPaymentCycle(mapPaymentCycle.get(item.getDate()));
                    dataColl.setMilkType(item.getMilkType());
                    dataColl.setMember(item.getMember());
                    dataColl.setSociety(item.getSociety());
                    dataColl.setDock(item.getDock());
                    dataColl.setMilkQualityType(milkQualityType);
                    dataColl.setShift(shift);
                    dataColl.setxCol1("SUMMARY");
                    dataColl.setUnionCode(item.getUnion());
                    dataColl.setInitData();
                    milkCollectionRepository.customSave(dataColl, identityHeader);
                    list.add(new CollectionImportDto(dataColl.getCode(), "insert", "success"));
                }
            } catch (Exception e) {
                list.add(new CollectionImportDto("0", e.getMessage(), "error"));
            }
        });
        return list;
    }

    @Override
    public List<MilkCollection> findAllCollectionByMember(LocalDateTime fromDt, LocalDateTime toDate, String code) {
        return milkCollectionRepository.findByCollectionDateBetweenAndMember(fromDt, toDate, memberRepository.findByCode(code));
    }

    @Override
    public List<MilkCollection> findAllCollectionByDockNo(LocalDateTime fromDt, LocalDateTime toDt, String dockNo) {
        Dock dock = dockRepository.findById(dockNo)
                .orElseThrow(() -> new EntityNotFoundException(Dock.class, "dock", dockNo));
        return milkCollectionRepository.findByCollectionDateBetweenAndDock(fromDt, toDt, dock);
    }

    @Override
    public MemberWiseCollectionDto findAllInOne(String code, int no, String milktype, LocalDate date, int shiftCode, String paymentCycleCode) {

        MemberWiseCollectionDto dto = new MemberWiseCollectionDto();
        List<Timestamp> listDates = milkCollectionRepository.findLastDates(milktype,
                CommonUtils.getLocalDateTimeFromDateAndShift(date, shiftRepository.findById(shiftCode).get()), code, no);
        Map<String, BigDecimal> avg;
        Map<String, BigDecimal> total;
        total = milkCollectionRepository.findTotals(code, paymentCycleCode, Integer.parseInt(milktype));
        Member member = memberRepository.findById(code).get();
        List<MilkCollection> collectionList = new ArrayList<>();
        if (listDates.size() != 0) {
            collectionList = milkCollectionRepository.findByMemberAndCollectionDateBetweenAndMilkTypeOrderByCollectionDateDesc(member, listDates.get(listDates.size() - 1).toLocalDateTime(),
                    CommonUtils.getLocalDateTimeFromDateAndShift(date, shiftRepository.findById(shiftCode).get()), milkTypeRepository.getById(Integer.parseInt(milktype)));
        }
        if (!listDates.isEmpty()) {
            avg = milkCollectionRepository.findAvgFatAndSnf(code, listDates.get(listDates.size() - 1).toLocalDateTime(),
                    CommonUtils.getLocalDateTimeFromDateAndShift(date, shiftRepository.findById(shiftCode).get()));
            dto.setAvg(avg);
        } else {
            dto.setAvg(null);
        }
        dto.setTotal(total);
        dto.setCollectionList(collectionList);
        return dto;
    }

    @Override
    public BigDecimal findTotalAmount(String societyPaymentCycleCode, String code) {
        Optional<SocietyPaymentCycle> spc = paymentCycleRepository.findById(societyPaymentCycleCode);
        if (spc.isPresent())
            return milkCollectionRepository.findTotalCollectionDateBetweenAndMember(code, societyPaymentCycleCode, spc.get().getFromDate(), spc.get().getToDate());
        return null;
    }

    @Override
    public MilkCollection desktopCollectionSave(MilkCollection collection, String identityInfo) {
        //voucher entry
        String existngVoucherNo = null;
        if (collection.getSampleNo() > 1) {
            Optional<MilkCollection> collPrev = milkCollectionRepository.findTop1ByDockAndCollectionDateOrderBySampleNoDesc(collection.getDock(), collection.getCollectionDate());
            if (collPrev.isPresent())
                existngVoucherNo = collPrev.get().getVoucherNo();
        }
        String voucherNo = createVoucher(collection, existngVoucherNo, identityInfo, (short) 1, null);
        collection.setCode(collection.getDock().getDockNo() + "-" + collection.getCollectionDate().format(CODE_DATE_FMT)
                + collection.getShift().getCode() + "-" + collection.getSampleNo());
        collection.setVoucherNo(voucherNo);
        collection.setInitData();
        MilkCollection collNew = milkCollectionRepository.save(collection);
        collNew.setSociety(collection.getSociety());
        collNew.setDock(collection.getDock());
        collNew.setSocietyPaymentCycle(collection.getSocietyPaymentCycle());
        collNew.setShift(collection.getShift());
        collNew.setMember(collection.getMember());
        collNew.setMilkQualityType(collection.getMilkQualityType());
        collNew.setMilkType(collection.getMilkType());
        return collNew;
    }
}
