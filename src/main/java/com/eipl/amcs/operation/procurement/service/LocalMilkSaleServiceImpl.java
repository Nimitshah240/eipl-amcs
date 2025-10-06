package com.eipl.amcs.operation.procurement.service;

import com.eipl.amcs.base.service.NextCodeService;
import com.eipl.amcs.exception.BusinessValidationFailException;
import com.eipl.amcs.master.account.model.*;
import com.eipl.amcs.master.account.repository.*;
import com.eipl.amcs.master.operation.model.MemberCreditLimit;
import com.eipl.amcs.master.operation.model.MemberCreditLimitTransaction;
import com.eipl.amcs.master.operation.repository.MemberCreditLimitRepository;
import com.eipl.amcs.master.operation.repository.MemberCreditLimitTransactionRepository;
import com.eipl.amcs.master.org.model.Society;
import com.eipl.amcs.master.procurement.model.SocietyPaymentCycle;
import com.eipl.amcs.master.procurement.repository.SocietyPaymentCycleRepository;
import com.eipl.amcs.operation.procurement.model.LocalMilkSale;
import com.eipl.amcs.operation.procurement.repository.LocalMilkSaleRepository;
import com.eipl.amcs.utils.AppConstant;
import com.eipl.amcs.util.CommonUtil;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.FieldError;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Service
public class LocalMilkSaleServiceImpl implements LocalMilkSaleService {
    @Autowired
    private LocalMilkSaleRepository localMilkSaleRepository;
    @Autowired
    private SocietyPaymentCycleRepository societyPaymentCycleRepository;
    @Autowired
    private NextCodeService nextCodeService;
    @Autowired
    private MemberCreditLimitRepository creditLimitRepository;
    @Autowired
    private MemberCreditLimitTransactionRepository memberCreditLimitTxnRepository;
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
    @Autowired
    private MemberCreditLimitRepository memberCreditLimitRepository;

    @Override
    public List<LocalMilkSale> findAll(LocalDateTime fromDt, LocalDateTime toDt) {
        return localMilkSaleRepository.findBySaleDateBetween(fromDt, toDt, Sort.by("saleDate").descending());
    }

    @Override
//    @Transactional
    public LocalMilkSale save(LocalMilkSale localMilkSale, String identityInfo) throws BusinessValidationFailException {
        // validation
        SocietyPaymentCycle paymentCycle = societyPaymentCycleRepository.findTop1ByFromDateLessThanEqualAndToDateGreaterThanEqual(localMilkSale.getSaleDate(), localMilkSale.getSaleDate());
        if (paymentCycle == null || paymentCycle.getLockBillingProcess())
            throw new BusinessValidationFailException(LocalMilkSale.class, CommonUtil.getFieldError("localMilkSale", "SaleDate", localMilkSale.getSaleDate(), "paymentcyclenotfound"));
        // pass
        String code = nextCodeService.getNextCode("LocalMilkSale", "code", localMilkSale.getSociety().getCode(), 2);
        localMilkSale.setCode(code);
        localMilkSale.setInitData();

        // check credit sale 0-Cash, 1-Credit, 2-Coupon
//        if (localMilkSale.getPaymentMode() == (short) 1) {
//            updateCreditLimit(newData, "CREATE", "Local milk sale", identityInfo);
//        }

        createAutoPosting(localMilkSale, localMilkSale.getVoucherNo());

        LocalMilkSale newData = localMilkSaleRepository.customSave(localMilkSale, identityInfo);
        newData.setMilkClass(localMilkSale.getMilkClass());
        newData.setMilkType(localMilkSale.getMilkType());
        newData.setSociety(localMilkSale.getSociety());
        newData.setShift(localMilkSale.getShift());
        newData.setDock(localMilkSale.getDock());
        return newData;
    }


