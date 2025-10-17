package com.eipl.amcs.master.procurement.dto;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.model.BaseModel;
import com.eipl.amcs.master.global.model.Shift;
import com.eipl.amcs.master.org.model.Society;
import com.eipl.amcs.utils.AppConstant;
import com.eipl.amcs.utils.CommonUtils;

import java.time.LocalDateTime;

public class SocietyPaymentCycle extends BaseModel {

    private String code;
    private LocalDateTime fromDate;
    private LocalDateTime toDate;
    private Integer intervalValue;
    private Boolean billing;
    private Boolean lockBillingProcess;
    private String unionCode;

    private Society society;
    private Shift fromShift;
    private Shift toShift;

    public SocietyPaymentCycle() {
        lockBillingProcess = false;
        billing = false;
        unionCode = MainApp.identityDto.getUnion().getCode();
        society = MainApp.identityDto.getSociety();
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public LocalDateTime getFromDate() {
        return fromDate;
    }

    public void setFromDate(LocalDateTime fromDate) {
        this.fromDate = fromDate;
    }

    public LocalDateTime getToDate() {
        return toDate;
    }

    public void setToDate(LocalDateTime toDate) {
        this.toDate = toDate;
    }

    public Integer getIntervalValue() {
        return intervalValue;
    }

    public void setIntervalValue(Integer intervalValue) {
        this.intervalValue = intervalValue;
    }

    public Boolean getBilling() {
        return billing;
    }

    public void setBilling(Boolean billing) {
        this.billing = billing;
    }

    public Boolean getLockBillingProcess() {
        return lockBillingProcess;
    }

    public void setLockBillingProcess(Boolean lockBillingProcess) {
        this.lockBillingProcess = lockBillingProcess;
    }

    public String getUnionCode() {
        return unionCode;
    }

    public void setUnionCode(String unionCode) {
        this.unionCode = unionCode;
    }

    public Society getSociety() {
        return society;
    }

    public void setSociety(Society society) {
        this.society = society;
    }

    public Shift getFromShift() {
        return fromShift;
    }

    public void setFromShift(Shift fromShift) {
        this.fromShift = fromShift;
    }

    public Shift getToShift() {
        return toShift;
    }

    public void setToShift(Shift toShift) {
        this.toShift = toShift;
    }

    public String toDateShiftString() {
        String sb = getFromDate().toLocalDate().format(AppConstant.DATE_FORMATTER) +
                CommonUtils.getShiftShort(getFromShift()) +
                " - " +
                getToDate().toLocalDate().format(AppConstant.DATE_FORMATTER) +
                CommonUtils.getShiftShort(getToShift());
        return sb;
    }
}