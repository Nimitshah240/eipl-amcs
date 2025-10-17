package com.eipl.amcs.operation.administartion.dto;

import com.eipl.amcs.base.model.BaseModelTxn;
import com.eipl.amcs.master.org.model.Society;

public class StaffSalaryHead extends BaseModelTxn {

    private Integer code;
    private Integer type;
    private String name;
    private Society society;


    public Society getSociety() {
        return society;
    }

    public void setSociety(Society society) {
        this.society = society;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
