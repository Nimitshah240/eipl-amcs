package com.eipl.amcs.master.account.service;

import com.eipl.amcs.base.service.NextCodeService;
import com.eipl.amcs.master.account.model.FinancialYear;
import com.eipl.amcs.master.account.model.Ledger;
import com.eipl.amcs.master.account.model.LedgerOpeningBalance;
import com.eipl.amcs.master.account.model.VoucherTransaction;
import com.eipl.amcs.master.account.repository.FinancialYearRepository;
import com.eipl.amcs.master.account.repository.LedgerOpeningBalanceRepository;
import com.eipl.amcs.master.account.repository.LedgerRepository;
import com.eipl.amcs.master.org.model.Society;
import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class LedgerOpeningBalanceServiceImpl implements LedgerOpeningBalanceService {
    private static final Logger log = LoggerFactory.getLogger(LedgerOpeningBalanceServiceImpl.class);
    @Autowired
    private LedgerOpeningBalanceRepository ledgerOpeningBalanceRepository;
    @Autowired
    private NextCodeService nextCodeService;
    @Autowired
    private LedgerRepository ledgerRepository;
    @Autowired
    private VoucherService voucherServiceImpl;
    @Autowired
    private FinancialYearRepository financialYearRepository;

    @Override
    public List<LedgerOpeningBalance> findAll() {
        List<LedgerOpeningBalance> list = ledgerOpeningBalanceRepository.findAll(Sort.by("code"));
//        for (LedgerOpeningBalance ledgerOpeningBalance : list) {
//            ledgerOpeningBalance.setLedger(Hibernate.unproxy(ledgerOpeningBalance.getLedger(), Ledger.class));
//            ledgerOpeningBalance.setSociety(Hibernate.unproxy(ledgerOpeningBalance.getSociety(), Society.class));
//        }
        log.info("cash findAll {} items fetched", list.size());
        return list;
    }

    @Override
    public LedgerOpeningBalance save(LedgerOpeningBalance ledgerOpeningBalance, String identityInfo) {
        String code = nextCodeService.getNextCode("LedgerOpeningBalance", "code", ledgerOpeningBalance.getSociety().getCode(), 0);
        ledgerOpeningBalance.setCode(code);
        ledgerOpeningBalance.setInitData();
        ledgerOpeningBalance.setSociety(Hibernate.unproxy(ledgerOpeningBalance.getSociety(), Society.class));
        ledgerOpeningBalance = ledgerOpeningBalanceRepository.customSave(ledgerOpeningBalance, identityInfo);
        return ledgerOpeningBalance;
    }


    @Override
    public LedgerOpeningBalance update(LedgerOpeningBalance ledgerOpeningBalance, String identityInfo) {
        ledgerOpeningBalance.setupdateData();
        ledgerOpeningBalance.setSociety(Hibernate.unproxy(ledgerOpeningBalance.getSociety(), Society.class));
        return ledgerOpeningBalanceRepository.customUpdate(ledgerOpeningBalance, identityInfo);
    }


    @Override
    public Optional<LedgerOpeningBalance> findById(String cashAdvanceNo) {
        return Optional.empty();
    }

    @Override
    public void delete(String code, String identityInfo) {
        LedgerOpeningBalance l = ledgerOpeningBalanceRepository.findById(code).orElseThrow();
        l.setSociety(Hibernate.unproxy(l.getSociety(), Society.class));
        ledgerOpeningBalanceRepository.customDelete(l, identityInfo);
    }


    @Override
    public void delete(LedgerOpeningBalance ledgerOpening, String identityInfo) {
        ledgerOpening.setSociety(Hibernate.unproxy(ledgerOpening.getSociety(), Society.class));
        ledgerOpeningBalanceRepository.customDelete(ledgerOpening, identityInfo);
    }

    @Override
    public List<LedgerOpeningBalance> importLedgerBalance(List<LedgerOpeningBalance> ledgerList, String header) {

        List<LedgerOpeningBalance> list = new ArrayList<>();

        ledgerList.forEach(item -> {
            LedgerOpeningBalance led = null;
            try {
                if (item.getCode() != null) {
                    Optional<LedgerOpeningBalance> ledBalanceData = ledgerOpeningBalanceRepository.findById(item.getCode());
                    if (ledBalanceData.isPresent()) {
                        LedgerOpeningBalance ledBal = ledBalanceData.get();
                        ledBal.setLedger(item.getLedger());
                        ledBal.setFinancialYearsCode(item.getFinancialYearsCode());
                        ledBal.setCreditDebit(item.getCreditDebit());
                        ledBal.setupdateData();

                        ledgerOpeningBalanceRepository.customUpdate(ledBal, header);


                        list.add(ledBal);
                    }
                } else {
                    String code = nextCodeService.getNextCode("LedgerOpeningBalance", "code", item.getSociety().getCode(), 0);
                    item.setCode(code);
                    item.setInitData();
                    item.setSociety(Hibernate.unproxy(item.getSociety(), Society.class));
                    led = ledgerOpeningBalanceRepository.customSave(item, header);
                    list.add(led);
                }
            } catch (Exception e) {
                list.add(led);
            }
        });

        return null;
    }

    @Override
    public BigDecimal getLedgerOpeningBalanceOfTypeCash(LocalDate toDate) {

        FinancialYear financialYear = financialYearRepository.findCurrentFinancialYear(toDate).orElse(null);
        if (financialYear == null)
            return BigDecimal.ZERO;

        LocalDate fromDate = financialYear.getStartDate();
        String fyCode = financialYear.getCode();

        List<Ledger> ledgers = ledgerRepository.findAll(); // Just For Cash Ledger Type
        List<LedgerOpeningBalance> ledgerOpeningBalanceList = ledgerOpeningBalanceRepository.findByLedgerInAndFinancialYearsCode(ledgers, fyCode);
        List<VoucherTransaction> voucherTransactionList = voucherServiceImpl.loadVoucherByVoucherDateBetween(fromDate, toDate);

        BigDecimal crLedgerOpeningBalance = ledgerOpeningBalanceList.stream().filter(ocl -> ocl.getCreditDebit() == true).map(LedgerOpeningBalance::getBalance)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal drLedgerOpeningBalance = ledgerOpeningBalanceList.stream().filter(ocl -> ocl.getCreditDebit() == false).map(LedgerOpeningBalance::getBalance)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal drVoucherTransaction = voucherTransactionList.stream().filter(ocl -> ocl.getCreditDebit() == false).map(VoucherTransaction::getAmount)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal crVoucherTransaction = voucherTransactionList.stream().filter(ocl -> ocl.getCreditDebit() == true).map(VoucherTransaction::getAmount)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        crVoucherTransaction = crVoucherTransaction.add(crLedgerOpeningBalance);
        drVoucherTransaction = drVoucherTransaction.add(drLedgerOpeningBalance);
        return crVoucherTransaction.subtract(drVoucherTransaction);
    }

}