package com.eipl.amcs.report.bootdto;

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

    public String getTotalProductCode() {
        return totalProductCode;
    }

    public void setTotalProductCode(String totalProductCode) {
        this.totalProductCode = totalProductCode;
    }

    public String getTotalProductName() {
        return totalProductName;
    }

    public void setTotalProductName(String totalProductName) {
        this.totalProductName = totalProductName;
    }

    public double getTotalStock() {
        return totalStock;
    }

    public void setTotalStock(double totalStock) {
        this.totalStock = totalStock;
    }

    public double getTotalValuation() {
        return totalValuation;
    }

    public void setTotalValuation(double totalValuation) {
        this.totalValuation = totalValuation;
    }

    public String getTotalUnit() {
        return totalUnit;
    }

    public void setTotalUnit(String totalUnit) {
        this.totalUnit = totalUnit;
    }

    public String getClosingProductCode() {
        return closingProductCode;
    }

    public void setClosingProductCode(String closingProductCode) {
        this.closingProductCode = closingProductCode;
    }

    public String getClosingProductName() {
        return closingProductName;
    }

    public void setClosingProductName(String closingProductName) {
        this.closingProductName = closingProductName;
    }

    public double getClosingStock() {
        return closingStock;
    }

    public void setClosingStock(double closingStock) {
        this.closingStock = closingStock;
    }

    public double getClosingValuation() {
        return closingValuation;
    }

    public void setClosingValuation(double closingValuation) {
        this.closingValuation = closingValuation;
    }

    public String getClosingUnit() {
        return closingUnit;
    }

    public void setClosingUnit(String closingUnit) {
        this.closingUnit = closingUnit;
    }
}