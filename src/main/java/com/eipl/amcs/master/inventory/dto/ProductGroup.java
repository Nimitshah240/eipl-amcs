package com.eipl.amcs.master.inventory.dto;

import com.eipl.amcs.base.model.BaseModel;
import com.eipl.amcs.master.account.model.LedgerMappingProductGroup;
import com.eipl.amcs.master.global.model.Unit;
import com.eipl.amcs.utils.CommonUtils;

public class ProductGroup extends BaseModel {

    private Integer code;
    private String name;
    private String nameLocal;
    private Unit unit;

    public ProductGroup() {
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

    public Unit getUnit() {
        return unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    @Override
    public String toString() {
        return CommonUtils.getLocalString(this.name, this.nameLocal);
    }

    public LedgerMappingProductGroup getLedgerMappingProductGroupCode() {

        return null;
    }
}