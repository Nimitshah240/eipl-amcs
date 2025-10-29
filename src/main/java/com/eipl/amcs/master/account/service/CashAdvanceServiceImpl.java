package com.eipl.amcs.master.account.service;

import com.eipl.amcs.base.service.NextCodeService;
import com.eipl.amcs.exception.BusinessValidationFailException;
import com.eipl.amcs.master.account.dto.CashAdvanceDto;
import com.eipl.amcs.master.account.model.*;
import com.eipl.amcs.master.account.repository.*;
import com.eipl.amcs.master.operation.model.Member;
import com.eipl.amcs.master.operation.repository.MemberRepository;
import com.eipl.amcs.master.org.model.Society;
import com.eipl.amcs.master.procurement.model.SocietyPaymentCycle;
import com.eipl.amcs.operation.inventory.model.ProductSaleInstallment;
import com.eipl.amcs.operation.inventory.repository.ProductSaleInstallmentRepository;
import com.eipl.amcs.utils.CommonUtils;
import com.eipl.amcs.utils.AppConstant;
import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.validation.FieldError;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class CashAdvanceServiceImpl implements CashAdvanceService {

    private static final Logger log = LoggerFactory.getLogger(CashAdvanceServiceImpl.class);
    @Autowired
    private CashAdvanceRepository cashAdvanceRepository;
    @Autowired
    private NextCodeService nextCodeService;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private ProductSaleInstallmentRepository installmentRepository;
    @Autowired
    private LedgerMappingEventRepository ledgerMappingEventRepository;
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

    @Override
    public List<CashAdvance> findAll() {
        List<CashAdvance> list = cashAdvanceRepository.findAll(Sort.by("code").descending());

        for (CashAdvance cash : list) {
            cash.setSociety(Hibernate.unproxy(cash.getSociety(), Society.class));
            cash.setSocietyPaymentCycle(Hibernate.unproxy(cash.getSocietyPaymentCycle(), SocietyPaymentCycle.class));
            cash.setMember(Hibernate.unproxy(cash.getMember(), Member.class));
        }
        log.info("cash findAll {} items fetched", list.size());
        return list;
    }

    @Override
    public CashAdvance save(CashAdvanceDto cashAdvanceDto, String identityInfo) {
        CashAdvance cashAdvance = cashAdvanceDto.getCashAdvance();
        String code = nextCodeService.getNextCode("CashAdvance", "code", cashAdvance.getSociety().getCode(), 2);
        cashAdvance.setCode(code);
        cashAdvance.setInitData();

        int i = 1;
        for (ProductSaleInstallment productSaleInstallment : cashAdvanceDto.getInstallmentList()) {
            productSaleInstallment.setCode(cashAdvance.getCode() + "-" + i);
            productSaleInstallment.setInvoiceNo(cashAdvance.getCode());
            productSaleInstallment.setPreviousPendingAmount(BigDecimal.ZERO);
            installmentRepository.customSave(productSaleInstallment, identityInfo);
            i++;
        }
        createAutoPosting(cashAdvance, cashAdvance.getVoucherNo());
        cashAdvanceRepository.save(cashAdvance);
        return null;

    }


    @Override
    public CashAdvance update(CashAdvanceDto cashAdvanceDto, String identityInfo) {
        CashAdvance cashAdvance = cashAdvanceDto.getCashAdvance();
        cashAdvance.setupdateData();
        return cashAdvanceRepository.save(cashAdvance);
    }


    @Override
    public Optional<CashAdvance> findById(String cashAdvanceNo) {
        return Optional.empty();
    }

    @Override
    public void delete(String cashAdvanceNo, String identityInfo) {
        List<ProductSaleInstallment> installmentList = installmentRepository.findByInvoiceNo(cashAdvanceNo);
        for (ProductSaleInstallment productSaleInstallment : installmentList) {
            if (productSaleInstallment.getSocietyPaymentCycle().getLockBillingProcess()) {
                FieldError nameNotValid = CommonUtils.getFieldError("cashadvance", "date", cashAdvanceNo, "can.not.delete");
                throw new BusinessValidationFailException(getClass(), nameNotValid);
            }
            installmentRepository.delete(productSaleInstallment);
        }
        CashAdvance cashAdvance = cashAdvanceRepository.findById(cashAdvanceNo).get();
        Optional<Voucher> voucher = voucherRepository.findById(cashAdvance.getVoucherNo());
        if (voucher.isEmpty()) return;
        List<VoucherTransaction> transactionList = voucherTxnRepository.findByVoucher(voucher.get());
        if (transactionList != null && !transactionList.isEmpty()) {
            for (VoucherTransaction voucherTransaction : transactionList) {
                List<VoucherSubLedger> voucherSubLedgerList = voucherSubLedgerRepository.findByVoucherTransaction(voucherTransaction);
                if (voucherSubLedgerList != null && voucherSubLedgerList.isEmpty()) {
                    for (VoucherSubLedger voucherSubLedger : voucherSubLedgerList) {
                        voucherSubLedgerRepository.delete(voucherSubLedger);
                    }
                }
                voucherTxnRepository.delete(voucherTransaction);
            }
            voucherRepository.delete(voucher.get());
        }
        cashAdvanceRepository.deleteById(cashAdvanceNo);
    }


    @Override
    public void delete(CashAdvanceDto cashAdvance, String identityInfo) {


        List<ProductSaleInstallment> installmentList = installmentRepository.findByInvoiceNo(cashAdvance.getCashAdvance().getCode());
        for (ProductSaleInstallment productSaleInstallment : installmentList) {
            if (productSaleInstallment.getSocietyPaymentCycle().getLockBillingProcess()) {
                FieldError nameNotValid = CommonUtils.getFieldError("cashadvance", "date", cashAdvance.getTableName(), "can.not.delete");
                throw new BusinessValidationFailException(getClass(), nameNotValid);
            }
            installmentRepository.delete(productSaleInstallment);
        }
        Optional<Voucher> voucher = voucherRepository.findById(cashAdvance.getCashAdvance().getVoucherNo());
        if (voucher.isEmpty()) return;
        List<VoucherTransaction> transactionList = voucherTxnRepository.findByVoucher(voucher.get());
        if (transactionList != null && !transactionList.isEmpty()) {
            for (VoucherTransaction voucherTransaction : transactionList) {
                List<VoucherSubLedger> voucherSubLedgerList = voucherSubLedgerRepository.findByVoucherTransaction(voucherTransaction);
                if (voucherSubLedgerList != null && voucherSubLedgerList.isEmpty()) {
                    for (VoucherSubLedger voucherSubLedger : voucherSubLedgerList) {
                        voucherSubLedgerRepository.delete(voucherSubLedger);
                    }
                }
                voucherTxnRepository.delete(voucherTransaction);
            }
            voucherRepository.delete(voucher.get());
        }
        cashAdvanceRepository.delete(cashAdvance.getCashAdvance());
    }


    private void createAutoPosting(CashAdvance cashAdvance, String voucherCode) {
        try {
            Optional<FinancialYear> financialYear = financialYearRepository.findCurrentFinancialYear(cashAdvance.getDate());

            List<LedgerMappingEvent> eventsList = ledgerMappingEventRepository.findByEventcode(AppConstant.EventCode.CASH_ADVANCE);
            if (eventsList == null || eventsList.isEmpty()) return;

            if (voucherCode == null) {
                voucherCode = nextCodeService.getNextCode("Voucher", "code",
                        cashAdvance.getSociety().getCode() + "/" + financialYear.get().getCode() + "/", 6);
                if (voucherCode == null) return;


                //Voucher
                Voucher voucher = new Voucher();
                voucher.setCode(voucherCode);
                voucher.setAutoPosted(true);
                voucher.setCancelled(false);
                voucher.setBillDate(cashAdvance.getDate());
                voucher.setVoucherDate(cashAdvance.getDate());
                voucher.setRemarks("CashAdvance Auto posting: " + cashAdvance.getDate().toString());
                voucher.setSociety(cashAdvance.getSociety());
                voucher.setVoucherType(eventsList.get(0).getVoucherType());
                voucher.setDockCode(cashAdvance.getSociety().getCode() + "01");
                voucher.setUnionCode(cashAdvance.getUnionCode());
                voucher.setInitData();
                voucher.setBillNo(cashAdvance.getCode());
                voucher.setFinancialYearsCode(financialYear.isPresent() ? financialYear.get().getCode() : null);
                cashAdvance.setVoucherNo(voucherCode);
                voucherRepository.save(voucher);
                //Debit Txn
                VoucherTransaction txn = null;

                int tc = 1;
                if (eventsList.get(0).getDebitLedger() != null) {
                    txn = new VoucherTransaction();
                    txn.setCode(voucherCode + "T" + tc);
                    txn.setAmount(cashAdvance.getAmount());
                    txn.setInitData();
                    txn.setCreditDebit(false);
                    txn.setLedger(eventsList.get(0).getDebitLedger());
                    txn.setNarration("Cash Advance  To " + cashAdvance.getMember().getCode());
                    txn.setVoucher(voucher);
                    voucherTxnRepository.save(txn);
                    tc++;
                    if (eventsList.get(0).getDebitSubLedger() != null ? eventsList.get(0).getDebitSubLedger() : false) {
                        Optional<SubLedger> sl = subLedgerRepository.findByReferenceCodeAndType(cashAdvance.getMember().getCode(), (short) 0);
                        if (sl.isPresent()) {
                            VoucherSubLedger vSubLedger = new VoucherSubLedger();
                            vSubLedger.setCode(txn.getCode() + "S1");
                            vSubLedger.setAmount(cashAdvance.getAmount());
                            vSubLedger.setInitData();
                            vSubLedger.setCreditDebit(false);
                            vSubLedger.setNarration("Cash Advance  To " + cashAdvance.getMember().getCode());

                            vSubLedger.setSubLedger(sl.get());
                            vSubLedger.setVoucher(voucher);
                            vSubLedger.setVoucherTransaction(txn);
                            voucherSubLedgerRepository.save(vSubLedger);
                        }
                    }
                }
                // Credit Ledger
                txn = new VoucherTransaction();
                txn.setCode(voucherCode + "T" + tc);
                txn.setAmount(cashAdvance.getAmount());
                txn.setInitData();
                txn.setCreditDebit(true);
                txn.setLedger(eventsList.get(0).getCreditLedger());
                txn.setNarration("Cash Advance ");
                txn.setVoucher(voucher);
                voucherTxnRepository.save(txn);
            }
        } catch (Exception e) {
        }
    }


}