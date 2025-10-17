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
public class MemberCollectionSummary {
    private Timestamp from_date;
    private Timestamp to_date;
    private String society_code;
    private String society_name;
    private String union_code;
    private String union_name;
    private String village_code;
    private String village_name;
    private Timestamp collection_date;
    private String qty_mode;
    private String member_code;
    private String member_name;
    private String milk_type;
    private BigDecimal installment_amount;
    private BigDecimal local_sale_credit;
    private BigDecimal netpayble;
    private BigDecimal deduction;
    private BigDecimal avg_rate;
    private BigDecimal amount;
    private BigDecimal qty;
    private BigDecimal avg_snf;
    private BigDecimal kg_snf;
    private BigDecimal avg_fat;
    private BigDecimal kg_fat;
    private BigDecimal avg_clr;
    private BigDecimal kg_clr;

}
