package com.eipl.amcs.report.dto;

import com.eipl.amcs.report.annotation.ReportDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.sql.Timestamp;

@NoArgsConstructor
@Getter
@Setter
@ReportDto
public class SocietyPurchase {
    private Timestamp collection_date;
    private String society_code;
    private Integer milk_type_code;
    private String society_name;
    private String animal_name;
    private String period;
    private BigDecimal mc_qty;
    private BigDecimal mc_fat;
    private BigDecimal mc_snf;
    private BigDecimal mc_amount;
    private BigDecimal ms_qty;
    private BigDecimal ms_amount;

//    public SocietyPurchase() {
//    }
//
//    public Timestamp getCollection_date() {
//        return collection_date;
//    }
//
//    public void setCollection_date(Timestamp collection_date) {
//        this.collection_date = collection_date;
//    }
//
//    public String getSociety_code() {
//        return society_code;
//    }
//
//    public void setSociety_code(String society_code) {
//        this.society_code = society_code;
//    }
//
//    public Integer getMilk_type_code() {
//        return milk_type_code;
//    }
//
//    public void setMilk_type_code(Integer milk_type_code) {
//        this.milk_type_code = milk_type_code;
//    }
//
//    public String getSociety_name() {
//        return society_name;
//    }
//
//    public void setSociety_name(String society_name) {
//        this.society_name = society_name;
//    }
//
//    public String getAnimal_name() {
//        return animal_name;
//    }
//
//    public void setAnimal_name(String animal_name) {
//        this.animal_name = animal_name;
//    }
//
//    public String getPeriod() {
//        return period;
//    }
//
//    public void setPeriod(String period) {
//        this.period = period;
//    }
//
//    public BigDecimal getMc_qty() {
//        return mc_qty;
//    }
//
//    public void setMc_qty(BigDecimal mc_qty) {
//        this.mc_qty = mc_qty;
//    }
//
//    public BigDecimal getMc_fat() {
//        return mc_fat;
//    }
//
//    public void setMc_fat(BigDecimal mc_fat) {
//        this.mc_fat = mc_fat;
//    }
//
//    public BigDecimal getMc_snf() {
//        return mc_snf;
//    }
//
//    public void setMc_snf(BigDecimal mc_snf) {
//        this.mc_snf = mc_snf;
//    }
//
//    public BigDecimal getMc_amount() {
//        return mc_amount;
//    }
//
//    public void setMc_amount(BigDecimal mc_amount) {
//        this.mc_amount = mc_amount;
//    }
//
//    public BigDecimal getMs_qty() {
//        return ms_qty;
//    }
//
//    public void setMs_qty(BigDecimal ms_qty) {
//        this.ms_qty = ms_qty;
//    }
//
//    public BigDecimal getMs_amount() {
//        return ms_amount;
//    }
//
//    public void setMs_amount(BigDecimal ms_amount) {
//        this.ms_amount = ms_amount;
//    }
}
