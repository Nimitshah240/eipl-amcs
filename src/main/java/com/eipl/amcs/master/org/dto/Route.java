package com.eipl.amcs.master.org.dto;

import com.eipl.amcs.base.model.BaseModel;
import com.eipl.amcs.master.org.model.Bmc;
import com.eipl.amcs.master.org.model.Union;
import com.eipl.amcs.utils.CommonUtils;

import java.time.LocalTime;

public class Route extends BaseModel {
    private String code;
    private String codeEx;
    private String name;
    private String nameLocal;

    private Integer capacity;
    private Integer lengthKms;
    private LocalTime startTime;
    private LocalTime returnTime;

    private Union union;
    private Bmc bmc;

    public Route() {
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

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public Integer getLengthKms() {
        return lengthKms;
    }

    public void setLengthKms(Integer lengthKms) {
        this.lengthKms = lengthKms;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getReturnTime() {
        return returnTime;
    }

    public void setReturnTime(LocalTime returnTime) {
        this.returnTime = returnTime;
    }

    public Union getUnion() {
        return union;
    }

    public void setUnion(Union union) {
        this.union = union;
    }

    public Bmc getBmc() {
        return bmc;
    }

    public void setBmc(Bmc bmc) {
        this.bmc = bmc;
    }

    @Override
    public String toString() {
        return CommonUtils.getLocalString(this.name, this.nameLocal);
    }
}