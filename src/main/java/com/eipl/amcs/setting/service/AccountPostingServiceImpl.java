package com.eipl.amcs.setting.service;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.service.NextCodeService;
import com.eipl.amcs.master.account.model.*;
import com.eipl.amcs.master.account.repository.*;
import com.eipl.amcs.master.global.model.MilkType;
import com.eipl.amcs.master.global.repository.MilkTypeRepository;
import com.eipl.amcs.master.inventory.model.Product;
import com.eipl.amcs.master.inventory.repository.ProductRepository;
import com.eipl.amcs.master.procurement.model.SocietyPaymentCycle;
import com.eipl.amcs.master.procurement.repository.SocietyPaymentCycleRepository;
import com.eipl.amcs.operation.procurement.model.LocalMilkSale;
import com.eipl.amcs.operation.procurement.model.MilkCollection;
import com.eipl.amcs.operation.procurement.repository.LocalMilkSaleRepository;
import com.eipl.amcs.operation.procurement.repository.MilkCollectionRepository;
import com.eipl.amcs.operation.procurement.service.MilkCollectionService;
import com.eipl.amcs.setting.model.AccountPosting;
import com.eipl.amcs.setting.repository.AccountPostingRepository;
import com.eipl.amcs.utils.AppConstant;
import com.eipl.amcs.utils.CommonUtils;
import com.eipl.amcs.utils.VoucherUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.eipl.amcs.utils.AppConstant.EventCode.LOCAL_MILK_SALE;


@Slf4j
@Service
public class AccountPostingServiceImpl implements AccountPostingService {

    @Autowired
    private AccountPostingRepository accountPostingRepository;
    @Autowired
    private NextCodeService nextCodeService;
    @Autowired
    private VoucherRepository voucherRepository;
    @Autowired
    private VoucherTransactionRepository voucherTransactionRepository;
    @Autowired
    private MilkCollectionService milkCollectionService;
    @Autowired
    private LocalMilkSaleRepository localMilkSaleRepository;
    @Autowired
    private SubLedgerRepository subLedgerRepository;
    @Autowired
    private VoucherSubLedgerRepository voucherSubLedgerRepository;
    @Autowired
    private LedgerMappingEventRepository ledgerMappingEventRepository;
    @Autowired
    private FinancialYearRepository financialYearRepository;
    @Autowired
    private VoucherRawRepository voucherRawRepository;
    @Autowired
    private MilkCollectionRepository milkCollectionRepository;
    @Autowired
    private SocietyPaymentCycleRepository societyPaymentCycleRepository;
    @Autowired
    private VoucherTransactionRawRepository voucherTransactionRawRepository;
    @Autowired
    private VoucherSubLedgerRawRepository voucherSubLedgerRawRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private MilkTypeRepository milkTypeRepository;


