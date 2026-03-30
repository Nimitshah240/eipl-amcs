package com.eipl.amcs.setting.dto;

import com.eipl.amcs.master.account.model.Ledger;
import com.eipl.amcs.master.account.model.LedgerMappingEvent;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class AccountPostingDto {
    private LedgerMappingEvent ledgerMappingEvent;
    private String narration;  // TODO NIMIT - MIGHT NOT USE CHECK AFTER PROD
    private Ledger ledger;
    private LocalDate date;
    private Short eventType;
    private boolean credit_debit; // true - credit, false - debit
    private BigDecimal creditAmount;
    private BigDecimal debitAmount;

    public AccountPostingDto(LedgerMappingEvent ledgerMappingEvent, LocalDate date, Short eventType,
                             Ledger ledger, String narration,
                             boolean credit_debit, BigDecimal creditAmount, BigDecimal debitAmount) {

        this.ledgerMappingEvent = ledgerMappingEvent;
        this.date = date;
        this.eventType = eventType;
        this.credit_debit = credit_debit;
        this.creditAmount = creditAmount;
        this.debitAmount = debitAmount;
        this.ledger = ledger;
        this.narration = narration;
    }
}