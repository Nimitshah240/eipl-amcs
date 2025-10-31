package com.eipl.amcs.report.dto;

import com.eipl.amcs.report.annotation.ReportDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.math.BigInteger;

@NoArgsConstructor
@Getter
@Setter
@ReportDto
public class PaymentForBank {

    public BigInteger sr_no;
    public String soc_code;
    public String soc_name;
    public String payment_period;
    public String member_Code;
    public String member_name;
    public String bank_acno;
    public String ifsc;
    public String bank_name;
    public String branch_name;
    public BigDecimal net_amount;
    public BigDecimal milk_amount;
    public BigDecimal other_ded_amount;

    public String getSoc_name() {
        return soc_name;
    }

    public String getSoc_code() {
        return soc_code;
    }


    public String getPayment_period() {
        return payment_period;
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

    public String getIfsc() {
        return ifsc;
    }

    public void setIfsc(String ifsc) {
        this.ifsc = ifsc;
    }

    public BigInteger getSr_no() {
        return sr_no;
    }


    public void setSr_no(BigInteger sr_no) {
        this.sr_no = sr_no;
    }


    public String getMember_Code() {
        return member_Code;
    }

    public void setMember_Code(String member_Code) {
        this.member_Code = member_Code;
    }

    public String getMember_name() {
        return member_name;
    }

    public void setMember_name(String member_name) {
        this.member_name = member_name;
    }

    public BigDecimal getNet_amount() {
        return net_amount;
    }

    public void setNet_amount(BigDecimal net_amount) {
        this.net_amount = net_amount;
    }

    public String getBank_acno() {
        return bank_acno;
    }

    public void setBank_acno(String bank_acno) {
        this.bank_acno = bank_acno;
    }

    public BigDecimal getOther_ded_amount() {
        return other_ded_amount;
    }

    public void setOther_ded_amount(BigDecimal other_ded_amount) {
        this.other_ded_amount = other_ded_amount;
    }

    public BigDecimal getMilk_amount() {
        return milk_amount;
    }

    public void setMilk_amount(BigDecimal milk_amount) {
        this.milk_amount = milk_amount;
    }
}
