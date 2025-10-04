package com.eipl.amcs.operation.administartion.dto;

import com.eipl.amcs.base.model.BaseModel;
import com.eipl.amcs.operation.inventory.model.ProductSaleInstallment;

import java.util.List;

public class CashAdvanceDto extends BaseModel {

    private CashAdvance cashAdvance;
    private List<ProductSaleInstallment> installmentList;


    public CashAdvanceDto() {
    }

    public CashAdvanceDto(CashAdvance cashAdvance, List<ProductSaleInstallment> installmentList) {
        this.cashAdvance = cashAdvance;
        this.installmentList = installmentList;
    }

    public CashAdvance getCashAdvance() {
        return cashAdvance;
    }

    public void setCashAdvance(CashAdvance cashAdvance) {
        this.cashAdvance = cashAdvance;
    }

    public List<ProductSaleInstallment> getInstallmentList() {
        return installmentList;
    }

    public void setInstallmentList(List<ProductSaleInstallment> installmentList) {
        this.installmentList = installmentList;
    }
}