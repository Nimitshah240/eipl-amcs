package com.eipl.amcs.report.dto;

import com.eipl.amcs.report.annotation.ReportDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.sql.Date;

@NoArgsConstructor
@Getter
@Setter
@ReportDto
public class MemberRegister {
    private Date p_from_date;
    private BigDecimal total_animal;
    private String member_type;
    private String registration_date;
    private String birth_date;
    private String mobile_no;
    private String gender;
    private String member_code;
    private String society_code;
    private String society_name;
    private String member_name;
    private String union_code;
    private String union_name;
    private String village_code;
    private String village_name;

//    public LocalDate getP_from_date() {
//        return p_from_date;
//    }
//
//    public void setP_from_date(LocalDate p_from_date) {
//        this.p_from_date = p_from_date;
//    }
//
//    public BigDecimal getTotal_animal() {
//        return total_animal;
//    }
//
//    public void setTotal_animal(BigDecimal total_animal) {
//        this.total_animal = total_animal;
//    }
//
//    public String getMember_type() {
//        return member_type;
//    }
//
//    public void setMember_type(String member_type) {
//        this.member_type = member_type;
//    }
//
//    public String getRegistration_date() {
//        return registration_date;
//    }
//
//    public void setRegistration_date(String registration_date) {
//        this.registration_date = registration_date;
//    }
//
//    public String getBirth_date() {
//        return birth_date;
//    }
//
//    public void setBirth_date(String birth_date) {
//        this.birth_date = birth_date;
//    }
//
//    public String getMobile_no() {
//        return mobile_no;
//    }
//
//    public void setMobile_no(String mobile_no) {
//        this.mobile_no = mobile_no;
//    }
//
//    public String getGender() {
//        return gender;
//    }
//
//    public void setGender(String gender) {
//        this.gender = gender;
//    }
//
//    public String getMember_code() {
//        return member_code;
//    }
//
//    public void setMember_code(String member_code) {
//        this.member_code = member_code;
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
//    public String getSociety_name() {
//        return society_name;
//    }
//
//    public void setSociety_name(String society_name) {
//        this.society_name = society_name;
//    }
//
//    public String getMember_name() {
//        return member_name;
//    }
//
//    public void setMember_name(String member_name) {
//        this.member_name = member_name;
//    }
//
//    public String getUnion_code() {
//        return union_code;
//    }
//
//    public void setUnion_code(String union_code) {
//        this.union_code = union_code;
//    }
//
//    public String getUnion_name() {
//        return union_name;
//    }
//
//    public void setUnion_name(String union_name) {
//        this.union_name = union_name;
//    }
//
//    public String getVillage_code() {
//        return village_code;
//    }
//
//    public void setVillage_code(String village_code) {
//        this.village_code = village_code;
//    }
//
//    public String getVillage_name() {
//        return village_name;
//    }
//
//    public void setVillage_name(String village_name) {
//        this.village_name = village_name;
//    }


}
