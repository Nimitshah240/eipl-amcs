package com.eipl.amcs.operation.inventory.dto;

import com.eipl.amcs.operation.inventory.model.ProductReceipt;
import com.eipl.amcs.base.model.BaseModel;
import com.eipl.amcs.master.account.model.TaxDetail;

import java.math.BigDecimal;
import com.eipl.amcs.operation.inventory.model.ProductReceiptTransaction;

public class ProductReceiptTax extends BaseModel {

    private String code;
    private BigDecimal value;
    private String societyCode;
    private String unionCode;
    private ProductReceipt productReceipt;
    private ProductReceiptTransaction productReceiptTransaction;
    private TaxDetail taxDetail;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public String getSocietyCode() {
        return societyCode;
    }

    public void setSocietyCode(String societyCode) {
        this.societyCode = societyCode;
    }

    public String getUnionCode() {
        return unionCode;
    }

    public void setUnionCode(String unionCode) {
        this.unionCode = unionCode;
    }

    public ProductReceiptTransaction getProductReceiptTransaction() {
        return productReceiptTransaction;
    }

    public void setProductReceiptTransaction(ProductReceiptTransaction productReceiptTransaction) {
        this.productReceiptTransaction = productReceiptTransaction;
    }

    public TaxDetail getTaxDetail() {
        return taxDetail;
    }

    public void setTaxDetail(TaxDetail taxDetail) {
        this.taxDetail = taxDetail;
    }

    public ProductReceipt getProductReceipt() {
        return productReceipt;
    }

    public void setProductReceipt(ProductReceipt productReceipt) {
        this.productReceipt = productReceipt;
    }
}
