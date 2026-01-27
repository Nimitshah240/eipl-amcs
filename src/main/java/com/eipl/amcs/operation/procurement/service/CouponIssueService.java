package com.eipl.amcs.operation.procurement.service;

import com.eipl.amcs.master.org.model.Society;
import com.eipl.amcs.operation.procurement.model.CouponIssue;

import java.time.LocalDate;
import java.util.List;

public interface CouponIssueService {

     String getNextCode(Society society);
     boolean insert(CouponIssue couponIssue, int intType, String strSourceOrgType, String strOperationType);
     boolean update(CouponIssue couponIssue, int intType, String strSourceOrgType, String strOperationType);
    boolean delete(CouponIssue couponIssue, int intType, String strSourceOrgType, String strOperationType);
    public List<CouponIssue> fetchAll();
    LocalDate fetchLatestDateByMember(String code, int intConsumerType, CouponIssue couponIssue);
    double fetchAllByMemberExceptCurrent(CouponIssue issue);

}
