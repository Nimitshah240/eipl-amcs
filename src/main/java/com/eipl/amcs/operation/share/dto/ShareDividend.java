package com.eipl.amcs.operation.share.dto;

import com.eipl.amcs.base.model.BaseModel;
import com.eipl.amcs.master.account.model.FinancialYear;
import com.eipl.amcs.master.operation.model.Member;
import com.eipl.amcs.master.org.model.Society;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ShareDividend extends BaseModel {

    private String code;
    private LocalDate disbursementDate;
    private BigDecimal dividendAmount;
    private BigDecimal dividendValue;
    private Boolean isDisbursed;
    private Integer noOfShare;
    private BigDecimal shareAmount;
    private String unionCode;
    private Member member;
    private FinancialYear financialYear;
    private Society society;
    private String xCol4;
    private String xCol5;
    private Integer dividendValueType;
    private String shareCode;

    public Integer getDividendValueType() {
        return dividendValueType;
    }

    public void setDividendValueType(Integer dividendValueType) {
        this.dividendValueType = dividendValueType;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getShareCode() {
        return shareCode;
    }

    public void setShareCode(String shareCode) {
        this.shareCode = shareCode;
    }

    public LocalDate getDisbursementDate() {
        return disbursementDate;
    }

    public void setDisbursementDate(LocalDate disbursementDate) {
        this.disbursementDate = disbursementDate;
    }

    public BigDecimal getDividendAmount() {
        return dividendAmount;
    }

    public void setDividendAmount(BigDecimal dividendAmount) {
        this.dividendAmount = dividendAmount;
    }

    public BigDecimal getDividendValue() {
        return dividendValue;
    }

    public void setDividendValue(BigDecimal dividendValue) {
        this.dividendValue = dividendValue;
    }

    public Boolean getDisbursed() {
        return isDisbursed;
    }

    public void setDisbursed(Boolean disbursed) {
        isDisbursed = disbursed;
    }

    public Integer getNoOfShare() {
        return noOfShare;
    }

    public void setNoOfShare(Integer noOfShare) {
        this.noOfShare = noOfShare;
    }

    public BigDecimal getShareAmount() {
        return shareAmount;
    }

    public void setShareAmount(BigDecimal shareAmount) {
        this.shareAmount = shareAmount;
    }

    public String getUnionCode() {
        return unionCode;
    }

    public void setUnionCode(String unionCode) {
        this.unionCode = unionCode;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public FinancialYear getFinancialYear() {
        return financialYear;
    }

    public void setFinancialYear(FinancialYear financialYear) {
        this.financialYear = financialYear;
    }

    public Society getSociety() {
        return society;
    }

    public void setSociety(Society society) {
        this.society = society;
    }

    public String getxCol4() {
        return xCol4;
    }

    public void setxCol4(String xCol4) {
        this.xCol4 = xCol4;
    }

    public String getxCol5() {
        return xCol5;
    }

    public void setxCol5(String xCol5) {
        this.xCol5 = xCol5;
    }



}

