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
//
//    public String getProductCode() {
//        return productCode;
//    }
//
//    public void setProductCode(String productCode) {
//        this.productCode = productCode;
//    }
//
//    public String getProductName() {
//        return productName;
//    }
//
//    public void setProductName(String productName) {
//        this.productName = productName;
//    }
//
//    public double getStock() {
//        return stock;
//    }
//
//    public void setStock(double stock) {
//        this.stock = stock;
//    }
//
//    public double getValuation() {
//        return valuation;
//    }
//
//    public void setValuation(double valuation) {
//        this.valuation = valuation;
//    }
//
//    public String getUnit() {
//        return unit;
//    }
//
//    public void setUnit(String unit) {
//        this.unit = unit;
//    }
//
//    public ProductStockValuation() {
//    }
//
//    public ProductStockValuation(String productCode, String productName, double stock, double valuation, String unit) {
//        this.productCode = productCode;
//        this.productName = productName;
//        this.stock = stock;
//        this.valuation = valuation;
//        this.unit = unit;
//    }

    @Override
    public String toString() {
        return this.getProductCode() + "-" + this.getProductName();
    }
}
