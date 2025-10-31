package com.eipl.amcs.report.dto;

import com.eipl.amcs.report.annotation.ReportDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Date;

@NoArgsConstructor
@Getter
@Setter
@ReportDto
public class ShiftReportCode {

    private BigInteger sr;
    private String society_code;
    private String society_name;
    private String member_code;
    private Date collection_date;
    private String shift_name;
    private String milk_type_name;
    private BigInteger sample_no;
    private BigDecimal qty;
    private BigDecimal fat;
    private BigDecimal snf;
    private BigDecimal amount;
    private BigInteger cow_member_count;
    private BigInteger buffalo_member_count;
    private BigInteger mix_member_count;
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
}
