package com.eipl.amcs.master.operation.dto;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.model.BaseModel;
import com.eipl.amcs.master.geo.model.*;
import com.eipl.amcs.master.global.model.Gender;
import com.eipl.amcs.master.operation.model.Member;
import com.eipl.amcs.master.org.model.Bank;
import com.eipl.amcs.master.org.model.Branch;

import java.time.LocalDate;

public class MemberDetail extends BaseModel {

    private String code;
    private Member member;
    private Short paymentMode;
    private String address;
    private String pincode;
    private String accountNo;
    private String ifsc;
    private String aadharNo;
    private String panNo;
    private String email;
    private LocalDate birthDate;
    private LocalDate registrationDate;
    private Short numberOfCow;
    private Short numberOfBuffalo;
    private String unionCode;
    private Gender gender;
    private Bank bank;
    private Branch branch;
    private State state;
    private District district;
    private SubDistrict subDistrict;
    private Village village;
    private Hamlet hamlet;

    public MemberDetail() {
        createdBy = MainApp.getUser() != null ? MainApp.getUser().getUsername() : null;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public Short getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(Short paymentMode) {
        this.paymentMode = paymentMode;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public String getIfsc() {
        return ifsc;
    }

    public void setIfsc(String ifsc) {
        this.ifsc = ifsc;
    }

    public String getAadharNo() {
        return aadharNo;
    }

    public void setAadharNo(String aadharNo) {
        this.aadharNo = aadharNo;
    }

    public String getPanNo() {
        return panNo;
    }

    public void setPanNo(String panNo) {
        this.panNo = panNo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public LocalDate getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(LocalDate registrationDate) {
        this.registrationDate = registrationDate;
    }

    public Short getNumberOfCow() {
        return numberOfCow;
    }

    public void setNumberOfCow(Short numberOfCow) {
        this.numberOfCow = numberOfCow;
    }

    public Short getNumberOfBuffalo() {
        return numberOfBuffalo;
    }

    public void setNumberOfBuffalo(Short numberOfBuffalo) {
        this.numberOfBuffalo = numberOfBuffalo;
    }

    public String getUnionCode() {
        return unionCode;
    }

    public void setUnionCode(String unionCode) {
        this.unionCode = unionCode;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
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

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public District getDistrict() {
        return district;
    }

    public void setDistrict(District district) {
        this.district = district;
    }

    public SubDistrict getSubDistrict() {
        return subDistrict;
    }

    public void setSubDistrict(SubDistrict subDistrict) {
        this.subDistrict = subDistrict;
    }

    public Village getVillage() {
        return village;
    }

    public void setVillage(Village village) {
        this.village = village;
    }

    public Hamlet getHamlet() {
        return hamlet;
    }

    public void setHamlet(Hamlet hamlet) {
        this.hamlet = hamlet;
    }
}