    private void createAutoPosting(LocalMilkSale localMilkSale, String voucherCode) {
        try {
            Optional<FinancialYear> financialYear = financialYearRepository.findCurrentFinancialYear(localMilkSale.getSaleDate().toLocalDate());

            List<LedgerMappingEvent> eventsList = ledgerMappingEventRepository.findByEventcode(AppConstant.EventCode.LOCAL_MILK_SALE);
            if (eventsList == null || eventsList.isEmpty()) return;

            if (eventsList.stream().anyMatch(e -> e.getXCol1().equalsIgnoreCase("0"))) return;

            if (voucherCode == null) {
                voucherCode = nextCodeService.getNextCode("Voucher", "code", localMilkSale.getSociety().getCode() + "/" + financialYear.get().getCode() + "/", 6);
                if (voucherCode == null) return;


                //Voucher
                Voucher voucher = new Voucher();
                voucher.setCode(voucherCode);
                voucher.setAutoPosted(true);
                voucher.setCancelled(false);
                voucher.setBillDate(localMilkSale.getSaleDate().toLocalDate());
                voucher.setVoucherDate(localMilkSale.getSaleDate().toLocalDate());
                voucher.setRemarks("LocalMilkSale Auto posting: " + localMilkSale.getSaleDate().toString());
                voucher.setSociety(localMilkSale.getSociety());
                voucher.setVoucherType(eventsList.get(0).getVoucherType());
                voucher.setDockCode(localMilkSale.getDock().getDockNo());
                voucher.setUnionCode(localMilkSale.getUnionCode());
                voucher.setInitData();
                voucher.setBillNo(localMilkSale.getInvoiceNo());
                voucher.setFinancialYearsCode(financialYear.isPresent() ? financialYear.get().getCode() : null);
                localMilkSale.setVoucherNo(voucherCode);
                voucherRepository.save(voucher);
                //Debit Txn
                VoucherTransaction txn = null;

                int tc = 1;
                if (localMilkSale.getCoupon().compareTo(BigDecimal.ZERO) > 0 && eventsList.get(0).getDebitLedger() != null) {
                    txn = new VoucherTransaction();

                    txn.setAmount(localMilkSale.getAmount());
                    txn.setInitData();
                    txn.setCreditDebit(false);
                    txn.setLedger(eventsList.get(0).getDebitLedger());
                    txn.setNarration("Local Milk Sale On Coupon To " + localMilkSale.getConsumerType() + ": " + localMilkSale.getConsumerCode());
                    txn.setVoucher(voucher);
                    String txnCode = voucher.getCode() + "T" + tc;
                    tc++;
                    txn.setCode(txnCode);
                    voucherTxnRepository.save(txn);
                    if (eventsList.get(0).getDebitSubLedger()) {
                        Optional<SubLedger> sl = subLedgerRepository.findByReferenceCodeAndType(localMilkSale.getConsumerCode(), localMilkSale.getConsumerType());
                        if (sl.isPresent()) {
                            VoucherSubLedger vSubLedger = new VoucherSubLedger();
                            vSubLedger.setCode(txnCode + "S1");
                            vSubLedger.setAmount(localMilkSale.getCoupon());
                            vSubLedger.setInitData();
                            vSubLedger.setCreditDebit(false);
                            vSubLedger.setNarration("Local Milk Sale On Coupon To " + localMilkSale.getConsumerType() + " Code: " + localMilkSale.getConsumerCode());

                            vSubLedger.setSubLedger(sl.get());
                            vSubLedger.setVoucher(voucher);
                            vSubLedger.setVoucherTransaction(txn);
                            voucherSubLedgerRepository.save(vSubLedger);
                        }
                    }
                }
                if (localMilkSale.getCash().compareTo(BigDecimal.ZERO) > 0 && eventsList.get(1).getDebitLedger() != null) {
                    txn = new VoucherTransaction();
                    txn.setCode(voucher.getCode() + "T" + tc);
                    txn.setAmount(localMilkSale.getAmount());
                    txn.setInitData();
                    txn.setCreditDebit(false);
                    txn.setLedger(eventsList.get(1).getDebitLedger());
                    txn.setNarration("Local Milk Sale Cash To " + localMilkSale.getConsumerType() + ": " + localMilkSale.getConsumerCode());
                    txn.setVoucher(voucher);
                    voucherTxnRepository.save(txn);
                    tc++;
                }
                if (localMilkSale.getCredit().compareTo(BigDecimal.ZERO) > 0 && eventsList.get(2).getDebitLedger() != null) {
                    txn = new VoucherTransaction();
                    txn.setCode(voucher.getCode() + "T" + tc);
                    txn.setAmount(localMilkSale.getAmount());
                    txn.setInitData();
                    txn.setCreditDebit(false);
                    txn.setLedger(eventsList.get(2).getDebitLedger());
                    txn.setNarration("Local Milk Sale On Credit To " + localMilkSale.getConsumerType() + ": " + localMilkSale.getConsumerCode());
                    txn.setVoucher(voucher);
                    voucherTxnRepository.save(txn);
                    tc++;
                    if (eventsList.get(2).getDebitSubLedger()) {
                        Optional<SubLedger> sl = subLedgerRepository.findByReferenceCodeAndType(localMilkSale.getConsumerCode(), localMilkSale.getConsumerType());
                        if (sl.isPresent()) {
                            VoucherSubLedger vSubLedger = new VoucherSubLedger();
                            vSubLedger.setAmount(localMilkSale.getCredit());
                            vSubLedger.setCode(txn.getCode() + "S1");
                            vSubLedger.setInitData();
                            vSubLedger.setCreditDebit(false);
                            vSubLedger.setNarration("Local Milk Sale On Credit To " + localMilkSale.getConsumerType() + " Code: " + localMilkSale.getConsumerCode());

                            vSubLedger.setSubLedger(sl.get());
                            vSubLedger.setVoucher(voucher);
                            vSubLedger.setVoucherTransaction(txn);
                            voucherSubLedgerRepository.save(vSubLedger);
                        }
                    }
                }
                // Credit Ledger
                txn = new VoucherTransaction();
                txn.setCode(voucher.getCode() + "T" + tc);
                txn.setAmount(localMilkSale.getCash().add(localMilkSale.getCoupon().add(localMilkSale.getCredit())));
                txn.setInitData();
                txn.setCreditDebit(true);
                txn.setLedger(eventsList.get(0).getCreditLedger());
                txn.setNarration("Local Milk Sale");
                txn.setVoucher(voucher);
                voucherTxnRepository.save(txn);
                tc++;
            } else {
                //Edit
                Optional<Voucher> voucher = voucherRepository.findById(voucherCode);
                if (voucher.isEmpty()) return;
                List<VoucherTransaction> voucherTransactionList = voucherTxnRepository.findByVoucher(voucher.get());
                VoucherTransaction txn = voucherTransactionList.stream().filter(p -> p.getLedger().getCode().equalsIgnoreCase(eventsList.get(0).getDebitLedger().getCode())).findAny().orElse(null);
                if (txn != null) {
                    if (localMilkSale.getCoupon().compareTo(BigDecimal.ZERO) > 0) {
                        txn.setAmount(localMilkSale.getAmount());
                        txn.setupdateData();
                        voucherTxnRepository.save(txn);
                        if (eventsList.get(0).getDebitSubLedger()) {
                            List<VoucherSubLedger> voucherSubLedgerList = voucherSubLedgerRepository.findByVoucherTransaction(txn);
                            VoucherSubLedger vs = voucherSubLedgerList.get(0);
                            if (vs != null) {
                                vs.setAmount(localMilkSale.getAmount());
                                vs.setupdateData();
                                Optional<SubLedger> sl = subLedgerRepository.findByReferenceCodeAndType(localMilkSale.getConsumerCode(), localMilkSale.getConsumerType());
                                vs.setSubLedger(sl.get());
                                voucherSubLedgerRepository.save(vs);
                            }
                        }
                    } else {
                        List<VoucherSubLedger> voucherSubLedgerList = voucherSubLedgerRepository.findByVoucherTransaction(txn);
                        for (VoucherSubLedger voucherSubLedger : voucherSubLedgerList) {
                            voucherSubLedgerRepository.delete(voucherSubLedger);
                        }
                        voucherTxnRepository.delete(txn);
                    }
                } else {
                    if (localMilkSale.getCoupon().compareTo(BigDecimal.ZERO) > 0) {
                        txn = new VoucherTransaction();
                        String txnCode = nextCodeService.getNextCode("VoucherTransaction", "code", localMilkSale.getSociety().getCode(), 1);
                        txn.setCode(txnCode);
                        txn.setAmount(localMilkSale.getAmount());
                        txn.setInitData();
                        txn.setLedger(eventsList.get(0).getDebitLedger());
                        txn.setNarration("Local Milk Sale On Coupon to" + localMilkSale.getConsumerType() + ":" + localMilkSale.getConsumerCode());
                        txn.setVoucher(voucher.get());
                        voucherTxnRepository.save(txn);
                        if (eventsList.get(0).getDebitSubLedger()) {
                            VoucherSubLedger vs = new VoucherSubLedger();
                            String code = nextCodeService.getNextCode("VoucherSubLedger", "code", localMilkSale.getSociety().getCode(), 1);
                            vs.setCode(code);
                            vs.setAmount(localMilkSale.getAmount());
                            vs.setInitData();
                            vs.setNarration("Local Milk Sale On Coupon to" + localMilkSale.getConsumerType() + ":" + localMilkSale.getConsumerCode());
                            Optional<SubLedger> sl = subLedgerRepository.findByReferenceCodeAndType(localMilkSale.getConsumerCode(), localMilkSale.getConsumerType());
                            vs.setSubLedger(sl.get());
                            vs.setVoucherTransaction(txn);
                            vs.setVoucher(voucher.get());
                            voucherSubLedgerRepository.save(vs);
                        }
                    }

                }

                //Debit ledger cash

                txn = voucherTransactionList.stream().filter(p -> p.getLedger().getCode().equalsIgnoreCase(eventsList.get(1).getDebitLedger().getCode())).findAny().orElse(null);
                if (txn != null) {
                    if (localMilkSale.getCash().compareTo(BigDecimal.ZERO) > 0) {
                        txn.setAmount(localMilkSale.getAmount());
                        txn.setupdateData();
                        voucherTxnRepository.save(txn);
                    } else {
                        voucherTxnRepository.delete(txn);
                    }
                } else {
                    if (localMilkSale.getCash().compareTo(BigDecimal.ZERO) > 0) {
                        txn = new VoucherTransaction();
                        String txnCode = nextCodeService.getNextCode("VoucherTransaction", "code", localMilkSale.getSociety().getCode(), 1);
                        txn.setCode(txnCode);
                        txn.setAmount(localMilkSale.getAmount());
                        txn.setInitData();
                        txn.setCreditDebit(false);
                        txn.setNarration("Local Milk Sale On Cash to: " + localMilkSale.getConsumerType() + ":" + localMilkSale.getConsumerCode());
                        txn.setLedger(eventsList.get(1).getDebitLedger());
                        txn.setVoucher(voucher.get());
                        voucherTxnRepository.save(txn);
                    }
                }

                // Debit Ledger Credit
                txn = voucherTransactionList.stream().filter(p -> p.getLedger().getCode().equalsIgnoreCase(eventsList.get(2).getDebitLedger().getCode())).findAny().orElse(null);

                if (txn != null) {
                    if (localMilkSale.getCredit().compareTo(BigDecimal.ZERO) > 0) {
                        txn.setAmount(localMilkSale.getAmount());
                        txn.setupdateData();
                        voucherTxnRepository.save(txn);
                        if (eventsList.get(2).getDebitSubLedger()) {
                            List<VoucherSubLedger> voucherSubLedgerList = voucherSubLedgerRepository.findByVoucherTransaction(txn);
                            VoucherSubLedger vs = voucherSubLedgerList.get(0);
                            if (vs != null) {
                                vs.setAmount(localMilkSale.getAmount());
                                vs.setupdateData();
                                Optional<SubLedger> sl = subLedgerRepository.findByReferenceCodeAndType(localMilkSale.getConsumerCode(), localMilkSale.getConsumerType());
                                vs.setSubLedger(sl.get());
                                voucherSubLedgerRepository.save(vs);
                            }
                        }

                    } else {
                        List<VoucherSubLedger> voucherSubLedgerList = voucherSubLedgerRepository.findByVoucherTransaction(txn);
                        for (VoucherSubLedger voucherSubLedger : voucherSubLedgerList) {
                            voucherSubLedgerRepository.delete(voucherSubLedger);
                        }
                        voucherTxnRepository.delete(txn);
                    }
                } else {
                    if (localMilkSale.getCredit().compareTo(BigDecimal.ZERO) > 0) {
                        txn = new VoucherTransaction();
                        String txnCode = nextCodeService.getNextCode("VoucherTransaction", "code", localMilkSale.getSociety().getCode(), 1);
                        txn.setCode(txnCode);
                        txn.setAmount(localMilkSale.getAmount());
                        txn.setInitData();
                        txn.setCreditDebit(false);
                        txn.setNarration("Local Milk Sale On Credit to: " + localMilkSale.getConsumerType() + ":" + localMilkSale.getConsumerCode());
                        txn.setLedger(eventsList.get(1).getDebitLedger());
                        txn.setVoucher(voucher.get());
                        voucherTxnRepository.save(txn);
                        if (eventsList.get(2).getDebitSubLedger()) {
                            VoucherSubLedger vs = new VoucherSubLedger();
                            String code = nextCodeService.getNextCode("VoucherSubLedger", "code", localMilkSale.getSociety().getCode(), 1);
                            vs.setCode(code);
                            vs.setAmount(localMilkSale.getAmount());
                            vs.setInitData();
                            vs.setNarration("Local Milk Sale On Credit to" + localMilkSale.getConsumerType() + ":" + localMilkSale.getConsumerCode());
                            Optional<SubLedger> sl = subLedgerRepository.findByReferenceCodeAndType(localMilkSale.getConsumerCode(), localMilkSale.getConsumerType());
                            vs.setSubLedger(sl.get());
                            vs.setVoucherTransaction(txn);
                            vs.setVoucher(voucher.get());
                            voucherSubLedgerRepository.save(vs);
                        }
                    }
                }

                txn = voucherTransactionList.stream().filter(p -> p.getLedger().getCode().equalsIgnoreCase(eventsList.get(0).getCreditLedger().getCode())
                        || p.getLedger().getCode().equalsIgnoreCase(eventsList.get(1).getCreditLedger().getCode()) ||
                        p.getLedger().getCode().equalsIgnoreCase(eventsList.get(2).getCreditLedger().getCode())).findAny().orElse(null);

                if (txn != null) {
                    txn.setAmount(localMilkSale.getAmount());
                    txn.setupdateData();
                    voucherTxnRepository.save(txn);
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateCreditLimit(LocalMilkSale obj, String op, String val, String identityInfo) {
        Optional<MemberCreditLimit> opMemberCredit = memberCreditLimitRepository.findByConsumerCodeAndConsumerType(obj.getConsumerCode(), obj.getConsumerType());
        if (opMemberCredit.isPresent()) {
            MemberCreditLimit oldObj = opMemberCredit.get();
            oldObj.setSociety(Hibernate.unproxy(oldObj.getSociety(), Society.class));
            String code = oldObj.getCode();
            BigDecimal olVal = oldObj.getBalance();
            if (op.equals("CREATE")) {
                oldObj.setBalance(oldObj.getBalance().subtract(obj.getCredit()).setScale(2, RoundingMode.HALF_UP));
                if (oldObj.getBalance().compareTo(BigDecimal.ZERO) < 0) {
                    FieldError creditlimiterror = CommonUtil.getFieldError("localmilksale", "creditlimt", obj.getCredit(), "creditlimiterror");
                    throw new BusinessValidationFailException(getClass(), creditlimiterror);
                }
            } else if (op.equals("DELETE")) {
                oldObj.setBalance(oldObj.getBalance().add(obj.getCredit()).setScale(2, RoundingMode.HALF_UP));
            }
            oldObj.setupdateData();
            memberCreditLimitRepository.customUpdate(oldObj, identityInfo);

            MemberCreditLimitTransaction txn = new MemberCreditLimitTransaction();
            String txnCode = nextCodeService.getNextCode("MemberCreditLimitTransaction", "code", code + "T", 0);
            txn.setCode(txnCode);
            txn.setOldValue(olVal);
            txn.setNewValue(obj.getCredit());
            txn.setBalance(oldObj.getBalance());
            txn.setConsumerCode(obj.getConsumerCode());
            txn.setConsumerType(obj.getConsumerType());
            txn.setReferenceCode(obj.getCode());
            txn.setTransactionType(val);
            txn.setTransactionDate(obj.getSaleDate().toLocalDate());
            txn.setSociety(obj.getSociety());
            txn.setUnionCode(obj.getUnionCode());
            txn.setInitData();
            memberCreditLimitTxnRepository.customSave(txn, identityInfo);
        }
    }

    @Override
//    @Transactional
    public LocalMilkSale update(LocalMilkSale localMilkSale, String identityInfo) {

        SocietyPaymentCycle paymentCycle = societyPaymentCycleRepository.findTop1ByFromDateLessThanEqualAndToDateGreaterThanEqual(localMilkSale.getSaleDate(), localMilkSale.getSaleDate());
        if (paymentCycle == null || paymentCycle.getLockBillingProcess())
            throw new BusinessValidationFailException(LocalMilkSale.class, CommonUtil.getFieldError("localMilkSale", "SaleDate", localMilkSale.getSaleDate(), "paymentcyclenotfound"));


        Optional<LocalMilkSale> oldObj = localMilkSaleRepository.findById(localMilkSale.getCode());
        if (oldObj.isPresent() && oldObj.get().getPaymentMode() == (short) 1) {
            updateCreditLimit(oldObj.get(), "DELETE", "Local milk sale rev", identityInfo);
        }
        localMilkSale.setupdateData();
        LocalMilkSale newData = localMilkSaleRepository.customUpdate(localMilkSale, identityInfo);
        newData.setMilkClass(localMilkSale.getMilkClass());
        newData.setMilkType(localMilkSale.getMilkType());
        newData.setSociety(localMilkSale.getSociety());
        newData.setShift(localMilkSale.getShift());
        newData.setDock(localMilkSale.getDock());

        // check credit sale 0-Cash, 1-Credit, 2-Coupon
//        if (localMilkSale.getPaymentMode() == (short) 1) {
//            updateCreditLimit(newData, "CREATE", "Local milk sale", identityInfo);
//        }

        createAutoPosting(localMilkSale, localMilkSale.getVoucherNo());
        return newData;
    }

    @Override
    public Optional<LocalMilkSale> findById(String code) {
        return localMilkSaleRepository.findById(code);
    }

    @Override
    @Transactional
    public void delete(String code, String identityInfo) {
        Optional<LocalMilkSale> oldObj = localMilkSaleRepository.findById(code);
        if (oldObj.isPresent()) {
            // validation
            SocietyPaymentCycle paymentCycle = societyPaymentCycleRepository.findTop1ByFromDateLessThanEqualAndToDateGreaterThanEqual(oldObj.get().getSaleDate(), oldObj.get().getSaleDate());
            if (paymentCycle == null || paymentCycle.getLockBillingProcess())
                throw new BusinessValidationFailException(LocalMilkSale.class, CommonUtil.getFieldError("localMilkSale", "SaleDate", oldObj.get().getSaleDate(), "paymentcyclenotfound"));
            if (oldObj.get().getPaymentMode() == (short) 1)
                updateCreditLimit(oldObj.get(), "DELETE", "Local sale delete", identityInfo);
            localMilkSaleRepository.customDelete(oldObj.get(), identityInfo);


            if (oldObj.get().getVoucherNo() != null) {
                Optional<Voucher> voucher = voucherRepository.findById(oldObj.get().getVoucherNo());
                if (voucher.isPresent()) {
                    List<VoucherTransaction> voucherTransactionList = voucherTxnRepository.findByVoucher(voucher.get());
                    for (VoucherTransaction voucherTransaction : voucherTransactionList) {
                        List<VoucherSubLedger> voucherSubLedgerList = voucherSubLedgerRepository.findByVoucherTransaction(voucherTransaction);
                        if (!voucherSubLedgerList.isEmpty()) {
                            for (VoucherSubLedger voucherSubLedger : voucherSubLedgerList) {
                                voucherSubLedgerRepository.delete(voucherSubLedger);
                            }
                        }
                        voucherTxnRepository.delete(voucherTransaction);
                    }
                    voucherRepository.delete(voucher.get());
                }
            }
        }
    }

    @Override
    @Transactional
    public void delete(LocalMilkSale localMilkSale, String identityInfo) {
        // validation
        SocietyPaymentCycle paymentCycle = societyPaymentCycleRepository.findTop1ByFromDateLessThanEqualAndToDateGreaterThanEqual(localMilkSale.getSaleDate(), localMilkSale.getSaleDate());
        if (paymentCycle == null || paymentCycle.getLockBillingProcess())
            throw new BusinessValidationFailException(LocalMilkSale.class, CommonUtil.getFieldError("localMilkSale", "SaleDate", localMilkSale.getSaleDate(), "paymentcyclenotfound"));
        localMilkSaleRepository.customDelete(localMilkSale, identityInfo);

        Optional<Voucher> voucher = voucherRepository.findById(localMilkSale.getVoucherNo());
        if (voucher.isPresent()) {
            List<VoucherTransaction> voucherTransactionList = voucherTxnRepository.findByVoucher(voucher.get());
            for (VoucherTransaction voucherTransaction : voucherTransactionList) {
                List<VoucherSubLedger> voucherSubLedgerList = voucherSubLedgerRepository.findByVoucherTransaction(voucherTransaction);
                if (!voucherSubLedgerList.isEmpty()) {
                    for (VoucherSubLedger voucherSubLedger : voucherSubLedgerList) {
                        voucherSubLedgerRepository.delete(voucherSubLedger);
                    }
                }
                voucherTxnRepository.delete(voucherTransaction);
            }
            voucherRepository.delete(voucher.get());
        }

    }


    @Override
    public List<LocalMilkSale> migrateCollections(List<LocalMilkSale> dtoList, String header) {
        for (LocalMilkSale localMilkSale : dtoList) {
            String code = nextCodeService.getNextCode("LocalMilkSale", "code", localMilkSale.getSociety().getCode(), 0);
            String invoice = code;
            localMilkSale.setCode(code);
            localMilkSale.setInvoiceNo(invoice);
            localMilkSale.setInitData();
            localMilkSaleRepository.save(localMilkSale);
        }
        return dtoList;
    }

}