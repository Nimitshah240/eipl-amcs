package com.eipl.amcs.master.account.dto;

import com.eipl.amcs.base.model.BaseModel;
import com.eipl.amcs.master.account.model.CashAdvance;
import com.eipl.amcs.operation.inventory.model.ProductSaleInstallment;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CashAdvanceDto extends BaseModel {
    private CashAdvance cashAdvance;
    private List<ProductSaleInstallment> installmentList;
}