package com.eipl.amcs.master.account.model;

import com.eipl.amcs.base.model.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tbl_product_stock_valuation")
public class ProductStockValuation extends BaseModel {
    @Id
    private String productStockValuationCode;
    private String productCode;
    private String productName;
    private double stock;
    private double valuation;
    private String unit;
    private LocalDate generatedAt;

    public ProductStockValuation(String productCode, String productName, double stock, double valuation, String unit,
                                 LocalDate generatedAt, String productStockValuationCode) {
        this.productCode = productCode;
        this.productName = productName;
        this.stock = stock;
        this.valuation = valuation;
        this.unit = unit;
        this.generatedAt = generatedAt;
        this.productStockValuationCode = productStockValuationCode;
    }

    @Override
    public String toString() {
        return this.getProductCode() + "-" + this.getProductName();
    }
}
