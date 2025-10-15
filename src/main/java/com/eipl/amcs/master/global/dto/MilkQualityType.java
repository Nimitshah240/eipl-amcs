package com.eipl.amcs.master.global.dto;

import com.eipl.amcs.base.model.BaseModel;
import com.eipl.amcs.utils.CommonUtils;

public class MilkQualityType extends BaseModel {

    private Integer code;
    private String name;
    private String nameLocal;

    public MilkQualityType() {
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
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

    @Override
    public String toString() {
        return CommonUtils.getLocalString(name, nameLocal);
    }
}