package com.eipl.amcs.master.org.dto;

import com.eipl.amcs.base.model.BaseModel;
import com.eipl.amcs.master.geo.model.*;
import com.eipl.amcs.master.org.model.Bank;
import com.eipl.amcs.master.org.model.Branch;
import com.eipl.amcs.utils.CommonUtils;

import java.time.LocalDate;

public class Union extends BaseModel {
    private String code;
    private String codeEx;
    private String name;
    private String nameLocal;

    private LocalDate registrationDate;
    private String registrationNo;
    private String bankAccountNo;
    private String ifsc;
    private String upiNo;

    private String address;
    private String city;
    private String phoneNo;
    private String pincode;
    private String contactPerson;
    private String contactPersonEmail;
    private String contactPersonMobileNo;
    private String contactPersonPanNo;
    private String contactPersonPhoneNo;
    private String faxNo;

    private Bank bank;
    private Branch branch;

    private State state;
    private District district;
    private SubDistrict subDistrict;
    private Village village;
    private Hamlet hamlet;

    public Union() {
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNameLocal() {
        return nameLocal;
    }

    public void setNameLocal(String nameLocal) {
        this.nameLocal = nameLocal;
    }

    public LocalDate getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(LocalDate registrationDate) {
        this.registrationDate = registrationDate;
    }

    public String getRegistrationNo() {
        return registrationNo;
    }

    public void setRegistrationNo(String registrationNo) {
        this.registrationNo = registrationNo;
    }

    public String getBankAccountNo() {
        return bankAccountNo;
    }

    public void setBankAccountNo(String bankAccountNo) {
        this.bankAccountNo = bankAccountNo;
    }

    public String getIfsc() {
        return ifsc;
    }

    public void setIfsc(String ifsc) {
        this.ifsc = ifsc;
    }

    public String getUpiNo() {
        return upiNo;
    }

    public void setUpiNo(String upiNo) {
        this.upiNo = upiNo;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    public String getContactPersonEmail() {
        return contactPersonEmail;
    }

    public void setContactPersonEmail(String contactPersonEmail) {
        this.contactPersonEmail = contactPersonEmail;
    }

    public String getContactPersonMobileNo() {
        return contactPersonMobileNo;
    }

    public void setContactPersonMobileNo(String contactPersonMobileNo) {
        this.contactPersonMobileNo = contactPersonMobileNo;
    }

    public String getContactPersonPanNo() {
        return contactPersonPanNo;
    }

    public void setContactPersonPanNo(String contactPersonPanNo) {
        this.contactPersonPanNo = contactPersonPanNo;
    }

    public String getContactPersonPhoneNo() {
        return contactPersonPhoneNo;
    }

    public void setContactPersonPhoneNo(String contactPersonPhoneNo) {
        this.contactPersonPhoneNo = contactPersonPhoneNo;
    }

    public String getFaxNo() {
        return faxNo;
    }

    public void setFaxNo(String faxNo) {
        this.faxNo = faxNo;
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

    @Override
    public String toString() {
        return CommonUtils.getLocalString(this.name, this.nameLocal);
    }
}
