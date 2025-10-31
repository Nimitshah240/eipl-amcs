package com.eipl.amcs.report.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LedgerClose {
    private String ledgerCode;
    private String ledgerName;
    private boolean creditDebit;
    private double balance;
}
