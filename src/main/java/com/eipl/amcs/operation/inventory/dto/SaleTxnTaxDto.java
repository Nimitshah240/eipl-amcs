package com.eipl.amcs.operation.inventory.dto;

import com.eipl.amcs.operation.inventory.model.ProductSaleTax;
import com.eipl.amcs.operation.inventory.model.ProductSaleTransaction;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class SaleTxnTaxDto {
    private ProductSaleTransaction transaction;
    private List<ProductSaleTax> saleTaxList;
}
