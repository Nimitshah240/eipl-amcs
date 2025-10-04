package com.eipl.amcs.report.dto;

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

    public ProductStockValuationWithSaleAndPurchase() {
    }

    public ProductStockValuationWithSaleAndPurchase(String productCode, String productName, double stock, double valuation, String unit, String purchaseProductCode, String purchaseProductName, double purchaseStock, double purchaseValuation, String purchaseUnit, String saleProductCode, String saleProductName, double saleStock, double saleValuation, String saleUnit) {
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
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public double getStock() {
        return stock;
    }

    public void setStock(double stock) {
        this.stock = stock;
    }

    public double getValuation() {
        return valuation;
    }

    public void setValuation(double valuation) {
        this.valuation = valuation;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getPurchaseProductCode() {
        return purchaseProductCode;
    }

    public void setPurchaseProductCode(String purchaseProductCode) {
        this.purchaseProductCode = purchaseProductCode;
    }

    public String getPurchaseProductName() {
        return purchaseProductName;
    }

    public void setPurchaseProductName(String purchaseProductName) {
        this.purchaseProductName = purchaseProductName;
    }

    public double getPurchaseStock() {
        return purchaseStock;
    }

    public void setPurchaseStock(double purchaseStock) {
        this.purchaseStock = purchaseStock;
    }

    public double getPurchaseValuation() {
        return purchaseValuation;
    }

    public void setPurchaseValuation(double purchaseValuation) {
        this.purchaseValuation = purchaseValuation;
    }

    public String getPurchaseUnit() {
        return purchaseUnit;
    }

    public void setPurchaseUnit(String purchaseUnit) {
        this.purchaseUnit = purchaseUnit;
    }

    public String getSaleProductCode() {
        return saleProductCode;
    }

    public void setSaleProductCode(String saleProductCode) {
        this.saleProductCode = saleProductCode;
    }

    public String getSaleProductName() {
        return saleProductName;
    }

    public void setSaleProductName(String saleProductName) {
        this.saleProductName = saleProductName;
    }

    public double getSaleStock() {
        return saleStock;
    }

    public void setSaleStock(double saleStock) {
        this.saleStock = saleStock;
    }

    public double getSaleValuation() {
        return saleValuation;
    }

    public void setSaleValuation(double saleValuation) {
        this.saleValuation = saleValuation;
    }

    public String getSaleUnit() {
        return saleUnit;
    }

    public void setSaleUnit(String saleUnit) {
        this.saleUnit = saleUnit;
    }
}
