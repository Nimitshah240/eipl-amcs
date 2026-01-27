package com.eipl.amcs.operation.procurement.service;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.service.NextCodeService;
import com.eipl.amcs.master.operation.model.Customer;
import com.eipl.amcs.master.operation.model.Member;
import com.eipl.amcs.master.operation.service.CustomerService;
import com.eipl.amcs.master.operation.service.MemberService;
import com.eipl.amcs.master.org.model.Society;
import com.eipl.amcs.operation.procurement.model.CouponBalance;
import com.eipl.amcs.operation.procurement.model.CouponBalanceTransaction;
import com.eipl.amcs.operation.procurement.model.CouponIssue;
import com.eipl.amcs.operation.procurement.repository.CouponIssueRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;


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
    @PersistenceContext
    EntityManager entityManager;
    private static Logger logger = LogManager.getLogger(CouponIssueServiceImpl.class.getName());

    @Override
    public String getNextCode(Society society) {
        String couponIssueNo = nextCodeService.getNextCode("CouponIssue", "couponIssueNo", MainApp.identityDto.getSociety().getCode()
                , 0);
        return couponIssueNo;
    }

    @Override
    public boolean insert(CouponIssue couponIssue, int intType, String strSourceOrgType, String strOperationType) {
        try {
            if (intType == 1) {
                String voucherNo = createAcVoucher(couponIssue, null);
                couponIssue.setVoucherNo(voucherNo);
            }

            if (intType == 1) {
                CouponBalance couponBal = couponBalanceService.fetchBalanceForConsumer(
                        couponIssue.getConsumerType(), couponIssue.getConsumerCode(),
                        couponIssue.getMilkType(), couponIssue.getMilkClass());

                if (couponBal == null) {
                    couponBal = new CouponBalance();
                    couponBal.setValuesInObject(couponIssue.getConsumerCode(), couponIssue.getConsumerType(),
                            couponIssue.getAmount(), couponIssue.getMilkType(), couponIssue.getMilkClass());
                    couponBalanceService.insert(couponBal, intType, strSourceOrgType, MainApp.OPERATION_CREATE);

                    CouponBalanceTransaction txn = new CouponBalanceTransaction();
                    txn.setValuesInObject(couponIssue.getConsumerCode(), couponIssue.getConsumerType(),
                            couponIssue.getMilkClass(), couponIssue.getMilkType(), couponBal.getCouponBalanceCode(),
                            "Coupon Issue", 0, couponIssue.getAmount(), couponIssue.getAmount());
                    couponBalanceService.insert(txn, intType, strSourceOrgType, MainApp.OPERATION_CREATE);
                } else {
                    couponBal.setValuesInObject(couponIssue.getConsumerCode(), couponIssue.getConsumerType(),
                            couponIssue.getAmount() + couponBal.getBalance(), couponIssue.getMilkType(),
                            couponIssue.getMilkClass());
                    couponBal.setUpdatedAt(LocalDateTime.now());
                    couponBal.setUpdatedBy(MainApp.getUser() != null ? MainApp.getUser().getCode() : null);
                    couponBalanceService.update(couponBal, intType, strSourceOrgType, MainApp.OPERATION_CREATE);

                    CouponBalanceTransaction prevTxn = couponBalanceService.fetchPrevTxn(
                            couponIssue.getConsumerType(), couponIssue.getConsumerCode(),
                            couponIssue.getMilkType(), couponIssue.getMilkClass());

                    CouponBalanceTransaction txn = new CouponBalanceTransaction();
                    txn.setValuesInObject(couponIssue.getConsumerCode(), couponIssue.getConsumerType(),
                            couponIssue.getMilkClass(), couponIssue.getMilkType(), couponBal.getCouponBalanceCode(),
                            "Coupon Issue", prevTxn.getBalance(), couponIssue.getAmount(),
                            prevTxn.getBalance() + couponIssue.getAmount());
                    couponBalanceService.insert(txn, intType, strSourceOrgType, MainApp.OPERATION_CREATE);
                }
            }

            couponIssueRepository.save(couponIssue);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to insert CouponIssue: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean update(CouponIssue couponIssue, int intType, String strSourceOrgType, String strOperationType) {
        try {
            if (intType == 1) {
                CouponIssue couponIssuePrev = fetchByCode(couponIssue.getCouponIssueNo());

                CouponBalance couponBal = couponBalanceService.fetchBalanceForConsumer(couponIssue.getConsumerType(),
                        couponIssue.getConsumerCode(), couponIssue.getMilkType(), couponIssue.getMilkClass());
                if (couponBal != null) {
//                    if (couponBal.getBalance() + (couponIssue.getAmount() - couponIssuePrev.getAmount()) < 0)
//                        throw new Exception();
//
//                    try {
//                        CouponBalance prevObj = couponBalanceService.fetchByCode(couponBal.getCouponBalanceCode());
//                        if (prevObj != null) {
//                            CouponBalanceHistory couponBalHistory = prevObj.clone();
//                            couponBalHistory.setOperationType(strOperationType);
//                            entityManager.persist(couponBalHistory);
//                        }
//                    } catch (CloneNotSupportedException e) {
//                    }

                    couponBal.setValuesInObject(couponIssue.getConsumerCode(), couponIssue.getConsumerType(),
                            couponBal.getBalance() + (couponIssue.getAmount() - couponIssuePrev.getAmount()),
                            couponIssue.getMilkType(), couponIssue.getMilkClass());
                    couponBal.setUpdatedAt(LocalDateTime.now());
                    couponBal.setUpdatedBy(MainApp.getUser() != null ? MainApp.getUser().getCode() : null);
                    couponBalanceService.update(couponBal, intType, strSourceOrgType, MainApp.OPERATION_UPDATE);

                    CouponBalanceTransaction prevTxn = couponBalanceService.fetchPrevTxn(couponIssue.getConsumerType(),
                            couponIssue.getConsumerCode(), couponIssue.getMilkType(), couponIssue.getMilkClass());
                    CouponBalanceTransaction txn = new CouponBalanceTransaction();
                    txn.setValuesInObject(couponIssue.getConsumerCode(), couponIssue.getConsumerType(),
                            couponIssue.getMilkClass(), couponIssue.getMilkType(), couponBal.getCouponBalanceCode(),
                            "Coupon Issue Edit", prevTxn.getBalance(),
                            couponIssue.getAmount() - couponIssuePrev.getAmount(),
                            prevTxn.getBalance() + couponIssue.getAmount() - couponIssuePrev.getAmount());
                    couponBalanceService.insert(txn, intType, strSourceOrgType, MainApp.OPERATION_UPDATE);
                }
            }
             couponIssueRepository.save(couponIssue);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to insert CouponIssue: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean delete(CouponIssue couponIssue, int intType, String strSourceOrgType, String strOperationType) {
        try {
            if (intType == 1) {
                CouponIssue couponIssuePrev = fetchByCode(couponIssue.getCouponIssueNo());

                CouponBalance couponBal = couponBalanceService.fetchBalanceForConsumer(couponIssue.getConsumerType(),
                        couponIssue.getConsumerCode(), couponIssue.getMilkType(), couponIssue.getMilkClass());
                if (couponBal != null) {
                    if (couponBal.getBalance() + (0 - couponIssuePrev.getAmount()) < 0)
                        throw new Exception();

//                    try {
//                       CouponBalance prevObj = couponBalanceService.fetchByCode(couponBal.getCouponBalanceCode());
//                        if (prevObj != null) {
//                            CouponBalanceHistory couponBalHistory = prevObj.clone();
//                            couponBalHistory.setOperationType(strOperationType);
//                            entityManager.persist(couponBalHistory);
//                        }
//                    } catch (CloneNotSupportedException e) {
//                    }

                    couponBal.setValuesInObject(couponIssue.getConsumerCode(), couponIssue.getConsumerType(),
                            couponBal.getBalance() + (0 - couponIssuePrev.getAmount()), couponIssue.getMilkType(),
                            couponIssue.getMilkClass());
                    couponBal.setUpdatedAt(LocalDateTime.now());
                    couponBal.setUpdatedBy(MainApp.getUser() != null ? MainApp.getUser().getCode() : null);
                    couponBalanceService.update(couponBal, intType, strSourceOrgType, MainApp.OPERATION_UPDATE);

                    CouponBalanceTransaction prevTxn = couponBalanceService.fetchPrevTxn(couponIssue.getConsumerType(),
                            couponIssue.getConsumerCode(), couponIssue.getMilkType(), couponIssue.getMilkClass());
                    CouponBalanceTransaction txn = new CouponBalanceTransaction();
                    txn.setValuesInObject(couponIssue.getConsumerCode(), couponIssue.getConsumerType(),
                            couponIssue.getMilkClass(), couponIssue.getMilkType(), couponBal.getCouponBalanceCode(),
                            "Coupon Issue Delete", prevTxn.getBalance(), 0 - couponIssuePrev.getAmount(),
                            prevTxn.getBalance() + 0 - couponIssuePrev.getAmount());
                    couponBalanceService.insert(txn, intType, strSourceOrgType, MainApp.OPERATION_CREATE);
                }
            }
             couponIssueRepository.deleteByCouponIssueNo(couponIssue.getCouponIssueNo());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to insert CouponIssue: " + e.getMessage(), e);
        }

    }

    @Override
    public List<CouponIssue> fetchAll() {
        List<CouponIssue> listAll = couponIssueRepository.findAll();

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
            }
            if (item.getConsumerName() == null || item.getConsumerName().isEmpty()) {
                item.setConsumerName(item.getConsumerCode());
            }
        }

        listAll.sort(Comparator.comparing(CouponIssue::getCreatedAt).reversed());
        return listAll;
    }


    public CouponIssue fetchByCode(String strCode) {
        CouponIssue couponIssue = couponIssueRepository.findByCouponIssueNo(strCode);
        return couponIssue;
    }

    @Override
    public LocalDate fetchLatestDateByMember(String code, int intConsumerType, CouponIssue couponIssue) {
        try {
            String excludeNo = (couponIssue != null && couponIssue.getCouponIssueNo() != null)
                    ? couponIssue.getCouponIssueNo() : "";
            CouponIssue latest = couponIssueRepository.findTopByConsumerCodeAndConsumerTypeAndIsDeleteFalseAndCouponIssueNoNotOrderByIssueDateDesc(
                    code, intConsumerType, excludeNo);
            return (latest != null) ? latest.getIssueDate() : null;
        } catch (Exception e) {
            logger.error("Error in fetchLatestDateByMember: {}", e.getMessage());
            return null;
        }
    }

    @Override
    public double fetchAllByMemberExceptCurrent(CouponIssue couponIssue) {
        int one = 1;
        int two = 2;
        if (couponIssue.getConsumerType() == 3 || couponIssue.getConsumerType() == 4) {
            one = 3;
            two = 4;
        }

        String currentNo = (couponIssue.getCouponIssueNo() == null) ? "" : couponIssue.getCouponIssueNo();

        try {
            Double result = couponIssueRepository.sumAmountByMemberExceptCurrent(
                    one,
                    two,
                    couponIssue.getConsumerCode(),
                    couponIssue.getIssueDate(),
                    false, // isDelete
                    couponIssue.getMilkType(),
                    couponIssue.getMilkClass(),
                    MainApp.getFinancialYear().getStartDate(),
                    MainApp.getFinancialYear().getEndDate(),
                    currentNo
            );

            return (result != null) ? result : 0.0;
        } catch (Exception e) {
            return 0.0;
        }
    }



    private String createAcVoucher(CouponIssue couponIssue, String voucherNo) {
        try {
//            List<LedgerMappingEvent> listMapping = mappingEventService.fetchByEventCode(2);
//            if (listMapping != null && !listMapping.isEmpty()) {
//                LedgerMappingEvent mapping = listMapping.get(0);
//                if (mapping != null && mapping.getCreditLedgerCode() != null && mapping.getDebitLedgerCode() != null
//                        && mapping.getVoucherTypeCode() != null && mapping.getPostingTypeCode().getId() == 1) {
//                    if (voucherNo == null) {
//                        voucherNo = voucherService.getNextVoucherNo();
//
//                        if (voucherNo != null) {
//                            Voucher voucher = new Voucher();
//                            voucher.setCancelled(false);
//                            voucher.setCollectionPointNo(Main.currentIdentity.getCollectionPointCode());
//                            voucher.setCreatedAt(LocalDateTime.now());
//                            voucher.setCreatedBy(Main.user.getId());
//                            voucher.setDcsCode(Main.currentIdentity.getDcsCode());
//                            voucher.setFinancialYearCode(Main.financialYear);
//                            voucher.setFlgSentboxEntry('Y');
//                            voucher.setRemarks("Coupon Issue Auto Posting");
//                            voucher.setSubCenterCode(Main.currentIdentity.getSubCenterCode());
//                            voucher.setSyncStatus('U');
//                            voucher.setUnionCode(Main.identityUnion);
//                            voucher.setVoucherDate(LocalDate.now());
//                            voucher.setBillDate(couponIssue.getIssueDate());
//                            voucher.setVoucherNo(voucherNo);
//                            voucher.setVoucherTypeCode(mapping.getVoucherTypeCode());
//                            voucher.setAutoPosted(true);
//
//                            voucher.setListVoucherTxn(new ArrayList<>());
//
//                            // Debit Txn
//                            VoucherTxn txn = new VoucherTxn();
//                            txn.setAmount(couponIssue.getAmount());
//                            txn.setCreatedAt(LocalDateTime.now());
//                            txn.setCreatedBy(Main.user.getId());
//                            txn.setCreditDebit(false);
//                            txn.setFlgSentboxEntry('Y');
//                            txn.setLedgerCode(mapping.getDebitLedgerCode());
//                            txn.setNarration("Coupon Issue Debit Ac Posting");
//                            txn.setSyncStatus('U');
//                            txn.setVoucherNo(voucher);
//                            voucher.getListVoucherTxn().add(txn);
//
//                            // Credit Ledger
//                            txn = new VoucherTxn();
//                            txn.setAmount(couponIssue.getAmount());
//                            txn.setCreatedAt(LocalDateTime.now());
//                            txn.setCreatedBy(Main.user.getId());
//                            txn.setCreditDebit(true);
//                            txn.setFlgSentboxEntry('Y');
//                            txn.setLedgerCode(mapping.getCreditLedgerCode());
//                            txn.setNarration("Coupon Issue Credit Ac Posting");
//                            txn.setSyncStatus('U');
//                            txn.setVoucherNo(voucher);
//                            if (mapping.isCreditSubLedger()) {
//                                VoucherSubLedger vSubLedger = new VoucherSubLedger();
//                                vSubLedger.setAmount(couponIssue.getAmount());
//                                vSubLedger.setCreatedAt(LocalDateTime.now());
//                                vSubLedger.setCreatedBy(Main.user.getId());
//                                vSubLedger.setCreditDebit(true);
//                                vSubLedger.setFlgSentboxEntry('Y');
//                                vSubLedger.setNarration(
//                                        "Coupon Issue To " + TypeNameUtil.getName(couponIssue.getConsumerType())
//                                                + " Code: " + couponIssue.getConsumerCode());
//                                SubLedger sl = subLedgerService.fetchByReferenceCode(couponIssue.getConsumerCode(),
//                                        couponIssue.getConsumerType());
//                                vSubLedger.setSubLedgerCode(sl);
//                                vSubLedger.setSyncStatus('U');
//                                vSubLedger.setVoucherNo(voucher);
//                                vSubLedger.setVoucherTxnCode(txn);
//
//                                if (txn.getListVoucherSubLedger() == null)
//                                    txn.setListVoucherSubLedger(new ArrayList<>());
//
//                                txn.getListVoucherSubLedger().add(vSubLedger);
//                            }
//                            voucher.getListVoucherTxn().add(txn);
//                            voucherService.insert(voucher, Main.OPERATION_SOURCE, Main.SOURCE_RECORD_ORG_TYPE,
//                                    Main.OPERATION_CREATE);
//                            return voucherNo;
//                        } else {
//                            return null;
//                        }
//                    } else {
//                        Voucher temp = voucherService.fetchVoucherDetail(voucherNo);
//
//                        // Debit Ledger
//                        VoucherTxn txn = temp.getListVoucherTxn().stream().filter(p -> p.getLedgerCode().getLedgerCode()
//                                .equals(mapping.getDebitLedgerCode().getLedgerCode())).findAny().orElse(null);
//                        if (txn != null) {
//                            txn.setAmount(couponIssue.getAmount());
//                            txn.setUpdatedAt(LocalDateTime.now());
//                            txn.setUpdatedBy(Main.user.getId());
//                            voucherService.update(txn, Main.OPERATION_SOURCE, Main.SOURCE_RECORD_ORG_TYPE,
//                                    Main.OPERATION_UPDATE);
//
//                        }
//
//                        // Credit Ledger
//                        txn = temp.getListVoucherTxn().stream().filter(p -> p.getLedgerCode().getLedgerCode()
//                                .equals(mapping.getCreditLedgerCode().getLedgerCode())).findAny().orElse(null);
//                        if (txn != null) {
//                            txn.setAmount(couponIssue.getAmount());
//                            txn.setUpdatedAt(LocalDateTime.now());
//                            txn.setUpdatedBy(Main.user.getId());
//                            voucherService.update(txn, Main.OPERATION_SOURCE, Main.SOURCE_RECORD_ORG_TYPE,
//                                    Main.OPERATION_UPDATE);
//                            if (mapping.isCreditSubLedger()) {
//
//                                VoucherSubLedger vSubLedger = txn.getListVoucherSubLedger() != null
//                                        && !txn.getListVoucherSubLedger().isEmpty()
//                                        ? txn.getListVoucherSubLedger().get(0)
//                                        : null;
//                                if (vSubLedger != null) {
//                                    vSubLedger.setAmount(couponIssue.getAmount());
//                                    vSubLedger.setUpdatedAt(LocalDateTime.now());
//                                    vSubLedger.setUpdatedBy(Main.user.getId());
//                                    SubLedger sl = subLedgerService.fetchByReferenceCode(couponIssue.getConsumerCode(),
//                                            couponIssue.getConsumerType());
//                                    vSubLedger.setSubLedgerCode(sl);
//                                    voucherService.update(vSubLedger, Main.OPERATION_SOURCE,
//                                            Main.SOURCE_RECORD_ORG_TYPE, Main.OPERATION_UPDATE);
//                                }
//                            }
//                        }
//                        return voucherNo;
//                    }
//                }
//            }
            return null;
        } catch (Exception e) {
            logger.catching(e);
            return voucherNo;
        }
    }
}
