package com.eipl.amcs.master.org.dto;

import com.eipl.amcs.base.model.BaseModel;
import com.eipl.amcs.master.geo.model.*;
import com.eipl.amcs.master.org.model.*;
import com.eipl.amcs.master.org.model.Bank;
import com.eipl.amcs.master.org.model.Bmc;
import com.eipl.amcs.master.org.model.Branch;
import com.eipl.amcs.master.org.model.Mcc;
import com.eipl.amcs.master.org.model.Plant;
import com.eipl.amcs.master.org.model.Route;
import com.eipl.amcs.master.org.model.Union;
import com.eipl.amcs.utils.CommonUtils;

import java.time.LocalDate;

public class Society extends BaseModel {
    private String code;
    private String codeEx;
    private String name;
    private String shortName;
    private String nameLocal;
    private String shortNameLocal;

    private LocalDate registrationDate;
    private String registrationCode;
    private String destinationCode;
    private Short destinationType;
    private LocalDate effectiveDate;
    private Short isBmc;
    private String email;
    private String panNo;
    private String phoneNo;
    private String contactPerson;
    private String contactPersonMobileNo;
    private String bankAccountNo;
    private String ifsc;
    private String address;
    private String city;
    private String pincode;
    private Boolean allowMultiFamilyMember;
    private String tinNo;
    private String serviceTax;
    private String upiNo;

    private Bank bank;
    private Branch branch;
    private Union union;
    private Plant plant;
    private Mcc mcc;
    private Bmc bmc;
    private Route route;

    private State state;
    private District district;
    private SubDistrict subDistrict;
    private Village village;
    private Hamlet hamlet;

    public Society() {
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

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getNameLocal() {
        return nameLocal;
    }

    public void setNameLocal(String nameLocal) {
        this.nameLocal = nameLocal;
    }

    public String getShortNameLocal() {
        return shortNameLocal;
    }

    public void setShortNameLocal(String shortNameLocal) {
        this.shortNameLocal = shortNameLocal;
    }

    public LocalDate getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(LocalDate registrationDate) {
        this.registrationDate = registrationDate;
    }

    public String getRegistrationCode() {
        return registrationCode;
    }

    public void setRegistrationCode(String registrationCode) {
        this.registrationCode = registrationCode;
    }

    public String getDestinationCode() {
        return destinationCode;
    }

    public void setDestinationCode(String destinationCode) {
        this.destinationCode = destinationCode;
    }

    public Short getDestinationType() {
        return destinationType;
    }

    public void setDestinationType(Short destinationType) {
        this.destinationType = destinationType;
    }

    public LocalDate getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(LocalDate effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public Short getIsBmc() {
        return isBmc;
    }

    public void setIsBmc(Short isBmc) {
        this.isBmc = isBmc;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPanNo() {
        return panNo;
    }

    public void setPanNo(String panNo) {
        this.panNo = panNo;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    public String getContactPersonMobileNo() {
        return contactPersonMobileNo;
    }

    public void setContactPersonMobileNo(String contactPersonMobileNo) {
        this.contactPersonMobileNo = contactPersonMobileNo;
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

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public Boolean getAllowMultiFamilyMember() {
        return allowMultiFamilyMember;
    }

    public void setAllowMultiFamilyMember(Boolean allowMultiFamilyMember) {
        this.allowMultiFamilyMember = allowMultiFamilyMember;
    }

    public String getTinNo() {
        return tinNo;
    }

    public void setTinNo(String tinNo) {
        this.tinNo = tinNo;
    }

    public String getServiceTax() {
        return serviceTax;
    }

    public void setServiceTax(String serviceTax) {
        this.serviceTax = serviceTax;
    }

    public String getUpiNo() {
        return upiNo;
    }

    public void setUpiNo(String upiNo) {
        this.upiNo = upiNo;
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

    public Union getUnion() {
        return union;
    }

    public void setUnion(Union union) {
        this.union = union;
    }

    public Plant getPlant() {
        return plant;
    }

    public void setPlant(Plant plant) {
        this.plant = plant;
    }

    public Mcc getMcc() {
        return mcc;
    }

    public void setMcc(Mcc mcc) {
        this.mcc = mcc;
    }

    public Bmc getBmc() {
        return bmc;
    }

    public void setBmc(Bmc bmc) {
        this.bmc = bmc;
    }

    public Route getRoute() {
        return route;
    }

    public void setRoute(Route route) {
        this.route = route;
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