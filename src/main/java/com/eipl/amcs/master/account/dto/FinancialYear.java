package com.eipl.amcs.master.account.dto;

import com.eipl.amcs.base.model.BaseModel;
import com.eipl.amcs.utils.CommonUtils;

import java.time.LocalDate;

/*
 * NO USAGE, SHIFT TO MODEL
 */
public class FinancialYear extends BaseModel {

    private String code;
    private LocalDate startDate;
    private LocalDate endDate;

    public FinancialYear() {
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    @Override
    public String toString() {
        return startDate.getYear() + "-" + endDate.getYear();
    }
}
