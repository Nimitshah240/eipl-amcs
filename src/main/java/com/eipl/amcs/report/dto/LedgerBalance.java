package com.eipl.amcs.report.dto;

public class LedgerBalance {
    private String ledgerCode;
    private String ledgerName;
    private double debit;
    private double credit;
    private double balance;
    private int incomeExpense;
    @Override
    public String toString() {
        return this.getLedgerCode()+ "-" + this.getLedgerName();
    }
    public LedgerBalance() {
    }

    public int getIncomeExpense() {
        return incomeExpense;
    }

    public void setIncomeExpense(int incomeExpense) {
        this.incomeExpense = incomeExpense;
    }

    public LedgerBalance(String ledgerCode, String ledgerName, double debit, double credit, double balance, int incomeExpense) {
        this.ledgerCode = ledgerCode;
        this.ledgerName = ledgerName;
        this.debit = debit;
        this.credit = credit;
        this.balance = balance;
        this.incomeExpense = incomeExpense;
    }

    public String getLedgerCode() {
        return ledgerCode;
    }

    public void setLedgerCode(String ledgerCode) {
        this.ledgerCode = ledgerCode;
    }

    public String getLedgerName() {
        return ledgerName;
    }

    public void setLedgerName(String ledgerName) {
        this.ledgerName = ledgerName;
    }

    public double getDebit() {
        return debit;
    }

    public void setDebit(double debit) {
        this.debit = debit;
    }

    public double getCredit() {
        return credit;
    }

    public void setCredit(double credit) {
        this.credit = credit;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }
}
