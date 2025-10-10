package com.eipl.amcs.master.org.dto;

import com.eipl.amcs.base.model.BaseModel;
import com.eipl.amcs.master.geo.model.*;
import com.eipl.amcs.master.org.model.Mcc;
import com.eipl.amcs.master.org.model.Union;
import com.eipl.amcs.utils.CommonUtils;

public class Bmc extends BaseModel {
    private String code;
    private String codeEx;
    private String name;
    private String nameLocal;

    private String address;
    private String city;
    private String phoneNo;
    private String contactPerson;
    private String contactPersonEmail;
    private String contactPersonMobileNo;

    private Union union;
    private Mcc mcc;
    private State state;
    private District district;
    private SubDistrict subDistrict;
    private Village village;
    private Hamlet hamlet;

    public Bmc() {
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

    public Union getUnion() {
        return union;
    }

    public void setUnion(Union union) {
        this.union = union;
    }

    public Mcc getMcc() {
        return mcc;
    }

    public void setMcc(Mcc mcc) {
        this.mcc = mcc;
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