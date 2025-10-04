package com.eipl.amcs.master.org.dto;

import com.eipl.amcs.base.model.BaseModel;
import com.eipl.amcs.utils.CommonUtils;

public class Bank extends BaseModel {
    private String code;
    private String name;
    private String nameLocal;
    private Short acNoLength;
    private boolean checkedAcNoLength;
    private boolean nationalizedBank;

    public Bank() {
    }

    public boolean isCheckedAcNoLength() {
        return checkedAcNoLength;
    }

    public void setCheckedAcNoLength(boolean checkedAcNoLength) {
        this.checkedAcNoLength = checkedAcNoLength;
    }

    public boolean isNationalizedBank() {
        return nationalizedBank;
    }

    public void setNationalizedBank(boolean nationalizedBank) {
        this.nationalizedBank = nationalizedBank;
    }

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

    public String getNameLocal() {
        return nameLocal;
    }

    public void setNameLocal(String nameLocal) {
        this.nameLocal = nameLocal;
    }

    public Short getAcNoLength() {
        return acNoLength;
    }

    public void setAcNoLength(Short acNoLength) {
        this.acNoLength = acNoLength;
    }

    @Override
    public String toString() {
        return CommonUtils.getLocalString(this.name, this.nameLocal);
    }
}