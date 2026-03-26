package com.eipl.amcs.operation.procurement.service;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.service.NextCodeService;
import com.eipl.amcs.master.account.model.*;
import com.eipl.amcs.master.account.repository.*;
import com.eipl.amcs.master.operation.model.Customer;
import com.eipl.amcs.master.operation.model.Member;
import com.eipl.amcs.master.operation.service.CustomerService;
import com.eipl.amcs.master.operation.service.MemberService;
import com.eipl.amcs.master.org.model.Society;
import com.eipl.amcs.operation.procurement.model.CouponBalance;
import com.eipl.amcs.operation.procurement.model.CouponIssue;
import com.eipl.amcs.operation.procurement.model.CouponIssueAudit;
import com.eipl.amcs.operation.procurement.repository.CouponIssueRepository;
import com.eipl.amcs.utils.AppConstant;
import com.eipl.amcs.utils.CommonUtils;
import com.eipl.amcs.utils.VoucherUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;


@Service
public class CouponIssueServiceImpl implements CouponIssueService {
    @Autowired
    private NextCodeService nextCodeService;
    @Autowired
    private CouponBalanceService couponBalanceService;
    @Autowired
    private CouponIssueRepository couponIssueRepository;
    @Autowired
    private MemberService memberService;
    @Autowired
    private CustomerService customerService;
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
    @PersistenceContext
    EntityManager entityManager;
    private static Logger logger = LogManager.getLogger(CouponIssueServiceImpl.class.getName());

