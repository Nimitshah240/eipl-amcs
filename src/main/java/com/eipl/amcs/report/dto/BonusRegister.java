package com.eipl.amcs.report.dto;

import com.eipl.amcs.report.annotation.ReportDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

@NoArgsConstructor
@Getter
@Setter
@ReportDto
public class BonusRegister {
    private String society_code;
    private String union_code;
    private String society_name;
    private String union_name;
    private String bonus_criteria;
    private String member_code;
    private String member_name;


    private BigDecimal bonus_criteria_value;
    private BigDecimal bonus_amount;
    private BigDecimal milk_amount;
    private BigDecimal milk_qty;

    private Date from_date;
    private Date to_date;

    public String getSociety_code() {
        return society_code;
    }

    public void setSociety_code(String society_code) {
        this.society_code = society_code;
    }

    public String getUnion_code() {
        return union_code;
    }

    public void setUnion_code(String union_code) {
        this.union_code = union_code;
    }

    public String getSociety_name() {
        return society_name;
    }

    public void setSociety_name(String society_name) {
        this.society_name = society_name;
    }

    public String getUnion_name() {
        return union_name;
    }

    public void setUnion_name(String union_name) {
        this.union_name = union_name;
    }

    public String getBonus_criteria() {
        return bonus_criteria;
    }

    public void setBonus_criteria(String bonus_criteria) {
        this.bonus_criteria = bonus_criteria;
    }

    public String getMember_code() {
        return member_code;
    }

    public void setMember_code(String member_code) {
        this.member_code = member_code;
    }

    public String getMember_name() {
        return member_name;
    }

    public void setMember_name(String member_name) {
        this.member_name = member_name;
    }

    public BigDecimal getBonus_criteria_value() {
        return bonus_criteria_value;
    }

    public void setBonus_criteria_value(BigDecimal bonus_criteria_value) {
        this.bonus_criteria_value = bonus_criteria_value;
    }

    public BigDecimal getBonus_amount() {
        return bonus_amount;
    }

    public void setBonus_amount(BigDecimal bonus_amount) {
        this.bonus_amount = bonus_amount;
    }

    public BigDecimal getMilk_amount() {
        return milk_amount;
    }

    public void setMilk_amount(BigDecimal milk_amount) {
        this.milk_amount = milk_amount;
    }

    public BigDecimal getMilk_qty() {
        return milk_qty;
    }

    public void setMilk_qty(BigDecimal milk_qty) {
        this.milk_qty = milk_qty;
    }

    public Date getFrom_date() {
        return from_date;
    }

    public void setFrom_date(Date from_date) {
        this.from_date = from_date;
    }

    public Date getTo_date() {
        return to_date;
    }

    public void setTo_date(Date to_date) {
        this.to_date = to_date;
    }
}