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
}
