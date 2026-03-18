package com.eipl.amcs.setting.dto;

import com.eipl.amcs.master.account.model.LedgerMappingEvent;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class MilkCollectionAccountPostingDto {
    private LedgerMappingEvent ledgerMappingEvent;
    private BigDecimal amount;
    private String narration;
    private LocalDate date;
    private boolean credit_debit; // true - credit, false - debit
}