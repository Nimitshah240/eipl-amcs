package com.eipl.amcs.master.account.dto;

import com.eipl.amcs.base.model.BaseModel;
import com.eipl.amcs.utils.CommonUtils;

public class LedgerType extends BaseModel {
    private Integer code;
    private String name;
    private String nameLocal;
    private boolean profitLoss;
    private boolean balanceSheet;

    public LedgerType() {
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

    public boolean isProfitLoss() {
        return profitLoss;
    }

    public void setProfitLoss(boolean profitLoss) {
        this.profitLoss = profitLoss;
    }

    public boolean isBalanceSheet() {
        return balanceSheet;
    }

    public void setBalanceSheet(boolean balanceSheet) {
        this.balanceSheet = balanceSheet;
    }

    @Override
    public String toString() {
        return CommonUtils.getLocalString(this.name, this.nameLocal);
    }
}
