package com.eipl.amcs.setting.service;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.service.NextCodeService;
import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.account.model.*;
import com.eipl.amcs.master.account.repository.*;
import com.eipl.amcs.master.global.repository.ShiftRepository;
import com.eipl.amcs.master.procurement.model.SocietyPaymentCycle;
import com.eipl.amcs.master.procurement.repository.SocietyPaymentCycleRepository;
import com.eipl.amcs.operation.procurement.model.LocalMilkSale;
import com.eipl.amcs.operation.procurement.model.MilkCollection;
import com.eipl.amcs.operation.procurement.repository.LocalMilkSaleRepository;
import com.eipl.amcs.operation.procurement.repository.MilkCollectionRepository;
import com.eipl.amcs.operation.procurement.service.LocalMilkSaleService;
import com.eipl.amcs.operation.procurement.service.MilkCollectionService;
import com.eipl.amcs.setting.dto.AccountPostingDto;
import com.eipl.amcs.setting.dto.CustomerTypeWiseTotalDto;
import com.eipl.amcs.setting.model.AccountPosting;
import com.eipl.amcs.setting.repository.AccountPostingRepository;
import com.eipl.amcs.utils.AppConstant;
import com.eipl.amcs.utils.CommonUtils;
import com.eipl.amcs.utils.VoucherUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Getter;
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
    private ShiftRepository shiftRepository;
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

