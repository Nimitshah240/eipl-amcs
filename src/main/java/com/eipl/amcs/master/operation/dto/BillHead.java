package com.eipl.amcs.master.operation.dto;

import com.eipl.amcs.base.model.BaseModel;
import com.eipl.amcs.master.org.model.Society;
import com.eipl.amcs.master.org.model.Union;

public class BillHead extends BaseModel {
    private String code;
    private String name;
    private String nameLocal;
    private Boolean defaultHead;
    private Boolean disburseAllowed;

    private Society society;
    private Union union;

    private short allowAdjustment;

    private short headType;

    public short getAllowAdjustment() {
        return allowAdjustment;
    }

    public void setAllowAdjustment(short allowAdjustment) {
        this.allowAdjustment = allowAdjustment;
    }

    public short getHeadType() {
        return headType;
    }

    public void setHeadType(short headType) {
        this.headType = headType;
    }

    public BillHead() {
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

    public Boolean getDefaultHead() {
        return defaultHead;
    }

    public void setDefaultHead(Boolean defaultHead) {
        this.defaultHead = defaultHead;
    }

    public Boolean getDisburseAllowed() {
        return disburseAllowed;
    }

    public void setDisburseAllowed(Boolean disburseAllowed) {
        this.disburseAllowed = disburseAllowed;
    }

    public Society getSociety() {
        return society;
    }

    public void setSociety(Society society) {
        this.society = society;
    }

    public Union getUnion() {
        return union;
    }

    public void setUnion(Union union) {
        this.union = union;
    }
}
