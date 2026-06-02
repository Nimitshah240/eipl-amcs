package com.eipl.amcs.report.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class RojmedDto {
    private String creditLedger;
    private Double creditSubAmount;
    private Double creditAmount;
    private String debitLedger;
    private Double debitSubAmount;
    private Double debitAmount;
    private Date date;
    private String rojmedDate;
}