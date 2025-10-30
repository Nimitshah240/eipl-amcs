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
}
