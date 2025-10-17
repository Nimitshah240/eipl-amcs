package com.eipl.amcs.operation.inventory.dto;

import com.eipl.amcs.base.model.BaseModel;
import com.eipl.amcs.master.account.model.TaxDetail;
import com.eipl.amcs.operation.inventory.model.ProductSale;
import com.eipl.amcs.operation.inventory.model.ProductSaleTransaction;

import java.math.BigDecimal;

public class ProductSaleTax extends BaseModel {

    private String code;
    private BigDecimal value;
    private String unionCode;
    private String societyCode;
    private ProductSaleTransaction productSaleToMemberTransaction;
    private ProductSale productSaleToMember;
    private TaxDetail taxDetail;

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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

    public ProductSaleTransaction getProductSaleToMemberTransaction() {
        return productSaleToMemberTransaction;
    }

    public void setProductSaleToMemberTransaction(ProductSaleTransaction productSaleToMemberTransaction) {
        this.productSaleToMemberTransaction = productSaleToMemberTransaction;
    }

    public ProductSale getProductSaleToMember() {
        return productSaleToMember;
    }

    public void setProductSaleToMember(ProductSale productSaleToMember) {
        this.productSaleToMember = productSaleToMember;
    }

    public TaxDetail getTaxDetail() {
        return taxDetail;
    }

    public void setTaxDetail(TaxDetail taxDetail) {
        this.taxDetail = taxDetail;
    }
}
