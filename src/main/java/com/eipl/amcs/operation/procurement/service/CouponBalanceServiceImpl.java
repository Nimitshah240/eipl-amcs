package com.eipl.amcs.operation.procurement.service;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.service.NextCodeService;
import com.eipl.amcs.master.global.model.MilkType;
import com.eipl.amcs.master.operation.model.Customer;
import com.eipl.amcs.master.operation.model.Member;
import com.eipl.amcs.master.operation.service.CustomerService;
import com.eipl.amcs.master.operation.service.MemberService;
import com.eipl.amcs.operation.procurement.model.CouponBalance;
import com.eipl.amcs.operation.procurement.repository.CouponBalanceAuditRepository;
import com.eipl.amcs.operation.procurement.repository.CouponBalanceRepository;
import com.eipl.amcs.operation.procurement.repository.CouponIssueRepository;
import com.eipl.amcs.utils.CommonUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
    private CouponIssueRepository couponIssueRepository;
    @Autowired
    private MemberService memberService;
    @Autowired
    private CustomerService customerService;


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


    @Override
    public List<CouponBalance> fetchAll() {
        return couponBalanceRepository.findAll(Sort.by("createdAt").descending());
    }

    @Override
    public List<CouponBalance> fetchAllGroupedByConsumerCode() {
        List<Object[]> results = couponBalanceRepository.findAllGroupedByConsumerCode();
        List<CouponBalance> couponBalances = new ArrayList<>();
        for (Object[] result : results) {
            CouponBalance couponBalance = new CouponBalance();
            couponBalance.setConsumerCode((String) result[0]);
            couponBalance.setBalance((Double) result[1]);
            couponBalance.setCreatedAt((LocalDateTime) result[2]);

            MilkType milkType = new MilkType();
            milkType.setCode((Integer) result[3]);
            milkType.setName((String) result[4]);
            milkType.setNameLocal((String) result[5]);
            couponBalance.setMilkType(milkType);

            couponBalance.setConsumerType((Integer) result[6]);

            if (couponBalance.getConsumerType() != null && couponBalance.getConsumerCode() != null) {
                int type = couponBalance.getConsumerType();
                if (type == 1 || type == 2) {
                    couponBalance.setConsumerTypeString("member");
                    Member m = memberService.findByMemberCode(couponBalance.getConsumerCode());
                    if (m != null) couponBalance.setConsumerName(m.toMemberName());

                } else if (type == 3 || type == 4) {
                    couponBalance.setConsumerTypeString(type == 3 ? "institute" : "retail sale");
                    Customer c = customerService.findByCustomerCodeAndType(couponBalance.getConsumerCode(),couponBalance.getConsumerType());
                    if (c != null) couponBalance.setConsumerName(c.toCustomerName());

                } else {
                    couponBalance.setConsumerTypeString("consumer");
                    Customer c = customerService.findByCustomerCodeAndType(couponBalance.getConsumerCode(),couponBalance.getConsumerType());
                    if (c != null) {
                        couponBalance.setConsumerName(c.toCustomerName());
                    } else {
                        couponBalance.setConsumerName(couponBalance.getConsumerCode());
                    }
                }
            }
            if (couponBalance.getConsumerName() == null || couponBalance.getConsumerName().isEmpty()) {
                couponBalance.setConsumerName(couponBalance.getConsumerCode());
            }

            couponBalances.add(couponBalance);
        }
        return couponBalances;
    }

}
