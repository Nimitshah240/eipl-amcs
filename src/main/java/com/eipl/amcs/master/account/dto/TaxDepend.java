package com.eipl.amcs.master.account.dto;

import com.eipl.amcs.base.model.BaseModel;
import com.eipl.amcs.master.account.model.TaxDetail;

public class TaxDepend extends BaseModel {

    private String code;
    private Short steps;

    private TaxDetail taxDetail;
    private TaxDetail taxDetails;

    public TaxDepend() {
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Short getSteps() {
        return steps;
    }

    public void setSteps(Short steps) {
        this.steps = steps;
    }

    public TaxDetail getTaxDetail() {
        return taxDetail;
    }

    public void setTaxDetail(TaxDetail taxDetail) {
        this.taxDetail = taxDetail;
    }

    public TaxDetail getTaxDetails() {
        return taxDetails;
    }

    public void setTaxDetails(TaxDetail taxDetails) {
        this.taxDetails = taxDetails;
    }
}