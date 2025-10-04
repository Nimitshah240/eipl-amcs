package com.eipl.amcs.operation.administartion.dto;

import com.eipl.amcs.base.model.BaseModel;
import com.eipl.amcs.master.org.model.Society;

import java.math.BigDecimal;
import java.time.LocalDate;

public class StaffSalaryMapping extends BaseModel {
    private Integer code;
    private BigDecimal amount;
    private String unionCode;
    private Society society;
    private StaffMember staffMember;
    private StaffSalaryHead staffSalaryHead;
    private LocalDate wefDate;

    public LocalDate getWefDate() {
        return wefDate;
    }

    public void setWefDate(LocalDate wefDate) {
        this.wefDate = wefDate;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
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

    public StaffMember getStaffMember() {
        return staffMember;
    }

    public void setStaffMember(StaffMember staffMember) {
        this.staffMember = staffMember;
    }

    public StaffSalaryHead getStaffSalaryHead() {
        return staffSalaryHead;
    }

    public void setStaffSalaryHead(StaffSalaryHead staffSalaryHead) {
        this.staffSalaryHead = staffSalaryHead;
    }
}
