package com.eipl.amcs.master.account.dto;

import com.eipl.amcs.base.model.BaseModel;
import com.eipl.amcs.master.account.model.BasicTax;
import com.eipl.amcs.master.account.model.Tax;

public class TaxDetail extends BaseModel {

    private String code;
    private Short type;
    private Double percentage;

    private BasicTax basicTax;
    private Tax tax;

    public TaxDetail() {

    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Short getType() {
        return type;
    }

    public void setType(Short type) {
        this.type = type;
    }

    public Double getPercentage() {
        return percentage;
    }

    public void setPercentage(Double percentage) {
        this.percentage = percentage;
    }

    public BasicTax getBasicTax() {
        return basicTax;
    }

    public void setBasicTax(BasicTax basicTax) {
        this.basicTax = basicTax;
    }

    public Tax getTax() {
        return tax;
    }

    public void setTax(Tax tax) {
        this.tax = tax;
    }
}