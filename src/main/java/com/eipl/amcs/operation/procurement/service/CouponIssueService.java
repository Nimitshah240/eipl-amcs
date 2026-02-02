package com.eipl.amcs.operation.procurement.service;

import com.eipl.amcs.master.org.model.Society;
import com.eipl.amcs.operation.procurement.model.CouponIssue;

import java.time.LocalDate;
import java.util.List;

public interface CouponIssueService {

    String getNextCode(Society society);

    boolean insert(CouponIssue couponIssue);

    boolean update(CouponIssue couponIssue);

    boolean delete(CouponIssue couponIssue);

    List<CouponIssue> fetchAll();
}