//    @Override
//    @Transactional(rollbackFor = Exception.class)
//    public AccountPosting save(AccountPosting accountPosting, List<AccountPostingDto> accountPostingDtoList) {
//        try {
//            validate(accountPosting);
//            createAccountPosting(accountPosting, accountPostingDtoList);
//        } catch (Exception e) {
//            log.error(e.getMessage());
//            throw new RuntimeException(e);
//        }
//        return null;
//    }

    private void validate(AccountPosting accountPosting) {
        if (!accountPostingRepository.findOverlappingPostings(accountPosting.getFromDate(), accountPosting.getFromShift(), accountPosting.getToDate(), accountPosting.getToShift(), accountPosting.getEventType(), (short) 2).isEmpty())
            throw new RuntimeException("posting.already.exists");
    }

    private void createAccountPosting(AccountPosting accountPosting, List<AccountPostingDto> accountPostingDtoList) {
        try {
            validate(accountPosting);

            List<LedgerMappingEvent> eventsList = ledgerMappingEventRepository.findByEventcode(AppConstant.EventCode.MILK_COLLECTION);
            if (eventsList == null || eventsList.isEmpty()) return;

            for (AccountPostingDto accountPostingDto : accountPostingDtoList) {

                Optional<FinancialYear> financialYear = financialYearRepository.findCurrentFinancialYear(accountPostingDto.getDate());


//                VOUCHER -------------
                String voucherCode = nextCodeService.getNextCode("Voucher", "code", MainApp.identityDto.getSociety().getCode() + "/" + financialYear.get().getCode() + "/", 0);
                if (voucherCode == null) continue;

                Voucher voucher = VoucherUtil.getVoucherInstance(voucherCode, "", accountPostingDto.getDate(), accountPosting.getToDate(), "Milk Collection Manual Posting" + accountPostingDto.getDate(), eventsList.get(0).getVoucherType(), financialYear.orElse(null).getCode(), MainApp.identityDto.getSociety(), MainApp.identityDto.getUnion().getCode(), MainApp.identityDto.getSociety().getCode() + "01");
                voucher.setAutoPosted(false);
                voucher.setVoucherTransactions(new ArrayList<>());

//              VOUCHER TRANSACTION ----------
                VoucherTransaction creditTxn = VoucherUtil.getVoucherTxn(voucher, accountPostingDto.getCreditAmount(), true, eventsList.get(0).getCreditLedger(), "Milk Collection Credit " + accountPostingDto.getCreditAmount(), "1");
                voucher.getVoucherTransactions().add(creditTxn);
                creditTxn.setVoucherSubLedgers(new ArrayList<>());

                VoucherTransaction debitTxn = VoucherUtil.getVoucherTxn(voucher, accountPostingDto.getDebitAmount(), false, eventsList.get(0).getDebitLedger(), "Milk Collection Debit " + accountPostingDto.getDebitAmount(), "2");
                voucher.getVoucherTransactions().add(debitTxn);
                debitTxn.setVoucherSubLedgers(new ArrayList<>());


//              VOUCHER SUB LEDGER ----------
                List<MilkCollection> milkCollectionList = new ArrayList<>();

                if (accountPosting.getPostingType() == 1) { // consolidate
                    milkCollectionList = milkCollectionService.findAllBetween(CommonUtils.getLocalDateTimeFromDateAndShift(accountPosting.getFromDate(), accountPosting.getFromShift()), CommonUtils.getLocalDateTimeFromDateAndShift(accountPosting.getToDate(), accountPosting.getToShift()));
                } else { // day wise
                    milkCollectionList = milkCollectionService.findAllBetween(CommonUtils.getLocalDateTimeFromDateAndShift(accountPostingDto.getDate(), shiftRepository.findById(1).get()), CommonUtils.getLocalDateTimeFromDateAndShift(accountPostingDto.getDate(), shiftRepository.findById(2).get())); // Whole day collection
                }

                Set<String> memberCodes = milkCollectionList.stream().map(m -> m.getMember().getCode()).filter(Objects::nonNull).collect(Collectors.toSet());

                List<SubLedger> subLedgerList = subLedgerRepository.findAllByTypeAndReferenceCodeIn((short) 1, new ArrayList<>(memberCodes));

                long voucherSubLedgerCode = Long.valueOf(nextCodeService.getNextCode("VoucherSubLedger", "code", MainApp.identityDto.getSociety().getCode(), 0));

                Map<String, BigDecimal> memberPeriodTotals = milkCollectionList.stream().collect(Collectors.groupingBy(m -> m.getMember().getCode(), Collectors.reducing(BigDecimal.ZERO, MilkCollection::getAmount, BigDecimal::add)));

                for (String memberCode : memberCodes) {
                    VoucherSubLedger voucherSubLedger = new VoucherSubLedger();
                    voucherSubLedger.setCode(String.valueOf(voucherSubLedgerCode));
                    voucherSubLedger.setVoucher(voucher);
                    voucherSubLedger.setCreditDebit(eventsList.get(0).getCreditSubLedger());
                    voucherSubLedger.setVoucherTransaction(eventsList.get(0).getCreditSubLedger() ? creditTxn : debitTxn);

                    voucherSubLedger.setSubLedger(subLedgerList.stream().filter(sb -> sb.getReferenceCode().equals(memberCode)).findFirst().orElse(null));
                    voucherSubLedger.setAmount(memberPeriodTotals.get(memberCode));

                    if (eventsList.get(0).getCreditSubLedger()) {
                        creditTxn.getVoucherSubLedgers().add(voucherSubLedger);
                    } else {
                        debitTxn.getVoucherSubLedgers().add(voucherSubLedger);
                    }

                    voucherSubLedgerCode++;
                }

                voucherRepository.customSave(voucher, CommonUtils.setIdentityHeader());
                if (!voucher.getVoucherTransactions().isEmpty()) {
                    for (VoucherTransaction voucherTransaction : voucher.getVoucherTransactions()) {
                        voucherTransactionRepository.customSave(voucherTransaction, CommonUtils.setIdentityHeader());
                        if (voucherTransaction.getVoucherSubLedgers() != null && !voucherTransaction.getVoucherSubLedgers().isEmpty()) {
                            for (VoucherSubLedger voucherSubLedger : voucherTransaction.getVoucherSubLedgers()) {
                                voucherSubLedgerRepository.customSave(voucherSubLedger, CommonUtils.setIdentityHeader());
                            }
                        }
                    }
                }
            }
            accountPosting.setCode(nextCodeService.getNextCode("AccountPosting", "code", MainApp.identityDto.getSociety().getCode(), 0));
            accountPostingRepository.customSave(accountPosting, CommonUtils.setIdentityHeader());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public List<AccountPostingDto> loadMilkCollectionAccountPosting(int type, LocalDateTime fromDateTime, LocalDateTime toDateTime) {
        try {

            MilkCollectionService milkCollectionService = EmcsAppContext.getContext().getBean(MilkCollectionService.class);
            MilkCollectionRepository milkCollectionRepository = EmcsAppContext.getContext().getBean(MilkCollectionRepository.class);
            List<MilkCollection> milkCollectionList = new ArrayList<>();

            List<LedgerMappingEvent> ledgerMappingEventsList = ledgerMappingEventRepository.findByEventcode(AppConstant.EventCode.MILK_COLLECTION);

            List<AccountPostingDto> accountPostingDtoList = new ArrayList<>();
            AccountPostingDto debitAccountPostingDto = null;
            AccountPostingDto creditAccountPostingDto = null;

            Map<LocalDate, BigDecimal> dateWiseTotal = new HashMap<>();

            if (type == 1) { // consolidate
                milkCollectionList = milkCollectionService.findAllBetween(fromDateTime, toDateTime);

                dateWiseTotal.put(LocalDate.now(), milkCollectionList.stream().map(MilkCollection::getAmount).filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add));

            } else if (type == 2) {// for day wise
                milkCollectionList = milkCollectionService.findAllBetween(fromDateTime, toDateTime);

                dateWiseTotal = milkCollectionList.stream().filter(item -> !item.getCollectionDate().isBefore(fromDateTime) && !item.getCollectionDate().isAfter(toDateTime)).collect(Collectors.groupingBy(item -> item.getCollectionDate().toLocalDate(), TreeMap::new, Collectors.reducing(BigDecimal.ZERO, MilkCollection::getAmount, BigDecimal::add)));
            } else if (type == 3) { // Payment Cycle wise
                SocietyPaymentCycleRepository societyPaymentCycleRepository = EmcsAppContext.getContext().getBean(SocietyPaymentCycleRepository.class);
                List<SocietyPaymentCycle> societyPaymentCycleList = societyPaymentCycleRepository.findCycles(fromDateTime, toDateTime);
                if (societyPaymentCycleList == null) {
                    return null;
                }
                milkCollectionList = milkCollectionRepository.findBySocietyPaymentCycleInOrderByCollectionDateAsc(societyPaymentCycleList);

                dateWiseTotal = milkCollectionList.stream().filter(item -> !item.getCollectionDate().isBefore(fromDateTime) && !item.getCollectionDate().isAfter(toDateTime)).collect(Collectors.groupingBy(item -> item.getSocietyPaymentCycle().getToDate().toLocalDate(), TreeMap::new, Collectors.reducing(BigDecimal.ZERO, MilkCollection::getAmount, BigDecimal::add)));

            }

            for (LocalDate paymentCycleToDate : dateWiseTotal.keySet()) {
                BigDecimal creditAmount = new BigDecimal(0);
                BigDecimal debitAmount = new BigDecimal(0);
                Boolean isCreditLedger = ledgerMappingEventsList.get(0).getEvents().getLedgerCredit();
                if (isCreditLedger) {
                    creditAmount = dateWiseTotal.get(paymentCycleToDate);
                } else {
                    debitAmount = dateWiseTotal.get(paymentCycleToDate);
                }
                creditAccountPostingDto = new AccountPostingDto(ledgerMappingEventsList.get(0), paymentCycleToDate, (short) 1, ledgerMappingEventsList.get(0).getCreditLedger(), ledgerMappingEventsList.get(0).getVoucherNarration(), isCreditLedger, creditAmount, debitAmount);
                debitAccountPostingDto = new AccountPostingDto(ledgerMappingEventsList.get(0), paymentCycleToDate, (short) 1, ledgerMappingEventsList.get(0).getDebitLedger(), ledgerMappingEventsList.get(0).getVoucherNarration(), !isCreditLedger, debitAmount, creditAmount);
                accountPostingDtoList.add(creditAccountPostingDto);
                accountPostingDtoList.add(debitAccountPostingDto);
            }
            return accountPostingDtoList;

        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;
    }

    public List<AccountPostingDto> loadLocalMilkSaleAccountPosting(int type, LocalDateTime fromDateTime, LocalDateTime toDateTime) {
        try {

            LocalMilkSaleService localMilkSaleService = EmcsAppContext.getContext().getBean(LocalMilkSaleService.class);
            LocalMilkSaleRepository localMilkSaleRepository = EmcsAppContext.getContext().getBean(LocalMilkSaleRepository.class);
//            MilkCollectionRepository milkCollectionRepository = EmcsAppContext.getContext().getBean(MilkCollectionRepository.class);
            List<LocalMilkSale> localMilkSaleList = localMilkSaleService.findAll(fromDateTime, toDateTime);

            List<LedgerMappingEvent> ledgerMappingEventsList = ledgerMappingEventRepository.findByEventcode(AppConstant.EventCode.LOCAL_MILK_SALE);

            List<AccountPostingDto> accountPostingDtoList = new ArrayList<>();
            AccountPostingDto debitAccountPostingDto = null;
            AccountPostingDto creditAccountPostingDto = null;

            Map<LocalDate, BigDecimal> dateWiseTotal = new HashMap<>();

            if (type == 1) { // consolidate
                dateWiseTotal.put(LocalDate.now(), localMilkSaleList.stream().map(LocalMilkSale::getAmount).filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add));

            } else if (type == 2) {// for day wise

                dateWiseTotal = localMilkSaleList.stream().filter(item -> !item.getSaleDate().isBefore(fromDateTime) && !item.getSaleDate().isAfter(toDateTime)).collect(Collectors.groupingBy(item -> item.getSaleDate().toLocalDate(), TreeMap::new, Collectors.reducing(BigDecimal.ZERO, LocalMilkSale::getAmount, BigDecimal::add)));
            } else if (type == 3) { // Payment Cycle wise
                SocietyPaymentCycleRepository societyPaymentCycleRepository = EmcsAppContext.getContext().getBean(SocietyPaymentCycleRepository.class);
                List<SocietyPaymentCycle> societyPaymentCycleList = societyPaymentCycleRepository.findCycles(fromDateTime, toDateTime);
                if (societyPaymentCycleList == null) {
                    return null;
                }
                for (SocietyPaymentCycle societyPaymentCycle : societyPaymentCycleList) {
                    BigDecimal total = localMilkSaleRepository.findLocalSaleAmountBetween(societyPaymentCycle.getFromDate(), societyPaymentCycle.getToDate());
                    if (total.compareTo(BigDecimal.ZERO) > 0)
                        dateWiseTotal.put(societyPaymentCycle.getToDate().toLocalDate(), localMilkSaleRepository.findLocalSaleAmountBetween(societyPaymentCycle.getFromDate(), societyPaymentCycle.getToDate()));
                }
            }
            for (LocalDate paymentCycleToDate : dateWiseTotal.keySet()) {
                BigDecimal creditAmount = new BigDecimal(0);
                BigDecimal debitAmount = new BigDecimal(0);
                Boolean isCreditLedger = ledgerMappingEventsList.get(0).getEvents().getLedgerCredit();
                if (isCreditLedger) {
                    creditAmount = dateWiseTotal.get(paymentCycleToDate);
                } else {
                    debitAmount = dateWiseTotal.get(paymentCycleToDate);
                }
                creditAccountPostingDto = new AccountPostingDto(ledgerMappingEventsList.get(0), paymentCycleToDate, (short) 1, ledgerMappingEventsList.get(0).getCreditLedger(), ledgerMappingEventsList.get(0).getVoucherNarration(), isCreditLedger, creditAmount, debitAmount);
                debitAccountPostingDto = new AccountPostingDto(ledgerMappingEventsList.get(0), paymentCycleToDate, (short) 1, ledgerMappingEventsList.get(0).getDebitLedger(), ledgerMappingEventsList.get(0).getVoucherNarration(), !isCreditLedger, debitAmount, creditAmount);
                accountPostingDtoList.add(creditAccountPostingDto);
                accountPostingDtoList.add(debitAccountPostingDto);
            }
            return accountPostingDtoList;

        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AccountPosting save(AccountPosting accountPosting, List<AccountPostingDto> dtoList) {

        try {
            validate(accountPosting);

            ObjectMapper objectMapper = Jackson2ObjectMapperBuilder.json().build();
            boolean isDraft = accountPosting.getStatus() == 1;


            LedgerMappingEvent event = ledgerMappingEventRepository.findByEventcode(accountPosting.getEventType()).get(0);
            if (event == null) return null;

            LocalDate postingDate = null;
            int txnCode = 1;
            VoucherWrapper wrapper = null;

//          DON'T CREATE NEW ACCOUNT POSTING ON EDITING, JUST CREATE NEW RAW DATA
            if (accountPosting.getCode() == null) {
                accountPosting.setCode(nextCodeService.getNextCode("AccountPosting", "code", MainApp.identityDto.getSociety().getCode(), 0));
            }
            accountPosting = accountPostingRepository.customSave(accountPosting, CommonUtils.setIdentityHeader());

//          CREATE NEW RAW DATA ON SAVE OR EDITING OF ACCOUNT POSTING IF DRAFTING.
            for (AccountPostingDto dto : dtoList) {

                FinancialYear fy = financialYearRepository.findCurrentFinancialYear(dto.getDate()).orElse(null);

                if (fy == null) continue;
                if (!dto.getDate().equals(postingDate)) {
                    wrapper = createVoucher(accountPosting, dto, fy, event, objectMapper, isDraft);
                    postingDate = dto.getDate();
                    txnCode = 1;
                }

                VoucherTransactionWrapper txnWrapper = createTransaction(dto, event, wrapper, txnCode, objectMapper, isDraft);
                txnCode++;

                if (!((event.getCreditSubLedger() && dto.isCredit_debit()) || (event.getDebitSubLedger() && !dto.isCredit_debit())))
                    continue;
                List<SubLedger> subLedgers = new ArrayList<>();
                Map<String, BigDecimal> memberTotals = new HashMap<>();
                List<CustomerTypeWiseTotalDto> result = new ArrayList<>();
                if (accountPosting.getEventType() == AppConstant.EventCode.MILK_COLLECTION) {
                    List<MilkCollection> milkList = fetchMilkData(accountPosting, dto);

                    if (milkList.isEmpty()) continue;

                    result = milkList.stream().collect(Collectors.groupingBy(m -> m.getMember().getCode(), Collectors.summingDouble(m -> m.getAmount().doubleValue()))).entrySet().stream().map(entry -> {
                        CustomerTypeWiseTotalDto memberTypeWiseTotalDto = new CustomerTypeWiseTotalDto();
                        memberTypeWiseTotalDto.setCustomerCode(entry.getKey());
                        memberTypeWiseTotalDto.setAmount(BigDecimal.valueOf(entry.getValue()));
                        memberTypeWiseTotalDto.setCustomerType("1");
                        return memberTypeWiseTotalDto;
                    }).collect(Collectors.toList());

                    subLedgers = subLedgerRepository.findAllByTypeAndReferenceCodeIn((short) 1, new ArrayList<>(memberTotals.keySet()));
                } else if (accountPosting.getEventType() == AppConstant.EventCode.LOCAL_MILK_SALE) {
                    List<LocalMilkSale> localMilkSaleList = fetchLocalMilkSaleData(accountPosting, dto);
                    result = localMilkSaleList.stream().collect(Collectors.groupingBy(LocalMilkSale::getConsumerCode, Collectors.groupingBy(LocalMilkSale::getConsumerType, Collectors.reducing(BigDecimal.ZERO, LocalMilkSale::getAmount, BigDecimal::add)))).entrySet().stream().flatMap(consumerEntry -> consumerEntry.getValue().entrySet().stream().map(typeEntry -> {
                        CustomerTypeWiseTotalDto consumerDto = new CustomerTypeWiseTotalDto();
                        consumerDto.setCustomerCode(consumerEntry.getKey());
                        consumerDto.setCustomerType(String.valueOf(typeEntry.getKey()));
                        consumerDto.setAmount(typeEntry.getValue());
                        return consumerDto;
                    })).collect(Collectors.toList());

                    List<String> customerCodes = result.stream().map(CustomerTypeWiseTotalDto::getCustomerCode).filter(Objects::nonNull).distinct().collect(Collectors.toList());

                    subLedgers = subLedgerRepository.findAllByReferenceCodeIn(customerCodes);

                }
                saveSubLedgers(result, subLedgers, wrapper, txnWrapper, event, objectMapper, isDraft);
            }
            return accountPosting;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }


    private VoucherWrapper createVoucher(AccountPosting accountPosting, AccountPostingDto dto, FinancialYear fy, LedgerMappingEvent event, ObjectMapper objectMapper, boolean isDraft) {

        String code = "";
        Voucher voucher = VoucherUtil.getVoucherInstance(null, "", dto.getDate(), accountPosting.getToDate(), event.getEvents().getEventName() + " Posting " + dto.getDate(), event.getVoucherType(), fy.getCode(), MainApp.identityDto.getSociety(), MainApp.identityDto.getUnion().getCode(), MainApp.identityDto.getSociety().getCode() + "01");

        voucher.setXCol5(accountPosting.getCode());
        voucher.setAutoPosted(false);
        voucher.setVoucherTransactions(new ArrayList<>());

        VoucherRaw voucherRaw = null;

        if (isDraft) {
            code = nextCodeService.getNextCode("VoucherRaw", "code", MainApp.identityDto.getSociety().getCode() + "/" + fy.getCode() + "/", 0);
            voucherRaw = objectMapper.convertValue(voucher, VoucherRaw.class);
            voucherRaw.setCode(code);
            voucherRaw = voucherRawRepository.save(voucherRaw);
        } else {
            code = nextCodeService.getNextCode("Voucher", "code", MainApp.identityDto.getSociety().getCode() + "/" + fy.getCode() + "/", 0);
            voucher.setCode(code);
            voucher = voucherRepository.customSave(voucher, CommonUtils.setIdentityHeader());

//-------------------- DELETE RAW VOUCHER, TRANSACTION, SUB LEDGER ON FINAL POSTING. ------------------
            List<VoucherRaw> voucherRaws = voucherRawRepository.findByXCol5(accountPosting.getCode());
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

        return new VoucherWrapper(voucher, voucherRaw);
    }

    private VoucherTransactionWrapper createTransaction(AccountPostingDto dto, LedgerMappingEvent event, VoucherWrapper voucherWrapper, int txnCode, ObjectMapper objectMapper, boolean isDraft) {

        boolean isCredit = dto.isCredit_debit();

        BigDecimal amount = isCredit ? dto.getCreditAmount() : dto.getDebitAmount();
        Ledger ledger = isCredit ? event.getCreditLedger() : event.getDebitLedger();

        String narration = isCredit ? dto.getLedgerMappingEvent().getVoucherTxnCreditNarration() : dto.getLedgerMappingEvent().getVoucherTxnDebitNarration();

        Voucher voucher = voucherWrapper.getVoucher();
        if (isDraft)
            voucher.setCode(voucherWrapper.getVoucherRaw().getCode());

        VoucherTransaction txn = VoucherUtil.getVoucherTxn(voucher, amount, isCredit, ledger, narration, String.valueOf(txnCode));

        txn.setVoucherSubLedgers(new ArrayList<>());

        VoucherTransactionRaw txnRaw = null;

        if (isDraft) {
            txnRaw = objectMapper.convertValue(txn, VoucherTransactionRaw.class);
            txnRaw.setVoucherRaw(voucherWrapper.getVoucherRaw());
            txnRaw = voucherTransactionRawRepository.save(txnRaw);
        } else {
            txn.setVoucher(voucher);
            txn = voucherTransactionRepository.customSave(txn, CommonUtils.setIdentityHeader());
        }

        return new VoucherTransactionWrapper(txn, txnRaw);
    }


    private List<MilkCollection> fetchMilkData(AccountPosting posting, AccountPostingDto dto) {

        if (posting.getEventType() != AppConstant.EventCode.MILK_COLLECTION) return Collections.emptyList();

        if (posting.getPostingType() == 1) {
            return milkCollectionService.findAllBetween(CommonUtils.getLocalDateTimeFromDateAndShift(posting.getFromDate(), posting.getFromShift()), CommonUtils.getLocalDateTimeFromDateAndShift(posting.getToDate(), posting.getToShift()));
        }

        if (posting.getPostingType() == 2) {
            return milkCollectionService.findAllBetween(CommonUtils.getLocalDateTimeFromDateAndShift(dto.getDate(), shiftRepository.findById(1).orElseThrow()), CommonUtils.getLocalDateTimeFromDateAndShift(dto.getDate(), shiftRepository.findById(2).orElseThrow()));
        }

        if (posting.getPostingType() == 3) {
            List<SocietyPaymentCycle> cycles = societyPaymentCycleRepository.findCycles(CommonUtils.getLocalDateTimeFromDateAndShift(posting.getFromDate(), posting.getFromShift()), CommonUtils.getLocalDateTimeFromDateAndShift(posting.getToDate(), posting.getToShift()));

            if (cycles == null) return Collections.emptyList();

            List<SocietyPaymentCycle> filtered = cycles.stream().filter(c -> c.getToDate() != null && c.getToDate().toLocalDate().equals(dto.getDate())).collect(Collectors.toList());

            return milkCollectionRepository.findBySocietyPaymentCycleInOrderByCollectionDateAsc(filtered);
        }

        return Collections.emptyList();
    }

    private List<LocalMilkSale> fetchLocalMilkSaleData(AccountPosting posting, AccountPostingDto dto) {
        List<LocalMilkSale> localMilkSaleList = new ArrayList<>();

        if (posting.getEventType() != AppConstant.EventCode.LOCAL_MILK_SALE) return Collections.emptyList();

        if (posting.getPostingType() == 1) {
            return localMilkSaleRepository.findBySaleDateBetween(CommonUtils.getLocalDateTimeFromDateAndShift(posting.getFromDate(), posting.getFromShift()), CommonUtils.getLocalDateTimeFromDateAndShift(posting.getToDate(), posting.getToShift()), Sort.by("saleDate"));
        } else if (posting.getPostingType() == 2) {
            return localMilkSaleRepository.findBySaleDateBetween(CommonUtils.getLocalDateTimeFromDateAndShift(dto.getDate(), shiftRepository.findById(1).orElseThrow()), CommonUtils.getLocalDateTimeFromDateAndShift(dto.getDate(), shiftRepository.findById(2).orElseThrow()), Sort.by("saleDate"));
        } else if (posting.getPostingType() == 3) {
            List<SocietyPaymentCycle> cycles = societyPaymentCycleRepository.findCycles(CommonUtils.getLocalDateTimeFromDateAndShift(posting.getFromDate(), posting.getFromShift()), CommonUtils.getLocalDateTimeFromDateAndShift(posting.getToDate(), posting.getToShift()));

            if (cycles == null) return Collections.emptyList();

            List<SocietyPaymentCycle> filtered = cycles.stream().filter(c -> c.getToDate() != null && c.getToDate().toLocalDate().equals(dto.getDate())).collect(Collectors.toList());
            for (SocietyPaymentCycle societyPaymentCycle : filtered) {
                localMilkSaleList.addAll(localMilkSaleRepository.findBySaleDateBetween(societyPaymentCycle.getFromDate(), societyPaymentCycle.getToDate(), Sort.by("saleDate")));
            }
            return localMilkSaleList;

        }
        return Collections.emptyList();
    }

    private void saveSubLedgers(List<CustomerTypeWiseTotalDto> memberTypeWiseTotalDtos, List<SubLedger> subLedgers, VoucherWrapper voucherWrapper, VoucherTransactionWrapper txnWrapper, LedgerMappingEvent event, ObjectMapper objectMapper, boolean isDraft) {

        for (CustomerTypeWiseTotalDto memberTypeWiseTotalDto : memberTypeWiseTotalDtos) {
            String tableName = "VoucherSubLedger";
            if (isDraft) tableName = "VoucherSubLedgerRaw";

            String code = nextCodeService.getNextCode(tableName, "code", MainApp.identityDto.getSociety().getCode(), 0);
            SubLedger subLedger = subLedgers.stream().filter(s -> s.getReferenceCode().equals(memberTypeWiseTotalDto.getCustomerCode()) && String.valueOf(s.getType()).equals(memberTypeWiseTotalDto.getCustomerType())).findFirst().orElse(null);

            VoucherSubLedger vsl = new VoucherSubLedger();
            vsl.setCode(code);
            vsl.setCreditDebit(event.getCreditSubLedger());
            vsl.setSubLedger(subLedger);
            vsl.setAmount(memberTypeWiseTotalDto.getAmount());

            if (isDraft) {
                VoucherSubLedgerRaw raw = objectMapper.convertValue(vsl, VoucherSubLedgerRaw.class);
                raw.setVoucherRaw(voucherWrapper.voucherRaw);
                raw.setVoucherTransactionRaw(txnWrapper.getTxnRaw());
                voucherSubLedgerRawRepository.save(raw);
            } else {
                vsl.setVoucher(voucherWrapper.voucher);
                vsl.setVoucherTransaction(txnWrapper.getTxn());
                voucherSubLedgerRepository.customSave(vsl, CommonUtils.setIdentityHeader());
            }
        }
//        for (String memberCode : memberTotals.keySet()) {
//
//            String tableName = "VoucherSubLedger";
//            if (isDraft) tableName = "VoucherSubLedgerRaw";
//
//            String code = nextCodeService.getNextCode(tableName, "code", MainApp.identityDto.getSociety().getCode(), 0);
//            SubLedger subLedger = subLedgers.stream().filter(s -> s.getReferenceCode().equals(memberCode)).findFirst().orElse(null);
//
//            VoucherSubLedger vsl = new VoucherSubLedger();
//            vsl.setCode(code);
//            vsl.setCreditDebit(event.getCreditSubLedger());
//            vsl.setSubLedger(subLedger);
//            vsl.setAmount(memberTotals.get(memberCode));
//
//            if (isDraft) {
//                VoucherSubLedgerRaw raw = objectMapper.convertValue(vsl, VoucherSubLedgerRaw.class);
//                raw.setVoucherRaw(voucherWrapper.voucherRaw);
//                raw.setVoucherTransactionRaw(txnWrapper.getTxnRaw());
//                voucherSubLedgerRawRepository.save(raw);
//            } else {
//                vsl.setVoucher(voucherWrapper.voucher);
//                vsl.setVoucherTransaction(txnWrapper.getTxn());
//                voucherSubLedgerRepository.save(vsl);
//            }
//        }
    }

    @Getter
    @AllArgsConstructor
    static class VoucherWrapper {
        private Voucher voucher;
        private VoucherRaw voucherRaw;
    }

    @Getter
    @AllArgsConstructor
    static class VoucherTransactionWrapper {
        private VoucherTransaction txn;
        private VoucherTransactionRaw txnRaw;
    }
}