    @Override
    public String getNextCode(Society society) {
        String couponIssueNo = nextCodeService.getNextCode("CouponIssue", "code", society.getCode(), 0);
        return couponIssueNo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean insert(CouponIssue couponIssue) {
        try {
            if (couponIssue.getCode() == null || couponIssue.getCode().isEmpty()) {
                couponIssue.setCode(getNextCode(couponIssue.getSociety()));
            }

            CouponBalance couponBal = couponBalanceService.fetchBalanceForConsumer(
                    couponIssue.getConsumerType(), couponIssue.getConsumerCode(),
                    couponIssue.getMilkType());
            String voucherNo = createAutoPosting(couponIssue, null, CommonUtils.setIdentityHeader());
            couponIssue.setVoucherNo(voucherNo);
            if (couponBal == null) {
                couponBal = new CouponBalance();
                couponBal.setValuesInObject(couponIssue.getConsumerCode(), couponIssue.getConsumerType(),
                        couponIssue.getAmount(), couponIssue.getMilkType());
                couponBalanceService.insert(couponBal);
            } else {
                couponBal.setValuesInObject(couponIssue.getConsumerCode(), couponIssue.getConsumerType(),
                        couponIssue.getAmount() + couponBal.getBalance(), couponIssue.getMilkType());
                couponBal.setUpdatedAt(LocalDateTime.now());
                couponBal.setUpdatedBy(MainApp.getUser() != null ? MainApp.getUser().getCode() : null);
                couponBalanceService.update(couponBal);
            }
            couponIssue.setCreatedAt(LocalDateTime.now());
            couponIssue.setCreatedBy(MainApp.getUser() != null ? MainApp.getUser().getCode() : null);
            couponIssueRepository.customSave(couponIssue, CommonUtils.setIdentityHeader());

            return true;
        } catch (Exception e) {
            throw new RuntimeException("Failed to insert CouponIssue: " + e.getMessage(), e);
        }
    }

    private String createAutoPosting(CouponIssue couponIssue, String voucherCode, String identityInfo) {
        try {
            if (voucherCode == null) {
                List<LedgerMappingEvent> eventsList = ledgerMappingEventRepository.findByEventcode(AppConstant.EventCode.COUPON_ISSUE);
                if (eventsList == null || eventsList.isEmpty()) {
                    throw new RuntimeException("Ledger Mapping not found for Coupon Issue (Event Code: " + AppConstant.EventCode.COUPON_ISSUE + ")");
                }
                LedgerMappingEvent mappingEvent = eventsList.get(0);
                if (mappingEvent.getVoucherType() == null) {
                    throw new RuntimeException("Voucher Type is not configured in Ledger Mapping for Coupon Issue (Event Code: " + AppConstant.EventCode.COUPON_ISSUE + ")");
                }
                if (mappingEvent.getCreditLedger() == null) {
                    throw new RuntimeException("Credit Ledger is not configured in Ledger Mapping for Coupon Issue (Event Code: " + AppConstant.EventCode.COUPON_ISSUE + ")");
                }
                if (mappingEvent.getDebitLedger() == null) {
                    throw new RuntimeException("Debit Ledger is not configured in Ledger Mapping for Coupon Issue (Event Code: " + AppConstant.EventCode.COUPON_ISSUE + ")");
                }

                Optional<FinancialYear> financialYearOpt = financialYearRepository.findCurrentFinancialYear(couponIssue.getIssueDate());
                if (!financialYearOpt.isPresent()) {
                    throw new RuntimeException("Financial Year not found for Issue Date: " + couponIssue.getIssueDate());
                }
                String finYearCode = financialYearOpt.get().getCode();

                voucherCode = nextCodeService.getNextCode("Voucher", "code",
                        couponIssue.getSociety().getCode() + "/" + finYearCode + "/", 0);
                if (voucherCode == null) {
                    throw new RuntimeException("Failed to generate Voucher Code");
                }

                // Voucher
                Voucher voucher = VoucherUtil.getVoucherInstance(voucherCode, String.valueOf(couponIssue.getIssueDate()), couponIssue.getIssueDate(),
                        couponIssue.getIssueDate(), mappingEvent.getVoucherNarration() + mappingEvent.getVoucherType(),
                        mappingEvent.getVoucherType(), finYearCode,
                        couponIssue.getSociety(), MainApp.identityDto.getUnion().getCode(), MainApp.identityDto.getSociety().getCode() + "01");
//   UPDATE in model
//                voucher.setProcessName("Coupon Issue");
//                voucher.setProcessReference(couponIssue.getCode());
                voucher.setVoucherTransactions(new ArrayList<>());

                BigDecimal amount = BigDecimal.valueOf(couponIssue.getAmount());

                long voucherSubLedgerCode = 0;
                if (Boolean.TRUE.equals(mappingEvent.getCreditSubLedger()) || Boolean.TRUE.equals(mappingEvent.getDebitSubLedger())) {
                    voucherSubLedgerCode = Long.parseLong(nextCodeService.getNextCode("VoucherSubLedger", "code", couponIssue.getSociety().getCode(), 0));
                }

                // Credit Transaction
                VoucherTransaction creditTxn = VoucherUtil.getVoucherTxn(voucher, amount, true, mappingEvent.getCreditLedger(),
                        "Product receipt " + couponIssue.getCode(), "1");

                if (Boolean.TRUE.equals(mappingEvent.getCreditSubLedger())) {
                    List<String> refCodeList = new ArrayList<>();
                    refCodeList.add(couponIssue.getConsumerCode());
                    List<SubLedger> subLedgerList = subLedgerRepository.findAllByTypeAndReferenceCodeIn((short) 1, refCodeList);
                    Optional<SubLedger> subLedger = subLedgerList.stream().findFirst();
                    if (subLedger.isPresent()) {
                        creditTxn.setVoucherSubLedgers(new ArrayList<>());
                        VoucherSubLedger voucherSubLedger = new VoucherSubLedger();
                        voucherSubLedger.setCode(String.valueOf(voucherSubLedgerCode));
                        voucherSubLedger.setVoucher(voucher);
                        voucherSubLedger.setCreditDebit(true);
                        voucherSubLedger.setVoucherTransaction(creditTxn);
                        voucherSubLedger.setSubLedger(subLedger.get());
                        voucherSubLedger.setAmount(amount);
                        voucherSubLedger.setNarration("Product receipt " + couponIssue.getCode());

                        voucherSubLedger.setCreatedAt(LocalDateTime.now());
                        voucherSubLedger.setCreatedBy(MainApp.getUser() != null ? MainApp.getUser().getCode() : null);

                        creditTxn.getVoucherSubLedgers().add(voucherSubLedger);
                        voucherSubLedgerCode++;
                    }
                }
                voucher.getVoucherTransactions().add(creditTxn);

                // Debit Transaction
                VoucherTransaction debitTxn = VoucherUtil.getVoucherTxn(voucher, amount, false, mappingEvent.getDebitLedger(),
                        "Coupon Issue: " + couponIssue.getCode(), "2");

                if (Boolean.TRUE.equals(mappingEvent.getDebitSubLedger())) {
                    List<String> refCodeList = new ArrayList<>();
                    refCodeList.add(couponIssue.getConsumerCode());
                    List<SubLedger> subLedgerList = subLedgerRepository.findAllByTypeAndReferenceCodeIn((short) 1, refCodeList);
                    Optional<SubLedger> subLedger = subLedgerList.stream().findFirst();
                    if (subLedger.isPresent()) {
                        debitTxn.setVoucherSubLedgers(new ArrayList<>());
                        VoucherSubLedger voucherSubLedger = new VoucherSubLedger();
                        voucherSubLedger.setCode(String.valueOf(voucherSubLedgerCode));
                        voucherSubLedger.setVoucher(voucher);
                        voucherSubLedger.setCreditDebit(false);
                        voucherSubLedger.setVoucherTransaction(debitTxn);
                        voucherSubLedger.setSubLedger(subLedger.get());
                        voucherSubLedger.setAmount(amount);
                        voucherSubLedger.setNarration("Coupon Issue: " + couponIssue.getCode());

                        voucherSubLedger.setCreatedAt(LocalDateTime.now());
                        voucherSubLedger.setCreatedBy(MainApp.getUser() != null ? MainApp.getUser().getCode() : null);

                        debitTxn.getVoucherSubLedgers().add(voucherSubLedger);
                        voucherSubLedgerCode++;
                    }
                }
                voucher.getVoucherTransactions().add(debitTxn);

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
                Optional<Voucher> voucher = voucherRepository.findById(voucherCode);
                if (voucher.isPresent()) {
                    List<VoucherTransaction> voucherTransactionList = voucherTxnRepository.findByVoucher(voucher.get());
                    for (VoucherTransaction voucherTransaction : voucherTransactionList) {
                        List<VoucherSubLedger> subLedgers = voucherSubLedgerRepository.findByVoucherTransaction(voucherTransaction);
                        voucherSubLedgerRepository.deleteAll(subLedgers);
                        voucherTxnRepository.delete(voucherTransaction);
                    }
                    voucherRepository.delete(voucher.get());
                }
            }
        } catch (Exception e) {
            logger.error("AutoPosting Error for CouponIssue " + couponIssue.getCode(), e);
            throw new RuntimeException("AutoPosting Failed: " + e.getMessage(), e);
        }
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean update(CouponIssue updatedCouponIssue) {
        try {
            CouponIssue couponIssuePrev = fetchByCode(updatedCouponIssue.getCode());
            CouponBalance couponBalancePrev = couponBalanceService.fetchBalanceForConsumer(couponIssuePrev.getConsumerType(),
                    couponIssuePrev.getConsumerCode(), couponIssuePrev.getMilkType());

            CouponBalance couponBalanceNew = couponBalanceService.fetchBalanceForConsumer(updatedCouponIssue.getConsumerType(),
                    updatedCouponIssue.getConsumerCode(), updatedCouponIssue.getMilkType());

            // Delete previous voucher
            if (couponIssuePrev.getVoucherNo() != null) {
                createAutoPosting(couponIssuePrev, couponIssuePrev.getVoucherNo(), CommonUtils.setIdentityHeader());
            }
            // Create new voucher
            String voucherNo = createAutoPosting(updatedCouponIssue, null, CommonUtils.setIdentityHeader());
            updatedCouponIssue.setVoucherNo(voucherNo);

            if (!Objects.equals(couponIssuePrev.getMilkType().getCode(), updatedCouponIssue.getMilkType().getCode()) || !Objects.equals(couponIssuePrev.getConsumerCode(), updatedCouponIssue.getConsumerCode())
                    || !Objects.equals(couponIssuePrev.getConsumerType(), updatedCouponIssue.getConsumerType())) {
                if (couponBalancePrev.getBalance() - (couponIssuePrev.getAmount()) >= 0) {
                    if (couponBalanceNew == null) {
                        couponBalanceNew = new CouponBalance();
                        couponBalanceNew.setValuesInObject(updatedCouponIssue.getConsumerCode(), updatedCouponIssue.getConsumerType(),
                                updatedCouponIssue.getAmount(), updatedCouponIssue.getMilkType());
                        couponBalanceService.insert(couponBalanceNew);
                    } else {
                        couponBalanceNew.setValuesInObject(updatedCouponIssue.getConsumerCode(), updatedCouponIssue.getConsumerType(),
                                couponBalanceNew.getBalance() + (updatedCouponIssue.getAmount()),
                                updatedCouponIssue.getMilkType());
                        couponBalanceService.update(couponBalanceNew);
                    }
                    couponBalancePrev.setValuesInObject(couponIssuePrev.getConsumerCode(), couponIssuePrev.getConsumerType(),
                            couponBalancePrev.getBalance() - (couponIssuePrev.getAmount()),
                            couponIssuePrev.getMilkType());
                    couponBalanceService.update(couponBalancePrev);
                } else {
                    throw new RuntimeException("insufficient.balance");
                }
            } else {
                if (couponBalanceNew != null && (couponBalanceNew.getBalance() + (updatedCouponIssue.getAmount() - couponIssuePrev.getAmount())) >= 0) {
                    couponBalanceNew.setValuesInObject(updatedCouponIssue.getConsumerCode(), updatedCouponIssue.getConsumerType(),
                            couponBalanceNew.getBalance() + (updatedCouponIssue.getAmount() - couponIssuePrev.getAmount()),
                            updatedCouponIssue.getMilkType());
                    couponBalanceService.update(couponBalanceNew);
                } else {
                    throw new RuntimeException("insufficient.balance");
                }
            }
            couponIssueRepository.customUpdate(updatedCouponIssue, CommonUtils.setIdentityHeader());
            return true;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean delete(CouponIssue couponIssue) {
        try {

            CouponIssue couponIssuePrev = fetchByCode(couponIssue.getCode());

            CouponBalance couponBal = couponBalanceService.fetchBalanceForConsumer(couponIssue.getConsumerType(),
                    couponIssue.getConsumerCode(), couponIssue.getMilkType());
            if (couponBal != null) {
                if ((couponBal.getBalance() - couponIssuePrev.getAmount()) < 0)
                    throw new Exception("insufficient.balance");


                couponBal.setValuesInObject(couponIssue.getConsumerCode(), couponIssue.getConsumerType(),
                        couponBal.getBalance() - couponIssuePrev.getAmount(), couponIssue.getMilkType());
                couponBal.setUpdatedAt(LocalDateTime.now());
                couponBal.setUpdatedBy(MainApp.getUser() != null ? MainApp.getUser().getCode() : null);
                couponBalanceService.update(couponBal);
            }

            // Delete  voucher
            if (couponIssuePrev.getVoucherNo() != null) {
                createAutoPosting(couponIssuePrev, couponIssuePrev.getVoucherNo(), CommonUtils.setIdentityHeader());
            }

            CouponIssueAudit audit = (CouponIssueAudit) couponIssuePrev.getAuditModel("DELETE", MainApp.getUser() != null ? MainApp.getUser().getCode() : "System");
            entityManager.persist(audit);

            couponIssueRepository.deleteByCode(couponIssue.getCode());
            return true;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

    }

    @Override
    public List<CouponIssue> fetchAll() {
        List<CouponIssue> listAll = couponIssueRepository.findAll(Sort.by("createdAt").descending());

        for (CouponIssue item : listAll) {
            if (item.getConsumerType() != null && item.getConsumerCode() != null) {
                int type = item.getConsumerType();
                if (type == 1 || type == 2) {
                    item.setConsumerTypeString("member");
                    Member m = memberService.findByMemberCode(item.getConsumerCode());
                    if (m != null) item.setConsumerName(m.getFirstName());

                } else if (type == 3 || type == 4) {
                    item.setConsumerTypeString(type == 3 ? "institute" : "retail sale");
                    Customer c = customerService.findByCustomerCode(item.getConsumerCode());
                    if (c != null) item.setConsumerName(c.getName());

                } else {
                    item.setConsumerTypeString("consumer");
                    Customer c = customerService.findByCustomerCode(item.getConsumerCode());
                    if (c != null) {
                        item.setConsumerName(c.getName());
                    } else {
                        item.setConsumerName(item.getConsumerCode());
                    }
                }
                try {
                    CouponBalance couponBal = couponBalanceService.fetchBalanceForConsumer(
                            item.getConsumerType(), item.getConsumerCode(),
                            item.getMilkType());
                    if (couponBal != null) {
                        item.setBalance(couponBal.getBalance());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (item.getConsumerName() == null || item.getConsumerName().isEmpty()) {
                item.setConsumerName(item.getConsumerCode());
            }
        }
        return listAll;
    }


    public CouponIssue fetchByCode(String strCode) {
        CouponIssue couponIssue = couponIssueRepository.findByCode(strCode);
        return couponIssue;
    }
}
