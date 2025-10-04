package com.eipl.amcs.report.dto;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Date;

public class ShiftReportCode {

    private Long sr;
    private String society_code;
    private String society_name;
    private String member_code;
    private Date collection_date;
    private String shift_name;
    private String milk_type_name;
    private Long sample_no;
    private BigDecimal qty;
    private BigDecimal fat;
    private BigDecimal snf;
    private BigDecimal amount;
    private BigDecimal cow_member_count;
    private BigDecimal buffalo_member_count;
    private BigDecimal mix_member_count;
    private BigDecimal cow_qty;
    private BigDecimal buffalo_qty;
    private BigDecimal mix_qty;
    private BigDecimal cow_amount;
    private BigDecimal buffalo_amount;
    private BigDecimal mix_amount;

    private BigDecimal local_cow_qty;
    private BigDecimal local_buffalo_qty;
    private BigDecimal local_mix_qty;
    private BigDecimal local_cow_amount;
    private BigDecimal local_buffalo_amount;
    private BigDecimal local_mix_amount;

    public ShiftReportCode() {

    }

    public Long getSr() {
        return sr;
    }

    public void setSr(Long sr) {
        this.sr = sr;
    }

    public String getSociety_code() {
        return society_code;
    }

    public void setSociety_code(String society_code) {
        this.society_code = society_code;
    }

    public String getSociety_name() {
        return society_name;
    }

    public void setSociety_name(String society_name) {
        this.society_name = society_name;
    }

    public String getMember_code() {
        return member_code;
    }

    public void setMember_code(String member_code) {
        this.member_code = member_code;
    }

    public Date getCollection_date() {
        return collection_date;
    }

    public void setCollection_date(Date collection_date) {
        this.collection_date = collection_date;
    }

    public String getShift_name() {
        return shift_name;
    }

    public void setShift_name(String shift_name) {
        this.shift_name = shift_name;
    }

    public String getMilk_type_name() {
        return milk_type_name;
    }

    public void setMilk_type_name(String milk_type_name) {
        this.milk_type_name = milk_type_name;
    }

    public Long getSample_no() {
        return sample_no;
    }

    public void setSample_no(Long sample_no) {
        this.sample_no = sample_no;
    }

    public BigDecimal getQty() {
        return qty;
    }

    public void setQty(BigDecimal qty) {
        this.qty = qty;
    }

    public BigDecimal getFat() {
        return fat;
    }

    public void setFat(BigDecimal fat) {
        this.fat = fat;
    }

    public BigDecimal getSnf() {
        return snf;
    }

    public void setSnf(BigDecimal snf) {
        this.snf = snf;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getCow_member_count() {
        return cow_member_count;
    }

    public void setCow_member_count(BigDecimal cow_member_count) {
        this.cow_member_count = cow_member_count;
    }

    public BigDecimal getBuffalo_member_count() {
        return buffalo_member_count;
    }

    public void setBuffalo_member_count(BigDecimal buffalo_member_count) {
        this.buffalo_member_count = buffalo_member_count;
    }

    public BigDecimal getMix_member_count() {
        return mix_member_count;
    }

    public void setMix_member_count(BigDecimal mix_member_count) {
        this.mix_member_count = mix_member_count;
    }

    public BigDecimal getCow_qty() {
        return cow_qty;
    }

    public void setCow_qty(BigDecimal cow_qty) {
        this.cow_qty = cow_qty;
    }

    public BigDecimal getBuffalo_qty() {
        return buffalo_qty;
    }

    public void setBuffalo_qty(BigDecimal buffalo_qty) {
        this.buffalo_qty = buffalo_qty;
    }

    public BigDecimal getMix_qty() {
        return mix_qty;
    }

    public void setMix_qty(BigDecimal mix_qty) {
        this.mix_qty = mix_qty;
    }

    public BigDecimal getCow_amount() {
        return cow_amount;
    }

    public void setCow_amount(BigDecimal cow_amount) {
        this.cow_amount = cow_amount;
    }

    public BigDecimal getBuffalo_amount() {
        return buffalo_amount;
    }

    public void setBuffalo_amount(BigDecimal buffalo_amount) {
        this.buffalo_amount = buffalo_amount;
    }

    public BigDecimal getMix_amount() {
        return mix_amount;
    }

    public void setMix_amount(BigDecimal mix_amount) {
        this.mix_amount = mix_amount;
    }

    public BigDecimal getLocal_cow_qty() {
        return local_cow_qty;
    }

    public void setLocal_cow_qty(BigDecimal local_cow_qty) {
        this.local_cow_qty = local_cow_qty;
    }

    public BigDecimal getLocal_buffalo_qty() {
        return local_buffalo_qty;
    }

    public void setLocal_buffalo_qty(BigDecimal local_buffalo_qty) {
        this.local_buffalo_qty = local_buffalo_qty;
    }

    public BigDecimal getLocal_mix_qty() {
        return local_mix_qty;
    }

    public void setLocal_mix_qty(BigDecimal local_mix_qty) {
        this.local_mix_qty = local_mix_qty;
    }

    public BigDecimal getLocal_cow_amount() {
        return local_cow_amount;
    }

    public void setLocal_cow_amount(BigDecimal local_cow_amount) {
        this.local_cow_amount = local_cow_amount;
    }

    public BigDecimal getLocal_buffalo_amount() {
        return local_buffalo_amount;
    }

    public void setLocal_buffalo_amount(BigDecimal local_buffalo_amount) {
        this.local_buffalo_amount = local_buffalo_amount;
    }

    public BigDecimal getLocal_mix_amount() {
        return local_mix_amount;
    }

    public void setLocal_mix_amount(BigDecimal local_mix_amount) {
        this.local_mix_amount = local_mix_amount;
    }
}
