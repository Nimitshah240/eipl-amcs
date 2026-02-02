package com.eipl.amcs.operation.procurement.service;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.service.NextCodeService;
import com.eipl.amcs.master.operation.model.Customer;
import com.eipl.amcs.master.operation.model.Member;
import com.eipl.amcs.master.operation.service.CustomerService;
import com.eipl.amcs.master.operation.service.MemberService;
import com.eipl.amcs.master.org.model.Society;
import com.eipl.amcs.operation.procurement.model.CouponBalance;
import com.eipl.amcs.operation.procurement.model.CouponIssue;
import com.eipl.amcs.operation.procurement.repository.CouponIssueRepository;
import com.eipl.amcs.utils.CommonUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;


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
        String couponIssueNo = nextCodeService.getNextCode("CouponIssue", "code", MainApp.identityDto.getSociety().getCode()
                , 0);
        return couponIssueNo;
    }

    @Override
    public boolean insert(CouponIssue couponIssue) {
        try {
            CouponBalance couponBal = couponBalanceService.fetchBalanceForConsumer(
                    couponIssue.getConsumerType(), couponIssue.getConsumerCode(),
                    couponIssue.getMilkType());

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

    @Override
    public boolean update(CouponIssue updatedCouponIssue) {
        try {
            CouponIssue couponIssuePrev = fetchByCode(updatedCouponIssue.getCode());
            CouponBalance couponBalancePrev = couponBalanceService.fetchBalanceForConsumer(couponIssuePrev.getConsumerType(),
                    couponIssuePrev.getConsumerCode(), couponIssuePrev.getMilkType());

            CouponBalance couponBalanceNew = couponBalanceService.fetchBalanceForConsumer(updatedCouponIssue.getConsumerType(),
                    updatedCouponIssue.getConsumerCode(), updatedCouponIssue.getMilkType());


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
