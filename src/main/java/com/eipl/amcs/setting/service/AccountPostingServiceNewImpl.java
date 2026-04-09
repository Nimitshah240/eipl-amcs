package com.eipl.amcs.setting.service;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.service.NextCodeService;
import com.eipl.amcs.master.account.model.*;
import com.eipl.amcs.master.account.repository.*;
import com.eipl.amcs.master.global.model.MilkType;
import com.eipl.amcs.master.global.repository.MilkTypeRepository;
import com.eipl.amcs.master.global.repository.ShiftRepository;
import com.eipl.amcs.master.inventory.model.Product;
import com.eipl.amcs.master.inventory.repository.ProductRepository;
import com.eipl.amcs.master.procurement.model.SocietyPaymentCycle;
import com.eipl.amcs.master.procurement.repository.SocietyPaymentCycleRepository;
import com.eipl.amcs.operation.procurement.model.MilkCollection;
import com.eipl.amcs.operation.procurement.repository.LocalMilkSaleRepository;
import com.eipl.amcs.operation.procurement.repository.MilkCollectionRepository;
import com.eipl.amcs.operation.procurement.service.MilkCollectionService;
import com.eipl.amcs.setting.dto.AccountPostingDtoNew;
import com.eipl.amcs.setting.model.AccountPosting;
import com.eipl.amcs.setting.repository.AccountPostingRepository;
import com.eipl.amcs.utils.AppConstant;
import com.eipl.amcs.utils.CommonUtils;
import com.eipl.amcs.utils.VoucherUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Slf4j
@Service
public class AccountPostingServiceNewImpl implements AccountPostingServiceNew {

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
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private MilkTypeRepository milkTypeRepository;


