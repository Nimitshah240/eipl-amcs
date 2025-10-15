package com.eipl.amcs.report.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductStockValuationWithSaleAndPurchase {
    private String productCode;
    private String productName;
    private double stock;
    private double valuation;
    private String unit;

    private String purchaseProductCode;
    private String purchaseProductName;
    private double purchaseStock;
    private double purchaseValuation;
    private String purchaseUnit;

    private String saleProductCode;
    private String saleProductName;
    private double saleStock;
    private double saleValuation;
    private String saleUnit;
    private String totalProductCode;
    private String totalProductName;
    private double totalStock;
    private double totalValuation;
    private String totalUnit;

    private String closingProductCode;
    private String closingProductName;
    private double closingStock;
    private double closingValuation;
    private String closingUnit;

    public ProductStockValuationWithSaleAndPurchase() {
    }

    public ProductStockValuationWithSaleAndPurchase(String productCode, String productName, double stock, double valuation, String unit, String purchaseProductCode, String purchaseProductName, double purchaseStock, double purchaseValuation, String purchaseUnit, String saleProductCode, String saleProductName, double saleStock, double saleValuation, String saleUnit, String totalProductCode, String totalProductName, double totalStock, double totalValuation, String totalUnit, String closingProductCode, String closingProductName, double closingStock, double closingValuation, String closingUnit) {
        this.productCode = productCode;
        this.productName = productName;
        this.stock = stock;
        this.valuation = valuation;
        this.unit = unit;
        this.purchaseProductCode = purchaseProductCode;
        this.purchaseProductName = purchaseProductName;
        this.purchaseStock = purchaseStock;
        this.purchaseValuation = purchaseValuation;
        this.purchaseUnit = purchaseUnit;
        this.saleProductCode = saleProductCode;
        this.saleProductName = saleProductName;
        this.saleStock = saleStock;
        this.saleValuation = saleValuation;
        this.saleUnit = saleUnit;
        this.totalProductCode = totalProductCode;
        this.totalProductName = totalProductName;
        this.totalStock = totalStock;
        this.totalValuation = totalValuation;
        this.totalUnit = totalUnit;
        this.closingProductCode = closingProductCode;
        this.closingProductName = closingProductName;
        this.closingStock = closingStock;
        this.closingValuation = closingValuation;
        this.closingUnit = closingUnit;
    }
}
