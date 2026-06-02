package com.eipl.amcs.master.account.service;

import com.eipl.amcs.base.service.NextCodeService;
import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.account.model.*;
import com.eipl.amcs.master.account.repository.*;
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
    private VoucherService voucherService;
    @Autowired
    private FinancialYearRepository financialYearRepository;
    @Autowired
    private VoucherRepository voucherRepository;
    @Autowired
    private LedgerGroupRepository ledgerGroupRepository;
    @Autowired
    private VoucherTransactionRepository voucherTxnRepository;

    @Override
    public List<LedgerOpeningBalance> findAll() {
        List<LedgerOpeningBalance> list = ledgerOpeningBalanceRepository.findAll(Sort.by("createdAt").descending());
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

    /**
     * Change History:
     * Date          Author           Version     Description
     * -----------   --------------   ---------   ---------------------------------
     * 23/05/2026    Nimit             1.0.0       Get opening balance for ledger group which are cash for particular
     * financial year till the date -1 for which user is finding rojmed data
     * 01/06/2026    Nimit             1.0.1       From date will pass from method call.
     */
    @Override
    public BigDecimal getLedgerOpeningBalanceOfTypeCash(LocalDate fromDate, LocalDate toDate) {


        // NIMIT | 23.05.2026 : Get Ledger of ledger group which have is_cash column true.
        List<LedgerGroup> ledgerGroupsOfIsCash = ledgerGroupRepository.findByIsCash(true);
        List<Ledger> ledgers = ledgerRepository.findByLedgerGroupIn(ledgerGroupsOfIsCash);


        // NIMIT | 23.05.2026 : Get Opening Balance of Fetch Cash Type Ledger for particular financial year.
        FinancialYearRepository financialYearRepository = EmcsAppContext.getContext().getBean(FinancialYearRepository.class);
        FinancialYear financialYear = financialYearRepository.findCurrentFinancialYear(toDate).orElse(null);
        if (financialYear == null)
            return BigDecimal.ZERO;
        String fyCode = financialYear.getCode();
        List<LedgerOpeningBalance> ledgerOpeningBalanceList = ledgerOpeningBalanceRepository.findByLedgerInAndFinancialYearsCode(ledgers, fyCode);


        // NIMIT | 23.05.2026 : Get all voucher transaction from financial year start to the day-1 from where user is standing.
        List<VoucherTransaction> voucherTransactionList = voucherService.loadVoucherTransactionByCashTypeAndDateBetween(fromDate, toDate.minusDays(1), false);


        // NIMIT | 23.05.2026 : Get credit and debit opening balance of ledger is cash type.
        BigDecimal crLedgerOpeningBalance = ledgerOpeningBalanceList.stream().filter(ocl -> ocl.getCreditDebit() == true).map(LedgerOpeningBalance::getBalance)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal drLedgerOpeningBalance = ledgerOpeningBalanceList.stream().filter(ocl -> ocl.getCreditDebit() == false).map(LedgerOpeningBalance::getBalance)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // NIMIT | 23.05.2026 : Get credit and debit amount of voucher transaction.
        BigDecimal drVoucherTransaction = voucherTransactionList.stream().filter(ocl -> ocl.getCreditDebit() == false).map(VoucherTransaction::getAmount)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal crVoucherTransaction = voucherTransactionList.stream().filter(ocl -> ocl.getCreditDebit() == true).map(VoucherTransaction::getAmount)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);


        BigDecimal finalOpeningBalance = crLedgerOpeningBalance.subtract(drLedgerOpeningBalance);
        return finalOpeningBalance.add(drVoucherTransaction).subtract(crVoucherTransaction);
    }

}