    @Transactional
    public List<AccountPostingDtoNew> loadAccountPostingData(AccountPosting draftAccountPosting) {
        try {
            if (draftAccountPosting.getEventType() == AppConstant.EventCode.MILK_COLLECTION) {

                int type = draftAccountPosting.getPostingType();
                List<MilkCollection> milkCollectionList = new ArrayList<>();
                LocalDateTime fromDateTime = CommonUtils.getLocalDateTimeFromDateAndShift(draftAccountPosting.getFromDate(), draftAccountPosting.getFromShift());
                LocalDateTime toDateTime = CommonUtils.getLocalDateTimeFromDateAndShift(draftAccountPosting.getToDate(), draftAccountPosting.getToShift());
                LedgerMappingEvent ledgerMappingEvent = ledgerMappingEventRepository.findByEventcode(AppConstant.EventCode.MILK_COLLECTION).get(0);
                VoucherType voucherType = ledgerMappingEvent.getVoucherType();
                Product cowProduct;
                Product mixProduct;
                Product buffProduct;
                List<Events> eventsList = eventRepository.findByEventCode(draftAccountPosting.getEventType());

                List<MilkType> milkTypeList = milkTypeRepository.findAll();

                List<Product> productList = productRepository.findAllByMilkAndMilkTypeIn(true, milkTypeList);

                Map<MilkType, Product> productMap =
                        productList.stream()
                                .collect(Collectors.toMap(
                                        Product::getMilkType,
                                        p -> p,
                                        (existing, replacement) -> existing
                                ));
                List<AccountPostingDtoNew> accountPostingDtoNewList = new ArrayList<>();

//                for (Product product : productList) {
//                    String name = product.getName().toLowerCase();
//
//                    name = name.replace("row", "raw");
//
//                    if (!(name.contains("milk") && name.contains("raw"))) {
//                        continue;
//                    }
//
//                    if (name.contains("mix")) {
//                        mixProduct = product;
//                    } else if (name.contains("cow")) {
//                        cowProduct = product;
//                    } else if (name.contains("buf")) {
//                        buffProduct = product;
//                    }
//                }

/*                ----------------------------------------------------------------------
                NEED TO LOAD PRODUCT OF COW BUFF AND MIX SO THAT I CAN TAKE LEDGER FROM IT FOR PURCHASE OR DEBIT SIDE - DONE
                ALSO GET LEDGER MAPPING EVENT IN LIST OF 101 EVENT CODE IT IS ONLY 1 BUT STILL TAKE IN LIST AND GET ONE -  DONE
                TAKE OUT VOUCHER TYPE FROM LEDGER MAPPING EVENT - DONE
                NEED TO LOAD FINANCIAL YEAR ALSO
                  ---------------------------------------------------------------------- */


                if (type == 3) { // Payment Cycle wise
//                    SocietyPaymentCycleRepository societyPaymentCycleRepository = EmcsAppContext.getContext().getBean(SocietyPaymentCycleRepository.class);
                    List<SocietyPaymentCycle> societyPaymentCycleList = societyPaymentCycleRepository.findCycles(fromDateTime, toDateTime);
                    if (societyPaymentCycleList == null) {
                        return null;
                    }
                    milkCollectionList = milkCollectionRepository.findBySocietyPaymentCycleInOrderByCollectionDateAsc(societyPaymentCycleList);

                } else { // Day or Consolidate wise.
                    milkCollectionList = milkCollectionService.findAllBetween(fromDateTime, toDateTime);
                }

                if (type == 2) {
                    Map<LocalDate, List<MilkCollection>> groupedByDate =
                            milkCollectionList.stream()
                                    .collect(Collectors.groupingBy(mc -> mc.getCollectionDate().toLocalDate()));

                    for (LocalDate date : groupedByDate.keySet()) {
                        AccountPostingDtoNew accountPostingDtoNew = new AccountPostingDtoNew();

                        FinancialYear fy = financialYearRepository.findCurrentFinancialYear(date).orElse(null);
                        List<MilkCollection> milkCollections = groupedByDate.get(date);
                        Map<String, BigDecimal> memberAmountMap =
                                milkCollections.stream()
                                        .collect(Collectors.groupingBy(
                                                mc -> mc.getMember().getCode(),
                                                Collectors.reducing(
                                                        BigDecimal.ZERO,
                                                        MilkCollection::getAmount,
                                                        BigDecimal::add
                                                )
                                        ));
                        Voucher voucher = VoucherUtil.getVoucherInstance(null, "", date, draftAccountPosting.getToDate(), eventsList.get(0).getEventName() + " Posting " + date, ledgerMappingEvent.getVoucherType(), fy.getCode(), MainApp.identityDto.getSociety(), MainApp.identityDto.getUnion().getCode(), MainApp.identityDto.getSociety().getCode() + "01");
                        voucher.setAutoPosted(false);
                        voucher.setVoucherTransactions(new ArrayList<>());

                        accountPostingDtoNew.setVoucher(voucher);
                        Map<MilkType, BigDecimal> milkTypeAmountMap = new HashMap<>();
                        BigDecimal totalAmount = BigDecimal.ZERO;
                        for (MilkCollection mc : milkCollections) {
                            BigDecimal amount = mc.getAmount();
                            milkTypeAmountMap.merge(mc.getMilkType(), amount, BigDecimal::add);
                            totalAmount = totalAmount.add(mc.getAmount());
                        }
                        int txnCode = 1;
                        List<VoucherTransaction> voucherTransactionList = new ArrayList<>();
                        for (MilkType milkType : milkTypeAmountMap.keySet()) {
                            VoucherTransaction txn = VoucherUtil.getVoucherTxn(voucher, milkTypeAmountMap.get(milkType), false, productMap.get(milkType).getPurchaseLedger(), ledgerMappingEvent.getVoucherTxnDebitNarration(), String.valueOf(txnCode++));
                            txn.setEvents(eventsList.get(0));
                            voucherTransactionList.add(txn);

                        }
                        VoucherTransaction crTxn = VoucherUtil.getVoucherTxn(voucher, totalAmount, true, ledgerMappingEvent.getCreditLedger(), ledgerMappingEvent.getVoucherTxnCreditNarration(), String.valueOf(txnCode));
                        crTxn.setEvents(eventsList.get(0));
                        voucherTransactionList.add(crTxn);
                        accountPostingDtoNew.setVoucherTransactionList(voucherTransactionList);

//                        VOUCHER SUB LEDGER
                        List<VoucherSubLedger> voucherSubLedgerList = new ArrayList<>();
                        List<String> memberCodes = new ArrayList<>(memberAmountMap.keySet());

                        List<SubLedger> subLedgers = subLedgerRepository.findAllByTypeAndReferenceCodeIn((short) 1, memberCodes);
                        for (SubLedger subLedger : subLedgers) {
                            VoucherSubLedger vsl = new VoucherSubLedger();
                            vsl.setCreditDebit(true);
                            vsl.setSubLedger(subLedger);
                            vsl.setAmount(memberAmountMap.get(subLedger.getReferenceCode()));
                            vsl.setInitData();
                            vsl.setNarration(ledgerMappingEvent.getVoucherNarration());
                            voucherSubLedgerList.add(vsl);
                        }
                        accountPostingDtoNew.setVoucherSubLedgerList(voucherSubLedgerList);
                        accountPostingDtoNewList.add(accountPostingDtoNew);
                    }
                    return accountPostingDtoNewList;
                } else if (type == 1) {
                    Map<String, List<MilkCollection>> grouped =
                            Map.of("ALL", milkCollectionList);
                } else if (type == 3) {
                    Map<SocietyPaymentCycle, List<MilkCollection>> groupedByCycle =
                            milkCollectionList.stream()
                                    .collect(Collectors.groupingBy(MilkCollection::getSocietyPaymentCycle));
                }


            } else if (draftAccountPosting.getEventType() == AppConstant.EventCode.LOCAL_MILK_SALE) {

            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}