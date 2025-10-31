package com.eipl.amcs.report.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductStockValuation {
    private String productCode;
    private String productName;
    private double stock;
    private double valuation;
    private String unit;

    @Override
    public String toString() {
        return this.getProductCode() + "-" + this.getProductName();
    }
}
