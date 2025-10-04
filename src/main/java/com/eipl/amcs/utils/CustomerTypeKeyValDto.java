package com.eipl.amcs.utils;

public class CustomerTypeKeyValDto {
    private short key;
    private String value;
    private boolean localSale;
    private boolean productSale;
    private boolean customerTypeCreate;

    public CustomerTypeKeyValDto() {
    }

    public CustomerTypeKeyValDto(short key, String value, boolean localSale, boolean productSale, boolean customerTypeCreate) {
        this.key = key;
        this.value = value;
        this.localSale = localSale;
        this.productSale = productSale;
        this.customerTypeCreate = customerTypeCreate;
    }

    public boolean isLocalSale() {
        return localSale;
    }

    public void setLocalSale(boolean localSale) {
        this.localSale = localSale;
    }

    public boolean isProductSale() {
        return productSale;
    }

    public void setProductSale(boolean productSale) {
        this.productSale = productSale;
    }

    public boolean isCustomerTypeCreate() {
        return customerTypeCreate;
    }

    public void setCustomerTypeCreate(boolean customerTypeCreate) {
        this.customerTypeCreate = customerTypeCreate;
    }

    public short getKey() {
        return key;
    }

    public void setKey(short key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
