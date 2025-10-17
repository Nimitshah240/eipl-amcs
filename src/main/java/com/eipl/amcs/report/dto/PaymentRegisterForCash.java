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
public class PaymentRegisterForCash {
    private String society_code;
    private String union_code;
    private String society_name;
    private String union_name;
    private String member_name;
    private String member_code;
    private String bank_name;
    private String branch_name;
    private String bank_acno;
    private String ifsc;
    private String society_payment_cycle_code;

    private Short payment_mode;

    private BigDecimal other_add_amount;
    private BigDecimal other_ded_amount;
    private BigDecimal net_amount;
    private BigDecimal milk_amount;
    private BigDecimal milk_qty;
    private BigDecimal cow_amount;
    private BigDecimal buffalo_amount;
    private BigDecimal mix_amount;
    private BigDecimal cow_qty;
    private BigDecimal buffalo_qty;
    private BigDecimal mix_qty;

    private Timestamp from_date;
    private Timestamp to_date;

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

    public BigDecimal getOther_add_amount() {
        return other_add_amount;
    }

    public void setOther_add_amount(BigDecimal other_add_amount) {
        this.other_add_amount = other_add_amount;
    }

    public BigDecimal getOther_ded_amount() {
        return other_ded_amount;
    }

    public void setOther_ded_amount(BigDecimal other_ded_amount) {
        this.other_ded_amount = other_ded_amount;
    }

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

    public String getMember_name() {
        return member_name;
    }

    public void setMember_name(String member_name) {
        this.member_name = member_name;
    }

    public String getMember_code() {
        return member_code;
    }

    public void setMember_code(String member_code) {
        this.member_code = member_code;
    }

    public String getBank_name() {
        return bank_name;
    }

    public void setBank_name(String bank_name) {
        this.bank_name = bank_name;
    }

    public String getBranch_name() {
        return branch_name;
    }

    public void setBranch_name(String branch_name) {
        this.branch_name = branch_name;
    }

    public String getBank_acno() {
        return bank_acno;
    }

    public void setBank_acno(String bank_acno) {
        this.bank_acno = bank_acno;
    }

    public String getIfsc() {
        return ifsc;
    }

    public void setIfsc(String ifsc) {
        this.ifsc = ifsc;
    }

    public String getSociety_payment_cycle_code() {
        return society_payment_cycle_code;
    }

    public void setSociety_payment_cycle_code(String society_payment_cycle_code) {
        this.society_payment_cycle_code = society_payment_cycle_code;
    }

    public Short getPayment_mode() {
        return payment_mode;
    }

    public void setPayment_mode(Short payment_mode) {
        this.payment_mode = payment_mode;
    }

    public BigDecimal getNet_amount() {
        return net_amount;
    }

    public void setNet_amount(BigDecimal net_amount) {
        this.net_amount = net_amount;
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

    public Timestamp getFrom_date() {
        return from_date;
    }

    public void setFrom_date(Timestamp from_date) {
        this.from_date = from_date;
    }

    public Timestamp getTo_date() {
        return to_date;
    }

    public void setTo_date(Timestamp to_date) {
        this.to_date = to_date;
    }
}