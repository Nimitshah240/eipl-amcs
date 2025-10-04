package com.eipl.amcs.master.operation.dto;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.model.BaseModel;
import com.eipl.amcs.master.global.model.MemberType;
import com.eipl.amcs.master.global.model.MilkType;
import com.eipl.amcs.master.org.model.Society;
import com.eipl.amcs.utils.CommonUtils;

import java.math.BigDecimal;

public class Member extends BaseModel {
    private String code;
    private String codeEx;
    private String firstName;
    private String middleName;
    private String lastName;
    private String firstNameLocal;
    private String middleNameLocal;
    private String lastNameLocal;
    private BigDecimal creditLimit;
    private String mobileNo;
    private MilkType milkType;
    private Society society;
    private MemberType memberType;

    public Member() {
        createdBy = MainApp.getUser() != null ? MainApp.getUser().getUsername() : null;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCodeEx() {
        return codeEx;
    }

    public void setCodeEx(String codeEx) {
        this.codeEx = codeEx;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstNameLocal() {
        return firstNameLocal;
    }

    public void setFirstNameLocal(String firstNameLocal) {
        this.firstNameLocal = firstNameLocal;
    }

    public String getMiddleNameLocal() {
        return middleNameLocal;
    }

    public void setMiddleNameLocal(String middleNameLocal) {
        this.middleNameLocal = middleNameLocal;
    }

    public String getLastNameLocal() {
        return lastNameLocal;
    }

    public void setLastNameLocal(String lastNameLocal) {
        this.lastNameLocal = lastNameLocal;
    }

    public BigDecimal getCreditLimit() {
        return creditLimit;
    }

    public void setCreditLimit(BigDecimal creditLimit) {
        this.creditLimit = creditLimit;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public MilkType getMilkType() {
        return milkType;
    }

    public void setMilkType(MilkType milkType) {
        this.milkType = milkType;
    }

    public MemberType getMemberType() {
        return memberType;
    }

    public void setMemberType(MemberType memberType) {
        this.memberType = memberType;
    }

    public Society getSociety() {
        return society;
    }

    public void setSociety(Society society) {
        this.society = society;
    }

    public String toMemberName() {
        if (middleName == null) {
            middleName = "";
        }
        if (lastName == null) {
            lastName = "";
        }
        return CommonUtils.getLocalString(firstName, getFirstNameLocal()) + " " +
                CommonUtils.getLocalString(middleName, getMiddleNameLocal()) + " " +
                CommonUtils.getLocalString(lastName, getLastNameLocal());

    }

    public String toMemberNameWithExCode() {
        if (middleName == null) {
            middleName = "";
        }
        if (lastName == null) {
            lastName = "";
        }
        return codeEx +"-" + CommonUtils.getLocalString(firstName, getFirstNameLocal()) + " " +
                CommonUtils.getLocalString(middleName, getMiddleNameLocal()) + " " +
                CommonUtils.getLocalString(lastName, getLastNameLocal());

    }

    public String toMemberName(String locale) {
        if (locale.equalsIgnoreCase("en")) {
            if (middleName == null)
                middleName = "";
            if (lastName == null)
                lastName = "";
            return firstName + " " + middleName + " " + lastName;
        }
        if (locale.equalsIgnoreCase("gu")) {
            if (middleNameLocal == null)
                middleNameLocal = "";
            if (lastNameLocal == null)
                lastNameLocal = "";
            return firstNameLocal + " " + middleNameLocal + " " + lastNameLocal;
        }
//        if (locale.equalsIgnoreCase("mr")) {
//            if (middleNameLocal == null)
//                middleNameLocal = "";
//            if (lastNameLocal == null)
//                lastNameLocal = "";
//            return firstNameLocal + " " + middleNameLocal + " " + lastNameLocal;
//        }
        return "";
    }
}