    @Transactional
    public List<Voucher> loadAccountPostingData(AccountPosting draftAccountPosting) {
        try {

            int type = draftAccountPosting.getPostingType();
            LocalDateTime fromDateTime = CommonUtils.getLocalDateTimeFromDateAndShift(draftAccountPosting.getFromDate(), draftAccountPosting.getFromShift());
            LocalDateTime toDateTime = CommonUtils.getLocalDateTimeFromDateAndShift(draftAccountPosting.getToDate(), draftAccountPosting.getToShift());

//          GET EVENT AND LEDGER MAPPING EVENT
            List<LedgerMappingEvent> ledgerMappingEvent = ledgerMappingEventRepository.findByEventcode(draftAccountPosting.getEventType());
            List<Events> eventsList = eventRepository.findByEventCode(draftAccountPosting.getEventType());

//          GET MILK TYPE LIST AND PRODUCT
            List<MilkType> milkTypeList = milkTypeRepository.findAll();
            Map<MilkType, Product> productMap = productRepository
                    .findAllByMilkAndMilkTypeIn(true, milkTypeList)
                    .stream()
                    .collect(Collectors.toMap(
                            Product::getMilkType,
                            p -> p,
                            (existing, replacement) -> existing
                    ));


            if (draftAccountPosting.getEventType() == AppConstant.EventCode.MILK_COLLECTION) {
                // Load milk collections
                List<MilkCollection> milkCollectionList;
                if (type == 3) {
                    List<SocietyPaymentCycle> cycles = societyPaymentCycleRepository.findCycles(fromDateTime, toDateTime);
                    if (cycles == null) return null;
                    milkCollectionList = milkCollectionRepository.findBySocietyPaymentCycleInOrderByCollectionDateAsc(cycles);
                } else {
                    milkCollectionList = milkCollectionService.findAllBetween(fromDateTime, toDateTime);
                }

                // Group collections by key (date or payment cycle)
                Map<LocalDate, List<MilkCollection>> groupedCollections;
                if (type == 1) { // Consolidate — single group under toDate
                    groupedCollections = Map.of(draftAccountPosting.getToDate(), milkCollectionList);
                } else if (type == 2) { // Day-wise
                    groupedCollections = milkCollectionList.stream()
                            .collect(Collectors.groupingBy(mc -> mc.getCollectionDate().toLocalDate()));
                } else { // type == 3, Payment Cycle-wise
                    groupedCollections = milkCollectionList.stream()
                            .collect(Collectors.groupingBy(
                                    mc -> LocalDate.from(mc.getSocietyPaymentCycle().getToDate())
                            ));
                }

                List<Voucher> voucherList = new ArrayList<>();
                for (Map.Entry<LocalDate, List<MilkCollection>> entry : groupedCollections.entrySet()) {
                    LocalDate date = entry.getKey();
                    List<MilkCollection> milkCollections = entry.getValue();
                    voucherList.add(buildCollectionVoucher(
                            date, milkCollections, draftAccountPosting,
                            ledgerMappingEvent.get(0), eventsList.get(0), productMap
                    ));
                }
                return voucherList;
            } else if (draftAccountPosting.getEventType() == AppConstant.EventCode.LOCAL_MILK_SALE_CASH) {
                Map<LocalDate, List<LocalMilkSale>> groupedSale = new HashMap<>();
                List<LocalMilkSale> localMilkSaleList = new ArrayList<>();

                if (type == 3) {
                    List<SocietyPaymentCycle> cycles = societyPaymentCycleRepository.findCycles(fromDateTime, toDateTime);

                    if (cycles == null || cycles.isEmpty()) {
                        return Collections.emptyList();
                    }

                    for (SocietyPaymentCycle cycle : cycles) {
                        List<LocalMilkSale> localMilkSaleLists = localMilkSaleRepository.findBySaleDateBetween(cycle.getFromDate(), cycle.getToDate(), Sort.by("saleDate"));
                        if (localMilkSaleLists == null)
                            continue;
                        localMilkSaleList.addAll(localMilkSaleLists);
                        groupedSale.put(
                                LocalDate.from(cycle.getToDate()),
                                localMilkSaleLists);
                    }
                } else {
                    localMilkSaleList = localMilkSaleRepository.findBySaleDateBetween(
                            fromDateTime,
                            toDateTime,
                            Sort.by("saleDate")
                    );
                }


                if (type == 1) { // Consolidated
                    groupedSale = Map.of(
                            draftAccountPosting.getToDate(),
                            localMilkSaleList
                    );

                } else if (type == 2) { // type == 2 OR type == 3 → day-wise
                    groupedSale = localMilkSaleList.stream()
                            .collect(Collectors.groupingBy(
                                    mc -> mc.getSaleDate().toLocalDate(),
                                    TreeMap::new, // keeps dates sorted (optional but useful)
                                    Collectors.toList()
                            ));
                }

                if (groupedSale == null)
                    return null;

                Map<LocalDate, Map<Short, Map<MilkType, List<LocalMilkSale>>>> finalResult =
                        groupedSale.entrySet().stream()
                                .collect(Collectors.toMap(
                                        Map.Entry::getKey,
                                        entry -> entry.getValue().stream()
                                                .collect(Collectors.groupingBy(
                                                        LocalMilkSale::getPaymentMode,
                                                        Collectors.groupingBy(
                                                                LocalMilkSale::getMilkType
                                                        )
                                                ))
                                ));

                return buildLocalMilkSaleVoucher(draftAccountPosting, productMap, finalResult);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return null;
    }


    private List<Voucher> buildLocalMilkSaleVoucher(AccountPosting draftAccountPosting, Map<MilkType, Product> productMap, Map<LocalDate, Map<Short, Map<MilkType, List<LocalMilkSale>>>> localSaleDateMap) {
        try {
            List<Voucher> voucherList = new ArrayList<>();

            for (LocalDate date : localSaleDateMap.keySet()) {
                FinancialYear fy = financialYearRepository.findCurrentFinancialYear(date).orElse(null);

                Map<Short, Map<MilkType, List<LocalMilkSale>>> mapOfPaymentTypeSale = localSaleDateMap.get(date);
                for (short paymentType : mapOfPaymentTypeSale.keySet()) {

//          ------------------------------ VOUCHER ----------------------------------------------------
                    BigDecimal totalAmount = BigDecimal.ZERO;
//                  GET LEDGER MAPPING EVENT OF THE TYPE -
                    int eventCode = Integer.valueOf(String.valueOf(LOCAL_MILK_SALE).concat(String.valueOf(paymentType + 1)));
                    LedgerMappingEvent ledgerMappingEvent = ledgerMappingEventRepository.findByEventcode(eventCode).get(0);
                    Events event = eventRepository.findByEventCode(eventCode).get(0);
//
//                  CREATE VOUCHER
                    Voucher voucher = VoucherUtil.getVoucherInstance(
                            null, "", date, draftAccountPosting.getToDate(),
                            event.getEventName() + " Posting " + date,
                            ledgerMappingEvent.getVoucherType(),
                            fy.getCode(),
                            MainApp.identityDto.getSociety(),
                            MainApp.identityDto.getUnion().getCode(),
                            MainApp.identityDto.getSociety().getCode() + "01"
                    );
                    voucher.setAutoPosted(false);
                    voucher.setVoucherTransactions(new ArrayList<>());
//          ------------------------------ VOUCHER ----------------------------------------------------
                    List<VoucherTransaction> voucherTransactionList = new ArrayList<>();
                    List<VoucherSubLedger> voucherSubLedgerList = new ArrayList<>();

                    Map<Short, Map<String, BigDecimal>> typeCodeAmountMap = new HashMap<>();
                    Map<MilkType, List<LocalMilkSale>> mapOfMilkTypeSale = mapOfPaymentTypeSale.get(paymentType);
                    int txnCode = 1;
                    for (MilkType milkType : mapOfMilkTypeSale.keySet()) {
                        List<LocalMilkSale> localMilkSaleList = mapOfMilkTypeSale.get(milkType);

                        if (paymentType == 2) {
                            typeCodeAmountMap.clear();
                            totalAmount = BigDecimal.ZERO;
                            voucherSubLedgerList = new ArrayList<>();
                        }

                        totalAmount = totalAmount.add(localMilkSaleList.stream()
                                .map(LocalMilkSale::getAmount)
                                .reduce(BigDecimal.ZERO, BigDecimal::add));
                        BigDecimal milkTypeAmt = localMilkSaleList.stream().
                                filter(localMilkSale -> localMilkSale.getMilkType().getCode() == milkType.getCode())
                                .map(LocalMilkSale::getAmount)
                                .reduce(BigDecimal.ZERO, BigDecimal::add);
                        localMilkSaleList.forEach(localMilkSale ->
                                typeCodeAmountMap.computeIfAbsent(localMilkSale.getConsumerType(), k -> new HashMap<>()).merge(localMilkSale.getConsumerCode(), localMilkSale.getAmount(), BigDecimal::add)
                        );

                        Product product = productMap.get(milkType);
                        VoucherTransaction crTxn = VoucherUtil.getVoucherTxn(
                                voucher, milkTypeAmt, true,
                                product.getLocalSaleLedger(),
                                ledgerMappingEvent.getVoucherTxnCreditNarration(),
                                String.valueOf(txnCode++)
                        );
                        crTxn.setEvents(event);
                        crTxn.setVoucherSubLedgers(new ArrayList<>());
                        voucherTransactionList.add(crTxn);

                        if (paymentType == 2) {
                            VoucherTransaction drTxn = VoucherUtil.getVoucherTxn(
                                    voucher, totalAmount, false,
                                    product.getCouponLedger(),
                                    ledgerMappingEvent.getVoucherTxnDebitNarration(),
                                    String.valueOf(txnCode));
                            drTxn.setEvents(event);
                            drTxn.setVoucherSubLedgers(new ArrayList<>());

                            if (ledgerMappingEvent.getDebitSubLedger()) {
                                for (short type : typeCodeAmountMap.keySet()) {
                                    Map<String, BigDecimal> mapOfConsumerCodeAndAmt = typeCodeAmountMap.get(type);
                                    for (String consumerCode : mapOfConsumerCodeAndAmt.keySet()) {
                                        SubLedger subLedger = subLedgerRepository.findByReferenceCodeAndType(consumerCode, type).orElse(null);
                                        if (subLedger == null)
                                            throw new RuntimeException("Sub Ledger Not Found");
                                        BigDecimal amt = mapOfConsumerCodeAndAmt.get(consumerCode);

                                        VoucherSubLedger vsl = new VoucherSubLedger();
                                        vsl.setCreditDebit(false);
                                        vsl.setSubLedger(subLedger);
                                        vsl.setAmount(amt);
                                        vsl.setInitData();
                                        vsl.setNarration(ledgerMappingEvent.getVoucherNarration());
                                        vsl.setVoucher(voucher);
                                        voucherSubLedgerList.add(vsl);
                                        log.info("Consumer Type : {} , Consumer Code : {}, Amt : {}", type, consumerCode, amt);
                                    }
                                }
                                drTxn.setVoucherSubLedgers(voucherSubLedgerList);
                                voucherTransactionList.add(drTxn);
                            }
                            voucher.setVoucherTransactions(voucherTransactionList);
                        }
                    }
                    log.info("CREATE DEBIT TXN. WITH AMT {}", totalAmount);
                    Ledger ledger = ledgerMappingEvent.getDebitLedger();
                    if (paymentType != 2) {
                        VoucherTransaction drTxn = VoucherUtil.getVoucherTxn(
                                voucher, totalAmount, false,
                                ledger,
                                ledgerMappingEvent.getVoucherTxnDebitNarration(),
                                String.valueOf(txnCode));
                        drTxn.setEvents(event);
                        drTxn.setVoucherSubLedgers(new ArrayList<>());

                        if (ledgerMappingEvent.getDebitSubLedger()) {
                            for (short type : typeCodeAmountMap.keySet()) {
                                Map<String, BigDecimal> mapOfConsumerCodeAndAmt = typeCodeAmountMap.get(type);
                                for (String consumerCode : mapOfConsumerCodeAndAmt.keySet()) {
                                    SubLedger subLedger = subLedgerRepository.findByReferenceCodeAndType(consumerCode, type).orElse(null);
                                    if (subLedger == null)
                                        throw new RuntimeException("Sub Ledger Not Found");
                                    BigDecimal amt = mapOfConsumerCodeAndAmt.get(consumerCode);

                                    VoucherSubLedger vsl = new VoucherSubLedger();
                                    vsl.setCreditDebit(false);
                                    vsl.setSubLedger(subLedger);
                                    vsl.setAmount(amt);
                                    vsl.setInitData();
                                    vsl.setNarration(ledgerMappingEvent.getVoucherNarration());
                                    vsl.setVoucher(voucher);
                                    voucherSubLedgerList.add(vsl);
                                    log.info("Consumer Type : {} , Consumer Code : {}, Amt : {}", type, consumerCode, amt);
                                }
                            }
                        }

                        drTxn.setVoucherSubLedgers(voucherSubLedgerList);
                        voucherTransactionList.add(drTxn);
                        voucher.setVoucherTransactions(voucherTransactionList);
                    }
                    voucherList.add(voucher);
                }
            }
            return voucherList;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    // --------------------------------------------------
// Extracted: builds one voucher for a group of milk collections
// --------------------------------------------------
    private Voucher buildCollectionVoucher(LocalDate date, List<MilkCollection> milkCollections, AccountPosting draftAccountPosting,
                                           LedgerMappingEvent ledgerMappingEvent, Events event, Map<MilkType, Product> productMap) {
        FinancialYear fy = financialYearRepository.findCurrentFinancialYear(date).orElse(null);

        // Aggregate amounts per member (for sub-ledger) and per milk type (for debit txns)
        Map<String, BigDecimal> memberAmountMap = new HashMap<>();
        Map<MilkType, BigDecimal> milkTypeAmountMap = new HashMap<>();
        BigDecimal totalAmount = BigDecimal.ZERO;

        for (MilkCollection mc : milkCollections) {
            BigDecimal amount = mc.getAmount();
            memberAmountMap.merge(mc.getMember().getCode(), amount, BigDecimal::add);
            milkTypeAmountMap.merge(mc.getMilkType(), amount, BigDecimal::add);
            totalAmount = totalAmount.add(amount);
        }

        // Create voucher
        Voucher voucher = VoucherUtil.getVoucherInstance(
                null, "", date, draftAccountPosting.getToDate(),
                event.getEventName() + " Posting " + date,
                ledgerMappingEvent.getVoucherType(),
                fy.getCode(),
                MainApp.identityDto.getSociety(),
                MainApp.identityDto.getUnion().getCode(),
                MainApp.identityDto.getSociety().getCode() + "01"
        );
        voucher.setAutoPosted(false);
        voucher.setVoucherTransactions(new ArrayList<>());

        List<VoucherTransaction> voucherTransactionList = new ArrayList<>();

        // Debit transactions — one per milk type
        int txnCode = 1;
        for (Map.Entry<MilkType, BigDecimal> entry : milkTypeAmountMap.entrySet()) {
            VoucherTransaction txn = VoucherUtil.getVoucherTxn(
                    voucher, entry.getValue(), false,
                    productMap.get(entry.getKey()).getPurchaseLedger(),
                    ledgerMappingEvent.getVoucherTxnDebitNarration(),
                    String.valueOf(txnCode++)
            );
            txn.setVoucherSubLedgers(new ArrayList<>());
            txn.setEvents(event);
            voucherTransactionList.add(txn);
        }

        // Credit transaction with sub-ledgers
        VoucherTransaction crTxn = VoucherUtil.getVoucherTxn(
                voucher, totalAmount, true,
                ledgerMappingEvent.getCreditLedger(),
                ledgerMappingEvent.getVoucherTxnCreditNarration(),
                String.valueOf(txnCode)
        );
        crTxn.setEvents(event);

        List<String> memberCodes = new ArrayList<>(memberAmountMap.keySet());
        List<SubLedger> subLedgers = subLedgerRepository.findAllByTypeAndReferenceCodeIn((short) 1, memberCodes);
        List<VoucherSubLedger> voucherSubLedgerList = new ArrayList<>();
        for (SubLedger subLedger : subLedgers) {
            VoucherSubLedger vsl = new VoucherSubLedger();
            vsl.setCreditDebit(true);
            vsl.setSubLedger(subLedger);
            vsl.setAmount(memberAmountMap.get(subLedger.getReferenceCode()));
            vsl.setInitData();
            vsl.setNarration(ledgerMappingEvent.getVoucherNarration());
            vsl.setVoucher(voucher);
            voucherSubLedgerList.add(vsl);
        }
        crTxn.setVoucherSubLedgers(voucherSubLedgerList);
        voucherTransactionList.add(crTxn);

        voucher.setVoucherTransactions(voucherTransactionList);
        return voucher;
    }


    private void validate(AccountPosting accountPosting) {
        if (!accountPostingRepository.findOverlappingPostings(accountPosting.getFromDate(), accountPosting.getFromShift(), accountPosting.getToDate(), accountPosting.getToShift(), accountPosting.getEventType(), accountPosting.getCode()).isEmpty())
            throw new RuntimeException("posting.already.exists");
        if (accountPostingRepository.existsByStatusAndEventTypeAndCodeNotExist((short) 1, accountPosting.getEventType(), accountPosting.getCode())) {
            throw new RuntimeException("one.draft.posting.exist");
        }
    }

    @Transactional
    public AccountPosting saveAccountPosting(AccountPosting accountPosting, List<Voucher> voucherList) {
        try {
            validate(accountPosting);
            boolean isDraft = accountPosting.getStatus() == 1;
            ObjectMapper objectMapper = Jackson2ObjectMapperBuilder.json().build();
            String tableName = "VoucherSubLedger";
            if (isDraft) tableName = "VoucherSubLedgerRaw";

            if (accountPosting.getCode() == null) {
                accountPosting.setCode(nextCodeService.getNextCode("AccountPosting", "code", MainApp.identityDto.getSociety().getCode(), 0));
            }
            accountPosting = accountPostingRepository.customSave(accountPosting, CommonUtils.setIdentityHeader());

            String code = "";
            VoucherRaw voucherRaw = null;

            for (Voucher voucher : voucherList) {
                voucher.setProcessReference(accountPosting.getCode());
                voucher.setProcessName(accountPosting.getEventType() == 101 ? "MILK COLLECTION" : "LOCAL MILK SALE");
                if (isDraft) {
                    code = nextCodeService.getNextCode("VoucherRaw", "code", MainApp.identityDto.getSociety().getCode() + "/" + voucher.getFinancialYearsCode() + "/", 0);
                    voucherRaw = objectMapper.convertValue(voucher, VoucherRaw.class);
                    voucherRaw.setCode(code);
                    voucherRawRepository.save(voucherRaw);
                } else {
                    code = nextCodeService.getNextCode("Voucher", "code", MainApp.identityDto.getSociety().getCode() + "/" + voucher.getFinancialYearsCode() + "/", 0);
                    voucher.setCode(code);
                    voucherRepository.customSave(voucher, CommonUtils.setIdentityHeader());

//-------------------- DELETE RAW VOUCHER, TRANSACTION, SUB LEDGER ON FINAL POSTING. ------------------
                    List<VoucherRaw> voucherRaws = voucherRawRepository.findByProcessReference(accountPosting.getCode());
                    if (!voucherRaws.isEmpty()) {

                        List<VoucherSubLedgerRaw> voucherSubLedgerRaws = new ArrayList<>();
                        List<VoucherTransactionRaw> voucherTransactionRaws = new ArrayList<>();

                        for (VoucherRaw voucherRaw1 : voucherRaws) {
                            voucherSubLedgerRaws.addAll(voucherRaw1.getVoucherSubLedgerRawList());
                            voucherTransactionRaws.addAll(voucherRaw1.getVoucherTransactionRawList());
                        }
                        if (!voucherSubLedgerRaws.isEmpty())
                            voucherSubLedgerRawRepository.deleteAll(voucherSubLedgerRaws);

                        if (!voucherTransactionRaws.isEmpty())
                            voucherTransactionRawRepository.deleteAll(voucherTransactionRaws);

                        voucherRawRepository.deleteAll(voucherRaws);
                    }
//            -------------------------------------------------------------------------
                }
                VoucherTransactionRaw txnRaw = null;
                int txnCode = 1;
                for (VoucherTransaction txn : voucher.getVoucherTransactions()) {
                    if (isDraft) {
                        txnRaw = objectMapper.convertValue(txn, VoucherTransactionRaw.class);
                        txnRaw.setVoucherRaw(voucherRaw);
                        txnRaw.setCode(voucherRaw.getCode() + "T" + txnCode++);
                        txnRaw = voucherTransactionRawRepository.save(txnRaw);
                    } else {
                        txn.setVoucher(voucher);
                        txn.setCode(voucher.getCode() + "T" + txnCode++);
                        voucherTransactionRepository.customSave(txn, CommonUtils.setIdentityHeader());
                    }

                    for (VoucherSubLedger vsl : txn.getVoucherSubLedgers()) {
                        code = nextCodeService.getNextCode(tableName, "code", MainApp.identityDto.getSociety().getCode(), 0);
                        if (isDraft) {
                            VoucherSubLedgerRaw raw = objectMapper.convertValue(vsl, VoucherSubLedgerRaw.class);
                            raw.setCode(code);
                            raw.setVoucherRaw(voucherRaw);
                            raw.setVoucherTransactionRaw(txnRaw);
                            voucherSubLedgerRawRepository.save(raw);
                        } else {
                            vsl.setCode(code);
                            vsl.setVoucher(voucher);
                            vsl.setVoucherTransaction(txn);
                            voucherSubLedgerRepository.customSave(vsl, CommonUtils.setIdentityHeader());
                        }
                    }
                }
            }
            return accountPosting;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}