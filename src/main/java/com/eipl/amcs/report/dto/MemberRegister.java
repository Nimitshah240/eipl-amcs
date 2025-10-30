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
}
