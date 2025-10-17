package com.eipl.amcs.operation.administartion.dto;

import com.eipl.amcs.base.model.BaseModelTxn;
import com.eipl.amcs.master.global.model.Gender;
import com.eipl.amcs.master.org.model.Bank;
import com.eipl.amcs.master.org.model.Branch;
import com.eipl.amcs.master.org.model.Society;

import java.time.LocalDate;

public class StaffMember extends BaseModelTxn {

    private String code;
    private String name;
    private String bankAccountNo;
    private String emailId;
    private String ifsc;
    private String mobileNo;
    private String panNo;
    private String pinCode;
    private Integer paymentMode;
    private LocalDate tenureFromDate;
    private LocalDate tenureToDate;
    private String unionCode;
    private Bank bank;
    private Branch branch;
    private Designation designation;
    private Society society;
    private Gender gender;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBankAccountNo() {
        return bankAccountNo;
    }

    public void setBankAccountNo(String bankAccountNo) {
        this.bankAccountNo = bankAccountNo;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getIfsc() {
        return ifsc;
    }

    public void setIfsc(String ifsc) {
        this.ifsc = ifsc;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getPanNo() {
        return panNo;
    }

    public void setPanNo(String panNo) {
        this.panNo = panNo;
    }

    public String getPinCode() {
        return pinCode;
    }

    public void setPinCode(String pinCode) {
        this.pinCode = pinCode;
    }

    public Integer getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(Integer paymentMode) {
        this.paymentMode = paymentMode;
    }

    public LocalDate getTenureFromDate() {
        return tenureFromDate;
    }

    public void setTenureFromDate(LocalDate tenureFromDate) {
        this.tenureFromDate = tenureFromDate;
    }

    public LocalDate getTenureToDate() {
        return tenureToDate;
    }

    public void setTenureToDate(LocalDate tenureToDate) {
        this.tenureToDate = tenureToDate;
    }

    public String getUnionCode() {
        return unionCode;
    }

    public void setUnionCode(String unionCode) {
        this.unionCode = unionCode;
    }

    public Bank getBank() {
        return bank;
    }

    public void setBank(Bank bank) {
        this.bank = bank;
    }

    public Branch getBranch() {
        return branch;
    }

    public void setBranch(Branch branch) {
        this.branch = branch;
    }

    public Society getSociety() {
        return society;
    }

    public void setSociety(Society society) {
        this.society = society;
    }

    public Designation getDesignation() {
        return designation;
    }

    public void setDesignation(Designation designation) {
        this.designation = designation;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }


}
