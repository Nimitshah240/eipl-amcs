package com.eipl.amcs.master.inventory.dto;

import com.eipl.amcs.base.model.BaseModel;
import com.eipl.amcs.master.account.model.Tax;
import com.eipl.amcs.master.global.model.Unit;
import com.eipl.amcs.master.org.model.Society;
import com.eipl.amcs.master.org.model.Union;
import com.eipl.amcs.utils.CommonUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.math.BigDecimal;

public class Product extends BaseModel {


    private String code;
    private String name;
    private String nameLocal;
    private String referenceCode;
    private BigDecimal baseConversionFactor;
    private BigDecimal conversionFactor;
    private Short entryType;
    private boolean indent;
    private boolean saleable;
    private Unit conversionUnit;
    private Unit primaryUom;
    private ProductGroup productGroup;
    private Tax tax;
    private Product secondaryPackaging;
    private Society society;
    private Union union;

    @JsonIgnore
    private StringProperty amount;

    public Product(String code, String name,String nameLocal) {
        this.code = code;
        this.name = name;
        this.nameLocal = nameLocal;
    }

    public String getAmount() {
        return amount.get();
    }

    public StringProperty amountProperty() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount.set(amount);
    }

    public Product() {
        amount = new SimpleStringProperty("0");
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNameLocal() {
        return nameLocal;
    }

    public void setNameLocal(String nameLocal) {
        this.nameLocal = nameLocal;
    }

    public String getReferenceCode() {
        return referenceCode;
    }

    public void setReferenceCode(String referenceCode) {
        this.referenceCode = referenceCode;
    }

    public BigDecimal getBaseConversionFactor() {
        return baseConversionFactor;
    }

    public void setBaseConversionFactor(BigDecimal baseConversionFactor) {
        this.baseConversionFactor = baseConversionFactor;
    }

    public BigDecimal getConversionFactor() {
        return conversionFactor;
    }

    public void setConversionFactor(BigDecimal conversionFactor) {
        this.conversionFactor = conversionFactor;
    }

    public Short getEntryType() {
        return entryType;
    }

    public void setEntryType(Short entryType) {
        this.entryType = entryType;
    }

    public Unit getConversionUnit() {
        return conversionUnit;
    }

    public void setConversionUnit(Unit conversionUnit) {
        this.conversionUnit = conversionUnit;
    }

    public Unit getPrimaryUom() {
        return primaryUom;
    }

    public void setPrimaryUom(Unit primaryUom) {
        this.primaryUom = primaryUom;
    }

    public ProductGroup getProductGroup() {
        return productGroup;
    }

    public void setProductGroup(ProductGroup productGroup) {
        this.productGroup = productGroup;
    }

    public Tax getTax() {
        return tax;
    }

    public void setTax(Tax tax) {
        this.tax = tax;
    }

    public Product getSecondaryPackaging() {
        return secondaryPackaging;
    }

    public void setSecondaryPackaging(Product secondaryPackaging) {
        this.secondaryPackaging = secondaryPackaging;
    }

    public Society getSociety() {
        return society;
    }

    public void setSociety(Society society) {
        this.society = society;
    }

    public Union getUnion() {
        return union;
    }

    public void setUnion(Union union) {
        this.union = union;
    }

    public boolean isIndent() {
        return indent;
    }

    public void setIndent(boolean indent) {
        this.indent = indent;
    }

    public boolean isSaleable() {
        return saleable;
    }

    public void setSaleable(boolean saleable) {
        this.saleable = saleable;
    }

    @Override
    public String toString() {
        return CommonUtils.getLocalString(this.name, this.nameLocal);
    }
}