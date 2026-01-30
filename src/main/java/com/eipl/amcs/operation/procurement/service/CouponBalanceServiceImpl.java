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
import com.eipl.amcs.utils.CommonUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;

@Service
public class CouponBalanceServiceImpl implements CouponBalanceService {

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
    public CouponBalance fetchBalanceForConsumer(int consumerType, String consumerCode, MilkType animalType) throws Exception {
        try {
            CouponBalance bal = couponBalanceRepository.findFirstByConsumerTypeAndConsumerCodeAndMilkType(
                    consumerType, consumerCode, animalType);
            logger.traceExit("fetch balance");
            return bal;
        } catch (Exception e) {
            logger.catching(e);
            throw new Exception("Error fetching coupon balance", e);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean insert(CouponBalance couponBalance) {
        try {
            logger.traceEntry("insert coupon bal");
            if (couponBalance.getCouponBalanceCode() == null || couponBalance.getCouponBalanceCode().isEmpty())
                couponBalance.setCouponBalanceCode(nextCodeService.getNextCode("CouponBalance", "couponBalanceCode",
                        MainApp.identityDto.getSociety().getCode(), 0));
            couponBalanceRepository.customSave(couponBalance, CommonUtils.setIdentityHeader());

            logger.traceExit("insert");
            return true;
        } catch (PersistenceException e) {
            logger.catching(e);
            return false;
        }
    }

    @Override
    public boolean update(CouponBalance couponBalance) {
        try {
            logger.traceEntry("update coupon balance: {}", couponBalance.getCouponBalanceCode());
            if (couponBalance.getBalance() >= 0) {
                couponBalanceRepository.customUpdate(couponBalance, CommonUtils.setIdentityHeader());
                logger.traceExit("update");
                return true;
            }
        } catch (Exception e) {
            logger.catching(e);
        }
        return false;
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
