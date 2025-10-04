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
public class DairySaleRegister {
    private Timestamp from_date;
    private Timestamp to_date;
    private String society_code;
    private Integer milk_type_code;
    private String society_name;
    private String animal_name;
    private String period;
    private BigDecimal mc_qty;
    private BigDecimal mc_fat;
    private BigDecimal mc_snf;
    private BigDecimal mc_amount;
    private BigDecimal md_qty;
    private BigDecimal md_fat;
    private BigDecimal md_snf;
    private BigDecimal md_amount;
    private BigDecimal difference;

//    public Timestamp getFrom_date() {
//        return from_date;
//    }
//
//    public void setFrom_date(Timestamp from_date) {
//        this.from_date = from_date;
//    }
//
//    public Timestamp getTo_date() {
//        return to_date;
//    }
//
//    public void setTo_date(Timestamp to_date) {
//        this.to_date = to_date;
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
//    public BigDecimal getMd_qty() {
//        return md_qty;
//    }
//
//    public void setMd_qty(BigDecimal md_qty) {
//        this.md_qty = md_qty;
//    }
//
//    public BigDecimal getMd_fat() {
//        return md_fat;
//    }
//
//    public void setMd_fat(BigDecimal md_fat) {
//        this.md_fat = md_fat;
//    }
//
//    public BigDecimal getMd_snf() {
//        return md_snf;
//    }
//
//    public void setMd_snf(BigDecimal md_snf) {
//        this.md_snf = md_snf;
//    }
//
//    public BigDecimal getMd_amount() {
//        return md_amount;
//    }
//
//    public void setMd_amount(BigDecimal md_amount) {
//        this.md_amount = md_amount;
//    }
//
//    public BigDecimal getDifference() {
//        return difference;
//    }
//
//    public void setDifference(BigDecimal difference) {
//        this.difference = difference;
//    }


}
