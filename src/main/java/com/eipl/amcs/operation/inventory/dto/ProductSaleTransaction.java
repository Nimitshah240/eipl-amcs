package com.eipl.amcs.operation.inventory.dto;

import com.eipl.amcs.base.model.BaseModel;
import com.eipl.amcs.base.model.BaseModelTxn;
import com.eipl.amcs.master.inventory.model.Product;

import java.math.BigDecimal;
import com.eipl.amcs.operation.inventory.model.ProductSale;

public class ProductSaleTransaction extends BaseModelTxn {

    private String invoiceTxnNo;
    private BigDecimal amount;
    private BigDecimal discount;
    private Integer quantity;
    private BigDecimal rate;

    public BigDecimal getNetAmount() {
        return netAmount;
    }

    public void setNetAmount(BigDecimal netAmount) {
        this.netAmount = netAmount;
    }

    private BigDecimal taxAmount;
    private BigDecimal netAmount;
    private Boolean isLooseSale;
    private String taxCode;
    private String unionCode;
    private String societyCode;
    private Integer unitCode;
    private ProductSale productSaleToMember;
    private Product product;


    public String getInvoiceTxnNo() {
        return invoiceTxnNo;
    }

    public void setInvoiceTxnNo(String invoiceTxnNo) {
        this.invoiceTxnNo = invoiceTxnNo;
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

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
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

    public Boolean getLooseSale() {
        return isLooseSale;
    }

    public void setLooseSale(Boolean looseSale) {
        isLooseSale = looseSale;
    }

    public String getTaxCode() {
        return taxCode;
    }

    public void setTaxCode(String taxCode) {
        this.taxCode = taxCode;
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

    public Integer getUnitCode() {
        return unitCode;
    }

    public void setUnitCode(Integer unitCode) {
        this.unitCode = unitCode;
    }

    public ProductSale getProductSaleToMember() {
        return productSaleToMember;
    }

    public void setProductSaleToMember(ProductSale productSaleToMember) {
        this.productSaleToMember = productSaleToMember;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
