package com.eipl.amcs.master.operation.dto;

import com.eipl.amcs.base.model.BaseModel;
import com.eipl.amcs.master.org.model.Society;
import com.eipl.amcs.master.org.model.Union;
import com.eipl.amcs.master.operation.model.BillHead;
import com.eipl.amcs.master.operation.model.Formula;
import java.time.LocalDate;

public class BillCriteria extends BaseModel {
    private String code;
    private String criteria;
    private String formula;
    private Society society;
    private Union union;
    private Formula formulaCode;

    private LocalDate startDate;
    private LocalDate endDate;
    private BillHead billHeadCode;

    public BillCriteria() {
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCriteria() {
        return criteria;
    }

    public void setCriteria(String criteria) {
        this.criteria = criteria;
    }

    public String getFormula() {
        return formula;
    }

    public void setFormula(String formula) {
        this.formula = formula;
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

    public Formula getFormulaCode() {
        return formulaCode;
    }

    public void setFormulaCode(Formula formulaCode) {
        this.formulaCode = formulaCode;
    }

    public void setStartDate(LocalDate startDate) {this.startDate = startDate;}

    public LocalDate getStartDate() {return startDate;}

    public void setEndDate(LocalDate endDate) {this.endDate = endDate;}

    public LocalDate getEndDate() {return endDate;}

    public void setBillHeadCode(BillHead billHeadCode) {this.billHeadCode = billHeadCode;}

    public BillHead getBillHeadCode() {return billHeadCode;}
}
