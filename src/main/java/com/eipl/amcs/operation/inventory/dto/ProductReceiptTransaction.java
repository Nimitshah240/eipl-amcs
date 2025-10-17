package com.eipl.amcs.operation.inventory.dto;

import com.eipl.amcs.base.model.BaseModel;
import com.eipl.amcs.master.account.model.Tax;
import com.eipl.amcs.master.global.model.Unit;
import com.eipl.amcs.master.inventory.model.Product;
import com.eipl.amcs.operation.inventory.model.ProductReceipt;

import java.math.BigDecimal;

public class ProductReceiptTransaction extends BaseModel {

    private String grnTxnNo;
    private BigDecimal amount;
    private BigDecimal discount;
    private BigDecimal quantity;
    private BigDecimal rate;
    private BigDecimal taxAmount;
    private BigDecimal netAmount;
    private String remark;
    private String unionCode;
    private String societyCode;
    private ProductReceipt productReceipt;
    private Product product;
    private Tax tax;
    private Unit unit;

    public Unit getUnit() {
        return unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    public String getGrnTxnNo() {
        return grnTxnNo;
    }

    public void setGrnTxnNo(String grnTxnNo) {
        this.grnTxnNo = grnTxnNo;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getUnionCode() {
        return unionCode;
    }

    public void setUnionCode(String unionCode) {
        this.unionCode = unionCode;
    }

    public String getSocietyCode() {
        return societyCode;
    }

    public void setSocietyCode(String societyCode) {
        this.societyCode = societyCode;
    }

    public ProductReceipt getProductReceipt() {
        return productReceipt;
    }

    public void setProductReceipt(ProductReceipt productReceipt) {
        this.productReceipt = productReceipt;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Tax getTax() {
        return tax;
    }

    public void setTax(Tax tax) {
        this.tax = tax;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public BigDecimal getTaxAmount() {
        return taxAmount;
    }

    public void setTaxAmount(BigDecimal taxAmount) {
        this.taxAmount = taxAmount;
    }

    public BigDecimal getNetAmount() {
        return netAmount;
    }

    public void setNetAmount(BigDecimal netAmount) {
        this.netAmount = netAmount;
    }
}
