package com.eipl.amcs.operation.procurement.service;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.service.NextCodeService;
import com.eipl.amcs.master.global.model.MilkClass;
import com.eipl.amcs.master.global.model.MilkType;
import com.eipl.amcs.operation.procurement.model.CouponBalance;
import com.eipl.amcs.operation.procurement.model.CouponBalanceAudit;
import com.eipl.amcs.operation.procurement.model.CouponBalanceTransaction;
import com.eipl.amcs.operation.procurement.repository.CouponBalanceAuditRepository;
import com.eipl.amcs.operation.procurement.repository.CouponBalanceRepository;
import com.eipl.amcs.operation.procurement.repository.CouponBalanceTransactionRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;

@Service
public class CouponBalanceServiceImpl implements CouponBalanceService{

    @PersistenceContext
    EntityManager entityManager;
    @Autowired
    private NextCodeService nextCodeService;
    @Autowired
    private CouponBalanceRepository couponBalanceRepository;
    @Autowired
    private CouponBalanceAuditRepository couponBalanceAuditRepository;
    @Autowired
    private CouponBalanceTransactionRepository txnRepo;

    private static Logger logger = LogManager.getLogger(CouponBalanceServiceImpl.class.getName());


    @Override
    public CouponBalance fetchBalanceForConsumer(int consumerType, String consumerCode, MilkType animalType, MilkClass milkClass) throws Exception {
        try {
            CouponBalance bal = couponBalanceRepository.findFirstByConsumerTypeAndConsumerCodeAndMilkTypeAndMilkClass(
                    consumerType, consumerCode, animalType, milkClass);
            logger.traceExit("fetch balance");
            return bal;
        } catch (Exception e) {
            logger.catching(e);
            throw new Exception("Error fetching coupon balance", e);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean insert(CouponBalance couponBalance, int intType, String strSourceOrgType, String strOperationType) {
        try {
            logger.traceEntry("insert coupon bal");
            if (couponBalance.getCouponBalanceCode() == null || couponBalance.getCouponBalanceCode().isEmpty())
                couponBalance.setCouponBalanceCode(nextCodeService.getNextCode("CouponBalance", "couponBalanceCode",
                        MainApp.identityDto.getSociety().getCode(), 0));
            couponBalanceRepository.save(couponBalance);

            logger.traceExit("insert");
            return true;
        } catch (PersistenceException e) {
            logger.catching(e);
            return false;
        }
    }

    @Override
    public boolean update(CouponBalance couponBalance, int intType, String strSourceOrgType, String strOperationType) {
        try {
            logger.traceEntry("update coupon balance: {}", couponBalance.getCouponBalanceCode());

            if (intType == 2 && MainApp.OPERATION_UPDATE.equals(strOperationType)) {
                try {
                    CouponBalance prevObj = couponBalanceRepository.findByCouponBalanceCode(couponBalance.getCouponBalanceCode());
                    if (prevObj != null) {
                        CouponBalanceAudit history = new CouponBalanceAudit();
                        history.setOperationType(strOperationType);
                        couponBalanceAuditRepository.save(history);
                    }
                } catch (Exception e) {
                    logger.error("Failed to create CouponBalance history", e);
                }
            }
            couponBalanceRepository.save(couponBalance);
            logger.traceExit("update");
            return true;
        } catch (PersistenceException e) {
            logger.catching(e);
            return false;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean insert(CouponBalanceTransaction couponBalanceTxn, int intType, String strSourceOrgType, String strOperationType) {
        try {
            logger.traceEntry("insert coupon bal txn");
            if (couponBalanceTxn.getCouponBalanceTransactionCode() == null
                    || couponBalanceTxn.getCouponBalanceTransactionCode().isEmpty())
                couponBalanceTxn.setCouponBalanceTransactionCode(nextCodeService.getNextCode("CouponBalanceTransaction",
                        "couponBalanceTransactionCode", MainApp.identityDto.getSociety().getCode(), 0));
            txnRepo.save(couponBalanceTxn);
            logger.traceExit("insert");
            return true;
        } catch (PersistenceException e) {
            logger.catching(e);
            return false;
        }
    }

    @Override
    public CouponBalanceTransaction fetchPrevTxn(int consumerType, String consumerCode, MilkType animalType, MilkClass milkClass) throws Exception {
        try {
            return txnRepo.findTopByConsumerTypeAndConsumerCodeAndMilkTypeAndMilkClassOrderByTransactionDateDescCreatedAtDesc(
                    consumerType, consumerCode, animalType, milkClass);
        } catch (Exception e) {
            logger.catching(e);
            throw new Exception("Failed to fetch previous transaction", e);
        }
    }

//    @Override
//    public boolean update(CouponBalance couponBalance, int intType, String strSourceOrgType, String strOperationType) {
//        try {
//            logger.traceEntry("update");
//            if (intType == 2) {
//                if (strOperationType.equals(MainApp.OPERATION_UPDATE)) {
//                    try {
//                        CouponBalance prevObj = fetchByCode(couponBalance.getCouponBalanceCode());
//                        if (prevObj != null) {
//                            CouponBalanceHistory couponBalHistory = prevObj.clone();
//                            couponBalHistory.setOperationType(strOperationType);
//                            entityManager.persist(couponBalHistory);
//                        }
//                    } catch (CloneNotSupportedException e) {
//                        logger.catching(e);
//                    }
//                }
//            }
//
//            // Update Object
//            entityManager.merge(couponBalance);
//            logger.traceExit("update");
//            return true;
//        } catch (PersistenceException e) {
//            logger.catching(e);
//            return false;
//        }
//    }
}
