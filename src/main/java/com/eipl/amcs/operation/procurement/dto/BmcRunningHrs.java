package com.eipl.amcs.operation.procurement.dto;

import com.eipl.amcs.base.model.BaseModel;

import java.math.BigDecimal;
import java.time.LocalDate;

public class BmcRunningHrs extends BaseModel {
    private Long code;
    private Boolean isActive;
    private BigDecimal amount;
    private LocalDate date;
    private int runningHoursDg;
    private int runningHoursPower;
    private int totalRunningHours;
    private String chillerNo;
    private String societyCode;
    private String unionCode;

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public String getSocietyCode() {
        return societyCode;
    }

    public void setSocietyCode(String societyCode) {
        this.societyCode = societyCode;
    }

    public Long getCode() {
        return code;
    }

    public void setCode(Long code) {
        this.code = code;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public int getRunningHoursDg() {
        return runningHoursDg;
    }

    public void setRunningHoursDg(int runningHoursDg) {
        this.runningHoursDg = runningHoursDg;
    }

    public int getRunningHoursPower() {
        return runningHoursPower;
    }

    public void setRunningHoursPower(int runningHoursPower) {
        this.runningHoursPower = runningHoursPower;
    }

    public int getTotalRunningHours() {
        return totalRunningHours;
    }

    public void setTotalRunningHours(int totalRunningHours) {
        this.totalRunningHours = totalRunningHours;
    }

    public String getChillerNo() {
        return chillerNo;
    }

    public void setChillerNo(String chillerNo) {
        this.chillerNo = chillerNo;
    }


    public String getUnionCode() {
        return unionCode;
    }

    public void setUnionCode(String unionCode) {
        this.unionCode = unionCode;
    }

}


