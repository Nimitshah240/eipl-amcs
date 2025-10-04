package com.eipl.amcs.operation.inventory.dto;

import com.eipl.amcs.master.account.dto.TaxDto;
import com.eipl.amcs.master.global.model.Unit;
import com.eipl.amcs.master.inventory.model.Product;

import java.time.LocalDate;
import java.util.List;

public class SaleTxnDto {
    private LocalDate date;
    private List<Product> productList;
    private List<Unit> unitList;
    private List<TaxDto> taxDtoList;

    public SaleTxnDto() {
    }

    public SaleTxnDto(LocalDate date, List<Product> productList, List<Unit> unitList, List<TaxDto> taxDtoList) {
        this.date = date;
        this.productList = productList;
        this.unitList = unitList;
        this.taxDtoList = taxDtoList;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public List<Product> getProductList() {
        return productList;
    }

    public void setProductList(List<Product> productList) {
        this.productList = productList;
    }

    public List<Unit> getUnitList() {
        return unitList;
    }

    public void setUnitList(List<Unit> unitList) {
        this.unitList = unitList;
    }

    public List<TaxDto> getTaxDtoList() {
        return taxDtoList;
    }

    public void setTaxDtoList(List<TaxDto> taxDtoList) {
        this.taxDtoList = taxDtoList;
    }
}
