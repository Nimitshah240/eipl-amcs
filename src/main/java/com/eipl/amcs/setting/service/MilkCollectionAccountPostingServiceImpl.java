package com.eipl.amcs.setting.service;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.service.NextCodeService;
import com.eipl.amcs.master.account.model.*;
import com.eipl.amcs.master.account.repository.*;
import com.eipl.amcs.master.global.repository.ShiftRepository;
import com.eipl.amcs.operation.procurement.model.MilkCollection;
import com.eipl.amcs.operation.procurement.service.MilkCollectionService;
import com.eipl.amcs.setting.dto.MilkCollectionAccountPostingDto;
import com.eipl.amcs.setting.model.MilkCollectionAccountPosting;
import com.eipl.amcs.setting.repository.MilkCollectionAccountPostingRepository;
import com.eipl.amcs.utils.AppConstant;
import com.eipl.amcs.utils.CommonUtils;
import com.eipl.amcs.utils.VoucherUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;


@Slf4j
@Service
public class MilkCollectionAccountPostingServiceImpl implements MilkCollectionAccountPostingService {

    @Autowired
    private MilkCollectionAccountPostingRepository milkCollectionAccountPostingRepository;
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
    private SubLedgerRepository subLedgerRepository;
    @Autowired
    private VoucherSubLedgerRepository voucherSubLedgerRepository;
    @Autowired
    private LedgerMappingEventRepository ledgerMappingEventRepository;
    @Autowired
    private FinancialYearRepository financialYearRepository;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MilkCollectionAccountPosting save(MilkCollectionAccountPosting milkCollectionAccountPosting, List<MilkCollectionAccountPostingDto> milkCollectionAccountPostingDtoList) {

        try {
            validate(milkCollectionAccountPosting);
            createAccountPosting(milkCollectionAccountPosting, milkCollectionAccountPostingDtoList);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
        return null;
    }

    private void validate(MilkCollectionAccountPosting milkCollectionAccountPosting) {
        if (!milkCollectionAccountPostingRepository.findOverlappingPostings(milkCollectionAccountPosting.getFromDate(), milkCollectionAccountPosting.getFromShift(), milkCollectionAccountPosting.getToDate(), milkCollectionAccountPosting.getToShift()).isEmpty())
            throw new RuntimeException("posting.already.exists");
    }

    @Deprecated
    private List<Voucher> createVoucher(LedgerMappingEvent ledgerMappingEvent, List<MilkCollectionAccountPostingDto> milkCollectionAccountPostingDtoList, MilkCollectionAccountPosting milkCollectionAccountPosting) {
        try {
            String code = MainApp.identityDto.getSociety().getCode() + "/" + MainApp.getFinancialYear().getCode() + "/";
            String codeI = nextCodeService.getNextCode("Voucher", "code", code, 0);
            List<Voucher> voucherList = new ArrayList<>();
            for (MilkCollectionAccountPostingDto milkCollectionAccountPostingDto : milkCollectionAccountPostingDtoList) {
                Voucher voucher = new Voucher();
                voucher.setCode(codeI);
                voucher.setVoucherDate(milkCollectionAccountPosting.getToDate());
                voucher.setBillDate(milkCollectionAccountPostingDto.getDate());
                voucher.setSociety(MainApp.identityDto.getSociety());
                voucher.setDockCode(MainApp.identityDto.getDock().getDockNo());
                voucher.setFinancialYearsCode(MainApp.getFinancialYear().getCode());
                voucher.setFinancialYearsCode(MainApp.getFinancialYear().getCode());
                voucher.setVoucherType(ledgerMappingEvent.getVoucherType());
                voucherList.add(voucherRepository.customSave(voucher, CommonUtils.setIdentityHeader()));

                // Generate next code
                long id = Long.parseLong(codeI.split("/")[2]);
                id++;
                codeI = code + id;
            }
            return voucherList;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException("error.occurred");
        }
    }

    @Deprecated
    private List<VoucherTransaction> createVoucherTransaction(List<Voucher> voucherList, List<MilkCollectionAccountPostingDto> creditMilkCollectionAccountPostingDto, List<MilkCollectionAccountPostingDto> debitMilkCollectionAccountPostingDto) {
        try {
            List<VoucherTransaction> voucherTransactionList = new ArrayList<>();

            long code = Long.parseLong(nextCodeService.getNextCode("VoucherTransaction", "code", MainApp.identityDto.getSociety().getCode(), 4));

            for (MilkCollectionAccountPostingDto milkCollectionAccountPostingDto : creditMilkCollectionAccountPostingDto) {
                VoucherTransaction creditVoucherTransaction = new VoucherTransaction();
                creditVoucherTransaction.setCode(Long.toString(code));
                creditVoucherTransaction.setAmount(milkCollectionAccountPostingDto.getAmount());
                creditVoucherTransaction.setCreditDebit(true);
                creditVoucherTransaction.setLedger(creditMilkCollectionAccountPostingDto.get(0).getLedgerMappingEvent().getCreditLedger());
                creditVoucherTransaction.setVoucher(voucherList.stream()
                        .filter(v -> v.getBillDate().equals(milkCollectionAccountPostingDto.getDate()))
                        .findFirst()
                        .orElse(null));
                voucherTransactionList.add(voucherTransactionRepository.customSave(creditVoucherTransaction, CommonUtils.setIdentityHeader()));
                code++;
            }
            for (MilkCollectionAccountPostingDto milkCollectionAccountPostingDto : debitMilkCollectionAccountPostingDto) {
                VoucherTransaction debitVoucherTransaction = new VoucherTransaction();
                debitVoucherTransaction.setCode(Long.toString(code));
                debitVoucherTransaction.setAmount(milkCollectionAccountPostingDto.getAmount());
                debitVoucherTransaction.setCreditDebit(false);
                debitVoucherTransaction.setLedger(debitMilkCollectionAccountPostingDto.get(0).getLedgerMappingEvent().getDebitLedger());
                debitVoucherTransaction.setVoucher(voucherList.stream()
                        .filter(v -> v.getBillDate().equals(milkCollectionAccountPostingDto.getDate()))
                        .findFirst()
                        .orElse(null));
                voucherTransactionList.add(voucherTransactionRepository.customSave(debitVoucherTransaction, CommonUtils.setIdentityHeader()));
                code++;
            }

            return voucherTransactionList;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Deprecated
    private void createVoucherSubLedger(LedgerMappingEvent ledgerMappingEvent, MilkCollectionAccountPosting milkCollectionAccountPosting, List<VoucherTransaction> voucherTransactionList, List<MilkCollectionAccountPostingDto> milkCollectionAccountPostingDtoList, List<Voucher> voucherList) {
        try {
            boolean isCreditSubLedger = ledgerMappingEvent.getCreditSubLedger();

            List<VoucherTransaction> filteredVoucherTransactionList = voucherTransactionList.stream()
                    .filter(vt -> vt.getCreditDebit() == isCreditSubLedger)
                    .collect(Collectors.toList());

            List<MilkCollection> milkCollectionList = milkCollectionService.findAllBetween(CommonUtils.getLocalDateTimeFromDateAndShift(milkCollectionAccountPosting.getFromDate(), shiftRepository.findById(milkCollectionAccountPosting.getFromShift()).get()), CommonUtils.getLocalDateTimeFromDateAndShift(milkCollectionAccountPosting.getToDate(), shiftRepository.findById(milkCollectionAccountPosting.getToShift()).get()));

            Set<String> memberCodes = milkCollectionList.stream()
                    .map(m -> m.getMember().getCode())
                    .filter(Objects::nonNull)
                    .collect(Collectors.toSet());

            Map<LocalDate, Map<String, BigDecimal>> dailyMemberTotals1 = new HashMap<>();
            if (milkCollectionAccountPosting.getPostingType() == 1) {
                // Consolidate
                Map<String, BigDecimal> memberPeriodTotals = milkCollectionList.stream()
                        .collect(Collectors.groupingBy(
                                m -> m.getMember().getCode(),
                                Collectors.reducing(
                                        BigDecimal.ZERO,
                                        MilkCollection::getAmount,
                                        BigDecimal::add
                                )
                        ));
                dailyMemberTotals1.put(LocalDate.now(), memberPeriodTotals);
            } else if (milkCollectionAccountPosting.getPostingType() == 2) {
                // Day wise
                dailyMemberTotals1 = milkCollectionList.stream()
                        .collect(Collectors.groupingBy(
                                m -> m.getCollectionDate().toLocalDate(),
                                Collectors.groupingBy(
                                        m -> m.getMember().getCode(),
                                        Collectors.reducing(BigDecimal.ZERO, MilkCollection::getAmount, BigDecimal::add)
                                )
                        ));
            }


            List<SubLedger> subLedgerList = subLedgerRepository.findAllByTypeAndReferenceCodeIn((short) 1, new ArrayList<>(memberCodes));
            List<VoucherSubLedger> voucherSubLedgerList = new ArrayList<>();

            long code = Long.valueOf(nextCodeService.getNextCode("VoucherSubLedger", "code", MainApp.identityDto.getSociety().getCode(), 0));

            for (LocalDate transactionDate : dailyMemberTotals1.keySet()) {
                Voucher voucher = voucherList.stream()
                        .filter(v -> v.getVoucherDate().equals(transactionDate))
                        .findFirst()
                        .orElse(null);
                for (String memberCode : dailyMemberTotals1.get(transactionDate).keySet()) {
                    VoucherSubLedger voucherSubLedger = new VoucherSubLedger();
                    voucherSubLedger.setCode(String.valueOf(code));
                    voucherSubLedger.setVoucher(voucher);
                    voucherSubLedger.setCreditDebit(isCreditSubLedger);
                    voucherSubLedger.setVoucherTransaction(filteredVoucherTransactionList.stream().filter(vt -> vt.getVoucher().equals(voucher))
                            .findFirst()
                            .orElse(null));

                    voucherSubLedger.setSubLedger(subLedgerList.stream().filter(sb -> sb.getReferenceCode().equals(memberCode))
                            .findFirst()
                            .orElse(null));
                    voucherSubLedger.setAmount(dailyMemberTotals1.get(transactionDate).get(memberCode));
                    voucherSubLedgerList.add(voucherSubLedgerRepository.customSave(voucherSubLedger, CommonUtils.setIdentityHeader()));
                    code++;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Deprecated
    private void createMilkCollectionAccountPosting(MilkCollectionAccountPosting milkCollectionAccountPosting) {
        try {
            milkCollectionAccountPosting.setCode(Long.valueOf(nextCodeService.getNextCode("MilkCollectionAccountPosting", "code", MainApp.identityDto.getSociety().getCode(), 0)));
            milkCollectionAccountPostingRepository.customSave(milkCollectionAccountPosting, CommonUtils.setIdentityHeader());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    private void createAccountPosting(MilkCollectionAccountPosting milkCollectionAccountPosting,
                                      List<MilkCollectionAccountPostingDto> milkCollectionAccountPostingDtoList) {
        try {
            validate(milkCollectionAccountPosting);

            List<LedgerMappingEvent> eventsList = ledgerMappingEventRepository.findByEventcode(AppConstant.EventCode.MILK_COLLECTION);
            if (eventsList == null || eventsList.isEmpty())
                return;

            for (MilkCollectionAccountPostingDto milkCollectionAccountPostingDto : milkCollectionAccountPostingDtoList) {

                Optional<FinancialYear> financialYear = financialYearRepository.findCurrentFinancialYear(milkCollectionAccountPostingDto.getDate());


//                VOUCHER -------------
                String voucherCode = nextCodeService.getNextCode("Voucher", "code",
                        MainApp.identityDto.getSociety().getCode() + "/" + financialYear.get().getCode() + "/", 0);
                if (voucherCode == null)
                    continue;

                Voucher voucher = VoucherUtil.getVoucherInstance(voucherCode, "", milkCollectionAccountPostingDto.getDate(),
                        milkCollectionAccountPosting.getToDate(), "Milk Collection Manual Posting " + milkCollectionAccountPostingDto.getDate(),
                        eventsList.get(0).getVoucherType(), financialYear.orElse(null).getCode(),
                        MainApp.identityDto.getSociety(), MainApp.identityDto.getUnion().getCode(), MainApp.identityDto.getSociety().getCode() + "01");
                voucher.setAutoPosted(false);
                voucher.setVoucherTransactions(new ArrayList<>());

//              VOUCHER TRANSACTION ----------
                VoucherTransaction creditTxn = VoucherUtil.getVoucherTxn(voucher, milkCollectionAccountPostingDto.getAmount(), true, eventsList.get(0).getCreditLedger(),
                        "Milk Collection Credit " + milkCollectionAccountPostingDto.getAmount(), "1");
                voucher.getVoucherTransactions().add(creditTxn);
                creditTxn.setVoucherSubLedgers(new ArrayList<>());

                VoucherTransaction debitTxn = VoucherUtil.getVoucherTxn(voucher, milkCollectionAccountPostingDto.getAmount(), false, eventsList.get(0).getDebitLedger(),
                        "Milk Collection Debit " + milkCollectionAccountPostingDto.getAmount(), "2");
                voucher.getVoucherTransactions().add(debitTxn);
                debitTxn.setVoucherSubLedgers(new ArrayList<>());


//              VOUCHER SUB LEDGER ----------
                List<MilkCollection> milkCollectionList = new ArrayList<>();

                if (milkCollectionAccountPosting.getPostingType() == 1) { // consolidate
                    milkCollectionList = milkCollectionService.findAllBetween(CommonUtils.getLocalDateTimeFromDateAndShift(milkCollectionAccountPosting.getFromDate(), shiftRepository.findById(milkCollectionAccountPosting.getFromShift()).get()), CommonUtils.getLocalDateTimeFromDateAndShift(milkCollectionAccountPosting.getToDate(), shiftRepository.findById(milkCollectionAccountPosting.getToShift()).get()));
                } else { // day wise
                    milkCollectionList = milkCollectionService.findAllBetween(CommonUtils.getLocalDateTimeFromDateAndShift(milkCollectionAccountPostingDto.getDate(), shiftRepository.findById(1).get()), CommonUtils.getLocalDateTimeFromDateAndShift(milkCollectionAccountPostingDto.getDate(), shiftRepository.findById(2).get())); // Whole day collection
                }

                Set<String> memberCodes = milkCollectionList.stream()
                        .map(m -> m.getMember().getCode())
                        .filter(Objects::nonNull)
                        .collect(Collectors.toSet());

                List<SubLedger> subLedgerList = subLedgerRepository.findAllByTypeAndReferenceCodeIn((short) 1, new ArrayList<>(memberCodes));

                long voucherSubLedgerCode = Long.valueOf(nextCodeService.getNextCode("VoucherSubLedger", "code", MainApp.identityDto.getSociety().getCode(), 0));

                Map<String, BigDecimal> memberPeriodTotals = milkCollectionList.stream()
                        .collect(Collectors.groupingBy(
                                m -> m.getMember().getCode(),
                                Collectors.reducing(
                                        BigDecimal.ZERO,
                                        MilkCollection::getAmount,
                                        BigDecimal::add
                                )
                        ));

                for (String memberCode : memberCodes) {
                    VoucherSubLedger voucherSubLedger = new VoucherSubLedger();
                    voucherSubLedger.setCode(String.valueOf(voucherSubLedgerCode));
                    voucherSubLedger.setVoucher(voucher);
                    voucherSubLedger.setCreditDebit(eventsList.get(0).getCreditSubLedger());
                    voucherSubLedger.setVoucherTransaction(eventsList.get(0).getCreditSubLedger() ? creditTxn : debitTxn);

                    voucherSubLedger.setSubLedger(subLedgerList.stream().filter(sb -> sb.getReferenceCode().equals(memberCode))
                            .findFirst()
                            .orElse(null));
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
            milkCollectionAccountPosting.setCode(Long.valueOf(nextCodeService.getNextCode("MilkCollectionAccountPosting", "code", MainApp.identityDto.getSociety().getCode(), 0)));
            milkCollectionAccountPostingRepository.customSave(milkCollectionAccountPosting, CommonUtils.setIdentityHeader());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}