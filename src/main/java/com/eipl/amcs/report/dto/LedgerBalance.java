package com.eipl.amcs.report.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LedgerBalance {
    private String ledgerCode;
    private String ledgerName;
    private double debit;
    private double credit;
    private double balance;
    private int incomeExpense;

    public LedgerBalance(String ledgerCode, String ledgerName, double debit, double credit, double balance) {
        this.ledgerCode = ledgerCode;
        this.ledgerName = ledgerName;
        this.debit = debit;
        this.credit = credit;
        this.balance = balance;
    }

    public LedgerBalance(String ledgerCode, String ledgerName, double debit, double credit, double balance, int incomeExpense) {
        this.ledgerCode = ledgerCode;
        this.ledgerName = ledgerName;
        this.debit = debit;
        this.credit = credit;
        this.balance = balance;
        this.incomeExpense = incomeExpense;
    }

    @Override
    public String toString() {
        return this.getLedgerCode() + "-" + this.getLedgerName();
    }
}
